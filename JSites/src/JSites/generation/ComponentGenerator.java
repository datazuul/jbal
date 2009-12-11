package JSites.generation;

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

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.components.source.SourceUtil;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceValidity;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

public class ComponentGenerator extends ServiceableGenerator
implements CacheableProcessingComponent {

    /** The input source */
    protected Source inputSource;

    /**
     * Recycle this component.
     * All instance variables are set to <code>null</code>.
     */
    public void recycle() {
        if (null != this.inputSource) {
            super.resolver.release(this.inputSource);
            this.inputSource = null;
        }
        super.recycle();
    }

    /**
     * Setup the file generator.
     * Try to get the last modification date of the source for caching.
     */
    @SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par)
        throws ProcessingException, SAXException, IOException {

        super.setup(resolver, objectModel, src, par);
        try {
        	String componentName=src;
        	File f=new File(src);
        	if(!f.exists()) {
        		componentName=src.substring(0,src.indexOf("."));
        		String base="";
        		if(componentName.contains("/")) {
        			base=src.substring(0,src.lastIndexOf("/")+1);
        			checkDir(base);
        			componentName=componentName.substring(componentName.lastIndexOf("/")+1);
        		}
        		componentName=componentName.replaceAll("\\d", "");
        		base=getResource("/")+"/components/"+componentName+"/";
        		componentName=base+componentName+"0.xml";
//        		System.out.println(componentName);
        	}
            this.inputSource = super.resolver.resolveURI(componentName);
        } 
        catch (SourceException se) {
            throw SourceUtil.handle("Error during resolving of '" + src + "'.", se);
        }
    }
    
    private void checkDir(String base) {
		File d=new File(base);
		if(!d.isDirectory()) {
			d.mkdirs();
		}
		if(base.contains("/data")) {
			String t=base.substring(0,base.lastIndexOf("/data"));
			File c=new File(t+"/images/contentimg");
			if(!c.isDirectory()) c.mkdirs();
		}
		
	}

	public String getResource(String name) {
    	String path = ObjectModelHelper.getContext(objectModel).getRealPath(name);
    	return path;
    }

    /**
     * Generate the unique key.
     * This key must be unique inside the space of this component.
     *
     * @return The generated key hashes the src
     */
    public Serializable getKey() {
        return this.inputSource.getURI();
    }

    /**
     * Generate the validity object.
     *
     * @return The generated validity object or <code>null</code> if the
     *         component is currently not cacheable.
     */
    public SourceValidity getValidity() {
        return this.inputSource.getValidity();
    }

    /**
     * Generate XML data.
     */
    public void generate()
        throws IOException, SAXException, ProcessingException {

        try {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("Source " + super.source +
                                  " resolved to " + this.inputSource.getURI());
            }
            SourceUtil.parse(this.manager, this.inputSource, super.xmlConsumer);
        } catch (SAXException e) {
            SourceUtil.handleSAXException(this.inputSource.getURI(), e);
        }
    }
}
