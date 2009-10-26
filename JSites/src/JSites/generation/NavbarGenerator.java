package JSites.generation;

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
import org.xml.sax.SAXException;

import JSites.authentication.Authentication;
import JSites.authentication.Permission;

import JSites.utils.DBGateway;
import JSites.utils.PageTree;

public class NavbarGenerator extends MyAbstractPageGenerator {

	private Vector<Long> activePids = null;
	private boolean newpage = false;
	private Session session = null;
	Permission permission;
	private boolean toggle = true;
	private boolean showAreasInFirstLevelPages = false;
	
	public void generate() throws SAXException {
	
		activePids = new Vector<Long>();

		contentHandler.startDocument();
		
		contentHandler.startElement("","navigator","navigator", new AttributesImpl());
		
		contentHandler.startElement("","id","id", new AttributesImpl());
		contentHandler.characters("navbar".toCharArray(),0,"navbar".length());
		contentHandler.endElement("","id","id");
		
		Connection conn = null;
		try{
			conn = this.getConnection(dbname); 
			activePids.add(Long.valueOf(pageId));
			
 			try{ newpage = Boolean.parseBoolean((String) request.getParameter("newpage")); }
 			catch(Exception e){ newpage = false;}

			PageTree tree = createPageTree(pageId, conn);
			
			contentHandler.startElement("","area","area", new AttributesImpl());
			
			if(pageId!=1){
				String areaName = DBGateway.getAreaName(pageId, conn);
				AttributesImpl hrefAttrs = new AttributesImpl();
				hrefAttrs.addCDATAAttribute("href","pageview?pid="+DBGateway.getStartFromPid(pageId, conn));
				contentHandler.startElement("","a","a", hrefAttrs);
				contentHandler.characters(areaName.toCharArray(),0,areaName.length());
				contentHandler.endElement("","a","a");	
			}
			contentHandler.endElement("","area","area");
						
			processTree(tree,0,conn);

		}catch(Exception e) {e.printStackTrace();}
			
		try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}
		
