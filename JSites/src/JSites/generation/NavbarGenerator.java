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

import java.io.IOException;
import java.sql.*;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.AttributesImpl;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.Utils;
import org.xml.sax.SAXException;

import JSites.authentication.Authentication;
import JSites.authentication.Permission;

import JSites.utils.DBGateway;
import JSites.utils.Page;
import JSites.utils.PageTree;

public class NavbarGenerator extends MyAbstractPageGenerator {

	private Vector<Long> activePids = null;
	private boolean newpage = false;
	private Session session = null;
	Permission permission;
	private boolean toggle = true;
//	private boolean showAreasInFirstLevelPages = false;
	
//	public static String aggiungiPagina="[ aggiungi pagina ]", aggiungiLink="[ aggiungi link esterno ]";
	
	public void generate() throws SAXException {
	
		activePids = new Vector<Long>();

		contentHandler.startDocument();
		
		contentHandler.startElement("","navigator","navigator", new AttributesImpl());
		
		contentHandler.startElement("","id","id", new AttributesImpl());
//		contentHandler.characters("navbar".toCharArray(),0,"navbar".length());
		contentHandler.endElement("","id","id");
		
		try{
			activePids.add(Long.valueOf(pageId));
			
 			try{ newpage = Boolean.parseBoolean((String) request.getParameter("newpage")); }
 			catch(Exception e){ newpage = false;}

//			PageTree tree = createPageTree(pageId, conn);
			
			AttributesImpl hrefAttrs = new AttributesImpl();
			if(pageId!=1){
				String areaName = DBGateway.getAreaName(datasourceComponent, pageId);
				
				hrefAttrs.addCDATAAttribute("href","pageview?pid="+DBGateway.getStartFromPid(datasourceComponent, pageId));
				hrefAttrs.addCDATAAttribute("areaname",areaName);
			}
			contentHandler.startElement("","selectedarea","selectedarea", hrefAttrs);
			contentHandler.endElement("","selectedarea","selectedarea");
			
			int level=1;
			
			String username=Authentication.getUsername(session);
			boolean authenticated=true;
			
			if(username==null || username.length()==0 || username.equals("guest"))
				authenticated=false;
			
			Connection conn=null;
			try {
				conn=datasourceComponent.getConnection();
				generateTree(conn,1,level,authenticated);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - NavbarGenerator.generate connection exception " + fe.getMessage());}
			}
			
						
//			processTree(tree,0,conn);

		}catch(Exception e) {e.printStackTrace();}
					
		contentHandler.endElement("","navigator","navigator");
		contentHandler.endDocument();
	}
	

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException{
		
		super.setup(resolver, objectModel, src, par);

		containerType="internal navbar";
		
		session = sessionManager.getSession(false);
		if(session!=null){
			permission = (Permission)session.getAttribute("permission");
			if(permission==null){
				permission = new Permission();
				permission.setPermission(Permission.ACCESSIBLE);
			}	
		}
		String toggleRead = this.request.getParameter("toggle");
		if(toggleRead!=null){
			toggle = Boolean.parseBoolean(toggleRead);
		}
		else toggle=true;
		
//		String showAreasInFirstLevelPagesRead = this.request.getParameter("showAreasInFirstLevelPages");
//		if(showAreasInFirstLevelPagesRead!=null){
//			showAreasInFirstLevelPages = Boolean.parseBoolean(showAreasInFirstLevelPagesRead);
//		}
//		else showAreasInFirstLevelPages = false;
	}

