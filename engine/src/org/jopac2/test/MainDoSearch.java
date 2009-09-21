package org.jopac2.test;

import java.sql.*;

import junit.framework.TestCase;

import org.jopac2.engine.NewSearch.DoSearchNew;
import org.jopac2.engine.parserRicerche.parser.booleano.EvalAlbero;
import org.jopac2.engine.parserRicerche.parser.exception.ExpressionException;
import org.jopac2.engine.parserRicerche.tree.Nodo;
import org.jopac2.engine.utils.SearchResultSet;
import org.junit.After;
import org.junit.Before;

public class MainDoSearch extends TestCase {
	
	public Connection conn;
	public String catalog="catalog";

	@Before
	public void setUp() throws Exception {
		super.setUp();
		conn=CreadbMySql.CreaConnessione(DBUtils.DBNAME);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		conn.close();
	}

	// la ricerca di una stringa con uno spazio deve generare un and delle parole
	public void testR3() throws ExpressionException, SQLException {
		String str="ANY=opera omnia&ANY=opera";
		DoSearchNew doSearchNew=DBUtils.InitDoSearch(conn,catalog);
		SearchResultSet rs = doSearchNew.executeSearch(str, false);
		System.out.println("time:"+rs.getQueryTime()+"#rec:"+rs.getQueryCount());
		System.out.println(rs.getQuery() + "[optimized:" + rs.getOptimizedQuery() + "]");
		assertTrue(str+ " count:"+rs.getQueryCount() ,rs.getQueryCount()==64);  
	}
	
	// la ricerca di una stringa con uno spazio deve generare un and delle parole
	public void testR3a2() throws ExpressionException, SQLException {
		String str="ANY=opera omnia decem tomos&ANY=opera";
		DoSearchNew doSearchNew=DBUtils.InitDoSearch(conn,catalog);
		SearchResultSet rs = doSearchNew.executeSearch(str, false);
		System.out.println("time:"+rs.getQueryTime()+"#rec:"+rs.getQueryCount());
		System.out.println(rs.getQuery() + " [optimized:" + rs.getOptimizedQuery() + "]");
		assertTrue(str+ " count:"+rs.getQueryCount() ,rs.getQueryCount()==11);  
	}
	
	// la ricerca di una stringa con uno spazio deve generare un and delle parole
	public void testR3b() throws ExpressionException, SQLException {
		String str="ANY=opera omnia@distribuita &ANY=opera";
		DoSearchNew doSearchNew=DBUtils.InitDoSearch(conn,catalog);
		SearchResultSet rs = doSearchNew.executeSearch(str, false);
		System.out.println("time:"+rs.getQueryTime()+"#rec:"+rs.getQueryCount());
		System.out.println(rs.getQuery() + "[optimized:" + rs.getOptimizedQuery() + "]");
		assertTrue(str+ " count:"+rs.getQueryCount() ,rs.getQueryCount()==18);  
	}

	public void testR1() throws ExpressionException, SQLException {
		String str="ANY=omnia&ANY=opera";
		DoSearchNew doSearchNew=DBUtils.InitDoSearch(conn,catalog);
		SearchResultSet rs = doSearchNew.executeSearch(str, false);
		System.out.println("time:"+rs.getQueryTime()+"#rec:"+rs.getQueryCount());
		System.out.println(rs.getQuery() + "[optimized:" + rs.getOptimizedQuery() + "]");
		assertTrue(str + " count:"+rs.getQueryCount(),rs.getQueryCount()==64);
	}
	
	public void testR1b() throws ExpressionException, SQLException {
		String str="ANY = omnia   &   ANY=opera";
		DoSearchNew doSearchNew=DBUtils.InitDoSearch(conn,catalog);
		SearchResultSet rs = doSearchNew.executeSearch(str, false);
		System.out.println("time:"+rs.getQueryTime()+"#rec:"+rs.getQueryCount());
		System.out.println(rs.getQuery() + "[optimized:" + rs.getOptimizedQuery() + "]");
		assertTrue(str + " count:"+rs.getQueryCount(),rs.getQueryCount()==64);
	}
	
	// la parola fratis non e' presente in nessuna notizia?
	public void testR2() throws ExpressionException, SQLException {
		String str="ANY=fratis";
		DoSearchNew doSearchNew=DBUtils.InitDoSearch(conn,catalog);
		SearchResultSet rs = doSearchNew.executeSearch(str, false);
		System.out.println("time:"+rs.getQueryTime()+"#rec:"+rs.getQueryCount());
		System.out.println(rs.getQuery() + "[optimized:" + rs.getOptimizedQuery() + "]");
		assertTrue(str+ " count:"+rs.getQueryCount(),rs.getQueryCount()==1);  //
	}	
	
	// la ricerca di una stringa senza classi impostale classi a ANY
	public void testR4() throws ExpressionException, SQLException {
		String str="opera & omnia";
		DoSearchNew doSearchNew=DBUtils.InitDoSearch(conn,catalog);
		SearchResultSet rs = doSearchNew.executeSearch(str, false);
		System.out.println("time:"+rs.getQueryTime()+"#rec:"+rs.getQueryCount());
		System.out.println(rs.getQuery() + "[optimized:" + rs.getOptimizedQuery() + "]");
		assertTrue(str+ " count:"+rs.getQueryCount() ,rs.getQueryCount()==64);  
	}	
	
	// classe non esistente restituisce ANY
	public void testR3a() throws ExpressionException, SQLException {
		String str="PIPPO=opera&ANY=opera";
		DoSearchNew doSearchNew=DBUtils.InitDoSearch(conn,catalog);
		SearchResultSet rs = doSearchNew.executeSearch(str, false);
		System.out.println("time:"+rs.getQueryTime()+"#rec:"+rs.getQueryCount());
		System.out.println(rs.getQuery() + "[optimized:" + rs.getOptimizedQuery() + "]");
		assertTrue(str+ " count:"+rs.getQueryCount() ,rs.getQueryCount()==557);  
	}	
	public void testR5() throws ExpressionException{
		Nodo sopTree = EvalAlbero.creaAlberoJopac("TIT=omnia opera");
		sopTree.switchToSOP();
	}
}
