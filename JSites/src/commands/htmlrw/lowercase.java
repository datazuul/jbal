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

import java.io.File;


public class lowercase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dir="/eut.1";
		subdir(dir);
		

	}
	
	public static void subdir(String dir) {
		File directory=new File(dir);
		if(directory.isDirectory()) {
			String[] cnt=directory.list();
			for(int i=0;i<cnt.length;i++) {
				//System.out.println(dir+"/"+cnt[i]);
				subdir(dir+"/"+cnt[i]);
			}
		}
		
		if(directory.isFile()) {
			fixfilename(dir);
		}
	}
	
	public static void fixfilename(String file) {
		String path=file.substring(0,file.lastIndexOf("/")+1);
		file=file.substring(file.lastIndexOf("/")+1);
		String extension="."+file.substring(file.lastIndexOf(".")+1);
		file=file.substring(0,file.lastIndexOf("."));
		String nfile=file;
		nfile=nfile.toLowerCase().replaceAll("�", "a");
		nfile=nfile.replaceAll("�", "o").replaceAll("�", "u");
		nfile=nfile.replaceAll("\\W", "_");
		
		if(!file.equals(nfile)) {
			File nf=new File(path+nfile+extension);
			File of=new File(path+file+extension);
			
			if(!of.renameTo(nf)) {
				System.out.println("ERR "+nfile+extension);
			}

		}
		

	}


}
