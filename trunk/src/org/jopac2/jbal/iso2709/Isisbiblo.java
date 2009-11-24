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
* @author	Albert Caramia
* @version ??/??/2002
* 
* @author	Romano Trampus
* @version ??/??/2002
*/

/**
 * JOpac2 - Modulo formato "ISISBiblo". v. 0.1
 *          Questo modulo permette di importare dati registrati nel formato
 *          CDS ISIS / BIBLO.
 *
 * CDS ISIS � prodotto dall'UNESCO
 *
 * TODO: come vengono visualizzati e cercati i terzi livelli? Come vengono visualizzati?
 * 
 */


import java.awt.image.BufferedImage;
import java.util.*;
//import java.lang.*;
//import java.sql.*;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.*;

public class Isisbiblo extends ISO2709Impl {
	private final static String rt="#";
	private final static String ft="#";
	private final static String dl="^";        //' delimiter
	


    public Isisbiblo(String stringa, String dTipo)  throws Exception {
    super();
    setTerminator("#","#","^");
    iso2709Costruttore(stringa,dTipo,0);
    initLinkUp();
    initLinkDown();
    initLinkSerie();
  }

    public Isisbiblo(String stringa, String dTipo, String livello)  throws Exception {
    super();
    setTerminator("#","#","^");
    iso2709Costruttore(stringa,dTipo,Integer.parseInt(livello));
    initLinkUp();
    initLinkDown();
    initLinkSerie();
  }
    
    public Hashtable<String, List<Tag>> getRecordMapping() {
		Hashtable<String, List<Tag>> r=new Hashtable<String, List<Tag>>();
		
		List<Tag> aut=new Vector<Tag>();
		aut.add(new Tag("011","R",""));
		aut.add(new Tag("011","A",""));
		aut.add(new Tag("012","R",""));
		aut.add(new Tag("014","N",""));
		aut.add(new Tag("021","R",""));
		aut.add(new Tag("021","A",""));
		aut.add(new Tag("031","R",""));
		aut.add(new Tag("031","A",""));
		aut.add(new Tag("041","C",""));
		aut.add(new Tag("041","N",""));
		aut.add(new Tag("051","C",""));
		aut.add(new Tag("051","N",""));
		aut.add(new Tag("054","C",""));
		aut.add(new Tag("054","N",""));
		
		aut.add(new Tag("011","r",""));
		aut.add(new Tag("011","a",""));
		aut.add(new Tag("012","r",""));
		aut.add(new Tag("014","n",""));
		aut.add(new Tag("021","r",""));
		aut.add(new Tag("021","a",""));
		aut.add(new Tag("031","r",""));
		aut.add(new Tag("031","a",""));
		aut.add(new Tag("041","c",""));
		aut.add(new Tag("041","n",""));
		aut.add(new Tag("051","c",""));
		aut.add(new Tag("051","n",""));
		aut.add(new Tag("054","c",""));
		aut.add(new Tag("054","n",""));
		r.put("AUT", aut);
		
		List<Tag> tit=new Vector<Tag>();
		tit.add(new Tag("011","T",""));
		tit.add(new Tag("011","C",""));
		tit.add(new Tag("011","P",""));
		tit.add(new Tag("011","S",""));
		tit.add(new Tag("021","T",""));
		tit.add(new Tag("021","C",""));
		tit.add(new Tag("021","P",""));
		tit.add(new Tag("021","S",""));
		tit.add(new Tag("031","T",""));
		tit.add(new Tag("031","C",""));
		tit.add(new Tag("031","P",""));
		tit.add(new Tag("031","S",""));
		tit.add(new Tag("045","T",""));
		tit.add(new Tag("045","P",""));
		
		tit.add(new Tag("011","a",""));
		tit.add(new Tag("011","c",""));
		tit.add(new Tag("011","p",""));
		tit.add(new Tag("011","s",""));
		tit.add(new Tag("021","t",""));
		tit.add(new Tag("021","c",""));
		tit.add(new Tag("021","p",""));
		tit.add(new Tag("021","s",""));
		tit.add(new Tag("031","t",""));
		tit.add(new Tag("031","c",""));
		tit.add(new Tag("031","p",""));
		tit.add(new Tag("031","s",""));
		tit.add(new Tag("045","t",""));
		tit.add(new Tag("045","p",""));
		r.put("TIT", tit);
		
		List<Tag> num=new Vector<Tag>();
		num.add(new Tag("018","B",""));
		num.add(new Tag("018","S",""));
		num.add(new Tag("018","b",""));
		num.add(new Tag("018","s",""));
		r.put("NUM", num);
		
		List<Tag> cll=new Vector<Tag>();
		cll.add(new Tag("016","T",""));
		cll.add(new Tag("016","P",""));
		cll.add(new Tag("016","C",""));
		cll.add(new Tag("016","t",""));
		cll.add(new Tag("016","p",""));
		cll.add(new Tag("016","c",""));
		r.put("CLL", cll);
		
		
		
		
//		List<Tag> mat=new Vector<Tag>();
//		mat.add(new Tag("040","a",""));
//		r.put("MAT", mat);
//		
//		List<Tag> sbj=new Vector<Tag>();
//		sbj.add(new Tag("730","a",""));
//		r.put("SBJ", sbj);
		
		List<Tag> bib=new Vector<Tag>();
		bib.add(new Tag("741","B",""));
		r.put("BIB", bib);
		
		List<Tag> inv=new Vector<Tag>();
		inv.add(new Tag("001","",""));
		r.put("INV", inv);
		
//		712 = biblioteca (o codice biblioteca)
		
		return r;
	}

