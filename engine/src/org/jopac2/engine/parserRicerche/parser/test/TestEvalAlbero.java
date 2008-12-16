package org.jopac2.engine.parserRicerche.parser.test;
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
import org.jopac2.engine.parserRicerche.parser.booleano.EvalAlbero;
import org.jopac2.engine.parserRicerche.parser.exception.ExpressionException;
import org.jopac2.engine.parserRicerche.tree.Nodo;
import org.jopac2.engine.parserRicerche.tree.stampa.DisegnaAlbero;

import junit.framework.*;


public class TestEvalAlbero extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testxxx() {
		Nodo n=new Nodo("pippo");
		assertTrue("OK",((String)n.getValore()).equals("pippo"));
	}

	public void testAnd() {
		Nodo v1=new Nodo("v1");
		Nodo v2=new Nodo("v2");
		Nodo n=Nodo.NodoAnd(v1,v2);
		assertTrue("rit" ,n.isAnd());
	}
	
	public void testOR() {
		Nodo v1=new Nodo("v1");
		Nodo v2=new Nodo("v2");
		Nodo n=Nodo.NodoOr(v1,v2);
		assertTrue("rit" ,n.isOr());
	}
	
	public void testNot() {
		Nodo v1=new Nodo("v1");
		Nodo v2=new Nodo("v2");
		Nodo n=Nodo.NodoAnd(v1,v2);
		n.switchNot();
		String w=n.toString();
		assertTrue("ok "+w , w.equals("[(-v1)]OR[(-v2)]"));
	}
	
	public void testNot2() {
		Nodo v1=new Nodo("v1");
		Nodo v2=new Nodo("v2");
		Nodo n=new Nodo(v1,v2,null);
		n.setOR();
		n.switchNot();
		String w=n.toString();
		assertTrue("ok:"+w , w.equals("[(-v1)]AND[(-v2)]"));
	}
	
	public void testNot3() {
		Nodo v1=new Nodo("v1");
		Nodo v2=new Nodo("v2");
		Nodo n=Nodo.NodoAnd(v1,v2);
		String w1=n.toString();
		n.switchNot(); //nego 2 volte ritorno alla stringa iniziale
		n.switchNot();
		String w2=n.toString();
		assertTrue("ok:"+w2 , w1.equals(w2));
	}	

	public void testop1() throws Exception {
		String r = EvalAlbero.creaAlbero("(1*2+3)*4").toString();
		assertTrue("OK:"+r,r.equals("[[[1]AND[2]]OR[3]]AND[4]"));
	}	
	public void testop2() throws Exception{
		String r = EvalAlbero.creaAlbero("!1+!(!2*!3)+!4").toString();
		//-a+-(-b*-c)+-d= -a+(b+c)+-d=-a+b+c+-d
		assertTrue("OK:"+r,r.equals("[[(-1)]OR[[2]OR[3]]]OR[(-4)]"));
	}	

	public void testop3() throws Exception{
		String r = EvalAlbero.creaAlbero("!1+!(!2*!3)*!4").toString();
		//-a+-(-b*-c)*-d= -a+(b+c)*-d=-a+b*-d+c*-d
		assertTrue("OK:"+r,r.equals("[(-1)]OR[[[2]OR[3]]AND[(-4)]]"));
	}	

	public void testNot4() {
		Nodo v1=new Nodo("v1");
		Nodo v2=new Nodo("v2");
		Nodo n=Nodo.NodoAnd(v1,v2);
		//String w1=n.toString();
		n.switchNot(); //	
		assertTrue("ok:" , !n.isNotSuOperatori());
	}	
	
	public void testop4() throws Exception {
		String r = EvalAlbero.creaAlbero("!(1*2+3)").toString();
		//-a+-(-b*-c)*-d= -a+(b+c)*-d=-a+b*-d+c*-d
		assertTrue("OK:"+r,r.equals("[[(-1)]OR[(-2)]]AND[(-3)]"));
	}		
	
	public void testSOP1() {
		Nodo v1=new Nodo("v1");
		Nodo v2=new Nodo("v2");
		Nodo n=Nodo.NodoAnd(v1,v2);
		//String w1=n.toString();
		assertTrue("ok1:" , n.isSOP());
		n.switchNot(); 	
		assertTrue("ok2:" , n.isSOP());
	}	
	
	public void testSOP2() throws Exception {
		Nodo r = EvalAlbero.creaAlbero("1*2+3*4");
		//-a+-(-b*-c)*-d= -a+(b+c)*-d=-a+b*-d+c*-d
		assertTrue("OK:",r.isSOP());
	}		
	
	public void testSOP3()throws Exception {
		Nodo r = EvalAlbero.creaAlbero("1*2+3*4*(5+6)");
		assertTrue("OK:",!r.isSOP());
	}	
	
	public void testSOP4()throws Exception {
		Nodo r = EvalAlbero.creaAlbero("1");
		assertTrue("OK:",r.isSOP());
	}	
	
	public void testand1()throws Exception {
		Nodo r = EvalAlbero.creaAlbero("1*2*3*4*(5*6)");
		assertTrue("OK:",r.isSubTreeSoloAnd());
	}	
	
	public void testand2()throws Exception {
		Nodo r = EvalAlbero.creaAlbero("1*2*3+4*(5*6)");
		assertTrue("OK:",!r.isSubTreeSoloAnd());
	}	
	public void testand3() throws Exception{
		Nodo r = EvalAlbero.creaAlbero("1");
		assertTrue("OK:",r.isSubTreeSoloAnd());
	}
	
	public void testnodeClone() throws Exception{
		Nodo r = EvalAlbero.creaAlbero("1+3*5+(1+2+4*(4*5))");
		Nodo r2= r.NodeClone();
		System.out.println(r);
		System.out.println(r2);
		assertTrue("OK:",r.toString().equals(r2.toString()));
	}	
	public void testsw1()throws Exception {
		Nodo r = EvalAlbero.creaAlbero("1");
		String i=r.toString();	
		r.switchToSOP();
		System.out.println("\nprima:"+i+" \ndopo:"+r.toString());			
		assertTrue("OK:",r.isSOP());
	}	
	public void testsw2()throws Exception {
		Nodo r = EvalAlbero.creaAlbero("1*2");
		String i=r.toString();	
		r.switchToSOP();
		System.out.println("\nprima:"+i+" \ndopo:"+r.toString());
		assertTrue("OK:",r.isSOP());
	}	
	public void testsw3()throws Exception {
		Nodo r = EvalAlbero.creaAlbero("1*2+3*4");
		String i=r.toString();	
		r.switchToSOP();
		System.out.println("\nprima:"+i+" \ndopo:"+r.toString());
		assertTrue("OK:",r.isSOP());
	}	
	public void testsw4() throws Exception{
		Nodo r = EvalAlbero.creaAlbero("1*2+4*(5+6)");
		String i=r.toString();	
		r.switchToSOP();
		System.out.println("\nprima:"+i+" \ndopo:"+r.toString());
		assertTrue("OK:",r.isSOP());
	}	
	
	public void testsw5() throws Exception{
		Nodo r = EvalAlbero.creaAlbero("(1l+2r)*3z+4k");
		String i=r.toString();	
		r.switchToSOP();
		System.out.println("\nprima:"+i+" \ndopo:"+r.toString());
		assertTrue("OK:",r.isSOP());
	}
	public void testsw6()throws Exception {
		Nodo r = EvalAlbero.creaAlbero("4k+3z*(1l+2r)");
		String i=r.toString();	
		r.switchToSOP();
		System.out.println("\nprima:"+i+" \ndopo:"+r.toString());
		assertTrue("OK:",r.isSOP());
	}	
	public void testsw7() throws Exception{
		Nodo r = EvalAlbero.creaAlbero("!(1*2+3)*4");
		String i=r.toString();	
		r.switchToSOP();
		System.out.println("\nprima:"+i+" \ndopo:"+r.toString());
		assertTrue("OK:",r.isSOP());
	}
	public void testsw8() throws Exception{
		Nodo r = EvalAlbero.creaAlbero("4*!(3+1*2)");
		String i=r.toString();	
		r.switchToSOP();
		System.out.println("\nprima:"+i+" \ndopo:"+r.toString());
		assertTrue("OK:",r.isSOP());
	}	
	public void testsw9()throws Exception {
		Nodo r = EvalAlbero.creaAlbero("autore=trampus*!(pippo+pluto*2paperino)");
		String i=r.toString();	
		r.switchToSOP();
		System.out.println("\nprima:"+i+" \ndopo:"+r.toString());
		assertTrue("OK:",r.isSOP());
	}

	//mancante )
	public void teste1() {
		try {
			@SuppressWarnings("unused") Nodo r = EvalAlbero.creaAlberoJopac("autore=trampus&!(pippo|pluto&2=paperino");			
			assertTrue("ko:",false);
		} catch (ExpressionException e) {
			System.out.println(e.getMessage());
			assertTrue("ok errore sintassi "+e.getMessage(),true);
		}		
	}

	//errore manca ))
	public void teste2() {
		try {
			@SuppressWarnings("unused") Nodo r = EvalAlbero.creaAlberoJopac("(autore=trampus&!(pippo|pluto&2=paperino");
			assertTrue("ko:",false);
		} catch (ExpressionException e) {
			System.out.println(e.getMessage());
			assertTrue("ok errore sintassi "+e.getMessage(),true);
		}		
	}	

	//errore &)
	public void teste3() {
		try {
			@SuppressWarnings("unused") Nodo r = EvalAlbero.creaAlberoJopac("(autore=trampus&)!pippo|pluto&2=paperino");
			assertTrue("ko:",false);
		} catch (ExpressionException e) {
			System.out.println(e.getMessage());
			assertTrue("ok errore sintassi "+e.getMessage(),true);
		}		
	}		
	
	//errore parentesi )) chiusa
	public void teste4() {
		try {
			Nodo r = EvalAlbero.creaAlberoJopac("(autore=trampus))&!pippo|pluto&2=paperino");
			System.out.println(r.toString());
			System.out.println(DisegnaAlbero.stampaAlbero(r));				
			assertTrue("ko:",false);
		} catch (ExpressionException e) {
			System.out.println(e.getMessage());
			assertTrue("ok errore sintassi "+e.getMessage(),true);
		}		
	}		
	
	//errore doppio &&
	public void teste5() {
		try {
			Nodo r = EvalAlbero.creaAlberoJopac("autore=trampus&&(pippo|pluto&2=paperino)");
			System.out.println(r.toString());
			System.out.println(DisegnaAlbero.stampaAlbero(r));			
			assertTrue("ko:"+r.toString(),false);
		} catch (ExpressionException e) {
			System.out.println(e.getMessage());
			assertTrue("ok errore sintassi "+e.getMessage(),true);
		}		
	}	
	
	//errore doppio fattore seguito da fattore senza operatore
	public void teste6() {
		try {
			Nodo r = EvalAlbero.creaAlberoJopac("autore=trampus(pippo|pluto&2=paperino)");
			System.out.println(r.toString());
			System.out.println(DisegnaAlbero.stampaAlbero(r));			
			assertTrue("ko:"+r.toString(),false);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertTrue("ok errore sintassi "+e.getMessage(),true);
		}
	}
	//TODO: inserire un test significativo sul nodeclone eliminato 
}
