package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Scanner;

import connexionsgbd.ConnexionSGBD;

/***
 * 
 * @author Pablo Godont, Léo-Solal Bedeau et Pierre Belabbes
 *
 */
public class IHM {
	private Connection conn;
	private Seminaire seminaire;

	public IHM(Connection conn) {
		this.conn = conn;
	}
        
        /**
        * Permet de lancer la finalisation du seminanire
        * 1/ soit en annulant 
        * 2/ soit en confirmant et faisant le calcul des recettes et des depenses
        * @param conn de type Connection : connexion a la bd
        * @param idS de type int : id du seminaire
        * @throws SQLException en cas d'erreur d'acces a la base de donnees
        */
	public void lancer_confirmation(Connection conn, int idS) throws SQLException {
		if(Calendar.getInstance().getTime().after(requetesbd.get_date_confirmation(conn, idS))) {
			int nbParticipant = requetesbd.get_nbPlace_occupee(conn, idS);
			if(nbParticipant<requetesbd.get_nbplace(conn, idS/2)) {
				requetesbd.annulation_seminaire(conn, idS);
				System.out.println("Il faut envoyer l'annulation au prestataire");
			}
			else {
				requetesbd.confirmation_seminaire(conn, idS);
				System.out.println("Il faut envoyer la confirmation au prestataire");
				System.out.println("Recettes : "+requetesbd.get_prix(conn, idS)*requetesbd.get_nbPlace_occupee(conn, idS));
				
				int idPresta = requetesbd.get_idPresta(conn, idS);
				double depenses = requetesbd.cout_salle(conn, idPresta) + requetesbd.cout_activite(conn, idS);
				depenses += requetesbd.cout_pause(conn, idPresta)*nbParticipant;
				if(requetesbd.get_repas(conn, idS).equals("o")) {
					depenses += requetesbd.cout_repas(conn, idPresta)*nbParticipant;
					if(requetesbd.get_duree(conn, idS).equals("journee")) {
						depenses += requetesbd.cout_pause(conn, idPresta)*nbParticipant;
					}
				}
				
				
				System.out.println("Dépenses : "+depenses);
				
			}
		}
		else { System.out.println("Ce séminaire est à plus d'une semaine, pas de confirmation/annulation à effectuer.");}
	}
	
        /**
        * Permet de s'inscrire a un seminaire
        * demande un ID de seminaire, si l'utilisateur a un ID lui demande sinon créer un personne et lui redemande son ID
        * appelle les methodes dans requestbd pour mettre a jour la base
        * valide l'inscription et place eventuellement sur liste d'attente
        * demande pour une autre inscription
        * @throws SQLException en cas d'erreur d'acces a la base de donnees
        */
	public void inscription() throws SQLException {
		int idSemi=0;
		int idPers=0;
		String tmp="";
		Scanner sci = new Scanner(System.in);
		System.out.println("Vous vous trouvez dans l'application d'inscription à un séminaire,");
		do {
			requetesbd.afficher_seminaires(conn);

			do {
				System.out.println("Pour vous y inscrire, veuillez indiquer un ID de séminaire :");
				idSemi = sci.nextInt();
			}while(!requetesbd.seminaire_existe(conn, idSemi));

			do {
				System.out.println("Avez vous un ID? (o/n)");
				tmp=sci.next();
			} while(!(tmp.equals("o")||tmp.equals("n")));

			if (tmp.equals("n")) {idPers = creer_personne(conn);}
			else {
				do {
					System.out.println("Quel est-il?");
					idPers=sci.nextInt();
				}while(!requetesbd.personne_existe(conn, idPers));
			}

			if(requetesbd.place_restante(conn, idSemi)) {
				requetesbd.ajouter_participant(conn, idSemi, idPers, "inscrit");
				System.out.println("Votre inscription au séminaire est terminée");
			}
			else {
				requetesbd.ajouter_participant(conn, idSemi, idPers, "en attente");
				System.out.println("Votre inscription au séminaire est terminée,\nvous êtes sur liste d'attente");
			}
			System.out.println("Souhaitez-vous effectuer une autre réservation ? (o/n)");
			tmp=sci.next();
		}while(tmp.equals("o"));
		sci.close();
	}
           
