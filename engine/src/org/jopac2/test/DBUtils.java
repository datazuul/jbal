package org.jopac2.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.Vector;

import org.jopac2.engine.dbengine.dbGateway.DbGateway;
import org.jopac2.engine.dbengine.importers.DataImporter;
import org.jopac2.engine.dbengine.newsearch.DoSearchNew;
import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.jbal.RecordInterface;

public class DBUtils {

	public static String DBNAME = "dbTest";
	public static int NUM_CONNESSIONI=5;
	
	//public static String PATH="C:/Docs/mysource/JOpac2/";
	public static String PATH="/java_source/keiko/";
	
	public static void Prepara(String catalog, boolean completo) throws Exception{
		if(completo){
//			CreaDB();
			CaricaDati(catalog);
		}
	} 
	
//	public static void CreaDB(String catalog) throws SQLException{
//		Connection conn=CreadbMySql.CreaConnessione("mysql");
//		DbGateway.dropDB(conn,catalog,DBNAME);
//		DbGateway.createDB(conn, catalog, DBNAME);
//		conn.close();
//
//	}
	
	/**
	 * @deprecated
	 * @throws FileNotFoundException
	 */
	public static void CaricaDati(String catalog) throws FileNotFoundException{
		FileInputStream is =new FileInputStream(new File(PATH+"docs/DBTest/kp.iso"));
		String ft="Isisbiblo";
		
		Connection[] connessioni=new Connection[NUM_CONNESSIONI];
		for(int i=0;i<NUM_CONNESSIONI;i++)
			connessioni[i]=CreadbMySql.CreaConnessione(DBNAME);
		
		DataImporter d = new DataImporter(is,ft,PATH+"WebContent/WEB-INF/conf",connessioni,catalog, true,null, System.out, System.out); //,null);
		System.out.println("caricamento dati...");
		d.start();
		try {
			d.join();
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}
		System.out.println("...completato");
	}
	
	/*
	 * inizializza classe per le ricerche, generando un oggetto sd stile Cocoon
	 */
	public static DoSearchNew InitDoSearch(Connection conn, String catalog){
//		StaticDataComponent sd = new StaticDataComponent();
//		sd.init(PATH+"WebContent/");
		DoSearchNew doSearchNew;
		doSearchNew = new DoSearchNew(conn,catalog);
		return doSearchNew;
	}
	
	public static void dumpSearchResultSet(Connection conn, String catalog, SearchResultSet rs){
		dumpSearchResultSet(conn, catalog, rs,null);
	}
	
	public static void dumpSearchResultSet(Connection conn, String catalog, SearchResultSet rs, String f){
		Vector<Long> v = rs.getRecordIDs();
		System.out.println(v);
		for (int i = 0; i < v.size(); i++) {
			RecordInterface m=null;
			try {
				m = DbGateway.getNotiziaByJID(conn, catalog, v.elementAt(i).toString());
				String o=null;
				if(f!=null) o=m.getField(f);
				if(o==null) o=m.getTitle();
				System.out.println(o);
			}
			catch(Exception e) {}
			finally {
				if(m!=null) m.destroy();
			}
		}
	}

}
