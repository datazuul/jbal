package org.jopac2.engine.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jopac2.engine.importers.DataImporter;


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
	
	public void doJob() {
		DataImporter dataimporter=new DataImporter(inputFile,filetype,JOpac2confdir, conns, clearDatabase);
		dataimporter.start();
	}

	public void destroy(String dbUrl) throws SQLException, IOException {
		this.inputFile.close();
		for(int i=0;i<conns.length;i++) {
			conns[i].close();
		}
		if(dbUrl.contains(":derby:")) {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
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
		String filename="/java_jopac2/engine/data/demo_Sebina.uni";
		String filetype="sebina";
		String JOpac2confdir="/java_jopac2/engine/src/org/jopac2/conf";
		//String dbUrl = "jdbc:derby:db"+sitename+";create=true";
		String dbUrl="jdbc:mysql://localhost/db"+sitename;
		String dbUser="root";
		String dbPassword="";
		
		JOpac2Import ji=new JOpac2Import(filename,filetype,JOpac2confdir,dbUrl,dbUser,dbPassword,true);
		ji.doJob();
		ji.wait();
		ji.destroy(dbUrl);
	}


}
