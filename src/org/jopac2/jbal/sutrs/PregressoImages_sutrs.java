package org.jopac2.jbal.sutrs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.Readers.TxtIndexRecordReader;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.utils.JOpac2Exception;


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
* @author	Romano Trampus
* @version 12/01/2007
*/


public class PregressoImages_sutrs extends Sutrs {
	
  public PregressoImages_sutrs(byte[] notizia,String dTipo,String livello)  throws Exception {
    this.iso2709Costruttore(notizia,dTipo,Integer.parseInt(livello));
  }
  
  public Hashtable<String, List<Tag>> getRecordMapping() {
		Hashtable<String, List<Tag>> r=new Hashtable<String, List<Tag>>();
		

		return r;
	}

	public String getRecordTypeDescription() {
		return "Pregresso ISO2709 format.";
	}
  
  public void init(String stringa) {
	  Tag t=new Tag("001",' ',' ');
	  try {
		t.setRawContent(stringa);
	} catch (JOpac2Exception e) {
		e.printStackTrace();
	}
	addTag(t);
	  setBid(stringa);
  }
  
  public String getBid() {
	  return bid;
  }
  
	public RecordReader getRecordReader(InputStream f) throws UnsupportedEncodingException {
		return new TxtIndexRecordReader(f,delimiters);
	}
	
	  /* (non-Javadoc)
	 * @see JOpac2.dataModules.iso2709.ISO2709Impl#clearSignatures()
	 */
	@Override
	public void clearSignatures() throws JOpac2Exception {
		//this.removeTag("950");
	}
}