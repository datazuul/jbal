package org.jopac2.engine.parserricerche.parser.booleano;
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
 * classe per la valutazione di espressioni
 * grammatica:
 * 
 * espressione: -- termine --+---------------+-- 
 *                           +-- + termine --+
 *                          
 * termine    : -- fattore --+---------------+--
 *                           +-- * fattore --+
 * 
 * fattore    : --+-------+--+-- numero ----------+-- 
 *                +-- ! --+  +-- ( espressione ) -+
 *  
 * 
 * History
 * 07.06.05 Albert: implementato il not
 * 26.09.05 Romano e Albert: implementato algoritmo per forma SOP
 * 29.04.06 Albert: implementata semplice gestione errori
 *
 */

import java.util.logging.*;

import org.jopac2.engine.parserricerche.parser.exception.ExpressionException;
import org.jopac2.engine.parserricerche.parser.exception.MissingBracketException;
import org.jopac2.engine.parserricerche.parser.exception.MissingOperatorException;
import org.jopac2.engine.parserricerche.parser.exception.SyntaxErrorException;
import org.jopac2.engine.parserricerche.parser.exp.Espressione;
import org.jopac2.engine.parserricerche.tree.Nodo;
import org.jopac2.engine.parserricerche.tree.stampa.StampaAlbero;


/**
 * 
 * @author albert
 *
 */
public class EvalAlbero {

	private static Logger logger = Logger.getLogger("parserRicerche.parser.booleano.EvalAlbero");	

	public EvalAlbero() {
		
	}

	private static Nodo opAnd(Nodo a, Nodo b) {
		Nodo and=Nodo.NodoAnd(a,b);
		return and;
	}

	private static Nodo opOr(Nodo a, Nodo b) {
		Nodo or=Nodo.NodoOr(a,b);
		return or;
	}

	private static Nodo opNot(Nodo a) {
		a.switchNot();
		return a;
	}

	private static Nodo fattore(Espressione exp) throws ExpressionException {
		Nodo rit = new Nodo();
		boolean azioneNot = false;
		if (exp.isNOT) {
			azioneNot = true;
			exp.leggiSimbolo();
		}
		if (exp.isAlfa) {  //exp.isNum
			// vechia righa
			//rit = exp.sym;
			//rit.set(exp.sym);
			rit.setValore(exp.sym);
			exp.leggiSimbolo();
		} else   
			// senza questo else viene perso un fattore
			// nel caso di fattori non separati correttamente da operatori
			// p.es: a(b+c) invece di a*(b+c)
		if (exp.sym.equals("(")) {
			exp.leggiSimbolo(); // consumo (
			rit = espressione(exp);
			if(!exp.sym.equals(")")){
				throw new MissingBracketException(exp);
			}
			exp.leggiSimbolo();
		}
		if (azioneNot)
			rit = opNot(rit);
		// TODO se ritorno "" sono in errore!?
		if(!rit.isOperatore()){
			if(((String)rit.getValore()).equals("")) {			
				throw new SyntaxErrorException(exp);
				//System.out.println("rit vuoto "+rit);
			}
		}
		return rit;
	}

	private static Nodo termine(Espressione exp) throws ExpressionException {
		Nodo rit  = fattore(exp);
		while (exp.isAND) {
			exp.leggiSimbolo(); // consumo operatore
			Nodo sec = fattore(exp);
			rit = opAnd(rit, sec);
		}
		return rit;
	}

	private static Nodo espressione(Espressione exp) throws ExpressionException {
		Nodo rit = termine(exp);
		while (exp.isOR) {
			exp.leggiSimbolo(); // consumo operatore
			Nodo sec = termine(exp);
			rit = opOr(rit, sec);
		}
		return rit;
	}
	
	/*
	 * costruisce un albero corrispondente alla espressione passata  
	 */
	public static Nodo creaAlbero(String exp) throws ExpressionException {
		Espressione x = new Espressione(exp);	    
		Nodo rit = espressione(x);
		logger.log(Level.INFO, "exp " + exp + 
				"[NOT:"+rit.isNotSuOperatori()+"]"+
				"[SOP:"+rit.isSOP()+"]"+
				":=" + rit.toString());
		return rit;
	}
	
	public static Nodo creaAlberoJopac(String pExp) throws ExpressionException {		
		Espressione exp = new Espressione(pExp,true);	    
		Nodo rit = espressione(exp);
		if(!exp.isCompleted){
			throw new MissingOperatorException(exp);
		}
		logger.log(Level.INFO, "exp " + pExp + 
				"[NOT:"+rit.isNotSuOperatori()+"]"+
				"[SOP:"+rit.isSOP()+"]"+
				":=" + rit.toString());
		return rit;
	}
	/**
	 * valuta l'albero passato effettuando le ottimizzazioni necessarie 
	 * @param exp
	 * @return
	 */
	public static String eval(Nodo exp) {
		return exp.toString();
	}
	
	public static void main(String[] args) {
		Nodo r2;
		try {
			//r2 = EvalAlbero.creaAlbero("!5*(1+(2*3)+4)");
			r2=EvalAlbero.creaAlberoJopac("autore=trampus(pippo|pluto&2=paperino)");
			System.out.println(r2.toString());
			//System.out.println(r2.PercorsoAlbero(0));		
			StampaAlbero.print(r2);
		} catch (ExpressionException e) {
			e.printStackTrace();
		}

	}

}
