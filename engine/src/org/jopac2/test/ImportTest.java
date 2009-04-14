package org.jopac2.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jopac2.engine.command.JOpac2Import;
import org.junit.After;
import org.junit.Before;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import junit.framework.TestCase;

public class ImportTest extends TestCase {
	
	//public Connection conn;
	@Before
	public void setUp() throws Exception {
		super.setUp();
		//conn=CreadbDerby.CreaConnessione(DBUtils.DBNAME);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		//conn.close();
	}

	public void testImportNotizie() throws Exception {
		String sitename="test";
		InputStream in=null;
		String filetype="sebina";
		String JOpac2confdir="/java_jopac2/engine/src/org/jopac2/conf";
		//String dbUrl = "jdbc:derby:db"+sitename+";create=true";
		String dbUrl="jdbc:mysql://localhost/db"+sitename;
		String dbUser="root";
		String dbPassword="";
		
		String inFile=new String(Base64.decode(org.jopac2.test.Base64DataExample.unimarcBase64DataExample),"utf-8");
		
		in = new ByteArrayInputStream(inFile.getBytes()); 
		
		JOpac2Import ji=new JOpac2Import(in,filetype,JOpac2confdir,dbUrl,dbUser,dbPassword,true);
		ji.doJob(false);
		//ji.wait();
		ji.destroy(dbUrl);
		assertTrue("Done " ,true);
	}	

	
}
