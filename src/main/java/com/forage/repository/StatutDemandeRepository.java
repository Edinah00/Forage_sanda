package com.forage.repository;

import com.forage.model.*;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutDemandeRepository extends JpaRepository<StatutDemande, Integer> {

    void deleteByDemandeId(int demandeId);
    void deleteByStatutId(int statutId);
    java.util.List<StatutDemande> findByDemandeId(int demandeId);
    StatutDemande findTopByDemandeIdOrderByDateStatutDesc(int demandeId);
    StatutDemande findTopByDemandeIdAndDateStatutLessThanOrderByDateStatutDesc(int demandeId, java.time.LocalDateTime dateStatut);
    StatutDemande findTopByDemandeIdAndDateStatutGreaterThanOrderByDateStatutAsc(int demandeId, java.time.LocalDateTime dateStatut);
// ◄ AJOUT 1 : Pour retrouver le statut d'un type de devis spécifique sur une demande (Évite les doublons de modification)
    Optional<StatutDemande> findByDemandeIdAndStatutId(int demandeId, int statutId);

    // ◄ AJOUT 2 : Pour calculer la durée lors d'une MODIFICATION en ignorant l'enregistrement en cours d'édition
    StatutDemande findTopByDemandeIdAndIdNotOrderByDateStatutDesc(int demandeId, int statutDemandeId);
    @Modifying
    @Query("DELETE FROM StatutDemande sd WHERE LOWER(sd.statut.libelle) LIKE LOWER(CONCAT('%', :mot, '%'))")
    void deleteByStatutLibelleContaining(@Param("mot") String mot);
}

