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
import java.sql.SQLException;
import java.util.Map;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.xml.sax.SAXException;

import JSites.utils.DBGateway;

public class BricioleGenerator extends MyAbstractPageGenerator {
	
	int startLevel = 2;

	public void generate() throws SAXException {
		
		contentHandler.startDocument();
		try{
			String nome = DBGateway.getPageName(datasourceComponent,this.pageId);
			long papid = DBGateway.getPapid(datasourceComponent,pageId);
			int pageLevel = DBGateway.getPageLevel(datasourceComponent,pageId, 0)+1;
			if( pageLevel >= startLevel){
				contentHandler.startElement("","briciole","briciole", emptyAttrs);
				if (papid >= startLevel) 
//					nome = DBGateway.getPageName(papid, conn);
					nome = getParent(papid) + " > " + nome;
				contentHandler.characters(nome.toCharArray(), 0, nome.length());
				contentHandler.endElement("","briciole","briciole");
			}
		} catch(Exception e) {e.printStackTrace();}
				
		contentHandler.endDocument();
		
	}

	private String getParent(long pid) throws SQLException {
		
		String ret = "<a href=\"pageview?pid="+pid+"\">"+DBGateway.getPageName(datasourceComponent,pid)+"</a>";
		long papid = DBGateway.getPapid(datasourceComponent,pid);
		if(papid >= startLevel){
			ret = getParent(papid)  + " > " + ret;
		}
		
		return ret;
		
	}

	@SuppressWarnings("rawtypes")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
		super.setup(resolver, objectModel, src, par);
		try {
			String startLevelString = null;
			startLevelString = par.getParameter("startLevel");
			if(startLevelString != null)
				startLevel = Integer.parseInt(startLevelString);
		} catch (ParameterException e) {}
		
	}

	
}
