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
import java.sql.SQLException;
import java.util.Map;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.*;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;

import JSites.authentication.Authentication;
import JSites.authentication.Permission;

public abstract class PageAction implements Action, Composable, Disposable {
	
	protected ComponentSelector dbselector;
	protected String dbname;
	protected Request request;
	protected Session session;
	protected ComponentManager manager;
	protected String username;
	protected String remoteAddr;
	protected long pid=-1;
	protected long cid=-1;
	protected long pacid=-1;
	protected String pidString=null;
	protected String cidString=null;
	protected String pacidString=null;
	protected Permission permission=null;
	protected String dataDirectory=null;
	
	@SuppressWarnings("rawtypes")
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		Connection conn=null;

		request = (Request)(objectModel.get("request"));
		session = request.getSession(true);
		dbname = request.getParameter("db");
		dataDirectory=request.getParameter("datadir");

		
		username=Authentication.getUsername(session);
		remoteAddr=request.getRemoteAddr();
		
		pidString=request.getParameter("pid");
		cidString=request.getParameter("cid");
		pacidString=request.getParameter("pacid");
		if(pidString!=null)	pid = Long.parseLong(pidString);
		if(cidString!=null)	cid = Long.parseLong(cidString);
		if(pacidString!=null)	pacid = Long.parseLong(pacidString);
		
		try { dbname = dbname == null ? parameters.getParameter("db") : dbname; } catch (ParameterException e) {
			e.printStackTrace();
			System.out.println("Request was: " + request.getQueryString());
			System.out.println("No db param in request and no db param sitemap");
		}
		
		try { dataDirectory = dataDirectory == null ? parameters.getParameter("datadir") : dataDirectory; } catch (ParameterException e) {
			e.printStackTrace();
			System.out.println("Request was: " + request.getQueryString());
			System.out.println("No datadir param in request and no datadir param sitemap");
		}
		
		DataSourceComponent datasourceComponent=null;
		try {
			datasourceComponent=((DataSourceComponent)dbselector.select(dbname));
			permission = Authentication.assignPermissions(datasourceComponent,session, request.getRemoteAddr(), pid);
		} catch(Exception e) {
			throw e;
		}
		finally {
			if(datasourceComponent!=null) this.manager.release(datasourceComponent);
		}
		return objectModel;
	}
	
	public void compose(ComponentManager cm) throws ComponentException {
		manager = cm;
        dbselector = (ComponentSelector) manager.lookup(DataSourceComponent.ROLE + "Selector");
    }
	
	public void dispose() {
		try {
//			if(conn!=null) conn.close();
		}
		catch(Exception e) {}
		this.manager.release(dbselector);
	}
}