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

import org.jopac2.engine.utils.SingleSearch;
import org.jopac2.utils.RecordItem;


/*
 *interfaccia che definisce un costruttore di richieste 
 */
public interface ReqManager {
    
    //protected String construct(IRQuery q);
	
	public void setInfo(SingleSearch ssearch, String query, String syntax_type, long TimeOut);
    
    public void sendquery() throws IOException; // TimeoutExceededException
    
    public Vector<RecordItem> getRecords();
    
    public int getCurrentStatus();
    
    public int getRecordCount();
}