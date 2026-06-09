package com.forage.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class StatutDemande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "idDemande")
    @JsonIgnoreProperties("statutDemandes")
    private Demande demande;
    @ManyToOne
    @JoinColumn(name = "idStatut")
    private Statut statut;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateStatut;
    private double dureeTravaille;
    private String observations;

    public StatutDemande() {}

    public StatutDemande(int id, Demande demande, Statut statut, LocalDateTime dateStatut) {
        this.id = id;
        this.demande = demande;
        this.statut = statut;
        this.dateStatut = dateStatut;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Demande getDemande() { return demande; }
    public void setDemande(Demande demande) { this.demande = demande; }
    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }
    public LocalDateTime getDateStatut() { return dateStatut; }
    public void setDateStatut(LocalDateTime dateStatut) { this.dateStatut = dateStatut; }
    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public double getDureeTravaille() {
        return dureeTravaille;
    }

    public void setDureeTravaille(double dureeTravaille) {
        this.dureeTravaille = dureeTravaille;
    }
}