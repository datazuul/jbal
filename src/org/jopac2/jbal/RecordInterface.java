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

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.TokenWord;

public interface RecordInterface {
	/**
	 * Restituisce una descrizione del tipo di record
	 * @return
	 */
	public String getRecordTypeDescription();
	
	/**
	 * Restituisce una mappatura tra gli elementi del record e i canali da indicizzare.
	 * @return
	 */
	public Hashtable<String,List<Tag>> getRecordMapping();
	
	
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
	public String getIndicatorLength();
	public void setIndicatorLength(String lCode);

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
	 * aggiunge una monografia inferiore
	 * @param part
	 * @throws JOpac2Exception 
	 */
	public void addPart(RecordInterface part) throws JOpac2Exception;

	/**
	 * Restituisce un vettore di record superiori
	 */
	public Vector<RecordInterface> getIsPartOf();
	
	/**
	 * aggiunge una monografia supeiore
	 * @param partof
	 * @throws JOpac2Exception 
	 */
	public void addPartOf(RecordInterface partof) throws JOpac2Exception;

	/**
	 * Restituisce un vettore di record di collana
	 */
	public Vector<RecordInterface> getSerie();
	
	/**
	 * aggiunge un record di collana
	 * @param serie
	 * @throws JOpac2Exception 
	 */
	public void addSerie(RecordInterface serie) throws JOpac2Exception;
	
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
	public void addTag(Tag newTag);

	/**
	 * Aggiunge al record bibliografico un vettore di informazioni codificate in
	 * un tag unimarc
	 */
	public void addTag(Vector<Tag> newTags);
	

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
	 * Restituisce la lingua
	 * @return
	 */
	public String getLanguage();
	
	/**
	 * Aggiunge lingue al record al record
	 * @param language
	 * @throws JOpac2Exception 
	 */
	public void setLanguage(String language) throws JOpac2Exception;

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
	
	public String toEncapsulatedRecordFormat();
	
	public String toXML() throws Exception;

	/**
	 * Restituisce un vettore di autori
	 */
	public Vector<String> getAuthors(); // Autori
	
	/**
	 * Aggiunge un autore al record
	 * @param author
	 * @throws JOpac2Exception 
	 */
	public void addAuthor(String author) throws JOpac2Exception;

	/**
	 * Restituisce un vettore di soggetti
	 * @return
	 */
	public Vector<SubjectInterface> getSubjects(); // Soggetti
	
	/**
	 * Aggiunge un subject al record
	 * @param subject
	 * @throws JOpac2Exception 
	 */
	public void addSubject(SubjectInterface subject) throws JOpac2Exception;

	/**
	 * Restituisce un vettore di classificazioni
	 * @return
	 */
	public Vector<ClassificationInterface> getClassifications(); // Classificazioni
	
	/**
	 * Aggiunge una classificazione al record
	 * @throws JOpac2Exception 
	 */
	public void addClassification(ClassificationInterface data) throws JOpac2Exception;

	/**
	 * Restituisce un vettore di editori
	 * @return
	 */
	public Vector<String> getEditors(); // Editori
	
	/**
	 * Aggiunge un editore al record
	 * @param editor
	 * @throws JOpac2Exception 
	 */
	public void addPublisher(String publisher) throws JOpac2Exception;

	/**
	 * Restituisce l'informazione sull'edizione
	 * @return
	 */
	public String getEdition(); // Edizione
	
	/**
	 * Imposta l'indicazione di edizione
	 * @param edition
	 * @throws JOpac2Exception 
	 */
	public void setEdition(String edition) throws JOpac2Exception;
	
	/**
	 * Restituisce l'informazione sul luogo di edizione
	 * @return
	 */
	public String getPublicationPlace(); // Luogo di pubblicazione
	
	/**
	 * imposta l'indicazione sul luogo di pubblicazione
	 * @throws JOpac2Exception 
	 */
	public void setPublicationPlace(String publicationPlace) throws JOpac2Exception;

