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
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.TokenWord;
import org.jopac2.utils.Utils;

public abstract class ISO2709 implements RecordInterface {
  public Vector<String> dati;
  public Vector<TokenWord> tw;

  protected String rt=String.valueOf((char)0x1d);
  protected String ft=String.valueOf((char)0x1e);
  public String dl=String.valueOf((char)0x1f);        //' delimiter
  public String cr=String.valueOf((char)13);
  public String lf=String.valueOf((char)10);
  private long JOpacID=0;
  protected String bid=null;
  protected String descrizioneTipo;
  private int _livello =0;  // serve a gestire le notizie per linkup e down

  public Vector<RecordInterface> linkUp=null;
  public Vector<RecordInterface> linkDown=null;
  public Vector<RecordInterface> linkSerie=null;
  
  public RecordInterface clone() {
	  return RecordFactory.buildRecord(0, this.toString(), this.getTipo(), this.getLivello());
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
  
/**
 * 01/10/2003 - RT
 *          Definisce il tipo di record e lo stato.
 */
  //private String recordStatus="a";
  //private String recordType="m";
  protected String recordStatus="n";
  protected String recordType="a";
  protected String recordBiblioLevel="m";
  protected String recordHierarchicalLevelCode="0";

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
      dati.addElement(newTag);
  }
  
public void addTag(Vector<String> newTags) {
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

  private String getTag(String record, String DirEntry) {
    int FieldLength = Integer.parseInt(Utils.mid(DirEntry, 4, 4));
    int Starting = Integer.parseInt(Utils.mid(DirEntry, 8, 5)) + 1;
    String t = Utils.mid(record, Starting, FieldLength);
    t=my_trim(t);
    return(t);
  }

  public Vector<String> getTag() {
    return dati;
  }
  
  public int getRows() {
      return dati.size();
  }

  public String getFirstTag(String tag) {
    String r;
    try {
      r=(String)(getTag(tag).elementAt(0));
    }
    catch (Exception e) {
      return "";
//      return e.getMessage();
    }
    return r;
  }

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

  public String getTagElements(String tag) {
  	Vector<String> myTag=getTag(tag);
  	String t,r="";
  	
  	for(int i=0;i<myTag.size();i++) {
  		t=((String)myTag.elementAt(i)).substring(3);
  		t=t.replaceAll(rt," ");
  		t=t.replaceAll(ft," ");
  		t=t.replaceAll(dl," ");
  		r+=t+"\n";
  	}
  	myTag.clear();
  	return r;
  }
  
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
public void removeTag(String tag) {
 if(tag.length()!=3) return;
 
 for(int i=0;i<dati.size();i++) {
      if(((String)(dati.elementAt(i))).startsWith(tag)) {
        dati.removeElementAt(i);
        i--; // per far in modo che dopo lo shift degli elementi venga ricontrollato questo indice
      }
    }
}

/**
 * Rimuove tutti i tag nell'area indicata (nella forma, ad esempio, 9xx)
 * @param area
 */
public void removeArea(String area) {
 if(area.length()!=3) return;
 
 area=area.toLowerCase().replaceAll("x", "");
 
 if(area.length()==0) return;
 
 for(int i=0;i<dati.size();i++) {
      if(((String)(dati.elementAt(i))).startsWith(area)) {
        dati.removeElementAt(i);
        i--; // per far in modo che dopo lo shift degli elementi venga ricontrollato questo indice
      }
    }
}

public Vector<String> getTag(String tag) {
    Vector<String> r=new Vector<String>();
    
    for(int i=0;i<dati.size();i++) {
      if(((String)(dati.elementAt(i))).startsWith(tag)) {
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
	  for(int i=0;i<dati.size();i++) {
		  r.append(dati.elementAt(i).replaceAll("["+dl+"]", "^")+"\n");
	  }
	  return r.toString();
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
  public abstract Vector<RecordInterface> getLinked(String tag) throws JOpac2Exception;

  public String quote(String t) {
    String c;

    String s = "";
    for(int k = 1;k<t.length()+1;k++) {
      c = Utils.mid(t, k, 1);
      if (c.equals(dl)==true) {
        s = s + "[" + Utils.mid(t, k + 1, 1) + "]";
        k = k + 1;
      } else {
        if (c.equals(String.valueOf((char)27))==true) {
          if (((String)Utils.mid(t, k + 1, 1)).equals("I")) {
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
  }

  public void init(String stringa) {
    inString=stringa;

    try {
      long recordlength = Long.parseLong((String)stringa.substring(0,5));
      if(recordlength > 0) {
        recordStatus = stringa.substring(5,6);
        recordType = stringa.substring(6,7);
        recordBiblioLevel = stringa.substring(7,8);
        recordHierarchicalLevelCode = stringa.substring(8,9);

        //String EntryMap = stringa.substring(20,24);
        int readed = 24;
        int ndir = 0;
        String DirEntry = stringa.substring(readed, readed+1);
        readed = readed + 1;

        while (DirEntry.equals(ft)==false) {
            String t = Utils.mid(stringa, readed, 12);
            readed = readed + 12;
            Directory[ndir] = t;
            ndir = ndir + 1;
            DirEntry = Utils.mid(stringa, readed, 1);
        }

        String record = Utils.mid(stringa, readed + 1);
        for(int z = 0; z<= ndir - 1;z++) {
          String s = my_trim(getTag(record, Directory[z]));
          String tag = Utils.mid(Directory[z], 1, 3);

          dati.addElement(tag+s);
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
       * Fare meglio questa parte, initCoded digerisce tutto, deve dare una eccezzione
       * se non e' codificato. Trovare un modo per verificare se e' codificato.
       */
    	//e.printStackTrace();
      initCoded(stringa);
    }
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
            dati.addElement(s);
            if(s.substring(0,3).equals("001")) {
              bid=s.substring(3);
            }
            s="";
            s+=ctk.substring(1);
          }
        }
        else {
          s+=dl+ctk;
        }
      }
      if(s.length()>0) {
        dati.addElement(s);
      }
    }
    catch (Exception e) {
      // aaahhhh, se arrivo qua vuol dire che non ci capisco una mazza,
      // o il record � proprio fallato
      e.printStackTrace();
//      Utils.Log(1,stringa);
    }
  }

  public void iso2709Costruttore(String notizia,String dTipo,int livello) {
    Directory=new String[1000];
    dati=new Vector<String>();
    tw=new Vector<TokenWord>();
    descrizioneTipo=dTipo;
    init(notizia);

    _livello=livello+1;  //  salva il livello creato per evitare ricorsione
  }

  /**
   * Object not=ISO2709.creaNotizia(id,contenuto,tipo,livelloPadre);
   * @deprecated
   */
  /*public static ISO2709 creaNotizia(long ID,String notizia,String tipo,int livello) {
    ISO2709 ma;
    try {
    	Class iso;
    	try {
    		iso=Class.forName("JOpac2.dataModules."+tipo);
    	}
    	catch (Exception e){
    		String fl=tipo.substring(0,1).toUpperCase();
    		tipo=fl+tipo.substring(1);
    		iso=Class.forName("JOpac2.dataModules."+tipo);
    	}
      //attenzione: si prende sempre il costruttore 1 quello definito per primo
//      java.lang.reflect.Constructor c=iso.getConstructors()[1];
      java.lang.reflect.Constructor c=iso.getConstructor(new Class[] {String.class, String.class, String.class });
      ma=(ISO2709)c.newInstance(new Object[] {notizia,tipo,Integer.toString(livello)});
    }
    catch (Exception e) {
      e.printStackTrace();
      ma=new ISO2709Impl(notizia,tipo);
    }
    ma.setJOpacID(ID);
    return ma;
  }
*/

  /**
   * 01/10/2003 - RT 
   *            Prende una istanza ISO2709 e la salva in formato ISO2709 restituendo una stringa
   *            DA FARE: che venga ritornato opzionalmente in formato XML
 * @throws Exception 
   */
  protected String toISO2709() throws Exception {
    String r=null;
	try {
	  	String leader = getStatus() + getType() +  // definisce il leader che deve essere scritto
	                    getBiblioLevel() + getHierarchicalLevelCode() + " 22";   // il primo carattere � sempre 'n'? CONTROLLARE su specifiche!
	
	    int ba=27;
	    String directory="";
	    String data="";
	
	    long temp = 24 + 12 * getRows() + 1;             // base address data
	    leader = leader.concat(Utils.pad("00000",temp));
	        
	    Vector<String> t=this.getTag();
	    
	    for(int i=0;i<t.size();i++){
	    	t.setElementAt(((String)t.elementAt(i)).replaceAll("[\r\n]","|").trim(),i);
	    }
	    leader=leader.concat("   ");                  // implementation defined
	    leader=leader.concat("4500");                 // entry map
	
	    ba+=this.getRows()*12;
	    long c=0;
	    for(int z=0;z<t.size();z++) {
	        directory=directory.concat(((String)t.elementAt(z)).substring(0,3));
	        directory=directory.concat(Utils.pad("0000",((String)t.elementAt(z)).length()-2));
	        directory=directory.concat(Utils.pad("00000",c));
	        c+=((String)t.elementAt(z)).length()-2;
	        ba+=((String)t.elementAt(z)).length()-2;	
	        data=data+((String)t.elementAt(z)).substring(3)+ft;
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
  
  public ISO2709(String notizia,String dTipo,String livello) {
    this.iso2709Costruttore(notizia,dTipo,Integer.parseInt(livello));
  }

  public ISO2709(String notizia,String dTipo) {
    this.iso2709Costruttore(notizia,dTipo,0);
  }
  
  //public abstract RecordReader getRecordReader(InputStream f);
  public abstract RecordReader getRecordReader(InputStream dataFile) throws UnsupportedEncodingException;


  public ISO2709() {

      dati=new Vector<String>();
      tw=new Vector<TokenWord>();
  }

  public Enumeration<TokenWord> getItems() {
		tw.removeAllElements();
		String ctk;
		boolean unico = true;

		for (int z = 0; z < dati.size(); z++) {
			String valore_tag = ((String) dati.elementAt(z)).substring(3);
			String tag = ((String) dati.elementAt(z)).substring(0, 3);

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
}