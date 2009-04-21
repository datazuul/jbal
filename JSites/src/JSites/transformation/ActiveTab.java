package JSites.transformation;

import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import JSites.utils.DBGateway;


public class ActiveTab extends MyAbstractPageTransformer {

	private long activePid;
	
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		super.setup(arg0, arg1, arg2, arg3);

		activePid = Long.parseLong(o.getParameter("pid"));

		Connection conn = null;
		try{
			conn = getConnection(dbname);
			long pLevel = DBGateway.getPageLevel(activePid,0,conn);
			
			while(pLevel>1){
				activePid = DBGateway.getPapid(activePid,conn);	
				pLevel = DBGateway.getPageLevel(activePid,0,conn);
			}
		}catch(Exception e) {e.printStackTrace();}
		
		try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}

	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException
	{
		AttributesImpl newAttrs = new AttributesImpl();
		for(int i=0;i<attributes.getLength();i++){
			String name = attributes.getQName(i);
		    String value = attributes.getValue(i);
		    if(!(name.equals("class")))
		    	newAttrs.addCDATAAttribute(name,value);
		}
		String href = attributes.getValue("href");
		if(href != null && href.contains("pid="+activePid))
			newAttrs.addCDATAAttribute("class","selected");
		else if(attributes.getValue("class")!=null)
			newAttrs.addCDATAAttribute("class",attributes.getValue("class"));

		super.startElement(namespaceURI, localName, qName, newAttrs);
	}
	
	
}
