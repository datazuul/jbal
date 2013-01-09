package JSites.utils;
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
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;


public class DirectoryHelper {
	private static final DirectoryHelper dh=new DirectoryHelper();
	
	
	public static boolean deleteDir(String path) {
		File dir = new File(path);
		boolean r=true;
		try {
			dir.delete();
		}
		catch (Exception e) {
			r=false;
		}
		return r;
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
	    
	    Vector<File> sv=new Vector<File>();
	    for(int i=0;i<children.length;i++)
	    	sv.addElement(children[i]);
	    
	    Collections.sort(sv);
	    
	    for(int i=0;i<children.length;i++)
	    	children[i]=sv.elementAt(i);
	    
	    return children;
	}
	
	public static File[] processFilesRecursive(File dir) {
		Vector<File> r=new Vector<File>();
		File[] t=processFiles(dir);
		if(t!=null && t.length>0) {
			r.addAll(Arrays.asList(t));
			int l=r.size()-1;
			for(int i=l;i>=0;i--) {
				if(r.elementAt(i).isDirectory()) {
					t=processFilesRecursive(r.elementAt(i));
					r.addAll(Arrays.asList(t));
				}
			}
		}
		return r.toArray(new File[r.size()]);
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
