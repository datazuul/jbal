package org.jopac2.engine.dbengine.dbGateway;

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

import org.jopac2.engine.dbengine.dbGateway.derby.derby;
import org.jopac2.engine.dbengine.dbGateway.mysql.mysql;
import org.jopac2.engine.dbengine.importers.LoadClasses;
import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.stemmer.Radice;
import org.jopac2.jbal.stemmer.StemmerItv2;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.jbal.xmlHandlers.ClassItem;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.ClasseDettaglio;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.ObjectPair;
import org.jopac2.utils.TokenWord;
import org.jopac2.utils.Utils;
import org.jopac2.utils.ZipUnzip;

import com.whirlycott.cache.Cache;
import com.whirlycott.cache.CacheConfiguration;
import com.whirlycott.cache.CacheException;
import com.whirlycott.cache.CacheManager;


public abstract class DbGateway {
	  /** @todo  le costanti vanno messe in file di configurazione!!! */
	
	// 18.07.2006 RT tolto * da SEPARATORI PAROLE per usarlo come inizio parole da indicizzare
	  public static final String SEPARATORI_PAROLE=" ,.;()/-'\\:=@$&!?[]#<>\016\017\n\t";
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
	  
	  public abstract Connection createConnection(String dbUrl, String dbUser, String dbPassword)  throws SQLException;


	/**
	 * Returns the number of record present in table 'notizie'
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	public static long getCountNotizie(Connection conn, String catalog) throws SQLException {
		long r=0;
		Statement stmt=null;
		ResultSet rs=null;
		try {
		    stmt=conn.createStatement();
		    rs=stmt.executeQuery("SELECT count(*) c from je_"+catalog+"_notizie");
		    if(rs.next()) {
		        r=rs.getLong("c");
		    }
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			if(rs!=null) rs.close();
		    if(stmt!=null) stmt.close();
		}
		return r;
	}

	/**
	 * Returns the number of record present in table anagrafe_parole
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	public static long getCountParole(Connection conn, String catalog) throws SQLException {
		long r=0;
		Statement stmt=null;
		ResultSet rs=null;
		try {
		    stmt=conn.createStatement();
		    rs=stmt.executeQuery("SELECT count(*) c from je_"+catalog+"_anagrafe_parole");
		    if(rs.next()) {
		        r=rs.getLong("c");
		    }
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			if(rs!=null) rs.close();
		    if(stmt!=null) stmt.close();
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
        	
            Statement stmt=null;
            try {
	            stmt=conn.createStatement();
	
	            long now=System.currentTimeMillis();
	            stmt.execute(sql);
	            if(timelog){
	            	if(out==null) out=System.out;
	            	out.println("time :"+sql+" "+(System.currentTimeMillis()-now)+"ms");
	            }
            }
            catch(SQLException e) {
            	throw e;
            }
    		finally {
    		    if(stmt!=null) stmt.close();
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

//    /**
//     * Creates database with <i>dbName</i>
//     * @param conn
//     * @param dbName
//     * @throws SQLException 
//     */
//    public static void createDB(Connection conn, String dbName) throws SQLException {
//        execute(conn, "create database "+dbName);
//    }
    
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
    
//    /**
//     * Drops database <i>dbName</i>
//     * @param conn
//     * @param dbName
//     */
//    public static void dropDB(Connection conn, String dbName) {
//        try {
//			execute(conn, "drop database if exists "+dbName);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//    }
    
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
    
