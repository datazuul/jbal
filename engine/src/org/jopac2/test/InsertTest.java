package org.jopac2.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import org.jopac2.engine.NewSearch.DoSearchNew;
import org.jopac2.engine.command.JOpac2Import;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.dbGateway.StaticDataComponent;
import org.jopac2.engine.listSearch.ListSearch;
import org.jopac2.engine.parserRicerche.parser.exception.ExpressionException;
import org.jopac2.engine.utils.MyTimer;
import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.RecordReader;
import org.junit.After;
import org.junit.Before;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import junit.framework.TestCase;

public class InsertTest extends TestCase {
	private static String sitename = "sebina";
	private InputStream in = null;
	private static String filetype = "sebina";
	private static String JOpac2confdir = "src/org/jopac2/conf";
	//private static String dbUrl = "jdbc:derby:/siti/jopac2/catalogs/db" + sitename + ";create=true";
	private static String dbUrl = "jdbc:mysql://localhost/db" + sitename;
	private static String dbUser = "root";
	private static String dbPassword = "";
	private static String catalog=sitename;

	private static String _classMySQLDriver = "com.mysql.jdbc.Driver";
	private static String _classHSQLDBDriver = "org.hsqldb.jdbcDriver";
	private static String _classDerbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
	private DoSearchNew doSearchNew;
	private static boolean ru = false;
	private Connection conn;

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
		System.out.println("presa");

		return conn;
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
		if (!ru) {
			String inFile = new String(
					Base64
							.decode(org.jopac2.test.Base64DataExample.unimarcBase64DataExample),
					"utf-8");

			String[] recs=inFile.split("\n");
			
//			RecordInterface ma=RecordFactory.buildRecord(0, "", filetype, 0);
//			RecordReader r=ma.getRecordReader(in);
//			ma.destroy();
			
			RecordInterface ma=null;
			
			Connection c=CreaConnessione();
			DbGateway dbgw=DbGateway.getInstance("mysql", null);
			
			
			// legge il primo record
			String rec=recs[0];
			
			// carica con la procedura il primo record, per creare tutto il db, i canali di ricerca, le liste
			InputStream in1 = new ByteArrayInputStream(rec.getBytes());
			JOpac2Import ji = new JOpac2Import(in1, catalog, filetype, JOpac2confdir,
					dbUrl, dbUser, dbPassword, true, System.out, System.out);
			ji.doJob(false);
			// ji.wait();
			ji.destroy(dbUrl);
			
			// avanti con l'inserimento dei prossimi
			
			for(int i=1;i<recs.length;i++) {
				ma=RecordFactory.buildRecord(0, recs[i].getBytes(), filetype, 0);
				if(ma!=null) {
					dbgw.inserisciNotizia(c, catalog, ma);
					ma.destroy();
				}
			}
			
			c.close();
//			in.close();
			ru = true;
		}

		StaticDataComponent sd = new StaticDataComponent();
		sd.init("src/org/jopac2/conf/commons/");
		conn = CreaConnessione();
		doSearchNew = new DoSearchNew(conn, catalog, sd);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		conn.close();
	}

	private SearchResultSet doSearch(String str) throws ExpressionException,
			SQLException {
		MyTimer t = new MyTimer(new String[] { "NEW", "esecuzione" });
		t.Start();
		SearchResultSet rs = doSearchNew.executeSearch(str, false);
		t.SaveTimer("time:" + rs.getQueryTime() + "#rec:" + rs.getQueryCount());
		t.Stop();
		System.out.println(rs.getQuery() + "\n[optimized:"
				+ rs.getOptimizedQuery() + "]");
		System.out.println(t);
		return rs;
	}

	private boolean checkStringSequence(Vector<String> v1, String[] a) {
		boolean r = false;
		if (v1 != null && v1.size() == a.length) {
			r = true;
			for (int i = 0; i < v1.size(); i++) {
				if (!v1.elementAt(i).equals(a[i])) {
					r = false;
					break;
				}
			}
		}
		return r;
	}

	private boolean checkIdSequence(Vector<Long> recordIDs, long[] a) {
		boolean r = false;
		if (recordIDs != null && recordIDs.size() == a.length) {
			r = true;
			for (int i = 0; i < recordIDs.size(); i++) {
				if (recordIDs.elementAt(i) != a[i]) {
					r = false;
					break;
				}
			}
		}
		return r;
	}

	private void outputJava(Vector<String> v1) {
		for (int i = 0; v1 != null && i < v1.size(); i++) {
			System.out.println("\"" + v1.elementAt(i) + "\",\n");
		}
	}

	// **** inizio test ***//

	public void testTblTipiNotizie() throws Exception {

		/**
		 * FROM l_classi_parole lcp," // QUESTA TABELLA DEVE ESSERE LA PRIMA +
		 * "          l_classi_parole_notizie lcpn," +
		 * "          anagrafe_parole a "
		 */

		String[] tipi_notizie = { "1,sebina", "2,sosebi", "3,sbn-unix",
				"4,isisbiblo", "5,easyweb", "6,bibliowin4", "7,pregresso",
				"8,ejournal", "9,eut", "10,eutmarc" , "11,mdb"};

		Vector<String> v1 = DbGateway.dumpTable(conn, "tipi_notizie");
		boolean r1 = checkStringSequence(v1, tipi_notizie);
		if (!r1) {
			System.out.println(v1);
		}
		assertTrue("Done ", r1);
	}

	public void testTblAnagrafeParole() throws Exception {


		Vector<String> v1 = DbGateway.dumpTable(conn, "anagrafe_parole order by id");
		// outputJava(v1);
		boolean r1 = checkStringSequence(v1, org.jopac2.test.Base64DataExample.anagrafe_parole);
		if (!r1) {
			for(int i=0;i<v1.size();i++)
				System.out.println(v1.elementAt(i));
		}
		assertTrue("Done ", r1);
	}

	
	  public void testSearchOrder() throws Exception {
		SearchResultSet rs = doSearch("(TIT=in)|(TIT=der)");
		long[] unordered = { 1, 3, 6, 7, 9, 10 };
		long[] ordered = { 7, 6, 1, 10, 3, 9 };
		boolean r1 = checkIdSequence(rs.getRecordIDs(), unordered);
		//SearchResultSet.dumpSearchResultSet(conn, rs);
		DbGateway.orderBy(conn, catalog, "TIT", rs);
		//SearchResultSet.dumpSearchResultSet(conn, rs);
		boolean r2 = checkIdSequence(rs.getRecordIDs(), ordered);
		assertTrue("Done ", r1 && r2);
	}

	public void testListTIT() throws Exception {
		SearchResultSet rs = ListSearch.listSearch(conn, catalog, "TIT",
				"English grammar in use", 100);
		long[] listres = { 1, 10, 2, 11, 17, 8, 5, 3, 4, 18, 20, 9 };
		//SearchResultSet.dumpSearchResultSet(conn, rs);
		boolean r1 = checkIdSequence(rs.getRecordIDs(), listres);
		assertTrue("Done ", r1);
	}
	
	public void testListAUT() throws Exception {
		SearchResultSet rs = ListSearch.listSearch(conn, catalog, "AUT",
				"a", 100);
		long[] listres = { 17, 19, 6, 3, 8, 4, 11, 4, 9, 5, 7, 10, 15, 1, 15, 16, 2, 5, 8, 12, 10, 13, 10, 14, 7, 15, 6, 6 };
		//SearchResultSet.dumpSearchResultSet(conn, rs);
		boolean r1 = checkIdSequence(rs.getRecordIDs(), listres);
		assertTrue("Done ", r1);
	}
	 

}
