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
import org.jopac2.utils.*;

public abstract class Marc extends ISO2709Impl {

  /* (non-Javadoc)
	 * @see JOpac2.dataModules.iso2709.ISO2709Impl#clearSignatures()
	 */
	@Override
	public void clearSignatures() throws JOpac2Exception {
		removeArea("9xx");
	}
	
	public String getComments() {
		return getFirstElement(getFirstTag("300"),"a");
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
    Vector<String> v=getTag(tag);
    Vector<RecordInterface> r=new Vector<RecordInterface>();
    
    
    try {
      if(v.size()>0) { // se il vettore ha elementi, allora faro' almeno una query
        for(int i=0;i<v.size();i++) {
        	RecordInterface not=RecordFactory.buildRecord(0,(String)v.elementAt(i),this.getTipo(),this.getLivello());
          //ISO2709 not=ISO2709.creaNotizia(0,(String)v.elementAt(i),this.getTipo(),this.getLivello());

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
    Vector<String> v=getTag("700");
    v.addAll(getTag("701"));
    v.addAll(getTag("702"));
    Vector<String> r=new Vector<String>();
    String k="";
    if(v.size()>0) {
      for(int i=0;i<v.size();i++) {
        k=getFirstElement((String)v.elementAt(i),"a");
        k+=" "+getFirstElement((String)v.elementAt(i),"b");
        if(!r.contains(k)) r.addElement(k);
      }
    }
    return r;
  }

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
  
  public String getDescription() {
  	return getTagElements("215");
  }

  private void marcCostruttore(String stringa,String dTipo,int livello) {
    // se vengo chiamato dopo il primo oggetto, non scorro la catena
    if(livello<=1) {
      initLinkUp();
      initLinkDown();
      initLinkSerie();
    }
  }

  
  
  public Marc(String stringa,String dTipo) {
    super(stringa,dTipo,"0");
    this.marcCostruttore(stringa,dTipo,0);
  }

  public Marc(String stringa,String dTipo,String livello) {
    super(stringa,dTipo,livello);
    this.marcCostruttore(stringa,dTipo,Integer.parseInt(livello));
  }

}