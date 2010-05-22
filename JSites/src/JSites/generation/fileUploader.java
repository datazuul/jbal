package JSites.generation;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2009 JOpac2 project
*
*     This file is part of JOpac2. http://www.jopac2.org
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
*  Please, see NOTICE.txt AND LEGAL directory for more info. Different licences
*  may apply for components included in JOpac2.
*
*******************************************************************************/

/*
 * @author	Romano Trampus
 * @version 19/05/2005
 */

import java.io.*;

import java.io.IOException;
import java.sql.SQLException;

import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;

import org.apache.cocoon.*;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;

import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentException;
//import org.apache.avalon.excalibur.datasource.DataSourceComponent;

import org.apache.cocoon.servlet.multipart.Part;

import JSites.components.*;

public class fileUploader extends myAbsGenerator {
    
    private FileUploadManager upload_manager;
    private Part part;
    
    
    public void generate() throws SAXException, IOException, ProcessingException {
    	
    	Request request = ObjectModelHelper.getRequest(objectModel);
    	ObjectModelHelper.getContext(objectModel).getAttributeNames().nextElement();
        part = (Part) request.get("upload-file");
        //String confdir=this.getResource("/WEB-INF/conf");
        
        contentHandler.startDocument();
        contentHandler.startElement("","root","root",new AttributesImpl());
        	
        InputStream in = null;
        FileOutputStream out = null;
        
        try {
			//upload_manager.upload(part);
			
        	String dir = ObjectModelHelper.getContext(objectModel).getRealPath("images/contentimg");
        	
			String path = dir + "/"+ part.getFileName();
			File destination = new File(path);
    		byte[] readchar = new byte[20480];
    		in = part.getInputStream();
			out = new FileOutputStream(destination);
			
    		int c = 0;
    		while ((c = in.read(readchar)) != -1) {
    			out.write(readchar, 0, c);
    		}

			sendElement("status","ok");
	        sendElement("filename",part.getFileName());
	        sendElement("mimetype",part.getMimeType());
	        sendElement("uploadname",part.getUploadName());
	        sendElement("size",String.valueOf(part.getSize()));
	        sendElement("uploadfolder",upload_manager.getUploadFolder());
			
		} catch (Exception e) {
			e.printStackTrace();
			String message="error";
			contentHandler.startElement("","status","status",new AttributesImpl());
	        contentHandler.characters(message.toCharArray(), 0, message.length());
	        contentHandler.endElement("","status","status");
		}
		finally {
			in.close();
			out.close();
			contentHandler.endElement("","root","root");
	        contentHandler.endDocument();
		}
    }
    
    public void compose(ComponentManager manager) throws ComponentException {
    	super.compose(manager);
        upload_manager = (FileUploadManager) manager.lookup(FileUploadManager.ROLE);
    }
    


	protected void subClassProcess(String componentType, long id, org.apache.cocoon.xml.AttributesImpl attrCid, boolean hasChildren) throws SAXException, SQLException {
		// TODO Auto-generated method stub
		
	}
}
