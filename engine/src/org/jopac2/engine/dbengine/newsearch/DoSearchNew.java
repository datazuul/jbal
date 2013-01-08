package org.jopac2.engine.dbengine.newsearch;
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
import java.sql.SQLException;
import java.util.Vector;

import org.jopac2.engine.dbengine.dbGateway.DbGateway;
import org.jopac2.engine.dbengine.dbGateway.StaticDataComponent;
import org.jopac2.engine.parserricerche.parser.exception.ExpressionException;
import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.jbal.RecordInterface;

/**
 * 
 * @author albert
 *
 */
public class DoSearchNew {

	private Vector<Long> resultSet;
	protected Connection conn = null;
//	protected StaticDataComponent staticdata;
	private String[] channels=null;
	protected String catalog="";
	
	public DoSearchNew(Connection c, String catalog) {
		this.conn = c;
//		this.staticdata = d;
		this.catalog = catalog;
		this.resultSet = new Vector<Long>();
		RecordInterface ma=DbGateway.getNotiziaByJID(c, catalog, 1);
		channels=ma.getChannels();
		ma.destroy();
	}
	
	public SearchResultSet executeSearch(String query, boolean useStemmer) throws ExpressionException, SQLException {
		SearchResultSet result = new SearchResultSet();
		result.setStemmer(useStemmer);
		long start_time = System.currentTimeMillis();
		resultSet.clear();		
		
		query = removeEmptyAttributes(query);
		
		result.setQuery(query);
		
		if(query!=null && query.length()>0) {
			QuerySearch qs=new QuerySearch(query,this.conn,catalog,channels); // this.staticdata
			qs.optimize(useStemmer);  // ottimizza ed esegue la query
			result.setOptimizedQuery(qs.toString());
			result.setItemCardinalities(qs.getQueryCardinality());				
			resultSet=qs.getResultsAsVector();
		}
		else {
			resultSet=DbGateway.getIdNotizie(conn,catalog);
		}
		//System.out.println("risultato:"+resultSet);
		double t = (System.currentTimeMillis() - start_time) / (1000.0);
		result.setQueryTime(t);
		result.setQueryCount(resultSet.size());
		result.setRecordIDs(resultSet);
		try {
			int idQ=DbGateway.saveQuery(conn, catalog, "", result);
			result.setQueryID(idQ);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Rimuove attributi di ricerca vuoti o totali (ANY=%)
	 * @param query
	 * @return
	 */
	private String removeEmptyAttributes(String query) {
		if(query!=null) {
			String[] parts=query.split("&");
			query="";
			for(int j=0;j<parts.length;j++) {
			
				String[] attrs=parts[j].split("\\|");
				
				for(int i=0;i<attrs.length;i++) {
					String[] av=attrs[i].split("=");
					if(av.length==2) {
						String nv=av[1].replaceAll("%", "").replaceAll("\\*", "").trim();
						if(nv.length()>0) query=query+av[0]+"="+av[1].trim()+"|";
					}
				}
				if(query.endsWith("|")) query=query.substring(0, query.length()-1);
				query=query+"&";
			}
		}
		if(query.endsWith("&")) query=query.substring(0, query.length()-1);
		return query;
	}

	public RecordInterface getRecord(Long t) {
		return DbGateway.getNotiziaByJID(conn, catalog, t.longValue());
	}
}
