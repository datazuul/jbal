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
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.xml.Mdb;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.apache.cocoon.xml.AttributesImpl;
import org.apache.commons.lang.ArrayUtils;

import JSites.transformation.MyAbstractPageTransformer;


public class CatalogSearchSaveTransformer extends MyAbstractPageTransformer {
	
	Hashtable<String,Channel> searchChannel=null;
	Hashtable<String,Channel> listChannel=null;
	Hashtable<String,El> fields=null;
	
	StringBuffer sb = new StringBuffer();
	StringBuffer order=new StringBuffer();
	
	@Override
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		super.setup(arg0, arg1, arg2, arg3);
	}


	@Override
	public void startElement(String uri, String loc, String raw, Attributes a) throws SAXException {
		if(loc.equals("catalogSearch")) {
			searchChannel=new Hashtable<String,Channel>();
			listChannel=new Hashtable<String,Channel>();
			fields=new Hashtable<String,El>();
			super.startElement(uri, loc, raw, a);
		}
		else if(loc.equals("search")){
			String name=a.getValue("name");
			String desc=a.getValue("desc");
			if(name!=null)
				searchChannel.put(name, new Channel(name,desc,""));
		}
		else if(loc.equals("list")){
			String name=a.getValue("name");
			String desc=a.getValue("desc");
			if(name!=null)
				listChannel.put(name, new Channel(name,desc,""));
		}
		else {
			fields.put(loc, new El(loc,a));
		}
	}

	@Override
	public void characters(char[] c, int start, int len) throws SAXException {
		sb.append(c,start,len);
	}

	@Override
	public void endElement(String uri, String loc, String raw) throws SAXException {
		if(loc.equals("catalogSearch")) {
			String[] chr=null;
			String catalogFormat=o.getParameter("catalogFormat");
			String catalog=o.getParameter("catalogConnection");
			
			if(catalogFormat.equalsIgnoreCase("mdb")) {
				Connection conn;
				try {
					conn = getConnection(dbname);
					chr=DbGateway.getChannels(conn,catalog,"mdb");
//					RecordInterface ma=DbGateway.getNotiziaByJID(conn, 1);
//					if(ma!=null) chr=ma.getChannels();
					conn.close();
				} catch (ComponentException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					RecordInterface ma=RecordFactory.buildRecord(0, "", catalogFormat, 0);
					chr=ma.getChannels();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			
			for(int k=0;chr!=null && k<chr.length;k++) {
				String tc=chr[k];
				if(tc.contains("/")) {
					tc=tc.substring(tc.lastIndexOf("/")+1);
				}
				if(!searchChannel.containsKey(tc)) searchChannel.put(tc, new Channel(tc,tc,""));
				if(!listChannel.containsKey(tc)) listChannel.put(tc, new Channel(tc,tc,""));
			}
	
//			Enumeration<String> i=searchChannel.keys();
//			String[] ord=new String[searchChannel.size()];
//			
//			@SuppressWarnings("unchecked")
//			Enumeration<String> e=o.getParameterNames();
//			int ii=0;
//			while(e.hasMoreElements()) {
//				String nam=e.nextElement();
//				if(nam.startsWith("search-")) {
//					ord[ii++]=nam.substring(7);
//				}
//			}
//			
//			while(i.hasMoreElements()) {
//				String nam=i.nextElement();
//				if(!ArrayUtils.contains(ord, nam)) {
//					ord[ii++]=nam;
//				}
//			}
			
			String order=o.getParameter("neworder").trim();
			
			Enumeration<String> i=listChannel.keys();
			while(i.hasMoreElements()) {
				String chn=i.nextElement();
				if(!order.contains(chn)) order=order+" "+chn;
			}
			
			
			
			String[] ord=order.split(" ");
			
			for(int iz=0;iz<ord.length;iz++) {
				Channel ch=searchChannel.get(ord[iz].trim());
				String t=o.getParameter("search-"+ch.getName());
				String desc=o.getParameter("name-"+ch.getName());
				if(desc==null) desc=ch.getName();
				ch.setDesc(desc);
				if(t!=null && t.length()>0) ch.setChecked("true");
				sendElement("search",ch);
			}
			
			
//			while(i.hasMoreElements()) {
//				String chn=i.nextElement();
//				Channel ch=searchChannel.get(chn);
//				String t=o.getParameter("search-"+ch.getName());
//				String desc=o.getParameter("name-"+ch.getName());
//				if(desc==null) desc=ch.getName();
//				ch.setDesc(desc);
//				if(t!=null && t.length()>0) ch.setChecked("true");
//				sendElement("search",ch);
//			}
						
			i=listChannel.keys();
			while(i.hasMoreElements()) {
				String chn=i.nextElement();
				Channel ch=listChannel.get(chn);
				String t=o.getParameter("list-"+ch.getName());
				String desc=o.getParameter("name-"+ch.getName());
				if(desc==null) desc=ch.getName();
				ch.setDesc(desc);
				if(t!=null && t.length()>0) ch.setChecked("true");
				sendElement("list",ch);
			}
			i=fields.keys();
			while(i.hasMoreElements()) {
				String chn=i.nextElement();
				El el=fields.get(chn);
				String t=o.getParameter(el.getName());
				if(t!=null && t.length()>0) el.setContent(t);
				super.startElement("", el.getName(), el.getName(), el.getAttr());
				super.characters(el.getContent().toCharArray(), 0, el.getContent().length());
				super.endElement("", el.getName(), el.getName());
			}
			searchChannel.clear();
			searchChannel=null;
			listChannel.clear();
			listChannel=null;
			fields.clear();
			fields=null;
			super.endElement(uri, loc, raw);
		}
		else if(loc.equals("search") || loc.equals("list")) {
			// do nothing
		}
		else {
			El el=fields.get(loc);
			el.setContent(sb.toString());
			fields.put(loc, el);
			sb.delete(0, sb.length());
		}
	}

	private void sendElement(String string, Channel ch) throws SAXException {
		AttributesImpl a=new AttributesImpl();
		a.addCDATAAttribute("name",ch.getName());
		a.addCDATAAttribute("desc",ch.getDesc());
		String chk=ch.getChecked();
		if(chk!=null && chk.equals("true")) {
			a.addCDATAAttribute("checked", "true");
		}
		super.startElement("", string, string, a);
		super.endElement("", string, string);
	}


	private class El {
		protected String name,content;
		protected Attributes attr;
		
		public El(String n, Attributes a) {
			name=n;
			attr=a;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public Attributes getAttr() {
			return attr;
		}
		public void setAttr(AttributesImpl attr) {
			this.attr = attr;
		}
		
	}

	private class Channel {
		protected String name,desc,checked;
		public Channel(String n, String d, String c) {
			name=n;
			desc=d;
			checked=d;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getChecked() {
			return checked;
		}

		public void setChecked(String checked) {
			this.checked = checked;
		}
	}
	
}
