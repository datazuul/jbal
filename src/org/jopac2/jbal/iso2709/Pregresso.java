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
 * JOpac2 - Modulo formato "Pregresso". v. 0.1
 *          Questo modulo permette di importare dati registrati in un  formato
 *          temporaneo usato da Univ. Trieste per il recupero di dati
 *
 * Il formato � il seguente:
 * 001     file immagine in formato tif
 * 100^a     body html
 * 200^a     parole del body
 * 300^a     file immagine in formato tif
 * 310^a	 ISBD (corpo schedina)
 * 320^a	 ISBN (o numero standard)
 * 330^inventario^collocazione
 * 340^a	 data inserimento
 *     
 */


import java.util.*;

import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;

public class Pregresso extends ISO2709Impl {
	//private static String rt=String.valueOf((char)0x1d);
    //private static String ft=String.valueOf((char)0x1e);
    //private static String dl=String.valueOf((char)0x1f);        //' delimiter

  public Pregresso(String stringa,String dTipo) {
    super();
    iso2709Costruttore(stringa,dTipo,0);
    initLinkUp();
    initLinkDown();
    initLinkSerie();
  }

  public Pregresso(String stringa,String dTipo,String livello) {
    super();
    iso2709Costruttore(stringa,dTipo,Integer.parseInt(livello));
    initLinkUp();
    initLinkDown();
    initLinkSerie();
  }

  public void initLinkUp() {
    linkUp=null;
  }

  public void initLinkSerie() {
    linkSerie=null;
  }

  public void initLinkDown() {
    linkDown=null;
  }

  // ritorna un vettore di elementi
  // Pregresso non ha link!
  public Vector<String> getLink(String tag) {

return null;
  }

  public Vector<String> getAuthors() {
    return this.getElement(getTag("300"),"a");
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
 *         Se non � un periodico sintesi_posseduto sara' null o String vuota.
 */
  public Vector<BookSignature> getSignatures() {
    String s=getFirstElement(getFirstTag("002"),"n");
    Vector<BookSignature> r=new Vector<BookSignature>();
    s=s.replaceAll("html","TIF");
    String t=getFirstTag("330");
    
    r.addElement(new BookSignature(s, "Catalogo Pregresso per autore", 
    		getFirstElement(t,"a"), 
    		getFirstElement(t,"b")));
  	return r;
  }
  
  /* (non-Javadoc)
   * @see JOpac2.dataModules.iso2709.ISO2709Impl#clearSignatures()
   */
  @Override
  public void clearSignatures() throws JOpac2Exception {
  	this.removeTag("002");
  	this.removeTag("330");
  }

  public String getTitle() {
    String r=getFirstElement(getFirstTag("310"),"a");

//  	String r=getFirstTag("310"); //getFirstElement(getFirstTag("310"),"a");
  	
  	if(r.length()==0)  getFirstElement(getFirstTag("100"),"a"); 
  	// r=getFirstTag("100");
    
  	//r=r.substring(r.indexOf(";a"));
    r=r.replaceAll("<p>","\n");
    r=r.replaceAll("|","");
    //r=r.replaceAll(";a","");
/*    r=r.replaceAll("&","&amp;");
    r=r.replaceAll("<", "&lt;");
    r=r.replaceAll(">", "&gt;");*/
    return r;
  }

  public String getISBD() {
    String r=getTitle();
    String k=getFirstElement(getFirstTag("311"),"a");
    if(k!=null && k.length()>0) r=r+" / "+k;
    return r;
  }

  public Pregresso() {
	  descrizioneTipo = "Pregresso";
  }

/* (non-Javadoc)
 * @see JOpac2.dataModules.ISO2709#getDescription()
 */
public String getDescription() {
	// TODO Auto-generated method stub
	return null;
}
}