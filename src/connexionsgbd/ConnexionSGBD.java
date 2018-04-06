package connexionsgbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/***
 * 
 * @author Pablo Godont, Léo-Solal Bedeau et Pierre Belabbes
 *
 */
public class ConnexionSGBD {

	
	/**
	 * Fichier de config pour accéder à la base de données
	 */
	private static final String configurationFile = "BD.properties";
	private static Connection conn = null;

	
	
	/**
	 * Chargement du driver
	 * @param driver, nom du driver à charger
	 * @throws ClassNotFoundException
	 */
	private static void loadDriver(String driver) throws ClassNotFoundException {
		Class.forName(driver);
	}


	/**
	 * Crée une connexion 
	 * @param dbUrl, lien de la BD
	 * @param username, identifiant de l'utilisateur
	 * @param password, mot de passe 
	 * @return
	 * @throws SQLException
	 */
	private static Connection newConnection(String dbUrl, String username, String password) throws SQLException{
		Connection conn = DriverManager.getConnection(dbUrl, username, password);
		conn.setAutoCommit(false);
		return conn;
	}


	/**
	 * Récupère la connexion en cours, si elle n'existe pas, la crée
	 * @return connexion : Connection
	 */
	public static Connection getConnection() {
		Connection rConn = null;
		if (conn == null) {
			try {
				String jdbcDriver, dbUrl, username, password;

				DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
				jdbcDriver = dap.getJdbcDriver();
				dbUrl = dap.getDatabaseUrl();
				username = dap.getUsername();
				password = dap.getPassword();

				// Load the database driver
				
					loadDriver(jdbcDriver);
				
				// Get a connection to the database
				//			System.out.println(username + password);



				rConn = newConnection(dbUrl, username, password);

			}catch( SQLException se ) {
				// Print information about SQL exceptions
				SQLWarningsExceptions.printExceptions(se);
			}
			catch( Exception e ) {
				System.err.println( "Exception: " + e.getMessage()) ;
				e.printStackTrace();
			}
		}else {
			rConn = conn;
		}
		return rConn;
	}
	
	
	/**
	 * Ferme la connexion en cours
	 */
	public static void closeConnection() {
		SQLWarningsExceptions.printWarnings(conn);
		try {
			conn.close();
		} catch( SQLException se ) {
			// Print information about SQL exceptions
			SQLWarningsExceptions.printExceptions(se);
		}
	}
}