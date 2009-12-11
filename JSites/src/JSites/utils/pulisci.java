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

public class pulisci {

	protected static final String MySQLclassDriver = "com.mysql.jdbc.Driver";

	protected static final String MySQLdbURL = "jdbc:mysql://localhost/";

	protected static final String dbName = "dbsito";

	protected static final String dbUser = "root";

	protected static final String dbPassword = "";

	
	private Connection MySQLconn = null;
	
	int cidcount = 39;
	int pagecount = 15;
	String basedir = "e:\\java_source\\prova2\\components\\";
	Statement st = null;

	public pulisci() {
	    
        try {
            
            Class.forName(MySQLclassDriver);
            MySQLconn = DriverManager.getConnection(MySQLdbURL + dbName, dbUser,dbPassword);
            st = MySQLconn.createStatement();
            
            
        }
        
        catch(Exception e){e.printStackTrace();}
	}
	
	public void doJob() throws SQLException{
		String query = "delete from tblcontenuti where ChildID>43"; //19   39
        st.execute(query);
        
        query = "delete from tblstrutture where idpagina>15";//15  20
        st.execute(query);
        
        query = "delete from tblcomponenti where id>43";//19  39
        st.execute(query);
        
        query = "update tblpagine set parentid=null where id>15";//15  20
        st.execute(query);
        
        query = "delete from tblpagine where id>15";//15  20
        st.execute(query);
	}
	
	public static void main(String[] args){
		pulisci p = new pulisci();
		try {
			p.doJob();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}