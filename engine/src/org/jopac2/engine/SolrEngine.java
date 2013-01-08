package org.jopac2.engine;

import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.Vector;

import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.stemmer.Radice;

public class SolrEngine implements Engine {

	public SearchResultSet executeSearch(String catalogQuery, boolean useStemmer)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void orderBy(Connection conn, String catalogConnection,
			String orderBy, SearchResultSet result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public RecordInterface getNotiziaByJID(String string) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public SearchResultSet listSearchBackward(String string, String string2,
			int i) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public SearchResultSet listSearch(String classe, String parole, int limit)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public SearchResultSet listSearchBackward(String classe, long sjid,
			int limit) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public SearchResultSet listSearch(String classe, long sjid, int limit)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector<Long> getJIDbyBID(Connection conn, String catalogConnection,
			String bid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public long getMaxId() throws Exception {
		// dalla tabella notizie
		return 0;
	}

	public RecordInterface getNotiziaByJID(long jid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateRecord(Radice stemmer, RecordInterface m) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void deleteRecordFromJid(long jid) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public long insertRecord(Radice stemmer, RecordInterface eut) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	public void insertRecord(Radice stemmer, RecordInterface ma, long jid)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void importRecords(InputStream in, String format,
			boolean background, PrintStream console, PrintStream console2)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public String[] getChannels(String string) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
