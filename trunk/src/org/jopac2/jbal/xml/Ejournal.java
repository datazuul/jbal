package org.jopac2.jbal.xml;

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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import org.jopac2.jbal.Readers.MdbTableRecordReader;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.Utils;

public class Ejournal extends XML {
	
	/**
<record>
<ID>12706</ID>
<Link>http://www.jstor.org/journals/01482076.html</Link>
<SITO>www.jstor.org</SITO>
<Editore>University of California press</Editore>
<Titolo>19th century music</Titolo>
<ISSN>0148-2076</ISSN>
<Sigla>U</Sigla>
<Raggiungibilita>da UnivTS</Raggiungibilita>
<Commissionario>Cilea</Commissionario>
<SovrapprezzoFullText>0</SovrapprezzoFullText>
<interfacciaFullText>JSTOR</interfacciaFullText>
<LTLinksolver>JSTOR</LTLinksolver>
<Note>Duplicato dicembre 2005</Note>
<NoteUtenza>1(1977) n. 1 - 25(2002) n. 2/3</NoteUtenza>
<Start>1977</Start>
<End>2002</End>
<Linkinfo>1:1:25:3</Linkinfo>
<Corrente>0</Corrente>
<CentroSpesa>42</CentroSpesa>
<LTLinksolverspec>E-JoUTS</LTLinksolverspec>
</record>
	 */
	
	public Ejournal(String notizia, String dTipo, String livello)
			throws NumberFormatException, SAXException, IOException {
		this.XMLCostruttore(notizia, dTipo, Integer.parseInt(livello));
	}

	public Ejournal(String notizia, String dTipo) throws SAXException, IOException {
		this.XMLCostruttore(notizia, dTipo, 0);
	}

	@Override
	public String getAbstract() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> getAuthors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<ClassificationInterface> getClassifications() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDescription() {
		return getNodeContent("Linkinfo");
	}

	@Override
	public String getEdition() {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector<String> getEditors() {
		Vector<String> v=new Vector<String>();
		Node n=getNode("Editore");
		if(n!=null) v.addElement(n.getTextContent());
		return v;
	}


	@Override
	public String getPublicationDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPublicationPlace() {
		// Commissionario
		return getNodeContent("Commissionario");
	}

	public RecordReader getRecordReader(InputStream dataFile) throws UnsupportedEncodingException {
		//EJournalHandler handler=new EJournalHandler("dataroot","record","EJournal");
		//return new XmlRecordReader(dataFile, handler);
		return new MdbTableRecordReader(dataFile, "Riviste");
	}
	
	
	public Vector<BookSignature> getSignatures() {
		Vector<BookSignature> v=new Vector<BookSignature>();
		String sito=getNodeContent("SITO");
		String link=getNodeContent("Link");
		String noteUtenza=getNodeContent("NoteUtenza");
		String LTLinksolver=getNodeContent("LTLinksolver");
		// String libraryId, String libraryName, String bookNumber, String bookLocalization, String bookCons
		v.addElement(new BookSignature(LTLinksolver,sito,link,noteUtenza));
		return v;
	}

	@Override
	public Vector<SubjectInterface> getSubjects() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
		return getNodeContent("Titolo");
	}

	
	  public String getBid() {
		  Node bid=getNode("ID");
	      if(bid==null) {return Long.toString(getJOpacID());}
	      else return bid.getTextContent();
	  }

	public String getStandardNumber() {
		return getNodeContent("ISSN");
	}


	public String getComments() {
		return getNodeContent("Note");
	}

	public String getISBD() {
		String r=getTitle();
		r+=Utils.ifExists(". - ",getEditors());
		
		r+=Utils.ifExists(" ((",getComments());
		r+=Utils.ifExists(". - ISSN ",getStandardNumber());
		return r;
	}

	public void addTag(Tag newTag) {
		// TODO Auto-generated method stub
		
	}

	public Vector<Tag> getTags(String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return null;
	}


}
