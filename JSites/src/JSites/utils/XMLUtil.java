package JSites.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import JSites.transformation.catalogSearch.Record2display;

public class XMLUtil {
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
	
	public static Document createDocument() throws ParserConfigurationException  {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.newDocument();
	}

	public static void appendNode(Document document, Node node, String name,
			String value) {
	    	Node e=document.createElement(name);
			e.setTextContent(value);
			node.appendChild(e);
	}
}
