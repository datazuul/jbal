package org.jopac2.engine.listSearch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.utils.SearchResultSet;

public class ListSearch {
	
	
	public static SearchResultSet listSearch(Connection conn,String classe,String parole,int limit) throws SQLException {	
		//long idClasse=StaticDataComponent.getChannelIndexbyName(classe);
		Vector<Long> listResult=new Vector<Long>();
//		String sql = "SELECT distinct id_notizia,testo "
//			       + "  FROM "+DbGateway.nomeTableListe(classe)+" "
//				   + " WHERE testo>=? " 
//			       + "ORDER BY testo " 
//			       + "LIMIT ?";//numero di parole da restituire>=
		
		
		String sql="select * from "+DbGateway.nomeTableListe(classe)+" b, "+
			"(SELECT distinct testo FROM "+DbGateway.nomeTableListe(classe)+" a "+
			"where testo >= ? order by testo limit ?) c "+
			"where b.testo=c.testo order by b.testo";

		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, parole);
		stmt.setLong(2, limit);
		ResultSet rs = stmt.executeQuery();
		int id_notizia;
		while (rs.next()) {
			id_notizia = rs.getInt("id_notizia");
			listResult.addElement(new Long(id_notizia));
		}
		rs.close();
		stmt.close();
		SearchResultSet result=new SearchResultSet();
		result.setRecordIDs(listResult);
		return result;
	}
}