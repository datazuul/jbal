package JSites.action;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2010 JOpac2 project
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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;

public class RedirectFromRequestRestoreAction implements Action, Composable, Disposable {
	
	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, SourceResolver resolver,
			Map objectModel, String source, Parameters params) {	

		Hashtable<String,String> lastdata=null;
		Request request = ObjectModelHelper.getRequest(objectModel);
		
		Session session=request.getSession();
		
		lastdata=(Hashtable<String,String>)session.getAttribute("redirectfrom");
		
		
		if(lastdata!=null) {
			Enumeration<String> ee=lastdata.keys();
			while(ee.hasMoreElements()) {
				String s=ee.nextElement();
				request.setAttribute(s, lastdata.get(s));			
			}

			session.removeAttribute("redirectfrom");
		}
		return objectModel;
	}
	
	public void compose(ComponentManager cm) throws ComponentException {
		
    }
	
	public void dispose() {
	}

}
