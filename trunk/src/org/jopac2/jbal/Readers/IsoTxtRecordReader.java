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
*/
//import java.util.*;
import java.io.*;

import org.jopac2.jbal.abstractStructure.Delimiters;

public class IsoTxtRecordReader extends IsoRecordReader {
    
    public IsoTxtRecordReader(InputStream f, Delimiters delimiters)  throws UnsupportedEncodingException {
        super(f,delimiters);
    }

    
    /**
     * Esempio record:

###      *****nam0 22*****   450 
001      CNGL0000002278
005      20000201000000.0
100      $a20000201d1983    m  u0itaa01      ba
200  1   $aCASA DI GUERRA$fBossi Fedrigotti, Isabella
210      $aMilano$cLonganesi & C.$d1983
676      $a853.91
700   0  $aBossi Fedrigotti, Isabella
950      $a0000002278$b20000201$c13756$e853.91 BOSS$jCNGL

     */
 
    /**
     *readRecord, legge un record. Se e' una sola linea ok, se no
     *ritorna tutto il record su una sola stringa.
     *Per sapere se e' finito il record guarda se c'e' il 
     *terminatore di record (rt);
     */
    public byte[] readRecord() throws IOException {
        String record="";
        String tempData=super.readLine();
        boolean endRecord=false;
        
        // recupera inizio record
        while(tempData!=null&&!tempData.startsWith("###")) {
        	tempData=super.readLine();
        }
        
        // legge il record fino a una riga vuota (o fine file)
        int charPos=5;
        while(tempData!=null&&!endRecord) {
        	if(tempData.charAt(charPos)>'9'||(tempData.charAt(charPos)<'0'&&tempData.charAt(charPos)!=32)) {
        		if(record.endsWith("\n")) record=record.substring(0,record.length()-1)+"\\|";
        	}
        	record=record+tempData+"\n";
            tempData=super.readLine();
            if((tempData==null)||(tempData.length()==0)) {
                endRecord=true;
            }
        }
        return record.getBytes();
    }
}
