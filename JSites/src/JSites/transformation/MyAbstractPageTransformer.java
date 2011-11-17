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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentSelector;
import org.apache.avalon.framework.component.Composable;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.cocoon.webapps.session.SessionManager;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.environment.http.HttpRequest;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.xml.sax.SAXException;
import org.apache.cocoon.xml.AttributesImpl;
import org.jopac2.jbal.RecordInterface;

import JSites.authentication.Permission;

public abstract class MyAbstractPageTransformer extends AbstractTransformer implements Composable, Disposable {
	
	public ComponentSelector dbselector;
	public ComponentManager manager;
//	public SessionManager sessionManager;
	protected Request request;
	protected Session session;
	protected String dbname = null;
	protected AttributesImpl emptyAttrs = new AttributesImpl();
	protected boolean debug=false;
	protected Permission permission=null;
	protected Hashtable<String,String> pagedata=null;
	
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		request = (Request)arg1.get("request");
		session = request.getSession(true);
	
		dbname = request.getParameter("db");
		
		try { dbname = dbname == null ? arg3.getParameter("db") : dbname; } catch (ParameterException e) {
			System.out.println(e.getMessage());
			System.out.println("Request was: " + request.getQueryString());
		}
		permission=(Permission)session.getAttribute("permission");
		pagedata=(Hashtable)session.getAttribute("lastdata");
	}
	
	public void compose(ComponentManager manager) throws ComponentException {
		this.manager = manager;
        dbselector = (ComponentSelector) manager.lookup(DataSourceComponent.ROLE + "Selector");
//        sessionManager = (SessionManager) manager.lookup(SessionManager.ROLE);
    }
	
	public void dispose() {
		this.manager.release(dbselector);
	}
    
    public Connection getConnection(String db) throws ComponentException, SQLException {
    	return ((DataSourceComponent)dbselector.select(db)).getConnection();
    }
    
	@SuppressWarnings("unchecked")
	public void printRequestData(Map arg1, Parameters arg3) {
		 
		 String name = "";
		 String value = "";
		 
		 if(arg1!=null) {
			 Set arg1Keys = arg1.keySet();
			 Iterator iter = arg1Keys.iterator();
			 
			 while(iter.hasNext()){
				 name = (String)iter.next();
				 value = arg1.get(name).toString();
				 System.out.println(name + " = " + value);
			 }
		 }
		 System.out.println("REQUEST QUERY = " + request.getQueryString());
		 
		 System.out.println("Request parameters:");
		 @SuppressWarnings("unchecked")
		Enumeration<String> e = (Enumeration<String>)request.getParameterNames();
		 while(e.hasMoreElements()){
			 name = (String)e.nextElement();
			 value = request.getParameter(name);
			 System.out.println(name + " = " + value);
		 }


		 System.out.println("--------------------");
		 		 
		 System.out.println("Request attributes:");
		 e = request.getAttributeNames();
		 while(e.hasMoreElements()){
			 name = (String)e.nextElement();
			 value = request.getAttribute(name).toString();
			 System.out.println(name + " = " + value);
		 }


		 System.out.println("--------------------");
		 
		 
		 if(arg3!=null) {
			 String[] arg3Keys = arg3.getNames();
			 
			 for(int i=0;i<arg3Keys.length;i++){
				 name = arg3Keys[i];
				 try {
					value = arg3.getParameter(name);
					System.out.println(name + " = " + value);
				} catch (Exception e1) {
					System.out.println("Exception parameter: "+ name);
					e1.printStackTrace();
				}
				 
			 }
		 }
	}
    
    protected String saveImgFile(RecordInterface ri) {
    	String r="images/pubimages/NS.jpg";
		try {
    	
			if(ri.getImage()!=null) {
			
				File dir = new File(this.request.getParameter("datadir") + "/images/pubimages");
				if(!dir.exists())dir.mkdirs();
				String imgstr = this.request.getParameter("datadir") + "/images/pubimages/eut" + ri.getJOpacID() + ".jpg";
				try {
					FileOutputStream imgfile = new FileOutputStream(imgstr);
					ImageIO.write(ri.getImage(), "jpeg", imgfile);
					imgfile.flush();
					imgfile.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				r="images/pubimages/eut" + ri.getBid() + ".jpg";
			}
		}
		catch(Exception e) {
			
		}
		return r;
		
	}
    
    protected void throwField(String name, String value) throws SAXException{
		if(value != null && value.length()>0){
			contentHandler.startElement("", name, name, emptyAttrs);
			contentHandler.characters(value.toCharArray(), 0, value.length());
			contentHandler.endElement("", name, name);
		}
	}
    
    protected void throwField(String name, String qName, String value) throws SAXException{
		if(value != null && value.length()>0){
			contentHandler.startElement("", name, qName, emptyAttrs);
			contentHandler.characters(value.toCharArray(), 0, value.length());
			contentHandler.endElement("", name, qName);
		}
	}
    
    protected String getParameter(String parameter) {
    	String r=request.getParameter(parameter);
    	if(r==null) r=(String)session.getAttribute(parameter);
    	return r;
    }
    
}