	/**
	 * @deprecated
	 * @param pid
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws JOpac2Exception
	 */
//	private PageTree createPageTree(long pid, Connection conn) throws SQLException, JOpac2Exception {
//		
//		long padre = DBGateway.getPapid(pid, conn);
//		TreeSet<PageTree> zii = getChildren(DBGateway.getPapid(padre,conn),conn);
//		TreeSet<PageTree> figli = null;
//		
//		figli=getChildren(pid,conn);
//		
//		TreeSet<PageTree> fratelli = getChildren(padre, conn);
//		PageTree ret = null;
//		String nomePagina=DBGateway.getPageName(pid, conn);
//		PageTree me=new PageTree(figli,figli.isEmpty(),nomePagina,pid);
//		
//		if(!toggle && (!figli.isEmpty())){
//			me.setChilds(new TreeSet<PageTree>());
//			me.setLeaf(false);
//		}
//
//		fratelli.remove(me); // apparentemente non fa nulla, invece toglie me istanziato senza figli e...
//    	fratelli.add(me);	 // .... mi aggiunge istanziato con i figli, xche' il compareTo guarda solo il nome
//
//		int livello = DBGateway.getPageLevel(pid, 0, conn);
//		long myArea=DBGateway.getStartFromPid(pid, conn);
//		
//		switch(livello) {
//		case 0: // homepage, visualizza soltanto i miei figli
//            ret=me;
//            break;
//        case 1: // aree
//        	
//        	Iterator<PageTree> i=figli.iterator();
//        	while(i.hasNext()) {
//        		PageTree figlio=i.next();
//      			figlio.setChilds(getChildren(figlio.getRootPid(),conn));
//        	}
//        	
//        	if(showAreasInFirstLevelPages)
//        		ret=new PageTree(fratelli,false,DBGateway.getPageName(padre, conn),padre);
//        	else
//        		ret=me;
//        		
//        		
//            break;
//        case 2: {// sottoaree
//        		Iterator<PageTree> fi = fratelli.iterator();
//        		while(fi.hasNext()){
//        			PageTree b = fi.next();
//        			TreeSet<PageTree> tempts = this.getChildren(b.getRootPid(), conn);
//        			b.setChilds(tempts);
//        		}
//        		
//        		ret=new PageTree(fratelli,false,DBGateway.getPageName(padre, conn),padre);
//        		if(showAreasInFirstLevelPages)
//        			ret = getPadre(ret, conn);
//        	}
//            break;
//        default: // il resto
//        	PageTree p=new PageTree(fratelli,false,DBGateway.getPageName(padre, conn),padre);
//        	zii.remove(p);
//        	zii.add(p);
//        	
//        	long pidNonno=DBGateway.getPapid(padre, conn);
//        	PageTree nonno = new PageTree(zii,false,DBGateway.getPageName(pidNonno, conn),pidNonno);
//    		long level = DBGateway.getPageLevel(pidNonno, 0, conn);
//        	
//        	while(level > 3 ) {
//        		TreeSet<PageTree> ts = new TreeSet<PageTree>();
//        		ts.add(nonno);
//        		long tempPid = DBGateway.getPapid(nonno.getRootPid(), conn);
//        		nonno = new PageTree(ts,false,DBGateway.getPageName(tempPid, conn),tempPid);
//        		level = DBGateway.getPageLevel(tempPid, 0, conn);
//        	}
//        	
//        	if(nonno.getRootPid() == myArea)
//        		ret = nonno;
//        	else{
//	        	ret = new PageTree(null,false,DBGateway.getAreaName(pid, conn),myArea);
//        	}
//	        	
//	        	TreeSet<PageTree> areaChildrenL2 = getChildren(myArea, conn);
//	        	
//	        	if(areaChildrenL2.contains(nonno)){
//	        		areaChildrenL2.remove(nonno);
//	        		areaChildrenL2 = fill2L(areaChildrenL2,nonno,conn);
//	        		areaChildrenL2.add(nonno);
//	    		}
//	        	else if(areaChildrenL2.contains(p)){
//	        		areaChildrenL2.remove(p);
//		        	areaChildrenL2 = fill2L(areaChildrenL2,nonno,conn);
//		        	areaChildrenL2.add(p);
//	        	}
//	        	else areaChildrenL2 = fill2L(areaChildrenL2,nonno,conn);
//
//	        	ret.setChilds(areaChildrenL2);
//        	
//        	break;
//		}
//		return ret;
//	}
	
	
	/**
	 * @deprecated
	 * @param me
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws JOpac2Exception
	 */
//	private PageTree getPadre(PageTree me, Connection conn) throws SQLException, JOpac2Exception {
//		long padre = DBGateway.getPapid(me.getRootPid(), conn);
//		TreeSet<PageTree> fratelli = getChildren(padre, conn);
//		fratelli.remove(me);
//		fratelli.add(me);
//
//		PageTree ret = new PageTree(fratelli,false, padre, conn);
//
//		return ret;
//	}

