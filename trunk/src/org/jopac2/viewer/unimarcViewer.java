package org.jopac2.viewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.RecordReader;

public class unimarcViewer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String charset="iso-8859-1";
//		String filename="/Java_src/java_jopac2/JOpac2/iso-test/winisis/teca2.iso";
		String filename="/home/romano/Documents/lavoro/lavoro-UniTS/ebooks/springer/Springer_MARC_Biomed_Business_Math_2005-2007.mrc";
		String tipo="isisteca";
		PrintWriter pw=new PrintWriter("/tmp/errors.log");
		
		if(args.length>0) {
			filename=args[0];
			tipo=args[1];
		}
		
		
		FileInputStream f=new FileInputStream(new File(filename));
		
		RecordInterface ma=null;
		try {
			ma=RecordFactory.buildRecord(0, null, tipo, 0);
			RecordReader r=ma.getRecordReader(f,charset);
			
			int i=0;
			int max=100000;
			
			byte[] line=r.readRecord();
			byte[] prevline=null;
			while(line!=null && i++<max) {
				try {
					System.out.println("--> "+i);

					ma=RecordFactory.buildRecord(0, line, tipo, 0);
					System.out.println(ma.toReadableString());
				}
				catch(Exception e2) {
					pw.println(new String(prevline));
					pw.println(new String(line));
				}
				prevline=line;
				line=r.readRecord();
			}
			r.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		f.close();
		pw.close();

	}

}
