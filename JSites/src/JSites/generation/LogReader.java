package JSites.generation;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2009 JOpac2 project
*
*     This file is part of JOpac2. http://www.jopac2.org
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
*  Please, see NOTICE.txt AND LEGAL directory for more info. Different licences
*  may apply for components included in JOpac2.
*
*******************************************************************************/

import java.io.BufferedReader;
import java.io.FileReader;
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
