package JSites.backup;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestBackup {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		String context="/java_jopac2/JSites/WebContent";
		String site="eut";
		
		String hostname="localhost";
		String schema="dbeut";
		String username="root";
		String password="";
		String port="3306";
		
      
		Class.forName ("com.mysql.jdbc.Driver").newInstance ();
		Connection conn = DriverManager.getConnection ("jdbc:mysql://" + hostname + ":" + port + "/" + schema, username, password);
        File backup=Backup.backup(conn, site, context);
		conn.close();
		System.out.println("Finito, backup: "+backup.getAbsolutePath());
	}

}
