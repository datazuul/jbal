package org.jopac2.test;


import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

public class AccentTest extends TestCase {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	
	public void testIsisbiblo1() throws Exception {
		String n1="àèéìòù";
		String r=null; //Utils.removeAccents(n1);
		boolean m=r.equals("aeeiou");
		if(!m) {
			System.out.println(n1+"\n"+r);
		}
		assertTrue("Equal: "+m ,m); 
	}
}
