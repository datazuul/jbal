/*
 * CreateLink.java
 * 
 * Created on 10 gennaio 2005, 20.58
 */

package JSites.transformation.catalogSearch;
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

/**
* @author	Romano Trampus
* @version	10/01/2005
* 
* @author	Romano Trampus
* @version	19/05/2005
*/
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.ProcessingException;
import org.apache.avalon.framework.parameters.Parameters;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.apache.cocoon.xml.AttributesImpl;

import java.util.Map;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.io.*;

import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.Composable;

import JSites.linkParsers.LinkParser;
import JSites.transformation.MyAbstractPageTransformer;

public class CreateLink extends MyAbstractPageTransformer implements Composable {
	static final int DEBUG=1;
//	private boolean isRecord = false;
//	private boolean isParola = false;
	//private Hashtable v;
//	private String data, link, currentNS, currentQName; //current, 
//	private String left, right;
	@SuppressWarnings("unchecked")
	private Map objectModel;
	private Hashtable<String,String> links = new Hashtable<String,String>(), classes = new Hashtable<String,String>();
	private boolean loaded = false;
//	private AttributesImpl currentAttributes;
	private StringBuffer currentData;
	protected java.util.Stack<Object> context;
	private Hashtable<String,String> current=new Hashtable<String,String>();
	private String linksdata=null;
	private boolean isLinks=false;
	

	
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src,
			Parameters par) throws ProcessingException, SAXException,
			IOException {
		
		super.setup(resolver, objectModel, src, par);
		
		this.objectModel = objectModel;
		
		
		if(DEBUG>1) System.out.println("DEBUG: entra setup");
		
		currentData = new StringBuffer(1110);
        context = new java.util.Stack<Object>();

        
        
//		if (!loaded) {
//			FileInputStream is;
//			links = new Hashtable<String,String>();
//			classes = new Hashtable<String,String>();
//			try {
//				String resourceName="/WEB-INF/conf/commons/links.lst";
//				if((src!=null)&&(src.length()>0)) resourceName=src;  
//				is = new FileInputStream(new File(
//						getResource(resourceName)));
//				BufferedReader br = new BufferedReader(
//						new InputStreamReader(is));
//				String read = br.readLine();
//				while (read != null) {
//					if (!read.startsWith("#")) {
//						StringTokenizer st = new StringTokenizer(read, ",");
//						if (st.hasMoreTokens()) {
//							currentTag = st.nextToken();
//							if (st.hasMoreTokens()) {
//								currentPrefix=st.nextToken();
//								if(currentPrefix.equals("null")) currentPrefix="";
//								links.put(currentTag, currentPrefix);
//							}
//							if (st.hasMoreTokens()) {
//								classes.put(currentTag, st.nextToken());
//							}
//						}
//					}
//					read = br.readLine();
//				}
//
//				//				links.load(is);
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if(DEBUG>1) System.out.println("++ CreateLink loaded");
//		} else {
//			if(DEBUG>1) System.out.println("++ CreateLink already loaded");
//		}
		// loaded = true;
		/** TODO
		 * se prende il file da src bisognerebbe fare che lo passa a StaticDataComponent e che lo carica
		 * una volta per tutte
		 */
		if(DEBUG>1) System.out.println("DEBUG: esce setup");
	}

	/*
	 * String isLink(String item) Ritorna l'url di base al quale aggiungere il
	 * link costruito o null.
	 *  
	 */
	private String getLink(String item) {
		if(DEBUG>1) System.out.println("DEBUG: chiamato getLink");
		if(item==null) {
			if(DEBUG>1) System.out.println("DEBUG: esco getLink: item==null");
			return null;
		}
		return links.containsKey(item) ? ((String) links.get(item)) : null;
	}

