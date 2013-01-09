package JSites.linkParsers;
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
* @version	19/05/2005
*/
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author romano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DefaultParser extends LinkParser {
	
	public String parse(Hashtable<String,String> current, String tag, String value, String url) {
		value=value.replaceAll("\\$","_");
		url=url.replaceAll("%TAG%",tag);
		url=url.replaceAll("%VALUE%",value);
		
		if(current!=null) {
			String k;
			Enumeration<String> n=current.keys();
			while(n.hasMoreElements()) {
				k=(String)n.nextElement();
				url=url.replaceAll("%"+k+"%",(String)current.get(k));
			}
		}
		
		return url;
	}
}
