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

/**
* @author	Albert Caramia
* @version ??/??/2002
* 
* @author	Romano Trampus
* @version ??/??/2002
* 
* @author	Romano Trampus
* @version 07/10/2006
* Aggiunto v712 agli autori (getAuthors)
*/

/**
 * JOpac2 - Modulo formato "Easyweb". v. 0.1
 *          Questo modulo permette di importare dati registrati nel formato
 *          CDS ISIS / Easyweb.
 *
 * CDS ISIS è prodotto dall'UNESCO
 * Easyweb è un marchio di Nexus S.r.l. - Firenze (I)
 */


import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
//import java.lang.*;
//import java.sql.*;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.importers.Readers.IsoRecordReader;
import org.jopac2.jbal.importers.Readers.RecordReader;
import org.jopac2.utils.*;

public class Easyweb extends ISO2709Impl {

  public Easyweb(String stringa,String dTipo) {
    super();
    setTerminator("#","#","^");
    iso2709Costruttore(stringa,dTipo,0);
    initLinkUp();
    initLinkDown();
    initLinkSerie();
  }

  public Easyweb(String stringa,String dTipo,String livello) {
    super();
    setTerminator("#","#","^");
    iso2709Costruttore(stringa,dTipo,Integer.parseInt(livello));
    initLinkUp();
    initLinkDown();
    initLinkSerie();
  }
  
	public RecordReader getRecordReader(InputStream dataFile) throws UnsupportedEncodingException {
		return new IsoRecordReader(dataFile,ft,rt,"latin1");
	}

  public void initLinkUp() {
    linkUp=getLink("601");
  }

  public void initLinkSerie() {
    linkSerie=getLink("420");
  }

  public void initLinkDown() {
    linkDown=getLink("611");
  }

