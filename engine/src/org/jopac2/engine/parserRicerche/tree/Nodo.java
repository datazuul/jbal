package org.jopac2.engine.parserRicerche.tree;
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
 * @author albert
 */
public class Nodo {
	
	//trasformare la query in SOP (sum of producs) ha il vantaggio di 
	//a. avere solo 2 livelli in memoria (per ricorsione) rispetto 
	//	 ad una espressione con parentesi
	//b. poter eliminare termini and se la cardinalità di uno è nulla
	
	/**
	 * TODO: utilizzare de morgan forse comporta un aumento di lavoro per la 
	 * forma SOP?
	 * TODO: inserire i tempi di elaborazione delle varie fasi
	 * TODO: inserire campi con stringa prima e dopo le trasformazioni
	 * TODO: inserire test tramite tavola verità delle forme trasformate 
	 */
	
	private boolean isNot = false;
	private boolean isAnd=false;
	private boolean isOr=false;
	private Object valore = "";
	private Object Sinistro = null;
	private Object Destro = null;

	public Nodo getDestro() {
		return (Nodo)Destro;
	}

	public void setDestro(Nodo destro) {
		Destro = destro;
	}

	public boolean isNot() {
		return isNot;
	}

	public void setNot(boolean isNot) {
		this.isNot = isNot;
	}

	/*
	 * il not viene spostato sulle foglie
	 da  
	!*       
    / \     
    L R
    a 	  
     +
    / \
   !L !R (foglie terminali)	  
	 */
	public void switchNot() {
		if (this.isOperatore()) {
			// se non sono foglia devo
			// negare i sottoalberi
			this.getSinistro().switchNot();
			this.getDestro().switchNot();
			// e cambiare operatore
			if (this.isAnd())
				this.setOR();
			else
				this.setAND();
		} else {
			this.isNot = !this.isNot;
		}
	}

	public Nodo getSinistro() {
		return (Nodo)Sinistro;
	}

	public void setSinistro(Nodo sinistro) {
		Sinistro = sinistro;
	}

	public Object getValore() {
		return valore;
	}
	/*
	 * necessario stampare il nodo operatore anche col il suo valore
	 * che potrebbe non essere uguale a AND o OR
	 */
	public String getValoreAsString(){
	  String v = "";
	  if (this.valore!=null)
		  v=this.getValore().toString();
	  if(this.isAnd())
		  v+="AND";
	  if(this.isOr())
		  v+="OR";
	  if (this.isNot)
		  v = "(-" + v +")";		  	 
	  return v;
	}
	
	public void setValore(Object valore) {
		this.valore = valore;
	}
	
	/**
	 * crea un nodo di tipo and
	 * @param sx
	 * @param dx
	 * @param valore
	 * @return
	 */
	public static Nodo NodoAnd(Object sx, Object dx,Object valore){
		Nodo w=new Nodo(sx,dx,valore);
		w.setAND();
		return w;
	}
	public static Nodo NodoAnd(Object sx, Object dx){
		return NodoAnd(sx,dx,null);
	}
	
	public static Nodo NodoOr(Object sx, Object dx,Object valore){
		Nodo w=new Nodo(sx,dx,valore);
		w.setOR();
		return w;
	}
	public static Nodo NodoOr(Object sx, Object dx){
		return NodoOr(sx,dx,null);
	}
	
	/*
	 * costruttore vuoto 
	 */
	public Nodo() {		
	}

	public Nodo(Object valore) {
		this.valore = valore;
	}
   /*
   * crea un nodo che punta ai sottoalberi sx e dx
   * per creare un nodo AND o OR dare
   * new Nodo(sx,dx,Nodo.AND) o Nodo(sx,dx,Nodo.OR) 
   */
	/**
	 * @param sx
	 * @param dx
	 * @param valore
	 */
	public Nodo(Object sx, Object dx, Object valore) {
		this.Sinistro = sx;
		this.Destro = dx;
		this.valore = valore;
		this.isAnd=false;
		this.isOr=false;
		/*
		// setta in automatico operatore se il valore passato lo richiede 
		if(this.valore!=null) {
			if(this.valore.equals(AND)){
				this.setAND();
			}
			if(this.valore.equals(OR)){
				this.setOR();
			}
		} else {
			// evita di settare il valore del nodo
			// con stringa fissa che poi va tenuta aggiornata
			// in caso di switchNot o switchToSOP				
			this.setValore("");
		}*/
	}

	public void setAND() {
		this.isAnd=true;
		this.isOr=false;
	}

	public void setOR() {
		this.isAnd=false;
		this.isOr=true;
	}

	public boolean isAnd(){
		return this.isAnd;
	}

	public boolean isOr(){
		return this.isOr;
	}
	
	public boolean isOperatore(){
		return this.isOr || this.isAnd;
	}

	public String toString() {
		/*		 
		String v = "";
		if(this.valore!=null) 
			v=this.valore.toString();
		if (this.isNot)
			v = "(-" + v + ")";
		if (this.isOperatore()) {
			if(this.isAnd)
				v=AND;
			else
				v=OR;
			return "[" + this.Sinistro.toString() + "]" + v + "["
					+ this.Destro.toString() + "]";
		} else {
			return v;
		}*/
		String v=this.getValoreAsString();
		if(this.isOperatore())
			return "[" + this.Sinistro.toString() + "]" + v + "[" + this.Destro.toString() + "]";
		else
			return v;
	}

	/**
	 * scorre l'albero per cercare se esiste un not su un nodo operatore 
	 * (situazione che non dovrebbe esistere visto l'algoritmo usato per il not)
	 */
	public boolean isNotSuOperatori() {
		if (this.isOperatore()) {
			boolean s = ((Nodo) this.Sinistro).isNotSuOperatori();
			boolean d = ((Nodo) this.Destro).isNotSuOperatori();
			return s || d || this.isNot;
		} else
			// sulla foglia restituisco falso
			return false;
	}
	
