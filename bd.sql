drop table est_presente;
drop table activite;
drop table participant;
drop table presentation;
drop table seminaire;
drop table personne;
drop table prestataire;
drop table theme;


CREATE TABLE THEME(
    idTheme NUMBER,
    intitule VARCHAR2(255),
    PRIMARY KEY(idTheme)
);


CREATE TABLE PRESTATAIRE(
    idPresta NUMBER,
    tarifSalle NUMBER,
    tarifPause NUMBER,
    tarifRepas NUMBER,
    adresse VARCHAR2(255),
    PRIMARY KEY (idPresta)
);



CREATE TABLE PERSONNE(
    idPers NUMBER,
    prenom VARCHAR2(255),
    nom VARCHAR2(255),
    tel NUMBER(10),
    email VARCHAR2(255),
    adresse VARCHAR2(255),
    PRIMARY KEY (idPers),
    CONSTRAINT ch_mail check (email like ('[A-Z0-9._%-]+@[A-Z0-9._%-]+\.[A-Z]{2,4}'))
);


CREATE TABLE SEMINAIRE(
    idSemi NUMBER,
    idPresta NUMBER,
    idTheme NUMBER,
    nbPlace NUMBER,
    prix NUMBER(6,2),
    idAnimateur NUMBER,
    repas VARCHAR2(3),
    dateSemi DATE,
    statut VARCHAR2(10),
    duree VARCHAR2(12),
    PRIMARY KEY (idSemi),
    CONSTRAINT fk_1 FOREIGN KEY (idAnimateur) REFERENCES PERSONNE(idPers),
    CONSTRAINT fk_2 FOREIGN KEY (idTheme) REFERENCES THEME(idTheme),
    CONSTRAINT fk_3 FOREIGN KEY (idPresta) REFERENCES PRESTATAIRE(idPresta),
    CONSTRAINT ch_duree CHECK (duree IN ('matin','apres-midi','journee')),
    CONSTRAINT ch_repas CHECK (repas IN ('oui','non')),
    CONSTRAINT ch_statut CHECK (statut IN ('non-confirme','confirme','annule'))
);


CREATE TABLE PRESENTATION(
    idPresentation NUMBER,
    idConferencier NUMBER,
    titre VARCHAR2(255),
    montant NUMBER,
    fichier VARCHAR2(255),
    PRIMARY KEY(idPresentation),
    CONSTRAINT fk_4 FOREIGN KEY(idConferencier) REFERENCES PERSONNE(idPers)
);

CREATE TABLE PARTICIPANT(
    idSemi NUMBER,
    idPers NUMBER,
    statut VARCHAR2(12),
    CONSTRAINT fk_5 FOREIGN KEY(idSemi) REFERENCES SEMINAIRE(idSemi),
    CONSTRAINT fk_6 FOREIGN KEY(idPers) REFERENCES PERSONNE(idPers),
    PRIMARY KEY(idSemi,idPers),
    CONSTRAINT ch_statut2 CHECK (statut IN ('inscrit','en attente', 'conferencier', 'animateur'))
);


CREATE TABLE ACTIVITE(
    idActivite NUMBER,
    idSemi NUMBER,
    numero NUMBER,
    PRIMARY KEY(idActivite),
    CONSTRAINT fk_7 FOREIGN KEY(idSemi) REFERENCES SEMINAIRE(idSemi),
    CONSTRAINT U UNIQUE(numero,idSemi),
    CONSTRAINT ch_numero CHECK (numero BETWEEN 1 AND 6)
);

CREATE TABLE EST_PRESENTE(
    idActivite NUMBER,
    idPresentation NUMBER,
    CONSTRAINT fk_8 FOREIGN KEY(idActivite) REFERENCES ACTIVITE(idActivite),
    CONSTRAINT fk_9 FOREIGN KEY(idPresentation) REFERENCES PRESENTATION(idPresentation),
    PRIMARY KEY(idActivite,idPresentation)
);
