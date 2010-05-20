package org.jopac2.engine.command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JbalHelper;


public class JOpac2ExportData {
	Connection[] conns;
	int max_conn=5;
	String catalog="eutmarc";
	
	private static String _classMySQLDriver = "com.mysql.jdbc.Driver";
	
	public JOpac2ExportData(String dbUrl, String catalog, String dbUser, String dbPassword) throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		conns=new Connection[max_conn];
		this.catalog=catalog;
		String driver=_classMySQLDriver;
		
		Class.forName(driver).newInstance();
		
		for(int i=0;i<conns.length;i++) {
			conns[i] = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		}
	}
	
	private void doJob(PrintWriter out) throws SQLException {
		DbGateway dbGateway=DbGateway.getInstance(conns[0].toString(),System.out);
		long max=DbGateway.getCountNotizie(conns[0], catalog);
		for(long jid=1;jid<max;jid++) {
			
			try {
				RecordInterface ma=DbGateway.getNotiziaByJID(conns[0], catalog, jid);
				out.println(ma.toString());
				ma.destroy();
			}
			catch (Exception e) {}
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
		String sitename="dbeut";
		String dbUrl="jdbc:mysql://140.105.147.166/"+sitename;
		String dbUser="root";
		String dbPassword="%op01rt!";
		String catalog="eutmarc";
		
		JOpac2ExportData ji=new JOpac2ExportData(dbUrl,catalog,dbUser,dbPassword);
		PrintWriter out=new PrintWriter("/tmp/eutmarc.iso");
		ji.doJob(out);
		out.close();
		
		ji.destroy();
	}


}
