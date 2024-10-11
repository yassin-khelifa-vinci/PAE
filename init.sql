DROP SCHEMA IF EXISTS pae CASCADE;
CREATE SCHEMA pae;

CREATE TABLE pae.users (
                           idUser SERIAL PRIMARY KEY,
                           lastName TEXT,
                           firstName TEXT,
                           email TEXT,
                           password TEXT,
                           phoneNumber TEXT,
                           registrationDate TEXT,
                           role TEXT,
                           schoolYear TEXT,
                           versionNumber INTEGER
);

CREATE TABLE pae.enterprises (
                                 idEnterprise SERIAL PRIMARY KEY,
                                 tradeName TEXT,
                                 designation TEXT,
                                 phoneNumber TEXT,
                                 email TEXT,
                                 isBlacklisted BOOLEAN,
                                 blacklistedReason TEXT,
                                 street TEXT,
                                 streetNumber TEXT,
                                 postalCode TEXT,
                                 city TEXT,
                                 country TEXT,
                                 versionNumber INTEGER
);

CREATE TABLE pae.contacts (
                              idContact SERIAL PRIMARY KEY,
                              userId INTEGER REFERENCES pae.users(idUser),
                              enterprise INTEGER REFERENCES pae.enterprises(idEnterprise),
                              contactStatus TEXT,
                              meetingPlace TEXT,
                              reasonForRefusal TEXT,
                              schoolYear TEXT,
                              versionNumber INTEGER
);

CREATE TABLE pae.responsables_stages(
                                        responsableId SERIAL PRIMARY KEY,
                                        lastName TEXT,
                                        firstName TEXT,
                                        email TEXT,
                                        phoneNumber TEXT,
                                        enterprise INTEGER REFERENCES pae.enterprises(idEnterprise)
);

CREATE TABLE pae.stages(
                           idStage SERIAL PRIMARY KEY,
                           userId INTEGER REFERENCES pae.users(idUser),
                           internshipProject TEXT,
                           internshipSupervisor INTEGER REFERENCES pae.responsables_stages(responsableId),
                           contact INTEGER REFERENCES pae.contacts(idContact),
                           signatureDate TEXT,
                           versionNumber INTEGER
);



-- Insertions pour les entreprises
INSERT INTO pae.enterprises (tradeName, designation, phoneNumber, email, street, streetNumber, postalCode, city, country, versionNumber)
VALUES
    ('Assyst Europe', NULL, '02.609.25.00', NULL, 'Avenue du Japon', '1/B9', '1420', 'Braine-l''Alleud', NULL, 1),
    ('AXIS SRL', NULL, '02 752 17 60', NULL, 'Avenue de l''Hélianthe', '63', '1180', 'Uccle', NULL, 1),
    ('Infrabel', NULL, '02 525 22 11', NULL, 'Rue Bara', '135', '1070', 'Bruxelles', NULL, 1),
    ('La route du papier', NULL, '02 586 16 65', NULL, 'Avenue des Mimosas', '83', '1150', 'Woluwe-Saint-Pierre', NULL, 1),
    ('LetsBuild', NULL, '014 54 67 54', NULL, 'Chaussée de Bruxelles', '135A', '1310', 'La Hulpe', NULL, 1),
    ('Niboo', NULL, '0487 02 79 13', NULL, 'Boulevard du Souverain', '24', '1170', 'Watermael-Boisfort', NULL, 1),
    ('Sopra Steria', NULL, '02 566 66 66', NULL, 'Avenue Arnaud Fraiteur', '15/23', '1050', 'Bruxelles', NULL, 1),
    ('The Bayard Partnership', NULL, '02 309 52 45', NULL, 'Grauwmeer', '1/57 bte 55', '3001', 'Leuven', NULL, 1);

-- Insertions pour les responsables de stage
INSERT INTO pae.responsables_stages (lastName, firstName, email, phoneNumber, enterprise)
VALUES
    ('Dossche', 'Stéphanie', 'stephanie.dossche@letsbuild.com', '014.54.67.54', 5),
    ('Alvarez Corchete', 'Roberto', NULL, '02.566.60.14', 7),
    ('Assal', 'Farid', 'f.assal@assyst-europe.com', '0474 39 69 09', 1),
    ('Ile', 'Emile', NULL, '0489 32 16 54', 4),
    ('Hibo', 'Owln', NULL, '0456 678 567', 3),
    ('Barn', 'Henri', NULL, '02 752 17 60', 2);

