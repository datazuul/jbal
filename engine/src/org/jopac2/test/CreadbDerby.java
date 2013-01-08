package org.jopac2.test;

import java.sql.Connection;
import java.sql.DriverManager;

public class CreadbDerby {

	private static String _classDriver = "com.mysql.jdbc.Driver";
	private static String _dbMasterUrl = "jdbc:mysql://localhost/";	
	private static String _dbUser = "root";
	private static String _dbPass = "%op01rt!";
	private static Connection conn = null;
	private static String _classDerbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
	
	public static Connection CreaConnessione(String _dbName) {
		boolean inizializzato = false;
		if (!inizializzato) {
			inizializzato = true;
			try {
				Class.forName(_classDriver).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			conn = DriverManager.getConnection(_dbMasterUrl + _dbName, _dbUser,	_dbPass);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
