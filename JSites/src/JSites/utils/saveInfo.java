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
/*import java.io.FileOutputStream;
import java.io.PrintWriter;*/
import java.sql.*;

public class saveInfo {

	protected static final String MySQLclassDriver = "com.mysql.jdbc.Driver";

	protected static final String MySQLdbURL = "jdbc:mysql://localhost/";

	protected static final String dbName = "dbsito";

	protected static final String dbUser = "root";

	protected static final String dbPassword = "";

	
	private Connection MySQLconn = null;
	PreparedStatement ps = null;
	
	int cidcount = 49;
	int pagecount = 20;
	String basedir = "e:\\java_source\\prova2\\components\\";

	public saveInfo(){
		try {
			Class.forName(MySQLclassDriver);
			MySQLconn = DriverManager.getConnection(MySQLdbURL + dbName, dbUser,dbPassword);
	        
	        ps = MySQLconn.prepareStatement("update tblpagine set parentid=? where codice=?");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}
		
	public void doRestore(){
        try {
            
            
            Statement st = MySQLconn.createStatement();
            ResultSet rs = st.executeQuery("select * from info");
            while(rs.next()){
            	String codice = rs.getString(1);
            	String parentcodice = rs.getString(2);
            	
            	Statement pst = MySQLconn.createStatement();
        		ResultSet prs = pst.executeQuery("select id from tblpagine where codice='"+parentcodice+"'");
        		if(prs.next()){
        			String parentid = prs.getString(1);
        			ps.setString(1,parentid);
        			ps.setString(2,codice);
        			ps.execute();
        			
        			Statement st5 = MySQLconn.createStatement();
        			st5.executeUpdate("update tblpagine set haschild=1 where codice ='"+parentcodice+"'");
        			
        		}
        		prs.close();
        		pst.close();
            	
            }
        }
        catch(Exception e){
        	e.printStackTrace();
        }
	}
            
	
	
	public void doSave() {    
        try{
            Statement st = MySQLconn.createStatement();
            PreparedStatement ps = MySQLconn.prepareStatement("insert into info (codice,parentcodice) values (?,?)");
            
            //PrintWriter pw = new PrintWriter(new FileOutputStream("CodiciSBN.txt"));
        
            String query = "select codice,parentid from tblpagine";
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
            	String codice = rs.getString(1);
            	String parentid = rs.getString(2);
            	if(parentid!=null){
            		Statement pst = MySQLconn.createStatement();
            		ResultSet prs = pst.executeQuery("select codice from tblpagine where id="+parentid);
            		if(prs.next()){
            			String parentcodice = prs.getString(1);
            			ps.setString(1,codice);
            			ps.setString(2,parentcodice);
            			ps.execute();
            		}
            		prs.close();
            		pst.close();
            		
            		
            		
            	}
            }
            rs.close();
            st.close();
            ps.close();
        }
        
        catch(Exception e){e.printStackTrace();}
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
		//saveInfo p = new saveInfo();
		//p.doSave();
		//p.doRestore();
	}
}