package JSites.utils;

import java.util.Date;

import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.SAXException;

public class TimeGenerator extends AbstractGenerator{
	
	AttributesImpl empty = new AttributesImpl();
	public void generate() throws SAXException {
		
		
		contentHandler.startDocument();
		contentHandler.startElement("","html","html", empty);
		contentHandler.startElement("","body","body", empty);
		contentHandler.startElement("","h1","h1", empty);
		contentHandler.startElement("","time","time", empty);
		
		String time = (new Date()).toString();
		contentHandler.characters(time.toCharArray(), 0, time.length());
		
		contentHandler.endElement("","time","time");
		contentHandler.endElement("","h1","h1");
		contentHandler.endElement("","body","body");
		contentHandler.endElement("","html","html");
		contentHandler.endDocument();
		
		
	}
		
}
