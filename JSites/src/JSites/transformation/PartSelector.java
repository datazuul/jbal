package JSites.transformation;

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

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import JSites.utils.DBGateway;

import JSites.authentication.Authentication;
import JSites.authentication.Permission;

public class PartSelector extends MyAbstractPageTransformer {
	
	private Permission permission;
	private long cid;
	private boolean write = true; 
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		super.setup(arg0, arg1, arg2, arg3); 
		write = true;   
		try{
			 long pageId=1;
			 String stringPid = request.getParameter("pid");
			 if(stringPid != null)
				 pageId = Long.parseLong(stringPid);
			 String stringCid = request.getParameter("cid");
			 if(stringCid != null)
				 cid = Long.parseLong(stringCid);
			 
			 try{
				 permission = Authentication.assignPermissions(datasourceComponent,session, request.getRemoteAddr(), pageId);
			 }catch(Exception e) {e.printStackTrace();}
			 
		 }
		 catch(Exception e){e.printStackTrace();}
		 
		 if(permission==null)permission=new Permission();
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException
	{
		try{
			if(DBGateway.getState(datasourceComponent, cid) == 4){
				if(localName.equals("titolo"))
					super.startElement(namespaceURI, localName, qName, attributes);
				else if(localName.equals("section"))
					super.startElement(namespaceURI, "oldsection", "oldsection", attributes);
				else
					write=false;
			}
			else 
				super.startElement(namespaceURI, localName, qName, attributes);

		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
		try{
			if(DBGateway.getState(datasourceComponent, cid) == 4){
				if(localName.equals("titolo"))
					super.endElement(namespaceURI, localName, qName);
				else
					write=true;
			}
			else 
				super.endElement(namespaceURI, localName, qName);
		}
		catch(Exception e){e.printStackTrace();}
    }
	
	public void characters(char[] a, int s, int e) throws SAXException{
		if(write)
		super.characters(a,s,e);
	}
	
	
	
}
