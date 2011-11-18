package org.jopac2.engine.utils;
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
* @author	Albert Caramia
* @version	19/01/2005
* 
*/
/**
 * @author Watashi
 */

import java.lang.reflect.Array;

public class MyTimer {

	private Object timer; // memorizza i currenttime
	private Object nomi; //memorizza i nomi dei timer
	private Object commenti;
	private int ntimer=0; // numero dei timers
	private int currtimer=0;
	/**
	 * inizializza il timer
	 * @param NomiTimer 
	 * l'elemento 0 e' il nome del timer
	 * gli altri sono i nomi
	 * viene aggiunto l'ultimo nome di stop
	 */
	public MyTimer(String[] NomiTimer){
		
		ntimer=Array.getLength(NomiTimer);
		currtimer=-1;
		try {
			nomi=Array.newInstance(String.class,ntimer+1);
			commenti=Array.newInstance(String.class,ntimer+1);
			for(int i=0;i<ntimer;i++) {
				Array.set(nomi,i,NomiTimer[i]);
				Array.set(commenti,i,null);
			}
			Array.set(nomi,ntimer,"stop");
			Array.set(commenti,ntimer,null);
			timer=Array.newInstance(Long.TYPE,ntimer+1);						
		} catch (NegativeArraySizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Start(){
		currtimer=-1;
		SaveTimer();
	}
	
	/**
	 * ferma il timer
	 * TODO riempire le misure intermedie se non sono state caricate
	 */
	public void Stop(){	
		//System.out.println(currtimer+" "+ntimer);	
	    currtimer=ntimer-1;		
		SaveTimer();		
	}
	/**
	 * prende una misura intermedia
	 * verificare che non si superi il limite del timer!
	 * TODO aggiungere il nome come commento per salvare info nel time
	 */
	public void SaveTimer(){
		Array.setLong(timer,++currtimer,System.currentTimeMillis());
	}
	public void SaveTimer(String Commento){
		SaveTimer();
		Array.set(commenti,currtimer,Commento);
	}	
	
	public String toString(){
		System.out.print(Array.get(nomi,0)+":");
		for(int i=1;i<=ntimer;i++){
			System.out.print(Array.get(nomi,i)+"["+(Array.getLong(timer,i)-Array.getLong(timer,i-1))+"ms]");
			if(Array.get(commenti,i)!=null)
				System.out.print("("+Array.get(commenti,i)+")");
			System.out.print("\t");
		}
		System.out.println("total time:"+(Array.getLong(timer,ntimer)-Array.getLong(timer,0)));
		return "";
	}
	
	public static void main(String[] args) throws Exception {
		MyTimer t = new MyTimer(new String[] {"Timer prova","t1","t2","t3"});
		t.Start();
		Thread.sleep(121);		
		t.SaveTimer("si tratta di t1");  
		Thread.sleep(213);
		t.SaveTimer();  
		t.SaveTimer("t3 tempo 0");  
		Thread.sleep(333);
		t.Stop();
		System.out.println(t);
	}
}