	/**
	 * Restituisce l'informazione sulla data di pubblicazione
	 * @return
	 */
	public String getPublicationDate(); // Data di pubblicazione
	
	/**
	 * imposta l'indicazione sulla data di pubblicazione
	 * @param publicationDate
	 * @throws JOpac2Exception 
	 */
	public void setPublicationDate(String publicationDate) throws JOpac2Exception;

	/**
	 * Restituisce il titolo associato al volume descritto nel
	 * record bibliografico corrente
	 * @return
	 */
	public String getTitle(); // Titolo

	/**
	 * imposta il titolo
	 * @param title
	 * @throws JOpac2Exception 
	 */
	public void setTitle(String title) throws JOpac2Exception;
	
	
	/**
	 * imposta il titolo
	 * false = Title is not significant
	 *         This title does not warrant an added entry.
	 * true  = Title is significant
	 *         An access point is to be made from this title.
	 *         
	 * @param title
	 * @param significant
	 * @throws JOpac2Exception
	 */
	public void setTitle(String title, boolean significant) throws JOpac2Exception;
	
	/**
	 * Restituisce una descrizione ISBD per il volume descritto nel record 
	 * bibliografico corrente
	 * @return
	 */
	public String getISBD(); // ISBD
	
	/**
	 * imposta la descrizione ISBD
	 * @param isbd
	 * @throws JOpac2Exception 
	 */
	public void setISBD(String isbd) throws JOpac2Exception;

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
	 * imposta l'abstract del record
	 * @param abstractText
	 * @throws JOpac2Exception 
	 */
	public void setAbstract(String abstractText) throws JOpac2Exception;

	/**
	 * Restituisce la descrizione fisica del volume descritto nel
	 * record bibliografico corrente
	 * @return
	 */
	public String getDescription(); // Descrizione fisica
	
	/**
	 * imposta la descrizione fisica del volume
	 * @param description
	 * @throws JOpac2Exception 
	 */
	public void setDescription(String description) throws JOpac2Exception;

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
	 * imposta il numero standard
	 * ISBN
	 * ISSN
	 * ISRN
	 * ISMN
	 * NBN National bibliography number
     * GPN Government publication number
     * Other system control number
	 * @param standardNumber
	 * @throws JOpac2Exception 
	 */
	public void setStandardNumber(String standardNumber, String codeSystem) throws JOpac2Exception;
	
	/**
	 * Restituisce le note sul materiale
	 * @return
	 */
	public String getComments();
	
	/**
	 * aggiunge un commento (nota) al record
	 * @param comment
	 * @throws JOpac2Exception 
	 */
	public void addComment(String comment) throws JOpac2Exception;

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
	 * Return a Tag vector for the tag given
	 * @param tag
	 * @return
	 */
	public Vector<Tag> getTags(String tag);
	
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
	 * Verifica se nella notizia c'e' la stringa nel tag indicato, indifferentemente da campo
	 */
	public boolean contains(String tag, String s);
	
	/**
	 * Verifica se nella notizia c'e' la stringa nel tag e nel campo indicato
	 */
	public boolean contains(String tag, String field, String s);
	
	public BufferedImage getImage();

	public String getPublicationNature();
	
	public String getPrice();
	
	public String getField(String field);
	
	/**
	 * Restituisce un array di canali che devono essere indicizzati
	 * @return
	 */
	public String[] getChannels();
	
	/**
	 * Verifica la coerenza NSB - NSE per ogni campo in tutti i tag indicati.
	 * NSB e NSE sono utilizzati normalmente per marcare la parte non significativa di un titolo, ovvero
	 * dove inizia la parte da indicizzare.
	 * Sono anche utilizzati i codici di controllo:
	 * String nsb=(String.valueOf((char)0x1b)+"H");
	 * String nse=(String.valueOf((char)0x1b)+"I");
	 * @param tag
	 * @param nsb
	 * @param nse
	 */
	//public void checkNSBNSE(String tag, String nsb, String nse);
}