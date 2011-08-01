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


public class JOpac2RebuildDatabase {
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
	
	public JOpac2RebuildDatabase(String catalog, String filetype, String dbUrl, String dbUser, String dbPassword, 
				PrintStream console, PrintStream outputErrorRecords) throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
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
	
	public void doJob() throws SQLException {
		DbGateway dbg=DbGateway.getInstance(conns[0].toString(), out);
		dbg.rebuildDatabase(conns, catalog, out);
		dbg.rebuildList(conns[0], catalog);
	}
	

	public static void main(String[] args) throws Exception {
		String filetype="eutmarc";
		//String dbUrl = "jdbc:derby:db"+sitename+";create=true";
		String dbUrl="jdbc:mysql://140.105.147.166/dbeut";
		String dbUser="root";
		String dbPassword="%op01rt!";
		String catalog="eutmarc";
		
		JOpac2RebuildDatabase rb=new JOpac2RebuildDatabase(catalog,filetype,dbUrl,dbUser,dbPassword, System.out, System.err);
		rb.doJob();
		
	}


}
