INSERT INTO THEME (idTheme, intitule)VALUES(2001,'intitule1');
INSERT INTO THEME (idTheme, intitule)VALUES(2002,'intitule2');
INSERT INTO THEME (idTheme, intitule)VALUES(2003,'intitule3');
INSERT INTO THEME (idTheme, intitule)VALUES(2004,'intitule4');
INSERT INTO THEME (idTheme, intitule)VALUES(2005,'intitule5');
INSERT INTO THEME (idTheme, intitule)VALUES(2006,'intitule6');
INSERT INTO THEME (idTheme, intitule)VALUES(2007,'intitule7');
INSERT INTO THEME (idTheme, intitule)VALUES(2008,'intitule8');
INSERT INTO THEME (idTheme, intitule)VALUES(2009,'intitule9');
INSERT INTO THEME (idTheme, intitule)VALUES(2010,'intitule10');


INSERT INTO PRESTATAIRE (idPresta, tarifSalle, tarifPause, tarifRepas, adresse) VALUES (1001, 150, 2, 8, '1 bd Jean Jaures');
INSERT INTO PRESTATAIRE (idPresta, tarifSalle, tarifPause, tarifRepas, adresse) VALUES (1002, 50, 2, 8, '27 rue Leon Tolstoi');
INSERT INTO PRESTATAIRE (idPresta, tarifSalle, tarifPause, tarifRepas, adresse) VALUES (1003, 70, 2, 8, '15 rue Emile Zola');
INSERT INTO PRESTATAIRE (idPresta, tarifSalle, tarifPause, tarifRepas, adresse) VALUES (1004, 80, 2, 8, '18 rue Andre Malraux');
INSERT INTO PRESTATAIRE (idPresta, tarifSalle, tarifPause, tarifRepas, adresse) VALUES (1005, 40, 2, 8, '13 rue Victor Hugo');
INSERT INTO PRESTATAIRE (idPresta, tarifSalle, tarifPause, tarifRepas, adresse) VALUES (1006, 300, 2, 8, '15 rue Alexandre Dumas');
INSERT INTO PRESTATAIRE (idPresta, tarifSalle, tarifPause, tarifRepas, adresse) VALUES (1007, 30, 2, 8, '25 rue Albert Camus');
INSERT INTO PRESTATAIRE (idPresta, tarifSalle, tarifPause, tarifRepas, adresse) VALUES (1008, 20, 2, 8, '90 rue Stefan Zweig');
INSERT INTO PRESTATAIRE (idPresta, tarifSalle, tarifPause, tarifRepas, adresse) VALUES (1009, 45, 2, 8, '45 rue Franz Kafka');
INSERT INTO PRESTATAIRE (idPresta, tarifSalle, tarifPause, tarifRepas, adresse) VALUES (1010, 80, 2, 8, '87 rue Fiodor Dostoievski');
    

INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3001, 'Rick','Wright', 0674527466, '13 rue Gig in the Sky' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3002, 'David','Gilmour', 0679827466, '4 rue Coming back to life' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3003, 'Nick','Mason', 0674529566, '65 rue Echoes' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3004, 'Roger','Waters', 0674427466, '14 rue The Wall' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3005, 'Robert','Plant', 0670527466, '19 rue Immigrant song' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3006, 'Jimmy','Page', 0674097466, '54 rue Sterway to Heaven' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3007, 'John','Bonham', 0676527466, '62 rue Kashmir' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3008, 'John-Paul','Jones', 0673427466, '24 rue Black Dog' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3009, 'Eric','Clapton', 0671287466, '47 rue Tears in Heaven' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3010, 'B.B','King', 0674527466, '91 rue Thrill is gone' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3011, 'Jimmy','Hendrix', 0674092746, '96 rue Little Wing' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3018, 'Slash','Slash', 0632877466, '43 rue November rain' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3032, 'Axl','Rose', 0674527474, '87 rue Paradise city' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3044, 'Joe','Satriani', 0674598746, '4 rue Satch Boogie' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3055, 'Deep','Purple', 0674512466, '53 rue Lazy' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3066, 'Dire','Straits', 0674527466, '11 rue Sultans of swing' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3077, 'Gary','Moore', 0424527466, '71 rue One day' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3088, 'Moody','Blues', 0674527536, '91 rue Nights in white satin' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3099, 'Yngwie','Malmsteen', 0674510466, '67 rue Black star' );
INSERT INTO PERSONNE (idPers, prenom, nom, tel, adresse)VALUES(3071, 'Stevie Ray','Vaughan', 0524527466, '1 rue Scuttle Buttin' );


