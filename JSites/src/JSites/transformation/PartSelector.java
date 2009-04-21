package JSites.transformation;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import JSites.utils.DBGateway;

import JSites.authentication.Authentication;
import JSites.authentication.Permission;

public class PartSelector extends MyAbstractPageTransformer {
	
	private Permission permission;
	private long cid;
	private boolean write = true; 
	
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		super.setup(arg0, arg1, arg2, arg3); 
		write = true;   
		try{
			 long pageId=1;
			 String stringPid = o.getParameter("pid");
			 if(stringPid != null)
				 pageId = Long.parseLong(stringPid);
			 String stringCid = o.getParameter("cid");
			 if(stringCid != null)
				 cid = Long.parseLong(stringCid);
			 
			 Connection conn = null; 
			 try{
				 conn = this.getConnection(dbname);
				 Session session = this.sessionManager.getSession(true);
				 permission = Authentication.assignPermissions(session, pageId, conn);
			 }catch(Exception e) {e.printStackTrace();}
				
			 try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}
			 
		 }
		 catch(Exception e){e.printStackTrace();}
		 
		 if(permission==null)permission=new Permission();
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException
	{
		try{
			Connection conn = this.getConnection(dbname);

			if(DBGateway.getState(cid, conn) == 4){
				if(localName.equals("titolo"))
					super.startElement(namespaceURI, localName, qName, attributes);
				else if(localName.equals("section"))
					super.startElement(namespaceURI, "oldsection", "oldsection", attributes);
				else
					write=false;
			}
			else 
				super.startElement(namespaceURI, localName, qName, attributes);

			conn.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
		try{
			Connection conn = this.getConnection(dbname);

			if(DBGateway.getState(cid, conn) == 4){
				if(localName.equals("titolo"))
					super.endElement(namespaceURI, localName, qName);
				else
					write=true;
			}
			else 
				super.endElement(namespaceURI, localName, qName);

			conn.close();
		}
		catch(Exception e){e.printStackTrace();}
    }
	
	public void characters(char[] a, int s, int e) throws SAXException{
		if(write)
		super.characters(a,s,e);
	}
	
	
	
}
