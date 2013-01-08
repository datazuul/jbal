package org.jopac2.engine.metasearch.managers;
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
* @author	Albert Caramia
* @version	19/01/2005
* 
* @author	Romano Trampus
* @version  19/01/2005
* 
* @author	Romano Trampus
* @version	19/05/2005
*/
import java.io.BufferedReader;
import java.net.URLEncoder;
import java.util.*;

import org.jopac2.utils.RecordItem;

public class EasyWebParser implements Parser {

	public Vector<RecordItem> parse(BufferedReader r, String host, String contextDir, String dbname) {
		Vector<RecordItem> result = new Vector<RecordItem>();
		String i;
		
		try {
			i=r.readLine();
			while(r.ready()) {
				i+=r.readLine();
			}
		
			i=URLEncoder.encode(i,"UTF-8");
			String data=i, dataType="pubmed", dataSyntax="pubmed", 
				databasecode="", id="";
			result.add(new RecordItem(host, dbname, data, dataType, dataSyntax, databasecode, id));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}