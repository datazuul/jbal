package JSites.test;


import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import JSites.transformation.catalogSearch.Templator;
import junit.framework.TestCase;


/**
 * 		
 * @author romano
 *
 */
public class TemplatorTest extends TestCase {
	
	private String xml1=""+
				"<record nature=\"a\">\n"+
				"<jid>6142</jid>\n"+
				"<db>treviso</db>\n"+
				"<type>sebina</type>\n"+
				"<nature>a</nature>\n"+
				"<image>images/pubimages/NS.jpg</image>\n"+
				"<title>Il *pensiero americano contemporaneo / direzione dell'opera Ferruccio Rossi-Landi</title>\n"+
				"<authors>\n"+
				"<author>Rossi-Landi , Ferruccio</author>\n"+
				"</authors>\n"+
				"<ISBD>Il *pensiero americano contemporaneo / direzione dell'opera Ferruccio Rossi-Landi. - Milano : Edizioni di Comunita. - 2 v. ; 25 cm. - In testa al front.: Centro di studi metodologici di Torino</ISBD>\n"+
				"<editors>\n"+
				"<editor>Edizioni di Comunita</editor>\n"+
				"</editors>\n"+
				"<publicationPlace>Milano</publicationPlace>\n"+
				"<subjects>\n"+
				"<subject>Societa</subject>\n"+
				"<subject>Milano</subject>\n"+
				"</subjects>\n"+
				"<haspart>\n"+
				"<record>\n"+
				"<isbd>Filosofia, epistemologia, logica / saggi di Barone ... [et al.]. - Milano : Edizioni di Comunita , 1958. - XI, 340 p. ; 25 cm</isbd>\n"+
				"<bid>RA4353750</bid>\n"+
				"<jid>0</jid>\n"+
				"</record>\n"+
				"<record>\n"+
				"<isbd>Scienze sociali / saggi di Barbano ... [et al.]. - Milano : Edizioni di Comunita , 1958. - XI, 390 p. ; 25 cm</isbd>\n"+
				"<bid>RA4353751</bid>\n"+
				"<jid>0</jid>\n"+
				"</record>\n"+
				"</haspart>\n"+
				"<bid>RA4388959</bid>\n"+
				"<jid>6142</jid>\n"+
				"</record>";	
	
	/**
	 * [[xxxx]] indica sempre un elemento da prendere, relativamente al contesto (sotto albero DOM impostato)
	 * Se nel template c'è solo [[record/isbd]] verra cercato in tutto l'albero
	 * 
	 * {{zzzz:[[yyyyy]]}} indica l'elemento yyyyy nel contesto zzzz
	 * Se l'elemento zzzz che indica il contesto esiste allora prende e interpreta quello dopo i :
	 * Se l'elemento zzzz che indica il contesto non esiste allora non fa nulla
	 */
	String a="<p>{{/record/haspart/record:<b><a href='[[bid]]'>[[isbd]]</a></b>}}</p>"; // se c'e' haspart/record, per ogni record fa quello indicato
	String b="<p>{{/record/haspart:<b>ciao</b>}}</p>"; // se c'e' haspart, per ogni haspart fa quello indicato

	
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	

	
	public void test1k() throws Exception {
		String in="<p>{{/record/haspart/record:<b><a href='[[bid]]'>[[isbd]]</a></b>}}</p>"; // se c'e' haspart/record, per ogni record fa quello indicato
		String out="<p><b><a href='RA4353750'>Filosofia, epistemologia, logica / saggi di Barone ... [et al.]. - Milano : Edizioni di Comunita , 1958. - XI, 340 p. ; 25 cm</a></b>" +
				"<b><a href='RA4353751'>Scienze sociali / saggi di Barbano ... [et al.]. - Milano : Edizioni di Comunita , 1958. - XI, 390 p. ; 25 cm</a></b></p>";
		Document doc=Templator.String2XML(xml1);
		String ret=Templator.parseContext(doc, "{{/:"+in+"}}");
		
		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}
	
	public void test2k() throws Exception {
		String in="<p>{{/record/haspart:<b>ciao</b>}}</p>"; // se c'e' haspart, per ogni haspart fa quello indicato
		String out="<p><b>ciao</b></p>";
		Document doc=Templator.String2XML(xml1);
		String ret=Templator.parseContext(doc, "{{/:"+in+"}}");
		
		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}
	
	public void test3k() throws Exception {
		String in="<p>{{/record/haspart:<b>ciao{{/:<i>[[record/ISBD]]</i>}}</b>}}</p>"; 
		String out="<p><b>ciao<i>Il *pensiero americano contemporaneo / direzione dell'opera Ferruccio Rossi-Landi. - Milano : Edizioni di Comunita. - 2 v. ; 25 cm. - In testa al front.: Centro di studi metodologici di Torino</i></b></p>";
		Document doc=Templator.String2XML(xml1);
		String ret=Templator.parseContext(doc, "{{/:"+in+"}}");
		
		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}

}
