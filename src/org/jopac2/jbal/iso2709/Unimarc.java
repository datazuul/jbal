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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import javax.imageio.ImageIO;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
//import java.lang.*;
//import java.sql.*;
import org.apache.xerces.impl.dv.util.Base64;
import org.jopac2.jbal.ElectronicResource;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.classification.DDC;
import org.jopac2.jbal.subject.CorporateBodyNameSubject;
import org.jopac2.jbal.subject.FamilyNameSubject;
import org.jopac2.jbal.subject.FormGenrePhysicalCharacteristicHeading;
import org.jopac2.jbal.subject.GeographicalNameSubject;
import org.jopac2.jbal.subject.NameAndTitleSubject;
import org.jopac2.jbal.subject.PersonalNameSubject;
import org.jopac2.jbal.subject.PlaceAccess;
import org.jopac2.jbal.subject.SubjectCategory_Provisional;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.jbal.subject.TitleSubject;
import org.jopac2.jbal.subject.TopicalNameSubject;
import org.jopac2.jbal.subject.UncontrolledSubjectTerms;
import org.jopac2.utils.*;

public abstract class Unimarc extends ISO2709Impl {
	public static String STATUS_CORRECTED_RECORD="c";
	public static String STATUS_DELETED_RECORD="d";
	public static String STATUS_NEW_RECORD="n";
	public static String STATUS_PREVIOUSLY_ISSUED_HIGHER_LEVEL_RECORD="o";
	public static String STATUS_PREVIOUSLY_ISSUED_INCOMPLETE_RECORD="p";
	
	
	public static String TYPE_LANGUAGE_MATERIALS_PRINTED="a";
	public static String TYPE_LANGUAGE_MATERIALS_MANUSCRIPT="b";
	public static String TYPE_MUSIC_SCORES_PRINTED="c";
	public static String TYPE_MUSIC_SCORES_MANUSCRIPT="d";
	public static String TYPE_CARTOGRAPHIC_MATERIALS_PRINTED="e";
	public static String TYPE_CARTOGRAPHIC_MATERIALS_MANUSCRIPT="f";
	public static String TYPE_ELECTRONIC_RESOURCE="l";
	
	public static String LEVEL_ANALYTIC="a"; // analytic (component part) bibliographic item
	public static String LEVEL_MONOGRAPHIC="m"; // monographic bibliographic item complete in one physical part or 
												// intended to be completed in a finite number of parts
	public static String LEVEL_SERIAL="s"; // serial - bibliographic item issued in successive parts and intended to be continued indefinitely
	public static String LEVEL_COLLECTION="c"; // collection - bibliographic item that is a made-up collection
	
	public static String HIERARCHICAL_UNDEFINED="#"; // hierarchical relationship undefined
	public static String HIERARCHICAL_NO="0"; //  no hierarchical relationship
	public static String HIERARCHICAL_HIGHEST="1"; // highest level record
	public static String HIERARCHICAL_BELOW="2"; // record below highest level (all levels below)
	

	public void clearSignatures() throws JOpac2Exception {
		removeArea("9xx");
	}
	
