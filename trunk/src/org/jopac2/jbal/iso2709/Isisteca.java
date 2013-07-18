package org.jopac2.jbal.iso2709;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2011 JOpac2 project
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
* @version 17/06/2011
*/

/**
 * JOpac2 - Modulo formato "ISISTeca". v. 0.1
 *          Questo modulo permette di importare dati registrati nel formato
 *          CDS ISIS / TECA.
 *
 * CDS ISIS e' prodotto dall'UNESCO
 *
 * TODO: come vengono visualizzati e cercati i terzi livelli? Come vengono visualizzati?
 * 
 */


/**
 * 
 * TAG 1		Titolo e indicazione di responsabilita'
   TAG 2		Edizione
   TAG 3		Peculiarita' del materiale
   TAG 4		Pubblicazione, distribuzione etc.
   TAG 5		Descrizione fisica
   TAG 6		Collezione
   TAG 7		Note
   TAG 8		ISBN
 */

/* 
TAG 1 TITOLO E INDICAZIONE DI RESPONSABILITA
-     Campo ripetibile

-     Codici di sottocampo

     ^a   titolo proprio (e altri titoli stesso autore)     		 	;
     ^b   designazione generica del materiale                  		[ ] 
     ^c   titolo proprio di altro autore                        			.
     ^d   titolo parallelo                                      			=
     ^e   complementi del titolo                                			:
     ^f   prima indicazione di responsabilita'                  		/
     ^g   seconda e successiva(e) indicazione di responsabilita'		;
     ^h   numero di una parte                                   			.
     ^i   nome di una parte                                     			,
           (se manca ^h, ".")
           
           
TAG 2 EDIZIONE

-    Campo  ripetibile

-    Codici di sottocampo

     ^a   indicazione di edizione                          	
     ^b   indicazione aggiuntiva di edizione           	,
     ^d   indicazione parallela di edizione            	=
     ^f   indicazione di responsabilita'              	/
     ^g   seconda e successiva(e) indicazione di resp. 	;   
     
TAG 3 PECULIARITA DEL MATERIALE

Questo campo non va generalmente usato per pubblicazioni monografiche a stampa o antiche. Andr� usato per altri tipi di 
pubblicazioni quali ad es. le pubblicazioni di musica a stampa le cui aree sono descritte dalle ISBD(PM), i periodici (ISBD(S)), 
il materiale cartografico (ISBD(CM)).  

-    Campo ripetibile


TAG 4 PUBBLICAZIONE, DISTRIBUZIONE ETC.

-    Campo  ripetibile
-    Codici di sottocampo

     ^a   luogo di pubblicazione					
     ^c   nome dell'editore/distributore          			:
     ^d   data di pubblicazione/distribuzione     		,
     ^e   luogo di stampa                         ( se presente		
     ^g   nome del tipografo                      			:     
     ^h   data di stampa                          			,

TAG 5  DESCRIZIONE FISICA
-    Campo non ripetibile

-    Codici di sottocampo

     ^a   designazione specifica ed estensione del mat.
     ^c   illustrazioni                                    : 
     ^d   dimensioni                                      ;
     ^e   materiale allegato        
     
TAG 6  COLLEZIONE

-    Campo ripetibile

-    Codici di sottocampo

     ^a   titolo
     ^b   titolo parallelo                  				 =
     ^e   complementi del titolo             			:
     ^f   indicazione di responsabilita'    			/
     ^r   seconda e successiva(e) ind. resp. 			;
     ^h   numero di una parte                			.
     ^i   nome di una parte (sottocoll.)     , se manca ^h, 	"."
     ^v   indicazione di volume              			;
     ^x   ISSN della collezione              			,
     
TAG 7 NOTE

Ogni nota rappresenta una ricorrenza del campo.  La punteggiatura all'interno di ciascuna nota va digitata.  Per quanto riguarda le note relative alla descrizione, si consiglia di rispettare, ove possibile, la punteggiatura convenzionale delle aree 1-6 delle ISBD.

-    Campo ripetibile

-    Inserire fra parentesi uncinate "<...>" quegli elementi delle note (per es., il titolo originale)  che si intendono invertire. Si ricorda di battere gli spazi vuoti all'esterno delle parentesi uncinate o fra di esse nel caso si presentino due parentesi (una di chiusura ed una di apertura) immediatamente successive. 


TAG 8  ISBN

-    Campo ripetibile


-    Il sistema introduce automaticamente la sigla dell'International Standard Book Number (ISBN).


TAG 9  AUTORI PERSONALI

-    Campo non ripetibile

-    Codici di sottocampo

     ^a   elemento principale 
     ^b  secondo elemento del nome			      	,
     ^x   altra parte del nome
     ^d   numeri arabi                    				[seguiti da . ]
     ^c   qualificazione                				,
     ^f   date                          					,


TAG 10  AUTORI COLLETTIVI

- Campo non ripetibile

- Codici di sottocampo

     ^a   elemento principale
     ^c   qualificazione elem. princ.           		( )
     ^q   qualificazione elem. princ.            		,   
     ^b   sottointestazione                      		.
     ^s   altra sottointestazione                		.
     ^x   qualificazione ente subordinato        		,
     ^y   qualificazione ente subordinato        		,
     ^d   numero del congresso                   		, [seguito da .]
     ^e   luogo del congresso                    		,
     ^f   data del congresso                     		,
     
TAG 11  TITOLO UNIFORME

Questo campo va usato nei casi previsti dalle RICA ai par. 85-90. Non e' stato previsto in TECA il caso di titoli uniformi di opere con autore (par. 92).

-    Campo ripetibile

-    Codici di sottocampo

     ^a   titolo uniforme              	 
     ^i   nome della parte              	.
     ^p   altro nome della parte        	.
     ^r   altro nome della parte        	.
     ^h   numero della parte            	,
     ^m   lingua                        		,
     ^l   suddivisione formale          	.


-    Per escludere gli articoli o qualsiasi altra particolare stringa di caratteri dall'ordinamento, ma non dalla stampa, questi vanno posti 
fra parentesi uncinate (<...>). Per le modalita' di inserimento vedi NOTE al TAG 1. 

TAG 12  AUTORI PERSONALI

-    Campo ripetibile

-    Codici di sottocampo: come TAG  9   +

     ^s    identificazione responsabilita' intellettuale  


-    Qualora si intenda utilizzare il sottocampo ^s, si devono impiegare i seguenti codici:

0    =    RINVII
1    =    COAUTORE
2    =    CURATORE
3    =    TRADUTTORE
4    =    PREFATORE
5    =    ILLUSTRATORE 

TAG 13  AUTORI COLLETTIVI

-    Campo ripetibile

-    Codici di sottocampo: come TAG 10

TAG 14 INTESTAZIONE SECONDARIA AL TITOLO

Contiene il titolo proprio o i suoi primi elementi e va usato per i casi previsti dalle RICA (vedi par. 2, 23, 24, 29.2, 30.2, 32.4, 42) e per le altre intestazioni secondarie al titolo ritenute opportune da ciascun centro bibliografico. Per evitare di digitare nuovamente il titolo proprio, si consiglia di marcare gi� nel TAG 1 l'inizio e la fine del blocco scelto (con l'esclusione degli indicatori di sottocampo) usando i  tasti funzione <F3> (per l'inizio), <F4> (per la fine) e <F5> (per ripristinarlo) e di servirsi nuovamente del tasto funzione <F5> in questo TAG per inserire il titolo proprio o la parte del titolo voluta.  

-    Campo ripetibile

TAG 15  SOGGETTI

-    Campo ripetibile

-    Codici di sottocampo

     ^n   numero del soggetto
     ^1   descrittore              
     ^2   descrittore              -
     ^3   descrittore              -
     ^4   descrittore              -
     ^5   descrittore              -
     ^6   descrittore              -
     ^7   descrittore              -
     ^8   descrittore              -
     ^9   descrittore              -

-    Il descrittore e' la parola o il gruppo di parole scelto per rappresentare un concetto presente nel soggetto del documento
-   In caso di descrittori costituiti da piu' elementi separati da una virgola, da un punto o racchiusi fra parentesi tonde, 
tale punteggiatura deve essere inserita manualmente.

TAG 16 CLASSIFICAZIONE DECIMALE DEWEY

- Questo campo puo' essere utilizzato dalle biblioteche che adottano la CDD o altro tipo di classificazione. Si avverte che questo campo 
non e' ripetibile; non e' quindi possibile assegnare piu' di una classe allo stesso documento. Per far questo occorre modificare 
opportunamente la FDT, la FST e i formati di stampa.

Note: si raccomanda di mantenere l'uniformita' nel modo di inserimento del numero di classificazione al fine di ottenere un 
ordinamento perfetto da parte del sistema. Per lo stesso motivo, la sigla che nella classe 800 si pone davanti al numero per 
indicare il paese deve invece seguirlo.

TAG 31 INDICE CDD

^1	Voce d'indice
^2	contesto :
^3	contesto :
^4	contesto :
^5	contesto :
^6	contesto :

TAG 17  ABSTRACT

-    Campo ripetibile

NOTE

-    Introdurre l'abstract usando le parentesi uncinate (<...>) per delimitare gli elementi che si intendono invertire. 
Si ricorda di battere gli spazi vuoti all'esterno delle parentesi uncinate prima e dopo o fra di esse nel caso si presentino 
due parentesi (una di chiusura ed una di apertura) immediatamente successive.

           
 */

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;
//import java.lang.*;
//import java.sql.*;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.IsoRecordReader;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.abstractStructure.Delimiters;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.*;