  // ritorna un vettore di elementi Easyweb
public Vector<RecordInterface> getLink(String tag) {
    Vector<String> v=getTag(tag);
    Vector<RecordInterface> r=new Vector<RecordInterface>();
    try {
      if(v.size()>0) { // se il vettore ha elementi, allora faro' almeno una query
        for(int i=0;i<v.size();i++) {
          Easyweb not=new Easyweb();
          //not.dati=new Vector<String>();
          String title=(String)v.elementAt(i);
          not.dati.addElement("100"+getFirstElement(title,"a"));
          not.setBid(getFirstElement(title,"Z"));
//          ISO2709.creaNotizia(0,(String)v.elementAt(i),this.getTipo(),this.getLivello());
          r.addElement(not);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return r;
  }
  
  public String getPublicationPlace() {
      return getFirstTag("301");
  }
  
  public String getPublicationDate() {
      return getFirstTag("310");
  }

  // TODO per questo tipo trovare l'abstract
  public String getAbstract() {
  	return null;
  }

  /*
   * TODO Implementare metodo getEdition
   */
  public String getEdition() {
  	return null;
  }

  
public Vector<String> getAuthors() {
    Vector<String> v=getTag("710");
    v.addAll(getTag("711"));
    v.addAll(getTag("712"));

    Vector<String> r=new Vector<String>();
    if(v.size()>0) {
      for(int i=0;i<v.size();i++) {
        
        String k=getFirstElement((String)v.elementAt(i),"a");
        r.addElement(k);
      }
    }
    return r;
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
 * 22/9/2004 - R.T.
 *          Ritorna un vettore di BookSignature
 */
public Vector<BookSignature> getSignatures() {
    Vector<String> v=getTag("800");
    Vector<BookSignature> res=new Vector<BookSignature>();
    if(v.size()>0) {
      for(int i=0;i<v.size();i++) {
        String codiceBib="";
        String b="";

        try {
            b=getFirstElement((String)v.elementAt(i),"a");
            codiceBib=getFirstElement((String)v.elementAt(i),"b"); //.substring(0,2);
        }
        catch (Exception e) {}
        String col=getFirstElement((String)v.elementAt(i),"c");
        String inv=getFirstElement((String)v.elementAt(i),"d");
        String con=getFirstElement((String)v.elementAt(i),"e");

        res.addElement(new BookSignature(codiceBib,b,inv,col,con));
      }
    }
    return res;
  }



  /* (non-Javadoc)
 * @see JOpac2.dataModules.iso2709.ISO2709Impl#clearSignatures()
 */
@Override
public void clearSignatures() throws JOpac2Exception {
	this.removeTag("800");
}

public String quote(String t) {
    String s = "";
    String c = "";
    String left="";
    String right="";
    int peq=0;
    int pdx=0;
    int psx=0;

    /**
     * Casi:
     *
     * <...=.....> devo togliere le angolate e prendere solo la parte a sx dell'=
     * <....> ...... devo togliere le angolate e mettere l'asterisco
     * <....> / oppore un separatore devo togilere le angolate e basta
     * <<....=....> .... >...... questo è un caso certo
     *
     * Questo caso va bene (mette solo gli *)
     * <... >..... = <... >......
     *
     *
     *
     * 1) cerco =
     * 2) scorro a sx fino a trovare < oppure >
     * 3) se ho trovato > lascio perdere e torno all'1)
     * 4) scorro a dx fino a trovare < oppure >
     * 5) se trovo < lascio perdere e passo 1)
     * 6) ok, prendo la stringa tra < e = e butto bia la parte dx
     *
     */

    peq=t.indexOf("=");
    while(peq>0) {
      left = t.substring(0, peq);
      right = t.substring(peq + 1);

      //System.out.println("left "+left);
      //System.out.println("right "+right);

      pdx = right.indexOf(">");

      if ((right.indexOf("<") > pdx)||(right.indexOf("<")==-1)) {
        psx = left.lastIndexOf("<");
        if ((left.lastIndexOf(">") < psx)||(left.indexOf(">")==-1)) {
          // OK!! prendo la parte sx e butto via la parte dx
          if(psx>0) {
            s = left.substring(0, psx) + left.substring(psx + 1) +
                right.substring(pdx + 1);
          }
          else {
            s = left.substring(psx + 1) +
                right.substring(pdx + 1);
          }
        }
        else {
          s = left + "|" + right;
        }
      }
      else {
        s = left + "|" + right;
      }
      t = s;
      peq=t.indexOf("=");
    }


//    t.replace('|','=');
//    la replace non funziona. faccio nel loop. Perche' non funziona??

    s="";

    for(int k = 1;k<t.length()+1;k++) {

      c = Utils.mid(t, k, 1);
      if (c.equals("<")==true) {
//        s = s + "&lt;";
      } else {
        if (c.equals(">")==true) {
          s = s + "*";
        }
        else {
          if(c.equals("|")==true) c="=";
          s = s + c;
        }
      }
    }
    return(s);
  }

  public String getTitle() {
    String r="";
    String tag=getFirstTag("100");
    r=(tag.substring(3));
    return r;
  }

  public String getISBD() {
    String r="";
    
    try {
        String tag=getFirstTag("100");
        r=(tag.substring(3));
    }
    catch(Exception e) {
        //r="Errore, 100 = "+getFirstTag("100");
    }
    String t=getFirstTag("200");
    if(t.length()>0) {
      r+=Utils.ifExists(". - ",t.substring(3));
    }
    t=getFirstTag("300");
    if(t.length()>0) {
      r+=Utils.ifExists(". - ",t.substring(3));
    }
    t=getFirstTag("320");
    if(t.length()>0) {
      r+=Utils.ifExists(". - ",t.substring(3));
    }
    return quote(r);
  }
  
  public Vector<String> getSubjects() {
      return getElement(getTag("730"),"a");
  }

  public Vector<String> getClassifications() {
      return getElement(getTag("740"),"a");
  }
  
  public Vector<String> getEditors() {return null;}
  
  public Easyweb() {
	  super();
  }

/* (non-Javadoc)
 * @see JOpac2.dataModules.ISO2709#getDescription()
 */
public String getDescription() {
	// TODO Auto-generated method stub
	return null;
}
}