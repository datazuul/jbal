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
import java.util.*;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.abstractStructure.Delimiters;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.TokenWord;
import org.jopac2.utils.Utils;

public abstract class ISO2709 implements RecordInterface {
  public Vector<Tag> dati;
  public Vector<TokenWord> tw;
  public static String[] channels={"ANY","AUT","TIT","NUM","LAN","MAT","DTE","SBJ","BIB","INV","CLL","ANY","JID","ABS","NAT"};


  protected String rt=String.valueOf((char)0x1d);
  protected String ft=String.valueOf((char)0x1e);
  public String dl=String.valueOf((char)0x1f);        //' delimiter
  public String cr=String.valueOf((char)13);
  public String lf=String.valueOf((char)10);
  public Delimiters delimiters=new Delimiters(rt,ft,dl);
  private long JOpacID=0;
  protected String bid=null;
  protected String descrizioneTipo;
  private int _livello =0;  // serve a gestire le notizie per linkup e down

  public Vector<RecordInterface> linkUp=null;
  public Vector<RecordInterface> linkDown=null;
  public Vector<RecordInterface> linkSerie=null;
  
  public RecordInterface clone() {
	  RecordInterface ma=null;
	  try {
		  ma=RecordFactory.buildRecord(0, this.toString(), this.getTipo(), this.getLivello());
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
	  return ma;
  }
  
  public String getDl() {
	  return dl;
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

  private String[] Directory;
  public String inString;

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

  public String getBid() {
    if(bid==null) {return Long.toString(getJOpacID());}
    if(bid.contains("/")){
		String[] bidparts = bid.split("/");
		bid = bidparts[bidparts.length-2]+bidparts[bidparts.length-1];
	}
    return bid;
  }
  
  private String getTag(byte[] record, String dirEntry) throws UnsupportedEncodingException {
	    String t=dirEntry.substring(3,7);
	    int FieldLength = Integer.parseInt(t);
	    t=dirEntry.substring(7,12);
	    int Starting = Integer.parseInt(t) + 1;
	    t=new String(record,Starting-1,FieldLength,"utf-8");
	    t=my_trim(t);
	    return(t);
  }

  private String getTag(String record, String DirEntry) {
	//x.substring(start-1,start+len-1)
	  String t=DirEntry.substring(3,7);
    int FieldLength = Integer.parseInt(t);
    t=DirEntry.substring(7,12);
    int Starting = Integer.parseInt(t) + 1;
    t=record.substring(Starting-1,Starting+FieldLength-1);
    //t = Utils.mid(record, Starting, FieldLength);
    t=my_trim(t);
    return(t);
  }

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

  /**
   * 09/03/2009 First get Tag, then get firstElement from Tag
   * @deprecated
   * @param tagString
   * @param element
   * @return
   */
  public String getFirstElement(String tagString,String element) {
    String r;
    try {
      r=(String)(getElement(tagString,element).elementAt(0));
    }
    catch (Exception e) {
      return "";
//      return e.getMessage();
    }
    return r;
  }
  
  
  /***
   * 09/03/2009 First get Tag, then get firstElement from Tag
   * @deprecated
   * 16/09/2004 - R.T.
   *     Dato un vettore di tag e il codice di un elemento
   *     ritorna un vettore che contiene tutti gli elementi dei tag
   *     del vettore.
   */
public Vector<String> getElement(Vector<String> tags, String elements) {
      Vector<String> v=new Vector<String>();
      
      for(int i=0;i<tags.size();i++) {
          v.addAll(getElement((String)tags.elementAt(i),elements));
      }
      return v;
  }

  /**
   * Rivedere dove viene usato qs metodo
   * @deprecated
   * @param tag
   * @return
   */
  public String getTagElements(String tag) {
  	Vector<Tag> myTag=getTags(tag);
  	String t,r="";
  	
  	for(int i=0;i<myTag.size();i++) {
  		t=myTag.elementAt(i).toString().substring(3);
  		t=t.replaceAll(rt," ");
  		t=t.replaceAll(ft," ");
  		t=t.replaceAll(dl," ");
  		r+=t+"\n";
  	}
  	myTag.clear();
  	return r;
  }
  
  /**
   * @deprecated
   * @param tagString
   * @param elements
   * @return
   */
public Vector<String> getElement(String tagString, String elements) {
	/**
	 * 4/2/2008 - R.T. modificato in modo che se elements==null || elements.lenght()==0 e non ci sono dl
	 * 			  ritorna un unico elemento
	 */
	
    Vector<String> r=new Vector<String>();
    Vector<String> t=new Vector<String>();
    
    if((elements==null)||(elements.length()==0)) {
    	t.addElement(tagString);
    }
    else {
	    String ctk;
	    StringTokenizer tk=new StringTokenizer(tagString,dl);
	    while(tk.hasMoreTokens()) {
	      ctk=tk.nextToken();
	      r.addElement(ctk);
	    }
	    // Mumble, fin qui ok, ho un vettore con tutti gli elementi, ma da qui in poi
	    // rifaccio (R.T.)
	    int cp=0;
	    for(int i=0;i<r.size();i++) {
	      // OK, prendo l'elemento
	      String element=((String)r.elementAt(i)).substring(0,1);
	      // in che indice si trova?
	      cp=elements.indexOf(element);
	      if(cp!=-1) {  // se l'elemento c'e'
	        // aggiungi l'elemento al nuovo vettore
	        t.addElement(((String)r.elementAt(i)).substring(1));
	      }
	    }
	    
	    r.clear();
	
	    // dovevano diventare una sola riga, se c'erano piu' elementi validi?
    }
    return(t);
  }

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

  private String my_trim(String t) {
    while(t.endsWith(dl)||t.endsWith(ft)||t.endsWith(rt)) {
      t=t.substring(0,t.length()-1);
    }
    return(t);
  }

  public String toString() {
    try {
		return toISO2709();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
  }
  
  public String toReadableString() {
	  StringBuffer r=new StringBuffer();
	  r.append("Record status: "+recordStatus+"\n");
	  r.append("Record type: "+recordType+"\n");
	  r.append("Record biblio level: "+recordBiblioLevel+"\n");
	  r.append("Record hierarchical level code: "+indicatorLength+"\n");
	  
	  Collections.sort(dati);
	  
	  for(int i=0;i<dati.size();i++) {
		  if(!dl.equals("^"))
			  r.append(dati.elementAt(i).toString().replaceAll("["+dl+"]", "^")+"\n");
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
      if (c.equals(dl)==true) {
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
          if (c.equals(ft)==false) {
            s = s + c;
          }
        }
      }
    }
    return(s);
  }

  public void setTerminator(String r,String f, String d) {
    rt=r;
    ft=f;
    dl=d;
    delimiters.setRt(rt);
    delimiters.setFt(ft);
    delimiters.setDl(dl);
  }
  
  /**
   * 01/10/2003 - RT
   *          Definisce il tipo di record e lo stato.
   */
    protected long recordlength=-1;
    protected String recordStatus="n";
    protected String implementationCodes="    "; // 4 char (recordType, recordBiblioLevel, '0' , '0')
    protected String recordType="a";
    protected String recordBiblioLevel="m";
    protected String indicatorLength="0";
    protected String subfieldIdentifierLength="0";
    protected String baseAddressOfData="     "; // 5 bytes
    protected String additionalRecordDefinition="   "; // 3 bytes
    protected String directoryMap="    "; // 4 bytes
    protected String characterEncodingScheme=" "; // 1 byte, a=utf8, blank=normal

  public void init(String stringa) throws Exception {
    inString=stringa;
    if(stringa.trim().length()==0) return;

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
    	
    	
      recordlength = Long.parseLong((String)stringa.substring(0,5));
      if(recordlength > 0) {
    	int stringLength=stringa.length();
    	int byteLength=stringa.getBytes().length;
    	
    	
    	/**
    	 * RT: 12/11/2009
    	 * Some systems code the record counting bytes instead of chars. This is an ambiguity in ISO2709 standard.
    	 * I think the correct interpretation is for chars, because transport of the record should be independent from
    	 * the charset coding. So jbal read and write in that sense.
    	 * Anyway this is a silence patch to read byte coded records.
    	 * I've posted a mail to IFLA to ask about this problem and I'm waiting for a copy of the ISO2709:2008.
    	 * I will correct with the right interpretation iff I solve the question :-)
    	 * Problem occurs only with utf8 data and in content part of the record.
    	 */
    	boolean byteCoded=false;  	
    	if(recordlength==byteLength && byteLength!=stringLength) byteCoded=true;
    	
    	// record status
        recordStatus = stringa.substring(5,6);
        // 4 bytes, are "Implementation Codes"
        implementationCodes=stringa.substring(6,10);
        recordType = implementationCodes.substring(0,1);
        recordBiblioLevel = implementationCodes.substring(1,2);
        characterEncodingScheme=implementationCodes.substring(3,4);
        
        // indicatorLength
        indicatorLength = stringa.substring(10,11);
        // Subfield identifier length
        subfieldIdentifierLength = stringa.substring(11,12);
        
        // base address of data
        baseAddressOfData=stringa.substring(12,17);
        
        // additional record definition
        additionalRecordDefinition=stringa.substring(17,20);
        
        // directory map
        directoryMap=stringa.substring(20,24);

        int readed = 24;
        int ndir = 0;
        String DirEntry = stringa.substring(readed, readed+1);
        readed = readed + 1;
        
        byte[] recordBytes=stringa.getBytes();

        while (DirEntry.equals(ft)==false) {
        	String t=null;
        	if(byteCoded) t=new String(recordBytes, readed-1, 12, "utf-8");
        	else t=stringa.substring(readed-1,readed+12-1);
        	
            readed = readed + 12;
            Directory[ndir] = t;
            ndir = ndir + 1;
            
            if(byteCoded) DirEntry=new String(recordBytes, readed-1, 1, "utf-8");
            else DirEntry = stringa.substring(readed-1, readed);
        }

        String record=stringa.substring(readed);
        for(int z = 0; z<= ndir - 1;z++) {
        	
          String s = null;
          if(byteCoded) s=my_trim(getTag(record.getBytes(), Directory[z]));
          else s=my_trim(getTag(record, Directory[z]));
          String tag=Directory[z].substring(0,3);

          dati.addElement(new Tag(tag+s,delimiters));
          if(tag.equals("001")) {
            bid=s;
          }
        }
      }
    }
    catch (Exception e) {
      // ops, non e' Unimarc? forse e' codificato, riproviamoci
//      System.out.print("Codificato: "+stringa);
        
      /** todo
       * Fare meglio questa parte, initCoded digerisce tutto, deve dare una eccezione
       * se non e' codificato. Trovare un modo per verificare se e' codificato.
       */
    	if(stringa.charAt(0)=='4') {
    		initCoded(stringa);
    	}
    	else {
    		throw e;
    	}
      
    }
//    System.out.println("OK: "+stringa);
  }

  public void initCoded(String stringa) {
    inString=stringa;
    try {
      StringTokenizer tk=new StringTokenizer(stringa,ft+dl+rt);
      String ctk;
      String s="";

      while(tk.hasMoreTokens()) {
        ctk=tk.nextToken();
//        Utils.Log(Utils.LOG_DEBUG,"Coded token: "+ctk);
        if(ctk.substring(0,1).equals("1")) {
          if(s.length()>0) {
        	  if(!s.startsWith(delimiters.getDl()))
        		  dati.addElement(new Tag(s,delimiters));
            if(s.substring(0,3).equals("001")) {
              bid=s.substring(3);
            }
            s="";
            s+=ctk.substring(1);
          }
        }
        else {
          s+=delimiters.getDl()+ctk;
        }
      }
      if(s.length()>0 && !s.startsWith(delimiters.getDl())) {
        dati.addElement(new Tag(s,delimiters));
      }
    }
    catch (Exception e) {
      // aaahhhh, se arrivo qua vuol dire che non ci capisco una mazza,
      // o il record e' proprio fallato
      e.printStackTrace();
//      Utils.Log(1,stringa);
    }
  }

  public void iso2709Costruttore(String notizia,String dTipo,int livello) throws Exception {
    Directory=new String[1000];
    dati=new Vector<Tag>();
    tw=new Vector<TokenWord>();
    descrizioneTipo=dTipo;
    init(notizia);

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
  public String toEncapsulatedRecordFormat() {
	  StringBuffer r=new StringBuffer();
	  
	  r.append(" 0"); // indicators with no note default
	  for(int i=0;dati!=null && i<dati.size();i++) {
		  Tag tag=dati.elementAt(i);
		  if(tag!=null) {
			  r.append(dl+"1"+tag.toString());
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
	                    implementationCodes.substring(2) +
	                    indicatorLength +
	                    subfieldIdentifierLength ;
	
	    int ba=27;
	    String directory="";
	    String data="";
	
	    long temp = 24 + 12 * getRows() + 1;             // base address of data
	    leader = leader.concat(Utils.pad("00000",temp));
	    
	    leader=leader.concat(additionalRecordDefinition);                  // implementation defined
	    leader=leader.concat(directoryMap);                 // entry map
	    
	    Vector<Tag> t=this.getTags();
	    Collections.sort(t);
	
	    
	    
	    ba+=this.getRows()*12;
	    long c=0;
	    for(int z=0;z<t.size();z++) {
	    	String k=t.elementAt(z).toString();
	        directory=directory.concat(k.substring(0,3));
	        directory=directory.concat(Utils.pad("0000",k.length()-2));
	        directory=directory.concat(Utils.pad("00000",c));
	        c+=k.length()-2;
	        ba+=k.length()-2;	
	        data=data+t.elementAt(z).toXML();
	    }
	    
	        
	    leader=Utils.pad("00000", ba-1).concat(leader);
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
	                    implementationCodes.substring(2) +
	                    indicatorLength +
	                    subfieldIdentifierLength ;
	
	    int ba=27;
	    String directory="";
	    String data="";
	
	    long temp = 24 + 12 * getRows() + 1;             // base address of data
	    leader = leader.concat(Utils.pad("00000",temp));
	    
	    leader=leader.concat(additionalRecordDefinition);                  // implementation defined
	    leader=leader.concat(directoryMap);                 // entry map
	    
	    Vector<Tag> t=this.getTags();
	    Collections.sort(t);
	
	    ba+=this.getRows()*12;
	    long c=0;
	    for(int z=0;z<t.size();z++) {
	    	String k=t.elementAt(z).toString();
	        directory=directory.concat(k.substring(0,3));
	        directory=directory.concat(Utils.pad("0000",k.length()-2));
	        directory=directory.concat(Utils.pad("00000",c));
	        c+=k.length()-2;
	        ba+=k.length()-2;	
	        data=data+k.substring(3)+ft;
	    }
	    if(rt!=null) data=data.concat(rt);
	        
	    leader=Utils.pad("00000", ba-1).concat(leader);
	    r=leader+directory+ft+data;
	}
	catch(java.lang.StringIndexOutOfBoundsException e) {
		r=null;
	}
	catch(Exception e) {
		throw new Exception("ISO2709.toString Exception: JID="+JOpacID+" Type="+this.getTipo());
	}
    return r;
}
  
  public String getTerminators() {
	  System.out.append("prende i terminatori");
    return ft+rt+dl;
  }
  
  public ISO2709(String notizia,String dTipo,String livello) throws Exception {
    this.iso2709Costruttore(notizia,dTipo,Integer.parseInt(livello));
  }

  public ISO2709(String notizia,String dTipo) throws Exception {
    this.iso2709Costruttore(notizia,dTipo,0);
  }
  
  //public abstract RecordReader getRecordReader(InputStream f);
  public abstract RecordReader getRecordReader(InputStream dataFile) throws UnsupportedEncodingException;


  public ISO2709() {
      dati=new Vector<Tag>();
      tw=new Vector<TokenWord>();
  }

  public Enumeration<TokenWord> getItems() {
		tw.removeAllElements();
		String ctk;
		boolean unico = true;

		for (int z = 0; z < dati.size(); z++) {
			String valore_tag = dati.elementAt(z).toString().substring(3);
			String tag = dati.elementAt(z).getTagName();

			if (valore_tag.indexOf(dl) >= 0)
				unico = false;

			StringTokenizer tk = new StringTokenizer(valore_tag, dl);
			while (tk.hasMoreTokens()) {
				String valore = "";

				ctk = tk.nextToken().trim();
				try {
					if (ctk.length() > 0) {
						String de = "";
						if (!unico) {
							de = ctk.substring(0, 1);
							valore = ctk.substring(1);
						} else {
							de = "";
							valore = ctk;
						}
						tw.addElement(new TokenWord(valore, tag, de));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		return tw.elements();
	}
  
  public String getPublicationNature(){ return  recordType;}
  
	public String getField(String field) {
		return null;
	}

}