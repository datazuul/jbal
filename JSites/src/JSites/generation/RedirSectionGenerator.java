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

import org.xml.sax.SAXException;
import JSites.utils.DBGateway;
import JSites.utils.site.Section;

public class RedirSectionGenerator extends MyAbstractPageGenerator {

	public void generate() throws SAXException {
		
		Section s = new Section();
		
		contentHandler.startDocument();

		try{
			s.testo = DBGateway.getRedirectURL(datasourceComponent,pageId);
			s.titolo = "Redirect";
			s.img = "images/contentimg/gk.jpg";
			s.ThrowSax(contentHandler);
		}catch(Exception e) {e.printStackTrace();}
		
		
		contentHandler.endDocument();
		
	}	
}
