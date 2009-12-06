

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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.apache.cocoon.xml.AttributesImpl;

import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentSelector;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.component.Composable;
import JSites.transformation.MyAbstractPageTransformer;

import java.sql.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Templator extends MyAbstractPageTransformer implements Composable,
		Disposable // , CacheableProcessingComponent
{
	private boolean isRecord = false, debug = false;
	// private boolean isOptimizedQuery=false;

	private Document document = null;
	private Element currentElement = null;
	private int level=0;

	protected ComponentManager manager;

	// private RecordInterface ma;
	private StringBuffer buffer = null;
	private String template = "";
	private Hashtable<String, Boolean> v;
	private Hashtable<String, String> para = new Hashtable<String, String>();

	public void sendElement(String element, String value) throws SAXException {
		if (value != null) {
			if (debug)
				System.out.println("Templator: element " + element + ": "
						+ value);
			value = value.replaceAll(String.valueOf((char) 30), "");
			if (value.length() > 0) {
				contentHandler.startElement("", element, element,
						new AttributesImpl());
				contentHandler.characters(value.toCharArray(), 0, value
						.length());
				contentHandler.endElement("", element, element);
			}
		} else {
			if (debug)
				System.out
						.println("Templator: ELEMENT " + element + " IS NULL");
		}
	}

	@SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src,
			Parameters par) throws ProcessingException, SAXException,
			IOException {
		super.setup(resolver, objectModel, src, par);
		debug = false;
		isRecord = false;
		level=0;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (buffer != null) {
			buffer.delete(0, buffer.length());
		} else {
			buffer = new StringBuffer();
		}

		if (src != null && src.equals("debug")) {
			System.out.println("Templator: DEBUG ENABLED");
			debug = true;
		}
	}

	public void characters(char[] ch, int start, int len) throws SAXException {
		buffer.append(ch, start, len);
	}
	
	private void setAttributes(Element currentElement2, Attributes attributes) {
		for (int i = 0; attributes != null && i < attributes.getLength(); i++) {
			currentElement2.setAttribute(attributes.getLocalName(i), attributes
					.getValue(i));
		}
	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attributes) throws SAXException {
		if (namespaceURI.equals("") && localName.equals("root")) {
			v.clear();
			buffer.delete(0, buffer.length());
			para.clear();
		}
		// if (namespaceURI.equals("") && localName.equals("optimizedQuery")) {
		// // isOptimizedQuery=true;
		// }
		if (namespaceURI.equals("") && localName.equals("template")) {
			// isTemplate=true;
		}  
		if (namespaceURI.equals("") && localName.equals("record")) {
			level++;
			if(!isRecord) {
				isRecord = true;
				currentElement = document.createElement(localName);
				document.appendChild(currentElement);
				return;
			}
		}  
		if (isRecord) {
			Element c = document.createElement(localName);
			setAttributes(currentElement, attributes);
			currentElement.appendChild(c);
			currentElement = c;
		} else {
			super.startElement(namespaceURI, localName, qName, attributes);
		}
	}

	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (namespaceURI.equals("") && localName.equals("optimizedQuery")) {
			populateHashParole();
			super.characters(buffer.toString().toCharArray(), 0, buffer
					.length());
			buffer.delete(0, buffer.length());
			// isOptimizedQuery=false;
		}  
		if (namespaceURI.equals("") && localName.equals("template")) {
			// isTemplate=false;
			template = buffer.toString(); // .replaceAll("[\n\r]", "").trim();
			buffer.delete(0, buffer.length());
		}  
		if (namespaceURI.equals("") && localName.equals("record")) {
			level--;
			if(level==0) {
				isRecord = false;
				currentElement.appendChild(document.createTextNode(buffer
						.toString()));
				buffer.delete(0, buffer.length());
				// String pritableRecordXML=XML2String(document);
				// System.out.println(pritableRecordXML);
	
//				String rRecord = parseTemplate(template, document);
				String rRecord=null;
				try {
					rRecord = Templator.parseContext(document, "{{/:"+template+"}}");
				} catch (Exception e) {
					e.printStackTrace();
				}
				super.startElement("", "record", "record", this.emptyAttrs);
				if (rRecord != null && rRecord.length() > 0)
					super.characters(rRecord.toCharArray(), 0, rRecord.length());
				super.endElement("", "record", "record");
				emptyDocument(document);
				return;
			}
		}  
		if (isRecord) {
			currentElement.appendChild(document.createTextNode(buffer
					.toString()));
			buffer.delete(0, buffer.length());
			currentElement = (Element) currentElement.getParentNode();
		} else {
			if (!isRecord)
				para.put(localName, buffer.toString());

			dispatch();
			super.endElement(namespaceURI, localName, qName);
		}
	}

	private void emptyDocument(Document document2) {
		removeAll(document.getFirstChild());
	}

	public static void removeAll(Node node) {
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			removeAll(list.item(i));
		}
		node.getParentNode().removeChild(node);
	}

	public static String XML2String(Document document2) {
		javax.xml.transform.TransformerFactory tfactory = TransformerFactory
				.newInstance();
		javax.xml.transform.Transformer xform;
		try {
			xform = tfactory.newTransformer();
			javax.xml.transform.Source src = new DOMSource(document2);
			java.io.StringWriter writer = new StringWriter();
			StreamResult result = new javax.xml.transform.stream.StreamResult(
					writer);
			xform.transform(src, result);
			return writer.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Document String2XML(String xmlSource) throws SAXException,
			IOException, ParserConfigurationException {
//		xmlSource="<div class=\"resultSet\">"+xmlSource+"</div>";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(xmlSource)));
	}

	
	
	/**
	 * @deprecated
	 * 
	 * NON CANCELLARE
	 * 
	 * TODO controllare sequenza output
	 * 
	 * @param template2
	 * @param document2
	 * @return
	 */
	private String parseTemplate(String template2, Document document2) {
		Document templateDocument = null;
		try {
			templateDocument = String2XML("<div class=\"resultSet\">"+template+"</div>");
		} catch (Exception e) {
			e.printStackTrace();
		}

		parseTemplate(templateDocument, document2);

		purgeEmptyElements(templateDocument, "a");
		purgeEmptyAttribute(templateDocument, "a","href");
		swapTagOrder(templateDocument,"a","string");
		swapTagOrder(templateDocument,"a","span");

		String output = XML2String(templateDocument);

		return output;
	}

	
	/**
	 * Inverte l'ordine di nodi innestati.
	 * <tag1>
	 * 		<tag2></tag2>
	 * </tag1>
	 * 
	 * diventa
	 * 
	 * <tag2>
	 * 		<tag1></tag1>
	 * <tag2>
	 * 
	 * @param node
	 * @param tag1
	 * @param tag2
	 */
	private void swapTagOrder(Node node, String tag1,
			String tag2) {
		if (node != null) {
			NodeList nl = node.getChildNodes();
			if (nl != null)
				for (int i = nl.getLength(); i >= 0; i--) {
					if (nl.item(i) != null && nl.item(i).getNodeName().equals(tag1)) {
						NodeList nl1=nl.item(i).getChildNodes();
						if(nl1!=null && nl1.getLength()==1 && nl1.item(0)!=null && nl1.item(0).getNodeName().equals(tag2)) {
							Node t1=nl.item(i);
							Node t2=nl1.item(0);
							// togli t2 da t1
							t1.removeChild(t2);
							// togli t1 da node
							node.removeChild(t1);
							// aggiungi t1 a t2
							t2.appendChild(t1);
							// aggiungi t2 a node
							node.appendChild(t2);
						}
						else {
							swapTagOrder(nl.item(i),tag1,tag2);
						}
					}
					else {
						swapTagOrder(nl.item(i),tag1,tag2);
					}
				}
		}
		
	}

	/**
	 * Toglie i casi
	 * <a>jljklkjl</a> (con href vuoto)
	 * @param templateDocument
	 * @param string
	 * @param string2
	 */
	private void purgeEmptyAttribute(Node node, String tag,
			String attribute) {
		if (node != null) {
			NodeList nl = node.getChildNodes();
			if (nl != null)
				for (int i = nl.getLength(); i >= 0; i--) {
					if (nl.item(i) != null) {
						if (isEmptyAttribute(nl.item(i), tag, attribute)) {
							NodeList kl=nl.item(i).getChildNodes();
							if(kl!=null) {
								for(int y=kl.getLength();y>=0;y--) {
									if(kl.item(y)!=null) {
										Node t=kl.item(y);
										nl.item(i).removeChild(t);
										node.appendChild(t);
									}
								}
							}
							node.removeChild(nl.item(i));
						} else {
							purgeEmptyAttribute(nl.item(i), tag, attribute);
						}
					}
				}
		}
	}

	private boolean isEmptyAttribute(Node node, String tag, String attribute) {
		boolean r=false;
		if(node!=null) {
			if(node.getNodeName().equals(tag)) {
				NamedNodeMap attributes = node.getAttributes();
				Node s=attributes.getNamedItem(attribute);
				if(s==null) r=true;
				else {
					String v=s.getNodeValue();
					if(v==null || v.trim().length()==0) r=true;
				}
			}
		}
		return r;
	}

	/**
	 * I browser sbagliano i casi come:
	 * <p>
	 * <a href="hkhkhk" />abcd
	 * </p>
	 * e li interpretano come
	 * <p>
	 * <a href="hkhkhk">abcd</a>
	 * </p>
	 * 
	 * @param templateDocument
	 * @param string
	 */
	private void purgeEmptyElements(Node node, String tag) {
		if (node != null) {
			NodeList nl = node.getChildNodes();
			if (nl != null)
				for (int i = nl.getLength(); i >= 0; i--) {
					if (isEmptyTag(nl.item(i), tag)) {
						if (nl.item(i) != null)
							node.removeChild(nl.item(i));
					} else {
						purgeEmptyElements(nl.item(i), tag);
					}
				}
		}
	}

	private boolean isEmptyTag(Node item, String tag) {
		boolean r = false;
		if (item == null)
			return true;
		if (item.getNodeName().equalsIgnoreCase(tag)) {
			r = true;
			NodeList nl = item.getChildNodes();
			for (int i = 0; nl != null && i < nl.getLength(); i++) {
				if ((nl.item(i).getNodeType() == Node.TEXT_NODE && nl.item(i)
						.getTextContent().trim().length() > 0)
						|| nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
					r = false;
					break;
				}
			}
		}
		return r;
	}

	
	/**
	 * @deprecated
	 * @param node
	 * @param document2
	 */
	private void parseTemplate(Node node, Document document2) {
		parseTemplate(node, document2, -1);
	}

	/**
	 * @deprecated
	 * @param node
	 * @param document2
	 * @param k
	 */
	private void parseTemplate(Node node, Document document2, int k) {
		if(node==null) return;
		int splittableNode = isSplittableNode(node, document2, k);
		if (splittableNode > 1) {
			// deve duplicare il nodo tante volte quante sono le entry
			Node[] nc = new Node[splittableNode - 1];
			Node parent = node.getParentNode();
			for (int i = 1; i < splittableNode; i++) {
				nc[i - 1] = node.cloneNode(true);
				parent.appendChild(nc[i - 1]);
				Node space = node.getOwnerDocument().createTextNode(" ");
				parent.appendChild(space);
				// ownerDocument.insertBefore(nc[i-1],node);
				parseTemplate(nc[i - 1], document2, i);
			}
			parseTemplate(node, document2, 0);
		} else {
			NodeList nl = node.getChildNodes();
			for (int i = 0; nl != null && i < nl.getLength(); i++) {
				if (nl.item(i).getNodeType() == Node.ELEMENT_NODE)
					parseTemplate(nl.item(i), document2);
			}
		}

	}

	/**
	 * @deprecated
	 * @param node
	 * @param document2
	 * @param nn
	 * @return
	 */
	private int isSplittableNode(Node node, Document document2, int nn) {
		int r = 1;
		NamedNodeMap attributes = node.getAttributes();

		for (int a = 0; attributes != null && a < attributes.getLength(); a++) {
			Node theAttribute = attributes.item(a);
			int k = isSplittableContent(theAttribute, document2, nn);
			r = k > r ? k : r;
		}

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			NodeList nl = node.getChildNodes();
			/**
			 * Loop al contrario perche' viene inserito un nodo
			 * processingInstruction per disabilitare l'escaping dell'output
			 */
			if (nl != null)
				for (int i = nl.getLength(); i >= 0; i--) {
					if (nl.item(i) != null
							&& nl.item(i).getNodeType() == Node.TEXT_NODE) {
						int k = isSplittableContent(nl.item(i), document2, nn);
						r = k > r ? k : r;
					}
				}
		}
		return r;
	}

	/**
	 * @deprecated
	 * @param node
	 * @param document2
	 * @param nn
	 * @return
	 */
	private int isSplittableContent(Node node, Document document2, int nn) {
		int r = 1;
		String c = "";
		if (node.getNodeType() == Node.TEXT_NODE) {
			c = node.getTextContent();
		} else {
			c = node.getNodeValue();
		}
		String[] blocks = c.split("\\[\\[");
		String output = blocks[0];
		for (int i = 1; i < blocks.length; i++) {
			if (blocks[i].contains("]]")) {
				String value = "";
				int p = blocks[i].indexOf("]]");
				String nodeName = blocks[i].substring(0, p);
				nodeName=nodeName.replaceAll("\\\\:", "&#58;");
				nodeName=nodeName.replaceAll("\\\\n", "<br/>");
				String pre="",post="";
				
				String[] toki=nodeName.split(":");
				if(toki.length==2) {pre=toki[0];nodeName=toki[1];}
				else if(toki.length==3) {pre=toki[0];nodeName=toki[1];post=toki[2];}
					
				
				blocks[i] = blocks[i].substring(p + 2);
				boolean m = nodeName.contains(",");
				String sep = " - ",rep = " - ";
				String origNodeName=nodeName;
				if (m) {
					String[] parts=nodeName.split(",");
					if(parts.length>1)
						sep = parts[1];
					if(parts.length>2)
						rep = parts[2];
					nodeName = parts[0];
				}
				
				Object result=null;
				XPath xpath = XPathFactory.newInstance().newXPath();
			    XPathExpression expr=null;
				try {
					expr = xpath.compile(nodeName);
					result = expr.evaluate(document2, XPathConstants.NODESET);
				} catch (XPathExpressionException e2) {
					e2.printStackTrace();
				}
				

			    NodeList nodes = (NodeList) result;
			    if(nodes.getLength()>0) value=nodes.item(0).getTextContent();
			    for (int j = 1; j < nodes.getLength(); j++) {
			    	value=value+sep+nodes.item(j).getTextContent();;
			    }
				if(value==null || value.length()==0) {
					value = para.get(nodeName);
					if (value == null)
						value = "";
				}
				
				if (m) {
					String[] values = value.split(sep);
					if (nn == -1) {
						value=values[0];
						for(int z=1;z<values.length;z++) value=value+rep+values[z];
					}
					else if (nn >= 0 || values.length == 1) {

						if (nn < values.length) {
							value = values[nn]+rep;
						}
					} else {
						value = "[[" + origNodeName + "]]";
						r = values.length;
					}
				}
				if(value.trim().length()>0) {
					value=parse(pre)+value+parse(post);
				}
				output += value;
			}
			output += blocks[i];
		}

		if (node.getNodeType() == Node.TEXT_NODE) {
			output = output.replaceAll("&gt;", ">").replaceAll("&lt;", "<")
					.replaceAll("&amp;", "&");
			node.setTextContent(markWord(output));
			ProcessingInstruction pi = node.getOwnerDocument()
					.createProcessingInstruction(
							Result.PI_DISABLE_OUTPUT_ESCAPING, "");
			node.getParentNode().insertBefore(pi, node);
		} else {
			node.setNodeValue(output);
		}

		return r;
	}

	/**
	 * @deprecated
	 * @param string
	 * @return
	 */
	private String parse(String string) {
		string=string.replaceAll("\\|\\|", "<").replaceAll("!!",">");
		return string;
	}
	
	public static void groupPair(Document doc, String separator, String child, String... elements) {
		Object result=null;
		XPath xpath = XPathFactory.newInstance().newXPath();
	    XPathExpression expr=null;
		try {
			expr = xpath.compile(elements[0]);
			result = expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e2) {
			e2.printStackTrace();
		}
		

	    NodeList nodes = (NodeList) result;

	    for (int j = 0; j < nodes.getLength(); j++) {
	    	Node parent=nodes.item(j).getParentNode();
	    	moveElements(doc,parent,separator,child,elements);
	    }
	}
	
	
	private static void moveElements(Document doc, Node parent, String separator, String child,
			String[] elements) {
		
		Node[] sub=getChildren(doc,parent,elements);
		
		int k=1;
		int p=0;
		while(k!=-1) {
			Node c=doc.createElement(child);
			
			k=-1;
			for(int i=0;i<elements.length;i++) {
				int z=appendSubChild(c,separator,sub[i],p);
				if(z>k) k=z;
			}
			if(k!=-1) parent.appendChild(c);
			p++;
		}
		for(int j=0;j<elements.length;j++) {
			if(sub[j]!=null) parent.removeChild(sub[j]);
		}
		
	}


	private static int appendSubChild(Node node, String sep, Node value, int p) {
		Node sc=node.getOwnerDocument().createElement(value.getNodeName());
		String[] v=value.getTextContent().split(sep);
		int z=-1;
		if(p<v.length) {
			z=v.length;
			sc.setTextContent(v[p].trim());
			node.appendChild(sc);
		}
		return z;
	}


	private static Node[] getChildren(Document doc, Node parent, String[] elements) {
		Node[] r=new Node[elements.length];
		for(int i=0;i<elements.length;i++) {
			Object result=null;
			XPath xpath = XPathFactory.newInstance().newXPath();
		    XPathExpression expr=null;
			try {
				expr = xpath.compile(elements[i]);
				result = expr.evaluate(doc, XPathConstants.NODESET);
			} catch (XPathExpressionException e2) {
				e2.printStackTrace();
			}
			
		    NodeList nodes = (NodeList) result;
		    if(nodes.getLength()>0) r[i]=nodes.item(0);
		    else r[i]=null;
		}
		return r;
	}
	
	
	public static String parseContext(Document doc, String in) throws Exception {
		StringBuffer buffer=new StringBuffer();
		int bc=0,be=0,c=0;
		String context="/";
		if(!in.startsWith("{{") || !in.endsWith("}}")) throw new Exception("Template context error");
		int u=in.indexOf(":");
		context=in.substring(2, u);
		in=in.substring(u+1,in.length()-2);
		
		Object result=null;
		XPath xpath = XPathFactory.newInstance().newXPath();
	    XPathExpression expr=null;
		try {
			expr = xpath.compile(context);
			result = expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e2) {
			e2.printStackTrace();
		}
		

	    NodeList nodes = (NodeList) result;
	    String ctx0=context;
	    for (int j = 0; j < nodes.getLength(); j++) {
	    	if(nodes.getLength()>1) context=ctx0+"["+(j+1)+"]";
	    	bc=0;be=0;c=0;
			while(c<in.length()) {
				bc=in.indexOf("{{",c);
				be=in.indexOf("[[",c);
				if(bc==-1) bc=in.length();
				if(be==-1) be=in.length();
				int o=Math.min(bc, be);
				buffer.append(in.substring(c, o));
				c=o;
				if(c<in.length()) {
					if(o==bc) {
						int z=closeBracket(in,o)+2;
						
						buffer.append(parseContext(doc,in.substring(o,z)));
						c=z;
					}
					else if(o==be) {
						int z=in.indexOf("]]",o)+2;
						String element=in.substring(o,z);
						if(element.startsWith("[[//")) {
							// ahah, l'elemento non ha contesto!
							buffer.append(parseContext(doc,"{{"+element.substring(2,element.length()-2)+":[[.]]}}"));
						}
						else {
							buffer.append(parseElement(doc,context,element));
						}
						c=z;
					}
				}
			}
	    }
		return buffer.toString();
	}

	/**
	 * ....{{......{{......{{....}}.....{{....}}......}}......}}.....
	 * 
	 * ......{{....}}......{{....}}....             // non va bene lastIndexOf
	 * 
	 * @param in
	 * @param o
	 * @return
	 */
	private static int closeBracket(String in, int o) {
		int i=o;
		int count=0;
		
		for(i=o; i<in.length()-1;i++) {
			if(in.charAt(i)=='{' && in.charAt(i+1)=='{') {
				count++;i++;
				continue;
			}
			if(in.charAt(i)=='}' && in.charAt(i+1)=='}') {
				count--;i++;
				if(count==0) {
					i--;break;
				}
				else {
					continue;
				}
			}
		}
		return i;
	}

	private static String parseElement(Document doc, String context, String element) {
		String value="";
		String sep=" - ";
		int n=-1;
		
		element=element.substring(2, element.length()-2);
		if(element.equals(".") && context.startsWith("//") && context.endsWith("]")) {
			int y=context.indexOf("[");
			String p=context.substring(y+1, context.indexOf("]"));
			element=context.substring(0,y);
			n=Integer.parseInt(p);
		}
		else {
			if(!context.equals("/") || !element.startsWith("/")) {
				if(context.endsWith("/")) element=context+element;
				else element=context+"/"+element;
			}
		}
		Object result=null;
		XPath xpath = XPathFactory.newInstance().newXPath();
	    XPathExpression expr=null;
		try {
			expr = xpath.compile(element);
			result = expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e2) {
			e2.printStackTrace();
		}
		

	    NodeList nodes = (NodeList) result;
	    if(n==-1) {
		    if(nodes.getLength()>0) value=nodes.item(0).getTextContent();
		    for (int j = 1; j < nodes.getLength(); j++) {
		    	value=value+sep+nodes.item(j).getTextContent();;
		    }
	    }
	    else {
	    	value=nodes.item(n-1).getTextContent();
	    }
		return value;
	}
	

	public static void listNodes(Node node, String indent) {
		String nodeName = node.getNodeName();
		System.out.println(indent + nodeName + " Node, type is "
				+ node.getClass().getName() + ":");
		System.out.println(indent + " " + node);

		NodeList list = node.getChildNodes();
		if (list.getLength() > 0) {
			System.out.println(indent + "Child Nodes of " + nodeName + " are:");
			for (int i = 0; i < list.getLength(); i++)
				listNodes(list.item(i), indent + " ");
		}
	}

	private String markWord(String value) {
		String left = "", right = "", r = "";
		StringTokenizer tk = new StringTokenizer(value,
				" ,.;()/-'\\:=@%$&!?[]#*<>\016\017", true);
		while (tk.hasMoreTokens()) {
			right = tk.nextToken();
			if (isParola(right.toLowerCase())) {
				right += " ";
				r += left;
				r += "<span class=\"match\">" + right + "</span>";
				left = "";
			} else {
				left += right;
			}
		}
		if (left.length() > 0) {
			r += left;
		}
		return r;
	}

	private boolean isParola(String parola) {
		return v.containsKey(quota(parola));
	}

	private String quota(String parola) {
		parola = parola.toLowerCase();
		parola = parola.replaceAll("\u010d", "c");
		// parola = parola.replaceAll("�","s");
		// parola = parola.replaceAll("�","z");//
		parola = parola.replaceAll("\u0107", "c");
		parola = parola.replaceAll("\u0111", "d");
		parola = parola.replaceAll("dj", "d");
		return parola;
	}

	private void populateHashParole() {
		/*
		 * optimizedQuery e' della forma:
		 * 
		 * 
		 * [Ritter(6:0ms,qry:123ms,op:0ms)](2:0ms,qry:0ms,op:0ms)AND[Alexander(9:
		 * 0ms,qry:76ms,op:0ms)]
		 * 
		 * Le parole cercate sono sempre comprese tra [......(
		 */
		String[] parole = buffer.toString().split("\\[");
		for (int i = 0; i < parole.length; i++) {
			int k = parole[i].indexOf('(');
			if (k > 1) {
				v.put(quota(parole[i].substring(0, k)), new Boolean(true));
			}
		}
		// v.put(quota(new String(ch,start,len).toLowerCase()),new
		// Boolean(true));
		// System.out.println("Got: "+new String(ch,start,len));
	}

	private void dispatch() throws SAXException {
		super.characters(buffer.toString().toCharArray(), 0, buffer.length());
		buffer.delete(0, buffer.length());
	}

	public Connection getConnection(String db) throws ComponentException,
			SQLException {
		return ((DataSourceComponent) dbselector.select(db)).getConnection();
	}

	public void compose(ComponentManager manager) throws ComponentException {
		this.manager = manager;
		v = new Hashtable<String, Boolean>();

		dbselector = (ComponentSelector) manager
				.lookup(DataSourceComponent.ROLE + "Selector");
	}

	public void dispose() {
		this.manager.release(dbselector);
	}

	/*
	 * public Serializable getKey() { if(src.startsWith("debug,")) {
	 * System.out.println("Record2display: DEBUG ENABLED"); debug=true;
	 * db=src.substring(6); } else { db=src; } return null; }
	 * 
	 * public SourceValidity getValidity() { long
	 * t=System.currentTimeMillis()/1000000;
	 * System.out.println("getValidity: "+t); if(t>0) { return new
	 * TimeStampValidity(t); } else { return null; } }
	 */

}
