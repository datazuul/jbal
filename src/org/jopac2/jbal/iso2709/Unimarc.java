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


/**
 * Ref.: http://www.ifla.org/VI/3/p1996-1/sec-uni.htm
 */

/*
* @author	Albert Caramia
* @version ??/??/2002
* 
* @author	Romano Trampus
* @version ??/??/2002
*/

import java.util.*;
//import java.lang.*;
//import java.sql.*;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.*;

public abstract class Unimarc extends ISO2709Impl {
	public static String MONOGRAFIA="M";
	public static String COLLANA="C";

	public void clearSignatures() throws JOpac2Exception {
		removeArea("9xx");
	}
	
	public String getComments() {
		Tag comment=getFirstTag("300");
		String r="";
		if(comment!=null) r=comment.getField("a").getContent();
		return r;
	}

public void initLinkUp() {
    try {
		linkUp=getLinked("461");
	} catch (JOpac2Exception e) {
		e.printStackTrace();
	}
  }

  public void initLinkSerie() {
    try {
		linkSerie=getLinked("410");
	} catch (JOpac2Exception e) {
		e.printStackTrace();
	}
  }

  public void initLinkDown() {
    try {
		linkDown=getLinked("463");
	} catch (JOpac2Exception e) {
		e.printStackTrace();
	}
  }
  

