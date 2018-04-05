package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

public class IHM {
	public static void creer_seminaire(Connection conn) throws SQLException {
		int idSemi = 0;
		int idPresta = 0;
		int idTheme = 0;
		int idAnimateur = 0;
		int nbPlace = 0;
		int idPresentation = 0;
		int nbAct = 3;
		double prix = 0;
		String dateSemi; // une String sera p-e plus pratique
		String repas = null; // 3
		String duree = null; // 12
		String tmp ="";
		
		
		
		
		Scanner sc = new Scanner(System.in);
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
		else {System.out.println("Le séminaire été ajouté avec succès, il a pour numéro : "+idSemi);}
		
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
				}while(idPresentation!=0 || !(requetesbd.conferencier_dispo(conn, idPresentation, dateSemi) && requetesbd.presentation_existe(conn, idPresentation)));
				if(idPresentation>0) {
					requetesbd.ajouter_activite(conn, idSemi, i, idPresentation);
				}
				i++;
			}while (!(i>nbAct));
		}
	}
}
