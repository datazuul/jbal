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

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.cocoon.xml.AttributesImpl;
import org.jopac2.engine.utils.ZipUnzip;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


public class CopyFile extends AbstractTransformer {
	
	private StringBuffer readbuffer=new StringBuffer();
	private String source=null,destination=null,unzip="false";
	boolean flag = false;
	@SuppressWarnings("unchecked")
	Map map;
	@SuppressWarnings("unchecked")
	Map objectModel;

	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		
	}
	
	public void startDocument() throws SAXException {
		super.startDocument();
		super.startElement("", "root", "root", new AttributesImpl());
	}
	
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
		if(!qName.equals("root")) super.startElement(namespaceURI, localName, qName, attributes);
	}
	
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

		if(qName.equals("source")) source=readbuffer.toString();
		if(qName.equals("destination")) destination=readbuffer.toString();
		if(qName.equals("unzip")) unzip=readbuffer.toString();
		
		super.characters(readbuffer.toString().toCharArray(), 0, readbuffer.length());
		if(!qName.equals("root"))  super.endElement(namespaceURI, localName, qName);
		readbuffer.delete(0,readbuffer.length());
	}
	
	public void endDocument() throws SAXException  {
		String status="ok";
		
		if(source!=null && destination!=null) {
			File s=new File(source);
			File d=new File(destination);
			try {
				if(s!=null && d!=null && safeFilename(d.getCanonicalPath())) {
					if(unzip.equals("true")) {
						ZipUnzip.unzipArchive(s.getCanonicalPath(), d.getCanonicalPath().substring(0,d.getCanonicalPath().lastIndexOf('/')));
					}
					else {
						org.apache.commons.io.FileUtils.copyFile(s, d);
					}
				}
				else {
					status="error, pathname wrong";
				}
			} catch (IOException e) {
				status="error, "+e.getMessage();
				e.printStackTrace();
			}
		}
		
		super.startElement("", "result", "resul", new AttributesImpl());
		super.characters(status.toCharArray(), 0, status.length());
		super.endElement("", "result", "resul");
		super.endElement("", "root", "root");
		
		super.endDocument();
	}

	private boolean safeFilename(String canonicalPath) {
		return true;
	}


	public void characters(char[] c, int s, int e) throws SAXException{
		readbuffer.append(c,s,e);
	}

}
