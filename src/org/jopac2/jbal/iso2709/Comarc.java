package org.jopac2.jbal.iso2709;

import java.util.Vector;

import org.jopac2.utils.BookSignature;


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
* 
* @author	Romano Trampus
* @version ??/03/2005
*/

/**
 * JOpac2 - Modulo formato "Comarc". v. 0.1
 *          Questo modulo permette di importare dati registrati nel formato
 *          Comarc.
 *
 */


public class Comarc extends Marc {

  public Comarc(String stringa,String dTipo) {
    super(stringa,dTipo);
  }

  public Comarc(String stringa,String dTipo,String livello) {
    super(stringa,dTipo,livello);
  }
  
  
public Vector<BookSignature> getSignatures() {
      Vector<String> v=getTag("996");
      Vector<BookSignature> res=new Vector<BookSignature>();
      
      String t1=getFirstTag("000");
      
      if(v.size()>0) {
        for(int i=0;i<v.size();i++) {
          String biblioteca=getFirstElement(t1,"c");
          biblioteca=biblioteca.substring(0,biblioteca.indexOf("::"));
          
          String codBib="http://sikkp.kp.sik.si/"; //getFirstElement((String)v.elementAt(i),"e").substring(0,2);

          Vector<String> collocazioni=getElement((String)v.elementAt(i),"e");
          Vector<String> inventari=getElement((String)v.elementAt(i),"d");
          Vector<String> sintesi=getElement((String)v.elementAt(i),"b");


          String r;
          String sin;
          String te;
          
          for(int j=0;j<inventari.size();j++) {
            try {r=cleanData((String)collocazioni.elementAt(j));} catch (Exception e) {r=null;}
            
            try {
            	te=(String)inventari.elementAt(j);
            	te=cleanData(te);
            } 
            catch (Exception e) {
            	te=null;
            }
            
            try {sin=cleanData((String)sintesi.elementAt(j));} catch (Exception e) {sin=null;} 
            
            res.addElement(new BookSignature(codBib,biblioteca,te,r,sin));
          }
          
        }
      }
      return res;
    }
    
    private String cleanData(String in) {
    	in=in.replaceAll("\\\\x"," ");
    	in=in.replaceAll("\\\\n"," ");
    	in=in.replaceAll("\\\\5","     ");
    	in=in.replaceAll("\\\\a"," - ");
    	in=in.replaceAll("\\\\u"," - ");
    	in=in.replaceAll("\\\\i"," - ");
    	return in;
    }
}