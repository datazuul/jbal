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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentSelector;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.modules.input.SitemapVariableHolder;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.AbstractGenerator;
import org.apache.cocoon.webapps.session.SessionManager;
import org.apache.cocoon.xml.AttributesImpl;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.Utils;
import org.xml.sax.SAXException;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import JSites.authentication.Authentication;
import JSites.authentication.Permission;

import JSites.components.staticDataComponent;

import JSites.utils.DBGateway;

public abstract class MyAbstractPageGenerator extends AbstractGenerator implements Composable, Disposable {
	
	private static Logger logger;
	
	protected String containerType = "";
	protected ComponentSelector dbselector;
	protected ComponentManager manager;
	protected DataSourceComponent datasourceComponent = null;
	protected Request request = null;
	protected long pageId = 0;
	protected long lastPageId = 0;
	protected SessionManager sessionManager = null;
	protected Session session = null;
	protected staticDataComponent staticData;
	protected Permission permission;
	protected AttributesImpl emptyAttrs;
	protected String dbname = "";
	protected String datadir = "";
	protected SitemapVariableHolder sitemapVariables = null;
	protected String _source="";
	    
	public void compose(ComponentManager manager) throws ComponentException {
		this.manager = manager;
        dbselector = (ComponentSelector) manager.lookup(DataSourceComponent.ROLE + "Selector");
        sessionManager = (SessionManager) manager.lookup(SessionManager.ROLE);
    }
    
    @Override
	public void recycle() {
		super.recycle();
		this.manager.release(datasourceComponent);
	}

