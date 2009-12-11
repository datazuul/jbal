package JSites.transformation;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <request target="/JSites/Tree/getChildren" sitemap="Tree/getChildren" source="">
	<requestHeaders>
		<header name="host">localhost:8080</header>
		<header name="user-agent">
			Mozilla/5.0 (Macintosh; U; Intel Mac OS X; it; rv:1.8.1.1) Gecko/20061204 Firefox/2.0.0.1
		</header>
		<header name="accept">
			text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*_/_*;q=0.5
		</header>
		<header name="accept-language">it-it,it;q=0.8,en-us;q=0.5,en;q=0.3</header>
		<header name="accept-encoding">gzip,deflate</header>
		<header name="accept-charset">ISO-8859-1,utf-8;q=0.7,*;q=0.7</header>
		<header name="keep-alive">300</header>
		<header name="connection">keep-alive</header>
		<header name="referer">http://localhost:8080/JSites/Tree/tree.html</header>
		<header name="cookie">JSESSIONID=48167C1779E959F05A86136CD5C005BF</header>
	</requestHeaders>
	<requestParameters>
		<parameter name="data">
			<value>
				{"node":{"widgetId":"2.2","objectId":"","index":1,"isFolder":true},"tree":{"widgetId":"secondTree","objectId":""}}
			</value>
		</parameter>
		<parameter name="dojo.preventCache">
			<value>1171098022228</value>
		</parameter>
	</requestParameters>
	<configurationParameters/>
</request>

 * @author romano
 *
 */

public class JSONReadDataTransformer extends AbstractTransformer implements Composable, Disposable {
	String dataParameter="data";
	String output="";
	
	public void compose(ComponentManager arg0) throws ComponentException {
		// TODO Auto-generated method stub
		
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		 if(arg2!=null && arg2.length()>0) dataParameter=arg2;
		 Request request = (Request)arg1.get("request");
		 output="";
		 String action=request.getRequestURI().substring(request.getRequestURI().lastIndexOf('/')+1);
		 try {
			JSONObject jsonObject=new JSONObject(request.getParameter(dataParameter));
			if(action.equals("getChildren")) {
				output=listString(getChildren(jsonObject),"[","]");
			}
			else if (true) {
				
			}
			//System.out.println(output);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<String> getChildren(JSONObject jsonObject) throws JSONException, IOException {
		/**
		 * [{title:"test",isFolder:true,objectId:"myobj"},{title:"test2"}]
		 */
		List<String> output=new ArrayList<String>();
		JSONObject node=jsonObject.getJSONObject("node");
		String path=node.getString("objectId");
		File dir=new File(path);
		
		if(dir.isDirectory()) {
			File[] content=dir.listFiles();
			for(int i=0;i<content.length;i++) {
				if(!content[i].isHidden()) {
					List<String> par=new ArrayList<String>();
					par.add("title:\""+content[i].getName()+"\"");
					par.add("isFolder:"+Boolean.toString(content[i].isDirectory()));
					par.add("objectId:\""+content[i].getCanonicalPath()+"\"");
					output.add(listString(par,"{","}"));
				}
			}
		}
		return output;
	}

	private String listString(List<String> par, String begin, String end) {
		String r=begin+par.get(0);
		for(int i=1;i<par.size();i++) {
			r+=","+par.get(i);
		}
		r+=end;
		return r;
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {}
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {}
	public void characters(char[] c, int s, int e) throws SAXException {};
	
	public void endDocument() throws SAXException {
		super.startDocument();
		super.characters(output.toCharArray(), 0, output.length());
		super.endDocument();
	}

}
