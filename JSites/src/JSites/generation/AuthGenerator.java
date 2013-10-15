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
import java.util.Enumeration;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;

import JSites.authentication.Auth;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.xml.sax.SAXException;

public class AuthGenerator extends MyAbstractPageGenerator {
	

//	private String displayName="",mail="";
	private String connectString="", realm="";

	public void generate() throws SAXException {
		String username = request.getParameter("name");
		String password = request.getParameter("password");
		
		contentHandler.startDocument();
		contentHandler.startElement("","authentication","authentication", this.emptyAttrs);
		
//		String[] data=test(username, password);
		if(username.equals("guest") || test(username, password)){
			contentHandler.startElement("","ID","ID", this.emptyAttrs);
			contentHandler.characters(username.toCharArray(), 0, username.length());
			contentHandler.endElement("","ID","ID");
			
			/*contentHandler.startElement("","role","role", this.emptyAttrs);
			contentHandler.characters("admin".toCharArray(), 0, 5);
			contentHandler.endElement("","role","role");*/
			
			contentHandler.startElement("","data","data", this.emptyAttrs);
			
//			contentHandler.startElement("","displayName","displayName", this.emptyAttrs);
//			contentHandler.characters(data[0].toCharArray(), 0, data[0].length());
//			contentHandler.endElement("","displayName","displayName");
			
			contentHandler.startElement("","username","username", this.emptyAttrs);
			contentHandler.characters(username.toCharArray(), 0, username.length());
			contentHandler.endElement("","username","username");
			
//			contentHandler.startElement("","mail","mail", this.emptyAttrs);
//			contentHandler.characters(data[1].toCharArray(), 0, data[1].length());
//			contentHandler.endElement("","mail","mail");
			
			contentHandler.endElement("","data","data");
		}
		
		
		contentHandler.endElement("","authentication","authentication");
		contentHandler.endDocument();
		
		
	}

	private boolean test(String u, String p) {
		
		boolean result = false;
		Auth ads=null;
        try {
            ads=new Auth(realm+"\\"+u, p, connectString);
            ads.Logon();
            
//            @SuppressWarnings("rawtypes")
//			NamingEnumeration n=ads.Search("(sAMAccountName="+u+")");
//            SearchResult sr = null;
//            
//            while(n.hasMoreElements()) {
//                sr = (SearchResult)n.nextElement();
//                //ads.listAttributes(sr,System.out);
//                result[0] = ads.getAttribute(sr,"displayName");
//                result[1] = ads.getAttribute(sr,"mail");
//
//                //System.out.println(ads.getAttribute(sr,"cn")+" "+ads.getAttribute(sr,"mail"));
//            }
            result=true;
                 
        }
        catch (Exception e) {
            System.out.println(u + " ha sbagliato password!");
        }
        finally {
        	try {
				ads.close();
			} catch (Exception e) {
//				e.printStackTrace();
			}
        }
        
        return result;
    }
	
	
	@SuppressWarnings("rawtypes")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException{
		super.setup(resolver, objectModel, src, par);
		
		try {
			connectString=par.getParameter("ad_connect_string");
			realm=par.getParameter("ad_realm");
		} catch (ParameterException e) {
			e.printStackTrace();
		}
	}

	
}
