package JSites.transformation;

/**
 * 
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentSelector;
import org.apache.avalon.framework.component.Composable;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.cocoon.webapps.session.SessionManager;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.apache.cocoon.xml.AttributesImpl;

public abstract class MyAbstractPageTransformer extends AbstractTransformer implements Composable, Disposable {
	
	public ComponentSelector dbselector;
	public ComponentManager manager;
	public SessionManager sessionManager;
	protected Request o;
	protected String dbname = null;
	protected AttributesImpl emptyAttrs = new AttributesImpl();
	
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		o = (Request)arg1.get("request");
		dbname = o.getParameter("db");
		
		try { dbname = dbname == null ? arg3.getParameter("db") : dbname; } catch (ParameterException e) {
			System.out.println(e.getMessage());
			System.out.println("Request was: " + o.getQueryString());
		}
	}
	
	public void compose(ComponentManager manager) throws ComponentException {
		this.manager = manager;
        dbselector = (ComponentSelector) manager.lookup(DataSourceComponent.ROLE + "Selector");
        sessionManager = (SessionManager) manager.lookup(SessionManager.ROLE);
    }
	
	public void dispose() {
		this.manager.release(dbselector);
	}
    
    public Connection getConnection(String db) throws ComponentException, SQLException {
    	return ((DataSourceComponent)dbselector.select(db)).getConnection();
    }
}