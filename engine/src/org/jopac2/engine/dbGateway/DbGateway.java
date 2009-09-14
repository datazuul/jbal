package org.jopac2.engine.dbGateway;

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

import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.jopac2.engine.dbGateway.derby.derby;
import org.jopac2.engine.dbGateway.hsqldb.hsqldb;
import org.jopac2.engine.dbGateway.mysql.mysql;
import org.jopac2.engine.importers.LoadClasses;
import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.engine.utils.ZipUnzip;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.jbal.xmlHandlers.ClassItem;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.ClasseDettaglio;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.ObjectPair;
import org.jopac2.utils.TokenWord;
import org.jopac2.utils.Utils;

import com.whirlycott.cache.Cache;
import com.whirlycott.cache.CacheConfiguration;
import com.whirlycott.cache.CacheManager;


public abstract class DbGateway {
	  /** @todo  le costanti vanno messe in file di configurazione!!! */
	
	// 18.07.2006 RT tolto * da SEPARATORI PAROLE per usarlo come inizio parole da indicizzare
	  public static final String SEPARATORI_PAROLE=" ,.;()/-'\\:=@%$&!?[]#<>\016\017\n\t";
	  static final int MAX_POSIZIONE_ASTERISCO=5;
	  static final boolean DEBUG=true;
	  private PrintStream out =null;
	  /** @todo  doppio asterisco. che vuol dire?
	   *   vul dire che le indicazioni di responsabilita' vengono indicizzate
	   *   con al massimo fino a 4 parole chiave che
	   *   vengono segnate dall'asterisco.*/
	  
	  public DbGateway(PrintStream console) {
		  out=console;
	  }


