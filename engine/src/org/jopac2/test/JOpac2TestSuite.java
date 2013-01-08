package org.jopac2.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JOpac2TestSuite {

	public static final boolean TEST_COMPLETO=true; // se 1 droppa, ricrea e reimporta il db
	private static String catalog="catalog";
	
    public static Test suite() throws Exception {    	
    	DBUtils.Prepara(catalog, TEST_COMPLETO);    	
        TestSuite suite = new TestSuite();
        suite.addTestSuite(MainDoSearch.class);
        suite.addTestSuite(MainBug.class);
        return suite;
    }  
    
    /**
     * Runs the test suite using the textual runner.
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        junit.textui.TestRunner.run(suite());
    }
}