	public void generate() throws SAXException {

    	contentHandler.startDocument();
    	boolean content=true;
    	
    	Connection conn=null;
    	PreparedStatement ps=null;
    	ResultSet rs1=null;
		try {

			logger.log(Level.CONFIG, dbname + ": "+ "Connesso a DB: " + dbname);
			if(!(permission.hasPermission(Permission.ACCESSIBLE))){
				contentHandler.endDocument();
				return;
			}
			conn=datasourceComponent.getConnection();
			ps = conn.prepareStatement("SELECT tblstrutture.PID, tblstrutture.CID, tblcomponenti.Type\n" +
					"FROM tblcomponenti INNER JOIN tblstrutture ON tblcomponenti.CID = tblstrutture.CID\n" +
					"WHERE (((tblstrutture.PID)=?) AND ((tblcomponenti.Type)=?))");
			
			if(containerType.equals("content")) {
				ps.setLong(1, pageId);
				ps.setString(2, containerType);
			}
			else {
				long tpid=DBGateway.getPidFrom(datasourceComponent,"_"+containerType);
				if(tpid==-1) tpid=1;
				if(tpid!=1) { // 1 = sempre HOMEPAGE, ed e' il default restituito se la pagina non esiste
					ps.setLong(1, tpid);
					ps.setString(2, "content");
					content=false;
				}
				else {
					ps.setLong(1, pageId);
					ps.setString(2, containerType);
				}
			}
				

			rs1 = ps.executeQuery();
			while(!rs1.isClosed() && rs1.next()){
				long cid = rs1.getLong("CID");
				logger.log(Level.CONFIG, dbname + ": "+ "Processing CID: " + cid);
				processChild(cid);
			}

		} catch (Exception e1) {
			System.out.println(request.getRequestURI() + " - " + request.getQueryString());
			e1.printStackTrace();
		}
		finally {
			if(rs1!=null) try{rs1.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - MyAbstractPageGenerator.generate() exception " + fe.getMessage());}
			if(ps!=null) try{ps.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - MyAbstractPageGenerator.generate() exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - MyAbstractPageGenerator.generate() exception " + fe.getMessage());}
		}
		
		contentHandler.endDocument();
	}

//	public Connection getConnection(String db) {
//		Connection conn=null;
//		try {
//			conn = ((DataSourceComponent)dbselector.select(db)).getConnection();
//		} catch (ComponentException e) {e.printStackTrace();
//		} catch (SQLException e) {e.printStackTrace();
//		}
//    	return conn;
//    }
    
    public String getResource(String name) {
    	String path = ObjectModelHelper.getContext(objectModel).getRealPath(name);
    	return path;
    }
    
    public void processChild(long id) throws SAXException, SQLException {
		
    	String[] componentType = {""};
		String attributes = "";
		boolean hasChildren = false;
		long cState = 0;
		boolean isNews = false;
				
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		try {
			conn=datasourceComponent.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery("select * from tblcomponenti where CID=" + id);
			if(!rs.isClosed() && rs.next()){
				
				componentType = rs.getString("Type").split(":");
				logger.log(Level.CONFIG, dbname + ": "+ "CID " + id + " is a " + componentType[0]);
				
				attributes = rs.getString("Attributes");
				hasChildren = rs.getBoolean("HasChildren");
				cState = DBGateway.getState(datasourceComponent,id);
			}
		} catch (Exception e) {e.printStackTrace();}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - MyAbstractPageGenerator.processChild exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - MyAbstractPageGenerator.processChild exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - MyAbstractPageGenerator.processChild exception " + fe.getMessage());}
		}
		
		if(isNews && ! (this instanceof PageEditor || this instanceof PageSaver)) return;
		
		if( (permission.hasPermission(Permission.EDITABLE) && cState > 0 && cState < 5) ||
			(permission.hasPermission(Permission.VALIDABLE) && cState > 1 && cState < 5) ||
		    cState == 3 || cState == 0)
		{
			AttributesImpl attrCid = new AttributesImpl();
			attrCid.addCDATAAttribute("cid",String.valueOf(id));
			attrCid.addCDATAAttribute("pid",String.valueOf(pageId));
			long order = DBGateway.getOrderNumber(datasourceComponent,id);
			if(order>0){
				attrCid.addCDATAAttribute("order",String.valueOf(order));
			}
			
			if(attributes!=null){
				attrCid.addCDATAAttribute("data",attributes);
			}
			if(componentType.length>1)
				attrCid.addCDATAAttribute("extra", componentType[1]);
			
			attrCid.addCDATAAttribute("container", containerType);
			
			subClassProcess(componentType[0], id, attrCid, hasChildren); // was componentType[0]
		}

	}

    @SuppressWarnings("rawtypes")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException{
		
		super.setup(resolver, objectModel, src, par);
		
		_source=src;
		
		InputStream ins=new FileInputStream(new File(getResource("WEB-INF/logging.properties")));
    	LogManager.getLogManager().readConfiguration(ins);
    	ins.close();
    	
    	logger=Logger.getLogger("MyAbstractPageGenerator");
		
		lastPageId = pageId;
		
		logger.log(Level.CONFIG, dbname + ": "+ "lastPageId: " + lastPageId);
		
		
		
		this.emptyAttrs = new AttributesImpl();
		request = (Request)(objectModel.get("request"));
		
//		session = sessionManager.getSession(true);
		session = request.getSession(true);
		
		dbname = request.getParameter("db");
		datadir = request.getParameter("datadir");
		logger.log(Level.CONFIG, dbname + ": "+ "Request: " + request.getQueryString());
		
		
		try{
			try {
				dbname = dbname == null ? par.getParameter("db") : dbname; 
				datadir = datadir == null ? par.getParameter("datadir") : datadir; 
			} catch (ParameterException e) {
//				System.out.println("The QueryString was: " + request.getQueryString());
				System.out.println("Class: "+this.getClass().getCanonicalName());
				System.out.println(e.getMessage());
			}
			if(dbname!=null) {
				try {
					datasourceComponent=((DataSourceComponent)dbselector.select(dbname));
				} catch (ComponentException e1) {
					e1.printStackTrace();
					throw new ProcessingException(e1.getMessage());
				}
				
				pageId = ManagePageID();
				init();
				logger.log(Level.CONFIG, dbname + ": "+ "Take the permissions.");
				String remoteaddr=request.getRemoteAddr();
				try {
					permission = Authentication.assignPermissions(datasourceComponent,session, remoteaddr, pageId);
				}
				catch(Exception e) {
					System.out.println("Authentication Error");
					System.out.println("Class: "+this.getClass().getCanonicalName());
					System.out.println(e.getMessage());
					System.out.println("dbname: "+dbname);
					System.out.println("datadir: "+datadir);
					System.out.println("remoteaddr: "+remoteaddr);
				}
				session.setAttribute("permission",permission);
				
				logger.log(Level.CONFIG, dbname + ": "+ "Permissions taken.");
							}
			try{
				containerType = parameters.getParameter("containerType");
			}
			catch(Exception e){containerType="N/A";}
			//System.out.println(this.containerType + ": " + this);
		}
		catch (Exception e2){ e2.printStackTrace(); }
	}

	protected void init(){}
	
    
    protected void subClassProcess(String componentType, long id, AttributesImpl attrCid, boolean hasChildren) throws SAXException, SQLException {

    	if(componentType.equals("content") && !containerType.equals("content")) { //shift content on footer, header, ...
    		componentType=containerType;
    	}
    	
    	if(permission.getPermissionCode()>Permission.ACCESSIBLE)
    		attrCid = doColor(id, attrCid);
    	
		String queryString = queryString4Children(id);

		long pacid = DBGateway.getPacid(datasourceComponent, id);
		attrCid.addCDATAAttribute("pacid",String.valueOf(pacid));
    	
		
		if(componentType.equals("content")){
			attrCid.addCDATAAttribute("accessible",String.valueOf(permission.hasPermission(Permission.ACCESSIBLE)));
			attrCid.addCDATAAttribute("editable",String.valueOf(permission.hasPermission(Permission.EDITABLE)));
			attrCid.addCDATAAttribute("validable",String.valueOf(permission.hasPermission(Permission.VALIDABLE)));
			attrCid.addCDATAAttribute("sfa",String.valueOf(permission.hasPermission(Permission.SFA)));
			attrCid.addCDATAAttribute("pageTitle",String.valueOf(DBGateway.getPageName(datasourceComponent,pageId)));
		}
		
		contentHandler.startElement("",componentType,componentType,attrCid);
		
		if(componentType.equals("content") && (permission.hasPermission(Permission.VALIDABLE) ) ){
			AttributesImpl dbAttrs = new AttributesImpl();
			dbAttrs.addCDATAAttribute("order", "0");
			dbAttrs.addCDATAAttribute("time", "presente");
			dbAttrs.addCDATAAttribute("disabling", "false");
			dbAttrs.addCDATAAttribute("editing", "false");
			dbAttrs.addCDATAAttribute("pacid",String.valueOf(pacid));
			dbAttrs.addCDATAAttribute("contentcid",String.valueOf(DBGateway.getContentCID(datasourceComponent,pageId)));
			
			contentHandler.startElement("","dbManager","dbManager",dbAttrs);
			contentHandler.endElement("","dbManager", "dbManager");

		}
		if(hasChildren){
			Connection conn=null;
			Statement st=null;
			ResultSet rs=null;
			try {
				conn=datasourceComponent.getConnection();
				st = conn.createStatement();
		    	rs = st.executeQuery(queryString); // ho tolto and IDStato=3
				while(!rs.isClosed() && rs.next()){
					processChild(rs.getLong(1));
				}
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			finally {
				if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - MyAbstractPageGenerator.subClassProcess resultset exception " + fe.getMessage());}
				if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - MyAbstractPageGenerator.subClassProcess statement exception " + fe.getMessage());}
				if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - MyAbstractPageGenerator.subClassProcess connection exception " + fe.getMessage());}
			}
		}
		contentHandler.endElement("",componentType,componentType);
	}
	
	protected String queryString4Children(long id) {
		return "select CID from tblcontenuti where StateID<4 and PaCID=" + id + " order by OrderNumber";
	}

	private long ManagePageID() throws SQLException { 
		
		long ret=-1;
		String spid=request.getParameter("pid");
		String pcode = request.getParameter("pcode");
		if(pcode!= null && !pcode.equals("null")){
			ret = DBGateway.getPidFrom(datasourceComponent,pcode);
		}
		else{
			try {
				ret = Long.parseLong(spid);
			}
			catch (Exception e2){ret = -1;} 
		}
		
		if(ret==0)ret=Long.parseLong(request.getParameter("papid"));	//creazione pagina
		else if(ret==-1){										//salvataggio nuova pagina
			if(request.getParameter("papid")==null)ret=1;
			else{
				Long l = (Long) session.getAttribute("newPageID");
			
				if(l==null){
					int ln = DBGateway.getNewPageId(datasourceComponent);
					DBGateway.creaPaginaInDB(datasourceComponent,ln,request);
					String remoteaddr=request.getRemoteAddr();
					try {
						Permission p=DBGateway.getPermission(datasourceComponent, Authentication.getUserData(session, "ID"), remoteaddr, ret);
						DBGateway.setPermission(datasourceComponent, new Long(ln), Authentication.getUserData(session, "ID"), p);

					} catch (JOpac2Exception e) {
						DBGateway.setPermission(datasourceComponent, new Long(ln), Authentication.getUserData(session, "ID"));
					}
					session.setAttribute("newPageID",new Long(ln));
					ret=ln;
				}
				else {
					ret = l.longValue();
				}
			}
		} 
		try {
			if(request.getRequestURI().endsWith("dodelete") && request.getParameter("cid").equals("0")){
				ret = DBGateway.getPapid(datasourceComponent, ret);
			}
		} catch (Exception e) { e.printStackTrace(); }
		
		return ret;
	}

	protected AttributesImpl doColor(long id, AttributesImpl attrCid){
		try{
			long cState = DBGateway.getState(datasourceComponent, id); 
			if(cState==1 && !(DBGateway.hasNewVersion(datasourceComponent, id))){ //in lavoro
				if(permission.hasPermission(Permission.EDITABLE))
					attrCid.addCDATAAttribute("editing","true");
					attrCid.addCDATAAttribute("time","strafuturo");
			}
			
			else if(cState==2){ //da validare
				attrCid.addCDATAAttribute("time","futuro");
				if(permission.hasPermission(Permission.VALIDABLE))
					attrCid.addCDATAAttribute("validating","true");
				if(permission.hasPermission(Permission.EDITABLE))
					attrCid.addCDATAAttribute("editing","true");
			}
			
			else if(cState==3){// && !(DBGateway.hasNewVersion(id))){ //attivo
				attrCid.addCDATAAttribute("time","presente");
				if(permission.hasPermission(Permission.VALIDABLE))
					attrCid.addCDATAAttribute("disabling","true");
				if(permission.hasPermission(Permission.EDITABLE))
					attrCid.addCDATAAttribute("editing","true");
			}
			
			else if(cState==4 || DBGateway.hasNewVersion(datasourceComponent, id)){ //vecchio
				attrCid.addCDATAAttribute("time","passato");
			}
			
			//else attrCid.addCDATAAttribute("editing","false");
		}
		catch (Exception e) { e.printStackTrace(); }
		return attrCid;
	}
	
	public void dispose() {
		this.manager.release(dbselector);
	}
	
	public void sendElement(String element, String content) throws SAXException {
		sendElement(element,content, emptyAttrs);
	}
	
	public void sendElement(String element, String content, AttributesImpl attr) throws SAXException {
		if(content==null || content.length()==0) content="null";
		contentHandler.startElement("",element,element, attr);
		if(content.length()>0)
			contentHandler.characters(content.toCharArray(), 0, content.length());
		contentHandler.endElement("",element,element);
	}
	

}
