package com.forage.controller;
import com.forage.model.Devis;
import com.forage.model.DevisDetail;
import com.forage.model.StatutDemande;
import com.forage.model.Demande;
import com.forage.model.TypeDevis;
import com.forage.service.DevisService;
import com.forage.service.DemandeService;
import com.forage.service.TypeDevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Controller
@RequestMapping("/devis")
public class DevisController {

    @Autowired private DevisService devisService;
    @Autowired private DemandeService demandeService;
    @Autowired private TypeDevisService typeDevisService;

    @GetMapping("/nouveau")
    public String showForm(Model model) {
        Devis devis = new Devis();

        // Initialiser avec une ligne vide
        devis.getDetails().add(new DevisDetail());

        model.addAttribute("devis", devis);
        model.addAttribute("demandes", demandeService.findAll());
        model.addAttribute("types", typeDevisService.findAll());
        model.addAttribute("list_devis", devisService.findAll());
        return "devis-form";
    }
    

    // @GetMapping("/demande-info")
    // @ResponseBody
    // public ResponseEntity<Map<String, Object>> demandeInfo(@RequestParam("reference") String reference) {
    //     Demande demande = demandeService.findByReference(reference);
    //     if (demande == null) {
    //         return ResponseEntity.notFound().build();
    //     }

    //     Map<String, Object> payload = new HashMap<>();
    //     payload.put("id", demande.getId());
    //     payload.put("reference", demande.getReference());
    //     if (demande.getDateDemande() != null) {
    //         payload.put("dateDemande", demande.getDateDemande().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    //     } else {
    //         payload.put("dateDemande", "");
    //     }
    //     payload.put("lieu", demande.getLieu());
    //     payload.put("client", demande.getClient() != null ? demande.getClient().getNom() : "");
    //     payload.put("commune", demande.getCommune() != null ? demande.getCommune().getLibelle() : "");
    //     payload.put("statuts", demande.getStatutDemandes() != null ? demande.getStatutDemandes() : "");

    //     return ResponseEntity.ok(payload);
    // }
    @GetMapping("/demande-info")
@ResponseBody
public ResponseEntity<Map<String, Object>> demandeInfo(@RequestParam("reference") String reference) {
    Demande demande = demandeService.findByReference(reference);
    if (demande == null) {
        return ResponseEntity.notFound().build();
    }

    Map<String, Object> payload = new HashMap<>();
    payload.put("id", demande.getId());
    payload.put("reference", demande.getReference());
    payload.put("lieu", demande.getLieu());
    payload.put("client", demande.getClient() != null ? demande.getClient().getNom() : "");
    payload.put("commune", demande.getCommune() != null ? demande.getCommune().getLibelle() : "");
    
    if (demande.getDateDemande() != null) {
        payload.put("dateDemande", demande.getDateDemande().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    // Liste pour remplir le Select
    java.util.List<Map<String, Object>> statutsList = new ArrayList<>();
    if (demande.getStatutDemandes() != null) { 
        for (StatutDemande sd : demande.getStatutDemandes()) {
            Map<String, Object> sMap = new HashMap<>();
            sMap.put("id", sd.getId()); // ID de la table de jointure (StatutDemande)
            sMap.put("libelle", sd.getStatut() != null ? sd.getStatut().getLibelle() : "Inconnu");
            sMap.put("observations", sd.getObservations() != null ? sd.getObservations() : "");
            
            // Format ISO nécessaire pour les inputs de type datetime-local (yyyy-MM-ddTHH:mm)
            if (sd.getDateStatut() != null) {
                sMap.put("dateIso", sd.getDateStatut().toString()); 
            } else {
                sMap.put("dateIso", "");
            }
            statutsList.add(sMap);
        }
    }
    payload.put("statuts", statutsList);

    return ResponseEntity.ok(payload);
}

    @PostMapping("/save")
    public String saveDevis(@ModelAttribute("devis") Devis devis,
                            @RequestParam("demandeId") int demandeId,
                            @RequestParam("typeId") int typeId) {
        // Filtrer les lignes vides
        if (devis.getDetails() != null) {
            devis.getDetails().removeIf(detail -> detail.getLibelle() == null || detail.getLibelle().isEmpty());
        }

        Demande demande = demandeService.findById(demandeId);
        TypeDevis type = typeDevisService.findById(typeId);
        if (demande == null || type == null) {
            return "redirect:/devis/nouveau";
        }
        devis.setDemande(demande);
        devis.setTypeDevis(type);
        if (devis.getCreatedAt() == null) {
            devis.setCreatedAt(LocalDateTime.now());
        }
        

        devisService.saveDevisWithStatus(devis);
        return "redirect:/devis/nouveau";
    }
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        Devis devis = devisService.findById(id);
        model.addAttribute("devis", devis);
        model.addAttribute("demandes", demandeService.findAll());
        model.addAttribute("types", typeDevisService.findAll());
        model.addAttribute("list_devis", devisService.findAll());
        return "devis-form"; 
    }
    @GetMapping("/delete")
    public String deleteDevis(@RequestParam("id") int id) {
        
        devisService.delete(id);
        
        return "redirect:/devis/nouveau";
    }
}
