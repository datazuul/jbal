package org.jopac2.engine.dbengine.listSearch;

import java.sql.Connection;
import java.sql.SQLException;
import org.jopac2.engine.dbengine.dbGateway.DbGateway;
import org.jopac2.engine.utils.SearchResultSet;

public class ListSearch {

	public static SearchResultSet listSearch(Connection conn, String catalog, String classe,String parole,int limit) throws SQLException {
		DbGateway db=DbGateway.getInstance(conn.toString(),null);
		return db.listSearch(conn,catalog.trim(),classe,parole,limit);
	}
	
	public static SearchResultSet listSearchBackward(Connection conn, String catalog, String classe,String parole,int limit) throws SQLException {
		DbGateway db=DbGateway.getInstance(conn.toString(),null);
		return db.listSearchBackward(conn,catalog.trim(),classe,parole,limit);
	}
	
	public static SearchResultSet listSearch(Connection conn, String catalog, String classe,long jid,int limit) throws SQLException {
		String testo=DbGateway.getClassContentFromJID(conn, catalog.trim(), classe, Long.toString(jid));
		return listSearch(conn, catalog.trim(), classe, testo, limit);
	}
	
	public static SearchResultSet listSearchBackward(Connection conn, String catalog, String classe,long jid,int limit) throws SQLException {
		String testo=DbGateway.getClassContentFromJID(conn, catalog.trim(), classe, Long.toString(jid));
		return listSearchBackward(conn, catalog.trim(), classe, testo,limit);
	}
}