package com.forage.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forage.model.*;
import com.forage.repository.*;
import com.forage.util.Util;

@Service
public class DevisService {

    @Autowired private DevisRepository          devisRepository;
    @Autowired private StatutDemandeService     statutDemandeService;
    @Autowired private StatutDemandeRepository  statutDemandeRepository;
    @Autowired private StatutRepository         statutRepository;

    @Transactional
    public void saveDevisWithStatus(Devis devis) throws IllegalStateException {
        // 1. Liaison des lignes de détail au devis parent
        if (devis.getDetails() != null) {
            for (DevisDetail detail : devis.getDetails()) {
                detail.setDevis(devis);
            }
        }

        Devis saved = devisRepository.save(devis);

        Demande demande = saved.getDemande();
        if (demande == null || saved.getTypeDevis() == null) return;

        String sigle = Util.getSigleForType(saved.getTypeDevis().getId());
        if (sigle == null) return;

        // 2. RÈGLE MÉTIER : Devis Forage (DFC) bloqué si DET absent
        if ("DFC".equals(sigle)) {
            List<StatutDemande> historique =
                    statutDemandeRepository.findByDemandeId(demande.getId());
            if (!Util.peutCreerDevisForage(historique)) {
                throw new IllegalStateException(
                    "Impossible de créer un Devis Forage : " +
                    "le Devis Étude n'est pas encore terminé (statut DET manquant).");
            }
        }

        Statut statut = statutRepository.findFirstBySigle(sigle).orElse(null);
        if (statut == null) return;

        // 3. Upsert du StatutDemande correspondant
        Optional<StatutDemande> existant =
                statutDemandeRepository.findByDemandeIdAndStatutId(demande.getId(), statut.getId());

        StatutDemande sd = existant.orElseGet(() -> {
            StatutDemande n = new StatutDemande();
            n.setDemande(demande);
            n.setStatut(statut);
            return n;
        });

        sd.setDateStatut(saved.getCreatedAt() != null ? saved.getCreatedAt() : LocalDateTime.now());
        statutDemandeService.changerStatut(sd);
    }

    public List<Devis> findAll()          { return devisRepository.findAll(); }
    public Devis       findById(int id)   { return devisRepository.findById(id).orElse(null); }

    @Transactional
    public void delete(int devisId) {
        Devis devis = devisRepository.findById(devisId).orElse(null);
        if (devis == null) return;

        Demande   demande = devis.getDemande();
        TypeDevis type    = devis.getTypeDevis();

        if (demande != null && type != null) {
            String sigle = Util.getSigleForType(type.getId());
            if (sigle != null) {
                statutRepository.findFirstBySigle(sigle).ifPresent(statut ->
                    statutDemandeRepository
                        .findByDemandeIdAndStatutId(demande.getId(), statut.getId())
                        .ifPresent(statutDemandeRepository::delete)
                );
            }
        }
        devisRepository.delete(devis);
    }
}