	public String getRecordTypeDescription() {
		return "CDS/ISIS BIBLO ISO2709 format.";
	}
    
    public String getTerminators() {
  	  System.out.println("prende i terminatori Isisbiblo");
      return ft+rt+dl;
    }

  public void initLinkUp() {
    /**
     * Il record isis/biblo ha tutti i dati dei tre livelli. Quindi non ci sono
     * link-up, ma solo link down al secondo livello.
     */
    //linkUp=getLink("");
  }

  public void initLinkSerie() {
    linkSerie=getLink("016");
  }

  public void initLinkDown() {
    linkDown=getLink("021");
  }

  // ritorna un vettore di elementi biblo
public Vector<RecordInterface> getLink(String tag) {
    Tag v=getFirstTag(tag);

    Vector<RecordInterface> r=new Vector<RecordInterface>();
    try {
      if(v!=null) { // se il vettore ha elementi, allora faro' almeno una query
          Isisbiblo not=new Isisbiblo();    // crea un nuovo elemento Isisbiblo
          not.dati=new Vector<Tag>();
          Tag t=new Tag("011",' ',' ');
          t.setRawContent(v.getRawContent());
          not.dati.addElement(t);
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
    Vector<Tag> v=getTags("041");     // Autore principale
    v.addAll(getTags("051"));    // coAutore principale
    v.addAll(getTags("054"));    // altri Autori

    Vector<String> r=new Vector<String>();
    if(v.size()>0) {
      for(int i=0;i<v.size();i++) {
        
        String k=Utils.ifExists("", v.elementAt(i).getField("C"),v.elementAt(i).getField("c"))+
        	Utils.ifExists(", ",v.elementAt(i).getField("N"),v.elementAt(i).getField("n"));
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
 * Nota: il formato isis/biblo non ha un codice di polo.
 *       ogni volume fisico corrisponde a un record.
 *          712 = biblioteca (o codice biblioteca)
 *          741^B = biblioteca
 *          741^S = sezione
 *          741^F = sottosezione
 *          741^I = intestazione
 *          741^N = numero catena
 *          741^V = numero volume
 *          001 = inventario
 * 16/9/2004 - R.T.
 *     Riformulato. Non usa piu' coppia ma ritorna un vettore
 *     di BookSignature per maggiore chiarezza
 */
public Vector<BookSignature> getSignatures() {
    Tag v=getFirstTag("741"); // prende dati collocazione
    Vector<BookSignature> res=new Vector<BookSignature>();
    
    try {
	    String codiceBib=Utils.ifExists("", v.getField("B"))+Utils.ifExists("", v.getField("b"));
	    String b=getFirstTag("712").getRawContent(); // prende la biblioteca (nome?)
	    
	    
	    String col=v.getField("S").getContent()+v.getField("s").getContent()+
	    	"/"+v.getField("F").getContent()+v.getField("f").getContent();     // collocazione
	    col+=Utils.ifExists("/",v.getField("N"),v.getField("n"));
	    String inv=getFirstTag("001").getRawContent(); // prende l'inventario
	    String con="";
	    try {
	        con=getFirstTag("173").getRawContent();     // consistenza del periodico
	    }
	    catch (Exception e) {
	        con="";
	        // campo 173 non trovato
	    }
	
	    res.addElement(new BookSignature(codiceBib,b,inv,col,con));
    }
    catch (Exception e) {
    	System.out.println("ERROR: Isisbiblo/getSignature");
    	res.clear();
    }
    return res;
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
     * <<....=....> .... >...... questo e' un caso certo
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
    	c=t.substring(k-1,k);
      //c = Utils.mid(t, k, 1);
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
    Tag tag=getFirstTag("011");
    if(tag!=null)
    	r=Utils.ifExists("", tag.getField("T"));
//    Utils.Log("Titolo: "+r);
    return r;
  }

  public String getISBD() {
    String r="";
    Tag tag=getFirstTag("011");
    if(tag!=null) {// AREA 1
    	r=Utils.ifExists("",tag.getField("T"),tag.getField("t")) + 				// titolo proprio
        Utils.ifExists(" : ",tag.getField("C"),tag.getField("c")) +    // compl. titolo
        Utils.ifExists(" : ",tag.getField("P"),tag.getField("p")) +    // titolo parallelo
        Utils.ifExists(" : ",tag.getField("R"),tag.getField("r")) +    // resp. princip.
        Utils.ifExists(" : ",tag.getField("A"),tag.getField("a")) +    // altra respon.
        Utils.ifExists(" : ",tag.getField("S"),tag.getField("s")) +    // titolo successivo
        Utils.ifExists(" : ",tag.getField("V"),tag.getField("v"));     // numero di volume se e' una collana. 
    }																							 // Se e' monografia non c'e' il ^V
    
    tag=getFirstTag("012");                            // AREA 2
    if(tag!=null) {
      r+=Utils.ifExists(". - ",tag.getField("E"),tag.getField("e"))+
      Utils.ifExists(", ",tag.getField("R"),tag.getField("r"));
    }
    tag=getFirstTag("014");
    r+=Utils.ifExists(". - ",tag.getField("L"),tag.getField("l"))+
        Utils.ifExists(" : ",tag.getField("N"),tag.getField("n"))+
        Utils.ifExists(", ",tag.getField("D"),tag.getField("d"));
    tag=getFirstTag("015");
    r+=Utils.ifExists(" ; ",tag.getField("E"),tag.getField("e"));
    
    // commento
    r+=Utils.ifExists(" ((", getFirstTag("172"));
    
    return quote(r);
  }

  public String getEdition() {
  	return null;
  }

  
  // TODO per questo tipo trovare l'abstract
  public String getAbstract() {
  	return null;
  }
  
  public Isisbiblo() {
  }

/* (non-Javadoc)
 * @see JOpac2.dataModules.ISO2709#getDescription()
 */
public String getDescription() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Vector<ClassificationInterface> getClassifications() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Vector<String> getEditors() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Vector<RecordInterface> getLinked(String tag) throws JOpac2Exception {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getPublicationDate() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getPublicationPlace() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Vector<SubjectInterface> getSubjects() {
	// TODO Auto-generated method stub
	return null;
}

public void addAuthor(String author) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addClassification(ClassificationInterface data) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addComment(String comment) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addPart(RecordInterface part) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addPartOf(RecordInterface partof) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addSerie(RecordInterface serie) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addSignature(BookSignature signature) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addSubject(SubjectInterface subject) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void clearSignatures() throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public String getComments() {
	// TODO Auto-generated method stub
	return null;
}

public String getStandardNumber() {
	// TODO Auto-generated method stub
	return null;
}

public void setAbstract(String abstractText) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setDescription(String description) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setEdition(String edition) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setISBD(String isbd) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setPublicationDate(String publicationDate) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setPublicationPlace(String publicationPlace) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setStandardNumber(String standardNumber, String codeSystem) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setTitle(String title) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setTitle(String title, boolean significant) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addPublisher(String publisher) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public BufferedImage getImage() {
	// TODO Auto-generated method stub
	return null;
}

public String getLanguage() {
	// TODO Auto-generated method stub
	return null;
}

public void setLanguage(String language) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}
}