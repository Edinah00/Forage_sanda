package com.forage.model;

import java.util.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private String adresse;
    private String contact;
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<Demande> demandes = new ArrayList<>();
    public Client() {
    }
    public Client(int id, String nom, String adresse, String contact) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.contact = contact;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public List<Demande> getDemandes() {
        return demandes;
    }
    public void setDemandes(List<Demande> demandes) {
        this.demandes = demandes;
    }
}
