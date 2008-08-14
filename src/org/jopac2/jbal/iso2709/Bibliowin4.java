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

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.jopac2.jbal.importers.Readers.IsoTxtRecordReader;
import org.jopac2.jbal.importers.Readers.RecordReader;
import org.jopac2.utils.*;

public class Bibliowin4 extends ISO2709Impl {
	private String rawRecord=null;

	public Bibliowin4(String stringa, String dTipo) {
		super();
		iso2709Costruttore(stringa, dTipo, 0);
		initLinkUp();
		initLinkDown();
		initLinkSerie();
	}

	public Bibliowin4(String stringa, String dTipo, String livello) {
		super();
		iso2709Costruttore(stringa, dTipo, Integer.parseInt(livello));
		initLinkUp();
		initLinkDown();
		initLinkSerie();
	}

	/**
	 * Esempio record:
	 * 
	 * ### *****nam0 22***** 450 001 CNGL0000002278 005 20000201000000.0 100
	 * $a20000201d1983 m u0itaa01 ba 200 1 $aCASA DI GUERRA$fBossi Fedrigotti,
	 * Isabella 210 $aMilano$cLonganesi & C.$d1983 676 $a853.91 700 0 $aBossi
	 * Fedrigotti, Isabella 950 $a0000002278$b20000201$c13756$e853.91 BOSS$jCNGL
	 * 
	 */

	public void init(String stringa) {
		setTerminator(null, "$", "$");
		
		rawRecord=stringa;
		
		if(stringa.startsWith("###")) {

			inString = stringa;
			String[] in = stringa.split("\n");
	
			for (int i = 0; i < in.length; i++) {
				if (in[i].startsWith("###")) {
					recordStatus = stringa.substring(5, 6);
					recordType = stringa.substring(6, 7);
					recordBiblioLevel = stringa.substring(7, 8);
					recordHierarchicalLevelCode = stringa.substring(8, 9);
				} else {
					dati.addElement(in[i]);
					if (in[i].startsWith("001"))
						bid = in[i].substring(9);
				}
			}
		}
		else {
			super.init(stringa);
		}
	}

	public void initLinkUp() {

	}

	public void initLinkSerie() {

	}

	public void initLinkDown() {

	}

	// ritorna un vettore di elementi biblo
	@SuppressWarnings("unchecked")
	public Vector getLink(String tag) {
		String v = getFirstTag(tag);

		Vector r = new Vector();
		try {
			if (v.length() > 0) { // se il vettore ha elementi, allora faro'
									// almeno una query
				Bibliowin4 not = new Bibliowin4(); // crea un nuovo elemento
													// Isisbiblo
				not.dati = new Vector();
				not.dati.addElement("011" + v.substring(3));
				not.setBid(getBid());
				not.setTerminator("#", "#", "^");
				r.addElement(not);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	public Vector<String> getSubjects() {
		return getElement(getTag("610"), "a");
	}

	public Vector<String> getClassifications() {
		return getElement(getTag("676"), "a");
	}

	public Vector<String> getEditors() {
		return getElement(getTag("210"), "c");
	}

	public String getPublicationPlace() {
		return getFirstElement(getFirstTag("210"), "a");
	}

	public String getPublicationDate() {
		return getFirstElement(getFirstTag("210"), "d");
	}

	// TODO per questo tipo trovare l'abstract
	public String getAbstract() {
		return null;
	}

	public Vector<String> getAuthors() { // 200 f ; g (ripetuto)
		String v = getFirstTag("200"); // Autore principale

		Vector<String> r = new Vector<String>();

		String t = getFirstElement(v, "f");
		if (t.length() > 0)
			r.addElement(t);

		Vector<String> g = getElement(v, "g");
		for (int i = 0; i < g.size(); i++) {
			r.addElement(g.elementAt(i));
		}
		g.clear();
		g = null;
		return r;
	}

	/**
	 * 29/12/2003 - R.T. Input: null Output: Vector di coppie. La coppia
	 * contiene nel primo elemento una Coppia con il codice della biblioteca e
	 * il nome della biblioteca e nel secondo elemento un Vector che contiene
	 * coppie (collocazione,inventario). Se serve anche il polo ci lo mettiamo
	 * nel codice della biblioteca?
	 */
	public Vector<BookSignature> getSignatures() {
		Vector<String> v = getTag("950"); // prende dati collocazione
		Vector<BookSignature> res = new Vector<BookSignature>();

		for (int i = 0; i < v.size(); i++) {
			String codiceBib = getFirstElement(v.elementAt(i), "j"); // non
																		// c'e'
																		// il
																		// nome
																		// biblioteca,
																		// solo
																		// il
																		// codice

			String col = getFirstElement(v.elementAt(i), "e") + // collocazione
					Utils.ifExists(": ", getFirstElement(v.elementAt(i), "n"));
			String inv = getFirstElement(v.elementAt(i), "c"); // prende
																// l'inventario

			res.addElement(new BookSignature(codiceBib, codiceBib, inv, col));
		}
		v.clear();
		v = null;
		return res;
	}

	/* (non-Javadoc)
	 * @see JOpac2.dataModules.iso2709.ISO2709Impl#clearSignatures()
	 */
	@Override
	public void clearSignatures() throws JOpac2Exception {
		this.removeArea("9");
	}

	public String getTitle() {
		String r = "";
		String tag = getFirstTag("200");
		r = getFirstElement(tag, "a")
				+ Utils.ifExists(": ", getFirstElement(tag, "e")); // specificazione
		// Utils.Log("Titolo: "+r);
		return r;
	}

	/*
	 * TODO Implementare metodo getEdition
	 */
	public String getEdition() {
		return null;
	}

	public String getISBD() {
		String r = "";
		String tag = getFirstTag("200"); // AREA 1
		String dim = getFirstTag("215");
		r = getFirstElement(tag, "a") + // titolo proprio
				Utils.ifExists(": ", getFirstElement(tag, "e")) + // specificazione
				Utils.ifExists(" / ", getFirstElement(tag, "f")); // resp.
																	// princip.

		Vector<String> g = getElement(tag, "g");
		for (int i = 0; i < g.size(); i++) {
			r = r + "; " + g.elementAt(i);
		}

		r = r + Utils.ifExists(". - ", getPublicationPlace()) + // luogo di
																// pubblicazione
				Utils.ifExists(" : ", getEditors()) + // editore
				Utils.ifExists(", ", getPublicationDate()) + // data di
																// pubblicazione
				Utils.ifExists(". - ", getFirstElement(dim, "a")) + // num.
																	// pagine
				Utils.ifExists(" : ", getFirstElement(dim, "c")) + // illustrazioni
				Utils.ifExists(" : ", getFirstElement(dim, "d")); // misure

		return quote(r);
	}

	public Bibliowin4() {
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public RecordReader getRecordReader(InputStream f) throws UnsupportedEncodingException {
		return new IsoTxtRecordReader(f, "$", "###");
	}
	
	protected String toISO2709() throws Exception {
		return rawRecord;
	}
}

