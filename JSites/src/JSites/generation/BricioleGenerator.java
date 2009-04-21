package JSites.generation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.xml.sax.SAXException;

import JSites.utils.DBGateway;

public class BricioleGenerator extends MyAbstractPageGenerator {
	
	int startLevel = 2;

	public void generate() throws SAXException {
		
		contentHandler.startDocument();

		Connection conn = null;
		try{
			conn = this.getConnection(dbname);
			String nome = DBGateway.getPageName(this.pageId, conn);
			long papid = DBGateway.getPapid(pageId, conn);
			int pageLevel = DBGateway.getPageLevel(pageId, 0, conn)+1;
			if( pageLevel >= startLevel){
				contentHandler.startElement("","briciole","briciole", emptyAttrs);
				if (papid > 1)
					nome = getParent(papid, conn) + " > " + nome;
				contentHandler.characters(nome.toCharArray(), 0, nome.length());
				contentHandler.endElement("","briciole","briciole");
			}
		} catch(Exception e) {e.printStackTrace();}
		
		try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}
		
		contentHandler.endDocument();
		
	}

	private String getParent(long pid, Connection conn) throws SQLException {
		
		String ret = "[pageview?pid="+pid+">"+DBGateway.getPageName(pid, conn)+"]";
		long papid = DBGateway.getPapid(pid, conn);
		if(papid > 1){
			ret = getParent(papid, conn)  + " > " + ret;
		}
		
		return ret;
		
	}

	@SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException {
		super.setup(resolver, objectModel, src, par);
		try {
			String startLevelString = null;
			startLevelString = par.getParameter("startLevel");
			if(startLevelString != null)
				startLevel = Integer.parseInt(startLevelString);
		} catch (ParameterException e) {}
		
	}

	
}
