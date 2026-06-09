package com.forage.service;

import com.forage.dto.AlertDto;
import com.forage.dto.AlertResponse;
import com.forage.model.*;
import com.forage.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlertService {

    // Sigle de l'étape finale : "Devis Forage terminé"
    private static final String SIGLE_ETAPE_FINALE = "DFT";

    private final StatutDemandeRepository statutDemandeRepository;
    private final DemandeRepository       demandeRepository;
    private final ParametreRepository     parametreRepository;
    private final StatutRepository        statutRepository;

    @Autowired
    public AlertService(StatutDemandeRepository statutDemandeRepository,
                        DemandeRepository       demandeRepository,
                        ParametreRepository     parametreRepository,
                        StatutRepository        statutRepository) {
        this.statutDemandeRepository = statutDemandeRepository;
        this.demandeRepository       = demandeRepository;
        this.parametreRepository     = parametreRepository;
        this.statutRepository        = statutRepository;
    }

    public List<AlertResponse> getAlerts() {
        List<Demande>   demandes   = demandeRepository.findAll();
        List<Parametre> parametres = parametreRepository.findAll();

        // ID du statut "Devis Forage terminé" (on le résout une fois)
        final int idEtapeFinale = statutRepository.findBySigle(SIGLE_ETAPE_FINALE)
                .map(Statut::getId)
                .orElse(-1);

        List<AlertResponse> result = new ArrayList<>();

        for (Demande demande : demandes) {
            List<StatutDemande> historique =
                    statutDemandeRepository.findByDemandeId(demande.getId());

            // ── 1. Alertes par intervalle ────────────────────────────
            // Clé : "idS1->idS2" — on ne garde qu'une alerte par intervalle
            Map<String, AlertDto> mapAlertes = new HashMap<>();

            for (Parametre p : parametres) {
                double dureeTravaille = 0;
                boolean trouve = false;

                for (int i = 0; i < historique.size(); i++) {
                    if (p.getStatut1().getId() == historique.get(i).getStatut().getId()) {
                        for (int j = i + 1; j < historique.size(); j++) {
                            if (p.getStatut2().getId() == historique.get(j).getStatut().getId()) {
                                dureeTravaille = historique.get(j).getDureeTravaille();
                                trouve = true;
                                break;
                            }
                        }
                    }
                    if (trouve) break;
                }

                if (!trouve) continue;

                // Logique d'intervalle : dureeMin <= dureeTravaille < dureeMax
                if (dureeTravaille >= p.getDureeMin() && dureeTravaille < p.getDureeMax()) {
                    String cle = p.getStatut1().getId() + "->" + p.getStatut2().getId();
                    AlertDto nouvelle = new AlertDto(p, dureeTravaille);

                    // Si plusieurs paramètres couvrent le même intervalle, on garde
                    // celui dont dureeMin est le plus élevé (le plus précis / grave)
                    mapAlertes.merge(cle, nouvelle,
                            (existant, nv) -> nv.getDureeMin() > existant.getDureeMin() ? nv : existant);
                }
            }

            List<AlertDto> alertDtos = new ArrayList<>(mapAlertes.values());

            // ── 2. Durée totale DC → DFT ─────────────────────────────
            Double totalDuree = null;

            if (idEtapeFinale != -1) {
                // Cherche si "Devis Forage terminé" est dans l'historique
                boolean aAtteintFinale = historique.stream()
                        .anyMatch(sd -> sd.getStatut().getId() == idEtapeFinale);

                if (aAtteintFinale) {
                    // Somme de toutes les dureeTravaille jusqu'à l'étape finale (incluse)
                    double somme = 0;
                    for (StatutDemande sd : historique) {
                        somme += sd.getDureeTravaille();
                        if (sd.getStatut().getId() == idEtapeFinale) break;
                    }
                    totalDuree = somme;
                }
            }

            result.add(new AlertResponse(demande, alertDtos, totalDuree));
        }

        return result;
    }
}