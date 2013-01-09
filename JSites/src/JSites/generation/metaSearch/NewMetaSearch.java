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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Vector;

import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.*;

import org.apache.cocoon.*;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.Cookie;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.Session;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.TimeStampValidity;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.Composable;
import org.jopac2.engine.metasearch.DoNewMetaSearch;
import org.jopac2.engine.utils.SingleSearch;

import JSites.generation.MyAbstractPageGenerator;
import JSites.utils.Util;


/**
 * Avvia la meta ricerca. Per ciascuna origine viene creato un thread. Il risultato di questo
 * genertore non sono i record ma la lista dei server/thread creati. Ciascun thread viene salvato
 * nella sessione per il successivo recupero.
 * @author romano
 */
public class NewMetaSearch extends MyAbstractPageGenerator implements Composable, CacheableProcessingComponent {
        
	@SuppressWarnings("unchecked")
	private static void invalidateMetaSearch(Session session) {
		Vector<SingleSearch> old=(Vector<SingleSearch>)session.getAttribute("metasearch");
        if(old!=null) {
        	for(int i=0;i<old.size();i++) {
        		old.elementAt(i).destroy();
        	}
        	old.removeAllElements();
        	old=null;
        }
	}

    public void generate() throws SAXException {
    	
    	Request request = ObjectModelHelper.getRequest(objectModel);
    	String message=Util.getRequestData(request, "query");
    	
    	if(message != null && message.length()>0) {
	    	Response res=(Response)this.objectModel.get(ObjectModelHelper.RESPONSE_OBJECT);
	    	res.addCookie(res.createCookie("lastSearch", message));
    	}
    	else {
    		message=((Cookie)request.getCookieMap().get("lastSearch")).getValue();
    	}
    	
    	try {
			message=URLDecoder.decode(message,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        contentHandler.startDocument();
        contentHandler.startElement("","root","root",new AttributesImpl());
        
        if(message.length()>0) {
        	contentHandler.startElement("","queryData","queryData",new AttributesImpl());
        	sendElement("parsedQuery",message);
        	contentHandler.endElement("","queryData","queryData");
        }
        
        sendElement("requestQuery",message);
        
        if(message.length()>0) {
        	String serverList=source;
        	if(serverList==null) serverList="/WEB-INF/conf/meta/servers.lst";
            
            String contextDir = ObjectModelHelper.getContext(objectModel).getRealPath("/");
//            String confpath = ObjectModelHelper.getContext(objectModel).getRealPath(serverList);        
            String confpath = contextDir+"components/metaSearch/servers.lst";
        	Vector<SingleSearch> hosts=DoNewMetaSearch.executeMetaSearch(message,objectModel,confpath,contextDir);
	        
        	Session session=request.getSession();
            // se ci sono altre ricerche in corso, invalida
            invalidateMetaSearch(session);
            
            // salva tutto nella sessione
            session.setAttribute("metasearch", hosts);
        	
            contentHandler.startElement("","hosts","hosts",new AttributesImpl());
            for(int i=0;i<hosts.size();i++) {
            	contentHandler.startElement("","server","server",new AttributesImpl());
	            sendElement("syntax",hosts.elementAt(i).getSyntax());
	            sendElement("host",hosts.elementAt(i).getHost());
	            sendElement("database",hosts.elementAt(i).getDbname());
	            sendElement("databasecode",hosts.elementAt(i).getDatabasecode());
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
     * compose viene rifatto perche' non c'e' bisogno del datasource.
     */
    public void compose(ComponentManager manager) throws ComponentException {

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
