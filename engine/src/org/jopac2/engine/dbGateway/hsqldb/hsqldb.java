package org.jopac2.engine.dbGateway.hsqldb;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.utils.SearchResultSet;

public class hsqldb extends DbGateway {
	public hsqldb(PrintStream console) {
		super(console);
	}

	protected void createHashTable(Connection conn) throws SQLException {
		DbGateway.execute(conn,
				"create table hash (id_notizia integer not null identity primary key,"
						+ "hash varchar(32) not null");

	}
	
	public void createAllTables(Connection conn) throws SQLException {
		dropTable(conn,"notizie");
		// TODO ciclare su tutte le classi
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
    	
        DbGateway.execute(conn,"create table notizie (id integer not null identity primary key,"+
        	"bid varchar(14),id_tipo integer,notizia varchar)");
        createHashTable(conn);
        
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
	    DbGateway.execute(conn,"create table temp_lcpn "+
            "(id_notizia int,id_parola int,id_classe int)");
	    
	    createTableRicerche(conn);
	}
	
	public void createTableRicerche(Connection conn) throws SQLException {
		dropTable(conn, "dettaglio_ricerche");
		String sql1="CREATE TABLE ricerche_dettaglio (" +
				  "id_notizia INT NOT NULL, " +
				  "id_ricerca INT NOT NULL, " +
				  "PRIMARY KEY(id_notizia, id_ricerca) " +
				") ";


		String sql2="CREATE TABLE ricerche (" +
				  "id INT NOT NULL identity primary key, " +
				  "jsession_id varchar(100) NOT NULL, " +
				  "testo_ricerca LONGTEXT, " +
				  "INDEX jsession_idx(jsession_id) " +
				") ";

		Statement stmt=conn.createStatement();
		stmt.execute(sql1);
		stmt.execute(sql2);
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
        
        try {
            Statement stmt=conn.createStatement();

            ResultSet r=stmt.executeQuery("select id from "+tableName+" "+
                "where ("+fieldName+"='"+fieldValue+"')");
            if(r.next()) currentID=r.getLong("id");
            r.close();
            
            if((currentID==-1)) {
                stmt.execute("insert into "+tableName+" ("+fieldName+") "+
                    "values('"+fieldValue+"')");
                r = stmt.executeQuery("call identity()"); 
                
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
    
    public  void createDBl_tables(Connection conn, PrintStream console) throws SQLException {
        execute(conn, "drop table if exists l_classi_parole",true, console);
        

        execute(conn, "create table l_classi_parole ("+
            "ID int not null identity primary key,id_parola int,id_classe int,"+
            "n_notizie int)");

        execute(conn, "create index l_classi_parole_idx1 on l_classi_parole (id_parola)");
        execute(conn, "create index l_classi_parole_idx2 on l_classi_parole (id_classe)");
        execute(conn, "create index l_classi_parole_idx3 on l_classi_parole (id_parola,id_classe)");

        execute(conn, "drop table if exists l_classi_parole_notizie",true, console);
        
    	execute(conn, "create table l_classi_parole_notizie ("+
	            "ID int not null identity primary key,id_l_classi_parole int,"+
	            "id_notizia int)");

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
            "group by id_parola,id_classe",true, console);

        //--minuti: 16min, 6780360 (=temp_lcpn)
        

        execute(conn, "insert into l_classi_parole_notizie(id_notizia,id_l_classi_parole) "+
	            "select id_notizia, lcp.id "+
	            "from l_classi_parole lcp, "+
	            "temp_lcpn "+
	            "where "+
	            "temp_lcpn.id_parola=lcp.id_parola and "+
	            "temp_lcpn.id_classe=lcp.id_classe",true, console);


//        execute("drop table if exists temp_lcpn");
    }
    
    /**
     * Drops table <i>tableName</i>
     * @param conn
     * @param tableName
     * @throws SQLException 
     */
    public void dropTable(Connection conn, String tableName) throws SQLException {
		execute(conn,"drop table " + tableName);
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
    
	public void createTableListe(Connection conn,String classe) throws SQLException {//CR_LISTE
		dropTable(conn, nomeTableListe(classe));
		String sql1="CREATE TABLE "+nomeTableListe(classe)+" (id INT NOT NULL auto_increment, id_notizia INT NOT NULL,testo varchar(50)," +
				    "PRIMARY KEY(id)) ENGINE = MYISAM CHARACTER SET utf8";
		Statement stmt=conn.createStatement();
		stmt.execute(sql1);
		stmt.close();
    	createIndex(conn,nomeTableListe(classe)+"_x1", nomeTableListe(classe), "testo(50)", false);//CR_LISTE

	}
	
	public SearchResultSet listSearch(Connection conn,String classe,String parole,int limit) throws SQLException {
		Vector<Long> listResult=new Vector<Long>();		
		
		String sql="select * from "+DbGateway.nomeTableListe(classe)+" b, "+
			"(SELECT distinct testo FROM "+DbGateway.nomeTableListe(classe)+" a "+
			"where testo >= ?  order by testo limit ?) c "+ 
			"where b.testo=c.testo order by b.testo";

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
}
