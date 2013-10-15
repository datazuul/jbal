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

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

import JSites.authentication.Authentication;
import JSites.authentication.Permission;
import JSites.utils.DBGateway;

/*
 * Aggiunge un parametrinoinoino che indica il Permission Code dell'utente relativamente alla pgina
 */

public class AuthorizationAction extends PageAction  {
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		
		super.act(redirector, resolver, objectModel, source, parameters);
		if(dbname==null)
			return objectModel;
		DataSourceComponent datasourceComponent=null;
		try {
			datasourceComponent=((DataSourceComponent)dbselector.select(dbname));
			long pageId;
			String pcode = request.getParameter("pcode");
			if(pcode != null) {
				pid = DBGateway.getPidFrom(datasourceComponent,pcode);
			}
			
			if(pid == -1){
				if(parameters.isParameter("pid"))
					pid = Long.parseLong(parameters.getParameter("pid"));
				else pid=1;
			}
			pageId = pid;
		
			
			String permissionCode = "";
			
			Permission permission = Authentication.assignPermissions(datasourceComponent,session, request.getRemoteAddr(), pageId);
			
			permissionCode = String.valueOf((int)permission.getPermissionCode());
			
			objectModel.put("permissionCode", permissionCode);
		} catch(Exception e) {}
		finally {
			if(datasourceComponent!=null) this.manager.release(datasourceComponent);
		}
		
		
		return objectModel;
	}
	
	
	public void compose(ComponentManager manager) throws ComponentException {
		super.compose(manager);
    }
	
}
