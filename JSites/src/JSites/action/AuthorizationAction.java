package JSites.action;

import java.sql.Connection;
import java.util.Map;

import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.webapps.session.SessionManager;

import JSites.authentication.Authentication;
import JSites.authentication.Permission;
import JSites.utils.DBGateway;

/*
 * Aggiunge un parametrinoinoino che indica il Permission Code dell'utente relativamente alla pgina
 */

public class AuthorizationAction extends PageAction  {
	
	private SessionManager sessionManager;
	
	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		
		super.act(redirector, resolver, objectModel, source, parameters);
		if(dbname==null)
			return objectModel;
		
		Connection conn = null;
		
		try{
			conn = this.getConnection(dbname);
			long pageId;
			String pcode = o.getParameter("pcode");
			
			if(pcode != null) pageId = DBGateway.getPidFrom(pcode, conn);
			else{
				String pid = o.getParameter("pid");
				if(pid == null){
					if(parameters.isParameter("pid"))
						pid = parameters.getParameter("pid");
					else pid="1";
				}
				pageId = Long.parseLong(pid);
			}
			
			String permissionCode = "";
			
			Session session = sessionManager.getSession(true);
			Permission permission = Authentication.assignPermissions(session, pageId, conn);
			
			permissionCode = String.valueOf((int)permission.getPermissionCode());
			
			objectModel.put("permissionCode", permissionCode);
		} catch(Exception e) {}
		
		try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}
		
		return objectModel;
	}
	
	
	public void compose(ComponentManager manager) throws ComponentException {
		super.compose(manager);
        sessionManager = (SessionManager) manager.lookup(SessionManager.ROLE);
    }
	
}
