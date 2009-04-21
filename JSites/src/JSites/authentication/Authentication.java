package JSites.authentication;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.webapps.authentication.user.UserState;

import JSites.utils.DBGateway;

public class Authentication {
	
	@SuppressWarnings("unchecked")
	public static String getUserData(Session s, String attribute) {
		String UID="";
		UserState uState = (UserState)s.getAttribute("org.apache.cocoon.webapps.authentication.components.DefaultAuthenticationManager/UserStatus");
		if(uState!=null)
			try {
				HashMap<String,String> map = (HashMap<String,String>)uState.getHandler("JSitesHandler").getContext().getAttribute("cachedmap");
				UID = (String)map.get(attribute);
			} catch (ProcessingException e) {}
		return UID;
	}

	private static String getUsername(Session s) {
		String UID=getUserData(s,"ID");
		
		if(UID == null)UID="guest";
		
		return UID;
	}
	
	public static Permission assignPermissions(Session session, long pid, Connection conn) throws SQLException {
		
		Permission ret = new Permission();
		ret = DBGateway.getPermission(getUsername(session), pid, conn);
		return ret;
			
	}

}
