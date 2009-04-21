package JSites.generation;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;

import it.univts.autenticazione.tomcat.realm.Auth;

import org.xml.sax.SAXException;

public class AuthGenerator extends MyAbstractPageGenerator {
	
	private String username;
	private String password;
	private String displayName="",mail="";

	public void generate() throws SAXException {
		username = request.getParameter("name");
		password = request.getParameter("password");
		
		contentHandler.startDocument();
		contentHandler.startElement("","authentication","authentication", this.emptyAttrs);
		
		if(username.equals("guest") || test(username, password)){
			contentHandler.startElement("","ID","ID", this.emptyAttrs);
			contentHandler.characters(username.toCharArray(), 0, username.length());
			contentHandler.endElement("","ID","ID");
			
			/*contentHandler.startElement("","role","role", this.emptyAttrs);
			contentHandler.characters("admin".toCharArray(), 0, 5);
			contentHandler.endElement("","role","role");*/
			
			contentHandler.startElement("","data","data", this.emptyAttrs);
			
			contentHandler.startElement("","displayName","displayName", this.emptyAttrs);
			contentHandler.characters(displayName.toCharArray(), 0, displayName.length());
			contentHandler.endElement("","displayName","displayName");
			
			contentHandler.startElement("","username","username", this.emptyAttrs);
			contentHandler.characters(username.toCharArray(), 0, username.length());
			contentHandler.endElement("","username","username");
			
			contentHandler.startElement("","mail","mail", this.emptyAttrs);
			contentHandler.characters(mail.toCharArray(), 0, mail.length());
			contentHandler.endElement("","mail","mail");
			
			contentHandler.endElement("","data","data");
		}
		
		
		contentHandler.endElement("","authentication","authentication");
		contentHandler.endDocument();
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	private boolean test(String u, String p) {
		
		boolean ret = false;
		
		String ds = "ldap://dc1ts.ds.units.it:389/dc=ds,dc=units,dc=it";
		ds = "ldap://ds.units.it:389/dc=ds,dc=units,dc=it";
		
        try {
            Auth ads=new Auth("DS\\"+u, p, 
            		ds);
            ads.Logon();

            ret=true;
            
            NamingEnumeration n=ads.Search("(sAMAccountName="+u+")");
            SearchResult sr = null;
            int count=0;
            
            while(n.hasMoreElements()) {
                count++;
                sr = (SearchResult)n.nextElement();
                //ads.listAttributes(sr,System.out);
                displayName = ads.getAttribute(sr,"displayName");
                mail = ads.getAttribute(sr,"mail");

                //System.out.println(ads.getAttribute(sr,"cn")+" "+ads.getAttribute(sr,"mail"));
            }
            
            ads.close();
            
        }
        catch (Exception e) {
            System.out.println(u + " ha sbagliato password!");
        }
        
        return ret;

    }
}