        /**
        * Permet d'annuler une inscription
        * demande un ID de participant et le seminaire duquel il veut se desinscrire 
        * puis appelle les methodes dans requestbd pour mettre a jour la base 
        * valide l'annulation
        * demande pour une autre annulation
        * @throws SQLException en cas d'erreur d'acces a la base de donnees
        */
	public void annulation_inscription() throws SQLException {
		int idSemi=0;
		int idPers=0;
		String tmp="";
		Scanner sca = new Scanner(System.in);
		System.out.println("Vous vous trouvez dans l'application d'annulation d'inscription à un séminaire,");
		do {			
			do {
				System.out.println("Veuillez renseigner votre ID :");
				idPers = sca.nextInt();
			}while(!requetesbd.personne_existe(conn, idPers));
			requetesbd.afficher_seminaires_personne(conn, idPers);

			do {
				System.out.println("De quel séminaire voulez-vous vous désinscrire ?");
				idSemi = sca.nextInt();
			}while(!requetesbd.seminaire_personne_existe(conn, idSemi, idPers));

			requetesbd.retrait_participant(conn, idSemi, idPers);
			requetesbd.en_attente_vers_inscrit(conn, idSemi);
			System.out.println("Annulation effectuée.\nSouhaitez-vous effectuer une autre annulation ?");
			tmp=sca.next();
		}while(tmp.equals("o"));
		sca.close();
	}
        
        /**
        * Permet de creer une personne
        * demande les infos perso de la personne
        * puis appelle les methodes dans requestbd pour mettre a jour la base 
        * @param conn de type Connection : connexion a la base de donnees
        * @throws SQLException en cas d'erreur d'acces a la base de donnees
        * @return id de la personne entrée en base
        */
	public int creer_personne(Connection conn) throws SQLException {
		int idPers=0;
		long tel=0;
		String prenom="";
		String nom="";
		String email="";
		String adresse="";
		Scanner scp = new Scanner(System.in);
		System.out.println("Renseignez votre prenom :");
		prenom=scp.next();
		System.out.println("Renseignez votre nom :");
		nom=scp.next();
		System.out.println("Renseignez votre email :");
		email=scp.next();
		System.out.println("Renseignez votre adresse :");
		adresse=scp.next();
		System.out.println("Renseignez votre téléphone :");
		tel=scp.nextInt();

		idPers = requetesbd.ajouter_personne(conn, prenom, nom, tel, email, adresse);
		System.out.println("Votre ID est : "+idPers);
		scp.close();
		return idPers;
	}
        
        
        /**
        * Permet de creer un seminaire
        * demande les infos du seminaire
        * puis appelle les methodes dans requestbd pour mettre a jour la base 
        * et calcul la balance budgetaire du seminaire
        * @throws SQLException en cas d'erreur d'acces a la base de donnees
        */

