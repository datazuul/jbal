package org.jopac2.engine.command;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.dbGateway.ParoleSpooler;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.iso2709.Eutmarc;
import org.jopac2.jbal.stemmer.Radice;
import org.jopac2.jbal.stemmer.StemmerItv2;
import org.jopac2.viewer.XmlMarcHandler;
import org.xml.sax.SAXException;

import com.whirlycott.cache.Cache;
import com.whirlycott.cache.CacheException;

public class ReimportFromXML {
	
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		ReimportFromXML re=new ReimportFromXML();
		re.doJob();
	}
	
	private void doJob() throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory spf=SAXParserFactory.newInstance();
		SAXParser sp=spf.newSAXParser();
		sp.parse("/Java_src/java_jopac2/JOpac2/iso-test/eutmarc/25.xml", new XmlImportMarcHandler("collection","record"));
	}
	
	

	public class XmlImportMarcHandler extends XmlMarcHandler {
		private String dbUrl = "jdbc:mysql://localhost/dbeut";
		private String dbUser = "root";
		private String dbPassword = "";
		public String catalog = "eutmarc";

		private String _classMySQLDriver = "com.mysql.jdbc.Driver";
		private String _classHSQLDBDriver = "org.hsqldb.jdbcDriver";
		private String _classDerbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
				
		private Connection conn = null;
		private DbGateway dbg = null;
				
		@Override
		public void process(RecordInterface ma2) {
			Radice stemmer=new StemmerItv2();

	        Cache cache=DbGateway.getCache();
	    	ParoleSpooler paroleSpooler=new ParoleSpooler(new Connection[] {conn},catalog,1,cache,stemmer,System.out);
			try {
				long jid=ma2.getJOpacID();
				if(jid==0) {
					try {
						jid=Long.parseLong(ma2.getBid());
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				
				
				
				String p=((Eutmarc)ma2).getPrice();
				
				if(p!=null) ma2.setAvailabilityAndOrPrice(p);
				
				
				if(jid==25) {
					try {
						DbGateway.cancellaNotiziaFromJid(conn, catalog, jid);
					}
					catch(Exception e) {}
					dbg.inserisciNotizia(conn, catalog, stemmer, paroleSpooler,ma2, jid);
				}
//				RecordInterface ma3=DbGateway.getNotiziaByJID(conn, catalog, jid);
//				
//				
//				String xmlFromFile=ma2.toXML();
//				String xmlFromDb=ma3.toXML();
				
//				if(!xmlFromFile.replaceAll("\\s", "").equals(xmlFromDb.replaceAll("\\s", ""))) {
//					System.out.println("No match jid: "+jid);
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					DbGateway.shutdownCache();
				} catch (CacheException e) {
					e.printStackTrace();
				}
			}
//			super.process(ma2);
		}

		public XmlImportMarcHandler(String rootElement, String recordElement) {
			super(rootElement, recordElement);
			try {
				conn=CreaConnessione();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public Connection CreaConnessione() throws SQLException {
			Connection conn = null;
			String driver = _classMySQLDriver;
			if (dbUrl.contains(":hsqldb:"))
				driver = _classHSQLDBDriver;
			if (dbUrl.contains(":derby:"))
				driver = _classDerbyDriver;

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
			dbg=DbGateway.getInstance(conn.toString(), System.out);
			System.out.println("presa");

			return conn;
		}
		
	}
}
