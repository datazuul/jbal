package org.jopac2.engine.Managers;
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
* @author	Romano Trampus
* @version  19/01/2005
* 
* @author	Romano Trampus
* @version	19/05/2005
*/
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.util.*;

import org.jopac2.utils.RecordItem;

public class SebinaParser implements Parser {

	/* TODO
	 * 
	 * DA FARE TUTTO
	 *  (non-Javadoc)
	 * @see JOpac2.Managers.Parser#parse(java.io.BufferedReader, java.lang.String)
	 */
	public Vector<RecordItem> parse(BufferedReader in, String host, String contextDir, String dbname) {
		Vector<RecordItem> result = new Vector<RecordItem>();
		
		String preamble="<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" "+
			"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\" " +
			"xmlns:dcterms=\"http://purl.org/dc/terms/\" " +
			"xmlns:dcq=\"http://purl.org/dc/qualifiers/1.0/\" " + 
			"xmlns:dc=\"http://purl.org/dc/elements/1.1/\"> "; 
		String ret="";
		
		if(dbname==null) dbname="sebinaopac";
		
// inizio: <!-- Tab. che carica i titoli -->
// fine: <!-- Fine tab. che carica i titoli -->		
		
		boolean titoli=false;
		String materiale="";
		String href="";
		String titolo="";
		
		try
		{
			String s;
			boolean eof = false;
			s = in.readLine();

			while( !eof ) {
				if(s.contains("<!-- Tab. che carica i titoli -->")) {titoli=true;s="";}
				if(s.contains("<!-- Fine tab. che carica i titoli -->")) titoli=false;
				
				if(titoli) {
					// controlla che inizi per "<TR>"
					if(s.startsWith("<TR>")) {
						materiale="";
						href="";
						titolo="";
						
						// le prime tre colonne non ci servono, quindi cerca il terzo "</td>" e
						// toglie quello che c'è prima (compreso)
						
						for(int i=0;i<3;i++) {
							s=s.substring(s.indexOf("</td>")+4);
						}
						
						// il tipo di materiale si trova in una cosa tipo:
						// [<i>Monografia</i>]
						
						int b=s.indexOf("[<i>")+4;
						int e=s.indexOf("</i>]");
//						System.out.println(b+" "+e);
						materiale=s.substring(b,e);
						
						// il link è del tipo:
						
						// <A Href="....."
						s=s.substring(s.indexOf("Href=")+6);
						b=s.indexOf("\"");
						href=s.substring(0,b);
						
						// il titolo finisce per </A>
						titolo=s.substring(b+2,s.indexOf("</A>"));
						
						int ampindex = titolo.indexOf("&");
						String temp = "";
						while(ampindex!=-1){
							temp = titolo.substring(ampindex+1,ampindex+2);
							if(!(temp.equals("#"))){
								titolo = titolo.substring(0,ampindex) + "&#38;" + titolo.substring(ampindex+1,titolo.length());
								//titolo = titolo.replaceAll("&"+temp,"&#38;"+temp);
							}
							ampindex = titolo.indexOf("&",ampindex+1);
						}

//						System.out.println("Materiale: "+materiale);
//						System.out.println("Href: "+href);
//						System.out.println("Titolo: "+ titolo);
						ret="<rdf:Description rdf:about=\""+host+href+"\"> ";
						ret+="<dc:source>"+host+"</dc:source>";
						ret+="<dc:title>"+titolo+"</dc:title>";
						ret+="<materiale>"+materiale+"</materiale>";
						ret+="</rdf:Description>";
						result.add(new RecordItem(host,dbname,preamble+ret+"</rdf:RDF>","rdf","xml",null,null));
//						System.out.println(s);
					}
				}
				
				
				try {
					s = in.readLine();
					if ( s == null ) {
            			eof = true;
						in.close();
					}
				}
				catch (EOFException eo) {
            		eof = true;
        		}
				catch (IOException e) {
					System.out.println("IO Error : "+e.getMessage());
        		}
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}