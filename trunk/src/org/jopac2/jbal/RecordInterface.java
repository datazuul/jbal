package org.jopac2.jbal;

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
 * @version 05/06/2006
 */

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.jopac2.jbal.importers.Readers.RecordReader;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.TokenWord;

public interface RecordInterface {
	/** clona il record, restituendone una nuova istanza
	 * 
	 * @return RecordInterface
	 */
	public RecordInterface clone();

	/**
	 * Implementa un distruttore per il record bibliografico corrente. Questo
	 * metodo libera la memoria occupata. NON cancella il record dal database.
	 */
	public void destroy();

	/**
	 * Resituisce il tipo di record bibliografico, ad esempio 'a'
	 */
	public String getType();

	/**
	 * Imposta il tipo di record bibliografico
	 */
	public void setType(String type);

	/**
	 * Restituisce lo stato del record bibliografico, ad esempio 'n'
	 */
	public String getStatus();

	/**
	 * Imposta lo stato del record bibliografico, ad esempio 'n'
	 */
	public void setStatus(String status);

	/**
	 * Restituisce il livello del record bibliografico corrente
	 */
	public String getBiblioLevel();

	/**
	 * Imposta il livello del record bibliografico corrente
	 */
	public void setBiblioLevel(String lev);

	/**
	 * Verificare questi due
	 */
	public String getHierarchicalLevelCode();
	public void setHierarchicalLevelCode(String lCode);

	/**
	 * Restituisce true se ci sono collegamenti superiori al record
	 * bibliografico corrente
	 */
	public boolean hasLinkUp();

	/**
	 * Restituisce true se ci sono collegamenti inferiori al record
	 * bibliografico corrente
	 */
	public boolean hasLinkDown();

	/**
	 * Restituisce true se ci sono collegamenti a collane al record
	 * bibliografico corrente
	 */
	public boolean hasLinkSerie();

	/**
	 * Restituisce un vettore di record inferiori
	 */
	public Vector<RecordInterface> getHasParts();

	/**
	 * Restituisce un vettore di record inferiori
	 */
	public Vector<RecordInterface> getIsPartOf();

	/**
	 * Restituisce un vettore di record di collana
	 */
	public Vector<RecordInterface> getSerie();
	
	/**
	 * Restituisce un vettore di record associati al tag indicato
	 * @param tag
	 * @return
	 * @throws JOpac2Exception 
	 */
	public Vector<RecordInterface> getLinked(String tag) throws JOpac2Exception;

	/**
	 * Aggiunge al record bibliografico una informazione codificata in un tag
	 * unimarc
	 */
	public void addTag(String newTag);

	/**
	 * Aggiunge al record bibliografico un vettore di informazioni codificate in
	 * un tag unimarc
	 */
	public void addTag(Vector<String> newTags);
	

	/**
	 * Restituisce il livello del record bibliografico corrente
	 */
	public int getLivello();

	/**
	 * Restituisce una descrizione del tipo di notizia
	 */
	public String getTipo();

	/**
	 * Imposta u codice univoco di identificazione per il database di JOpac2
	 */
	public void setJOpacID(long l);

	/**
	 * Restituisce il codice univoco di identificazione del database JOpac2
	 */
	public long getJOpacID();

	/**
	 * Imposta un BID (identificativo bibliografico) per il record corrente
	 */
	public void setBid(String b);

	/**
	 * Restituisce i BID (identificativo bibliografico) del record corrente
	 */
	public String getBid();

	/**
	 * Costruisce una rappresentazione testuale del record bibliografico
	 * corrente
	 */
	public String toString();
	
	public String toReadableString();

	/**
	 * Restituisce un vettore di autori
	 */
	public Vector<String> getAuthors(); // Autori

	/**
	 * Restituisce un vettore di soggetti
	 * @return
	 */
	public Vector<String> getSubjects(); // Soggetti

