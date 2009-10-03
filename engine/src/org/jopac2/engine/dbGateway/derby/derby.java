package org.jopac2.engine.dbGateway.derby;

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

public class derby extends DbGateway {    	
	public derby(PrintStream console) {
		super(console);
	}

	String autoincrement=""; // "GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)";
	String autoincrement1="GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)";


	protected void createHashTable(Connection conn, String catalog) throws SQLException {
		DbGateway.execute(conn,"create table je_"+catalog+"_hash (hash varchar(32) not null,"+
        "id_notizia integer not null,primary key(id_notizia))");
    }
	
	public void createAllTables(Connection conn, String catalog) throws SQLException {
    	dropTable(conn,"je_"+catalog+"_notizie");
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
    	
    	
        DbGateway.execute(conn,"create table je_"+catalog+"_notizie (id integer not null "+autoincrement+","+
        "bid varchar(50),id_tipo integer,notizia blob,primary key(id))"); 
        createHashTable(conn, catalog);
	    //DbGateway.execute(conn,"create table data_element (ID int not null,"+
	    //    "id_notizia int,id_classi_dettaglio int) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    //DbGateway.execute(conn,"create table l_parole_de (ID int not null,"+
	    //"id_parola int,id_de int) ENGINE = MYISAM DEFAULT CHARSET=utf8");
	    DbGateway.execute(conn,"create table je_"+catalog+"_anagrafe_parole (ID int not null "+autoincrement+","+
	        "parola varchar(50), stemma varchar(50),primary key(id))"); //type=memory // ,primary key(id)
	    DbGateway.execute(conn,"create table je_"+catalog+"_classi ("+
	        "ID int not null "+autoincrement1+",nome char(30) ,primary key(id))");
	    DbGateway.execute(conn,"create table je_"+catalog+"_classi_dettaglio ("+
	        "ID int not null "+autoincrement1+",id_tipo int, id_classe int,"+
	        "tag char(50),data_element char(1) ,primary key(id))"); //,primary key(id)
	    DbGateway.execute(conn,"create table je_"+catalog+"_tipi_notizie ("+
	        "ID int not null "+autoincrement1+", nome varchar(20),primary key(id))"); //,primary key(id)
	    DbGateway.execute(conn,"create table je_"+catalog+"_temp_lcpn "+
            "(id_notizia int,id_parola int,id_classe int " +
//            ", primary key(id_notizia,id_parola,id_classe) "+
            ")");
	
	    createTableRicerche(conn, catalog);
	    //DbGateway.createTableListe(conn,"TIT");//CR_LISTE
    }
	
	public void createTableRicerche(Connection conn, String catalog) throws SQLException {
		dropTable(conn, "je_"+catalog+"_dettaglio_ricerche");
		String sql1="CREATE TABLE je_"+catalog+"_ricerche_dettaglio (" +
				  "id_notizia INT NOT NULL, " +
				  "id_ricerca INT NOT NULL, " +
				  "PRIMARY KEY(id_notizia, id_ricerca) " +
				") ";


		String sql2="CREATE TABLE je_"+catalog+"_ricerche (" +
				  "id INT NOT NULL "+autoincrement1+", " +
				  "jsession_id varchar(100) NOT NULL, " +
				  "testo_ricerca clob, " +
				  "PRIMARY KEY(id)" +
				  //", INDEX jsession_idx(jsession_id) " +
				") ";

		Statement stmt=conn.createStatement();
		stmt.execute(sql1);
		stmt.execute(sql2);
		
		execute(conn, "create index je_"+catalog+"_jsession_idx on je_"+catalog+"_ricerche (jsession_id)");

		stmt.close();
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
                r = stmt.executeQuery("VALUES IDENTITY_VAL_LOCAL()");
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
    
    public  void createDBl_tables(Connection conn, String catalog, PrintStream console) throws SQLException {
    	try {
        execute(conn, "drop table je_"+catalog+"_l_classi_parole",true,console);
    	}
    	catch(Exception e) {}
        

        execute(conn, "create table je_"+catalog+"_l_classi_parole ("+
            "ID int not null "+autoincrement1+",id_parola int,id_classe int,"+ // auto_increment
            "n_notizie int ,primary key(id)) ");

        execute(conn, "create index je_"+catalog+"_l_classi_parole_idx1 on je_"+catalog+"_l_classi_parole (id_parola)");
        execute(conn, "create index je_"+catalog+"_l_classi_parole_idx2 on je_"+catalog+"_l_classi_parole (id_classe)");
        execute(conn, "create index je_"+catalog+"_l_classi_parole_idx3 on je_"+catalog+"_l_classi_parole (id_parola,id_classe)");

        try {
        	execute(conn, "drop table je_"+catalog+"_l_classi_parole_notizie",true,console);
        }
        catch(Exception e) {}
        

        execute(conn, "create table je_"+catalog+"_l_classi_parole_notizie ("+
            "ID int not null "+autoincrement1+",id_l_classi_parole int,"+
            "id_notizia int,primary key(id)) ");

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
            "group by id_parola,id_classe",true,console);

        //--minuti: 16min, 6780360 (=temp_lcpn)
        

        execute(conn, "insert into je_"+catalog+"_l_classi_parole_notizie(id_notizia,id_l_classi_parole) "+
            "select id_notizia,lcp.id "+
            "from je_"+catalog+"_l_classi_parole lcp "+ // use index (l_classi_parole_idx3)
            ", je_"+catalog+"_temp_lcpn temp_lcpn "+ // force index (temp_3)
            "where "+
            "temp_lcpn.id_parola=lcp.id_parola and "+
            "temp_lcpn.id_classe=lcp.id_classe",true,console);


//        execute("drop table if exists temp_lcpn");
    }
    
