package org.jopac2.engine.parserricerche.tree.stampa.ascii;
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
 * classe per supportare il disegno in ascii
 * costruisce una lavagna su cui indirizzare stringhe di testo
 * con coordinate riga e colonna
 * origine [0,0] -> basso sx
 * 
 * @author Caramia
 *
 */

public class DrawAscii {

	private static final int MAXR=512;
	private static final int MAXC=512;
	private char[][] Screen=new char[MAXR+1][MAXC+1];
	private int maxriga=-1;
	private int maxcol=-1;
	
	public DrawAscii(){
		// righe e colonne massime sono gestite in maniera dinamica  
		// e non determinate in anticipo
	}
	
	/**
	 * disegna una stringa alla posizione indicata
	 * @param riga
	 * @param colonna
	 * @param label	 
	 */
	public void DrawString(int riga, int colonna, String label){		
		if(riga>maxriga)
			maxriga=riga>MAXR?MAXR:riga;
		if(colonna+label.length()-1>maxcol)
			maxcol=colonna+label.length()-1>MAXC?MAXC:colonna+label.length()-1;
		for(int j=0;j<label.length();j++)
			Screen[riga>MAXR?MAXR:riga][colonna+j>MAXC?MAXC:colonna+j]=label.charAt(j);
	}
	
	/**
	 * 	restituisce l'attuale renderizzazione
	 * 	@return
	 */
	public String getScreen(){
		String rit="";
		for(int r=maxriga;r>=0;r--) {
			for(int c=0;c<=maxcol;c++){
				char ch=Screen[r][c];
				if(ch<=' ')
					ch=' ';
				rit+=ch;
			}
			rit+="\n";				
		}
		return rit;
	}
	
	public String toString(){
		return this.getScreen();
	}
	
	public void printScreen(){
		System.out.println(this.getScreen());
	}
	
	public static void main(String[] args) {
		DrawAscii w=new DrawAscii();
		w.DrawString(0,0,"pippo");
		w.DrawString(10,10,"pluto");
		w.printScreen();
		w.DrawString(3,2,"*");
		w.printScreen();		
	}

}
