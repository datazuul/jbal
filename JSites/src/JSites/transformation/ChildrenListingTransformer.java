package JSites.transformation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeSet;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import JSites.utils.site.Section;

public class ChildrenListingTransformer extends MyAbstractPageTransformer{
	
	private boolean listing = false;
	private boolean recording = false;
	private boolean imaging = false;
	private String testo = "";
	private String titolo ="";
	private String img = "";
	private String list = "";
	private String link = "";
	private String titoloPrinc = "";
	
	private AttributesImpl emptyAttrs = new AttributesImpl();
	
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		super.setup(arg0, arg1, arg2, arg3);
		listing = false;
		recording = false;
		imaging = false;
		testo = "";
		titolo ="";
		img = "";
		list = "";
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
		if(localName.equals("listParent"))
			listing = true;
		else if(localName.equals("testo"))
			recording = true;
		else if(localName.equals("img"))
			imaging = true;
	}
	
	public void startDocument() throws SAXException {
		super.startDocument();
		super.startElement("","section","section", emptyAttrs);
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if(localName.equals("listParent"))
			listing = false;
		else if(localName.equals("testo"))
			recording = false;
		else if(localName.equals("img"))
			imaging = false;
	}
	
	public void characters(char[] c, int s, int e) throws SAXException{
		if(listing){
			TreeSet<String> treelist = new TreeSet<String>();
			String papid = (new String(c).substring(s,s+e));

			Connection conn = null;
			try{
				conn = getConnection(dbname);
				Statement st = conn.createStatement();
				PreparedStatement ps = conn.prepareStatement("Select PID, name from tblpagine where Valid=1 and InSidebar=1 and PaPID = ?");
						
				ResultSet rs = null;
				
				String query = "";
				if(papid.matches("\\d+"))
					query = "select PID, Name from tblpagine where Valid=1 and InSidebar=1 and PID="+papid;
				else
					query = "select PID, Name from tblpagine where Valid=1 and InSidebar=1 and Name='"+papid+"'";
					
				rs = st.executeQuery(query);
				
				if(rs.next()){
					papid = rs.getString("PID");
					titoloPrinc = rs.getString("Name");
					link = "pageview?pid=" + papid;
					titolo = "[" + link + ">" + titoloPrinc + "]";
				}
				rs.close();
				
				emptyAttrs.addCDATAAttribute("type","2");
				super.startElement("", "titolo", "titolo", emptyAttrs);
				super.characters(titolo.toCharArray(), 0, titolo.length());
				super.endElement("", "titolo", "titolo");
				emptyAttrs.clear();
				

				query = "select PID from tblpagine where Valid=1 and InSidebar=1 and PID!=14 and PaPID="+papid;
				rs = st.executeQuery(query);
				
				while(rs.next()){
					ps.setLong(1,rs.getLong(1));
					ResultSet rs1 = ps.executeQuery();
					while(rs1.next()){
						treelist.add(rs1.getString("Name") + " #" + rs1.getString("PID"));
					}
					rs1.close();
				}
				ps.close();
				rs.close();
				st.close();
			}catch(Exception ex) {ex.printStackTrace();}
			
			try{ if(conn!=null)conn.close(); } catch(Exception ex){System.out.println("Non ho potuto chiudere la connessione");}
				
			list = Section.listChilds(treelist);
		}
		else if(recording)
			testo = testo + new String(c).substring(s,s+e);
		else if(imaging)
			img = img + new String(c).substring(s,s+e);
	}
	
	public void endDocument() throws SAXException{
		
		testo = testo + list;
		
		super.startElement("", "testo", "testo", emptyAttrs);
		super.characters(testo.toCharArray(), 0, testo.length());
		super.endElement("", "testo", "testo");

		emptyAttrs.addCDATAAttribute("link",link);
		emptyAttrs.addCDATAAttribute("alt",titoloPrinc);
		super.startElement("", "img", "img", emptyAttrs);
		super.characters(img.toCharArray(), 0, img.length());
		super.endElement("", "img", "img");
		emptyAttrs.clear();
		
		super.endElement("", "section", "section");

		super.endDocument();
	}

}