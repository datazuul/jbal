package org.jopac2.engine.parserricerche.parser.exp;
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
import java.util.StringTokenizer;
import java.util.logging.*;

/**
 * classe di appoggio alla valutazione di espressioni
 * 
 * @author caramia
 * 
 */
public class Espressione {
	private static Logger logger = Logger.getLogger("parser.Espressione");
	// private static FileHandler lfh = new FileHandler("%t/exp.log"); 
	// perche' non va?
	private String exp;
	private StringTokenizer st;
	public String sym;
	public boolean isAlfa;
	public boolean isOp;
	public boolean isAND;
	public boolean isOR;
	public boolean isNOT;
	public boolean isCompleted;
	public int currentPosition=0;

	public static String OP_AND = "*"; 
	public static String OP_OR = "+";  
	public static String OP_NOT = "!";
	public static String OPERATORI = OP_AND + OP_OR + OP_NOT;
	public static final String BLANK = " ";

	private static void InitLogger() {
		// logger.setLevel(Level.ALL); // setta logger al livello massimo
		logger.setLevel(Level.OFF); // disabilita logger
	}
	
	/**
	 * costruttore completo
	 * @param exp
	 * @param operatoriJopac: se true  usa &=AND, |=OR, !=NOT
	 * 						: se false usa *=AND, +=OR, !=NOT
	 */
	public Espressione(String exp,boolean operatoriJopac){
		InitLogger();
		logger.log(Level.INFO, "classe Espressione creata per stringa:[" + exp+ ","+operatoriJopac+"]");		
		if(operatoriJopac){
			OP_AND="&";
			OP_OR="|";
			OP_NOT="!";
			OPERATORI = OP_AND + OP_OR + OP_NOT;
		}
		this.exp = exp.trim(); // pulisce dagli spazi inziali e finali
		// commentato per bug su TIT=a b
		//this.exp = this.exp.replaceAll(" ", ""); // elimina spazi interni
		this.st = new StringTokenizer(this.exp, OPERATORI + "()", true);
		this.currentPosition=0;
		leggiSimbolo();
	}

	/**
     * costruttore di default 
     * @param exp
     */
	public Espressione(String exp) {
		this(exp,false);
	}

	/**
	 * segnala errore nella posizione corretta
	 * @return
	 */
	public String segnalaErrore(){
		try {
			String w=exp.substring(0,currentPosition);
			w+="<b>["+exp.substring(currentPosition,currentPosition+sym.length());
			w+="]</b>"+exp.substring(currentPosition+sym.length());
			return w;			
		} catch (Exception e){
			return exp.toString();			
		}		
	}
	
	/**
	 * e' un numero se inizia per una cifra [0..9] puo' parserizzare sia un id che
	 * una forma id1=id2
	 * 
	 * @param x
	 * @return x e' un numero?
	 */
	/*private static boolean isNumero(String x) {
		char c = '.';
		if (x.length() > 0)
			c = x.charAt(0);
		return (c >= '0') && (c <= '9');
	}*/

	/**
	 * e' un numero se inizia per una cifra [0..9] puo' parserizzare sia un id che
	 * una forma id1=id2
	 * 
	 * @param x
	 * @return x e' un numero?
	 */
	private static boolean isAlfanumerico(String x) {
		char c = '.';
		if (x.length() > 0)
			c = x.toUpperCase().charAt(0);
		return ((c >= '0')&&(c<='9'))||((c>='A')&&(c<='Z'));
	}
	
	public String toString() {
		return "currsym:[" + sym + "] exp:[" + exp + "]";
	}

	public String leggiSimbolo() {
		if (st.hasMoreTokens()) {
			if(sym!=null){
				currentPosition+=sym.length(); // avanza posizione
			}
			sym = st.nextToken(); 	
			sym=sym.trim(); // elimina spazi iniziali e finali del simbolo
			logger.log(Level.INFO, "symbolo:[" + sym + "]");
			//isNum = isNumero(sym);
			isAlfa = isAlfanumerico(sym);
			isAND = sym.equals(OP_AND);
			isOR = sym.equals(OP_OR);
			isNOT = sym.equals(OP_NOT);
			isOp = isAND || isOR || isNOT;
			isCompleted = false;
		} else {
			currentPosition=exp.length();
			sym = "";
			isCompleted = true;
		}
		return sym;
	}
}