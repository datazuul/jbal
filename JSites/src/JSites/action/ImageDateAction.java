package JSites.action;

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
import org.apache.cocoon.acting.AbstractAction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

import JSites.utils.site.ImageData;


public class ImageDateAction extends AbstractAction {

	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, SourceResolver resolver,
			Map objectModel, String source, Parameters params) {
		Map<String, String> sitemapParams = new HashMap<String, String>();
		

		Request request = ObjectModelHelper.getRequest(objectModel);
		String reqDate=request.getParameter("date");
		//request.setAttribute("hello", "world");
		
		if(reqDate==null||reqDate.length()==0) {
			reqDate=JSites.utils.DateUtil.getDateString();
		}
		
	    String path = ObjectModelHelper.getContext(objectModel).getRealPath(source);
	    
	    ImageData image=checkImage(reqDate,path);

		sitemapParams.put("imagename", image.getUrl());
		//sitemapParams.put("imagename", "images/jopac2.jpg");
		sitemapParams.put("type", image.getType());
		
		return sitemapParams;
	}

	private ImageData checkImage(String reqDate, String path) {
		ImageData image=new ImageData();
		try
		{
			File f=new File(path);
			BufferedReader br = new BufferedReader(new FileReader(f));
			while(br.ready())
			{
				String l=br.readLine();				
				image.setup(l);
				if(image.isValidWithDate(reqDate)) break;
			}
			br.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return image;
	}

}