	/**
	 * @deprecated
	 * @param areaChildrenL2
	 * @param nonno
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws JOpac2Exception
	 */
//	private TreeSet<PageTree> fill2L(TreeSet<PageTree> areaChildrenL2, PageTree nonno, Connection conn) throws SQLException, JOpac2Exception{
//		
//		Iterator<PageTree> ai = areaChildrenL2.iterator();
//    	while(ai.hasNext()) {
//    		PageTree areaChild = ai.next();
//    		TreeSet<PageTree> areaChildChildrenL3 = getChildren(areaChild.getRootPid(), conn);
//    		if(areaChildChildrenL3.contains(nonno)){
//    			areaChildChildrenL3.remove(nonno);
//    			areaChildChildrenL3.add(nonno);
//    		}
//    		areaChild.addAndReplaceChildren(areaChildChildrenL3);
//    	}
//    	return areaChildrenL2;
//
//	}


	/**
	 * @deprecated
	 * @param pid
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws JOpac2Exception
	 */
//	private TreeSet<PageTree> getChildren(long pid, Connection conn) throws SQLException, JOpac2Exception {
//		return getChildren(pid,1,conn);
//	}
	
	
	private void generateTree(Connection conn, int pid, int level, boolean authenticated) throws SQLException, JOpac2Exception, SAXException {
		String pageName=DBGateway.getPageName(datasourceComponent, pid);
		String pageCode=DBGateway.getPageCode(datasourceComponent, pid);
		
		createNavPage(pageName,pageCode,pid,level);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("Select PID,PCode,Name,HasChild,Valid,InSidebar from tblpagine where PaPID=" +  pid + " order by priority,Name");
		try {
			while(rs.next()){
				int dbid = rs.getInt("PID");
				
				Permission tempperm = null;
				if(authenticated) {
					tempperm = Authentication.assignPermissions(datasourceComponent, session, request.getRemoteAddr(), dbid);
					if( (rs.getBoolean("Valid") && rs.getBoolean("InSidebar")) || 
							tempperm.hasPermission(Permission.EDITABLE) || 
							tempperm.hasPermission(Permission.VALIDABLE)) {
						if(rs.getBoolean("haschild")) {
								generateTree(conn, dbid, level+1, authenticated);
						}
						else {
							createNavPage(rs.getString("Name"),rs.getString("PCode"),rs.getInt("PID"),level+1);
							contentHandler.endElement("","navpage","navpage");
						}
					}
				}
				else {
					if(rs.getBoolean("Valid") && rs.getBoolean("InSidebar") && dbid>1) {
						if(rs.getBoolean("haschild") && level < 10) {
							generateTree(conn, dbid, level+1, authenticated);
						}
						else {
							createNavPage(rs.getString("Name"),rs.getString("PCode"),rs.getInt("PID"),level+1);
							contentHandler.endElement("","navpage","navpage");
						}
					}
				}
			}
		}
		catch(SQLException e) {
			
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - NavbarGenerator.generateTree exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - NavbarGenerator.generateTree exception " + fe.getMessage());}

		}
		
//		if(authenticated) {
//			Permission tempperm = Authentication.assignPermissions(session, pid, conn);
//			if(DBGateway.getPageLevel(pid,0, conn)<4 && tempperm.hasPermission(Permission.EDITABLE)){
//				addAddPage(aggiungiPagina, "pagecreate?papid="+pid+"&pid=0&type=section");
//				addAddPage(aggiungiLink, "pagecreate?papid="+pid+"&pid=0&type=externalLink");
//			}
//		}
			

		
		contentHandler.endElement("","navpage","navpage");
	}
	
	
	
