package org.jopac2.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.abstractStructure.Delimiters;
import org.jopac2.utils.ZipUnzip;

public class unimarcViewer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String charset="cp858";
//		String charset="utf-8";
//		String charset="iso-8859-1";
//		String filename="/Java_src/java_jopac2/JOpac2/iso-test/winisis/teca2.iso";
//		String filename="/home/romano/Documents/lavoro/lavoro-UniTS/ebooks/springer/Springer_MARC_Biomed_Business_Math_2005-2007.mrc";
//		String filename="/Java_src/java_sba/colloca/docs/M1_DW_D4_DA_SG.uni";
		String filename="/home/romano/Documents/lavoro/lavoro-suore/MST_ok.ISO";
		String tipo="isisbiblo";
		PrintWriter pw=new PrintWriter("/tmp/errors.log");
		
		if(args.length>0) {
			filename=args[0];
			tipo=args[1];
		}
				
		InputStream is=null;
				
		if(filename.endsWith(".gz")) {
			is = new GZIPInputStream(new FileInputStream(filename));
		}
		else {
			is=new FileInputStream(new File(filename));
		}

		RecordInterface ma=null;
		try {
			ma=RecordFactory.buildRecord("0", null, Charset.forName(charset), tipo, 0);
//			RecordInterface utf8=RecordFactory.buildRecord(0, null, Charset.forName("utf-8"), tipo, 0);
//			Delimiters currentDelimiters=new Delimiters();
////			currentDelimiters.setRt((byte)'#');
////			currentDelimiters.setFt((byte)'^');
//			currentDelimiters.setDl((byte)'^');
//			((org.jopac2.jbal.iso2709.Isisbiblo)ma).delimiters=currentDelimiters;
			
			RecordReader r=ma.getRecordReader(is,charset);
			
			
			int i=0;
			int max=100000;
			
			byte[] line=r.readRecord();
			byte[] prevline=null;
			while(line!=null && i++<max) {
				try {
					System.out.println("--> "+i);
					
					ma.buildRecord(0, line, 0);
					System.out.println("Original ("+charset+"):");
					System.out.println(ma.toReadableString());
//					System.out.println(ma.getISBD());
//					String newr=ma.toString();
//					System.out.println(newr);
//					RecordInterface ri=RecordFactory.buildRecord(0, newr.getBytes("utf-8"), "isisbiblo", 0);
//					System.out.println(ri.toReadableString());
//					byte[] compressed=ZipUnzip.compressString(ri.toString());
//					byte[] decompressed=ZipUnzip.decompressString(compressed);
//					utf8.buildRecord(0, decompressed, 0);
//					System.out.println("Decompressed:\n"+utf8.toReadableString());
//					RecordInterface ri1=RecordFactory.buildRecord(0, decompressed, "isisbiblo", 0);
//					System.out.println("Decompressed1:\n"+ri1.toReadableString());
				}
				catch(Exception e2) {
					e2.printStackTrace();
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
