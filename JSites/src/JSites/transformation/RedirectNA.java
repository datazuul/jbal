package JSites.transformation;

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
import java.util.*;

import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.AttributesImpl;
import org.jopac2.utils.JOpac2Exception;
import org.xml.sax.SAXException;

import JSites.utils.DBGateway;

import JSites.authentication.Authentication;
import JSites.authentication.Permission;

public class RedirectNA extends MyAbstractPageTransformer {
	
	private boolean redirect = true;
	private boolean isView = false;
	private Permission permission;
	private String redirURL = "pageview?pid=1";
	
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		 super.setup(arg0, arg1, arg2, arg3);
		 redirect = true;
		 String q = request.getRequestURI();
		 if(q.contains("view"))
			 isView = true;
		 try{
			 long pageId=1;
			 String pCode = request.getParameter("pcode");
			 String stringPid = request.getParameter("pid");
			 if(stringPid != null && !(stringPid.equals("0"))){
				 if(stringPid.matches("\\d+"))
					 pageId = Long.parseLong(stringPid);
				 else {
					 System.out.println("Ooouuu, i ga richiesto >> pid="+stringPid + " << ... ma semo mati?");
				 }
			 }
			 else if(request.getParameter("papid")!=null && (permission.hasPermission(Permission.ACCESSIBLE)||permission.hasPermission(Permission.EDITABLE))){
				 redirect = false;
			 }
			 			
			 try {
				if (pCode != null)
					pageId = DBGateway.getPidFrom(datasourceComponent, pCode);
					if(pageId == -1) pageId=1;

//				Session session = this.sessionManager.getSession(true);
				permission = Authentication.assignPermissions(datasourceComponent, session, request.getRemoteAddr(), pageId);
//				try {
//					permission = Authentication.assignPermissions(session, pageId, conn);
//				} catch (JOpac2Exception e) {
//					e.printStackTrace();
//				}
				String readRedir = DBGateway.getRedirectURL(datasourceComponent, pageId);
				if (readRedir != null && isView
						&& permission.hasPermission(Permission.ACCESSIBLE)) {
					redirURL = readRedir;
					redirect = true;

					if (permission.hasPermission(Permission.EDITABLE)
							|| permission.hasPermission(Permission.VALIDABLE)) {
					} else {
						return;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
//				if (conn != null && !e.getMessage().contains("User/Username exception")) {
//					try {
//						DBGateway.caricaDB(conn);
//					}
//					catch(Exception ee) {
//						ee.printStackTrace();
//					}
//					finally {
//						conn.close();
//					}
//				}
//				e.printStackTrace();
//			}
//
//			try {
//				if (conn != null)
//					conn.close();
//			} catch (SQLException e) {
//				System.out.println("Non ho potuto chiudere la connessione");
			}	 
			 
		 }
		 catch(Exception e){e.printStackTrace();}
		 if(permission==null)permission=new Permission();
		 if(permission.hasPermission(Permission.ACCESSIBLE)){
			 redirect = false;
		 }
		 
		 if(!redirect){
			 try{
				 String uri = request.getParameter("action");
				 if(uri!=null){
					 if(uri.contains("save")){
						 String pid = request.getParameter("pid");
						 if(pid==null)
							 pid = request.getParameter("papid");
						 if(pid!=null){
							 redirURL = "pageview?pid="+pid;
							 redirect = true;
						 }
					 }
				 }
			 }
			 catch(Exception e){e.printStackTrace();}
		 }

	}
	
	
	public void endDocument() throws SAXException{
		if(redirect){
			AttributesImpl attr=new AttributesImpl();
			//attr.addCDATAAttribute("internal","true");
			super.startElement("","redirect","redirect",attr);
			super.characters(redirURL.toCharArray(), 0, redirURL.length());
			super.endElement("","redirect","redirect");
		}
		super.endDocument();
	}
	
}
