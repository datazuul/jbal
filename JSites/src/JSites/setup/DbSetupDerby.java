package JSites.setup;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import JSites.utils.DBGateway;

public class DbSetupDerby extends DbSetup {
	private static String postfix=""; //"DEFAULT CHARSET=utf8;";
	
	private void dropTable(Connection conn, String tableName) {
		String sql="DROP TABLE "+tableName;
		try {
			Statement st=conn.createStatement();
//			st.execute(sql);
		}
		catch(Exception e) {
//			e.printStackTrace();
			// eccezione se la tabella non esiste
		}
	}
	
	public void createTableTblStrutture(Connection conn) throws SQLException {
		dropTable(conn,"tblstrutture");
		Statement st=conn.createStatement();		
		String sql="CREATE TABLE tblstrutture (" +
				"PID int NOT NULL default 0," +
				"CID int NOT NULL default 0," +
				"PRIMARY KEY (PID,CID)) " + postfix;
//				"KEY Index_1 USING BTREE (PID)," +
//				"KEY Index_2 USING BTREE (CID)) " + postfix;
		st.execute(sql);
		sql="create index tblstrutture_Index_2 on tblstrutture(PID)";
		st.execute(sql);
		sql="create index tblstrutture_Index_3 on tblstrutture(CID)";
		st.execute(sql);
		st.close();
	}
	
	public void createTableTblStati(Connection conn) throws SQLException {
		dropTable(conn,"tblstati");
		Statement st=conn.createStatement();		
		String sql="CREATE TABLE tblstati (" +
				"StateID int NOT NULL generated always as identity," +
				"StateName varchar(5) NOT NULL," +
				"Scope varchar(45) NOT NULL," +
				"PRIMARY KEY  (StateID)) "  + postfix;
//				"KEY Index_3 USING BTREE (Scope)) " + postfix;
//				"ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;";
		st.execute(sql);
		sql="create index tblstati_Index_2 on tblstati(Scope)";
		st.execute(sql);
		st.close();
	}
	
	public void createTableTblRoles(Connection conn) throws SQLException {
		dropTable(conn,"tblroles");
		Statement st=conn.createStatement();		
		String sql="CREATE TABLE tblroles (" +
				"Username varchar(50) NOT NULL," +
				"PermissionCode int NOT NULL default 0," +
				"PID int NOT NULL default 0) " + postfix;
//				"KEY Index_2 (User)," +
//				"KEY Index_3 USING BTREE (User))" + postfix;
//				"ENGINE=MyISAM DEFAULT CHARSET=latin1;";
		st.execute(sql);
		sql="create index tblroles_Index_2 on tblroles(Username)";
		st.execute(sql);
		sql="create index tblroles_Index_3 on tblroles(Username,PID)";
		st.execute(sql);
		st.close();
	}
	
	public void createTableTblRedirects(Connection conn) throws SQLException {
		dropTable(conn,"tblredirects");
		Statement st=conn.createStatement();		
		String sql="CREATE TABLE tblredirects (" +
				"PID int  NOT NULL default 0," +
				"Url varchar(1024) NOT NULL" +
				") "+postfix;
		st.execute(sql);
		sql="create index tblredirects_Index_1 on tblredirects(PID)";
		st.execute(sql);
		st.close();
	}
	
	public void createTableTblPagine(Connection conn) throws SQLException {
		dropTable(conn,"tblpagine");
		Statement st=conn.createStatement();		
		String sql="CREATE TABLE tblpagine (" +
				"PID int NOT NULL," + // generated always as identity
				"Name varchar(100) NOT NULL default ''," +
				"PaPID int default 0," +
				"Valid int NOT NULL default 1," +
				"HasChild int NOT NULL default 0," +
				"PCode varchar(20) NOT NULL," +
				"InSidebar int  NOT NULL default 1," + 
				"PRIMARY KEY  (PID)) " + postfix;
//				"UNIQUE KEY Codice USING BTREE (PCode)," +
//				"KEY Index_2 USING BTREE (PaPID)) " + postfix;
//				"ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;";
		st.execute(sql);
		
		sql="create index tblpagine_Index_Codice on tblpagine(PCode)";
		st.execute(sql);
		sql="create index tblpagine_Index_1 on tblpagine(PaPID)";
		st.execute(sql);
		
		sql="create index tblpagine_Index_pid on tblpagine(PID)";
		st.execute(sql);
		
		st.close();
	}
	
