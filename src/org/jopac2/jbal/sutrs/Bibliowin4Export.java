package org.jopac2.jbal.sutrs;


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
* @author	Romano Trampus
* @version 25/05/2005
*/

/**
 * TODO: R.T.: 05/06/2006 sutrs deve essere tipo a se, non inglobato in iso2709
 */

import java.awt.image.BufferedImage;
import java.util.*;

import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.iso2709.ISO2709Impl;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;

public class Bibliowin4Export extends ISO2709Impl {
	private String[] rawRecord=null;

  public Bibliowin4Export(String notizia,String dTipo,String livello) {
    this.iso2709Costruttore(notizia,dTipo,Integer.parseInt(livello));
  }
  

  public Bibliowin4Export(String notizia,String dTipo) {
    this.iso2709Costruttore(notizia,dTipo,0);
  }

  public Bibliowin4Export() {
	  super();
  }
  
  public String getLabel(String label) {
  	String r="";
  	int i=0;
  	
  	while(i<rawRecord.length) {
  		
  		i++;
  	}
  	return r;
  }
  
  
  public void init(String stringa) {
  	inString=stringa;
  	rawRecord=stringa.split("\n");
  }
	
  	public Vector<String> getAuthors() {
	    return null;
	  }
	
	public String getEdition() {
		return "";
	}

	public String getPublicationPlace() {return null;}
	
	public String getPublicationDate() {

	    
	    return "";
	}
	

	public String getTitle() {
		String r="";

		return r;
	}
	
	public String getISBD() {
	    String r=null;
	    r=getTitle();
	    
	    return quote(r);
	  }
	
	public String getComments() {
		return null;
	}
	

	public String getAbstract() {
		// TODO Auto-generated method stub
		return "";
	}
	
	public String getDescription() {
		return null;
	}
	
	public void addSignature(BookSignature signature) {

	}
		
   public Vector<BookSignature> getSignatures() {

		Vector<BookSignature> ret=new Vector<BookSignature>();
		
		return ret;
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


public void addSubject(SubjectInterface subject) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}


public void clearSignatures() throws JOpac2Exception {
	// TODO Auto-generated method stub
	
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
}