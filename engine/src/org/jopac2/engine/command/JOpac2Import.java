package org.jopac2.engine.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.importers.DataImporter;


public class JOpac2Import {
	InputStream inputFile;
	String filetype;
	String JOpac2confdir;
	Connection[] conns;
	boolean clearDatabase=true;
	int max_conn=5;
	private PrintStream out=null,outputErrorRecords=out;
	private String catalog="";
	
	private static String _classMySQLDriver = "com.mysql.jdbc.Driver";
    private static String _classDerbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
//    private static String _classDerbyDriver = "org.apache.derby.jdbc.ClientDriver";

    
	
	public JOpac2Import(String inputFile, String catalog, String filetype, String JOpac2confdir, String dbUrl, String dbUser, 
			String dbPassword, boolean clearDatabase,PrintStream console,PrintStream outputErrorRecords) throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		File fi=new File(inputFile);
		this.inputFile=new FileInputStream(fi);
		this.JOpac2confdir=JOpac2confdir;
		this.filetype=filetype;
		out=console;
		this.outputErrorRecords=outputErrorRecords;
		this.catalog=catalog;
		conns=new Connection[max_conn];
		String driver=_classMySQLDriver;
		if(dbUrl.contains(":derby:")) driver=_classDerbyDriver;
		
		Class.forName(driver).newInstance();
		
		for(int i=0;i<conns.length;i++) {
			conns[i] = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		}
	}
	
	public JOpac2Import(InputStream in, String catalog, String filetype, String JOpac2confdir, String dbUrl, String dbUser, String dbPassword, 
				boolean clearDatabase, PrintStream console, PrintStream outputErrorRecords) throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		this.inputFile=in;
		this.JOpac2confdir=JOpac2confdir;
		this.filetype=filetype;
		out=console;
		this.outputErrorRecords=outputErrorRecords;
		this.catalog=catalog;
		conns=new Connection[max_conn];
		String driver=_classMySQLDriver;
		if(dbUrl.contains(":derby:")) driver=_classDerbyDriver;
		
		Class.forName(driver).newInstance();
		
		for(int i=0;i<conns.length;i++) {
			conns[i] = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		}
	}
	
	public void doJob() {
		doJob(true);
	}
	
	public void doJob(boolean background) {
		//Transliterator t=Transliterator.getInstance("NFD; [:Nonspacing Mark:] Remove; NFC");

		DataImporter dataimporter=new DataImporter(inputFile, filetype,JOpac2confdir, conns, catalog, clearDatabase,DbGateway.getCache(),out,outputErrorRecords); //,t);
		if(background)
			dataimporter.start();
		else
			dataimporter.doJob();
	}

	public void destroy(String dbUrl) throws SQLException, IOException {
		this.inputFile.close();
		for(int i=0;i<conns.length;i++) {
			conns[i].close();
		}
		if(dbUrl.contains(":derby:")) {
			try {
				DriverManager.getConnection("jdbc:derby:;shutdown=true");
			} catch (Exception e) {
			}
		}
	}
	

}
