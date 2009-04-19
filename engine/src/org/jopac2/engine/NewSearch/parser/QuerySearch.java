package org.jopac2.engine.NewSearch.parser;
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
 * classe di adattamento tra jopac e nuovo parser
 */

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.BitSet;

import java.sql.Connection;
import java.sql.SQLException;

import org.jopac2.engine.NewSearch.BitSetUtils;
import org.jopac2.engine.NewSearch.NewItemCardinality;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.parserRicerche.parser.booleano.EvalAlbero;
import org.jopac2.engine.parserRicerche.parser.exception.ExpressionException;
import org.jopac2.engine.parserRicerche.tree.Nodo;


/**
 * 
 * @author albert
 *
 */
public class QuerySearch {

	private static Logger logger = Logger
			.getLogger("JOpac2.NewSearch.parser.QuerySearch");

	private Nodo sopTree = null;
	private BitSet resultSet = null;
	private Connection conn = null;
	@SuppressWarnings("unused")
	private String ricercaOriginale="";
	@SuppressWarnings("unused")
	private String ricercaOttimizzata="";

	private static void InitLogger() {
		//logger.setLevel(Level.ALL); // setta logger al livello massimo
		logger.setLevel(Level.OFF); // disabilita logger
	}

	public QuerySearch(String ricercaJopac, Connection conn) throws ExpressionException {
		InitLogger();
		//try {
		this.ricercaOriginale=ricercaJopac;
			this.sopTree = EvalAlbero.creaAlberoJopac(ricercaJopac);
		//} catch (ExpressionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		// TIT=a b c & AUT=www & AUT=ddd
		// (TIT=A & TIT=B & TIT=c) & AUT=www & AUT=ddd
		this.espandeFoglie(this.sopTree);
		this.sopTree.switchToSOP();
		this.ricercaOttimizzata=sopTree.toString();
		this.conn = conn;
	}
	
	// TIT=a b c & AUT=www & AUT=ddd
	// (TIT=A & TIT=B & TIT=c) & AUT=www & AUT=ddd
	private void espandeFoglie(Nodo tree){
		if(tree.isOperatore()){
			espandeFoglie(tree.getSinistro());
			espandeFoglie(tree.getDestro());		
		} else {
			// sono su una foglia
			String valore=tree.getValoreAsString();
			
			String parola="";
			StringTokenizer st = new StringTokenizer(valore, "=");			
			String classe = st.nextToken().trim();

			// se e' presente un solo token, allora impostare la classe come ANY di default
			if(!st.hasMoreTokens()){
				parola = classe;
				classe="ANY";
			} else {
				parola=st.nextToken().trim();
			}
			StringTokenizer stPar=DbGateway.paroleTokenizer(parola);
			int numeroParole=stPar.countTokens();
			//TODO si puo' rendere in maniera ricorsiva inserendo un nodo and, a sx la prima parola
			// a dx il resto delle parole e poi richiamando espandeFoglie
			if(numeroParole>1){				
				// il nodo attuale diventa un nodo and
				tree.setAND();
				tree.setValore(null);
				// il nodo sx prende il primo token
				Nodo sx=new Nodo(classe+"="+stPar.nextToken());
				tree.setSinistro(sx);
				Nodo currNode=tree;
				// nel ciclo for aggiungo 2 nodi: un sinistro e un and
				for(int i=1;i<numeroParole-1;i++){					
					Nodo nsx=new Nodo(classe+"="+stPar.nextToken());
					Nodo nroot=new Nodo(nsx,null,null);  // il nodo dx non e' ancora fissato					
					nroot.setAND();
					currNode.setDestro(nroot);
					currNode=nroot;
				}
				// il nodo dx prende l'ultimo token
				Nodo dx=new Nodo(classe+"="+stPar.nextToken());
				currNode.setDestro(dx);				
			}
		}		
	}

	public String toString() {
		return sopTree.toString();
	}

