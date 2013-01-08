package org.jopac2.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.zip.GZIPInputStream;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.iso2709.Unimarc;

public class unimarcUtils {
	private static String ELECTRONIC_RESOURCE="electronic_resource";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String charset="utf-8";
//		String filename="/Java_src/java_jopac2/JOpac2/iso-test/winisis/teca2.iso";
		String ifile="/home/romano/Documents/lavoro/lavoro-UniTS/ebooks/springer/Springer_MARC_Biomed_Business_Math_2005-2007.mrc";
		String ofile=null;
		String efile=null;
		String tipo="sebina";
		String start=null;
		String end=null;
		String recordtype=null;
		boolean xml=false,delete=false;
		
		
		
		for(int i=0;i<args.length;i++) {
			if(args[i].startsWith("if=")) ifile=args[i].substring(3);
			if(args[i].startsWith("of=")) ofile=args[i].substring(3);
			if(args[i].startsWith("ef=")) efile=args[i].substring(3);
			if(args[i].startsWith("format=")) tipo=args[i].substring(7);
			if(args[i].startsWith("type=")) recordtype=args[i].substring(5);
			if(args[i].startsWith("start=")) start=args[i].substring(6);
			if(args[i].startsWith("end=")) end=args[i].substring(4);
			if(args[i].equals("xml")) xml=true;
			if(args[i].equals("delete")) delete=true;
		}
		
//		if(args.length>0) {
//			filename=args[0];
//			tipo=args[1];
//		}
		PrintStream pw=null;
		if(efile!=null) pw=new PrintStream(efile);
		else pw=System.err;
		
		PrintStream out=null;
		if(ofile!=null) out=new PrintStream(ofile);
		else out=System.out;
		
		FileInputStream f=new FileInputStream(new File(ifile));
		InputStream is=f;
		
		if(ifile.endsWith(".gz")) {
			is = new GZIPInputStream(new FileInputStream(ifile));
		}
		
		RecordInterface ma=null;
		try {
			ma=RecordFactory.buildRecord(0, null, tipo, 0);
			RecordReader r=ma.getRecordReader(is,charset);
			
			int i=0,min=0;
			int max=100000;
			if(end!=null) {
				try {
					max=Integer.parseInt(end);
				}
				catch(Exception e) {
					max=100000;
				}
			}
			if(start!=null) {
				try {
					min=Integer.parseInt(start);
				}
				catch(Exception e) {
					min=0;
				}
			}
			
			byte[] line=r.readRecord();
			byte[] prevline=null;
			while(line!=null && i++<max) {
				try {
					System.out.println("--> "+i);
					if(i>=min) {
						ma=RecordFactory.buildRecord(0, line, tipo, 0);
						
						if(delete) ma.setStatus(Unimarc.STATUS_DELETED_RECORD);
						if(recordtype!=null) {
							if(recordtype.equals(ELECTRONIC_RESOURCE)) ma.setType(Unimarc.TYPE_ELECTRONIC_RESOURCE);
						}
						
						if(xml) out.println(ma.toXML());
						else if(ofile==null) out.println(ma.toReadableString());
						else out.println(ma.toString());
					}
				}
				catch(Exception e2) {
					pw.println("Previous= "+new String(prevline));
					pw.println("Current  = "+new String(line));
					e2.printStackTrace(pw);
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
