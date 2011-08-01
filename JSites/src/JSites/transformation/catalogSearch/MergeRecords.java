package JSites.transformation.catalogSearch;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jopac2.jbal.xml.XMLHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import JSites.utils.DOMUtils;
import JSites.utils.XMLUtil;

public class MergeRecords implements RecordFunction {

	public Document[] processRecords(Document aDoc, Document bDoc,
			String... args) throws ParserConfigurationException {
		boolean merge=true;
		Document[] result=null;
		Document merged=XMLUtil.createDocument();

		String aRootName=aDoc.getDocumentElement().getNodeName();
		String bRootName=bDoc.getDocumentElement().getNodeName();
		
		Node common=merged.createElement(aRootName);
		merged.appendChild(common);
		
		if(!aRootName.equals(bRootName)) merge=false; // must have same root element
		
		for(int i=0;merge && i<args.length;i++) {
			try {
				Node t=getNodeSet(aDoc, args[i], "item");
				appendChildNodes(common,t);
				
				// throws exception if nodes are not equal
				DOMUtils.compareNodes(t, getNodeSet(bDoc, args[i], "item"), true);
			} catch (Exception e) {
				merge=false;
				e.printStackTrace();
			}
		}
		
		if(merge) {
			for(int i=0;i<args.length;i++) {
				removeElement(args[i],aDoc);
				removeElement(args[i],bDoc);
			}
			
			importNodes(common,aDoc,"item");
			importNodes(common,bDoc,"item");
			
			removeElement("/"+aRootName+"/item",aDoc);
			removeElement("/"+bRootName+"/item",bDoc);
			
			Node aItem=merged.createElement("item");
			Node bItem=merged.createElement("item");
			
			
			appendChildNodes(aItem,aDoc.getDocumentElement());
			appendChildNodes(bItem,bDoc.getDocumentElement());
			
			if(aItem.hasChildNodes())
				common.appendChild(common.getOwnerDocument().importNode(aItem,true));
			if(bItem.hasChildNodes())
				common.appendChild(common.getOwnerDocument().importNode(bItem,true));
			
			DOMUtils.trimEmptyTextNodes(merged);
			
			result=new Document[] {merged};
		}
		else {
			result=new Document[] {aDoc, bDoc};
		}
		
		return result;
	}
	
	private void importNodes(Node common, Document doc, String nodeName) {
		NodeList nl=doc.getDocumentElement().getChildNodes();
		for(int i=0;nl!=null && i<nl.getLength();i++) {
			if(nl.item(i).getNodeName().equals(nodeName))
				common.appendChild(common.getOwnerDocument().importNode(nl.item(i), true));
		}
	}

	private void appendChildNodes(Node item, Node node) {
		NodeList nl=node.getChildNodes();
		for(int j=0;nl!=null && j<nl.getLength(); j++) {
			item.appendChild(item.getOwnerDocument().importNode(nl.item(j), true));
		}
	}

	private static void removeElement(String path, Node doc) {
		if(path.startsWith("/")) path=path.substring(1);
		while(path.indexOf('/') > -1) {
		      Node child = null;
		      String nodeName = path.substring(0, path.indexOf('/'));
		      path = path.substring(path.indexOf('/')+1);
		      
		      child = doc.getFirstChild();
		      
		      while(child!=null) {
		        if (child.getNodeName().equals(nodeName)) {
		          // found
		          doc = child;
		          break;
		        }
		        child = child.getNextSibling();
		      }
		      
		      if (child==null) {
		        return; // not found
		      }
		}
		
		// found
		NodeList childs=doc.getChildNodes();
		for(int i=childs.getLength()-1;i>=0;i--) {
			if(childs.item(i).getNodeName().equals(path)) doc.removeChild(childs.item(i));
		}

	}

	public static Node getNodeSet(Document doc, String path, String newNodeName) {
		Node result=null;
		NodeList nodes=getNodeList(doc,path);
		if(nodes!=null) {
			result=doc.createElement(newNodeName);
			
		    for (int j = 0; j < nodes.getLength(); j++) {
		    	result.appendChild(nodes.item(j).cloneNode(true));
		    }
		}
		return result;
	}
	
	public static NodeList getNodeList(Document doc, String path) {
		Object result=null;
		XPath xpath = XPathFactory.newInstance().newXPath();
	    XPathExpression expr=null;
		try {
			expr = xpath.compile(path);
			result = expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e2) {
			e2.printStackTrace();
		}
		
		return result==null?null:(NodeList)result;
	}
	
	public static String getValue(Document doc, String path) {
		String sep=" - ";
		String value=null;
		Object result=getNodeList(doc,path);
		
		if(result!=null) {
		    NodeList nodes = (NodeList) result;
		    
		    if(nodes.getLength()>0) value=nodes.item(0).getTextContent();
		    for (int j = 1; j < nodes.getLength(); j++) {
		    	value=value+sep+nodes.item(j).getTextContent();;
		    }
		}
	    return value;
	}
	
	  public static Node appendNode(String path, Node source, Node nodeToAppend) throws Exception {
		  if(path.startsWith("/")) path=path.substring(1);
		  
	    while(path.indexOf('/') > -1) {
	      Node child = null;
	      String nodeName = path.substring(0, path.indexOf('/'));
	      path = path.substring(path.indexOf('/')+1);
	      
	      child = source.getFirstChild();
	      
	      while(child!=null) {
	        if (child.getNodeName().equals(nodeName)) {
	          // found
	          source = child;
	          break;
	        }
	        child = child.getNextSibling();
	      }
	      
	      if (child==null) {
	        // not found
	    	  try {
					child = source.getOwnerDocument().createElement(nodeName);
					source.appendChild(child);
					source = child;
	    	  }
	    	  catch(Exception e) {
	    		  e.printStackTrace();
	    	  }
	      }
	    }
	    
	    try {
	    	source.appendChild(source.getOwnerDocument().importNode(nodeToAppend,true));
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
	    
	    return source;
	    
//	    String nodeName;
//	    if (path.indexOf('.') > -1) {
//	      nodeName = path.substring(0, path.indexOf('.'));
//	    }
//	    else {
//	      if (path.length()==0) {
//	        throw new Exception("Cannot set node with empty name");
//	      }
//	      nodeName = path;
//	    }
//	    Node child = null;
//	    if (nodeName.length()>0) {
//	      if (overwrite) {
//	        child = n.getFirstChild();
//	        while(child!=null) {
//	          if (child.getNodeName().equals(nodeName)) {
//	            // found
//	            n = child;
//	            break;
//	          }
//	          child = child.getNextSibling();
//	        }
//	        if (child==null) {
//	          child = merged.createElement(nodeName);
//	          n.appendChild(child);
//	          n = child;
//	        }
//	      }
//	      else {
//	        child = merged.createElement(nodeName);
//	        n.appendChild(child);
//	        n = child;
//	      }
//	    }
//	    
//	    if (path.indexOf('.') > -1) {
//	      if (!(n instanceof Element)) {
//	        throw new Exception("Node "+n.getNodeName()+" should be an element so it can contain attributes");
//	      }
//	      return n;
//	    }
//	    else {
//	      if (overwrite) {
//	        child = n.getFirstChild();
//	        // remove all children
//	        while(child!=null) {
//	          Node temp = child.getNextSibling();
//	          n.removeChild(child);
//	          child = temp;
//	        }
//	        return n;
//	      }
//	      else {
//	        return child;
//	      }
//	    }
	  }

}
