package JSites.transformation.catalogSearch;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.ArrayUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Group implements TextFunction {

	public String parse(Document doc, String in, String... args) throws Exception {
			groupPair(doc, args[0],args[1],(String[]) ArrayUtils.subarray(args, 2,args.length));
			String newContext=args[2];
			newContext=newContext.substring(0,newContext.lastIndexOf("/")+1)+args[1];
			return Templator.parseContext(doc,"{{"+newContext+":"+in+"}}");
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
}
