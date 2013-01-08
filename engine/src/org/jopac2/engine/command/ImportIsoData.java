package org.jopac2.engine.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ImportIsoData {
	
	private static String JOpac2confdir = "src/org/jopac2/conf";
//	private static String _classMySQLDriver = "com.mysql.jdbc.Driver";

	public static void main(String[] args) throws Exception {
		String dbHost="localhost";
		String dbUser = "root";
		String dbPassword = "";
		String catalog="tesi";
		String database="dbsito";
		String filename="/home/romano/tesi.uni";
		String type="sebina";
		
		if(args.length==0) {
			System.out.println("Usage:\nImportIsoData [-h host] -u uername -p password -d database -t type -c catalog -f filename.uni\n\n");
		}
		else {
			for(int i=0;i<args.length;i++) {
				if(args[i].startsWith("-")) {
					if(args[i].equals("-h")) dbHost=args[++i];
					if(args[i].equals("-u")) dbUser=args[++i];
					if(args[i].equals("-p")) dbPassword=args[++i];
					if(args[i].equals("-c")) catalog=args[++i];
					if(args[i].equals("-d")) database=args[++i];
					if(args[i].equals("-f")) filename=args[++i];
					if(args[i].equals("-t")) type=args[++i];
				}
			}
			
			System.out.println("Using:\n");
			System.out.println("\tHost: "+dbHost);
			System.out.println("\tUser: "+dbUser);
			System.out.println("\tPass: "+dbPassword);
			System.out.println("\tDatabase: "+database);
			System.out.println("\tType: "+type);
			System.out.println("\tCatalog: "+catalog);
			System.out.println("\tFilename: "+filename);
			
	
			String dbUrl = "jdbc:mysql://"+dbHost+"/"+database;
			String filetype = type;
			
			
			File f=new File(filename);
	
			InputStream in = new FileInputStream(f);
	
			JOpac2Import ji = new JOpac2Import(in, catalog, filetype, JOpac2confdir,
					dbUrl, dbUser, dbPassword, true, System.out, System.out);
			ji.doJob(false);
			ji.destroy(dbUrl);
		}

	}
	
}
