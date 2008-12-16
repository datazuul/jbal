package org.jopac2.engine.dbGateway;
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

/*
* @author	Romano Trampus
* @version	10/12/2004
* 
* @author   Romano Trampus
* @version	19/05/2005
*/

import org.jopac2.engine.MetaSearch.DoNewMetaSearch;
import org.jopac2.engine.Z3950.SBAInternalClient;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.utils.StopWordRecognizer;


//import org.apache.cocoon.environment.*;
import java.util.*;
import java.io.File;
import java.sql.*;
//import javax.servlet.ServletConfig;

//import javax.servlet.ServletConfig;

/**
 * TODO questa classe è da rivedere. O si fa singleton oppure deve prendere una connection e risolvere sul db.
 * Forse la prima per performance standarizzando i nomi / id delle classi.
 * Si potrebbe anche abolire la tabella classi e tenere solo i codici?
 */


public final class StaticDataComponent { //, Contextualizable, ThreadSafe {
   private Hashtable<String,Long> tipi;
   private SBAInternalClient staticclient = null;
   //private NewMetaSearch dms;
   private StopWordRecognizer stopword;
   private String path;
   
   
   // ANY ripetuto due volte per mettere in posizione 0 qualche cosa e mantenere compatibilita' con i db esistenti
   private static String[] channels={"ANY","AUT","TIT","NUM","LAN","MAT","DTE","SBJ","BIB","INV","CLL","ANY","JID","ABS"};
   
   public StopWordRecognizer getStopWordRecognizer() {
   		return stopword;
   }
   
   public Hashtable<String,Long> getTipi() {
       return tipi;
   }
   
   public SBAInternalClient getStaticClient(){
   		return staticclient;
   }

  
  /**
   * aggiunta a fini di test fuori dal contesto di coocon
   * @param c
   * @param path
   */
  public void init(String path) {
	  this.path=path;
	  init();
  }
  
   public void init() {
	    staticclient = new SBAInternalClient(); 
            
	    stopword=new StopWordRecognizer();
	    stopword.LoadList(new File(path+"stopwords.lst"));
   }

	public static String getChannelNamebyIndex(String classe) {
		return channels[Integer.parseInt(classe)];
	}

	public static long getChannelIndexbyName(String channelName) {
		int r=-1;
		for(int i=0;i<channels.length;i++) {
			if(channelName.equals(channels[i])) {
				r=i;
				break;
			}
		}
		return r;
	}
}
