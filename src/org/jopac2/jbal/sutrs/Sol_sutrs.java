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

import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.utils.HtmlCodec;
import org.jopac2.utils.JOpac2Exception;

public class Sol_sutrs extends Sutrs {
//  private static String rt=String.valueOf((char)0x1d);
//  private static String ft=String.valueOf((char)0x1e);
//  private static String dl=String.valueOf((char)0x1f);        //' delimiter
	
  /*public int delimiterPosition=27;
  public char delimiter=':';
  */
  public Sol_sutrs(String notizia,String dTipo,String livello) {
    this.iso2709Costruttore(notizia,dTipo,Integer.parseInt(livello));
  }

  public void init(String stringa) {
	  
  	inString="";
  	stringa=stringa.replaceAll("\r","\n");
  	stringa = stringa.replaceFirst("\n\t","");
  	stringa = stringa.replaceAll("\n\t","\n");

  	stringa = HtmlCodec.decode(stringa);
  	
  	String[] lines = stringa.split("\n");
  	
  	for(int index=0;index<lines.length;index++){
  		String temp = lines[index];
  		
  		if(!(temp.startsWith("Localizzazioni:"))){
  			if(!(inString.equals("")))inString = inString + "|";
  			inString = inString + temp;
  		}
  		if(temp.startsWith("Livello bibliografico:")){
  			if(temp.contains("Monografia")){
  				setType("M");
  				Tag t=new Tag("035",' ',' ');
  				t.addField(new Field("a",temp.substring(23)));
  				addTag(t);
  			}
  		}
  		else if(temp.startsWith("Type :")){
  			Tag t=new Tag("315",' ',' ');
			t.addField(new Field("a",temp.substring(6).trim()));
			addTag(t);
  		}
  		else if(temp.startsWith("Isbd :")){
  			temp = temp.substring(6).trim();
  			Tag t=new Tag("200",' ',' ');
			t.addField(new Field("a",temp));
			addTag(t);
  		}
  		/*else if(temp.startsWith("Publisher :")){
  			String tag2add = "210  ";
  			temp = temp.substring(11).tm();
  			String luogo, editore, anno = "";
  			StringTokenizer st1 = new StringTokenizer(temp,":,");
  			luogo = st1.nextToken().trim(); tag2add = tag2add + dl+"a"+luogo;
  			if(st1.hasMoreTokens()){editore = st1.nextToken().trim(); tag2add = tag2add +dl+"c"+editore;}
  			if(st1.hasMoreTokens()){anno = st1.nextToken().trim(); tag2add = tag2add+dl+"d"+anno;}
  			addTag(tag2add);
  		}*/
  		else if(temp.startsWith("Descrizione fisica:")){
  			temp = temp.substring(19).trim();
  			Tag t=new Tag("215",' ',' ');
			t.addField(new Field("a",temp));
			addTag(t);
  		}
  		else if(temp.startsWith("Fa parte di:") || temp.startsWith("Collezione:") ){
  			temp = temp.substring(12).trim();
  			temp = temp.replaceAll("\\*","");
  			temp = temp.split("#")[0];
  			Tag t=new Tag("410",' ',' ');
			t.addField(new Field("a",temp));
			addTag(t);
  		}
  		else if(temp.startsWith("Creator :") || temp.startsWith("Contributor :")){
  			temp = temp.substring(temp.indexOf(":")+1).trim();
  			if(temp.contains(",")){
  				Tag t=new Tag("700",' ',' ');
  				t.addField(new Field("a",temp.split(",")[0].trim()));
  				t.addField(new Field("b",temp.split(",")[1].trim()));
  				addTag(t);
  			}
  			else {
  				Tag t=new Tag("700",' ',' ');
  				t.addField(new Field("a",temp));
  				addTag(t);
  			}
		}
  		else if(temp.startsWith("Subject :")){
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
  		
  		/*else if(temp.contains("Identifier :")){
  			temp = temp.substring(12).trim();
  			bid = temp;
  		}*/
  		
  		else if(temp.contains("Identifier Sebina:")){
  			temp = temp.substring(18).trim();
  			bid = temp;
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
  }
  
  /* (non-Javadoc)
   * @see JOpac2.dataModules.iso2709.ISO2709Impl#clearSignatures()
   */
  @Override
  public void clearSignatures() throws JOpac2Exception {
  	this.removeTag("970");
  }
}
