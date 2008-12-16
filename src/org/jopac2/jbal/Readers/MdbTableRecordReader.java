package org.jopac2.jbal.Readers;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jopac2.utils.MdbDatabase;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;



public class MdbTableRecordReader extends RecordReader {


	Database db=null;
	Table table=null;
	File fin=null;

	public MdbTableRecordReader(InputStream in, String tableName) throws UnsupportedEncodingException {
		super(in);
		try {
			FileInputStream fin=(FileInputStream) in;
			FileChannel fc=fin.getChannel();
			db=new MdbDatabase(fc, false);
			table=db.getTable(tableName);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String readRecord() throws IOException {
		Map<String,Object> row=table.getNextRow();
		StringBuffer record=null;
		if(row!=null) {
			record=new StringBuffer("<record>\n");
			Set<String> keys=row.keySet();
			Iterator<String> i=keys.iterator();
			while(i.hasNext()) {
				String curKey=i.next();
				record.append("<"+curKey+">"+row.get(curKey)+"</"+curKey+">\n");
			}
			record.append("</record>");
		}
		return record==null?null:record.toString().replaceAll("&", "&amp;");
	}

}
