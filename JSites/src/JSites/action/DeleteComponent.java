package JSites.action;

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
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;

import JSites.authentication.Authentication;
import JSites.utils.DBGateway;

public class DeleteComponent extends PageAction {
	
	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		
		super.act(redirector, resolver, objectModel, source, parameters);
		if(parameters.getParameter("containerType").equals("content")){
			Request request=ObjectModelHelper.getRequest(objectModel);
			Session session=request.getSession(true);
			
			String username=Authentication.getUsername(session);
			String remoteAddr=request.getRemoteAddr();
			
			Connection conn = null;
			try{
				conn = this.getConnection(dbname);
				if(cid!=0)DBGateway.deleteComponent(cid, username, remoteAddr, conn);
			}catch(Exception e){e.printStackTrace();}
			
			try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}
		}
		return objectModel;
	}


}
