package JSites.setup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DbSetup {
	public static DbSetup getInstance(String name) {
		DbSetup dbs=null;
		if(name.contains("mysql")) return new DbSetupMysql();
		if(name.contains("derby")) return new DbSetupDerby();
		return dbs;
	}
	
	public abstract boolean existTable(Connection conn, String tablename) throws SQLException;
	
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
	
	public void createDatabase(Connection conn, String databaseName) throws SQLException {
		String sql="CREATE DATABASE IF NOT EXISTS "+databaseName+";";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}
	
	public abstract void createTableNews(Connection conn) throws SQLException;
	public abstract void createTableInfo(Connection conn) throws SQLException;


	public void loadTableInfo(Connection conn) {
	}
	
	public abstract void createTableTblComponenti(Connection conn) throws SQLException;

	public void loadTableTblComponenti(Connection conn) throws SQLException {
//		String sql="INSERT INTO tblcomponenti " +
//				"(CID,Type,Attributes,HasChildren,HistoryCid,InsertDate) " +
//				"VALUES " +
//				"(1,'header',NULL,1,0,'0000-00-00 00:00:00')," +
//				"(2,'navbar',NULL,1,0,'0000-00-00 00:00:00')," +
//				"(3,'content',NULL,1,0,'0000-00-00 00:00:00')," +
//				"(4,'footer',NULL,1,0,'0000-00-00 00:00:00')," +
//				"(5,'tabs',NULL,0,0,'0000-00-00 00:00:00')," +
//				"(6,'lingue',NULL,0,0,'0000-00-00 00:00:00')," +
//				"(7,'strumenti',NULL,0,0,'0000-00-00 00:00:00')," +
//				"(8,'ricerca',NULL,0,0,'0000-00-00 00:00:00')," +
//				"(9,'section',NULL,0,0,'2006-07-20 00:00:00')," +
//				"(10,'footerContent',NULL,0,0,'0000-00-00 00:00:00')," +
//				"(11,'navbar',NULL,1,0,'0000-00-00 00:00:00')," +
//				"(12,'sidebar',NULL,0,0,'0000-00-00 00:00:00')";
		String sql="INSERT INTO tblcomponenti " +
		"(CID,Type,Attributes,HasChildren,HistoryCid) " +
		"VALUES " +
		"(1,'header',NULL,1,0)," +
		"(2,'navbar',NULL,1,0)," +
		"(3,'content',NULL,1,0)," +
		"(4,'footer',NULL,1,0)," +
		"(5,'tabs',NULL,0,0)," +
		"(6,'lingue',NULL,0,0)," +
		"(7,'strumenti',NULL,0,0)," +
		"(8,'ricerca',NULL,0,0)," +
		"(9,'section',NULL,0,0)," +
		"(10,'footerContent',NULL,0,0)," +
		"(11,'navbar',NULL,1,0)," +
		"(12,'sidebar',NULL,0,0)," +
		"(13,'briciole',NULL,0,0)";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}
	
	public abstract void createTableTblContenuti(Connection conn) throws SQLException;

	public void loadTableTblContenuti(Connection conn) throws SQLException {
		String sql="INSERT INTO tblcontenuti " +
				"(PaCID,CID,StateID,OrderNumber) VALUES " +
				"(1,5,3,1)," +
				"(1,6,5,3)," +
				"(1,7,3,2)," +
				"(1,8,3,4)," +
				"(2,12,3,1)," +
				"(3,9,4,6)," +
				"(4,10,3,1)," +
				"(11,12,3,1)";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}

	public abstract void createTableTblPagine(Connection conn) throws SQLException;
	
	public void loadTableTblPagine(Connection conn) throws SQLException {
		String sql="INSERT INTO tblpagine (" +
				"PID,Name,PaPID,Valid,HasChild,PCode,InSidebar) " +
				"VALUES " +
				"(1,'Homepage',NULL,1,1,'HMP',1)";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}

	public abstract void createTableTblRedirects(Connection conn) throws SQLException;
	
	public void loadTableTblRedirects(Connection conn) {
	}

	public abstract void createTableTblRoles(Connection conn) throws SQLException;

	public void loadTableTblRoles(Connection conn) throws SQLException {
		String sql="INSERT INTO tblroles (Username,PermissionCode,PID) VALUES " +
				"('admin',15,0)";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}

	public abstract void createTableTblStati(Connection conn) throws SQLException;

	public void loadTableTblStati(Connection conn) throws SQLException {
		String sql="INSERT INTO tblstati (StateName,Scope) VALUES " +
				"('WRK','modifica')," +
				"('PND','modifica')," +
				"('ACT','validazione')," +
				"('OLD','archivio')," +
				"('DEL','cancellazione')";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}

	public abstract void createTableTblStrutture(Connection conn) throws SQLException;

	public void loadTableTblStrutture(Connection conn) throws SQLException {
		String sql="INSERT INTO tblstrutture (PID,CID) VALUES " +
				"(1,1)," +
				"(1,2)," +
				"(1,3)," +
				"(1,4)";
		Statement st=conn.createStatement();
		st.execute(sql);
		st.close();
	}
}
