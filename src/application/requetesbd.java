package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import connexionsgbd.SQLWarningsExceptions;
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
         * @return tmp : l'adresse du prestataire
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
         * table cible : Seminaire, trigger sur l'insertion
	 * @param conn de type Connection : connexion a la base de donnees
         * @param idSemi de type int : id du seminaire
         * @return boolean : l'adresse du prestataire
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
        
        
	public static int get_nbplace(Connection conn, int idS) throws SQLException {
		int tmp=0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT nbPlace N from Seminaire where idSemi="+idS)) {
                if(rs.next()) {tmp=rs.getInt("N");}
            }
	return tmp;
	}
	public static void en_attente_vers_inscrit(Connection conn, int idS) throws SQLException {
		int idPers = resa_plus_ancienne(conn, idS);
		// Execute the query
		try ( // Get a statement from the connection
                        Statement stmt = conn.createStatement()) {
                    // Execute the query
                    try {
                        stmt.executeUpdate("UPDATE statut='inscrit' where idPers="+idPers+" AND idSemi="+idS);
                    } catch( SQLException se ) {
                        // Print information about SQL exceptions
                        SQLWarningsExceptions.printExceptions(se);
                        conn.rollback();
		}
                conn.commit();
		// Close the result set, statement and the connection

		}	}
	public static int resa_plus_ancienne(Connection conn, int idS) throws SQLException {
		int idP = 0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT idPers P from Participant where dateResa = (SELECT MIN(dateResa) from Participant where idSemi="+idS+" AND statut='en attente')")) {
                if(rs.next()){
                    idP = rs.getInt("P");
                }
            }
		return idP;
	
	}
	
	
	
	
	
	
// recherche de l'animateur idAn et renvoi d'un bouléen selon son existance
	public static boolean animateur_existe(Connection conn, int idAn) throws SQLException {
		int res=0;
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement(); // Execute the query
                    ResultSet rs = stmt.executeQuery("SELECT count(idPers) cnt from Personne where idPers = "+idAn)) {
                if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
            }
	return res==1;
	}
// recherche du thème idT et renvoi d'un bouléen selon son existance
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
// recherche du prestataire idP et renvoi d'un bouléen selon son existance
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
// recherche de la presentation idP et renvoi d'un bouléen selon son existance
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
	public static void ajouter_est_presente(Connection conn, int idAct, int idP) throws SQLException {
            // Execute the query
            try (Statement stmt = conn.createStatement()) {
                // Execute the query
                stmt.executeUpdate("INSERT INTO Est_presente VALUES ("+idAct+","+idP+")");
                // Close the result set, statement and the connection
            }
	}
	public static void ajouter_participant(Connection conn, int idPers, int idSemi, String statut) throws SQLException {
            // Execute the query
            try ( // Get a statement from the connection
                    Statement stmt = conn.createStatement()) {
                // Execute the query
                stmt.executeUpdate("INSERT INTO Participant VALUES ("+idSemi+","+idPers+","+statut+", TO_DATE(TO_CHAR(sysdate,'dd-mm-yy'),'dd-mm-yy'))");
                // Close the result set, statement and the connection
            }
	}
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

