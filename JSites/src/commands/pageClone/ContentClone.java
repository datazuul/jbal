package commands.pageClone;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import JSites.utils.PageClone;

public class ContentClone {
	private static String sitename="sito";
	private static String dbUrl = "jdbc:mysql://localhost/db" + sitename;
	private static String dbUser = "root";
	private static String dbPassword = "";
    private static String dataDirectory="/siti/"+sitename+"_components";
	private static String _classMySQLDriver = "com.mysql.jdbc.Driver";

	private Connection conn;

	public Connection CreaConnessione() throws SQLException {
		Connection conn = null;
		String driver = _classMySQLDriver;
		
		
		boolean inizializzato = false;
		if (!inizializzato) {
			inizializzato = true;
			try {
				Class.forName(driver).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("getting conn....");
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		System.out.println("presa");

		return conn;
	}

	private void doJob() throws SQLException, IOException {
		conn=CreaConnessione();
		PageClone.cloneRecursivePage(27, 14, dataDirectory, "", "", conn);
		conn.close();
	}

	public static void main(String[] args) throws SQLException, IOException {
		ContentClone cc=new ContentClone();
		cc.doJob();
	}



}
