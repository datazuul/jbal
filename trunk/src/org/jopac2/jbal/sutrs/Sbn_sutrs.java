package org.jopac2.jbal.sutrs;


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
* @author	Iztok Cergol
* @version 19/07/2005
*/

import java.util.*;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.utils.JOpac2Exception;

public class Sbn_sutrs extends Sutrs {
//  private static String rt=String.valueOf((char)0x1d);
//  private static String ft=String.valueOf((char)0x1e);
  private static String dl=String.valueOf((char)0x1f);        //' delimiter
	
  /*public int delimiterPosition=27;
  public char delimiter=':';
  */
  public Sbn_sutrs(String notizia,String dTipo,String livello) {
    this.iso2709Costruttore(notizia,dTipo,Integer.parseInt(livello));
  }
 
  

  /* (non-Javadoc)
 * @see JOpac2.dataModules.iso2709.ISO2709Impl#clearSignatures()
 */
@Override
public void clearSignatures() throws JOpac2Exception {
	this.removeTag("950");
}



public void init(String stringa) {
  	inString="";
  	stringa = stringa.replaceAll("\\|                    "," ");
  	stringa = stringa.replaceAll("\n    ", " ");
  	stringa = stringa.replaceAll("  "," ").replaceAll("  "," ").replaceAll("  "," ").replaceAll("\\|","\n");
  	
  	//System.out.println(stringa);
  	
  	//String[] in=stringa.split("\n");
  	
  	String[] lines = stringa.split("\n");
  	
  	for(int index=0;index<lines.length;index++){
  		String temp = lines[index];
  		
  		if(!(temp.startsWith("Localizzazioni:"))){
  			if(!(inString.equals("")))inString = inString + "|";
  			inString = inString + temp;
  		}
  		
  		
  		if(temp.startsWith("Livello bibliografico:")){
  			if(temp.contains("Monografia")){
  				setBiblioLevel("M");
  				addTag("035  "+dl+"a"+temp.substring(23));
  			}
  		}
  		else if(temp.startsWith("Tipo documento:")){
  			addTag("315  "+dl+"a"+temp.substring(15).trim());
  		}
  		else if(temp.startsWith("Titolo:")){
  			temp = temp.substring(7).trim();
  			temp = temp.replaceAll("\\*","");
  			addTag("200  "+dl+"a"+temp);
  		}
  		else if(temp.startsWith("Note Generali:")) {
  			temp = temp.substring(14).trim();
  			addTag("300  "+dl+"a"+temp);
  		}
  		else if(temp.startsWith("Pubblicazione:")){
  			String tag2add = "210  ";
  			temp = temp.substring(14).trim();
  			String luogo, editore, anno = "";
  			StringTokenizer st1 = new StringTokenizer(temp,":,");
  			luogo = st1.nextToken().trim(); tag2add = tag2add + dl+"a"+luogo;
  			if(st1.hasMoreTokens()){editore = st1.nextToken().trim(); tag2add = tag2add +dl+"c"+editore;}
  			if(st1.hasMoreTokens()){anno = st1.nextToken().trim(); tag2add = tag2add+dl+"d"+anno;}
  			addTag(tag2add);
  		}
  		else if(temp.startsWith("Descrizione fisica:")){
  			temp = temp.substring(19).trim();
  			addTag("215  "+dl+"a"+temp);
  		}
  		else if(temp.startsWith("Fa parte di:")){
  			temp = temp.substring(12).trim();
  			temp = temp.replaceAll("\\*","");
  			temp = temp.split("#")[0];
  			addTag("461  "+dl+"a"+temp);
  			
  			if(linkUp==null) linkUp=new Vector<RecordInterface>();
			RecordInterface ma=RecordFactory.buildRecord(0, "Titolo:"+temp, "sbn_sutrs", 0);
			linkUp.addElement(ma);
  		}
  		else if(temp.startsWith("Collezione:")) {
  			temp = temp.substring(11).trim();
  			temp = temp.replaceAll("\\*","");
  			temp = temp.split("#")[0];
  			addTag("410  "+dl+"a"+temp);
  			
  			if(linkSerie==null) linkSerie=new Vector<RecordInterface>();
			RecordInterface ma=RecordFactory.buildRecord(0, "Titolo:"+temp, "sbn_sutrs", 0);
			linkSerie.addElement(ma);
  		}
  		else if(temp.startsWith("Comprende:")) {
  			temp = temp.substring(10).trim();
  			temp = temp.replaceAll("\\*","");
  			String[] temp1 = temp.split("}");
  			if(linkDown==null) linkDown=new Vector<RecordInterface>();
  			
  			for(int t=0;t<temp1.length;t++) {
  				int lc=temp1[t].indexOf("#");
  				if(lc>=0) temp=temp1[t].substring(0,temp1[t].indexOf("#"));
  				else temp=temp1[t];
  				addTag("463  "+dl+"a"+temp);
  				RecordInterface ma=RecordFactory.buildRecord(0, "Titolo:"+temp, "sbn_sutrs", 0);
  				linkDown.addElement(ma);
  			}
  		}
  		else if(temp.startsWith("Nomi:")){
  			temp = temp.substring(5).trim();
  			StringTokenizer st1 = new StringTokenizer(temp,"#}");
  			String nome_cognome, codice, tag700=null;
  			while(st1.hasMoreTokens()){
	  			nome_cognome = st1.nextToken().trim();
	  			if(nome_cognome.endsWith(","))nome_cognome = nome_cognome.substring(0,nome_cognome.length()-1);
	  			if(st1.hasMoreTokens()) {
		  			codice = st1.nextToken();	
		  			if(codice.contains("/")) {
		  				String[] s=codice.split("/");
		  				codice="";
		  				for(int i=2;i<4&&i<s.length;i++)
		  					codice = codice + s[i];
		  			}
	  			}
	  			else {
	  				codice=null;
	  			}
	  			if(nome_cognome.contains(",")) {
	  				tag700="700  "+ dl+"a"+nome_cognome.split(",")[0].trim()+ dl + 
	  						"b"+nome_cognome.split(",")[1].trim();
	  			}
	  			else {
	  				tag700="700  "+dl+"a"+nome_cognome;
	  			}
	  			if(codice!=null) tag700=tag700+dl+"3"+codice;
	  			addTag(tag700);
	  		}
		}
  		else if(temp.startsWith("Soggetti:")){
  			temp = temp.substring(9).trim();
  			addTag("615  "+dl+"a"+temp);
  		}
  		else if(temp.startsWith("Paese di pubblicazione:")){
  			temp = temp.substring(23).trim();
  			addTag("102  "+dl+"a"+temp);
  		}
  		else if(temp.startsWith("Lingua di pubblicazione:")){
  			temp = temp.substring(24).trim();
  			addTag("101  "+dl+"a"+temp);
  		}
  		
  		else if(temp.contains("ICCU") && temp.contains("ident")){
  			bid = temp;
  		}
  		else if(temp.startsWith("Localizzazioni:")) {
  			temp=temp.substring(15);
  			String[] l=temp.split("#");
  			for(int i=0;i<l.length;i++) {
  				addTag("950  "+dl+"l"+l[i]);
  			}
  		}
  	}
  	
  	if(bid==null)bid="";
  	
  	if(bid.contains("/")){
		String[] bidparts = bid.split("/");
		bid = bidparts[bidparts.length-2]+bidparts[bidparts.length-1];
	}
  	
  	inString = inString.trim();
  	addTag("001"+bid);
  	addTag("970  " + inString);
	
  	 	
  	/*String currentLabel="";
  	String currentContent="";
  	for(int i=0;i<in.length;i++) {
  		if((in[i].length()>delimiterPosition)&&(in[i].charAt(delimiterPosition)==delimiter)) {
  			if(currentContent.length()>0) {
  				dati.addElement(currentLabel+":"+currentContent.trim());
  			}
  			currentLabel=cleanLabel(in[i].substring(0,delimiterPosition));
  			currentContent=in[i].substring(delimiterPosition+1).trim();
  		}
  		else {
  			currentContent+=in[i];
  		}
  	}
  	if(currentContent.length()>0) {
		dati.addElement(currentLabel+":"+currentContent.trim());
	}*/
  }
	
}