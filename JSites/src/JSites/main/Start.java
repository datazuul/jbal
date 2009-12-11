package JSites.main;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2009 JOpac2 project
*
*     This file is part of JOpac2. http://www.jopac2.org
*
*  JOpac2 is free software; you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation; either version 2 of the License, or
*  (at your option) any later version.
*
*  JOpac2 is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with JOpac2; if not, write to the Free Software
*  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*
*  Please, see NOTICE.txt AND LEGAL directory for more info. Different licences
*  may apply for components included in JOpac2.
*
*******************************************************************************/

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
