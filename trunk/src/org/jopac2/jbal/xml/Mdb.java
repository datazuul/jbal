package org.jopac2.jbal.xml;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.jopac2.jbal.Readers.MdbTableRecordReader;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.TokenWord;
import org.xml.sax.SAXException;

public class Mdb extends XML {
	public Mdb(byte[] notizia, String dTipo, String livello)
		throws NumberFormatException, SAXException, IOException {
	this.XMLCostruttore(notizia, dTipo, Integer.parseInt(livello));
	}
	
	public Mdb(byte[] notizia, String dTipo) throws SAXException, IOException {
	this.XMLCostruttore(notizia, dTipo, 0);
	}

	@Override
	public String getAbstract() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> getAuthors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<ClassificationInterface> getClassifications() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEdition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> getEditors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getISBD() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPublicationDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPublicationPlace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecordReader getRecordReader(InputStream dataFile) throws UnsupportedEncodingException {
		MdbTableRecordReader r=new MdbTableRecordReader(dataFile, "this");
		return r;
	}
	
	@Override
	public RecordReader getRecordReader(InputStream dataFile, String charset) throws UnsupportedEncodingException {
		MdbTableRecordReader r=new MdbTableRecordReader(dataFile, "this");
		return r;
	}

	@Override
	public Vector<BookSignature> getSignatures() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<SubjectInterface> getSubjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addTag(Tag newTag) {
		// TODO Auto-generated method stub
		
	}

	public String getComments() {
		// TODO Auto-generated method stub
		return null;
	}

	public BufferedImage getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getStandardNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector<Tag> getTags(String tag) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String[] getChannels() {
		Vector<TokenWord> v=XMLHelper.exploreData(document);
		
		String[] ch=new String[v.size()+1];
		for(int i=0;i<v.size();i++) ch[i+1]=v.elementAt(i).getTag();
		ch[0]="ANY";
		return ch;
	}

}