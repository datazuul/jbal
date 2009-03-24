package org.jopac2.utils;

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
* @version	??/??/2002
* 
* @author	Romano Trampus
* @version	??/??/2002
* 
*/

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import java.net.URLDecoder;

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;

import com.ibm.icu.text.Transliterator;

public class Utils {

  public static String cr=String.valueOf((char)13);
  public static String lf=String.valueOf((char)10);

  
  public static final String SEPARATORI_PAROLE=" ,.;()/-'\\:=@%$&!?[]#*<>\016\017";
  
  static public String parseQuery(String query) {
	if(query.startsWith("&")) query=query.substring(1);
    StringTokenizer tk=new StringTokenizer(query,"&");
    String newQuery="";
    String currentOperator="";

    while(tk.hasMoreTokens()) {
      String classe=tk.nextToken();
      if(tk.hasMoreTokens()) {
	      String parola=tk.nextToken();
	      String operatore="and";
	      if(tk.hasMoreTokens()) {
	    	  operatore=tk.nextToken();
	    	  operatore=operatore.substring(operatore.indexOf("=")+1);
	      }
	      classe=classe.substring(classe.indexOf("=")+1);
	      parola=parola.substring(parola.indexOf("=")+1);
	      parola=parola.trim();
	      
	      if(operatore.equals("%26")||operatore.equalsIgnoreCase("and")) {
	        operatore="&";
	      }
	      if(operatore.equals("%7C")||operatore.equalsIgnoreCase("or")) {
	        operatore="|";
	      }
	      if(operatore.length()==0) operatore="&";
	
	      /** @todo normalizzare i parametri delle query (togliere i +, decodificare gli hex */
	      parola=Utils.pulisci(parola);
	
	      if(parola.length()>0) {
	        StringTokenizer tk1=new StringTokenizer(parola,"+ ");
	        while (tk1.hasMoreTokens()) {
	          newQuery+=currentOperator+classe+"="+tk1.nextToken();
	          currentOperator=operatore;
	        }
	      }
      }
    }
    return newQuery;
  }
    
  
  static public String parseNewQuery(String query) {
    return query.replaceAll("%26", "&").replaceAll("&amp;", "&");
  }
  
  // IMPORTANTE: non usare un'istanza statica perché va in errore Cocoon
  //static final Transliterator accentsconverter = Transliterator.getInstance("NFD; [:Nonspacing Mark:] Remove; NFC");
  
  public static String removeAccents(String s) {
	  Transliterator accentsconverter = Transliterator.getInstance("NFD; [:Nonspacing Mark:] Remove; NFC");
		return accentsconverter.transliterate(s);
	}
  
  
  /**
   * 13/6/2003 - R.T.
   *    Dato un vettore e una stringa ritorna un vettore con tutti e soli
   *    gli elementi che contengono la stringa. CASE SENSITIVE.
   */
  public static Vector<String> getElements(Vector<String> v, String key) {
    Vector<String> r=new Vector<String>();
    int i;

    for(i=0;i<v.size();i++) {
      if(((String)v.elementAt(i)).indexOf(key)>=0) {
        r.addElement(v.elementAt(i));
      }
    }
    return r;
  }
  
  public static String Windows2UTF8(String s) {
  	String r=s;

  	try {
		//String s1 = new String(s.getBytes(), "Windows-31j"); // make that into a String
	  	byte[] newbytes = s.getBytes("UTF-8"); // encode the String into UTF-8
	  	r=new String(newbytes,"UTF-8");
  	}
  	catch (Exception e) {
  		e.printStackTrace();
  	}
	return r;
  }
  
  public static String pulisci(String s) {
    try {
      s = URLDecoder.decode(s, "ISO-8859-1");
    }
    catch(Exception e) {

    }
    s=s.replace('\'','+');
    s=s.replace('*','+');
    s=s.replace('-','+');
    s=s.replace('.','+');
    s=s.replace(',','+');
    s=s.replace(';','+');
    return s;
  }
  
  public static String pad(String l,long i) {
      String val=String.valueOf(i);
      int k=val.length();
      l=l.substring(k)+val;
      return(l);
  }
  
