package org.jopac2.engine.listSearch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

import org.jopac2.engine.dbGateway.StaticDataComponent;

public class ListSearch {
	
	public static Vector<String> listSearch(Connection conn,String classe,String fromParola,int limit) throws SQLException {	
		long idClasse=StaticDataComponent.getChannelIndexbyName(classe);
		Hashtable<String, Integer> h = new Hashtable<String, Integer>();
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
				int id_sequenza_tag = rs0.getInt("id_sequenza_tag");
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
					h.put(cur_name, new Integer(h.get(cur_name).intValue() + 1));
				} else {
					h.put(cur_name, new Integer(1));
				}
				rs1.close();
				stmt1.close();
			}
			rs0.close();
			stmt0.close();
		}
		rs.close();
		stmt.close();
		conn.close();
		Vector<String> v = new Vector<String>(h.keySet());
		Collections.sort(v);
		return v;
	}
}