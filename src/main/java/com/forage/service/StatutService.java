package com.forage.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.forage.model.*;
import com.forage.repository.*;
@Service
public class StatutService {
    @Autowired private StatutRepository statutRepository;

    
    public List<Statut> findAll() {
        return statutRepository.findAll();
    }
    public Statut findById(int id) {
        return statutRepository.findById(id).orElse(null);
    }
}
