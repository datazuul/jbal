package org.jopac2.engine.parserricerche.parser.test;
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
import junit.framework.*;

import java.util.Vector;

import org.jopac2.engine.parserricerche.parser.booleano.EvalAlbero;
import org.jopac2.engine.parserricerche.tree.Nodo;
import org.jopac2.engine.parserricerche.tree.stampa.DisegnaAlbero;

public class TestDisegnaAlbero extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void test()throws Exception {
		Nodo tree=EvalAlbero.creaAlbero("!(!5*(1+(2*3)+4))");
		tree.switchToSOP();
		Vector<String[]> v=DisegnaAlbero.getAlbero(tree);
		System.out.println(v);
	}
	
}
