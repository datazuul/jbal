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
* @author	Romano Trampus
* @version ??/??/2002
*/

/**
 * JOpac2 - Modulo formato "Sebina". v. 0.1
 *          Questo modulo permette di importare dati registrati nel formato
 *          Sebina.
 *
 * Sebina indice e Sebina è prodotto da Akros Informatica - Ravenna
 */


import java.util.*;

import org.jopac2.utils.*;


public class Sebina extends Marc {

  public Sebina(String stringa,String dTipo) {
    super(stringa,dTipo);
  }

  public Sebina(String stringa,String dTipo,String livello) {
    super(stringa,dTipo,livello);
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

  /**
   * 13/2/2003 - R.T.
   * Input: null
   * Output: Vector di coppie. La coppia contiene nel primo elemento una Coppia con
   *         il codice della biblioteca e il nome della biblioteca
   *         e nel secondo elemento un Vector che contiene
   *         coppie (collocazione,inventario).
   *         Se serve anche il polo ci lo mettiamo nel codice della biblioteca?
   * 24/6/2003 - R.T.
   *         Deve trasportare anche la consistenza, nel caso sia un periodico.
   *         Ciascun elemento del vettore e' una coppia (biblioteca,dati).
   *         Biblioteca e' una coppia (codice_biblioteca, nome_biblioteca).
   *         I dati sono una coppia (collocazione, inventario)
   *         Collocazione e' una coppia (collocazione, sintesi_posseduto)
   *         Inventario e' una String
   *         Se non e' un periodico sintesi_posseduto sara' null o String vuota.
   * 16/9/2004 - R.T.
   *         Riformulato. Non usa piu' coppie ma l'oggetto BookSignature.
   *         Ritorna un vettore di BookSignatures.
   */
    public Vector<BookSignature> getSignatures() {
      Vector<String> v=getTag("950");
      Vector<BookSignature> res=new Vector<BookSignature>();
      if(v.size()>0) {
        for(int i=0;i<v.size();i++) {
          String biblioteca=getFirstElement((String)v.elementAt(i),"a");
          
          /* 950^f contiene il codice Sebina solo se i dati vengono dal Pregresso
           * altrimenti e' i primi due caratteri dell'inventario
           */
          String codBib=getFirstElement((String)v.elementAt(i),"f");
          
          
          try {
	          if(codBib.equals(""))
	        	  codBib=getFirstElement((String)v.elementAt(i),"e").substring(0,2);
          }
          catch (Exception e) {}
          

          Vector<String> collocazioni=getElement((String)v.elementAt(i),"d");
          Vector<String> inventari=getElement((String)v.elementAt(i),"e");
          Vector<String> sintesi=getElement((String)v.elementAt(i),"b");

          for(int j=0;j<collocazioni.size();j++) {
            String r="";
            String te="";
            String sin="";

            try {
              String t=(String)collocazioni.elementAt(j);
              r+=t.substring(2,12);
              if(!t.substring(12,24).startsWith("/")) {
                r+="/";
              }
              r+=t.substring(12,24);
              if(!((t.substring(12,24)).trim()).equals("/")) {
                r+="/";
              }
              r+=t.substring(24,36);
            }
            catch (Exception e) {
              r=(String)collocazioni.elementAt(j);
            }


            try {
            	te =((String)inventari.elementAt(j)).substring(2,5)+" ";
            	te+=((String)inventari.elementAt(j)).substring(5,14);
                sin=((String)sintesi.elementAt(j));
            }
            catch (Exception e) {
                //sin="[Errore decodifica]";
            	sin="";
            	try {
            		te=((String)inventari.elementAt(j));
            	}
            	catch (Exception e1) {}
            }
            
            res.addElement(new BookSignature(codBib,biblioteca,te,r,sin));
          }
        }
      }
      return res;
    }
    
    
    

}