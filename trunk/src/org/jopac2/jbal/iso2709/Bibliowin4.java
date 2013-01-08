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
 * @version ??/??/2004
 * 
 * @author	Romano Trampus
 * @version ??/??/2004
 */

/**
 * JOpac2 - Modulo formato "BiblioWin 4.0". v. 0.1
 *          Questo modulo permette di importare dati registrati nel formato
 *          di esportazione di BiblioWin 4.0.
 *
 *
 * TODO: come vengono visualizzati e cercati i terzi livelli? Come vengono visualizzati?
 * 
 */

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

import org.jopac2.jbal.Readers.IsoTxtRecordReader;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.utils.*;

public class Bibliowin4 extends Unimarc {
	private String rawRecord=null;
	
	  public Bibliowin4(byte[] stringa, Charset charset, String dTipo)  throws Exception {
		    super(stringa,charset,dTipo);
		  }

		  public Bibliowin4(byte[] stringa,Charset charset,String dTipo, String livello)  throws Exception {
		    super(stringa,charset,dTipo,livello);
		  }
		  
		  public Hashtable<String, List<Tag>> getRecordMapping() {
				Hashtable<String, List<Tag>> r=super.getRecordMapping();
				
				List<Tag> bib=new Vector<Tag>();
				bib.add(new Tag("950","j",""));
				r.put("BIB", bib);
				
				List<Tag> inv=new Vector<Tag>();
				inv.add(new Tag("950","c",""));
				r.put("INV", inv);
				
				List<Tag> cll=new Vector<Tag>();
				cll.add(new Tag("913","a",""));
				r.put("CLL", cll);
				
				return r;
			}

			public String getRecordTypeDescription() {
				return "Bibliowin4 unimarc format.";
			}

	/**
	 * Esempio record:
	 * 
	 * ### *****nam0 22***** 450 001 CNGL0000002278 005 20000201000000.0 100
	 * $a20000201d1983 m u0itaa01 ba 200 1 $aCASA DI GUERRA$fBossi Fedrigotti,
	 * Isabella 210 $aMilano$cLonganesi & C.$d1983 676 $a853.91 700 0 $aBossi
	 * Fedrigotti, Isabella 950 $a0000002278$b20000201$c13756$e853.91 BOSS$jCNGL
	 * @throws Exception 
	 * 
	 */

	public void init(byte[] stringa, Charset charset) throws Exception {
		setTerminator((byte) '#',(byte)'$',(byte)'$');
//		setTerminator(null, "$", "$");
		if(stringa==null) return;
		rawRecord=new String(stringa);
		
		if(rawRecord.startsWith("###")) {

			inString = rawRecord;
			String[] in = rawRecord.split("\n");
	
			for (int i = 0; i < in.length; i++) {
				if (in[i].startsWith("###")) {
					recordStatus = rawRecord.substring(5, 6);
					recordType = rawRecord.substring(6, 7);
					recordBiblioLevel = rawRecord.substring(7, 8);
					indicatorLength = rawRecord.substring(8, 9);
				} else {
					dati.addElement(new Tag(in[i]));
					if (in[i].startsWith("001"))
						bid = in[i].substring(9);
				}
			}
		}
		else {
			super.init(stringa,charset);
		}
	}

	public void initLinkUp() {

	}

	public void initLinkSerie() {

	}

	public void initLinkDown() {

	}


	/**
	 * 29/12/2003 - R.T. Input: null Output: Vector di coppie. La coppia
	 * contiene nel primo elemento una Coppia con il codice della biblioteca e
	 * il nome della biblioteca e nel secondo elemento un Vector che contiene
	 * coppie (collocazione,inventario). Se serve anche il polo ci lo mettiamo
	 * nel codice della biblioteca?
	 */
	public Vector<BookSignature> getSignatures() {
		Vector<Tag> v = getTags("950"); // prende dati collocazione
		Vector<BookSignature> res = new Vector<BookSignature>();

		for (int i = 0; i < v.size(); i++) {
			String codiceBib = v.elementAt(i).getField("j").getContent(); 
			// non c'e' il nome biblioteca, solo il codice

			String col = v.elementAt(i).getField("e").getContent() + // collocazione
					Utils.ifExists(": ", v.elementAt(i).getField("n").getContent());
			String inv = v.elementAt(i).getField("c").getContent(); // prende
																// l'inventario

			res.addElement(new BookSignature(codiceBib, codiceBib, inv, col));
		}
		v.clear();
		v = null;
		return res;
	}

	public void addSignature(BookSignature signature) throws JOpac2Exception {
		// TODO Auto-generated method stub
		
	}

	public RecordReader getRecordReader(InputStream f) throws UnsupportedEncodingException {
//		return new IsoTxtRecordReader(f, "$", "###");
		return new IsoTxtRecordReader(f, delimiters);
	}
	
	protected String toISO2709() throws Exception {
		return rawRecord;
	}

	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return null;
	}
}

