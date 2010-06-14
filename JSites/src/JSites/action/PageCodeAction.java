package JSites.action;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2010 JOpac2 project
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

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentSelector;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.acting.Action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashMap;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;

import JSites.utils.DBGateway;


public class PageCodeAction implements Action, Composable, Disposable {
	private ComponentSelector dbselector;
	protected ComponentManager manager;

	
	
	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, SourceResolver resolver,
			Map objectModel, String source, Parameters params) {	

		Map<String,String> lastdata=new Hashtable<String,String>();
		Request request = ObjectModelHelper.getRequest(objectModel);
		String pid=request.getParameter("pid");
		String pcode=request.getParameter("pcode");
		
		Session session=request.getSession();
		
		Enumeration<String> ee=request.getParameterNames();
		while(ee.hasMoreElements()) {
			String s=ee.nextElement();
//			System.out.println("** "+s+" = "+request.getParameter(s));
			lastdata.put(s, request.getParameter(s));
		}
		
		
		
		String pname="";
		String dbname=source;
		
		Connection conn=null;
		
		if(pid==null || pid.length()==0) {
			try {
				conn=getConnection(dbname);
				pid=Long.toString(DBGateway.getPidFrom(pcode, conn));
			}
			catch(Exception e) {e.printStackTrace();}
			finally {
				if(conn!=null) try{conn.close();} catch(Exception e) {}
			}
		}
		else if((pcode==null || pcode.length()==0) && !pid.equals("0") ) {
			try {
				conn=getConnection(dbname);
				pcode=DBGateway.getPageCode(Long.parseLong(pid), conn);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				if(conn!=null) try{conn.close();} catch(Exception e) {}
			}
		}
		
		if(!pid.equals("0")) {
			try {
				conn=getConnection(dbname);
				pname=DBGateway.getPageName(Long.parseLong(pid), conn);
			}
			catch(Exception e) {e.printStackTrace();}
			finally {
				if(conn!=null) try{conn.close();} catch(Exception e) {}
			}
		}
		
		if(pcode==null) pcode="_null";
				
		request.setAttribute("pageid", pid);
		request.setAttribute("pagecode", pcode);
		request.setAttribute("pagename", pname);
		
		lastdata.put("pageid", pid);
		lastdata.put("pagecode", pcode);
		lastdata.put("pagename", pname);
		
		
		String ru=request.getRequestURI();
		if(ru.contains("/")) ru=ru.substring(0, ru.lastIndexOf("/"));
		ru=ru+"/"+pcode;
		lastdata.put("pagerequest", ru);
		
		session.setAttribute("redirectfrom", lastdata);
		return objectModel;
	}
	
	public void compose(ComponentManager cm) throws ComponentException {
		manager = cm;
        dbselector = (ComponentSelector) manager.lookup(DataSourceComponent.ROLE + "Selector");
    }
	
	public void dispose() {
		this.manager.release(dbselector);
	}
	
	public Connection getConnection(String db) throws ComponentException, SQLException {
    	return ((DataSourceComponent)dbselector.select(db)).getConnection();
    }
}
