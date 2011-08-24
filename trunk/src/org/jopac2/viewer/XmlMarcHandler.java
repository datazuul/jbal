package org.jopac2.viewer;
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
 * TODO: creare un'nterfaccia
 */


import org.apache.cocoon.xml.AttributesImpl;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.utils.JOpac2Exception;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlMarcHandler extends DefaultHandler {
	protected String rootElement, recordElement;
	protected boolean isRoot=false, isRecord=false;
	protected long start_time;
	protected java.util.Stack<Object> context;
	static final int DEBUG=1;
	protected StringBuffer currentData;
	protected String tipoNotizia;;
	protected long i=0,idTipo;
	protected double t=0;
	protected RecordInterface ma=null;
	private String leader="leader";
	private String controlField="controlfield";
	private String dataField="datafield";
	private String subField="subfield";
	private Attributes currentAttributes;
	private Tag currentTag;
	private Field currentField;

	public XmlMarcHandler(String rootElement, String recordElement) {
		super();
        this.rootElement=rootElement;
        this.recordElement=recordElement;
        start_time=System.currentTimeMillis();
        context = new java.util.Stack<Object>();
        currentData=new StringBuffer();
	}

	
    public void startElement(String ns, String name, String qname, Attributes attrs) throws SAXException {
    	if(DEBUG>1) System.out.println("DEBUG: entra startElement: "+qname);
        dispatch();
        context.push(new Object[] {ns, name, qname, new AttributesImpl(attrs)});
        
        if(qname.equals(rootElement)) {isRoot=true;}
        if(qname.equals(recordElement)) {
        	isRecord=true;
        	try {
        		if(ma!=null) throw new JOpac2Exception("Not null record at beginning of new record. " +
        				"Previous record could be not processed?");
				ma=RecordFactory.buildRecord(0, null, "eutmarc",0);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        if(qname.equals(controlField)||qname.equals(dataField)) {
        	currentTag=new Tag(attrs.getValue("tag"));
        	
        	String i1=attrs.getValue("ind1");
        	String i2=attrs.getValue("ind2");
        	
        	if(i1!=null && i1.length()>0) {
        		currentTag.setModifier1(i1.charAt(0));
        	}
        	if(i2!=null && i2.length()>0) {
        		currentTag.setModifier2(i2.charAt(0));
        	}
        }
        if(qname.equals(subField)) {
        	currentField=new Field(attrs.getValue("code"),null);
        }
        
    	super.startElement(ns,name,qname,attrs);
    
        if(DEBUG>1) System.out.println("DEBUG: esce startElement: "+qname);
    }
    
    public void endElement(java.lang.String ns, java.lang.String name, java.lang.String qname) throws SAXException {
    	if(DEBUG>1) System.out.println("DEBUG: entra endElement: "+qname);
        
    	
        if(qname.equals(rootElement)) {isRoot=false;}
        if(qname.equals(recordElement)) {    		
        	process(ma);
        	ma.destroy();
        	ma=null;
        	isRecord=false;
        }
        
        if(qname.equals(controlField)) {    		
        	try {
				currentTag.setRawContent(currentData.toString());
			} catch (JOpac2Exception e) {
				e.printStackTrace();
			}
			ma.addTag(currentTag);
			currentTag=null;
        }
        if(qname.equals(dataField)) {    		
        	ma.addTag(currentTag);
        	currentTag=null;
        }
        if(qname.equals(subField)) {    		
        	currentField.setContent(currentData.toString());
        	currentTag.addField(currentField);
        	currentField=null;
        }
        if(qname.equals(leader)) {
        	ma.setStatus(currentData.substring(5,6));
            // 4 bytes, are "Implementation Codes"
            ma.setType(currentData.substring(6,7));
            ma.setBiblioLevel(currentData.substring(7,8));
            ma.setHierarchicalLevel(currentData.substring(8,9));
            ma.setCharacterEncodingScheme(currentData.substring(9,10));
        }
                
        dispatch();
        context.pop();
    	super.endElement(ns,name,qname);


       	
       	if(DEBUG>1) System.out.println("DEBUG: esce startElement: "+qname);
    }

	private void process(RecordInterface ma2) {
		try {
			System.out.println(ma.toXML());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void characters(char[] chars, int start, int len) throws SAXException {
    	if(DEBUG>1) System.out.println("DEBUG: entra character");
   		currentData.append(chars, start, len);
   		if(DEBUG>1) System.out.println("DEBUG: esce character");
    }

	public void dispatch() throws SAXException {
		if(DEBUG>1) System.out.println("DEBUG: entra dispatch");
        if (currentData.length() == 0) return; //skip it
        if(DEBUG>1) System.out.println("DEBUG: dispatch non skipped");
        Object[] ctx = (Object[]) context.peek();
        @SuppressWarnings("unused") String ns=(String) ctx[0];
        @SuppressWarnings("unused") String name=(String) ctx[1];
        String here = (String) ctx[2];
        @SuppressWarnings("unused") AttributesImpl attrs = (AttributesImpl) ctx[3];
        
        if(DEBUG>1) System.out.println("+++ Dispatch data for: "+here);
                
        super.characters(currentData.toString().toCharArray(), 0, currentData.length());
    	currentData.delete(0, currentData.length());
    	
        if(DEBUG>1) System.out.println("DEBUG: esce dispatch");
    }

}
