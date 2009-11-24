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
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.dbGateway.LoadData;
import org.jopac2.jbal.Readers.XsltTransformer;

import com.whirlycott.cache.Cache;

public class DataImporter extends Thread {
	private InputStream f;
	private String catalog;
	private String filetype,tempDir,confdir;
	Connection[] conn=null;
	boolean clearDatabase=true;
	DbGateway dbGateway=null;
	Cache cache=null;
	//Transliterator t=null;
	String[] channels=null;
	PrintStream out=null;
	PrintStream outputErrorRecords=out;
	
	public DataImporter(InputStream f,String filetype,String JOpac2confdir,Connection[] conns, String catalog, boolean clearDatabase, Cache cache, PrintStream console, PrintStream outputErrorRecords) { //, Transliterator t) {
		this.f=f;
		this.filetype=filetype;
		confdir=JOpac2confdir;
		this.conn=conns;
		this.clearDatabase=clearDatabase;
		dbGateway=DbGateway.getInstance(conns[0].toString(),console);
		this.cache=cache;
		this.out=console;
		this.outputErrorRecords=outputErrorRecords;
		this.catalog=catalog;
	}
	

	
	public void consolidateDB(Connection conn) throws SQLException {

        out.println("Creating indexes");
        dbGateway.createDBindexes(conn,catalog);
        out.println("Generating l_tables");
        dbGateway.createDBl_tables(conn,catalog,out);
	}
	
    private String[] loadData(InputStream f,String dbType, String temporaryDir) throws SQLException {
    	String[] r=null;
    	DbGateway.commitAll(conn);
        LoadData ld=new LoadData(conn,catalog,clearDatabase,confdir,out,outputErrorRecords);
        r=ld.doJob(f,dbType,temporaryDir,cache); //,t);
        ld.destroy();
        return r;
    }
	
    public synchronized void doJob() {
    	tempDir=System.getProperty("java.io.tmpdir");
        
        if(tempDir!=null) {
            out.println("Uso tempdir="+tempDir);
			try {
				int max_conn=5;

				out.println("Loading data");

				/**
				 * TODO come gestire i filtri di trasformazione sulle notizie?!
				 * Se si mantengono i dati raw, dovrebbe essere gestito da jbal, credo
				 */
				String[] ch=null;
				
				String XMLfilter=null; //DbGateway.getFilter(conn[0],filetype);
				if(XMLfilter!=null) { // gestisce un filtro di trasformazione XSLT
					PipedOutputStream pipedOut=new PipedOutputStream();
					PipedInputStream pipedIn=new PipedInputStream(pipedOut);
					
					XsltTransformer t1=new XsltTransformer(f,pipedOut,confdir+"/dataDefinition/"+XMLfilter);
					t1.start();
					
					
					ch=loadData(pipedIn,filetype,tempDir);
					
					
					t1.join();
				}
				else {
					ch=loadData(f,filetype,tempDir);
				}
				DbGateway.commitAll(conn);
				
	            consolidateDB(conn[0]);
	            
	            DbGateway dbGateway=DbGateway.getInstance(conn[0].toString(),out);
	    		dbGateway.rebuildList(conn[0],catalog,ch);
	            
	            out.println("End of process: OK");

	            for(int i=0;i<max_conn;i++) {
					conn[i].close();
				}
	            
	            f.close();

	            this.notifyAll();
			} catch (Exception e) {
				e.printStackTrace(out);
			}
        }
    }
    
	public void run() {
		try {
			doJob();
		}
		catch(Exception e) {
			e.printStackTrace(out);
		}
	}
}
