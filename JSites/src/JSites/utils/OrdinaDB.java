package JSites.utils;

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