package com.forage.controller;

import com.forage.model.*;
import com.forage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/demandes")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;
    @Autowired
    private ClientService clientService;

    @Autowired
    private CommuneService communeService;

    // Liste des demandes
    @GetMapping("/liste")
    public String listeDemandes(Model model) {
        model.addAttribute("demandes", demandeService.findAll());
        return "demande-liste";
    }

    @GetMapping("/delete")
    public String deleteDemande(@RequestParam("id") int id) {
        
        demandeService.delete(id);
        
        return "redirect:/demandes/liste";
    }

    // Formulaire d'ajout
    @GetMapping("/nouveau")
    public String showForm(Model model) {
        model.addAttribute("demande", new Demande());
        
        java.util.List<Client> clients = clientService.findAll();
        java.util.List<Commune> communes = communeService.findAll();
        
        model.addAttribute("clients", clients); 
        model.addAttribute("communes", communes);
        
        return "demande-form";
    }

    // Enregistrer
    @PostMapping("/save")
    public String saveDemande(@ModelAttribute("demande") Demande demande) {
        demandeService.saveWithInitialStatus(demande);
        return "redirect:/demandes/liste";
    }
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        Demande demande = demandeService.findById(id);
        model.addAttribute("demande", demande);
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("communes", communeService.findAll());
        return "demande-form"; 
    }
}