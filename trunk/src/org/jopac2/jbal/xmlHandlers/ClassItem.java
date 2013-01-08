/*
 * ClassItem.java
 *
 * Created on 5 febbraio 2004, 15.14
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
* @version	05/02/2004
*/
/**
 *
 * @author  romano
 * 
 * TODO
 * 18-09-2009 ClassiDettaglio in engine fa praticamente la stessa cosa e si trova nel
 * package giusto. Questa classe forse va rimossa.
 */
public class ClassItem {
    
    private String cName,eName,t,f;
    private long idTipo=-1;
    
    /** Creates a new instance of ClassItem */
    public ClassItem(String className, String elementName, String tag, String field) {
        cName=className;
        eName=elementName;
        t=tag;
        f=field;
    }
    
    public String getClassName() {
        return cName;
    }
    
    public String getElementName() {
        return eName;
    }
    
    public String getTag() {
        return t;
    }
    
    public String getField() {
        return f;
    }
    
    public String toString() {
        return(cName+"::"+eName+"::"+t+"::"+f);
    }

	public void setIdTipo(long idTipo) {
		this.idTipo = idTipo;
	}

	public long getIdTipo() {
		return idTipo;
	}
}
