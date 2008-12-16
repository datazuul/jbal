package org.jopac2.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jopac2.engine.parserRicerche.parser.exception.ExpressionException;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;


public class DataModulesTest {
	private static String _classDriver = "com.mysql.jdbc.Driver"; //"org.hsqldb.jdbcDriver";//
	private static String _dbMasterUrl = "jdbc:mysql://joyce.units.it/"; //"jdbc:hsqldb:hsql://localhost:9002/";//
	private static String _dbUser = "root"; //"sa";//
	private static String _dbPass = "%op01rt!";
	private static String _dbName = "dbunimarc"; 
	private static Connection conn = null;
	

	public static Connection CreaConnessione() {
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
			System.out.println("getting conn....");
			conn = DriverManager.getConnection(_dbMasterUrl + _dbName, _dbUser,
					_dbPass);
			System.out.println("presa");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) throws ExpressionException, SQLException {
		Connection conn=CreaConnessione();
		String sql="select notizia from notizie where id=1885";
		Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery(sql);
		while(rs.next()) {
			String thisRecord=rs.getString("notizia");
			System.out.println(thisRecord);
			RecordInterface ma=RecordFactory.buildRecord(0, thisRecord, "Sebina", 0);
			System.out.println(ma.toString());
		}
		rs.close();
		st.close();
		conn.close();
	}
}
