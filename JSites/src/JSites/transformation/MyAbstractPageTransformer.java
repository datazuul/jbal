package JSites.transformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.imageio.ImageIO;

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
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.iso2709.Eutmarc;

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
    
    protected String saveImgFile(RecordInterface ri) {
		
		if(ri.getImage()==null)return "images/pubimages/NS.jpg";
		
		File dir = new File(this.o.getParameter("datadir") + "/images/pubimages");
		if(!dir.exists())dir.mkdirs();
		String imgstr = this.o.getParameter("datadir") + "/images/pubimages/eut" + ri.getJOpacID() + ".jpg";
		try {
			FileOutputStream imgfile = new FileOutputStream(imgstr);
			ImageIO.write(ri.getImage(), "jpeg", imgfile);
			imgfile.flush();
			imgfile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "images/pubimages/eut" + ri.getBid() + ".jpg";
		
	}
    
    protected void throwField(String name, String value) throws SAXException{
		if(value != null && value.length()>0){
			contentHandler.startElement("", name, name, emptyAttrs);
			contentHandler.characters(value.toCharArray(), 0, value.length());
			contentHandler.endElement("", name, name);
		}
	}
}