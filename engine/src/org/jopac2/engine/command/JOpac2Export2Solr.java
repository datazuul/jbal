package org.jopac2.engine.command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import org.jopac2.engine.dbengine.dbGateway.DbGateway;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JbalHelper;


public class JOpac2Export2Solr {
	Connection[] conns;
	int max_conn=5;
	String catalog="eutmarc";
	
	private static String _classMySQLDriver = "com.mysql.jdbc.Driver";
	
	public JOpac2Export2Solr(String dbUrl, String catalog, String dbUser, String dbPassword) throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
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
		
		out.println("<add overwrite=\"true\" commitWithin=\"10000\">");
		
		
		for(long jid=1;jid<max;jid++) {
			
			try {
				RecordInterface ma=DbGateway.getNotiziaByJID(conns[0], catalog, jid);				String[] channels=ma.getChannels();
				String[] values=new String[channels.length];
				String any="";
				String bid=ma.getBid();
				String image=ma.getBase64Image();
				String raw=ma.toString().replaceAll("&", "&#38;");
				
				out.println("\t<doc>");
				out.println("\t\t<field name=\"jid\">"+jid+"</field>");
				out.println("\t\t<field name=\"bid\">"+bid+"</field>");
//				out.println("\t\t<field name=\"raw\">"+raw+"</field>");
//				out.println("\t\t<field name=\"image\">"+image+"</field>");
				
				for(int i=0;i<channels.length;i++) {
					if(!channels[i].equalsIgnoreCase("any")) {
						values[i]=getChannelValue(conns[0],catalog,channels[i],ma);
						any=any+values[i]+" ";
						out.println("\t\t<field name=\""+channels[i]+"\">"+values[i]+"</field>");
					}
				}
				ma.destroy();
				
				out.println("</doc>");
				
			}
			catch (Exception e) {}
		}
		
		out.println("</add>");
	}
	
	private String getChannelValue(Connection conn, String catalog, String classe,
			RecordInterface ma) throws SQLException {
		String r="";
		if(classe.equals("TIT")) {
			r=ma.getTitle(); //testo=ma.getTitle();
		}
		else if(classe.equals("NUM")) {
		
			r=ma.getStandardNumber(); // testo=ma.getStandardNumber();
		}
		else if(classe.equals("DTE")) {
			r=ma.getPublicationDate(); // testo=ma.getPublicationDate();
		}
		else if(classe.equals("AUT")) {
			r=vectorToString(ma.getAuthors());
		}
		else if(classe.equals("SBJ")) {
			r=subjectsToString(ma.getSubjects());
		}
		else if(classe.equals("BIB")) {
			Vector<BookSignature> signatures=ma.getSignatures();
			for(int i=0;signatures!=null && i<signatures.size();i++) {
				try {
					r=r+signatures.elementAt(i).getLibraryName()+" ";
				}
				catch(Exception e) {e.printStackTrace();}
			}
		}
		else {
			r=ma.getField(classe); // prova diretto per file Mdb
		}
		
		if(r==null) r="";
		r=r.replaceAll("[^\\w^\\s]", "");
		
		return r;
	}

	
	private String subjectsToString(Vector<SubjectInterface> subjects) {
		String r="";
		for(int i=0;subjects!=null && i<subjects.size();i++) {
			r=r+subjects.elementAt(i).toString()+" ";
		}
		return r;
	}
	
	private String vectorToString(Vector<String> subjects) {
		String r="";
		for(int i=0;subjects!=null && i<subjects.size();i++) {
			r=r+subjects.elementAt(i).toString()+" ";
		}
		return r;
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
		String dbUrl="jdbc:mysql://localhost/"+sitename;
		String dbUser="root";
		String dbPassword="";
		String catalog="eutmarc";
		
		JOpac2Export2Solr ji=new JOpac2Export2Solr(dbUrl,catalog,dbUser,dbPassword);
		PrintWriter out=new PrintWriter("/home/romano/eut-solr.xml");
		ji.doJob(out);
		out.close();
		
		ji.destroy();
	}


}
