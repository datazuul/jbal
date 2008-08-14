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

import org.jopac2.utils.HtmlCodec;
import org.jopac2.utils.JOpac2Exception;

public class Sol_sutrs extends Sutrs {
//  private static String rt=String.valueOf((char)0x1d);
//  private static String ft=String.valueOf((char)0x1e);
  private static String dl=String.valueOf((char)0x1f);        //' delimiter
	
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
  				addTag("035  "+dl+"a"+temp.substring(23));
  			}
  		}
  		else if(temp.startsWith("Type :")){
  			addTag("315  "+dl+"a"+temp.substring(6).trim());
  		}
  		else if(temp.startsWith("Isbd :")){
  			temp = temp.substring(6).trim();
  			addTag("200  "+dl+"a"+temp);	
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
  			addTag("215  "+dl+"a"+temp);
  		}
  		else if(temp.startsWith("Fa parte di:") || temp.startsWith("Collezione:") ){
  			temp = temp.substring(12).trim();
  			temp = temp.replaceAll("\\*","");
  			temp = temp.split("#")[0];
  			addTag("410  "+dl+"a"+temp);
  		}
  		else if(temp.startsWith("Creator :") || temp.startsWith("Contributor :")){
  			temp = temp.substring(temp.indexOf(":")+1).trim();
  			if(temp.contains(",")){ addTag("700  "+ dl+"a"+temp.split(",")[0].trim()+ dl+"b"+temp.split(",")[1].trim()); }
  			else addTag("700  "+dl+"a"+temp);
		}
  		else if(temp.startsWith("Subject :")){
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
  	addTag("001"+bid);
  	addTag("970  " + inString);
  }
  
  /* (non-Javadoc)
   * @see JOpac2.dataModules.iso2709.ISO2709Impl#clearSignatures()
   */
  @Override
  public void clearSignatures() throws JOpac2Exception {
  	this.removeTag("970");
  }
}
