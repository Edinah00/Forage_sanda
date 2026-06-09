package com.forage.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class DevisDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String libelle;
    private double quantite;
    private double prixUnitaire;

    @ManyToOne
    @JoinColumn(name = "idDevis")
    private Devis devis;

    public DevisDetail() {}

    // Méthode pratique pour calculer le total de la ligne
    public double getMontantLigne() {
        return quantite * prixUnitaire;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public double getQuantite() { return quantite; }
    public void setQuantite(double quantite) { this.quantite = quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public Devis getDevis() { return devis; }
    public void setDevis(Devis devis) { this.devis = devis; }
}