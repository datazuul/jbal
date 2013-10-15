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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.SAXException;

import JSites.authentication.Permission;

public class PageCreator extends MyAbstractPageGenerator {
	
	protected void subClassProcess(String componentType, long id, AttributesImpl attrCid, boolean hasChildren, Connection conn) throws SAXException, SQLException {
		if(componentType.equals("content") && !containerType.equals("content")) { //shift content on footer, header, ...
    		componentType=containerType;
    	}
		
		if(permission.hasPermission(Permission.EDITABLE)){
		
			if(componentType.equals("sidebar")){
				attrCid.addCDATAAttribute("pid",String.valueOf(pageId));
				attrCid.addCDATAAttribute("newpage","true");
			}
			else if(componentType.equals("content")) {
				attrCid.clear();
				attrCid.addCDATAAttribute("cid","0");
				attrCid.addCDATAAttribute("accessible",String.valueOf(permission.hasPermission(Permission.ACCESSIBLE)));
				attrCid.addCDATAAttribute("editable",String.valueOf(permission.hasPermission(Permission.EDITABLE)));
				attrCid.addCDATAAttribute("validable",String.valueOf(permission.hasPermission(Permission.VALIDABLE)));
				attrCid.addCDATAAttribute("sfa",String.valueOf(permission.hasPermission(Permission.SFA)));
				hasChildren = false;
			}
			attrCid = doColor(id, attrCid);
			contentHandler.startElement("",componentType,componentType,attrCid);
			
			if(hasChildren){
				Statement st = conn.createStatement();
		    	ResultSet rs = st.executeQuery("select CID from tblcontenuti where PaCID=" + id +"  order by OrderNumber"); // ho tolto and IDStato=3
				while(rs.next()){
					long childId = rs.getLong(1);
					processChild(childId);
				}
				rs.close();
				st.close();
			}
			
			if(componentType.equals("content"))
				newComponent(request.getParameter("type"));	
			
			contentHandler.endElement("",componentType,componentType);
		}
		else
			super.subClassProcess(componentType, id, attrCid, hasChildren);
	}
	
	private void newComponent(String newComponentType) throws SAXException, SQLException{
    	
    	AttributesImpl editing_attrs = new AttributesImpl();
    	editing_attrs.addCDATAAttribute("cid",String.valueOf(0));
    	editing_attrs.addCDATAAttribute("pacid",String.valueOf(0));
    	editing_attrs.addCDATAAttribute("papid",String.valueOf(pageId));
    	editing_attrs.addCDATAAttribute("type",newComponentType);
    	editing_attrs.addCDATAAttribute("order","1");
    	
    	contentHandler.startElement("","editing","editing",editing_attrs);
    	contentHandler.endElement("","editing","editing");
    }

}
