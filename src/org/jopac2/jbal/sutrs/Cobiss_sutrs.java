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

import java.util.*;

import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.Utils;

public class Cobiss_sutrs extends Sutrs {

  public Cobiss_sutrs(String notizia,String dTipo,String livello) {
    this.iso2709Costruttore(notizia,dTipo,Integer.parseInt(livello));
  }

  public Cobiss_sutrs(String notizia,String dTipo) {
    this.iso2709Costruttore(notizia,dTipo,0);
  }


  public Cobiss_sutrs() {
	  super();
      //dati=new Vector<String>();
  }

  /*
   * TODO Implementare metodo getEdition
   */
  public String getEdition() {
  	return null;
  }
  
  public String getAbstract() {
  	return null;
  }

  
  public Vector<String> getAuthors() {
	  Vector<String> ret = string2vector(getLabel("AUTHOR"));
	  if(ret.isEmpty())
		  ret = string2vector(getLabel("RESPONSIBILITY"));
	  return ret;
  }

  public Vector<String> getEditors() {return string2vector(getLabel("IMPRINT"));}
  public String getPublicationPlace() {return getLabel("COUNTRY OF PUBLICATION");}
  public String getPublicationDate() {return getLabel("PUBLICATION YEAR");}
  public String getTitle() {return getLabel("TITLE");}
  
  public String getISBD() {
	  String ret = getLabel("TITLE")+" / "+ getLabel("AUTHOR");
	  if(getLabel("PHYSICAL DESCRIPTION").length()>1)
		  ret = ret +"  ; - " + getLabel("PHYSICAL DESCRIPTION");
	  
	  return  ret + Utils.ifExists(" ",getLabel("SERIES"));
  }
  
  public String getBid() {
    String t=getLabel("COBISS.SI-ID");
    t=t.replaceAll("\r"," ");
    if(t.contains(" ")) t=t.substring(0,t.indexOf(" "));
    return t;
  }

  /* (non-Javadoc)
   * @see JOpac2.dataModules.iso2709.ISO2709Impl#clearSignatures()
   */
  @Override
  public void clearSignatures() throws JOpac2Exception {
  	//this.removeTag("950");
  }
  
  public String getDescription() {return null;}
  
}