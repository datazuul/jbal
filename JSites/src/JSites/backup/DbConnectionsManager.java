package JSites.backup;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DbConnectionsManager {
	String dbConnectionsFile=null;
	Document dbConnectionsDom=null;
	String autocommitString="true";
	int maxPool=500, minPool=50;

	
//	<jdbc name="dbjopac2">
//		<pool-controller max="500" min="50" />
//		<auto-commit>true</auto-commit>
//		<dburl>jdbc:mysql://localhost/dbjopac?autoReconnect=true</dburl>
//		<user>root</user>
//		<password></password>
//	</jdbc>
	
	
	public DbConnectionsManager(String contextPath) throws ParserConfigurationException, SAXException, IOException {
		dbConnectionsFile=contextPath+"/WEB-INF/dbConnections.xml";
		File file = new File(dbConnectionsFile);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		dbConnectionsDom = db.parse(file);
		dbConnectionsDom.getDocumentElement().normalize();
	}
	
	public void saveDbConnections() throws TransformerFactoryConfigurationError, TransformerException {
		File file = new File(dbConnectionsFile);
        Result result = new StreamResult(file);
        Source source = new DOMSource(dbConnectionsDom);

        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(source, result);

	}
	
	public Element removeConnection(String connectionName) throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "/datasources/jdbc[@name='"+connectionName+"']";

        Element r = (Element) xpath.evaluate(expression, dbConnectionsDom, XPathConstants.NODE);
        r.getParentNode().removeChild(r);

        return r;
	}
	
	public void addConnection(String connectionName, String host, String user, String password) {
		if(host==null) host="localhost";
		String dbUrl="jdbc:mysql://"+host+"/"+connectionName+"?autoReconnect=true";
		Element root=dbConnectionsDom.getDocumentElement();
		Element c=dbConnectionsDom.createElement("jdbc");
		c.setAttribute("name", connectionName);
		root.appendChild(c);
		
		Element dburlNode=dbConnectionsDom.createElement("dburl");
		dburlNode.setTextContent(dbUrl);
		c.appendChild(dburlNode);
		
		Element userNode=dbConnectionsDom.createElement("user");
		userNode.setTextContent(user);
		c.appendChild(userNode);
		
		Element passwordNode=dbConnectionsDom.createElement("password");
		passwordNode.setTextContent(password);
		c.appendChild(passwordNode);
		
		Element autocommitNode=dbConnectionsDom.createElement("auto-commit");
		autocommitNode.setTextContent(autocommitString);
		c.appendChild(autocommitNode);
		
		Element poolcontrollerNode=dbConnectionsDom.createElement("pool-controller");
		poolcontrollerNode.setAttribute("max", Integer.toString(maxPool));
		poolcontrollerNode.setAttribute("min", Integer.toString(minPool));
		c.appendChild(poolcontrollerNode);
	}
	
	public String getDbUrl(String connectionName) throws XPathExpressionException {
		String r=null;
		XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "/datasources/jdbc[@name='"+connectionName+"']";

        r = (String) xpath.evaluate(expression+"/dburl", dbConnectionsDom, XPathConstants.STRING);

        return r;
	}
	
	public String getDbUser(String connectionName) throws XPathExpressionException {
		String r=null;
		XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "/datasources/jdbc[@name='"+connectionName+"']";

        r = (String) xpath.evaluate(expression+"/user", dbConnectionsDom, XPathConstants.STRING);

        return r;
	}
	
	public String getDbPassword(String connectionName) throws XPathExpressionException {
		String r=null;
		XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "/datasources/jdbc[@name='"+connectionName+"']";

        r = (String) xpath.evaluate(expression+"/password", dbConnectionsDom, XPathConstants.STRING);

        return r;
	}
	
	
}
