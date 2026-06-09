package com.forage.service;

import com.forage.dto.AlertDto;
import com.forage.dto.AlertResponse;
import com.forage.dto.DemandeDto;
import com.forage.model.*;
import com.forage.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private final StatutDemandeRepository statutDemandeRepository;
    private final DemandeRepository demandeRepository;
    private final ParametreRepository parametreRepository;

    @Autowired
    public AlertService(StatutDemandeRepository statutDemandeRepository, DemandeRepository demandeRepository,
            ParametreRepository parametreRepository) {
        this.statutDemandeRepository = statutDemandeRepository;
        this.demandeRepository = demandeRepository;
        this.parametreRepository = parametreRepository;
    }

    // public List<AlertResponse> getAlerts() {
    //     List<Demande> demandes = demandeRepository.findAll();
    //     List<Parametre> parametres = parametreRepository.findAll();
    //     List<AlertResponse> alerts = new ArrayList<>();
    //     List<AlertDto> alertDtos;
    //     int dureeTravaille;
    //     for (Demande demande : demandes) {
    //         List<StatutDemande> statutDemandes = statutDemandeRepository.findByDemandeId(demande.getId());
    //         alertDtos = new ArrayList<>();
    //         for (Parametre parametre : parametres) {

    //             dureeTravaille = 0;
    //             for (int i = 0; i < statutDemandes.size(); i++) {
    //                 StatutDemande statutDemande = statutDemandes.get(i);

    //                 if (parametre.getStatut1().getId() == statutDemande.getStatut().getId()) {

    //                     for (int j = i + 1; j < statutDemandes.size(); j++) {
    //                         StatutDemande statutDemande2 = statutDemandes.get(j);

    //                         if (parametre.getStatut2().getId() == statutDemande2.getStatut().getId()) {

    //                             // if (statutDemande.getDureeTravaille() != 0) {
    //                             dureeTravaille += statutDemande2.getDureeTravaille();
    //                             // }

    //                             // Optionnel : On peut casser la boucle j si on a trouvé le statut de fin
    //                             break;
    //                         }
    //                     }
    //                 }
    //             }
    //             if (dureeTravaille > parametre.getDuree()) {

    //                 AlertDto alert = new AlertDto(parametre, dureeTravaille);
    //                 alertDtos.add(alert);
    //             }
    //         }
    //         alerts.add(new AlertResponse(demande, alertDtos));
    //     }
    //     return alerts;
    // }
    public List<AlertResponse> getAlerts() {
    List<Demande> demandes = demandeRepository.findAll();
    List<Parametre> parametres = parametreRepository.findAll();
    List<AlertResponse> alerts = new ArrayList<>();
    
    for (Demande demande : demandes) {
        List<StatutDemande> statutDemandes = statutDemandeRepository.findByDemandeId(demande.getId());
        
        // Utilisation d'une Map pour filtrer les doublons d'intervalles sur cette demande
        // Clé : "ID_STATUT1->ID_STATUT2" | Valeur : L'objet AlertDto le plus critique
        Map<String, AlertDto> mapAlertesUniques = new HashMap<>();

        for (Parametre parametre : parametres) {
            int dureeTravaille = 0;
            boolean intervalleTrouve = false;

            for (int i = 0; i < statutDemandes.size(); i++) {
                StatutDemande statutDemande = statutDemandes.get(i);

                if (parametre.getStatut1().getId() == statutDemande.getStatut().getId()) {
                    for (int j = i + 1; j < statutDemandes.size(); j++) {
                        StatutDemande statutDemande2 = statutDemandes.get(j);

                        if (parametre.getStatut2().getId() == statutDemande2.getStatut().getId()) {
                            dureeTravaille += statutDemande2.getDureeTravaille();
                            intervalleTrouve = true;
                            break;
                        }
                    }
                }
                if (intervalleTrouve) break; // Évite de cumuler si le statut apparaît en double
            }

            // Si le seuil du paramètre est dépassé
            if (intervalleTrouve && dureeTravaille > parametre.getDuree()) {
                String cléIntervalle = parametre.getStatut1().getId() + "->" + parametre.getStatut2().getId();
                
                AlertDto nouvelleAlerte = new AlertDto(parametre, dureeTravaille);

                // LOGIQUE CRITIQUE : Si l'intervalle a déjà été enregistré pour un autre seuil
                if (mapAlertesUniques.containsKey(cléIntervalle)) {
                    AlertDto alerteExistante = mapAlertesUniques.get(cléIntervalle);
                    
                    // On ne conserve que celle qui a le seuil le plus élevé (la couleur la plus grave)
                    if (nouvelleAlerte.getDureeSeuil() > alerteExistante.getDureeSeuil()) {
                        mapAlertesUniques.put(cléIntervalle, nouvelleAlerte);
                    }
                } else {
                    // Premier seuil dépassé trouvé pour cet intervalle
                    mapAlertesUniques.put(cléIntervalle, nouvelleAlerte);
                }
            }
        }

        // On transforme les valeurs filtrées de la Map en liste pour le DTO de réponse
        List<AlertDto> alertDtos = new ArrayList<>(mapAlertesUniques.values());
        alerts.add(new AlertResponse(demande, alertDtos));
    }
    
    return alerts;
}

    // public AlertResponse getAlertsByReference(String reference) {
    // if (reference == null || reference.trim().isEmpty()) {
    // return null;
    // }

    // String ref = reference.trim();
    // Optional<Demande> demandeOpt = demandeRepository.findByReference(ref);
    // if (demandeOpt.isEmpty()) {
    // demandeOpt = demandeRepository.findByReferenceIgnoreCase(ref);
    // }
    // if (demandeOpt.isEmpty()) {
    // demandeOpt = demandeRepository.findFirstByReferenceContainingIgnoreCase(ref);
    // }

    // if (demandeOpt.isEmpty()) {
    // return null;
    // }

    // Demande demande = demandeOpt.get();
    // List<AlertDto> alerts =
    // alertRepository.findAlertsByDemandeId(demande.getId());

    // return new AlertResponse(new DemandeDto(demande.getId(),
    // demande.getReference()), alerts);
    // }
}
