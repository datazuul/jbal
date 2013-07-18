/*
 * LoadClasses.java
 *
 * Created on 5 febbraio 2004, 14.14
 */

package org.jopac2.engine.dbengine.importers;
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

/*
* @author	Albert Caramia
* @version ??/??/2002
* 
* @author	Romano Trampus
* @version ??/??/2002
* 
* @author	Romano Trampus
* @version	05/02/2004
*/
import java.io.PrintStream;
import java.util.*;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
//import org.jopac2.jbal.xmlHandlers.ClassHandlerImpl;
import org.jopac2.jbal.xmlHandlers.ClassItem;
//import org.jopac2.jbal.xmlHandlers.ClassParser;
//import org.jopac2.jbal.xmlHandlers.DataType;
//import org.jopac2.jbal.xmlHandlers.DataTypeHandlerImpl;
//import org.jopac2.jbal.xmlHandlers.DataTypeParser;

/**
 *
 * @author  romano
 */
public class LoadClasses {
    
//    private String configFile=null;
//    private String configPath=null;
    private Vector<ClassItem> SQLInstructions;
    private PrintStream out=null;
    
    public Vector<ClassItem> getSQLInstructions() {
        return SQLInstructions;
    }
    
    
//    /** 
//     * @deprecated
//    */
//    public LoadClasses(String xmlConfFile, PrintStream console) {
//        configFile=xmlConfFile;
//        configPath=xmlConfFile.substring(0,xmlConfFile.lastIndexOf('/'))+"/";
//        out=console;
//        out.println("Uso CONFIGPATH: "+configPath);
//    }
    
    /** Creates a new instance of LoadClasses */
    public LoadClasses(PrintStream console) {
//        configFile=xmlConfFile;
//        configPath=xmlConfFile.substring(0,xmlConfFile.lastIndexOf('/'))+"/";
        out=console;
//        out.println("Uso CONFIGPATH: "+configPath);
    }
    
    
//    /**
//     * @deprecated
//     * @return
//     */
//    private Vector<DataType> getTypes() {
//        DataTypeHandlerImpl dataHandler=new DataTypeHandlerImpl();
////        dataHandler.DEBUG=true;
//        DataTypeParser p=new DataTypeParser(dataHandler,null);
//        try {
//            org.xml.sax.InputSource i=new org.xml.sax.InputSource(configFile);
//            p.parse(i);
//        }
//        catch (Exception e) {
//            out.println(e.getMessage());
//        }
//        return dataHandler.getDataTypes();
//    }
    
    /**
     * 
     * @param name
     * @return
     */
    private Vector<ClassItem> processType(String name) {
        Vector<ClassItem> SQLInstructions=new Vector<ClassItem>();
        
        try {
        	RecordInterface ma=RecordFactory.buildRecord("0", null, name, 0);
        	Hashtable<String, List<Tag>> mapping=ma.getRecordMapping();
        	ma.destroy();
        	Enumeration<String> e=mapping.keys();
        	while(e.hasMoreElements()) {
        		String currentElement=e.nextElement();
        		Iterator<Tag> i=mapping.get(currentElement).iterator();
        		while(i.hasNext()) {
        			Tag t=i.next();
        			String tag=t.getTagName();
        			Vector<Field> f=t.getFields();
        			for(int n=0;f!=null && n<f.size();n++)
        				SQLInstructions.addElement(new ClassItem(name,currentElement,tag,f.elementAt(n).getFieldCode()));
        		}
        	}
        	
        }
        catch (Exception e) {
            out.println("WARN: "+name+" "+e.getMessage()+" (missing mapping?)");
        }
        return SQLInstructions;
    }
    
	public void doJob(String[] channels) {
        String[] v=RecordFactory.getRecordInterfaces();
        
        SQLInstructions=new Vector<ClassItem>();
        
        for(int i=0;i<v.length;i++) {            
            try {
                SQLInstructions.addAll(processType(v[i]));
            }
            catch (Exception e) {
                System.err.println("No data for class: "+v[i]);
            }
        }
        
        // aggiunta per Mdb
        for(int j=0;j<SQLInstructions.size();j++) {
        	String cl=SQLInstructions.elementAt(j).getClassName();
        	for(int z=0;z<channels.length;z++) {
        		if(channels[z].equals(cl)) {
        			channels[z]="";
        		}
        	}
        }
        for(int j=0;j<channels.length;j++) {
        	if(channels[j].length()>0) SQLInstructions.addElement(new ClassItem("Mdb", channels[j], channels[j], ""));
        }
    }
}
