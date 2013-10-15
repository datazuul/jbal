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

public class ForEach implements TextFunction {

	/**
	 * Example:
	 * {{^ForEach(/record/item):<a href='[[primo]]'>[[secondo]]</a>}}
	 * @param doc
	 * @param in = <a href='[[primo]]'>[[secondo]]</a>
	 * @param args = '/record/item'
	 * @return
	 * @throws Exception
	 */
	public String parse(Document doc, String in, boolean escape, String... args) throws Exception {
			String r="";
			
			Object result=null;
			XPath xpath = XPathFactory.newInstance().newXPath();
		    XPathExpression expr=null;
			try {
				expr = xpath.compile(args[0]);
				result = expr.evaluate(doc, XPathConstants.NODESET);
			} catch (XPathExpressionException e2) {
				e2.printStackTrace();
			}
			

		    NodeList nodes = (NodeList) result;

		    for (int j = 1; j <= nodes.getLength(); j++) {
//		    	Node parent=nodes.item(j).getParentNode(); // questo e' lo specifico item
		    	r=r+Templator.parseContext(doc,"{{"+args[0]+"["+j+"]"+":"+in+"}}",escape); // + qualche cosa che è la valutazione dell'input su quel nodo
		    }
			
			return r;
	}
	
}
