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
import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
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
	this.removeTags("950");
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
  				Tag t=new Tag("035",' ',' ');
  				t.addField(new Field("a",temp.substring(23)));
  				addTag(t);
  			}
  		}
  		else if(temp.startsWith("Tipo documento:")){
  			Tag t=new Tag("315",' ',' ');
			t.addField(new Field("a",temp.substring(15).trim()));
			addTag(t);
  		}
  		else if(temp.startsWith("Titolo:")){
  			temp = temp.substring(7).trim();
  			temp = temp.replaceAll("\\*","");
  			Tag t=new Tag("200",' ',' ');
			t.addField(new Field("a",temp));
			addTag(t);
  		}
  		else if(temp.startsWith("Note Generali:")) {
  			temp = temp.substring(14).trim();
  			Tag t=new Tag("300",' ',' ');
			t.addField(new Field("a",temp));
			addTag(t);
  		}
  		else if(temp.startsWith("Pubblicazione:")){
  			temp = temp.substring(14).trim();
  			String  editore, anno = "",luogo;
  			StringTokenizer st1 = new StringTokenizer(temp,":,");
  			Tag t=new Tag("210",' ',' ');
			
  			luogo = st1.nextToken().trim();
  			t.addField(new Field("a",luogo));
  			if(st1.hasMoreTokens()){
  				editore = st1.nextToken().trim();
  				t.addField(new Field("c",editore));
  			}
  			if(st1.hasMoreTokens()){
  				anno = st1.nextToken().trim();
  				t.addField(new Field("d",anno));
  			}
  			addTag(t);
  		}
  		else if(temp.startsWith("Descrizione fisica:")){
  			temp = temp.substring(19).trim();
  			Tag t=new Tag("215",' ',' ');
			t.addField(new Field("a",temp));
			addTag(t);
  		}
  		else if(temp.startsWith("Fa parte di:")){
  			temp = temp.substring(12).trim();
  			temp = temp.replaceAll("\\*","");
  			temp = temp.split("#")[0];
  			Tag t=new Tag("461",' ',' ');
			t.addField(new Field("a",temp));
			addTag(t);
  			
  			if(linkUp==null) linkUp=new Vector<RecordInterface>();
			RecordInterface ma=RecordFactory.buildRecord(0, "Titolo:"+temp, "sbn_sutrs", 0);
			linkUp.addElement(ma);
  		}
  		else if(temp.startsWith("Collezione:")) {
  			temp = temp.substring(11).trim();
  			temp = temp.replaceAll("\\*","");
  			temp = temp.split("#")[0];
  			Tag t=new Tag("410",' ',' ');
			t.addField(new Field("a",temp));
			addTag(t);
  			
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
  				Tag ta=new Tag("463",' ',' ');
  				ta.addField(new Field("a",temp));
  				addTag(ta);
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
	  			
	  			Tag t=new Tag("700",' ',' ');
				
	  			
	  			if(nome_cognome.contains(",")) {
		  			t.addField(new Field("a",nome_cognome.split(",")[0].trim()));
		  			t.addField(new Field("b",nome_cognome.split(",")[1].trim()));
	  			}
	  			else {
	  				t.addField(new Field("a",nome_cognome));
	  			}
	  			if(codice!=null) tag700=tag700+dl+"3"+codice;
				addTag(t);
	  		}
		}
  		else if(temp.startsWith("Soggetti:")){
  			temp = temp.substring(9).trim();
  			Tag t=new Tag("615",' ',' ');
			t.addField(new Field("a",temp));
			addTag(t);
  		}
  		else if(temp.startsWith("Paese di pubblicazione:")){
  			temp = temp.substring(23).trim();
  			Tag t=new Tag("102",' ',' ');
			t.addField(new Field("a",temp));
			addTag(t);
  		}
  		else if(temp.startsWith("Lingua di pubblicazione:")){
  			temp = temp.substring(24).trim();
  			Tag t=new Tag("101",' ',' ');
			t.addField(new Field("a",temp));
			addTag(t);
  		}
  		
  		else if(temp.contains("ICCU") && temp.contains("ident")){
  			int g=temp.indexOf(":");
  			bid = temp.substring(g+1);
  		}
  		else if(temp.startsWith("Localizzazioni:")) {
  			temp=temp.substring(15);
  			String[] l=temp.split("#");
  			for(int i=0;i<l.length;i++) {
  				Tag t=new Tag("950",' ',' ');
  				t.addField(new Field("l",l[i]));
  				addTag(t);
  			}
  		}
  	}
  	
  	if(bid==null)bid="";
  	
  	if(bid.contains("/")){
		String[] bidparts = bid.split("/");
		bid = bidparts[bidparts.length-2]+bidparts[bidparts.length-1];
	}
  	
  	inString = inString.trim();
  	try {
		Tag t=new Tag("001",' ',' ');
		t.setRawContent(bid);
		addTag(t);
		
		t=new Tag("970",' ',' ');
		t.setRawContent(inString);
		addTag(t);
	} catch (JOpac2Exception e) {
		e.printStackTrace();
	}
	
  	 	
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