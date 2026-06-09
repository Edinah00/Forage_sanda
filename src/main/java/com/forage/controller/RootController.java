package com.forage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    // Redirect root of the webapp to the demandes list
    @GetMapping("/")
    public String root() {
        return "redirect:/demandes/liste";
    }

    // Allow /nouveau at app root to redirect to the demandes form
    @GetMapping("/nouveau")
    public String nouveauRoot() {
        return "redirect:/demandes/nouveau";
    }
}
