package org.jopac2.engine.command;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jopac2.engine.dbengine.dbGateway.DbGateway;
import org.jopac2.engine.dbengine.dbGateway.ParoleSpooler;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.iso2709.Eutmarc;
import org.jopac2.jbal.stemmer.Radice;
import org.jopac2.jbal.stemmer.StemmerItv2;
import org.jopac2.commands.XmlMarcHandler;
import org.xml.sax.SAXException;

import com.whirlycott.cache.Cache;
import com.whirlycott.cache.CacheException;

public class ReimportFromXML {
	private String catalog="sutrs";
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		ReimportFromXML re=new ReimportFromXML();
		re.doJob();
	}
	
	private void doJob() throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory spf=SAXParserFactory.newInstance();
		SAXParser sp=spf.newSAXParser();
		sp.parse("/Java_src/java_jopac2/JOpac2/iso-test/pregresso/sutrs.xml", new XmlImportMarcHandler("collection","record"));
	}
	
	

	public class XmlImportMarcHandler extends XmlMarcHandler {
		private String dbUrl = "jdbc:mysql://localhost/dbsutrs";
		private String dbUser = "root";
		private String dbPassword = "";

		private String _classMySQLDriver = "com.mysql.jdbc.Driver";
		private String _classHSQLDBDriver = "org.hsqldb.jdbcDriver";
		private String _classDerbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
				
		private Connection conn = null;
		private DbGateway dbg = null;
		private ParoleSpooler paroleSpooler=null;
		Radice stemmer=null;
		Cache cache=null;
		int inserted=0;
				
		@Override
		public void process(RecordInterface ma2) {
			if(conn==null)
				try {
				conn=CreaConnessione();
				
			} catch (Exception e) {
				e.printStackTrace();
			}

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
			
				try {
					DbGateway.cancellaNotiziaFromJid(conn, catalog, jid);
				}
				catch(Exception e) {}
				dbg.inserisciNotizia(conn, catalog, stemmer, paroleSpooler,ma2, jid);
				inserted++;
				if(inserted%1000==0) System.out.println("Inserted: "+inserted);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {

			}
//			super.process(ma2);
		}
		

		public void endDocument() throws SAXException {
			try {
			DbGateway.shutdownCache();
		} catch (CacheException e) {
			e.printStackTrace();
		}
			super.endDocument();
		}



		public XmlImportMarcHandler(String rootElement, String recordElement) {
			super(rootElement, recordElement);
			stemmer=new StemmerItv2();
			cache=DbGateway.getCache();
			if(conn==null)
				try {
				conn=CreaConnessione();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			paroleSpooler=new ParoleSpooler(new Connection[] {conn},catalog,1,cache,stemmer,System.out);
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
			dbg.createAllTables(conn, catalog);
			
			RecordInterface ma=null;
			try {
				ma = RecordFactory.buildRecord(0, null, "pregresso",0);
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			String[] channels=ma.getChannels();
			ma.destroy();
			
			try {
				dbg.rebuildList(conn,catalog,channels);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			System.out.println("presa");
			return conn;
		}
		
	}
}
