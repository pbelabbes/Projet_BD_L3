package application;

import java.sql.Date;
import java.sql.SQLException;

import com.sun.corba.se.pept.transport.Connection;

import connexionsgbd.ConnexionSGBD;

public class Seminaire {

	int idSemi = 0;
	int idPresta = 0;
	int idTheme = 0;
	int idAnimateur = 0;
	int nbPlace = 0;
	int idPresentation = 0;
	int nbAct = 3;
	int idAct = 0;
	int depenses_min=0;
	int depenses_max=0;
	double prix = 0;
	String dateSemi; // format Date source d'erreur
	String repas = null; // initialisée à 'null' pour correspondre au schéma de la BD si besoin est
	String duree = null; // idem

	java.sql.Connection conn;

	/**
	 * Constructeur de création de séminaire
	 * @param idPresta
	 * @param idTheme
	 * @param nbPlace
	 * @param prix
	 * @param idAnimateur
	 * @param repas
	 * @param dateSemi
	 * @param duree
	 */
	public Seminaire(int idPresta, int idTheme, int nbPlace, double prix, int idAnimateur, String repas, String dateSemi, String duree) {

		this.idPresta = idPresta;
		this.idTheme = idTheme;
		this.nbPlace = nbPlace;
		this.prix = prix;
		this.idAnimateur = idAnimateur;
		this.repas = repas; 
		this.dateSemi = dateSemi; 
		this.duree = duree; 

		this.conn = ConnexionSGBD.getConnection();

	}

	/**
	 * Creation d'un séminaire dans la base de donnée
	 */
	public void save() {
		try {
			idSemi = requetesbd.ajouter_seminaire(conn, idPresta, idTheme, nbPlace, prix, idAnimateur, repas, dateSemi, duree);

			if (idSemi==-1) {
				System.out.println("Le séminaire n'a pu être ajouté");

			}else {
				System.out.println("Le séminaire été ajouté avec succès, il a pour numéro : "+idSemi);

				requetesbd.ajouter_animateur_participant(conn, idAnimateur, idSemi);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Calcul la depense minimale pour l'orga du seminaire
	 * @return depense minimal : int
	 */
	public int calcDepenseMin() {
	
		try {
			
		
		depenses_min = requetesbd.cout_salle(conn, idPresta) + requetesbd.cout_activite(conn, idSemi);
		
		depenses_min += requetesbd.cout_pause(conn, idPresta)*nbPlace/2;
		if(repas.equals("o")) {
			depenses_min += requetesbd.cout_repas(conn, idPresta)*nbPlace/2;
			if(duree.equals("journee")) {
				depenses_min += requetesbd.cout_pause(conn, idPresta)*nbPlace/2;
			}
		}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return depenses_min;
		
		
	}
	
	
	/**
	 * Calcul la dépense maximale pour l'orga du séminaire
	 * @return depenses_max : int
	 */
	public int calcDepenseMax() {
		
		try {
			
		depenses_max = requetesbd.cout_salle(conn, idPresta) + requetesbd.cout_activite(conn, idSemi);
		depenses_max += requetesbd.cout_pause(conn, idPresta)*nbPlace;
		if(repas.equals("o")) {
			depenses_max += requetesbd.cout_repas(conn, idPresta)*nbPlace;
			if(duree.equals("journee")) {
				depenses_max += requetesbd.cout_pause(conn, idPresta)*nbPlace;
			}
		}
		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		return depenses_max;
	}
}

