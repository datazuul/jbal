package org.jopac2.engine.MetaSearch.Managers;
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
* @author	Iztok Cergol
* @version	19/08/2004
*/
import java.io.IOException;
import java.util.Vector;

import org.jopac2.engine.Z3950.InternalClient;
import org.jopac2.utils.RecordItem;

import com.k_int.IR.SearchTask;
import com.k_int.IR.TimeoutExceededException;


/*
 *implementazione di GetConstructor per JOpac2
 *si tratta di un elaboratore di stringhe
 */
public class JOpac2Z3950RequestManager extends AbstractManager
{	
	private SearchTask searchtask=null;
	private InternalClient client=null;
	
	
    public void sendquery() throws IOException // TimeoutExceededException, 
    {	
    	try {
    		client=new InternalClient();
    		searchtask=client.getJOpac2SearchData(ss, q, syntax);
    		client.getRecords(ss,searchtask); // questo crea il vettore e fa il fetch nel thread
			//records = SBAInternalClient.getRecords(sd);
		} catch (TimeoutExceededException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public Vector<RecordItem> getRecords() {
		return client.getRecords(); // questo prende il puntatore ai record, non fa il fetch
	}

	public int getCurrentStatus() {
		return client.getStatus();
	}
	
	public int getRecordCount() {
		return client.getNFragments();
	}

	public void destroy() {
		client.stop();
		client.destroy();
		
	}
    
	
    
}
