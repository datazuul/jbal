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
import java.sql.*;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.SAXException;

import JSites.authentication.Permission;


public class FullListingGenerator extends MyAbstractPageGenerator {

	public void generate() throws SAXException {
			
		contentHandler.startDocument();
		
		Connection conn = null;
		try{
			conn = this.getConnection(dbname);
			Statement st = conn.createStatement();
			String query = "select PID, name from tblpagine where PaPID="+pageId + " and PID!=14 and Valid=1 and InSidebar = 1 order by Name";
			if(permission.hasPermission(Permission.VALIDABLE))
				query = "select PID, Name from tblpagine where PaPID="+pageId + " and PID!=14 order by Name";
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				long cpid = rs.getLong("PID");
				String src = "cocoon://components/listing/view?pid=" + cpid;
				
				AttributesImpl attrs = new AttributesImpl();
				attrs.addCDATAAttribute("src", src);
	
				contentHandler.startElement("http://apache.org/cocoon/include/1.0", "include", "include", attrs);
				contentHandler.endElement("http://apache.org/cocoon/include/1.0", "include", "include");
			}
			rs.close();
			st.close();
		} catch(Exception e) {e.printStackTrace();}
		
		try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}
		
		contentHandler.endDocument();
	}

	@SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException{
		super.setup(resolver, objectModel,src,par);
		containerType="listing";
	}

	
}
