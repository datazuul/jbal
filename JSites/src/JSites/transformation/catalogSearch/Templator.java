/*
 * Record2display.java
 *
 * Created on 15 settembre 2004, 11.58
 */

package JSites.transformation.catalogSearch;
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

/**
* @author	Albert Caramia
* @version	??/??/2002
* 
* @author	Romano Trampus
* @version	??/??/2002
* 
* @author	Romano Trampus
* @version	15/09/2004
* 
* @author 	Romano Trampus
* @version	09/03/2006
* Corretto bug sulle connessioni e aggiunto dispose: se la variabile di tipo 
* Connection e' globale allora non viene rilasciata e il pool si satura.
*/

import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.ProcessingException;

import org.apache.avalon.framework.parameters.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.apache.cocoon.xml.AttributesImpl;

import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentSelector;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.component.Composable;
import org.jopac2.utils.BookSignature;

import JSites.transformation.MyAbstractPageTransformer;

import java.sql.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class Templator extends MyAbstractPageTransformer implements Composable, Disposable //, CacheableProcessingComponent
{
    private boolean isRecord=false,debug=false;
    private boolean isOptimizedQuery=false;

    private Document document=null;
    private Element currentElement=null;

    protected ComponentManager manager;

    //private RecordInterface ma;
    private StringBuffer buffer=null;
    private String template="";
    private Hashtable<String,Boolean> v;

    
    public void sendElement(String element,String value) throws SAXException {
    	if(value!=null) {
    		if(debug) System.out.println("Templator: element " + element + ": "+ value);
	    	value=value.replaceAll(String.valueOf((char)30),"");
	    	if(value.length()>0) {
		    	contentHandler.startElement("",element,element,new AttributesImpl());
		        contentHandler.characters(value.toCharArray(), 0, value.length());
		        contentHandler.endElement("",element,element);
	    	}
    	}
    	else {
    		if(debug) System.out.println("Templator: ELEMENT "+ element + " IS NULL");
    	}
    }
    
    @SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par)
            throws ProcessingException, SAXException, IOException
    {
    	super.setup(resolver, objectModel, src, par);
    	debug=false;isRecord=false;
    	DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
    	try {
    		DocumentBuilder builder=factory.newDocumentBuilder();
			document=builder.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if(buffer!=null) {
    		buffer.delete(0, buffer.length());
    	}
    	else {
    		buffer=new StringBuffer();
    	}
    	
    	if(src!=null && src.equals("debug")) {
    		System.out.println("Templator: DEBUG ENABLED");
    		debug=true;
    	}
    }

    public void characters(char[] ch, int start, int len) throws SAXException {
    	buffer.append(ch, start, len);
    }
	
	public void startElement(String namespaceURI, String localName, String qName,
            Attributes attributes) throws SAXException
    {
		if (namespaceURI.equals("") && localName.equals("root")) {
			v.clear();buffer.delete(0, buffer.length());
		}
		else if (namespaceURI.equals("") && localName.equals("optimizedQuery")) {
			isOptimizedQuery=true;
		}
        if (namespaceURI.equals("") && localName.equals("template")) {
//            isTemplate=true;
        }
        else if (namespaceURI.equals("") && localName.equals("record")) {
        	isRecord=true;
        	currentElement=document.createElement(localName);
        	document.appendChild(currentElement);
            return;
        }
        else if(isRecord) {
        	Element c=document.createElement(localName);
        	setAttributes(currentElement,attributes);
        	currentElement.appendChild(c);
        	currentElement=c;
        }
        else {
        	super.startElement(namespaceURI, localName, qName, attributes);
        }
    }

    private void setAttributes(Element currentElement2, Attributes attributes) {
		for(int i=0;attributes!=null && i<attributes.getLength();i++) {
			currentElement2.setAttribute(attributes.getLocalName(i), attributes.getValue(i));
		}
	}

	public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException
    {
        if (namespaceURI.equals("") && localName.equals("optimizedQuery")) {
        	populateHashParole();
        	super.characters(buffer.toString().toCharArray(),0,buffer.length());
        	super.endElement(namespaceURI, localName, qName);
        	isOptimizedQuery=false;
        }
        else if (namespaceURI.equals("") && localName.equals("template")) {
//    		isTemplate=false;
    		template=buffer.toString(); //.replaceAll("[\n\r]", "").trim();
    		buffer.delete(0, buffer.length());
    	}
    	else if (namespaceURI.equals("") && localName.equals("record")) {
            isRecord=false;
            currentElement.appendChild(document.createTextNode(buffer.toString()));
            buffer.delete(0, buffer.length());
//            String pritableRecordXML=XML2String(document);
//            System.out.println(pritableRecordXML);
            String rRecord=parseTemplate(template,document);
            super.startElement("", "record", "record", this.emptyAttrs);
        	if(rRecord!=null && rRecord.length()>0) 
        		super.characters(rRecord.toCharArray(), 0, rRecord.length());
            super.endElement("", "record", "record");
            emptyDocument(document);
        }
        else if(isRecord) {
        	 currentElement.appendChild(document.createTextNode(buffer.toString()));
        	 buffer.delete(0, buffer.length());
        	 currentElement=(Element) currentElement.getParentNode();
        }
        else {
        	dispatch();
        	super.endElement(namespaceURI, localName, qName);
        }
    }

	private void emptyDocument(Document document2) {
		removeAll(document.getFirstChild());
	}

    public static void removeAll(Node node) {
        NodeList list = node.getChildNodes();
        for (int i=0; i<list.getLength(); i++) {
            removeAll(list.item(i));
        }
        node.getParentNode().removeChild(node);
    }

	
	private String XML2String(Document document2) {
		javax.xml.transform.TransformerFactory tfactory = TransformerFactory.newInstance();
		javax.xml.transform.Transformer xform;
		try {
			xform = tfactory.newTransformer();
			javax.xml.transform.Source src = new DOMSource(document2);
			java.io.StringWriter writer = new StringWriter();
			StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
			xform.transform(src, result);
			return writer.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String parseTemplate(String template2, Document document2) {
		String[] blocks=template2.split("\\[\\[");
		String output=blocks[0];
		for(int i=1;i<blocks.length;i++) {
			if(blocks[i].contains("]]")) {
				int p=blocks[i].indexOf("]]");
				String nodeName=blocks[i].substring(0,p);
				blocks[i]=blocks[i].substring(p+2);
				NodeList ce=document2.getElementsByTagName(nodeName);
				Element e=(Element) ce.item(0);
				String value="";
				if(e!=null) value=e.getTextContent();
				value=markWord(value);
				output+=value;
			}
			output+=blocks[i];
		}
		return output;
	}
	
	private String markWord(String value) {
		String left="", right="", r="";
        StringTokenizer tk=new StringTokenizer(value," ,.;()/-'\\:=@%$&!?[]#*<>\016\017",true);
        while(tk.hasMoreTokens()) {
            right=tk.nextToken();
            if(isParola(right.toLowerCase())) {
            	right+=" ";
            	r+=left;
            	r+="<span class=\"match\">"+right+"</span>";
//                super.startElement("","text","text",new AttributesImpl());
//                super.characters(left.toCharArray(),0,left.length());
//                super.endElement("","text","text");
//                super.startElement("","match","match",new AttributesImpl());
//                super.characters(right.toCharArray(),0,right.length());
//                super.endElement("","match","match");
                left="";
            }
            else {
                left+=right;
            }
        }
        if(left.length()>0) {
        	r+=left;
//            super.startElement("","text","text",new AttributesImpl());
//            super.characters(left.toCharArray(),0,left.length());
//            super.endElement("","text","text");
        }
		return r;
	}
	
    private boolean isParola(String parola) {
        return v.containsKey(quota(parola));
    }

	private String quota(String parola) {
    	parola = parola.toLowerCase();
		parola = parola.replaceAll("\u010d","c");
		parola = parola.replaceAll("�","s");
		parola = parola.replaceAll("�","z");//
		parola = parola.replaceAll("\u0107","c");
		parola = parola.replaceAll("\u0111","d");
		parola = parola.replaceAll("dj","d");
		return parola;
	}
	
    private void populateHashParole() {
    	/*
    	 * optimizedQuery e' della forma:
    	 * 
    	 *      [Ritter(6:0ms,qry:123ms,op:0ms)](2:0ms,qry:0ms,op:0ms)AND[Alexander(9:0ms,qry:76ms,op:0ms)]
    	 *      
    	 * Le parole cercate sono sempre comprese tra [......(
    	 */
    	String[] parole=buffer.toString().split("\\[");
    	for(int i=0;i<parole.length;i++) {
    		int k=parole[i].indexOf('(');
    		if(k>1) {
    			v.put(quota(parole[i].substring(0,k)), new Boolean(true));
    		}
    	}
    	//v.put(quota(new String(ch,start,len).toLowerCase()),new Boolean(true));
//      System.out.println("Got: "+new String(ch,start,len));
    }

	private void dispatch() throws SAXException {
    	super.characters(buffer.toString().toCharArray(), 0, buffer.length());
    	buffer.delete(0, buffer.length());
    }
    
    public Connection getConnection(String db) throws ComponentException, SQLException {
    	return ((DataSourceComponent)dbselector.select(db)).getConnection();
    }
    
    public void compose(ComponentManager manager) throws ComponentException {
    	this.manager=manager;
        v=new Hashtable<String,Boolean>();

        dbselector =
            (ComponentSelector) manager.lookup(DataSourceComponent.ROLE + "Selector");
    }
    
    public void dispose() {
        this.manager.release(dbselector);
    }

/*	public Serializable getKey() {
    	if(src.startsWith("debug,")) {
    		System.out.println("Record2display: DEBUG ENABLED");
    		debug=true;
    		db=src.substring(6);
    	}
    	else {
    		db=src;
    	}
		return null;
	}

	public SourceValidity getValidity() {
		long t=System.currentTimeMillis()/1000000;
		System.out.println("getValidity: "+t);
		if(t>0) {
			return new TimeStampValidity(t);
		}
		else {
			return null;
		}
	}*/


}



