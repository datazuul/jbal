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
import org.jopac2.engine.utils.SingleSearch;

import JSites.generation.MyAbstractPageGenerator;

/**
 * Avvia la meta ricerca. Per ciascuna origine viene creato un thread. Il risultato di questo
 * genertore non sono i record ma la lista dei server/thread creati. Ciascun thread viene salvato
 * nella sessione per il successivo recupero.
 * @author romano
 */
public class ActiveMetaSearch extends MyAbstractPageGenerator implements Composable, CacheableProcessingComponent {    

    @SuppressWarnings("unchecked")
	public void generate() throws SAXException {
    	
    	Request request = ObjectModelHelper.getRequest(objectModel);
    	
        contentHandler.startDocument();
        contentHandler.startElement("","root","root",new AttributesImpl());
        
        
        Session session=request.getSession();
        
        Vector<SingleSearch> hosts=(Vector<SingleSearch>)session.getAttribute("metasearch");
		
        if(hosts!=null && hosts.size()>0) {
        	
            contentHandler.startElement("","hosts","hosts",new AttributesImpl());
            for(int i=0;i<hosts.size();i++) {
            	contentHandler.startElement("","server","server",new AttributesImpl());
	            sendElement("syntax",hosts.elementAt(i).getSyntax());
	            sendElement("host",hosts.elementAt(i).getHost());
	            if(hosts.elementAt(i).getPrefix().startsWith("/"))
	            	sendElement("database",hosts.elementAt(i).getPrefix());
	            else
	            	sendElement("database",hosts.elementAt(i).getHost());
	            sendElement("databasecode",hosts.elementAt(i).getDatabasecode());
	            sendElement("currentstatus",Integer.toString(hosts.elementAt(i).getManager().getCurrentStatus()));
	            
	            if(hosts.elementAt(i).getManager().getRecords()==null) {
	            	sendElement("status","...");
	            }
	            else {
	            	sendElement("fetched",Integer.toString(hosts.elementAt(i).getManager().getRecords().size()));
	            	sendElement("status","end");
	            	sendElement("time",Long.toString(hosts.elementAt(i).getManager().getRunningTime()));
	            }
	            sendElement("nrecord",Integer.toString(hosts.elementAt(i).getManager().getRecordCount()));
	            
	            contentHandler.endElement("","server","server");
            }
            contentHandler.endElement("","hosts","hosts");
        }
        else {
        	sendElement("error","no query");
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
