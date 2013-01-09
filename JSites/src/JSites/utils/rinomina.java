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

public class rinomina {

	protected static final String MySQLclassDriver = "com.mysql.jdbc.Driver";

	protected static final String MySQLdbURL = "jdbc:mysql://joyce.units.it/";

	protected static final String dbName = "dbsito";

	protected static final String dbUser = "root";

	protected static final String dbPassword = "";

	
	private Connection MySQLconn = null;
	
	int cidcount = 49;
	int pagecount = 20;
	String basedir = "e:\\java_source\\prova2\\components\\";
	
	Statement st = null;
    PreparedStatement ps = null;

	public rinomina() {
	    
        try {
            
            Class.forName(MySQLclassDriver);
            MySQLconn = DriverManager.getConnection(MySQLdbURL + dbName, dbUser,dbPassword);
            st = MySQLconn.createStatement();
            ps = MySQLconn.prepareStatement("update tblpagine set name=? where id=?");
        }
        
        catch(Exception e){e.printStackTrace();}
	}
	
	public void doJob() throws SQLException{
		String query = "select id,name from tblpagine";
        ResultSet rs = st.executeQuery(query);
        while(rs.next()){
        	long cid = rs.getLong(1);
        	String nome = rs.getString(2);
        	String newnome = elabora(nome);
        	System.out.println(nome + " --> "+newnome);
        	ps.setString(1,newnome);
        	ps.setLong(2,cid);
        	ps.execute();
        }
		
	}
	
	protected static String elabora(String nome) {
		
		String[] splitted = nome.split(" ");
		nome = "";
		
		for(int i=0;i<splitted.length;i++){
			
			/*if(splitted[i].matches("(?i)biblioteca")){
				splitted[i] = "Bib.";
			}*/
			if(splitted[i].matches("(?i)bib.")){
				splitted[i] = "";
			}
			else if(splitted[i].matches("(?i)dipartimento")){
				splitted[i] = "Dip.";
			}
			else if(splitted[i].matches("(?i)ingegneria")){
				splitted[i] = "Ing.";
			}
			else if(splitted[i].matches("(?i)della") || splitted[i].matches("(?i)di") || 
					splitted[i].matches("(?i)del") || splitted[i].matches("(?i)biblioteca")){
				splitted[i] = "";
			}
				
			nome = nome.trim() + " " + splitted[i].trim();
		}
			
		return nome.trim();
	}

	public static void main(String[] args){
		rinomina p = new rinomina();
		try {
			p.doJob();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}