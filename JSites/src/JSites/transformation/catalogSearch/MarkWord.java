/*
 * MarkWord.java
 *
 * Created on 24 settembre 2004, 8.46
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
* @version	24/09/2004
* 
* @author	Romano Trampus
* @version	14/01/2007
* Riscritto sulle nuove ricerche (utilizza optimizedQuery per trovare le parole utilizzate nella ricerca)
* Corretta la gestione dei characters
*/
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.ProcessingException;
import org.apache.avalon.framework.parameters.Parameters;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import java.util.Map;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.io.IOException;
//import java.lang.*;

import org.apache.avalon.framework.component.ComponentManager;
//import org.apache.avalon.framework.component.ComponentSelector;
import org.apache.avalon.framework.component.ComponentException;
//import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.component.Composable;
public class MarkWord extends AbstractTransformer implements Composable
{
    private boolean isRecord=false;
    private boolean isOptimizedQuery=false;
    private Hashtable<String,Boolean> v;
    private String left,right;
    private StringBuffer currentBuffer=new StringBuffer(500);
    
    
    @SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par)
            throws ProcessingException, SAXException, IOException
    {
//    	System.out.println("++ MarkWord started");
    }
    
    private boolean isParola(String parola) {
        return v.containsKey(quota(parola));
    }

    private String quota(String parola) {
    	parola = parola.toLowerCase();
		parola = parola.replaceAll("\u010d","c");
		parola = parola.replaceAll("š","s");
		parola = parola.replaceAll("ž","z");//
		parola = parola.replaceAll("\u0107","c");
		parola = parola.replaceAll("\u0111","d");
		parola = parola.replaceAll("dj","d");
		return parola;
	}

	public void characters(char[] ch, int start, int len) throws SAXException {
		currentBuffer.append(ch,start,len);
    }
    
    
    
    public void startElement(String namespaceURI, String localName, String qName,
            Attributes attributes) throws SAXException
    {
    	dispatch();
//    	System.out.println("Start:"+localName);
        if (namespaceURI.equals("") && localName.equals("root")) {v.clear();currentBuffer.delete(0, currentBuffer.length());}
        if (namespaceURI.equals("") && localName.equals("record")) isRecord=true;
        if (namespaceURI.equals("") && localName.equals("optimizedQuery")) isOptimizedQuery=true;
        
        super.startElement(namespaceURI, localName, qName, attributes);
    }

    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException
    {
    	dispatch();
        if (namespaceURI.equals("") && localName.equals("record")) isRecord=false;
        if (namespaceURI.equals("") && localName.equals("optimizedQuery")) isOptimizedQuery=false;
        super.endElement(namespaceURI, localName, qName);
    }
    
//    public void startDocument() throws SAXException {}
//    public void endDocument() throws SAXException {}
    
    public void compose(ComponentManager manager) throws ComponentException {
        v=new Hashtable<String,Boolean>();
        System.out.println("++ markWork loaded");
    }
    
    private void populateHashParole() {
    	/*
    	 * optimizedQuery e' della forma:
    	 * 
    	 *      [Ritter(6:0ms,qry:123ms,op:0ms)](2:0ms,qry:0ms,op:0ms)AND[Alexander(9:0ms,qry:76ms,op:0ms)]
    	 *      
    	 * Le parole cercate sono sempre comprese tra [......(
    	 */
    	String[] parole=currentBuffer.toString().split("\\[");
    	for(int i=0;i<parole.length;i++) {
    		int k=parole[i].indexOf('(');
    		if(k>1) {
    			v.put(quota(parole[i].substring(0,k)), new Boolean(true));
    		}
    	}
    	//v.put(quota(new String(ch,start,len).toLowerCase()),new Boolean(true));
//      System.out.println("Got: "+new String(ch,start,len));
    }
    
    private void dispatch() throws SAXException {
    	if(isOptimizedQuery) {
        	populateHashParole();
        	super.characters(currentBuffer.toString().toCharArray(),0,currentBuffer.length());
        }
        if(isRecord) {
            left="";
            StringTokenizer tk=new StringTokenizer(currentBuffer.toString()," ,.;()/-'\\:=@%$&!?[]#*<>\016\017",true);
            while(tk.hasMoreTokens()) {
                right=tk.nextToken();
                if(isParola(right.toLowerCase())) {
                	right+=" ";
                    super.startElement("","text","text",new AttributesImpl());
                    super.characters(left.toCharArray(),0,left.length());
                    super.endElement("","text","text");
                    super.startElement("","match","match",new AttributesImpl());
                    super.characters(right.toCharArray(),0,right.length());
                    super.endElement("","match","match");
                    left="";
                }
                else {
                    left+=right;
                }
            }
            if(left.length()>0) {
                super.startElement("","text","text",new AttributesImpl());
                super.characters(left.toCharArray(),0,left.length());
                super.endElement("","text","text");
            }
        }
        else {
            super.characters(currentBuffer.toString().toCharArray(),0,currentBuffer.length());
        }
        currentBuffer.delete(0, currentBuffer.length());
    }
}
