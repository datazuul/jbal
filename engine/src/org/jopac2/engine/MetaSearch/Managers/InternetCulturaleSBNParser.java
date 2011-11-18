package org.jopac2.engine.MetaSearch.Managers;
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
* @version	19/01/2005
* 
* @author	Romano Trampus
* @version  19/01/2005
* 
* @author	Romano Trampus
* @version	19/05/2005
*/
import java.io.BufferedReader;
import java.util.*;

import org.jopac2.utils.RecordItem;

public class InternetCulturaleSBNParser implements Parser {

	public Vector<RecordItem> parse(BufferedReader r, String host, String contextDir, String dbname) {
		Vector<RecordItem> result = new Vector<RecordItem>();
		StringBuffer i=new StringBuffer();
		@SuppressWarnings("unused")
		int ntrovati=0;
		String inizioRecord="Livello bibliografico:";
		
		try {
			boolean trovati=false;
			boolean script=false;
			boolean record=false;
			boolean comment=false;
			
			String ti=r.readLine();
			while(ti!=null) {
				if(ti.contains("<script")||ti.contains("<SCRIPT")) {
					int ps=ti.indexOf("<script");
					if(ps==-1) ps=ti.indexOf("<SCRIPT");
					String ti1=ti.substring(ps);
					ti=ti.substring(0,ps);
					script=true;
					if(ti1.contains("</script>")||ti1.contains("</SCRIPT>")) {
						ps=ti1.indexOf("</script>");
						if(ps==-1) ps=ti1.indexOf("</SCRIPT>");
						ti=ti1.substring(ps);
						script=false;
					}
				}
				if(ti.contains("<!--")) {
					int ps=ti.indexOf("<!--");
					String ti1=ti.substring(ps);
					ti=ti.substring(0,ps);
					ps=ti1.indexOf("-->");
					if(ps!=-1) {
						ti=ti+ti1.substring(ps+3);
					}
					else {
						comment=true;
					}
				}
				
				ti=removeTags(ti);
				if(ti.contains(inizioRecord)) {
					if(record) {
						result.add(new RecordItem(host, dbname, setupRecord(i.toString()), "sutrs", "sbn_sutrs", "", ""));
						i.delete(0, i.length());
					}
					record=true;
				}
				if(!trovati && ti.contains("Trovati : ")) {
					ntrovati=parseTrovati(ti);
					trovati=true;
				}
				else {
					if(ti!=null && ti.length()>0 && record) i.append(ti.replaceAll("&nbsp;", " ")+"\n");
				}
				ti=r.readLine();
				while((script || comment) && ti !=null) {
					if(script && ti.contains("</script>")||ti.contains("</SCRIPT>")) {
						int ps=ti.indexOf("</script>");
						if(ps==-1) ps=ti.indexOf("</SCRIPT>");
						ti=ti.substring(ps);
						script=false;
					}
					else if(comment && ti.contains("-->")) {
						int ps=ti.indexOf("-->");
						ti=ti.substring(ps+3);
					}
					else {
						ti=r.readLine();
					}
				}
			}
			
			if(record) {
				result.add(new RecordItem(host, dbname, setupRecord(i.toString()), "sutrs", "sbn_sutrs", "", ""));
				i.delete(0, i.length());
			}
		
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**

Livello bibliografico:	Monografia
Tipo documento:	Testo a stampa
Autore:	Trampus, Antonio 
Titolo:	Riforme politiche e "pubblica felicita" negli                          scritti di Carli sul problema dell'educazione                          /  Antonio   Trampus 
Pubblicazione:	[Trieste] : Del Bianco, [1991-1992] 
Descrizione fisica:	P. 14- 40 ; 24 cm 
   Note Generali:
   Estr. da: Quaderni istriani 5/6
   Nomi:
   Trampus ,  Antonio 
Paese di pubblicazione:	ITALIA
Lingua di pubblicazione:	italiano    
Codice del documento:	IT\ICCU\USM\1275690 
Localizzazioni:	Tutte | OPAC locale  | Anagrafe Biblioteche | Nessuna
	Servizi
  MI1262 [USMO3] -  Biblioteca di
  scienze della storia e della documentazione
  storica dell'Universit‡ degli studi di
  Milano -  Milano -  MI
P	D	O	A


	 */
	
	private String setupRecord(String record) {
		record=record.replaceAll("[ \t]+", " ");
		String[] s=record.split("\n");
		String[] tags= {"Livello bibliografico:","Tipo documento:",
				"Autore:","Titolo:","Pubblicazione:","Descrizione fisica:",
				"Note Generali:","Nomi:","Paese di pubblicazione:",
				"Lingua di pubblicazione:","Codice del documento:",
				"Localizzazioni:","Fa parte di:","Collezione:",
				"Comprende:","Soggetti:"};
		boolean ch=true;
		while(ch) {
			ch=false;
			for(int i=1;i<s.length;i++) {
				if(!startWith(s[i].trim(),tags) && 
						(i<s.length-1 && !s[i+1].equals(":")) && 
						s[i].trim().length()>0) {
					s[i-1]=s[i-1]+" "+s[i];
					s[i]="";
					ch=true;
				}

				if(s[i].equals(":")) {
					s[i-1]=s[i-1]+":";
					s[i]="";
					ch=true;
				}
			}
		}
		record="";
		for(int i=0;i<s.length;i++) {
			if(s[i].length()>0)
				record=record+s[i].trim()+"\n";
		}
		record=record.replaceAll("\n","|");
		return record;
	}
	
	private boolean startWith(String s, String[] tags) {
		boolean r=false;
		for(int i=0;i<tags.length && !r; i++) {
			r=s.startsWith(tags[i]);
		}
		return r;
	}
	
	private String removeTags(String ti) {
		ti=ti.replaceAll("</td><td", "\t<td");
		ti=ti.replaceAll("</tr>", "\n");
		int s=ti.indexOf("<");
		int e=ti.indexOf(">");
		
		while(ti.contains("<") && ti.contains(">") && s<e) {
			String ti1=ti.substring(s+1,e);
			while(ti1.contains("<")) {
				s=ti.indexOf("<",s+1);
				ti1=ti.substring(s+1,e);
			}
			ti=ti.substring(0,s)+ti.substring(e+1);
			s=ti.indexOf("<");
			e=ti.indexOf(">");
		}
		return ti;
	}
	
	private int parseTrovati(String trovati) {
		int nt=0;
		trovati=trovati.replaceAll("[^0-9]", "");
		nt=Integer.parseInt(trovati);
		return nt;
	}
}