/*	private boolean isLink(String item) {
		if(DEBUG>1) System.out.println("DEBUG: chiamato isLink");
		return links.containsKey(item);
	}*/

	private String buildLink(String data, String tag, String prefix) {
		/* TODO meglio usare replace per fare questa cosa */
//		String t = "";
		
		String currentHost=(String)current.get("source");

		if(DEBUG>1) System.out.println("DEBUG: entra buildLink: "+currentHost==null?tag:currentHost+":"+tag+" / "+data);
		if (classes.containsKey(currentHost==null?tag:currentHost+":"+tag)) {
			String className;

			className = (String) classes.get(currentHost==null?tag:currentHost+":"+tag);

			try {
				LinkParser worker = (LinkParser) Class.forName(className)
						.newInstance();
				data = worker.parse(current,tag,data,prefix);
				// chiama la classe per costruire t e basta
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(DEBUG>1) System.out.println("DEBUG: esce getLink"); 
		return data;
	}

    public void startElement(String ns, String name, String qname, Attributes attrs) throws SAXException {
    	if(DEBUG>1) System.out.println("DEBUG: entra startElement: "+qname);
        dispatch(true);
        if(qname.equals("links")) {
        	isLinks=true;
        }
        else {
	        context.push(new Object[] {ns, name, qname, new AttributesImpl(attrs)});
	        
	        if(qname.equals("record")) {current.clear();}
	        
	        String currentHost=(String)current.get("source");
	        String t = getLink(currentHost==null?qname:currentHost+":"+qname);
	        if(t==null) {
	        	super.startElement(ns,name,qname,attrs);
	        }
        }
        if(DEBUG>1) System.out.println("DEBUG: esce startElement: "+qname);

    }
    
    public void endElement(java.lang.String ns, java.lang.String name, java.lang.String qname) throws SAXException {
    	if(DEBUG>1) System.out.println("DEBUG: entra endElement: "+qname);
    	if(qname.equals("links")) {
    		setupLinks(currentData.toString());
    		currentData.delete(0, currentData.length());
    	}
    	else {
	        dispatch(false);
	        try {
	        	context.pop();
	        	super.endElement("",qname,qname);
	        }
	        catch (Exception e) {
	        	e.printStackTrace();
	        }
    	}
       	
       	if(DEBUG>1) System.out.println("DEBUG: esce endElement: "+qname);
    }
    
    public void characters(char[] chars, int start, int len) throws SAXException {
    	if(DEBUG>1) System.out.println("DEBUG: entra character");
   		currentData.append(chars, start, len);
   		if(DEBUG>1) System.out.println("DEBUG: esce character");
    }
    
    private void setupLinks(String linksdata) {
    	String[] linkLines = linksdata.split("\n");
    	
    	links.clear();
    	classes.clear();
        
        String currentTag,currentPrefix;
        
        String read = null;
        
        for(int i=0;i<linkLines.length;i++){
        	read = linkLines[i];
        	if (!read.startsWith("#")) {
				StringTokenizer st = new StringTokenizer(read, ",");
				if (st.hasMoreTokens()) {
					currentTag = st.nextToken();
					if (st.hasMoreTokens()) {
						currentPrefix=st.nextToken();
						if(currentPrefix.equals("null")) currentPrefix="";
						links.put(currentTag, currentPrefix);
					}
					if (st.hasMoreTokens()) {
						classes.put(currentTag, st.nextToken());
					}
				}
			}
        }
    }

	public void dispatch(final boolean fireOnlyIfMixed) throws SAXException {
		if(DEBUG>1) System.out.println("DEBUG: entra dispatch");
        if (currentData.length() == 0) return; //skip it
        if(DEBUG>1) System.out.println("DEBUG: dispatch non skipped");
        Object[] ctx = (Object[]) context.peek();
        String ns=(String) ctx[0];
        String name=(String) ctx[1];
        String here = (String) ctx[2];
        AttributesImpl attrs = (AttributesImpl) ctx[3];
        
//        if (fireOnlyIfMixed) throw new IllegalStateException("Unexpected characters() event! (Missing DTD?)");
//        handler.handle_element(buffer.length() == 0 ? null : buffer.toString(), attrs);
        if(DEBUG>1) System.out.println("+++ Dispatch data for: "+here);
        
        current.put(here,currentData.toString());

        String currentHost=(String)current.get("source");
        String t=getLink(currentHost==null?here:currentHost+":"+here);
        if(t!=null) {
        	if(currentData.length()!=0) {
        		String link = buildLink(currentData.toString(), here, t);
        		attrs.addCDATAAttribute("key",link);
        	}
        	super.startElement(ns, name, here, attrs);
        }
        if(currentData.length()>0) {
        	super.characters(currentData.toString().toCharArray(), 0, currentData.length());
        	currentData.delete(0, currentData.length());
        }
        
        if(DEBUG>1) System.out.println("DEBUG: esce dispatch");
    }


	public String getResource(String name) {
		String path = ObjectModelHelper.getContext(objectModel).getRealPath(
				name);
		return path;
	}

	public void compose(ComponentManager manager) throws ComponentException {
//		StaticDataComponent staticdata = (StaticDataComponent) manager
				//.lookup(StaticDataComponent.ROLE);
		if(DEBUG>1) System.out.println("++ CreateLink compose executed");
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}
}