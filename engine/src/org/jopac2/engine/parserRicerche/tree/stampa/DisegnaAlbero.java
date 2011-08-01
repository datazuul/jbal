package org.jopac2.engine.parserRicerche.tree.stampa;
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
import java.util.Vector;

import org.jopac2.engine.parserRicerche.tree.Nodo;
import org.jopac2.engine.parserRicerche.tree.stampa.ascii.DrawAscii;


/**
 * 
 * @author albert
 *
 */
public class DisegnaAlbero {
	
/*	private static String spazi(int n){
		String t="";
		for(int r=0;r<n;r++)
			t+="*";
		return t;
	}*/
	
	/**
	 * restituisce una stringa con immagine in ascii dell'albero
	 * 
	 * @param tree
	 * @return
	 */	
	public static String stampaAlbero(Nodo tree) {
		try {
			return stampaAlberoInt(tree);
		} catch (RuntimeException e) {
			return "albero non stampabile ["+e.getMessage()+"]";
		}
	}
	
	/**
	 * restituisce una stringa con immagine in ascii dell'albero
	 * @param tree
	 * @return
	 */		
	private static String stampaAlberoInt(Nodo tree) {
		Vector<String[]> v = getAlbero(tree);
		Vector<int[]> x = new Vector<int[]>(); // conterrà le x dei nodi
		for (int i = v.size() - 1; i >= 0; i--) { // parto dal basso
			// gw è l'array degli elementi al livello i
			String[] gw = (String[]) v.elementAt(i);
			int[] pos = new int[gw.length];
			int curr_x = 0;
			int len_foglia = 0;
			// livello minore
			if (i == v.size() - 1) {
				for (int j = 0; j < gw.length / 2; j++) {
					int k = j * 2;
					// foglia sx
					len_foglia = gw[k].length();
					pos[k] = curr_x ;
					curr_x += len_foglia;
					// foglia sopra
					String[] lw=(String[]) v.elementAt(i-1);					
					curr_x+=lw[j].length();
					// foglia dx
					len_foglia = gw[k + 1].length();
					pos[k + 1] = curr_x;
					curr_x += len_foglia;
					// spazio fisso tra due sotto alberi
					curr_x += 2;					
				}
			} else {//livelli superiori, calcolo la media livelli inferiori
				// array livello subito sottostante, ultimo aggiunto nell'array
				int[] p=(int[]) x.elementAt(x.size()-1);
				for (int k = 0; k < gw.length ; k++) {
					int sx=p[k*2];
					int dx=p[k*2+1];
					pos[k]=(sx+dx)/2; 
					//centrare la lunghezza stringa nello spazio tra sx e dx
				}
			}
			x.add(pos);		
		}
		
		String rit="";
		DrawAscii video=new DrawAscii();
		for(int k=0;k<x.size();k++){
			int iv=v.size()-1-k;
			String[] gw=(String[])v.elementAt(k);
			int[] pos=(int[])x.elementAt(iv);
			//gw w pos dovrebbero avere stessa lunghezza
			//System.out.println(pos.length+":"+gw.length);						 
			//String r="";
			//String rp="";
			int y=pos.length/2;			
			for(int l=0;l<((y==0)?1:y);l++){
				int j=l*2;
				//devo inserire in posizione pos[j] il carattere gw[j]
				//calcolo spazi da aggiungere
				//int s=r.length();
				//int sp=pos[j]-s;				
				//r+=spazi(sp)+gw[j];
				//s=rp.length();
				//sp=pos[j]-s;
				//rp+=spazi(sp)+"+";
				
				//inserire a due a due e mettere - di unione
				video.DrawString(iv*2,pos[j],gw[j]);
				video.DrawString(iv*2+1,pos[j],"+");
				if(k>0){ //per i livelli non root
					video.DrawString(iv*2,pos[j+1],gw[j+1]);
					video.DrawString(iv*2+1,pos[j+1],"+");
					//System.out.println((pos[j]+1)+":"+(pos[j+1]-1));
					for(int t=pos[j]+1;t<=pos[j+1]-1;t++) {
						video.DrawString(iv*2+1,t,"-");
					}
				}
			}
			//rit+=rp+"\n";
			//rit+=r+"\n";			
		}
		rit=video.getScreen();
		return rit;		
	}

	/**
	 * restituisce un vettore con le componenti dell'albero in array per ciascun
	 * livello
	 * 
	 * @param tree
	 * @param l
	 * @param pos
	 * @param v
	 */
	public static Vector<String[]> getAlbero(Nodo tree) {
		Vector<String[]> v = new Vector<String[]>();
		getAlbero(tree, 0, 0, v);
		return v;
	}

	private static void getAlbero(Nodo tree, int l, int pos, Vector<String[]> v) {
		if (v.size() - 1 < l) {
			// w=2 elevato alla l
			int w = 1 << l;// shift
			// int w = 1;
			// for (int t = 0; t <= l - 1; t++)
			// w = w * 2;
			String[] liv = new String[w];
			for (int t = 0; t < w; t++)
				liv[t] = ".";
			v.add(liv);
		}
		((String[]) v.elementAt(l))[pos] = tree.getValoreAsString();
		if (tree.isOperatore()&l<=16) { //limite di 16 livelli per evitare ricorsione troppo profonda e heap space error
			getAlbero(tree.getSinistro(), l + 1, pos * 2, v); // +s=bit a 0
			getAlbero(tree.getDestro(), l + 1, pos * 2 + 1, v); // +d=bit a 1
		}
	}
}
