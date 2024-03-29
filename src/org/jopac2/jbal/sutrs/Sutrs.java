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
import java.nio.charset.Charset;
import java.util.*;

import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.iso2709.ISO2709Impl;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.Utils;

public class Sutrs extends ISO2709Impl {

  public int delimiterPosition=27;
  public char delimiter=':';

  public Sutrs(byte[] notizia, Charset charset,String dTipo,String livello) throws Exception  {
    this.iso2709Costruttore(notizia,charset,dTipo,Integer.parseInt(livello));
  }
  
	public Hashtable<String, List<Tag>> getRecordMapping() {
		return null;
	}

	public String getRecordTypeDescription() {
		return "General SUTRS text";
	}
  
  public void setDelimiter(char delimiter) {
  	this.delimiter=delimiter;
  }
  
  public void setDelimiterPosition(int delimiterPosition) {
  	this.delimiterPosition=delimiterPosition;
  }

  public Sutrs(byte[] notizia, Charset charset,String dTipo) throws Exception  {
    this.iso2709Costruttore(notizia,charset,dTipo,0);
  }

  public Sutrs() {
	  super();
      //dati=new Vector<String>();
  }
  
  public String getLabel(String label) {
  	String r="";
  	int i=0;
  	Tag t;
  	
  	while(i<dati.size()) {
  		t=dati.elementAt(i);
  		if(t.getRawContent().startsWith(label)) r=t.getRawContent().substring(label.length()+1);
  		i++;
  	}
  	
  	return r;
  }
  
  protected Vector<String> string2vector(String s) {
  	Vector<String> v=new Vector<String>();
  	if(s.length()>2)
  		v.addElement(s);
  	return v;
  }
  
  protected String cleanLabel(String label) {
  	while(label.charAt(0)==' ') label=label.substring(1);
  	int i=label.length()-1;
  	while((i>0)&&
  			((label.charAt(i)==' ')||
  			(label.charAt(i)==':')||
			(label.charAt(i)=='.')
  			)) i--;
  	return label.substring(0,i+1);
  }
  
  public void init(String stringa) throws Exception {
  	inString=stringa;
  	String[] in=stringa.split("\n");
  	StringTokenizer st = new StringTokenizer(stringa,"\\|\n");
  	while(st.hasMoreTokens()){
  		String temp = st.nextToken();
  		if(temp.contains("ICCU") && temp.contains("ident")){
  			bid = temp;
  		}
  	}
  	
  	if(bid==null){
  		StringTokenizer st1 = new StringTokenizer(stringa," !\"�$%&()='?^[*]+@#�-.,;:_\n\r|");
  		while(st1.hasMoreElements()){
  			String temp = st1.nextToken();
  			if(temp.matches("[a-zA-Z][a-zA-Z]..?\\d\\d\\d\\d\\d\\d\\d")){
  				bid = temp;
  			}
  		}
  	}
  	
  	if(bid==null)bid="";
  	
  	if(bid.contains("/")){
		String[] bidparts = bid.split("/");
		bid = bidparts[bidparts.length-2]+bidparts[bidparts.length-1];
	}
  	
  	String currentLabel="";
  	String currentContent="";
  	for(int i=0;i<in.length;i++) {
  		if((in[i].length()>delimiterPosition)&&(in[i].charAt(delimiterPosition)==delimiter)) {
  			if(currentContent.length()>0) {
  				Tag t=new Tag(currentLabel,' ',' ');
  				try {
					t.setRawContent(currentContent.trim());
				} catch (JOpac2Exception e) {
					e.printStackTrace();
				}
  				dati.addElement(t);
  			}
  			currentLabel=cleanLabel(in[i].substring(0,delimiterPosition));
  			currentContent=in[i].substring(delimiterPosition+1);
  		}
  		else {
  			currentContent+=in[i];
  		}
  	}
  	if(currentContent.length()>0) {
  		Tag t=new Tag(currentLabel,' ',' ');
		try {
			t.setRawContent(currentContent.trim());
		} catch (JOpac2Exception e) {
			e.printStackTrace();
		}
		dati.addElement(t);
	}
  }
	
  public Vector<String> getAuthors() {
	    Vector<Tag> v=getTags("700");

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
	
	public String getEdition() {
		// TODO Auto-generated method stub
		return "";
	}

	public String getPublicationPlace() {return null;}
	
	public String getPublicationDate() {
	  	
return null;
	}

	public String getTitle() {
		Tag t=getFirstTag("200");
		return t==null?"":Utils.ifExists("", t.getField("a"));
	}
	
	public String getISBD() {
		Tag t=getFirstTag("200");
		String r="";
		if(t!=null) r+=Utils.ifExists("", t.getField("a"));
		t=getFirstTag("210");
		if(t!=null) {
			r+=Utils.ifExists(". - ",t.getField("a"));
			r+=Utils.ifExists(" : ",t.getField("c"));
			r+=Utils.ifExists(" , ",t.getField("d"));
		}
		t=getFirstTag("215");
	    if(t!=null) {
		    r+=Utils.ifExists(". - ",t.getField("a"));
		    r+=Utils.ifExists(" : ",t.getField("c"));
		    r+=Utils.ifExists(" ; ",t.getField("d"));
		    r+=Utils.ifExists(" + ",t.getField("e"));
	    }

	    t=getFirstTag("300");
	    if(t!=null) {
	    	r+=Utils.ifExists(". - ",t.getField("a"));
	    }
	    
	    t=getFirstTag("010");
	    if(t!=null) {
	    	r+=Utils.ifExists(". - ",t.getField("a"));
	    }
		return r;
	  }
	
	public String getComments() {
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see JOpac2.dataModules.ISO2709#getAbstract()
	 */
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
		    return null;
	   }

	@Override
	public Vector<ClassificationInterface> getClassifications() {
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

	public void addEditor(String editor) throws JOpac2Exception {
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

	@Override
	public Vector<String> getEditors() {
		// TODO Auto-generated method stub
		return null;
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