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
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.jopac2.jbal.ElectronicResource;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.abstractStructure.Delimiters;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.iso2709.ISO2709Impl;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.*;

public abstract class XML implements RecordInterface {
  protected Node document=null;

  public static String cr=String.valueOf((char)13);
  public static String lf=String.valueOf((char)10);
  private String JOpacID="0";
  protected String bid=null;
  private String descrizioneTipo;
  private int _livello =0;  // serve a gestire le notizie per linkup e down
  protected Charset _charset=null;

  public Vector<RecordInterface> linkUp=null;
  public Vector<RecordInterface> linkDown=null;
  public Vector<RecordInterface> linkSerie=null;
  
  private Vector<TokenWord> tw;
  
  //public static String[] channels={"ANY","AUT","TIT","NUM","LAN","MAT","DTE","SBJ","BIB","INV","CLL","ANY","JID","ABS","NAT"};

  
  public RecordInterface clone() {
	  try {
		return RecordFactory.buildRecord("0", this.toString().getBytes(), this.getTipo(), this.getLivello());
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchMethodException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InstantiationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
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
  private String indicatorLength="0";

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
  

  public String getIndicatorLength() {
	  return indicatorLength;
  }
  public void setIndicatorLength(String lCode) {
	  indicatorLength=lCode;
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
   *    invece degli html. Il resto verra' fatto fuori.
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
  
  public void addTag(Vector<Tag> newTags) {
      //dati.addAll(newTags);
  }
  
  public int getLivello() {
    return _livello;
  }

  public String getTipo() {
    return descrizioneTipo;
  }

  public void setJOpacID(String l) {
    JOpacID=l;
  }

  public String getJOpacID() {
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
      if(bid==null) {return getJOpacID();}
      else return bid.getTextContent();
  }

  public String toString() {
    return XMLHelper.toString(document);
  }
  
  public String toReadableString() {
	  return toString();
  }
  
  public String toXML() {
	  return toString();
  }

  public abstract Vector<String> getAuthors(); // Autori
  public abstract Vector<SubjectInterface> getSubjects(); // Soggetti
  public abstract Vector<ClassificationInterface> getClassifications(); // Classificazioni
  public abstract Vector<String> getEditors(); // Editori
  public abstract String getEdition(); //Edizione
  public abstract String getPublicationPlace(); // Luogo di pubblicazione
  public abstract String getPublicationDate(); // Data di pubblicazione
  public abstract String getTitle(); // Titolo
  public abstract String getISBD(); // ISBD
  public abstract Vector<BookSignature> getSignatures(); // Localizzazioni
  public abstract String getAbstract(); // Abstract
  public abstract String getDescription(); // Descrizione fisica

  public void init(byte[] in) throws SAXException, IOException {
	  if(in==null) return;
	  String ins=new String(in);
	  tw = new Vector<TokenWord>();
	  if (ins != null && ins.length() > 0) {
			InputSource is = new InputSource(new StringReader(ins));
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

  protected void XMLCostruttore(byte[] notizia,Charset charset, String dTipo,int livello) throws SAXException, IOException {
    descrizioneTipo=dTipo;
    _charset=charset;
    init(notizia);
    _livello=livello+1;  //  salva il livello creato per evitare ricorsione
  }
  
  protected void XMLCostruttore(byte[] notizia, String dTipo,int livello) throws SAXException, IOException {
	    descrizioneTipo=dTipo;
	    init(notizia);
	    _livello=livello+1;  //  salva il livello creato per evitare ricorsione
	  }
  
  public String getTerminators() {
    return null;
  }
  
  public XML(byte[] notizia,String dTipo,String livello) throws NumberFormatException, SAXException, IOException {
    this.XMLCostruttore(notizia,dTipo,Integer.parseInt(livello));
  }

  public XML(byte[] notizia,String dTipo) throws SAXException, IOException {
    this.XMLCostruttore(notizia,dTipo,0);
  }
  
  //public abstract RecordReader getRecordReader(BufferedReader f);
  public abstract RecordReader getRecordReader(InputStream dataFile) throws UnsupportedEncodingException;


  public XML() {
      document=null;
      tw=new Vector<TokenWord>();
  }
  
	public Hashtable<String, List<Tag>> getRecordMapping() {
		return null;
	}

	public String getRecordTypeDescription() {
		return "General XML text";
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
	
	public void setTitle(String title)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}
	
	public void setTitle(String title, boolean significant)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void addAuthor(String author)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void addClassification(ClassificationInterface data)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void addComment(String comment)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void addPublisher(String publisher) throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void addPart(RecordInterface part)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void addPartOf(RecordInterface partof)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void addSerie(RecordInterface serie)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void addSubject(SubjectInterface subject)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void setAbstract(String abstractText)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void setDescription(String description)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void setEdition(String edition)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void setISBD(String isbd)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void setPublicationDate(String publicationDate)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void setPublicationPlace(String publicationPlace)  throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}

	public void setStandardNumber(String standardNumber, String codeSystem) throws JOpac2Exception {
		throw new JOpac2Exception("No such method defined!");
	}
	
	public String toEncapsulatedRecordFormat() {		
		return toString();
	}
	
	public String getPublicationNature(){ return  null;}
	
	public String getPrice() {
		return null;
	}
	
	public String getField(String field) {
		/** TODO
		 * modificare in modo che si possa specificare tutto il path
		 */
		if(field.contains("/")) field=field.substring(field.lastIndexOf("/")+1);
		return getNodeContent(field);
	}
	
	public String[] getChannels() {
		Vector<TokenWord> v=XMLHelper.exploreData(document);
		
		String[] ch=new String[v.size()+ISO2709Impl.channels.length];
		for(int i=0;i<ISO2709Impl.channels.length;i++) ch[i]=ISO2709Impl.channels[i];
		for(int i=0;i<v.size();i++) ch[i+ISO2709Impl.channels.length]=v.elementAt(i).getTag();
		ch[0]="ANY";
		return ch;
	}
	
	public String getLanguage() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLanguage(String language) throws JOpac2Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addElectronicVersion(ElectronicResource electronicResource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ElectronicResource[] getElectronicVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeElectronicVersion(ElectronicResource electronicResource) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ElectronicResource getElectronicVersion(String type) {
		ElectronicResource el=null;
		
		return el;
	}
	
	@Override
	public void buildRecord(int id, byte[] rawdata, int level) throws Exception {
		init(rawdata);
		
	}
	
	@Override
	public void setDelimiters(Delimiters delimiters) {
		// non fa niente
		
	}

	@Override
	public Delimiters getDelimiters() {
		return null;
	}
}