	/**
	 * Returns the number of record present in table 'notizie'
	 * @param conn
	 * @return
	 */
	public static long getCountNotizie(Connection conn) {
		long r=0;
		try {
		    Statement stmt=conn.createStatement();
		    ResultSet rs=stmt.executeQuery("SELECT count(*) c from notizie");
		    if(rs.next()) {
		        r=rs.getLong("c");
		    }
		    rs.close();
		    stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * Returns the number of record present in table anagrafe_parole
	 * @param conn
	 * @return
	 */
	public static long getCountParole(Connection conn) {
		long r=0;
		try {
		    Statement stmt=conn.createStatement();
		    ResultSet rs=stmt.executeQuery("SELECT count(*) c from anagrafe_parole");
		    if(rs.next()) {
		        r=rs.getLong("c");
		    }
		    rs.close();
		    stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return r;
	}
	
    public static void execute(Connection conn, String sql, boolean timelog) throws SQLException {
    	execute( conn,  sql,  timelog, null);
    }

	
	/**
	 * Execute <i>sql</i> query on connection <i>conn</i>
	 * @param conn
	 * @param sql
	 * @param timelog, boolean if the execution has to be logged
	 * @throws SQLException 
	 */
    public static void execute(Connection conn, String sql, boolean timelog, PrintStream out) throws SQLException {
        	if(conn==null||conn.isClosed())System.out.println("Conn is closed");
        	
            Statement stmt=conn.createStatement();

            long now=System.currentTimeMillis();
            stmt.execute(sql);
            if(timelog){
            	if(out==null) out=System.out;
            	out.println("time :"+sql+" "+(System.currentTimeMillis()-now)+"ms");
            }
            stmt.close();
    }
    
    /**
     * Executes sql query with no logging
     * @param conn
     * @param sql
     * @throws SQLException 
     */
    public static void execute(Connection conn, String sql) throws SQLException {
    	execute(conn, sql, false);
    }

    /**
     * Creates database with <i>dbName</i>
     * @param conn
     * @param dbName
     * @throws SQLException 
     */
    public static void createDB(Connection conn, String dbName) throws SQLException {
        execute(conn, "create database "+dbName);
    }
    
    /**
     * Drops index with <i>indexName</i> on table <i>tableName</i>
     * @param conn
     * @param indexName
     * @param tableName
     * @throws SQLException 
     */
    public static void dropIndex(Connection conn, String indexName, String tableName) throws SQLException {
    	execute(conn,"drop index " + indexName + " on " + tableName);
    }
    
    public abstract void createIndex(Connection conn, String indexName, String tableName, String keys, boolean unique) throws SQLException;
    
    
    
    public void createIndex(Connection conn, String indexName, String tableName, String keys) throws SQLException {
    	createIndex(conn, indexName, tableName, keys, false);
    }
    
    /**
     * Drops database <i>dbName</i>
     * @param conn
     * @param dbName
     */
    public static void dropDB(Connection conn, String dbName) {
        try {
			execute(conn, "drop database if exists "+dbName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public abstract void dropTable(Connection conn, String tableName) throws SQLException;
    


    /**
     * Commits on all connections in array conn[]
     * @param conn
     */
    public static void commitAll(Connection[] conn) {
    	for(int i=0;i<conn.length;i++)
			try {
				if(! conn[i].getAutoCommit())
				conn[i].commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
    public void importClassiDettaglio(String[] channels,Connection conn, PrintStream console) {
    	LoadClasses lClasses=new LoadClasses(console);
        lClasses.doJob(channels);
        Vector<ClassItem> SQLInstructions=lClasses.getSQLInstructions();
        ClassItem currentItem;
        
        for(int i=0;i<SQLInstructions.size();i++) {
            currentItem=SQLInstructions.elementAt(i);
            try {
				insertClassItem(channels,conn, currentItem);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
    }
    
//    /**
//     * @deprecated
//     * @param channels
//     * @param conn
//     * @param configFile
//     * @param console
//     */
//    public void importClassiDettaglio(String[] channels,Connection conn, String configFile, PrintStream console) {
//        LoadClasses lClasses=new LoadClasses(configFile, console);
//        lClasses.doJob(channels);
//        Vector<ClassItem> SQLInstructions=lClasses.getSQLInstructions();
//        ClassItem currentItem;
//        
//        for(int i=0;i<SQLInstructions.size();i++) {
//            currentItem=SQLInstructions.elementAt(i);
//            try {
//				insertClassItem(channels,conn, currentItem);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//        }
//    }
    
    public void create1stIndex(Connection conn) throws SQLException{
    	createIndex(conn,"anagrafe_parole_parola", "anagrafe_parole", "parola(30)", true);
    }
    
    public void createDBindexes(Connection conn) throws SQLException {
    	createIndex(conn,"notizie_bid", "notizie", "bid");
    	createIndex(conn,"anagrafe_parole_stemma", "anagrafe_parole", "stemma");
    	createIndex(conn,"classi_dettaglio_id_classe", "classi_dettaglio", "id_classe");
    	createIndex(conn,"classi_dettaglio_id_tag", "classi_dettaglio", "tag");
    	createIndex(conn,"classi_dettaglio_id_de", "classi_dettaglio", "data_element");
    	createIndex(conn,"temp_3", "temp_lcpn", "id_parola,id_classe");
    	
    }
    
    protected abstract void createHashTable(Connection conn) throws SQLException;
    public abstract void createAllTables(Connection conn) throws SQLException;
    public abstract long getIDwhere(Connection conn, String tableName, String fieldName, String fieldValue);
    public abstract void createDBl_tables(Connection conn, PrintStream console) throws SQLException;
    
    
    public long getClassID(Connection conn, String className) {
        return getIDwhere(conn, "tipi_notizie","nome",className);
    }
    
    /*public static long getElementNameID(Connection conn, String elementName) {
        return getIDwhere(conn, "classi","nome",elementName);
    }*/
    

	public void insertClassItem(String[] channels,Connection conn, ClassItem currentItem) throws SQLException {
        long currentClassID=getClassID(conn, currentItem.getClassName());
        long currentElementID=StaticDataComponent.getChannelIndexbyName(channels,currentItem.getElementName()); //DbGateway.getElementNameID(conn, currentItem.getElementName());
        
        String sql="insert into classi_dettaglio (id_classe,id_tipo,tag,data_element) "+
        	"values ("+currentElementID+","+currentClassID+",'"+
        	currentItem.getTag()+"','"+currentItem.getField()+"')";
        DbGateway.execute(conn, sql);
//        System.out.println(currentItem);
	}
	
	public static RecordInterface getNotiziaByJID(Connection conn, long jid) {
		RecordInterface ma = getNotiziaByJID(conn,Long.toString(jid));
		if((ma!=null) && (ma.getJOpacID() != jid))
			ma.setJOpacID(jid);
		return  ma;
	}
	
	public static String getNotiziaTypeByJID(Connection conn, String jid) throws SQLException {
		String tipo=null;
		Statement stmt=conn.createStatement();
		String sql="select tipi_notizie.nome as nome " +
			"from notizie,tipi_notizie where notizie.id='"+jid+"' and tipi_notizie.id=notizie.id_tipo";
		ResultSet rs=stmt.executeQuery(sql);
		if(rs.next()) {
			tipo=rs.getString("nome");
		}
		rs.close();
		stmt.close();
		return tipo;
	}
	
	public static Vector<RecordInterface> getNotiziaByHash(Connection conn, String hash) {
		Vector<RecordInterface> r=new Vector<RecordInterface>();
		
		Iterator<Long> jid=getJIDByHash(conn, hash).iterator();
		
		while(jid.hasNext()) {
			r.addElement(getNotiziaByJID(conn,jid.next()));
		}
		return r;
	}
	
	public static Vector<Long> getJIDByHash(Connection conn, String hash) {
		Vector<Long> r=new Vector<Long>();
		try {
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery("select id_notizia from hash where hash='"+hash+"'");
			while(rs.next()) {
				r.addElement(rs.getLong("id_notizia"));
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return r;
	}
	
    public static RecordInterface getNotiziaByJID(Connection conn, String jid) {
    	RecordInterface ma=null;
    	try {
            Statement stmt=conn.createStatement();
            String sql="select notizie.id as id, notizie.notizia as notizia, tipi_notizie.nome as nome " +
    			"from notizie,tipi_notizie where notizie.id="+jid+" and tipi_notizie.id=notizie.id_tipo";
            ResultSet rs=stmt.executeQuery(sql);
            
//            boolean rep=false;

            if(rs.next()) {
	            String tipo=rs.getString("nome");
	            Blob notizia=rs.getBlob("notizia");
	            //ma=RecordFactory.buildRecord(rs.getLong("id"),rs.getString("notizia"),tipo,0);
	            ma=RecordFactory.buildRecord(rs.getLong("id"),ZipUnzip.decompressString(notizia.getBytes(1, (int)notizia.length())),tipo,0);
	            
            }
            
            if(rs.next()) {
            	System.out.println("ERRORE (duplicati JID): id="+jid+" conn="+conn.getCatalog());
            }
            
            rs.close();
            stmt.close();
            
            if(ma==null) {
            	System.out.println("ERRORE (null): id="+jid+" conn="+conn.getCatalog());
            }
          
/*          if(debug) {
        	  if(rep) System.out.println("Record2Display: recuperato 1 record");
        	  else if(rep) System.out.println("Record2Display: ERRORE RECORD NON RECUPERATO");
        	  
        	  if(ma==null) if(rep) System.out.println("Record2Display: ERRORE RECORD NULL");
        	  else if(rep) System.out.println("Record2Display: record valido: "+ma.getISBD());
          } */
        }
        catch (Exception e) {
          e.printStackTrace();
          ma=null;
        }
        return ma;
    }
    
    /**
     * Cancella la notizia puntata da <i>bid</i>. Se piu' di una notizia ha lo stesso bid allora
     * vengono cancellate tutte.
     * @param conn
     * @param bid
     * @throws SQLException
     */
    public static void cancellaNotiziaFromBid(Connection conn, String bid) throws SQLException {
    	Statement qry=conn.createStatement();
        ResultSet rs=qry.executeQuery("select id from notizie where bid='"+bid+"'");
        while(rs.next()) {
        	cancellaNotiziaFromJid(conn, rs.getLong("ID"));
        }
        qry.close();
    }
    
    /**
     * Cancella la notizia puntata da <i>jid</i>. jid e' la chiave primaria della tabella notizie.
     * @param conn
     * @param jid
     * @throws SQLException
     */
    public static void cancellaNotiziaFromJid(Connection conn, long jid) throws SQLException {
    	Statement qry2=conn.createStatement();
          /*ResultSet rsde=qry2.executeQuery("select id from data_element where id_notizia="+jid);
          while(rsde.next()) {
            execute(conn[1],"delete from l_parole_de where id_de="+rsde.getString("id"));
          }
          execute(conn[1],"delete from data_element where id_notizia="+jid);
          */

          //rsde.close();
          ResultSet lcpn=qry2.executeQuery("select id, id_l_classi_parole from l_classi_parole_notizie where id_notizia="+jid);
          while(lcpn.next()) {
            execute(conn,"update l_classi_parole set n_notizie=n_notizie-1 where id="+lcpn.getString("id_l_classi_parole"));
          }
          execute(conn,"delete from l_classi_parole_notizie where id_notizia="+jid);

        execute(conn,"delete from notizie where id='"+jid+"'");
        execute(conn,"delete from hash where id_notizia='"+jid+"'");
        qry2.close();
        lcpn.close();
    }
    
    public static long getMaxIdTable(Connection conn, String tableName) throws SQLException {
    	long id_nz=0;
        Statement stmt=conn.createStatement();
        ResultSet rs=stmt.executeQuery("SELECT Max("+tableName+".id) AS MaxOfid FROM "+tableName);
        if(rs.next()) {
          id_nz=rs.getLong(1);
        }
        rs.close();
        stmt.close();
        return id_nz;
    }
    

    /**
     * 
     * @param conn
     * @param tipo tipoNotizia 
     * @param notizia Stringa
     * @param clDettaglio Hashtable (tag+dataelement,id_classe) dalla tabella classi_dettaglio
     * @throws SQLException
     */    
    public void inserisciNotizia(Connection conn, String tipo, 
    		String notizia) throws SQLException {
  	  RecordInterface ma;
  	  //ma=ISO2709.creaNotizia(0,notizia,tipo,0);
  	  try {
		ma=RecordFactory.buildRecord(0,notizia,tipo,0);
		inserisciNotizia(conn,ma);
	} catch (Exception e) {
		e.printStackTrace();
	} 
  	  
    }
    
    public long inserisciNotizia(Connection conn, RecordInterface notizia) throws SQLException {
    	long idTipo=getIdTipo(conn, notizia.getTipo());
    	long jid=insertTableNotizie(conn,notizia, idTipo);
    	return inserisciNotiziaInner(conn,notizia,jid);
    }
    
    public long inserisciNotizia(Connection conn, RecordInterface notizia, long jid) throws SQLException {
    	long idTipo=getIdTipo(conn, notizia.getTipo());
    	insertTableNotizie(conn,notizia, idTipo, jid);
    	return inserisciNotiziaInner(conn,notizia,jid);
    }
    
    /**
     * 
     * @param conn
     * @param notizia ISO2709
     * @throws SQLException
     */
    private long inserisciNotiziaInner(Connection conn, RecordInterface notizia, long jid) throws SQLException {
    	long idTipo=getIdTipo(conn, notizia.getTipo());
    	
    	if(notizia.toString()!=null) {
    	
	    	insertHashNotizia(conn,notizia, jid);
	    	
	    	Connection c[]={conn};
	    	Cache cache=getCache();
	    	ParoleSpooler paroleSpooler=new ParoleSpooler(c,c.length,cache,out);
	    	
	    	insertNotizia(c,notizia,idTipo,jid,paroleSpooler);
	    	
	    	paroleSpooler.destroy();
	    	String[] channels=notizia.getChannels();
	    	if(notizia!=null) {
				for(int i=0;i<channels.length;i++)
					updateTableListe(conn,channels[i],(int)jid,notizia);
				
			}
    	}
    	return jid;
    }
    
    public void updateNotizia(Connection conn, RecordInterface notizia) throws SQLException {
    	long jid=notizia.getJOpacID();
    	cancellaNotiziaFromJid(conn, jid);
    	inserisciNotizia(conn,notizia,jid);
    }
    
    /**
     * Inserisce nella tabella hash la firma MD5 della notizia
     * @param conn
     * @param notizia
     * @param idTipo
     */
	private static void insertHashNotizia(Connection conn, RecordInterface notizia, long jid) throws SQLException {
		String hash;
		try {
			hash = notizia.getHash();
			insertHashNotizia(conn,hash,jid);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JOpac2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void insertHashNotizia(Connection conn, String hash, long jid) throws SQLException {
		String sql="insert into hash (id_notizia, hash) values("+jid+",'"+hash+"')";
		Statement stmt=conn.createStatement();
		stmt.execute(sql);
		stmt.close();
	}
	
	public static void updateHashNotizia(Connection conn, String hash, long jid) throws SQLException {
		String sql="update hash set hash='"+hash+"' where id_notizia="+jid;
		Statement stmt=conn.createStatement();
		stmt.execute(sql);
		stmt.close();
	}
	
	public static void insertHashNotizia(Connection conn, long jid) throws SQLException {
		RecordInterface ma=getNotiziaByJID(conn,jid);
		if(ma!=null) {
			insertHashNotizia(conn,ma,jid);
			ma.destroy();
		}
	}

	public static void updateHashNotizia(Connection conn, long jid) throws SQLException {
		RecordInterface ma=getNotiziaByJID(conn,jid);
		String hash;
		if(ma!=null) {
			try {
				hash = ma.getHash();
				if(getHashNotizia(conn,jid)!=null)
					updateHashNotizia(conn, hash, jid);
				else
					insertHashNotizia(conn, hash, jid);
			}
			catch(Exception e) {
				System.out.println("ERRORE hash per notizia jid="+jid);
				System.out.println(e.getMessage());
			}
			ma.destroy();
		}
	}
	
	public static String getHashNotizia(Connection conn, String jid) throws SQLException {
		return getHashNotizia(conn,Long.parseLong(jid));
	}
	
	public static String getHashNotizia(Connection conn, long jid) throws SQLException {
		String hash=null;
		Statement stmt=conn.createStatement();
		String sql="select hash from hash where id_notizia="+jid;
		ResultSet rs=stmt.executeQuery(sql);
		if(rs.next()) hash=rs.getString("hash");
		rs.close();
		stmt.close();
		return hash;
	}

	private static void insertNotizia(Connection[] conn, RecordInterface notizia, long idTipo, long jid, ParoleSpooler paroleSpooler) throws SQLException {
    	Vector<ClasseDettaglio> clDettaglio=initClDettaglio(conn[conn.length>1?1:conn.length-1],idTipo);
    	long idSequenzaTag=0;
    	
    	Enumeration<TokenWord> e=notizia.getItems();
    	while(e.hasMoreElements()) {
    		TokenWord tw=e.nextElement();
    		int k=clDettaglio.indexOf(new ClasseDettaglio(-1,-1,-1,tw.getTag(),tw.getDataelement()));
    		if(k>=0) {
    			ClasseDettaglio cd=clDettaglio.elementAt(k);
	            InsertParole(conn,tw.getValue(),jid,idSequenzaTag++,cd,paroleSpooler);
    		}
    	}

        clDettaglio.removeAllElements();
        clDettaglio=null; 
    }
	
	private static long insertTableNotizie(Connection conn, RecordInterface notizia, long idTipo) throws SQLException {
		long jid=getMaxIdTable(conn,"notizie")+1;
		return insertTableNotizie(conn, notizia, idTipo, jid);
	}

	private static long insertTableNotizie(Connection conn, RecordInterface notizia, long idTipo, long jid) throws SQLException {
    	
    	String record = notizia.getBid();
    	if(record.equals("0")) {record=String.valueOf(jid);}
    	
        try {
        	PreparedStatement preparedNotizia=conn.prepareStatement("insert into notizie (id,bid,id_tipo,notizia) values (?,?,?,?)");
        	
        	preparedNotizia.setLong(1,jid);
        	preparedNotizia.setString(2,record);
        	preparedNotizia.setLong(3,idTipo);
        	//preparedNotizia.setString(4,notizia.toString());
        	preparedNotizia.setBytes(4, ZipUnzip.compressString(notizia.toString()));
        	//preparedNotizia.setBlob(4, new Blob(JOpac2.utils.ZipUnzip.compressString(notizia.toString())));
    		preparedNotizia.execute();
    	} catch (SQLException e1) {
    		e1.printStackTrace();
    	}

		return jid;
	}

	
	/**
	 * Ricostruisce la tabelle hash con le firme MD5 delle notizie
	 */
	public void rebuildHash(Connection conn) throws SQLException {
		dropTable(conn, "hash");
		createHashTable(conn);
		createIndex(conn, "idx_hash", "hash", "hash");
		long maxid=DbGateway.getMaxIdTable(conn, "notizie");
		long step=maxid/100;
		for(long jid=0;jid<maxid;jid++) {
			if(jid % step == 0)
				System.out.println("Rebuilding hash: "+Utils.percentuale(maxid,jid)+"%");
			DbGateway.insertHashNotizia(conn, jid);
		}

	}
	
	public static void rebuildDatabase(Connection[] conn, PrintStream console) throws SQLException {
		Hashtable<Integer,String> tipiNotizie=new Hashtable<Integer,String>(); 

		Statement stmt=conn[0].createStatement();
		stmt.execute("truncate table l_parole_de");
		stmt.execute("truncate table l_classi_parole");
		stmt.execute("truncate table l_classi_parole_notizie");
		stmt.execute("truncate table l_parole_de");
		stmt.execute("truncate table data_element");
		stmt.execute("truncate table anagrafe_parole");
		// TODO ciclare su tutte le classi
		stmt.execute("truncate table "+nomeTableListe("TIT"));  //CR_LISTE
				
		ResultSet rs=stmt.executeQuery("select * from tipi_notizie");
		while(rs.next()) {
			tipiNotizie.put(new Integer(rs.getInt("id")),rs.getString("nome"));
		}
		rs.close();
		
		long current=0;
		
		Cache cache=getCache();
		
    	ParoleSpooler paroleSpooler=new ParoleSpooler(conn,conn.length,cache,console);
		
		rs=stmt.executeQuery("select * from notizie");
		while(rs.next()) {
			int idTipo=rs.getInt("id_tipo");
			String tipo=tipiNotizie.get(new Integer(idTipo));
			RecordInterface ma=null;
			try {
				ma=RecordFactory.buildRecord(0,rs.getString("notizia"),tipo,0);
				insertNotizia(conn,ma,idTipo,rs.getLong("id"),paroleSpooler); //ISO2709 notizia, long idTipo, long jid
			} catch (Exception e) {
				e.printStackTrace();
			} 
			//ISO2709 ma=ISO2709.creaNotizia(0,rs.getString("notizia"),tipo,0);
			current++;
			if(current%1000==0) {
				console.println(current+" record reimportati");
			}
			ma.destroy();
			ma=null;
		}
		rs.close();
		stmt.close();
		paroleSpooler.destroy();
		console.println(current+" record reimportati");
	}
	
	private static Cache cache=null;
	
	public static Cache getCache() {
		if(cache==null) {
	//		Use the cache manager to create the default cache
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cache;
	}

	/**
	 * @param valore
	 * @return
	 */
	public static StringTokenizer paroleTokenizer(String valore) {
		return  new StringTokenizer(processaMarcatori(valore),SEPARATORI_PAROLE);
	}

	private static void InsertParole(Connection conn[], String valore, long jid, long idSequenzaTag, ClasseDettaglio cl, 
			ParoleSpooler paroleSpooler) throws SQLException {
	    String parola;
	    StringTokenizer tk=paroleTokenizer(valore);

	    while(tk.hasMoreTokens()) {
	      parola=tk.nextToken().trim();
	      
	      if(parola.startsWith("*")) {
	    	  parola=parola.substring(1); // parola senza l'asterisco
	      }
	      
	      if((parola!=null)&&(parola.length()>0)) {
		      long id_parola=InsertParola(conn[conn.length>1?1:conn.length-1],parola,paroleSpooler);
		      long id_lcp=insertLClassiParole(conn[conn.length>2?2:conn.length-1],id_parola,cl.getIdClasse());
		      insertLClassiParoleNotizie(conn[conn.length>3?3:conn.length-1],jid,id_lcp);
	      }
	    }
	}
	


	private static long insertLClassiParoleNotizie(Connection conn, long jid, long id_lcp) throws SQLException {
    	long id_lcpn=getMaxIdTable(conn,"l_classi_parole_notizie")+1;
    	Statement stmt=conn.createStatement();
    	stmt.execute("INSERT INTO l_classi_parole_notizie (id,id_l_classi_parole,id_notizia) VALUES (" + 
    			id_lcpn + ", " + id_lcp + ", " + jid +")");
    	stmt.close();
		return id_lcpn;
	}

	public static long insertLClassiParole(Connection conn,long id_parola,long id_classe) throws SQLException {
		long id_lcp=-1;
		long nz=0;
        Statement stmt=conn.createStatement();
        ResultSet rs=stmt.executeQuery("SELECT id, n_notizie FROM l_classi_parole where id_parola=" + id_parola + 
        		" and id_classe="+ id_classe +";");
        if(rs.next()) {
          id_lcp=rs.getLong("id");
          nz=rs.getLong("n_notizie")+1;
        }
        rs.close();
        stmt.close();
        
        if(id_lcp==-1) {
        	id_lcp=getMaxIdTable(conn,"l_classi_parole")+1;
        	stmt=conn.createStatement();
        	stmt.execute("INSERT INTO l_classi_parole (id,id_parola,id_classe,n_notizie) VALUES (" + 
        			id_lcp + ", " + id_parola + ", " + id_classe + ", " + "1)");
        	stmt.close();
        }
        else {
        	stmt=conn.createStatement();
        	stmt.execute("UPDATE l_classi_parole SET n_notizie=" + nz + " WHERE id=" + id_lcp);
        	stmt.close();        	
        }
		return id_lcp;
	}

	public long getIdTipo(Connection conn, String tipo) {
		return getIDwhere(conn, "tipi_notizie", "nome", tipo);
	}
	
	  // determina se eliminare parole (inizia per ESC+H e ESC+I o *)
	  /**
	   * TODO: dovrebbe essere in MARC o ISO2709 piuttosto?
	   * 07/03/2003 - R.T.
	   *      Cambiato: ai fini dell'indicizzazione vanno inserite anche le
	   *      parole prima della parte significativa del titolo.
	   *      Quindi semplicemente se c'e' l'asterisco lo tolgo e se ci sono
	   *      i marcatori li tolgo MA LASCIO LE PAROLE INCLUSE
	   */
	  public static String processaMarcatori(String valore) {
	    //int posiz_asterisco=valore.indexOf("*");
	    String returnValue=valore;
	    //if((posiz_asterisco>0)&&(posiz_asterisco<=MAX_POSIZIONE_ASTERISCO)) {
	      //returnValue=returnValue.replaceFirst("\\*","");
	    //}
	    returnValue=returnValue.replaceAll((String.valueOf((char)0x1b)+"H"),"");
	    returnValue=returnValue.replaceAll((String.valueOf((char)0x1b)+"I"),"");

	    return returnValue;
	  }
	  
	  public static long InsertParola(Connection conn,String parola, ParoleSpooler paroleSpooler) throws SQLException {
		  long r=-1;
	      parola=pulisciParola(parola);
	      
	      r=paroleSpooler.getParola(parola);
		  if(r==-1) {
	    	  long id_ap_f=getMaxIdTable(conn,"anagrafe_parole")+1+paroleSpooler.getCurrentValue();
	    	  r=id_ap_f;
	    	  paroleSpooler.insertParola(id_ap_f,parola);
	      }
	      return r;
	  }
	  
	  public static void InsertParola(Connection conn, String parola, String stemma, Long id_parola) {
		  try {
			DbGateway.execute(conn, "insert into anagrafe_parole (parola,ID,stemma) values ('"+parola+"',"+id_parola+", '"+stemma+"');");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  }
	  
	  // converte minuscole e accentate
	  public static String pulisciParola(String parola) {
	    /** @todo  pulisci parola accentate e altro*/
		  parola=parola.replaceAll("\\\"","");
	    return parola.toLowerCase();
	  }

	  /**
	   * Inizializza una Hashtable con i valori della tabella classi_dettaglio per il tipo idTipo.
	   * @param conn
	   * @param idTipo
	   * @return
	   */
	public static Vector<ClasseDettaglio> initClDettaglio(Connection conn, long idTipo) {
		Vector<ClasseDettaglio> cl_dettaglio=new Vector<ClasseDettaglio>();
	    try {
	        Statement stmt=conn.createStatement();
	        ResultSet rs=stmt.executeQuery("select * from classi_dettaglio where id_tipo="+idTipo);
	        while(rs.next()) {
	        	cl_dettaglio.add(new ClasseDettaglio(rs.getLong("id"),rs.getLong("id_tipo"),
	        			rs.getLong("id_classe"),rs.getString("tag"),rs.getString("data_element")));
	        }
	        rs.close();
	        stmt.close();
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      }

		return cl_dettaglio;
	}

/*	private static void inserisciClasse(Connection conn, String id, String nome) throws SQLException {
		Statement stmt=conn.createStatement();
		stmt.execute("insert into classi (id,nome) values('"+id+"','"+nome+"')");
		stmt.close();
	} */
	
/*	public static void createClasses(Connection conn) throws SQLException {
		Hashtable<String,String> classi=getClassi();
		Enumeration<String> e=classi.keys();
		while(e.hasMoreElements()) {
			String k=e.nextElement();
			String v=classi.get(k);
			inserisciClasse(conn,k,v);
		}
	}*/

	public static Vector<Long> getAllRecords(Connection myConnection) {
		Vector<Long> r=new Vector<Long>();
		try {
	        Statement stmt=myConnection.createStatement();
	        ResultSet rs=stmt.executeQuery("select id from notizie");
	        while(rs.next()) {
	        	r.add(rs.getLong("id"));
	        }
	        rs.close();
	        stmt.close();
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      }
		return r;
	}
	
	public static Vector<String> dumpTable(Connection conn,String tableName) {
        String r="";
        Statement stm=null;
        ResultSet rs=null;
        ResultSetMetaData rsmd=null;
        Vector<String> vr=new Vector<String>();
        
        try {
            stm=conn.createStatement();
            rs=stm.executeQuery("select * from "+tableName);
            rsmd= rs.getMetaData();

            while(rs.next()) {
                for(int i=1;i<=rsmd.getColumnCount();i++) {
                    r+=rs.getString(i)+",";
                }
                r=r.substring(0, r.lastIndexOf(","));
                vr.addElement(r);
                r="";
            }
            
        }
        catch(SQLException e) {
            r=e.getMessage();
        }
        finally {
            // Always make sure result sets and statements are closed,
            // and the connection is returned to the pool
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { ; }
                rs = null;
            }
            if (stm != null) {
                try { stm.close(); } catch (SQLException e) { ; }
                stm = null;
            }
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { ; }
                conn = null;
            }
        }
        return vr;
    }
	
	
	/**
     * esegue una query e restituisce tabella HTML
     */
    public static String SqlToHtml(Connection conn,String SQL,boolean outputHtml) {
        String html=SQL+(outputHtml?"<br/><table border=1><tr>":"\n");
        Statement stm=null;
        ResultSet rs=null;
        ResultSetMetaData rsmd=null;
        String begintr=outputHtml?"<tr>":"", endtr=outputHtml?"</tr>":"\n";
        String begintd=outputHtml?"<td>":"", endtd=outputHtml?"</td>":"\t";
        try {
            stm=conn.createStatement();
            rs=stm.executeQuery(SQL);
            rsmd= rs.getMetaData();
            for(int i=1;i<=rsmd.getColumnCount();i++) {
                html+=begintd+rsmd.getColumnName(i)+endtd;
            }
            html+=endtr;
            while(rs.next()) {
                html+=begintr;
                for(int i=1;i<=rsmd.getColumnCount();i++) {
                    html+=begintd+rs.getString(i)+endtd;
                }
                html+=endtr;
            }
            html+=outputHtml?"</table>":"";
        }
        catch(SQLException e) {
            html=e.getMessage();
        }
        finally {
            // Always make sure result sets and statements are closed,
            // and the connection is returned to the pool
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { ; }
                rs = null;
            }
            if (stm != null) {
                try { stm.close(); } catch (SQLException e) { ; }
                stm = null;
            }
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { ; }
                conn = null;
            }
        }
        return html;
    }
	public static void desc(Connection conn, String sql) {
		if(DEBUG) System.out.println(SqlToHtml(conn, "desc "+sql,false));
	}

	public static Vector<Long> getIdNotizie(Connection conn) {
		Vector<Long> r=new Vector<Long>();
		long max=0;
		
		try {
			
			String query="select max(id) as m " +
					"from notizie ";
					
	        Statement stmt=conn.createStatement();
	        ResultSet rs=stmt.executeQuery(query);
	        
	        if(rs.next()) max=rs.getLong("m");
	        rs.close();
	        stmt.close();
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      }
	    
	    for(long i=1;i<=max;i++) r.addElement(new Long(i));
	    
		return r;
	}

	public static Vector<ObjectPair> getLastSearchFrq(Connection myConnection, String idSessione, boolean useStemma) throws SQLException {
		String campo=useStemma?"parola":"stemma";
		int idRicerca=-1;
		//int n_notizie=-1;
		Vector<ObjectPair> r=null;
		
		Statement stmt=myConnection.createStatement();
		ResultSet rs=stmt.executeQuery("select max(id) as maxid from ricerche where jsession_id='"+idSessione+"'");
		if(rs.next()) idRicerca=rs.getInt("maxid");
		rs.close();
		stmt.close();
		
/*		stmt=myConnection.createStatement();
		rs=stmt.executeQuery("select count(*) as n from notizie");
		if(rs.next()) n_notizie=rs.getInt("n");
		rs.close();
		stmt.close();
*/		
		if(idRicerca>0) {		// &&n_notizie>0
			String sql="select "+ campo +", sum(n_notizie) as fq " +
				"from l_classi_parole_notizie lcpn, l_classi_parole lcp, anagrafe_parole a, ricerche_dettaglio dr " +
				"where lcpn.id_notizia=dr.id_notizia " +
				"and lcp.id=lcpn.id_l_classi_parole " +
				"and a.id=lcp.id_parola " +
				"and dr.id_ricerca= " + idRicerca + " " +
				"and lcp.id_classe in (1,2) " +
				"group by " + campo + " " +
				"order by " + campo;
			r=new Vector<ObjectPair>();
			stmt=myConnection.createStatement();
			rs=stmt.executeQuery(sql);
			while(rs.next()) {
				String parola=rs.getString(campo);
				r.addElement(new ObjectPair(parola,new Float(rs.getFloat("fq"))));
			}
			rs.close();
			stmt.close();
		}
		return r;
	}
	
	public abstract void createTableRicerche(Connection conn) throws SQLException;

	
	
	public static String nomeTableListe(String classe){//CR_LISTE
		if(classe.contains("/")) classe=classe.substring(classe.lastIndexOf("/")+1); // per Mdb
		return classe+"_NDX";
	}
	
	public abstract void createTableListe(Connection conn,String classe) throws SQLException;
	
	public static void updateTableListe(Connection conn, String classe, long id_notizia, String testo) throws SQLException {//CR_LISTE
		if(testo!=null) {
			String tab=nomeTableListe(classe);
			String dl=String.valueOf((char)0x1b);
			
			//String sql="INSERT INTO "+tab+" SET id_notizia=?, testo=?";
			String sql="INSERT INTO "+tab+" (id_notizia, testo) values (?,?)"; // qs sintassi per compatibilita' derby
			PreparedStatement pst=conn.prepareStatement(sql);
			pst.setLong(1, id_notizia);
			testo=testo.replaceAll(dl+"I", ""); // rimuove delimitatori asterisco
			testo=testo.replaceAll(dl+"H", "");
			if(testo.length()>50) testo=testo.substring(0,49);
			pst.setString(2, testo);
			pst.execute();
			pst.close();
		}
	}
	
	public void rebuildList(Connection conn) {
//		String done="";
		RecordInterface ma=DbGateway.getNotiziaByJID(conn, 1);
		String[] channels=ma.getChannels();
		ma.destroy();
		
		try {
			rebuildList(conn,channels);
		} catch (SQLException e) {
			if(out==null) out=System.out;
			e.printStackTrace(out);
		}
		
//		for(int i=0;i<channels.length;i++) {
//			if(!done.contains("|"+channels[i]+"|")) {
//				try {
//					rebuildList(conn,channels[i]);
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//				done+="|"+channels[i]+"|"; // semplice per non fare due volte lo stesso indice
//			}
//		}
	}
	
	public void rebuildList(Connection conn, String[] channels) throws SQLException {
		out.println("Indexing channels:");
		for(int i=0;i<channels.length;i++) {
			createTableListe(conn,channels[i]);
			out.println("   "+channels[i]);
		}
		long maxid=DbGateway.getMaxIdTable(conn, "notizie");
		long step=maxid/100;
		for(int jid=0;jid<=maxid;jid++) {
			if(step==0 || jid % step == 0)
				out.println("Rebuilding list indexes: "+Utils.percentuale(maxid,jid)+"%");
			RecordInterface ma=DbGateway.getNotiziaByJID(conn, jid);
			if(ma!=null) {
				for(int i=0;i<channels.length;i++)
					updateTableListe(conn,channels[i],jid,ma);
				
			}
		}
	}


	private void updateTableListe(Connection conn, String classe, int jid,
			RecordInterface ma) throws SQLException {
		if(classe.equals("TIT")) DbGateway.updateTableListe(conn, classe, jid, ma.getTitle()); //testo=ma.getTitle();
		else if(classe.equals("NUM")) DbGateway.updateTableListe(conn, classe, jid, ma.getStandardNumber()); // testo=ma.getStandardNumber();
		else if(classe.equals("DTE")) DbGateway.updateTableListe(conn, classe, jid, ma.getPublicationDate()); // testo=ma.getPublicationDate();
		else if(classe.equals("AUT")) DbGateway.updateTableListe(conn, classe, jid, ma.getAuthors());
		else if(classe.equals("SBJ")) DbGateway.updateTableListe(conn, classe, jid, subjectsToString(ma.getSubjects()));
		else if(classe.equals("BIB")) {
			Vector<BookSignature> signatures=ma.getSignatures();
			for(int i=0;signatures!=null && i<signatures.size();i++) {
				DbGateway.updateTableListe(conn, classe, jid, signatures.elementAt(i).getLibraryName());
			}
		}
		else {
			DbGateway.updateTableListe(conn, classe, jid, ma.getField(classe)); // prova diretto per file Mdb
		}
		
	}


	public void rebuildList(Connection conn, String classe) throws SQLException {
		createTableListe(conn,classe);//CR_LISTE		
		long maxid=DbGateway.getMaxIdTable(conn, "notizie");
		long step=maxid/100;
		for(int jid=0;jid<maxid;jid++) {
			if(step==0 || jid % step == 0)
				out.println("Rebuilding list index ("+nomeTableListe(classe)+"): "+Utils.percentuale(maxid,jid)+"%");
			RecordInterface ma=DbGateway.getNotiziaByJID(conn, jid);
			inserisciNotiziaListe(conn,classe,jid,ma);
		}

	}
	
	private void inserisciNotiziaListe(Connection conn, String classe, long jid, RecordInterface ma) throws SQLException {
		if(ma!=null) {
			if(classe.equals("TIT")) DbGateway.updateTableListe(conn, classe, jid, ma.getTitle()); //testo=ma.getTitle();
			else if(classe.equals("NUM")) DbGateway.updateTableListe(conn, classe, jid, ma.getStandardNumber()); // testo=ma.getStandardNumber();
			else if(classe.equals("DTE")) DbGateway.updateTableListe(conn, classe, jid, ma.getPublicationDate()); // testo=ma.getPublicationDate();
			else if(classe.equals("AUT")) DbGateway.updateTableListe(conn, classe, jid, ma.getAuthors());
			else if(classe.equals("SBJ")) DbGateway.updateTableListe(conn, classe, jid, subjectsToString(ma.getSubjects()));
			else if(classe.equals("BIB")) {
				Vector<BookSignature> signatures=ma.getSignatures();
				for(int i=0;signatures!=null && i<signatures.size();i++) {
					DbGateway.updateTableListe(conn, classe, jid, signatures.elementAt(i).getLibraryName());
				}
			}
			else {
				DbGateway.updateTableListe(conn, classe, jid, ma.getField(classe)); // prova diretto per file Mdb
				//System.out.println("LISTA NON IMPLEMENTATA PER: "+classe);
			}
			
			// TODO "LAN","MAT","INV","CLL","ABS"
		}
	}

	private Vector<String> subjectsToString(Vector<SubjectInterface> subjects) {
		Vector<String> r=new Vector<String>();
		for(int i=0;subjects!=null && i<subjects.size();i++) {
			r.addElement(subjects.elementAt(i).toString());
		}
		return r;
	}


	private static void updateTableListe(Connection conn, String classe,
			long jid, Vector<String> testi) throws SQLException {
		for(int i=0;testi!=null && i<testi.size();i++) {
			DbGateway.updateTableListe(conn, classe, jid, testi.elementAt(i));
		}
	}
	
	public static int saveQuery(Connection myConnection, String id, SearchResultSet result) throws SQLException {
		System.out.println("Session JID="+id);
		PreparedStatement stmt=myConnection.prepareStatement("insert into ricerche (jsession_id,testo_ricerca) values(?,?)");
		Statement st=myConnection.createStatement();
		stmt.setString(1, id);
		stmt.setString(2, result.getOptimizedQuery());
		stmt.execute();
		String sql;
		if(myConnection.toString().contains("mysql")) {
    		sql="select last_insert_id()";
    	}
		else {
			sql="select max(id) from ricerche where jsession_id='"+id+"'";
		}
		int newId=-1;
		ResultSet rs=st.executeQuery(sql);
		if (rs.next())
			newId=rs.getInt(1);
		rs.close();
		st.close();
		stmt=myConnection.prepareStatement("insert into ricerche_dettaglio values(?,?)");
		Enumeration<Long> e=result.getRecordIDs().elements();
		long l;
		stmt.setInt(2, newId);
		while(e.hasMoreElements()){
			l=e.nextElement().longValue();
			stmt.setLong(1, l);
			stmt.execute();
		}
		stmt.close();
		return newId;
	}

	public static void cleanUpRicerche(Connection conn, String sessionId) throws SQLException {
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery("select id from ricerche where jsession_id='"+sessionId+"'");
		while(rs.next()) {
			Statement stmt1=conn.createStatement();
			stmt1.execute("delete from ricerche_dettaglio where id_ricerca="+rs.getString("id"));
			stmt1.close();
		}
		rs.close();
		stmt.execute("delete from ricerche where jsession_id='"+sessionId+"'");
		stmt.close();
	}
	
	public static void orderBy(Connection conn,String classe,SearchResultSet rset) throws SQLException{
		String sql="select n.id,t.testo from notizie n,ricerche_dettaglio rd,"+nomeTableListe(classe)+" t " +
		           "where rd.id_ricerca=? and " +
		           "      n.id = rd.id_notizia and " +
		           "      t.id_notizia = n.id " + 
		           "order by t.testo";
		PreparedStatement stmt=conn.prepareStatement(sql);
		stmt.setLong(1, rset.getQueryID());
		ResultSet rs=stmt.executeQuery();
		Vector<Long> rid =rset.getRecordIDs();
		rid.clear();
		while(rs.next()){
			rid.addElement(new Long(rs.getLong(1)));
		}
		rs.close();
		stmt.close();
	}

	public static DbGateway getInstance(String string, PrintStream console) {
		if(string.contains("mysql")) return new mysql(console);
		else if(string.contains("hsqldb")) return new hsqldb(console);
		else if(string.contains("derby")) return new derby(console);
		return null;
	}
	
	public static String getClassContentFromJID(Connection conn, String classe, long jid) throws SQLException {
		String sql="select * from "+nomeTableListe(classe)+" where id_notizia="+jid;
		String r=null;
	
		Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery(sql);
		if(rs.next()) {
			r=rs.getString("testo");
		}
		rs.close();
		st.close();
		return r;
	}
	
	public abstract SearchResultSet listSearchFB(Connection conn, String classe,
			String parole, int limit, boolean forward) throws SQLException;

	public SearchResultSet listSearchBackward(Connection conn,String classe,String parole,int limit) throws SQLException {
		return listSearchFB(conn, classe, parole, limit, false);
	}
	
	public SearchResultSet listSearch(Connection conn,String classe,String parole,int limit) throws SQLException {
		return listSearchFB(conn, classe, parole, limit, true);
	}
	
	public static String[] getChannels(Connection conn, String classe) throws SQLException {
		Vector<String> r=new Vector<String>();
		String sql="SELECT tag FROM classi_dettaglio c, tipi_notizie t where t.id=c.id_tipo and t.nome='"+classe+"'";
		Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery(sql);
		while(rs.next()) {
			String c=rs.getString("tag");
			if(c!=null && c.contains("/")) c=c.substring(c.lastIndexOf("/")+1);
			r.addElement(c);
		}
		rs.close();
		st.close();
		return r.toArray(new String[r.size()]);
	}


	public long getClassIDClasseDettaglio(Connection conn, String classe) throws SQLException {
		String sql="select id_classe from classi_dettaglio where tag='"+classe+"'";
		Statement st=conn.createStatement();
		long r=-1;
		ResultSet rs=st.executeQuery(sql);
		while(rs.next()) {
			r=rs.getLong("id_classe");
		}
		rs.close();
		st.close();
		return r;
	}

}
