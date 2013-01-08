package org.jopac2.engine;

import java.sql.Connection;

public abstract class EngineFactory {
	public static Engine getEngine(Connection conn, String catalog, String type) throws Exception {
		Engine engine=null;
		if(type.equals("solr")) engine=new SolrEngine();
		else if(type.equals("db")) engine=new DbEngine(conn,catalog);
		return engine;
	}
}