	/**
	 * scorre l'albero e restituisce true se trova solo and
	 */
	public boolean isSubTreeSoloAnd() {
		if (this.isOperatore()) {
			if (this.isAnd()) {
				boolean s = this.getSinistro().isSubTreeSoloAnd();
				boolean d = this.getDestro().isSubTreeSoloAnd();
				return s && d ;
			}
			else {
				return false;
			}
		} else
			// sulla foglia restituisco true
			return true;
	}
	
	/**
	 * scorre l'albero: una forma SOP è data da
	 * se il nodo è
	 *  1. terminale->true
	 *  2. or -> i sottoalberi devono essere SOP
	 *  3. and ->  i sottoalberi devono contenere solo AND
	 * 
	 */
	public boolean isSOP() {
		if(this.isOperatore()){
			if (this.isOr()) {
				boolean s=this.getSinistro().isSOP();
				boolean d=this.getDestro().isSOP();				
				return s && d;
			} else {
				boolean s=this.getSinistro().isSubTreeSoloAnd();
				boolean d=this.getDestro().isSubTreeSoloAnd();				
				return s && d;
			}
		} else {  // su foglia restituisco true
			return true;
		}
	}	
	
	/**
	 * restituisce una copia dell'albero 
	 */
	public Nodo NodeClone(){		
		if(this.isOperatore()) {
			Nodo r=new Nodo(this.getSinistro().NodeClone(),this.getDestro().NodeClone(),this.valore);
			if(this.isAnd)
				r.setAND();
			if(this.isOr)
				r.setOR();
			return r;
		} else {
			//TODO: nel caso di foglia not, viene settato correttamente il valore?
			return this;
		}
	}
	
	
	/**
	 * elimina rami con (A AND (NOT A))
	 * rami con (A OR A)
	 * (A OR (NOT A)) etc... 
	 */
	public Nodo normalizza(){
		//TODO: Implementare per ottimizzare ricerche
		return this;
	}			
	
	/**	      	  
	 trasforma un albero nella forma a sx nella corrispettiva forma a destra	              
	*       +    
   / \     / \
  +  Z    *   *
 / \     / \ / \
 L R     L Z R Z
	 */
	public void switchToSOP() {
		while(!this.isSOP())
		if(this.isOperatore()){
			// sul nodo OR verifico solo che la situazione sottostante sia corretta
			if (this.isOr()) {
				boolean s=this.getSinistro().isSOP();
				boolean d=this.getDestro().isSOP();	
				if(!s)
				  this.getSinistro().switchToSOP();				
				if(!d)
				  this.getDestro().switchToSOP();
			} else {
				// sul nodo and effettuo le trasformazioni
				boolean s=this.getSinistro().isSubTreeSoloAnd();					
				if(!s) { //sono sul nodo and e il ramo sx non è solo and
					if(this.getSinistro().isOr()){ // se è esattamente il livello sotto agisco
						// eliminati i nodeclone in quanto i sottoalberi possono rimanere unici
						// e non necessitano di venire duplicati. la navigazione dell'albero
						// non verrà inficiata dalla presenza di puntatori a sottoalberi provenienti da nodi diversi
						Nodo z1=this.getDestro();//.NodeClone();
						Nodo z2=this.getDestro();//.NodeClone();
						Nodo l=this.getSinistro().getSinistro();//.NodeClone();
						Nodo r=this.getSinistro().getDestro();//.NodeClone();
						
						Nodo ns=new Nodo(l,z1,null);
						ns.setAND();
						Nodo nd=new Nodo(r,z2,null);
						nd.setAND();
						this.setOR();
						this.setDestro(nd);
						this.setSinistro(ns);					
					} else { 
						// sono su un nodo and, il cui sottoalbero non è composto solo da and
						// ma il nodo direttamente sottostante non è un or. devo scendere
						// di livello per poter effettuare le trasformazioni 
						this.getSinistro().switchToSOP();
					}					
				}	
				// la gestione del ramo destro è speculare al ramo sinistro sopra codificato
				boolean d=this.getDestro().isSubTreeSoloAnd();
				if(!d) { //sono sul nodo and e il ramo dx non è solo and
					if(this.getDestro().isOr()){ // se è esattamente il livello sotto agisco
						// eliminati i nodeclone per il motivo di cui sopra
						Nodo z1=this.getSinistro();//.NodeClone();
						Nodo z2=this.getSinistro();//.NodeClone();
						Nodo l=this.getDestro().getSinistro();//.NodeClone();
						Nodo r=this.getDestro().getDestro();//.NodeClone();
						
						Nodo ns=new Nodo(l,z1,null);
						ns.setAND();
						Nodo nd=new Nodo(r,z2,null);
						nd.setAND();
						this.setOR();
						this.setDestro(ns);
						this.setSinistro(nd);
					} else {
						this.getDestro().switchToSOP();
					}
				}				
			}
		}  // se sono su una foglia esco senza azioni
	}	
	
	public int GetLivelli(){
		ContaLivelli c= new ContaLivelli();
		return c.Conta(this);
	}
}

class ContaLivelli {
	private int maxliv=0;
	
	public int Conta(Nodo tree){
		this.Conta(tree,0);
		return maxliv;
	}
	
	public void Conta (Nodo tree,int l){
		if(l>maxliv)
			maxliv=l;
		if(tree.isOperatore()){
			Conta(tree.getSinistro(),l+1);
			Conta(tree.getDestro(),l+1);
		}
	}
}