    /**
     * Drops table <i>tableName</i>
     * @param conn
     * @param tableName
     * @throws SQLException 
     */
    public void dropTable(Connection conn, String tableName) throws SQLException {
    	try {
    		execute(conn,"drop table " + tableName);
    	}
    	catch(SQLException e) {} // nop, table does not exists
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

		int k=keys.indexOf('(');
		while(k>=0) {
			int z=keys.indexOf(')');
			keys=keys.substring(0,k)+keys.substring(z+1);
			k=keys.indexOf('(');
		}

    	String sql="create " + isUnique + " index " + indexName + " on " + tableName + " (" + keys + ")";
    	execute(conn, sql);
    }
    
	public void createTableListe(Connection conn, String catalog, String classe) throws SQLException {//CR_LISTE
		dropTable(conn, "je_"+catalog+"_"+nomeTableListe(classe));
		String sql1="CREATE TABLE je_"+catalog+"_"+nomeTableListe(classe)+" (id INT NOT NULL "+autoincrement1+", id_notizia INT NOT NULL,testo varchar(50)," +
				    "PRIMARY KEY(id))";
		Statement stmt=conn.createStatement();
		stmt.execute(sql1);
		stmt.close();
    	createIndex(conn,"je_"+catalog+"_"+nomeTableListe(classe)+"_x1", "je_"+catalog+"_"+nomeTableListe(classe), "testo(50)", false);//CR_LISTE

	}
	
	public SearchResultSet listSearchFB(Connection conn, String catalog, String classe,String parole,int limit,boolean forward) throws SQLException {
		Vector<Long> listResult=new Vector<Long>();
		
		/**
		 * SELECT * FROM (
		 *	    SELECT ROW_NUMBER() OVER() AS rownum, myLargeTable.*
		 *	    FROM myLargeTable
		 *	) AS tmp
		 *	WHERE rownum <= 5; 
		 */
		String d=">=";
		if(!forward) d="<=";
		
		String sql="select * " +
				"from je_"+catalog+"_"+DbGateway.nomeTableListe(classe)+" b, "+
					"(" +
						"select * " +
						"from " +
						"(SELECT distinct testo, ROW_NUMBER() OVER() AS rownum " +
							"FROM je_"+catalog+"_"+DbGateway.nomeTableListe(classe)+" a "+
							"where testo "+d+" ?  " +
						") as tmp where rownum <= ?" +
					") c "+ //order by testo limit ?
					"where b.testo=c.testo " +
					"order by b.testo"+(forward?"":" desc");

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

	@Override
	  public Connection createConnection(String dbUrl, String dbUser, String dbPassword) throws SQLException {
		Connection conn = null;
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";

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
