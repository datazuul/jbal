package org.jopac2.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class EncodeUtil {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String filename="/java_jopac2/engine/data/demo_Sebina.uni";
		int i=20;
		
		System.out.println("--- INIZIO ---");
		
		Reader in = new InputStreamReader(new FileInputStream(filename), "UTF-8");
		BufferedReader br=new BufferedReader(in);
		
		String line="";
		
		String o="";
		
		while(i>0) {
			line=br.readLine();
			o+=line+"\n";
			
			i--;
		}
		
		String encoded=Base64.encode(o.getBytes());
		System.out.println(encoded);
		
		br.close();
	}

}
