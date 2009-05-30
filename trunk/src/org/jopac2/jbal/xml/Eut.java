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


import org.xml.sax.SAXException;

import org.jopac2.jbal.Readers.MdbTableRecordReader;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.utils.BookSignature;


public class Eut extends XML {
	
	/**
	 * 
	 * <record> 
	 * 	<ID>1</ID> 
	 * 	<Titolo>Le due sponde del Mediterraneo : l'immagine riflessa; 473 p., 24 cm.</Titolo> 
	 * 	<Autore 1>null</Autore 1> 
	 * 	<Autore 2>null</Autore 2> 
	 * 	<Autore 3>null</Autore 3> 
	 * 	<Autore 4>null</Autore 4> 
	 * 	<ISBN>null</ISBN> 
	 * 	<Anno>1999.0</Anno> 
	 * 	<Prezzo>euro 20,00</Prezzo> 
	 * 	<Abstract>Presentazione ... </Abstract> 
	 * 	<File immagine>due_sponde_mediterraneo</File immagine> 
	 * </record>
	 * 
	 * 
	 */
	
	public Eut(String notizia, String dTipo, String livello)
			throws NumberFormatException, SAXException, IOException {
		this.XMLCostruttore(notizia, dTipo, Integer.parseInt(livello));
	}

	public Eut(String notizia, String dTipo) throws SAXException, IOException {
		this.XMLCostruttore(notizia, dTipo, 0);
	}

	@Override
	public String getAbstract() {
		return getNodeContent("Abstract");
	}

	@Override
	public Vector<String> getAuthors() {
		Vector<String> v=new Vector<String>();
		if(getNodeContent("Autore1")!=null) v.addElement(getNodeContent("Autore1"));
		if(getNodeContent("Autore2")!=null) v.addElement(getNodeContent("Autore2"));
		if(getNodeContent("Autore3")!=null) v.addElement(getNodeContent("Autore3"));
		if(getNodeContent("Autore4")!=null) v.addElement(getNodeContent("Autore4"));
		return v;
	}

	@Override
	public Vector<ClassificationInterface> getClassifications() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDescription() {
		return null;
	}

	@Override
	public String getEdition() {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector<String> getEditors() {
		Vector<String> v=new Vector<String>();
		return v;
	}


	public String getPublicationDate() {
		return getNodeContent("Anno");
	}

	public String getPublicationPlace() {
		// Commissionario
		return "";
	}

	public RecordReader getRecordReader(InputStream f) throws UnsupportedEncodingException {
		//EutHandler handler=new EutHandler("root","record","Eut");
		//return new XmlRecordReader(f, handler);
		return new MdbTableRecordReader(f, "Foglio1");
	}
	
	
	public Vector<BookSignature> getSignatures() {
		Vector<BookSignature> v=new Vector<BookSignature>();
		String sito=getNodeContent("File_immagine");
		
		sito=sito.toLowerCase().replaceAll("�", "a");
		sito=sito.replaceAll("�", "o").replaceAll("�", "u");
		sito=sito.replaceAll("\\W", "_");
		
		String link=getNodeContent("Prezzo");
		String noteUtenza=sito;
		String LTLinksolver=getNodeContent("Prezzo");
		// String libraryId, String libraryName, String bookNumber, String bookLocalization, String bookCons
		v.addElement(new BookSignature(LTLinksolver,sito,link,noteUtenza));
		return v;
	}

	@Override
	public Vector<String> getSubjects() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
		return getNodeContent("Titolo");
	}

	
	  public String getBid() {
		  return Long.toString(getJOpacID());
	  }

	public String getStandardNumber() {
		return getNodeContent("ISBN");
	}


	public String getComments() {
		return "";
	}

	public String getISBD() {
		return getNodeContent("Titolo");
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
