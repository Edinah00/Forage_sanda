package com.forage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Dit à Spring : "Je suis un aiguilleur de requêtes"
public class TestController {

    @GetMapping("/test") // Si l'utilisateur tape : localhost:8080/forage/test
    public String direBonjour(Model model) {
        
        // On prépare une donnée pour la page
        String monNom = "Etudiant en L2";
        
        // On met cette donnée dans le "panier" (Model) pour que la JSP puisse la voir
        model.addAttribute("nomAffiche", monNom);
        
        return "maPage"; // Spring va chercher /WEB-INF/views/maPage.jsp
    }
}