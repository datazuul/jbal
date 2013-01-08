package org.jopac2.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jopac2.engine.parserricerche.parser.exception.ExpressionException;
import org.junit.After;
import org.junit.Before;

import junit.framework.TestCase;

public class MainBug extends TestCase {
	
	public Connection conn;
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

	// creazione di parole con stemma nullo
	public void testBug1_parole_stemma_nulli() throws ExpressionException {
		String sql="select count(*) from anagrafe_parole where parola is null or stemma is null";
		Statement st;
		long cnt=-1;
		try {
			st = conn.createStatement();
			ResultSet rs=st.executeQuery(sql);
			rs.next();
			cnt=rs.getLong(1);
			rs.close();
			st.close();		
		} 
		catch (SQLException e) {
			e.printStackTrace();
			cnt=-2;
		}
		assertTrue(sql + " count:"+cnt ,cnt==0);
	}	
	
	// id_parole  o id_classi a 0
	public void testBug2_id_zero() throws ExpressionException {
		String sql="select count(*) from l_classi_parole where id_parola = 0 or id_classe = 0";
		Statement st;
		long cnt=-1;
		try {
			st = conn.createStatement();
			ResultSet rs=st.executeQuery(sql);
			rs.next();
			cnt=rs.getLong(1);
			rs.close();
			st.close();		
		} 
		catch (SQLException e) {
			e.printStackTrace();
			cnt=-2;
		}
		assertTrue(sql + " count:"+cnt ,cnt==0);
	}
	
	// notizie non puntate da l_classi_parole_notizie
	public void testBug3_notizie_orfane() throws ExpressionException {
		String sql="select count(*) from notizie n where n.id not in (select id_notizia from l_classi_parole_notizie lcpn)";
		Statement st;
		long cnt=-1;
		try {
			st = conn.createStatement();
			ResultSet rs=st.executeQuery(sql);
			rs.next();
			cnt=rs.getLong(1);
			rs.close();
			st.close();		
		} 
		catch (SQLException e) {
			e.printStackTrace();
			cnt=-2;
		}
		assertTrue(sql + " count:"+cnt ,cnt==0);
	}	
	
	// parole non puntate da l_classi_parole
	public void testBug4_parole_orfane() throws ExpressionException {
		String sql="select count(*) from anagrafe_parole a where a.id not in (select id_parola from l_classi_parole lcp)";
		Statement st;
		long cnt=-1;
		try {
			st = conn.createStatement();
			ResultSet rs=st.executeQuery(sql);
			rs.next();
			cnt=rs.getLong(1);
			rs.close();
			st.close();		
		} 
		catch (SQLException e) {
			e.printStackTrace();
			cnt=-2;
		}
		assertTrue(sql + " count:"+cnt ,cnt==0);
	}	

	
}
