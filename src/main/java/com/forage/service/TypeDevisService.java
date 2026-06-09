package com.forage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forage.model.TypeDevis;
import com.forage.repository.TypeDevisRepository;

@Service
public class TypeDevisService {
    @Autowired private TypeDevisRepository typeDevisRepository;

    public List<TypeDevis> findAll() {
        return typeDevisRepository.findAll();
    }

    public TypeDevis findById(int id) {
        return typeDevisRepository.findById(id).orElse(null);
    }
}