	public String getComments() {
		Tag comment=getFirstTag("300");
		String r="";
		if(comment!=null) {
			Field f=comment.getField("a");
			if(f!=null) r=f.getContent();
		}
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
  
  public void setBid(String bid)  {
			if(bid==null || bid.length()==0) return;
				  super.setBid(bid);
				  try {
				  removeTags("001");
				  Tag t=new Tag("001");
				  t.setRawContent(bid);
				  addTag(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
  }
  

  public Vector<RecordInterface> getLinked(String tag) throws JOpac2Exception {
    Vector<Tag> v=getTags(tag);
    Vector<RecordInterface> r=new Vector<RecordInterface>();
    
    
    try {
      if(v.size()>0) { // se il vettore ha elementi, allora faro' almeno una query
        for(int i=0;i<v.size();i++) {
        	RecordInterface not=RecordFactory.buildRecord("0",v.elementAt(i).toString().getBytes(),this.getTipo(),this.getLivello());
          //ISO2709 not=ISO2709.creaNotizia(0,(String)v.elementAt(i),this.getTipo(),this.getLivello());
        	if(tag.equals("410")) {
        		not.removeTags("410");
        		not.setBiblioLevel(Unimarc.LEVEL_COLLECTION);
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
    	  k="";
    	k+=Utils.ifExists("", v.elementAt(i).getField("a"));
        //k=v.elementAt(i).getField("a").getContent();
        k+=Utils.ifExists(" ", v.elementAt(i).getField("b"));
        //k+=" "+v.elementAt(i).getField("b").getContent();
        if(!r.contains(k)) r.addElement(k);
      }
    }
    return r;
  }

    /**
     * Indicator 1: Title Significance Indicator
     * 
     * This specifies whether the agency preparing the record considers that the title proper as specified in the first $a subfield deserves treatment as an access point. This corresponds to making a title added entry or treating the title as main entry under certain cataloguing codes.
     * 
     * 0 Title is not significant
     * This title does not warrant an added entry.
     * 
     * 1 Title is significant
     * An access point is to be made from this title.
     * 
     * For access points for any title other than the first occurring title proper, see RELATED FIELDS below.
     * 
     * Indicator 2: blank (not defined) 
     * 
     * 200  $a ; $a [] $b . $c = $d : $e / $f ; $g . $h ,. $i
	 * 
     */
  public String getTitle() {
    String r="";
    Tag tag=getFirstTag("200");
    if(tag!=null) {
    Vector<Field> a=tag.getFields("a");
	    if(a!=null && a.size()>0) {
		    r=a.elementAt(0).getContent();
		    for(int i=1;i<a.size();i++) r+=" ; "+a.elementAt(i).getContent();
		    r+=Utils.ifExists(" [] ",tag.getField("b"));
		    r+=Utils.ifExists(" . ",tag.getField("c"));
		    r+=Utils.ifExists(" = ",tag.getField("d"));
		    r+=Utils.ifExists(" : ",tag.getField("e"));
		    r+=Utils.ifExists(" / ",tag.getField("f"));
		    r+=Utils.ifExists(" ; ",tag.getField("g"));
		    r+=Utils.ifExists(" . ",tag.getField("h"));
		    r+=Utils.ifExists(" , ",tag.getField("i"));
	    }
    }
    return Field.printableNSBNSE(r);
  }

  public String getISBD() {
    String r;
    r=getTitle();

    
    r+=getEdition();

    
    Tag tag=getFirstTag("210");
    if(tag!=null) {
	    r+=Utils.ifExists(". - ",tag.getField("a"));
	    r+=Utils.ifExists(" : ",tag.getField("c"));
	    r+=Utils.ifExists(" , ",tag.getField("d"));
    }


    tag=getFirstTag("215");
    if(tag!=null) {
	    r+=Utils.ifExists(". - ",tag.getField("a"));
	    r+=Utils.ifExists(" : ",tag.getField("c"));
	    r+=Utils.ifExists(" ; ",tag.getField("d"));
	    r+=Utils.ifExists(" + ",tag.getField("e"));
    }

    tag=getFirstTag("300");
    if(tag!=null) {
    	r+=Utils.ifExists(". - ",tag.getField("a"));
    }
    
    tag=getFirstTag("010");
    if(tag!=null) {
    	r+=Utils.ifExists(". - ",tag.getField("a"));
    }

    return quote(r);
  }
  

  /**
   * 330^a
   */
  public String getAbstract() {
	  String r=null;
	  Field a=null;
	  Tag t=getFirstTag("330");
	  if(t!=null) a=t.getField("a");
	  if(a!=null) r=a.getContent();
  	return r;
  }
  
  /**
   * 205 $a = $d / $f ; $g , $b
   */
  public String getEdition() {
	  String r="";
	  Tag t=getFirstTag("205");
	  if(t!=null) {
		  Field a=t.getField("a");
		  Field d=t.getField("d");
		  Field f=t.getField("f");
		  Field g=t.getField("g");
		  Field b=t.getField("b");
		  r+=Utils.ifExists("", a);
		  r+=Utils.ifExists(" = ", d);
		  r+=Utils.ifExists(" / ", f);
		  r+=Utils.ifExists(" ; ", g);
		  r+=Utils.ifExists(" , ", b);
	  }
	  return r;
  }
  
  /* 
   * TODO:Prendere fuori il BID giusto se esiste?
   * @see JOpac2.dataModules.ISO2709#getBid()
   */
  public String getBid() {
	  String r=null;
	  Tag t=getFirstTag("001");
	  if(t!=null) r=t.getRawContent();
	  if(r==null || r.trim().length()==0) r=super.getBid();
	  return r;
  }
  
  
	public Vector<SubjectInterface> getSubjects() {
		Vector<SubjectInterface> r=new Vector<SubjectInterface>();
		
		addSubject(r,"600",new PersonalNameSubject(true));
		addSubject(r,"601",new CorporateBodyNameSubject(' ', ' '));
		addSubject(r,"602",new FamilyNameSubject());
		addSubject(r,"604",new NameAndTitleSubject());
		addSubject(r,"605",new TitleSubject());
		addSubject(r,"606",new TopicalNameSubject(' '));
		addSubject(r,"607",new GeographicalNameSubject(' '));
		addSubject(r,"608",new FormGenrePhysicalCharacteristicHeading(' '));
		
		addSubject(r,"610",new UncontrolledSubjectTerms('0'));
		addSubject(r,"615",new SubjectCategory_Provisional());
		addSubject(r,"620",new PlaceAccess(' '));
		
		
	    return r;
	}

	private void addSubject(Vector<SubjectInterface> r, String tag,
			SubjectInterface subject) {
		
		Vector<Tag> v=getTags(tag);
		  for(int i=0;v!=null && i<v.size();i++) {
			  SubjectInterface sub=subject.clone();
			  sub.setData(v.elementAt(i));
			  r.addElement(sub);
		  }
		
	}

	public Vector<ClassificationInterface> getClassifications() {
		Vector<ClassificationInterface> r=new Vector<ClassificationInterface>();
		Vector<Tag> t=getTags("676");
		for(int i=0;t!=null && i<t.size();i++) {
			/**
			 * 		$a Number (not repeatable)
			 *      $c Description // dedotto da sebina
			 * 		$v Edition (not repeatable)
			 * 		$z Language of edition (not repeatable)
			 */
			Field num=t.elementAt(i).getField("a");
			Field desc=t.elementAt(i).getField("c");
			Field ed=t.elementAt(i).getField("v");
			Field lan=t.elementAt(i).getField("z");
			
			if(num!=null) {
				r.addElement(new DDC(num!=null?num.getContent():null,
						desc!=null?desc.getContent():null,
						ed!=null?ed.getContent():null,
						lan!=null?lan.getContent():null));
			}
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
		return tag==null?"":Utils.ifExists("", tag.getField("a"));
	}


  public String getPublicationDate() {
  	
    Vector<Tag> v = getTags("210");

    String k="";
    if(v.size()>0) {      
        k=Utils.ifExists("", v.elementAt(0).getField("d"));
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
		
//		if(codeSystem.equals("ISBN")) tagName="010";
//		else if(codeSystem.equals("ISSN")) tagName="011";
//		else if(codeSystem.equals("ISMN")) tagName="013";
//		else if(codeSystem.equals("ISRN")) tagName="015";
//		else if(codeSystem.equals("NBN")) tagName="020";
//		else if(codeSystem.equals("GPN")) tagName="022";
		
		String code = "ISBN";
		String value=getValue("010","a");
		
		if(value==null) {value=getValue("011","a");code="ISSN";}
		if(value==null) {value=getValue("013","a");code="ISMN";}
		if(value==null) {value=getValue("015","a");code="ISRN";}
		if(value==null) {value=getValue("020","a");code="NBN";}
		if(value==null) {value=getValue("022","a");code="GPN";}
		if(value==null) {value=getValue("035","a");code ="NA";}
		if(value!=null && value.length()>0)value = code + " " + value;
		return value==null?"":value;
	}

  private String getValue(String tag, String field) {
	String r=null;
	Tag t=getFirstTag(tag);
	if(t!=null) {
		Field f=t.getField(field);
		if(f!=null) r=f.getContent();
		if(r!=null && r.trim().length()==0) r=null; 
	}
	return r;
}

/**
   * 215  ^a : ^c ; ^d + ^e
   */
	  public String getDescription() {
		  Tag tag=getFirstTag("215");
		  String r="";
		  if(tag!=null) {
			  r+=Utils.ifExists("",tag.getField("a"));
			  r+=Utils.ifExists(" : ",tag.getField("c"));
			  r+=Utils.ifExists(" ; ",tag.getField("d"));
			  r+=Utils.ifExists(" + ",tag.getField("e"));
		  }
		  	return r;
		  }
  
	/**
     * Indicator 1: Title Significance Indicator
     * 
     * This specifies whether the agency preparing the record considers that the title proper as specified in the first $a subfield deserves treatment as an access point. This corresponds to making a title added entry or treating the title as main entry under certain cataloguing codes.
     * 
     * 0 Title is not significant
     * This title does not warrant an added entry.
     * 
     * 1 Title is significant
     * An access point is to be made from this title.
     * 
     * For access points for any title other than the first occurring title proper, see RELATED FIELDS below.
     * 
     * Indicator 2: blank (not defined) 
     * 
     * 200  $a ; $a [] $b . $c = $d : $e / $f ; $g . $h ,. $i
	 */
	public void setTitle(String title, boolean significant)  throws JOpac2Exception {
		if(title==null || title.length()==0) return;
		
		Field i=null, h=null, e=null, d=null, c=null, b=null; // a ripetibile, f+g ripetibile
		Vector<Field> a=new Vector<Field>(), f=new Vector<Field>();
		char s='1';
		if(!significant) s='0';
		removeTags("200");
		Tag t=new Tag("200",s,' ');
				
		if(title.contains(" , ")) {
			int j=title.indexOf(" , ");
			String k=title.substring(j+3);
			title=title.substring(0,j);
			i=new Field("i",k);
		}
		if(title.contains(" . ")) {
			int j=title.lastIndexOf(" . ");
			String k=title.substring(j+3);
			if(k.length()<20) {
				title=title.substring(0,j);
				if(i==null) i=new Field("i",k);
				else h=new Field("h",k);
			}
		}
		if(title.contains(" / ")) {
			int j=title.indexOf(" / ");
			String k=title.substring(j+3);
			title=title.substring(0,j);
			String[] p=k.split(" ; ");
			f.addElement(new Field("f",p[0]));
			for(int z=1;z<p.length;z++) f.addElement(new Field("g",p[z]));
		}
		if(title.contains(" : ")) {
			int j=title.indexOf(" : ");
			String k=title.substring(j+3);
			title=title.substring(0,j);
			e=new Field("e",k);
		}
		if(title.contains(" = ")) {
			int j=title.indexOf(" = ");
			String k=title.substring(j+3);
			title=title.substring(0,j);
			d=new Field("d",k);
		}
		if(title.contains(" . ")) {
			int j=title.indexOf(" . ");
			String k=title.substring(j+3);
			title=title.substring(0,j);
			c=new Field("c",k);
		}
		if(title.contains(" [] ")) {
			int j=title.indexOf(" [] ");
			String k=title.substring(j+3);
			title=title.substring(0,j);
			b=new Field("b",k);
		}
		String[] p=title.split(" ; ");
		for(int z=0;z<p.length;z++) a.addElement(new Field("a",p[z]));
		
		for(int z=0;z<a.size();z++) t.addField(a.elementAt(z));
		if(b!=null) t.addField(b);
		if(c!=null) t.addField(c);
		if(d!=null) t.addField(d);
		if(e!=null) t.addField(e);
		for(int z=0;z<f.size();z++) t.addField(f.elementAt(z));
		if(h!=null) t.addField(h);
		if(i!=null) t.addField(i);
		addTag(t);
	}
	
	public void setTitle(String title)  throws JOpac2Exception {
		setTitle(title,true);
	}
	
	

	@Override
	public void addAuthorsFromTitle() throws JOpac2Exception {
		Tag t=getFirstTag("200"); // prende tag titolo
		Vector<Field> v=new Vector<Field>(); // vettore contenente le responsabilita'
		Field f=t.getField("f"); // prima responsabilita'
		if(f!=null) { // se non c'è la prima responsabilita', non puo' averne altre
			v.add(f);
			v.addAll(t.getFields("g")); // responsabilita' successive
			for(int i=0;i<v.size();i++) addAuthor(v.elementAt(i).getContent()); // imposta gli autori
		}
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
		if(author==null || author.length()==0) return;
		
		 // 1. determina se esiste 200^f. Se esiste aggiungi in ^g, altrimenti in ^f
				Tag tag200=getFirstTag("200");
				if(tag200==null) tag200=new Tag("200",' ',' ');
				Field auth=new Field("f",author);
				Field mr=tag200.getField("f");
				if(mr!=null && !mr.getContent().equals(author)) {
					auth.setFieldCode("g");
					tag200.addField(auth);
				}
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

	
	public void addClassification(ClassificationInterface data)  throws JOpac2Exception {
		if(data==null) return;
		
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
	@Override
	public void addComment(String comment)  throws JOpac2Exception {
		if(comment==null || comment.length()==0) return;
		Tag t=new Tag("300",' ',' ');
		t.addField(new Field("a",comment.trim()));
		addTag(t);
	}
	
	@Override
	public String getPublisherName() {
		Tag tag=getFirstTag("210");
		String r="";
		if(tag!=null) {
			r+=Utils.ifExists("",tag.getField("c"));
		}
		return r;
	}

	@Override
	public void setPublisherName(String publisherName) throws JOpac2Exception {
		if(publisherName==null || publisherName.length()==0) return;
		
		Tag p=getFirstTag("210");
		if(p==null) p=new Tag("210");
		p.removeField("c");
		p.addField(new Field("c",publisherName));
		removeTags("210");
		addTag(p);
	}

	/**
	 * TODO questa implementazione e' scorretta e assume che il Manufacture non sia mai presente!
	 *      (non riempie mai $e $f $g $h)
	 * TODO sbaglia anche l'indirizzo ($b) perché in ISBD non c'e' un separatore :-(
	 *      (codice commentato)
	 * 210  $a ; $a $b : $c , $d  $e $e $f : $g , $h 
	 * Indicators
	 * 		Indicator 1: blank (not defined)
	 * 		Indicator 2: blank (not defined)
	 * $a Place of Publication, Distribution, etc.
	 * $b Address of Publisher, Distributor, etc.
	 * $c Name of Publisher, Distributor, etc.
	 * $d Date of Publication, Distribution, etc.
	 * 
	 * $e Place of Manufacture (if present)
	 * $f Address of Manufacturer (if present)
	 * $g Name of Manufacturer (if present)
	 * $h Date of Manufacture (if present)
	 */
	public void addPublisher(String publisher)  throws JOpac2Exception {
		if(publisher==null || publisher.length()==0) return;
		removeTags("210");
		String k=null;
		Tag t=new Tag("210",' ',' ');
		Field a=null, b=null, c=null, d=null;
		if(publisher.contains(" , ")) {
			int i=publisher.indexOf(" , ");
			k=publisher.substring(i+3);
			publisher=publisher.substring(0,i);
			d=new Field("d",k);
		}
		if(publisher.contains(" : ")) {
			int i=publisher.indexOf(" : ");
			k=publisher.substring(i+3);
			publisher=publisher.substring(0,i);
			c=new Field("c",k);
		}
//		if(publisher.contains(" ; ")) {
//			int i=publisher.indexOf(" ; ");
//			k=publisher.substring(i+3);
//			publisher=publisher.substring(0,i);
//			b=new Field("b",k);
//		}
		String[] fa=publisher.split(" ; ");
		
		if(fa.length>0) {
			for(int y=0;y<fa.length;y++) t.addField(new Field("a",fa[y]));
		}
		
		if(b!=null) t.addField(b);
		if(c!=null) t.addField(c);
		if(d!=null) t.addField(d);
		addTag(t);
	}

	/**
	 * 463
	 */
	public void addPart(RecordInterface part)  throws JOpac2Exception {
		if(part!=null) {
			Tag tag=new Tag("463");
			tag.setRawContent(part.toEncapsulatedRecordFormat());
			addTag(tag);
		}
	}

	/**
	 * 
	 * 461
	 */
	public void addPartOf(RecordInterface partof)  throws JOpac2Exception {
		if(partof!=null) {
			Tag tag=new Tag("461");
			tag.setRawContent(partof.toEncapsulatedRecordFormat());
			addTag(tag);
		}
	}

	/**
	 * http://www.ifla.org/VI/3/p1996-1/uni4.htm#410
	 * EX 1 Embedded fields technique
	 * 225 0#$aLetters from China
	 * 410 #0$12001#$aLetters from China$1700#1$aStrong,$bAnna Louise,$f1885-1970 
	 */
	public void addSerie(RecordInterface serie)  throws JOpac2Exception {
		if(serie!=null) {
			Tag tag225=new Tag("225",' ','0');
			Vector<Tag> tit=serie.getTags("200");
			if(tit!=null && tit.size()>0) {
				Field t=tit.elementAt(0).getField("a");
				if(t!=null) {
					tag225.addField(new Field("a",t.getContent()));
				}
				addTag(tag225);
			}
			Tag tag410=new Tag("410");
			tag410.setRawContent(serie.toEncapsulatedRecordFormat());
			addTag(tag410);
		}
	}

	public void addSubject(SubjectInterface subject)  throws JOpac2Exception {
		if(subject==null) return;
		
		String tag=subject.getTagIdentifier();
		Tag t=new Tag(tag,' ',' ');
		Vector<Field> d=subject.getData();
		for(int i=0;i<d.size();i++) {
			t.addField(d.elementAt(i));
		}
		addTag(t);
	}

	/**
	 * 330^a
	 */
	public void setAbstract(String abstractText)  throws JOpac2Exception {
		if(abstractText==null || abstractText.length()==0) return;
		
		Tag a=new Tag("330",' ',' ');
		a.addField(new Field("a",abstractText));
		addTag(a);
	}

	/**
    * 215  ^a : ^c ; ^d + ^e
    * Indicators
    * 	Indicator 1: blank (not defined)
    * 	Indicator 2: blank (not defined)
    * $a Specific Material Designation and Extent of Item
    * $c Other Physical Details
    * $d Dimensions
    * $e Accompanying Material
    */
	public void setDescription(String description)  throws JOpac2Exception {
		if(description==null || description.length()==0) return;
		String k="";
		Tag t=new Tag("215",' ',' ');
		Field a=null, c=null, d=null, e=null;
		if(description.contains(" + ")) {
			int i=description.indexOf(" + ");
			k=description.substring(i+3);
			description=description.substring(0,i);
			e=new Field("e",k);
		}
		if(description.contains(" ; ")) {
			int i=description.indexOf(" ; ");
			k=description.substring(i+3);
			description=description.substring(0,i);
			d=new Field("d",k);
		}
		if(description.contains(" : ")) {
			int i=description.indexOf(" : ");
			k=description.substring(i+3);
			description=description.substring(0,i);
			c=new Field("c",k);
		}
		a=new Field("a",description);
		
		if(a!=null) t.addField(a);
		if(c!=null) t.addField(c);
		if(d!=null) t.addField(d);
		if(e!=null) t.addField(e);
		addTag(t);
	}
	
	/**
	 * 
     * 205 $a = $d / $f ; $g , $b
     * Indicators
     * 		Indicator 1: blank (not defined)
     * 		Indicator 2: blank (not defined)
     * $a Edition Statement
     * $b Issue Statement
     * $d Parallel Edition Statement
     * $f Statement of Responsibility Relating to Edition
     * $g Subsequent Statement of Responsibility
     *
	 */
	public void setEdition(String edition)  throws JOpac2Exception {
		if(edition==null || edition.length()==0) return;
		
		Tag e=new Tag("205",' ',' ');
		Field a=null, d=null, f=null, g=null, b=null;
		
		if(edition.contains(" , ")) {
			int i=edition.indexOf(" , ");
			String k=edition.substring(i+3);
			edition=edition.substring(0,i);
			b=new Field("b",k);
		}
		if(edition.contains(" ; ")) {
			int i=edition.indexOf(" ; ");
			String k=edition.substring(i+3);
			edition=edition.substring(0,i);
			g=new Field("g",k);
		}
		if(edition.contains(" / ")) {
			int i=edition.indexOf(" / ");
			String k=edition.substring(i+3);
			edition=edition.substring(0,i);
			f=new Field("f",k);
		}
		if(edition.contains(" = ")) {
			int i=edition.indexOf(" = ");
			String k=edition.substring(i+3);
			edition=edition.substring(0,i);
			d=new Field("d",k);
		}
		a=new Field("a",edition);
		
		if(a!=null) e.addField(a);
		if(d!=null) e.addField(d);
		if(f!=null) e.addField(f);
		if(g!=null) e.addField(g);
		if(b!=null) e.addField(b);
		addTag(e);
	}

	public void setISBD(String isbd)  throws JOpac2Exception {
		throw new JOpac2Exception("ERROR: Use single setTitle(), setEdition, ....");
	}

	/**
	 * 210 $d
	 */
	public void setPublicationDate(String publicationDate)  throws JOpac2Exception {
		if(publicationDate==null || publicationDate.length()==0) return;
		
		Tag p=getFirstTag("210");
		if(p==null) p=new Tag("210",' ',' ');
		p.removeField("d");
		p.addField(new Field("d",publicationDate));
		removeTags("210");
		addTag(p);
	}

	/**
	 * 210 $a
	 */
	public void setPublicationPlace(String publicationPlace)  throws JOpac2Exception {
		if(publicationPlace==null || publicationPlace.length()==0) return;
		
		Tag p=getFirstTag("210");
		if(p==null) p=new Tag("210",' ',' ');
		p.removeField("a");
		p.addField(new Field("a",publicationPlace));
		removeTags("210");
		addTag(p);
	}

	/**
	 * 
	 * @param standardNumber
	 * @param codeSystem
	 * @throws JOpac2Exception
	 */
	public void setStandardNumber(String standardNumber, String codeSystem) throws JOpac2Exception {
		if(standardNumber==null || codeSystem==null || standardNumber.length()==0 || codeSystem.length()==0) return;
		
		String tagName="035";
		if(codeSystem.equals("ISBN")) tagName="010";
		else if(codeSystem.equals("ISSN")) tagName="011";
		else if(codeSystem.equals("ISMN")) tagName="013";
		else if(codeSystem.equals("ISRN")) tagName="015";
		else if(codeSystem.equals("NBN")) tagName="020";
		else if(codeSystem.equals("GPN")) tagName="022";
		Tag n=getFirstTag(tagName);
		if(n==null) n=new Tag(tagName,' ',' ');
		n.removeField("a");
		if(tagName.equals("035"))
			n.addField(new Field("a","("+codeSystem+")"+standardNumber));
		else
			n.addField(new Field("a",standardNumber));

		removeTags(tagName);
		addTag(n);
	}
	
	/**
	 * 101 x$a$b$c$d$e$f$g$h$i$j
	 * Indicators
	 * 		Indicator 1: Translation indicator
	 * 			0  Item is in the original language(s) of the work
	 * 			1  Item is a translation of the original work or an intermediate work
	 * 			2  Item contains translations other than translated summaries
	 *		Indicator 2: blank (not defined)
	 * $a Language of Text, Soundtrack etc. 
	 * $b Language of Intermediate Text when Item is Not Translated from Original. 
	 * $c Language of Original Work 
	 * $d Language of Summary 
	 * $e Language of Contents Page 
	 * $f Language of Title Page if Different from Text 
	 * $g Language of Title Proper if Not First Language of Text, Soundtrack, etc. 
	 * $h Language of Libretto, etc. 
	 * $i Language of Accompanying Material (Other than Summaries, Abstracts or Librettos) 
	 * $j Language of Subtitles 
	 */
	public String getLanguage() {
		String r="";
		Tag t=getFirstTag("101");
		r+=Utils.ifExists("", t.getField("a"));
		r+=Utils.ifExists(" ", t.getField("b"));
		r+=Utils.ifExists(" ", t.getField("c"));
		r+=Utils.ifExists(" ", t.getField("d"));
		r+=Utils.ifExists(" ", t.getField("e"));
		r+=Utils.ifExists(" ", t.getField("f"));
		r+=Utils.ifExists(" ", t.getField("g"));
		r+=Utils.ifExists(" ", t.getField("h"));
		r+=Utils.ifExists(" ", t.getField("i"));
		r+=Utils.ifExists(" ", t.getField("j"));
		return r;
	}
	
	/**
	 * Simply split codes and put
	 * 101 x$a$b$c$d$e$f$g$h$i$j
	 * Indicators
	 * 		Indicator 1: Translation indicator
	 * 			0  Item is in the original language(s) of the work
	 * 			1  Item is a translation of the original work or an intermediate work
	 * 			2  Item contains translations other than translated summaries
	 *		Indicator 2: blank (not defined)
	 * $a Language of Text, Soundtrack etc. 
	 * $b Language of Intermediate Text when Item is Not Translated from Original. 
	 * $c Language of Original Work 
	 * $d Language of Summary 
	 * $e Language of Contents Page 
	 * $f Language of Title Page if Different from Text 
	 * $g Language of Title Proper if Not First Language of Text, Soundtrack, etc. 
	 * $h Language of Libretto, etc. 
	 * $i Language of Accompanying Material (Other than Summaries, Abstracts or Librettos) 
	 * $j Language of Subtitles 
	 * @throws JOpac2Exception 
	 */
	public void setLanguage(String language) throws JOpac2Exception {
		if(language==null || language.length()==0) return;
		String[] l=language.split(" ");
		removeTags("101");
		Tag t=new Tag("101",' ',' ');
		if(l.length>1) t.setModifier1('1'); // if more than 1 lang assume is translated
		else t.setModifier1('0');
		for(int i=0;i<l.length;i++) {
			String f=Character.toString((char)(97+i)); // starting form 'a'
			t.addField(new Field(f,l[i]));
		}
		addTag(t);
	}

  private void marcCostruttore(byte[] stringa,Charset charset, String dTipo,int livello) {
    // se vengo chiamato dopo il primo oggetto, non scorro la catena
    if(livello<=1) {
      initLinkUp();
      initLinkDown();
      initLinkSerie();
    }
  }

  
  
  public Unimarc(byte[] stringa, Charset charset, String dTipo)  throws Exception {
    super(stringa,charset,dTipo,"0");
    this.marcCostruttore(stringa,charset,dTipo,0);
  }

  public Unimarc(byte[] stringa, Charset charset, String dTipo,String livello)  throws Exception {
    super(stringa,charset,dTipo,livello);
    this.marcCostruttore(stringa,charset,dTipo,Integer.parseInt(livello));
  }

  @Override
	public String getAvailabilityAndOrPrice() {
		String ret = null;
		Tag tag = getFirstTag("010");
		if(tag!=null){
			Field f = tag.getField("d");
			if(f!=null)
				ret = f.getContent();
		}
		return ret;
	}
	
	@Override
	public void setAvailabilityAndOrPrice(String availabilityAndOrPrice) throws JOpac2Exception {
		Tag t=getFirstTag("010");
		if(t==null) t=new Tag("010");
		t.removeField("d");
		t.addField(new Field("d",availabilityAndOrPrice));
		removeTags("010");
		addTag(t);
	}
  
	@Override
	public Hashtable<String, List<Tag>> getRecordMapping() {
		Hashtable<String, List<Tag>> r=new Hashtable<String, List<Tag>>();
		
		List<Tag> aut=new Vector<Tag>();
		aut.add(new Tag("200","f",""));
		aut.add(new Tag("200","g",""));
		aut.add(new Tag("700","a",""));
		aut.add(new Tag("700","b",""));
		aut.add(new Tag("700","c",""));
		aut.add(new Tag("701","a",""));
		aut.add(new Tag("701","b",""));
		aut.add(new Tag("701","c",""));
		aut.add(new Tag("702","a",""));
		aut.add(new Tag("702","b",""));
		aut.add(new Tag("702","c",""));
		r.put("AUT", aut);
		
		List<Tag> tit=new Vector<Tag>();
		tit.add(new Tag("200","a",""));
		tit.add(new Tag("200","c",""));
		tit.add(new Tag("200","d",""));
		tit.add(new Tag("200","e",""));
		tit.add(new Tag("500","a",""));
		tit.add(new Tag("510","a",""));
		tit.add(new Tag("517","a",""));
		r.put("TIT", tit);
		
		List<Tag> num=new Vector<Tag>();
		num.add(new Tag("010","a",""));
		num.add(new Tag("011","a",""));
		num.add(new Tag("013","a",""));
		r.put("NUM", num);
		
		List<Tag> lan=new Vector<Tag>();
		lan.add(new Tag("101","a",""));
		r.put("LAN", lan);
		
		List<Tag> mat=new Vector<Tag>();
		mat.add(new Tag("200","b",""));
		r.put("MAT", mat);
		
		List<Tag> dte=new Vector<Tag>();
		dte.add(new Tag("500","k",""));
		dte.add(new Tag("210","d",""));
		r.put("DTE", dte);
		
		// publication place
		List<Tag> plc=new Vector<Tag>();
		plc.add(new Tag("210","a",""));
		r.put("PLC", plc);
		
		List<Tag> sbj=new Vector<Tag>();
		sbj.add(new Tag("600","a",""));
		sbj.add(new Tag("601","a",""));
		sbj.add(new Tag("602","a",""));
		sbj.add(new Tag("604","1",""));
		sbj.add(new Tag("605","a",""));
		sbj.add(new Tag("606","a",""));
		sbj.add(new Tag("607","a",""));
		sbj.add(new Tag("610","a",""));
		r.put("SBJ", sbj);
		
		List<Tag> cll=new Vector<Tag>();
		cll.add(new Tag("410","a",""));
		r.put("CLL", cll);
		
		return r;
	}

	@Override
	public String getRecordTypeDescription() {
		return "General unimarc format.";
	}
	
	@Override
	public void setBase64Image(String base64EncodedImage)  throws JOpac2Exception {
		if(base64EncodedImage == null){
    		try {
				removeTags("911");
			} catch (JOpac2Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
    	}
        Tag t=new Tag("911",' ',' ');
        t.addField(new Field("a",base64EncodedImage));
        try {
                removeTags("911");
        } catch (JOpac2Exception e) {
        }
        addTag(t);
	}
	
	@Override
    public void setImage(BufferedImage image, int maxx, int maxy) {
    	if(image == null){
    		try {
				removeTags("911");
			} catch (JOpac2Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
    	}
            ByteArrayOutputStream a=new ByteArrayOutputStream();
               try {
                    Image im=image.getScaledInstance(maxx, maxy, Image.SCALE_SMOOTH);
                    BufferedImage dest = new BufferedImage(maxx,maxy,
                                    BufferedImage.TYPE_INT_RGB);
                    dest.createGraphics().drawImage(im, 0, 0, null);
                    ImageIO.write (dest, "jpeg", a);                        
                    String coded=Base64.encode(a.toByteArray());
                    Tag t=new Tag("911",' ',' ');
                    t.addField(new Field("a",coded));
                    try {
                            removeTags("911");
                    } catch (JOpac2Exception e) {
                    }
                    addTag(t);
                    a.reset();
            } catch (IOException e) {
                    e.printStackTrace();
            }
    }
   
	@Override
    public BufferedImage getImage() throws JOpac2Exception {
            BufferedImage r=null;
            Tag t=getFirstTag("911");
            if(t!=null) {
                    Field i=t.getField("a");
                    if(i!=null) {
                            String coded=i.getContent();
                            byte[] b=Base64.decode(coded);
                            try {
                            		if(b!=null)
                            			r=ImageIO.read(new ByteArrayInputStream(b));
                            } catch (IOException e) {
                            	throw new JOpac2Exception(e.getMessage());
                            }
                    }
            }
            return r;
    }
	
	
    
	@Override
	public void removeImage() throws JOpac2Exception {
		Tag t=getFirstTag("911");
		if(t!=null) {
			Field f=t.getField("a");
			if(f!=null) t.removeField("f");
			if(t.getFields()!=null && t.getFields().isEmpty()) removeTags("911");
		}
	}

	@Override
    public String getBase64Image() {
    	String r=null;
        Tag t=getFirstTag("911");
        if(t!=null) {
                Field i=t.getField("a");
                if(i!=null) {
                    r=i.getContent();
                }
        }
        return r;
    }
	
	
	@Override
	public void addElectronicVersion(ElectronicResource electronicResource) {
//		Indicatore 1: Modalità di accesso
//	    Nessuna informazione fornita
//	    0 Email
//	    1 FTP
//	    2 Login da remoto (Telnet)
//	    3 Dial-up
//	    4 HTTP
//	    7 Modalità specifica in $y
//		$y Metodo di accesso (non ripetibile)
		Tag resource=new Tag("856",' ',' ');
		if(!StringUtils.isBlank(electronicResource.getAccessmethod())) {
			int i=ArrayUtils.indexOf(ElectronicResource.accessType, electronicResource.getAccessmethod());
			if(i>=0) {
				resource.setModifier1((char)('0'+i));
			}
			else {
				resource.setModifier1('7');
				resource.addField(new Field("y",electronicResource.getAccessmethod()));
			}
		}
		
//	    $a Nome Host (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getHostaname())) resource.addField(new Field("a",electronicResource.getHostaname()));
		
//	    $b Numero di accesso (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getAccessnumber())) resource.addField(new Field("b",electronicResource.getAccessnumber()));

//		$c Compressione dell'informazione (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getCompression())) resource.addField(new Field("c",electronicResource.getCompression()));
		
//	    $d Percorso (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getPath())) resource.addField(new Field("d",electronicResource.getPath()));
		
//	    $e Data e orario della consultazione e dell'accesso(non ripetibile)
		if(!StringUtils.isBlank(electronicResource.getLastaccesstime())) resource.addField(new Field("e",electronicResource.getLastaccesstime()));
		
//	    $f Nome del file (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getFilename())) resource.addField(new Field("f",electronicResource.getFilename()));
		
//	    $h Processore delle richieste (non ripetibile)
		if(!StringUtils.isBlank(electronicResource.getRequestprocessor())) resource.addField(new Field("h",electronicResource.getRequestprocessor()));
		
//	    $i Istruzione (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getCommand())) resource.addField(new Field("i",electronicResource.getCommand()));
		
//	    $j Bits per second (non ripetibile)
		if(!StringUtils.isBlank(electronicResource.getBitpersecond())) resource.addField(new Field("j",electronicResource.getBitpersecond()));
		
//	    $k Password (Not ripetibile)
		if(!StringUtils.isBlank(electronicResource.getPassword())) resource.addField(new Field("k",electronicResource.getPassword()));
		
//	    $l Logon/login (non ripetibile)
		if(!StringUtils.isBlank(electronicResource.getLogin())) resource.addField(new Field("l",electronicResource.getLogin()));
		
//	    $m Contatto per l'assistenza nell'accesso (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getContact())) resource.addField(new Field("m",electronicResource.getContact()));
		
//	    $n Nome dell'ubicazione dell'host in $a (non ripetibile)
		if(!StringUtils.isBlank(electronicResource.getLocation())) resource.addField(new Field("n",electronicResource.getLocation()));
		
//	    $o Sistema operativo (non ripetibile)
		if(!StringUtils.isBlank(electronicResource.getOperatingsystem())) resource.addField(new Field("o",electronicResource.getOperatingsystem()));
		
//	    $p Porta (non ripetibile)
		if(!StringUtils.isBlank(electronicResource.getPortnumber())) resource.addField(new Field("p",electronicResource.getPortnumber()));
		
//	    $q Tipo di formato elettronico (non ripetibile)
		if(!StringUtils.isBlank(electronicResource.getMimetype())) resource.addField(new Field("q",electronicResource.getMimetype()));
		
//	    $r Impostazioni (non ripetibile)
		if(!StringUtils.isBlank(electronicResource.getSettings())) resource.addField(new Field("r",electronicResource.getSettings()));
		
//	    $s Dimensioni del file (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getSettings())) resource.addField(new Field("s",electronicResource.getSettings()));
		
//	    $t Emulazione del terminale (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getTerminalemulationsettings())) resource.addField(new Field("t",electronicResource.getTerminalemulationsettings()));
		
//	    $u Uniform Resource Locator (non ripetibile)
		if(!StringUtils.isBlank(electronicResource.getUrl())) resource.addField(new Field("u",electronicResource.getUrl()));
		
//	    $v Orario in cui è possibile l'accesso alla risorsa (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getAccesstime())) resource.addField(new Field("v",electronicResource.getAccesstime()));
		
//	    $w Numero di controllo della registrazione (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getRecordcontrolnumber())) resource.addField(new Field("w",electronicResource.getRecordcontrolnumber()));
		
//	    $x Nota non pubblica (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getNonpublicnote())) resource.addField(new Field("x",electronicResource.getNonpublicnote()));
		
//	    $z Nota pubblica (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getPublicnote())) resource.addField(new Field("z",electronicResource.getPublicnote()));
		
//	    $2 Testo del link (ripetibile)
		if(!StringUtils.isBlank(electronicResource.getLinktext())) resource.addField(new Field("2",electronicResource.getLinktext()));
		
		addTag(resource);
	}

	@Override
	public ElectronicResource[] getElectronicVersion() {
		Vector<ElectronicResource> v=new Vector<ElectronicResource>();
		
		Vector<Tag> resources=getTags("856");
		for(Tag resource:resources) {
			ElectronicResource el=convertElectronicResource(resource);
			v.addElement(el);
		}
		return v.toArray(new ElectronicResource[v.size()]);
	}
	
	private ElectronicResource convertElectronicResource(Tag resource) {
		ElectronicResource el=new ElectronicResource();
//		Indicatore 1: Modalità di accesso
//	    Nessuna informazione fornita
//	    0 Email
//	    1 FTP
//	    2 Login da remoto (Telnet)
//	    3 Dial-up
//	    4 HTTP
//	    7 Modalità specifica in $y
//		$y Metodo di accesso (non ripetibile)
			if(resource.getModifier1()<'7') {
				int i=resource.getModifier1()-'0';
				el.setAccessmethod(ElectronicResource.accessType[i]);
			}
			else if(Utils.ifExists(resource.getField("y"))) {
				el.setAccessmethod(resource.getField("y").getContent());
			}
		
//	    $a Nome Host (ripetibile)
			if(Utils.ifExists(resource.getField("a"))) el.setHostaname(resource.getField("a").getContent());
		
//	    $b Numero di accesso (ripetibile)
			if(Utils.ifExists(resource.getField("b"))) el.setAccessnumber(resource.getField("b").getContent());

//		$c Compressione dell'informazione (ripetibile)
			if(Utils.ifExists(resource.getField("c"))) el.setCompression(resource.getField("c").getContent());
		
//	    $d Percorso (ripetibile)
			if(Utils.ifExists(resource.getField("d"))) el.setPath(resource.getField("d").getContent());
			
//	    $e Data e orario della consultazione e dell'accesso(non ripetibile)
			if(Utils.ifExists(resource.getField("e"))) el.setLastaccesstime(resource.getField("e").getContent());
		
//	    $f Nome del file (ripetibile)
			if(Utils.ifExists(resource.getField("f"))) el.setFilename(resource.getField("f").getContent());
		
//	    $h Processore delle richieste (non ripetibile)
			if(Utils.ifExists(resource.getField("h"))) el.setRequestprocessor(resource.getField("h").getContent());
		
//	    $i Istruzione (ripetibile)
			if(Utils.ifExists(resource.getField("i"))) el.setCommand(resource.getField("i").getContent());
		
//	    $j Bits per second (non ripetibile)
			if(Utils.ifExists(resource.getField("j"))) el.setBitpersecond(resource.getField("j").getContent());
		
//	    $k Password (Not ripetibile)
			if(Utils.ifExists(resource.getField("k"))) el.setPassword(resource.getField("k").getContent());
		
//	    $l Logon/login (non ripetibile)
			if(Utils.ifExists(resource.getField("l"))) el.setLogin(resource.getField("l").getContent());
		
//	    $m Contatto per l'assistenza nell'accesso (ripetibile)
			if(Utils.ifExists(resource.getField("m"))) el.setContact(resource.getField("m").getContent());
		
//	    $n Nome dell'ubicazione dell'host in $a (non ripetibile)
			if(Utils.ifExists(resource.getField("n"))) el.setLocation(resource.getField("n").getContent());
		
//	    $o Sistema operativo (non ripetibile)
			if(Utils.ifExists(resource.getField("o"))) el.setOperatingsystem(resource.getField("o").getContent());
		
//	    $p Porta (non ripetibile)
			if(Utils.ifExists(resource.getField("p"))) el.setPortnumber(resource.getField("p").getContent());
		
//	    $q Tipo di formato elettronico (non ripetibile)
			if(Utils.ifExists(resource.getField("q"))) el.setMimetype(resource.getField("q").getContent());
		
//	    $r Impostazioni (non ripetibile)
			if(Utils.ifExists(resource.getField("r"))) el.setSettings(resource.getField("r").getContent());
		
//	    $s Dimensioni del file (ripetibile)
			if(Utils.ifExists(resource.getField("s"))) el.setFilesize(resource.getField("s").getContent());
		
//	    $t Emulazione del terminale (ripetibile)
			if(Utils.ifExists(resource.getField("t"))) el.setTerminalemulationsettings(resource.getField("t").getContent());
		
//	    $u Uniform Resource Locator (non ripetibile)
			if(Utils.ifExists(resource.getField("u"))) el.setUrl(resource.getField("u").getContent());
		
//	    $v Orario in cui è possibile l'accesso alla risorsa (ripetibile)
			if(Utils.ifExists(resource.getField("v"))) el.setAccesstime(resource.getField("v").getContent());
		
//	    $w Numero di controllo della registrazione (ripetibile)
			if(Utils.ifExists(resource.getField("w"))) el.setRecordcontrolnumber(resource.getField("w").getContent());
		
//	    $x Nota non pubblica (ripetibile)
			if(Utils.ifExists(resource.getField("x"))) el.setNonpublicnote(resource.getField("x").getContent());
		
//	    $z Nota pubblica (ripetibile)
			if(Utils.ifExists(resource.getField("z"))) el.setPublicnote(resource.getField("z").getContent());
		
//	    $2 Testo del link (ripetibile)
			if(Utils.ifExists(resource.getField("2"))) el.setLinktext(resource.getField("2").getContent());
		return el;
	}

	/**
	 * Rimuove confrontando l'url
	 */
	public void removeElectronicVersion(ElectronicResource electronicResource) {
		for(Tag tag:getTags("856")) {
			ElectronicResource el=convertElectronicResource(tag);
			if(el.equals(electronicResource)) removeTag(tag);
		}
	}

	/**
	 * Restituisce la versione elettronica corrispondente al type indicato.
	 */
	public ElectronicResource getElectronicVersion(String type) {
		ElectronicResource el=null;
		for(Tag tag:getTags("856")) {
			el=convertElectronicResource(tag);
			if(type.equals(el.getAccessmethod())) {
				break;
			}
			else {
				el=null;
			}
		}
		return el;
	}
	
	public String getRecordModificationDate() {
		String r="";
		Tag t=getFirstTag("005");
		if(t!=null) r=t.getRawContent();
		return r;
	}
	
	public void setRecordModificationDate(String date) throws JOpac2Exception {
		Tag t=new Tag("005");
		try {
			removeTags("005");
		} catch (JOpac2Exception e) {
		}
		t.setRawContent(date);
		addTag(t);
	}
}