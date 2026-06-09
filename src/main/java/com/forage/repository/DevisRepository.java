package com.forage.repository;

import com.forage.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Integer> {
    void deleteByDemandeId(int demandeId);
}

