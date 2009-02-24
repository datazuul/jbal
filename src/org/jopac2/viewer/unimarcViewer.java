package org.jopac2.viewer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;

public class unimarcViewer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String filename="/java_sba/colloca/docs/diprova.uni";
		
		if(args.length>0) filename=args[0];
		
		FileReader f=new FileReader(filename);
		BufferedReader r=new BufferedReader(f);
		
		String line=r.readLine();
		while(line!=null) {
			RecordInterface ma=RecordFactory.buildRecord(0, line, "sebina", 0);
			System.out.println(ma.toReadableString());
			line=r.readLine();
		}
		
		r.close();
		f.close();

	}

}
