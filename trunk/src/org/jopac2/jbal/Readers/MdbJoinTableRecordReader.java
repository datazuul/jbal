package org.jopac2.jbal.Readers;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class MdbJoinTableRecordReader extends MdbTableRecordReader {

	public MdbJoinTableRecordReader(InputStream in, String tableName, String lookupTable ) throws UnsupportedEncodingException {
		super(in, tableName);
		// TODO Auto-generated constructor stub
	}

}
