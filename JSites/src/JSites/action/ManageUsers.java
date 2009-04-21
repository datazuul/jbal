package JSites.action;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

import JSites.authentication.Permission;

import JSites.utils.DBGateway;
import JSites.utils.Util;

public class ManageUsers extends PageAction {
	
	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		
		super.act(redirector, resolver, objectModel, source, parameters);
		
		if(parameters.getParameter("containerType").equals("content")){
			Connection conn= null;
			try{
				conn = this.getConnection(this.dbname);
				String pid = o.getParameter("pid"); 
				Enumeration<?> params = o.getParameterNames();
				while(params.hasMoreElements()){
					String name = (String)params.nextElement();
					if(name.startsWith("utente")){
						String index = name.replaceAll("utente","");
		
						String username = o.getParameter(name);
						String editable = o.getParameter("editable" + index);
						String validable = o.getParameter("validable" + index);
						
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
	
						if(pid != null && username.length()>0)
							DBGateway.setPermission(Long.parseLong(pid), username, p, conn);					
					}
					else if(name.equals("pageTitle")){
						String titolo = Util.readRequestParameter(o.getParameter(name));
						DBGateway.setPageName(Long.parseLong(pid), titolo, conn);
					}
					else if(name.equals("pageCode")){
						String code = o.getParameter(name);
						DBGateway.setPageCode(Long.parseLong(pid), code, conn);
					}
				}
				
			}catch(Exception e){e.printStackTrace();}
		
			try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}
		}
		
		return objectModel;
	}
	
}
