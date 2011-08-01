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

import org.jopac2.engine.parserRicerche.parser.booleano.EvalAlbero;
import org.jopac2.engine.parserRicerche.tree.Nodo;

/**
 * 
 * @author albert
 *
 */
public class StampaAlbero {

	public static void print(Nodo tree){
		StampaAlbero s= new StampaAlbero();
		int maxliv=s.LivelliAlbero(tree);
		String r="";
		for(int i=0;i<=maxliv;i++){
		  r+=s.getLivello(tree,i)+"\n";
		}
		System.out.println(r);
	}
	
	public int LivelliAlbero(Nodo tree) {
		return tree.GetLivelli();
	}
	
	public String getLivello(Nodo tree,int liv){
		GetLivello g =new GetLivello();
		g.getLivello(tree,liv,0,"");
		String e="";
		for(int i=0;i<g.v.size();i++){
			e+=(String)g.v.elementAt(i)+" ";
		}
		return e;
	}
	
	public String spazi(int n){
		String t="";
		for(int r=0;r<n;r++)
			t+=" ";
		return t;
	}

	/*
	 * stampa l'albero
	 */
	public String PercorsoAlbero(Nodo tree,int l) {
		String s="";
		for(int i=0;i<=l;i++) s+=".";		
		if(tree.isOperatore()){
			return 
			s+tree.getValore().toString()+"\n"+
			s+PercorsoAlbero(tree.getSinistro(),l+1)+"\n"+			
			s+PercorsoAlbero(tree.getDestro(),l+1);
		} else {
			String v=tree.getValore().toString();
			if(tree.isNot())
				v="("+v+")";
			return s+v;
		}
	}	
	
	public String getAlbero(Nodo tree){		
		Vector<String[]> v=DisegnaAlbero.getAlbero(tree);
		String e="";
		int p=0;
		int m=0;
		for(int i=v.size()-1;i>=0;i--){
			m=p*2+1;
			String[] gw=(String[])v.elementAt(i);
			e+=spazi(p);
			for(int j=0;j<gw.length;j++){
				e+=gw[j]+spazi(m);
			}
			e+="\n";
			p=p*2+1;
		}
		return e;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {	
		//Nodo r2 = EvalAlbero.creaAlberoJopac("!5&(1|(2&3)|4)");
		Nodo r3 = EvalAlbero.creaAlberoJopac("2=i&2=promessi&2=sposi&(2=trampus|2=alighieri|2=caramia)");
		//StampaAlbero s=new StampaAlbero();
		//System.out.println(s.LivelliAlbero(r2));
		r3.switchToSOP();
		System.out.println(DisegnaAlbero.stampaAlbero(r3));
		//System.out.println(s.getAlbero(r2));				
	}

}

class GetLivello {
	java.util.Vector<String> v=new java.util.Vector<String>();
	public void getLivello(Nodo tree,int reqLiv,int l,String pos){
		if(l==reqLiv)
			v.add(tree.getValore()+pos);
		if(tree.isOperatore()){
			getLivello(tree.getSinistro(),reqLiv,l+1,pos+"s");
			getLivello(tree.getDestro(),reqLiv,l+1,pos+"d");
		}		
	}	
}