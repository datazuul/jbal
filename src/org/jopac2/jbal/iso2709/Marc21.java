package org.jopac2.jbal.iso2709;

import java.util.Vector;

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
	 * TODO Marc21 è tutto da fare!!!
	 */
  public Marc21(String stringa,String dTipo) {
    super(stringa,dTipo);
  }

  public Marc21(String stringa,String dTipo,String livello) {
    super(stringa,dTipo,livello);
  }

  public Vector<String> getAuthors() {
    Vector<String> v=getTag("100");
    v.addAll(getTag("110"));
    v.addAll(getTag("111"));
    Vector<String> r=new Vector<String>();
    String k="";
    if(v.size()>0) {
      for(int i=0;i<v.size();i++) {
        k=getFirstElement((String)v.elementAt(i),"a");
        k+=" "+getFirstElement((String)v.elementAt(i),"b");
        k+=" "+getFirstElement((String)v.elementAt(i),"c");
        k+=" "+getFirstElement((String)v.elementAt(i),"d");
        r.addElement(k);
      }
    }
    return r;
  }
  
  public String getTitle() {
    String r;
    String tag=getFirstTag("245");
    r=quote(getFirstElement(tag,"a"));
    return r;
  }
  
  public String getISBD() {
    String r;
    r=getTitle();
    String tag=getFirstTag("245");
    r+=getFirstElement(tag,"h");
    r+=getFirstElement(tag,"b");
    r+=getFirstElement(tag,"c");
    r+=getFirstElement(tag,"n");
    r+=getFirstElement(tag,"p");
    r+=getFirstElement(tag,"f");
    
    r+=" .- ";
    tag=getFirstTag("260");;
    r+=getFirstElement(tag,"a");
    r+=getFirstElement(tag,"b");
    r+=getFirstElement(tag,"c");

    r+=" .- ";
    tag=getFirstTag("300");;
    r+=getFirstElement(tag,"a");
    r+=getFirstElement(tag,"b");
    r+=getFirstElement(tag,"c");


    return quote(r);
  }

  public String getEdition() {
  	String r,tag;
  	tag=getFirstTag("250");;
    r=getFirstElement(tag,"a");
    r+=getFirstElement(tag,"b");
  	return r;
  }

  
  public String getBid() {
  	return getFirstTag("001");
  }
  
  // TODO per questo tipo trovare l'abstract
  public String getAbstract() {
  	return null;
  }
  public Vector<String> getSubjects() {
  	return getElement(getTag("650"),"a");
  }


	public String getDescription() {
		return getTagElements("300");
	}
}