INSERT INTO SEMINAIRE (idSemi, idPresta, idTheme, nbPlaces, prix, idAnimateur, repas, dateSemi, statut, duree) VALUES (0001, 1001, 2001, 10, 20, 3011, 'oui','16-MAR-19', 'confirme', 'matin');
INSERT INTO SEMINAIRE (idSemi, idPresta, idTheme, nbPlaces, prix, idAnimateur, repas, dateSemi, statut, duree) VALUES (0002, 1002, 2002, 80, 30, 3018, 'non', '18-MAR-19', 'annule', 'apres-midi');
INSERT INTO SEMINAIRE (idSemi, idPresta, idTheme, nbPlaces, prix, idAnimateur, repas, dateSemi, statut, duree) VALUES (0003, 1003, 2003, 50, 10, 3032, 'oui', '28-MAR-19', 'non-confirme', 'journee');
INSERT INTO SEMINAIRE (idSemi, idPresta, idTheme, nbPlaces, prix, idAnimateur, repas, dateSemi, statut, duree) VALUES (0004, 1004, 2004, 65, 20, 3044, 'non', '23-APR-19', 'non-confirme', 'matin');
INSERT INTO SEMINAIRE (idSemi, idPresta, idTheme, nbPlaces, prix, idAnimateur, repas, dateSemi, statut, duree) VALUES (0005, 1005, 2005, 25, 25, 3055, 'oui', '13-APR-19', 'non-confirme', 'apres-midi');
INSERT INTO SEMINAIRE (idSemi, idPresta, idTheme, nbPlaces, prix, idAnimateur, repas, dateSemi, statut, duree) VALUES (0006, 1006, 2006, 150, 10, 3066, 'non', '12-MAY-19', 'non-confirme', 'apres-midi');
INSERT INTO SEMINAIRE (idSemi, idPresta, idTheme, nbPlaces, prix, idAnimateur, repas, dateSemi, statut, duree) VALUES (0007, 1007, 2007, 200, 20, 3077, 'oui', '15-MAY-19', 'non-confirme', 'journee');
INSERT INTO SEMINAIRE (idSemi, idPresta, idTheme, nbPlaces, prix, idAnimateur, repas, dateSemi, statut, duree) VALUES (0008, 1008, 2008, 140, 30, 3088, 'non', '18-JUN-19', 'non-confirme', 'apres-midi');
INSERT INTO SEMINAIRE (idSemi, idPresta, idTheme, nbPlaces, prix, idAnimateur, repas, dateSemi, statut, duree) VALUES (0009, 1009, 2009, 130, 50, 3099, 'oui', '27-JUN-19', 'non-confirme', 'matin');
INSERT INTO SEMINAIRE (idSemi, idPresta, idTheme, nbPlaces, prix, idAnimateur, repas, dateSemi, statut, duree) VALUES (0010, 1010, 2010, 60, 20, 3071, 'non', '24-JUL-19', 'non-confirme', 'matin');




