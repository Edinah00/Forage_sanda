INSERT INTO Client (nom, adresse, contact) VALUES 
('Jean Dupont', 'Rue 10, Dakar', '771234567'),
('Marie Fall', 'Quartier Escale, Mbour', '789876543'),
('Société Agro-Plus', 'Zone Industrielle, Thiès', '338001122'),
('Association Forage-Vie', 'Village Nord, Podor', '705554433');

INSERT INTO Region (libelle) VALUES 
('Dakar'),
('Thiès'),
('Saint-Louis'),
('Ziguinchor');

INSERT INTO District (libelle, idRegion) VALUES 
('Dakar Plateau', '1'),
('Rufisque', '1'),
('Mbour', '2'),
('Tivaouane', '2'),
('Podor', '3'),
('Oussouye', '4');

INSERT INTO Commune (libelle, idDistrict) VALUES 
('Gorce', '1'),
('Yoff', '1'),
('Saly Portudal', '3'),
('Somone', '3'),
('Ndioum', '5'),
('Cap Skirring', '6');

INSERT INTO Statut (libelle,sigle) VALUES 
('Demande créée','DC'),
('Devis Étude crée','DEC'),
('Devis Étude refusé','DER'),
('Devis forage crée','DFC'),
('Devis forage refusé','DFR');

INSERT INTO TypeDevis (libelle) VALUES 
('Etude'),
('Forage');

INSERT INTO Parametre (idStatut1,idStatut2,duree,alerte) VALUES 
(1,2,960,'rouge'),
(1,2,1440,'vert'),
(2,4,960,'orange'),
(1,4,1440,'bleu');
