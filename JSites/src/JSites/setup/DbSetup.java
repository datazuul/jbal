package JSites.setup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbSetup {
	
	public static Connection getConnection(String _dbName, String _classDriver, 
			String _dbMasterUrl, String _dbUser, String _dbPass) {
		Connection conn=null;
	
		try {
			Class.forName(_classDriver).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(_dbMasterUrl + _dbName, _dbUser,	_dbPass);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void createDatabase(Connection conn, String databaseName) throws SQLException {
		String sql="CREATE DATABASE IF NOT EXISTS "+databaseName+";";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}
	
	public static void createTableNews(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblnews;";
		Statement st=conn.createStatement();
		st.execute(sql);
		
		sql="CREATE TABLE tblnews (CID integer, startdate varchar(8) NOT NULL," +
				"enddate varchar(8) NOT NULL," +
				"list tinyint(1), " +
				"PRIMARY KEY (CID)," +
				"KEY Index_2 (startdate), " +
				"KEY Index_3 (enddate), " +
				"KEY Index_4 (list) ) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		
		st.execute(sql);
		st.close();
	}
	
	public static void createTableInfo(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS info;";
		Statement st=conn.createStatement();
		st.execute(sql);
		
		sql="CREATE TABLE info (PCode varchar(3) NOT NULL," +
				"PaPCode varchar(3) NOT NULL) " +
				"ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		
		st.execute(sql);
		st.close();
	}

	public static void loadTableInfo(Connection conn) {
	}
	

	public static void createTableTblComponenti(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblcomponenti;";
		Statement st=conn.createStatement();
		st.execute(sql);
		
		sql="CREATE TABLE tblcomponenti (CID int(10) unsigned NOT NULL default '0'," +
				"Type varchar(45) NOT NULL default ''," +
				"Attributes longtext," +
				"HasChildren tinyint(1) unsigned NOT NULL default '0'," +
				"HistoryCid int(10) unsigned NOT NULL default '0'," +
				"InsertDate datetime NOT NULL default '0000-00-00 00:00:00'," +
				"PRIMARY KEY  (CID)," +
				"KEY Index_2 (Type)) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		st.execute(sql);
		st.close();
	}


	public static void loadTableTblComponenti(Connection conn) throws SQLException {
		String sql="INSERT INTO tblcomponenti " +
				"(CID,Type,Attributes,HasChildren,HistoryCid,InsertDate) " +
				"VALUES " +
				"(1,'header',NULL,1,0,'0000-00-00 00:00:00')," +
				"(2,'navbar',NULL,1,0,'0000-00-00 00:00:00')," +
				"(3,'content',NULL,1,0,'0000-00-00 00:00:00')," +
				"(4,'footer',NULL,1,0,'0000-00-00 00:00:00')," +
				"(5,'tabs',NULL,0,0,'0000-00-00 00:00:00')," +
				"(6,'lingue',NULL,0,0,'0000-00-00 00:00:00')," +
				"(7,'strumenti',NULL,0,0,'0000-00-00 00:00:00')," +
				"(8,'ricerca',NULL,0,0,'0000-00-00 00:00:00')," +
				"(9,'section',NULL,0,0,'2006-07-20 00:00:00')," +
				"(10,'footerContent',NULL,0,0,'0000-00-00 00:00:00')," +
				"(11,'navbar',NULL,1,0,'0000-00-00 00:00:00')," +
				"(12,'sidebar',NULL,0,0,'0000-00-00 00:00:00');";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}
	
	public static void createTableTblContenuti(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblcontenuti;";
		Statement st=conn.createStatement();
		st.execute(sql);
		
		sql="CREATE TABLE tblcontenuti (" +
				"PaCID int(10) unsigned NOT NULL default '0'," +
				"CID int(10) unsigned NOT NULL default '0'," +
				"StateID int(10) unsigned NOT NULL default '3'," +
				"OrderNumber int(10) unsigned NOT NULL default '0'," +
				"UNIQUE KEY Index_4 USING BTREE (PaCID,CID)," +
				"KEY Index_1 USING BTREE (PaCID)," +
				"KEY Index_2 USING BTREE (CID)," +
				"KEY FK_tblcontenuti_3 USING BTREE (StateID)" +
				") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		st.execute(sql);
		st.close();
	}

	public static void loadTableTblContenuti(Connection conn) throws SQLException {
		String sql="INSERT INTO tblcontenuti " +
				"(PaCID,CID,StateID,OrderNumber) VALUES " +
				"(1,5,3,1)," +
				"(1,6,5,3)," +
				"(1,7,3,2)," +
				"(1,8,3,4)," +
				"(2,12,3,1)," +
				"(3,9,4,6)," +
				"(11,12,3,1);";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}

	public static void createTableTblPagine(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblpagine;";
		Statement st=conn.createStatement();
		st.execute(sql);
		
		sql="CREATE TABLE tblpagine (" +
				"PID int(10) unsigned NOT NULL auto_increment," +
				"Name varchar(100) NOT NULL default ''," +
				"PaPID int(10) unsigned default '0'," +
				"Valid tinyint(1) unsigned NOT NULL default '1'," +
				"HasChild tinyint(1) unsigned NOT NULL default '0'," +
				"PCode varchar(20) NOT NULL," +
				"InSidebar tinyint(1) unsigned NOT NULL default '1'," +
				"PRIMARY KEY  (PID)," +
				"UNIQUE KEY Codice USING BTREE (PCode)," +
				"KEY Index_2 USING BTREE (PaPID)" +
				") ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;";
		st.execute(sql);
		st.close();
	}
	
	public static void loadTableTblPagine(Connection conn) throws SQLException {
		String sql="INSERT INTO tblpagine (" +
				"PID,Name,PaPID,Valid,HasChild,PCode,InSidebar) " +
				"VALUES " +
				"(1,'Homepage',NULL,1,1,'HMP',1);";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}

	public static void createTableTblRedirects(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblredirects;";
		Statement st=conn.createStatement();
		st.execute(sql);
		
		sql="CREATE TABLE tblredirects (" +
				"PID int(10) unsigned NOT NULL default '0'," +
				"Url text NOT NULL," +
				"KEY Index_1 USING BTREE (PID)" +
				") ENGINE=MyISAM DEFAULT CHARSET=latin1;";
		st.execute(sql);
		st.close();
	}
	
	public static void loadTableTblRedirects(Connection conn) {
	}

	public static void createTableTblRoles(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblroles;";
		Statement st=conn.createStatement();
		st.execute(sql);
		
		sql="CREATE TABLE tblroles (" +
				"User varchar(50) NOT NULL," +
				"PermissionCode int(10) unsigned NOT NULL default '0'," +
				"PID int(10) unsigned NOT NULL default '0'," +
				"PRIMARY KEY  (User,PID)," +
				"KEY Index_2 (User)," +
				"KEY Index_3 USING BTREE (User)" +
				") ENGINE=MyISAM DEFAULT CHARSET=latin1;";
		st.execute(sql);
		st.close();
	}

	public static void loadTableTblRoles(Connection conn) throws SQLException {
		String sql="INSERT INTO tblroles (User,PermissionCode,PID) VALUES " +
				"('admin',7,0)";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}

	
	public static void createTableTblStati(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblstati;";
		Statement st=conn.createStatement();
		st.execute(sql);
		
		sql="CREATE TABLE tblstati (" +
				"StateID int(10) unsigned NOT NULL auto_increment," +
				"StateName varchar(5) NOT NULL," +
				"Scope varchar(45) NOT NULL," +
				"PRIMARY KEY  (StateID)," +
				"KEY Index_3 USING BTREE (Scope)" +
				") ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;";
		st.execute(sql);
		st.close();
	}

	public static void loadTableTblStati(Connection conn) throws SQLException {
		String sql="INSERT INTO tblstati (StateID,StateName,Scope) VALUES " +
				"(1,'WRK','modifica')," +
				"(2,'PND','modifica')," +
				"(3,'ACT','validazione')," +
				"(4,'OLD','archivio')," +
				"(5,'DEL','cancellazione');";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}

	public static void createTableTblStrutture(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblstrutture;";
		Statement st=conn.createStatement();
		st.execute(sql);
		
		sql="CREATE TABLE tblstrutture (" +
				"PID int(10) unsigned NOT NULL default '0'," +
				"CID int(10) unsigned NOT NULL default '0'," +
				"UNIQUE KEY Index_3 USING BTREE (PID,CID)," +
				"KEY Index_1 USING BTREE (PID)," +
				"KEY Index_2 USING BTREE (CID)" +
				") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		st.execute(sql);
		st.close();
	}

	public static void loadTableTblStrutture(Connection conn) throws SQLException {
		String sql="INSERT INTO tblstrutture (PID,CID) VALUES " +
				"(1,1)," +
				"(1,2)," +
				"(1,3)," +
				"(1,4);";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}
}