INSERT INTO PRESENTATION (idPresentation, idConferencier, titre, montant, fichier) VALUES (4001, 3001, 'titrebidon1', 100, 'bidul1');
INSERT INTO PRESENTATION (idPresentation, idConferencier, titre, montant, fichier) VALUES (4002, 3002, 'titrebidon2', 150, 'bidul2');
INSERT INTO PRESENTATION (idPresentation, idConferencier, titre, montant, fichier) VALUES (4003, 3003, 'titrebidon3', 200, 'bidul3');
INSERT INTO PRESENTATION (idPresentation, idConferencier, titre, montant, fichier) VALUES (4004, 3004, 'titrebidon4', 100, 'bidul4');
INSERT INTO PRESENTATION (idPresentation, idConferencier, titre, montant, fichier) VALUES (4005, 3005, 'titrebidon5', 150, 'bidul5');
INSERT INTO PRESENTATION (idPresentation, idConferencier, titre, montant, fichier) VALUES (4006, 3006, 'titrebidon6', 200, 'bidul6');
INSERT INTO PRESENTATION (idPresentation, idConferencier, titre, montant, fichier) VALUES (4007, 3007, 'titrebidon7', 100, 'bidul7');
INSERT INTO PRESENTATION (idPresentation, idConferencier, titre, montant, fichier) VALUES (4008, 3008, 'titrebidon8', 150, 'bidul8');
INSERT INTO PRESENTATION (idPresentation, idConferencier, titre, montant, fichier) VALUES (4009, 3009, 'titrebidon9', 200, 'bidul9');
INSERT INTO PRESENTATION (idPresentation, idConferencier, titre, montant, fichier) VALUES (4010, 3010, 'titrebidon10', 200, 'bidul10');
    
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3001,'conferencier','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3002,'conferencier','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3003,'conferencier','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3004,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3005,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3006,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3007,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3008,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3009,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3010,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3011,'animateur','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3018,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3032,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3044,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3055,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3066,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3077,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3088,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3099,'inscrit','16-MAR-19');
INSERT INTO PARTICIPANT (idSemi, idPers, statut, dateResa) VALUES    (0001,3071,'inscrit','16-MAR-19');


INSERT INTO ACTIVITE (idActivite, idSemi, numero) VALUES (5001,0001, 1);
INSERT INTO ACTIVITE (idActivite, idSemi, numero) VALUES (5002,0001, 2);
INSERT INTO ACTIVITE (idActivite, idSemi, numero) VALUES (5003,0001, 3);
INSERT INTO ACTIVITE (idActivite, idSemi, numero) VALUES (5004,0003, 1);
INSERT INTO ACTIVITE (idActivite, idSemi, numero) VALUES (5005,0003, 2);
INSERT INTO ACTIVITE (idActivite, idSemi, numero) VALUES (5006,0003, 3);
INSERT INTO ACTIVITE (idActivite, idSemi, numero) VALUES (5007,0003, 4);
INSERT INTO ACTIVITE (idActivite, idSemi, numero) VALUES (5008,0003, 5);
INSERT INTO ACTIVITE (idActivite, idSemi, numero) VALUES (5009,0003, 6);
INSERT INTO ACTIVITE (idActivite, idSemi, numero) VALUES (5010,0004, 1);
    
INSERT INTO EST_PRESENTE (idActivite, idPresentation) VALUES (5001,4001);
INSERT INTO EST_PRESENTE (idActivite, idPresentation) VALUES (5002,4002);
INSERT INTO EST_PRESENTE (idActivite, idPresentation) VALUES (5003,4003);
INSERT INTO EST_PRESENTE (idActivite, idPresentation) VALUES (5004,4004);
INSERT INTO EST_PRESENTE (idActivite, idPresentation) VALUES (5005,4005);
INSERT INTO EST_PRESENTE (idActivite, idPresentation) VALUES (5006,4006);
INSERT INTO EST_PRESENTE (idActivite, idPresentation) VALUES (5007,4007);
INSERT INTO EST_PRESENTE (idActivite, idPresentation) VALUES (5008,4008);
INSERT INTO EST_PRESENTE (idActivite, idPresentation) VALUES (5009,4009);
INSERT INTO EST_PRESENTE (idActivite, idPresentation) VALUES (5010,4010);