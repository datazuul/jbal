/*
 * Created on 23-apr-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.jopac2.engine.dbengine.newsearch;
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
import java.util.BitSet;
/**
 * 
 * @author albert
 *
 */
public class BitSetUtils {
	
	public static final int MAX_NOTIZIE = 2177393;
	
	/**
	 * carica il bitarray con un vettore di id_notizie in array di bit
	 * @param VettoreInput
	 */
	public static BitSet ToBit(Vector<Long> longVector) {
		int i;
		long t;
		BitSet bitArray=null;
		if(longVector!=null) {
			bitArray=new BitSet(MAX_NOTIZIE);
			for (i = 0; i < longVector.size(); i++) {
				t = ((Long) longVector.elementAt(i)).longValue();
				bitArray.set((int)t);
			}
		}
		else
			bitArray=new BitSet();			
		return bitArray;
	}
	
	/**
	 * restituisce un vettore con i valori rispettivi dei bit popolati
	 * @return Vettore di long
	 */
	public static Vector<Long> ToVector(BitSet bitArray) {
		Vector<Long> r = new Vector<Long>();		
			for(int i=bitArray.nextSetBit(0); i>=0; i=bitArray.nextSetBit(i+1)) {
				r.add(new Long(i));	//TODO: sistemare r.add conforme java5
			}
		return r;
	}	
}