	private void createNavPage(String pageName, String pageCode, int pid,
			int level) throws SAXException {
		AttributesImpl hrefAttrs = new AttributesImpl();
		
		hrefAttrs.addCDATAAttribute("name",pageName);
		hrefAttrs.addCDATAAttribute("code",pageCode);
		hrefAttrs.addCDATAAttribute("pid",Integer.toString(pid));
		hrefAttrs.addCDATAAttribute("level",Integer.toString(level));
		hrefAttrs.addCDATAAttribute("link",pageCode);
		if(pid==pageId) {
			hrefAttrs.addCDATAAttribute("selected","true");
		}

		contentHandler.startElement("","navpage","navpage", hrefAttrs);
		
	}


//	private void generateTreeNew(int pid, int level, boolean authenticated, Connection conn) throws SQLException, JOpac2Exception, SAXException {
//		String pageName=DBGateway.getPageName(pid, conn);
//		String pageCode=DBGateway.getPageCode(pid, conn);
//		
//		AttributesImpl hrefAttrs = new AttributesImpl();
//			
//		hrefAttrs.addCDATAAttribute("name",pageName);
//		hrefAttrs.addCDATAAttribute("code",pageCode);
//		hrefAttrs.addCDATAAttribute("pid",Integer.toString(pid));
//		hrefAttrs.addCDATAAttribute("level",Integer.toString(level));
//		hrefAttrs.addCDATAAttribute("link",pageCode);
//		if(pid==pageId) {
//			hrefAttrs.addCDATAAttribute("selected","true");
//		}
//		
//		TreeSet<Page> tree=new TreeSet<Page>();
//
//		contentHandler.startElement("","navpage","navpage", hrefAttrs);
//		
//		Statement st = conn.createStatement();
//		ResultSet rs = st.executeQuery("Select * from tblpagine");
//		while(rs.next()){
//			Page page=new Page(rs.getInt("pid"),rs.getInt("papid"),rs.getString("name"),rs.getString("pcode"),
//					rs.getBoolean("valid"),rs.getBoolean("haschild"),rs.getBoolean("insidebar"),
//					rs.getString("username"),rs.getString("remoteip"),rs.getString("resp"),rs.getDate("insertdate"));
//			
//			
//			int dbid = rs.getInt("PID");
//			
//			Permission tempperm = null;
//			if(authenticated) {
//				tempperm = Authentication.assignPermissions(session, request.getRemoteAddr(), dbid, conn);
//				if( (rs.getBoolean("Valid") && rs.getBoolean("InSidebar")) || 
//						tempperm.hasPermission(Permission.EDITABLE) || 
//						tempperm.hasPermission(Permission.VALIDABLE)) {
//							generateTree(dbid, level+1, authenticated, conn);
//				}
//			}
//			else {
//				if(rs.getBoolean("Valid") && rs.getBoolean("InSidebar")) {
//					generateTree(dbid, level+1, authenticated, conn);
//				}
//			}
//		}
//		
////		if(authenticated) {
////			Permission tempperm = Authentication.assignPermissions(session, request.getRemoteAddr(), pid, conn);
////			if(DBGateway.getPageLevel(pid,0, conn)<4 && tempperm.hasPermission(Permission.EDITABLE)){
////				addAddPage(aggiungiPagina, "pagecreate?papid="+pid+"&pid=0&type=section");
////				addAddPage(aggiungiLink, "pagecreate?papid="+pid+"&pid=0&type=externalLink");
////			}
////		}
//			
//		rs.close();
//		st.close();
//		
//		contentHandler.endElement("","navpage","navpage");
//	}
	
	
	/**
	 * @param pageName
	 * @param link
	 * @throws SAXException
	 */
//	private void addAddPage(String pageName, String link) throws SAXException {
//		AttributesImpl hrefAttrs = new AttributesImpl();
//		
//		hrefAttrs.addCDATAAttribute("name",pageName);
//		hrefAttrs.addCDATAAttribute("link",link);
//
//		contentHandler.startElement("","navpage","navpage", hrefAttrs);
//		contentHandler.endElement("","navpage","navpage");
//	}

