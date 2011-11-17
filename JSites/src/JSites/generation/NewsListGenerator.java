package JSites.generation;

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
import java.sql.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.SAXException;

import JSites.authentication.Permission;

import JSites.utils.DBGateway;
import JSites.utils.site.NewsItem;

public class NewsListGenerator extends MyAbstractPageGenerator {

	public void generate() throws SAXException {
		
		contentHandler.startDocument();
		
		contentHandler.startElement("","newslist","newslist", new AttributesImpl());
		
		String reqDate=JSites.utils.DateUtil.getDateString();
			
		Connection conn = null;
		try{
			conn = this.getConnection(dbname);
			Vector<NewsItem> news = DBGateway.getNewsCID(conn, reqDate);
			Iterator<NewsItem> i = news.iterator();
			NewsItem t = null;
			while(i.hasNext()) {
				t = i.next();
				if(DBGateway.isGrandParent(this.pageId, t.getPid(), conn)) {
					AttributesImpl a=new AttributesImpl();
					a.addCDATAAttribute("pid", Long.toString(t.getPid()));
					a.addCDATAAttribute("cid", Long.toString(t.getCid()));
					a.addCDATAAttribute("insertionDate", t.getInsertionDate());
					a.addCDATAAttribute("startDate", t.getStartDate());
					a.addCDATAAttribute("endDate",t.getEndDate());
					a.addCDATAAttribute("pageName",t.getPageName());
					
					contentHandler.startElement("", "newsitem", "newsitem", a);
					
					String src = "cocoon://components/section/view?cid=" + t.getCid();
					AttributesImpl attrs = new AttributesImpl();
					attrs.addCDATAAttribute("src", src);
					
					contentHandler.startElement("http://apache.org/cocoon/include/1.0", "include", "include", attrs);
					contentHandler.endElement("http://apache.org/cocoon/include/1.0", "include", "include");
					
					contentHandler.endElement("","newsitem", "newsitem");
				}
			}
		}catch(Exception e) {e.printStackTrace();}
		
		try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}
		
		
		contentHandler.endElement("","newslist","newslist");
		
		contentHandler.endDocument();
	}


	@SuppressWarnings("rawtypes")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException{
		
		super.setup(resolver, objectModel, src, par);

		containerType="newsviewer";
		
		/*session = sessionManager.getSession(false);
		if(session!=null){
			permission = (Permission)session.getAttribute("permission");
			if(permission==null){
				permission = new Permission();
				permission.setPermission(Permission.ACCESSIBLE);
			}	
		}		*/
		
		if(permission==null){
			permission = new Permission();
			permission.setPermission(Permission.ACCESSIBLE);
		}
		
	}

	

}
