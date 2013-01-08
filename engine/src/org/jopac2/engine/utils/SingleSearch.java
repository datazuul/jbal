package org.jopac2.engine.utils;

import org.jopac2.engine.metasearch.managers.AbstractManager;


/**
* 
* @author	Iztok Cergol
* @version	15/08/2004
* 
*/
/*
 *La classe SingleSearch e' costituita dai campi:
 *host - identifica l'host da interrogare
 *port - porta sulla quale l'host accetta richieste
 *prefix - parte iniziale della richiesta del motodo GET
 *query - stringa che verra' costruita successivamente in relazione ai termini cercati
 *constructor_class - classe che identifica il costruttore della query appena presentata
 */
public class SingleSearch
{
	public static final char ISO2709_RS = 035;
	  public static final char ISO2709_FS = 036;
	  public static final char ISO2709_IDFS = 037;
	  private static final String PREFIX_QUERY_TYPE = "PREFIX";
	  private static final String CCL_QUERY_TYPE = "CCL";

	  private String principal = null;
	  private String group = null;
	  private String credentials = null;
	  private String querytype = PREFIX_QUERY_TYPE;
	  private String current_result_set_name = null;
	  private int result_set_count=0;
	  private String element_set_name = null; // Default to a null (no) element set name
	
	
    private String host;
    private int port;
    private String prefix;
    @SuppressWarnings("unchecked")
	private Class constructor_class;
    private String syntax_name;
    private String UID, passw;
    private int auth_type = 0; // 0=none, 1=anonymous, 2=open, 3=idpass
    private long timeout;
    private String contextDir;
    private String databasecode;
    private String dbname=null;
    private String campi=null;
    private AbstractManager manager=null;
    
    /**
     * 
     * @param h = host
     * @param p = port
     * @param pr = prefix
     * @param classe 
     * @param sn = syntax name
     * @param user
     * @param password
     * @param authentication_type
     * @param tout
     * @param contextDir
     * @param databasecode
     * @param campi
     * @throws ClassNotFoundException
     */
    public SingleSearch(String h, int p, String pr, String classe, String sn, String user, String password, 
    		int authentication_type, long tout, String contextDir, String databasecode, String campi) throws ClassNotFoundException
    {
        host = h;
        port = p;
        prefix = pr;
        if(classe.indexOf(":")>0) {
        	constructor_class = Class.forName(classe.substring(0,classe.indexOf(":")));
        	dbname=classe.substring(classe.indexOf(":")+1);
        }
        else {
        	constructor_class = Class.forName(classe);
        }
        syntax_name = sn;
        UID = user;
        passw = password;
        auth_type = authentication_type; 
        timeout = tout;
        this.contextDir=contextDir;
        this.databasecode=databasecode;
        this.campi=campi;
    }
    
    /*
     *La funzione getRequest torna la concatenazione del prefix e del query
     *cioe' la vera e propria richiesta da applicare al server HTTP col metodo GET
     */    
    public String getPrefix()
    {
        return prefix;
    }
    
    @SuppressWarnings("unchecked")
	public Class getConstructorClass()
    {
        return constructor_class;
    }
    
    public String getHost()
    {
        return host;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public String getSyntax()
    {
    	return syntax_name;
    }
    
/*    public void setQuery(IRQuery q)
    {
        query = q;
    }
*/    
    public String getUserId()
    {
    	return UID;
    }
    
    public String getPassword()
    {
    	return passw;
    }
    
    public int getAuthType()
    {
    	return auth_type;
    }
    
    public long getTimeOut(){
    	return timeout;
    }

	public void setPort(int p) {
		port = p;
	}

	/**
	 * @return Returns the contextDir.
	 */
	public String getContextDir() {
		return contextDir;
	}

	/**
	 * @param contextDir The contextDir to set.
	 */
	public void setContextDir(String contextDir) {
		this.contextDir = contextDir;
	}

	/**
	 * @return Returns the databasecode.
	 */
	public String getDatabasecode() {
		return databasecode;
	}

	/**
	 * @param databasecode The databasecode to set.
	 */
	public void setDatabasecode(String databasecode) {
		this.databasecode = databasecode;
	}

	/**
	 * @return Returns the dbname.
	 */
	public String getDbname() {
		return dbname;
	}

	/**
	 * @param dbname The dbname to set.
	 */
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	/**
	 * @return Returns the campi.
	 */
	public String getCampi() {
		return campi;
	}

	/**
	 * @param campi The campi to set.
	 */
	public void setCampi(String campi) {
		this.campi = campi;
	}

	/**
	 * manager e' la classe (oggetto) che sta eseguendo la ricerca
	 * @param manager
	 */
	public void setManager(AbstractManager manager) {
		this.manager=manager;
	}

	/**
	 * Restituisce l'AbstractManager che sta eseguendo (o ha eseguito) la ricerca o null 
	 * se la ricerca non e' stata lanciata
	 * @return
	 */
	public AbstractManager getManager() {
		return manager;
	}
	
	public String getDataSyntax() {
		if (syntax_name.contains(":")) {
			return syntax_name.substring(0, syntax_name.indexOf(":"));
		} else {
			return syntax_name;
		}
	}
	
	public String getDataType() {
		if (syntax_name.contains(":")) {
			return syntax_name.substring(syntax_name.indexOf(":") + 1);
			
		} else {
			return syntax_name;
		}
	}
	
	public void destroy() {
		manager.destroy();
	}
}
