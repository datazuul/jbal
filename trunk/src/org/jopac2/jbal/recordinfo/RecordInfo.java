package org.jopac2.jbal.recordinfo;

import java.io.Serializable;

public class RecordInfo implements Serializable {
	private static final long serialVersionUID = 2485121416854465588L;
	
	private String jid, titolo, edizione, editore, collazione, collana, copertina, numerostandard, note, 
			areadisciplinare, testoabstract, bibliografia, dspace, opac, sintesiposseduto, prezzo, bid,xml, authors;	

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getEdizione() {
		return edizione;
	}

	public void setEdizione(String edizione) {
		this.edizione = edizione;
	}

	public String getEditore() {
		return editore;
	}

	public void setEditore(String editore) {
		this.editore = editore;
	}

	public String getCollazione() {
		return collazione;
	}

	public void setCollazione(String collazione) {
		this.collazione = collazione;
	}

	public String getCollana() {
		return collana;
	}

	public void setCollana(String collana) {
		this.collana = collana;
	}

	public String getNumerostandard() {
		return numerostandard;
	}

	public void setNumerostandard(String numerostandard) {
		this.numerostandard = numerostandard;
	}

	public String getCopertina() {
		return copertina;
	}

	public void setCopertina(String copertina) {
		this.copertina = copertina;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAreadisciplinare() {
		return areadisciplinare;
	}

	public void setAreadisciplinare(String areadisciplinare) {
		this.areadisciplinare = areadisciplinare;
	}

	public String getTestoabstract() {
		return testoabstract;
	}

	public void setTestoabstract(String testoabstract) {
		this.testoabstract = testoabstract;
	}

	public String getBibliografia() {
		return bibliografia;
	}

	public void setBibliografia(String bibliografia) {
		this.bibliografia = bibliografia;
	}

	public String getDspace() {
		return dspace;
	}

	public void setDspace(String dspace) {
		this.dspace = dspace;
	}

	public String getOpac() {
		return opac;
	}

	public void setOpac(String opac) {
		this.opac = opac;
	}

	public String getSintesiposseduto() {
		return sintesiposseduto;
	}

	public void setSintesiposseduto(String sintesiposseduto) {
		this.sintesiposseduto = sintesiposseduto;
	}


	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getJid() {
		return jid;
	}

	public void setPrezzo(String prezzo) {
		this.prezzo = prezzo;
	}

	public String getPrezzo() {
		return prezzo;
	}

	public String getXML() {
		return xml;
	}
	
	public void setXML(String xml) {
		this.xml=xml;
	}

}