		contentHandler.endElement("","navigator","navigator");
		contentHandler.endDocument();
	}
	
	@SuppressWarnings("unchecked")
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
		
		String showAreasInFirstLevelPagesRead = this.request.getParameter("showAreasInFirstLevelPages");
		if(showAreasInFirstLevelPagesRead!=null){
			showAreasInFirstLevelPages = Boolean.parseBoolean(showAreasInFirstLevelPagesRead);
		}
		else showAreasInFirstLevelPages = false;
	}

	
	private PageTree createPageTree(long pid, Connection conn) throws SQLException, JOpac2Exception {
		long padre = DBGateway.getPapid(pid, conn);
		TreeSet<PageTree> zii = getChildren(DBGateway.getPapid(padre,conn),conn);
		TreeSet<PageTree> figli = null;
		
		figli=getChildren(pid,conn);
		
		TreeSet<PageTree> fratelli = getChildren(padre, conn);
		PageTree ret = null;
		String nomePagina=DBGateway.getPageName(pid, conn);
		PageTree me=new PageTree(figli,figli.isEmpty(),nomePagina,pid);
		
		if(!toggle && (!figli.isEmpty())){
			me.setChilds(new TreeSet<PageTree>());
			me.setLeaf(false);
		}

		fratelli.remove(me); // apparentemente non fa nulla, invece toglie me istanziato senza figli e...
    	fratelli.add(me);	 // .... mi aggiunge istanziato con i figli, xche' il compareTo guarda solo il nome

		int livello = DBGateway.getPageLevel(pid, 0, conn);
		long myArea=DBGateway.getStartFromPid(pid, conn);
		
		switch(livello) {
		case 0: // homepage, visualizza soltanto i miei figli
            ret=me;
            break;
        case 1: // aree
        	
        	Iterator<PageTree> i=figli.iterator();
        	while(i.hasNext()) {
        		PageTree figlio=i.next();
      			figlio.setChilds(getChildren(figlio.getRootPid(),conn));
        	}
        	
        	if(showAreasInFirstLevelPages)
        		ret=new PageTree(fratelli,false,DBGateway.getPageName(padre, conn),padre);
        	else
        		ret=me;
        		
        		
            break;
        case 2: {// sottoaree
        		Iterator<PageTree> fi = fratelli.iterator();
        		while(fi.hasNext()){
        			PageTree b = fi.next();
        			TreeSet<PageTree> tempts = this.getChildren(b.getRootPid(), conn);
        			b.setChilds(tempts);
        		}
        		
        		ret=new PageTree(fratelli,false,DBGateway.getPageName(padre, conn),padre);
        		if(showAreasInFirstLevelPages)
        			ret = getPadre(ret, conn);
        	}
            break;
        default: // il resto
        	PageTree p=new PageTree(fratelli,false,DBGateway.getPageName(padre, conn),padre);
        	zii.remove(p);
        	zii.add(p);
        	
        	long pidNonno=DBGateway.getPapid(padre, conn);
        	PageTree nonno = new PageTree(zii,false,DBGateway.getPageName(pidNonno, conn),pidNonno);
    		long level = DBGateway.getPageLevel(pidNonno, 0, conn);
        	
        	while(level > 3 ) {
        		TreeSet<PageTree> ts = new TreeSet<PageTree>();
        		ts.add(nonno);
        		long tempPid = DBGateway.getPapid(nonno.getRootPid(), conn);
        		nonno = new PageTree(ts,false,DBGateway.getPageName(tempPid, conn),tempPid);
        		level = DBGateway.getPageLevel(tempPid, 0, conn);
        	}
        	
        	if(nonno.getRootPid() == myArea)
        		ret = nonno;
        	else{
	        	ret = new PageTree(null,false,DBGateway.getAreaName(pid, conn),myArea);
        	}
	        	
	        	TreeSet<PageTree> areaChildrenL2 = getChildren(myArea, conn);
	        	
	        	if(areaChildrenL2.contains(nonno)){
	        		areaChildrenL2.remove(nonno);
	        		areaChildrenL2 = fill2L(areaChildrenL2,nonno,conn);
	        		areaChildrenL2.add(nonno);
	    		}
	        	else if(areaChildrenL2.contains(p)){
	        		areaChildrenL2.remove(p);
		        	areaChildrenL2 = fill2L(areaChildrenL2,nonno,conn);
		        	areaChildrenL2.add(p);
	        	}
	        	else areaChildrenL2 = fill2L(areaChildrenL2,nonno,conn);

	        	ret.setChilds(areaChildrenL2);
        	
        	break;
		}
		return ret;
	}
	
	private PageTree getPadre(PageTree me, Connection conn) throws SQLException, JOpac2Exception {
		long padre = DBGateway.getPapid(me.getRootPid(), conn);
		TreeSet<PageTree> fratelli = getChildren(padre, conn);
		fratelli.remove(me);
		fratelli.add(me);

		PageTree ret = new PageTree(fratelli,false, padre, conn);

		return ret;
	}

	private TreeSet<PageTree> fill2L(TreeSet<PageTree> areaChildrenL2, PageTree nonno, Connection conn) throws SQLException, JOpac2Exception{
		
		Iterator<PageTree> ai = areaChildrenL2.iterator();
    	while(ai.hasNext()) {
    		PageTree areaChild = ai.next();
    		TreeSet<PageTree> areaChildChildrenL3 = getChildren(areaChild.getRootPid(), conn);
    		if(areaChildChildrenL3.contains(nonno)){
    			areaChildChildrenL3.remove(nonno);
    			areaChildChildrenL3.add(nonno);
    		}
    		areaChild.addAndReplaceChildren(areaChildChildrenL3);
    	}
    	return areaChildrenL2;

	}


	private TreeSet<PageTree> getChildren(long pid, Connection conn) throws SQLException, JOpac2Exception {
		return getChildren(pid,1,conn);
	}

	/*
	 * long c = 0 se vogliamo nipoti, 
	 * altrimenti: se vogliamo solo i figli, allora c = 1
	 */
	private TreeSet<PageTree> getChildren(long tempPid, long c, Connection conn) throws SQLException, JOpac2Exception {
		
		TreeSet<PageTree> v = new TreeSet<PageTree>();
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("Select PID,Name,HasChild,Valid,InSidebar from tblpagine where PaPID=" +  tempPid);
		while(rs.next()){
			long dbid = rs.getLong("PID");
			Permission tempperm = Authentication.assignPermissions(session, dbid, conn);
			if( (rs.getBoolean("Valid") && rs.getBoolean("InSidebar")) || tempperm.hasPermission(Permission.EDITABLE) || tempperm.hasPermission(Permission.VALIDABLE)){
				PageTree pt = null;
				TreeSet<PageTree> v1 = null;
				if(c==0){
					v1 = getChildren(dbid,1, conn);
					//v1.add(new PageTree(new Vector(),true, aggiungiPagina, tempPid));
				}
				else v1 = new TreeSet<PageTree>();
				pt = new PageTree(v1,DBGateway.isLeaf(dbid, session, conn), rs.getString("Name"), dbid );
				v.add(pt);
			}
		}
		Permission tempperm = Authentication.assignPermissions(session, tempPid, conn);
		if(DBGateway.getPageLevel(tempPid,0, conn)<4 && tempperm.hasPermission(Permission.EDITABLE)){
			v.add(new PageTree(new TreeSet<PageTree>(),true, PageTree.aggiungiPagina, tempPid));
			v.add(new PageTree(new TreeSet<PageTree>(),true, PageTree.aggiungiLink, tempPid));
		}
			
		rs.close();
		st.close();
		return v;
	}

	private void processTree(PageTree tree, long level, Connection conn) throws SAXException, SQLException {
		
		String text = tree.getRootName();
		
		AttributesImpl attrs = new AttributesImpl();
		attrs.addCDATAAttribute("text",text);
		
		boolean haschild = false;
		
		haschild = !(tree.isLeaf());
		
		if(tree.getRootName().equals(PageTree.aggiungiPagina)){
			attrs.addCDATAAttribute("link","pagecreate?papid="+tree.getRootPid()+"&pid=0&type=section");
			if(tree.getRootPid() == pageId && newpage) attrs.addCDATAAttribute("active","true");
		}
		else if(tree.getRootName().equals(PageTree.aggiungiLink)){
			attrs.addCDATAAttribute("link","pagecreate?papid="+tree.getRootPid()+"&pid=0&type=externalLink");
			if(tree.getRootPid() == pageId && newpage) attrs.addCDATAAttribute("active","true");
		}
		else{
			if(haschild && level > 1){
				if(tree.getChilds().isEmpty() || (!toggle && tree.getRootPid() == pageId)){
					attrs.addCDATAAttribute("img2","closed");
				}
				else
					attrs.addCDATAAttribute("img2","opened");
			}
			
			String link = "pageview?pid="+tree.getRootPid();
			if(toggle && pageId == tree.getRootPid())link = link + "&amp;toggle=false";
			
			attrs.addCDATAAttribute("link",link);
			if(activePids.contains(Long.valueOf(tree.getRootPid()))){
				attrs.addCDATAAttribute("active","true");
			}
		}
		if(level>0)
			contentHandler.startElement("","voce","voce", attrs);
		
		Iterator<PageTree> iter = tree.getChilds().iterator();
		long childLevel = level + 1;
		while(iter.hasNext()){
			PageTree pt = (PageTree)iter.next();
			processTree(pt,childLevel, conn);
		}	
		if(level>0)
			contentHandler.endElement("","voce","voce");
	}

}
