package org.jopac2.engine;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.Vector;

import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.stemmer.Radice;

public interface Engine {

	SearchResultSet executeSearch(String catalogQuery, boolean useStemmer) throws Exception;

	void orderBy(Connection conn, String catalogConnection, String orderBy,
			SearchResultSet result) throws Exception;

	RecordInterface getNotiziaByJID(String string) throws Exception;

	SearchResultSet listSearchBackward(String classe, String parole, int limit) throws Exception;

	SearchResultSet listSearch(String classe, String parole, int limit) throws Exception;

	SearchResultSet listSearchBackward(String classe, long sjid, int limit) throws Exception;

	SearchResultSet listSearch(String classe, long sjid, int limit) throws Exception;

	Vector<String> getJIDbyBID(Connection conn, String catalogConnection,
			String bid) throws Exception;

	long getMaxId() throws Exception;

//	RecordInterface getNotiziaByJID(String jid) throws Exception;

	void updateRecord(Radice stemmer, RecordInterface m) throws Exception;

	void deleteRecordFromJid(String jid) throws Exception;
	
	void truncateCatalog() throws Exception;

	String insertRecord(Radice stemmer, RecordInterface record) throws Exception;

	void insertRecord(Radice stemmer, RecordInterface ma, String jid) throws Exception;

	void importRecords(InputStream in, Charset charset, String format, boolean background, PrintStream console,
			PrintStream console2) throws Exception;

	/**
	 * Get channels for selected type
	 * @param type
	 * @return
	 * @throws Exception
	 */
	String[] getChannels(String type) throws Exception;

}
