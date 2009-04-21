package JSites.utils;

import java.sql.*;

public class rinomina {

	protected static final String MySQLclassDriver = "com.mysql.jdbc.Driver";

	protected static final String MySQLdbURL = "jdbc:mysql://joyce.units.it/";

	protected static final String dbName = "dbsito";

	protected static final String dbUser = "root";

	protected static final String dbPassword = "%op01rt!";

	
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