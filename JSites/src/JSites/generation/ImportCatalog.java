package JSites.generation;

/*******************************************************************************
 *
 *  JOpac2 (C) 2002-2005 JOpac2 project
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
 * @version 19/05/2005
 */

import java.io.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;

import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentException;

import org.apache.cocoon.servlet.multipart.Part;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.importers.DataImporter;
import com.whirlycott.cache.Cache;
import com.whirlycott.cache.CacheConfiguration;
import com.whirlycott.cache.CacheManager;


public class ImportCatalog extends MyAbstractPageGenerator {
    
    private Part part;
	private int max_conn=5;
    
    public void generate()  {
    	Request request = ObjectModelHelper.getRequest(objectModel);
    	ObjectModelHelper.getContext(objectModel).getAttributeNames().nextElement();
        part = (Part) request.get("upload-file");
        String pid=request.getParameter("pid");
        String cid=request.getParameter("cid");
        String catalog=request.getParameter("conn");
        String format=request.getParameter("format");
        String dbtype=request.getParameter("dbtype");
        
        try {
			contentHandler.startDocument();
			contentHandler.startElement("","root","root",new AttributesImpl());
		} catch (SAXException e2) {
			e2.printStackTrace();
		}
        if(pid==null || cid==null) {
        	try {
				sendElement("error","pid or cid missing");
			} catch (SAXException e) {
				e.printStackTrace();
			}
        }
        else if(part!=null && pid!=null && cid!=null && catalog!=null && format!=null && dbtype!=null) {
        	
	        
	        try {
	        	contentHandler.startElement("","loading","loading",new AttributesImpl());
		        InputStream in = null;
	        	String dir = this.source;
	        	
	    		in = part.getInputStream();
	    		
	    		new File(dir).mkdirs();
	    		
	    		String path = dir + "/"+ part.getFileName();
				File destination = new File(path);
	    		byte[] readchar = new byte[20480];
	    		in = part.getInputStream();
	    		FileOutputStream out = new FileOutputStream(destination);
				
	    		int c = 0;
	    		while ((c = in.read(readchar)) != -1) {
	    			out.write(readchar, 0, c);
	    		}
	    		out.close();
	    		in.close();
	    		
	    		in=new FileInputStream(destination);
	    		contentHandler.startElement("","load","load",new AttributesImpl());
				sendElement("status","ok");
				sendElement("dir",dir);
		        sendElement("filename",part.getFileName());
		        sendElement("mimetype",part.getMimeType());
		        sendElement("uploadname",part.getUploadName());
		        sendElement("size",String.valueOf(part.getSize()));
		        sendElement("cid",cid);
	        	sendElement("pid",pid);
	        	sendElement("catalog",catalog);
	        	sendElement("format",format);
	        	sendElement("dbtype",dbtype);
		        contentHandler.endElement("","load","load");

		        
				String JOpac2confdir=getResource("/")+"WEB-INF/conf/engine/";
				
				boolean background=true;

				Cache cache=null;
				
				try {
					CacheConfiguration cacheConf=new CacheConfiguration();
					cacheConf.setTunerSleepTime(10);
					cacheConf.setBackend("com.whirlycott.cache.impl.ConcurrentHashMapImpl");
					cacheConf.setMaxSize(100000);
					cacheConf.setPolicy("com.whirlycott.cache.policy.LFUMaintenancePolicy");
					cacheConf.setName("JOpac2cache");
					cache= CacheManager.getInstance().createCache(cacheConf);
					//cache = CacheManager.getInstance().getCache();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//Transliterator t=Transliterator.getInstance("NFD; [:Nonspacing Mark:] Remove; NFC");
				
				PrintStream console=System.out;
				try {
					console=new PrintStream(dir+"/status"+cid);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				Connection[] conns=new Connection[max_conn];
				
				Connection tc=this.getConnection(dbname);
				
				DbGateway dbg=DbGateway.getInstance(tc.toString(), console);
				String dbUrl=tc.getMetaData().getURL();

				for(int i=0;i<conns.length;i++) {
					conns[i] = dbg.createConnection(dbUrl, "root", "");// this.getConnection(dbname);
				}
				
				DataImporter dataimporter=new DataImporter(in,format,JOpac2confdir, conns, catalog, true,cache, console); //,t);
				if(background) {
					dataimporter.start();
				}
				else {
					dataimporter.doJob();
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				String message="error";
				try {
					contentHandler.startElement("","status","status",new AttributesImpl());
					contentHandler.characters(message.toCharArray(), 0, message.length());
					contentHandler.endElement("","status","status");
				} catch (SAXException e1) {
					e1.printStackTrace();
				}
			}
			try {
				contentHandler.endElement("","loading","loading");
			} catch (SAXException e) {
				e.printStackTrace();
			}
        }
        else {
        	// generate form
        	try {
        		contentHandler.startElement("","form","form",new AttributesImpl());
            	sendElement("cid",cid);
            	sendElement("pid",pid);
            	sendElement("catalog",catalog);
				sendElement("format",format);
			} catch (SAXException e) {
				e.printStackTrace();
			}
        	try {
				sendElement("dbtype",dbtype);
				contentHandler.endElement("","form","form");
			} catch (SAXException e) {
				e.printStackTrace();
			}
        }
		try {
			contentHandler.endElement("","root","root");
			contentHandler.endDocument();
		} catch (SAXException e) {
			e.printStackTrace();
		}
    }
    
    
    public void compose(ComponentManager manager) throws ComponentException {
    	super.compose(manager);
    }
    


	protected void subClassProcess(String componentType, long id, org.apache.cocoon.xml.AttributesImpl attrCid, boolean hasChildren) throws SAXException, SQLException {
		// TODO Auto-generated method stub
		
	}
}
