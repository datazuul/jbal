package JSites.action;

import java.sql.Connection;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

import JSites.utils.DBGateway;

public class ErasePage extends PageAction {
	
	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		
		super.act(redirector, resolver, objectModel, source, parameters);
		long pid = Long.parseLong(o.getParameter("pid"));
		
		Connection conn = null;
		
		try{
			conn = this.getConnection(dbname);
			DBGateway.erasePage(pid, conn);
		}catch(Exception e){e.printStackTrace();}
		
		try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}

		return objectModel;
	}
	
	


}
