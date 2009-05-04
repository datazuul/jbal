package JSites.transformation.catalogSearch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
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

import JSites.transformation.MyAbstractPageTransformer;
import JSites.utils.Util;


public class CatalogSearchSaveTransformer extends MyAbstractPageTransformer {
	
	Hashtable<String,Channel> searchChannel=null;
	Hashtable<String,Channel> listChannel=null;
	Hashtable<String,El> fields=null;
	
	StringBuffer sb = new StringBuffer();
	
	@SuppressWarnings("unchecked")
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
			Enumeration<String> i=searchChannel.keys();
			while(i.hasMoreElements()) {
				String chn=i.nextElement();
				Channel ch=searchChannel.get(chn);
				String t=o.getParameter("search-"+ch.getName());
				if(t!=null && t.length()>0) ch.setChecked("true");
				sendElement("search",ch);
			}
			i=listChannel.keys();
			while(i.hasMoreElements()) {
				String chn=i.nextElement();
				Channel ch=listChannel.get(chn);
				String t=o.getParameter("list-"+ch.getName());
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
