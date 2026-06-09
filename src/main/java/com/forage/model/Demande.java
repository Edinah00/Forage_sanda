package com.forage.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

import jakarta.persistence.*;
@Entity
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String reference;
    @ManyToOne 
    @JoinColumn(name = "idClient") 
    @JsonIgnore
    private Client client;
    @ManyToOne 
    @JoinColumn(name = "idCommune") 
    private Commune commune;
    private LocalDateTime dateDemande;
    private String lieu;
    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<StatutDemande> statutDemandes = new ArrayList<>();

    public List<StatutDemande> getStatutDemandes() {
        return statutDemandes;
    }

    public void setStatutDemandes(List<StatutDemande> statutDemandes) {
        this.statutDemandes = statutDemandes;
    }

    public Demande() {
    }

    public Demande(int id, String reference, Client client, Commune commune, LocalDateTime dateDemande, String lieu) {
        this.id = id;
        this.reference = reference;
        this.client = client;
        this.commune = commune;
        this.dateDemande = dateDemande;
        this.lieu = lieu;
    }
    @PrePersist
    public void generateReference() {
        if (this.reference == null || this.getReference().trim().isEmpty()) {
            this.reference = "DEM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        if (this.dateDemande == null) {
            this.dateDemande = LocalDateTime.now(); // Date/Heure actuelle par défaut
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Commune getCommune() {
        return commune;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

}