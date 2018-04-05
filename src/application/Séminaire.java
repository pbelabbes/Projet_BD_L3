package application;

import java.sql.Date;

public class Séminaire {
	protected int idSemi = 0;
	protected int idPresta = 0;
	protected int idTheme = 0;
	protected int idAnimateur = 0;
	protected int nbPlace = 0;
	protected double prix = 0;
	protected Date dateSemi;
	protected String repas = ""; // 3
	protected String statut = ""; // 10
	protected String duree = ""; // 12
	
	public void setIdSemi(int id) {
		idSemi = id;
	}
	public void setIdPresta(int id) {
		idPresta = id;
	}
	public void setIdTheme(int id) {
		idTheme = id;
	}
	public void setIdAnimateur(int id) {
		idAnimateur = id;
	}
	public void setNbPlace(int nb) {
		nbPlace = nb;
	}
	public void setPrix(double p) {
		//Set la taille du double ici?
		prix = p;
	}
	public void setDateSemi(Date d) {
		dateSemi = d;
	}
	public void setRepas(String s) {
		if (s.equalsIgnoreCase("oui") || s.equalsIgnoreCase("non")){
			repas = s;	
		} else { System.out.println("Repas vaut \"oui\" ou \"non\"");}
	}
	public void setStatut(String s) {
		if (s.equalsIgnoreCase("non-confirme") || s.equalsIgnoreCase("confirme") || s.equalsIgnoreCase("annule")){
			statut = s;	
		} else { System.out.println("Statut vaut \"confirme\", \"non-confirme\" ou \"annule\"");}
	}
	public void setDuree(String s) {
		if (s.equalsIgnoreCase("matin") || s.equalsIgnoreCase("apres-midi") || s.equalsIgnoreCase("journee")){
			duree = s;	
		} else { System.out.println("Durée vaut \"matin\", \"apres-midi\" ou \"journee\"");}
	}
	public int getIdSemi() { return idSemi;}
	public int getIdPresta() { return idPresta;}
	public int getIdTheme() { return idTheme;}
	public int getIdAnimateur() { return idAnimateur;}
	public int getNbPlaces() { return nbPlace;}
	public double getPrix() { return prix;}
	public Date getDate() { return dateSemi;}
	public String getRepas() { return repas;}
	public String getStatut() { return statut;}
	public String getDuree() { return duree;}
}
