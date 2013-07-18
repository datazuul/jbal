package org.jopac2.engine.dbengine;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.Vector;

import org.jopac2.engine.Engine;
import org.jopac2.engine.dbengine.dbGateway.DbGateway;
import org.jopac2.engine.dbengine.importers.DataImporter;
import org.jopac2.engine.dbengine.listSearch.ListSearch;
import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.abstractStructure.Delimiters;
import org.jopac2.jbal.stemmer.Radice;

import com.whirlycott.cache.Cache;
import com.whirlycott.cache.CacheConfiguration;
import com.whirlycott.cache.CacheException;
import com.whirlycott.cache.CacheManager;

import org.jopac2.engine.dbengine.dbGateway.ParoleSpooler;

public class DbEngine extends org.jopac2.engine.dbengine.newsearch.DoSearchNew implements Engine  {
	private int max_conn=5;

	public DbEngine(Connection conn, String catalog) throws Exception {
		super(conn,catalog);
	}

	public void orderBy(Connection conn, String catalog,
			String orderBy, SearchResultSet result) throws Exception {
		DbGateway.orderBy(conn, catalog, orderBy, result);
	}

	public RecordInterface getNotiziaByJID(String jid) throws Exception {
		return DbGateway.getNotiziaByJID(conn, catalog, jid);
	}
	
//	public RecordInterface getNotiziaByJID(long jid) throws Exception {
//		return DbGateway.getNotiziaByJID(conn, catalog, jid);
//	}

	public SearchResultSet listSearchBackward(String classe, String parole,
			int limit) throws Exception {
		return ListSearch.listSearchBackward(conn, catalog, classe, parole, limit);
	}

	public SearchResultSet listSearch(String classe, String parole, int limit)
			throws Exception {
		return ListSearch.listSearch(conn, catalog, classe, parole, limit);
	}

	public SearchResultSet listSearchBackward(String classe, long sjid,
			int limit) throws Exception {
		return ListSearch.listSearchBackward(conn, catalog, classe, sjid, limit);
	}

	public SearchResultSet listSearch(String classe, long sjid, int limit)
			throws Exception {
		return ListSearch.listSearch(conn, catalog, classe, sjid, limit);
	}

	public Vector<String> getJIDbyBID(Connection conn, String catalogConnection,
			String bid) throws Exception {
		return DbGateway.getJIDbyBID(conn, catalogConnection, bid);
	}

	public long getMaxId() throws Exception {
		return DbGateway.getMaxIdTable(conn, "je_"+catalog+"_notizie");
	}

	public void updateRecord(Radice stemmer, RecordInterface m) throws Exception {
		Cache cache=null;
//		cache=DbGateway.getCache();
		DbGateway dbg=DbGateway.getInstance("mysql", null);
    	ParoleSpooler paroleSpooler=new ParoleSpooler(new Connection[] {conn},catalog,1,cache,stemmer,System.out);
		dbg.updateNotizia(conn, catalog, stemmer, paroleSpooler, m);
//		try {
//			if(cache!=null) DbGateway.shutdownCache();
//		} catch (CacheException e) {
//			e.printStackTrace();
//		}
	}

	public void deleteRecordFromJid(String jid) throws Exception {
		DbGateway.cancellaNotiziaFromJid(conn, catalog, jid);
	}

	public String insertRecord(Radice stemmer, RecordInterface record) throws Exception {
		ParoleSpooler paroleSpooler=new ParoleSpooler(new Connection[] {conn},catalog,1,null,stemmer,System.out);
		DbGateway dbg=DbGateway.getInstance("mysql", null);
		return dbg.inserisciNotizia(conn, catalog, stemmer, paroleSpooler, record);
	}
	
	public void insertRecord(Radice stemmer, RecordInterface record, String jid) throws Exception {
		ParoleSpooler paroleSpooler=new ParoleSpooler(new Connection[] {conn},catalog,1,null,stemmer,System.out);
		DbGateway dbg=DbGateway.getInstance("mysql", null);
		dbg.inserisciNotizia(conn, catalog, stemmer, paroleSpooler, record, jid);
	}

	public void importRecords(InputStream in, Charset charset, String format, boolean background,
			PrintStream console, PrintStream console2) throws Exception {
		Connection[] conns=new Connection[1];
		conns[0]=conn;

		DbGateway dbg=DbGateway.getInstance(conn.toString(), console);
		String dbUrl=conn.getMetaData().getURL();

//		for(int i=0;i<conns.length;i++) {
//			conns[i] = dbg.createConnection(dbUrl, dbUser, dbPassword);// this.getConnection(dbname);
//		}
		
		Cache cache=null;
		
		try {
			CacheConfiguration cacheConf=new CacheConfiguration();
			cacheConf.setTunerSleepTime(10);
			cacheConf.setBackend("com.whirlycott.cache.impl.ConcurrentHashMapImpl");
			cacheConf.setMaxSize(100000);
			cacheConf.setPolicy("com.whirlycott.cache.policy.LFUMaintenancePolicy");
			cacheConf.setName("JOpac2cache");
			cache= CacheManager.getInstance().createCache(cacheConf);
			//cache = CacheManager.getInstance().getCache();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		DataImporter dataimporter=new DataImporter(in,format,charset, null, dbUrl, conns, catalog, true, cache, console,console); //,t);
		if(background) {
			dataimporter.start();
		}
		else {
			dataimporter.doJob();
			
		}		
	}

	public String[] getChannels(String type) throws Exception {
		return DbGateway.getChannels(conn, catalog, type);
	}

	public void truncateCatalog() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
