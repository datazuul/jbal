package org.jopac2.test;

import java.sql.Connection;

import org.jopac2.engine.command.JOpac2Import;
import org.junit.After;
import org.junit.Before;

import junit.framework.TestCase;

public class ImportTestDerby extends TestCase {
	
	public Connection conn;
	@Before
	public void setUp() throws Exception {
		super.setUp();
		conn=CreadbDerby.CreaConnessione(DBUtils.DBNAME);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		conn.close();
	}

	//

	public void importNotizie() throws Exception {
		String sitename="eutmarc";
		String filename="/java_jopac2/engine/data/eutRecords.iso";
		String filetype="eutmarc";
		String JOpac2confdir="/java_jopac2/engine/src/org/jopac2/conf";
		//String dbUrl = "jdbc:derby:db"+sitename+";create=true";
		String dbUrl="jdbc:mysql://localhost/db"+sitename;
		String dbUser="root";
		String dbPassword="";
		
		JOpac2Import ji=new JOpac2Import(filename,filetype,JOpac2confdir,dbUrl,dbUser,dbPassword,true);
		ji.doJob();
		ji.wait();
		ji.destroy(dbUrl);
		assertTrue("Done " ,true);
	}	

	
}
