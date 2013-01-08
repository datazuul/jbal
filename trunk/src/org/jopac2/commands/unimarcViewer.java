package org.jopac2.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.zip.GZIPInputStream;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.RecordReader;

public class unimarcViewer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String charset="utf-8";
//		String filename="/Java_src/java_jopac2/JOpac2/iso-test/winisis/teca2.iso";
//		String filename="/home/romano/Documents/lavoro/lavoro-UniTS/ebooks/springer/Springer_MARC_Biomed_Business_Math_2005-2007.mrc";
		String filename="/Java_src/java_sba/colloca/docs/M1_DW_D4_DA_SG.uni";
		String tipo="isisteca";
		PrintWriter pw=new PrintWriter("/tmp/errors.log");
		
		if(args.length>0) {
			filename=args[0];
			tipo=args[1];
		}
		
		
		FileInputStream f=new FileInputStream(new File(filename));
		InputStream is=f;
		
		if(filename.endsWith(".gz")) {
			is = new GZIPInputStream(new FileInputStream(filename));
		}

		RecordInterface ma=null;
		try {
			ma=RecordFactory.buildRecord(0, null, tipo, 0);
			RecordReader r=ma.getRecordReader(is,charset);
			
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
		
		
		is.close();
		
		pw.close();

	}

}