-- Insertions pour les utilisateurs
INSERT INTO pae.users (lastName, firstName, email, phoneNumber,password, role, registrationDate, schoolYear, versionNumber)
VALUES
    ('Baroni', 'Raphaël', 'raphael.baroni@vinci.be', '0481 01 01 01', '$2a$12$hyuW/.zIP/cAPJkuBj11KeKIfWql7gcUp9yY7TUrBDRsfav0L5rIK','TEACHER', '21/09/20', '2020-2021', 1),
    ('Lehmann', 'Brigitte', 'brigitte.lehmann@vinci.be', '0482 02 02 02', '$2a$12$hyuW/.zIP/cAPJkuBj11KeKIfWql7gcUp9yY7TUrBDRsfav0L5rIK','TEACHER', '21/09/20', '2020-2021', 1),
    ('Leleux', 'Laurent', 'laurent.leleux@vinci.be', '0483 03 03 03','$2a$12$hyuW/.zIP/cAPJkuBj11KeKIfWql7gcUp9yY7TUrBDRsfav0L5rIK', 'TEACHER', '21/09/20', '2020-2021', 1),
    ('Lancaster', 'Annouck', 'annouck.lancaster@vinci.be', '0484 04 04 04','$2a$12$TXa5vqwx0uzt8vdzUV4nvO6s0JrMGKOqLABc7ust8UXvzb4aLKGle', 'ADMINISTRATIVE', '21/09/20', '2020-2021', 1),
    ('Skile', 'Elle', 'elle.skile@student.vinci.be', '0491 00 00 01','$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.',  'STUDENT', '21/09/21', '2021-2022', 1),
    ('Ilotie', 'Basile', 'basile.Ilotie@student.vinci.be', '0491 00 00 11','$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.',  'STUDENT', '21/09/21', '2021-2022', 1),
    ('Frilot', 'Basile', 'basile.frilot@student.vinci.be', '0491 00 00 21','$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.',  'STUDENT', '21/09/21', '2021-2022', 1),
    ('Ilot', 'Basile', 'basile.Ilot@student.vinci.be', '0492 00 00 01', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '21/09/21', '2021-2022', 1),
    ('Dito', 'Arnaud', 'arnaud.dito@student.vinci.be', '0493 00 00 01', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '21/09/21', '2021-2022', 1),
    ('Dilo', 'Arnaud', 'arnaud.dilo@student.vinci.be', '0494 00 00 01', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '21/09/21', '2021-2022', 1),
    ('Dilot', 'Cedric', 'cedric.dilot@student.vinci.be', '0495 00 00 01', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '21/09/21', '2021-2022', 1),
    ('Linot', 'Auristelle', 'auristelle.linot@student.vinci.be', '0496 00 00 01', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '21/09/21', '2021-2022', 1),
    ('Demoulin', 'Basile', 'basile.demoulin@student.vinci.be', '0496 00 00 02', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '23/09/22', '2022-2023', 1),
    ('Moulin', 'Arthur', 'arthur.moulin@student.vinci.be', '0497 00 00 02','$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.',  'STUDENT', '23/09/22', '2022-2023', 1),
    ('Moulin', 'Hugo', 'hugo.moulin@student.vinci.be', '0497 00 00 03', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '23/09/22', '2022-2023', 1),
    ('Demoulin', 'Jeremy', 'jeremy.demoulin@student.vinci.be', '0497 00 00 20','$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.',  'STUDENT', '23/09/22', '2022-2023', 1),
    ('Mile', 'Aurèle', 'aurele.mile@student.vinci.be', '0497 00 00 21', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '23/09/22', '2022-2023', 1),
    ('Mile', 'Frank', 'frank.mile@student.vinci.be', '0497 00 00 75', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '27/09/22', '2022-2023', 1),
    ('Dumoulin', 'Basile', 'basile.dumoulin@student.vinci.be', '0497 00 00 58', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '27/09/22', '2022-2023', 1),
    ('Dumoulin', 'Axel', 'axel.dumoulin@student.vinci.be', '0497 00 00 97', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '27/09/22', '2022-2023', 1),
    ('Line', 'Caroline', 'caroline.line@student.vinci.be', '0486 00 00 01', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '18/09/23', '2023-2024', 1),
    ('Ile', 'Achille', 'ach.ile@student.vinci.be', '0487 00 00 01', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '18/09/23', '2023-2024', 1),
    ('Ile', 'Basile', 'basile.Ile@student.vinci.be', '0488 00 00 01', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '18/09/23', '2023-2024', 1),
    ('Skile', 'Achille', 'achille.skile@student.vinci.be', '0490 00 00 01', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '18/09/23', '2023-2024', 1),
    ('Skile', 'Carole', 'carole.skile@student.vinci.be', '0489 00 00 01', '$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.', 'STUDENT', '18/09/23', '2023-2024', 1),
    ('Ile', 'Théophile', 'theophile.ile@student.vinci.be', '0488 35 33 89','$2a$12$1EOdxz37tw3v7QUrJL1BZu3YrM0if0D5fEW6V5xTUZ5T50W0UM5X.',  'STUDENT', '01/03/24', '2023-2024', 1);

