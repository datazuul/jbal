package org.jopac2.engine.NewSearch;

/*******************************************************************************
 *
 *  JOpac2 (C) 2002-2007 JOpac2 project
 *
 *     This file is part of JOpac2. http://jopac2.sourceforge.net
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
 *******************************************************************************/

/**
 *
 * @author Romano 
 * @author Albert
 * Used in doSeach to save the cardinality of each item used in the search query.
 * modificata per salvare anche l'id_lcp
 * 18.10.05 ac:
 *   - inserito metodo toString 
 *   - inserito metodo setClasseParola
 *   - inserito metodo ricercaId_lcp 
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.StringTokenizer;

import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.dbGateway.StaticDataComponent;
import org.jopac2.jbal.stemmer.Radice;
import org.jopac2.jbal.stemmer.StemmerItv2;


public class NewItemCardinality {

	private long classe = 0;
	private String parola = "";
	private long cardinality = 0;
	private long id_lcp = 0;
	private String nomeClasse;
	private long cardinalityQueryTime = 0;
	private long queryTime = 0;
	private long operationTime = 0;
	public BitSet bit = null;
	private Radice stemmer=null;

	public long getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(long operationTime) {
		this.operationTime = operationTime;
	}

	private void init(long classe, String nomeClasse, String parola,
			long cardinality, long id_lcp) {
		this.classe = classe;
		this.parola = parola;
		this.cardinality = cardinality;
		this.id_lcp = id_lcp;
		this.nomeClasse = nomeClasse;
		this.stemmer = new StemmerItv2();
	}

	public NewItemCardinality(long classe, String parola, long cardinality,
			long id_lcp) {
		init(classe, null, parola, cardinality, id_lcp);
	}

	public NewItemCardinality(long classe, String nomeClasse, String parola,
			long cardinality, long id_lcp) {
		init(classe, nomeClasse, parola, cardinality, id_lcp);
	}

	public NewItemCardinality() {
		this.stemmer = new StemmerItv2();
	}

	public String toString() {
		// return "(classe:" + this.nClasse + "[" + this.classe + "],id_lcp:"
		// + this.id_lcp + ",parola:" + this.parola + ",card:"
		// + this.cardinality + ",qTime:"+this.queryTime+")";
		return this.parola + "(" + this.cardinality + ":"
				+ this.cardinalityQueryTime + "ms,qry:" + this.queryTime
				+ "ms,op:" + this.operationTime + "ms)";
	}
	
	public void setClasseParola(long classe, String nomeClasse, String parola) {
		this.classe=classe;
		this.nomeClasse=nomeClasse;
		this.parola=parola;
	}
	/**
	 * valorizza classe,nClasse,parola in base alla stringa passata nella forma
	 * id_classe=parola
	 * 
	 * @param s
	 */
	public void setClasseParola(String[] channels,String s, Connection conn) {
		if(s!=null && s.length()>0){
			StringTokenizer st = new StringTokenizer(s, "=");			
			String classe = st.nextToken().trim();
			DbGateway dbGateway=DbGateway.getInstance(conn.toString(), System.out);

			// se e' presente un solo token, allora impostare la classe come ANY di default
			if(!st.hasMoreTokens()){
				this.parola = classe;
				classe="ANY";
			} else {
				this.parola=st.nextToken().trim();
			}
				
			if (classe.charAt(0) > '9') { // se il primo carattere di classe e' una lettera
				// decodifica nome classe
				this.nomeClasse = classe;
				try {
					this.classe=dbGateway.getClassIDClasseDettaglio(conn, classe);
//					this.classe=StaticDataComponent.getChannelIndexbyName(channels,classe);
				}
				catch(Exception e){
					// se la classe non esiste viene usata ANY
					this.nomeClasse="ANY";
					try {
						this.classe=dbGateway.getClassIDClasseDettaglio(conn, classe);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
//					this.classe=StaticDataComponent.getChannelIndexbyName(channels,nomeClasse);
				}
			} else { 
				// classe e' un numero
				this.classe = Integer.parseInt(classe);
				this.nomeClasse = classe; // per semplicita, dovrei ricodificare indietro
			}
			this.id_lcp = 0;
			this.cardinality = 0;
		} else {
			// stringa di ricerca nulla o vuota
			this.id_lcp=0;
			this.cardinality=0;
			this.parola="";
			this.classe=0;
			this.nomeClasse="ERRORE";
		}
	}



	/**
	 * valorizza id_lcp e cardinality ricercando sul db la coppia
	 * id_classe,parola
	 * 
	 * @param conn
	 */
/*	private void ricercaId_lcp(Connection conn) {
		this.id_lcp = -1;
		this.cardinality = -1;
		this.cardinalityQueryTime = -1;
		long now = System.currentTimeMillis();
		try {
			String sql = "SELECT anagrafe_parole.id, "
					+ "       l_classi_parole.n_notizie, "
					+ "       l_classi_parole.id as id_lcp "
					+ "  FROM l_classi_parole, "
					+ "       anagrafe_parole "
					+ " WHERE anagrafe_parole.parola = ? "
					+ "       and l_classi_parole.id_classe = ? "
					+ "       and anagrafe_parole.id = l_classi_parole.id_parola ";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, this.parola);
			stmt.setLong(2, this.classe);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				this.cardinality = rs.getLong("n_notizie");
				this.id_lcp = rs.getLong("id_lcp");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.cardinalityQueryTime = System.currentTimeMillis() - now;
	}*/

	/**
	 * effettua la ricerca in tabella
	 * 
	 * @param id_lcp
	 *            chiave
	 * @return bitarray di id_notizie
	 */
/*	private BitSet doRicercaBitArray(Connection conn, long id_lcp) {
		BitSet b = new BitSet();
		long now = System.currentTimeMillis();
		this.queryTime = -1;
		try {			
			String sql = " SELECT id_notizia "
					+ "      FROM l_classi_parole_notizie "
					+ "     WHERE id_l_classi_parole = ? ";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, id_lcp);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				b.set((int) rs.getLong(1));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.queryTime = System.currentTimeMillis() - now;
		return b;
	}*/

	/**
	 * effettua la ricerca in any utilizzando data_element e l_parole_de
	 * 
	 * @param parola
	 * @return bitarray di id_notizie
	 */
	/*private BitSet doRicercaBitArrayAnyOLD(Connection conn) {
		BitSet b = new BitSet();
		long now = System.currentTimeMillis();
		this.queryTime = -1;
		try {
			String sql = "SELECT DISTINCT data_element.id_notizia "
					+ "  FROM anagrafe_parole,l_parole_de,data_element "
					+ " WHERE anagrafe_parole.parola= ? "
					+ "       and l_parole_de.id_parola=anagrafe_parole.id "
					+ "       and data_element.id=l_parole_de.id_de";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, this.parola);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				b.set((int) rs.getLong(1));
			}
			this.cardinality = b.cardinality();
			this.id_lcp = -1;
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.queryTime = System.currentTimeMillis() - now;
		return b;
	}
	*/
	
	/**
	 * effettua la ricerca in any utilizzando l_classi_parole
	 * 
	 * @param parola
	 * @return bitarray di id_notizie
	 * @throws SQLException 
	 */
	public BitSet doRicercaBitArrayAny(Connection conn, boolean useStemmer) throws SQLException {
		BitSet b = new BitSet();
		long now = System.currentTimeMillis();
		this.queryTime = -1;
		//try {
			String sql;
			if(useStemmer) 
				sql = "SELECT DISTINCT lcpn.id_notizia "
						+ "     FROM l_classi_parole_notizie lcpn," 
						+ "		     l_classi_parole lcp, " 
						+"           anagrafe_parole a "
						+ "    WHERE a.stemma like ? " 
						+ "          and lcp.id_parola=a.id "
						+ "          and lcpn.id_l_classi_parole=lcp.id";
			else
				sql = "SELECT DISTINCT lcpn.id_notizia "
					+ "     FROM l_classi_parole_notizie lcpn," 
					+ "		     l_classi_parole lcp, " 
					+"           anagrafe_parole a "
					+ "    WHERE a.parola like ? " 
					+ "          and lcp.id_parola=a.id "
					+ "          and lcpn.id_l_classi_parole=lcp.id";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			if(useStemmer) stmt.setString(1, stemmer.radice(this.parola.toLowerCase()));
			else stmt.setString(1, this.parola.toLowerCase());
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				b.set((int) rs.getLong(1));
			}
			rs.close();
			stmt.close();

			this.cardinality = b.cardinality();
			this.id_lcp = -1;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		this.queryTime = System.currentTimeMillis() - now;
		return b;
	}
	
	/**
	 * effettua la ricerca con caratteri jolly
	 * 
	 * @param parola
	 * @return bitarray di id_notizie
	 * @throws SQLException 
	 */
	public BitSet doRicercaBitArrayJolly(Connection conn, boolean useStemmer) throws SQLException {
		BitSet b = new BitSet();
		long now = System.currentTimeMillis();
		this.queryTime = -1;
		//try {
			String sql;
			if(useStemmer) 
				sql= "SELECT DISTINCT lcpn.id_notizia "
					+ "     FROM l_classi_parole lcp," // QUESTA TABELLA DEVE ESSERE LA PRIMA
				    + "          l_classi_parole_notizie lcpn," 
					+ "          anagrafe_parole a "
					+ "    WHERE a.stemma like ? "
					+ "          and lcp.id_classe = ?"
					+ "          and lcp.id_parola=a.id "
					+ "          and lcpn.id_l_classi_parole=lcp.id";
			else
				sql= "SELECT DISTINCT lcpn.id_notizia "
					+ "     FROM l_classi_parole lcp," // QUESTA TABELLA DEVE ESSERE LA PRIMA
				    + "          l_classi_parole_notizie lcpn," 
					+ "          anagrafe_parole a "
					+ "    WHERE a.parola like ? "
					+ "          and lcp.id_classe = ?"
					+ "          and lcp.id_parola=a.id "
					+ "          and lcpn.id_l_classi_parole=lcp.id";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			if(useStemmer) 
				stmt.setString(1, stemmer.radice(this.parola.toLowerCase()));
			else 
				stmt.setString(1, this.parola.toLowerCase());
			
			stmt.setLong(2, this.classe);
						
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				b.set((int) rs.getLong(1));
			}
			this.cardinality = b.cardinality();
			this.id_lcp = -1;
			rs.close();
			stmt.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		this.queryTime = System.currentTimeMillis() - now;
		return b;
	}
	
	public long getClasse() {
		return classe;
	}

	public String getClasseAsString() {
		return nomeClasse;
	}

	public String getParola() {
		return parola;
	}

	public long getCardinality() {
		return cardinality;
	}

	public void setCardinality(long cardinality) {
		this.cardinality = cardinality;
	}

	public long getId_lcp() {
		return id_lcp;
	}

	public void setId_lcp(long x) {
		this.id_lcp=x;
	}
}