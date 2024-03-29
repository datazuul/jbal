/*
 * File:           ClassHandlerImpl.java
 * Date:           18 gennaio 2004  15.57
 *
 * @author  romano
 * @version generated by NetBeans XML module
 */

package org.jopac2.jbal.xmlHandlers;
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
* @version	18/01/2004
*/
import org.xml.sax.*;

public class ClassHandlerImpl implements ClassHandler {
    
    public static final boolean DEBUG = false;
    private String className;
    private String currentElement;
    private java.util.Vector<ClassItem> SQLInstructions;
    
    public ClassHandlerImpl() {
        SQLInstructions=new java.util.Vector<ClassItem>();
    }
    
    public java.util.Vector<ClassItem> getSQLInstructions() {
        return SQLInstructions;
    }
    
    public void start_Element(String elementName,final Attributes meta) throws SAXException {
        currentElement=elementName;
    }
    
    public void end_Element() throws SAXException {
        if (DEBUG) System.err.println("chiamato end_Element");
        currentElement=null;
    }   
    
    public void start_class(final Attributes meta) throws SAXException {
        className=meta.getValue("name");
        
        if (DEBUG) System.err.println("start_class: " + meta);
    }
    
    public void end_class() throws SAXException {
        if (DEBUG) System.err.println("end_class()");
        className=null;
    }
    
    public void handle_element(final Attributes meta) throws SAXException {
        SQLInstructions.addElement(new ClassItem(className,currentElement,meta.getValue("tag"),meta.getValue("field")));
        if (DEBUG) System.err.println(className+"::"+currentElement+"::"+meta.getValue("tag")+"::"+meta.getValue("field"));
        if (DEBUG) System.err.println("handle_element: " + meta);
    }
    
    public void start_description(final Attributes meta) throws SAXException {
        if (DEBUG) System.err.println("start_description: " + meta);
    }
    
    public void end_description() throws SAXException {
        if (DEBUG) System.err.println("end_description()");
    }
 
}