	/**
	 * @deprecated
	 * long c = 0 se vogliamo nipoti, 
	 * altrimenti: se vogliamo solo i figli, allora c = 1
	 */
//	private TreeSet<PageTree> getChildren(long tempPid, long c, Connection conn) throws SQLException, JOpac2Exception {
//		
//		TreeSet<PageTree> v = new TreeSet<PageTree>();
//		String remoteaddr=request.getRemoteAddr();
//		Statement st = conn.createStatement();
//		ResultSet rs = st.executeQuery("Select PID,Name,HasChild,Valid,InSidebar from tblpagine where PaPID=" +  tempPid);
//		while(rs.next()){
//			long dbid = rs.getLong("PID");
//			Permission tempperm = Authentication.assignPermissions(session, remoteaddr, dbid, conn);
//			if( (rs.getBoolean("Valid") && rs.getBoolean("InSidebar")) || tempperm.hasPermission(Permission.EDITABLE) || tempperm.hasPermission(Permission.VALIDABLE)){
//				PageTree pt = null;
//				TreeSet<PageTree> v1 = null;
//				if(c==0){
//					v1 = getChildren(dbid,0, conn);
//					//v1.add(new PageTree(new Vector(),true, aggiungiPagina, tempPid));
//				}
//				else v1 = new TreeSet<PageTree>();
//				pt = new PageTree(v1,DBGateway.isLeaf(dbid, remoteaddr, session, conn), rs.getString("Name"), dbid );
//				v.add(pt);
//			}
//		}
//		Permission tempperm = Authentication.assignPermissions(session, remoteaddr, tempPid, conn);
//		if(DBGateway.getPageLevel(tempPid,0, conn)<4 && tempperm.hasPermission(Permission.EDITABLE)){
//			v.add(new PageTree(new TreeSet<PageTree>(),true, PageTree.aggiungiPagina, tempPid));
//			v.add(new PageTree(new TreeSet<PageTree>(),true, PageTree.aggiungiLink, tempPid));
//		}
//			
//		rs.close();
//		st.close();
//		return v;
//	}

	/**
	 * @deprecated
	 * @param tree
	 * @param level
	 * @param conn
	 * @throws SAXException
	 * @throws SQLException
	 */
//	private void processTree(PageTree tree, long level, Connection conn) throws SAXException, SQLException {
//		
//		String text = tree.getRootName();
//		
//		AttributesImpl attrs = new AttributesImpl();
//		
//		
////		AttributesImpl hrefAttrs = new AttributesImpl();
////		
////		hrefAttrs.addCDATAAttribute("name",text); //pageName
////		hrefAttrs.addCDATAAttribute("code",pageCode);
////		hrefAttrs.addCDATAAttribute("pid",Integer.toString(pid));
////		hrefAttrs.addCDATAAttribute("level",Integer.toString(level));
////		hrefAttrs.addCDATAAttribute("link",pageCode);
////		if(pid==pageId) {
////			hrefAttrs.addCDATAAttribute("selected","true");
////		}
//
//		
//		
//		attrs.addCDATAAttribute("text",text);
//		
//		boolean haschild = false;
//		
//		haschild = !(tree.isLeaf());
//		
//		if(tree.getRootName().equals(PageTree.aggiungiPagina)){
//			attrs.addCDATAAttribute("link","pagecreate?papid="+tree.getRootPid()+"&pid=0&type=section");
//			if(tree.getRootPid() == pageId && newpage) attrs.addCDATAAttribute("active","true");
//		}
//		else if(tree.getRootName().equals(PageTree.aggiungiLink)){
//			attrs.addCDATAAttribute("link","pagecreate?papid="+tree.getRootPid()+"&pid=0&type=externalLink");
//			if(tree.getRootPid() == pageId && newpage) attrs.addCDATAAttribute("active","true");
//		}
//		else{
//			if(haschild && level > 1){
//				if(tree.getChilds().isEmpty() || (!toggle && tree.getRootPid() == pageId)){
//					attrs.addCDATAAttribute("img2","closed");
//				}
//				else
//					attrs.addCDATAAttribute("img2","opened");
//			}
//			
//			String link = "pageview?pid="+tree.getRootPid();
//			if(toggle && pageId == tree.getRootPid())link = link + "&amp;toggle=false";
//			
//			attrs.addCDATAAttribute("link",link);
//			if(activePids.contains(Long.valueOf(tree.getRootPid()))){
//				attrs.addCDATAAttribute("active","true");
//			}
//		}
//		if(level>0)
//			contentHandler.startElement("","navpage","navpage", attrs);
//		
//		Iterator<PageTree> iter = tree.getChilds().iterator();
//		long childLevel = level + 1;
//		while(iter.hasNext()){
//			PageTree pt = (PageTree)iter.next();
//			processTree(pt,childLevel, conn);
//		}	
//		if(level>0)
//			contentHandler.endElement("","navpage","navpage");
//	}

}
