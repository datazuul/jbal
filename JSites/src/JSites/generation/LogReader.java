package JSites.generation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.xml.sax.SAXException;



public class LogReader extends MyAbstractPageGenerator {

	public void generate() throws SAXException {
		
		contentHandler.startDocument();
		contentHandler.startElement("", "content", "content", this.emptyAttrs);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(_source));
			String line;
			String prevLine="";
			while ((line = reader.readLine()) != null)
			{
				if(different(line,prevLine)) {
					sendCharacters(prevLine+"\n");
				}
				prevLine=line;
			}
			sendCharacters(prevLine+"\n");
			reader.close();
		} catch (Exception e) {
			sendCharacters(e.getMessage()+"\n");
			e.printStackTrace();
		}

		contentHandler.endElement("", "content", "content");
		contentHandler.endDocument();
	}
	
	
	private boolean different(String line, String prevLine) {
		boolean r=false;
		if(line.startsWith("WARN:") || line.startsWith("time")) {
			r=true;
		}
		else {
			int i=line.indexOf(":");
			if(i>=0) {
				if(prevLine.length()<i) r=true;
				else if(line.substring(0,i).equals(prevLine.substring(0,i))) r=false;
				else r=true;
			}
			else {
				r=true;
			}
		}
		return r;
	}


	private void sendCharacters(String message) throws SAXException {
		contentHandler.characters(message.toCharArray(), 0, message.length());
	}


	@SuppressWarnings("unused")
	private void throwField(String name, String value) throws SAXException{
		if(value != null && value.length()>0){
			contentHandler.startElement("", name, name, emptyAttrs);
			contentHandler.characters(value.toCharArray(), 0, value.length());
			contentHandler.endElement("", name, name);
		}
	}
	
	
	
	

}
