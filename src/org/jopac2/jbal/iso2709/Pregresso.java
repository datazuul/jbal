package org.jopac2.jbal.iso2709;

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

/*
* @author	Romano Trampus
* @version ??/??/2002
*/

/**
 * JOpac2 - Modulo formato "Pregresso". v. 0.1
 *          Questo modulo permette di importare dati registrati in un  formato
 *          temporaneo usato da Univ. Trieste per il recupero di dati
 *
 * Il formato � il seguente:
 * 001     file immagine in formato tif
 * 100^a     body html
 * 200^a     parole del body
 * 300^a     file immagine in formato tif
 * 310^a	 ISBD (corpo schedina)
 * 320^a	 ISBN (o numero standard)
 * 330^inventario^collocazione
 * 340^a	 data inserimento
 *     
 */


import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.util.*;

import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;

public class Pregresso extends ISO2709Impl {
	//private static String rt=String.valueOf((char)0x1d);
    //private static String ft=String.valueOf((char)0x1e);
    //private static String dl=String.valueOf((char)0x1f);        //' delimiter

  public Pregresso(byte[] stringa, Charset charset,String dTipo)  throws Exception {
    super();
    iso2709Costruttore(stringa,charset,dTipo,0);
    initLinkUp();
    initLinkDown();
    initLinkSerie();
  }
  

  public Pregresso(byte[] stringa, Charset charset,String dTipo,String livello)  throws Exception {
    super();
    iso2709Costruttore(stringa,charset,dTipo,Integer.parseInt(livello));
    initLinkUp();
    initLinkDown();
    initLinkSerie();
  }
  
  
  
  @Override
public String getBid() {
	String r=super.getBid();
	if(r!=null) r=r.replaceAll(delimiters.getFt(), "");
	return r;
}


public Hashtable<String, List<Tag>> getRecordMapping() {
		Hashtable<String, List<Tag>> r=new Hashtable<String, List<Tag>>();
		
		List<Tag> tit=new Vector<Tag>();

		tit.add(new Tag("300","a",""));
		tit.add(new Tag("310","a",""));
		tit.add(new Tag("320","a",""));
		tit.add(new Tag("330","a",""));
		tit.add(new Tag("330","b",""));
		
		r.put("TIT", tit);

		return r;
	}

	public String getRecordTypeDescription() {
		return "Pregresso ISO2709 format.";
	}

  public void initLinkUp() {
    linkUp=null;
  }

  public void initLinkSerie() {
    linkSerie=null;
  }

  public void initLinkDown() {
    linkDown=null;
  }

  // ritorna un vettore di elementi
  // Pregresso non ha link!
  public Vector<String> getLink(String tag) {

return null;
  }

  public Vector<String> getAuthors() {
	    Vector<Tag> v=getTags("300");
	    Vector<String> r=new Vector<String>();
	    String k="";
	    if(v.size()>0) {
	      for(int i=0;i<v.size();i++) {
	        k=v.elementAt(i).getField("a").getContent();
	        r.addElement(k);
	      }
	    }
	    return r;
	  }

/**
 * 13/2/2003 - R.T.
 * Input: null
 * Output: Vector di coppie. La coppia contiene nel primo elemento una Coppia con
 *         il codice della biblioteca e il nome della biblioteca
 *         e nel secondo elemento un Vector che contiene
 *         coppie (collocazione,inventario).
 *         Se serve anche il polo ci lo mettiamo nel codice della biblioteca?
 * 24/6/2003 - R.T.
 *         Deve trasportare anche la consistenza, nel caso sia un periodico.
 *         Ciascun elemento del vettore e' una coppia (biblioteca,dati).
 *         Biblioteca e' una coppia (codice_biblioteca, nome_biblioteca).
 *         I dati sono una coppia (collocazione, inventario)
 *         Collocazione e' una coppia (collocazione, sintesi_posseduto)
 *         Inventario e' una String
 *         Se non e' un periodico sintesi_posseduto sara' null o String vuota.
 */
  public Vector<BookSignature> getSignatures() {
	  Tag t002=getFirstTag("002");
	  Field f002=null;
	  if(t002!=null) f002=t002.getField("n");
	  
	  
    String s="";
    
    if(f002!=null) s=f002.getContent();
    Vector<BookSignature> r=new Vector<BookSignature>();
    s=s.replaceAll("html","TIF");
    Tag t=getFirstTag("330");
    if(t!=null) {
	    Field number=t.getField("a");
	    Field localization=t.getField("b");
	    
	    r.addElement(new BookSignature(s, "Catalogo Pregresso per autore", 
	    		number!=null?number.getContent():"", 
	    		localization!=null?localization.getContent():""));
    }
  	return r;
  }
  
