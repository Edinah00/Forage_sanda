package com.forage.dto;

import com.forage.model.Parametre;

public class AlertDto {
    // On extrait uniquement les données plates dont le JavaScript a besoin
    private String alerte;
    private int dureeSeuil;
    private double dureeTravaille;
    private String statut1;
private String statut2;

    // Le constructeur reçoit toujours le Parametre, mais extrait ses valeurs immédiatement
    public AlertDto(Parametre parametre, double dureeTravaille) {
        if (parametre != null) {
            this.alerte = parametre.getAlerte();
            this.dureeSeuil = parametre.getDuree();
            this.statut1 = parametre.getStatut1().getId() + " : " + parametre.getStatut1().getLibelle();
            this.statut2 = parametre.getStatut2().getId() + " : " + parametre.getStatut2().getLibelle();
        } else {
            this.alerte = "N/A";
            this.dureeSeuil = 0;
        }
        this.dureeTravaille = dureeTravaille;
    }
public String getStatut1() {
        return statut1;
    }

    public void setStatut1(String statut1) {
        this.statut1 = statut1;
    }

    public String getStatut2() {
        return statut2;
    }

    public void setStatut2(String statut2) {
        this.statut2 = statut2;
    }
    // Getters et Setters simples (sans entités Hibernate complexes)
    public String getAlerte() {
        return alerte;
    }

    public void setAlerte(String alerte) {
        this.alerte = alerte;
    }

    public int getDureeSeuil() {
        return dureeSeuil;
    }

    public void setDureeSeuil(int dureeSeuil) {
        this.dureeSeuil = dureeSeuil;
    }

    public double getDureeTravaille() {
        return dureeTravaille;
    }

    public void setDureeTravaille(double dureeTravaille) {
        this.dureeTravaille = dureeTravaille;
    }
}