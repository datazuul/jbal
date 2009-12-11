package JSites.transformation.catalogSearch;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.jopac2.engine.NewSearch.DoSearchNew;
import org.jopac2.engine.NewSearch.NewItemCardinality;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.dbGateway.StaticDataComponent;
import org.jopac2.engine.listSearch.ListSearch;
import org.jopac2.engine.parserRicerche.parser.exception.ExpressionException;
import org.jopac2.engine.utils.SearchResultSet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.apache.cocoon.xml.AttributesImpl;

import JSites.transformation.MyAbstractPageTransformer;
import JSites.utils.Util;


public class CatalogSearchTransformer extends MyAbstractPageTransformer {
	
	String catalogConnection = null, dbType=null;
	StringBuffer sb = new StringBuffer();
	String dbUrl=null;
	
	boolean readCatalogConnection = false, readDbType = false;
	//boolean readLinks = false;
	
	@SuppressWarnings("unchecked")
	@Override
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		super.setup(arg0, arg1, arg2, arg3);
		catalogConnection = "";
	}


	@Override
	public void startElement(String uri, String loc, String raw, Attributes a) throws SAXException {
		if(loc.equals("catalogConnection")){
			readCatalogConnection = true;
		}
		else if(loc.equals("dbType")){
			readDbType = true;
		}
		else if(loc.equals("catalogSearch"))
			super.startElement("", "root", "root", a);
		else
			super.startElement(uri, loc, raw, a);
	}

	@Override
	public void characters(char[] c, int start, int len) throws SAXException {
		if(readCatalogConnection || readDbType)
			sb.append(c,start,len);
		else
			super.characters(c, start, len);
	}

	@Override
	public void endElement(String uri, String loc, String raw) throws SAXException {
		if(loc.equals("catalogConnection")){
			readCatalogConnection = false;
			catalogConnection = sb.toString().trim();
			sb.delete(0, sb.length());
		}
		else if(loc.equals("dbType")){
			readDbType = false;
			dbType = sb.toString().trim();
			sb.delete(0, sb.length());
		}
		else if(loc.equals("catalogSearch")) {
			try {
				throwResults();
			} 
			catch (Exception e) { e.printStackTrace();} 
			super.endElement("", "root", "root");
		}
		else {
			super.endElement(uri, loc, raw);
		}
	}

	private void throwResults() throws ComponentException, SQLException, SAXException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Connection conn=null;
		
		conn = getConnection(dbname);

		SearchResultSet result=null;
		
		String catalogQuery = getQuery(o);
		String orderBy= o.getParameter("orderby");
		
		if(checkListParameter("list")) {
			String[] list=getListParameter("list");
			if(list[1]!=null && list[1].length()>0) {
				result=ListSearch.listSearch(conn, catalogConnection, list[0], list[1], 100);
				throwField("listRecord", list[0]);
				
				throwResults(conn, catalogQuery, result);
			}
		}
		else if(checkListParameter("nlist")) {
			String[] list=getListParameter("nlist");
			if(list[1]!=null && list[1].length()>0) {
				long sjid=Long.parseLong(list[1]);
				result=ListSearch.listSearch(conn, catalogConnection, list[0], sjid, 100);
				throwField("listRecord", list[0]);
				
				throwResults(conn, catalogQuery, result);
			}
		}
		else if(catalogQuery!=null && catalogQuery.length()>0) {
			catalogQuery = catalogQuery.replaceAll("\\(.*\\)", "").trim();
			if(catalogQuery.toLowerCase().startsWith("jid=")) {
				int e = catalogQuery.indexOf("&");
				if(e<0)e=catalogQuery.length();
				
				long id = Long.parseLong( catalogQuery.substring(4, e) );
				result = new SearchResultSet();
				Vector<Long> recordIDs = new Vector<Long>();
				recordIDs.add(id);
				result.setRecordIDs(recordIDs);
			}
			else if(catalogQuery.toLowerCase().startsWith("bid=")) {
				int e = catalogQuery.indexOf("&");
				if(e<0)e=catalogQuery.length();
				
				String bid = catalogQuery.substring(4, e);
				result = new SearchResultSet();
				Vector<Long> recordIDs = org.jopac2.engine.dbGateway.DbGateway.getJIDbyBID(conn, catalogConnection, bid);
				result.setRecordIDs(recordIDs);
			}
			else {
				StaticDataComponent sd = new StaticDataComponent();
				
				sd.init(JSites.utils.DirectoryHelper.getPath()+"/WEB-INF/conf/");
				DoSearchNew doSearchNew = new DoSearchNew(conn,catalogConnection, sd);
				
				boolean useStemmer=false;
	
				try {
					result = doSearchNew.executeSearch(catalogQuery, useStemmer);
					if(orderBy!=null && orderBy.length()>0) {
						DbGateway.orderBy(conn, catalogConnection, orderBy, result);
					}
				} catch (ExpressionException e1) {
					e1.printStackTrace();
				}
			}
			throwResults(conn, catalogQuery, result);
		}

		conn.close();
	}
	
	@SuppressWarnings("unchecked")
	private String[] getListParameter(String p) {
		String[] r=new String[2];
		Enumeration<String> e=o.getParameterNames();
		while(e.hasMoreElements()) {
			String n=e.nextElement();
			if(n.startsWith(p)) {
				r[0]=n.substring(p.length());
				r[1]=o.getParameter(n);
				break;
			}
		}
		return r;
	}


	@SuppressWarnings("unchecked")
	private boolean checkListParameter(String p) {
		boolean r=false;
		Enumeration<String> e=o.getParameterNames();
		while(e.hasMoreElements()) {
			String n=e.nextElement();
			if(n.startsWith(p)) {
				r=true;
				break;
			}
		}
		return r;
	}


	private void throwResults(Connection conn, String catalogQuery, SearchResultSet result) throws SAXException {
		int page = 0;
		String sPage = o.getParameter("page");
		if( sPage != null ) {
			try {
				page = Integer.parseInt(sPage);
			} catch (NumberFormatException e) {
				page =0;
			}
		}
		
		boolean useStemmer=result.getStemmer();
			
        throwField("catalogConnection",catalogConnection);
        throwField("dbType",dbType);
        throwQueryData(catalogQuery,result,useStemmer);
        throwSearchData(result);
		
        Vector<Long> v = result.getRecordIDs();
		
		int nrec = v.size();
		int start = (page * 10) + 0;
		int end = (page * 10) + 9;
		
		if(end>nrec-1) end = nrec-1;
		
		if(v.size()>0) {
		
			throwField("sjid", Long.toString(v.get(start)));
			throwField("ejid", Long.toString(v.get(end)));
			
			contentHandler.startElement("","resultSet","resultSet",new AttributesImpl());
	        		
			for(int i=start;i<=end;i++){
				Long t = v.get(i);
				throwField("record",t.toString());	
			}
	        contentHandler.endElement("","resultSet","resultSet");
		}
	}


	private void throwQueryData(String catalogQuery, SearchResultSet result,
			boolean useStemmer) throws SAXException {
		contentHandler.startElement("","queryData","queryData",new AttributesImpl());
		throwField("requestQuery",catalogQuery);
		throwField("stemmer",Boolean.toString(useStemmer));
		throwField("switchStemmer",Util.removeAttribute(catalogQuery,"stemmer")+"&stemmer="+Boolean.toString(!useStemmer));
		throwField("query",result.getQuery());
		throwField("optimizedQuery",result.getOptimizedQuery());
		throwField("queryCount",Long.toString(result.getQueryCount()));
		throwField("queryTime",Double.toString(result.getQueryTime()));

        contentHandler.endElement("","queryData","queryData");
		
	}


	private void throwSearchData(SearchResultSet result) throws SAXException {
        Vector<NewItemCardinality> itemCardinalities=result.getItemCardinalities();
        if(itemCardinalities!=null) {
        	contentHandler.startElement("","searchData","searchData",new AttributesImpl());
        	Enumeration<NewItemCardinality> v1=result.getItemCardinalities().elements();
            NewItemCardinality it;

            String classe,parola,conta;           
            while(v1.hasMoreElements()) {
                it=(NewItemCardinality)v1.nextElement();
                classe=it.getClasseAsString();
                parola=it.getParola();
                conta=Long.toString(it.getCardinality());
                
                contentHandler.startElement("","item","item",new AttributesImpl());
                throwField("classe",classe);
                throwField("parola",parola);
                throwField("conta",conta);
                contentHandler.endElement("","item","item");
            }
            
            contentHandler.endElement("","searchData","searchData");
        }
		
	}


	private String getQuery(Request o) {
		
		String ret = o.getParameter("query");
		try {
			if(ret!=null)
				ret = URLDecoder.decode(ret, "UTF-8");
		} catch (UnsupportedEncodingException e) { }
		
		return ret;
	}


	
}
