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
import java.io.*;
import java.sql.*;

public class RinominaDB {

	protected static final String MySQLclassDriver = "com.mysql.jdbc.Driver";

	protected static final String MySQLdbURL = "jdbc:mysql://localhost/";

	protected static final String dbName = "dbsbn";

	protected static final String dbUser = "root";

	protected static final String dbPassword = "";

	
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