-- Insertions pour les contacts de stage
INSERT INTO pae.contacts (userId, enterprise, contactStatus, meetingPlace, reasonForRefusal, schoolYear, versionNumber)
VALUES
    -- Année académique 2023-2024
    (25, 5, 'ACCEPTED', 'A distance', NULL, '2023-2024', 1),
    (22, 7, 'ACCEPTED', 'Dans l''entreprise', NULL, '2023-2024', 1),
    (22, 6, 'TURNED_DOWN', NULL, 'N''ont pas accepté d''avoir un entretien', '2023-2024', 1),
    (23, 1, 'ACCEPTED', 'Dans l''entreprise', NULL, '2023-2024', 1),
    (23, 5, 'ON_HOLD', NULL, NULL, '2023-2024', 1),
    (23, 7, 'ON_HOLD', NULL, NULL, '2023-2024', 1),
    (23, 6, 'TURNED_DOWN', 'Dans l''entreprise', 'Ne prennent qu''un seul étudiant', '2023-2024', 1),
    (21, 6, 'TURNED_DOWN', 'A distance', 'Pas d’affinité avec le l’ERP Odoo', '2023-2024', 1),
    (21, 7, 'UNSUPERVISED', NULL, NULL, '2023-2024', 1),
    (21, 5, 'ADMITTED', 'A distance', NULL, '2023-2024', 1),
    (26, 7, 'STARTED', NULL, NULL, '2023-2024', 1),
    (26, 6, 'STARTED', NULL, NULL, '2023-2024', 1),
    (26, 5, 'STARTED', NULL, NULL, '2023-2024', 1),
    (24, 7, 'STARTED', NULL, NULL, '2023-2024', 1),

    -- Année académique 2021-2022
    (5, 4, 'ACCEPTED', 'A distance', null, '2021-2022', 1),
    (8, 7, 'UNSUPERVISED', NULL, NULL, '2021-2022', 1),
    (7, 8, 'TURNED_DOWN', 'A distance', 'Ne prennent pas de stage', '2021-2022', 1),
    (9, 7, 'ACCEPTED', 'Dans l''entreprise', null, '2021-2022', 1),
    (10, 7, 'ACCEPTED', 'Dans l''entreprise', null, '2021-2022', 1),
    (11, 1, 'ACCEPTED', 'Dans l''entreprise', null, '2021-2022', 1),
    (11, 7, 'TURNED_DOWN', 'Dans l''entreprise', 'Choix autre étudiant', '2021-2022', 1),
    (12, 3, 'ACCEPTED', 'A distance', null, '2021-2022', 1),
    (12, 7, 'ON_HOLD', NULL, NULL, '2021-2022', 1),
    (12, 6, 'TURNED_DOWN', 'A distance', 'Choix autre étudiant', '2021-2022', 1),

    -- Année académique 2022-2023
    (16, 1, 'ACCEPTED', 'A distance', null, '2022-2023', 1),
    (14, 2, 'ACCEPTED', 'Dans l''entreprise', null, '2022-2023', 1),
    (15, 2, 'ACCEPTED', 'Dans l''entreprise', null, '2022-2023', 1),
    (17, 2, 'ACCEPTED', 'A distance', null, '2022-2023', 1),
    (18, 2, 'ACCEPTED', 'A distance', null, '2022-2023', 1),
    (19, 2, 'TURNED_DOWN', 'Dans l''entreprise', 'Entretien n''a pas eu lieu', '2022-2023', 1),
    (19, 6, 'TURNED_DOWN', 'Dans l''entreprise', 'Entretien n''a pas eu lieu', '2022-2023', 1),
    (19, 7, 'TURNED_DOWN', 'A distance', 'Entretien n''a pas eu lieu', '2022-2023', 1),
    (20, 7, 'ACCEPTED', 'A distance', null, '2022-2023', 1),
    (7, 7, 'TURNED_DOWN', 'A distance', 'Choix autre étudiant', '2022-2023', 1);


-- Insertions pour les stages
-- Année académique 2023-2024
INSERT INTO pae.stages (userId, internshipProject, internshipSupervisor, contact, signatureDate, versionNumber)
VALUES
    (25, 'Un ERP : Odoo', 1, 1, '2023-10-10', 1),
    (22, 'sBMS project - a complex environment', 2, 2, '2023-11-23', 1),
    (23, 'CRM : Microsoft Dynamics 365 For Sales', 3, 4, '2023-10-12', 1);

-- Année académique 2021-2022
INSERT INTO pae.stages (userId, internshipProject, internshipSupervisor, contact, signatureDate, versionNumber)
VALUES
    (5, 'Conservation et restauration d’œuvres d’art', 4, 15, '2021-11-25', 1),
    (9, 'L''analyste au centre du développement', 2, 18, '2021-11-17', 1),
    (10, 'L''analyste au centre du développement', 2, 19, '2021-11-17', 1),
    (11, 'ERP : Microsoft Dynamics 366', 3, 20, '2021-11-23', 1),
    (12, 'Entretien des rails', 5, 22, '2021-11-22', 1);

-- Année académique 2022-2023
INSERT INTO pae.stages (userId, internshipProject, internshipSupervisor, contact, signatureDate, versionNumber)
VALUES
    (16, 'CRM : Microsoft Dynamics 365 For Sales', 3, 25, '2022-11-23', 1),
    (14, 'Un métier : chef de projet', 6, 26, '2022-10-19', 1),
    (15, 'Un métier : chef de projet', 6, 27, '2022-10-19', 1),
    (17, 'Un métier : chef de projet', 6, 28, '2022-10-19', 1),
    (18, 'Un métier : chef de projet', 6, 29, '2022-10-19', 1),
    (20, 'sBMS project - Java Development', 2, 33, '2022-10-17', 1);