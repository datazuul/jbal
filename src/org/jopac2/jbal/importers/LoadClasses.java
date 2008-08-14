/*
 * LoadClasses.java
 *
 * Created on 5 febbraio 2004, 14.14
 */

package org.jopac2.jbal.importers;
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
import java.util.*;
import org.jopac2.jbal.xmlHandlers.*;

/**
 *
 * @author  romano
 */
public class LoadClasses {
    
    private String configFile;
    private String configPath;
    private Vector<ClassItem> SQLInstructions;
    
    public Vector<ClassItem> getSQLInstructions() {
        return SQLInstructions;
    }
    
    
    /** Creates a new instance of LoadClasses */
    public LoadClasses(String xmlConfFile) {
        configFile=xmlConfFile;
        configPath=xmlConfFile.substring(0,xmlConfFile.lastIndexOf('/'))+"/";
        System.out.println("Uso CONFIGPATH: "+configPath);
    }
    
    private Vector<DataType> getTypes() {
        DataTypeHandlerImpl dataHandler=new DataTypeHandlerImpl();
//        dataHandler.DEBUG=true;
        DataTypeParser p=new DataTypeParser(dataHandler,null);
        try {
            org.xml.sax.InputSource i=new org.xml.sax.InputSource(configFile);
            p.parse(i);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return dataHandler.getDataTypes();
    }
    
    public Vector<ClassItem> processType(String name) {
        Vector<ClassItem> SQLInstructions=null;
        ClassHandlerImpl classHandler=new ClassHandlerImpl();
        ClassParser p=new ClassParser(classHandler,null);
        try {
            org.xml.sax.InputSource i=new org.xml.sax.InputSource(configPath+name+".xml");
            p.parse(i);
            SQLInstructions=p.getSQLInstructions();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return SQLInstructions;
    }
    
	public void doJob() {
        Vector<DataType> v=getTypes();
        Iterator<DataType> i=v.iterator();
        
        SQLInstructions=new Vector<ClassItem>();
        
        while(i.hasNext()) {
            DataType currentDataType=(DataType) i.next();
            
            String currentName=currentDataType.getName();
//            System.err.println("Processing: "+currentName);
            try {
                SQLInstructions.addAll(processType(currentName));
            }
            catch (Exception e) {
                System.err.println("No data for class: "+currentName);
            }
        }
    }
}