  public void clearSignatures() throws JOpac2Exception {
  	this.removeTags("002");
  	this.removeTags("330");
  }

  public String getTitle() {
	  String r="";
	  Tag t310=getFirstTag("310");
	  Field a310=null;
	  if(t310!=null) a310=t310.getField("a");
	  
	 if(a310!=null) {
		 r=a310.getContent();
	 }
//  	String r=getFirstTag("310"); //getFirstElement(getFirstTag("310"),"a");
  	
  	if(r.length()==0)  {
  		Tag t100=getFirstTag("100");
  		Field a100=null;
  		if(t100!=null) a100=t100.getField("a");
  		if(a100!=null) r=a100.getContent(); 
  	}
  	// r=getFirstTag("100");
    
  	//r=r.substring(r.indexOf(";a"));
    r=r.replaceAll("<p>","\n");
    r=r.replaceAll("|","");
    //r=r.replaceAll(";a","");
/*    r=r.replaceAll("&","&amp;");
    r=r.replaceAll("<", "&lt;");
    r=r.replaceAll(">", "&gt;");*/

    return r;
  }

  public String getISBD() {
    String r=getTitle();
    Tag t311=getFirstTag("311");
    Field a311=null;
    if(t311!=null) a311=t311.getField("a");
    String k=null;
    if(a311!=null) k=a311.getContent();
    if(k!=null && k.length()>0) r=r+" / "+k;
    return r;
  }

  public Pregresso() {
	  descrizioneTipo = "Pregresso";
  }

/* (non-Javadoc)
 * @see JOpac2.dataModules.ISO2709#getDescription()
 */
public String getDescription() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getAbstract() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Vector<ClassificationInterface> getClassifications() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getEdition() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Vector<String> getEditors() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Vector<RecordInterface> getLinked(String tag) throws JOpac2Exception {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getPublicationDate() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getPublicationPlace() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Vector<SubjectInterface> getSubjects() {
	// TODO Auto-generated method stub
	return null;
}

public void addAuthor(String author) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addClassification(ClassificationInterface data) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addComment(String comment) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addPart(RecordInterface part) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addPartOf(RecordInterface partof) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addSerie(RecordInterface serie) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addSignature(BookSignature signature) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addSubject(SubjectInterface subject) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public String getComments() {
	// TODO Auto-generated method stub
	return null;
}

public String getStandardNumber() {
	// TODO Auto-generated method stub
	return null;
}

public void setAbstract(String abstractText) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setDescription(String description) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setEdition(String edition) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setISBD(String isbd) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setPublicationDate(String publicationDate) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setPublicationPlace(String publicationPlace) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setStandardNumber(String standardNumber, String codeSystem) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setTitle(String title) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setTitle(String title, boolean significant) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addPublisher(String publisher) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public BufferedImage getImage() {
	// TODO Auto-generated method stub
	return null;
}

public String getLanguage() {
	// TODO Auto-generated method stub
	return null;
}

public void setLanguage(String language) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

@Override
public void setImage(BufferedImage image, int maxx, int maxy) {
	// TODO Auto-generated method stub
	
}

@Override
public String getBase64Image() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getAvailabilityAndOrPrice() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void setAvailabilityAndOrPrice(String availabilityAndOrPrice)
		throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

@Override
public String getPublisherName() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void setPublisherName(String publisherName) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}


@Override
public String getRecordModificationDate() {
	// TODO Auto-generated method stub
	return null;
}


@Override
public void setRecordModificationDate(String date) {
	// TODO Auto-generated method stub
	
}
}