  /**
   * Return a string concatenation of prefix and (e1 || e2) content
   * or empty string
   * Note: if e1 has a value e2 is not evaluated
   * @param prefix
   * @param e1
   * @param e2
   * @return
   */
  public static String ifExists(String prefix, Field e1, Field e2) {
	  String r="";
	  if(e1!=null && e1.getContent().length()>0) {
		  r+=prefix+e1.getContent();
	  }
	  else if(e2!=null && e2.getContent().length()>0) {
		  r+=prefix+e2.getContent();
	  }
	  return r;
  }
  
  /**
   * Return a string concatenation of prefix and e1
   * or empty string
   * @param prefix
   * @param e1
   * @return
   */
  public static String ifExists(String prefix, Field e1) {
	  String r="";
	  if(e1!=null && e1.getContent().length()>0) {
		  r+=prefix+e1.getContent();
	  }
	  return r;
  }
  
  /**
   * Return a string concatenation of prefix and tag.getRawContent
   * or empty string
   * @param prefix
   * @param tag
   * @return
   */
  public static String ifExists(String prefix, Tag tag) {
	  String r="";
	  if(tag!=null && tag.getRawContent().length()>0) {
		  r+=prefix+tag.getRawContent();
	  }
	  return r;
  }
  
  public static String ifExists(String prefix,String element) {
    if(element!=null && element.length()>0) {
      return prefix+element;
    }
    else {
      return "";
    }
  }
  
  public static String ifExists(String prefix,Vector<String> element) {
    if(element!=null && element.size()>0) {
      return ifExists(prefix,element.elementAt(0));
    }
    else {
      return "";
    }
  }

  public static String ifExists(String prefix,String element,String suffix) {
    if(element.length()>0) {
      return prefix+element+suffix;
    }
    else {
      return "";
    }
  }

  public static String unQuote(String s) {
    String dl=String.valueOf((char)0x1b);
    String ctk;
    String r="";
    if(s.startsWith(dl+"H")) s=s.substring(2);
    StringTokenizer tk=new StringTokenizer(s,dl);
    if(tk.hasMoreTokens()) {
      ctk=tk.nextToken();
      if(!ctk.substring(0,1).equals(dl)) r+=ctk;
      while(tk.hasMoreTokens()) {
        ctk=tk.nextToken();
        if(ctk.substring(0,1).equals("I")) {
          r+="*";
        }
        r+=ctk.substring(1);
      }
    }
    r=r.replace('_',' ');
    return(r);
  }

/**
  public static Vector opVectorOr(Vector a,Vector b) {
    Vector c=new Vector(b);

    c.removeAll(a);
    c.addAll(a);

    return c;
  }


  public static Vector opVectorAnd(Vector a,Vector b) {
    Vector c=new Vector(a);

    c.retainAll(b);
    return c;
  }

  public static Vector opVectorNot(Vector a,Vector b) {
    Vector c=new Vector(a);
    c.removeAll(b);
    return c;
  }

  public static Vector opVector(Vector a, Vector b,String op) {
//    System.out.println("operazione "+op);
    if(op.equals("|")) return opVectorOr(a,b);
    if(op.equals("!")) return opVectorNot(a,b);
    return opVectorAnd(a,b);

  }
*/
  /**
   * @deprecated
   */
 /* public static String mid(String x, int start,int len) {
    return x.substring(start-1,start+len-1);
  }*/

  /**
   * @deprecated
   * @param x
   * @param start
   * @return
   */
  /*public static String mid(String x, int start) {
    return x.substring(start-1);
  }*/
  
 /* public static String getResource(String name) {
  	servletContext sc=
  }*/
  
