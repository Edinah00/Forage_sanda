package com.forage.controller;

import com.forage.dto.AlertResponse;
import com.forage.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping("/alerts")
    public ResponseEntity<?> getAlerts() {
        List<AlertResponse> response = alertService.getAlerts();
        if (response == null || response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Demande introuvable pour la reference fournie."));
        }
        return ResponseEntity.ok(response);
    }
    // public ResponseEntity<?> getAlerts(@RequestParam("reference") String reference) {
    //     AlertResponse response = alertService.getAlertsByReference(reference);
    //     if (response == null) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND)
    //             .body(Map.of("error", "Demande introuvable pour la reference fournie."));
    //     }
    //     return ResponseEntity.ok(response);
    // }
}
