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

INSERT INTO Statut (id, libelle, sigle) VALUES
(1, 'Demande créée',           'DC'),
(2, 'Devis Étude créé',        'DEC'),
(3, 'Devis Étude terminé',     'DET'),
(4, 'Devis Étude refusé',      'DER'),
(5, 'Devis Forage créé',       'DFC'),
(6, 'Devis Forage terminé',    'DFT'),
(7, 'Devis Forage refusé',     'DFR');

INSERT INTO TypeDevis (libelle) VALUES 
('Etude'),
('Forage');

-- Étape DC → DEC (délai pour créer le devis étude)
INSERT INTO Parametre (idStatut1, idStatut2, dureeMin, dureeMax, alerte) VALUES
(1, 2,    0,  480, 'vert'),    -- jusqu'à 8h  : ok
(1, 2,  480,  960, 'orange'),  -- 8h à 16h   : attention
(1, 2,  960, 9999, 'rouge');   -- au-delà 16h : critique

-- Étape DEC → DET (délai pour terminer le devis étude)
INSERT INTO Parametre (idStatut1, idStatut2, dureeMin, dureeMax, alerte) VALUES
(2, 3,    0,  960, 'vert'),
(2, 3,  960, 1440, 'orange'),
(2, 3, 1440, 9999, 'rouge');

-- Étape DET → DFC (délai pour créer le devis forage)
INSERT INTO Parametre (idStatut1, idStatut2, dureeMin, dureeMax, alerte) VALUES
(3, 5,    0,  480, 'vert'),
(3, 5,  480,  960, 'orange'),
(3, 5,  960, 9999, 'rouge');

-- Étape globale DC → DFT (durée totale jusqu'à forage terminé)
INSERT INTO Parametre (idStatut1, idStatut2, dureeMin, dureeMax, alerte) VALUES
(1, 6,    0, 2880, 'vert'),    -- jusqu'à 48h
(1, 6, 2880, 4320, 'orange'),  -- 48h à 72h
(1, 6, 4320, 9999, 'rouge');   -- au-delà 72h