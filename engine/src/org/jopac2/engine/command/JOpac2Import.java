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

import com.ibm.icu.text.Transliterator;


public class JOpac2Import {
	InputStream inputFile;
	String filetype;
	String JOpac2confdir;
	Connection[] conns;
	boolean clearDatabase=true;
	int max_conn=5;
	
	private static String _classMySQLDriver = "com.mysql.jdbc.Driver";
	private static String _classHSQLDBDriver = "org.hsqldb.jdbcDriver";
    private static String _classDerbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
    
	
	public JOpac2Import(String inputFile, String filetype, String JOpac2confdir, String dbUrl, String dbUser, String dbPassword, boolean clearDatabase) throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		File fi=new File(inputFile);
		this.inputFile=new FileInputStream(fi);
		this.JOpac2confdir=JOpac2confdir;
		this.filetype=filetype;
		conns=new Connection[max_conn];
		String driver=_classMySQLDriver;
		if(dbUrl.contains(":hsqldb:")) driver=_classHSQLDBDriver;
		if(dbUrl.contains(":derby:")) driver=_classDerbyDriver;
		
		Class.forName(driver).newInstance();
		
		for(int i=0;i<conns.length;i++) {
			conns[i] = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		}
	}
	
	public JOpac2Import(InputStream in, String filetype, String JOpac2confdir, String dbUrl, String dbUser, String dbPassword, boolean clearDatabase) throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		this.inputFile=in;
		this.JOpac2confdir=JOpac2confdir;
		this.filetype=filetype;
		conns=new Connection[max_conn];
		String driver=_classMySQLDriver;
		if(dbUrl.contains(":hsqldb:")) driver=_classHSQLDBDriver;
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
		Transliterator t=Transliterator.getInstance("NFD; [:Nonspacing Mark:] Remove; NFC");

		DataImporter dataimporter=new DataImporter(inputFile,filetype,JOpac2confdir, conns, clearDatabase,DbGateway.getCache(),t);
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
	
	private static void testMac() throws Exception {
		//JOpac2Import ji=new JOpac2Import(args[0],args[1],args[2],args[3],args[4],args[5],true);
		//String webcontentdir="/java_source/keiko/WebContent";
		String sitename="sebina";
		String filename="/java_jopac2/engine/data/demo_Sebina.uni";
		String filetype="sebina";
		String JOpac2confdir="/java_jopac2/engine/src/org/jopac2/conf";
		String dbUrl = "jdbc:derby:/t/db"+sitename+";create=true";
		//String dbUrl="jdbc:mysql://localhost/db"+sitename;
		String dbUser="root";
		String dbPassword="";
		
		JOpac2Import ji=new JOpac2Import(filename,filetype,JOpac2confdir,dbUrl,dbUser,dbPassword,true);
		ji.doJob();
		ji.wait();
		ji.destroy(dbUrl);
	}
	
	private static void testWin() throws Exception {
		String sitename="sebina";
		String filename="D:/Dev/workspace/engine/data/demo_Sebina.uni";
		String filetype="sebina";
		String JOpac2confdir="D:/Dev/workspace/engine/src/org/jopac2/conf";
		//String dbUrl = "jdbc:derby:db"+sitename+";create=true";
		String dbUrl="jdbc:mysql://localhost/db"+sitename;
		String dbUser="root";
		String dbPassword="";
		
		//DbGateway.createDB(conn, dbName)		
		JOpac2Import ji=new JOpac2Import(filename,filetype,JOpac2confdir,dbUrl,dbUser,dbPassword,true);
		ji.doJob();
		ji.wait();
		ji.destroy(dbUrl);
	}
	
	public static void main(String[] args) throws Exception {
		testMac();
		//testWin();
	}


}
