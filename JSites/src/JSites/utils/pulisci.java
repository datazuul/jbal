package JSites.utils;

import java.sql.*;

public class pulisci {

	protected static final String MySQLclassDriver = "com.mysql.jdbc.Driver";

	protected static final String MySQLdbURL = "jdbc:mysql://localhost/";

	protected static final String dbName = "dbsito";

	protected static final String dbUser = "root";

	protected static final String dbPassword = "%op01rt!";

	
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