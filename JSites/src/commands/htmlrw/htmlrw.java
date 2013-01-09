package commands.htmlrw;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2009 JOpac2 project
*
*     This file is part of JOpac2. http://www.jopac2.org
*
*  JOpac2 is free software; you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation; either version 2 of the License, or
*  (at your option) any later version.
*
*  JOpac2 is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with JOpac2; if not, write to the Free Software
*  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*
*  Please, see NOTICE.txt AND LEGAL directory for more info. Different licences
*  may apply for components included in JOpac2.
*
*******************************************************************************/

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
