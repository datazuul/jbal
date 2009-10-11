package JSites.setup;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbSetupDerby extends DbSetup {
	private static String postfix=""; //"DEFAULT CHARSET=utf8;";
	
	private void dropTable(Connection conn, String tableName) {
		String sql="DROP TABLE "+tableName;
		try {
			Statement st=conn.createStatement();
			st.execute(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
			// eccezione se la tabella non esiste
		}
	}
	
	public void createTableTblStrutture(Connection conn) throws SQLException {
		dropTable(conn,"tblstrutture");
		Statement st=conn.createStatement();		
		String sql="CREATE TABLE tblstrutture (" +
				"PID int(10) unsigned NOT NULL default '0'," +
				"CID int(10) unsigned NOT NULL default '0'," +
				"UNIQUE KEY Index_3 USING BTREE (PID,CID)," +
				"KEY Index_1 USING BTREE (PID)," +
				"KEY Index_2 USING BTREE (CID)) " + postfix;
		st.execute(sql);
		st.close();
	}
	
	public void createTableTblStati(Connection conn) throws SQLException {
		dropTable(conn,"tblstati");
		Statement st=conn.createStatement();		
		String sql="CREATE TABLE tblstati (" +
				"StateID int(10) unsigned NOT NULL auto_increment," +
				"StateName varchar(5) NOT NULL," +
				"Scope varchar(45) NOT NULL," +
				"PRIMARY KEY  (StateID)," +
				"KEY Index_3 USING BTREE (Scope)) " + postfix;
//				"ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;";
		st.execute(sql);
		st.close();
	}
	
	public void createTableTblRoles(Connection conn) throws SQLException {
		dropTable(conn,"tblroles");
		Statement st=conn.createStatement();		
		String sql="CREATE TABLE tblroles (" +
				"User varchar(50) NOT NULL," +
				"PermissionCode int(10) unsigned NOT NULL default '0'," +
				"PID int(10) unsigned NOT NULL default '0'," +
				"PRIMARY KEY  (User,PID)," +
				"KEY Index_2 (User)," +
				"KEY Index_3 USING BTREE (User))" + postfix;
//				"ENGINE=MyISAM DEFAULT CHARSET=latin1;";
		st.execute(sql);
		st.close();
	}
	
	public void createTableTblRedirects(Connection conn) throws SQLException {
		dropTable(conn,"tblredirects");
		Statement st=conn.createStatement();		
		String sql="CREATE TABLE tblredirects (" +
				"PID int(10) unsigned NOT NULL default '0'," +
				"Url text NOT NULL," +
				"KEY Index_1 USING BTREE (PID)" +
				") "+postfix;
		st.execute(sql);
		st.close();
	}
	
	public void createTableTblPagine(Connection conn) throws SQLException {
		dropTable(conn,"tblpagine");
		Statement st=conn.createStatement();		
		String sql="CREATE TABLE tblpagine (" +
				"PID int(10) unsigned NOT NULL auto_increment," +
				"Name varchar(100) NOT NULL default ''," +
				"PaPID int(10) unsigned default '0'," +
				"Valid tinyint(1) unsigned NOT NULL default '1'," +
				"HasChild tinyint(1) unsigned NOT NULL default '0'," +
				"PCode varchar(20) NOT NULL," +
				"InSidebar tinyint(1) unsigned NOT NULL default '1'," +
				"PRIMARY KEY  (PID)," +
				"UNIQUE KEY Codice USING BTREE (PCode)," +
				"KEY Index_2 USING BTREE (PaPID)) " + postfix;
//				"ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;";
		st.execute(sql);
		st.close();
	}
	
	public void createTableTblContenuti(Connection conn) throws SQLException {
		dropTable(conn,"tblcontenuti");
		Statement st=conn.createStatement();
		
		String sql="CREATE TABLE tblcontenuti (" +
				"PaCID int(10) unsigned NOT NULL default '0'," +
				"CID int(10) unsigned NOT NULL default '0'," +
				"StateID int(10) unsigned NOT NULL default '3'," +
				"OrderNumber int(10) unsigned NOT NULL default '0'," +
				"UNIQUE KEY Index_4 USING BTREE (PaCID,CID)," +
				"KEY Index_1 USING BTREE (PaCID)," +
				"KEY Index_2 USING BTREE (CID)," +
				"KEY FK_tblcontenuti_3 USING BTREE (StateID))" + postfix;
		st.execute(sql);
		st.close();
	}
	
	public void createTableNews(Connection conn) throws SQLException {
		dropTable(conn,"tblnews");
		Statement st=conn.createStatement();
		String sql="CREATE TABLE tblnews (CID integer, startdate varchar(8) NOT NULL," +
				"enddate varchar(8) NOT NULL," +
				"list tinyint(1), " +
				"PRIMARY KEY (CID)," +
				"KEY Index_2 (startdate), " +
				"KEY Index_3 (enddate), " +
				"KEY Index_4 (list) ) "+postfix;
		
		st.execute(sql);
		st.close();
	}
	
	public void createTableTblComponenti(Connection conn) throws SQLException {
		dropTable(conn,"tblcomponenti");		
		Statement st=conn.createStatement();
		String sql="CREATE TABLE tblcomponenti (CID int(10) unsigned NOT NULL default '0'," +
				"Type varchar(45) NOT NULL default ''," +
				"Attributes longtext," +
				"HasChildren tinyint(1) unsigned NOT NULL default '0'," +
				"HistoryCid int(10) unsigned NOT NULL default '0'," +
				"InsertDate datetime NOT NULL default '0000-00-00 00:00:00'," +
				"PRIMARY KEY  (CID)," +
				"KEY Index_2 (Type)) "+postfix;
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

}
