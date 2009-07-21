package org.jopac2.jbal.iso2709;

import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.jbal.subject.UncontrolledSubjectTerms;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;

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
* @author	Albert Caramia
* @version ??/??/2002
* 
* @author	Romano Trampus
* @version ??/??/2002
*/

public class Marc21 extends ISO2709Impl {

	/*
	 * TODO Marc21 e' tutto da fare!!!
	 */
  public Marc21(String stringa,String dTipo) {
    super(stringa,dTipo);
  }

  public Marc21(String stringa,String dTipo,String livello) {
    super(stringa,dTipo,livello);
  }
  
	public Hashtable<String, List<Tag>> getRecordMapping() {
		return null;
	}

	public String getRecordTypeDescription() {
		return "General Marc21";
	}

  public Vector<String> getAuthors() {
    Vector<Tag> v=getTags("100");
    v.addAll(getTags("110"));
    v.addAll(getTags("111"));
    Vector<String> r=new Vector<String>();
    String k="";
    if(v.size()>0) {
      for(int i=0;i<v.size();i++) {
        k=v.elementAt(i).getField("a").getContent();
        k+=" "+v.elementAt(i).getField("b").getContent();
        k+=" "+v.elementAt(i).getField("c").getContent();
        k+=" "+v.elementAt(i).getField("d").getContent();
        r.addElement(k);
      }
    }
    return r;
  }
  
  public String getTitle() {
    String r;
    Tag tag=getFirstTag("245");
    r=quote(tag.getField("a").getContent());
    return r;
  }
  
  public String getISBD() {
    String r;
    r=getTitle();
    Tag tag=getFirstTag("245");
    r+=tag.getField("h").getContent();
    r+=tag.getField("b").getContent();
    r+=tag.getField("c").getContent();
    r+=tag.getField("n").getContent();
    r+=tag.getField("p").getContent();
    r+=tag.getField("f").getContent();
    
    r+=" .- ";
    tag=getFirstTag("260");;
    r+=tag.getField("a").getContent();
    r+=tag.getField("b").getContent();
    r+=tag.getField("c").getContent();

    r+=" .- ";
    tag=getFirstTag("300");;
    r+=tag.getField("a").getContent();
    r+=tag.getField("b").getContent();
    r+=tag.getField("c").getContent();


    return quote(r);
  }

  public String getEdition() {
  	Tag tag;
  	String r;
  	tag=getFirstTag("250");
    r=tag.getField("a").getContent();
    r+=tag.getField("b").getContent();
  	return r;
  }

  
  public String getBid() {
  	return getFirstTag("001").getRawContent();
  }
  
  // TODO per questo tipo trovare l'abstract
  public String getAbstract() {
  	return null;
  }
  public Vector<SubjectInterface> getSubjects() {
	  Vector<SubjectInterface> r=new Vector<SubjectInterface>();
		Vector<Tag> v=getTags("650");
		  for(int i=0;v!=null && i<v.size();i++) {
			  UncontrolledSubjectTerms sub=new UncontrolledSubjectTerms('0');
			  Vector<Field> s=v.elementAt(i).getFields("a");
			  for(int j=0;s!=null && j<s.size();j++) {
				  sub.setSubjectData(s.elementAt(j).getContent());
			  }
			  r.addElement(sub);
		  }
	      return r;
  }


	public String getDescription() {
		return getFirstTag("300").getRawContent();
	}

	@Override
	public Vector<ClassificationInterface> getClassifications() {
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
	public Vector<BookSignature> getSignatures() {
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

	public void clearSignatures() throws JOpac2Exception {
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

	public void setPublicationDate(String publicationDate)
			throws JOpac2Exception {
		// TODO Auto-generated method stub
		
	}

	public void setPublicationPlace(String publicationPlace)
			throws JOpac2Exception {
		// TODO Auto-generated method stub
		
	}

	public void setStandardNumber(String standardNumber, String codeSystem) throws JOpac2Exception {
		// TODO Auto-generated method stub
		
	}

	public void setTitle(String title) throws JOpac2Exception {
		// TODO Auto-generated method stub
		
	}

	public void setTitle(String title, boolean significant)
			throws JOpac2Exception {
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
}