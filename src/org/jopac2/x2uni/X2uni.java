package org.jopac2.x2uni;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;

public class X2uni {

	private static String workDir="/Users/romano/Documents/JOpac2-related/";
	// /Users/romano/Documents/JOpac2-related/seminarioPN/bd/biblo.iso
	private static String inFile=workDir+"seminarioPN/bd/biblo.iso";
	private static String outFile=workDir+"seminarioPN/bd/biblo.uni";
	
	public static void main(String[] args) throws Exception {
		FileInputStream f=new FileInputStream(new File(inFile));
		
		PrintWriter p=new PrintWriter(outFile);
		
		RecordInterface mat=RecordFactory.buildRecord("0", null, "isisbiblo", 0);
		RecordReader recordReader=mat.getRecordReader(f);
		
		byte[] line=recordReader.readRecord();
		while(line!=null) {
			RecordInterface ma=RecordFactory.buildRecord("0", line, "isisbiblo", 0);
			RecordInterface uni=null;
			
//			p.println(uni.toString());
//			System.out.println(ma.toReadableString());
//			System.out.println(uni.toReadableString());
			
			uni=record2Unimarc(ma);
			p.println(uni.toString());
			
//			Vector<BookSignature> vb=ma.getSignatures();
//			for(int i=0;vb!=null && i<vb.size();i++) {
////				if(vb.elementAt(i).getLibraryId().equals("BNP")) {
//					uni=record2Unimarc(ma);
////					p.println(uni.toString());
////					break;
////				}
//			}
			if(uni!=null) uni.destroy();
			ma.destroy();
			line=recordReader.readRecord();
		}
		
		f.close();
		p.close();

	}

	private static RecordInterface record2Unimarc(RecordInterface ma) throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		RecordInterface uni=RecordFactory.buildRecord("0", null, "sebina", 0);
		uni.setType(ma.getType());
		
		try {
			uni.setAbstract(ma.getAbstract());
		} catch (JOpac2Exception e) {
			e.printStackTrace();
		}
		uni.setBiblioLevel(ma.getBiblioLevel());
		uni.setBid(ma.getBid());
		try {
			uni.setLanguage(ma.getLanguage());
		} catch (JOpac2Exception e3) {
			e3.printStackTrace();
		}
		try {
			uni.setDescription(ma.getDescription());
		} catch (JOpac2Exception e2) {
			e2.printStackTrace();
		}
		try {
			uni.setEdition(ma.getEdition());
		} catch (JOpac2Exception e1) {
			e1.printStackTrace();
		}
		try {
			uni.setPublicationDate(ma.getPublicationDate());
		} catch (JOpac2Exception e1) {
			e1.printStackTrace();
		}
		try {
			uni.setPublicationPlace(ma.getPublicationPlace());
		} catch (JOpac2Exception e1) {
			e1.printStackTrace();
		}
		try {
			uni.setStandardNumber(ma.getStandardNumber(), "ISBN");
		} catch (JOpac2Exception e1) {
			e1.printStackTrace();
		}
		try {
			uni.setTitle(ma.getTitle());
		} catch (JOpac2Exception e) {
			e.printStackTrace();
		}
		
		Vector<String> editors=ma.getEditors();
		if(editors!=null && editors.size()>0) {
			try {
				uni.addPublisher(editors.elementAt(0));
			} catch (JOpac2Exception e) {
				e.printStackTrace();
			}
		}
		
		
		Vector<String> a=ma.getAuthors();
		for(int i=0;a!=null && i<a.size();i++)
			try {
				uni.addAuthor(a.elementAt(i));
			} catch (JOpac2Exception e) {
				e.printStackTrace();
			}
		
		Vector<ClassificationInterface> c=ma.getClassifications();
		for(int i=0;c!=null && i<c.size();i++) {
			try {
				uni.addClassification(c.elementAt(i));
			} catch (JOpac2Exception e) {
				e.printStackTrace();
			}
		}
		
		Vector<SubjectInterface> sub=ma.getSubjects();
		for(int i=0;sub!=null && i<sub.size();i++) {
			try {
				uni.addSubject(sub.elementAt(i));
			} catch (JOpac2Exception e) {
				e.printStackTrace();
			}
		}
			
		try {
			uni.addComment(ma.getComments());
		} catch (JOpac2Exception e) {
			e.printStackTrace();
		}
		
		Vector<RecordInterface> se=ma.getSerie();
		for(int i=0; se!=null && i<se.size();i++) {
			try {
				uni.addSerie(record2Unimarc(se.elementAt(i)));
			} catch (JOpac2Exception e) {
				e.printStackTrace();
			}
		}
		
		Vector<RecordInterface> inf=ma.getHasParts();
		for(int i=0; inf!=null && i<inf.size();i++) {
			try {
				uni.addPart(record2Unimarc(inf.elementAt(i)));
			} catch (JOpac2Exception e) {
				e.printStackTrace();
			}
		}
		
		Vector<RecordInterface> sup=ma.getIsPartOf();
		for(int i=0; sup!=null && i<sup.size();i++) {
			try {
				uni.addPartOf(record2Unimarc(sup.elementAt(i)));
			} catch (JOpac2Exception e) {
				e.printStackTrace();
			}
		}

		Vector<BookSignature> vb=ma.getSignatures();
		for(int i=0;vb!=null && i<vb.size();i++) {
			try {
				uni.addSignature(vb.elementAt(i));
			} catch (JOpac2Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return uni;

		
	}

}
