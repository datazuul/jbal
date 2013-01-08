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
* @author	Iztok Cergol
* @version	01/03/2005
* 
*/
/*
 * 01-03-2005 Iztok
 * 		per linkRecuperi stop-word
 */


import java.io.*;
import java.util.*;
@SuppressWarnings("unchecked")
public class StopWordRecognizer
{
	private TreeSet stopWordList;
	
	public StopWordRecognizer()
	{
		stopWordList = new TreeSet();
	}
	
	/**
	 * @param File
	 * @return carica dal File le stopword 
	 */
	public void LoadList(File f)
	{
		try
		{
			if(!f.exists()) {
				System.err.println("Not found: "+f.getCanonicalPath());
			}
			else {
				BufferedReader br = new BufferedReader(new FileReader(f));
				while(br.ready())
				{
					stopWordList.add(br.readLine().toLowerCase());
				}
				br.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param parola
	 * @return true se la parola e' nelle stopword, false altrimenti
	 */
	public boolean isStopWord(String parola)
	{
		return stopWordList.contains(parola.toLowerCase());
	}

	/**
	 * @param testo
	 * @return testo senza il testo tra parentesi (parentesi include)
	 */
	public String deparent(String testo){
		char aperta = (new Character('(')).charValue();
		char chiusa = (new Character(')')).charValue();
		char pntvrg = (new Character(';')).charValue();
		String ret="";
		boolean ok = true;
		char[] caratteri = new char[testo.length()];
		testo.getChars(0,testo.length(),caratteri,0);
		
		for(int i=0;i<caratteri.length;i++)
		{
			if(caratteri[i] == pntvrg) ok = false;
			if(caratteri[i] == aperta) ok = false;
			if(caratteri[i] == chiusa){ok = true; caratteri[i]=(new Character(' ').charValue());}
			if(ok)ret = ret + caratteri[i];
		}
		
		int i = ret.indexOf("pag");
		if(i==-1) i = ret.indexOf("p.");
		if(i==-1) i = ret.indexOf("p,");
		if(i==-1)
		{
			i = ret.indexOf("\r\n");
			if(i<100)i = ret.indexOf("\r\n",70);
		}
		if(i>0)ret = ret.substring(0,i);
		
		return ret;
		
	}
}