	public void creer_seminaire() throws SQLException {
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


		Scanner scs = new Scanner(System.in);

		do {



			// saisie de la date		
			System.out.println("Vous vous trouvez dans l'application de création de séminaire,");
			do {
				System.out.println("Veuillez renseigner la date à laquelle aura lieu ce nouveau séminaire.\nUn séminaire ne peut pas être crée moins d'un mois avant la date à laquelle il a lieu\n(format 'dd-mm-yy'; pas plus de 3 séminaires par jour) :");
				dateSemi = scs.next();
			} while (!requetesbd.date_valide(conn, dateSemi));



			// saisie du thème		
			do {
				requetesbd.afficher_theme(conn);
				System.out.println("Veuillez renseigner l'id du thème du séminaire :");
				idTheme = scs.nextInt();
			} while (!requetesbd.theme_existe(conn, idTheme));


			// saisie de l'animateur		
			do {
				System.out.println("Veuillez saisir le numéro (>0) identifiant l'animateur chargé de ce séminaire :");
				idAnimateur = scs.nextInt();
			} while (idAnimateur<1 || !(requetesbd.animateur_existe(conn, idAnimateur) && requetesbd.animateur_dispo(conn, idAnimateur, dateSemi)));



			// saisie de la durée		
			do {
				System.out.println("Quelle sera la durée de ce séminaire (matin, apres-midi ou journee) ?");
				duree = scs.next();
			} while (!(duree.equalsIgnoreCase("matin") || duree.equalsIgnoreCase("apres-midi") || duree.equalsIgnoreCase("journee")));



			// saisie du repas		
			if (duree.equals("journee")) {repas = new String("oui"); nbAct=6;}
			else {
				do {
					System.out.println("Un repas est-il prévu ? (oui/non)");
					repas = scs.next();
				} while (!(repas.equalsIgnoreCase("oui") || repas.equalsIgnoreCase("non")));
			}


			// saisie du nombre de places		
			System.out.println("Combien y a-t-il de places disponibles ?");
			nbPlace = scs.nextInt();


			// saisie du tarif de vente		
			System.out.println("A combien le prix de la place s'élève-t-il ?");
			prix = scs.nextDouble();		



			// saisie du prestataire		
			do {
				requetesbd.afficher_prestataires(conn);
				System.out.println("Veuillez saisir le numéro (>0) identifiant le prestataire chargé de ce séminaire :");
				idPresta = scs.nextInt();
			} while (idPresta<1 || !(requetesbd.prestataire_existe(conn, idPresta)));


			// ajout du séminaire	

			seminaire = new Seminaire( idPresta, idTheme, nbPlace, prix, idAnimateur, repas, dateSemi, duree);
			seminaire.save();
			idSemi = seminaire.getId();


			// saisie du programme
			do {
				System.out.println("Avez-vous un progamme à enregistrer maintenant ? (o/n)\n(Vous avez jusqu'à un mois avant le séminaire pour le faire)");
				tmp = scs.next();
			}while(!(tmp.equals("o") || tmp.equals("n")));

			if (tmp.equals("o")) {
				requetesbd.afficher_presentations(conn);
				System.out.println("Il y a trois crénaux de présentation par demi-journée (à compléter au moins un mois avant le séminaire),");
				int i=1;
				do {
					do {
						System.out.println("veuillez donner l'ID de présentation pour le créneau "+i+" (0 pour laisser vide) :");
						idPresentation=scs.nextInt();
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

			depenses_min = seminaire.calcDepenseMin();
			depenses_max = seminaire.calcDepenseMax();
			System.out.println("Les dépenses prévues se trouvent dans la fourchette ["+depenses_min+", "+depenses_max+"]");
			System.out.println("Souhaitez vous en enregistreer un autre? (o/n)");
			tmp=scs.next();

		}while(tmp.equals("o"));
		scs.close();
	}


	public static void main(String args[]) throws SQLException{

		Connection conn = ConnexionSGBD.getConnection();
		IHM ihm = new IHM(conn);
		Scanner scm = new Scanner(System.in);
		String tmp="";
		
		do {
			System.out.println("Bonjour, souhaitez vous [c]réer un séminaire ? vous [i]nscrire à un séminaire ? [a]nnuler une inscription ?");
			tmp=scm.next();
		}while(!(tmp.equals("c") || tmp.equals("i") || tmp.equals("a")));

		if(tmp.equals("c")){
			ihm.creer_seminaire();
		}
		else if(tmp.equals("i")){
			ihm.inscription();
		}
		else if(tmp.equals("a")){
			ihm.annulation_inscription();
		}
		
		conn.close();
		scm.close();
	}
}
