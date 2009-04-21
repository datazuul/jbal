package JSites.utils;
/*******************************************************************************
*
*  JOpac2 (C) 2002-2007 JOpac2 project
*
*     This file is part of JOpac2. http://jopac2.sourceforge.net
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
*******************************************************************************/
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URL;


public class DirectoryHelper {
	private static final DirectoryHelper dh=new DirectoryHelper();
	
	public static String[] getFileList(String file) {
		return null;
	}
	
	public static File[] processFiles(String path) {
		File dir = new File(path);
		return processFiles(dir);
	}
	
	public String _getPath() {
		return dh.getClass().getResource(dh.getClass().getSimpleName()+".class").getPath();
	}
	
	public static String getPath() {
		String p=dh._getPath();
		if(p.contains("WEB-INF")) {
			p=p.substring(0,p.indexOf("WEB-INF")-1);
		}
		return p;
	}
	
	public static File[] processFiles(File dir) {
	    // It is also possible to filter the list of returned files.
	    // This example does not return any files that start with `.'.
	    FilenameFilter filter = new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return !name.startsWith(".");
	        }
	    };
	    
	    File[] children = dir.listFiles(filter);
	    return children;
	}

	public static File[] processSubdir(String path) {
	    // This filter only returns directories
	    FileFilter fileFilter = new FileFilter() {
	        public boolean accept(File file) {
	            return (file.isDirectory()&&!file.getName().startsWith("."));
	        }
	    };
	    
		File dir = new File(path);
	    
	    File[] children = dir.listFiles(fileFilter);
	    return children;
	}
	
    public static boolean containsFile(File dir, String filename) {
    	boolean r=false;
    	File[] files=DirectoryHelper.processFiles(dir);
    	for(int i=0;i<files.length;i++) {
    		if(files[i].getName().equals(filename)) { 
    			r=true;
    			break;
    		}
    	}
		return r;
	}
}
