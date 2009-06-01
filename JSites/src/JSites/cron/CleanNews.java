package JSites.cron;

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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;

public class CleanNews extends ServiceableCronJob implements Configurable, ConfigurableCronJob {
	
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
								invalidateNews(conn);
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

				System.out.println("...cleaning news...");
			}
			
			this.manager.release(selector);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void invalidateNews(Connection conn) {
		try {
			String today=JSites.utils.DateUtil.getDateString();
			String sql="select n.CID as CID, n.enddate as enddate " +
					"from tblnews n, tblcontenuti c " +
					"where n.enddate < '" + today + "'" +
					" and n.CID=c.CID" +
					" and c.StateId=3";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			Statement st = conn.createStatement();
			while(rs.next()) {
				try {
					String sql1="update tblcontenuti set StateID=4 " +
							"where CID="+rs.getString("CID");
					String endDate=rs.getString("enddate");
					
					if(endDate!=null && endDate.trim().length()>0) 
						st.executeUpdate(sql1);
					
				}
				catch(Exception e) {}
			}
			st.close();
			rs.close();
			stmt.close();
		}
		catch(Exception e) {
			// TODO commentato perchè i cataloghi non hanno la tabella news. 
			//      Da rivedere la selezione delle connessioni
//			e.printStackTrace();
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




	@SuppressWarnings("unchecked")
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
