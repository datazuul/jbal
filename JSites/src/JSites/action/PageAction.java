package JSites.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.*;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

public abstract class PageAction implements Action, Composable, Disposable{
	
	private ComponentSelector dbselector;
	protected String dbname;
	protected Request o;
	protected ComponentManager manager;
	
	@SuppressWarnings("unchecked")
	public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
		o = (Request)(objectModel.get("request"));
		dbname = o.getParameter("db");
		
		try { dbname = dbname == null ? parameters.getParameter("db") : dbname; } catch (ParameterException e) {
			e.printStackTrace();
			System.out.println("Request was: " + o.getQueryString());
			System.out.println("No db param in request and no db param sitemap");
		}
		
		return objectModel;
	}
	
	public void compose(ComponentManager cm) throws ComponentException {
		manager = cm;
        dbselector = (ComponentSelector) manager.lookup(DataSourceComponent.ROLE + "Selector");
    }
	
	public void dispose() {
		this.manager.release(dbselector);
	}
	
	public Connection getConnection(String db) throws ComponentException, SQLException {
    	return ((DataSourceComponent)dbselector.select(db)).getConnection();
    }
}