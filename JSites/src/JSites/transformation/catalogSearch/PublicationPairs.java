package JSites.transformation.catalogSearch;

import java.sql.Connection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.jbal.iso2709.Eutmarc;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


import JSites.transformation.MyAbstractPageTransformer;

public class PublicationPairs extends MyAbstractPageTransformer {

	private boolean started;
	StringBuffer sb = new StringBuffer();

	public void startElement(String uri, String loc, String raw, Attributes a) throws SAXException {
		
		if(loc.equals("list"))
			started = true;

		super.startElement(uri, loc, raw, a);
	}
	
	public void characters(char[] c, int start, int len) throws SAXException {
		if(started)
			sb.append(c,start,len);
		else
			super.characters(c, start, len);
	}
	
	@Override
	public void endElement(String uri, String loc, String raw) throws SAXException {
		if(loc.equals("list")){
			started = false;
			String publist = sb.toString().trim();
			sb.delete(0, sb.length());
			
			StringTokenizer st = new StringTokenizer(publist, ",");
			String a=""; String b = "";
			while(st.hasMoreTokens()){
				a = st.nextToken();
				if(st.hasMoreElements()) b = st.nextToken();
				throwBox(a,b);
				a = "";
				b = "";
			}
		}
		super.endElement(uri, loc, raw);
	}
	
	

	private void throwBox(String a, String b) {
		int an=-1; int bn=-1;
		if(a.matches("\\d+"))
			an = Integer.parseInt(a);
		if(b.matches("\\d+"))
			bn = Integer.parseInt(b);
		try{
			Connection conn = getConnection("dbeutmarc");
			
			super.startElement("", "box", "box", emptyAttrs);
			
			super.startElement("", "unit1", "unit1", emptyAttrs);
			
			Eutmarc e1 = (Eutmarc) DbGateway.getNotiziaByJID(conn, an);
			
			throwField("img1", saveImgFile(e1));
			throwField("titolo1", e1.getTitle());
			throwField("testo1", getTestoFrom(e1));

			super.endElement("", "unit1", "unit1");
//			___________________________________
			
			if(bn>-1){
				super.startElement("", "unit2", "unit2", emptyAttrs);
				
				Eutmarc e2 = (Eutmarc) DbGateway.getNotiziaByJID(conn, bn);
				throwField("img2", saveImgFile(e2));
				throwField("titolo2", e2.getTitle());
				throwField("testo2", getTestoFrom(e2));
				
				super.endElement("", "unit2", "unit2");
			}
			
			super.endElement("", "box", "box");
			
		}catch(Exception ex){ex.printStackTrace();}
		
	}

	private String getTestoFrom(Eutmarc e1) {
		String testo1= "";
		
		if(!e1.getAuthors().isEmpty()){
			testo1 += "__autore__ ";
			Iterator<String> iteraut = e1.getAuthors().iterator();
			while(iteraut.hasNext()){
				String aut = iteraut.next().trim();
				testo1 += aut + "; "; 
			}
			testo1 = testo1.trim();
			while(testo1.endsWith(";")){
				testo1 = testo1.substring(0,testo1.length()-1);
			}
			testo1 = testo1 + "\n";
		}
		
		String isbn = e1.getStandardNumber();
		if(isbn != null && isbn.length()>0)
			testo1 += "__isbn__ " + e1.getStandardNumber() + "\n";
		
		testo1 += "__anno__ " + e1.getPublicationDate() + "\n";
		testo1 += "__abstract__ " + e1.getAbstract() + "\n";
		testo1 += "__prezzo__ " + e1.getPrezzo() +  "� ([orderAddItem?JID="+e1.getJOpacID()+">Acquista])";
		testo1 += "\n\n[pageview?pid=7&query=jid="+e1.getJOpacID()+">__Dettagli__]";
		String link = e1.getLink();
		if(link!=null && link.length() > 0){
			testo1 += "\n\n[" + link +">Full-Text disponibile]"; 
		}
		
		return testo1;
	}
	
}
