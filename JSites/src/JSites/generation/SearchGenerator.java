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

import java.io.IOException;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.jopac2.engine.NewSearch.DoSearchNew;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.iso2709.Eutmarc;
import org.xml.sax.SAXException;

public class SearchGenerator extends MyAbstractPageGenerator {
	
	private String catalogQuery = "";
	private DoSearchNew doSearchNew = null;
	private Connection conn;
	private String catalog;
	
	@Override
	@SuppressWarnings("rawtypes")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws IOException, ProcessingException, SAXException {
		
		super.setup(resolver, objectModel, src, par);
		
		try {
			catalog = par.getParameter("catalog");
			conn = this.getConnection(dbname);
		} catch (ParameterException e) { }
		
	}

	
	public void generate() throws SAXException {
		
		contentHandler.startDocument();
		contentHandler.startElement("", "content", "content", this.emptyAttrs);
		

		contentHandler.startElement("", "record", "record", emptyAttrs);
		
		Long t = Long.parseLong(request.getParameter("JID"));
		
		RecordInterface ri = DbGateway.getNotiziaByJID(conn,catalog,t);
		
		String ts = String.valueOf(t);
		throwField("ID", ts);
		
		String title = ri.getTitle();
		throwField("title", title);
		
		if(ri instanceof Eutmarc){
			Eutmarc rieut = ((Eutmarc)ri);
			throwField("prezzo", rieut.getAvailabilityAndOrPrice());
			try {
			if(rieut.getImage() != null)
				throwField("image", "images/pubimages/eut"+t+".jpg");
			else
				throwField("image", "images/pubimages/NS.jpg");
			}
			catch(Exception e) {};
			
		}
		

		contentHandler.startElement("", "authors", "authors", emptyAttrs);
		Vector<String> auths = ri.getAuthors();
		Iterator<String> autiter = auths.iterator();
		while(autiter.hasNext()){
			String author = autiter.next();
			author = author.replaceAll(" ,", ",");
			throwField("author", author);
		}
		contentHandler.endElement("", "authors", "authors");

		
		String bid = ri.getBid();
		throwField("bid", bid);
		
		String pubdate = ri.getPublicationDate();
		throwField("pubdate", pubdate);
		
		String pubplace = ri.getPublicationPlace();
		throwField("pubplace", pubplace);
		
		String abs = ri.getAbstract();
		throwField("abstract", abs);
		
		String isbn = ri.getStandardNumber();
		throwField("isbn", isbn);
		
		contentHandler.endElement("", "record", "record");	
			

		contentHandler.endElement("", "content", "content");
		contentHandler.endDocument();
	}
	
	
	private void throwField(String name, String value) throws SAXException{
		if(value != null && value.length()>0){
			contentHandler.startElement("", name, name, emptyAttrs);
			contentHandler.characters(value.toCharArray(), 0, value.length());
			contentHandler.endElement("", name, name);
		}
	}
	
	
	
	

}
