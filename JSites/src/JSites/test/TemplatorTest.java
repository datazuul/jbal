package JSites.test;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2009 JOpac2 project
*
*     This file is part of JOpac2. http://www.jopac2.org
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
*  Please, see NOTICE.txt AND LEGAL directory for more info. Different licences
*  may apply for components included in JOpac2.
*
*******************************************************************************/

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import JSites.transformation.catalogSearch.Group;
import JSites.transformation.catalogSearch.Templator;
import JSites.utils.XMLUtil;
import junit.framework.TestCase;


/**
 * 		
 * @author romano
 *
 */
public class TemplatorTest extends TestCase {
	private boolean escape=false;
	private String xml1="<record nature=\"a\">\n"+
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
	
	/*************************************/

	String xml2="<record>" +
				"	<primo>a, b, c</primo>" +
				"	<secondo>1, 2, 3</secondo>" +
				"</record>";
	
	String c="<record>" +
			"	<item>" +
			"		<primo>a</primo>" +
			"		<secondo>1</secondo>" +
			"	</item>" +
			"	<item>" +
			"		<primo>b</primo>" +
			"		<secondo>2</secondo>" +
			"	</item>" +
			"	<item>" +
			"		<primo>c</primo>" +
			"		<secondo>3</secondo>" +
			"	</item>" +
			"</record>";
	
	
	
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	

	public void test1p() throws Exception {
		String out="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<record>		" +
				"<item><primo>a</primo><secondo>1</secondo></item>" +
				"<item><primo>b</primo><secondo>2</secondo></item>" +
				"<item><primo>c</primo><secondo>3</secondo></item>" +
				"</record>";
		Document doc=XMLUtil.String2XML(xml2);
		Group.groupPair(doc, ",", "item", "/record/primo", "/record/secondo");
		String ret=XMLUtil.XML2String(doc);
		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}
	
	public void test1p_fin() throws Exception {
		String indoc="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<record>		" +
				"<item><primo>a</primo><secondo>1</secondo></item>" +
				"<item><primo>b</primo><secondo>2</secondo></item>" +
				"<item><primo>c</primo><secondo>3</secondo></item>" +
				"</record>";
		String in="{{/record/item:<a href='[[primo]]'>[[secondo]]</a>}}";
		String out="<a href='a'>1</a><a href='b'>2</a><a href='c'>3</a>";
		Document doc=XMLUtil.String2XML(indoc);
		String ret=Templator.parseContext(doc, in,escape);
		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}
	
	
	public void test1p_limit() throws Exception {
		String in="{{^Limit(/record/ISBD|30| <a href=\"/dettaglio/?query=JID=[[/record/jid]]\">Continua</a>):[[.]]}}";
		String out="Il *pensiero americano <a href=\"/dettaglio/?query=JID=6142\">Continua</a>";
		Document doc=XMLUtil.String2XML(xml1);
		String ret=Templator.parseContext(doc, in,escape);

		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}
	
	public void test2p_limit() throws Exception {
		String in="{{^Limit(Nella vecchia fattoria, ia ia oooooo, c'è la mucca di zio tobia, ia ia oooo|30| <a href=\"\">Continua</a>):[[.]]}}";
		String out="Nella vecchia fattoria, ia ia <a href=\"\">Continua</a>";
		Document doc=XMLUtil.String2XML(xml1);
		String ret=Templator.parseContext(doc, in,escape);

		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}
	
	public void test1p_group() throws Exception {
		String in="{{^Group(,|item|/record/primo|/record/secondo):<a href='[[primo]]'>[[secondo]]</a>}}";
		String out="<a href='a'>1</a><a href='b'>2</a><a href='c'>3</a>";
		Document doc=XMLUtil.String2XML(xml2);
		String ret=Templator.parseContext(doc, in,escape);

		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}
	
	public void test2p_group() throws Exception {
		String in="{{^Group(,|item|/record/primo):<a href='[[primo]]'>[[primo]]</a>}}";
		String out="<a href='a'>a</a><a href='b'>b</a><a href='c'>c</a>";

		Document doc=XMLUtil.String2XML(xml2);
		String ret=Templator.parseContext(doc, in,escape);

		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}
	
	public void test1k() throws Exception {
		String in="<p>{{/record/haspart/record:<b><a href='[[bid]]'>[[isbd]]</a></b>}}</p>"; // se c'e' haspart/record, per ogni record fa quello indicato
		String out="<p><b><a href='RA4353750'>Filosofia, epistemologia, logica / saggi di Barone ... [et al.]. - Milano : Edizioni di Comunita , 1958. - XI, 340 p. ; 25 cm</a></b>" +
				"<b><a href='RA4353751'>Scienze sociali / saggi di Barbano ... [et al.]. - Milano : Edizioni di Comunita , 1958. - XI, 390 p. ; 25 cm</a></b></p>";
		Document doc=XMLUtil.String2XML(xml1);
		String ret=Templator.parseContext(doc, "{{/:"+in+"}}",escape);
		
		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}
	
	public void test2k() throws Exception {
		String in="<p>{{/record/haspart:<b>ciao</b>}}</p>"; // se c'e' haspart, per ogni haspart fa quello indicato
		String out="<p><b>ciao</b></p>";
		Document doc=XMLUtil.String2XML(xml1);
		String ret=Templator.parseContext(doc, "{{/:"+in+"}}",escape);
		
		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}
	
	public void test3k() throws Exception {
		String in="<p>{{/record/haspart:<b>ciao{{/:<i>[[record/ISBD]]</i>}}</b>}}</p>"; 
		String out="<p><b>ciao<i>Il *pensiero americano contemporaneo / direzione dell'opera Ferruccio Rossi-Landi. - Milano : Edizioni di Comunita. - 2 v. ; 25 cm. - In testa al front.: Centro di studi metodologici di Torino</i></b></p>";
		Document doc=XMLUtil.String2XML(xml1);
		String ret=Templator.parseContext(doc, "{{/:"+in+"}}",escape);
		
		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}
	
	public void test4k() throws Exception {
		String in="<p>{{//bid:<b>[[.]]</b>}}</p>"; 
		String out="<p><b>RA4353750</b><b>RA4353751</b><b>RA4388959</b></p>";
		Document doc=XMLUtil.String2XML(xml1);
		String ret=Templator.parseContext(doc, "{{/:"+in+"}}",escape);
		
		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}
	
	public void test5k() throws Exception {
		String in="<p>{{/:<b>[[//bid]]</b>}}</p>"; 
		String out="<p><b>RA4353750RA4353751RA4388959</b></p>";
		Document doc=XMLUtil.String2XML(xml1);
		String ret=Templator.parseContext(doc, "{{/:"+in+"}}",escape);
		
		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}	
	
	public void testRAW() throws Exception {
		String in="[[*RAW*]]"; 
		String out="<pre><?xml version=\"1.0\" encoding=\"UTF-8\"?><record>	<primo>a, b, c</primo>	<secondo>1, 2, 3</secondo></record></pre>";
		Document doc=XMLUtil.String2XML(xml2);
		String ret=Templator.parseContext(doc, "{{/:"+in+"}}",escape);
		ret=ret.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
		System.out.println(ret);
		assertTrue("OK",ret.equals(out));
	}

}
