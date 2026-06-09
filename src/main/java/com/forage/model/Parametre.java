package com.forage.model;

import jakarta.persistence.*;

@Entity
public class Parametre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER) // ou LAZY selon vos besoins
@JoinColumn(name = "idStatut1", referencedColumnName = "id")
    private Statut statut1;
    @ManyToOne(fetch = FetchType.EAGER) // ou LAZY selon vos besoins
@JoinColumn(name = "idStatut2", referencedColumnName = "id")
    private Statut statut2;
    private int duree;
    private String alerte;
    public Parametre() {}
    public Parametre(Statut statut1, Statut statut2, int duree, String alerte) {
        this.statut1 = statut1;
        this.statut2 = statut2;
        this.duree = duree;
        this.alerte = alerte;
    }
    public Statut getStatut1() {
        return statut1;
    }
    public void setStatut1(Statut statut1) {
        this.statut1 = statut1;
    }
    public Statut getStatut2() {
        return statut2;
    }
    public void setStatut2(Statut statut2) {
        this.statut2 = statut2;
    }
    public int getDuree() {
        return duree;
    }
    public void setDuree(int duree) {
        this.duree = duree;
    }
    public String getAlerte() {
        return alerte;
    }
    public void setAlerte(String alerte) {
        this.alerte = alerte;
    }

}
