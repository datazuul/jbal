package org.jopac2.engine.utils;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jopac2.engine.dbGateway.DbGateway;


public class RebuildDatabase {
	private static String _classDriver = "com.mysql.jdbc.Driver";
	private static String _dbMasterUrl = "jdbc:mysql://localhost/";
	private static String _dbUser = "root";
	private static String _dbPass = "";
	private static String _dbName = "demo";//"dbpregresso";
	private static boolean inizializzato = false;

	public static Connection CreaConnessione() {
		Connection conn=null;
		
		if (!inizializzato) {
			inizializzato = true;
			try {
				Class.forName(_classDriver).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			conn = DriverManager.getConnection(_dbMasterUrl + _dbName, _dbUser,
					_dbPass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	private static void TestConnessione() {
		try {
			Connection conn=CreaConnessione();
			Statement s = conn.createStatement();
			ResultSet r = s.executeQuery("select count(*) from notizie");
			r.next();
			long e = r.getLong(1);
			System.out.println("Totale record: "+e);
			r.close();
			s.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// rebuildDatabase(Connection[] conn)
		try {
			TestConnessione();
			Connection conn1,conn2,conn3,conn4,conn5,conn6,conn7;
			conn1=CreaConnessione();
			conn2=CreaConnessione();
			conn3=CreaConnessione();
			conn4=CreaConnessione();
			conn5=CreaConnessione();
			conn6=CreaConnessione();
			conn7=CreaConnessione();
			Connection[] c={conn1,conn2,conn3,conn4,conn5,conn6,conn7};
			DbGateway.rebuildDatabase(c);
			conn7.close();
			conn6.close();
			conn5.close();
			conn4.close();
			conn3.close();
			conn2.close();
			conn1.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
