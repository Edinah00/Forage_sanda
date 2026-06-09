DROP DATABASE Forage;
CREATE DATABASE Forage;
USE Forage;

-- Création de la table Demande
CREATE TABLE Demande (
    id INT PRIMARY KEY AUTO_INCREMENT,
    reference VARCHAR(255),
    idClient INT,
    dateDemande DATE,
    lieu VARCHAR(255),
    idCommune INT
);
CREATE TABLE Client (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255),
    adresse VARCHAR(255),
    contact VARCHAR(255)
);
CREATE TABLE Commune(
    id INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(255),
    idDistrict VARCHAR(255)
);
CREATE TABLE District(
    id INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(255),
    idRegion VARCHAR(255)
);
CREATE TABLE Region(
    id INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(255),
    
);

CREATE TABLE Statut (
    id INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(255),
    sigle VARCHAR(50)
);

-- Création de la table StatutDemande (Table de liaison)
CREATE TABLE StatutDemande (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idDemande INT,
    idStatut INT,
    dateStatut DATETIME,
    observations TEXT,
    dureeTravaille DECIMAL(10, 2),
    CONSTRAINT fk_demande FOREIGN KEY (idDemande) REFERENCES Demande(id),
    CONSTRAINT fk_statut FOREIGN KEY (idStatut) REFERENCES Statut(id)
);
CREATE TABLE TypeDevis (
    id INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(255)
);
CREATE TABLE Devis (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idDemande INT,
    idType INT,
    createdAt DATETIME,
    observations TEXT,
    CONSTRAINT fk_demandedevis FOREIGN KEY (idDemande) REFERENCES Demande(id),
    CONSTRAINT fk_type FOREIGN KEY (idType) REFERENCES TypeDevis(id)
);
CREATE TABLE Parametre (
    id INT PRIMARY KEY AUTO_INCREMENT,
    idStatut1 INT,
    idStatut2 INT,
    duree INT,
    alerte VARCHAR(30)
);
CREATE TABLE DevisDetail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(255),
    quantite DECIMAL(10, 2),
    prixUnitaire DECIMAL(10, 2),
    idDevis INT,
    CONSTRAINT fk_devis FOREIGN KEY (idDevis) REFERENCES Devis(id)
);

INSERT INTO Statut (libelle, sigle) VALUES 
('Demande créée', 'C'),
('Devis Étude créée', 'DEC'),
('Devis Étude refusé', 'DER'),
('Devis forage créé', 'DFC'),
('Devis forage refusé', 'DFR');


INSERT INTO TypeDevis (libelle) VALUES
('Forage'),
('Étude');

