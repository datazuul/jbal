package org.jopac2.jbal.xml;


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
*/

/***
 *Modificato:
 * 01/10/2003 - RT
 *              Aggiunto metodo per salvare un record in formato ISO2709
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.iso2709.ISO2709Impl;
import org.jopac2.utils.*;

public abstract class XML implements RecordInterface {
  private Node document=null;

  public static String cr=String.valueOf((char)13);
  public static String lf=String.valueOf((char)10);
  private long JOpacID=0;
  protected String bid=null;
  private String descrizioneTipo;
  private int _livello =0;  // serve a gestire le notizie per linkup e down

  public Vector<RecordInterface> linkUp=null;
  public Vector<RecordInterface> linkDown=null;
  public Vector<RecordInterface> linkSerie=null;
  
  private Vector<TokenWord> tw;
  
  public RecordInterface clone() {
	  return RecordFactory.buildRecord(0, this.toString(), this.getTipo(), this.getLivello());
  }
  
	/* (non-Javadoc)
	 * @see JOpac2.dataModules.iso2709.ISO2709Impl#getHash()
	 */
	public String getHash() throws JOpac2Exception, NoSuchAlgorithmException {
		RecordInterface ma=this.clone();
		ma.clearSignatures();
		String n=ma.toString();
		ma.destroy();
		
		String output=null;
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(n.getBytes());
		byte[] md5sum = digest.digest();
		BigInteger bigInt = new BigInteger(1, md5sum);
		output = bigInt.toString(16);
		
		return output;
	}
  
  public String getDl() {
	  return null;
  }
  
  public void destroy() {
  	if(document!=null) {
  		XMLHelper.destroy(document);
  		document=null;
  		tw.clear();
  		tw=null;
  	}
  	if(linkUp!=null) linkUp.clear();
  	if(linkDown!=null) linkDown.clear();
  	if(linkSerie!=null) linkSerie.clear();

    //System.gc();
  }
  
