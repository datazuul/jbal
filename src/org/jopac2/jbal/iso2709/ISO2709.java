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
*/

/***
 *Modificato:
 * 01/10/2003 - RT
 *              Aggiunto metodo per salvare un record in formato ISO2709
 */

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.abstractStructure.Delimiters;
import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.TokenWord;
import org.jopac2.utils.Utils;

public abstract class ISO2709 implements RecordInterface {
	  protected Charset _charset=null;
	  private DirEntry[] Directory;
	  public String inString;
	  public Vector<Tag> dati;
	  public Vector<TokenWord> tw;
	  public static String[] channels={"ANY","AUT","TIT","NUM","LAN","MAT","DTE","SBJ","BIB","INV","CLL","ANY","JID","ABS","NAT"};

	  public byte cr=13;
	  public byte lf=10;
	  public Delimiters delimiters=new Delimiters();
	  private String JOpacID="0";
	  protected String bid=null;
	  protected String descrizioneTipo;
	  private String charset="utf-8";
	  private int _livello =0;  // serve a gestire le notizie per linkup e down

	  public Vector<RecordInterface> linkUp=null;
	  public Vector<RecordInterface> linkDown=null;
	  public Vector<RecordInterface> linkSerie=null;
	  
	  /**
	   * 01/10/2003 - RT
	   *          Definisce il tipo di record e lo stato.
	   */
	    protected long recordlength=-1;
	    protected String recordStatus="n";
	    protected String implementationCodes="    "; // 4 char (recordType, recordBiblioLevel, hierarchicalLevel , 'blanck')
	    protected String recordType="a";
	    protected String recordBiblioLevel="m";
	    protected String recordHierarchicalLevel="#"; // hierarchical relationship undefined in UNIMARC
	    protected String indicatorLength="2";  // One numeric digit giving the length of the indicators. This is invariably 2 in UNIMARC.
	    protected String subfieldIdentifierLength="2"; // One numeric digit giving the length of the subfield identifier; e.g. '$a'. This is invariably 2 in UNIMARC.
	    protected String baseAddressOfData="     "; // 5 bytes
	    protected String additionalRecordDefinition="   "; // 3 bytes
//	    protected String directoryMap="    "; // 4 bytes  // R.T.: 29/07/2011 see init()
	    protected String recordCharacterEncodingScheme="a"; // 1 byte, a=utf8, blank=normal


	
  @Override
	public void addAuthorsFromTitle() throws JOpac2Exception {
		// TODO Auto-generated method stub
		
	}

public String getCharacterEncodingScheme() {
		return recordCharacterEncodingScheme;
	}

	public void setCharacterEncodingScheme(
			String recordCharacterEncodingScheme) {
		this.recordCharacterEncodingScheme = recordCharacterEncodingScheme;
	}

public String getHierarchicalLevel() {
		return recordHierarchicalLevel;
	}

	public void setHierarchicalLevel(String recordHierarchicalLevel) {
		this.recordHierarchicalLevel = recordHierarchicalLevel;
	}

