package org.jopac2.test;
/*******************************************************************************
*
*  JOpac2 (C) 2002-2007 JOpac2 project
*
*     This file is part of JOpac2. http://jopac2.sourceforge.net
*
*  JOpac2 is free software; you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation; either version 2 of the License, or
*  (at your option) any later version.
*
*  JOpac2 is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with JOpac2; if not, write to the Free Software
*  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*
*******************************************************************************/
import java.sql.*;


import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.SortedMap;
import java.util.Vector;

import org.jopac2.engine.NewSearch.DoSearchNew;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.dbGateway.StaticDataComponent;
import org.jopac2.engine.parserRicerche.parser.exception.ExpressionException;
import org.jopac2.engine.utils.MyTimer;
import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;


/**
 * @author Albert Caramia
 */
@SuppressWarnings("unused")
public class TestDoSearchNew {
	//private static String _classDriver = "org.hsqldb.jdbcDriver";//"com.mysql.jdbc.Driver";
	//private static String _dbMasterUrl = "jdbc:hsqldb:hsql://localhost:9002/";//"jdbc:mysql://localhost/";
	//private static String _dbUser = "sa";//"root";
	//private static String _dbName = "jopac2example";
	private static String _classDriver = "com.mysql.jdbc.Driver";
	private static String _dbMasterUrl = "jdbc:mysql://localhost/";	
	private static String _dbUser = "root";
	private static String _dbPass = "";
	private static String _dbName = "dbsebina"; 
	private static Connection conn = null;
	
	private static DoSearchNew doSearchNew;

	public static Connection CreaConnessione() {
		boolean inizializzato = false;
		if (!inizializzato) {
			inizializzato = true;
			try {
				Class.forName(_classDriver).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			System.out.println("getting conn....");
			conn = DriverManager.getConnection(_dbMasterUrl + _dbName, _dbUser,
					_dbPass);
			System.out.println("presa");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	

	
	private static void TestConnessione() {
		try {
			Statement s = conn.createStatement();
			ResultSet r = s.executeQuery("select count(*) from notizie");
			//DbGateway.desc(conn,"select count(*) from notizie");
			r.next();
			long e = r.getLong(1);
			if (e <1) {
				System.out.println("errore");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void dumpSearchResultSet(SearchResultSet rs){
		Vector<Long> v = rs.getRecordIDs();
		System.out.println(v);
		for (int i = 0; i < v.size(); i++) {
			RecordInterface m = DbGateway.getNotiziaByJID(conn, v.elementAt(i).toString());			
			System.out.println(m.getTitle());
		}
	}
	
	private static void TestClasseNew(String str) throws ExpressionException, SQLException {		
		MyTimer t=new MyTimer(new String[] {"NEW","esecuzione"});
		t.Start();
		SearchResultSet rs = doSearchNew.executeSearch(str, false);
		t.SaveTimer("time:"+rs.getQueryTime()+"#rec:"+rs.getQueryCount());
		t.Stop();
		System.out.println(rs.getQuery() + "\n[optimized:" + rs.getOptimizedQuery() + "]");
		System.out.println(t);
		
		dumpSearchResultSet(rs);
		DbGateway.orderBy(conn, "TIT", rs);
		dumpSearchResultSet(rs);	
	}
	
	
	private static void execute(String sql) {
        try {
            Statement stmt=conn.createStatement();
            stmt.execute(sql);
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }	
		
	public static void Init(){
		CreaConnessione();
		TestConnessione();	
		StaticDataComponent sd = new StaticDataComponent();
		sd.init("/java_jopac2/engine/src/org/jopac2/conf/commons/");
		doSearchNew = new DoSearchNew(conn,sd);
	}
	
	
	
	public static void main(String[] args) throws ExpressionException, SQLException {
		Init();	
		
		TestClasseNew("TIT=grammar");
		
		//testListe();
		/*
		String str="";
		str="2=in&2=sergio&2=trampus&2=pippo";
		str="2=i&2=promessi&2=sposi&(2=trampus|2=alighieri|2=caramia|2=manzoni)";		
		//System.out.println("deve rest #rec:159");
		str="TIT=i&TIT=promessi&TIT=sposi&(TIT=trampus|TIT=alighieri|TIT=caramia|TIT=manzoni)";		
		//str="TIT=africa&ANY=lyons";			
		//str="TIT=africa&AUT=lyons";
		//str="ANY=murakami&ANY=yasusuke|ANY=trampus";
		//str="ANY=murakami&ANY=yasusuke|ANY=tramp%";
		//str="TIT=The&TIT=POLITICAL&TIT=economy&TIT=of&TIT=Japan&TIT=Iger&TIT=eral&TIT=editors&TIT=Yasusuke&TIT=Murakami&TIT=and&TIT=Hugh&TIT=T&TIT=Patrick";
		//str="TIT=The&TIT=POLITICAL&TIT=economy&TIT=of&TIT=Japan&TIT=/&TIT=Iger&TIT=eral&TIT=editors&TIT=Yasusuke&TIT=Murakami&TIT=and&TIT=Hugh&TIT=T&TIT=Patrick&TIT=j&TIT=Stanford&TIT=Ca&TIT=:&TIT=Stanford&TIT=University&TIT=Press&TIT=1987&TIT=v&TIT=24&TIT=cm";
		str="ANY=grammar";
		TestClasseNew("AUT=grammar&ANY=english");
		TestClasseNew("ANY=grammar");
		//TestClasseNew(str);
		//TestClasseNew("ANY=grammar&AUT=english");
		//testListe();*/
	}
}