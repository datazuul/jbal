package org.jopac2.jbal;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.net.*;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;

public abstract class ClassList{ 

public static String[] findClasses(ClassLoader classLoader, String packageFilter) {
    Vector<String> classTable = new Vector<String>();
    Object[] classPaths;
    try{
      // get a list of all classpaths
      classPaths = ((java.net.URLClassLoader) classLoader).getURLs();
    }catch(ClassCastException cce){
      // or cast failed; tokenize the system classpath
      classPaths = System.getProperty("java.class.path", "").split(File.pathSeparator);      
    }
    
    for(int h = 0; h < classPaths.length; h++){
      Enumeration<?> files = null;
      JarFile module = null;
      // for each classpath ...
      File classPath = new File( (URL.class).isInstance(classPaths[h]) ?
                                  ((URL)classPaths[h]).getFile() : classPaths[h].toString() );
      if( classPath.isDirectory()){   // is our classpath a directory and jar filters are not active?
        List<String> dirListing = new ArrayList<String>();
        // get a recursive listing of this classpath
        recursivelyListDir(dirListing, classPath, new StringBuffer() );
        // an enumeration wrapping our list of files
        files = Collections.enumeration( dirListing );
      }else if( classPath.getName().endsWith(".jar") ){    // is our classpath a jar?
        try{
          // if our resource is a jar, instantiate a jarfile using the full path to resource
          module = new JarFile( classPath );
        }catch (MalformedURLException mue){
        	continue;
        }catch (IOException io){
          continue;
        }
        // get an enumeration of the files in this jar
        files = module.entries();
      }
      
      // for each file path in our directory or jar
      while( files != null && files.hasMoreElements() ){
        // get each fileName
        String fileName = files.nextElement().toString();
        // we only want the class files
        if( fileName.endsWith(".class") ){
          // convert our full filename to a fully qualified class name
          String className = fileName.replaceAll("/", ".").substring(0, fileName.length() - 6);
          // debug class list
          // System.out.println(className);
          // skip any classes in packages not explicitly requested in our package filter          
          if( packageFilter != null) {
        	String t=className;
        	if(t.contains(".")) t=t.substring( 0, t.lastIndexOf("."));
        	  if(!t.contains(packageFilter) )
        		  continue;
          }
          try {
        	  if(className.contains(".")) className=className.substring(className.lastIndexOf(".")+1);
//	          System.out.println(className);
        	  RecordInterface ma=RecordFactory.buildRecord(0, null, className, 0);
	          if(ma!=null) {
		          classTable.addElement(className);
		          ma.destroy();
	          }
          }
          catch(Exception e){}
        }
      }
      
      // close the jar if it was used
      if(module != null){
        try{
          module.close();
        }catch(IOException ioe){
          continue;
        }
      }
      
    } // end for loop
    
    return classTable.toArray(new String[classTable.size()]);
  } // end method
  
  /**
   * Recursively lists a directory while generating relative paths. This is a helper function for findClasses.
   * Note: Uses a StringBuffer to avoid the excessive overhead of multiple String concatentation
   *
   * @param dirListing     A list variable for storing the directory listing as a list of Strings
   * @param dir                 A File for the directory to be listed
   * @param relativePath A StringBuffer used for building the relative paths
   */
  private static void recursivelyListDir(List<String> dirListing, File dir, StringBuffer relativePath){
    int prevLen; // used to undo append operations to the StringBuffer
    
    // if the dir is really a directory 
    if( dir.isDirectory() ){
      // get a list of the files in this directory
      File[] files = dir.listFiles();
      // for each file in the present dir
      for(int i = 0; i < files.length; i++){
        // store our original relative path string length
        prevLen = relativePath.length();
        // call this function recursively with file list from present
        // dir and relateveto appended with present dir
        recursivelyListDir(dirListing, files[i], relativePath.append( prevLen == 0 ? "" : "/" ).append( files[i].getName() ) );
        //  delete subdirectory previously appended to our relative path
        relativePath.delete(prevLen, relativePath.length());
      }
    }else{
      // this dir is a file; append it to the relativeto path and add it to the directory listing
      dirListing.add( relativePath.toString() );
    }
  }
}

