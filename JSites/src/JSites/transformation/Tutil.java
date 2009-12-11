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
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class Tutil extends MyAbstractPageTransformer {
	
	public static int index = 0;
	private long cid = 0;
	private long only_cid = 0;
	private String filename = "/tmp/file";
	
	PrintWriter pw;

	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		//super.setup(arg0, arg1, arg2, arg3);
		 o = (Request)arg1.get("request");
		//System.out.println("Setting up");
		 filename=arg2;
		 Set arg1Keys = arg1.keySet();
		 Iterator iter = arg1Keys.iterator();
		 String name = "";
		 String value = "";
		 Request o = (Request)(arg1.get("request"));
		 while(iter.hasNext()){
			 name = (String)iter.next();
			 value = arg1.get(name).toString();
			 System.out.println(name + " = " + value);
		 }
		 System.out.println("REQUEST QUERY = " + o.getQueryString());
		 
		 Enumeration e = o.getParameterNames();
		 while(e.hasMoreElements()){
			 name = (String)e.nextElement();
			 value = o.getParameter(name);
			 System.out.println(name + " = " + value);
		 }
		 try{
			 String oc = (String)arg3.getParameter("only-cid");
			 if(oc!=null){
				 this.only_cid = Long.parseLong(oc);
			 }
		 }catch(Exception eee){System.out.println("No 'only-cid' parameter specified");}
		 
		 try{
			 this.cid = Long.parseLong(o.getParameter("cid"));
		 }
		 catch(Exception eee){System.out.println("No 'cid' found"); }

		 System.out.println("--------------------");
		 
		 
		 String[] arg3Keys = arg3.getNames();
		 
		 for(int i=0;i<arg3Keys.length;i++){
			 name = arg3Keys[i];
			 try {
				value = arg3.getParameter(name);
				System.out.println(name + " = " + value);
			} catch (Exception e1) {
				System.out.println("Exception parameter: "+ name);
				e1.printStackTrace();
			}
			 
		 }

	}
	
	public void startDocument() throws SAXException{
		index++;
		try {
			pw = new PrintWriter(new FileOutputStream(filename+index+"_"+cid+".xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.startDocument();
		
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException
	{
		print(localName, attributes);
		
		try{
			super.startElement(namespaceURI, localName, qName, attributes);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(namespaceURI);
			System.out.println(localName);
			System.out.println(qName);
			System.out.println(attributes);
		}
		String s = "<" + localName + " ";
		int end = attributes.getLength();
		for(int i=0;i<end;i++){
			String name = attributes.getLocalName(i);
			String value = attributes.getValue(i);
			s = s + name + "=\"" + value + "\" ";
		}
		s = s + ">";
		if(this.only_cid == 0 || (only_cid!=0 && this.cid == only_cid ) )
			pw.print(s);
		pw.flush();
	}

	public void endDocument() throws SAXException{
		super.endDocument();
		pw.flush();
		pw.close();
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
		super.endElement(namespaceURI, localName, qName);
		if(this.only_cid == 0 || (only_cid!=0 && this.cid == only_cid ) )
			pw.print("</" + localName + ">");
		pw.flush();
    }
	
	public void characters(char[] a, int s, int e) throws SAXException{
		super.characters(a,s,e);
		String aa = new String(a);
		if(this.only_cid == 0 || (only_cid!=0 && this.cid == only_cid ) )
			pw.print(aa.substring(s,s+e));
	}
	
	private void print(String localName, Attributes attributes) {
		System.out.println(localName);
		for(int i=0;i<attributes.getLength();i++){
			System.out.println(attributes.getValue(i));
		}
	}

}
