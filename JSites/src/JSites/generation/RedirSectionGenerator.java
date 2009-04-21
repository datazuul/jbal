package JSites.generation;

import java.sql.Connection;
import org.xml.sax.SAXException;
import JSites.utils.DBGateway;
import JSites.utils.site.Section;

public class RedirSectionGenerator extends MyAbstractPageGenerator {

	public void generate() throws SAXException {
		
		Section s = new Section();
		
		contentHandler.startDocument();

		Connection conn = this.getConnection(dbname);
		try{
			s.testo = DBGateway.getRedirectURL(pageId,conn);
			s.titolo = "Redirect";
			s.img = "images/contentimg/gk.jpg";
			s.ThrowSax(contentHandler);
		}catch(Exception e) {e.printStackTrace();}
		
		try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}
		
		contentHandler.endDocument();
		
	}	
}
