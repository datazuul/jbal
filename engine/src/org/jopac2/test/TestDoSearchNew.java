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

	private static RecordInterface getNotizia(String n) {
		String textOut = new String();
		ResultSet rs = null;
		RecordInterface ma = null;
		try {
			Statement stmt = conn.createStatement();
			rs = stmt
					.executeQuery("select * from notizie,tipi_notizie where notizie.id='"
							+ n + "' and tipi_notizie.id=notizie.id_tipo");
			//Vector v;
			long idDTipo = 0;
			while (rs.next()) {
				String tipo = rs.getString("nome");
				//idDTipo=((Long)tipi.get(tipo)).longValue();
				//crea notizia padre con livello=0
				ma=RecordFactory.buildRecord(rs.getLong("notizie.id"), rs.getString("notizia"), tipo, 0);
				//ma = ISO2709.creaNotizia(rs.getLong("notizie.id"), rs
				//		.getString("notizia"), tipo, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ma = null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
		}
		return ma;
	}

	private static void TestClasseNew(String str) throws ExpressionException {		
		MyTimer t=new MyTimer(new String[] {"NEW","esecuzione"});
		t.Start();
		SearchResultSet rs = doSearchNew.executeSearch(str, false);
		t.SaveTimer("time:"+rs.getQueryTime()+"#rec:"+rs.getQueryCount());
		t.Stop();
		System.out.println(rs.getQuery() + "\n[optimized:" + rs.getOptimizedQuery() + "]");
		System.out.println(t);		
		/*		 
		Vector v = rs.getRecordIDs();
		System.out.println(v);
		for (int i = 0; i < v.size(); i++) {
			ISO2709 m = getNotizia(v.elementAt(i).toString());
			Pregresso p = (Pregresso) m;			
			System.out.println(p.getTitle());
		}
		*/		
	}
	
	public static void testListe() throws SQLException {
		String id_classe="2";
		String from_parola="grammar";
		
		Hashtable<String,Integer> h=new Hashtable<String,Integer>();
		String sql="SELECT distinct id_parola,posizione_parola,parola " +
			"FROM notizie_posizione_parole a, anagrafe_parole p " +
			"WHERE a.id_parola=p.id and a.id_classe="+id_classe+" and a.posizione_parola=0 " +
			"and parola>='"+from_parola+"' " +
			"ORDER BY p.parola " +
			"LIMIT 100";

	
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()) {
				String sql0="SELECT  b.id_notizia, b.id_sequenza_tag "+
							"FROM notizie_posizione_parole b " +
							"WHERE " +
							"b.id_parola=" + rs.getInt("id_parola") +
							" and b.id_classe=" + id_classe + 
							" and b.posizione_parola=0";
				
				Statement stmt0=conn.createStatement();
				ResultSet rs0=stmt0.executeQuery(sql0);
				while(rs0.next()) {
					int id_notizia=rs0.getInt("id_notizia");
					int id_sequenza_tag=rs0.getInt("id_sequenza_tag");

					String sql1="SELECT b.id_sequenza_tag,b.id_parola,b.posizione_parola,p.parola " +
								"FROM notizie_posizione_parole b, anagrafe_parole p " +
								"WHERE  b.id_parola=p.id and b.id_notizia=" + id_notizia + " " +
								"and b.id_classe=" + id_classe + " " +
								"and b.id_sequenza_tag="+id_sequenza_tag +" " +
								"order by id_notizia, posizione_parola";
					Statement stmt1=conn.createStatement();
					ResultSet rs1=stmt1.executeQuery(sql1);
					//int cur_id_notizia=0;
					String cur_name="";

					while(rs1.next()) {
						cur_name+=rs1.getString("parola")+" ";
					}
					cur_name=cur_name.trim();
					//System.out.println(cur_name);
					if(h.containsKey(cur_name)) {
						h.put(cur_name, new Integer(h.get(cur_name).intValue()+1));
					}
					else {
						h.put(cur_name, new Integer(1));
					}
					rs1.close();
					stmt1.close();
				}
				rs0.close();
				stmt0.close();
		}
		rs.close();
		stmt.close();
		conn.close();
		Vector<String> v=new Vector<String>(h.keySet());
		Collections.sort(v);
		for(int i=0;i<v.size();i++) {
			String c=v.elementAt(i);
			System.out.println(c+": "+h.get(c));
		}
		/*Vector v=DbGateway.listRecords(conn, 1, "altan");
		for (int i=0;i<v.size();i++)
			System.out.println(v.elementAt(i));
		*/
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