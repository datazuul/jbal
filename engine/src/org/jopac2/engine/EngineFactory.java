package org.jopac2.engine;

import java.sql.Connection;

import org.jopac2.engine.dbengine.DbEngine;
import org.jopac2.engine.solrengine.SolrEngine;

public abstract class EngineFactory {
	public static Engine getEngine(Connection conn, String catalog, String type) throws Exception {
		Engine engine=null;
		if(type.equals("solr")) engine=new SolrEngine(catalog); // catalog è l'url
		else if(type.equals("db")) engine=new DbEngine(conn,catalog);
		return engine;
	}
	
}
