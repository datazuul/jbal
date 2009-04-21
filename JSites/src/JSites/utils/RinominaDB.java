package JSites.utils;

import java.io.*;
import java.sql.*;

public class RinominaDB {

	protected static final String MySQLclassDriver = "com.mysql.jdbc.Driver";

	protected static final String MySQLdbURL = "jdbc:mysql://joyce.units.it/";

	protected static final String dbName = "dbsbn";

	protected static final String dbUser = "root";

	protected static final String dbPassword = "%op01rt!";

	
	private Connection MySQLconn = null;
	private Statement MySQLstatement = null;
	
	int cidcount = 49;
	int pagecount = 20;
	String basedir = "e:\\java_source\\prova2\\components\\";
	File SQLCommands = new File("e:\\DB2.txt");
	

	public RinominaDB() {
	    
        try {
            
            Class.forName(MySQLclassDriver);
            MySQLconn = DriverManager.getConnection(MySQLdbURL + dbName, dbUser,dbPassword);
            MySQLstatement = MySQLconn.createStatement();
        }
        
        catch(Exception e){e.printStackTrace();}
	}
	
	public void doJob() throws IOException, SQLException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(SQLCommands)));
		String read = br.readLine();
		String command ="";
		while(read!=null){
			command = command + read;
			if(command.endsWith(";")){
				command = command.replaceAll("dbsito", dbName);
				System.out.println("executing: " + command);
				execute(command);
				command = "";
			}
			
			read = br.readLine();
			
		}
		MySQLstatement.close();
		MySQLconn.close();
		
	}
	

	private void execute(String command) {
		try {
			MySQLstatement.executeUpdate(command);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args){
		RinominaDB p = new RinominaDB();
		
			try {
				p.doJob();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
}