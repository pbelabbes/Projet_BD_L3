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
	 * Afficher toutes les informations sur tous les
spectacles.
	 *
	 * @param conn connexion � la base de donn�es
	 * @throws SQLException en cas d'erreur d'acc�s � la
base de donn�es
	 */
	
	
// Recherche de l'index d'activité le plus grand
	public static int act_max(Connection conn) throws
	SQLException {
		int tmp = 0;
		// Get a statement from the connection
		Statement stmt = conn.createStatement() ;
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT MAX(idActivite) FROM Activite");
				while(rs.next()){
					tmp = rs.getInt(1);
					System.out.println("Dernier idActivite : " +	rs.getInt(1));
				}
				// Close the result set, statement and the connection
				rs.close() ;
				stmt.close() ;
	return tmp;
	}
// Recherche de l'index de séminaire le plus grand
	public static int semi_max(Connection conn) throws
	SQLException {
		int tmp = 0;
		// Get a statement from the connection
		Statement stmt = conn.createStatement() ;
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT MAX(idSemi) max FROM Seminaire");
				if(rs.next()){
					tmp = rs.getInt(1);
					System.out.println("Dernier idSemi : " + tmp);
				}
				// Close the result set, statement and the connection
				rs.close() ;
				stmt.close() ;
	return tmp;
	}
// Validation de la date par rapport au trigger de la contrainte_1
	public static boolean date_valide(Connection conn, String dateSemi) throws
	SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
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
		stmt.close() ;
	return true;
	}
	
	
	
	
	
	
	
// recherche de l'animateur idAn et renvoi d'un bouléen selon son existance
	public static boolean animateur_existe(Connection conn, int idAn) throws SQLException {
		int res=0;
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT count(idPers) cnt from Personne where idPers = "+idAn);
		if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
		// Close the result set, statement and the connection
		rs.close();
		stmt.close() ;
	return res==1;
	}
// recherche du thème idT et renvoi d'un bouléen selon son existance
	public static boolean theme_existe(Connection conn, int idT) throws SQLException {
		int res=0;
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT count(idTheme) cnt from Theme where idTheme = "+idT);
		if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
		// Close the result set, statement and the connection
		rs.close();
		stmt.close() ;
	return res==1;
	}
// recherche du prestataire idP et renvoi d'un bouléen selon son existance
	public static boolean prestataire_existe(Connection conn, int idPresta) throws SQLException {
		int res=0;
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT count(idPresta) cnt from Prestataire where idPresta = "+idPresta);
		if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
		// Close the result set, statement and the connection
		rs.close();
		stmt.close();
	return res==1;
	}
// recherche de la presentation idP et renvoi d'un bouléen selon son existance
	public static boolean presentation_existe(Connection conn, int idP) throws SQLException {
		int res=0;
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT count(idPresentation) cnt from Presentation where idPresentation = "+idP);
		if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
		// Close the result set, statement and the connection
		rs.close();
		stmt.close() ;
	return res==1;
	}
	
	
	
	
	
	
// Vérification de la disponibilité d'une personne (contrainte_? sur les inscriptions multiples)
	public static boolean animateur_dispo(Connection conn, int idAn, String dateSem) throws SQLException {
		int res=0;
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT count(idSemi) cnt from Seminaire where idAnimateur = "+idAn+" AND TO_DATE('"+dateSem+"','dd-mm-yy')=dateSemi");
		if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
		// Close the result set, statement and the connection
		rs.close();
		stmt.close() ;
	return res==0;
	}
	public static boolean conferencier_dispo(Connection conn, int idP, String dateSem) throws SQLException {
		int res=0;
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT count(idPers) cnt from (SELECT idSemi from seminaire where dateSemi=TO_DATE('"+dateSem+"','dd-mm-yy')) S join (SELECT idPers, idSemi from Participant where idPers="+idP+"AND statut='conferencier') P on S.idsemi=P.idSemi");
		if (rs.next()){res = rs.getInt("cnt");} else {System.out.println("ID invalide");}
		// Close the result set, statement and the connection
		rs.close();
		stmt.close() ;
	return res==0;
	}
	
	
	
	
	
	
	
	
