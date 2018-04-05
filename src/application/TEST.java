package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import application.requetesbd;
import connexionsgbd.DatabaseAccessProperties;
import connexionsgbd.SQLWarningsExceptions;

class TEST {
	
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
			
			
			
			
			
			
			requetesbd.date_valide(conn, "16-10-19");
			requetesbd.semi_max(conn);
			


		
			
			
			
			
			
			
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
