package com.forage.dto;

import java.util.List;
import com.forage.model.*;
public class AlertResponse {
    private Demande demande;
    private List<AlertDto> alerts;

    public AlertResponse(Demande demande, List<AlertDto> alerts) {
        this.demande = demande;
        this.alerts = alerts;
    }

    public Demande getDemande() {
        return demande;
    }

    public List<AlertDto> getAlerts() {
        return alerts;
    }
}
