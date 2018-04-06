package connexionsgbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import application.IHM;
import application.requetesbd;

class ConnexionSGBD {
	
	private static final String configurationFile = "BD.properties";
	
	public static void main(String args[]){
		
		try {
			String jdbcDriver, dbUrl, username, password;
			
			DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
			jdbcDriver = dap.getJdbcDriver();
			dbUrl = dap.getDatabaseUrl();
			username = dap.getUsername();
			password = dap.getPassword();
			
			// Load the database driver
			Class.forName(jdbcDriver);
			// Get a connection to the database
			System.out.println(username + password);
			Connection conn = DriverManager.getConnection(dbUrl, username, password);
			conn.setAutoCommit(false);
			
			
			
			
			IHM.creer_seminaire(conn);

			

			
		
			// Print information about connection warnings
			SQLWarningsExceptions.printWarnings(conn);
			conn.close();
		}
		catch( SQLException se ) {
			// Print information about SQL exceptions
			SQLWarningsExceptions.printExceptions(se);
			return;
		}
		catch( Exception e ) {
			System.err.println( "Exception: " + e.getMessage()) ;
			e.printStackTrace();
			return;
		}
	}
}