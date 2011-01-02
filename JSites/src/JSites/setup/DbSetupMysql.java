package JSites.setup;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2009 JOpac2 project
*
*     This file is part of JOpac2. http://www.jopac2.org
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
*  Please, see NOTICE.txt AND LEGAL directory for more info. Different licences
*  may apply for components included in JOpac2.
*
*******************************************************************************/

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbSetupMysql extends DbSetup {
	public void createTableInfo(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS info;";
		Statement st=conn.createStatement();
//		st.execute(sql);
		
		sql="CREATE TABLE info (PCode varchar(3) NOT NULL," +
				"PaPCode varchar(3) NOT NULL) " +
				"ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		
		try {
			st.execute(sql);
		}
		catch(SQLException e) {
			st.close();
			throw e;
		}
		st.close();
	}
	
	public void createTableTblComponenti(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblcomponenti;";
		Statement st=conn.createStatement();
//		st.execute(sql);
		
		sql="CREATE TABLE tblcomponenti (CID int(10) unsigned NOT NULL default '0'," +
				"Type varchar(45) NOT NULL default ''," +
				"Attributes longtext," +
				"HasChildren tinyint(1) unsigned NOT NULL default '0'," +
				"HistoryCid int(10) unsigned NOT NULL default '0'," +
				"InsertDate datetime NOT NULL default '0000-00-00 00:00:00'," +
				"username varchar(50) NOT NULL default 'unknown'," +
				"remoteip varchar(20) NOT NULL default 'unknown'," +
				"PRIMARY KEY  (CID)," +
				"KEY Index_3 (username)," +
				"KEY Index_4 (remoteip)," +
				"KEY Index_2 (Type)) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		try {
			st.execute(sql);
		}
		catch(SQLException e) {
			st.close();
			throw e;
		}
		st.close();
	}
	
	public void createTableNews(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblnews;";
		Statement st=conn.createStatement();
//		st.execute(sql);
		
		sql="CREATE TABLE tblnews (CID integer, startdate varchar(8) NOT NULL," +
				"enddate varchar(8) NOT NULL," +
				"list tinyint(1), " +
				"PRIMARY KEY (CID)," +
				"KEY Index_2 (startdate), " +
				"KEY Index_3 (enddate), " +
				"KEY Index_4 (list) ) ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		
		try {
			st.execute(sql);
		}
		catch(SQLException e) {
			st.close();
			throw e;
		}
		st.close();
	}
	
	public void createTableTblContenuti(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblcontenuti;";
		Statement st=conn.createStatement();
//		st.execute(sql);
		
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
		try {
			st.execute(sql);
		}
		catch(SQLException e) {
			st.close();
			throw e;
		}
		st.close();
	}
	
	public void createTableTblPagine(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblpagine;";
		Statement st=conn.createStatement();
//		st.execute(sql);
		
		sql="CREATE TABLE tblpagine (" +
				"PID int(10) unsigned NOT NULL auto_increment," +
				"Name varchar(100) NOT NULL default ''," +
				"PaPID int(10) unsigned default '0'," +
				"Valid tinyint(1) unsigned NOT NULL default '1'," +
				"HasChild tinyint(1) unsigned NOT NULL default '0'," +
				"PCode varchar(20) NOT NULL," +
				"InSidebar tinyint(1) unsigned NOT NULL default '1'," +
				"resp varchar(50), " +
				"insertdate datetime NOT NULL default '0000-00-00 00:00:00'," +
				"username varchar(50) NOT NULL default 'unknown'," +
				"remoteip varchar(20) NOT NULL default 'unknown'," +
				"KEY index_username (username)," +
				"KEY index_remoteip (remoteip)," +
				"KEY index_insertdate (insertdate)," +
				"KEY index_resp (resp)," +
				"PRIMARY KEY  (PID)," +
				"UNIQUE KEY Codice USING BTREE (PCode)," +
				"KEY Index_2 USING BTREE (PaPID)" +
				") ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;";
		try {
			st.execute(sql);
		}
		catch(SQLException e) {
			st.close();
			throw e;
		}
		st.close();
	}
	
	public void createTableTblRedirects(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblredirects;";
		Statement st=conn.createStatement();
//		st.execute(sql);
		
		sql="CREATE TABLE tblredirects (" +
				"PID int(10) unsigned NOT NULL default '0'," +
				"Url text NOT NULL," +
				"KEY Index_1 USING BTREE (PID)" +
				") ENGINE=MyISAM DEFAULT CHARSET=latin1;";
		try {
			st.execute(sql);
		}
		catch(SQLException e) {
			st.close();
			throw e;
		}
		st.close();
	}
	
	public void createTableTblRoles(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblroles;";
		Statement st=conn.createStatement();
//		st.execute(sql);
		
		sql="CREATE TABLE tblroles (" +
				"Username varchar(50) NOT NULL," +
				"PermissionCode int(10) unsigned NOT NULL default '0'," +
				"PID int(10) unsigned NOT NULL default '0'," +
				"PRIMARY KEY  (Username,PID)," +
				"KEY Index_2 (Username)," +
				"KEY Index_3 USING BTREE (Username)" +
				") ENGINE=MyISAM DEFAULT CHARSET=latin1;";
		try {
			st.execute(sql);
		}
		catch(SQLException e) {
			st.close();
			throw e;
		}
		st.close();
	}
	
	public void createTableTblStati(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblstati;";
		Statement st=conn.createStatement();
//		st.execute(sql);
		
		sql="CREATE TABLE tblstati (" +
				"StateID int(10) unsigned NOT NULL auto_increment," +
				"StateName varchar(5) NOT NULL," +
				"Scope varchar(45) NOT NULL," +
				"PRIMARY KEY  (StateID)," +
				"KEY Index_3 USING BTREE (Scope)" +
				") ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;";
		try {
			st.execute(sql);
		}
		catch(SQLException e) {
			st.close();
			throw e;
		}
		st.close();
	}
	
	public void createTableTblStrutture(Connection conn) throws SQLException {
		String sql="DROP TABLE IF EXISTS tblstrutture;";
		Statement st=conn.createStatement();
//		st.execute(sql);
		
		sql="CREATE TABLE tblstrutture (" +
				"PID int(10) unsigned NOT NULL default '0'," +
				"CID int(10) unsigned NOT NULL default '0'," +
				"UNIQUE KEY Index_3 USING BTREE (PID,CID)," +
				"KEY Index_1 USING BTREE (PID)," +
				"KEY Index_2 USING BTREE (CID)" +
				") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
		try {
			st.execute(sql);
		}
		catch(SQLException e) {
			st.close();
			throw e;
		}
		st.close();
	}

	@Override
	public boolean existTable(Connection conn, String tablename)
			throws SQLException {
		boolean r=false;
		Statement st=conn.createStatement();
		String sql="show tables like \""+tablename+"\"";
		ResultSet rs=st.executeQuery(sql);
		if(rs.next()) {
			r=true;
		}
		rs.close();
		st.close();
		return r;
	}

}