  /** Prende una stringa nella forma
   * classe=parola [&|] classe=parola .....
   * e la ottimizza per cardinalita' delle ricerche */
  public static String purgeQuery(String query) {


   // Separo in gruppi di AND, questi gruppi poi vanno in or
     StringTokenizer tk=new StringTokenizer(query,"|");
     StringTokenizer andTk;
     String newQuery="";
     String andCQuery="";
     String andQuery="";
     String item="";
     String classe="",parola="";

     //int idClasse=0;
     //int cardinal=0;

     Hashtable<Integer,String> valori=new Hashtable<Integer,String>();
     
     valori.put(new Integer(1),"AUT");
     valori.put(new Integer(2),"TIT");
     valori.put(new Integer(3),"ISN");
     valori.put(new Integer(7),"SGG");
     
     //String searchParole="";
 
     while(tk.hasMoreTokens()) {  // qui dentro lavoro solo sul gruppo &
       //TreeMap andItems=new TreeMap();
       
   // il token ha solo stringhe in and, mi basta che quella con cardinalita'
   // minore sia in testa
       andQuery=tk.nextToken();
       andCQuery="";
       andTk=new StringTokenizer(andQuery,"&");
       while(andTk.hasMoreTokens()) {
         
         item=andTk.nextToken();
         classe=item.substring(0,item.indexOf("="));
         parola=item.substring(item.indexOf("=")+1);

         // classe puo' essere un numero o una stringa. Se e' una stringa prendi il numero.
         try {
           // ovvero prova a dedurne un numero
         	Integer i=new Integer(classe);
            classe=(String)valori.get(i);
         }
         catch (Exception e) {}
         
         andCQuery+="&"+classe+"="+parola;
       }
       newQuery="|"+andCQuery.substring(1); // toglie l' & iniziale
     }
     if(newQuery.length()>0) {
       newQuery=newQuery.substring(1); // toglie l' | iniziale
     }
     return newQuery;
   }
   

public static Vector<Parametro> queryToVector(String s) {

    //String currentOperator="|";
    s=purgeQuery(s);
    StringTokenizer tk=new StringTokenizer(s,"&|=",true);
    Vector<Parametro> result=new Vector<Parametro>();

    

    if(s.length()>0) {  //dopo l'ottimizzazione stringa non vuota
      String classe,parola; //,dummy;
      while(tk.hasMoreTokens()) {
        classe=tk.nextToken();
        tk.nextToken(); // simbolo di =
        parola=tk.nextToken();
        
        result.addElement(new Parametro(classe,parola));
        
        try{tk.nextToken();}catch(Exception e){}
        
        
/* TODO il client fa solo ricerche in AND, per l'OR ci penseremo.
 * 
 */        
 /*       resultSet=Utils.opVector(resultSet,doRaffRicerca(Integer.parseInt(classe),
          parola.trim(),currentSearch),currentOperator);
          currentOperator=tk.nextToken().trim();*/
      }
    }
    return result;
}

	public static String[] extractAttribute(String message, String attribute) {
		if(message.startsWith("&")) message=message.substring(1);
		String[] att=message.split("&");
		int k=0;
		for(int i=0;i<att.length;i++) {
			if(att[i].startsWith(attribute+"=")) { // qui solo conto quanti saranno
				String[] z=att[i].substring(attribute.length()+1).split(",");
				k+=z.length;
			}
		}
		if(k>0)	{
			String[] r=new String[k];
			for(int i=0;i<att.length;i++) {
				if(att[i].startsWith(attribute+"=")) {
					String[] z=att[i].substring(attribute.length()+1).split(",");
					for(int h=0;h<z.length;h++) {
						k--;
						r[k]=z[h];
					}
				}
			}
			return r;
		}
		else {
			return null;
		}		
	}
	
	public static String removeAttribute(String queryString, String attribute) {
		if(queryString != null) {
			attribute=attribute.trim();
			boolean questionMark=queryString.startsWith("?");
			if(questionMark) queryString=queryString.substring(1);
			if(queryString.endsWith("&")) queryString=queryString.substring(0,queryString.length()-1);
			String[] attributes=queryString.split("&");
			StringBuffer ret=new StringBuffer();
			for(int i=0;i<attributes.length;i++) {
				if(attributes[i].contains("=")) {
					String[] p=attributes[i].split("=");
					if(!p[0].toLowerCase().trim().equalsIgnoreCase(attribute)) ret.append("&"+attributes[i]);
				}
				else {
					if(!attributes[i].toLowerCase().trim().equalsIgnoreCase(attribute)) ret.append("&"+attributes[i]);
				}
			}
			String r=ret.toString();
			if(r.startsWith("&")) r=r.substring(1);
			if(questionMark) r="?"+r;
			return r;
		}
		else {
			return null;
		}
	}

	public static String percentuale(long max, long current) {
		return Long.toString((current*100)/max);
	}
}