	/**
	 * Restituisce un vettore di classificazioni
	 * @return
	 */
	public Vector<String> getClassifications(); // Classificazioni

	/**
	 * Restituisce un vettore di editori
	 * @return
	 */
	public Vector<String> getEditors(); // Editori

	/**
	 * Restituisce l'informazione sull'edizione
	 * @return
	 */
	public String getEdition(); // Edizione
	
	/**
	 * Restituisce l'informazione sul luogo di edizione
	 * @return
	 */
	public String getPublicationPlace(); // Luogo di pubblicazione

	/**
	 * Restituisce l'informazione sulla data di pubblicazione
	 * @return
	 */
	public String getPublicationDate(); // Data di pubblicazione

	/**
	 * Restituisce il titolo associato al volume descritto nel
	 * record bibliografico corrente
	 * @return
	 */
	public String getTitle(); // Titolo

	/**
	 * Restituisce una descrizione ISBD per il volume descritto nel record 
	 * bibliografico corrente
	 * @return
	 */
	public String getISBD(); // ISBD

	/**
	 * Restituisce un vettore di localizzazioni per il record bibliografico corrente
	 * @return
	 */
	public Vector<BookSignature> getSignatures(); // Localizzazioni
	
	/**
	 * Aggiunge la signature al record
	 * @param signature
	 */
	public void addSignature(BookSignature signature) throws JOpac2Exception;
	

	/**
	 * Restituisce l'abstract del record bibliografico corrente
	 * @return
	 */
	public String getAbstract(); // Abstract

	/**
	 * Restituisce la descrizione fisica del volume descritto nel
	 * record bibliografico corrente
	 * @return
	 */
	public String getDescription(); // Descrizione fisica

	/**
	 * Costruisce una rappresentazione ISO2709 per il record bibliografico
	 * corrente
	 * @return
	 */
	//public String toISO2709();

	/**
	 * Restituisce un RecordRedaer per il tipoNotizia corrente
	 * @return
	 */
	//public RecordReader getRecordReader(BufferedReader f);
	public RecordReader getRecordReader(InputStream dataFile) throws UnsupportedEncodingException;
	
	/**
	 * Restituisce una serie di item parola/tag/data_element della notizia
	 * @return
	 */
	public Enumeration<TokenWord> getItems();

	/**
	 * Restituisce il numero standard (ISBN per i libri, ISSN per i periodici)
	 * @return
	 */
	public String getStandardNumber();
	
	/**
	 * Restituisce le note sul materiale
	 * @return
	 */
	public String getComments();

	/**
	 * Rimuove le signatures (inventari, collocazioni e altri codici gestionali della biblioteca)
	 * dal record corrente.
	 * In UNIMARC dovrebbere corrispondere a cancellare tutti i tag della serie 9xx.
	 * @throws JOpac2Exception
	 */
	public void clearSignatures() throws JOpac2Exception;
	
	
	/**
	 * Rimuove il tag (o gli elementi) indicati
	 * @param tag
	 * @throws JOpac2Exception
	 */
	public void removeTags(String tag) throws JOpac2Exception;
	
	/**
	 * Calcola un hash relativo alla notizia, senza tener conto di eventuali dati gestionali.
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public String getHash() throws JOpac2Exception, NoSuchAlgorithmException;
	
	/**
	 * Calcola il livello di similitudine tra due record.
	 */
	public float similarity(RecordInterface ma);
	
	/**
	 * Verifica se nella notizia c'e' la stringa indicata, indifferentemente dal tag (indicizzati e no)
	 */
	public boolean contains(String s);
	
	/**
	 * Verifica se nella notizia c'è la stringa nel tag indicato, indifferentemente da campo
	 */
	public boolean contains(String tag, String s);
	
	/**
	 * Verifica se nella notizia c'è la stringa nel tag e nel campo indicato
	 */
	public boolean contains(String tag, String field, String s);
}