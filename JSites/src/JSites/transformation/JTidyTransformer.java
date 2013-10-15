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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.serializers.encoding.XMLEncoder;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import JSites.utils.JTidy;

public class JTidyTransformer extends MyAbstractPageTransformer {
	private StringBuffer sb=new StringBuffer();

	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		 request = (Request)arg1.get("request");
	}
	
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException
	{
		super.startElement(namespaceURI, localName, qName, attributes);
	}

	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
		if(sb!=null && sb.length()>0) {
			String jsb=JTidy.jtidyNoOfficeNamespace(sb.toString());
			sb.delete(0, sb.length());
			super.characters(jsb.toCharArray(), 0, jsb.length());
		}
		super.endElement(namespaceURI, localName, qName);
    }
	
	public void characters(char[] a, int s, int e) throws SAXException{
		sb.append(a,s,e);
	}
	


}
