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
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.AttributesImpl;
import org.apache.cocoon.xml.dom.DOMStreamer;
import org.apache.excalibur.source.SourceValidity;
import org.jopac2.engine.Engine;
import org.jopac2.engine.EngineFactory;
import org.jopac2.engine.ItemCardinality;
import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.utils.Utils;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import JSites.transformation.MyAbstractPageTransformer;
import JSites.utils.Util;


public class CatalogSearchTransformer extends MyAbstractPageTransformer implements CacheableProcessingComponent{
	
	private String catalogConnection = null, dbType=null, defaultQuery=null, catalogOrder=null, direction=null;
//	private String engineType="db";
	private String rxp=null,merge="";
	private StringBuffer sb = null;
//	private String dbUrl=null;
	private int rxpn=10;
	private boolean desc=false; //, isMerge=false;
	private DOMStreamer streamer;
//	private String[] toMerge=null;
//    private Record2document r2d=null;
//    private MergeRecords mr=null;
    private Document prevDoc=null;
	
    private boolean readCatalogConnection = false, readDbType = false, isRxp = false, 
		isDefaultQuery = false, isCatalogOrder=false, isDirection=false;
	//boolean readLinks = false;
	
	@SuppressWarnings("unchecked")
	@Override
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		super.setup(arg0, arg1, arg2, arg3);
		catalogConnection = "";
		merge="";rxp=null;
//		dbUrl=null;
		sb = new StringBuffer();
//		toMerge=null;
    	streamer = new DOMStreamer(this.xmlConsumer);
    	prevDoc=null;

