package JSites.test;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2011 JOpac2 project
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

import junit.framework.TestCase;

import org.w3c.dom.Document;

import JSites.transformation.catalogSearch.MergeRecords;
import JSites.utils.XMLUtil;


/**
 * 		
 * @author romano
 *
 */
public class MergeRecordTest extends TestCase {
	private MergeRecords mr;
	
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
				"<jid>6142</jid>\n"+
				"</record>";	
	
	
	/*************************************/

	String xml2="<record>" +
				"	<primo>a</primo>" +
				"	<secondo>b</secondo>" +
				"</record>";
	
	String xml3="<record>" +
				"	<primo>a</primo>" +
				"	<secondo>c</secondo>" +
				"</record>";
	
	String xml4="<record>" +
				"	<primo>a</primo>" +
				"	<secondo>d</secondo>" +
				"</record>";
	
	String xml5="<record>" +
				"	<primo>a</primo>" +
				"	<secondo>b</secondo>" +
				"	<ter>d</ter>" +
				"	<qua>c</qua>" +
				"</record>";

	String xml6="<record>" +
				"	<primo>a</primo>" +
				"	<secondo>c</secondo>" +
				"	<ter>d</ter>" +
				"	<qua>c</qua>" +
				"</record>";
	
	String xml7="<record>" +
				"	<primo>a</primo>" +
				"	<secondo>d</secondo>" +
				"	<ter>d</ter>" +
				"	<qua>c</qua>" +
				"</record>";
	
	
	String xmlout1="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<record>" +
				"<primo>a</primo>" +
				"<item><secondo>b</secondo></item>" +
				"<item><secondo>c</secondo></item>" +
				"</record>";
	
	String xmlout2="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<record>" +
				"<primo>a</primo>" +
				"<item><secondo>b</secondo></item>" +
				"<item><secondo>c</secondo></item>" +
				"<item><secondo>d</secondo></item>" +
				"</record>";
	
	String xmlout3="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<record>" +
				"<primo>a</primo>" +
				"<ter>d</ter>" +
				"<item><secondo>b</secondo><qua>c</qua></item>" +
				"<item><secondo>c</secondo><qua>c</qua></item>" +
				"</record>";

