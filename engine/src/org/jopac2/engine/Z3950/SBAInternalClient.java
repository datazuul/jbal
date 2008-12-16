/*******************************************************************************
 *
 *  JOpac2 (C) 2002-2007 JOpac2 project
 *
 *     This file is part of JOpac2. http://jopac2.sourceforge.net
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
 *******************************************************************************/

/**
 * @author Iztok Cergol
 * @version 18/08/2004
 */

package org.jopac2.engine.Z3950;

import java.util.*;


// Information Retrieval Interfaces
import com.k_int.IR.*;
import java.io.IOException;

import org.jopac2.engine.Z3950.IRClient.Z3950Origin;
import org.jopac2.engine.utils.QueryUtil;
import org.jopac2.engine.utils.SingleSearch;
import org.jopac2.utils.Parametro;
import org.jopac2.utils.RecordItem;
import org.jopac2.utils.Utils;

public class SBAInternalClient implements Observer {
	private int RUNNING=1;
	private int status=RUNNING;
	private int nfragments=-1;
	private Vector<RecordItem> result=null;
	private boolean stop=false;
	
	
	
	public SBAInternalClient() {
		super();
	}
	
	public void stop() {
		stop=true;
	}
	
	public Vector<RecordItem> getRecords() {
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Vector<RecordItem> getRecords(SingleSearch ss,SearchTask st) {
		result = new Vector<RecordItem>();
		Enumeration<InformationFragment> rs_enum = st.getTaskResultSet().elements();

		while (!stop && rs_enum.hasMoreElements()) {
			try {
				InformationFragment f = rs_enum.nextElement();
				result.add(new RecordItem(ss.getHost(), ss.getPrefix(),
						(String) f.getOriginalObject(), ss.getDataType(),
						ss.getDataSyntax(), ss.getDatabasecode(), null));
			} catch (java.lang.NullPointerException npe) {
				npe.printStackTrace();
			}// throw new java.io.IOException();}
		}
		

		
		return result;
	}

	@SuppressWarnings("unchecked")
	public SearchTask getJOpac2SearchData(SingleSearch ss, String sq,
			String syntax) throws TimeoutExceededException, IOException {
		
		Vector<Parametro> queryVector = Utils.queryToVector(Utils.removeAttribute(sq, "stemmer"));
		// Package ir_package = Package.getPackage("com.k_int.IR");

		// OIDRegister reg = OIDRegister.getRegister();

		Properties p = new Properties();

		String querySyntax = ss.getDataSyntax();

		p.put("ServiceHost", ss.getHost());
		p.put("ServicePort", (new Integer(ss.getPort())).toString());
		p.put("service_short_name", "demo");
		p.put("service_long_name", "demo");
		p.put("default_record_syntax", querySyntax.toLowerCase());
		p.put("default_element_set_name", "F");
		p.put("service_user_principal", ss.getUserId());
		p.put("service_user_credentials", ss.getPassword());
		p.put("service_auth_type", ss.getAuthType());
		p.put("charset", "UTF-8");

/*		// Remember you don't really need to do this...
		Observer fragment_count_observer = new Observer() {
			public void update(Observable o, Object arg) {
				IREvent e = (IREvent) arg;

				if (e.event_type == IREvent.SOURCE_RESET) {
					// System.err.println("TIME: Sub Fragment source reset");
				} else if (e.event_type == IREvent.FRAGMENT_COUNT_CHANGE) {
					System.err
							.println("TIME: Number of fragments has changed to "
									+ e.event_info);
				}
			}
		};*/

		Observer[] all_observers = new Observer[] { this }; //fragment_count_observer, 

		Searchable s = new Z3950Origin();
		s.init(p);

		IRQuery e = new IRQuery();
		e.collections.add(ss.getPrefix());

		e.hints.put("record_syntax", querySyntax.toLowerCase());

		e.query = new com.k_int.IR.QueryModels.PrefixString(new QueryUtil()
				.costruisciQuery(queryVector));

		System.err.println("Searching");
		SearchTask st=null;
		try {
			st = (SearchTask) s.createTask(e, null, all_observers);
			status=st.evaluate((int)ss.getTimeOut());
			// int status = st.evaluate(40000);
			// System.out.println("+++ Task Status Code = " +
			// st.getTaskStatusCode());

			// System.err.println("Private task status:
			// "+st.lookupPrivateStatusCode(st.getPrivateTaskStatusCode()));
		} catch (SearchException se) {
			se.printStackTrace();
		} catch (com.k_int.IR.TimeoutExceededException tee) {
			// tee.printStackTrace();
		}
		
		return st;
	}
	
	public void destroy() {
		if(result!=null) {
			result.removeAllElements();
			result=null;
		}
	}
	
	
	public int getStatus() {
		return status;
	}
	
	public int getNFragments() {
		return nfragments;
	}
	
	public void update(Observable o, Object arg) {
		IREvent e = (IREvent) arg;

		if (e.event_type == IREvent.SOURCE_RESET) {
			// System.err.println("TIME: Sub Fragment source reset");
		} else if (e.event_type == IREvent.FRAGMENT_COUNT_CHANGE) {
			nfragments=(Integer)e.event_info;
		}
	}
}