public class Isisteca extends ISO2709Impl {
//	private final static String rt="#";
//	private final static String ft="#";
//	private final static String dl="^";        //' delimiter
	


    public Isisteca(byte[] stringa, Charset charset, String dTipo)  throws Exception {
    super();
//    setTerminator("#","#","^");
    iso2709Costruttore(stringa,charset,dTipo,0);
    initLinkUp();
    initLinkDown();
    initLinkSerie();
  }

    public Isisteca(byte[] stringa, Charset charset, String dTipo, String livello)  throws Exception {
    super();
//    setTerminator("#","#","^");
    iso2709Costruttore(stringa,charset,dTipo,Integer.parseInt(livello));
    initLinkUp();
    initLinkDown();
    initLinkSerie();
  }
    
    public Hashtable<String, List<Tag>> getRecordMapping() {
		Hashtable<String, List<Tag>> r=new Hashtable<String, List<Tag>>();
		
		List<Tag> aut=new Vector<Tag>();
		aut.add(new Tag("009","a",""));
		aut.add(new Tag("009","b",""));
		aut.add(new Tag("009","x",""));
		aut.add(new Tag("009","d",""));
		aut.add(new Tag("009","c",""));
		aut.add(new Tag("009","f",""));
		aut.add(new Tag("010","a",""));
		aut.add(new Tag("010","c",""));
		aut.add(new Tag("010","q",""));
		aut.add(new Tag("010","b",""));
		aut.add(new Tag("010","s",""));
		aut.add(new Tag("010","x",""));
		aut.add(new Tag("010","y",""));
		aut.add(new Tag("010","d",""));
		aut.add(new Tag("010","e",""));
		aut.add(new Tag("010","f",""));
		aut.add(new Tag("012","a",""));
		aut.add(new Tag("012","b",""));
		aut.add(new Tag("012","x",""));
		aut.add(new Tag("012","d",""));
		aut.add(new Tag("012","c",""));
		aut.add(new Tag("012","f",""));
		aut.add(new Tag("012","s",""));
		aut.add(new Tag("013","a",""));
		aut.add(new Tag("013","c",""));
		aut.add(new Tag("013","q",""));
		aut.add(new Tag("013","b",""));
		aut.add(new Tag("013","s",""));
		aut.add(new Tag("013","x",""));
		aut.add(new Tag("013","y",""));
		aut.add(new Tag("013","d",""));
		aut.add(new Tag("013","e",""));
		aut.add(new Tag("013","f",""));
		r.put("AUT", aut);
		
		List<Tag> tit=new Vector<Tag>();
		tit.add(new Tag("001","a",""));
		tit.add(new Tag("001","c",""));
		tit.add(new Tag("001","d",""));
		tit.add(new Tag("001","e",""));
		tit.add(new Tag("001","h",""));
		tit.add(new Tag("001","i",""));
//		tit.add(new Tag("006","a",""));
//		tit.add(new Tag("006","b",""));
//		tit.add(new Tag("006","e",""));
//		tit.add(new Tag("006","f",""));
		
		tit.add(new Tag("011","a",""));
		tit.add(new Tag("011","i",""));
		tit.add(new Tag("011","p",""));
		tit.add(new Tag("011","r",""));
		tit.add(new Tag("011","h",""));
		r.put("TIT", tit);
		
		List<Tag> num=new Vector<Tag>();
		num.add(new Tag("008","",""));
		r.put("NUM", num);
		
		// collezione
		List<Tag> cll=new Vector<Tag>();
		tit.add(new Tag("006","a",""));
		tit.add(new Tag("006","b",""));
		tit.add(new Tag("006","e",""));
		tit.add(new Tag("006","f",""));
		r.put("CLL", cll);
		
		List<Tag> mat=new Vector<Tag>();
		mat.add(new Tag("030","",""));
		r.put("MAT", mat);
		
		List<Tag> sbj=new Vector<Tag>();
		sbj.add(new Tag("015","n",""));
		sbj.add(new Tag("015","1",""));
		sbj.add(new Tag("015","2",""));
		sbj.add(new Tag("015","3",""));
		sbj.add(new Tag("015","4",""));
		sbj.add(new Tag("015","5",""));
		sbj.add(new Tag("015","6",""));
		sbj.add(new Tag("015","7",""));
		sbj.add(new Tag("015","8",""));
		sbj.add(new Tag("015","9",""));
		r.put("SBJ", sbj);
		
//		List<Tag> bib=new Vector<Tag>();
//		bib.add(new Tag("741","B",""));
//		r.put("BIB", bib);
		
		List<Tag> inv=new Vector<Tag>();
		inv.add(new Tag("021","n",""));
		r.put("INV", inv);
		
//		712 = biblioteca (o codice biblioteca)
		
		return r;
	}

