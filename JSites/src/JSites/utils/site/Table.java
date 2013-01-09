package JSites.utils.site;
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
import JSites.utils.HtmlCodec;

public class Table extends Component {

	public String title = "";
	public String description = "";
	public int rows = 0;
	public int columns = 0;
	public String body = "";
	
	public String XMLSerialize() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><table><title>" + HtmlCodec.encode(title) + "</title>" +
			   "<description>" + HtmlCodec.encode(description) + "</description><rows>" + rows + "</rows><columns>" + columns + "</columns>" + 
			   body + "</table>";
	}

}
