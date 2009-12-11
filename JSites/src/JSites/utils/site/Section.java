package JSites.utils.site;
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
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import JSites.utils.HtmlCodec;

public class Section extends Component {
	
	public String titolo = "Titolo";
	public String img = "images/contentimg/no_image";
	public String testo = "";
	
	public String XMLSerialize(boolean isFirst){
		titolo = titolo.substring(0,1).toUpperCase() + titolo.substring(1);

		String ret = "<?xml version=\"1.0\" encoding=\"utf-8\"?><section>" + 
					 "<titolo>"+HtmlCodec.encode(titolo)+"</titolo>" +
					 "<img>"+HtmlCodec.encode(img)+"</img>"+
					 "<testo>"+HtmlCodec.encode(testo).replaceAll("&gt;",">").replaceAll("&amp;","&")+"</testo>"+
					 "</section>";
		if(isFirst)
			ret = ret.replaceAll("<titolo>", "<titolo type=\"1\">");
		else
			ret = ret.replaceAll("<titolo>", "<titolo type=\"2\">");
		return ret;
		
	}
	
	public String XMLSerialize(){
		return XMLSerialize(false);
	}

	public void ThrowSax(ContentHandler handler) throws SAXException {
		
		AttributesImpl attrs = new AttributesImpl();
		attrs.addCDATAAttribute("time", "presente");
		handler.startElement("","section","section",attrs);

		handler.startElement("","testo","testo",attrs);
		handler.characters(testo.toCharArray(), 0, testo.length());
		handler.endElement("","testo","testo");
		
		handler.startElement("","titolo","titolo",attrs);
		handler.characters(titolo.toCharArray(), 0, titolo.length());
		handler.endElement("","titolo","titolo");
		
		handler.startElement("","img","img",attrs);
		handler.characters(img.toCharArray(), 0, img.length());
		handler.endElement("","img","img");

		handler.endElement("","section","section");
		
	}
	
	public static String listChilds(TreeSet<String> treelist){
		
		String list = "";
		Iterator<String> iter = treelist.iterator();
		while(iter.hasNext()){
			String[] name_id = ((String)iter.next()).split(" #");
			list = list + "[pageview?pid=" + name_id[1] + ">" + 
			name_id[0] + "], ";
		}
		
		list = list.trim();
		if(list.length()>1)
			list = list.substring(0,list.length()-1);
		
		return list;
	}

}
