package org.jopac2.engine.utils;
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
import java.io.*;
import java.util.*;
import java.util.zip.*;

public class ZipUnzip {
  private static final int BUFFER_SIZE=1024;
  
  /**
   * http://www.exampledepot.com/egs/java.util.zip/DecompArray.html?l=rel
   * The Java Developers Almanac 1.4
   * by Patrick Chan (Author)
   * Prentice Hall PTR; 4 edition (March 25, 2002)
   * # ISBN-10: 0201752808
   * # ISBN-13: 978-0201752809
   * @param in
   * @return
   */
  public static final byte[] compressString(String in) {
	  byte[] input=null;
	try {
		input = in.getBytes("UTF-8");
	} catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	    
	    // Create the compressor with highest level of compression
	    Deflater compressor = new Deflater();
	    compressor.setLevel(Deflater.BEST_COMPRESSION);
	    
	    // Give the compressor the data to compress
	    compressor.setInput(input);
	    compressor.finish();
	    
	    // Create an expandable byte array to hold the compressed data.
	    // You cannot use an array that's the same size as the orginal because
	    // there is no guarantee that the compressed data will be smaller than
	    // the uncompressed data.
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
	    
	    // Compress the data
	    byte[] buf = new byte[1024];
	    while (!compressor.finished()) {
	        int count = compressor.deflate(buf);
	        bos.write(buf, 0, count);
	    }
	    try {
	        bos.close();
	    } catch (IOException e) {
	    }
	    
	    // Get the compressed data
	    byte[] compressedData = bos.toByteArray();
	    return compressedData;
  }
  
  /**
   * http://www.exampledepot.com/egs/java.util.zip/DecompArray.html?l=rel
   * The Java Developers Almanac 1.4
   * by Patrick Chan (Author)
   * Prentice Hall PTR; 4 edition (March 25, 2002)
   * # ISBN-10: 0201752808
   * # ISBN-13: 978-0201752809
   * @param compressedData
   * @return
   */
  public static final byte[] decompressString(byte[] compressedData) {
//	 Create the decompressor and give it the data to compress
	    Inflater decompressor = new Inflater();
	    decompressor.setInput(compressedData);
	    
	    // Create an expandable byte array to hold the decompressed data
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedData.length);
	    
	    // Decompress the data
	    byte[] buf = new byte[1024];
	    boolean running=true;
	    while (running && !decompressor.finished()) {
	        try {
	            int count = decompressor.inflate(buf);
	            bos.write(buf, 0, count);
	        } catch (DataFormatException e) {
	        	running=false;
	        }
	    }
	    try {
	        bos.close();
	    } catch (IOException e) {
	    }
	    
	    byte[] r=null;

		r = running?bos.toByteArray():compressedData;
		
	    return r;
  }
	
  public static final void copyInputStream(InputStream in, OutputStream out) throws IOException
  {
    byte[] buffer = new byte[ BUFFER_SIZE ];
    int len;
    

    while((len = in.read(buffer)) >= 0)
      out.write(buffer, 0, len);

    in.close();
    out.close();
  }
  
  
  public static final void zipArchive(String zipfile, Vector<String> files, String base) {
      try
      {
        // Reference to the file we will be adding to the zipfile
        BufferedInputStream origin = null;

        // Reference to our zip file
        FileOutputStream dest = new FileOutputStream( zipfile );

        // Wrap our destination zipfile with a ZipOutputStream
        ZipOutputStream out = new ZipOutputStream( new BufferedOutputStream( dest ) );

        // Create a byte[] buffer that we will read data from the source
        // files into and then transfer it to the zip file
        byte[] data = new byte[ BUFFER_SIZE ];

        // Iterate over all of the files in our list
        
        for( Iterator<String> i=files.iterator(); i.hasNext(); )
        {
          // Get a BufferedInputStream that we can use to read the source file
          String filename = ( String )i.next();
          System.out.println( "Adding: " + filename );
          File f=new File(filename);
          if(!f.isDirectory()) {
	          FileInputStream fi = new FileInputStream( filename );
	          origin = new BufferedInputStream( fi, BUFFER_SIZE );
	
	          // Setup the entry in the zip file
	          ZipEntry entry = new ZipEntry( filename.substring(base.length()+1) );
	          out.putNextEntry( entry );
	
	          // Read data from the source file and write it out to the zip file
	          int count;
	          while( ( count = origin.read(data, 0, BUFFER_SIZE ) ) != -1 )
	          {
	            out.write(data, 0, count);
	          }
	
	          // Close the source file
	          origin.close();
          }
          else {
        	  ZipEntry entry=new ZipEntry( filename.substring(base.length()+1) );
        	  out.putNextEntry( entry );
          }
        }

        // Close the zip file
        out.close();
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }
  }

  @SuppressWarnings("unchecked")
public static final void unzipArchive(String zipfile, String destPath) {
    Enumeration<ZipEntry> entries;
    ZipFile zipFile;
    try {
      zipFile = new ZipFile(zipfile);

      entries = (Enumeration<ZipEntry>) zipFile.entries();

      while(entries.hasMoreElements()) {
        ZipEntry entry = (ZipEntry)entries.nextElement();

        if(entry.isDirectory()) {
          // Assume directories are stored parents first then children.
          System.err.println("Extracting directory: " + entry.getName());
          // This is not robust, just for demonstration purposes.
          (new File(destPath+"/"+entry.getName())).mkdir();
          continue;
        }

        System.err.println("Extracting file: " + entry.getName());
        String destName=destPath+"/"+entry.getName();
        destName=destName.replaceAll("\\\\","/");
        
        checkDestname(destName);
        
        copyInputStream(zipFile.getInputStream(entry),
           new BufferedOutputStream(new FileOutputStream(destName)));
      }

      zipFile.close();
    } catch (IOException ioe) {
      System.err.println("Unhandled exception:");
      ioe.printStackTrace();
      return;
    }
  }


	private static void checkDestname(String destName) {
		int ix = destName.lastIndexOf('/');
		String pDir=destName.substring(0,ix);
		File d = new File(pDir);
		if (!(d.exists() && d.isDirectory())) {
			System.out.println("Creating Directory: " + pDir);
			if (!d.mkdirs()) {
				System.err.println("Warning: unable to mkdir " + pDir);
			}
		}
	}
}
