package com.forage.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String libelle;
    @ManyToOne 
    @JoinColumn(name = "idRegion") 
    private Region region; 
    @OneToMany(mappedBy = "district")
    @JsonIgnore
    private List<Commune> communes = new ArrayList<>();

    public District() {}

    public District(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public List<Commune> getCommunes() { return communes; }
    public void setCommunes(List<Commune> communes) { this.communes = communes; }
}
