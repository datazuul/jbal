package JSites.action;

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
import java.util.*;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

import JSites.utils.DBGateway;

public class SaveOrder extends PageAction {
	
	private Vector<String> oCids = null;
	private Vector<String> oNums = null;
	
	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		
		super.act(redirector, resolver, objectModel, source, parameters);
		
		if(parameters.getParameter("containerType").equals("content")){
			
			oCids = new Vector<String>();
			oNums = new Vector<String>();
			boolean orderOk = true;
			String pid = "";

			
			Enumeration<String> en = o.getParameterNames();
			while(en.hasMoreElements()){
				String name = en.nextElement();
				if(name.matches("cid\\d+")){
					String value = o.getParameterValues(name)[0];
					if(oNums.contains(value)){
						orderOk = false;
						break;
					}
					else{
						oCids.add(name);
						oNums.add(value);
					}
				}
				else if(name.matches("pid")){
					pid = o.getParameterValues(name)[0];
					if(!(pid.matches("\\d+"))) pid = "";
				}
			}
			
			if(oNums.size() != oCids.size())
				orderOk = false;
			
			if(orderOk){
				Connection conn = null;
				try{
					conn = this.getConnection(dbname);
					DBGateway.orderComponents(oCids, oNums, pid, conn);
				}catch(Exception e){e.printStackTrace();}
				
				try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}

			}
			
			oCids.clear();
			oNums.clear();
		}
		return objectModel;
	}
}
