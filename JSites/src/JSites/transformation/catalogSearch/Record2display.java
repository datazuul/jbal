/*
 * Record2display.java
 *
 * Created on 15 settembre 2004, 11.58
 */

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

/**
* @author	Albert Caramia
* @version	??/??/2002
* 
* @author	Romano Trampus
* @version	??/??/2002
* 
* @author	Romano Trampus
* @version	15/09/2004
* 
* @author 	Romano Trampus
* @version	09/03/2006
* Corretto bug sulle connessioni e aggiunto dispose: se la variabile di tipo 
* Connection e' globale allora non viene rilasciata e il pool si satura.
*/

import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.ProcessingException;

import org.apache.avalon.framework.parameters.Parameters;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.apache.cocoon.xml.AttributesImpl;

import java.util.Map;
import java.util.Vector;
import java.io.IOException;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentSelector;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.component.Composable;
import org.jopac2.engine.NewSearch.DoSearchNew;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.dbGateway.StaticDataComponent;
import org.jopac2.engine.parserRicerche.parser.exception.ExpressionException;
import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.BookSignature;

import com.ibm.icu.impl.duration.impl.Utils;
import com.mysql.jdbc.Util;

import JSites.generation.ImportCatalog;
import JSites.transformation.MyAbstractPageTransformer;


import java.sql.*;


public class Record2display extends MyAbstractPageTransformer implements Composable, Disposable //, CacheableProcessingComponent
{
    private boolean isRecord=false,debug=false,isCatalogConnection=false,isDbType=false;
    //private DataSourceComponent datasource;
    protected ComponentSelector dbselector;
    protected ComponentManager manager;

    //private RecordInterface ma;
    private BookSignature b;
    private StringBuffer buffer=null;
    protected String catalog,dbType;
    
    public void sendElement(String element,String value) throws SAXException {
    	if(value!=null) {
    		if(debug) System.out.println("Record2Display: element " + element + ": "+ value);
	    	value=value.replaceAll(String.valueOf((char)30),"");
	    	if(value.length()>0) {
		    	contentHandler.startElement("",element,element,new AttributesImpl());
		        contentHandler.characters(value.toCharArray(), 0, value.length());
		        contentHandler.endElement("",element,element);
	    	}
    	}
    	else {
    		if(debug) System.out.println("Record2display: ELEMENT "+ element + " IS NULL");
    	}
    }
    