	public String getRecordTypeDescription() {
		return "CDS/ISIS TECA ISO2709 format.";
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
          Isisteca not=new Isisteca();    // crea un nuovo elemento Isisteca
          not.dati=new Vector<Tag>();
          Tag t=new Tag("011",' ',' ');
          t.setRawContent(v.getRawContent());
          not.dati.addElement(t);
 //          ISO2709.creaNotizia(0,v,this.getTipo(),this.getLivello());
 //         Utils.Log("Display:");
 //         Utils.Log(not.toTag());
          not.setBid(getBid());
//          not.setTerminator("#","#","^");
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
    	System.out.println("ERROR: Isisteca/getSignature");
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
  
  public Isisteca() {
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

public RecordReader getRecordReader(InputStream dataFile) throws UnsupportedEncodingException {
	RecordReader r=new IsoRecordReader(dataFile,delimiters,"ISO-8859-1");
	r.setChToIndex(channels);
	return r;
}

@Override
public void setImage(BufferedImage image, int maxx, int maxy) {
	// TODO Auto-generated method stub
	
}

@Override
public String getBase64Image() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getAvailabilityAndOrPrice() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void setAvailabilityAndOrPrice(String availabilityAndOrPrice)
		throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

@Override
public String getPublisherName() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void setPublisherName(String publisherName) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

@Override
public String getRecordModificationDate() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void setRecordModificationDate(String date) {
	// TODO Auto-generated method stub
	
}
}