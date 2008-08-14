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

import java.util.*;

import org.jopac2.jbal.iso2709.ISO2709Impl;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.Utils;

public class Sutrs extends ISO2709Impl {

public int delimiterPosition=27;
  public char delimiter=':';

  public Sutrs(String notizia,String dTipo,String livello) {
    this.iso2709Costruttore(notizia,dTipo,Integer.parseInt(livello));
  }
  
  public void setDelimiter(char delimiter) {
  	this.delimiter=delimiter;
  }
  
  public void setDelimiterPosition(int delimiterPosition) {
  	this.delimiterPosition=delimiterPosition;
  }

  public Sutrs(String notizia,String dTipo) {
    this.iso2709Costruttore(notizia,dTipo,0);
  }

  public Sutrs() {
	  super();
      //dati=new Vector<String>();
  }
  
  public String getLabel(String label) {
  	String r="";
  	int i=0;
  	String t;
  	
  	while(i<dati.size()) {
  		t=(String)dati.elementAt(i);
  		if(t.startsWith(label)) r=t.substring(label.length()+1);
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
  
  public void init(String stringa) {
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
  		StringTokenizer st1 = new StringTokenizer(stringa," !\"£$%&()='?^[*]+@#§-.,;:_\n\r|");
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
  				dati.addElement(currentLabel+":"+currentContent.trim());
  			}
  			currentLabel=cleanLabel(in[i].substring(0,delimiterPosition));
  			currentContent=in[i].substring(delimiterPosition+1);
  		}
  		else {
  			currentContent+=in[i];
  		}
  	}
  	if(currentContent.length()>0) {
		dati.addElement(currentLabel+":"+currentContent.trim());
	}
  }
	
  	public Vector<String> getAuthors() {
	    Vector<String> v=getTag("700");
	    v.addAll(getTag("701"));
	    v.addAll(getTag("702"));
	    Vector<String> r=new Vector<String>();
	    String k="";
	    if(v.size()>0) {
	      for(int i=0;i<v.size();i++) {
	        k=getFirstElement((String)v.elementAt(i),"a");
	        k+=" "+getFirstElement((String)v.elementAt(i),"b");
	        r.addElement(k);
	      }
	    }
	    return r;
	  }
	
	/* (non-Javadoc)
	 * @see JOpac2.dataModules.ISO2709#getEdition()
	 */
	public String getEdition() {
		// TODO Auto-generated method stub
		return "";
	}

	public String getPublicationPlace() {return null;}
	
	public String getPublicationDate() {
	  	
	    Vector<String> v = getTag("210");

	    String k="";
	    if(v.size()>0) {      
	        k=getFirstElement((String)v.elementAt(0),"d");      
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
	
	/* (non-Javadoc)
	 * @see JOpac2.dataModules.ISO2709#getTitle()
	 */
	public String getTitle() {
		String r;
		String tag=getFirstTag("200");
		r=quote(getFirstElement(tag,"a"));
		return r;
	}
	
	public String getISBD() {
	    String r;
	    r=getTitle();
	    String tag=getFirstTag("200");;
	    r+=getFirstElement(tag,"d");
	    r+=Utils.ifExists(" : ",getFirstElement(tag,"e"));
	    r+=Utils.ifExists(" / ",getFirstElement(tag,"f"));
	    r+=Utils.ifExists(" ; ",getFirstElement(tag,"g"));

	    tag=getFirstTag("205");
	    r+=Utils.ifExists(". - ",getFirstElement(tag,"a"));
	    r+=Utils.ifExists(" ; ",getFirstElement(tag,"b"));

	    tag=getFirstTag("210");
	    r+=Utils.ifExists(". - ",getFirstElement(tag,"a"));
	    r+=Utils.ifExists(" : ",getFirstElement(tag,"c"));
	    r+=Utils.ifExists(" , ",getFirstElement(tag,"d"));


	    tag=getFirstTag("215");
	    r+=Utils.ifExists(". - ",getFirstElement(tag,"a"));
	    r+=Utils.ifExists(" : ",getFirstElement(tag,"c"));
	    r+=Utils.ifExists(" ; ",getFirstElement(tag,"d"));
	    r+=Utils.ifExists(" + ",getFirstElement(tag,"e"));

	    tag=getFirstTag("300");
	    r+=Utils.ifExists(". - ",getFirstElement(tag,"a"));

	    tag=getFirstTag("010");
	    r+=Utils.ifExists(". - ",getFirstElement(tag,"a"));

	    return quote(r);
	  }
	
	public String getComments() {
		return getFirstElement(getFirstTag("300"),"a");
	}
	
	
	/* (non-Javadoc)
	 * @see JOpac2.dataModules.ISO2709#getAbstract()
	 */
	public String getAbstract() {
		// TODO Auto-generated method stub
		return "";
	}
	
	public String getDescription() {
		return getFirstElement(getTagElements("215"),"a");
	}
	
	public void addSignature(BookSignature signature) {
		  
		  if(getSignatures().contains(signature)) return;
		  
		  String codBib = signature.getLibraryId();
		  String biblioteca = signature.getLibraryName();
		  String te = signature.getBookNumber();
		  String r = signature.getBookLocalization();
		  String sin = signature.getBookCons();
		  StringBuffer tagString = new StringBuffer();
		  tagString.append("950  ");
		  
		  if(biblioteca.length()>0) tagString.append(dl+"a"+biblioteca);
		  if(sin.length()>0) tagString.append(dl+"b"+sin);
		  if(r.length()>0) tagString.append(dl+"d"+r);
		  if(te.length()>0) tagString.append(dl+"e"+te);
		  if(codBib.length()>0) tagString.append(dl+"f"+codBib);
		  
		  addTag(tagString.toString());
		}
		
	   public Vector<BookSignature> getSignatures() {
		    String libraryId="", libraryName="", bookNumber="", bookLocalization="";
			Vector<String> loc=getTag("950");
			Vector<BookSignature> ret=new Vector<BookSignature>();
			for(int i=0;i<loc.size();i++) {
				String thisloc=getFirstElement(loc.elementAt(i),"l").trim();
				if(thisloc!=null && thisloc.length()>0) {
					int a=thisloc.indexOf("-");
					int b=thisloc.indexOf("-",a+1);
					if(a>0 && b>a) {
						libraryId=thisloc.substring(0, a);
						libraryName=thisloc.substring(b+1);
						bookNumber=thisloc.substring(a+1,b);
						ret.addElement(new BookSignature(libraryId, libraryName, bookNumber, bookLocalization));
					}
					
				}
				else {
					libraryId=getFirstElement(loc.elementAt(i),"f");
					libraryName=getFirstElement(loc.elementAt(i),"a");
					bookNumber=getFirstElement(loc.elementAt(i),"e");
					bookLocalization=getFirstElement(loc.elementAt(i),"d");
					ret.addElement(new BookSignature(libraryId, libraryName, bookNumber, bookLocalization));
				}
			}
			return ret;
	   }
}