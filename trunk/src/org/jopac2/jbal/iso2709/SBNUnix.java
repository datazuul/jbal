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
 * JOpac2 - Modulo formato "SBN-UNIX Client/server". v. 0.1
 *          Questo modulo permette di importare dati registrati nel formato
 *          SBN-UNIX Client/server.
 *
 * SBN-UNIX Client/server � prodotto dall'ICCU - Italia
 */



import java.awt.image.BufferedImage;
import java.util.*;

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.utils.*;

public class SBNUnix extends Unimarc {

  public SBNUnix(byte[] stringa,String dTipo)  throws Exception {
    super(stringa,dTipo);
  }

  public SBNUnix(byte[] stringa,String dTipo,String livello)  throws Exception {
    super(stringa,dTipo,livello);
  }
  
  public Hashtable<String, List<Tag>> getRecordMapping() {
		Hashtable<String, List<Tag>> r=super.getRecordMapping();
		
		List<Tag> bib=new Vector<Tag>();
		bib.add(new Tag("950","a",""));
		r.put("BIB", bib);
		
		List<Tag> inv=new Vector<Tag>();
		inv.add(new Tag("950","e",""));
		r.put("INV", inv);
		
		return r;
	}

	public String getRecordTypeDescription() {
		return "SBNUnix unimarc format.";
	}

/**
 * 13/2/2003 - R.T.
 * Input: null
 * Output: Vector di BookSignature
 *         Se serve anche il polo ci lo mettiamo nel codice della biblioteca?
 * 24/6/2003 - R.T.
 *         Deve trasportare anche la consistenza, nel caso sia un periodico.
 *         Ciascun elemento del vettore e' una coppia (biblioteca,dati).
 *         Biblioteca e' una coppia (codice_biblioteca, nome_biblioteca).
 *         I dati sono una coppia (collocazione, inventario)
 *         Collocazione e' una coppia (collocazione, sintesi_posseduto)
 *         Inventario e' una String
 *         Se non e' un periodico sintesi_posseduto sara' null o String vuota.
 */
  public Vector<BookSignature> getSignatures() {
    Vector<Tag> v=getTags("950");
    Vector<BookSignature> res=new Vector<BookSignature>();
    if(v!=null && v.size()>0) {
      for(int i=0;i<v.size();i++) {
        String biblioteca=v.elementAt(i).getField("a").getContent();
        String codBib=v.elementAt(i).getField("e").getContent().substring(0,2);
        //Parametro bib=new Parametro(codBib,biblioteca);

        Vector<Field> collocazioni=v.elementAt(i).getFields("d");
        Vector<Field> inventari=v.elementAt(i).getFields("e");
        Vector<Field> sintesi=v.elementAt(i).getFields("b");
        //Vector<Parametro> colInv=new Vector<Parametro>();

        for(int j=0;j<collocazioni.size();j++) {
          String r="";
          try {
            String t=collocazioni.elementAt(j).getContent();
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
          //colInv.addElement(new Parametro(new Parametro(r,sin),te));
          res.addElement(new BookSignature(codBib,biblioteca,te,r,sin));
        }
        
        //res.addElement(new Parametro(bib,colInv));
      }
    }
    return res;
  }

public void addSignature(BookSignature signature) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public BufferedImage getImage() {
	// TODO Auto-generated method stub
	return null;
}
  
 
}
