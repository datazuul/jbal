package org.jopac2.test;

import junit.framework.TestCase;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.junit.After;
import org.junit.Before;


public class UnimarcTest extends TestCase {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	
	public void testS1() throws Exception {
		String n1="01189nam0+2200253+++4500001002000000005000900020020001100029100004100040101000800081102000700089200005100096210002700147215001800174225010300192410013400295606004400429676005700473700004200530801002300572899006000595899005800655899013200713899009000845%1EIT%5CICCU%5CTSA%5C0021482%1E19970702%1E++%1Fb59-485%1E++%1Fa19970630d1959++++%7C%7C%7C%7C%7Citac0103++++ba%1E%7C+%1Faita%1E++%1FaIT%1E1+%1FaShakespeare+e+il+Rinascimento%1FfNemi+D%27Agostino%1E++%1FaTrieste%1FcSmolars%1Fd1959%1E++%1Fa23+p.%1Fd23+cm.%1E0+%1FaUniversita+degli+studi+di+Trieste%2C+Facolta+di+lettere+e+filosofia%2C+Istituto+di+filologia+germanica%1E+1%1F1001IT%5CICCU%5CCFI%5C0083335%1F12001+%1FaUniversita+degli+studi+di+Trieste%2C+Facolta+di+lettere+e+filosofia%2C+Istituto+di+filologia+germanica%1E++%1FaShakespeare%2C+William+e+Rinascimento%1F2FI%1E++%1Fa822.3%1FvFI%1F1LETTERATURA+DRAMMATICA+INGLESE.+1558-1625%1E+1%1FaD%27Agostino%2C+Nemi%1F3IT%5CICCU%5CCFIV%5C032233%1E+0%1FaIT%1FbICCU%1Fc20040216%1E++%1FaBiblioteca+nazionale+centrale+Firenze+FI%1F1FI0098%1F2CFICF%1E++%1FaBiblioteca+statale+Isontina+Gorizia+GO%1F1GO0025%1F2TSABI%1E++%1FaBiblioteca+delle+facolt%E2%80%A1+di+Giurisprudenza+e+Lettere+e+filosofia+dell%27Universit%E2%80%A1+degli+studi+di+Milano+Milano+MI%1F1MI0190%1F2USMA6%1E++%1FaBiblioteca+generale+dell%27Universita%27+degli+studi+di+Trieste+Trieste+TS%1F1TS0137%1F2TSABG%1E%1D";
		
		n1=java.net.URLDecoder.decode(n1, "utf-8");
		
		RecordInterface ma1=RecordFactory.buildRecord(0, n1, "unimarc", 0);
		
		String n2=ma1.toString();
		
		ma1.destroy();

		System.out.println("Originale: "+n1);
		System.out.println("Calcolato: "+n2);
		assertTrue("Confronto: "+n1.equals(n2),n1.equals(n2));  
	}
}
