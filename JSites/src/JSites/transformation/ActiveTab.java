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
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import JSites.utils.DBGateway;


public class ActiveTab extends MyAbstractPageTransformer {

	private long activePid;
	
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		super.setup(arg0, arg1, arg2, arg3);

		activePid = Long.parseLong(request.getParameter("pid"));

		Connection conn = null;
		try{
			conn = getConnection(dbname);
			long pLevel = DBGateway.getPageLevel(activePid,0,conn);
			
			while(pLevel>1){
				activePid = DBGateway.getPapid(activePid,conn);	
				pLevel = DBGateway.getPageLevel(activePid,0,conn);
			}
		}catch(Exception e) {e.printStackTrace();}
		
		try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}

	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException
	{
		AttributesImpl newAttrs = new AttributesImpl();
		for(int i=0;i<attributes.getLength();i++){
			String name = attributes.getQName(i);
		    String value = attributes.getValue(i);
		    if(!(name.equals("class")))
		    	newAttrs.addCDATAAttribute(name,value);
		}
		String href = attributes.getValue("href");
		if(href != null && href.contains("pid="+activePid))
			newAttrs.addCDATAAttribute("class","selected");
		else if(attributes.getValue("class")!=null)
			newAttrs.addCDATAAttribute("class",attributes.getValue("class"));

		super.startElement(namespaceURI, localName, qName, newAttrs);
	}
	
	
}
