package JSites.generation.metaSearch;

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
* @author	Iztok Cergol
* @version  ??/??/2004
* 
* @author   Romano Trampus
* @version	19/05/2005
*/

import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;

import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.*;

import org.apache.cocoon.*;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.TimeStampValidity;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.Composable;
import org.jopac2.engine.metasearch.managers.AbstractManager;
import org.jopac2.engine.utils.SingleSearch;
import org.jopac2.utils.RecordItem;

import JSites.generation.MyAbstractPageGenerator;



/**
 * Avvia la meta ricerca. Per ciascuna origine viene creato un thread. Il risultato di questo
 * genertore non sono i record ma la lista dei server/thread creati. Ciascun thread viene salvato
 * nella sessione per il successivo recupero.
 * @author romano
 */
public class MetaSearchResults extends MyAbstractPageGenerator implements Composable, CacheableProcessingComponent {
    
    //private StaticDataComponent staticdata;
    

	@SuppressWarnings("unchecked")
	private static SingleSearch getSingleSearch(Session session, String host, String database) {
		Vector<SingleSearch> old = (Vector<SingleSearch>)session.getAttribute("metasearch");

		SingleSearch am=null;
        if(old!=null && host!=null) {
        	for(int i=0;i<old.size();i++) {
        		String thisHost=old.elementAt(i).getHost();
        		String thisDatabase=old.elementAt(i).getPrefix();
        		if(thisHost.equals(host) && 
        				(database==null || (database.equals(thisDatabase)))) {
        			am=old.elementAt(i);
        		}
        	}
        }
        return am;
	}

    public void generate() throws SAXException {
    	
    	Request request = ObjectModelHelper.getRequest(objectModel);
    	
    	String host=request.getParameter("host");
    	String database=request.getParameter("database");


    	
        contentHandler.startDocument();
        contentHandler.startElement("","root","root",new AttributesImpl());
        
        SingleSearch ss=getSingleSearch(request.getSession(),host,database);
	    if(ss!=null) {        
            contentHandler.startElement("","resultset","resultset",new AttributesImpl());
            AbstractManager am=ss.getManager();
            if(am!=null) {
            	Vector<RecordItem> result=am.getRecords();
            	for(int i=0;result!=null&&i<result.size();i++) {
            		contentHandler.startElement("","record","record",new AttributesImpl());
            		sendElement("database",result.elementAt(i).getDatabase());
            		sendElement("databasecode",ss.getDatabasecode());
            		sendElement("datasyntax",result.elementAt(i).getDataSyntax());
            		sendElement("datatype",result.elementAt(i).getDataType());
            		sendElement("host",result.elementAt(i).getHost());
            		sendElement("id",result.elementAt(i).getId());
            		sendElement("data",result.elementAt(i).getData());
                    contentHandler.endElement("","record","record");
            	}
            }
            contentHandler.endElement("","resultset","resultset");
        }
        else {
        	sendElement("error-result-selector","no query");
        }
        contentHandler.endElement("","root","root");
        contentHandler.endDocument();
    }
   
    /* 
     * compose viene rifatto invece di usare quello in MyAbsGenerator
     * perche' non c'e' bisogno del datasource.
     */
    public void compose(ComponentManager manager) throws ComponentException {
            //staticdata = (StaticDataComponent) manager.lookup(StaticDataComponent.ROLE);
            //staticdata.init();
    }
    
    public Serializable getKey() {
    	return null;
    }


    @Override
	public SourceValidity getValidity() {
		long t=System.currentTimeMillis()/1000000;
		System.out.println("getValidity: "+t);
		if(t>0) {
			return new TimeStampValidity(t);
		}
		else {
			return null;
		}
	}
}
