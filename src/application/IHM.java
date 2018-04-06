package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;
import connexionsgbd.*;
public class IHM {
	
	
	
	public static void creer_seminaire(Connection conn) throws SQLException {
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
		String tmp ="";
		
		
		
		
		Scanner sc = new Scanner(System.in);
			do {
// saisie de la date		
			System.out.println("Vous vous trouvez dans l'application de création de séminaire,");
			do {
				System.out.println("Veuillez renseigner la date à laquelle aura lieu ce nouveau séminaire.\nUn séminaire ne peut pas être crée moins d'un mois avant la date à laquelle il a lieu\n(format 'dd-mm-yy'; pas plus de 3 séminaires par jour) :");
				dateSemi = sc.next();
			} while (!requetesbd.date_valide(conn, dateSemi));
// saisie du thème		
			do {
				requetesbd.afficher_theme(conn);
				System.out.println("Veuillez renseigner l'id du thème du séminaire :");
				idTheme = sc.nextInt();
			} while (!requetesbd.theme_existe(conn, idTheme));
// saisie de l'animateur		
			do {
				System.out.println("Veuillez saisir le numéro (>0) identifiant l'animateur chargé de ce séminaire :");
				idAnimateur = sc.nextInt();
			} while (idAnimateur<1 || !(requetesbd.animateur_existe(conn, idAnimateur) && requetesbd.animateur_dispo(conn, idAnimateur, dateSemi)));
			
// saisie de la durée		
			do {
				System.out.println("Quelle sera la durée de ce séminaire (matin, apres-midi ou journee) ?");
				duree = sc.next();
			} while (!(duree.equalsIgnoreCase("matin") || duree.equalsIgnoreCase("apres-midi") || duree.equalsIgnoreCase("journee")));
			
// saisie du repas		
			if (duree.equals("journee")) {repas = new String("oui"); nbAct=6;}
			else {
				do {
					System.out.println("Un repas est-il prévu ? (oui/non)");
					repas = sc.next();
				} while (!(repas.equalsIgnoreCase("oui") || repas.equalsIgnoreCase("non")));
			}
// saisie du nombre de places		
			System.out.println("Combien y a-t-il de places disponibles ?");
			nbPlace = sc.nextInt();
// saisie du tarif de vente		
			System.out.println("A combien le prix de la place s'élève-t-il ?");
			prix = sc.nextDouble();		
	
// saisie du prestataire		
			do {
				requetesbd.afficher_prestataires(conn);
				System.out.println("Veuillez saisir le numéro (>0) identifiant le prestataire chargé de ce séminaire :");
				idPresta = sc.nextInt();
			} while (idPresta<1 || !(requetesbd.prestataire_existe(conn, idPresta)));
// ajout du séminaire	
			idSemi = requetesbd.ajouter_seminaire(conn, idPresta, idTheme, nbPlace, prix, idAnimateur, repas, dateSemi, duree);
			if (idSemi==-1) {System.out.println("Le séminaire n'a pu être ajouté");}
			else {
				System.out.println("Le séminaire été ajouté avec succès, il a pour numéro : "+idSemi);
				requetesbd.ajouter_animateur_participant(conn, idAnimateur, idSemi);
			}
			
// saisie du programme
			do {
				System.out.println("Avez-vous un progamme à enregistrer maintenant ? (o/n)\n(Vous avez jusqu'à un mois avant le séminaire pour le faire)");
				tmp = sc.next();
			}while(!(tmp.equals("o") || tmp.equals("n")));
			
			if (tmp.equals("o")) {
				requetesbd.afficher_presentations(conn);
				System.out.println("Il y a trois crénaux de présentation par demi-journée (à compléter au moins un mois avant le séminaire),");
				int i=1;
				do {
					do {
						System.out.println("veuillez donner l'ID de présentation pour le créneau "+i+" (0 pour laisser vide) :");
						idPresentation=sc.nextInt();
					}while(idPresentation!=0 && !(requetesbd.conferencier_dispo(conn, idPresentation, dateSemi) && requetesbd.presentation_existe(conn, idPresentation)));
					if(idPresentation>0) {
						idAct = requetesbd.ajouter_activite(conn, idSemi, i, idPresentation);
						if(idAct!=-1) {
							System.out.println("Activité ajoutée avec succès, son ID est : "+idAct);
							requetesbd.ajouter_est_presente(conn, idAct, idPresentation);
						}
					}
					i++;
				}while (!(i>nbAct));
			}
// calculs pécuniers
			System.out.println("L'enregistrement de ce séminaire est terminé.");
			System.out.println("Les recettes prévues se trouvent dans la fourchette ["+prix*nbPlace/2+", "+prix*nbPlace+"]");
			
			depenses_min = requetesbd.cout_salle(conn, idPresta) + requetesbd.cout_activite(conn, idSemi);
			depenses_max = depenses_min;
			depenses_min += requetesbd.cout_pause(conn, idPresta)*nbPlace/2;
			depenses_max += requetesbd.cout_pause(conn, idPresta)*nbPlace;
			if(repas.equals("o")) {
				depenses_min += requetesbd.cout_repas(conn, idPresta)*nbPlace/2;
				depenses_max += requetesbd.cout_repas(conn, idPresta)*nbPlace;
				if(duree.equals("journee")) {
					depenses_min += requetesbd.cout_pause(conn, idPresta)*nbPlace/2;
					depenses_max += requetesbd.cout_pause(conn, idPresta)*nbPlace;
				}
			}
			System.out.println("Les dépenses prévues se trouvent dans la fourchette ["+depenses_min+", "+depenses_max+"]");
			System.out.println("Souhaitez vous en enregistreer un autre? (o/n)");
			tmp=sc.next();
		}while(tmp.equals("o"));
	}
	
	public void main (String [] args) {
		
	
		
		try {
			creer_seminaire(ConnexionSGBD.getConnection());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
