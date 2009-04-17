/*
 * Created on 30-dic-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jopac2.engine.importers;
/*******************************************************************************
*
*  JOpac2 (C) 2002-2007 JOpac2 project
*
*     This file is part of JOpac2. http://jopac2.sourceforge.net
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
*******************************************************************************/

/**
* @author	Albert Caramia
* @version ??/??/2002
* 
* @author	Romano Trampus
* @version ??/??/2002
* 
* @author	Romano Trampus
* @version	30/12/2004
*/

import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.dbGateway.LoadData;
import org.jopac2.jbal.Readers.XsltTransformer;

public class DataImporter extends Thread {
	private InputStream f;
	private String filetype,tempDir,confdir;
	Connection[] conn=null;
	boolean clearDatabase=true;
	DbGateway dbGateway=null;
	
	public DataImporter(InputStream f,String filetype,String JOpac2confdir,Connection[] conns, boolean clearDatabase) {
		this.f=f;
		this.filetype=filetype;
		confdir=JOpac2confdir;
		this.conn=conns;
		this.clearDatabase=clearDatabase;
		dbGateway=DbGateway.getInstance(conns[0].toString());
	}
	
	private void inizializeDB(Connection conn) throws SQLException {
		System.out.println("Creating tables");
		dbGateway.createAllTables(conn);
		System.out.println("Importing data types");
		/*try {
			DbGateway.createClasses(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		dbGateway.importClassiDettaglio(conn,confdir+"/dataDefinition/DataType.xml");
		
//		System.out.println("Droping db indexes");
//		manager.dropDBindexes();
		
		System.out.println("Create DB 1st index");
		dbGateway.create1stIndex(conn);
//        System.out.println("Create DB indexes");
//		manager.commitAll();
	}
	
	public void consolidateDB(Connection conn) throws SQLException {

        System.out.println("Creating indexes");
        dbGateway.createDBindexes(conn);
        System.out.println("Generating l_tables");
        dbGateway.createDBl_tables(conn);
	}
	
    private void loadData(InputStream f,String dbType, String temporaryDir) {
    	DbGateway.commitAll(conn);
        LoadData ld=new LoadData(conn);
        ld.doJob(f,dbType,temporaryDir,dbGateway.getClassID(conn[0], dbType));
        ld.destroy();
    }
	
    public synchronized void doJob() {
    	tempDir=System.getProperty("java.io.tmpdir");
        
        if(tempDir!=null) {
            System.out.println("Uso tempdir="+tempDir);
            //JOpac2DBmanager manager; //usa parametri di connessione da Cocoon
			try {
				int max_conn=5;
/*				if(datasource!=null) {
					Connection tc=datasource.getConnection();
					
					if(tc.toString().contains("hsqldb")) {
						max_conn=5;
					}
					tc.close();
					
					conn=new Connection[max_conn];
					for(int i=0;i<max_conn;i++) {
						conn[i]=datasource.getConnection();
					}
				}*/
				
				//manager = new JOpac2DBmanager(conn);
				if(clearDatabase) inizializeDB(conn[0]);
				//long now=System.currentTimeMillis();
				
				System.out.println("Loading data");
				
				
				/**
				 * TODO come gestire i filtri di trasformazione sulle notizie?!
				 * Se si mantengono i dati raw, dovrebbe essere gestito da jbal, credo
				 */
				
				String XMLfilter=null; //DbGateway.getFilter(conn[0],filetype);
				if(XMLfilter!=null) { // gestisce un filtro di trasformazione XSLT
					PipedOutputStream pipedOut=new PipedOutputStream();
					PipedInputStream pipedIn=new PipedInputStream(pipedOut);
					
					XsltTransformer t1=new XsltTransformer(f,pipedOut,confdir+"/dataDefinition/"+XMLfilter);
					t1.start();
					
					
					loadData(pipedIn,filetype,tempDir);
					
					
					t1.join();
				}
				else {
					loadData(f,filetype,tempDir);
				}
				DbGateway.commitAll(conn);
				
	            consolidateDB(conn[0]);
	            
	            DbGateway dbGateway=DbGateway.getInstance(conn[0].toString());
	    		dbGateway.rebuildList(conn[0]);
	            
	            System.out.println("End of process: OK");
	            //System.out.println("Total time: "+((System.currentTimeMillis()-now)/60000)+" minutes.");
				
	            // chiude sempre le connessioni
	            for(int i=0;i<max_conn;i++) {
					conn[i].close();
				}

	            this.notifyAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
	public void run() {
        doJob();
	}
}
