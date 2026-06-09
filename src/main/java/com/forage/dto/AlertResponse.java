package com.forage.dto;

import java.util.List;
import com.forage.model.Demande;

public class AlertResponse {
    private Demande        demande;
    private List<AlertDto> alerts;

    /**
     * Durée totale (DC → DFT) en minutes.
     * Null si la demande n'a pas encore atteint "Devis Forage terminé".
     */
    private Double totalDureeTravaille;

    public AlertResponse(Demande demande, List<AlertDto> alerts, Double totalDureeTravaille) {
        this.demande              = demande;
        this.alerts               = alerts;
        this.totalDureeTravaille  = totalDureeTravaille;
    }

    public Demande getDemande()                           { return demande; }
    public List<AlertDto> getAlerts()                     { return alerts; }
    public Double getTotalDureeTravaille()                { return totalDureeTravaille; }
}