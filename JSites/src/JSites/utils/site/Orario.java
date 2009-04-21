package JSites.utils.site;

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
               "<hfield type=\"\">" + HtmlCodec.encode("Lunedì") + "</hfield><hfield type=\"\">" + HtmlCodec.encode("Martedì") + "</hfield><hfield type=\"\">" + HtmlCodec.encode("Mercoledì") + "</hfield>"+
               "<hfield type=\"\">" + HtmlCodec.encode("Giovedì") + "</hfield><hfield type=\"\">" + HtmlCodec.encode("Venerdì") + "</hfield><hfield type=\"\">" + HtmlCodec.encode("Sabato") + "</hfield>"+
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
