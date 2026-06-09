package com.forage.controller;

import com.forage.model.*;
import com.forage.service.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/statut-demande")
public class StatutDemandeController{
    
    @Autowired private StatutService statutService;
    @Autowired private DemandeService demandeService;
    @Autowired private StatutDemandeService statutDemandeService;

    @GetMapping("/nouveau")
    public String showForm(Model model) {
        StatutDemande statutDemande = new StatutDemande();
        model.addAttribute("statutDemande", statutDemande);
        model.addAttribute("demandes", demandeService.findAll());
        model.addAttribute("statuts", statutService.findAll());
        model.addAttribute("statutDemandes", statutDemandeService.findAll());
        return "statut-ajout";
    }
    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id,
                        @RequestHeader(value = "Referer", required = false) String referer) {
        
        statutDemandeService.delete(id);

        if (referer != null && referer.contains("/statut-demande/edit")) {
            return "redirect:/statut-demande/edit";
        }
        return "redirect:/statut-demande/nouveau";
    }
    // @PostMapping("/save")
    // public String save(@ModelAttribute("statutDemande") StatutDemande statutDemande,
    //                         @RequestParam("demandeId") int demandeId,
    //                         @RequestParam("statutId") int statutId) {
        
    //     Demande demande = demandeService.findById(demandeId);
    //     Statut type = statutService.findById(statutId);
    //     if (demande == null || type == null) {
    //         return "redirect:/devis/nouveau";
    //     }
    //     statutDemande.setDemande(demande);
    //     statutDemande.setStatut(type);

    //     // statutDemandeService.save(statutDemande);
    //     statutDemandeService.changerStatut(statutDemande);
    //     return "redirect:/statut-demande/nouveau";
    // }
    @PostMapping("/save")
    public String save(@ModelAttribute("statutDemande") StatutDemande statutDemande,
                       @RequestParam("demandeId") int demandeId,
                       @RequestParam("statutId") int statutId,
                       @RequestParam("dateStatut") String dateStatutStr) { // <-- On intercepte explicitement la String
        
        Demande demande = demandeService.findById(demandeId);
        Statut type = statutService.findById(statutId);
        if (demande == null || type == null) {
            return "redirect:/statut-demande/nouveau";
        }
        
        statutDemande.setDemande(demande);
        statutDemande.setStatut(type);

        // FORCE LA PARSE DE LA DATE DEPUIS LE FORMULAIRE
        if (dateStatutStr != null && !dateStatutStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime dateSaisie = LocalDateTime.parse(dateStatutStr, formatter);
            statutDemande.setDateStatut(dateSaisie);
        } else if (statutDemande.getDateStatut() == null) {
            statutDemande.setDateStatut(LocalDateTime.now());
        }

        statutDemandeService.changerStatut(statutDemande);
        return "redirect:/statut-demande/nouveau";
    }
    @GetMapping("/edit")
    public String showForm2(Model model) {
        
       model.addAttribute("statutDemandes", statutDemandeService.findAll());
        return "statut-edit";
    }
    @PostMapping("/modifier-statut")
public String modifierStatut(@RequestParam("statutDemandeId") int statutDemandeId,
                            @RequestParam("statutDate") String statutDate,
                            @RequestParam("observations") String observations) {
    
    // 1. On crée un pattern strict qui correspond au format du input HTML (yyyy-MM-ddTHH:mm)
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    
    // 2. On force le parse strict. Si la chaîne dit 22h, Java stockera 22h pile.
    LocalDateTime dateForcee = LocalDateTime.parse(statutDate, formatter);
    statutDemandeService.updateDateAndObservationAndRecompute(statutDemandeId, dateForcee, observations);
    return "redirect:/statut-demande/edit";
}
    // @PostMapping("/modifier-statut")
    // public String modifierStatut(@RequestParam("statutDemandeId") int statutDemandeId,
    //                             @RequestParam("statutDate") String statutDate) {
        
    //     // Vous avez maintenant l'ID reçu du JavaScript !
    //     StatutDemande sd = statutDemandeService.findById(statutDemandeId);
        
    //     // Exemple de modification de la date
    //     sd.setDateStatut(LocalDateTime.parse(statutDate));
    //     statutDemandeService.save(sd);
        
    //     return "redirect:/statut-demande/edit";
    // }
}
