/*
 * XmlRecordReader.java
 *
 * Created on 1 giugno 2006, 22.56
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
* 
* @author	Romano Trampus
* @version	01/06/2006
* 
*/
//import java.util.*;
import java.io.*;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;

import org.jopac2.jbal.Readers.RecordReader;

public class XmlRecordReader extends RecordReader  {
    private SAXParser parser;
    private XmlHandler handler;
    
    public XmlRecordReader(InputStream f, XmlHandler handler) throws UnsupportedEncodingException {
        super(f);
        this.handler=handler;
        parser=new SAXParser();
    }
    
	public void parse(BufferedReader f, LoadDataInterface data) throws IOException {
		handler.setParoleSpooler(paroleSpooler);
		handler.setLoadData(data);
		handler.setTipoNotizia(tipoNotizia);
		handler.setIdTipo(idTipo);
		parser.setContentHandler(handler);
        try { parser.parse(new InputSource(f)); } 
        catch (Exception e) {e.printStackTrace();}
	}
 
	/**
	 * Non fa nulla per xml-parser
	 */
    public String readRecord() throws IOException {
        String record="";

        return record;
    }
}
