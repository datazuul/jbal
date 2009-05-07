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
 * CDS ISIS � prodotto dall'UNESCO
 * Easyweb � un marchio di Nexus S.r.l. - Firenze (I)
 */


import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
//import java.lang.*;
//import java.sql.*;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.IsoRecordReader;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
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
    Vector<Tag> v=getTags(tag);
    Vector<RecordInterface> r=new Vector<RecordInterface>();
    try {
      if(v.size()>0) { // se il vettore ha elementi, allora faro' almeno una query
        for(int i=0;i<v.size();i++) {
          Easyweb not=new Easyweb();
          //not.dati=new Vector<String>();
          Tag title=v.elementAt(i);
          Tag n=new Tag("100",' ',' ');
          n.setRawContent(title.getField("a").getContent());
          not.dati.addElement(n);
          not.setBid(title.getField("Z").getContent());
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
      return getFirstTag("301").getRawContent();
  }
  
  public String getPublicationDate() {
      return getFirstTag("310").getRawContent();
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
    Vector<Tag> v=getTags("710");
    v.addAll(getTags("711"));
    v.addAll(getTags("712"));

    Vector<String> r=new Vector<String>();
    if(v.size()>0) {
      for(int i=0;i<v.size();i++) {
        r.addElement(v.elementAt(i).getField("a").getContent());
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
    Vector<Tag> v=getTags("800");
    Vector<BookSignature> res=new Vector<BookSignature>();
    if(v.size()>0) {
      for(int i=0;i<v.size();i++) {
        String codiceBib="";
        String b="";

        try {
            b=v.elementAt(i).getField("a").getContent();
            codiceBib=v.elementAt(i).getField("b").getContent(); //.substring(0,2);
        }
        catch (Exception e) {}
        String col=v.elementAt(i).getField("c").getContent();
        String inv=v.elementAt(i).getField("d").getContent();
        String con=v.elementAt(i).getField("e").getContent();

        res.addElement(new BookSignature(codiceBib,b,inv,col,con));
      }
    }
    return res;
  }


public void clearSignatures() throws JOpac2Exception {
	this.removeTags("800");
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
     * <<....=....> .... >...... questo � un caso certo
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
    Tag tag=getFirstTag("100");
    r=(tag.getRawContent());
    return r;
  }

  public String getISBD() {
    String r="";
    
    try {
        Tag tag=getFirstTag("100");
        r=(tag.getRawContent());
    }
    catch(Exception e) {
        //r="Errore, 100 = "+getFirstTag("100");
    }
    Tag t=getFirstTag("200");
    if(t!=null) {
      r+=Utils.ifExists(". - ",t.getRawContent());
    }
    t=getFirstTag("300");
    if(t!=null) {
        r+=Utils.ifExists(". - ",t.getRawContent());
      }
    t=getFirstTag("320");
    if(t!=null) {
        r+=Utils.ifExists(". - ",t.getRawContent());
      }
    return quote(r);
  }
  
  public Vector<String> getSubjects() {
	  Vector<Tag> v=getTags("730");
	  Vector<String> r=new Vector<String>();
	  for(int i=0;v!=null && i<v.size();i++) {
		  r.addElement(v.elementAt(i).getField("a").getContent());
	  }
      return r;
  }

  public Vector<String> getClassifications() {
	  Vector<Tag> v=getTags("740");
	  Vector<String> r=new Vector<String>();
	  for(int i=0;v!=null && i<v.size();i++) {
		  r.addElement(v.elementAt(i).getField("a").getContent());
	  }
      return r;
  }
  
  public Vector<String> getEditors() {return null;}
  
  public Easyweb() {
	  super();
  }


public String getDescription() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Vector<RecordInterface> getLinked(String tag) throws JOpac2Exception {
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
}