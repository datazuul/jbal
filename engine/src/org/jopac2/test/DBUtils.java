package org.jopac2.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

import org.jopac2.engine.NewSearch.DoSearchNew;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.dbGateway.StaticDataComponent;
import org.jopac2.engine.importers.DataImporter;

public class DBUtils {

	public static String DBNAME = "dbTest";
	public static int NUM_CONNESSIONI=5;
	
	//public static String PATH="C:/Docs/mysource/JOpac2/";
	public static String PATH="/java_source/keiko/";
	
	public static void Prepara(boolean completo) throws Exception{
		if(completo){
			CreaDB();
			CaricaDati();
		}
	} 
	
	public static void CreaDB() throws SQLException{
		Connection conn=CreadbMySql.CreaConnessione("mysql");
		DbGateway.dropDB(conn,DBNAME);
		DbGateway.createDB(conn, DBNAME);
		conn.close();

	}
	
	/**
	 * @deprecated
	 * @throws FileNotFoundException
	 */
	public static void CaricaDati() throws FileNotFoundException{
		FileInputStream is =new FileInputStream(new File(PATH+"docs/DBTest/kp.iso"));
		String ft="Isisbiblo";
		
		Connection[] connessioni=new Connection[NUM_CONNESSIONI];
		for(int i=0;i<NUM_CONNESSIONI;i++)
			connessioni[i]=CreadbMySql.CreaConnessione(DBNAME);
		
		DataImporter d = new DataImporter(is,ft,PATH+"WebContent/WEB-INF/conf",connessioni,true,null,null);
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
	public static DoSearchNew InitDoSearch(Connection conn){
		StaticDataComponent sd = new StaticDataComponent();
		sd.init(PATH+"WebContent/");
		DoSearchNew doSearchNew;
		doSearchNew = new DoSearchNew(conn,sd);
		return doSearchNew;
	}

}
