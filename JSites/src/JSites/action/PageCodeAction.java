package JSites.action;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2011 JOpac2 project
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
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.components.modules.input.SitemapVariableHolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.w3c.dom.Document;

import JSites.utils.DBGateway;
import JSites.utils.Page;


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
		Document metadata=null;
		
		String pname="";
		String[] tsource=source.split(":");
		String dbname=tsource[0];
		String entry=tsource.length>1?tsource[1]:null;
		String template=null;
		
		try {
			template=(String)getAttribute("template");
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
		}

		
		Connection conn=null;
		
		Session session=request.getSession();
		
		if(pid==null && pcode==null) {
			String uri=request.getRequestURI();
			int k=uri.lastIndexOf('/');
			pcode=uri.substring(k+1);
		}
		
		Map<String,String> rf=(Map<String,String>)session.getAttribute("redirectfrom");
		
		if(rf!=null && pcode!=null && rf.containsKey("pcode") && rf.get("pcode").equals(pcode)) return objectModel;
		
		Enumeration<String> ee=request.getParameterNames();
		while(ee.hasMoreElements()) {
			String s=ee.nextElement();
//			System.out.println("** "+s+" = "+request.getParameter(s));
			lastdata.put(s, request.getParameter(s));
		}
		
		
		
		// get default template
		if(!lastdata.containsKey("_template")) {
			// user doesn't submit template
			String ptemplate=(String)session.getAttribute("template");
			if(ptemplate!=null) {
				template=ptemplate;
			}
			lastdata.put("_template", template);
		}
		else {
			// user submits template
			if(!lastdata.get("_template").equals("default")) {
				// is not "default"
				session.setAttribute("template", lastdata.get("_template"));
			}
			else {
				// is "default", restore default template defined in sitemap.xmap
				session.setAttribute("template", template);
				lastdata.put("_template", template);
			}
		}
		
		

		if(pid==null && pcode.length()==0) pid="1"; // homepage
		
		if(pid==null || pid.length()==0) {
			try {
				conn=getConnection(dbname);
				if(pcode!=null) {
					metadata=DBGateway.getPageMetadata(pcode, lastdata, conn);
					pid=lastdata.get("pid");
					
//					pid=Long.toString(DBGateway.getPidFrom(pcode, conn));
				}
				else { // db probabily empty, go to homepage
					pid="0";pcode="";
				}
			}
			catch(Exception e) {e.printStackTrace();}
			finally {
				if(conn!=null) try{conn.close();} catch(Exception e) {}
			}
		}
		else if((pcode==null || pcode.length()==0) && !pid.equals("0") ) {
			try {
				conn=getConnection(dbname);
				metadata=DBGateway.getPageMetadata(Long.parseLong(pid), lastdata, conn);
				pcode=lastdata.get("pcode");
				
//				pcode=DBGateway.getPageCode(Long.parseLong(pid), conn);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				if(conn!=null) try{conn.close();} catch(Exception e) {}
			}
		}
		
		if(pcode==null) pcode="_null";
				
		request.setAttribute("pageid", pid);
		request.setAttribute("pagecode", pcode);
//		request.setAttribute("pagename", pname);
		
		if(pid!=null) lastdata.put("pageid", pid);
		if(pcode!=null) lastdata.put("pagecode", pcode);
//		lastdata.put("pagename", pname);
		
		Set<String> set=lastdata.keySet();
		Iterator<String> i=set.iterator();
		
		while(i.hasNext()) {
			String t=i.next();
			request.setAttribute(t, lastdata.get(t));
		}
		
		
		String ru=request.getRequestURI();
		if(ru.contains("/")) ru=ru.substring(0, ru.lastIndexOf("/"));
		ru=ru+"/"+pcode;
		lastdata.put("pagerequest", ru);
		
		session.setAttribute("redirectfrom", lastdata);
		
		if(metadata!=null) session.setAttribute("metadata", metadata);
		return objectModel;
	}
	
	@SuppressWarnings("deprecation")
	public Object getAttribute(String name) throws ConfigurationException  {
		SitemapVariableHolder holder = null;
        try {
            holder = (SitemapVariableHolder)this.manager.lookup(SitemapVariableHolder.ROLE);
            return holder.get(name); 
        } catch (Exception ce) {
            throw new ConfigurationException("Unable to lookup SitemapVariableHolder.", ce);
        } finally {
            this.manager.release(holder);
        }
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