	public void createTableTblContenuti(Connection conn) throws SQLException {
		dropTable(conn,"tblcontenuti");
		Statement st=conn.createStatement();
		
		String sql="CREATE TABLE tblcontenuti (" +
				"PaCID int NOT NULL," + //  default '0'
				"CID int NOT NULL," +  //  default '0'
				"StateID int NOT NULL default 3," + //  default '3'
				"OrderNumber int NOT NULL," + //  default '0'
				"PRIMARY KEY (PaCID,CID)) " + postfix; 
//				"UNIQUE KEY Index_4 USING BTREE (PaCID,CID)," +
//				"KEY Index_1 USING BTREE (PaCID)," +
//				"KEY Index_2 USING BTREE (CID)," +
//				"KEY FK_tblcontenuti_3 USING BTREE (StateID))" + postfix;
		st.execute(sql);
		
		sql="create index tblcontenuti_Index_1 on tblcontenuti(PaCID)";
		st.execute(sql);
		sql="create index tblcontenuti_Index_2 on tblcontenuti(CID)";
		st.execute(sql);
		sql="create index tblcontenuti_Index_3 on tblcontenuti(StateID)";
		st.execute(sql);
		
		st.close();
	}
	
	public void createTableNews(Connection conn) throws SQLException {
		dropTable(conn,"tblnews");
		Statement st=conn.createStatement();
		String sql="CREATE TABLE tblnews (CID integer, startdate varchar(8) NOT NULL," +
				"enddate varchar(8) NOT NULL," +
				"list int, " +
				"PRIMARY KEY (CID)) " + postfix;
//				"KEY Index_2 (startdate), " +
//				"KEY Index_3 (enddate), " +
//				"KEY Index_4 (list) ) "+postfix;
		
		st.execute(sql);
		sql="create index tblnews_Index_2 on tblnews(startdate)";
		st.execute(sql);
		sql="create index tblnews_Index_3 on tblnews(enddate)";
		st.execute(sql);
		sql="create index tblnews_Index_4 on tblnews(list)";
		st.execute(sql);
		
		
		st.close();
	}
	
	public void createTableTblComponenti(Connection conn) throws SQLException {
		dropTable(conn,"tblcomponenti");		
		Statement st=conn.createStatement();
		String sql="CREATE TABLE tblcomponenti (CID int NOT NULL, " + // default '0'," +
				"Type varchar(45) NOT NULL ," + // default ''
				"Attributes varchar(10)," +
				"HasChildren int NOT NULL ," + // default '0'
				"HistoryCid int NOT NULL," + // default '0'
				"InsertDate timestamp NOT NULL default CURRENT_TIMESTAMP ," + //default '0000-00-00 00:00:00'
				"PRIMARY KEY  (CID)) " + postfix;
//				"KEY Index_2 (Type)) "+postfix;
		st.execute(sql);
		
		sql="create index tblcomponenti_Index_2 on tblcomponenti(Type)";
		st.execute(sql);
		st.close();
	}
	
	
	public void createTableInfo(Connection conn) throws SQLException {
		dropTable(conn,"info");
		Statement st=conn.createStatement();
		String sql="CREATE TABLE info (PCode varchar(3) NOT NULL," +
				"PaPCode varchar(3) NOT NULL)"+postfix;
		st.execute(sql);
		st.close();
	}
	
	@Override
	public boolean existTable(Connection conn, String tablename)
			throws SQLException {
		boolean r=false;
		
		try {
			DBGateway.executeStatement(conn,"create table begintable (id int not null)");
			DBGateway.executeStatement(conn, "drop table begintable");
		}
		catch(SQLException e) {}
		
		String sql="select * from "+tablename;
		ResultSet rs = null;
		Statement st = null;
		try {
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			if(rs.next()) r=true;
		}
		catch(SQLException e) {
			// r=false;
		}
		finally {
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		
		
//		String sql="SHOW TABLES";
		
//		Statement st=conn.createStatement();
//		ResultSet rs=st.executeQuery(sql);
//		while(rs.next()) {
//			if(tablename.equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
//				r=true;
//				break;
//			}
//		}
//		rs.close();
//		st.close();
		return r;
	}

}