	String xmlout4="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<record>" +
				"<primo>a</primo>" +
				"<ter>d</ter>" +
				"<item><secondo>b</secondo><qua>c</qua></item>" +
				"<item><secondo>c</secondo><qua>c</qua></item>" +
				"<item><secondo>d</secondo><qua>c</qua></item>" +
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
	
	
	String per0="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<record>" +
				"<jid>11706</jid>" +
				"<db>ej</db>" +
				"<bid>11706</bid>" +
				"<ID>22380</ID>" +
				"<Link>http://www.jstor.org/journals/00128163.html</Link>" +
				"<SITO>www.jstor.org</SITO>" +
				"<Editore>University of North Carolina press</Editore>" +
				"<Titolo>Early American Literature</Titolo>" +
				"<ISSN>0012-8163</ISSN>" +
				"<Sigla>U</Sigla>" +
				"<Raggiungibilita>da UnivTS</Raggiungibilita>" +
				"<Commissionario>Cilea</Commissionario>" +
				"<SovrapprezzoFullText>false</SovrapprezzoFullText>" +
				"<Corrente>false</Corrente>" +
				"<Registrazione>(Arts &amp; Sciences 5) ; alert 29.01.09</Registrazione>" +
				"<AccessoAttivo>true</AccessoAttivo>" +
				"<Verificato>true</Verificato>" +
				"<DataVerifica>Tue Jan 12 00:00:00 CET 2010</DataVerifica>" +
				"<interfacciaFullText>JSTOR</interfacciaFullText>" +
				"<LTLinksolver>JSTOR</LTLinksolver>" +
				"<Note>MW: 3.1.0</Note>" +
				"<NoteUtenza>3(1968) n. 2 - 41(2006) n. 3</NoteUtenza>" +
				"<Start>1968</Start>" +
				"<End>2006</End>" +
				"<Linkinfo>3:2:41:3</Linkinfo>" +
				"<CentroSpesa>42</CentroSpesa>" +
				"<LTLinksolverspec>E-JoUTS</LTLinksolverspec>" +
				"<NuovIinserimento>false</NuovIinserimento>" +
				"<DataInserimento>Tue Feb 03 00:00:00 CET 2009</DataInserimento>" +
			"</record>";
	String per1="<record>" +
				"<jid>12662</jid>" +
				"<db>ej</db>" +
				"<bid>12662</bid>" +
				"<ID>23407</ID>" +
				"<Link>http://gateway.proquest.com/openurl?ctx_ver=Z39.88-2003&amp;xri:pqil:res_ver=0.2&amp;res_id=xri:lion&amp;rft_id=xri:lion:contents:abl_toc/EarlyAmericanLiteratureChapelHi/issues.htm</Link>" +
				"<SITO>lion.chadwyck.co.uk</SITO>" +
				"<Editore>University of North Carolina Press</Editore>" +
				"<Titolo>Early American Literature </Titolo>" +
				"<ISSN>0012-8163</ISSN>" +
				"<Sigla>U</Sigla>" +
				"<Raggiungibilita>da UnivTS</Raggiungibilita>" +
				"<Commissionario>Dea</Commissionario>" +
				"<SovrapprezzoFullText>false</SovrapprezzoFullText>" +
				"<Corrente>true</Corrente>" +
				"<AccessoAttivo>false</AccessoAttivo>" +
				"<Verificato>false</Verificato>" +
				"<DataVerifica>Wed Jan 20 00:00:00 CET 2010</DataVerifica>" +
				"<interfacciaFullText>Chadwyck</interfacciaFullText>" +
				"<LTLinksolver>Chadwyck</LTLinksolver>" +
				"<NoteUtenza>(2004) -</NoteUtenza>" +
				"<Start>2004</Start>" +
				"<Linkinfo>:3::</Linkinfo>" +
				"<CentroSpesa>42</CentroSpesa>" +
				"<LTLinksolverspec>E-JoUTS</LTLinksolverspec>" +
				"<IDArea>0</IDArea>" +
				"<NuovIinserimento>false</NuovIinserimento>" +
				"<DataInserimento>Wed Jan 20 00:00:00 CET 2010</DataInserimento>" +
			"</record>";

