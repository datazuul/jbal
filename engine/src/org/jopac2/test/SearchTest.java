package org.jopac2.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import junit.framework.TestCase;

public class SearchTest extends TestCase {
	private static String sitename = "eut";
	private InputStream in = null;
	private static String filetype = "eutmarc";
	private static String JOpac2confdir = "src/org/jopac2/conf";
	/**
	 * 
	 * */
//	private static String dbUrl = "jdbc:derby:/Java_src/java_master/prestito/derbydb/db" + sitename + ";create=true";
	private static String dbUrl = "jdbc:mysql://localhost/db" + sitename;
	private static String dbUser = "root";
	private static String dbPassword = "";
	private static String catalog="eutmarc";

	private static String _classMySQLDriver = "com.mysql.jdbc.Driver";
	private static String _classDerbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
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
		
//		if (!ru) {
//			String inFile = new String(
//					Base64
//							.decode(org.jopac2.test.Base64DataExample.unimarcBase64DataExample),
//					"utf-8");
//
//			in = new ByteArrayInputStream(inFile.getBytes());
//
//			JOpac2Import ji = new JOpac2Import(in, catalog, filetype, JOpac2confdir,
//					dbUrl, dbUser, dbPassword, true, System.out, System.out);
//			ji.doJob(false);
//			// ji.wait();
//			ji.destroy(dbUrl);
//			ru = true;
//		}

//		StaticDataComponent sd = new StaticDataComponent();
//		sd.init("src/org/jopac2/conf/commons/");
		conn = CreaConnessione();
//		DbGateway dbGateway=DbGateway.getInstance(conn.toString(),System.out);
//		dbGateway.rebuildList(conn,catalog);
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

	

	@SuppressWarnings("unused")
	private void outputJava(Vector<String> v1) {
		for (int i = 0; v1 != null && i < v1.size(); i++) {
			System.out.println("\"" + v1.elementAt(i) + "\",\n");
		}
	}

	// **** inizio test ***//
	  public void testSearchOrder() throws Exception {
			SearchResultSet rs = doSearch("(DTE=2010)");
			long[] unordered = { 1, 3, 6, 7, 9, 10 };
			long[] ordered = { 7, 6, 1, 10, 3, 9 };
			boolean r1 = TestUtils.checkIdSequence(rs.getRecordIDs(), unordered);
			DBUtils.dumpSearchResultSet(conn, catalog, rs);
			DbGateway.orderBy(conn, catalog,"AUT", rs);
			DBUtils.dumpSearchResultSet(conn, catalog, rs);
			boolean r2 = TestUtils.checkIdSequence(rs.getRecordIDs(), ordered);
			assertTrue("Done ", r1 && r2);
		}
	
//	
//	public void testTblTipiNotizie() throws Exception {
//		String[] tipi_notizie = {"1,bibliowin4", "2,comarc", "3,easyweb", 
//				"4,eutmarc", "5,isisbiblo", "6,pregresso", "7,sbnunix", 
//				"8,sebina", "9,sosebi", "10,mdb"};
//
//
//		Vector<String> v1 = DbGateway.dumpTable(conn, "je_"+catalog+"_tipi_notizie");
//		boolean r1 = checkStringSequence(v1, tipi_notizie);
//		if (!r1) {
//			System.out.println(v1);
//		}
//		assertTrue("Done ", r1);
//	}
//
//	
//	public void testTblAnagrafeParole() throws Exception {
//		Vector<String> v1 = DbGateway.dumpTable(conn, "je_"+catalog+"_anagrafe_parole order by id");
//		// outputJava(v1);
//		boolean r1 = checkStringSequence(v1, org.jopac2.test.Base64DataExample.anagrafe_parole);
//		if (!r1) {
//			System.out.println(v1);
//		}
//		assertTrue("Done ", r1);
//	}
//
//	
//
//
//	public void testListTIT() throws Exception {
//		SearchResultSet rs = ListSearch.listSearch(conn, catalog, "TIT",
//				"English grammar in use", 100);
//		long[] listres = { 1, 10, 2, 11, 17, 8, 5, 3, 4, 18, 20, 9 };
////		SearchResultSet.dumpSearchResultSet(conn, rs);
//		boolean r1 = checkIdSequence(rs.getRecordIDs(), listres);
//		assertTrue("Done ", r1);
//	}
//	
//	public void testListAUT() throws Exception {
//		SearchResultSet rs = ListSearch.listSearch(conn, catalog, "AUT",
//				"a", 100);
//		long[] listres = { 17, 19, 6, 3, 8, 4, 11, 4, 9, 5, 7, 10, 15, 1, 15, 16, 2, 5, 8, 12, 10, 13, 10, 14, 7, 15, 6, 6 };
//		SearchResultSet.dumpSearchResultSet(conn, catalog, rs);
//		boolean r1 = checkIdSequence(rs.getRecordIDs(), listres);
//		assertTrue("Done ", r1);
//	}
//	
//	public void testListTITBackward() throws Exception {
//		SearchResultSet rs = ListSearch.listSearchBackward(conn, catalog, "TIT",
//				"English grammar in use", 100);
//		long[] listres = { 16, 14, 15, 12, 13, 6, 7, 19 };
//		SearchResultSet.dumpSearchResultSet(conn, catalog, rs);
//		boolean r1 = checkIdSequence(rs.getRecordIDs(), listres);
//		assertTrue("Done ", r1);
//	}

}
