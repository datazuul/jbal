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
/*
 * DataType.java
 *
 * Created on 18 gennaio 2004, 14.00
 */

/**
 *
 * @author  romano
 */
public class DataType {
    
    private String n;
    private String d;
    
    /** Creates a new instance of DataType */
    public DataType(String name, String description) {
        n=name;
        d=description;
    }
    
    public String getName() {
        return n;
    }
    
    public String getDescription() {
        return d;
    }
}
