package com.forage.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forage.model.Devis;
import com.forage.model.DevisDetail;
import com.forage.model.Demande;
import com.forage.model.Statut;
import com.forage.model.StatutDemande;
import com.forage.model.TypeDevis;
import com.forage.repository.DevisRepository;
import com.forage.repository.StatutDemandeRepository;
import com.forage.repository.StatutRepository;
import com.forage.util.Util;

@Service
public class DevisService {
    @Autowired private DevisRepository devisRepository;
    @Autowired private StatutDemandeService statutDemandeService;
    @Autowired private StatutDemandeRepository statutDemandeRepository;
    @Autowired private StatutRepository statutRepository;
   

    // @Transactional
    // public void saveDevisWithStatus(Devis devis) {
    //     if (devis.getDetails() != null) {
    //         for (DevisDetail detail : devis.getDetails()) {
    //             detail.setDevis(devis);
    //         }
    //     }

    //     Devis saved = devisRepository.save(devis);

    //     Demande demande = saved.getDemande();
    //     if (demande != null && saved.getTypeDevis() != null) {
    //         String sigle = Util.getSigleForType(saved.getTypeDevis().getId());
    //         if (sigle != null) {
    //             Statut statut = statutRepository.findBySigle(sigle).orElse(null);
    //             if (statut != null) {
    //                 StatutDemande sd = new StatutDemande();
    //                 sd.setDemande(demande);
    //                 sd.setStatut(statut);
    //                 sd.setDateStatut(devis.getCreatedAt() != null ? devis.getCreatedAt() : LocalDateTime.now());
    //                 statutDemandeService.changerStatut(sd);
    //                // statutDemandeRepository.save(sd);
    //             }
    //         }
    //     }
    // }
    @Transactional
public void saveDevisWithStatus(Devis devis) {
    // 1. On lie d'abord chaque ligne de détail au devis parent
    if (devis.getDetails() != null) {
        for (DevisDetail detail : devis.getDetails()) {
            detail.setDevis(devis);
        }
    }

    // On sauvegarde le devis (ajoute ou met à jour selon si devis.id > 0)
    Devis saved = devisRepository.save(devis);

    // 2. Gestion du statut de manière chirurgicale pour éviter les ajouts intempestifs
    Demande demande = saved.getDemande();
    if (demande != null && saved.getTypeDevis() != null) {
        String sigle = Util.getSigleForType(saved.getTypeDevis().getId());
        if (sigle != null) {
            Statut statut = statutRepository.findBySigle(sigle).orElse(null);
            if (statut != null) {
                
                // CORRECTION : On cherche s'il existe DEJA un statut pour cette demande ET ce statut précis
                Optional<StatutDemande> statutExistant = statutDemandeRepository
                        .findByDemandeIdAndStatutId(demande.getId(), statut.getId());

                StatutDemande sd;
                if (statutExistant.isPresent()) {
                    // Si le statut existe déjà, on REPREND la ligne existante au lieu d'en recréer une
                    sd = statutExistant.get();
                } else {
                    // Uniquement si c'est la toute première fois, on fait une nouvelle insertion
                    sd = new StatutDemande();
                    sd.setDemande(demande);
                    sd.setStatut(statut);
                }

                // On met à jour les dates et infos temporelles
                sd.setDateStatut(saved.getCreatedAt() != null ? saved.getCreatedAt() : LocalDateTime.now());

                // On passe par ta méthode "changerStatut" qui gère déjà le calcul des durées travaillées
                statutDemandeService.changerStatut(sd);
            }
        }
    }
}
    public List<Devis> findAll() {
        return devisRepository.findAll();
    }
    public Devis findById(int id) {
        return devisRepository.findById(id).orElse(null);
    }

    // @Transactional
    // public void delete(int id) {
    
    //     Devis devis = devisRepository.findById(id).orElse(null);
    //    // StatutDemande statutDemande = statutDemandeRepository.findById(id).orElse(null);
        
    //     if (devis != null) {
            
    //         statutDemandeRepository.deleteByStatutLibelleContaining("Devis"); 
            
        
    //         devisRepository.delete(devis);
    //     }
    // }
    @Transactional
public void delete(int devisId) {
    Devis devis = devisRepository.findById(devisId).orElse(null);
    
    if (devis != null) {
        Demande demande = devis.getDemande();
        TypeDevis type = devis.getTypeDevis();
        
        if (demande != null && type != null) {
            // 1. On retrouve le sigle spécifique au type de devis qu'on supprime (ex: DEV_FORAGE)
            String sigle = Util.getSigleForType(type.getId());
            if (sigle != null) {
                Statut statut = statutRepository.findBySigle(sigle).orElse(null);
                
                if (statut != null) {
                    // 2. CORRECTION CRITIQUE : On ne supprime QUE la ligne qui associe CETTE demande et CE statut précis
                    Optional<StatutDemande> sd = statutDemandeRepository
                        .findByDemandeIdAndStatutId(demande.getId(), statut.getId());
                    
                    // On retire uniquement cette transition d'historique
                    sd.ifPresent(statutDemandeRepository::delete);
                }
            }
        }
        
        // 3. Enfin, on supprime le devis lui-même
        devisRepository.delete(devis);
    }
}
}
