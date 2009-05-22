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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;

import org.apache.cocoon.*;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;

import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentException;

import org.apache.cocoon.servlet.multipart.Part;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.importers.DataImporter;
import org.jopac2.utils.Utils;

import com.ibm.icu.text.Transliterator;
import com.whirlycott.cache.Cache;
import com.whirlycott.cache.CacheConfiguration;
import com.whirlycott.cache.CacheManager;


public class ImportCatalog extends myAbsGenerator {
    
    private Part part;
	private int max_conn=5;
    public static String _classDerbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
    
    public void generate() throws SAXException, IOException, ProcessingException {
    	Request request = ObjectModelHelper.getRequest(objectModel);
    	ObjectModelHelper.getContext(objectModel).getAttributeNames().nextElement();
        part = (Part) request.get("upload-file");
        String pid=request.getParameter("pid");
        String cid=request.getParameter("cid");
        String con=request.getParameter("conn");
        String format=request.getParameter("format");
        String dbtype=request.getParameter("dbtype");
        
        contentHandler.startDocument();
        contentHandler.startElement("","root","root",new AttributesImpl());
        if(pid==null || cid==null) {
        	sendElement("error","pid or cid missing");
        }
        else if(part!=null && pid!=null && cid!=null && con!=null && format!=null && dbtype!=null) {
        	contentHandler.startElement("","loading","loading",new AttributesImpl());
	        InputStream in = null;
	        
	        try {				
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
	
				sendElement("status","ok");
		        sendElement("filename",part.getFileName());
		        sendElement("mimetype",part.getMimeType());
		        sendElement("uploadname",part.getUploadName());
		        sendElement("size",String.valueOf(part.getSize()));
		        
		        /**
		         * TODO: rendere ralitivo qs path
		         */
				String JOpac2confdir="/java_jopac2/engine/src/org/jopac2/conf";
				
				boolean background=false;
				Connection[] conns=new Connection[max_conn];
				String driver=_classDerbyDriver;
				String dbUrl = "jdbc:derby:"+dir+con+";create=true";
				
				if(dbtype.equalsIgnoreCase("derby")) {
					Class.forName(driver).newInstance();
					
					for(int i=0;i<conns.length;i++) {
						conns[i] = DriverManager.getConnection(dbUrl, "", "");
					}
				}
				else {
					for(int i=0;i<conns.length;i++) {
						conns[i] = this.getConnection(con);
					}
				}
				
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
				
				DataImporter dataimporter=new DataImporter(in,format,JOpac2confdir, conns, true,cache); //,t);
				if(background) {
					dataimporter.start();
				}
				else {
					dataimporter.doJob();
					if(dbtype.equalsIgnoreCase("derby")) {
						try {
							DriverManager.getConnection("jdbc:derby:"+dir+con+";shutdown=true");
						} catch (Exception e) {
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				String message="error";
				contentHandler.startElement("","status","status",new AttributesImpl());
		        contentHandler.characters(message.toCharArray(), 0, message.length());
		        contentHandler.endElement("","status","status");
			}
			contentHandler.endElement("","loading","loading");
        }
        else {
        	// generate form
        	contentHandler.startElement("","form","form",new AttributesImpl());
        	sendElement("cid",cid);
        	sendElement("pid",pid);
        	sendElement("conn",con);
        	sendElement("format",format);
        	sendElement("dbtype",dbtype);
        	contentHandler.endElement("","form","form");
        }
		contentHandler.endElement("","root","root");
        contentHandler.endDocument();
    }
    
    public void compose(ComponentManager manager) throws ComponentException {
    	super.compose(manager);
    }
    


	protected void subClassProcess(String componentType, long id, org.apache.cocoon.xml.AttributesImpl attrCid, boolean hasChildren) throws SAXException, SQLException {
		// TODO Auto-generated method stub
		
	}
}
