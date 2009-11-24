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
 * JOpac2 - Modulo formato "Sosebi". v. 0.1
 *          Questo modulo permette di importare dati registrati nel formato
 *          Sosebi (export Unimarc).
 *
 * Sosebi � prodotto da So.Se.Bi. Italia
 */


/**
 * 01/06/2003 - R.T.
 *     Modulo Sosebi , realizzato dal modulo Sebina.
 *     Cambiato getSignatures
 *     Introdotto quote(....)
 */


import java.awt.image.BufferedImage;
import java.util.*;

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.utils.*;

public class Sosebi extends Unimarc {

  public Sosebi(String stringa,String dTipo)  throws Exception {
    super(stringa,dTipo);
  }

  public Sosebi(String stringa,String dTipo,String livello)  throws Exception {
    super(stringa,dTipo,livello);
  }
  
  public Hashtable<String, List<Tag>> getRecordMapping() {
		Hashtable<String, List<Tag>> r=super.getRecordMapping();
		
		List<Tag> bib=new Vector<Tag>();
		bib.add(new Tag("801","b",""));
		r.put("BIB", bib);
		
		List<Tag> inv=new Vector<Tag>();
		inv.add(new Tag("950","e",""));
		r.put("INV", inv);
		
		return r;
	}

	public String getRecordTypeDescription() {
		return "Sosebi unimarc format.";
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
    Vector<Tag> v=getTags("950");
    Vector<BookSignature> res=new Vector<BookSignature>();
    Tag bi=getFirstTag("801");
    String biblioteca=bi.getField("b").getContent();
    if(v.size()>0) {
      for(int i=0;i<v.size();i++) {
//        String biblioteca=getFirstElement((String)v.elementAt(i),"a");
        String codBib=v.elementAt(i).getField("e").getContent().substring(0,2);

        Vector<Field> collocazioni=v.elementAt(i).getFields("d");
        Vector<Field> inventari=v.elementAt(i).getFields("e");
        Vector<Field> sintesi=v.elementAt(i).getFields("b");

        for(int j=0;j<collocazioni.size();j++) {
          String r="";
          try {
            String t=(String)collocazioni.elementAt(j).getContent();
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
            r+="[Errore decodifica]";
          }

          String te=inventari.elementAt(j).getContent().substring(2,5)+" ";
          String sin=sintesi.elementAt(j).getContent();
          te+=inventari.elementAt(j).getContent().substring(5,14);
          
          res.addElement(new BookSignature(codBib,biblioteca,te,r,sin));
        }
      }
    }
    return res;
  }

  public String quote(String s) {
    String r=super.quote(s);
    r=r.replaceAll("\0161\017","'*");
    r=r.replaceAll("\016A\017u","?");


    return r;
  }

public void addSignature(BookSignature signature) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public BufferedImage getImage() {
	// TODO Auto-generated method stub
	return null;
}
}
