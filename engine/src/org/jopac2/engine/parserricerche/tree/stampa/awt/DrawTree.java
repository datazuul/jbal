package org.jopac2.engine.parserricerche.tree.stampa.awt;
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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Vector;
import javax.swing.*;

import org.jopac2.engine.parserricerche.parser.booleano.EvalAlbero;
import org.jopac2.engine.parserricerche.tree.Nodo;
import org.jopac2.engine.parserricerche.tree.stampa.DisegnaAlbero;

/**
 * 
 * @author albert
 *
 */
public class DrawTree {
	
	public static void main(String[] args)throws Exception {
		Nodo tree=EvalAlbero.creaAlbero("!(!5*(1+(2*3)+4))");
		Nodo tree2=EvalAlbero.creaAlbero("!(!5*(1+(2*3)+4))");
		JFrame f2 = new DrawTreeFrame(tree2);
		f2.setVisible(true);		
		tree.switchToSOP();
		JFrame f = new DrawTreeFrame(tree);
		f.setVisible(true);
	}
}

class DrawTreeFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DrawTreeFrame(Nodo tree) {
		setTitle("Tree");
		setSize(448+9, 235);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(new DrawTreePanel(tree));
	}
}

class DrawTreePanel extends JPanel {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Nodo tree;
	
	public DrawTreePanel(Nodo tree){
		this.tree=tree;
	}
	
	public void paintComponent(Graphics g1) {
		Graphics2D g=(Graphics2D)g1;
		super.paintComponent(g);
		//Rectangle r= g.getDeviceConfiguration().getBounds();
		Rectangle r1=g.getClipBounds();
		Rectangle r= new Rectangle(0,0,r1.width-1,r1.height-1);
		g.draw(r);
		System.out.println(r);
		Vector<String[]> v= DisegnaAlbero.getAlbero(tree);
		
		//determina intero numero massimo foglie ultimo livello
		//double l=Math.pow(2,(double)tree.GetLivelli());
		//long ll=new Double(l).longValue();
		//int li=Integer.parseInt((new Long(ll).toString()));
		int numFoglie=1<<tree.GetLivelli();
		numFoglie*=2; //lascio spazio a dx di ciascuna foglia
		int xMax=r.width;
		int rx=xMax/numFoglie;	
		int ry=rx;  //cerchio	
		
		int cy=ry;	
		for(int i=0;i<v.size();i++){
			String[] gw=(String[])v.elementAt(i);
			int nFoglieLivAttuale=1<<(i+1);
			int spazioPerFoglia=xMax/nFoglieLivAttuale;
			double xo;
			double yo;
			for(int j=0;j<gw.length;j++){
				xo=spazioPerFoglia*(j*2+1);
				//System.out.print(j+" "+xo);
				xo=xo-rx/2;
				yo=cy-ry/2;
				g.drawRect((int)xo,(int)yo,rx,ry);
				g.drawString(gw[j],(int)(xo+rx/2),(int)(yo+ry/2));				
			}
			System.out.println();
			cy+=ry;
			
		}
		//Polygon q = DrawFractal.draw(3);
		//g.drawPolygon(q);
	}
}