  public RecordInterface clone() {
	  RecordInterface ma=null;
	  try {
		  ma=RecordFactory.buildRecord("0", this.toString().getBytes(), this.getTipo(), this.getLivello());
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
	  return ma;
  }
  
  public void destroy() {
  	if(dati!=null) {dati.clear();dati=null;tw.clear();tw=null;}
  	if(linkUp!=null) linkUp.clear();
  	if(linkDown!=null) linkDown.clear();
  	if(linkSerie!=null) linkSerie.clear();
  	Directory=null;
  	inString=null;
    //System.gc();
  }
  

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
  
  public String getIndicatorLength(){
  	return indicatorLength;
  }
  
  public void setIndicatorLength(String lCode){
  	indicatorLength = lCode;
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

  public Vector<RecordInterface> getHasParts() {
    return linkDown;
  }

  public Vector<RecordInterface> getIsPartOf() {
    return linkUp;
  }

  public Vector<RecordInterface> getSerie() {
    return linkSerie;
  }

  public void addTag(Tag newTag) {
      dati.addElement(newTag);
  }
  
  public void addTag(Vector<Tag> newTags) {
      dati.addAll(newTags);
  }
  
  /**
   * todo: colori meglio e vari per livelli
   */

  private String colour="lightgray";

  public int getLivello() {
    return _livello;
  }

  public void setColour(String c) {
    colour=c;
  }

  public String getColour() {
    return colour;
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

  public String getBid() {
    if(bid==null) {return getJOpacID();}
    if(bid.contains("/")){
		String[] bidparts = bid.split("/");
		bid = bidparts[bidparts.length-2]+bidparts[bidparts.length-1];
	}
    return bid;
  }
  
  private byte[] getTag(byte[] record, DirEntry dirEntry) throws UnsupportedEncodingException {
	    byte[] t=new byte[dirEntry.getFieldLength()];
	    System.arraycopy(record,dirEntry.getStartPosition()-1,t,0,dirEntry.getFieldLength());
//	    t=my_trim(t);
	    return(t);
  }

//  private String getTag(String record, String DirEntry) {
//	//x.substring(start-1,start+len-1)
//	  String t=DirEntry.substring(3,7);
//    int FieldLength = Integer.parseInt(t);
//    t=DirEntry.substring(7,12);
//    int Starting = Integer.parseInt(t) + 1;
//    t=record.substring(Starting-1,Starting+FieldLength-1);
//    //t = Utils.mid(record, Starting, FieldLength);
//    t=my_trim(t);
//    return(t);
//  }

  public Vector<Tag> getTags() {
    return dati;
  }
  
  public int getRows() {
      return dati.size();
  }

  public Tag getFirstTag(String tag) {
    Tag r;
    try {
      r=(getTags(tag).elementAt(0));
    }
    catch (Exception e) {
      return null;
//      return e.getMessage();
    }
    return r;
  }

//  /**
//   * 09/03/2009 First get Tag, then get firstElement from Tag
//   * @deprecated
//   * @param tagString
//   * @param element
//   * @return
//   */
//  public String getFirstElement(String tagString,String element) {
//    String r;
//    try {
//      r=(String)(getElement(tagString,element).elementAt(0));
//    }
//    catch (Exception e) {
//      return "";
////      return e.getMessage();
//    }
//    return r;
//  }
  
  
//  /***
//   * 09/03/2009 First get Tag, then get firstElement from Tag
//   * @deprecated
//   * 16/09/2004 - R.T.
//   *     Dato un vettore di tag e il codice di un elemento
//   *     ritorna un vettore che contiene tutti gli elementi dei tag
//   *     del vettore.
//   */
//public Vector<String> getElement(Vector<String> tags, String elements) {
//      Vector<String> v=new Vector<String>();
//      
//      for(int i=0;i<tags.size();i++) {
//          v.addAll(getElement((String)tags.elementAt(i),elements));
//      }
//      return v;
//  }

//  /**
//   * Rivedere dove viene usato qs metodo
//   * @deprecated
//   * @param tag
//   * @return
//   */
//  public String getTagElements(String tag) {
//  	Vector<Tag> myTag=getTags(tag);
//  	String t,r="";
//  	
//  	for(int i=0;i<myTag.size();i++) {
//  		t=myTag.elementAt(i).toString().substring(3);
//  		t=t.replaceAll(rt," ");
//  		t=t.replaceAll(ft," ");
//  		t=t.replaceAll(dl," ");
//  		r+=t+"\n";
//  	}
//  	myTag.clear();
//  	return r;
//  }
  
//  /**
//   * @deprecated
//   * @param tagString
//   * @param elements
//   * @return
//   */
//public Vector<String> getElement(String tagString, String elements) {
//	/**
//	 * 4/2/2008 - R.T. modificato in modo che se elements==null || elements.lenght()==0 e non ci sono dl
//	 * 			  ritorna un unico elemento
//	 */
//	
//    Vector<String> r=new Vector<String>();
//    Vector<String> t=new Vector<String>();
//    
//    if((elements==null)||(elements.length()==0)) {
//    	t.addElement(tagString);
//    }
//    else {
//	    String ctk;
//	    StringTokenizer tk=new StringTokenizer(tagString,dl);
//	    while(tk.hasMoreTokens()) {
//	      ctk=tk.nextToken();
//	      r.addElement(ctk);
//	    }
//	    // Mumble, fin qui ok, ho un vettore con tutti gli elementi, ma da qui in poi
//	    // rifaccio (R.T.)
//	    int cp=0;
//	    for(int i=0;i<r.size();i++) {
//	      // OK, prendo l'elemento
//	      String element=((String)r.elementAt(i)).substring(0,1);
//	      // in che indice si trova?
//	      cp=elements.indexOf(element);
//	      if(cp!=-1) {  // se l'elemento c'e'
//	        // aggiungi l'elemento al nuovo vettore
//	        t.addElement(((String)r.elementAt(i)).substring(1));
//	      }
//	    }
//	    
//	    r.clear();
//	
//	    // dovevano diventare una sola riga, se c'erano piu' elementi validi?
//    }
//    return(t);
//  }

/**
 * Rimuove tutti i tag con codice tag
 * @param tag
 */
/*public void removeTag(String tag) {
 //if(tag.length()!=3) return;
 
 for(int i=0;i<dati.size();i++) {
      if(dati.elementAt(i).getTagName().equals(tag)) {
        dati.removeElementAt(i);
        i--; // per far in modo che dopo lo shift degli elementi venga ricontrollato questo indice
      }
    }
}*/

/**
 * Rimuove tutti i tag nell'area indicata (nella forma, ad esempio, 9xx)
 * @param area
 */
public void removeArea(String area) {
 if(area.length()!=3) return;
 
 area=area.toLowerCase().replaceAll("x", "");
 
 if(area.length()==0) return;
 
 for(int i=0;i<dati.size();i++) {
      if(dati.elementAt(i).getTagName().equals(area)) {
        dati.removeElementAt(i);
        i--; // per far in modo che dopo lo shift degli elementi venga ricontrollato questo indice
      }
    }
}

public Vector<Tag> getTags(String tag) {
    Vector<Tag> r=new Vector<Tag>();
    
    for(int i=0;i<dati.size();i++) {
      if(dati.elementAt(i).getTagName().equals(tag)) {
        if(!r.contains(dati.elementAt(i))) r.addElement(dati.elementAt(i));
      }
    }
    return(r);
  }

  /**
   * @deprecated
   * @param t
   * @return
   */
  @SuppressWarnings("unused")
private String my_trimo(String t) {
    while(t.endsWith(delimiters.getDl())||t.endsWith(delimiters.getFt())||t.endsWith(delimiters.getFt())) {
      t=t.substring(0,t.length()-1);
    }
    return(t);
  }

  @Override
  public String toString() {
    try {
		return toISO2709();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
  }
  
  @Override
  public String toReadableString() {
	  StringBuffer r=new StringBuffer();
	  r.append("Record status: "+recordStatus+"\n");
	  r.append("Record type: "+recordType+"\n");
	  r.append("Record biblio level: "+recordBiblioLevel+"\n");
	  r.append("Record hierarchical level code: "+recordHierarchicalLevel+"\n");
	  r.append("Record char encoding: "+recordCharacterEncodingScheme+"\n");
	  
	  Collections.sort(dati);
	  
	  for(int i=0;i<dati.size();i++) {
		  if(!delimiters.getDl().equals("^"))
			  r.append(dati.elementAt(i).toString().replaceAll("["+delimiters.getDl()+"]", "^")+"\n");
		  else 
			  r.append(dati.elementAt(i).toString()+"\n");
	  }
	  return r.toString();
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
  public abstract Vector<RecordInterface> getLinked(String tag) throws JOpac2Exception;

  public String quote(String t) {
    String c;

    String s = "";
    for(int k = 1;k<t.length()+1;k++) {
    	c=t.substring(k-1,k);
      //c = Utils.mid(t, k, 1);
      if (c.equals(delimiters.getDl())==true) {
    	  s=s+"["+t.substring(k,k+1)+"]";
        //s = s + "[" + Utils.mid(t, k + 1, 1) + "]";
        k = k + 1;
      } else {
        if (c.equals(String.valueOf((char)27))==true) {
          if(t.substring(k,k+1).equals("I")) {//   if (((String)Utils.mid(t, k + 1, 1)).equals("I")) {
            s = s + "*";
          }
          k = k + 1;
        } else {
          if (c.equals(delimiters.getFt())==false) {
            s = s + c;
          }
        }
      }
    }
    return(s);
  }

  public void setTerminator(byte rt,byte ft, byte dl) {
    delimiters.setRt(rt);
    delimiters.setFt(ft);
    delimiters.setDl(dl);
  }
  
  
  /**
 * @param stringa
 * @throws Exception
 */
public void init(byte[] stringa, Charset charset) throws Exception {
//    inString=stringa;
	_charset=charset;
    if(stringa==null || stringa.length==0) return;

    try {
    	/**
    	 * http://www.ifla.org/VI/3/p1996-1/uni.htm
    	 * 
    	 
			Record length						5			0-4
			Record status						1			5
			Implementation codes				4			6-8
				Character encoding scheme			1			9 // aggiunto da http://www.loc.gov/marc/specifications/speccharconversion.html
			Indicator length					1			10
			Subfield identifier length			1			11
			Base address of data				5			12-16
			Additional record definition		3			17-19
			Directory map						4			20-23
    	 */
    	
    	
      recordlength = Long.parseLong(new String(stringa,0,5));
      if(recordlength > 0) {
//    	int stringLength=stringa.length;
//    	int byteLength=stringa.getBytes().length;
    	
    	
    	/**
    	 * RT: 22/07/2011
    	 * Checked against ISO 2709:2008. Length is octect (byte).
    	 */
//    	boolean byteCoded=true;  	
    	//if(recordlength==byteLength && byteLength!=stringLength) byteCoded=true;
    	
    	// record status
        recordStatus = new String(stringa,5,1); //stringa.substring(5,6);
        // 4 bytes, are "Implementation Codes"
        implementationCodes=new String(stringa,6,4); //stringa.substring(6,10);
        recordType = implementationCodes.substring(0,1);
        recordBiblioLevel = implementationCodes.substring(1,2);
        recordHierarchicalLevel = implementationCodes.substring(2,3);
        recordCharacterEncodingScheme=implementationCodes.substring(3,4);
        
        // indicatorLength
        indicatorLength = new String(stringa,10,1); //stringa.substring(10,11);
        // Subfield identifier length
        subfieldIdentifierLength = new String(stringa,11,1); //stringa.substring(11,12);
        
        // base address of data
        baseAddressOfData=new String(stringa,12,5); // stringa.substring(12,17);
        
        // additional record definition
        additionalRecordDefinition=new String(stringa,17,3); //stringa.substring(17,20);
        
        // directory map
//        directoryMap=new String(stringa,20,4); //stringa.substring(20,24);
        
        int entryLengthOfFieldLength=-1;
        try {entryLengthOfFieldLength=Integer.parseInt(new String(stringa,20,1));} catch(Exception e) {entryLengthOfFieldLength=4;}
        
        int entryStartPositionLength=-1;
        try {entryStartPositionLength=Integer.parseInt(new String(stringa,21,1));} catch(Exception e) {entryStartPositionLength=5;}
        
        int entryImplementationDefinedLength=-1;
        try {entryImplementationDefinedLength=Integer.parseInt(new String(stringa,22,1));} catch(Exception e) {entryImplementationDefinedLength=0;}
        @SuppressWarnings("unused")
		int entryReservedFutureUse=stringa[24];
        
        int entryLength = entryLengthOfFieldLength + entryStartPositionLength + entryImplementationDefinedLength + 3; // 3 = tag length

        int readed = 24;
        int ndir = 0;
        
        byte[] recordBytes=stringa;

        while (stringa[readed]!=delimiters.getByteFt()) {
        	String t=new String(recordBytes, readed, entryLength);
        	
    		readed=readed+entryLength;
            Directory[ndir] = new DirEntry(t,entryLengthOfFieldLength, entryStartPositionLength, entryImplementationDefinedLength,entryReservedFutureUse);
            ndir = ndir + 1;
        }
        
        readed++;

        byte[] record=new byte[recordBytes.length-readed];
        System.arraycopy(stringa, readed, record, 0, record.length);
        for(int z = 0; z<= ndir - 1;z++) {
        	
//          byte[] s=my_trim(getTag(record, Directory[z]));
        	byte[] s=getTag(record, Directory[z]);
          String tag=Directory[z].getTag();

          dati.addElement(new Tag(tag, s, charset, delimiters));
          if(tag.equals("001")) {
            bid=new String(s,charset);
          }
        }
      }
    }
    catch (Exception e) {
      // ops, non e' Unimarc? forse e' codificato, riproviamoci
//      System.out.print("Codificato: "+new String(stringa));
    	
        
      /** todo
       * Fare meglio questa parte, initCoded digerisce tutto, deve dare una eccezione
       * se non e' codificato. Trovare un modo per verificare se e' codificato.
       */
    	if(stringa[0]=='4') {
    		initCoded(stringa);
    	}
    	else {
//    		init(inString);
    		throw e;
    	}
      
    }
//    System.out.println("OK: "+stringa);
  }

  public void initCoded(byte[] stringa) {
	  Tag t=new Tag(new String(stringa,0,3), stringa, Charset.forName("utf8"), delimiters);
	  Tag n=null;
	  
	  
	  for(int i=0;t.getFields()!=null && i<t.getFields().size();i++) {
		  if(t.getFields().elementAt(i).getFieldCode().equals("1")) {
			  if(n!=null) this.addTag(n);
			  String c=t.getFields().elementAt(i).getContent();
			  String tn=c.substring(0,3);
			  n=new Tag(tn);
			  if(tn.startsWith("00")) {
				  try {
					n.setRawContent(c.substring(3));
				} catch (JOpac2Exception e) {
					e.printStackTrace();
				}
			  }
			  else {
				  if(c.length()>3) n.setModifier1(c.charAt(3));
				  if(c.length()>4) n.setModifier2(c.charAt(4));
			  }
		  }
		  else {n.addField(t.getFields().elementAt(i));}
	  }
	  if(n!=null) this.addTag(n);
	  
//    inString=stringa;
//    try {
//      StringTokenizer tk=new StringTokenizer(stringa,delimiters.getFt()+delimiters.getByteDl()+delimiters.getByteRt());
//      String ctk;
//      String s="";
//
//      while(tk.hasMoreTokens()) {
//        ctk=tk.nextToken();
////        Utils.Log(Utils.LOG_DEBUG,"Coded token: "+ctk);
//        if(ctk.substring(0,1).equals("1")) {
//          if(s.length()>0) {
//        	  if(!s.startsWith(delimiters.getDl()))
//        		  dati.addElement(new Tag(s,delimiters));
//            if(s.substring(0,3).equals("001")) {
//              bid=s.substring(3);
//            }
//            s="";
//            s+=ctk.substring(1);
//          }
//        }
//        else {
//          s+=delimiters.getDl()+ctk;
//        }
//      }
//      if(s.length()>0 && !s.startsWith(delimiters.getDl())) {
//        dati.addElement(new Tag(s,delimiters));
//      }
//    }
//    catch (Exception e) {
//      // aaahhhh, se arrivo qua vuol dire che non ci capisco una mazza,
//      // o il record e' proprio fallato
//      e.printStackTrace();
////      Utils.Log(1,stringa);
//    }
  }

  public void iso2709Costruttore(byte[] notizia,Charset charset,String dTipo,int livello) throws Exception {
    Directory=new DirEntry[1000];
    dati=new Vector<Tag>();
    tw=new Vector<TokenWord>();
    descrizioneTipo=dTipo;
    _charset=charset;
    if(notizia!=null)
    	init(notizia, charset);

    _livello=livello+1;  //  salva il livello creato per evitare ricorsione
  }

  /**
   * http://www.ifla.org/VI/3/p1996-1/uni4.htm
   * Indicator 1: blank (not defined)
   * Indicator 2: Note Indicator (0 default)
   * 
   * Codes only with Embedded fields technique
   * 
   */
  @Override
  public String toEncapsulatedRecordFormat() {
	  StringBuffer r=new StringBuffer();
	  
	  r.append(" 0"); // indicators with no note default
	  for(int i=0;dati!=null && i<dati.size();i++) {
		  Tag tag=dati.elementAt(i);
		  if(tag!=null) {
			  r.append(delimiters.getDl()+"1"+tag.toString());
		  }
	  }
	  
	  return r.toString();
  }
  
  
  /**
   * 28/12/2009 - RT 
   *            Prende una istanza ISO2709 e la salva in formato XML MARC restituendo una stringa
   *            
   * http://www.bncf.firenze.sbn.it/progetti/unimarc/slim/documentation/unimarcslim.html
   *        SECONDO ME LO SCHEMA BNCF E' MORTO, ho mandato una mail il 28/12/2009
   * http://www.loc.gov/standards/marcxml/
   * http://didattica.spbo.unibo.it/bibliotime/num-viii-3/bassi.htm
   * http://www.rba.ru/rusmarc/
   * http://www.loc.gov/standards/
   *            
   * @throws Exception 
   */
  @Override
  public String toXML() throws Exception {
	  /**
  	 * http://www.ifla.org/VI/3/p1996-1/uni.htm
  	 * 
  	 
			Record length						5			0-4
			Record status						1			5
			Implementation codes				4			6-9
			Indicator length					1			10
			Subfield identifier length			1			11
			Base address of data				5			12-16
			Additional record definition		3			17-19
			Directory map						4			20-23

  	 */
    String r=null;
	try {
		String leader = recordStatus + 
			recordType + 
	        recordBiblioLevel + 
	        recordHierarchicalLevel +
	        recordCharacterEncodingScheme +
	        indicatorLength +
	        subfieldIdentifierLength;
	
	    int ba=0;
	    String directory="";
	    String data="";
	
	    int maxFL=getMaxFieldLength();
	    int entryLength=maxFL+maxFL+1+3; // 3 = tag length
	    
	    long temp = 24 + entryLength * getRows() + 1;             // base address of data
	    leader = leader.concat(Utils.pad("00000",temp));
	    
	    leader=leader.concat(additionalRecordDefinition);                  // implementation defined
	    
	    leader=leader.concat(Integer.toString(maxFL)+Integer.toString(maxFL+1)+"0 ");                 // entry map, maxFL and maxFL+1 digit padding, see in the for loop beyond 
	    String lengthPad=pad0(maxFL);
	    String posPad=lengthPad+"0";    
	    
	    removeEmptyTags();
	    
	    Vector<Tag> t=this.getTags();
	    Collections.sort(t);

	    ba+=this.getRows()*entryLength;
	    long c=0;
	    for(int z=0;z<t.size();z++) {
	    	String k=t.elementAt(z).toString();
	    	int kLength=k.getBytes().length;
	        directory=directory.concat(k.substring(0,3)); // 3 bytes (tag length)
	        directory=directory.concat(Utils.pad(lengthPad,kLength-2)); // modified for byte counting! pad to 4 digit (see entry map definition above).
	        directory=directory.concat(Utils.pad(posPad,c)); // pad to 5 digit (see entry map definition above)
	        c+=kLength-2;  // is always full lenght - tag length (3) + 1 for ft at end of each data. Total = kLength - 3 +1
	        ba+=kLength-2; // is always full lenght - tag length (3) + 1 for ft at end of each data. Total = kLength - 3 +1
	        data=data+t.elementAt(z).toXML();
	    }
	    
	        
	    ba = 5 + leader.length() + ba + 2; // 5 digit for record length + directory + 1 ft + 1 rt
	    leader=Utils.pad("00000", ba).concat(leader); // it was ba-1, I don't understand why :-(	    
	    
	    r="<record>\n" + 
	    	"\t<leader>"+leader+"</leader>\n" + 
//	    	directory + 
	    	data + 
	    	"\n</record>";
	}
	catch(java.lang.StringIndexOutOfBoundsException e) {
		r=null;
	}
	catch(Exception e) {
		throw new Exception("ISO2709.toXML Exception: JID="+JOpacID+" Type="+this.getTipo());
	}
    return r;
}
  
  
  private void removeEmptyTags() {
	for(int i=dati.size()-1;i>=0;i--) {
		if(isEmpty(dati.elementAt(i))) dati.removeElementAt(i);
	}
  }

private boolean isEmpty(Tag tag) {
	boolean r=true;
	tag.removeEmptyFields();
	if(tag.getFields()!=null && tag.getFields().size()>0) r=false;
	if(r && tag.getRawContent()!=null && tag.getRawContent().trim().length()>0) r=false;
	return r;
}

/**
   * 01/10/2003 - RT 
   *            Prende una istanza ISO2709 e la salva in formato ISO2709 restituendo una stringa
   * @throws Exception 
   */
  protected String toISO2709() throws Exception {
	  /**
  	 * http://www.ifla.org/VI/3/p1996-1/uni.htm
  	 * 
  	 
			Record length						5			0-4
			Record status						1			5
			Implementation codes				4			6-9
			        For Unimarc they are:
					6 = type
					7 = bibliographic level
					8 = hierarcical level
					9 = undefined
			Indicator length					1			10   	One numeric digit giving the length of the indicators. This is invariably 2 in UNIMARC.
			Subfield identifier length			1			11		One numeric digit giving the length of the subfield identifier; e.g. '$a'. This is invariably 2 in UNIMARC.
			Base address of data				5			12-16
			Additional record definition		3			17-19
			Directory map						4			20-23

  	 */
    String r=null;
	try {
	  	String leader = recordStatus + 
	  					recordType + 
	                    recordBiblioLevel + 
	                    recordHierarchicalLevel +
	                    recordCharacterEncodingScheme +
	                    indicatorLength +
	                    subfieldIdentifierLength ;
	
	    int ba=0;
	    String directory="";
	    String data="";
//	    ArrayList<Byte> data=new ArrayList<Byte>();
//	    ArrayList<Byte> directory=new ArrayList<Byte>();
//	    ArrayList<Byte> leader=new ArrayList<Byte>();
	    
//	    addStringAsByte(leader,_leader);
	    
	    Delimiters outputDelimiters=new Delimiters(); // use always standard delimiters
	    
	    
	    int maxFL=getMaxFieldLength();
	    int entryLength=maxFL+maxFL+1+3; // 3 = tag length
	    
	    long temp = 24 + entryLength * getRows() + 1;             // base address of data
//	    addStringAsByte(leader,Utils.pad("00000",temp));
	    leader = leader.concat(Utils.pad("00000",temp));
//	    addStringAsByte(leader,additionalRecordDefinition);	 // implementation defined
	    leader=leader.concat(additionalRecordDefinition);                  // implementation defined
	    
	    
	    
	    leader=leader.concat(Integer.toString(maxFL)+Integer.toString(maxFL+1)+"0 ");                 
//	    addStringAsByte(leader,Integer.toString(maxFL)+Integer.toString(maxFL+1)+"0 "); // entry map, maxFL and maxFL+1 digit padding, see in the for loop beyond 
	    String lengthPad=pad0(maxFL);
	    String posPad=lengthPad+"0";    
	    
	    removeEmptyTags();
	    
	    Vector<Tag> t=this.getTags();
	    Collections.sort(t);

	    ba+=this.getRows()*entryLength;
	    long c=0;
	    for(int z=0;z<t.size();z++) {
//	    	byte[] kb=t.elementAt(z).toBytes();
//	    	int kLength=kb.length;
//	        directory=directory.concat(new String(kb,0,3)); // 3 bytes (tag length)

	    	String k=t.elementAt(z).toString();
	    	int kLength=k.getBytes("utf-8").length;
	    	String tag=k.substring(0,3);
	        directory=directory.concat(tag); // 3 bytes (tag length)
	        
	        directory=directory.concat(Utils.pad(lengthPad,kLength-2)); // modified for byte counting! pad to 4 digit (see entry map definition above).
	        directory=directory.concat(Utils.pad(posPad,c)); // pad to 5 digit (see entry map definition above)
	        c+=kLength-2;  // is always full lenght - tag length (3) + 1 for ft at end of each data. Total = kLength - 3 +1
	        ba+=kLength-2; // is always full lenght - tag length (3) + 1 for ft at end of each data. Total = kLength - 3 +1
	        data=data+k.substring(3)+outputDelimiters.getFt(); // get data starting at position 3 (ommitting tag) + one byte for ft
	    }
	    data=data+outputDelimiters.getRt();
	        
	    ba = 5 + leader.length() + ba + 2; // 5 digit for record length + directory + 1 ft + 1 rt
	    
	    leader=Utils.pad("00000", ba).concat(leader); // it was ba-1, I don't understand why :-(
	    r=leader+directory+outputDelimiters.getFt()+data;
	}
	catch(java.lang.StringIndexOutOfBoundsException e) {
		r=null;
	}
	catch(Exception e) {
		throw new Exception("ISO2709.toString Exception: JID="+JOpacID+" Type="+this.getTipo());
	}
    return r;
}
  
  private void addStringAsByte(ArrayList<Byte> arrayList, String dataToAdd) throws UnsupportedEncodingException {
	byte[] t=dataToAdd.getBytes("utf-8");
	for(int i=0;i<t.length;i++) arrayList.add(new Byte(t[i]));
}

private String pad0(int n) {
	  String r="";
	for(int i=0;i<n;i++) r+="0";
	return r;
}

private int getMaxFieldLength() {
	int r=9999;
    for(int z=0;z<dati.size();z++) {
    	int kLength=dati.elementAt(z).toBytes().length;
        if(kLength>r) r=kLength;
    }
    r=Integer.toString(r).length();
	return r;
}

public Delimiters getTerminators() {
	  System.out.append("prende i terminatori");
    return delimiters;
  }
  
  public ISO2709(byte[] notizia, Charset charset, String dTipo, String livello) throws Exception {
    this.iso2709Costruttore(notizia,charset,dTipo,Integer.parseInt(livello));
  }

  public ISO2709(byte[] notizia,Charset charset,String dTipo) throws Exception {
    this.iso2709Costruttore(notizia,charset,dTipo,0);
  }
  
  //public abstract RecordReader getRecordReader(InputStream f);
  public RecordReader getRecordReader(InputStream dataFile) throws UnsupportedEncodingException {
	  return getRecordReader(dataFile,charset);
  }
  public abstract RecordReader getRecordReader(InputStream dataFile, String charset) throws UnsupportedEncodingException;


  public ISO2709() {
      dati=new Vector<Tag>();
      tw=new Vector<TokenWord>();
  }

  
  public Enumeration<TokenWord> getItems() {
		tw.removeAllElements();
//		String ctk;
//		boolean unico = true;

		for (int z = 0; z < dati.size(); z++) {
			String tag = dati.elementAt(z).getTagName();
			
			Vector<Field> fields=dati.elementAt(z).getFields();

			if (fields!=null && fields.size()>0) {
				for(int i=0;i<fields.size();i++) {
					tw.addElement(new TokenWord(fields.elementAt(i).getContent(), tag, fields.elementAt(i).getFieldCode()));
				}
			}
			else {
				tw.addElement(new TokenWord(dati.elementAt(z).getRawContent(), tag, ""));
			}
//				unico = false;

//			StringTokenizer tk = new StringTokenizer(valore_tag, delimiters.getDl());
//			while (tk.hasMoreTokens()) {
//				String valore = "";
//
//				ctk = tk.nextToken().trim();
//				try {
//					if (ctk.length() > 0) {
//						String de = "";
//						if (!unico) {
//							de = ctk.substring(0, 1);
//							valore = ctk.substring(1);
//						} else {
//							de = "";
//							valore = ctk;
//						}
//						tw.addElement(new TokenWord(valore, tag, de));
//					}
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			}
		}

		return tw.elements();
	}
  
  @Override
  public String getPublicationNature(){ return  recordType;}
  
  @Override
	public String getField(String field) {
		return null;
	}
	
	@Override
	public void setBase64Image(String base64EncodedImage) throws JOpac2Exception {
		throw new JOpac2Exception("Unimplemented method");
	}
	
	@Override
	public void buildRecord(int id, byte[] rawdata, int level) throws Exception {
		destroy();
		dati=new Vector<Tag>();
	    tw=new Vector<TokenWord>();
	    Directory=new DirEntry[1000];
		init(rawdata,_charset);
		
	}
	
	@Override
	public void setDelimiters(Delimiters delimiters) {
		this.delimiters=delimiters;
		
	}

	@Override
	public Delimiters getDelimiters() {
		return delimiters;
	}

}