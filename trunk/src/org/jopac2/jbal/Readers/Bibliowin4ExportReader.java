package org.jopac2.jbal.Readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class Bibliowin4ExportReader extends RecordReader {
	private String tempDir=null;
	

	public Bibliowin4ExportReader(InputStream in, String charsetEncoding) throws UnsupportedEncodingException {
		super(in,charsetEncoding);
		init();
	}
	
	public Bibliowin4ExportReader(InputStream in) throws UnsupportedEncodingException {
		super(in);
		init();
	}
	
	private void init() {
		try {
			File idsTemp=File.createTempFile("ids", "");
			idsTemp.mkdir();
			tempDir=idsTemp.getCanonicalPath();
			while(this.ready()) {
				saveRecord(tempDir);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveRecord(String tempDir) throws IOException {
		String line=readLine();
		while(!line.startsWith("IDS ") && !line.startsWith("IDZ ")) {
			line=readLine();
		}
		String idRec=line.substring(4).trim();
		FileOutputStream recFile=new FileOutputStream(tempDir+File.pathSeparator+idRec,true);
		PrintWriter pw=new PrintWriter(recFile);
		do {
			pw.println(line);
			line=readLine();
		}
		while(line!=null && !line.startsWith("------------"));
		pw.close();
		recFile.close();
	}

	@Override
	public byte[] readRecord() throws IOException {
		File tempD=new File(tempDir);
		File[] recs=tempD.listFiles();
		StringBuffer r=new StringBuffer();
		if(recs.length>0) {
			FileReader fr=new FileReader(recs[0]);
			BufferedReader in=new BufferedReader(fr);
			String l=in.readLine();
			while(l!=null) {
				r.append(l);
				l=in.readLine();
			}
			in.close();
			fr.close();
			recs[0].delete();
		}
		else {
			tempD.delete();
		}
		return recs.length>0?r.toString().getBytes():null;
	}

}
