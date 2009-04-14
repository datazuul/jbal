package org.jopac2.engine.dbGateway.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jopac2.engine.dbGateway.DbGateway;

public class mysql extends DbGateway {

	protected void createHashTable(Connection conn) throws SQLException {
		DbGateway.execute(conn,"create table hash (hash varchar(32) not null,"+
        "id_notizia integer not null,primary key(id_notizia)) ENGINE = MYISAM DEFAULT CHARSET=utf8");
    }
	
	public void createAllTables(Connection conn) throws SQLException {
    	dropTable(conn,"notizie");
    	//TODO cicla su tutte
    	dropTable(conn,nomeTableListe("TIT"));//CR_LISTE
    	//DbGateway.dropTable(conn,"data_element");
    	//DbGateway.dropTable(conn,"l_parole_de");
    	dropTable(conn,"anagrafe_parole");
    	dropTable(conn,"classi");
    	dropTable(conn,"classi_dettaglio");
    	dropTable(conn,"tipi_notizie");
    	dropTable(conn,"temp_lcpn");
    	dropTable(conn,"hash");
    	dropTable(conn, "ricerche_dettaglio");
    	dropTable(conn, "ricerche");
    	
    	String mysql=" ENGINE = MYISAM DEFAULT CHARSET=utf8";
    	String autoincrement="auto_increment";
    	
    	
        DbGateway.execute(conn,"create table notizie (id integer not null "+autoincrement+","+
        "bid varchar(50),id_tipo integer,notizia blob,primary key(id))"+mysql);
        createHashTable(conn);
	    //DbGateway.execute(conn,"create table data_element (ID int not null,"+
	    //    "id_notizia int,id_classi_dettaglio int) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    //DbGateway.execute(conn,"create table l_parole_de (ID int not null,"+
	    //"id_parola int,id_de int) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    DbGateway.execute(conn,"create table anagrafe_parole (ID int not null "+autoincrement+","+
	        "parola varchar(50), stemma varchar(50),primary key(id))"+mysql); //type=memory
	    //DbGateway.execute(conn,"create table classi ("+
	    //    "ID int not null auto_increment,nome char(30) ,primary key(id)) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    DbGateway.execute(conn,"create table classi_dettaglio ("+
	        "ID int not null "+autoincrement+",id_tipo int, id_classe int,"+
	        "tag char(50),data_element char(1) ,primary key(id))"+mysql);
	    DbGateway.execute(conn,"create table tipi_notizie ("+
	        "ID int not null "+autoincrement+", nome text,primary key(id))"+mysql);
	    DbGateway.execute(conn,"create table temp_lcpn "+
            "(id_notizia int,id_parola int,id_classe int " +
//            ", primary key(id_notizia,id_parola,id_classe) "+
            ")"+mysql);
	    createTableRicerche(conn);
	    //DbGateway.createTableListe(conn,"TIT");//CR_LISTE
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
    public long getIDwhere(Connection conn, String tableName, String fieldName, String fieldValue) {
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
                r = stmt.executeQuery("SELECT last_insert_id()");
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
    
    public void createDBl_tables(Connection conn) throws SQLException {
        execute(conn, "drop table if exists l_classi_parole",true);
        
        
        execute(conn, "create table l_classi_parole ("+
            "ID int not null auto_increment,id_parola int,id_classe int,"+
            "n_notizie int ,primary key(id)) ENGINE = MYISAM DEFAULT CHARSET=utf8");

        execute(conn, "create index l_classi_parole_idx1 on l_classi_parole (id_parola)");
        execute(conn, "create index l_classi_parole_idx2 on l_classi_parole (id_classe)");
        execute(conn, "create index l_classi_parole_idx3 on l_classi_parole (id_parola,id_classe)");

        execute(conn, "drop table if exists l_classi_parole_notizie",true);
        
        
        execute(conn, "create table l_classi_parole_notizie ("+
            "ID int not null auto_increment,id_l_classi_parole int,"+
            "id_notizia int,primary key(id)) ENGINE = MYISAM DEFAULT CHARSET=utf8");

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
        

        execute(conn, "insert into l_classi_parole_notizie(id_notizia,id_l_classi_parole) "+
            "select id_notizia,lcp.id "+
            "from l_classi_parole lcp use index (l_classi_parole_idx3), "+
            "temp_lcpn force index (temp_3) "+
            "where "+
            "temp_lcpn.id_parola=lcp.id_parola and "+
            "temp_lcpn.id_classe=lcp.id_classe",true);


//        execute("drop table if exists temp_lcpn");
    }
    
    public void createTableRicerche(Connection conn) throws SQLException {
		dropTable(conn, "dettaglio_ricerche");
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
		
		createIndex(conn,"ricerche_dettaglio_idx1", "ricerche_dettaglio", "id_ricerca");		
	}
    
    /**
     * Drops table <i>tableName</i>
     * @param conn
     * @param tableName
     * @throws SQLException 
     */
    public void dropTable(Connection conn, String tableName) throws SQLException {
    		execute(conn,"drop table if exists " + tableName);
    }
    
    /**
     * Creates index with <i>indexName</i> on table <i>tableName</i> using keys <i>keys</i>
     * @param conn
     * @param indexName
     * @param tableName
     * @param unique true/false
     * @param keys
     * @throws SQLException 
     */
    public void createIndex(Connection conn, String indexName, String tableName, String keys, boolean unique) throws SQLException {
    	String isUnique="";
    	if(unique) isUnique="unique";

    	String sql="create " + isUnique + " index " + indexName + " on " + tableName + " (" + keys + ")";
    	execute(conn, sql);
    }

}
