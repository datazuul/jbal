package JSites.test;


import JSites.transformation.WikiTransformer3;
import junit.framework.TestCase;


/**
 * 		
 * @author romano
 *
 */
public class WikiTest extends TestCase {
	WikiTransformer3 wiki=new WikiTransformer3();
	
	
	protected void setUp() throws Exception {
		wiki.DEBUG=true;
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private String doWiki(String testcase) throws Exception {
		wiki.doFinal(new StringBuffer(testcase));
		return wiki.DEBUG_RET.toString();
	}
	
	public void testDoppiaQuadraLink() throws Exception {
		String ret=doWiki("[[http://www.oaister.org/>OAIster] [http://www.openarchives.eu>Openarchives.eu] ");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<a></a>" +
				"<a href=\"http://www.oaister.org/\">OAIster</a> " +
				"<a href=\"http://www.openarchives.eu\">Openarchives.eu</a> "));
	}
	
	
	public void testFineListe() throws Exception {
		String ret=doWiki("riga normale\n* elenco 1\n*elenco 2\n**sottoelenco 1\n**sottoelenco2\n\nriga normale\n");
		System.out.println(ret);
		assertTrue("OK",ret.equals("riga normale<br></br>" +
				"<ul level=\"1\"><li> elenco 1</li><li>elenco 2</li>" +
				"<ul level=\"2\"><li>sottoelenco 1</li><li>sottoelenco2</li>" +
				"</ul>" +
				"</ul>" +
				"riga normale<br></br>"));
	}
	
	
	public void testLinkSbagliato() throws Exception {
		String ret=doWiki("_blank>>http://140.105.147.200/BancheDati/webuniv/bd.htm>Banche dati]");
		System.out.println(ret);
		assertTrue("OK",ret.equals("_blank>>http://140.105.147.200/BancheDati/webuniv/bd.htm>Banche dati]"));
	}
	
	
	public void testTable() throws Exception {
		String ret=doWiki("[$table>border=1[$td>>testo]]");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<table border=\"1\"><td>testo</td></table>"));
	}
	
	public void testTable1() throws Exception {
		String ret=doWiki("[$table[$td>>testo]]");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<table><td>testo</td></table>"));
	}
	
	

	public void testItalic1() throws Exception {
		String ret=doWiki("abc ''corsivo'' def");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abc <i>corsivo</i> def"));
	}
	
	public void testBold1() throws Exception {
		String ret=doWiki("abc __bold__ def");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abc <b>bold</b> def"));
	}
	
	public void testItalic2() throws Exception {
		String ret=doWiki("abc ''corsivo def");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abc <i>corsivo def</i>"));
	}
	
	public void testBoldItalic1() throws Exception {
		String ret=doWiki("abc __bold ''corsivo'' bold __def");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abc <b>bold <i>corsivo</i> bold </b>def"));
	}

	public void testBoldItalic2() throws Exception {
		String ret=doWiki("abc __bold ''corsivo__ bold __def");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abc <b>bold <i>corsivo<b> bold </b>def</i></b>"));
	}
	
	public void testBoldItalic3() throws Exception {
		String ret=doWiki("abc __bold ''corsivo__ bold'' def");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abc <b>bold <i>corsivo<b> bold<i> def</i></b></i></b>"));
	}
	
	public void testUnorderedList() throws Exception {
		String ret=doWiki("* uno\n* due\n* tre");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<ul level=\"1\"><li> uno</li><li> due</li><li> tre</li></ul>"));
	}
	
	public void testOrderedList() throws Exception {
		String ret=doWiki("# uno\n# due\n# tre");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<ol level=\"1\"><li> uno</li><li> due</li><li> tre</li></ol>"));
	}
	
	public void testOrderedList1() throws Exception {
		String ret=doWiki("# uno\n## due\n# tre");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<ol level=\"1\"><li> uno</li><ol level=\"2\"><li> due</li></ol><li> tre</li></ol>"));
	}
	
	public void testOrderedList2() throws Exception {
		String ret=doWiki("#### uno\n## due\n# tre");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<ol level=\"4\"><li> uno</li></ol><ol level=\"2\"><li> due</li></ol><ol level=\"1\"><li> tre</li></ol>"));
	}
	
	public void testHref() throws Exception {
		String ret=doWiki("abcd [/prova>testo] defg");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abcd <a href=\"/prova\">testo</a> defg"));
	}
	
	public void testHref1() throws Exception {
		String ret=doWiki("abcd [/prova_testo] defg");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abcd <a href=\"/prova_testo\">/prova_testo</a> defg"));
	}
	
	public void testHref2() throws Exception {
		String ret=doWiki("[http://pregresso.units.it/JOpac2/preg-sutrs>http://pregresso.units.it/JOpac2/preg-sutrs].");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<a href=\"http://pregresso.units.it/JOpac2/preg-sutrs\">http://pregresso.units.it/JOpac2/preg-sutrs</a>."));
	}
	
	public void testHrefBlank() throws Exception {
		String ret=doWiki("[_blank>>http://pregresso.units.it/JOpac2/preg-sutrs>pregresso].");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<a target=\"_blank\" href=\"http://pregresso.units.it/JOpac2/preg-sutrs\">pregresso</a>."));
	}
	
	public void testImage0() throws Exception {
		String ret=doWiki("abcd [im");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abcd <a>im</a>"));
	}	
	
	public void testImage() throws Exception {
		String ret=doWiki("abcd [img:/imagesrc>testo_alt] defg");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abcd <img src=\"/imagesrc\" alt=\"testo_alt\"></img> defg"));
	}
	
	public void testImage1() throws Exception {
		String ret=doWiki("abcd [img:/imagesrc>testo_alt defg");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abcd <img src=\"/imagesrc\">testo_alt defg</img>"));
	}
	
	public void testImage2() throws Exception {
		String ret=doWiki("abcd [img:/imagesrc>] defg");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abcd <img src=\"/imagesrc\" alt=\"\"></img> defg"));
	}
	
	public void testNewLine() throws Exception {
		String ret=doWiki("abcd\ndefg");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abcd<br></br>defg"));
	}
	
	public void testQuoteChar() throws Exception {
		String ret=doWiki("abcd\\[defg");
		System.out.println(ret);
		assertTrue("OK",ret.equals("abcd[defg"));
	}
	
	public void testBriciole() throws Exception {
		String ret=doWiki("[pageview?pid=5>Struttura] > [pageview?pid=137>  Coordinamento] > [pageview?pid=171>Nuova pagina]");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<a href=\"pageview?pid=5\">Struttura</a> > <a href=\"pageview?pid=137\">  Coordinamento</a> > <a href=\"pageview?pid=171\">Nuova pagina</a>"));
	}
	
	public void testTesto() throws Exception {
		String ret=doWiki("BIBLIOTECA AREA 3. TECNICO-SCIENTIFICA");
		System.out.println(ret);
		assertTrue("OK",ret.equals("BIBLIOTECA AREA 3. TECNICO-SCIENTIFICA"));
	}
	
	
	
	public void testSection1665() throws Exception {
		String ret=doWiki("[img:http://biblio44.units.it/Biblioteche/info/serciv/units.jpg>Universita' di Trieste]" +
				"[img:http://biblio44.units.it/Biblioteche/info/serciv/scn.jpg>Servizio Civile Nazionale] " +
				"[http://biblio44.units.it/Biblioteche/info/serciv/manifesto2006.pdf>__BANDO per la selezione di n. 22 volontari per il progetto dell'Università degli Studi di Trieste \"ALLARGHIAMO LA BIBLIOTECA\"__]" +
				"[http://biblio44.units.it/Biblioteche/info/serciv/ALLEGATO 2.doc>__Allegato 2__]" +
				"[http://biblio44.units.it/Biblioteche/info/serciv/ALLEGATO 3.doc>__Allegato 3__]" +
				"__SCADENZA: 2 OTTOBRE 2006__");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<img src=\"http://biblio44.units.it/Biblioteche/info/serciv/units.jpg\" alt=\"Universita' di Trieste\"></img>" +
				"<img src=\"http://biblio44.units.it/Biblioteche/info/serciv/scn.jpg\" alt=\"Servizio Civile Nazionale\"></img> " +
				"<a href=\"http://biblio44.units.it/Biblioteche/info/serciv/manifesto2006.pdf\">" +
					"<b>BANDO per la selezione di n. 22 volontari per il progetto dell'Università degli Studi di Trieste \"ALLARGHIAMO LA BIBLIOTECA\"</b>" +
				"</a>" +
				"<a href=\"http://biblio44.units.it/Biblioteche/info/serciv/ALLEGATO 2.doc\">" +
					"<b>Allegato 2</b>" +
				"</a>" +
				"<a href=\"http://biblio44.units.it/Biblioteche/info/serciv/ALLEGATO 3.doc\">" +
					"<b>Allegato 3</b>" +
				"</a>" +
				"<b>SCADENZA: 2 OTTOBRE 2006</b>"));
		
	}
	
	public void testSectionHref() throws Exception {
		String ret=doWiki("[http://biblio44.units.it/Biblioteche/info/serciv/ALLEGATO 2.doc>__Allegato 2__]");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<a href=\"http://biblio44.units.it/Biblioteche/info/serciv/ALLEGATO 2.doc\">" +
					"<b>Allegato 2</b>" +
				"</a>"));
		
	}
	
	
	
	public void testTextArea() throws Exception {
		String ret=doWiki("[$textarea>name=testo>rows=10>cols=50>>Inserisci testo qua]");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<textarea name=\"testo\" rows=\"10\" cols=\"50\">Inserisci testo qua</textarea>"));
	}
	
	public void testForm1() throws Exception {
		String ret=doWiki("[$form>action=login?fan=true>method=get>>Voio scriver\n" +
				"Go scritto invio: [$input>type=text>name=paiazo]\n" +
				"[$textarea>name=testo>rows=10>cols=50>>Inserisci testo qua\n" +
				"due righe]]");
		System.out.println(ret);
		assertTrue("OK",ret.equals("<form action=\"login?fan=true\" method=\"get\">Voio scriver<br></br>" +
				"Go scritto invio: <input type=\"text\" name=\"paiazo\"></input><br></br>" +
				"<textarea name=\"testo\" rows=\"10\" cols=\"50\">Inserisci testo qua<br></br>" +
				"due righe</textarea></form>"));
	}

}