	String perOut0="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<record>" +
				"<ISSN>0012-8163</ISSN>" +
				"<Titolo>Early American Literature</Titolo>" +
				"<item>" +
					"<jid>11706</jid>" +
					"<db>ej</db>" +
					"<bid>11706</bid>" +
					"<ID>22380</ID>" +
					"<Link>http://www.jstor.org/journals/00128163.html</Link>" +
					"<SITO>www.jstor.org</SITO>" +
					"<Editore>University of North Carolina press</Editore>" +
					"<Sigla>U</Sigla>" +
					"<Raggiungibilita>da UnivTS</Raggiungibilita>" +
					"<Commissionario>Cilea</Commissionario>" +
					"<SovrapprezzoFullText>false</SovrapprezzoFullText>" +
					"<Corrente>false</Corrente>" +
					"<Registrazione>(Arts &amp; Sciences 5) ; alert 29.01.09</Registrazione>" +
					"<AccessoAttivo>true</AccessoAttivo>" +
					"<Verificato>true</Verificato>" +
					"<DataVerifica>Tue Jan 12 00:00:00 CET 2010</DataVerifica>" +
					"<interfacciaFullText>JSTOR</interfacciaFullText>" +
					"<LTLinksolver>JSTOR</LTLinksolver>" +
					"<Note>MW: 3.1.0</Note>" +
					"<NoteUtenza>3(1968) n. 2 - 41(2006) n. 3</NoteUtenza>" +
					"<Start>1968</Start>" +
					"<End>2006</End>" +
					"<Linkinfo>3:2:41:3</Linkinfo>" +
					"<CentroSpesa>42</CentroSpesa>" +
					"<LTLinksolverspec>E-JoUTS</LTLinksolverspec>" +
					"<NuovIinserimento>false</NuovIinserimento>" +
					"<DataInserimento>Tue Feb 03 00:00:00 CET 2009</DataInserimento>" +
				"</item>" +
				"<item>" +
					"<jid>12662</jid>" +
					"<db>ej</db>" +
					"<bid>12662</bid>" +
					"<ID>23407</ID>" +
					"<Link>http://gateway.proquest.com/openurl?ctx_ver=Z39.88-2003&amp;xri:pqil:res_ver=0.2&amp;res_id=xri:lion&amp;rft_id=xri:lion:contents:abl_toc/EarlyAmericanLiteratureChapelHi/issues.htm</Link>" +
					"<SITO>lion.chadwyck.co.uk</SITO>" +
					"<Editore>University of North Carolina Press</Editore>" +
					"<Sigla>U</Sigla>" +
					"<Raggiungibilita>da UnivTS</Raggiungibilita>" +
					"<Commissionario>Dea</Commissionario>" +
					"<SovrapprezzoFullText>false</SovrapprezzoFullText>" +
					"<Corrente>true</Corrente>" +
					"<AccessoAttivo>false</AccessoAttivo>" +
					"<Verificato>false</Verificato>" +
					"<DataVerifica>Wed Jan 20 00:00:00 CET 2010</DataVerifica>" +
					"<interfacciaFullText>Chadwyck</interfacciaFullText>" +
					"<LTLinksolver>Chadwyck</LTLinksolver>" +
					"<NoteUtenza>(2004) -</NoteUtenza>" +
					"<Start>2004</Start>" +
					"<Linkinfo>:3::</Linkinfo>" +
					"<CentroSpesa>42</CentroSpesa>" +
					"<LTLinksolverspec>E-JoUTS</LTLinksolverspec>" +
					"<IDArea>0</IDArea>" +
					"<NuovIinserimento>false</NuovIinserimento>" +
					"<DataInserimento>Wed Jan 20 00:00:00 CET 2010</DataInserimento>" +
					"</item>" +
				"</record>";
	
	protected void setUp() throws Exception {
		mr=new MergeRecords();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void test1merge() throws Exception {
		Document aDoc=XMLUtil.String2XML(xml2);
		Document bDoc=XMLUtil.String2XML(xml3);
		
		Document[] result=mr.processRecords(aDoc, bDoc, "/record/primo");
		
		String ret=XMLUtil.XML2String(result[0]);
		System.out.println(ret);
		assertTrue("OK",ret.equals(xmlout1));
	}
	
	public void test2merge() throws Exception {
		Document aDoc=XMLUtil.String2XML(xmlout1);
		Document bDoc=XMLUtil.String2XML(xml4);
		
		Document[] result=mr.processRecords(aDoc, bDoc, "/record/primo");
		
		String ret=XMLUtil.XML2String(result[0]);
		System.out.println(ret);
		assertTrue("OK",ret.equals(xmlout2));
	}
	
	public void test3merge() throws Exception {
		Document aDoc=XMLUtil.String2XML(xml5);
		Document bDoc=XMLUtil.String2XML(xml6);
		
		Document[] result=mr.processRecords(aDoc, bDoc, "/record/primo","/record/ter");
		
		String ret=XMLUtil.XML2String(result[0]);
		System.out.println(ret);
		assertTrue("OK",ret.equals(xmlout3));
	}
	
	public void test4merge() throws Exception {
		Document aDoc=XMLUtil.String2XML(xml7);
		Document bDoc=XMLUtil.String2XML(xmlout3);
		
		Document[] result=mr.processRecords(aDoc, bDoc, "/record/primo","/record/ter");
		
		String ret=XMLUtil.XML2String(result[0]);
		System.out.println(ret);
		assertTrue("OK",ret.equals(xmlout4));
	}
	
	public void test1mergePeriodici() throws Exception {
		Document aDoc=XMLUtil.String2XML(per0);
		Document bDoc=XMLUtil.String2XML(per1);
		
		Document[] result=mr.processRecords(aDoc, bDoc, "/record/ISSN", "/record/Titolo");
		
		String ret=XMLUtil.XML2String(result[0]);
		System.out.println(ret);
		assertTrue("OK",ret.equals(perOut0));
	}
}
