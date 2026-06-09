package com.forage.repository;

import com.forage.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutRepository extends JpaRepository<Statut, Integer> {
	java.util.Optional<Statut> findBySigle(String sigle);
}

