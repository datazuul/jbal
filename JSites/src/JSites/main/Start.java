package JSites.main;

import java.net.URL;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;



public class Start {
	public void doJob() throws Exception {
//		String jetty_default=new java.io.File("./start.jar").exists()?".":"../..";;
//		String jetty_home = System.getProperty("jetty.home",jetty_default);
        URL jsitesApp=this.getClass().getClassLoader().getResource(".");
		
		
		Server server = new Server();
         
		Connector connector=new SelectChannelConnector();
		connector.setPort(Integer.getInteger("jetty.port",8080).intValue());
		server.setConnectors(new Connector[]{connector});
		
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		webapp.setWar(jsitesApp.toURI().getPath()+"/../..");
//		webapp.setDefaultsDescriptor(jetty_home+"/etc/webdefault.xml");
		
		server.setHandler(webapp);
		
		server.start();
		server.join();

		

	}
	

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Start s=new Start();
		s.doJob();
	}

}