/**
 * 01/10/2003 - RT
 *          Definisce il tipo di record e lo stato.
 */
  //private String recordStatus="a";
  //private String recordType="m";
  private String recordStatus="n";
  private String recordType="a";
  private String recordBiblioLevel="m";
  private String recordHierarchicalLevelCode="0";

  public String getType() {
       return recordType;
  }
  
  public void setType(String type){
  		recordType = type;
  }
  
  public String getStatus() {
       return recordStatus;
  }
  
  public void setStatus(String status){
  		recordStatus = status;
  }
  
  public String getBiblioLevel(){
  	return recordBiblioLevel;
  }
  
  public void setBiblioLevel(String lev){
  	recordBiblioLevel = lev;
  }
  
  public String getHierarchicalLevelCode(){
  	return recordHierarchicalLevelCode;
  }
  
  public void setHierarchicalLevelCode(String lCode){
  	recordHierarchicalLevelCode = lCode;
  }
  
  public boolean hasLinkUp() {
    return (linkUp!=null&&linkUp.size()>0);
  }

  public boolean hasLinkDown() {
    return (linkDown!=null&&linkDown.size()>0);
  }

  public boolean hasLinkSerie() {
    return (linkSerie!=null&&linkSerie.size()>0);
  }

  /**
   * 13/2/2003 - R.T.
   *    Modificato, ritorna un vettore con i contenuti
   *    invece degli html. Il resto verrà fatto fuori.
   */
  public Vector<RecordInterface> getHasParts() {
    return linkDown;
  }

  public Vector<RecordInterface> getIsPartOf() {
    return linkUp;
  }

  public Vector<RecordInterface> getSerie() {
    return linkSerie;
  }

  public void addTag(String newTag) {
      //dati.addElement(newTag);
  }
  
  public void addTag(Vector<String> newTags) {
      //dati.addAll(newTags);
  }
  
  public int getLivello() {
    return _livello;
  }

  public String getTipo() {
    return descrizioneTipo;
  }

  public void setJOpacID(long l) {
    JOpacID=l;
  }

  public long getJOpacID() {
    return JOpacID;
  }

  public void setBid(String b) {
    bid=b;
  }
  
  protected Node getNode(String nodename) {
	return XMLHelper.getNode(document,nodename);  
  }
  
  protected String getNodeContent(String nodename) {
	  return XMLHelper.getNodeContent(document, nodename);
  }

  public String getBid() {
	  Node bid=getNode("bid");
      if(bid==null) {return Long.toString(getJOpacID());}
      else return bid.getTextContent();
  }

  public String toString() {
    return XMLHelper.toString(document);
  }
  
  public String toReadableString() {
	  return toString();
  }

  public abstract Vector<String> getAuthors(); // Autori
  public abstract Vector<String> getSubjects(); // Soggetti
  public abstract Vector<String> getClassifications(); // Classificazioni
  public abstract Vector<String> getEditors(); // Editori
  public abstract String getEdition(); //Edizione
  public abstract String getPublicationPlace(); // Luogo di pubblicazione
  public abstract String getPublicationDate(); // Data di pubblicazione
  public abstract String getTitle(); // Titolo
  public abstract String getISBD(); // ISBD
  public abstract Vector<BookSignature> getSignatures(); // Localizzazioni
  public abstract String getAbstract(); // Abstract
  public abstract String getDescription(); // Descrizione fisica

  public void init(String in) throws SAXException, IOException {
	  tw = new Vector<TokenWord>();
	  if (in != null && in.length() > 0) {
			InputSource is = new InputSource(new StringReader(in));
			DOMParser p = new DOMParser();
			
			try {
				p.parse(is);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			document = p.getDocument();
		}
  }

  protected void XMLCostruttore(String notizia,String dTipo,int livello) throws SAXException, IOException {
    descrizioneTipo=dTipo;
    init(notizia);

    _livello=livello+1;  //  salva il livello creato per evitare ricorsione
  }
  
  public String getTerminators() {
    return null;
  }
  
  public XML(String notizia,String dTipo,String livello) throws NumberFormatException, SAXException, IOException {
    this.XMLCostruttore(notizia,dTipo,Integer.parseInt(livello));
  }

  public XML(String notizia,String dTipo) throws SAXException, IOException {
    this.XMLCostruttore(notizia,dTipo,0);
  }
  
  //public abstract RecordReader getRecordReader(BufferedReader f);
  public abstract RecordReader getRecordReader(InputStream dataFile) throws UnsupportedEncodingException;


  public XML() {
      document=null;
      tw=new Vector<TokenWord>();
  }
  
  public Enumeration<TokenWord> getItems() {
		tw.removeAllElements();
		tw=XMLHelper.exploreData(document);
		
		return tw.elements();
	}
  
	public void addSignature(BookSignature booksignature) throws JOpac2Exception {
		throw new JOpac2Exception(
				"Error: method addSignature is not implemented");
	}
	
	public void clearSignatures() throws JOpac2Exception {
		throw new JOpac2Exception(
				"Error: method clearSignatures is not implemented");
	}

	public float similarity(RecordInterface ma) {
		return ISO2709Impl.similarity(this,ma);
	}
	
	public boolean contains(String s) {
		String d=toString();
		return d.contains(s);
	}

	public boolean contains(String tag, String s) {
		String d=getNodeContent(tag).toString();
		return d.contains(s);
	}

	public boolean contains(String tag, String field, String s) {
		String d=getNodeContent(tag+"/"+field).toString();
		return d.contains(s);
	}
	
	public void removeTags(String tag) throws JOpac2Exception {
		/**
		 * To implement!
		 */
		throw new JOpac2Exception(
		"Error: method removeTags is not implemented");
	}
	
	public Vector<RecordInterface> getLinked(String tag) throws JOpac2Exception {
		/**
		 * To implement!
		 */
		throw new JOpac2Exception(
		"Error: method removeTags is not implemented");
	}
	
	public void checkNSBNSE(String tag, String nsb, String nse) {
		// TODO Auto-generated method stub
		
	}
}