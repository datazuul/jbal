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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.apache.avalon.framework.component.ComponentException;
import org.apache.cocoon.xml.AttributesImpl;
import org.jopac2.utils.Utils;
import org.xml.sax.SAXException;

import JSites.authentication.Authentication;
import JSites.authentication.Permission;

import JSites.utils.DBGateway;

public class PageSaver extends MyAbstractPageGenerator{
	
	private long save_cid = 0;
	private long newcid = 0;
	private String save_componentType = "";
	private long save_pacid = 0;
	private long statecode = 0;
	private long order = 0;

	public void processChild(long id) throws SAXException, SQLException {
		
// resumed		
		if(! (containerType.equals("content"))){
			super.processChild(id);
			return;
		}
		
		if( save_pacid == id ){					//se il contenitore e' quello giusto	
			DBGateway.setHasChild(datasourceComponent,id);
			boolean isActive = false;
			try { 
				isActive = DBGateway.getState(datasourceComponent,newcid)==3;
				if(newcid==0 || isActive){		//se il componente deve essere validato (stato PND)							
					//o se il componente e' una new entry	
					newcid = DBGateway.getNewFreeCid(datasourceComponent);		//cerca un nuovo CID nel DB
					DBGateway.saveDBComponentAndRelationship(datasourceComponent,newcid, save_componentType, 0, save_pacid, save_cid, 
							new java.util.Date(), statecode, pageId, request.getParameter("url"), order, 
							Authentication.getUsername(session), request.getRemoteAddr());
				}
				else if (statecode==2){	DBGateway.setPending(datasourceComponent,newcid); }
				else if (statecode==1){	DBGateway.setWorking(datasourceComponent,newcid); }
			} catch (Exception e) {e.printStackTrace();}
			
			super.processChild(id);
		}
		
	}

	public void saveChild(long cid, long pacid) throws SAXException, SQLException {
			
		String type = DBGateway.getTypeForCid(datasourceComponent,cid);

		AttributesImpl saving_attrs = new AttributesImpl();
		saving_attrs.addCDATAAttribute("cid",String.valueOf(cid));
		saving_attrs.addCDATAAttribute("pacid",String.valueOf(pacid));
		saving_attrs.addCDATAAttribute("pid",String.valueOf(pageId));
		
		order = DBGateway.getOrderNumber(datasourceComponent,cid);
		
		saving_attrs.addCDATAAttribute("order",String.valueOf(order));
		saving_attrs.addCDATAAttribute("type",type);
		
		saving_attrs = doColor(cid,saving_attrs);
		
		contentHandler.startElement("","saving","saving",saving_attrs);
		contentHandler.endElement("","saving","saving");

	}

	protected void init(){
		
		super.init();
		try {
			//-----------LETTURA PARAMETRI E ATTRIBUTI
			save_componentType = request.getParameter("type");

			newcid = save_cid = Long.parseLong(request.getParameter("cid"));
			save_pacid = getPacid();

			if(request.getParameter("order") != null)
			try{ order = Long.parseLong(request.getParameter("order"));} catch(Exception e){e.printStackTrace();}
			try{ statecode = DBGateway.getStateCode(datasourceComponent,request.getParameter("state"));} catch(Exception e){e.printStackTrace();}
		}
		catch (Exception e2) {	e2.printStackTrace(); }

	}

	protected void subClassProcess(String componentType, long id, AttributesImpl attrCid, boolean hasChildren) throws SAXException, SQLException {
// resumed
		if(! (containerType.equals("content"))){
			super.subClassProcess(componentType, id, attrCid, hasChildren);
			return;
		}
		
		attrCid = doColor(id, attrCid);
		attrCid.addCDATAAttribute("accessible",String.valueOf(permission.hasPermission(Permission.ACCESSIBLE)));
		attrCid.addCDATAAttribute("editable",String.valueOf(permission.hasPermission(Permission.EDITABLE)));
		attrCid.addCDATAAttribute("validable",String.valueOf(permission.hasPermission(Permission.VALIDABLE)));
		attrCid.addCDATAAttribute("sfa",String.valueOf(permission.hasPermission(Permission.SFA)));
		
		contentHandler.startElement("",componentType,componentType,attrCid);
		
		if(hasChildren){
			Connection conn=null;
			Statement st=null;
			ResultSet rs=null;
			
			try {
				conn=datasourceComponent.getConnection();
				st = conn.createStatement();
		    	rs = st.executeQuery("select CID from tblcontenuti where PaCID=" + id +" and StateID<4 order by OrderNumber"); // ho tolto and IDStato=3
				while(rs.next()){
					long childId = rs.getLong(1);
	// resumer outer if
					if(this.containerType.equals("content")){
						if(childId==newcid) saveChild(childId,id);
					}
					else {
						processChild(childId);
					}
	
				}
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			finally {
				if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPermission resultset exception " + fe.getMessage());}
				if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPermission statement exception " + fe.getMessage());}
				if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPermission connection exception " + fe.getMessage());}
		
			}
		}
		contentHandler.endElement("",componentType,componentType);
		
	}
	
	

	private long getPacid() throws SQLException, ComponentException {
		
		long ret = 0;
		String pc=request.getParameter("pacid");
		if(pc!=null && !pc.equals("0")) {
			ret=Long.parseLong(pc);
		}
		else {
			ret=DBGateway.getContentCID(datasourceComponent,pageId);
		}
		
		if(ret == 0){
			Long l = (Long) session.getAttribute("newPacid");
			if(l!=null) ret = l.longValue();
		}
		if(ret==0){
			ret = DBGateway.getNewFreeCid(datasourceComponent);
			session.setAttribute("newPacid",new Long(ret));
			DBGateway.saveDBComponent(datasourceComponent,ret,"content",1,0,new Date(), pageId, request.getParameter("url"), 
					Authentication.getUsername(session), request.getRemoteAddr());
			DBGateway.linkPageContainers(datasourceComponent,ret, pageId);
		}
		return ret;
	}
}
