package com.forage.repository;

import com.forage.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommuneRepository extends JpaRepository<Commune, Integer> {}
