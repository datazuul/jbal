package org.jopac2;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.binarystor.mysql.MySQLDump;

public class provaMySQLDump {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		String hostname="localhost";
		String schema="dbeutmarc";
		String username="root";
		String password="";
		String port="3306";
		
      
		Class.forName ("com.mysql.jdbc.Driver").newInstance ();
        Connection conn = DriverManager.getConnection ("jdbc:mysql://" + hostname + ":" + port + "/" + schema, username, password);
        
        MySQLDump mysqldump=new MySQLDump();
        mysqldump.init(conn);
      
        //Create temporary file to hold SQL output.
        File temp = File.createTempFile(schema, ".sql");
        BufferedWriter out = new BufferedWriter(new FileWriter(temp));
          
        out.write(mysqldump.getHeader());
          
        mysqldump.dumpAllTables(out);

        out.flush();
        out.close();
        mysqldump.cleanup(); // chiude la connessione
	}

}
