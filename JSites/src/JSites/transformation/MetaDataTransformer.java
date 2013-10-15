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
import java.util.*;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import JSites.transformation.catalogSearch.Templator;
import JSites.utils.XMLUtil;

public class MetaDataTransformer extends MyAbstractPageTransformer {
	
	public static int index = 0;
	
	private Document metadata=null;
	

	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException {
		//super.setup(arg0, arg1, arg2, arg3);
		 request = (Request)arg1.get("request");
		//System.out.println("Setting up");

//		 Request request = (Request)(arg1.get("request"));
		 
		 if(debug) printRequestData(arg1,arg3);
		 
		 if(request.getAttribute("metadata")!=null) {
			 metadata=(Document)request.getAttribute("metadata");
//			 System.out.println(XMLUtil.XML2String(metadata));
		 }

	}
	
	public void startDocument() throws SAXException{
		index++;
		
		super.startDocument();
		
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException
	{
//		print(localName, attributes);
		
		try{
			super.startElement(namespaceURI, localName, qName, attributes);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(namespaceURI);
			System.out.println(localName);
			System.out.println(qName);
			System.out.println(attributes);
		}
		
		
	}

	public void endDocument() throws SAXException{
		super.endDocument();
		
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
		super.endElement(namespaceURI, localName, qName);
		
    }
	
	public void characters(char[] a, int s, int e) throws SAXException{

		String ds=new String(a,s,e);
		if(metadata!=null && ds.contains("/metadata")) {
			try {
				ds = Templator.parseContext(metadata, "{{/:"+ds+"}}",false);
				super.characters(ds.toCharArray(), 0, ds.length());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			super.characters(a,s,e);
		}
	}
	
	private void print(String localName, Attributes attributes) {
		System.out.println(localName);
		for(int i=0;i<attributes.getLength();i++){
			System.out.println(attributes.getValue(i));
		}
	}

}
