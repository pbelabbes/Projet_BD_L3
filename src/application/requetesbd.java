package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import connexionsgbd.SQLWarningsExceptions;
import java.util.regex.Pattern;
/*
 * To change this license header, choose License Headers in
Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/***
 * @author 
 */
public class requetesbd {
    
    
	/**
	 * Recherche de l'index d'activité le plus grand
         * table cible Activite, recherche du max dans les idActivite
	 * @param conn de type Connection : connexion a la base de donnees
         * @return tmp : index d'activité le plus grand
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static int act_max(Connection conn) throws SQLException {
		int tmp = 0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT MAX(idActivite) FROM Activite")) {
                while(rs.next()){
                    tmp = rs.getInt(1);
                    System.out.println("Dernier idActivite : " + rs.getInt(1));
                }
            }
	return tmp;
	}
        
        
        /**
	 * Recherche de l'index de séminaire le plus grand
         * table cible : Seminaire, recherche du max dans les idSemi
	 * @param conn de type Connection : connexion a la base de donnees
         * @return tmp : index d'activité le plus grand
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static int semi_max(Connection conn) throws
	SQLException {
		int tmp = 0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT MAX(idSemi) max FROM Seminaire")) {
                if(rs.next()){
                    tmp = rs.getInt(1);
                    System.out.println("Dernier idSemi : " + tmp);
                }
            }
	return tmp;
	}
        
        /**
	 * Recherche de l'index de la derniere personne enregistree 
         * table cible : Personne, recherche du max dans les idPers
	 * @param conn de type Connection : connexion a la base de donnees
         * @return tmp : index d'activité le plus grand
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static int pers_max(Connection conn) throws SQLException {
		int tmp = 0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT MAX(idPers) FROM Personne ")) {
                while(rs.next()){
                    tmp = rs.getInt(1);
                    System.out.println("Dernier idPersonne : " +rs.getInt(1));
                }
            }
	return tmp;
	}
	
        /**
	 * Validation de la date par rapport au trigger de la contrainte_1 
         * a savoir : pas plus de 3 dates identiques + enregistrée 1 mois avant la date du seminaire
         * table cible : Seminaire, trigger sur l'insertion
	 * @param conn de type Connection : connexion a la base de donnees
         * @param dateSemi de type string : date de du seminaire
         * @return boolean : valide la contrainte de date = true sinon false
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static boolean date_valide(Connection conn, String dateSemi) throws SQLException {
		// Execute the query
		try ( // Get a statement from the connection
                        Statement stmt = conn.createStatement()) {
                    // Execute the query
                    try {
                        int idSem = semi_max(conn)+1;
                        stmt.executeQuery("INSERT INTO SEMINAIRE VALUES ("+ idSem +", 1001, 2001, 10, 20, 3011, 'oui', TO_DATE('"+dateSemi+"','dd-mm-yy'), 'confirme', 'matin')");
                    } catch( SQLException se ) {
                        // Print information about SQL exceptions
                        SQLWarningsExceptions.printExceptions(se);
                        conn.rollback();
                        return false;
		}
                conn.rollback();
		// Close the result set, statement and the connection

		}	return true;
	}
	
	
	
	
	/**
	 * récupération de l'adresse d'un prestataire d'apres son ID
         * table cible : Prestataire, recherche l'adresse du prestataire
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idPresta de type int : id du prestataire
         * @return tmp : l'adresse du prestataire
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static String get_adresse_prestataire(Connection conn, int idPresta) throws SQLException {
		String tmp="Adr NC";
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT adresse adr from Prestataire where idPresta="+idPresta)) {
                if(rs.next()) {tmp=rs.getString("adr");}
            }
	return tmp;
	}
        
        /**
	 * récupération de l'intitulé d'un theme d'apres son ID
         * table cible : Theme, recherche de l'intitulé du theme correspondant a l'id
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idTheme de type int : id du theme
         * @return tmp : intitule du theme
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static String get_theme(Connection conn, int idTheme) throws SQLException {
		String tmp="Theme NC";
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT intitule I from Theme where idTheme="+idTheme)) {
                if(rs.next()) {tmp=rs.getString("I");}
            }
	return tmp;
	}
        
        /**
	 * Booléen indiquant s'il reste des places ou non
         * table cible : Seminaire, recherche du nombre de place restante 
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idSemi de type int : id du seminaire
         * @return boolean : reste des places = true sinon false
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static boolean place_restante(Connection conn, int idSemi) throws
	SQLException {
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement()) {
                // Execute the query
                ResultSet rs = stmt.executeQuery("SELECT count(idPers) cnt from Participant where statut='inscrit' AND idSemi="+idSemi);
                if(rs.next()) {
                    if(rs.getInt("cnt")>=get_nbplace(conn, idSemi)) {
                        return false;
                    }
                }
                // Close the result set, statement and the connection
            }
	return true;
	}
        
        /**
	 * Booléen indiquant s'il reste des places ou non
         * table cible : Seminaire, recherche du nombre de place pour un seminaire
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idSemi de type int : id du seminaire
         * @return tmp : nombre de place a un seminaire
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static int get_nbplace(Connection conn, int idSemi) throws SQLException {
		int tmp=0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT nbPlace N from Seminaire where idSemi="+idSemi)) {
                if(rs.next()) {tmp=rs.getInt("N");}
            }
	return tmp;
	}
        
        /**
	 * Update le status la status d'une personne vers inscrit
         * table cible : Seminaire, update le status la status d'une personne vers inscrit
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idSemi de type int : id du seminaire 
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void en_attente_vers_inscrit(Connection conn, int idSemi) throws SQLException {
		int idPers = resa_plus_ancienne(conn, idSemi);
		// Execute the query
		try ( // Get a statement from the connection
                        Statement stmt = conn.createStatement()) {
                    // Execute the query
                    try {
                        stmt.executeUpdate("UPDATE statut='inscrit' where idPers="+idPers+" AND idSemi="+idSemi);
                    } 
                    catch( SQLException se ) {
                        // Print information about SQL exceptions
                        SQLWarningsExceptions.printExceptions(se);
                        conn.rollback();
		}
                conn.commit();
		// Close the result set, statement and the connection

		}	
        }
        
        /**
	 * recherche la resa la plus ancienne dans les participants en attente pour un seminaire
         * table cible : participant, participant avec status en attente le plus ancien
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idSemi de type int : id du seminaire 
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return idP : id de la personne qui a la resa la plus ancienne 
	 */
	public static int resa_plus_ancienne(Connection conn, int idSemi) throws SQLException {
		int idP = 0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT idPers P from Participant where dateResa = (SELECT MIN(dateResa) from Participant where idSemi="+idSemi+" AND statut='en attente')")) {
                if(rs.next()){
                    idP = rs.getInt("P");
                }
            }
		return idP;
	}
	
	
	
	
	
	/**
	 * recherche de l'animateur par son id et renvoi d'un bouléen selon son existance
         * table cible : personne, recherche l'id de l'animateur
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idAnimateur de type int : id de la personne
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return res : boolean avec O de base et si non-existant,  sinon 1
	 */
	public static boolean animateur_existe(Connection conn, int idAnimateur) throws SQLException {
		int res=0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT count(idPers) cnt from Personne where idPers = "+idAnimateur)) {
                if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
            }
	return res==1;
	}
        
        /**
	 * recherche du thème idT et renvoi d'un bouléen selon son existance
         * table cible : Theme, recherche l'id du theme
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idT de type int : id du theme
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return res : boolean avec O de base et si non-existant, sinon 1
	 */
	public static boolean theme_existe(Connection conn, int idT) throws SQLException {
		int res=0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT count(idTheme) cnt from Theme where idTheme = "+idT)) {
                if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
            }
	return res==1;
	}
        
        /**
	 * recherche du prestataire idP et renvoi d'un bouléen selon son existance
         * table cible : Prestataire, recherche l'id du prestataire
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idPresta de type int : id du prestataire
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return res : boolean avec O de base et si non-existant, sinon 1
	 */
	public static boolean prestataire_existe(Connection conn, int idPresta) throws SQLException {
		int res=0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT count(idPresta) cnt from Prestataire where idPresta = "+idPresta)) {
                if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
            }
	return res==1;
	}
        
        /**
	 * recherche de la presentation idP et renvoi d'un bouléen selon son existance
         * table cible : Presentation, recherche l'id de la presentation
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idP de type int : id de la presentation
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return res : boolean avec O de base et si non-existant, sinon 1
	 */
	public static boolean presentation_existe(Connection conn, int idP) throws SQLException {
		int res=0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT count(idPresentation) cnt from Presentation where idPresentation = "+idP)) {
                if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
            }
	return res==1;
	}
        
         /**
	 * recherche du seminaire idS et renvoi d'un bouléen selon son existance
         * table cible : Seminaire, recherche l'id du seminaire
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idS de type int : id du seminaire
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return res : boolean avec O de base et si non-existant, sinon 1
	 */
	public static boolean seminaire_existe(Connection conn, int idS) throws SQLException {
		int res=0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT count(idSemi) cnt from Seminaire where idSemi = "+idS)) {
                if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
            }
	return res==1;
	}
        
        /**
	 * recherche du seminaire idS et renvoi d'un bouléen selon son existance
         * table cible : Personne, recherche l'id de la personne
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idP de type int : id de la personne
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return res : boolean avec O de base et si non-existant, sinon 1
	 */
	public static boolean personne_existe(Connection conn, int idP) throws SQLException {
		int res=0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT count(idPers) cnt from Personne where idPersonne = "+idP)) {
                if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
            }
	return res==1;
	}
        
        /**
	 * recherche si un participant existe pour plusieur seminaires
         * table cible : participant, compte le nombre de seminaire aquel il participe
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idS de type int : id du seminaire
         * @param idP de type int : id de la personne
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return res : boolean avec O de base et si non-existant, sinon 1
	 */
	public static boolean seminaire_personne_existe(Connection conn, int idS, int idP) throws SQLException {
		int res=0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT count(idSemi) cnt from Participant where idSemi="+idS+" AND idPers="+idP)) {
                if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
            }
	return res==1;
	}
	
	
	
	
	
