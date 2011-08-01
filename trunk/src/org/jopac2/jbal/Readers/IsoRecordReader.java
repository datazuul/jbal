/*
 * RecordReader.java
 *
 * Created on 17 novembre 2003, 21.06
 */

package org.jopac2.jbal.Readers;
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

/**
* @author	Albert Caramia
* @version	??/??/2002
* 
* @author	Romano Trampus
* @version	??/??/2002
* 
* @author	Romano Trampus
* @version	17/11/2003
* 
* @author	Romano Trampus
* @version	32/11/2011
*/
//import java.util.*;
import java.io.*;

import org.jopac2.jbal.abstractStructure.Delimiters;

public class IsoRecordReader extends RecordReader {
//    private String endRecord;
    private byte recordTerminator;
    
    
    public IsoRecordReader(InputStream f, Delimiters delimiters) throws UnsupportedEncodingException {
        super(f);
        this.recordTerminator=delimiters.getByteRt();
//        endRecord=new String(new byte[] {fieldTerminator,recordTerminator});
    }
    
    public IsoRecordReader(InputStream f, Delimiters delimiters, String charsetEncoding) throws UnsupportedEncodingException {
        super(f,charsetEncoding);
        this.recordTerminator=delimiters.getByteRt();
//        endRecord=new String(new byte[] {fieldTerminator,recordTerminator});
    }

 
    /**
     *readRecord, legge un record. Se e' una sola linea ok, se no
     *ritorna tutto il record su una sola stringa.
     */
    public byte[] readRecord() throws IOException {

    	int rl=-1;
        byte[] recordRaw=null;

        byte[] recordLength=new byte[5];
        
        int result=readBytes(recordLength, 0, 5);
        if(result!=-1) {
        	try {
        		rl=Integer.parseInt(new String(recordLength));
        		recordRaw=new byte[rl];
        		for(int i=0;i<5;i++) recordRaw[i]=recordLength[i]; // copy of length
        		int p=5;  // current position into be read
        		
        		result=readBytes(recordRaw,p,rl-p);
        		
        		
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        	}
        	
        	if(recordRaw[rl-1]!=recordTerminator) {
            	// ops, something wrong, save error and search for next recordTerminator
            	if(errorStream!=null)
            		errorStream.write(recordRaw);
            	int b=inputStream.read();
            	while(b!=-1 && b!=recordTerminator) {
            		if(errorStream!=null)
            			errorStream.write(b);
            		b=inputStream.read();
            	}
            	if(errorStream!=null && b!=-1)
        			errorStream.write(b);
            }
        }
        
        return recordRaw;
    }

	private int readBytes(byte[] recordRaw, int p, int l) throws IOException {
		int result=-2;
		int rl=l+p;
		int ip=p;
		
		while(result!=-1 && p!=rl) {
    		result=inputStream.read(recordRaw,p,rl-p); // read record except already read
    		if(result!=-1)
    			p=removeCRLF(recordRaw,p,result);  // remove 10 and 13 from read data
		}
		return p==ip?-1:p;
	}

	private int removeCRLF(byte[] recordRaw, int p, int l) {
		int n=p,o=p;
		while(o!=l+p) {
			switch(recordRaw[o]) {
			case 10:
				o++;
				break;
			case 13:
				o++;
				break;
			default: 
				recordRaw[n++]=recordRaw[o++];
				break;
			}
		}
		return n;
	}

	
    
}
