-- 1) Dans Seminaire : pas plus de 3 dates identiques. + enregistrée 1 mois à l'avance

create or replace trigger contrainte_1 before
    update or insert on Seminaire for each row
        declare cnt number;
        begin
            SELECT count(idSemi) into cnt from Seminaire WHERE dateSemi = :new.dateSemi;
            IF((cnt>=3) OR (:new.dateSemi<add_months(sysdate, 1))) THEN
                RAISE_APPLICATION_ERROR (-20100, 'date trop proche ou overbookee');
            end IF;
        end;
/
show errors

    
-- 2) Dans Activite : SI (Seminaire.duree = ‘journee’)
-- ALORS count(idActivité)=6
-- SINON count(idActivite)=3

create or replace procedure contrainte_2 (Sem_idSemi IN number) IS
    begin
        declare cnt number; d number;
        begin
            SELECT count(idActivite) into cnt from Activite where idSemi = Sem_idSemi;
            SELECT duree into d from Seminaire where idSemi = Sem_idSemi;
            IF((d='journee' AND cnt!=6) OR (d!='journee' AND cnt!=3)) THEN
                RAISE_APPLICATION_ERROR (-20100, 'problème activités');
            end IF;
        end;
    end;
/
show errors

create or replace trigger contrainte_2 after
    insert on Seminaire for each row
        declare l_job_id binary_integer;
        begin
            dbms_job.submit(
                job  => l_job_id,
                what => 'contrainte_2(:new.idSemi);',
                next_date => add_months(:new.dateSemi, -1)
            );
            dbms_Output.put_line('Job_id ='||To_Char(l_job_id));
        end;
/
show errors


-- 4) Dans Seminaire : durée et repas doivent être complétés 1 mois avant Date

create or replace procedure contrainte_4 (Sem_idSemi IN number) IS
    begin
        declare R varchar2(3); D varchar2(11);
        begin
            SELECT duree into D from Seminaire where idSemi = Sem_idSemi;
            SELECT repas into R from Seminaire where idSemi = Sem_idSemi;
            IF((D=NULL) OR (R=NULL)) THEN
                RAISE_APPLICATION_ERROR (-20100, 'problème de planning');
            end IF;
        end;
    end;
/
show errors

create or replace trigger contrainte_4 after
    insert on seminaire for each row
        declare l_job_id binary_integer;
        begin
            dbms_job.submit(
                job => l_job_id,
                what => 'contrainte_4(:new.idSemi);',
                next_date => add_months(:new.dateSemi, -1)
            );
            dbms_Output.put_line('Job_id ='||To_Char(l_job_id));
        end;
/
show errors


-- 6) Dans Presentation : Titre et Montant 1 mois avant Seminaire.Date

create or replace procedure contrainte_6 (pres_idPres IN number) IS
    begin
        declare T varchar(255); M number(6,2);
        begin
            SELECT montant into M from Presentation where idPresentation = pres_idPres;
            SELECT titre into T from Presentation where idPresentation = pres_idPres;
            IF((T=NULL) OR (M=NULL)) THEN
                RAISE_APPLICATION_ERROR (-20100, 'problème présentation');
            end IF;
        end;
    end;
/
show errors

create or replace trigger contrainte_6 after
    insert on seminaire for each row
        declare l_job_id binary_integer;
        begin
            dbms_job.submit(
                job => l_job_id,
                what => 'contrainte_4(:new.idSemi);',
                next_date => add_months(:new.dateSemi, -1)
            );
            dbms_Output.put_line('Job_id ='||To_Char(l_job_id));
        end;
/
show errors


-- 7) Dans Presentation : Fichier 2 semaine avant Seminaire.Date

create or replace procedure contrainte_7 (pres_idPres IN number) IS
    begin
        declare F varchar(255);
        begin
            SELECT fichier into F from Presentation where idPresentation = pres_idPres;
            IF(F=NULL) THEN
                RAISE_APPLICATION_ERROR (-20100, 'problème présentation');
            end IF;
        end;
    end;
/
show errors

create or replace trigger contrainte_7 after
    insert on Seminaire for each row
        declare l_job_id binary_integer;
        begin
            dbms_job.submit(
                job => l_job_id,
                what => 'contrainte_4(:new.idSemi);',
                next_date => :new.dateSemi - 14
            );
            dbms_output.put_line('Job_id ='||To_Char(l_job_id));
        end;
/
show errors


-- 10) Dans participants : le nombre de participants (exit du staff) < Seminaire.nbPlaces

create or replace trigger contrainte_10 before
update or insert on Participant for each row
    declare cnt number; nb number;
    begin
        SELECT count(idPers) into cnt from Participant WHERE idSemi = :new.idSemi AND statut='inscrit';
        SELECT nbPlaces into nb from Seminaire where :new.idSemi = idSemi;
            IF(cnt>=nb) THEN
                UPDATE Participant SET statut='en attente' where idPers = :new.idPers;
            end IF;
        end;
/
show errors


-- 11)TRIGGER d’annulation / confimation probablement à gérer au niveau de l'application
   
create or replace procedure contrainte_11 (Sem_idSemi IN number) IS
    begin
        declare cnt number; nb number;
        begin
            SELECT count(idPers) into cnt FROM Participant join Seminaire on (Seminaire.idSemi = Sem_idSemi AND Participant.idSemi = Sem_idSemi AND Participant.statut = 'inscrit');
            SELECT nbPlaces into nb FROM Seminaire where Sem_idSemi = idSemi;
            IF(cnt<=nb/2) THEN
                UPDATE Seminaire SET statut='annule' where idSemi = Sem_idSemi;
            end IF;
            IF(cnt>=nb/2) THEN 
                UPDATE Seminaire SET statut='confirme' where idSemi = Sem_idSemi;
            end IF;
        end;
    end;
/
show errors

create or replace trigger contrainte_11 after
    insert on Seminaire for each row
        declare l_job_id binary_integer;
        begin     
            dbms_job.submit(
                job => l_job_id,
                what => 'contrainte_4(:new.idSemi);',
                next_date => :new.dateSemi -1
            );
            dbms_output.put_line('Job_id ='||To_Char(l_job_id));
        end;
/
show errors


-- 12) Dans participant : lors d’un désistement, changer un statut ‘en attente’ vers ‘inscrit’

Create or replace trigger contrainte_12 after
delete on Participant for each row
    declare pers number;
    begin
        SELECT idPers into pers from Participant where statut='en attente' AND dateResa = (SELECT MIN(dateResa) from Participant where statut='en attente');
        IF (pers != NULL) THEN
            UPDATE Participant SET statut='inscrit' where idPers = pers;
        end IF;
    end;
/