// Vérification de la disponibilité d'une personne (contrainte_? sur les inscriptions multiples)
        /**
	 * recherche si il y a un animateur pour un seminaire
         * table cible : Seminaire, recherche l'id de la personne
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idAn de type int : id de la personne avec status animateur pour un seminaire
         * @param dateSem de type String : date du seminaire
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return res : boolean avec O de base et si non-existant, sinon 1
	 */
	public static boolean animateur_dispo(Connection conn, int idAn, String dateSem) throws SQLException {
		int res=0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT count(idSemi) cnt from Seminaire where idAnimateur = "+idAn+" AND TO_DATE('"+dateSem+"','dd-mm-yy')=dateSemi")) {
                if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
            }
	return res==0;
	}
        
         /**
	 * recherche si il y a un conferencier pour un seminaire
         * table cible : Seminaire, recherche l'id de la personne
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idP de type int : id de la personne avec status animateur pour un seminaire
         * @param dateSem de type String : date du seminaire
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return res : boolean avec O de base et si non-existant, sinon 1
	 */
	public static boolean conferencier_dispo(Connection conn, int idP, String dateSem) throws SQLException {
		int res=0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT count(idPers) cnt from (SELECT idSemi from seminaire where dateSemi=TO_DATE('"+dateSem+"','dd-mm-yy')) S join (SELECT idPers, idSemi from Participant where idPers="+idP+"AND statut='conferencier') P on S.idsemi=P.idSemi")) {
                if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
            }
	return res==0;
	}
	
	
	
	
	
	
	
	