    @SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par)
            throws ProcessingException, SAXException, IOException
    {
    	super.setup(resolver, objectModel, src, par);
    	debug=false;isRecord=false;
    	if(buffer!=null) {
    		buffer.delete(0, buffer.length());
    	}
    	else {
    		buffer=new StringBuffer();
    	}
    	
    	if(src!=null && src.equals("debug")) {
    		System.out.println("Record2display: DEBUG ENABLED");
    		debug=true;
    	}
    }

    /*private String creaChiave(String s) {
        String r=s.replaceAll("[ ,.;()/-\\:=@%$&!?#*<>]","+");

        return r;
    }*/
    

    
    private void includeData(Vector<RecordInterface> v, String name) throws SAXException {
    	super.startElement("",name,name,new AttributesImpl());
    	for(int i=0; v!=null & i<v.size();i++) {
    		contentHandler.startElement("","record","record",new AttributesImpl());
    		sendElement("isbd",v.elementAt(i).getISBD());
    		sendElement("bid",v.elementAt(i).getBid());
    		sendElement("jid",Long.toString(v.elementAt(i).getJOpacID()));
	        contentHandler.endElement("","record","record");
    	}
    	super.endElement("",name,name);
    }
    
    private void includeData(Vector<RecordInterface> v, String name, String tag) throws SAXException {
        super.startElement("",name,name,new AttributesImpl());
           
        for(int i=0;i<v.size();i++) {
        	sendElement(tag,(v.elementAt(i)).getISBD());
        }
        super.endElement("",name,name);
    }
    
    public void characters(char[] ch, int start, int len) throws SAXException {
    	buffer.append(ch, start, len);
    }
    
    private void sendMdb(RecordInterface ma2) throws SAXException {
    	String[] ch=ma2.getChannels();
    	for(int i=0;i<ch.length;i++) {
    		if(ch[i].startsWith("/")) {
    			String name=ch[i].substring(ch[i].lastIndexOf("/")+1);
    			String value=ma2.getField(name);
    			sendElement(name,value);
    		}
    	}
        sendElement("bid",ma2.getBid());
    	sendElement("jid", String.valueOf(ma2.getJOpacID()));
    }
    
    private void sendIso(RecordInterface ma2) throws SAXException {
    	Vector<String> v=null;
    	Vector<RecordInterface> vma=null;
    	Vector<BookSignature> vbs=null;
    	
        sendElement("type",ma2.getTipo());
        
		String file = saveImgFile(ma2);
		AttributesImpl a = new AttributesImpl();
		
		String nat = ma2.getPublicationNature();
		sendElement("nature",nat);
		if(nat != null && nat.length()==1)
			a.addCDATAAttribute("nature", nat);
		
		contentHandler.startElement("","image","image",a);
        contentHandler.characters(file.toCharArray(), 0, file.length());
        contentHandler.endElement("","image","image");
    	
    	if(debug) {
    		System.out.println(ma2.toString());
    	}
    	
    	sendElement("title",ma2.getTitle());
    	
    	v=ma2.getAuthors();
        if(v!=null && v.size()>0) {
            super.startElement("","authors","authors",new AttributesImpl());
            for(int i=0;i<v.size();i++) {
            	sendElement("author",(String)v.elementAt(i));
            }
            super.endElement("","authors","authors");
            v.clear();
        }
        v=null;

        
        sendElement("ISBD",ma2.getISBD());
        sendElement("description",ma2.getDescription());
        
        super.startElement("","editors","editors",new AttributesImpl());
        v=ma2.getEditors();
        if(v!=null) {
	        for(int i=0;i<v.size();i++) {
	        	sendElement("editor",(String)v.elementAt(i));
	        }
	        v.clear();
        }
        v=null;
        super.endElement("","editors","editors");
        
    	sendElement("standardNumber",ma2.getStandardNumber());
    	sendElement("publicationPlace",ma2.getPublicationPlace());
    	sendElement("publicationDate",ma2.getPublicationDate());

        Vector<SubjectInterface> v1=ma2.getSubjects();
        if((v1!=null)&&(v1.size()>0)) {
            super.startElement("","subjects","subjects",new AttributesImpl());

            for(int i=0;i<v1.size();i++) {
            	sendElement("subject",(v1.elementAt(i).getData()).elementAt(0).getContent());
            }

            super.endElement("","subjects","subjects");
            v1.clear();v1=null;
        }
        
        Vector<ClassificationInterface> vc=ma2.getClassifications();
        if((vc!=null)&&(vc.size()>0)) {
            super.startElement("","classifications","classifications",new AttributesImpl());

            for(int i=0;i<vc.size();i++) {
            	sendElement("classification",(String)vc.elementAt(i).toString());
            }

            super.endElement("","classifications","classifications");
            vc.clear();v=null;
        }
        
        if(ma2.hasLinkUp()) {
            vma=ma2.getIsPartOf();
            includeData(vma,"partof");
            if(vma!=null) {vma.clear();vma=null;}
        }
        if(ma2.hasLinkDown()) {
            vma=ma2.getHasParts();
            includeData(vma,"haspart");
            if(vma!=null) {vma.clear();vma=null;}
        }
        if(ma2.hasLinkSerie()) {
            vma=ma2.getSerie();
            includeData(vma,"serie","isbdserie");
            if(vma!=null) {vma.clear();vma=null;}
        }

    	sendElement("abstract",ma2.getAbstract());
        
        try {
        	if(v!=null) v.clear();
        	v=null;
        	vbs=ma2.getSignatures();
        }
        catch(Exception e) {
        	// ignora eventuali errori nella decodifica delle signature
        }

        if((vbs!=null)&&(vbs.size()>0)) {
            super.startElement("","signatures","signatures",new AttributesImpl());
            try {

                for(int i=0;i<vbs.size();i++) {
                	try {
                    	b=vbs.elementAt(i);
                        super.startElement("","signature","signature",new AttributesImpl());
                        sendElement("libraryId",b.getLibraryId());
                        sendElement("libraryName",b.getLibraryName());
                        sendElement("idNumber",b.getBookNumber());
                        sendElement("localization",b.getBookLocalization());
                        sendElement("holding",b.getBookCons());
                        super.endElement("","signature","signature");
                	}
                	catch(Exception e) {
                		// ignoro errori di codifica delle signatures
                	}
                }
            }
            catch(NullPointerException e) {}
            super.endElement("","signatures","signatures");
            vbs.clear();vbs=null;
        }
       
        sendElement("prezzo", ma2.getPrice());
        sendElement("bid",ma2.getBid());
//    	sendElement("jid", String.valueOf(ma2.getJOpacID()));
	}

	
	public void startElement(String namespaceURI, String localName, String qName,
            Attributes attributes) throws SAXException
    {
        if (namespaceURI.equals("") && localName.equals("record"))
            isRecord=true;
        if (namespaceURI.equals("") && localName.equals("catalogConnection")) {
            isCatalogConnection=true;
            return;
        }
        if (namespaceURI.equals("") && localName.equals("dbType")) {
            isDbType=true;
            return;
        }
        super.startElement(namespaceURI, localName, qName, attributes);
    }

    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException
    {
    	if (namespaceURI.equals("") && localName.equals("catalogConnection")) {
    		catalog=buffer.toString().replaceAll("[\n\r]", "").trim();
    		buffer.delete(0, buffer.length());
    		isCatalogConnection=false;
    		return;
    	}
    	else if (namespaceURI.equals("") && localName.equals("dbType")) {
    		dbType=buffer.toString().replaceAll("[\n\r]", "").trim();
    		buffer.delete(0, buffer.length());
    		isDbType=false;
    		return;
    	}
        if (namespaceURI.equals("") && localName.equals("record")) {
        	String id=buffer.toString().replaceAll("[\n\r]", "").trim();
            if(isRecord) {
            	sendElement("jid",id);
            	sendElement("db",catalog);
            	
            	Connection myConnection;
    			try {
    				    					
					myConnection = getConnection(dbname);
    				
    	            RecordInterface ma=DbGateway.getNotiziaByJID(myConnection,catalog,id);
    	            myConnection.close();
    	            if(ma!=null) { 
    	            	ma.setJOpacID(Long.parseLong(id));
    	            	String display=JSites.utils.Util.getRequestData(request, "display");
    	            	if(display!=null && display.equals("iso")) {
    	            		sendElement("bid",ma.getBid());
    	                	sendElement("jid", String.valueOf(ma.getJOpacID()));
    	                	sendElement("data",ma.toString());
    	            	}
    	            	else if(display!=null && display.equals("xml")) {
    	            		sendElement("bid",ma.getBid());
    	                	sendElement("jid", String.valueOf(ma.getJOpacID()));
    	                	try {
								sendElement("data",ma.toXML());
							} catch (Exception e) {
								sendElement("error",e.getMessage());
							}
    	            	}
    	            	else if(ma.getTipo().equals("mdb")) {
    	            		sendMdb(ma);
    	            	}
    	            	else {
    	            		sendIso(ma);
    	            	}
    	            	String nature=ma.getPublicationNature();
    	            	if(nature!=null && nature.equals("P")){
    	            		viewFascicoli(ma);
    	            	}
    	            	ma.destroy();
    	            }
    	            else {
    	            	sendElement("ERRORE","Ricevuto null da id_notizia = "+id);
    	            }      
    			} catch (ComponentException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			} catch (SQLException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
            }
            isRecord=false;
            buffer.delete(0, buffer.length());
        }
        else {
        	dispatch();
        }
        
        super.endElement(namespaceURI, localName, qName);
    }
    


	private void dispatch() throws SAXException {
    	super.characters(buffer.toString().toCharArray(), 0, buffer.length());
    	buffer.delete(0, buffer.length());
    }
    
    public Connection getConnection(String db) throws ComponentException, SQLException {
    	return ((DataSourceComponent)dbselector.select(db)).getConnection();
    }
    
    public void compose(ComponentManager manager) throws ComponentException {
    	this.manager=manager;
        dbselector =
            (ComponentSelector) manager.lookup(DataSourceComponent.ROLE + "Selector");
    }
    
    public void dispose() {
        this.manager.release(dbselector);
    }

/*	public Serializable getKey() {
    	if(src.startsWith("debug,")) {
    		System.out.println("Record2display: DEBUG ENABLED");
    		debug=true;
    		db=src.substring(6);
    	}
    	else {
    		db=src;
    	}
		return null;
	}

	public SourceValidity getValidity() {
		long t=System.currentTimeMillis()/1000000;
		System.out.println("getValidity: "+t);
		if(t>0) {
			return new TimeStampValidity(t);
		}
		else {
			return null;
		}
	}*/
    
    private void viewFascicoli(RecordInterface ma) {
    	try {
	    	String tit = ma.getTitle();
			StaticDataComponent sd = new StaticDataComponent();
			sd.init(JSites.utils.DirectoryHelper.getPath()+"/WEB-INF/conf/");
			DoSearchNew doSearchNew = new DoSearchNew(getConnection(dbname),catalog,sd);
			SearchResultSet result = doSearchNew.executeSearch("CLL="+tit, false);
			
			
		} catch (ExpressionException e1) { e1.printStackTrace();
		} catch (ComponentException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace();
		}
		
	}


}



