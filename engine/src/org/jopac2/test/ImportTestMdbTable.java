package org.jopac2.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import org.jopac2.engine.command.JOpac2Import;
import org.jopac2.engine.dbengine.dbGateway.DbGateway;
import org.jopac2.engine.dbengine.dbGateway.StaticDataComponent;
import org.jopac2.engine.dbengine.listSearch.ListSearch;
import org.jopac2.engine.dbengine.newsearch.DoSearchNew;
import org.jopac2.engine.parserricerche.parser.exception.ExpressionException;
import org.jopac2.engine.utils.MyTimer;
import org.jopac2.engine.utils.SearchResultSet;
import org.junit.After;
import org.junit.Before;


import junit.framework.TestCase;

public class ImportTestMdbTable extends TestCase {
	private static String filename="/home/romano/Downloads/mdb/rivisted.mdb";
	private static String tablename="tTitoli";
	private static String sitename = "sito";
	private InputStream in = null;
	private static String filetype = "mdb:"+tablename;
	private static String JOpac2confdir = "src/org/jopac2/conf";
//	private static String dbUrl = "jdbc:derby://localhost:1527/db"+sitename+";create=true";
//	private static String dbUrl = "jdbc:derby:/tmp/db" + sitename + ";create=true";
	private static String dbUrl = "jdbc:mysql://localhost/db" + sitename;
	private static String dbUser = "root";
	private static String dbPassword = "";
	private static String catalog="rivistemed";
	
	private static String _classMySQLDriver = "com.mysql.jdbc.Driver";
	private static String _classDerbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
//	private static String _classDerbyDriver = "org.apache.derby.jdbc.ClientDriver";

	private DoSearchNew doSearchNew;
	private static boolean ru = false;
	private Connection conn;

	public Connection CreaConnessione() throws SQLException {
		Connection conn = null;
		String driver = _classMySQLDriver;
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
			File f=new File(filename);

			in = new FileInputStream(f);

			JOpac2Import ji = new JOpac2Import(in, catalog, filetype, Charset.forName("utf-8"), JOpac2confdir,
					dbUrl, dbUser, dbPassword, true, System.out, System.out);
			ji.doJob(false);
			// ji.wait();
			ji.destroy(dbUrl);
			ru = true;
		}

//		StaticDataComponent sd = new StaticDataComponent();
//		sd.init("src/org/jopac2/conf/commons/");
		conn = CreaConnessione();
		doSearchNew = new DoSearchNew(conn, catalog);
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

		String[] tipi_notizie = { "1,bibliowin4", "2,comarc", "3,easyweb",
				"4,eutmarc", "5,isisbiblo", "6,pregresso", "7,sbnunix",
				"8,sebina", "9,sosebi", "10,mdb" };

		Vector<String> v1 = DbGateway.dumpTable(conn, "je_"+catalog+"_tipi_notizie");
		
		
		boolean r1 = checkStringSequence(v1, tipi_notizie);
		if (!r1) {
			System.out.println(v1);
		}
		assertTrue("Done ", r1);
	}


	
//	  public void testSearchOrder() throws Exception {
//		SearchResultSet rs = doSearch("(NomeRisorsa=business)|(NomeRisorsa=stampa)");
//		long[] unordered = { 24, 25, 26, 27, 28, 29, 53, 59, 60 };
//		long[] ordered = { 53, 24, 25, 26, 28, 29, 27, 59, 60 };
//		boolean r1 = checkIdSequence(rs.getRecordIDs(), unordered);
//		SearchResultSet.dumpSearchResultSet(conn, rs, "NomeRisorsa");
//		DbGateway.orderBy(conn, "NomeRisorsa", rs);
//		SearchResultSet.dumpSearchResultSet(conn, rs, "NomeRisorsa");
//		boolean r2 = checkIdSequence(rs.getRecordIDs(), ordered);
//		assertTrue("Done ", r1 && r2);
//	}
//
//	public void testList() throws Exception {
//		SearchResultSet rs = ListSearch.listSearch(conn, "NomeRisorsa",
//				"English grammar in use", 10);
//		long[] listres = { 106, 70, 22, 37, 42, 38, 43, 39, 35, 40, 36, 41, 44, 71, 17, 72, 56, 57 };
//		SearchResultSet.dumpSearchResultSet(conn, rs, "NomeRisorsa");
//		boolean r1 = checkIdSequence(rs.getRecordIDs(), listres);
//		assertTrue("Done ", r1);
//	}
	
	
	public void testList() throws Exception {
	SearchResultSet rs = ListSearch.listSearch(conn, catalog, "NomeRisorsa",
			"a", 10);
	long[] listres = { 1, 37, 5, 6, 7, 8, 9, 38, 10, 11 };
	DBUtils.dumpSearchResultSet(conn, catalog, rs, "NomeRisorsa");
	boolean r1 = TestUtils.checkIdSequence(rs.getRecordIDs(), listres);
	assertTrue("Done ", r1);
}
	
	public void testAida() throws Exception {
		SearchResultSet rs = doSearch("(NomeRisorsa=aida)");

		long[] unordered = { 5 };
		long[] ordered = { 5 };
		boolean r1 = TestUtils.checkIdSequence(rs.getRecordIDs(), unordered);
		DBUtils.dumpSearchResultSet(conn, catalog, rs, "NomeRisorsa");
		DbGateway.orderBy(conn, catalog,"NomeRisorsa", rs);
		DBUtils.dumpSearchResultSet(conn, catalog, rs, "NomeRisorsa");
		boolean r2 = TestUtils.checkIdSequence(rs.getRecordIDs(), ordered);
		assertTrue("Done ", r1 && r2);
	}
//	
//	public void testScienzeFisiche() throws Exception {
//		SearchResultSet rs = doSearch("(AreaDisciplinare=scienze fisiche)");
//
//		long[] unordered = { 71 };
//		long[] ordered = { 71 };
//		boolean r1 = checkIdSequence(rs.getRecordIDs(), unordered);
//		SearchResultSet.dumpSearchResultSet(conn, catalog, rs, "NomeRisorsa");
//		DbGateway.orderBy(conn, catalog,"NomeRisorsa", rs);
//		SearchResultSet.dumpSearchResultSet(conn, catalog, rs, "NomeRisorsa");
//		boolean r2 = checkIdSequence(rs.getRecordIDs(), ordered);
//		assertTrue("Done ", r1 && r2);
//	}

}
