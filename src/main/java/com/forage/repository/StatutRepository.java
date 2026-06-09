package com.forage.repository;

import com.forage.model.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatutRepository extends JpaRepository<Statut, Integer> {

    /** Retourne le premier statut correspondant au sigle (évite NonUniqueResultException). */
    Optional<Statut> findFirstBySigle(String sigle);

    /** Conservé pour compatibilité — préférer findFirstBySigle. */
    Optional<Statut> findBySigle(String sigle);
}