package org.jopac2.jbal.iso2709;

import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
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


public class Comarc extends Unimarc {

  public Comarc(byte[] stringa,Charset charset,String dTipo)  throws Exception {
    super(stringa,charset,dTipo);
  }

  public Comarc(byte[] stringa,Charset charset,String dTipo, String livello)  throws Exception {
    super(stringa,charset,dTipo,livello);
  }
  
  
public Vector<BookSignature> getSignatures() {
      Vector<Tag> v=getTags("996");
      Vector<BookSignature> res=new Vector<BookSignature>();
      
      Tag t1=getFirstTag("000");
      
      if(v.size()>0) {
        for(int i=0;i<v.size();i++) {
          String biblioteca=t1.getField("c").getContent();
          biblioteca=biblioteca.substring(0,biblioteca.indexOf("::"));
          
          String codBib="http://sikkp.kp.sik.si/"; //getFirstElement((String)v.elementAt(i),"e").substring(0,2);

          Vector<Field> collocazioni=v.elementAt(i).getFields("e");
          Vector<Field> inventari=v.elementAt(i).getFields("d");
          Vector<Field> sintesi=v.elementAt(i).getFields("b");


          String r;
          String sin;
          String te;
          
          for(int j=0;j<inventari.size();j++) {
            try {r=cleanData(collocazioni.elementAt(j).getContent());} catch (Exception e) {r=null;}
            
            try {
            	te=inventari.elementAt(j).getContent();
            	te=cleanData(te);
            } 
            catch (Exception e) {
            	te=null;
            }
            
            try {sin=cleanData(sintesi.elementAt(j).getContent());} catch (Exception e) {sin=null;} 
            
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

	public void addSignature(BookSignature signature) throws JOpac2Exception {
		// TODO Auto-generated method stub
		
	}

	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return null;
	}

}