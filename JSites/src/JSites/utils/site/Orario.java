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
import java.sql.ResultSet;
import java.sql.SQLException;

import JSites.utils.HtmlCodec;

public class Orario extends Table {
	
	public void init(ResultSet rs) throws SQLException {
		
		title = "Orario";
		rows = 6;
		columns = 4;

		body = "<header><upper>"+
		       "<hfield type=\"tabella_area\">Apertura Mattino</hfield>" + "<hfield type=\"tabella_area\">Chiusura Mattino</hfield>"+
		       "<hfield type=\"tabella_area\">Apertura Pomeriggio</hfield><hfield type=\"tabella_area\">Chiusura Pomeriggio</hfield>"+
               "</upper><left>"+
               "<hfield type=\"\">" + HtmlCodec.encode("Luned�") + "</hfield><hfield type=\"\">" + HtmlCodec.encode("Marted�") + "</hfield><hfield type=\"\">" + HtmlCodec.encode("Mercoled�") + "</hfield>"+
               "<hfield type=\"\">" + HtmlCodec.encode("Gioved�") + "</hfield><hfield type=\"\">" + HtmlCodec.encode("Venerd�") + "</hfield><hfield type=\"\">" + HtmlCodec.encode("Sabato") + "</hfield>"+
               "</left></header><body>"+
               "<record><field>" + rs.getString("LUmaI") + "</field><field>" + rs.getString("LUmaF") + "</field>" +
               "<field>" + rs.getString("LUpoI") + "</field><field>" + rs.getString("LUpoF") + "</field></record>" + 
               "<record><field>" + rs.getString("MAmaI") + "</field><field>" + rs.getString("MAmaF") + "</field>" +
               "<field>" + rs.getString("MApoI") + "</field><field>" + rs.getString("MApoF") + "</field></record>" + 
               "<record><field>" + rs.getString("MEmaI") + "</field><field>" + rs.getString("MEmaF") + "</field>" +
               "<field>" + rs.getString("MEpoI") + "</field><field>" + rs.getString("MEpoF") + "</field></record>" + 
               "<record><field>" + rs.getString("GImaI") + "</field><field>" + rs.getString("GImaF") + "</field>" +
               "<field>" + rs.getString("GIpoI") + "</field><field>" + rs.getString("GIpoF") + "</field></record>" + 
               "<record><field>" + rs.getString("VEmaI") + "</field><field>" + rs.getString("VEmaF") + "</field>" +
               "<field>" + rs.getString("VEpoI") + "</field><field>" + rs.getString("VEpoF") + "</field></record>" + 
               "<record><field>" + rs.getString("SAmaI") + "</field><field>" + rs.getString("SAmaF") + "</field>" +
               "<field>" + rs.getString("SApoI") + "</field><field>" + rs.getString("SApoF") + "</field></record>" + 
               "</body>";
		body = body.toLowerCase();
		body = body.replaceAll("continuato", "----").replaceAll("chiuso", "").replaceAll("null","");
	}

}
