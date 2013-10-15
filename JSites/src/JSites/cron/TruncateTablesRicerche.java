package JSites.cron;

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

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.cocoon.components.CocoonComponentManager;
import org.apache.cocoon.components.cron.ConfigurableCronJob;
import org.apache.cocoon.components.cron.ServiceableCronJob;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.excalibur.source.Source;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;

public class TruncateTablesRicerche extends ServiceableCronJob implements Configurable, ConfigurableCronJob {
	
	private String datasources=null;
	
	public void configure(final Configuration config)
    throws ConfigurationException {
		datasources=config.getChild("datasources").getValue();
    }


	public void execute(String name) {
		if ( this.getLogger().isInfoEnabled() ) {
            getLogger().info("CronJob " + name + " launched at " + new Date());
        }

		ServiceSelector selector=null;
		
		System.out.println("...truncate tables ricerche...");
		
        try {
			selector = (ServiceSelector) manager.lookup(DataSourceComponent.ROLE+"Selector");
			CocoonComponentManager sr=(CocoonComponentManager) manager.lookup(SourceResolver.ROLE);
			Source s=sr.resolveURI(datasources);
			
			
			if (datasources != null && datasources.length() > 0) {
				InputSource is = new InputSource(s.getInputStream());
				DOMParser p = new DOMParser();
				
				try {
					p.parse(is);
					Node document = p.getDocument();
					Node ds=getNode(document,"datasources");
					
					NodeList children = ds.getChildNodes();
					
					int len = children.getLength();
					for (int i=0; i<len; i++) {
						Node t=children.item(i);
						if(t.getNodeName()=="jdbc") {
							String dbname=lookAttr(t,"name");
							Connection conn = null;
							try{
								conn=((DataSourceComponent)selector.select(dbname)).getConnection();
								truncateTablesRicerche(conn);
							}catch(Exception e){e.printStackTrace();}
							
							try{ if(conn!=null)conn.close(); } catch(Exception e){System.out.println("Non ho potuto chiudere la connessione");}
						}
					}
					System.out.println();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}
			
			this.manager.release(selector);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void truncateTablesRicerche(Connection conn) throws SQLException {
		ResultSet rs=null;
		DatabaseMetaData md=null;
		try {			
			md = conn.getMetaData();
		    rs = md.getTables(null, null, "%", new String[] {"TABLE"});
		    while (rs.next()) {
		    	String tableName=rs.getString(3);
		    	if(tableName.endsWith("_ricerche")||tableName.endsWith("_ricerche_dettaglio")) {
		    		try {
		    			truncateTable(conn,tableName);
		    			System.out.println("\t"+tableName);
		    		}
		    		catch(Exception e) {}
		    	}
		    }
		}
		catch(Exception e) {
			
		}
		finally {
			if(rs!=null) rs.close();
		}
		
	}


	private void truncateTable(Connection conn, String tableName) throws SQLException {
		String sql="truncate table "+tableName;
		Statement st=null;
		try {
			st=conn.createStatement();
			st.execute(sql);
		}
		catch(Exception e) {
			System.out.println("\t\tProblems truncating "+tableName);
		}
		finally {
			st.close();
		}
	}


	private static String lookAttr(Node n,String name) {
		  NamedNodeMap m=n.getAttributes();
		  return lookAttr(m,name);
		  }
		  
	  private static String lookAttr(NamedNodeMap attrs,String name) {
	  	String r=null;
	  	int len = attrs.getLength();
	    for (int i=0; i<len; i++) {
	        Attr attr = (Attr)attrs.item(i);
	        if(attr.getNodeName().equals(name)) r=attr.getNodeValue();
	    }
	    return r;
	  }
	
	private static Node getNode(Node node, String nodename) {
		 Node r=null;
		 if(node.getNodeName().equals(nodename)) {
			 r=node;
		 }
		 else { 
			NodeList children = node.getChildNodes();
			int len = children.getLength();
			for (int i=0; i<len&&r==null; i++)
				r=getNode(children.item(i),nodename);
		 }
		 return r;
	  }




	public void setup(Parameters params, Map objects) {
        if (null != params) {
        	try {
				datasources=params.getParameter("datasources");
			} catch (ParameterException e) {
				e.printStackTrace();
			}

        }
    }

}
