package com.forage.model;

import jakarta.persistence.*;

@Entity
public class Parametre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idStatut1", referencedColumnName = "id")
    private Statut statut1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idStatut2", referencedColumnName = "id")
    private Statut statut2;

    /** Borne inférieure de l'intervalle (inclusive) */
    private int dureeMin;

    /** Borne supérieure de l'intervalle (exclusive) */
    private int dureeMax;

    private String alerte;

    public Parametre() {}

    public Parametre(Statut statut1, Statut statut2, int dureeMin, int dureeMax, String alerte) {
        this.statut1  = statut1;
        this.statut2  = statut2;
        this.dureeMin = dureeMin;
        this.dureeMax = dureeMax;
        this.alerte   = alerte;
    }

    // ── Getters / Setters ────────────────────────────────────
    public int getId()            { return id; }
    public void setId(int id)     { this.id = id; }

    public Statut getStatut1()               { return statut1; }
    public void setStatut1(Statut statut1)   { this.statut1 = statut1; }

    public Statut getStatut2()               { return statut2; }
    public void setStatut2(Statut statut2)   { this.statut2 = statut2; }

    public int getDureeMin()                 { return dureeMin; }
    public void setDureeMin(int dureeMin)    { this.dureeMin = dureeMin; }

    public int getDureeMax()                 { return dureeMax; }
    public void setDureeMax(int dureeMax)    { this.dureeMax = dureeMax; }

    public String getAlerte()                { return alerte; }
    public void setAlerte(String alerte)     { this.alerte = alerte; }
}