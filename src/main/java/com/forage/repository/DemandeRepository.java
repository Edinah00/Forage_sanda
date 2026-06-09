package com.forage.repository;

import com.forage.model.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Integer> {
    
    // // Spring Data JPA génère automatiquement les méthodes CRUD de base :
    // // save(), findById(), findAll(), deleteById(), etc.

    // // Vous pouvez aussi ajouter des méthodes de recherche personnalisées par convention de nommage :
    
    // Trouver des demandes par référence
    java.util.Optional<Demande> findByReference(String reference);
    java.util.Optional<Demande> findByReferenceIgnoreCase(String reference);
    java.util.Optional<Demande> findFirstByReferenceContainingIgnoreCase(String reference);
    
    // // Trouver les demandes d'un client spécifique
    // List<Demande> findByClientId(int idClient);
    
    // // Trouver par lieu (recherche partielle)
    // List<Demande> findByLieuContainingIgnoreCase(String lieu);
}
