package org.jopac2.engine.NewSearch;
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
import java.sql.Connection;
import java.util.Vector;

import org.jopac2.engine.NewSearch.parser.QuerySearch;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.dbGateway.StaticDataComponent;
import org.jopac2.engine.parserRicerche.parser.exception.ExpressionException;
import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.jbal.RecordInterface;

/**
 * 
 * @author albert
 *
 */
public class DoSearchNew {

	private Vector<Long> resultSet;
	private Connection conn = null;
	protected StaticDataComponent staticdata;
	//private Hashtable classi;
	//private Hashtable valori;
	
	public DoSearchNew(Connection c, StaticDataComponent d) {
		this.conn = c;
		this.staticdata = d;
		//this.classi = (Hashtable) d.getClassi();
		//this.valori = (Hashtable) d.getValori();
		this.resultSet = new Vector<Long>();
	}
	
	public SearchResultSet executeSearch(String query, boolean useStemmer) throws ExpressionException {
		SearchResultSet result = new SearchResultSet();
		result.setStemmer(useStemmer);
		long start_time = System.currentTimeMillis();
		resultSet.clear();		
		result.setQuery(query);
		
		if(query!=null && query.length()>0) {
			QuerySearch qs=new QuerySearch(query,this.conn); // this.staticdata
			qs.optimize(useStemmer);  // ottimizza ed esegue la query
			result.setOptimizedQuery(qs.toString());
			result.setItemCardinalities(qs.getQueryCardinality());				
			resultSet=qs.getResultsAsVector();
		}
		else {
			resultSet=DbGateway.getIdNotizie(conn);
		}
		//System.out.println("risultato:"+resultSet);
		double t = (System.currentTimeMillis() - start_time) / (1000.0);
		result.setQueryTime(t);
		result.setQueryCount(resultSet.size());
		result.setRecordIDs(resultSet);
		return result;
	}

	public RecordInterface getRecord(Long t) {
		return DbGateway.getNotiziaByJID(conn, t.longValue());
	}
}
