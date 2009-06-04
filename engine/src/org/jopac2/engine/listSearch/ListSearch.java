package org.jopac2.engine.listSearch;

import java.sql.Connection;
import java.sql.SQLException;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.utils.SearchResultSet;

public class ListSearch {
	
	
	public static SearchResultSet listSearch(Connection conn,String classe,String parole,int limit) throws SQLException {
		DbGateway db=DbGateway.getInstance(conn.toString(),null);
		return db.listSearch(conn,classe,parole,limit);
	}
}