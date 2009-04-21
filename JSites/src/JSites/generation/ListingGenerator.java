package JSites.generation;


import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.TreeSet;



import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.xml.sax.SAXException;

import JSites.authentication.Authentication;
import JSites.authentication.Permission;

import JSites.utils.DBGateway;
import JSites.utils.site.Section;

public class ListingGenerator extends MyAbstractPageGenerator {

	public void generate() throws SAXException {
			
		contentHandler.startDocument();
		long pid = Long.parseLong(request.getParameter("pid"));

		Connection conn = null;
		try{
			conn = this.getConnection(dbname);
			Section s = new Section();
			String nome = DBGateway.getPageName(pid, conn);
			if(!(DBGateway.isPageValid(pid, conn))){
				nome = nome + " (questa pagina non è attiva)";
			}
			if(nome.startsWith("Altre")){
				try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}
				contentHandler.endDocument();
				return;
			}
			s.titolo = "[pageview?pid=" + pid + ">" + nome + "]";
			s.testo = getChildList(conn, pid);
			if(!(s.testo.equals(""))){
				s.testo = s.testo;
			}
			
			s.ThrowSax(contentHandler);
		} catch(Exception e) {e.printStackTrace();}
		
		try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}
		
		contentHandler.endDocument();
	}
	
	private String getChildList(Connection conn, long cpid) throws SQLException {
		TreeSet<String> tree = getTree(conn, cpid);
		return Section.listChilds(tree);
	}

	private TreeSet<String> getTree(Connection conn, long papid) throws SQLException {
		TreeSet<String> tree = new TreeSet<String>();
		PreparedStatement st = conn.prepareStatement("select Name, PID from tblpagine where PaPID=?");
		st.setLong(1, papid);
		ResultSet rs = st.executeQuery();
		long tempPid = 0; Permission tempP = null;
		while(rs.next()){
			tempPid = rs.getLong("PID");
			tempP = Authentication.assignPermissions(session, tempPid, conn);
			if(tempP.hasPermission(Permission.ACCESSIBLE) && DBGateway.isPageValid(tempPid,conn) && DBGateway.isPageInSidebar(tempPid,conn)){
				tree.add(rs.getString("Name") + " #" + tempPid);
				tree.addAll(getTree(conn, tempPid));
			}
		}
		rs.close();
		st.close();
		return tree;
	}
	
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException{
		super.setup(resolver, objectModel,src,par);
		containerType="listing";
	}
}