// module d'affichage des tables
	public static void afficher_prestataires(Connection conn) throws SQLException {
		// Get a statement from the connectionaire
		Statement stmt = conn.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT * from Prestataire");
		while(rs.next()){
			System.out.print("ID : " + rs.getInt(1));
			System.out.print(" Tarif salle : " + rs.getDouble(2));
			System.out.print(" Tarif pause : " + rs.getDouble(3));
			System.out.print(" Tarif repas : " + rs.getDouble(4));
			System.out.println(" Adresse : " + rs.getString(5));
		}
		// Close the result set, statement and the connection
		rs.close();
		stmt.close() ;
	}
	public static void afficher_presentations(Connection conn) throws SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT * from Presentation");
		while(rs.next()){
			System.out.print("ID : " + rs.getInt(1));
			System.out.print(" Conférencier : " + rs.getString(2));
			System.out.print(" Titre : '" + rs.getString(3)+"'");
			System.out.println(" Tarif : " + rs.getDouble(4));
		}
		// Close the result set, statement and the connection
		rs.close();
		stmt.close();
	}
	public static void afficher_theme(Connection conn) throws SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT * from Theme");
		while(rs.next()){
			System.out.print("ID : " + rs.getInt(1)+" ");
			System.out.println("Intitulé : " + rs.getString(2));
		}
		// Close the result set, statement and the connection
		rs.close();
		stmt.close() ;
	}
	
	
	
	
	
	
// module d'insertion de n-uplets
	public static int ajouter_seminaire(Connection conn, int idPresta, int idTheme, int nbPlace, double prix, int idAnimateur, String repas, String dateSemi, String duree) throws SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
		// Execute the query
		int idSem = semi_max(conn)+1;
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
		stmt.close();
		return idSem;
	}
	public static int ajouter_activite(Connection conn, int idSemi, int numero, int idPresentation) throws SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
		// Execute the query
		int idAct = act_max(conn)+1;
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
		stmt.close() ;
		return idAct;
	}
	public static void ajouter_est_presente(Connection conn, int idAct, int idP) throws SQLException {
		Statement stmt = conn.createStatement();
		// Execute the query
		stmt.executeUpdate("INSERT INTO Est_presente VALUES ("+idAct+","+idP+")");
		// Close the result set, statement and the connection
		stmt.close();
	}
	public static void ajouter_animateur_participant(Connection conn, int idAnimateur, int idSemi) throws SQLException {
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
		// Execute the query
		stmt.executeUpdate("INSERT INTO Participant VALUES ("+idSemi+","+idAnimateur+",'animateur', TO_DATE(TO_CHAR(sysdate,'dd-mm-yy'),'dd-mm-yy'))");
		// Close the result set, statement and the connection
		stmt.close();
	}
	
	
	
	
	
// Extraction des données pécunières	
	public static int cout_activite(Connection conn, int idSemi) throws SQLException {
		int cout = 0;
		Statement stmt = conn.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT SUM(montant) tot from Presentation natural join Est_presente natural join Activite where idSemi="+idSemi);
		if(rs.next()) {cout = rs.getInt("tot"); }
		conn.commit();
		// Close the result set, statement and the connection
		stmt.close();
		return cout;
	}
	public static int cout_repas(Connection conn, int idP) throws SQLException {
		int tot = 0;
		Statement stmt = conn.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT tarifRepas from Prestataire where idPresta="+idP);
		if(rs.next()) {tot = rs.getInt(1); }
		conn.commit();
		// Close the result set, statement and the connection
		stmt.close();
		return tot;
	}
	public static int cout_pause(Connection conn, int idP) throws SQLException {
		int tot = 0;
		Statement stmt = conn.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT tarifPause from Prestataire where idPresta="+idP);
		if(rs.next()) {tot = rs.getInt(1); }
		conn.commit();
		// Close the result set, statement and the connection
		stmt.close();
		return tot;
	}
	public static int cout_salle(Connection conn, int idP) throws SQLException {
		int tot = 0;
		Statement stmt = conn.createStatement();
		// Execute the query
		ResultSet rs = stmt.executeQuery("SELECT tarifSalle from Prestataire where idPresta="+idP);
		if(rs.next()) {tot = rs.getInt(1); }
		conn.commit();
		// Close the result set, statement and the connection
		stmt.close();
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
