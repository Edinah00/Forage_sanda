package com.forage.service;

import com.forage.model.Demande;
import com.forage.model.Statut;
import com.forage.model.StatutDemande;
import com.forage.repository.*; // À créer (extends JpaRepository)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class DemandeService  {

    @Autowired
    private DemandeRepository demandeRepository;
    @Autowired private StatutDemandeRepository statutDemandeRepository;
    @Autowired private StatutRepository statutRepository;
    @Autowired private DevisRepository devisRepository;

    
    public List<Demande> findAll() {
        return demandeRepository.findAll();
    }

    public Demande findById(int id) {
        return demandeRepository.findById(id).orElse(null);
    }

    public Demande findByReference(String reference) {
        if (reference == null) {
            return null;
        }
        String normalized = reference.trim();
        if (normalized.isEmpty()) {
            return null;
        }

        return demandeRepository.findByReference(normalized)
        .orElseGet(() -> demandeRepository.findByReferenceIgnoreCase(normalized)
            .orElseGet(() -> demandeRepository.findFirstByReferenceContainingIgnoreCase(normalized)
                .orElse(null)));
    }

    @Transactional
    public void saveWithInitialStatus(Demande demande) {
        
        Demande savedDemande = demandeRepository.save(demande);

        Statut initialStatut = statutRepository.findById(1).orElse(null);

        if (initialStatut != null) {
            StatutDemande sd = new StatutDemande();
            sd.setDemande(savedDemande);
            sd.setStatut(initialStatut);
            sd.setDateStatut(savedDemande.getDateDemande()); 
            
            statutDemandeRepository.save(sd);
        }
    }

    public void save(Demande demande) {
        demandeRepository.save(demande);
    }
    @Transactional
    public void delete(int id) {
    
        Demande demande = demandeRepository.findById(id).orElse(null);
        
        if (demande != null) {
            
            statutDemandeRepository.deleteByDemandeId(id); 
            devisRepository.deleteByDemandeId(id); 
        
            demandeRepository.delete(demande);
        }
    }

    // public void delete(int id) {
    //     demandeRepository.deleteById(id);
    // }
}