package org.jopac2.jbal.xml;

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

import java.io.StringWriter;
import java.util.Vector;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jopac2.utils.TokenWord;

public class XMLHelper {

	  public static void exploreData(Node node, Vector<TokenWord> v,String prefix) {
		  	int type = node.getNodeType();
		  	switch(type) {
		  		case Node.DOCUMENT_NODE:
					{
						NodeList children = node.getChildNodes();
						int len = children.getLength();
						for (int i=0; i<len; i++)
							exploreData(children.item(i),v,prefix);
					}
					break;
			  	case Node.ELEMENT_NODE:
			  		{
						NodeList children = node.getChildNodes();
						int len = children.getLength();
						for (int i=0; i<len; i++)
							exploreData(children.item(i),v,prefix+"/"+node.getNodeName());
			  		}
					break;
			  	case Node.TEXT_NODE:
		  			if(node.getNodeValue().replaceAll("\n", "").length()>0) 
		  				v.addElement(new TokenWord(node.getNodeValue(),prefix,""));
			    break;
		  	}
		  }
		  
	  public static Vector<TokenWord> exploreData(Node node) {
		Vector<TokenWord> v=new Vector<TokenWord>();
	  	exploreData(node, v, "");
	  	return v;
	  }
		  
	  public static void exploreAttributes(Node node) {
	  
	    NamedNodeMap attrs = node.getAttributes();
	  
	    int len = attrs.getLength();
	    for (int i=0; i<len; i++) {
	        Attr attr = (Attr)attrs.item(i);
	        System.out.print(" " + attr.getNodeName() + "=\"" +
	                  attr.getNodeValue() + "\"");
	    }
	  }
	  
	  public static String lookAttr(Node n,String name) {
		  NamedNodeMap m=n.getAttributes();
		  return lookAttr(m,name);
		  }
		  
	  public static String lookAttr(NamedNodeMap attrs,String name) {
	  	String r=null;
	  	int len = attrs.getLength();
	    for (int i=0; i<len; i++) {
	        Attr attr = (Attr)attrs.item(i);
	        if(attr.getNodeName().equals(name)) r=attr.getNodeValue();
	    }
	    return r;
	  }
	  
	  public static Node getNode(Node node, String nodename) {
		 Node r=null;
		 if(node.getNodeName().equals(nodename)) {
			 r=node;
		 }
		 else { 
			NodeList children = node.getChildNodes();
			int len = children.getLength();
			for (int i=0; i<len&&r==null; i++)
				r=getNode(children.item(i),nodename);
		 }
		 return r;
	  }
	  
	  public static String getNodeContent(Node n, String nodename) {
		  Node r=getNode(n,nodename);
		  if(r!=null) return r.getTextContent(); else return null;
	  }

	public static void destroy(Node document) {
		// TODO: ricorsivamente disallocare tutti i nodi
	}

	public static String toString(Node document) {
		Transformer transformer;
		String output=null;
		if(document!=null)
			try {
				transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	
	//			initialize StreamResult with File object to save to file
				StreamResult result = new StreamResult(new StringWriter());
				DOMSource source = new DOMSource(document);
				transformer.transform(source, result);
	
				output = result.getWriter().toString();
	
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}

		return output;
	}
  }
