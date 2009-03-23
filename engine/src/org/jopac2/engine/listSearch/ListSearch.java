package org.jopac2.engine.listSearch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.jopac2.engine.dbGateway.StaticDataComponent;
import org.jopac2.engine.utils.SearchResultSet;

public class ListSearch {
	
	/**
	 * @deprecated
	 * @param conn
	 * @param classe
	 * @param fromParola
	 * @param limit
	 * @return
	 * @throws SQLException
	 */
	public static SearchResultSet listSearch_old(Connection conn,String classe,String fromParola,int limit) throws SQLException {	
		long idClasse=StaticDataComponent.getChannelIndexbyName(classe);
		//Hashtable<String, Vector<Integer>> h = new Hashtable<String, Vector<Integer>>();
		Vector<Long> listResult=new Vector<Long>();
		//estrae le parole>=al parametro passato che sono nelle classe passata delle notizie in posizione 0
		String sql = "SELECT distinct id_parola,posizione_parola,p.parola "
				   + "  FROM notizie_posizione_parole a, anagrafe_parole p "
				   + " WHERE a.id_parola=p.id " 
				   + "       and a.id_classe=? "  
			       + "       and a.posizione_parola=0 " 
			       + "       and p.parola>=? " 
			       + "ORDER BY p.parola " 
			       + "LIMIT ?";//numero di parole da restituire>=

		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, idClasse);
		stmt.setString(2, fromParola);
		stmt.setLong(3, limit);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			//per ciascuna parola estratta determina l'id notizia collegato e id_sequenza_tag (linea del titolo)
			String sql0 = "SELECT b.id_notizia, b.id_sequenza_tag "
					+ "      FROM notizie_posizione_parole b "
					+ "     WHERE b.id_parola=?"  
					+ "           and b.id_classe=?"  
					+ "           and b.posizione_parola=0";
			PreparedStatement stmt0 = conn.prepareStatement(sql0);
			stmt0.setInt(1, rs.getInt("id_parola"));
			stmt0.setLong(2, idClasse);
			ResultSet rs0 = stmt0.executeQuery();
			while (rs0.next()) {
				int id_notizia = rs0.getInt("id_notizia");
				
				listResult.addElement(new Long(id_notizia));
				
/*				int id_sequenza_tag = rs0.getInt("id_sequenza_tag");
				
				//per ciascuna notizia e id_sequenza_tag determinata, riscostruisce la sequenza del titolo in cur_name 
				String sql1 = "SELECT b.id_sequenza_tag,b.id_parola,b.posizione_parola,p.parola,id_notizia "
						    + "  FROM notizie_posizione_parole b, anagrafe_parole p "
						    + " WHERE b.id_parola=p.id " 
						    + "       and b.id_notizia=?"
						    + "       and b.id_classe=?"
						    + "       and b.id_sequenza_tag=? "
                            + "ORDER BY id_notizia, posizione_parola";
				PreparedStatement stmt1 = conn.prepareStatement(sql1);
				stmt1.setInt(1,id_notizia);
				stmt1.setLong(2,idClasse);
				stmt1.setInt(3,id_sequenza_tag);
				ResultSet rs1 = stmt1.executeQuery();
				// int cur_id_notizia=0;
				String cur_name = "";
				while (rs1.next()) {
					cur_name += rs1.getString("parola") + " ";
					String w = rs1.getString("id_notizia");
					// dump per debug
					// RecordInterface
					// recordInterface=DbGateway.getNotiziaByJID(conn, w);
					// System.out.println(recordInterface);
				}
				cur_name = cur_name.trim(); 
				//conteggio occorrenze di cur_name
				if (h.containsKey(cur_name)) {
					Vector<Integer> vt=h.get(cur_name);
					vt.addElement(new Integer(id_notizia));
					h.put(cur_name, vt);
				} else {
					Vector<Integer> vt=new Vector<Integer>();
					vt.addElement(new Integer(id_notizia));
					h.put(cur_name, vt);
				}
				rs1.close();
				stmt1.close(); */
			}
			rs0.close();
			stmt0.close();
		}
		rs.close();
		stmt.close();
		conn.close();
		//Vector<String> v = new Vector<String>(h.keySet());
		//Collections.sort(v);
		//return v;
		
		SearchResultSet result=new SearchResultSet();
		result.setRecordIDs(listResult);
		return result;
	}
	
	public static SearchResultSet listSearch(Connection conn,String classe,String parole,int limit) throws SQLException {	
		long idClasse=StaticDataComponent.getChannelIndexbyName(classe);
		Vector<Long> listResult=new Vector<Long>();
		String sql = "SELECT distinct id_notizia,testo "
				   + "  FROM TIT_NDX "
				   + " WHERE testo>=? " 
			       + "ORDER BY testo " 
			       + "LIMIT ?";//numero di parole da restituire>=

		PreparedStatement stmt = conn.prepareStatement(sql);
		//stmt.setLong(1, idClasse);
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
		conn.close();
		SearchResultSet result=new SearchResultSet();
		result.setRecordIDs(listResult);
		return result;
	}
}