package org.jopac2.engine.dbGateway.mysql;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.utils.SearchResultSet;

public class mysql extends DbGateway {

	public mysql(PrintStream console) {
		super(console);
	}

	protected void createHashTable(Connection conn, String catalog) throws SQLException {
		DbGateway.execute(conn,"create table je_"+catalog+"_hash (hash varchar(32) not null,"+
        "id_notizia integer not null,primary key(id_notizia)) ENGINE = MYISAM DEFAULT CHARSET=utf8");
    }
	
	public void createAllTables(Connection conn, String catalog) throws SQLException {
    	dropTable(conn,"je_"+catalog+"_notizie");
    	//TODO cicla su tutte
    	dropTable(conn,"je_"+catalog+"_"+nomeTableListe("TIT"));//CR_LISTE
    	//DbGateway.dropTable(conn,"data_element");
    	//DbGateway.dropTable(conn,"l_parole_de");
    	dropTable(conn,"je_"+catalog+"_anagrafe_parole");
    	dropTable(conn,"je_"+catalog+"_classi");
    	dropTable(conn,"je_"+catalog+"_classi_dettaglio");
    	dropTable(conn,"je_"+catalog+"_tipi_notizie");
    	dropTable(conn,"je_"+catalog+"_temp_lcpn");
    	dropTable(conn,"je_"+catalog+"_hash");
    	dropTable(conn, "je_"+catalog+"_ricerche_dettaglio");
    	dropTable(conn, "je_"+catalog+"_ricerche");
    	
    	String mysql=" ENGINE = MYISAM DEFAULT CHARSET=utf8";
    	String autoincrement="auto_increment";
    	
    	
        DbGateway.execute(conn,"create table je_"+catalog+"_notizie (id integer not null "+autoincrement+","+
        "bid varchar(50),id_tipo integer,notizia blob,primary key(id))"+mysql);
        createHashTable(conn,catalog);
	    //DbGateway.execute(conn,"create table je_"+catalog+"_data_element (ID int not null,"+
	    //    "id_notizia int,id_classi_dettaglio int) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    //DbGateway.execute(conn,"create table je_"+catalog+"_l_parole_de (ID int not null,"+
	    //"id_parola int,id_de int) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    DbGateway.execute(conn,"create table je_"+catalog+"_anagrafe_parole (ID int not null "+autoincrement+","+
	        "parola varchar(50), stemma varchar(50),primary key(id))"+mysql); //type=memory
	    DbGateway.execute(conn,"create table je_"+catalog+"_classi ("+
	        "ID int not null auto_increment,nome char(30) ,primary key(id)) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    DbGateway.execute(conn,"create table je_"+catalog+"_classi_dettaglio ("+
	        "ID int not null "+autoincrement+",id_tipo int, id_classe int,"+
	        "tag char(50),data_element char(1) ,primary key(id))"+mysql);
	    DbGateway.execute(conn,"create table je_"+catalog+"_tipi_notizie ("+
	        "ID int not null "+autoincrement+", nome text,primary key(id))"+mysql);
	    DbGateway.execute(conn,"create table je_"+catalog+"_temp_lcpn "+
            "(id_notizia int,id_parola int,id_classe int " +
//            ", primary key(id_notizia,id_parola,id_classe) "+
            ")"+mysql);
	    createTableRicerche(conn,catalog);
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
        fieldValue=fieldValue.toLowerCase();
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
    
    public void createDBl_tables(Connection conn, String catalog, PrintStream console) throws SQLException {
        execute(conn, "drop table if exists je_"+catalog+"_l_classi_parole",true, console);
        
        
        execute(conn, "create table je_"+catalog+"_l_classi_parole ("+
            "ID int not null auto_increment,id_parola int,id_classe int,"+
            "n_notizie int ,primary key(id)) ENGINE = MYISAM DEFAULT CHARSET=utf8");

        execute(conn, "create index je_"+catalog+"_l_classi_parole_idx1 on je_"+catalog+"_l_classi_parole (id_parola)");
        execute(conn, "create index je_"+catalog+"_l_classi_parole_idx2 on je_"+catalog+"_l_classi_parole (id_classe)");
        execute(conn, "create index je_"+catalog+"_l_classi_parole_idx3 on je_"+catalog+"_l_classi_parole (id_parola,id_classe)");

        execute(conn, "drop table if exists je_"+catalog+"_l_classi_parole_notizie",true, console);
        
        
        execute(conn, "create table je_"+catalog+"_l_classi_parole_notizie ("+
            "ID int not null auto_increment,id_l_classi_parole int,"+
            "id_notizia int,primary key(id)) ENGINE = MYISAM DEFAULT CHARSET=utf8");

        execute(conn, "create index je_"+catalog+"_l_classi_parole_notizie_idclassi "+
            "on je_"+catalog+"_l_classi_parole_notizie (id_l_classi_parole)");
        execute(conn, "create index je_"+catalog+"_l_classi_parole_notizie_idnotizie "+
            "on je_"+catalog+"_l_classi_parole_notizie (id_notizia)");

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
        
        execute(conn, "insert into je_"+catalog+"_l_classi_parole (id_parola,id_classe,n_notizie) "+
            "select id_parola,id_classe,count(*) as n_notizie "+
            "from je_"+catalog+"_temp_lcpn "+
            "group by id_parola,id_classe",true, console);

        //--minuti: 16min, 6780360 (=temp_lcpn)
        

        execute(conn, "insert into je_"+catalog+"_l_classi_parole_notizie(id_notizia,id_l_classi_parole) "+
            "select id_notizia,lcp.id "+
            "from je_"+catalog+"_l_classi_parole lcp use index (je_"+catalog+"_l_classi_parole_idx3), "+
            "je_"+catalog+"_temp_lcpn temp_lcpn force index (je_"+catalog+"_temp_3) "+
            "where "+
            "temp_lcpn.id_parola=lcp.id_parola and "+
            "temp_lcpn.id_classe=lcp.id_classe",true, console);


//        execute("drop table if exists temp_lcpn");
    }
    
    public void createTableRicerche(Connection conn, String catalog) throws SQLException {
		dropTable(conn, "je_"+catalog+"_dettaglio_ricerche");
		String sql1="CREATE TABLE je_"+catalog+"_ricerche_dettaglio (" +
				  "id_notizia INT NOT NULL, " +
				  "id_ricerca INT NOT NULL, " +
				  "PRIMARY KEY(id_notizia, id_ricerca) " +
				") " +
				"ENGINE = MYISAM " +
				"CHARACTER SET utf8";


		String sql2="CREATE TABLE je_"+catalog+"_ricerche (" +
				  "id INT NOT NULL AUTO_INCREMENT, " +
				  "jsession_id varchar(100) NOT NULL, " +
				  "testo_ricerca LONGTEXT, " +
				  "PRIMARY KEY(id), " +
				  "INDEX je_"+catalog+"_jsession_idx(jsession_id) " +
				") " +
				"ENGINE = MYISAM " +
				"CHARACTER SET utf8";

		Statement stmt=conn.createStatement();
		stmt.execute(sql1);
		stmt.execute(sql2);
		stmt.close();
		
		createIndex(conn,"je_"+catalog+"_ricerche_dettaglio_idx1", "je_"+catalog+"_ricerche_dettaglio", "id_ricerca");		
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
    
	public void createTableListe(Connection conn,String catalog, String classe) throws SQLException {//CR_LISTE
		dropTable(conn, "je_"+catalog+"_"+nomeTableListe(classe));
		String sql1="CREATE TABLE je_"+catalog+"_"+nomeTableListe(classe)+" (id INT NOT NULL auto_increment, id_notizia INT NOT NULL,testo varchar(50)," +
				    "PRIMARY KEY(id)) ENGINE = MYISAM CHARACTER SET utf8";
		Statement stmt=conn.createStatement();
		stmt.execute(sql1);
		stmt.close();
    	createIndex(conn,"je_"+catalog+"_"+nomeTableListe(classe)+"_x1", "je_"+catalog+"_"+nomeTableListe(classe), "testo(50)", false);//CR_LISTE

	}
	
	public SearchResultSet listSearchFB(Connection conn,String catalog,String classe,String parole,int limit, boolean forward) throws SQLException {
		Vector<Long> listResult=new Vector<Long>();
		
		String d=">=";
		if(!forward) d="<=";
		
		String sql="select * from je_"+catalog+"_"+DbGateway.nomeTableListe(classe)+" b, "+
			"(SELECT distinct testo FROM je_"+catalog+"_"+DbGateway.nomeTableListe(classe)+" a "+
			"where testo "+d+" ?  order by testo limit ?) c "+ 
			"where b.testo=c.testo order by b.testo"+(forward?"":" desc");

		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, parole);
		stmt.setLong(2, limit);
		ResultSet rs = stmt.executeQuery();
		int id_notizia;
		while (rs.next()) {
			id_notizia = rs.getInt("id_notizia");
			listResult.addElement(new Long(id_notizia));
		}
		rs.close();
		stmt.close();
		SearchResultSet result=new SearchResultSet();
		result.setRecordIDs(listResult);
		return result;
	}
	
	  public Connection createConnection(String dbUrl, String dbUser, String dbPassword) throws SQLException {
		Connection conn = null;
		String driver = "com.mysql.jdbc.Driver";

		boolean inizializzato = false;
		if (!inizializzato) {
			inizializzato = true;
			try {
				Class.forName(driver).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("getting conn....");
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		System.out.println("presa");

		return conn;
	}
}
