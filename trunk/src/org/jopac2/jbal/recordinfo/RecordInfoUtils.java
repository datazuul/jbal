package org.jopac2.jbal.recordinfo;

import java.util.Vector;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.iso2709.Unimarc;
import org.jopac2.jbal.subject.UncontrolledSubjectTerms;

public class RecordInfoUtils {
		public static void setInfo(RecordInterface ma, RecordInfo r) {
			String titolo=ma.getTitle();
			String author="";
			if(!titolo.contains("/")) {
				Vector<String> authors=ma.getAuthors();
				for(int i=0;authors!=null && i<authors.size();i++) {
					titolo=titolo+" ; "+authors.elementAt(i);
					author=author+" ; "+authors.elementAt(i);
				}
				titolo=titolo.replaceFirst(" ; ", " / ");
				author=author.replaceFirst(" ; ", "");
			}
			r.setTitolo(titolo);
			r.setAuthors(author);
			
			if(ma.getIsPartOf()!=null && ma.getIsPartOf().size()>0)
				r.setCollana(ma.getIsPartOf().elementAt(0).getTitle());
			
			r.setCollazione(ma.getDescription());
			r.setEdizione(ma.getEdition());
			if(ma.getEditors()!=null && ma.getEditors().size()>0)
				r.setEditore(ma.getEditors().elementAt(0));
			String numeroStandard=ma.getStandardNumber();
			if(numeroStandard!=null) {
				numeroStandard=numeroStandard.replaceAll("ISBN", "").trim();
				r.setNumerostandard(numeroStandard);
			}
			r.setTestoabstract(ma.getAbstract());
			r.setNote(ma.getComments());
			r.setCopertina(ma.getBase64Image());
			if(ma.getSubjects()!=null && ma.getSubjects().size()>0)
				r.setAreadisciplinare((ma.getSubjects().elementAt(0)).toString());
			
			String prezzo=ma.getAvailabilityAndOrPrice();
			r.setPrezzo(prezzo);
		
			r.setJid(ma.getJOpacID());
			r.setBid(ma.getBid());
			
			try {
				r.setXML(ma.toXML());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		
		public static RecordInterface setRecord(RecordInfo recordInfo, String type) {
			RecordInterface ma=null;
			try {
				ma=RecordFactory.buildRecord("0", null, type, 0);
				ma.setJOpacID(recordInfo.getJid());
				ma.setTitle(recordInfo.getTitolo());
				ma.addAuthorsFromTitle();
				String areaDisciplinare=recordInfo.getAreadisciplinare();
				if(areaDisciplinare!=null && areaDisciplinare.trim().length()>0) {
					UncontrolledSubjectTerms s=new UncontrolledSubjectTerms(UncontrolledSubjectTerms.LEVEL_UNSPECIFIED);
					s.setSubjectData(areaDisciplinare);
					ma.addSubject(s);
				}
				
				
				
	//			recordInfo.getBibliografia();
				String collana=recordInfo.getCollana();
				if(collana!=null && collana.trim().length()>0) {
					RecordInterface c=RecordFactory.buildRecord("0", null, type, 0);
					c.setTitle(collana);
					c.setBiblioLevel(Unimarc.LEVEL_COLLECTION);
					ma.addSerie(c);
				}
				
				ma.setDescription(recordInfo.getCollazione());
				ma.setBase64Image(recordInfo.getCopertina());
			
				ma.addPublisher(recordInfo.getEditore());
	
				ma.setEdition(recordInfo.getEdizione());
				ma.addComment(recordInfo.getNote());
				ma.setStandardNumber(recordInfo.getNumerostandard(),"ISBN");
				
				ma.setStandardNumber(recordInfo.getOpac(), "OPAC"); // bidsbn
				ma.setStandardNumber(recordInfo.getDspace(), "DSPACE"); // handle OpenstarTS
				ma.setAvailabilityAndOrPrice(recordInfo.getPrezzo());
	//			recordInfo.getSintesiposseduto();
				ma.setAbstract(recordInfo.getTestoabstract());
				
				ma.setJOpacID(recordInfo.getJid());
				ma.setBid(recordInfo.getBid());
				
//				Connection conn=null;
//				try {
//					conn=RecordFinderServlet.CreaConnessione();
//					RecordInterface maPrev=engine.getNotiziaByJID(ma.getJOpacID());
//					if(maPrev!=null) System.out.println(maPrev.toReadableString());
//					System.out.println(ma.toReadableString());
//					engine.deleteRecordFromJid(ma.getJOpacID());
//					engine.insertRecord(stemmer, ma, ma.getJOpacID());
//				}
//				catch(Exception e) {
//					e.printStackTrace();
//				}
//				finally {
//					if(conn!=null) conn.close();
//				}
				
				
//				System.out.println(ma.toReadableString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ma;
		}
}

