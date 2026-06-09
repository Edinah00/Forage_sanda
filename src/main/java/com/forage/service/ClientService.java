package com.forage.service;
import com.forage.model.*;
import com.forage.repository.*; // À créer (extends JpaRepository)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ClientService {
    @Autowired private ClientRepository clientRepository;
    public List<Client> findAll() { return clientRepository.findAll(); }
}

