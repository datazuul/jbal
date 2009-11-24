package htmlrw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class htmlrw {

	public static void main(String[] args) {
		String dir=args[0];
		subdir(dir);
	}
	
	public static void subdir(String dir) {
		File directory=new File(dir);
		if(directory.isDirectory()) {
			String[] cnt=directory.list();
			for(int i=0;i<cnt.length;i++) {
				System.out.println(dir+"/"+cnt[i]);
				subdir(dir+"/"+cnt[i]);
			}
		}
		
		if(directory.isFile()&&
				!dir.endsWith(".jpg")&&
				!dir.endsWith(".gif")) {
			fixhref(dir);
		}
	}
	
	public static void fixhref(String file) {
		String nfile="";
		if(file.contains("?")) nfile=file.replace("?", "_");
		else nfile=file+".old";
		
		File nf=new File(nfile);
		
		try {
			FileReader of=new FileReader(file);
			PrintWriter pw=new PrintWriter(nf);			
			BufferedReader br=new BufferedReader(of);
			while(br.ready()) {
				pw.println(fixline(br.readLine()));
			}
			of.close();
			br.close();
			pw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File of=new File(file);
		of.delete();
		if(nfile.endsWith(".old")) {
			if(!nf.renameTo(of)) {
				System.out.println("ERR");
			}
			
		}
	}

	private static String fixline(String string) {
		string=string.replace("iso-8859-1", "utf-8");
		String m=string.toLowerCase();
		if(m.contains("href=\"")) {
			int i=m.indexOf("href=\"")+6;
			String t=string.substring(i);
			String first=string.substring(0,i);
			int e=t.indexOf("\"");
			String middle=t.substring(0,e);
			String end=fixline(t.substring(e+1));
			string=first+middle.replace("?", "_")+"\""+end;
		}
		return string;
	}
}