	/**
	 * ottimizza la query eliminando i termini in and con cardinalita' nulla
	 * popola le foglie con NewItemCardinality
	 * @throws SQLException 
	 * 
	 */
	public void optimize(boolean useStemmer) throws SQLException {
		sopTree.normalizza();
		//System.out.println(DisegnaAlbero.stampaAlbero(sopTree));
		// TODO: contare il tempo totale del walktree
		// TODO: contare il tempo totale delle operazioni and e or
		// TODO: computare il tempo totale delle operazioni di cardinalita
		// TODO: computare tempo totale operazioni di fetch  
		walkTree(sopTree, useStemmer);
		//System.out.println(DisegnaAlbero.stampaAlbero(sopTree));
		NewItemCardinality root=(NewItemCardinality)sopTree.getValore();
		//System.out.println("risultato:"+root.bit.ToVector());
		this.resultSet=root.bit;		
		//logger.log(Level.INFO, sopTree.toString());
	}

	// con l'eliminazione del nodeclone, e' possibile che si percorrano piu'
	// volte le stesse parti dell'albero
	// va quindi testato il caso e non va sostituito l'oggetto originario con un
	// nuovo oggetto MyItemCardinality
	public long walkTree(Nodo tree, boolean useStemmer) throws SQLException {
		// TODO: commentare questa istruzione  
		if (tree.getValore()== null
				|| !tree.getValore().getClass().getName().equals(
						NewItemCardinality.class.getName())) {
			if (tree.isOperatore()) {
				walkTree(tree.getSinistro(), useStemmer);
				walkTree(tree.getDestro(), useStemmer);				
				// effettua operazione and o or
				// TODO: inserire qui ottimizzazione se cardinalita di uno dei sottoalberi e' nulla
				NewItemCardinality n = new NewItemCardinality(0, "", "", -1, 0);
				if(tree.isAnd()) {					
					NewItemCardinality s=(NewItemCardinality)tree.getSinistro().getValore();
					NewItemCardinality d=(NewItemCardinality)tree.getDestro().getValore();					
					long now = System.currentTimeMillis();
					n.bit=(BitSet)s.bit.clone();
					n.bit.and(d.bit);
					n.setCardinality(n.bit.cardinality());				
					n.setOperationTime(System.currentTimeMillis()-now);
				}
				if(tree.isOr()) {					
					NewItemCardinality s=(NewItemCardinality)tree.getSinistro().getValore();
					NewItemCardinality d=(NewItemCardinality)tree.getDestro().getValore();
					long now = System.currentTimeMillis();
					n.bit=(BitSet)s.bit.clone();
					n.bit.or(d.bit);
					n.setCardinality(n.bit.cardinality());
					n.setOperationTime(System.currentTimeMillis()-now);
				}	
				// TODO: inserire qui operatore not
				//n.setClasseParola()
				tree.setValore(n);
				return n.getCardinality();
			} else {
				// il valore del nodo e nella forma classe=parola
				NewItemCardinality n = new NewItemCardinality();				
				n.setClasseParola(tree.getValoreAsString(),null);	// TODO era staticDataComponent			
				if(n.getClasseAsString().equals("ANY")){										
					//effettua la ricerca e popola il bitarray di notizie
					n.bit=n.doRicercaBitArrayAny(this.conn, useStemmer);
				} else {					
					//n.ricercaId_lcp(this.conn);// forse si puo risalire alla connessione in altro modo
					//effettua la ricerca e popola il bitarray di notizie
					//n.bit=n.doRicercaBitArray(this.conn,n.getId_lcp());
					if(!n.getClasseAsString().equals("ERRORE")){
						n.setId_lcp(-1);
						n.bit=n.doRicercaBitArrayJolly(this.conn, useStemmer);
					} else {
						n.setId_lcp(-1);
						n.bit=null;						
					}
				}
				tree.setValore(n);
				return n.getCardinality();
			}
		} else {
			// qui tree.getValore() non e' mai nullo
			// ramo gia' parserizzato
			return ((NewItemCardinality) tree.getValore()).getCardinality();
		}
	}

	/**
	 * esegue la query
	 * 
	 */
	public void evaluate() {
		// attualmente effettuato in walktree 
		// per evitare il flag su parti di albero replicate
	}

	public Vector<Long> getResultsAsVector() {
		return BitSetUtils.ToVector(resultSet);
	}

	/**
	 * popola il vettore con le cardinalita' dei vari elementi della query
	 * per ora restituisce il vettore vuoto 
	 * @return
	 */
	public Vector<NewItemCardinality> getQueryCardinality() {
		//TODO: restituire il vettore corretto
		return new Vector<NewItemCardinality>();
	}

}
