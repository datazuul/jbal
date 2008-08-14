package org.jopac2.jbal.importers.Readers;
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

import java.sql.SQLException;

import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.jopac2.jbal.dbGateway.LoadData;
import org.jopac2.jbal.dbGateway.ParoleSpooler;

public abstract class XmlHandler extends DefaultHandler {
	protected String rootElement, recordElement;
	protected boolean isRoot=false, isRecord=false;
	protected long start_time;
	protected java.util.Stack<Object> context;
	static final int DEBUG=1;
	protected StringBuffer currentData;
	protected String tipoNotizia;;
	protected long i=0,idTipo;
	protected double t=0;
	protected ParoleSpooler paroleSpooler;
	protected LoadData data;
	protected StringBuffer currentRecord;

	public XmlHandler(String rootElement, String recordElement) {
		super();
        this.rootElement=rootElement;
        this.recordElement=recordElement;
        start_time=System.currentTimeMillis();
        context = new java.util.Stack<Object>();
        currentRecord=new StringBuffer();
        currentData=new StringBuffer();
	}
	
	
	
    public void startElement(String ns, String name, String qname, Attributes attrs) throws SAXException {
    	if(DEBUG>1) System.out.println("DEBUG: entra startElement: "+qname);
        dispatch(true);
        context.push(new Object[] {ns, name, qname, new AttributesImpl(attrs)});
        
        if(qname.equals(rootElement)) {isRoot=true;}
        if(qname.equals(recordElement)) {isRecord=true;}
        
    	super.startElement(ns,name,qname,attrs);
    	
    	if(isRecord) {
    		currentRecord.append("<"+name);
    		for(int i=0;i<attrs.getLength();i++) {
    			currentRecord.append(" "+attrs.getQName(i)+"='"+attrs.getValue(i)+"'");
    		}
    		
    		currentRecord.append(">");
    	}
    
        if(DEBUG>1) System.out.println("DEBUG: esce startElement: "+qname);
    }
    
    public void endElement(java.lang.String ns, java.lang.String name, java.lang.String qname) throws SAXException {
    	if(DEBUG>1) System.out.println("DEBUG: entra endElement: "+qname);
        dispatch(false);
        
        if(isRoot&&isRecord) currentRecord.append("</"+qname+">");
        
        if(qname.equals(rootElement)) {isRoot=false;}
        if(qname.equals(recordElement)) {    		
        		
            if(isRoot&&isRecord&&currentRecord.length()>0) {
            	i++;
                if((i%1000)==0) {
                    t=(System.currentTimeMillis()-start_time)/(60000.0);
                    System.out.println(i+" record in "+ t +" minuti (" + i/t + " R/m)");
                }

                try {
                	data.process("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n"+currentRecord.toString(),tipoNotizia,idTipo,paroleSpooler);
    			} catch (SQLException e) {
    				e.printStackTrace();
    			} 
            }
            currentRecord.delete(0, currentRecord.length());
        	isRecord=false;
        }
        
        try {
        	context.pop();
        	super.endElement("",qname,qname);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
       	
       	if(DEBUG>1) System.out.println("DEBUG: esce startElement: "+qname);
    }
    
    public void characters(char[] chars, int start, int len) throws SAXException {
    	if(DEBUG>1) System.out.println("DEBUG: entra character");
   		currentData.append(chars, start, len);
   		if(DEBUG>1) System.out.println("DEBUG: esce character");
    }

	public void dispatch(final boolean fireOnlyIfMixed) throws SAXException {
		if(DEBUG>1) System.out.println("DEBUG: entra dispatch");
        if (currentData.length() == 0) return; //skip it
        if(DEBUG>1) System.out.println("DEBUG: dispatch non skipped");
        Object[] ctx = (Object[]) context.peek();
        @SuppressWarnings("unused") String ns=(String) ctx[0];
        @SuppressWarnings("unused") String name=(String) ctx[1];
        String here = (String) ctx[2];
        @SuppressWarnings("unused") AttributesImpl attrs = (AttributesImpl) ctx[3];
        
        if(DEBUG>1) System.out.println("+++ Dispatch data for: "+here);
        
        currentRecord.append(currentData.toString().replaceAll("&", "&amp;"));
        
    	

        super.characters(currentData.toString().toCharArray(), 0, currentData.length());
    	currentData.delete(0, currentData.length());
    	
        if(DEBUG>1) System.out.println("DEBUG: esce dispatch");
    }

	public void setParoleSpooler(ParoleSpooler paroleSpooler) {
		this.paroleSpooler=paroleSpooler;
	}



	public void setLoadData(LoadData data) {
		this.data=data;
	}



	public void setIdTipo(long idTipo) {
		this.idTipo=idTipo;
	}



	public void setTipoNotizia(String tipoNotizia) {
		this.tipoNotizia=tipoNotizia;
	}
}
