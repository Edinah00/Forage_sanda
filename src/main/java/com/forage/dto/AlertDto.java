package com.forage.dto;

import com.forage.model.Parametre;

public class AlertDto {

    private String alerte;
    private int    dureeMin;        // borne inférieure de l'intervalle
    private int    dureeMax;        // borne supérieure de l'intervalle
    private double dureeTravaille;
    private String statut1;
    private String statut2;

    public AlertDto(Parametre parametre, double dureeTravaille) {
        if (parametre != null) {
            this.alerte      = parametre.getAlerte();
            this.dureeMin    = parametre.getDureeMin();
            this.dureeMax    = parametre.getDureeMax();
            this.statut1     = parametre.getStatut1().getId() + " : " + parametre.getStatut1().getLibelle();
            this.statut2     = parametre.getStatut2().getId() + " : " + parametre.getStatut2().getLibelle();
        } else {
            this.alerte   = "N/A";
            this.dureeMin = 0;
            this.dureeMax = 0;
        }
        this.dureeTravaille = dureeTravaille;
    }

    // ── Getters / Setters ────────────────────────────────────
    public String getAlerte()                    { return alerte; }
    public void setAlerte(String alerte)         { this.alerte = alerte; }

    public int getDureeMin()                     { return dureeMin; }
    public void setDureeMin(int dureeMin)        { this.dureeMin = dureeMin; }

    public int getDureeMax()                     { return dureeMax; }
    public void setDureeMax(int dureeMax)        { this.dureeMax = dureeMax; }

    public double getDureeTravaille()            { return dureeTravaille; }
    public void setDureeTravaille(double d)      { this.dureeTravaille = d; }

    public String getStatut1()                   { return statut1; }
    public void setStatut1(String statut1)       { this.statut1 = statut1; }

    public String getStatut2()                   { return statut2; }
    public void setStatut2(String statut2)       { this.statut2 = statut2; }
}