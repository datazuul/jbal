package org.jopac2.engine.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.importers.DataImporter;


public class JOpac2CreateList {
	Connection[] conns;
	int max_conn=5;
	
	private static String _classMySQLDriver = "com.mysql.jdbc.Driver";
	private static String _classHSQLDBDriver = "org.hsqldb.jdbcDriver";
	
	public JOpac2CreateList(String dbUrl, String dbUser, String dbPassword) throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		conns=new Connection[max_conn];
		String driver=_classMySQLDriver;
		if(dbUrl.contains(":hsqldb:")) driver=_classHSQLDBDriver;
		
		Class.forName(driver).newInstance();
		
		for(int i=0;i<conns.length;i++) {
			conns[i] = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		}
	}
	
	private void doJob() {
		try {
			DbGateway.rebuildList(conns[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void destroy() throws SQLException, IOException {
		for(int i=0;i<conns.length;i++) {
			conns[i].close();
		}
	}
	
	/**
	 * @param args file, formato, databaseurl, append
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws Exception {
		//JOpac2Import ji=new JOpac2Import(args[0],args[1],args[2],args[3],args[4],args[5],true);
		//String webcontentdir="/java_source/keiko/WebContent";
		String sitename="sebina";
		String dbUrl="jdbc:mysql://localhost/db"+sitename;
		String dbUser="root";
		String dbPassword="";
		
		JOpac2CreateList ji=new JOpac2CreateList(dbUrl,dbUser,dbPassword);
		ji.doJob();
		ji.wait();
		ji.destroy();
	}


}
