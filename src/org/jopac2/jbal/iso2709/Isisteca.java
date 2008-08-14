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
* 
* @author	Romano Trampus
* @version 16/01/2004
*/

/**
 * JOpac2 - Modulo formato "ISISTeca". v. 0.1
 *          Questo modulo permette di importare dati registrati nel formato
 *          CDS ISIS / TECA.
 *
 * CDS ISIS è prodotto dall'UNESCO
 *
 * TODO: come vengono visualizzati e cercati i terzi livelli? Come vengono visualizzati?
 * 
 */

import java.util.*;
//import java.lang.*;
//import java.sql.*;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.utils.*;

public class Isisteca extends ISO2709Impl {

    public Isisteca(String stringa, String dTipo) {
    super();
    setTerminator("#","#","^");
    iso2709Costruttore(stringa,dTipo,0);
    initLinkUp();
    initLinkDown();
    initLinkSerie();
  }

    public Isisteca(String stringa, String dTipo, String livello) {
    super();
    setTerminator("#","#","^");
    iso2709Costruttore(stringa,dTipo,Integer.parseInt(livello));
    initLinkUp();
    initLinkDown();
    initLinkSerie();
  }

  public void initLinkUp() {
    //linkUp=getLink("");
  }

  public void initLinkSerie() {
    linkSerie=getLink("016");
  }

  public void initLinkDown() {
    linkDown=getLink("021");
  }

  // ritorna un vettore di elementi teca
public Vector<RecordInterface> getLink(String tag) {
    String v=getFirstTag(tag);

    Vector<RecordInterface> r=new Vector<RecordInterface>();
    try {
      if(v.length()>0) { // se il vettore ha elementi, allora faro' almeno una query
          Isisteca not=new Isisteca();    // crea un nuovo elemento Isisteca
          not.dati=new Vector<String>();
          not.dati.addElement("011"+v.substring(3));
 //          ISO2709.creaNotizia(0,v,this.getTipo(),this.getLivello());
 //         Utils.Log("Display:");
 //         Utils.Log(not.toTag());
          not.setBid(getBid());
          not.setTerminator("#","#","^");
          r.addElement(not);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return r;
  }

  public Vector<String> getAuthors() {
    Vector<String> v=getTag("041");     // Autore principale
    v.addAll(getTag("051"));    // coAutore principale
    v.addAll(getTag("054"));    // altri Autori

    Vector<String> r=new Vector<String>();
    if(v.size()>0) {
      for(int i=0;i<v.size();i++) {
        String k=getFirstElement((String)v.elementAt(i),"C")+", "+getFirstElement((String)v.elementAt(i),"N");
        r.addElement(k);
      }
    }
    return r;
  }

/**
 * 29/12/2003 - R.T.
 * Input: null
 * Output: Vector di coppie. La coppia contiene nel primo elemento una Coppia con
 *         il codice della biblioteca e il nome della biblioteca
 *         e nel secondo elemento un Vector che contiene
 *         coppie (collocazione,inventario).
 *         Se serve anche il polo ci lo mettiamo nel codice della biblioteca?
 * Nota: il formato isis/teca non ha un codice di polo.
 *       ogni volume fisico corrisponde a un record.
 *          712 = biblioteca (o codice biblioteca)
 *          741^B = biblioteca
 *          741^S = sezione
 *          741^F = sottosezione
 *          741^I = intestazione
 *          741^N = numero catena
 *          741^V = numero volume
 *          001 = inventario
 */
public Vector<BookSignature> getSignatures() {
    String v=getFirstTag("741"); // prende dati collocazione
    Vector<BookSignature> res=new Vector<BookSignature>();
    
    String codiceBib=getFirstElement(v,"B");
    String b=((String)(getFirstTag("712"))).substring(3); // prende la biblioteca (nome?)
    //Parametro bib=new Parametro(codiceBib,b);
    //Vector t=new Vector();
    String col=getFirstElement(v,"S")+"/"+getFirstElement(v,"F");     // collocazione
    col+=Utils.ifExists("/",getFirstElement(v,"N"));
    String inv=(String)(getTag("001").elementAt(0)); // prende l'inventario
    String con="";
    try {
        con=((String)getFirstTag("173")).substring(3);     // consistenza del periodico
    }
    catch (Exception e) {
        con=null;
        // campo 173 non trovato
    }
    res.addElement(new BookSignature(codiceBib, b, inv, col, con));
    //t.addElement(new Parametro(new Parametro(col,con),inv));

    //res.addElement(new Parametro(bib,t));

    return res;
  }

  /*
   * TODO Implementare metodo getEdition
   */
  public String getEdition() {
  	return null;
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

      System.out.println("left "+left);
      System.out.println("right "+right);

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
    String tag=getFirstTag("011");
    r=getFirstElement(tag,"T");
//    Utils.Log("Titolo: "+r);
    return r;
  }

  public String getISBD() {
    String r="";
    String tag=getFirstTag("011");                          // AREA 1
    r=getFirstElement(tag,"T")+                             // titolo proprio
        Utils.ifExists(" : ",getFirstElement(tag,"C")) +    // compl. titolo
        Utils.ifExists(" = ",getFirstElement(tag,"P")) +    // titolo parallelo
        Utils.ifExists(" / ",getFirstElement(tag,"R")) +    // resp. princip.
        Utils.ifExists(" ; ",getFirstElement(tag,"A")) +    // altra respon.
        Utils.ifExists(" . ",getFirstElement(tag,"S")) +    // titolo successivo
        Utils.ifExists(" ; ",getFirstElement(tag,"V"));     // numero di volume se è una collana. Se è monografia non c'è il ^V
    
    String t=getFirstTag("012");                            // AREA 2
    if(t.length()>0) {
      r+=Utils.ifExists(". - ",getFirstElement(t,"E")+", "+getFirstElement(t,"R"));
    }
    t=getFirstTag("014");
    r+=Utils.ifExists(". - ",getFirstElement(t,"L")+
        Utils.ifExists(" : ",getFirstElement(t,"N"))+
        Utils.ifExists(", ",getFirstElement(t,"D")));
    t=getFirstTag("015");
    r+=Utils.ifExists(" ; ",getFirstElement(t,"E"));
    
    return quote(r);
  }

  // TODO per questo tipo trovare l'abstract
  public String getAbstract() {
  	return null;
  }
  

  
  public Isisteca() {
  }

/* (non-Javadoc)
 * @see JOpac2.dataModules.ISO2709#getDescription()
 */
public String getDescription() {
	// TODO Auto-generated method stub
	return null;
}
}
