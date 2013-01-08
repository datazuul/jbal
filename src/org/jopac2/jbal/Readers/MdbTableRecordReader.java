package org.jopac2.jbal.Readers;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jopac2.utils.MdbDatabase;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.Column;



public class MdbTableRecordReader extends RecordReader {


	private Database db=null;
	private Table table=null;
	private File fin=null;
	boolean firstRecord=true;

	public MdbTableRecordReader(InputStream in, String tableName) throws UnsupportedEncodingException {
		super(in);
		try {
			FileInputStream fin=(FileInputStream) in;
			FileChannel fc=fin.getChannel();
			db=new MdbDatabase(fc, false);
			table=db.getTable(tableName);
			List<Column> c=table.getColumns();
			chToIndex=new String[table.getColumnCount()];
			for(int i=0;c!=null && i<c.size(); i++) {
				chToIndex[i]=c.get(i).getName().replaceAll(" ", "_").replaceAll("/", "_");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public byte[] readRecord() throws IOException {
		Map<String,Object> row=table.getNextRow();
		
		StringBuffer record=null;
		if(row!=null) {
			record=new StringBuffer("<record>\n");
			Set<String> keys=row.keySet();
			Iterator<String> i=keys.iterator();
			while(i.hasNext()) {
				String curKey=i.next().replaceAll(" ", "_");
				curKey=curKey.replaceAll("/", "_");
				Object curV=row.get(curKey);
				if(curV!=null) {
					String curValue=curV.toString();
					curValue=curValue.replaceAll("<", "&lt;");
					curValue=curValue.replaceAll(">", "&gt;");
					record.append("<"+curKey+">"+curValue+"</"+curKey+">\n");
				}
			}
			record.append("</record>");
		}
		return record==null?null:record.toString().replaceAll("&", "&amp;").getBytes();
	}

}
