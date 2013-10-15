/*
 * Created on 19-set-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.cocoon.xml.AttributesImpl;
import org.jopac2.utils.Utils;
import org.xml.sax.SAXException;

import JSites.authentication.Permission;
import JSites.utils.DBGateway;

/**
 * @deprecated
 * @author romano
 *
 */
public class PageOrderer extends MyAbstractPageGenerator{
	
	private long orderedPacid = 0; //inteso come id del contenitore
	//private String childType = ""; //tipo del componente corrente
	
	public void orderChild(long id) throws SQLException, SAXException{

		Connection conn=null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("select * from tblcomponenti where CID='"+id+"'");
			if(!rs.next()){
				rs.close();
				st.close();
				return;
			}
			String componentType = rs.getString("Type");
			String attributes = rs.getString("Attributes");
			boolean hasChildren = rs.getBoolean("HasChildren");
			
			AttributesImpl attrCid = new AttributesImpl();
			attrCid.addCDATAAttribute("cid",String.valueOf(id));
			attrCid.addCDATAAttribute("type",componentType);
			if(componentType.equals("content")){
				attrCid.addCDATAAttribute("accessible",String.valueOf(permission.hasPermission(Permission.ACCESSIBLE)));
				attrCid.addCDATAAttribute("editable",String.valueOf(permission.hasPermission(Permission.EDITABLE)));
				attrCid.addCDATAAttribute("validable",String.valueOf(permission.hasPermission(Permission.VALIDABLE)));
				attrCid.addCDATAAttribute("sfa",String.valueOf(permission.hasPermission(Permission.SFA)));
			}
			if(attributes!=null){
				attrCid.addCDATAAttribute("data",attributes);
			}
			
			subClassProcess("ordering", id, attrCid, hasChildren);
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - PageOrderer.orderChild resultset exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - PageOrderer.orderChild statement exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - PageOrderer.orderChild connection exception " + fe.getMessage());}
		}
		


	}

	protected void init(){
		super.init();
		try{
//			String temp = request.getParameter("pacid");
//			orderedPacid = Long.parseLong(temp);
			
			String temp = request.getParameter("pid");
			orderedPacid = DBGateway.getContentCID(datasourceComponent,Long.parseLong(temp));
			
			
		}
		catch(Exception e){
//			System.out.println("The QueryString was: " + request.getQueryString());
			System.out.println("Class: "+this.getClass().getCanonicalName());
			e.printStackTrace();
		}
	}

	protected void subClassProcess(String componentType, long id, AttributesImpl attrCid, boolean hasChildren) throws SAXException, SQLException {
		
		boolean ordering = false;
		if(id==orderedPacid){
			ordering = true;
			componentType = componentType+"2Order";
		}
		attrCid = doColor(id, attrCid);
		if(componentType.equals("content")){
			attrCid.addCDATAAttribute("accessible",String.valueOf(permission.hasPermission(Permission.ACCESSIBLE)));
			attrCid.addCDATAAttribute("editable",String.valueOf(permission.hasPermission(Permission.EDITABLE)));
			attrCid.addCDATAAttribute("validable",String.valueOf(permission.hasPermission(Permission.VALIDABLE)));
		}
		contentHandler.startElement("",componentType,componentType,attrCid);

		if(hasChildren){
			Connection conn=null;
			PreparedStatement st=null;
			ResultSet rs=null;
			
			try {
				conn=datasourceComponent.getConnection();
				st = conn.prepareStatement("select CID from tblcontenuti where StateID=3 and PaCID=? order by OrderNumber");
				st.setLong(1, id);
				rs = st.executeQuery(); // ho tolto and IDStato=3
				while(rs.next()){
					long childId = rs.getLong(1);
			
					if(ordering)
						orderChild(childId);
					else
						processChild(childId);
				}
			}
			catch(SQLException e) {
				
			}
			finally {
				if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - PageOrderer.subClassProcess resultset exception " + fe.getMessage());}
				if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - PageOrderer.subClassProcess statement exception " + fe.getMessage());}
				if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - PageOrderer.subClassProcess connection exception " + fe.getMessage());}
			}
		}

		contentHandler.endElement("",componentType,componentType);

	}
	
    protected AttributesImpl subClassSetAttributes(AttributesImpl attrCid) {
    	long cid = Long.parseLong(attrCid.getValue("cid"));
    	if(orderedPacid==cid)attrCid.addCDATAAttribute("ordering","true");
		return attrCid;
	}
    
	
}
