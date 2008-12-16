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
}