/*
 * Created on 12-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jopac2.engine.Managers;
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
* @version	12/05/2005
*/
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import org.jopac2.engine.utils.SingleSearch;
import org.jopac2.utils.RecordItem;


/**
 * @author Iztok
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractManager extends Thread implements ReqManager {
	
	private long start = 0;
	private long end = 0;
	private long timeout = 0;
	private boolean stopped = false;
	
	protected SingleSearch ss = null;
	protected String q = "";
	protected String syntax = "";
	
	protected String contextDir=null;
	
	public void setInfo(SingleSearch ssearch, String query, String syntax_type, long TimeOut){
		ss = ssearch; 
		q = query;
		syntax = syntax_type;
		timeout = TimeOut;
	}
	

	
	public void run(){
		try {
			start = (new Date()).getTime();
			sendquery();
			end = (new Date()).getTime();
			System.out.println("=== "+ss.getHost()+" has ended");
			setStopped(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public long getRunningTime(){
		if(end>0) return end - start;
		else return 0;
	}
	
	public long getTimeOut(){
		return timeout;
	}

	public synchronized void setStopped(boolean s) {
		stopped = s;
	}
	
	public synchronized boolean isStopped(){
		return stopped;
	}


	/**
	 * @return Returns the contextDir.
	 */
	public String getContextDir() {
		return contextDir;
	}

	/**
	 * @param contextDir The contextDir to set.
	 */
	public void setContextDir(String contextDir) {
		this.contextDir = contextDir;
	}
	
	public abstract void destroy();
	
	public abstract Vector<RecordItem> getRecords();
	
	public abstract int getRecordCount();
}
