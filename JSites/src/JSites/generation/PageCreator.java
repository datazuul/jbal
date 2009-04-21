package JSites.generation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.SAXException;

import JSites.authentication.Permission;

public class PageCreator extends MyAbstractPageGenerator {
	
	protected void subClassProcess(String componentType, long id, AttributesImpl attrCid, boolean hasChildren, Connection conn) throws SAXException, SQLException {
		
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
				hasChildren = false;
			}
			attrCid = doColor(id, attrCid, conn);
			contentHandler.startElement("",componentType,componentType,attrCid);
			
			if(hasChildren){
				Statement st = conn.createStatement();
		    	ResultSet rs = st.executeQuery("select CID from tblcontenuti where PaCID='" + id +"'  order by OrderNumber"); // ho tolto and IDStato=3
				while(rs.next()){
					long childId = rs.getLong(1);
					processChild(childId, conn);
				}
				rs.close();
				st.close();
			}
			
			if(componentType.equals("content"))
				newComponent(request.getParameter("type"));	
			
			contentHandler.endElement("",componentType,componentType);
		}
		else
			super.subClassProcess(componentType, id, attrCid, hasChildren, conn);
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