    public void importClassiDettaglio(String[] channels,Connection conn, String catalog, PrintStream console) {
    	LoadClasses lClasses=new LoadClasses(console);
        lClasses.doJob(channels);
        Vector<ClassItem> SQLInstructions=lClasses.getSQLInstructions();
        ClassItem currentItem;
        
        for(int i=0;i<SQLInstructions.size();i++) {
            currentItem=SQLInstructions.elementAt(i);
            try {
				insertClassItem(channels,conn, catalog, currentItem);
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
    
    public void create1stIndex(Connection conn, String catalog) throws SQLException{
    	createIndex(conn,"je_"+catalog+"_anagrafe_parole_parola", "je_"+catalog+"_anagrafe_parole", "parola(30)", true);
    }
    
    public void createDBindexes(Connection conn, String catalog) throws SQLException {
    	createIndex(conn,"je_"+catalog+"_notizie_bid", "je_"+catalog+"_notizie", "bid");
    	createIndex(conn,"je_"+catalog+"_anagrafe_parole_stemma", "je_"+catalog+"_anagrafe_parole", "stemma");
    	createIndex(conn,"je_"+catalog+"_classi_dettaglio_id_classe", "je_"+catalog+"_classi_dettaglio", "id_classe");
    	createIndex(conn,"je_"+catalog+"_classi_dettaglio_id_tag", "je_"+catalog+"_classi_dettaglio", "tag");
    	createIndex(conn,"je_"+catalog+"_classi_dettaglio_id_de", "je_"+catalog+"_classi_dettaglio", "data_element");
    	createIndex(conn,"je_"+catalog+"_temp_3", "je_"+catalog+"_temp_lcpn", "id_parola,id_classe");
    	
    }
    
    protected abstract void createHashTable(Connection conn, String catalog) throws SQLException;
    public abstract void createAllTables(Connection conn, String catalog) throws SQLException;
    public abstract long getIDwhere(Connection conn, String tableName, String fieldName, String fieldValue);
    public abstract void createDBl_tables(Connection conn, String catalog, PrintStream console) throws SQLException;
    
    
    public long getTypeID(Connection conn, String catalog, String className) {
        return getIDwhere(conn, "je_"+catalog+"_tipi_notizie","nome",className);
    }
    
    public long getClassNameID(Connection conn, String catalog, String elementName) {
        return getIDwhere(conn, "je_"+catalog+"_classi","nome",elementName);
    }
    

	public void insertClassItem(String[] channels,Connection conn, String catalog, ClassItem currentItem) throws SQLException {
        long currentTypeID=getTypeID(conn, catalog, currentItem.getClassName());
        long currentClassID=getClassNameID(conn, catalog, currentItem.getElementName());
        
        String sql="insert into je_"+catalog+"_classi_dettaglio (id_classe,id_tipo,tag,data_element) "+
        	"values ("+currentClassID+","+currentTypeID+",'"+
        	currentItem.getTag()+"','"+currentItem.getField()+"')";
        DbGateway.execute(conn, sql);
//        System.out.println(currentItem);
	}
	
//	public static RecordInterface getNotiziaByJID(Connection conn, String catalog, String jid) {
//		RecordInterface ma=null;
//		try {
//			ma = getNotiziaByJID(conn, catalog, jid);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		if((ma!=null) && (ma.getJOpacID() != jid))
//			ma.setJOpacID(jid);
//		return  ma;
//	}
	
	public static String getNotiziaTypeByJID(Connection conn, String catalog, String jid) throws SQLException {
		String tipo=null;
		Statement stmt=null;
		ResultSet rs=null;
		try {
		stmt=conn.createStatement();
		String sql="select je_"+catalog+"_tipi_notizie.nome as nome " +
			"from je_"+catalog+"_notizie,je_"+catalog+"_tipi_notizie " +
					"where je_"+catalog+"_notizie.id='"+jid+"' " +
							"and je_"+catalog+"_tipi_notizie.id=je_"+catalog+"_notizie.id_tipo";
		rs=stmt.executeQuery(sql);
		if(rs.next()) {
			tipo=rs.getString("nome");
		}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
		    if(stmt!=null) stmt.close();
		}
		return tipo;
	}
	
	public static Vector<RecordInterface> getNotiziaByHash(Connection conn, String catalog, String hash) throws SQLException {
		Vector<RecordInterface> r=new Vector<RecordInterface>();
		
		Iterator<String> jid=getJIDByHash(conn, catalog, hash).iterator();
		
		while(jid.hasNext()) {
			r.addElement(getNotiziaByJID(conn,catalog,jid.next()));
		}
		return r;
	}
	
	public static Vector<String> getJIDByHash(Connection conn, String catalog, String hash) throws SQLException {
		Vector<String> r=new Vector<String>();
		Statement stmt=null;
		ResultSet rs=null;
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery("select id_notizia from je_"+catalog+"_hash where hash='"+hash+"'");
			while(rs.next()) {
				r.addElement(rs.getString("id_notizia"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if(rs!=null) rs.close();
		    if(stmt!=null) stmt.close();
		}
		return r;
	}
	
    public static RecordInterface getNotiziaByJID(Connection conn, String catalog, String jid) throws SQLException {
    	RecordInterface ma=null;
    	Statement stmt=null;
		ResultSet rs=null;
		try {
            stmt=conn.createStatement();
            String sql="select je_"+catalog+"_notizie.id as id, je_"+catalog+"_notizie.notizia as notizia, je_"+catalog+"_tipi_notizie.nome as nome " +
    			"from je_"+catalog+"_notizie,je_"+catalog+"_tipi_notizie " +
    					"where je_"+catalog+"_notizie.id="+jid+" " +
    							"and je_"+catalog+"_tipi_notizie.id=je_"+catalog+"_notizie.id_tipo";
            rs=stmt.executeQuery(sql);
            
//            boolean rep=false;

            if(rs.next()) {
	            String tipo=rs.getString("nome");
	            Blob notizia=rs.getBlob("notizia");
//	            ma=RecordFactory.buildRecord(rs.getLong("id"),rs.getString("notizia").getBytes(),tipo,0);
	            ma=RecordFactory.buildRecord(rs.getString("id"),ZipUnzip.decompressString(notizia.getBytes(1, (int)notizia.length())),tipo,0);
	            
            }
            
            if(rs.next()) {
            	System.out.println("ERRORE (duplicati JID): id="+jid+" conn="+conn.getCatalog());
            }
            
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
		finally {
			if(rs!=null) rs.close();
		    if(stmt!=null) stmt.close();
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
    public static void cancellaNotiziaFromBid(Connection conn, String catalog, String bid) throws SQLException {
    	Statement qry=null;
		ResultSet rs=null;
		try {
	    	qry=conn.createStatement();
	        rs=qry.executeQuery("select id from je_"+catalog+"_notizie where bid='"+bid+"'");
	        while(rs.next()) {
	        	cancellaNotiziaFromJid(conn, catalog, rs.getString("ID"));
	        }
    	}
    	catch(SQLException e) {
    		throw e;
    	}
    	finally {
	        if(rs!=null) rs.close();
	        if(qry!=null) qry.close();
    	}
    }
    
    /**
     * Cancella la notizia puntata da <i>jid</i>. jid e' la chiave primaria della tabella notizie.
     * @param conn
     * @param jid
     * @throws SQLException
     */
    public static void cancellaNotiziaFromJid(Connection conn, String catalog, String jid) throws SQLException {
    	Statement qry2=null;
		ResultSet lcpn=null;
		try {
	    	qry2=conn.createStatement();
	          /*ResultSet rsde=qry2.executeQuery("select id from data_element where id_notizia="+jid);
	          while(rsde.next()) {
	            execute(conn[1],"delete from l_parole_de where id_de="+rsde.getString("id"));
	          }
	          execute(conn[1],"delete from data_element where id_notizia="+jid);
	          */
	
	          //rsde.close();
	          lcpn=qry2.executeQuery("select id, id_l_classi_parole from je_"+catalog+"_l_classi_parole_notizie where id_notizia="+jid);
	          while(lcpn.next()) {
	            execute(conn,"update je_"+catalog+"_l_classi_parole set n_notizie=n_notizie-1 where id="+lcpn.getString("id_l_classi_parole"));
	          }
	          execute(conn,"delete from je_"+catalog+"_l_classi_parole_notizie where id_notizia="+jid);
	
	        execute(conn,"delete from je_"+catalog+"_notizie where id='"+jid+"'");
	        execute(conn,"delete from je_"+catalog+"_hash where id_notizia='"+jid+"'");
    	}
    	catch(SQLException e) {
    		throw e;
    	}
    	finally {
	        if(qry2!=null) qry2.close();
	        if(lcpn!=null) lcpn.close();
    	}
    	cancellaNotiziaDaListe(conn, catalog, jid);
    }
    
    public static void cancellaNotiziaDaListe(Connection conn, String catalog, String jid) throws SQLException {
    	String[] listTables=getListTables(conn, catalog);
    	for(int i=0;i<listTables.length;i++) {
    		cancellaNotiziaDaLista(conn, listTables[i],jid);
    	}
    }
    
    private static void cancellaNotiziaDaLista(Connection conn, String listName,
			String jid) throws SQLException {
    	Statement st=null;
    
    	try {
	    	st=conn.createStatement();
	    	st.execute("delete FROM "+listName+" where id_notizia="+jid);
    	}
    	catch(SQLException e) {
    		
    	}
    	finally {
	    	if(st!=null) st.close();
    	}
	}

	private static String[] getListTables(Connection conn, String catalog) throws SQLException {
    	Statement st=conn.createStatement();
		ResultSet rs=st.executeQuery("show tables");
		Vector<String> v=new Vector<String>();
		while(rs.next()) {
			String tableName=rs.getString(1);
			if(tableName.startsWith("je_"+catalog) && tableName.endsWith("_ndx")) {
				v.add(tableName);
			}
		}
		return v.toArray(new String[v.size()]);
	}

	public static long getMaxIdTable(Connection conn, String tableName) throws SQLException {
    	long id_nz=0;
    	Statement stmt = null;
		ResultSet rs = null;
		try {
	        stmt=conn.createStatement();
	        rs=stmt.executeQuery("SELECT Max("+tableName+".id) AS MaxOfid FROM "+tableName);
	        if(rs.next()) {
	          id_nz=rs.getLong(1);
	        }
    	}
    	catch(SQLException e) {
    		throw e;
    	}
		finally {
			if(rs!=null) rs.close();
		    if(stmt!=null) stmt.close();
		}
        return id_nz;
    }
    

    /**
     * 
     * @param conn
     * @param catalog String 
     * @param tipo tipoNotizia 
     * @param notizia byte[]
     * @throws SQLException
     */    
    public void inserisciNotizia(Connection conn, String catalog, String tipo, Radice stemmer, ParoleSpooler paroleSpooler,
    		byte[] notizia) throws SQLException {
  	  RecordInterface ma;
  	  try {
			ma=RecordFactory.buildRecord("0",notizia,tipo,0);
			inserisciNotizia(conn,catalog,stemmer,paroleSpooler,ma);
		} catch (Exception e) {
			e.printStackTrace();
		} 
  	  
    }
    
    public String inserisciNotizia(Connection conn, String catalog, Radice stemmer, ParoleSpooler paroleSpooler, RecordInterface notizia) throws SQLException {
    	long idTipo=getIdTipo(conn, catalog, notizia.getTipo());
    	String jid=insertTableNotizie(conn,catalog,notizia, idTipo);
    	return inserisciNotiziaInner(conn,catalog,notizia,stemmer, paroleSpooler, jid);
    }
    
    public String inserisciNotizia(Connection conn, String catalog, Radice stemmer, ParoleSpooler paroleSpooler, RecordInterface notizia, String jid) throws SQLException {
    	long idTipo=getIdTipo(conn, catalog, notizia.getTipo());
    	insertTableNotizie(conn,catalog, notizia, idTipo, jid);
    	return inserisciNotiziaInner(conn,catalog, notizia,stemmer,paroleSpooler, jid);
    }
    
    /**
     * 
     * @param conn
     * @param notizia ISO2709
     * @throws SQLException
     */
    private String inserisciNotiziaInner(Connection conn, String catalog, RecordInterface notizia, Radice stemmer, ParoleSpooler paroleSpooler, String jid) throws SQLException {
    	long idTipo=getIdTipo(conn, catalog, notizia.getTipo());
    	
    	
    	if(notizia.toString()!=null) {
    	
	    	insertHashNotizia(conn,catalog,notizia, jid);
	    	
	    	Connection c[]={conn};
	    	
	    	insertNotizia(c,catalog,stemmer,notizia,idTipo,jid,paroleSpooler);
	    	
	    	paroleSpooler.destroy();
	    	String[] channels=notizia.getChannels();
	    	if(notizia!=null) {
				for(int i=0;i<channels.length;i++)
					updateTableListe(conn,catalog,channels[i],jid,notizia);
				
			}
    	}
    	return jid;
    }
    
    public void updateNotizia(Connection conn, String catalog, Radice stemmer, ParoleSpooler paroleSpooler, RecordInterface notizia) throws SQLException {
    	String jid=notizia.getJOpacID();
    	cancellaNotiziaFromJid(conn, catalog, jid);
    	inserisciNotizia(conn,catalog,stemmer,paroleSpooler,notizia,jid);
    }
    
    /**
     * Inserisce nella tabella hash la firma MD5 della notizia
     * @param conn
     * @param notizia
     * @param idTipo
     */
	private static void insertHashNotizia(Connection conn, String catalog, RecordInterface notizia, String jid) throws SQLException {
		String hash;
		try {
			hash = notizia.getHash();
			insertHashNotizia(conn,catalog,hash,jid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (JOpac2Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void insertHashNotizia(Connection conn, String catalog, String hash, String jid) throws SQLException {
		String sql="insert into je_"+catalog+"_hash (id_notizia, hash) values("+jid+",'"+hash+"')";
		DbGateway.execute(conn, sql);
	}
	
	public static void updateHashNotizia(Connection conn, String catalog, String hash, String jid) throws SQLException {
		String sql="update je_"+catalog+"_hash set hash='"+hash+"' where id_notizia="+jid;
		DbGateway.execute(conn, sql);
	}
	
	public static void insertHashNotizia(Connection conn, String catalog, String jid) throws SQLException {
		RecordInterface ma=getNotiziaByJID(conn,catalog,jid);
		if(ma!=null) {
			insertHashNotizia(conn,catalog,ma,jid);
			ma.destroy();
		}
	}

	public static void updateHashNotizia(Connection conn, String catalog, String jid) throws SQLException {
		RecordInterface ma=getNotiziaByJID(conn,catalog,jid);
		String hash;
		if(ma!=null) {
			try {
				hash = ma.getHash();
				if(getHashNotizia(conn,catalog,jid)!=null)
					updateHashNotizia(conn, catalog, hash, jid);
				else
					insertHashNotizia(conn, catalog, hash, jid);
			}
			catch(Exception e) {
				System.out.println("ERRORE hash per notizia jid="+jid);
				System.out.println(e.getMessage());
			}
			ma.destroy();
		}
	}
	
//	public static String getHashNotizia(Connection conn, String catalog, String jid) throws SQLException {
//		return getHashNotizia(conn,catalog,Long.parseLong(jid));
//	}
	
	public static String getHashNotizia(Connection conn, String catalog, String jid) throws SQLException {
		String hash=null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
		stmt=conn.createStatement();
		String sql="select je_"+catalog+"_hash from hash where id_notizia="+jid;
		rs=stmt.executeQuery(sql);
		if(rs.next()) hash=rs.getString("hash");
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			rs.close();
			stmt.close();
		}
		return hash;
	}

	/**
	 * Inserisce gli indici di una notizia ma non la notizia in table_notizie!
	 * paroleSpooler può essere null
	 * @param conn
	 * @param catalog
	 * @param notizia
	 * @param idTipo
	 * @param jid
	 * @param paroleSpooler
	 * @throws SQLException
	 */
	private static void insertNotizia(Connection[] conn, String catalog, Radice stemmer, RecordInterface notizia, long idTipo, String jid, ParoleSpooler paroleSpooler) throws SQLException {
    	Vector<ClasseDettaglio> clDettaglio=initClDettaglio(conn[conn.length>1?1:conn.length-1],catalog, idTipo);
    	long idSequenzaTag=0;
    	
    	Enumeration<TokenWord> e=notizia.getItems();
    	while(e.hasMoreElements()) {
    		TokenWord tw=e.nextElement();
    		int k=clDettaglio.indexOf(new ClasseDettaglio(-1,-1,-1,tw.getTag(),tw.getDataelement()));
    		if(k>=0) {
    			ClasseDettaglio cd=clDettaglio.elementAt(k);
	            insertParole(conn,catalog,stemmer,tw.getValue(),jid,idSequenzaTag++,cd,paroleSpooler);
    		}
    	}

        clDettaglio.removeAllElements();
        clDettaglio=null; 
    }
	
	private static String insertTableNotizie(Connection conn, String catalog, RecordInterface notizia, long idTipo) throws SQLException {
		String jid=getMaxIdTable(conn,"je_"+catalog+"_notizie")+"1"; // TODO NON VA BENE!
		return insertTableNotizie(conn, catalog, notizia, idTipo, jid);
	}

	private static String insertTableNotizie(Connection conn, String catalog, RecordInterface notizia, long idTipo, String jid) throws SQLException {
    	
    	String record = notizia.getBid();
    	if(record.equals("0")) {record=String.valueOf(jid);}
    	
        PreparedStatement preparedNotizia=null;
		try {
        	preparedNotizia=conn.prepareStatement("insert into je_"+catalog+"_notizie (id,bid,id_tipo,notizia) values (?,?,?,?)");
        	
        	preparedNotizia.setString(1,jid);
        	preparedNotizia.setString(2,record);
        	preparedNotizia.setLong(3,idTipo);
        	//preparedNotizia.setString(4,notizia.toString());
        	preparedNotizia.setBytes(4, ZipUnzip.compressString(notizia.toString()));
        	//preparedNotizia.setBlob(4, new Blob(JOpac2.utils.ZipUnzip.compressString(notizia.toString())));
    		preparedNotizia.execute();
    	} catch (SQLException e1) {
    		throw e1;
    	}
    	finally {
    		if(preparedNotizia!=null) preparedNotizia.close();
    	}
		return jid;
	}
	
	public static Vector<String> getJIDbyBID(Connection conn, String catalog, String bid) throws SQLException {
		Vector<String> r=new Vector<String>();
		ResultSet rs=null;
		Statement qry=null;
		try {
	    	qry=conn.createStatement();
	    	rs=qry.executeQuery("select id from je_"+catalog+"_notizie where bid='"+bid+"'");
	        while(rs.next()) {
	        	r.addElement(rs.getString("id"));
	        }
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(qry!=null) qry.close();
		}
        return r;
	}

	
	/**
	 * Ricostruisce la tabelle hash con le firme MD5 delle notizie
	 */
	public void rebuildHash(Connection conn, String catalog) throws SQLException {
		dropTable(conn, "je_"+catalog+"_hash");
		createHashTable(conn,catalog);
		createIndex(conn, "je_"+catalog+"_idx_hash", "je_"+catalog+"_hash", "hash");
		long maxid=DbGateway.getMaxIdTable(conn, "je_"+catalog+"_notizie");
		long step=maxid/100;
		for(long jid=0;jid<maxid;jid++) {
			if(jid % step == 0)
				System.out.println("Rebuilding hash: "+Utils.percentuale(maxid,jid)+"%");
			DbGateway.insertHashNotizia(conn, catalog, Long.toString(jid));
		}

	}
	
	public void rebuildDatabase(Connection[] conn, String catalog, PrintStream console) throws SQLException {
		Hashtable<Integer,String> tipiNotizie=new Hashtable<Integer,String>(); 

		Statement stmt = null;
		ResultSet rs = null;
		ParoleSpooler paroleSpooler = null;
		long current=0;
		Radice stemmer=new StemmerItv2();

		try {
			rebuildSchemata(catalog,conn[0]);
			stmt=conn[0].createStatement();
			// TODO ciclare su tutte le classi
//			stmt.execute("truncate table je_"+catalog+"_"+nomeTableListe("TIT"));  //CR_LISTE
					
			rs=stmt.executeQuery("select * from je_"+catalog+"_tipi_notizie");
			while(rs.next()) {
				tipiNotizie.put(new Integer(rs.getInt("id")),rs.getString("nome"));
			}
			rs.close();
			
			Cache cache=getCache();
			
	    	paroleSpooler=new ParoleSpooler(conn,catalog,conn.length,cache,stemmer,console);
			
	    	boolean first=true;
	    	
			rs=stmt.executeQuery("select * from je_"+catalog+"_notizie");
			while(rs.next()) {
				int idTipo=rs.getInt("id_tipo");
//				String tipo=tipiNotizie.get(new Integer(idTipo));
				RecordInterface ma=null;
				try {
					String jid=rs.getString("id");
					ma=DbGateway.getNotiziaByJID(conn[0], catalog, jid);
					if(first) {
						first=false;
						importClassiDettaglio(ma.getChannels(),conn[0],catalog,console);
					}
					
					ma.setJOpacID(jid);
					execute(conn[0],"delete from je_"+catalog+"_notizie where id='"+jid+"'");
					insertTableNotizie(conn[0], catalog, ma, idTipo, ma.getJOpacID());
					insertNotizia(conn,catalog,stemmer,ma,idTipo,rs.getString("id"),paroleSpooler); //ISO2709 notizia, long idTipo, long jid
				} catch (Exception e) {
					e.printStackTrace();
				} 
				//ISO2709 ma=ISO2709.creaNotizia(0,rs.getString("notizia"),tipo,0);
				current++;
				if(current%1000==0) {
					console.println(current+" record reimportati");
				}
				if(ma!=null) ma.destroy();
				ma=null;
			}
			try {
				CacheManager.getInstance().shutdown();
			} catch (CacheException e) {
				e.printStackTrace();
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			rs.close();
			stmt.close();
			paroleSpooler.destroy();
		}
		console.println(current+" record reimportati");
	}
	
	private void rebuildSchemata(String catalog, Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		try {
			stmt.execute("truncate table je_"+catalog+"_classi");
		}
		catch(Exception e) {}
		try {
			stmt.execute("truncate table je_"+catalog+"_classi_dettaglio");
		}
		catch(Exception e) {}
		try {
			stmt.execute("truncate table je_"+catalog+"_l_parole_de");}
		catch(Exception e) {}
		try {
			stmt.execute("truncate table je_"+catalog+"_l_classi_parole");}
		catch(Exception e) {}
		try {
			stmt.execute("truncate table je_"+catalog+"_l_classi_parole_notizie");}
		catch(Exception e) {}
		try {
			stmt.execute("truncate table je_"+catalog+"_l_parole_de");}
		catch(Exception e) {}
		try {
			stmt.execute("truncate table je_"+catalog+"_data_element");}
		catch(Exception e) {}
		try {
			stmt.execute("truncate table je_"+catalog+"_anagrafe_parole");}
		catch(Exception e) {}
		stmt.close();
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
	
	public static void shutdownCache() throws CacheException {
//		CacheManager.getInstance().destroy();
		CacheManager.getInstance().shutdown();
	}

	/**
	 * @param valore
	 * @return
	 */
	public static StringTokenizer paroleTokenizer(String valore) {
		return  new StringTokenizer(processaMarcatori(valore),SEPARATORI_PAROLE);
	}

	/**
	 * paroleSpooler può essere null
	 * @param conn
	 * @param catalog
	 * @param valore
	 * @param jid
	 * @param idSequenzaTag
	 * @param cl
	 * @param paroleSpooler
	 * @throws SQLException
	 */
	private static void insertParole(Connection conn[], String catalog, Radice stemmer, String valore, String jid, long idSequenzaTag, ClasseDettaglio cl, 
			ParoleSpooler paroleSpooler) throws SQLException {
	    String parola;
	    StringTokenizer tk=paroleTokenizer(valore);

	    while(tk.hasMoreTokens()) {
	      parola=tk.nextToken().trim();
	      
	      if(parola.startsWith("*")) {
	    	  parola=parola.substring(1); // parola senza l'asterisco
	      }
	      
	      if((parola!=null)&&(parola.length()>0)) {
	    	  String stemma=stemmer.radice(parola);
		      long id_parola=insertParola(conn[conn.length>1?1:conn.length-1],catalog,parola,stemma,paroleSpooler);
		      long id_lcp=insertLClassiParole(conn[conn.length>2?2:conn.length-1],catalog,id_parola,cl.getIdClasse());
		      insertLClassiParoleNotizie(conn[conn.length>3?3:conn.length-1],catalog,jid,id_lcp);
	      }
	    }
	}
	


	private static long insertLClassiParoleNotizie(Connection conn, String catalog, String jid, long id_lcp) throws SQLException {
    	long id_lcpn=getMaxIdTable(conn,"je_"+catalog+"_l_classi_parole_notizie")+1;
    	String sql="INSERT INTO je_"+catalog+"_l_classi_parole_notizie (id,id_l_classi_parole,id_notizia) " +
    			"VALUES (" + id_lcpn + ", " + id_lcp + ", " + jid +")";
    	DbGateway.execute(conn, sql);
		return id_lcpn;
	}

	public static long insertLClassiParole(Connection conn,String catalog, long id_parola,long id_classe) throws SQLException {
		long id_lcp=-1;
		long nz=0;
		
		Statement stmt = null;
		ResultSet rs = null;
		try {
	        stmt=conn.createStatement();
	        rs  = stmt.executeQuery("SELECT id, n_notizie FROM je_"+catalog+"_l_classi_parole where id_parola=" + id_parola + 
	        		" and id_classe="+ id_classe +";");
	        if(rs.next()) {
	          id_lcp=rs.getLong("id");
	          nz=rs.getLong("n_notizie")+1;
	        }
	        
	        if(id_lcp==-1) {
	        	id_lcp=getMaxIdTable(conn,"je_"+catalog+"_l_classi_parole")+1;
	        	String sql = "INSERT INTO je_"+catalog+"_l_classi_parole (id,id_parola,id_classe,n_notizie) VALUES (" + 
	        			id_lcp + ", " + id_parola + ", " + id_classe + ", " + "1)";
	        	DbGateway.execute(conn, sql);
	        }
	        else {
	        	String sql = "UPDATE je_"+catalog+"_l_classi_parole SET n_notizie=" + nz + " WHERE id=" + id_lcp;
	        	DbGateway.execute(conn, sql);
	        }
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(stmt!=null) stmt.close();
			if(rs!=null) rs.close();
		}
		return id_lcp;
	}

	public long getIdTipo(Connection conn, String catalog, String tipo) {
		return getIDwhere(conn, "je_"+catalog+"_tipi_notizie", "nome", tipo);
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
	  
	  /**
	   * paroleSpooler puo' essere null
	   * @param conn
	   * @param catalog
	   * @param parola
	   * @param paroleSpooler
	   * @return
	   * @throws SQLException
	   */
	  public static long insertParola(Connection conn,String catalog, String parola, String stemma, ParoleSpooler paroleSpooler) throws SQLException {
		  long r=-1;
	      parola=pulisciParola(parola);	      
	      
	      if(paroleSpooler!=null) r=paroleSpooler.getParola(parola);
		  if(r==-1) {
			  if(paroleSpooler!=null) {
		    	  long id_ap_f=getMaxIdTable(conn,"je_"+catalog+"_anagrafe_parole")+1+paroleSpooler.getCurrentValue();
		    	  r=id_ap_f;
		    	  paroleSpooler.insertParola(id_ap_f,parola);
		    	  insertParola(conn,catalog,parola,stemma,r);
			  }
			  else {
				  r=getMaxIdTable(conn,"je_"+catalog+"_anagrafe_parole")+1;
				  insertParola(conn,catalog,parola,stemma,r);
			  }
	      }
	      return r;
	  }
	  
	  public static void insertParola(Connection conn, String catalog, String parola, String stemma, Long id_parola) {
		  try {
			DbGateway.execute(conn, "insert into je_"+catalog+"_anagrafe_parole (parola,ID,stemma) values ('"+parola+"',"+id_parola+", '"+stemma+"');");
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
	 * @throws SQLException 
	   */
	public static Vector<ClasseDettaglio> initClDettaglio(Connection conn, String catalog, long idTipo) throws SQLException {
		Vector<ClasseDettaglio> cl_dettaglio=new Vector<ClasseDettaglio>();
	    Statement stmt = null;
		ResultSet rs = null;
		try {
	        stmt=conn.createStatement();
	        rs=stmt.executeQuery("select * from je_"+catalog+"_classi_dettaglio where id_tipo="+idTipo);
	        while(rs.next()) {
	        	cl_dettaglio.add(new ClasseDettaglio(rs.getLong("id"),rs.getLong("id_tipo"),
	        			rs.getLong("id_classe"),rs.getString("tag"),rs.getString("data_element")));
	        }
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      }
	      finally {
	    	  if(rs!=null) rs.close();
	    	  if(stmt!=null) stmt.close();
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

	public static Vector<Long> getAllRecords(Connection myConnection, String catalog) throws SQLException {
		Vector<Long> r=new Vector<Long>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
	        stmt=myConnection.createStatement();
	        rs=stmt.executeQuery("select id from je_"+catalog+"_notizie");
	        while(rs.next()) {
	        	r.add(rs.getLong("id"));
	        }
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      }
	      finally {
	    	  if(rs!=null) rs.close();
		      if(stmt!=null) stmt.close();
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
    public static String sqlToHtml(Connection conn,String SQL,boolean outputHtml) {
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
		if(DEBUG) System.out.println(sqlToHtml(conn, "desc "+sql,false));
	}

	public static Vector<String> getIdNotizie(Connection conn, String catalog) throws SQLException {
		Vector<String> r=new Vector<String>();
		long max=0;
		
		max=DbGateway.getMaxIdTable(conn, "je_"+catalog+"_notizie ");
				    
	    for(long i=1;i<=max;i++) r.addElement(Long.toString(i));
	    
		return r;
	}

	public static Vector<ObjectPair> getLastSearchFrq(Connection conn, String catalog, String idSessione, boolean useStemma) throws SQLException {
		String campo=useStemma?"parola":"stemma";
		long idRicerca=-1;
		//int n_notizie=-1;
		Vector<ObjectPair> r=null;
		
		idRicerca=DbGateway.getMaxIdTable(conn, "je_"+catalog+"_ricerche where jsession_id='"+idSessione+"'"); // prende anche il where
		
			
		if(idRicerca>0) {		// &&n_notizie>0
			String sql="select "+ campo +", sum(n_notizie) as fq " +
				"from je_"+catalog+"_l_classi_parole_notizie lcpn, je_"+catalog+"_l_classi_parole lcp, " +
						"je_"+catalog+"_anagrafe_parole a, je_"+catalog+"_ricerche_dettaglio dr " +
				"where lcpn.id_notizia=dr.id_notizia " +
				"and lcp.id=lcpn.id_l_classi_parole " +
				"and a.id=lcp.id_parola " +
				"and dr.id_ricerca= " + idRicerca + " " +
				"and lcp.id_classe in (1,2) " +
				"group by " + campo + " " +
				"order by " + campo;
			r=new Vector<ObjectPair>();
			Statement stmt = null;
			ResultSet rs = null;
			try {
				stmt =conn.createStatement();
				rs=stmt.executeQuery(sql);
				while(rs.next()) {
					String parola=rs.getString(campo);
					r.addElement(new ObjectPair(parola,new Float(rs.getFloat("fq"))));
				}
			}
			catch(SQLException e) {
				throw e;
			}
			finally {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
			}
		}
		return r;
	}
	
	public abstract void createTableRicerche(Connection conn, String catalog) throws SQLException;

	
	
	public static String nomeTableListe(String classe){//CR_LISTE
		if(classe.contains("/")) classe=classe.substring(classe.lastIndexOf("/")+1); // per Mdb
		return classe.trim().toLowerCase()+"_ndx";
	}
	
	public abstract void createTableListe(Connection conn,String catalog, String classe) throws SQLException;
	
	public static void updateTableListe(Connection conn, String catalog, String classe, String id_notizia, String testo) throws SQLException {//CR_LISTE
		if(testo!=null) {
			String tab="je_"+catalog+"_"+nomeTableListe(classe);
			String dl=String.valueOf((char)0x1b);
			
			//String sql="INSERT INTO "+tab+" SET id_notizia=?, testo=?";
			String sql="INSERT INTO "+tab+" (id_notizia, testo) values (?,?)"; // qs sintassi per compatibilita' derby
			PreparedStatement pst=null;
			try {
				pst=conn.prepareStatement(sql);
				pst.setString(1, id_notizia);
				testo=testo.replaceAll(dl+"I", ""); // rimuove delimitatori asterisco
				testo=testo.replaceAll(dl+"H", "");
				if(testo.length()>50) testo=testo.substring(0,49);
				pst.setString(2, testo.toLowerCase());
				pst.execute();
			}
			catch(SQLException e) {
				throw e;
			}
			finally {
				if(pst!=null) pst.close();
			}
		}
	}
	
	public void rebuildList(Connection conn, String catalog) {
//		String done="";
		
		try {
			RecordInterface ma=DbGateway.getNotiziaByJID(conn, catalog, "1");
			String[] channels=ma.getChannels();
			ma.destroy();
			
			rebuildList(conn,catalog,channels);
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
	
	public void rebuildList(Connection conn, String catalog, String[] channels) throws SQLException {
		out.println("Indexing channels:");
		for(int i=0;i<channels.length;i++) {
			createTableListe(conn,catalog,channels[i]);
			out.println("   "+channels[i]);
		}
		long maxid=DbGateway.getMaxIdTable(conn, "je_"+catalog+"_notizie");
		long step=maxid/100;
		for(int jid=0;jid<=maxid;jid++) {
			if(step==0 || jid % step == 0)
				out.println("Rebuilding list indexes: "+Utils.percentuale(maxid,jid)+"%");
			RecordInterface ma=DbGateway.getNotiziaByJID(conn, catalog, Long.toString(jid));
			if(ma!=null) {
				for(int i=0;i<channels.length;i++)
					updateTableListe(conn,catalog,channels[i],Long.toString(jid),ma);
				ma.destroy();
			}
		}
	}


	private void updateTableListe(Connection conn, String catalog, String classe, String jid,
			RecordInterface ma) throws SQLException {
		if(classe.equals("TIT")) {
			try {
				DbGateway.updateTableListe(conn, catalog, classe, jid, ma.getTitle()); //testo=ma.getTitle();
			}
			catch(Exception e) {e.printStackTrace();}
		}
		else if(classe.equals("NUM")) {
			try {
				DbGateway.updateTableListe(conn, catalog, classe, jid, ma.getStandardNumber()); // testo=ma.getStandardNumber();
			}
			catch(Exception e) {e.printStackTrace();}
		}
		else if(classe.equals("DTE")) {
			try {
				DbGateway.updateTableListe(conn, catalog, classe, jid, ma.getPublicationDate()); // testo=ma.getPublicationDate();
			}
			catch(Exception e) {e.printStackTrace();}
		}
		else if(classe.equals("AUT")) {
			try {
				DbGateway.updateTableListe(conn, catalog, classe, jid, ma.getAuthors());
			}
			catch(Exception e) {e.printStackTrace();}
		}
		else if(classe.equals("SBJ")) {
			try {
				DbGateway.updateTableListe(conn, catalog, classe, jid, subjectsToString(ma.getSubjects()));
			}
			catch(Exception e) {e.printStackTrace();}
		}
		else if(classe.equals("BIB")) {
			Vector<BookSignature> signatures=ma.getSignatures();
			for(int i=0;signatures!=null && i<signatures.size();i++) {
				try {
					DbGateway.updateTableListe(conn, catalog, classe, jid, signatures.elementAt(i).getLibraryName());
				}
				catch(Exception e) {e.printStackTrace();}
			}
		}
		else {
			try {
				DbGateway.updateTableListe(conn, catalog, classe, jid, ma.getField(classe)); // prova diretto per file Mdb
			}
			catch(Exception e) {e.printStackTrace();}
		}
		
	}


	public void rebuildList(Connection conn, String catalog, String classe) throws SQLException {
		createTableListe(conn, catalog, classe);//CR_LISTE		
		long maxid=DbGateway.getMaxIdTable(conn, "je_"+catalog+"_notizie");
		long step=maxid/100;
		for(int jid=0;jid<maxid;jid++) {
			if(step==0 || jid % step == 0)
				out.println("Rebuilding list index ("+nomeTableListe(classe)+"): "+Utils.percentuale(maxid,jid)+"%");
			RecordInterface ma=DbGateway.getNotiziaByJID(conn, catalog, Long.toString(jid));
			inserisciNotiziaListe(conn,catalog, classe,Long.toString(jid),ma);
			ma.destroy();
		}

	}
	
	private void inserisciNotiziaListe(Connection conn, String catalog, String classe, String jid, RecordInterface ma) throws SQLException {
		if(ma!=null) {
			if(classe.equals("TIT")) DbGateway.updateTableListe(conn, catalog, classe, jid, ma.getTitle()); //testo=ma.getTitle();
			else if(classe.equals("NUM")) DbGateway.updateTableListe(conn, catalog, classe, jid, ma.getStandardNumber()); // testo=ma.getStandardNumber();
			else if(classe.equals("DTE")) DbGateway.updateTableListe(conn, catalog, classe, jid, ma.getPublicationDate()); // testo=ma.getPublicationDate();
			else if(classe.equals("AUT")) DbGateway.updateTableListe(conn, catalog, classe, jid, ma.getAuthors());
			else if(classe.equals("SBJ")) DbGateway.updateTableListe(conn, catalog, classe, jid, subjectsToString(ma.getSubjects()));
			else if(classe.equals("BIB")) {
				Vector<BookSignature> signatures=ma.getSignatures();
				for(int i=0;signatures!=null && i<signatures.size();i++) {
					DbGateway.updateTableListe(conn, catalog, classe, jid, signatures.elementAt(i).getLibraryName());
				}
			}
			else {
				DbGateway.updateTableListe(conn, catalog, classe, jid, ma.getField(classe)); // prova diretto per file Mdb
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


	private static void updateTableListe(Connection conn, String catalog, String classe,
			String jid, Vector<String> testi) throws SQLException {
		for(int i=0;testi!=null && i<testi.size();i++) {
			DbGateway.updateTableListe(conn, catalog, classe, jid, testi.elementAt(i));
		}
	}
	
	public static int saveQuery(Connection myConnection, String catalog, String id, SearchResultSet result) throws SQLException {
		//System.out.println("Session JID="+id);
		PreparedStatement stmt = null;
		Statement st = null;
		ResultSet rs = null;
		int newId=-1;
		
		catalog=catalog.trim();

		try {
			stmt=myConnection.prepareStatement("insert into je_"+catalog+"_ricerche (jsession_id,testo_ricerca) values(?,?)");
			st=myConnection.createStatement();
			stmt.setString(1, id);
			stmt.setString(2, result.getOptimizedQuery());
			stmt.execute();
			String sql;
//			if(myConnection.toString().contains("mysql")) {
//	    		sql="select last_insert_id()";
//	    	}
//			else {
				sql="select max(id) from je_"+catalog+"_ricerche where jsession_id='"+id+"'";
//			}
			rs=st.executeQuery(sql);
			if (rs.next())
				newId=rs.getInt(1);
			
			stmt.close();
			
			stmt=myConnection.prepareStatement("insert into je_"+catalog+"_ricerche_dettaglio values(?,?)");
			Enumeration<String> e=result.getRecordIDs().elements();
			String l;
			stmt.setInt(2, newId);
			while(e.hasMoreElements()){
				l=e.nextElement();
				stmt.setString(1, l);
				stmt.execute();
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(stmt!=null) stmt.close();
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return newId;
	}

	public static void cleanUpRicerche(Connection conn, String catalog, String sessionId) throws SQLException {
		Statement stmt=null;
		ResultSet rs=null;
		try {
			stmt=conn.createStatement();
			rs=stmt.executeQuery("select id from je_"+catalog+"_ricerche where jsession_id='"+sessionId+"'");
			while(rs.next()) {
				String sql="delete from je_"+catalog+"_ricerche_dettaglio where id_ricerca="+rs.getString("id");
				DbGateway.execute(conn, sql);
			}
		
			String sql="delete from je_"+catalog+"_ricerche where jsession_id='"+sessionId+"'";
			DbGateway.execute(conn, sql);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(rs!=null) stmt.close();
		}
	}
	
	public static void orderBy(Connection conn,String catalog, String classe,SearchResultSet rset) throws SQLException{
//		String sql="select n.id,t.testo from je_"+catalog+"_notizie n,je_"+catalog+"_ricerche_dettaglio rd, je_"+catalog+"_"+nomeTableListe(classe)+" t " +
//		           "where rd.id_ricerca=? and " +
//		           "      n.id = rd.id_notizia and " +
//		           "      t.id_notizia = n.id " + 
//		           "order by t.testo";
		
		
		String sql="select x.id,t.testo " +
			"from ( " +
			"      select n.id from je_"+catalog+"_notizie n,je_"+catalog+"_ricerche_dettaglio rd " +
			"      where rd.id_ricerca=? and " +
			"      	     n.id = rd.id_notizia " +
			") x left join je_"+catalog+"_"+nomeTableListe(classe)+" t " +
			"on t.id_notizia = x.id " +
			"order by t.testo";
		
		
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try {
			stmt=conn.prepareStatement(sql);
			stmt.setLong(1, rset.getQueryID());
			rs=stmt.executeQuery();
			Vector<String> rid =rset.getRecordIDs();
			rid.clear();
			while(rs.next()){
				rid.addElement(Long.toString(rs.getLong(1)));
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(stmt!=null) stmt.close();
		}
	}

	public static DbGateway getInstance(String string, PrintStream console) {
		if(string.contains("mysql")) return new mysql(console);
		else if(string.contains("derby")) return new derby(console);
		return null;
	}
	
	public static String getClassContentFromJID(Connection conn, String catalog, String classe, String jid) throws SQLException {
		String sql="select * from je_"+catalog+"_"+nomeTableListe(classe)+" where id_notizia="+jid;
		String r=null;
	
		Statement st=null;
		ResultSet rs=null;
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			if(rs.next()) {
				r=rs.getString("testo");
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return r;
	}
	
	public abstract SearchResultSet listSearchFB(Connection conn, String catalog, String classe,
			String parole, int limit, boolean forward) throws SQLException;

	public SearchResultSet listSearchBackward(Connection conn,String catalog, String classe,String parole,int limit) throws SQLException {
		return listSearchFB(conn, catalog, classe, parole, limit, false);
	}
	
	public SearchResultSet listSearch(Connection conn, String catalog, String classe,String parole,int limit) throws SQLException {
		return listSearchFB(conn, catalog, classe, parole, limit, true);
	}
	
	public static String[] getChannels(Connection conn, String catalog, String tipo) throws SQLException {
		Vector<String> r=new Vector<String>();
		String sql="SELECT n.nome " +
				"FROM je_"+catalog+"_classi_dettaglio c, je_"+catalog+"_tipi_notizie t,  je_"+catalog+"_classi n " +
				"where t.id=c.id_tipo and t.nome='"+tipo+"' and n.id=c.id_classe";
		Statement st=null;
		ResultSet rs=null;
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			while(rs.next()) {
				String c=rs.getString("nome");
				if(c!=null && c.contains("/")) c=c.substring(c.lastIndexOf("/")+1);
				r.addElement(c);
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return r.toArray(new String[r.size()]);
	}


//	public long getClassIDClasseDettaglio(Connection conn, String classe) throws SQLException {
//		String sql="select id_classe from classi_dettaglio where tag='"+classe+"'";
//		Statement st=conn.createStatement();
//		long r=-1;
//		ResultSet rs=st.executeQuery(sql);
//		while(rs.next()) {
//			r=rs.getLong("id_classe");
//		}
//		rs.close();
//		st.close();
//		return r;
//	}

}
