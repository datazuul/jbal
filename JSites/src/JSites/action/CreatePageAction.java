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
import java.util.Date;
import java.util.Map;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

import JSites.authentication.Permission;
import JSites.utils.DBGateway;

public class CreatePageAction extends PageAction {
	
	@SuppressWarnings("rawtypes")
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		
		super.act(redirector, resolver, objectModel, source, parameters);
		if(permission.hasPermission(Permission.VALIDABLE)) {
			String pcode="";
			DataSourceComponent datasourceComponent=null;
			try {
				datasourceComponent=((DataSourceComponent)dbselector.select(dbname));
				String title=request.getParameter("title");
				if(pid!=-1 && title!=null && title.trim().length()>0) {
					int newpid = DBGateway.getNewPageId(datasourceComponent);
					DBGateway.createPage(datasourceComponent, title, newpid, (int)pid);
					
					long newContentCid = DBGateway.getNewFreeCid(datasourceComponent);
					DBGateway.saveDBComponent(datasourceComponent, newContentCid,"content",1,0,new Date(), newpid, "", 
							username, remoteAddr);
					DBGateway.linkPageContainers(datasourceComponent, newContentCid, newpid);
					
					DBGateway.clonePermission(datasourceComponent, (int)pid,newpid);
					
					pcode=DBGateway.getPageCode(datasourceComponent, newpid);
					pid=newpid;
				}
				else {
					pcode=DBGateway.getPageCode(datasourceComponent, pid);
				}
			}catch(Exception e){e.printStackTrace();}
			finally {
				if(datasourceComponent!=null) this.manager.release(datasourceComponent);
				request.setAttribute("pagecode", pcode);
				request.setAttribute("pid", pid);
			}
		}
		return objectModel;
	}
	
	


}
