package JSites.transformation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.AttributesImpl;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.utils.JOpac2Exception;
import org.xml.sax.SAXException;

import JSites.utils.DBGateway;

import JSites.authentication.Authentication;
import JSites.authentication.Permission;

public class RedirectNA extends MyAbstractPageTransformer {
	
	private boolean redirect = true;
	private boolean isView = false;
	private Permission permission;
	private String redirURL = "pageview?pid=1";
	
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		 super.setup(arg0, arg1, arg2, arg3);
		 redirect = true;
		 String q = o.getRequestURI();
		 if(q.contains("view"))
			 isView = true;
		 Connection conn=null;
		 try{
			 long pageId=1;
			 String pCode = o.getParameter("pcode");
			 String stringPid = o.getParameter("pid");
			 if(stringPid != null && !(stringPid.equals("0"))){
				 if(stringPid.matches("\\d+"))
					 pageId = Long.parseLong(stringPid);
				 else {
					 System.out.println("Ooouuu, i ga richiesto >> pid="+stringPid + " << ... ma semo mati?");
				 }
			 }
			 else if(o.getParameter("papid")!=null && (permission.hasPermission(Permission.ACCESSIBLE)||permission.hasPermission(Permission.EDITABLE))){
				 redirect = false;
			 }
			 
			 conn = this.getConnection(dbname);
			
			 try {
				if (pCode != null)
					pageId = DBGateway.getPidFrom(pCode, conn);

				Session session = this.sessionManager.getSession(true);
				permission = Authentication.assignPermissions(session, pageId, conn);
//				try {
//					permission = Authentication.assignPermissions(session, pageId, conn);
//				} catch (JOpac2Exception e) {
//					e.printStackTrace();
//				}
				String readRedir = DBGateway.getRedirectURL(pageId, conn);
				if (readRedir != null && isView
						&& permission.hasPermission(Permission.ACCESSIBLE)) {
					redirURL = readRedir;
					redirect = true;

					if (permission.hasPermission(Permission.EDITABLE)
							|| permission.hasPermission(Permission.VALIDABLE)) {
					} else {
						conn.close();
						return;
					}
				}
			} catch (SQLException e) {
				if (conn != null && !e.getMessage().contains("User/Username exception")) {
					try {
						DBGateway.caricaDB(conn);
					}
					catch(Exception ee) {
						ee.printStackTrace();
					}
					finally {
						conn.close();
					}
				}
				e.printStackTrace();
			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println("Non ho potuto chiudere la connessione");
			}	 
			 
		 }
		 catch(Exception e){e.printStackTrace();}
		 if(permission==null)permission=new Permission();
		 if(permission.hasPermission(Permission.ACCESSIBLE)){
			 redirect = false;
		 }
		 
		 if(!redirect){
			 try{
				 String uri = o.getParameter("action");
				 if(uri!=null){
					 if(uri.contains("save")){
						 String pid = o.getParameter("pid");
						 if(pid==null)
							 pid = o.getParameter("papid");
						 if(pid!=null){
							 redirURL = "pageview?pid="+pid;
							 redirect = true;
						 }
					 }
				 }
			 }
			 catch(Exception e){e.printStackTrace();}
		 }

	}
	
	
	public void endDocument() throws SAXException{
		if(redirect){
			AttributesImpl attr=new AttributesImpl();
			//attr.addCDATAAttribute("internal","true");
			super.startElement("","redirect","redirect",attr);
			super.characters(redirURL.toCharArray(), 0, redirURL.length());
			super.endElement("","redirect","redirect");
		}
		super.endDocument();
	}
	
}
