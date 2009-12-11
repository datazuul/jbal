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

import java.sql.SQLException;

import java.util.Enumeration;

import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.SAXException;

import JSites.authentication.Permission;

import JSites.utils.DBGateway;


public class PageEditor extends MyAbstractPageGenerator {
	
	private long editedcid = 0;
	private long pacid4new = 0;
	private String type4new = "";
	private long order = -1;
	boolean notYetDone = true;
	
	protected void init(Connection conn){
		editedcid = 0;
		pacid4new = 0;
		type4new = "";  
		order = -1;
		notYetDone = true;
		
		String tempCid = request.getParameter("cid");
		if (tempCid != null && (!(tempCid.equals("0")))) editedcid = Long.parseLong(tempCid);
		else {
			String spacid4new = request.getParameter("pacid");
			try{
				pacid4new = Long.parseLong(spacid4new);
			}catch(Exception e){
				System.out.println("Parsing fault. Parameter 'pacid4new' was: " + spacid4new);
				System.out.println("The request was: " + request.getQueryString());
			}
			
			type4new = request.getParameter("type");
			
			String sorder = request.getParameter("order");
			try{
				order = Long.parseLong(sorder);
			}catch(Exception e){
				System.out.println("Parsing fault. Parameter 'order' was: " + sorder);
				System.out.println("The request was: " + request.getQueryString());
			}
		}
		
	}

	protected void subClassProcess(String componentType, long id, AttributesImpl attrCid, boolean hasChildren, Connection conn) throws SAXException, SQLException{
	
		if(containerType.equals("header") || containerType.equals("footer") || containerType.equals("navbar")){
			super.subClassProcess(componentType, id, attrCid, hasChildren, conn);
			return;
		}
		if(permission.hasPermission(Permission.EDITABLE)){
		
			attrCid = doColor(id, attrCid,conn);
			if(componentType.equals("content")){
				attrCid.addCDATAAttribute("accessible",String.valueOf(permission.hasPermission(Permission.ACCESSIBLE)));
				attrCid.addCDATAAttribute("editable",String.valueOf(permission.hasPermission(Permission.EDITABLE)));
				attrCid.addCDATAAttribute("validable",String.valueOf(permission.hasPermission(Permission.VALIDABLE)));
				attrCid.addCDATAAttribute("sfa",String.valueOf(permission.hasPermission(Permission.SFA)));
			}
			
			contentHandler.startElement("",componentType,componentType,attrCid);
			
				if(pacid4new!=0 && pacid4new == id && notYetDone)
		    		newChild(order);
				else
					editChild(editedcid,id,conn);

			contentHandler.endElement("",componentType,componentType);	
		}
		else
			super.subClassProcess(componentType, id, attrCid, hasChildren, conn);
			
	}
    
	@SuppressWarnings("unchecked")
	private void editChild(long cid, long pacid, Connection conn) throws SAXException, SQLException{

    	String type = DBGateway.getTypeForCid(cid,conn);
    	if(type==null)return;
    	AttributesImpl editing_attrs = new AttributesImpl();
    	
    	Enumeration<String> eee = request.getParameterNames();
    	
    	while(eee.hasMoreElements()){
    		String name = (String)eee.nextElement();
    		String value = request.getParameter(name);
    		editing_attrs.addCDATAAttribute(name,value);
    	}
    	
    	editing_attrs.addCDATAAttribute("pacid",String.valueOf(pacid));
    	if(editing_attrs.getValue("order") == null){
    		editing_attrs.addCDATAAttribute("order",String.valueOf(order));
    	}
    	editing_attrs.addCDATAAttribute("type",type);
    	
    	editing_attrs = doColor(cid,editing_attrs, conn);
    	
    	contentHandler.startElement("","editing","editing",editing_attrs);
    	contentHandler.endElement("","editing","editing");
    }
    
	private void newChild(long o) throws SAXException, SQLException{
    	
    	AttributesImpl editing_attrs = new AttributesImpl();
    	editing_attrs.addCDATAAttribute("cid","0");
    	editing_attrs.addCDATAAttribute("pacid",String.valueOf(pacid4new));
    	editing_attrs.addCDATAAttribute("type",type4new);
    	editing_attrs.addCDATAAttribute("order",String.valueOf(o));
    	
    	contentHandler.startElement("","editing","editing",editing_attrs);
    	contentHandler.endElement("","editing","editing");
    	notYetDone = false;
    }
    
	

}