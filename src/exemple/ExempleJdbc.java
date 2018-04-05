package exemple;

import java.sql.*;

public class ExempleJdbc {
	
	final static String jdbcDriverName ="oracle.jdbc.driver.OracleDriver";
	final static String urlDB ="jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:im2ag";
	
	// Programme principal
	
		//Chargement du pilote
	private static void loadDriver() throws ClassNotFoundException {
		Class.forName(jdbcDriverName);
		}
	
	
		//ouverture de connexion
	private static Connection newConnection() throws SQLException{
		Connection conn = DriverManager.getConnection(urlDB,"belabbep","NJ6Pbiwe");
		
		return conn;
	}
	
		// exécution d'une requête
	
	public static void listPersons(Connection conn) throws SQLException {
		Statement st = null;
		try {
			
				// create new statement
			
				st = conn.createStatement();
				
				String query ="SELECT nom,prenom,age FROM personne";
				ResultSet rs = st.executeQuery(query);
				
				while(rs.next()) {
					String nom=rs.getString(1);
					String prenom=rs.getString("prenom");
					int age = rs.getInt(3);
					System.out.println(nom + " "+ prenom + " : "+ age);
										
				}
			} finally {
				
				//close statement and connection
				if (st != null) {
					st.close();
				}
				
				if ( conn != null) {
					conn.close();
				}
			}
	}
	
	
	public static void main(String [] args) {
		
		
		try {
			// chargement du pilote
			
			loadDriver();
			
			//ouverture de connexion 
			Connection conn = newConnection();
			
			//execution d'une requete
			listPersons(conn);
		}
		catch(Exception e) {
			System.out.println("Error" + e );
		}
		
		
		
		
		
		
		
		
		
		
	}
}