  public Vector<RecordInterface> getLinked(String tag) throws JOpac2Exception {
    Vector<Tag> v=getTags(tag);
    Vector<RecordInterface> r=new Vector<RecordInterface>();
    
    
    try {
      if(v.size()>0) { // se il vettore ha elementi, allora faro' almeno una query
        for(int i=0;i<v.size();i++) {
        	RecordInterface not=RecordFactory.buildRecord(0,v.elementAt(i).toString(),this.getTipo(),this.getLivello());
          //ISO2709 not=ISO2709.creaNotizia(0,(String)v.elementAt(i),this.getTipo(),this.getLivello());
        	if(tag.equals("410")) {
        		not.removeTags("410");
        		not.setBiblioLevel(Unimarc.COLLANA); // Cosi' è giusto ma controllare il nome del campo!! Non e' il tipo?!
        		Vector<Tag> d=not.getTags("200");
        		for(int j=0;d!=null && j<d.size();j++) {
        			d.elementAt(j).removeField("v"); // l'indicazione del volume non deve essere nel record di collana
        		}
        	}
    	  r.addElement(not);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return r;
  }

    public Vector<String> getAuthors() {
    Vector<Tag> v=getTags("700");
    v.addAll(getTags("701"));
    v.addAll(getTags("702"));
    Vector<String> r=new Vector<String>();
    String k="";
    if(v.size()>0) {
      for(int i=0;i<v.size();i++) {
        k=v.elementAt(i).getField("a").getContent();
        k+=" "+v.elementAt(i).getField("b").getContent();
        if(!r.contains(k)) r.addElement(k);
      }
    }
    return r;
  }

  public String getTitle() {
    String r;
    Tag tag=getFirstTag("200");
    r=quote(tag.getField("a").getContent());
    r+=Utils.ifExists(" : ",tag.getField("e").getContent());
    return r;
  }

  public String getISBD() {
    String r;
    r=getTitle();
    Tag tag=getFirstTag("200");
    r+=tag.getField("d").getContent();
    r+=Utils.ifExists(" : ",tag.getField("e").getContent());
    r+=Utils.ifExists(" / ",tag.getField("f").getContent());
    r+=Utils.ifExists(" ; ",tag.getField("g").getContent());

    tag=getFirstTag("205");
    r+=Utils.ifExists(". - ",tag.getField("a").getContent());
    r+=Utils.ifExists(" ; ",tag.getField("b").getContent());

    tag=getFirstTag("210");
    r+=Utils.ifExists(". - ",tag.getField("a").getContent());
    r+=Utils.ifExists(" : ",tag.getField("c").getContent());
    r+=Utils.ifExists(" , ",tag.getField("d").getContent());


    tag=getFirstTag("215");
    r+=Utils.ifExists(". - ",tag.getField("a").getContent());
    r+=Utils.ifExists(" : ",tag.getField("c").getContent());
    r+=Utils.ifExists(" ; ",tag.getField("d").getContent());
    r+=Utils.ifExists(" + ",tag.getField("e").getContent());

    tag=getFirstTag("300");
    r+=Utils.ifExists(". - ",tag.getField("a").getContent());

    tag=getFirstTag("010");
    r+=Utils.ifExists(". - ",tag.getField("a").getContent());

    return quote(r);
  }
  
  
//TODO per questo tipo trovare  l'abstract
  public String getAbstract() {
  	return null;
  }
  
  /*
   * TODO da implementare getEdition
   */
  public String getEdition() {return null;}
  
  /* 
   * TODO:Prendere fuori il BID giustoe esiste?
   * @see JOpac2.dataModules.ISO2709#getBid()
   */
  public String getBid() {
  	if(super.getBid()==null) {
  		return Long.toString(getJOpacID());
  	}
  	else {
  		return super.getBid();
  	}
  }
  
  
	public Vector<String> getSubjects() {
		Vector<String> r=new Vector<String>();
		Vector<Tag> t=getTags("610");
		for(int i=0;t!=null && i<t.size();i++) {
			Field s=t.elementAt(i).getField("a");
			if(s!=null) r.addElement(s.getContent());
		}
		return r;
	}

	public Vector<String> getClassifications() {
		Vector<String> r=new Vector<String>();
		Vector<Tag> t=getTags("676");
		for(int i=0;t!=null && i<t.size();i++) {
			Field s=t.elementAt(i).getField("a");
			if(s!=null) r.addElement(s.getContent());
		}
		return r;
	}

	public Vector<String> getEditors() {
		Vector<String> r=new Vector<String>();
		Vector<Tag> t=getTags("210");
		for(int i=0;t!=null && i<t.size();i++) {
			Field s=t.elementAt(i).getField("c");
			if(s!=null) r.addElement(s.getContent());
		}
		return r;
	}

	public String getPublicationPlace() {
		Tag tag=getFirstTag("210");
		return tag==null?"":tag.getField("a").getContent();
	}


  public String getPublicationDate() {
  	
    Vector<Tag> v = getTags("210");

    String k="";
    if(v.size()>0) {      
        k=v.elementAt(0).getField("d").getContent();
    }
    k = k.trim();
    
    while(k.matches("[\\D].+")){
    	k = k.substring(1);
    }
    while(k.matches(".+[\\D]")){
    	k = k.substring(0,k.length()-1);
    }
    
    if(k.matches("[\\d]+[\\D]+[\\d]+"))   
    	k = k.substring(0,4);
    
    return k;
  }
  
  /**
   * 10,11,13, 15, 20, 22, 35^a
   */
	public String getStandardNumber() {
		Tag tag=getFirstTag("010");
		if(tag==null) tag=getFirstTag("011");
		if(tag==null) tag=getFirstTag("013");
		if(tag==null) tag=getFirstTag("015");
		if(tag==null) tag=getFirstTag("020");
		if(tag==null) tag=getFirstTag("022");
		if(tag==null) tag=getFirstTag("035");
		return tag==null?"":tag.getField("a").getContent();
	}

  /**
   * 215  ^a : ^c ; ^d + ^e
   */
	  public String getDescription() {
		  Tag tag=getFirstTag("215");
		  String r="";
		  r+=Utils.ifExists("",tag.getField("a").getContent());
		    r+=Utils.ifExists(" : ",tag.getField("c").getContent());
		    r+=Utils.ifExists(" ; ",tag.getField("d").getContent());
		    r+=Utils.ifExists(" + ",tag.getField("e").getContent());
		  	return r;
		  }
  
	public void setTitle(String title, boolean significant)  throws JOpac2Exception {
		char s='1';
		if(!significant) s='0';
		removeTags("200");
		Tag t=new Tag("200",s,' ');
		t.addField(new Field("a",title));
		addTag(t);
	}
	
	public void setTitle(String title)  throws JOpac2Exception {
		setTitle(title,true);
	}

	public void addAuthor(String author)  throws JOpac2Exception {
		/**
		 *  in 200 aggiungere ^f per il primo e successivamente ^g (ripetibile)
		 *  700 ^a Non ripetibile
		 *      Indicator 1: blank (not defined)
		 *      Indicator 2: Form of Name Indicator
		 *            0 Name entered under forename or direct order
         *            1 Name entered under surname (family name, patronymic, etc.)
		 *  701 come 700 ma ripetibile
		 */
		
		// 1. determina se esiste 200^f. Se esiste aggiungi in ^g, altrimenti in ^f
		Tag tag200=getFirstTag("200");
		Field auth=new Field("f",author);
		Field mr=tag200.getField("f");
		if(mr!=null) {
			auth.setFieldCode("g");
		}
		tag200.addField(auth);
		removeTags("200");
		addTag(tag200);
		
		// 2. determina se esiste già il campo 700. Se non esiste lo crea, altrimenti crea 701
		Tag tag=getFirstTag("700");
		if(tag==null) {
			tag=new Tag("700",' ','1');
			tag.setField(new Field("a",author));
		}
		else {
			tag=new Tag("701",' ','1');
			tag.setField(new Field("a",author));
		}
		addTag(tag);
	}

	
	/**
	 * For all: indicators blank
	 * 675 Universal Decimal Classification (UDC)
	 * 		$a Number (not repeatable)
	 * 		$v Edition (not repeatable)
	 * 		$z Language of edition (not repeatable)
	 * 676 Dewey Decimal Classification (DDC)
	 * 		$a Number (not repeatable)
	 * 		$v Edition (not repeatable)
	 * 		$z Language of edition (not repeatable)
	 * 680 Library of Congress Classification (LCC)
	 * 		$a Class number (not repeatable)
	 *		$b Book number (not repeatable)
	 * 686 Other Class Numbers (any other of the above)
	 * 		$a Class number (repeatable)
	 *		$b Book Number (repeatable)
	 * 		$c Classification Subdivision (repeatable)
	 *		$2 System Code (not repeatable)
	 */
	public void addClassification(ClassificationInterface data)  throws JOpac2Exception {
		String tag="";
		String classificationSchema=data.getClassificationName();
		if(classificationSchema.equals("UDC")) tag="675";
		else if(classificationSchema.equals("DDC")) tag="676";
		else if(classificationSchema.equals("LCC")) tag="680";
		else tag="686";
		Tag t=new Tag(tag,' ',' ');
		Vector<Field> d=data.getData();
		for(int i=0;i<d.size();i++) {
			t.addField(d.elementAt(i));
		}
		addTag(t);
	}

	/**
	 * 300^a
	 */
	public void addComment(String comment)  throws JOpac2Exception {
		Tag t=new Tag("300",' ',' ');
		t.addField(new Field("a",comment.trim()));
		addTag(t);
	}

	/**
	 * 210^c (210 non ripetibile, esistente)
	 */
	public void addPublisher(String publisher)  throws JOpac2Exception {
		Tag t=getFirstTag("210");
		if(t==null) t=new Tag("210",' ',' ');
		t.removeField("c");
		t.addField(new Field("c",publisher));
	}

	public void addPart(RecordInterface part)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void addPartOf(RecordInterface partof)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void addSerie(RecordInterface serie)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	/**
	 * 600 Personal Name Used as Subject
	 * 		Indicator 1: blank (not defined)
	 * 		Indicator 2: Form of Name Indicator
	 * 				0 Name entered under forename or in direct order
	 *				1 Name entered under surname (family name, patronymic etc.)
	 *		$a Entry Element
	 *		$b Part of Name Other than Entry Element
	 *		$c Additions to Name Other than Dates
	 * 		$d Roman Numerals
	 *		$f Dates
	 *		$g Expansion of Initials of Forename
	 *		$j Form Subdivision
	 *		$p Affiliation/address
	 *		$t Title
	 *		$j Form Subdivision
	 *		$x Topical Subdivision
	 *		$y Geographical Subdivision
	 *		$z Chronological Subdivision
	 *		$2 System Code
	 *		$3 Authority Record Number
	 * 601 Corporate Body Name Used as Subject
	 * 		Indicator 1: Meeting Indicator
	 * 			0 Corporate name
	 * 			1 Meeting
	 * 		Indicator 2: Form of Name Indicator
	 * 			0 Name in inverted order
	 * 			1 Name entered under place or jurisdiction
	 * 			2 Name entered under name in direct order
	 * 		$a Entry Element
	 * 		$b Subdivision (or name if entered under place)
	 * 		$c Addition to Name or Qualifier
	 * 		$d Number of Meeting and/or Number of Part of a Meeting
	 * 		$e Location of Meeting
	 * 		$f Date of Meeting
	 * 		$g Inverted Element
	 * 		$h Part of Name other than Entry Element and Inverted Element
	 * 		$j Form Subdivision
	 * 		$t Title (Not used. For author/title subject headings, use field 604 NAME AND TITLE USED AS SUBJECT.)
	 * 		$j Form Subdivision
	 * 		$x Topical Subdivision
	 * 		$y Geographical Subdivision
	 * 		$z Chronological Subdivision
	 * 		$2 System Code (It is recommended that subfield $2 always be present in each occurrence of the field)
	 * 		$3 Authority Record Number
	 * 602 Family Name Used as Subject
	 * 604 Name and Title Used as Subject
	 * 605 Title Used as Subject
	 * 606 Topical Name Used as Subject
	 * 607 Geographical Name Used as Subject
	 * 608 Form, Genre or Physical Characteristics Heading
	 * 610 Uncontrolled Subject Terms
	 * 615 Subject Category (Provisional)
	 * 620 Place Access
	 * 626 Technical Details Access (Electronic Resources) (Obsolete) 
	 */
	public void addSubject(SubjectInterface subject)  throws JOpac2Exception {
		String tag=subject.getTagIdentifier();
		Tag t=new Tag(tag,' ',' ');
		Vector<Field> d=subject.getData();
		for(int i=0;i<d.size();i++) {
			t.addField(d.elementAt(i));
		}
		addTag(t);
	}

	public void setAbstract(String abstractText)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void setDescription(String description)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void setEdition(String edition)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void setISBD(String isbd)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void setPublicationDate(String publicationDate)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void setPublicationPlace(String publicationPlace)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void setStandardNumber(String standardNumber) throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

  private void marcCostruttore(String stringa,String dTipo,int livello) {
    // se vengo chiamato dopo il primo oggetto, non scorro la catena
    if(livello<=1) {
      initLinkUp();
      initLinkDown();
      initLinkSerie();
    }
  }

  
  
  public Unimarc(String stringa,String dTipo) {
    super(stringa,dTipo,"0");
    this.marcCostruttore(stringa,dTipo,0);
  }

  public Unimarc(String stringa,String dTipo,String livello) {
    super(stringa,dTipo,livello);
    this.marcCostruttore(stringa,dTipo,Integer.parseInt(livello));
  }

}