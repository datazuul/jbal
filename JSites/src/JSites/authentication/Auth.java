package JSites.authentication;


import javax.naming.ldap.*;
import javax.naming.*;
import javax.naming.directory.*;

import java.io.PrintStream;
import java.util.*;

/*
 * Auth.java
 *
 * Created on 13 aprile 2004, 20.40
 */

/**
 *
 * @author  romano
 */
public class Auth {
    private Hashtable env;
    private LdapContext ctx;
    private SearchControls ctls;
    
    public NamingEnumeration Search(String filter) throws Exception {
        NamingEnumeration answer=null;
        answer=ctx.search("",filter,ctls);
        return answer;
    }
    
    public Enumeration SearchLDAP(String userID) throws Exception {
        NamingEnumeration n=null;
        n=ctx.search("","(uid="+userID+")", ctls);
        return n;
    }
    
    public NamingEnumeration SearchADS(String userID) throws Exception {
        NamingEnumeration n=null;
        n=ctx.search("","(sAMAccountName="+userID+")", ctls);
        return n;
    }
    
    public void close() throws Exception {
        ctx.close();
    }
    
    public boolean Logon() throws Exception {
        try {
            ctx=new javax.naming.ldap.InitialLdapContext(env,null);
        }
        catch (Exception e) {
            throw e;
        }
        return true;
    }
    
    public String getAttribute(SearchResult sr, String attribute) throws NamingException {
        Attributes attrs=sr.getAttributes();
        String ret="";

        for (NamingEnumeration en = attrs.getAll(); en.hasMore();) {
            Attribute attrib = (Attribute)en.next();
            if(attrib.getID().equals(attribute)) {
                NamingEnumeration e = attrib.getAll();
                while(e.hasMore()) {
                    ret += e.next()+"\n";
                }
            }
        }
        ret=ret.trim();
        return ret;
    }
    
    public void listAttributes(SearchResult sr, PrintStream out) throws NamingException {
        Attributes attrs=sr.getAttributes();

        for (NamingEnumeration en = attrs.getAll(); en.hasMore();) {
            Attribute attrib = (Attribute)en.next();
            out.println(attrib.getID()+": ");
            
            NamingEnumeration e = attrib.getAll();
            while(e.hasMore()) {
                out.println("\t\t"+e.next());
            }
        
        }
    }
    
    public  void formatResults(NamingEnumeration en) throws Exception{
	int count=0;
	try {
	    while (en.hasMore()) {
		SearchResult sr = (SearchResult)en.next();
		System.out.println("SEARCH RESULT:" + sr.getName());
		formatAttributes(sr.getAttributes());
		System.out.println("====================================================");
		count++;
		   }

	   System.out.println("Search returned "+ count+ " results");

	} catch (NamingException e) {
	    e.printStackTrace();
	}
    }
    
/*
* Generic method to format the Attributes .Displays all the multiple values of
* each Attribute in the Attributes
*/
 public  void formatAttributes(Attributes attrs) throws Exception{
	if (attrs == null) {
	    System.out.println("This result has no attributes");
	} else {
	    try {
		for (NamingEnumeration en = attrs.getAll(); en.hasMore();) {
		    Attribute attrib = (Attribute)en.next();
		    System.out.println("ATTRIBUTE :" + attrib.getID());
		    for (NamingEnumeration e = attrib.getAll();e.hasMore();)
			 System.out.println("\t\t        = " + e.next());
		}

	    } catch (NamingException e) {
		e.printStackTrace();
	    }

	}
}

    
    /** Creates a new instance of Auth */
    public Auth(String username, String password, String provider_url) {
        env=new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory"); 
        env.put(Context.REFERRAL, "follow" );

        env.put(Context.PROVIDER_URL, provider_url); 
        env.put(Context.SECURITY_AUTHENTICATION,"simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        
        ctls = new SearchControls();
        ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        ctls.setDerefLinkFlag(true);
    }
    
}
