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
import java.util.Enumeration;
import java.util.Map;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

import JSites.authentication.Permission;

import JSites.utils.DBGateway;
import JSites.utils.Util;

public class ManageUsersAction extends PageAction {
	
	@SuppressWarnings("rawtypes")
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		
		super.act(redirector, resolver, objectModel, source, parameters);
		if(parameters.getParameter("containerType").equals("content")) {
			DataSourceComponent datasourceComponent=null;
			try {
				datasourceComponent=((DataSourceComponent)dbselector.select(dbname));
				String pcode = request.getParameter("pcode");
				
				if(pid==-1 || pid==0) {
					pid=DBGateway.getPidFrom(datasourceComponent, pcode);
				}
				if(pid==-1) pid=1;
				
				Enumeration<?> params = request.getParameterNames();
				while(params.hasMoreElements()){
					String name = (String)params.nextElement();
					if(name.startsWith("utente")){
						String cUsername= request.getParameter(name);
						String index = name.replaceAll("utente","");
		
						String editable = request.getParameter("editable" + index);
						String validable = request.getParameter("validable" + index);
						
						boolean atLeastOne = false;
						
						Permission p = new Permission();
						if(editable != null){
							atLeastOne = true;
							p.setPermission(Permission.EDITABLE);
						}
							
						if(validable != null){
							atLeastOne = true;
							p.setPermission(Permission.VALIDABLE);
						}					
						
						if(atLeastOne)
							p.setPermission(Permission.ACCESSIBLE);
	
						if(pid != -1 && username.length()>0 && permission.hasPermission(Permission.VALIDABLE))
							DBGateway.setPermission(datasourceComponent, pid, cUsername, p);					
					}
					else if(name.equals("pageTitle") && permission.hasPermission(Permission.EDITABLE)){
						String titolo = Util.getRequestData(request, name); //Util.readRequestParameter(request.getParameter(name));
						DBGateway.setPageName(datasourceComponent, pid, titolo);
					}
					else if(name.equals("pageCode") && permission.hasPermission(Permission.EDITABLE)){
						String code = request.getParameter(name);
						DBGateway.setPageCode(datasourceComponent, pid, code);
					}
					else if(name.equals("pagePaPCode") && permission.hasPermission(Permission.VALIDABLE)){
						String PaPCode = request.getParameter(name);
						if(PaPCode!=null && PaPCode.trim().length()>0) {
							long papid=DBGateway.getPidFrom(datasourceComponent, PaPCode);
//							Permission p=DBGateway.getPermission(username, null, papid, conn);
//							if(p.hasPermission(Permission.VALIDABLE))
							DBGateway.setPaPid(datasourceComponent, pid,papid);
//							p.erasePermissions();
						}
					}
					else if(name.equals("resp") && permission.hasPermission(Permission.VALIDABLE)){
						String code = request.getParameter(name);
						DBGateway.setPageResp(datasourceComponent, pid, code);
					}
					else if(name.startsWith("priority_") && permission.hasPermission(Permission.VALIDABLE)) {
						String code=name.substring(9);
						String priority=request.getParameter(name);
						DBGateway.setPagePriority(datasourceComponent, code,priority);
					}
				}
				
			}catch(Exception e){e.printStackTrace();}
			finally {
				if(datasourceComponent!=null) this.manager.release(datasourceComponent);
			}
		
		}
		
		return objectModel;
	}
	
}