// module d'affichage des tables
        /**
	 * recherche les prestataires
         * table cible : Prestataire
	 * @param conn de type Connection : connexion a la base de donnees
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void afficher_prestataires(Connection conn) throws SQLException {
            // Execute the query
            try ( // Get a statement from the connectionaire
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT * from Prestataire")) {
                while(rs.next()){
                    System.out.print("ID : " + rs.getInt(1));
                    System.out.print(" Tarif salle : " + rs.getDouble(2));
                    System.out.print(" Tarif pause : " + rs.getDouble(3));
                    System.out.print(" Tarif repas : " + rs.getDouble(4));
                    System.out.println(" Adresse : " + rs.getString(5));
                }
            }
	}
        
        /**
	 * recherche les presentations
         * table cible : presentation
	 * @param conn de type Connection : connexion a la base de donnees
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void afficher_presentations(Connection conn) throws SQLException {
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT * from Presentation")) {
                while(rs.next()){
                    System.out.print("ID : " + rs.getInt(1));
                    System.out.print(" Conférencier : " + rs.getString(2));
                    System.out.print(" Titre : '" + rs.getString(3)+"'");
                    System.out.println(" Tarif : " + rs.getDouble(4));
                }
            }
	}
        
        /**
	 * recherche les themes
         * table cible : theme
	 * @param conn de type Connection : connexion a la base de donnees
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void afficher_theme(Connection conn) throws SQLException {
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT * from Theme")) {
                while(rs.next()){
                    System.out.print("ID : " + rs.getInt(1)+" ");
                    System.out.println("Intitulé : " + rs.getString(2));
                }
            }
	}
        
        /**
	 * recherche les seminaires
         * table cible : Seminaire
	 * @param conn de type Connection : connexion a la base de donnees
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void afficher_seminaires(Connection conn) throws SQLException {
            // Execute the query
            try ( // Get a statement from the connectionaire
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT * from Seminaire")) {
                while(rs.next()){
                    System.out.print("ID : " + rs.getInt(1));
                    System.out.print(" Adresse : " + get_adresse_prestataire(conn, rs.getInt(2)));
                    System.out.print(" Theme : " + get_theme(conn, rs.getInt(3)));
                    System.out.print(" Tarif : " + rs.getDouble(5));
                    System.out.print(" Date : " + rs.getDate(8));
                    System.out.println(" Duree : " + rs.getString(10));
                }
            }
	}
        
        /**
	 * recherche les seminaires et leurs participants
         * table cible : Seminaire, Participant
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idP de type int : id des Personne
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void afficher_seminaires_personne(Connection conn, int idP) throws SQLException {
            // Execute the query
            try ( // Get a statement from the connectionaire
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT * from Seminaire natural join (SELECT idSemi from Participant where idPers="+idP+")")) {
                while(rs.next()){
                    System.out.print("ID : " + rs.getInt(1));
                    System.out.print(" Adresse : " + get_adresse_prestataire(conn, rs.getInt(2)));
                    System.out.print(" Theme : " + get_theme(conn, rs.getInt(3)));
                    System.out.print(" Tarif : " + rs.getDouble(5));
                    System.out.print(" Date : " + rs.getDate(8));
                    System.out.println(" Duree : " + rs.getString(10));
                }
            }
	}
	
	
	
	
	
// module d'insertion de n-uplets
        /**
	 * Insertion d'un seminaire
         * table cible : Seminaire
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idPresta de type int : id du prestataire ou se iendra le seminaire
         * @param idTheme de type int : id du theme du seminaire
         * @param nbPlace de type int : nombre de place pour le seminaire
         * @param prix de type double : prix fixe du seminaire
         * @param idAnimateur de type int : id de la personne qui aura le status d'animateur pour ce seminaire
         * @param repas de type string : deux choix possible oui ou non, trigger sur oui si la valeur de duree est journee
         * @param dateSemi de type string : date du seminaire
         * @param duree de type string : rensihne si le seminaire est sur une demi journee ou sur une journee entiere
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return idSem : id du seminaire
	 */
	public static int ajouter_seminaire(Connection conn, int idPresta, int idTheme, int nbPlace, double prix, int idAnimateur, String repas, String dateSemi, String duree) throws SQLException {
            int idSem;
		// Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement()) {
			// Execute the query
                idSem = semi_max(conn)+1;
                try {
                    stmt.executeUpdate("INSERT INTO SEMINAIRE VALUES ("+idSem+","+idPresta+","+idTheme+","+nbPlace+","+prix+","+idAnimateur+",'"+repas+"', TO_DATE('"+dateSemi+"','dd-mm-yy'),'non-confirme','"+duree+"')");
                } catch( SQLException se ) {
                    // Print information about SQL exceptions
                    SQLWarningsExceptions.printExceptions(se);
                    conn.rollback();
                    return -1;
		}
                conn.commit();
		// Close the result set, statement and the connection
		}		return idSem;
	}
        
        /**
	 * Insertion d'une activite
         * table cible : Activite
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idSemi de type int : id du Seminaire ou se deroulera l'activite
         * @param numero de type int : position de l'activite dans le programme du seminaire 
         * @param idPresentation de type int : id de la presentation qui sera faite pendant l'activite
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return idAct : id de l'activite
	 */
	public static int ajouter_activite(Connection conn, int idSemi, int numero, int idPresentation) throws SQLException {
            int idAct;
		// Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement()) {
			// Execute the query
                idAct = act_max(conn)+1;
                try {
                    stmt.executeUpdate("INSERT INTO Activite VALUES ("+idAct+","+idSemi+","+numero+")");
                } catch( SQLException se ) {
                    // Print information about SQL exceptions
                    SQLWarningsExceptions.printExceptions(se);
                    conn.rollback();
                    return -1;
		}
                conn.commit();
		// Close the result set, statement and the connection
		}		return idAct;
	}
        
        /**
	 * Insertion dans Est_Presente qui fait le lien entre Activite et presentation
         * table cible : Est_Presente
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idAct de type int : id de l'activite auquel est lie une presentation
         * @param idP de type int : id de la presentation qui sera faite pendant une activite
         * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void ajouter_est_presente(Connection conn, int idAct, int idP) throws SQLException {
            // Execute the query
            try (Statement stmt = conn.createStatement()) {
                // Execute the query
                stmt.executeUpdate("INSERT INTO Est_presente VALUES ("+idAct+","+idP+")");
                // Close the result set, statement and the connection
            }
	}
        
        /**
	 * Insertion dans Participant
         * table cible : Participant
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idPers de type int : id de la personne a ajouter aux particiants
         * @param idSemi de type int : id du seminaire auquel la personne veut participer
         * @param statut de type string : enumeration sur le statut du participant, animateur, inscrit, en attente...
         * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void ajouter_participant(Connection conn, int idPers, int idSemi, String statut) throws SQLException {
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement()) {
                // Execute the query
                stmt.executeUpdate("INSERT INTO Participant VALUES ("+idSemi+","+idPers+","+statut+", TO_DATE(TO_CHAR(sysdate,'dd-mm-yy'),'dd-mm-yy'))");
                // Close the result set, statement and the connection
            }
	}
        
        /**
	 * Insertion dans Personne
         * table cible : Personne
	 * @param conn de type Connection : connexion a la base de donnees
         * @param prenom de type string : prenom de la personne
         * @param nom de type string : nom de la personne
         * @param tel de type int : telephone de la personne
         * @param email de type string : mail de la personne
         * @param adr de type string : adresse de la personne
         * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return idPers : id de la personne creer dans la base
	 */
	public static int ajouter_personne(Connection conn, String prenom, String nom, int tel, String email, String adr) throws SQLException {
            int idPers;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement()) {
                // Execute the query
                idPers = pers_max(conn)+1;
                stmt.executeUpdate("INSERT INTO Personne VALUES ("+idPers+","+prenom+","+nom+","+tel+","+email+","+adr+")");
                // Close the result set, statement and the connection
                }
            return idPers;
        }
	
	
	
	
	/**
	 * suppression dans Participant
         * table cible : Participant
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idS de type int : id du Seminaire duquel on supprime le participant
         * @param idP de type int : id de la personne a supprimer aux particiants
         * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void retrait_participant(Connection conn, int idS, int idP) throws SQLException {
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement()) {
                // Execute the query
                stmt.executeUpdate("DELETE FROM Participant where idSemi="+idS+" AND idPers ="+idP);
                // Close the result set, statement and the connection
            }
	}
	
	
	
	
	
// Extraction des données pécunières
        /**
	 * recherhche du cout d'une activite
         * table cible : Presentation, Est_Pressente, Activite
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idSemi de type int : id du Seminaire duquel on calcul le cout des activites
         * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return cout : cout des activites
	 */
	public static int cout_activite(Connection conn, int idSemi) throws SQLException {
		int cout = 0;
            // Execute the query
            try (Statement stmt = conn.createStatement()) {
                // Execute the query
                ResultSet rs = stmt.executeQuery("SELECT SUM(montant) tot from Presentation natural join Est_presente natural join Activite where idSemi="+idSemi);
                if(rs.next()) {cout = rs.getInt("tot"); }
                conn.commit();
                // Close the result set, statement and the connection
            }
		return cout;
	}
        
        /**
	 * recherhche du cout d'un repas
         * table cible : Prestataire
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idP de type int : id du prestataire dont on recupere le tarif de repas
         * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return tot : cout d'un repas chez ce prestataire
	 */
	public static int cout_repas(Connection conn, int idP) throws SQLException {
		int tot = 0;
            // Execute the query
            try (Statement stmt = conn.createStatement()) {
                // Execute the query
                ResultSet rs = stmt.executeQuery("SELECT tarifRepas from Prestataire where idPresta="+idP);
                if(rs.next()) {tot = rs.getInt(1); }
                conn.commit();
                // Close the result set, statement and the connection
            }
		return tot;
	}
        
        /**
	 * recherhche du cout d'une pause
         * table cible : Prestataire
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idP de type int : id du prestataire dont on recupere le tarif de pause
         * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return tot : cout d'une pause chez ce prestataire
	 */
	public static int cout_pause(Connection conn, int idP) throws SQLException {
		int tot = 0;
            // Execute the query
            try (Statement stmt = conn.createStatement()) {
                // Execute the query
                ResultSet rs = stmt.executeQuery("SELECT tarifPause from Prestataire where idPresta="+idP);
                if(rs.next()) {tot = rs.getInt(1); }
                conn.commit();
                // Close the result set, statement and the connection
            }
		return tot;
	}
        
        /**
	 * recherhche du cout de la salle
         * table cible : Prestataire
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idP de type int : id du prestataire dont on recupere le tarif de repas
         * @throws SQLException en cas d'erreur d'acces a la base de donnees
         * @return tot : cout de la salle chez ce prestataire
	 */
	public static int cout_salle(Connection conn, int idP) throws SQLException {
		int tot = 0;
            // Execute the query
            try (Statement stmt = conn.createStatement()) {
                // Execute the query
                ResultSet rs = stmt.executeQuery("SELECT tarifSalle from Prestataire where idPresta="+idP);
                if(rs.next()) {tot = rs.getInt(1); }
                conn.commit();
                // Close the result set, statement and the connection
            }
		return tot;
	}
	

