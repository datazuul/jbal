package JSites.transformation.catalogSearch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
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
import org.jopac2.engine.dbGateway.StaticDataComponent;
import org.jopac2.engine.listSearch.ListSearch;
import org.jopac2.engine.parserRicerche.parser.exception.ExpressionException;
import org.jopac2.engine.utils.SearchResultSet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.apache.cocoon.xml.AttributesImpl;

import JSites.generation.ImportCatalog;
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
		if(dbType != null && dbType.equalsIgnoreCase("derby")) {
			String driver=ImportCatalog._classDerbyDriver;
			dbUrl = "jdbc:derby:"+o.getParameter("datadir")+"/catalogs/"+catalogConnection+";create=true";
			
			Class.forName(driver).newInstance();
			
			conn = DriverManager.getConnection(dbUrl, "", "");
		}
		else {
			conn = getConnection(catalogConnection);
		}
		SearchResultSet result=null;
		
		String catalogQuery = getQuery(o);
		
		if(checkListParameter()) {
			String[] list=getListParameter();
			if(list[1]!=null && list[1].length()>0) {
//				Statement stmt=conn.createStatement();
//				ResultSet ts=stmt.executeQuery("select * from "+list[0]+"_NDX");
//				while(ts.next()) {
//					System.out.println(ts.getString("testo"));
//				}
//				ts.close();
//				stmt.close();
				result=ListSearch.listSearch(conn, list[0], list[1], 100);
				throwResults(conn, catalogQuery, result);
			}
		}
		else if(catalogQuery!=null && catalogQuery.length()>0) {
			catalogQuery = catalogQuery.replaceAll("\\(.*\\)", "").trim();
			if(catalogQuery.toLowerCase().startsWith("jid=")){
				int e = catalogQuery.indexOf("&");
				if(e<0)e=catalogQuery.length();
				
				long id = Long.parseLong( catalogQuery.substring(4, e) );
				result = new SearchResultSet();
				Vector<Long> recordIDs = new Vector<Long>();
				recordIDs.add(id);
				result.setRecordIDs(recordIDs);
			}
			else{
				StaticDataComponent sd = new StaticDataComponent();
				
				sd.init(JSites.utils.DirectoryHelper.getPath()+"/WEB-INF/conf/");
				DoSearchNew doSearchNew = new DoSearchNew(conn,sd);
				boolean useStemmer=false;
	
				try {
					result = doSearchNew.executeSearch(catalogQuery, useStemmer);
				} catch (ExpressionException e1) {
					e1.printStackTrace();
				}
			}
			throwResults(conn, catalogQuery, result);
		}

		conn.close();
	}
	
	@SuppressWarnings("unchecked")
	private String[] getListParameter() {
		String[] r=new String[2];
		Enumeration<String> e=o.getParameterNames();
		while(e.hasMoreElements()) {
			String n=e.nextElement();
			if(n.startsWith("list")) {
				r[0]=n.substring(4);
				r[1]=o.getParameter(n);
				break;
			}
		}
		return r;
	}


	@SuppressWarnings("unchecked")
	private boolean checkListParameter() {
		boolean r=false;
		Enumeration<String> e=o.getParameterNames();
		while(e.hasMoreElements()) {
			String n=e.nextElement();
			if(n.startsWith("list")) {
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
		
		if(end>nrec-1)end = nrec-1;
		
		if(page>0)
			throwField("prevPage",catalogQuery+(new Integer(page-1).toString()));
		if(page<(nrec/10))
			throwField("nextPage",catalogQuery+(new Integer(page+1).toString()));
		
		contentHandler.startElement("","resultSet","resultSet",new AttributesImpl());
        		
		for(int i=start;i<=end;i++){
			Long t = v.get(i);
			throwField("record",t.toString());	
		}
        contentHandler.endElement("","resultSet","resultSet");		
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
