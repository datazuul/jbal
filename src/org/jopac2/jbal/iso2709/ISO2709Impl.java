package org.jopac2.jbal.iso2709;


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

/*
* @author	Albert Caramia
* @version ??/??/2002
* 
* @author	Romano Trampus
* @version ??/??/2002
*/

/***
 *Modificato:
 * 01/10/2003 - RT
 *              Aggiunto metodo per salvare un record in formato ISO2709
 */

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jopac2.jbal.ElectronicResource;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.IsoRecordReader;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.abstractStructure.Field;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.utils.SimilarityHelp;
//import java.lang.*;
//import JOpac2.utils.*;
import org.jopac2.utils.JOpac2Exception;

public abstract class ISO2709Impl extends ISO2709 {
  //public Vector<String> dati;

  public ISO2709Impl(byte[] notizia,Charset charset, String dTipo,String livello) throws Exception {
    this.iso2709Costruttore(notizia,charset,dTipo,Integer.parseInt(livello));
  }

  @Override
public void removeImage() throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public ISO2709Impl(byte[] notizia, Charset charset, String dTipo) throws Exception {
    this.iso2709Costruttore(notizia,charset,dTipo,0);
  }


  public ISO2709Impl() {
	  super();
      //dati=new Vector();
  }

  
	

	
	/* (non-Javadoc)
	 * @see JOpac2.dataModules.iso2709.ISO2709Impl#getHash()
	 */
	public String getHash() throws JOpac2Exception, NoSuchAlgorithmException {
		RecordInterface ma=this.clone();
		ma.clearSignatures();
		String n=ma.toString();
		ma.destroy();
		
		String output=null;
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(n.getBytes());
		byte[] md5sum = digest.digest();
		BigInteger bigInt = new BigInteger(1, md5sum);
		output = bigInt.toString(16);
		
		return output;
	}
	
	public String[] getChannels() {
		return channels;
	}

	

	public RecordReader getRecordReader(InputStream dataFile, String charset) throws UnsupportedEncodingException {
		RecordReader r=new IsoRecordReader(dataFile,delimiters,charset);
		r.setChToIndex(channels);
		return r;
	}

	/*
	public String getComments() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getStandardNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addSignature(BookSignature booksignature) throws JOpac2Exception {
		throw new JOpac2Exception(
				"Error: method addSignature is not implemented");
	}*/

	public float similarity(RecordInterface ma) {
		return similarity(this,ma);
	}
  
	public static float similarity(RecordInterface ma1, RecordInterface ma2) {
		return SimilarityHelp.weightedSimilarity(ma1,ma2);
	}

	public boolean contains(String s) {
		String d=dati.toString();
		return d.contains(s);
	}

	public boolean contains(String tag, String s) {
		String d=getTags(tag).toString();
		return d.contains(s);
	}

	public boolean contains(String tag, String field, String s) {
		Vector<Tag> t=getTags(tag);
		boolean r=false;
		for(int i=0;t!=null && i<t.size();i++) {
			if(t.elementAt(i).getField(field).getContent().contains(s)) {
				r=true;
				break;
			}
		}
		return r;
	}

	public void removeTags(String tag) throws JOpac2Exception {
		Pattern pattern=Pattern.compile(tag);
		for(int i=dati.size()-1;i>=0;i--) {
			Matcher matcher=pattern.matcher(dati.elementAt(i).getTagName());
			if(matcher.find()) {
				dati.removeElementAt(i);
			}
		}
	}
	
	/**
	 * Rimuove il tag se ha esattamente lo stesso contenuto (incluso tagname e modificatori)
	 * Anche eventuali duplicati.
	 * @param tag
	 */
	public void removeTag(Tag tag) {
		for(int i=dati.size()-1;i>=0;i--) {
			if(dati.elementAt(i).equalsContent(tag)) {
				dati.removeElementAt(i);
			}
		}
	}
	
	public String getPrice() {
		String ret = null;
		Tag tag = getFirstTag("010");
		if(tag!=null){
			Field f = tag.getField("d");
			if(f!=null)
				ret = f.getContent();
		}
		return null;
	}



	/**
	 * Use tag.checkNSBNSE(...) instead

	public void checkNSBNSE(String tag, String nsb, String nse) {
		for(int i=0;i<dati.size();i++) {
			if(dati.elementAt(i).getTagName().equals(tag)) {
				Tag t=new Tag(tag);
				t.checkNSBNSE(nsb, nse);
				dati.setElementAt(t, i);
			}
		}
	}*/

	@Override
	public void addElectronicVersion(ElectronicResource electronicResource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ElectronicResource[] getElectronicVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeElectronicVersion(ElectronicResource electronicResource) {
		// TODO Auto-generated method stub
		
	}
	
}