    	catalogConnection = null;
    	dbType=null; 
    	defaultQuery=null; 
    	catalogOrder=null; 
    	direction=null;
	}
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		streamer.setConsumer(this.xmlConsumer);
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		streamer.recycle();
	}

	@Override
	public void startElement(String uri, String loc, String raw, Attributes a) throws SAXException {
		if(loc.equals("catalogConnection")){
			readCatalogConnection = true;
		}
		else if(loc.equals("dbType")){
			readDbType = true;
		}
		else if(loc.equals("defaultQuery")){
			isDefaultQuery = true;
		}
		else if(loc.equals("catalogOrder")){
			isCatalogOrder = true;
		}
		else if(loc.equals("rxp")) {
			isRxp=true;
		}
		else if(loc.equals("direction")) {
			isDirection=true;
		}
		else if(loc.equals("catalogSearch")) {
			super.startElement("", "root", "root", a);
		}
		else if (uri.equals("") && loc.equals("merge")) {
        	if(a.getValue("checked")!=null && !a.getValue("checked").isEmpty()) {
        		merge=merge+"/record/"+a.getValue("name")+" ";
        	}
//            isMerge=true;
            super.startElement(uri, loc, raw, a);
            return;
        }
		else
			super.startElement(uri, loc, raw, a);
	}

	@Override
	public void characters(char[] c, int start, int len) throws SAXException {
//		if(readCatalogConnection || readDbType || isRxp || isDefaultQuery || isCatalogOrder || isDirection)
			sb.append(c,start,len);
//		else
//			super.characters(c, start, len);
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
		else if(loc.equals("defaultQuery")){
			isDefaultQuery = false;
			defaultQuery = sb.toString().trim();
			sb.delete(0, sb.length());
		}
		else if(loc.equals("catalogOrder")){
			isCatalogOrder = false;
			catalogOrder = sb.toString().trim();
			sb.delete(0, sb.length());
		}
		else if(loc.equals("rxp")) {
			isRxp = false;
			rxp = sb.toString().trim();
			sb.delete(0, sb.length());
			String t=(String)session.getAttribute("rxp"); //Util.getRequestData(request, "rxp");
			if(t!=null && t.trim().length()>0) rxp=t;
		}
		else if(loc.equals("direction")) {
			isDirection = false;
			direction = sb.toString().trim();
			sb.delete(0, sb.length());
			String t=Util.getRequestData(request, "direction");
			if(t!=null && t.trim().length()>0) direction=t;
		}
		else if(loc.equals("catalogSearch")) {
			try {
				throwResults();
			} 
			catch (Exception e) { e.printStackTrace();}
			if(rxp == null || rxp.length()==0) rxp="10";
			throwField("rxp",rxp);
			super.endElement("", "root", "root");
		}
		else {
			super.characters(sb.toString().toCharArray(), 0, sb.length());
			sb.delete(0, sb.length());
			super.endElement(uri, loc, raw);
		}
	}

	private void throwResults() throws ComponentException, SQLException, SAXException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		
		String attr_query=getQuery();
		String attr_order=Util.getRequestData(request, "orderby");
		String attr_page=(String)request.getAttribute("page");
		String attr_pagecode=(String)request.getAttribute("pagecode");
		
		merge=merge.trim();
		String[] toMerge=null;
		if(merge.length()>0) {
			toMerge=merge.split(" ");
		}
		else {
			toMerge=null;
		}
		CatalogSearchRequest catalogSearchRequest=new CatalogSearchRequest(attr_query,attr_order,attr_page);
		if(attr_pagecode!=null) {
			String attr_sessionName=attr_pagecode.toLowerCase()+"_catalogSearchRequest";
			if(attr_query!=null && attr_query.length()>0) {
				while(session.getAttribute(attr_sessionName)!=null) session.removeAttribute(attr_sessionName);
				session.setAttribute(attr_sessionName, catalogSearchRequest);
			}
			else {
				if(session.getAttribute(attr_sessionName)!=null) catalogSearchRequest=(CatalogSearchRequest)session.getAttribute(attr_sessionName);
				if(catalogSearchRequest.getAttr_page()!=null) request.setAttribute("page", catalogSearchRequest.getAttr_page());
			}
		}
		
		

		SearchResultSet result=null;
		
		if(catalogConnection==null || catalogConnection.equals("dbconn")) {
			String t=Util.getRequestData(request, "catalog");
			if(t!=null && t.length()>0) catalogConnection=t;
		}
		
		String engineType="db";
		if(catalogConnection.startsWith("http")) engineType="solr";
			
		String enType=Util.getRequestData(request, "engine");
		if(enType!=null && !enType.isEmpty()) engineType=enType;
		
		try {
        	rxpn=Integer.parseInt(rxp);
        }
        catch(Exception e) {}
        
        String descendant=Util.getRequestData(request, "descendant");
        
        if(descendant==null) {
			try {
				descendant=sitemapParameters.getParameter("descendant");
			} catch (ParameterException e2) {
				// nothing, just ignore
			}
        }
        		
		if(descendant!=null && descendant.equalsIgnoreCase("true")) desc=true;
		if(direction!=null && (direction.equalsIgnoreCase("descendant") || direction.equalsIgnoreCase("desc"))) desc=true;
		
		String catalogQuery = catalogSearchRequest.getAttr_query(); // getQuery();
		
		String orderBy=catalogSearchRequest.getAttr_order(); // Util.getRequestData(request, "orderby");
		
		if(orderBy!=null) orderBy=orderBy.trim();
		
		if(orderBy==null || orderBy.length()==0) orderBy=catalogOrder;

		Connection conn = null;
		try {
			
			if(engineType.equals(EngineFactory.ENGINE_DB)) conn=datasourceComponent.getConnection(); // se è solr non serve una connessione al db
			Engine engine = EngineFactory.getEngine(conn,catalogConnection,engineType);
			
			if(checkListParameter("list")) {
				String[] list=getListParameter("list");
				if(list[1]!=null && list[1].length()>0) {
					if(desc) {
	//					result=ListSearch.listSearchBackward(conn, catalogConnection, list[0], list[1], 100);
						result=engine.listSearchBackward(list[0], list[1], 100);
						desc=false; // sempre true, perche' e' il metodo di lista diverso
					}
					else {
	//					result=ListSearch.listSearch(conn, catalogConnection, list[0], list[1], 100);
						result=engine.listSearch(list[0], list[1], 100);
					}
					throwField("listRecord", list[0]);
					
					throwResults(engineType, catalogQuery, toMerge, result); 
				}
			}
			else if(checkListParameter("nlist")) {
				String[] list=getListParameter("nlist");
				if(list[1]!=null && list[1].length()>0) {
					long sjid=Long.parseLong(list[1]);
					
					if(desc) {
	//					result=ListSearch.listSearchBackward(conn, catalogConnection, list[0], sjid, 100);
						result=engine.listSearchBackward(list[0], sjid, 100);
						desc=false; // sempre true, perche' e' il metodo di lista diverso
					}
					else {
	//					result=ListSearch.listSearch(conn, catalogConnection, list[0], sjid, 100);
						result=engine.listSearch(list[0], sjid, 100);
					}
					
					throwField("listRecord", list[0]);
					
					throwResults(engineType, catalogQuery, toMerge, result); // sempre true, perche' e' il metodo di lista diverso
				}
			}
			else if(catalogQuery!=null && catalogQuery.length()>0) {
				catalogQuery = catalogQuery.replaceAll("\\(.*\\)", "").trim();
				if(catalogQuery.toLowerCase().startsWith("jid=")) {
					int e = catalogQuery.indexOf("&");
					if(e<0)e=catalogQuery.length();
					
					String id = catalogQuery.substring(4, e);
					result = new SearchResultSet();
					Vector<String> recordIDs = new Vector<String>();
					recordIDs.add(id);
					result.setRecordIDs(recordIDs);
				}
				else if(catalogQuery.toLowerCase().startsWith("bid=")) {
					int e = catalogQuery.indexOf("&");
					if(e<0)e=catalogQuery.length();
					
					String bid = catalogQuery.substring(4, e);
					result = new SearchResultSet();
					Vector<String> recordIDs = engine.getJIDbyBID(conn, catalogConnection, bid);
					result.setRecordIDs(recordIDs);
				}
				else {
	//				StaticDataComponent sd = new StaticDataComponent();
					
	//				sd.init(JSites.utils.DirectoryHelper.getPath()+"/WEB-INF/conf/");
					
					
					try {
						
						boolean useStemmer=false;
						
						String t=Util.getRequestData(request, "stemmer");
						if(t!=null && t.equalsIgnoreCase("true")) useStemmer=true;
						result = engine.executeSearch(catalogQuery, useStemmer);
						if(orderBy!=null && orderBy.length()>0) {
							engine.orderBy(conn, catalogConnection, orderBy, result);
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	//				DoSearchNew doSearchNew = new DoSearchNew(conn,catalogConnection);
					
					
	//				/**
	//				 * use * for input query, % in engine query
	//				 */
	//				catalogQuery=catalogQuery.replaceAll("\\*", "%");
					
	
				}
//				if(engineType.equals(EngineFactory.ENGINE_DB) && (conn==null||conn.isClosed())) {
//					System.out.println(Utils.currentDate()+" - CatalogSearchTransformer.throwResults:");
//					System.out.println("dbname: "+dbname);
//					System.out.println("catalog: "+catalogConnection);
//					if(conn==null) System.out.println("Connection is null!");
//					else System.out.println("Connection is closed!");
//					conn=datasourceComponent.getConnection();
//				}
				throwResults(engineType, catalogQuery, toMerge, result);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - CatalogSearchTransformer.throwResults() exception " + fe.getMessage());}
		}
	}
	
	@SuppressWarnings("unchecked")
	private String[] getListParameter(String p) {
		boolean found=false;
		String[] r=new String[2];
		Enumeration<String> e=request.getParameterNames();
		while(e.hasMoreElements()) {
			String n=e.nextElement();
			if(n.startsWith(p)) {
				r[0]=n.substring(p.length());
				r[1]=request.getParameter(n);
				found=true;
				break;
			}
		}
		if(!found) {
			e=request.getAttributeNames();
			while(e.hasMoreElements()) {
				String n=e.nextElement();
				if(n.startsWith(p)) {
					r[0]=n.substring(p.length());
					r[1]=(String)request.getAttribute(n);
					found=true;
					break;
				}
			}
		}
		if(!found) {
			String names[]=sitemapParameters.getNames();
			for(int i=0; names!=null && i<names.length;i++) {
				String n=names[i];
				if(n.startsWith(p)) {
					r[0]=n.substring(p.length());
					try {
						r[1]=(String)sitemapParameters.getParameter(n);
					} catch (ParameterException e1) {
						// nothing, just ignore
					}
					break;
				}
			}
		}
		return r;
	}


	@SuppressWarnings("unchecked")
	private boolean checkListParameter(String p) {
		boolean r=false;
		Enumeration<String> e=request.getParameterNames();
		while(e.hasMoreElements()) {
			String n=e.nextElement();
			if(n.startsWith(p)) {
				r=true;
				break;
			}
		}
		if(!r) {
			e=request.getAttributeNames();
			while(e.hasMoreElements()) {
				String n=e.nextElement();
				if(n.startsWith(p)) {
					r=true;
					break;
				}
			}
		}
		if(!r) {
			String [] names=sitemapParameters.getNames();
			for(int i=0;names!=null && i<names.length;i++) {
				String n=names[i];
				if(n.startsWith(p)) {
					r=true;
					break;
				}
			}
		}
		return r;
	}


	private void throwResults(String engineType, String catalogQuery, String[] toMerge,
			SearchResultSet result) throws SAXException {
		int page = 0;
    	Record2document r2d=new Record2document();
    	MergeRecords mr=new MergeRecords();
		String display = JSites.utils.Util.getRequestData(request, "display");
		String sPage = Util.getRequestData(request, "page");
		if (sPage != null) {
			try {
				page = Integer.parseInt(sPage);
			} catch (NumberFormatException e) {
				page = 0;
			}
		}

		boolean useStemmer = result.getStemmer();

//		throwField("db", catalogConnection);
		throwField("dbType", dbType);

		throwQueryData(catalogQuery, result, useStemmer);
		throwSearchData(result);

		Vector<String> v = result.getRecordIDs();
		if (desc)
			Collections.reverse(v);

		int nrec = v.size();
		int start = (page * rxpn) + 0;
		int end = (page * rxpn) + (rxpn - 1);

		if (end > nrec - 1)
			end = nrec - 1;

		contentHandler.startElement("", "resultSet", "resultSet",
				new AttributesImpl());
		
		if (v.size() > 0) {
			int i=start;
			end=start;
			int _rxpn=rxpn;
			Connection conn=null;

			
			try {
				if(engineType.equals(EngineFactory.ENGINE_DB)) conn=datasourceComponent.getConnection();
				Engine engine=EngineFactory.getEngine(conn, catalogConnection, engineType);
				
				while (_rxpn>0 && end<nrec) {
					String t = v.get(i++);

//					RecordInterface ma = DbGateway.getNotiziaByJID(
//							myConnection, catalogConnection, t.toString());
					
					
					RecordInterface ma = engine.getNotiziaByJID(t);
					
//					myConnection.close();
					
					end++;

					if (ma != null) {
						Document currentDocument = r2d.getDocument(ma, catalogConnection,
								t.toString(), display, this.request.getParameter("datadir"));
						ma.destroy();
						if (toMerge != null) {
							if (prevDoc == null) {
								prevDoc = currentDocument;
							} else {
								Document[] resultDoc = mr.processRecords(
										prevDoc, currentDocument, toMerge);
								if (resultDoc.length == 1) {
									// ha unito i due record
									prevDoc = resultDoc[0];
								} else {
									// non ha unito i due record
									streamer.stream(prevDoc
											.getDocumentElement());
									prevDoc = resultDoc[1];
									_rxpn--;
								}
							}
						} else {
							streamer.stream(currentDocument
									.getDocumentElement());
							_rxpn--;
						}
					} else {
						super.startElement("", "record", "record",
								new AttributesImpl());
						throwField("ERRORE", "Ricevuto null da id_notizia = "
								+ t.toString());
						super.endElement("", "record", "record");
					}
					
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			finally {
				if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - CatalogSearchTransformer.throwResults(...) exception " + fe.getMessage());}
			}
			
			if(prevDoc!=null) {
				if(_rxpn>0) {
				streamer.stream(prevDoc
						.getDocumentElement());
				}
				prevDoc=null;
			}

			// throwField("record",t.toString());
		}
		
		contentHandler.endElement("", "resultSet", "resultSet");
		if(v!=null && v.size()>0) {
			throwField("sjid", v.get(start<nrec?start:nrec-1));
			throwField("ejid", v.get(end<nrec?end:nrec-1));
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
		if(desc) throwField("direction","descendant");
		else throwField("direction","ascendant");
		throwField("rxp",Integer.toString(rxpn));

        contentHandler.endElement("","queryData","queryData");
		
	}


	private void throwSearchData(SearchResultSet result) throws SAXException {
        Vector<ItemCardinality> itemCardinalities=result.getItemCardinalities();
        if(itemCardinalities!=null) {
        	contentHandler.startElement("","searchData","searchData",new AttributesImpl());
        	Enumeration<ItemCardinality> v1=result.getItemCardinalities().elements();
            ItemCardinality it;

            String classe,parola,conta;           
            while(v1.hasMoreElements()) {
                it=(ItemCardinality)v1.nextElement();
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


	private String getQuery() {
		
		String ret = Util.getRequestData(request,"query");
		
//		if(ret==null) {
//			ret=((Map<String,String>)request.getSession().getAttribute("redirectfrom")).get("query");
//			
//		}
		
		if(ret==null || ret.length()==0) ret=defaultQuery;
		
		
		
		
		/**
		 * use * for input query, % in engine query
		 */
		if(ret!=null) {
			ret=ret.replaceAll("\\*", "%").trim();
		}
		return ret;
	}

	@Override
	public Serializable getKey() {
		return System.currentTimeMillis();
	}

	@Override
	public SourceValidity getValidity() {
		// TODO Auto-generated method stub
		return null;
	}


	
}
