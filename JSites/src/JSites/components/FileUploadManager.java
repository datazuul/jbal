package JSites.components;

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
* @author	Romano Trampus
* @version 29/12/2004
*/

import org.apache.avalon.framework.component.Component;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.cocoon.servlet.multipart.Part;


public interface FileUploadManager extends Component {
	String ROLE="JOpac2.FileUploadManager";
	
	public void contextualize(Context context) throws ContextException;
	public void configure(Configuration conf) throws ConfigurationException;
	public String getUploadFolder();
	public void setUploadFolder(String uploadfolder);
	public String upload(Part source) throws Exception;
	public void upload(Part source, String destfilename) throws Exception;
	
}


