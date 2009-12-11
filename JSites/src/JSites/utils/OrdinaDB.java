package JSites.utils;

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

import java.sql.*;

public class OrdinaDB {

	protected static final String MySQLclassDriver = "com.mysql.jdbc.Driver";

	protected static final String MySQLdbURL = "jdbc:mysql://localhost/";
	
	protected static final String dbName = "dbsito";

	protected static final String dbUser = "root";

	protected static final String dbPassword = "";

	private Connection conn = null;
	
	int cidcount = 39;
	int pagecount = 15;
	String basedir = "e:\\java_source\\prova2\\components\\";

	public OrdinaDB() {
	    
        try {

            Class.forName(MySQLclassDriver);
            conn = DriverManager.getConnection(MySQLdbURL + dbName, dbUser,dbPassword);
            
            Statement st1 = conn.createStatement();
            
            
            
            String q1 = "select id from tblcomponenti where type='content'";

            ResultSet rs1 = execute(st1,q1);
            while(rs1.next()){
            	long contentId = rs1.getLong(1);
            	
            	OrdinaDB.normalizeDB(contentId, conn);
            	
            	
            	
            	
            }
            
        }
        catch(Exception e){e.printStackTrace();}
	}

	
		
		public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}
		@SuppressWarnings("unused")
		OrdinaDB crea = new OrdinaDB();

	}

	static void normalizeDB(long contentId, Connection conn) throws SQLException {
		
		Statement st2 = conn.createStatement();
		PreparedStatement ps = conn.prepareStatement("update tblcontenuti set OrderNumber=? where CID=?");
		
		String q2 = "select CID from tblcontenuti where PaCID="+contentId+" and StateID=3 order by OrderNumber";
        ResultSet rs2 = st2.executeQuery(q2);
        long order=1;
        while(rs2.next()){
        	long childID=rs2.getLong(1);
        	ps.setLong(1,order);
        	ps.setLong(2,childID);
        	ps.execute();
        	order++;
        }
	}
	
	private ResultSet execute(Statement s, String query) throws SQLException{
		return s.executeQuery(query);
	}
	
	
	/*private void execute(Statement s, String query) throws SQLException{
		//s.execute(query);
	}*/

}