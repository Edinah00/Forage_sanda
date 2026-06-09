package com.forage.dto;

public class DemandeDto {
    private int id;
    private String reference;

    public DemandeDto(int id, String reference) {
        this.id = id;
        this.reference = reference;
    }

    public int getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }
}
