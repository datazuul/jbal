package org.jopac2.viewer;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.*;

public class XmlMarcViewer {

	/**
	 * Get a XMLMarc stream and build Marc record
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory spf=SAXParserFactory.newInstance();
		SAXParser sp=spf.newSAXParser();
		sp.parse("/Java_src/java_jopac2/JOpac2/iso-test/eutmarc/eut.xml", new XmlMarcHandler("collection","record"));
	}

}
