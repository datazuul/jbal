package org.jopac2.viewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.RecordReader;

public class unimarcViewer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String filename="/java_sba/colloca/docs/diprova.uni";
		String tipo="sebina";
		
		if(args.length>0) {
			filename=args[0];
			tipo=args[1];
		}
		
		
		FileInputStream f=new FileInputStream(new File(filename));
		
		RecordInterface ma=RecordFactory.buildRecord(0, "", tipo, 0);
		RecordReader r=ma.getRecordReader(f);
		
		int i=10;
		int max=100;
		
		String line=r.readRecord();
		while(line!=null && i++<max) {
			ma=RecordFactory.buildRecord(0, line, tipo, 0);
			System.out.println(ma.toReadableString());
			line=r.readRecord();
		}
		
		r.close();
		f.close();

	}

}