/*	public static void employes(Connection conn) throws
	SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement() ;
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT * FROM	emp");
				while(rs.next()){
					System.out.print("empno : " + rs.getInt(1)+" ");
					System.out.print("nom : " + rs.getString(2)+" ");
					System.out.println("job : " + rs.getString(3));
				}
				// Close the result set, statement and the connection
				rs.close();
				stmt.close();
	}
	public static void comm(Connection conn) throws
	SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement() ;
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT empno, sal, nvl(comm, 0) FROM	emp");
				while(rs.next()){
					System.out.print("empno : " + rs.getInt(1)+" ");
					System.out.print("sal : " + rs.getString(2)+" ");
					System.out.println("comm : " + rs.getString(3));
				}
				// Close the result set, statement and the connection
				rs.close();
				stmt.close();
	}
	public static void superieur_hierarchique(Connection conn) throws
	SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement() ;
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT SUB.ENAME EMPLOYE, nvl(CHEF.ENAME, 'big boss') CHEF from EMP SUB left outer join EMP CHEF on (SUB.MGR=CHEF.EMPNO) order by SUB.ENAME");
				while(rs.next()){
					System.out.print("employe : " + rs.getString(1)+" ");
					System.out.println("chef : " + rs.getString(2));
				}
				// Close the result set, statement and the connection
				rs.close();
				stmt.close();
	}
	public static void somme_revenus_dept(Connection conn) throws
	SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement() ;
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT sum(nvl(SAL, 0) + nvl(comm, 0)) REVENU, DEPT.deptno from EMP right outer join DEPT on (EMP.deptno=DEPT.deptno) group by DEPT.deptno order by REVENU desc");
				while(rs.next()){
					System.out.print("revenu : " + rs.getInt(1)+" ");
					System.out.println("departement : " + rs.getInt(2));
				}
				// Close the result set, statement and the connection
				rs.close();
				stmt.close();
	}
	public static void nom_spec(Connection conn) throws
	SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement() ;
		// Execute the query
		Scanner scan =new Scanner(System.in);
		System.out.println("Donnez un numéro de spectacle");
		String nums=scan.nextLine();
		ResultSet rs = stmt.executeQuery("SELECT nomS from LESSPECTACLES where numS="+nums);
			int i = 0;
			while(rs.next()){
				i++;
				System.out.println("nom : " + rs.getString(1));
			}
			if (i==0) {System.out.println("Aucun spectacle associé au numéro "+nums);}

			// Close the result set, statement and the connection
			rs.close();
			stmt.close();
	}
	public static void num_spec(Connection conn) throws
	SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement() ;
		// Execute the query
		Scanner scan =new Scanner(System.in);
		System.out.println("Donnez un nom de spectacle");
		String noms=scan.nextLine().toLowerCase();
		ResultSet rs = stmt.executeQuery("SELECT numS from LESSPECTACLES where lower(nomS) like \'%"+noms+"%\'");
			int i = 0;
			while(rs.next()){
				i++;
				System.out.println("numéro : " + rs.getString(1));
			}
			if (i==0) {System.out.println("Aucun spectacle associé au nom \'"+noms+"\'");}

			// Close the result set, statement and the connection
			rs.close();
			stmt.close();
	}
	public static void representation_num(Connection conn) throws
	SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement() ;
		// Execute the query
		Scanner scan =new Scanner(System.in);
		System.out.println("Donnez un numéro de spectacle");
		String nums=scan.nextLine();
		ResultSet rs1 = stmt.executeQuery("SELECT nomS from LESSPECTACLES where numS="+nums);
			int i = 0;
			while(rs1.next()){
				i++;
				System.out.println("Représentations de "+rs1.getString(1) + " :");
			}
			if (i==0) {System.out.println("Aucun spectacle associé au numéro "+nums);}
			rs1.close();

		ResultSet rs2 = stmt.executeQuery("SELECT dateRep from lesrepresentations where nums="+nums);
			i=0;
			while(rs2.next()){
				i++;
				System.out.println(rs2.getString(1));
			}
			if (i==0) {System.out.println("Aucune représentation pour ce spectacle.");}

			// Close the result set, statement and the connection
			rs2.close();
			stmt.close();
	}
	public static void ajout_rep(Connection conn) throws
	SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement() ;
		// Execute the query
		Scanner scan =new Scanner(System.in);
		System.out.println("Donnez un numéro de spectacle");
		String nums=scan.nextLine().toLowerCase();
		System.out.println("Donnez une nouvelle date de représentation (format dd-mm-yyyy)");
		String dateRep=scan.nextLine();
		try{
			ResultSet rs = stmt.executeQuery("insert into LESREPRESENTATIONS values ('"+nums+"', TO_DATE('"+dateRep+"', 'dd-mm-yyyy'))");
			// Close the result set, statement and the connection
			rs.close();
			stmt.close();
		} catch (SQLException E) {
			int err =E.getErrorCode();
			if (err==1) {
				System.out.println("Cette représentation existe déjà.");
			}else if(err==2291) {
				System.out.println("Ce numéro de spéctacle n'existe pas.");
			}else {System.out.println("Erreur Oracle : "+err);}
		}
	}
	public static void retirer_rep(Connection conn) throws
	SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement() ;
		// Execute the query
		Scanner scan =new Scanner(System.in);
		System.out.println("Donnez un numéro de spectacle");
		String nums=scan.nextLine().toLowerCase();
		System.out.println("Donnez la date de représentation à supprimer (format yyyy-mm-dd)");
		String dateRep=scan.nextLine();
		ResultSet rs1 = stmt.executeQuery("SELECT nums from lesrepresentations where nums="+nums+" AND dateRep=TO_DATE('"+dateRep+"', 'yyyy-mm-dd')");
		int i=0;
		while(rs1.next()){
			i++;
			System.out.println(rs1.getString(1));
		}
		rs1.close();
		if (i==0) {System.out.println("Cette représentation n'existe pas.");
		}else {
			try{//System.out.println("TRYING");
				ResultSet rs2 = stmt.executeQuery("delete from LESREPRESENTATIONS where (numS="+nums+" AND dateRep=TO_DATE('"+dateRep+"', 'yyyy-mm-dd'))");
				// Close the result set, statement and the connection
				rs2.close();
				stmt.close();
			} catch (SQLException E) {//System.out.println("CATCHING");
				int err = E.getErrorCode();
				if(err==2291) {
					System.out.println("Ce numéro de spéctacle n'existe pas.");
				}else {System.out.println("Erreur Oracle : "+err);}
			}
		}
	}
*/}

