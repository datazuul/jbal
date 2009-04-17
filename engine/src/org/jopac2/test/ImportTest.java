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
import org.jopac2.engine.parserRicerche.parser.exception.ExpressionException;
import org.jopac2.engine.utils.MyTimer;
import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.jbal.RecordInterface;
import org.junit.After;
import org.junit.Before;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import junit.framework.TestCase;

public class ImportTest extends TestCase {
	private static String sitename="test";
	private InputStream in=null;
	private static String filetype="sebina";
	private static String JOpac2confdir="/java_jopac2/engine/src/org/jopac2/conf";
	//String dbUrl = "jdbc:derby:db"+sitename+";create=true";
	private static String dbUrl="jdbc:mysql://localhost/db"+sitename;
	private static String dbUser="root";
	private static String dbPassword="";
	
	private static String _classMySQLDriver = "com.mysql.jdbc.Driver";
	private static String _classHSQLDBDriver = "org.hsqldb.jdbcDriver";
    private static String _classDerbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
    private DoSearchNew doSearchNew;
    
	private Connection conn;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		String inFile=new String(Base64.decode(org.jopac2.test.Base64DataExample.unimarcBase64DataExample),"utf-8");
		
		in = new ByteArrayInputStream(inFile.getBytes()); 
		
		JOpac2Import ji=new JOpac2Import(in,filetype,JOpac2confdir,dbUrl,dbUser,dbPassword,true);
		ji.doJob(false);
		//ji.wait();
		ji.destroy(dbUrl);
		
		StaticDataComponent sd = new StaticDataComponent();
		sd.init("/java_jopac2/engine/src/org/jopac2/conf/commons/");
		conn=CreaConnessione();
		doSearchNew = new DoSearchNew(conn,sd);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		conn.close();
	}
	
	private void dumpSearchResultSet(SearchResultSet rs){
		Vector<Long> v = rs.getRecordIDs();
		System.out.println(v);
		for (int i = 0; i < v.size(); i++) {
			RecordInterface m = DbGateway.getNotiziaByJID(conn, v.elementAt(i).toString());			
			System.out.println(m.getTitle());
		}
	}
	
	private SearchResultSet TestClasseNew(String str) throws ExpressionException, SQLException {		
		MyTimer t=new MyTimer(new String[] {"NEW","esecuzione"});
		t.Start();
		SearchResultSet rs = doSearchNew.executeSearch(str, false);
		t.SaveTimer("time:"+rs.getQueryTime()+"#rec:"+rs.getQueryCount());
		t.Stop();
		System.out.println(rs.getQuery() + "\n[optimized:" + rs.getOptimizedQuery() + "]");
		System.out.println(t);
		return rs;	
	}

	public void testSearchOrder() throws Exception {
		SearchResultSet rs = TestClasseNew("(TIT=in)|(TIT=der)");
		
		long[] unordered={1, 3, 6, 7, 9, 10};
		long[] ordered={7, 6, 1, 10, 3, 9};
		
		boolean r1=checkIdSequence(rs.getRecordIDs(),unordered);
		
		dumpSearchResultSet(rs);
		
		DbGateway.orderBy(conn, "TIT", rs);
		
		dumpSearchResultSet(rs);
		
		boolean r2=checkIdSequence(rs.getRecordIDs(),ordered);
		
		assertTrue("Done " ,r1 && r2);
	}	

	private boolean checkIdSequence(Vector<Long> recordIDs, long[] a) {
		boolean r=false;
		if(recordIDs!=null && recordIDs.size()==a.length) {
			r=true;
			for(int i=0;i<recordIDs.size();i++) {
				if(recordIDs.elementAt(i)!=a[i]) {
					r=false;
					break;
				}
			}
		}
		return r;
	}

	public Connection CreaConnessione() throws SQLException {
		Connection conn=null;
		String driver=_classMySQLDriver;
		if(dbUrl.contains(":hsqldb:")) driver=_classHSQLDBDriver;
		if(dbUrl.contains(":derby:")) driver=_classDerbyDriver;
		
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
		conn = DriverManager.getConnection(dbUrl, dbUser,
				dbPassword);
		System.out.println("presa");

		return conn;
	}
}
