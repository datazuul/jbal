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

import org.jopac2.engine.importers.LoadClasses;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.xmlHandlers.ClassItem;
import org.jopac2.utils.ClasseDettaglio;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.ObjectPair;
import org.jopac2.utils.TokenWord;
import org.jopac2.utils.Utils;
import org.jopac2.utils.ZipUnzip;


public final class DbGateway {
	  /** @todo  le costanti vanno messe in file di configurazione!!!*/
	
	// 18.07.2006 RT tolto * da SEPARATORI PAROLE per usarlo come inizio parole da indicizzare
	  public static final String SEPARATORI_PAROLE=" ,.;()/-'\\:=@%$&!?[]#<>\016\017\n\t";
	  static final int MAX_POSIZIONE_ASTERISCO=5;
	  static final boolean DEBUG=true;
	  /** @todo  doppio asterisco. che vuol dire?
	   *   vul dire che le indicazioni di responsabilit� vengono indicizzate
	   *   con al massimo fino a 4 parole chiave che
	   *   vengono segnate dall'asterisco.*/


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
	
	
	/**
	 * Execute <i>sql</i> query on connection <i>conn</i>
	 * @param conn
	 * @param sql
	 * @param timelog, boolean if the execution has to be logged
	 */
    public static void execute(Connection conn, String sql, boolean timelog) {
        try {
        	if(conn==null||conn.isClosed())System.out.println("Conn is closed");
        	
            Statement stmt=conn.createStatement();

            long now=System.currentTimeMillis();
            stmt.execute(sql);
            if(timelog){
            	System.out.println("time :"+sql+" "+(System.currentTimeMillis()-now)+"ms");
            }
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Executes sql query with no logging
     * @param conn
     * @param sql
     */
    public static void execute(Connection conn, String sql) {
    	execute(conn, sql, false);
    }

    /**
     * Creates database with <i>dbName</i>
     * @param conn
     * @param dbName
     */
    public static void createDB(Connection conn, String dbName) {
        execute(conn, "create database "+dbName);
    }
    
    /**
     * Drops index with <i>indexName</i> on table <i>tableName</i>
     * @param conn
     * @param indexName
     * @param tableName
     */
    public static void dropIndex(Connection conn, String indexName, String tableName) {
    	execute(conn,"drop index " + indexName + " on " + tableName);
    }
    
    /**
     * Creates index with <i>indexName</i> on table <i>tableName</i> using keys <i>keys</i>
     * @param conn
     * @param indexName
     * @param tableName
     * @param unique true/false
     * @param keys
     */
    public static void createIndex(Connection conn, String indexName, String tableName, String keys, boolean unique) {
    	String isUnique="";
    	if(unique) isUnique="unique";
    	/*
    	 * hsqldb non permette limiti sugli indici di varchar
    	 */
    	if(conn.toString().contains("hsqldb")) {
    		int k=keys.indexOf('(');
    		while(k>=0) {
    			int z=keys.indexOf(')');
    			keys=keys.substring(0,k)+keys.substring(z+1);
    			k=keys.indexOf('(');
    		}
    	}
    	String sql="create " + isUnique + " index " + indexName + " on " + tableName + " (" + keys + ")";
    	execute(conn, sql);
    }
    
    public static void createIndex(Connection conn, String indexName, String tableName, String keys) {
    	createIndex(conn, indexName, tableName, keys, false);
    }
    
    /**
     * Drops database <i>dbName</i>
     * @param conn
     * @param dbName
     */
    public static void dropDB(Connection conn, String dbName) {
        execute(conn, "drop database if exists "+dbName);
    }
    
    /**
     * Drops table <i>tableName</i>
     * @param conn
     * @param tableName
     */
    public static void dropTable(Connection conn, String tableName) {
    	execute(conn,"drop table if exists " + tableName);
    }

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
    
    public static void importClassiDettaglio(Connection conn, String configFile) {
        LoadClasses lClasses=new LoadClasses(configFile);
        lClasses.doJob();
        Vector<ClassItem> SQLInstructions=lClasses.getSQLInstructions();
        ClassItem currentItem;
        
        for(int i=0;i<SQLInstructions.size();i++) {
            currentItem=SQLInstructions.elementAt(i);
            DbGateway.insertClassItem(conn, currentItem);
        }
    }
    
    public static void create1stIndex(Connection conn){
    	DbGateway.createIndex(conn,"anagrafe_parole_parola", "anagrafe_parole", "parola(30)", true);
    }
    
    public static void createDBindexes(Connection conn) {
    	DbGateway.createIndex(conn,"notizie_bid", "notizie", "bid");
    	DbGateway.createIndex(conn,"anagrafe_parole_stemma", "anagrafe_parole", "stemma");
    	DbGateway.createIndex(conn,"classi_dettaglio_id_classe", "classi_dettaglio", "id_classe");
    	DbGateway.createIndex(conn,"classi_dettaglio_id_tag", "classi_dettaglio", "tag");
    	DbGateway.createIndex(conn,"classi_dettaglio_id_de", "classi_dettaglio", "data_element");
    	//DbGateway.createIndex(conn,"intSearchProp", "intSearch", "proprietario");
    	DbGateway.createIndex(conn,"temp_3", "temp_lcpn", "id_parola,id_classe");
//		  "PRIMARY KEY(id_notizia, id_classe, posizione_parola), " +
//		  "INDEX idx_classe(id_classe, posizione_parola)) ");
    	
    	DbGateway.createIndex(conn, "idx_pcp", "notizie_posizione_parole", "id_classe, posizione_parola, parola");
    	DbGateway.createIndex(conn, "idx_all", "notizie_posizione_parole", "id_parola, id_notizia, id_sequenza_tag");
    	DbGateway.createIndex(conn, "idx_np", "notizie_posizione_parole", "id_notizia, posizione_parola");
    	//ALTER TABLE `dbTrev`.`notizie_posizione_parole` ADD INDEX `idx2`(`id_notizia`, `posizione_parola`);
    	DbGateway.createIndex(conn,"TIT_NDX_x1", "TIT_NDX", "testo(50)", true);//CR_LISTE
    }
    
    private static void createHashTable(Connection conn) {
    	boolean isMysql=true,isHsqldb=false;
		if(conn.toString().contains("hsqldb")) {
			isMysql=false;isHsqldb=true;
		}
		if(isMysql) {
			DbGateway.execute(conn,"create table hash (hash varchar(32) not null,"+
	        "id_notizia integer not null,primary key(id_notizia)) ENGINE = MYISAM DEFAULT CHARSET=utf8");
		}
		if(isHsqldb) {
			DbGateway.execute(conn,"create table hash (id_notizia integer not null identity primary key,"+
	        "hash varchar(32) not null");
		}
    }
    
    private static void createAllTablesMysql(Connection conn) throws SQLException {
        DbGateway.execute(conn,"create table notizie (id integer not null auto_increment,"+
        "bid varchar(50),id_tipo integer,notizia blob,primary key(id)) ENGINE = MYISAM DEFAULT CHARSET=utf8");
        DbGateway.createHashTable(conn);
	    //DbGateway.execute(conn,"create table data_element (ID int not null,"+
	    //    "id_notizia int,id_classi_dettaglio int) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    //DbGateway.execute(conn,"create table l_parole_de (ID int not null,"+
	    //"id_parola int,id_de int) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    DbGateway.execute(conn,"create table anagrafe_parole (ID int not null auto_increment,"+
	        "parola varchar(50), stemma varchar(50),primary key(id)) ENGINE = MYISAM DEFAULT CHARSET=utf8"); //type=memory
	    //DbGateway.execute(conn,"create table classi ("+
	    //    "ID int not null auto_increment,nome char(30) ,primary key(id)) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    DbGateway.execute(conn,"create table classi_dettaglio ("+
	        "ID int not null auto_increment,id_tipo int, id_classe int,"+
	        "tag char(50),data_element char(1) ,primary key(id)) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    DbGateway.execute(conn,"create table tipi_notizie ("+
	        "ID int not null auto_increment, nome text,primary key(id)) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    DbGateway.execute(conn,"create table intSearchKeys ("+
	        "ID int not null auto_increment, id_intSearch int, "+
	        "id_notizia int, primary key(ID)) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    DbGateway.execute(conn,"create table intSearch (ID int not null auto_increment, "+
	        "data_estrazione datetime, nome_ricerca varchar(50), "+
	        "proprietario varchar(32), n_notizie int, sql_cmd longtext, "+
	        "primary key(ID)) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    DbGateway.execute(conn,"create table temp_lcpn "+
            "(id_notizia int,id_parola int,id_classe int " +
//            ", primary key(id_notizia,id_parola,id_classe) "+
            ") ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    DbGateway.execute(conn, "CREATE TABLE notizie_posizione_parole " +
	    		"(id_notizia INT NOT NULL DEFAULT 0, " +
	    		 "id_classe INT NOT NULL DEFAULT 0, " +
	    		 "id_sequenza_tag INT NOT NULL DEFAULT 0, " +
	    		 "id_parola INT NOT NULL DEFAULT 0, " +
	    		 "parola  varchar(50), " +
	    		 "posizione_parola INT NOT NULL DEFAULT 0) " +
//	    		  "INDEX idx_key(id_notizia, id_classe, posizione_parola), " +
//	    		  "INDEX idx_classe(id_classe, posizione_parola)) " +
	    		 "ENGINE = MYISAM " +
	    		 "CHARACTER SET utf8");
	    DbGateway.createTableRicerche(conn);
	    DbGateway.createTableListe(conn);//CR_LISTE
    }
    
	private static void createAllTablesHSQLDB(Connection conn) throws SQLException {
        DbGateway.execute(conn,"create table notizie (id integer not null identity primary key,"+
        	"bid varchar(14),id_tipo integer,notizia varchar)");
        DbGateway.createHashTable(conn);
        
	    //DbGateway.execute(conn,"create table data_element (ID int not null identity primary key,"+
	    //    "id_notizia int,id_classi_dettaglio int)");
	    //DbGateway.execute(conn,"create table l_parole_de (ID int not null,"+
	    //	"id_parola int,id_de int)");
	    DbGateway.execute(conn,"create table anagrafe_parole (ID int not null identity primary key,"+
	        "parola varchar(50), stemma varchar(50))");
	    DbGateway.execute(conn,"create table classi (ID int not null identity primary key,nome char(30))");
	    DbGateway.execute(conn,"create table classi_dettaglio ("+
	        "ID int not null identity primary key,id_tipo int, id_classe int,"+
	        "tag char(50),data_element char(1))");
	    DbGateway.execute(conn,"create table tipi_notizie ("+
	        "ID int not null identity primary key, nome varchar)");
	    DbGateway.execute(conn,"create table intSearchKeys ("+
	        "ID int not null identity primary key, id_intSearch int, id_notizia int)");
	    DbGateway.execute(conn,"create table intSearch (ID int not null identity primary key, "+
	        "data_estrazione datetime, nome_ricerca varchar(50), "+
	        "proprietario varchar(32), n_notizie int, sql_cmd varchar)");
	    DbGateway.execute(conn,"create table temp_lcpn "+
            "(id_notizia int,id_parola int,id_classe int)");
	    DbGateway.execute(conn, "CREATE TABLE notizie_posizione_parole " +
	    		"(id_notizia INT, " +
	    		 "id_classe INT, " +
	    		 "id_sequenza_tag INT, " +
	    		 "id_parola INT, " +
	    		 "posizione_parola INT) ");
//	    		  "PRIMARY KEY(id_notizia, id_classe, posizione_parola), " +
//	    		  "INDEX idx_classe(id_classe, posizione_parola)) ");
	    DbGateway.createTableRicerche(conn);
	}
    
    /**
     * Creates all tables
     * @param conn
     */
	public static void createAllTables(Connection conn) {
		/**
		 * TODO: astrarre la creazione delle tabelle?
		 */
		boolean isMysql=true,isHsqldb=false;
		
		if(conn.toString().contains("hsqldb")) {
			isMysql=false;isHsqldb=true;
		}
		
    	DbGateway.dropTable(conn,"notizie");
    	DbGateway.dropTable(conn,"notizie_posizione_parole");
    	DbGateway.dropTable(conn,"TIT_NDX");//CR_LISTE
    	//DbGateway.dropTable(conn,"data_element");
    	//DbGateway.dropTable(conn,"l_parole_de");
    	DbGateway.dropTable(conn,"anagrafe_parole");
    	DbGateway.dropTable(conn,"classi");
    	DbGateway.dropTable(conn,"classi_dettaglio");
    	DbGateway.dropTable(conn,"tipi_notizie");
    	DbGateway.dropTable(conn,"intSearchKeys");
    	DbGateway.dropTable(conn,"intSearch");
    	DbGateway.dropTable(conn,"temp_lcpn");
    	DbGateway.dropTable(conn,"hash");
    	DbGateway.dropTable(conn, "ricerche_dettaglio");
    	DbGateway.dropTable(conn, "ricerche");
    	
    	
		try {
			if(isMysql) createAllTablesMysql(conn);
			if(isHsqldb) createAllTablesHSQLDB(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
	


	/**
	 * Gets <i>id</i> in table <i>tableName</i> where field <i>fieldName</i> is equal to <i>fieldValue</i>
	 * If <i>id</i> is not available the value is inserted and the id generated by the primary key is returned.
	 * @param conn
	 * @param tableName
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
    public static long getIDwhere(Connection conn, String tableName, String fieldName, String fieldValue) {
        long currentID=-1;
        
        try {
            Statement stmt=conn.createStatement();

            ResultSet r=stmt.executeQuery("select id from "+tableName+" "+
                "where ("+fieldName+"='"+fieldValue+"')");
            if(r.next()) currentID=r.getLong("id");
            r.close();
            
            if((currentID==-1)) {
                stmt.execute("insert into "+tableName+" ("+fieldName+") "+
                    "values('"+fieldValue+"')");
                if(conn.toString().contains("hsqldb")) r = stmt.executeQuery("call identity()"); 
                else r = stmt.executeQuery("SELECT last_insert_id()");
                r.next();
                currentID=r.getLong(1);
                r.close();
            }
            stmt.close();
        }
        catch (Exception e) {
        	e.printStackTrace();
            currentID=-1;
        }
        return currentID;
    }
    
    public static long getClassID(Connection conn, String className) {
        return getIDwhere(conn, "tipi_notizie","nome",className);
    }
    
    /*public static long getElementNameID(Connection conn, String elementName) {
        return getIDwhere(conn, "classi","nome",elementName);
    }*/
    
    public static void createDBl_tables(Connection conn) {
    	boolean isHsqldb=false;
        execute(conn, "drop table if exists l_classi_parole",true);
        
        /**
         * TODO: generalizzare il tipo di db
         */
        if(conn.toString().contains("hsqldb")) {
        	isHsqldb=true;
	        execute(conn, "create table l_classi_parole ("+
		            "ID int not null identity primary key,id_parola int,id_classe int,"+
		            "n_notizie int)");
        }
        else {
	        execute(conn, "create table l_classi_parole ("+
	            "ID int not null auto_increment,id_parola int,id_classe int,"+
	            "n_notizie int ,primary key(id)) ENGINE = MYISAM DEFAULT CHARSET=utf8");
        }
        execute(conn, "create index l_classi_parole_idx1 on l_classi_parole (id_parola)");
        execute(conn, "create index l_classi_parole_idx2 on l_classi_parole (id_classe)");
        execute(conn, "create index l_classi_parole_idx3 on l_classi_parole (id_parola,id_classe)");

        execute(conn, "drop table if exists l_classi_parole_notizie",true);
        
        if(conn.toString().contains("hsqldb")) {
        	execute(conn, "create table l_classi_parole_notizie ("+
    	            "ID int not null identity primary key,id_l_classi_parole int,"+
    	            "id_notizia int)");
        }
        else {
	        execute(conn, "create table l_classi_parole_notizie ("+
	            "ID int not null auto_increment,id_l_classi_parole int,"+
	            "id_notizia int,primary key(id)) ENGINE = MYISAM DEFAULT CHARSET=utf8");
        }
        execute(conn, "create index l_classi_parole_notizie_idclassi "+
            "on l_classi_parole_notizie (id_l_classi_parole)");
        execute(conn, "create index l_classi_parole_notizie_idnotizie "+
            "on l_classi_parole_notizie (id_notizia)");

   //     execute("drop table if exists temp_lcpn");

        //--minuti: 60 minuti (ris:6,7m recs a partire da 7,9m su l_parole_de, 340k notizie)

   /*     execute("create table temp_lcpn as "+
            "select id_notizia,id_parola,id_classe "+
            "from data_element,classi_dettaglio,l_parole_de "+
            "where data_element.id_classi_dettaglio = classi_dettaglio.id "+
            "      and l_parole_de.id_de=data_element.id "+
            "group by id_notizia,id_parola,id_classe ENGINE = MYISAM");

        //-- 6min (usato da entrambe le query successive)
        
        execute("create index temp_3 on temp_lcpn (id_parola,id_classe)"); */

        //-- 1min, 667k
        
        execute(conn, "insert into l_classi_parole (id_parola,id_classe,n_notizie) "+
            "select id_parola,id_classe,count(*) as n_notizie "+
            "from temp_lcpn "+
            "group by id_parola,id_classe",true);

        //--minuti: 16min, 6780360 (=temp_lcpn)
        
        if(isHsqldb) {
	        execute(conn, "insert into l_classi_parole_notizie(id_notizia,id_l_classi_parole) "+
		            "select id_notizia, lcp.id "+
		            "from l_classi_parole lcp, "+
		            "temp_lcpn "+
		            "where "+
		            "temp_lcpn.id_parola=lcp.id_parola and "+
		            "temp_lcpn.id_classe=lcp.id_classe",true);
        }
        else {
	        execute(conn, "insert into l_classi_parole_notizie(id_notizia,id_l_classi_parole) "+
	            "select id_notizia,lcp.id "+
	            "from l_classi_parole lcp use index (l_classi_parole_idx3), "+
	            "temp_lcpn force index (temp_3) "+
	            "where "+
	            "temp_lcpn.id_parola=lcp.id_parola and "+
	            "temp_lcpn.id_classe=lcp.id_classe",true);
        }

//        execute("drop table if exists temp_lcpn");
    }

	public static void insertClassItem(Connection conn, ClassItem currentItem) {
        long currentClassID=DbGateway.getClassID(conn, currentItem.getClassName());
        long currentElementID=StaticDataComponent.getChannelIndexbyName(currentItem.getElementName()); //DbGateway.getElementNameID(conn, currentItem.getElementName());
        
        String sql="insert into classi_dettaglio (id_classe,id_tipo,tag,data_element) "+
        	"values ("+currentElementID+","+currentClassID+",'"+
        	currentItem.getTag()+"','"+currentItem.getField()+"')";
        DbGateway.execute(conn, sql);
//        System.out.println(currentItem);
	}
	
	public static RecordInterface getNotiziaByJID(Connection conn, long jid) {
		return getNotiziaByJID(conn,Long.toString(jid));
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
    			"from notizie,tipi_notizie where notizie.id='"+jid+"' and tipi_notizie.id=notizie.id_tipo";
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
    public static void cancellaNotiziaFromBid(Connection conn[], String bid) throws SQLException {
    	Statement qry=conn[1].createStatement();
        ResultSet rs=qry.executeQuery("select id from notizie where bid='"+bid+"'");
        while(rs.next()) {
        	cancellaNotiziaFromJid(conn, rs.getString("ID"));
        }
        qry.close();
    }
    
    /**
     * Cancella la notizia puntata da <i>jid</i>. jid � la chiave primaria della tabella notizie.
     * @param conn
     * @param jid
     * @throws SQLException
     */
    public static void cancellaNotiziaFromJid(Connection conn[], String jid) throws SQLException {
    	Statement qry2=conn[2].createStatement();
          /*ResultSet rsde=qry2.executeQuery("select id from data_element where id_notizia="+jid);
          while(rsde.next()) {
            execute(conn[1],"delete from l_parole_de where id_de="+rsde.getString("id"));
          }
          execute(conn[1],"delete from data_element where id_notizia="+jid);
          */

          //rsde.close();
          ResultSet lcpn=qry2.executeQuery("select id, id_l_classi_parole from l_classi_parole_notizie where id_notizia="+jid);
          while(lcpn.next()) {
            execute(conn[1],"update l_classi_parole set n_notizie=n_notizie-1 where id="+lcpn.getString("id_l_classi_parole"));
          }
          execute(conn[1],"delete from l_classi_parole_notizie where id_notizia="+jid);

        execute(conn[1],"delete from notizie where id='"+jid+"'");
        execute(conn[1],"delete from notizie_posizione_parole where id_notizia="+jid);
        qry2.close();
    }
    
    public static long getMaxIdTable(Connection conn, String tableName) throws SQLException {
    	long id_nz=0;
        Statement stmt=conn.createStatement();
        ResultSet rs=stmt.executeQuery("SELECT Max("+tableName+".id) AS MaxOfid FROM "+tableName+";");
        if(rs.next()) {
          id_nz=rs.getLong(1);
        }
        rs.close();
        stmt.close();
        return id_nz;
    }
    
    
    
/*    rs=stmt.executeQuery("SELECT Max(data_element.id) AS MaxOfid FROM data_element;");
    if(rs.next()) {
      id_de_f=rs.getInt(1);
    }
    rs.close();
    rs=stmt.executeQuery("SELECT Max(l_parole_de.id) AS MaxOfid FROM l_parole_de;");
    if(rs.next()) {
      id_lpd_f=rs.getInt(1);
    }
    rs.close();*/

    /**
     * 
     * @param conn
     * @param tipo tipoNotizia 
     * @param notizia Stringa
     * @param clDettaglio Hashtable (tag+dataelement,id_classe) dalla tabella classi_dettaglio
     * @throws SQLException
     */    
    public static void inserisciNotizia(Connection conn, String tipo, 
    		String notizia) throws SQLException {
  	  RecordInterface ma;
  	  //ma=ISO2709.creaNotizia(0,notizia,tipo,0);
  	  ma=RecordFactory.buildRecord(0,notizia,tipo,0);
  	  inserisciNotizia(conn,ma);
    }
    
    /**
     * 
     * @param conn
     * @param notizia ISO2709
     * @param clDettaglio Hashtable (tag+dataelement,id_classe) dalla tabella classi_dettaglio
     * @throws SQLException
     */
    public static void inserisciNotizia(Connection conn, RecordInterface notizia) throws SQLException {
    	long idTipo=getIdTipo(conn, notizia.getTipo());
    	long jid=insertTableNotizie(conn,notizia, idTipo);
    	
    	if(notizia.toString()!=null) {
    	
	    	insertHashNotizia(conn,notizia, jid);
	    	
	    	Connection c[]={conn};
	    	ParoleSpooler paroleSpooler=new ParoleSpooler(c,c.length);
	    	
	    	insertNotizia(c,notizia,idTipo,jid,paroleSpooler);
    	}
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
    	Vector<ClasseDettaglio> clDettaglio=initClDettaglio(conn[1],idTipo);
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

/*	
 * 12 giugno 2007, questo metodo era gi� commentato
 * private static void linkNotiziaTag(Connection[] conn, long jid, String tag, 
			String valore_tag, long idTipo, String dl, Vector<ClasseDettaglio> clDettaglio, 
			ParoleSpooler paroleSpooler) {
	    String ctk;
	    
	    boolean unico=true;
	    
	    if(valore_tag.indexOf(dl)>=0) unico=false; 
	    
	    StringTokenizer tk=new StringTokenizer(valore_tag,dl);
	    while(tk.hasMoreTokens()) {
	      String valore="";

	      ctk=tk.nextToken().trim();
	      try {
	    	if(ctk.length()>0) {
	    		String de="";
	    		if(!unico) de=ctk.substring(0,1);
	    		
	    		int k=clDettaglio.indexOf(new ClasseDettaglio(-1,-1,-1,tag,de));
	    		if(k>=0) {
	    			ClasseDettaglio cd=clDettaglio.elementAt(k);
	    			if(!unico) valore=ctk.substring(1);
		            InsertParole(conn,valore,jid,cd,paroleSpooler);
	    		}
	    	}
	      }
	      catch (Exception e) {
	    	  e.printStackTrace();
//			        System.out.println("WARNING! id_notizia:"+Long.toString(id_notizia)+" / tag: "+tag+" / de: "+id_cl_dettaglio+" ignored");
	      }
	    }
	}*/
	
	/**
	 * Ricostruisce la tabelle hash con le firme MD5 delle notizie
	 */
	public static void rebuildHash(Connection conn) throws SQLException {
		DbGateway.dropTable(conn, "hash");
		DbGateway.createHashTable(conn);
		DbGateway.createIndex(conn, "idx_hash", "hash", "hash");
		long maxid=DbGateway.getMaxIdTable(conn, "notizie");
		long step=maxid/100;
		for(long jid=0;jid<maxid;jid++) {
			if(jid % step == 0)
				System.out.println("Rebuilding hash: "+Utils.percentuale(maxid,jid)+"%");
			DbGateway.insertHashNotizia(conn, jid);
		}

	}
	
	public static void rebuildDatabase(Connection[] conn) throws SQLException {
		Hashtable<Integer,String> tipiNotizie=new Hashtable<Integer,String>(); 

		Statement stmt=conn[0].createStatement();
		stmt.execute("truncate table l_parole_de");
		stmt.execute("truncate table l_classi_parole");
		stmt.execute("truncate table l_classi_parole_notizie");
		stmt.execute("truncate table l_parole_de");
		stmt.execute("truncate table data_element");
		stmt.execute("truncate table anagrafe_parole");
		stmt.execute("truncate table notizie_posizione_parole");
		stmt.execute("truncate table TIT_NDX");  //CR_LISTE
				
		ResultSet rs=stmt.executeQuery("select * from tipi_notizie");
		while(rs.next()) {
			tipiNotizie.put(new Integer(rs.getInt("id")),rs.getString("nome"));
		}
		rs.close();
		
		long current=0;
		
    	ParoleSpooler paroleSpooler=new ParoleSpooler(conn,conn.length);
		
		rs=stmt.executeQuery("select * from notizie");
		while(rs.next()) {
			int idTipo=rs.getInt("id_tipo");
			String tipo=tipiNotizie.get(new Integer(idTipo));
			RecordInterface ma=RecordFactory.buildRecord(0,rs.getString("notizia"),tipo,0);
			//ISO2709 ma=ISO2709.creaNotizia(0,rs.getString("notizia"),tipo,0);
			insertNotizia(conn,ma,idTipo,rs.getLong("id"),paroleSpooler); //ISO2709 notizia, long idTipo, long jid
			current++;
			if(current%1000==0) {
				System.out.println(current+" record reimportati");
			}
			ma.destroy();
			ma=null;
		}
		rs.close();
		stmt.close();
		System.out.println(current+" record reimportati");
	}
	
	public static StringTokenizer paroleTokenizer(String valore) {
		return  new StringTokenizer(processaMarcatori(valore),SEPARATORI_PAROLE);
	}

	private static void InsertParole(Connection conn[], String valore, long jid, long idSequenzaTag, ClasseDettaglio cl, 
			ParoleSpooler paroleSpooler) throws SQLException {
	    String parola;
	    StringTokenizer tk=paroleTokenizer(valore);
	    boolean asterisco_trovato=!(valore.indexOf(" *")<DbGateway.MAX_POSIZIONE_ASTERISCO);
	    long posizione_parola=0;

	    while(tk.hasMoreTokens()) {
	      parola=tk.nextToken().trim();
	      
	      if(parola.startsWith("*")) {
	    	  asterisco_trovato=true;
	    	  parola=parola.substring(1); // parola senza l'asterisco
	      }
	      
	      if((parola!=null)&&(parola.length()>0)) {
		      long id_parola=InsertParola(conn[1],parola,paroleSpooler);
		      long id_lcp=insertLClassiParole(conn[2],id_parola,cl.getIdClasse());
		      insertLClassiParoleNotizie(conn[3],jid,id_lcp);
		      
		      if(asterisco_trovato) {
		    	  insertNotiziePosizioneParole(conn[0],jid, idSequenzaTag, cl.getIdClasse(), id_parola, posizione_parola, parola);
		    	  posizione_parola++;
		      }
	      }
	    }
	}
	
	/*private static long insertLParoleDE(Connection conn, long id_parola, long id_de) throws SQLException {
    	long id_lpde=getMaxIdTable(conn,"l_parole_de")+1;
    	Statement stmt=conn.createStatement();
    	stmt.execute("INSERT INTO l_parole_de (id,id_parola,id_de) VALUES (" + 
    			id_lpde + ", " + id_parola + ", " + id_de + ")");
    	stmt.close();
		return id_lpde;
	}*/

	/*private static long insertDataElement(Connection conn, long jid, long id_cl) throws SQLException {
    	long id_de=getMaxIdTable(conn,"data_element")+1;
    	Statement stmt=conn.createStatement();
    	stmt.execute("INSERT INTO data_element (id,id_notizia,id_classi_dettaglio) VALUES (" + 
    			id_de + ", " + jid + ", " + id_cl +")");
    	stmt.close();
		return id_de;
	}*/

	public static void insertNotiziePosizioneParole(Connection conn, long jid, long idSequenzaTag, long idClasse, long id_parola, long posizione_parola, String parola) throws SQLException {
		Statement stmt=conn.createStatement();
    	stmt.execute("INSERT INTO notizie_posizione_parole (id_notizia,id_classe,id_sequenza_tag, id_parola,posizione_parola, parola) " +
    			"VALUES (" + jid + ", " + idClasse + ", " + idSequenzaTag + ", " + id_parola + ", " + posizione_parola + ", '" + parola + "')");
    	stmt.close();
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

	public static long getIdTipo(Connection conn, String tipo) {
		return getIDwhere(conn, "tipi_notizie", "nome", tipo);
	}
	
	  // determina se eliminare parole (inizia per ESC+H e ESC+I o *)
	  /**
	   * TODO: dovrebbe essere in MARC o ISO2709 piuttosto?
	   * 07/03/2003 - R.T.
	   *      Cambiato: ai fini dell'indicizzazione vanno inserite anche le
	   *      parole prima della parte significativa del titolo.
	   *      Quindi semplicemente se c'� l'asterisco lo tolgo e se ci sono
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
		  DbGateway.execute(conn, "insert into anagrafe_parole (parola,ID,stemma) values ('"+parola+"',"+id_parola+", '"+stemma+"');");
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

	/**
	 * ricostruisce la stringa legata ad un valore rispettando la posizione delle parole
	 * @param myConnection
	 * @param lclasseid
	 * @param offset
	 * @return
	 */
	public static String ricostruisceValore(Connection myConnection, int lclasseid, long id_notizia) {
		String query="SELECT id_notizia, id_parola,posizione_parola,parola "+
		"FROM notizie_posizione_parole a, anagrafe_parole p " +
		"WHERE a.id_notizia="+id_notizia+" and a.id_parola=p.id and a.id_classe="+lclasseid+
		" ORDER BY a.posizione_parola";
		String dmy="";
		
		desc(myConnection,query);
		
		try {
	        Statement stmt=myConnection.createStatement();
	        ResultSet rs=stmt.executeQuery(query);
	        
	        while(rs.next()) {
	        	dmy+=rs.getString("parola")+" ";
	        }
	        rs.close();
	        stmt.close();
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      }
		return dmy.trim();
	}	
	
	/**
	 * restituisce elenco di valori,legati alla classe, ricostruendo la stringa relativa
	 * @param myConnection
	 * @param id_classe
	 * @param from_parola
	 * @return
	 */
	public static Vector<String> listParole(Connection myConnection, int id_classe, String from_parola) {
		Vector<String> r=new Vector<String>();
		String sql="SELECT distinct parola " +
			"FROM notizie_posizione_parole a " +
			"WHERE a.id_classe="+id_classe+" and a.posizione_parola=0 " +
			"and parola>='"+from_parola+"' " +
			"ORDER BY parola " +
			"LIMIT 10";
		
		desc(myConnection,sql);
		
		try {
	        Statement stmt=myConnection.createStatement();
	        ResultSet rs=stmt.executeQuery(sql);
	        
	        while(rs.next()) {
	        	r.addElement(rs.getString("parola"));
	        }
	        
	        rs.close();
	        stmt.close();
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      }
		return r;
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

	public static void createTableRicerche(Connection conn) throws SQLException {
		DbGateway.dropTable(conn, "dettaglio_ricerche");
		String sql1="CREATE TABLE ricerche_dettaglio (" +
				  "id_notizia INT NOT NULL, " +
				  "id_ricerca INT NOT NULL, " +
				  "PRIMARY KEY(id_notizia, id_ricerca) " +
				") " +
				"ENGINE = MYISAM " +
				"CHARACTER SET utf8";


		String sql2="CREATE TABLE ricerche (" +
				  "id INT NOT NULL AUTO_INCREMENT, " +
				  "jsession_id varchar(100) NOT NULL, " +
				  "testo_ricerca LONGTEXT, " +
				  "PRIMARY KEY(id), " +
				  "INDEX jsession_idx(jsession_id) " +
				") " +
				"ENGINE = MYISAM " +
				"CHARACTER SET utf8";

		Statement stmt=conn.createStatement();
		stmt.execute(sql1);
		stmt.execute(sql2);
		stmt.close();
	}
	
	public static void createTableListe(Connection conn) throws SQLException {//CR_LISTE
		String classe="TIT";
		String tab=classe+"_NDX";
		DbGateway.dropTable(conn, tab);
		String sql1="CREATE TABLE "+tab+" (id_notizia INT NOT NULL,testo varchar(50)," +
				    "PRIMARY KEY(id_notizia)) ENGINE = MYISAM CHARACTER SET utf8";
		Statement stmt=conn.createStatement();
		stmt.execute(sql1);
		stmt.close();
	}
	
	public static void updateTableListe(Connection conn, int id_notizia, String testo) throws SQLException {
		String classe="TIT";
		String tab=classe+"_NDX";
		String dl=String.valueOf((char)0x1b);
		
		String sql="REPLACE INTO "+tab+" SET id_notizia=?, testo=?";
		PreparedStatement pst=conn.prepareStatement(sql);
		pst.setInt(1, id_notizia);
		testo=testo.replaceAll(dl+"I", ""); // rimuove delimitatori asterisco
		testo=testo.replaceAll(dl+"H", "");
		if(testo.length()>50) testo=testo.substring(0,49);
		pst.setString(2, testo);
		pst.execute();
		pst.close();
	}
	
	public static void rebuildList(Connection conn) throws SQLException {
		//String classe="TIT";
		//String tab=classe+"_NDX";
		
		createTableListe(conn);
		
		long maxid=DbGateway.getMaxIdTable(conn, "notizie");
		long step=maxid/100;
		for(int jid=0;jid<maxid;jid++) {
			if(jid % step == 0)
				System.out.println("Rebuilding list index: "+Utils.percentuale(maxid,jid)+"%");
			RecordInterface ma=DbGateway.getNotiziaByJID(conn, jid);
			if(ma!=null) 
				DbGateway.updateTableListe(conn, jid, ma.getTitle());
		}

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
}
