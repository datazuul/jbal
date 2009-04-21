package JSites.generation;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2005 JOpac2 project
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
* @author   Romano Trampus
* @version	31/12/2004
*/

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.io.IOException;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;

import org.apache.avalon.framework.parameters.Parameters;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentSelector;
import org.apache.avalon.framework.component.Composable;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.generation.AbstractGenerator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author romano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class myAbsGenerator extends AbstractGenerator implements Composable, Disposable {
//	public DataSourceComponent datasource;
	public ComponentManager manager;
    public ComponentSelector dbselector;
    protected Parameters par;
    
    public void sendElement(String element,String value) throws SAXException {
    	contentHandler.startElement("",element,element,new AttributesImpl());
        contentHandler.characters(value.toCharArray(), 0, value.length());
        contentHandler.endElement("",element,element);
    }
    
    public void compose(ComponentManager manager) throws ComponentException {
    	this.manager = manager;
        dbselector = (ComponentSelector) manager.lookup(DataSourceComponent.ROLE + "Selector");
    }
    
	public void dispose() {
		this.manager.release(dbselector);
	}
    
    public Connection getConnection(String db) throws ComponentException, SQLException {
    	return ((DataSourceComponent)dbselector.select(db)).getConnection();
    }
    
    public String getResource(String name) {
    	String path = ObjectModelHelper.getContext(objectModel).getRealPath(name);
    	return path;
    }
    
    @SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws IOException, ProcessingException, SAXException {
    	this.par = par;
    	super.setup(resolver,objectModel,src,par);
    }
    
}
