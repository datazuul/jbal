package JSites.authentication;

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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.webapps.authentication.user.UserState;
import org.jopac2.utils.JOpac2Exception;

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

	public static String getUsername(Session s) {
		String UID=getUserData(s,"ID");
		
		if(UID == null)UID="guest";
		
		return UID;
	}
	
	public static Permission assignPermissions(Session session, long pid, Connection conn) throws SQLException, JOpac2Exception {
		
		Permission ret = new Permission();
		ret = DBGateway.getPermission(getUsername(session), pid, conn);
		return ret;
			
	}

}
