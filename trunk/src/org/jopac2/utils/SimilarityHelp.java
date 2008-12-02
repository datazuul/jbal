package org.jopac2.utils;

import java.util.StringTokenizer;
import java.util.Vector;

import org.jopac2.jbal.RecordInterface;

public class SimilarityHelp {
	public static float weightedSimilarity(RecordInterface ma, RecordInterface mb) {
		float r=1;float t=0;
		r*=intersectionFactor(ma.getISBD(),mb.getISBD());
		
		
		t=intersectionFactor(cleanUncinate(ma.getAuthors()),cleanUncinate(mb.getAuthors()));
		if(t!=-1) r*=t;
		t=intersectionFactor(ma.getPublicationDate(),mb.getPublicationDate());
		if(t!=-1) r*=t;
		t=intersectionFactor(ma.getPublicationPlace(),mb.getPublicationPlace());
		if(t!=-1) r*=t;
		t=intersectionFactor(ma.getStandardNumber(),mb.getStandardNumber());
		if(t!=-1) r*=t;
		return r;
	}
	
	private static Vector<String> cleanUncinate(Vector<String> authors) {
		Vector<String> v=new Vector<String>();
		for(int i=0;authors!=null && i < authors.size();i++) {
			String c=authors.elementAt(i)+" ";
			int s=c.indexOf("<");
			int e=c.indexOf(">");
			if(s>=0 && e>s) {
				c=c.substring(0,s)+c.substring(e+1);
				v.addElement(c);
			}
			else {
				v.addElement(authors.elementAt(i));
			}
				
		}
		return v;
	}

	public static float intersectionFactor(Vector<String> va, Vector<String> vb) {
		StringBuffer sa=new StringBuffer();
		StringBuffer sb=new StringBuffer();
		
		for(int i=0;i<va.size();i++) sa.append(va.elementAt(i)+" ");
		for(int i=0;i<vb.size();i++) sb.append(vb.elementAt(i)+" ");
		return intersectionFactor(sa.toString(),sb.toString());
	}
	
	public static float intersectionFactor(String a, String b) {
		int tot=1,common=1;
		if((a==null)||(b==null)||(a.length()==0)||(b.length()==0)) {
			return -1;
		}
		else {
			StringTokenizer tka=new StringTokenizer(JbalHelper.processaMarcatori(a),JbalHelper.SEPARATORI_PAROLE);
			StringTokenizer tkb=new StringTokenizer(JbalHelper.processaMarcatori(b),JbalHelper.SEPARATORI_PAROLE);
			
			Vector<String> ta=new Vector<String>();
			Vector<String> tb=new Vector<String>();
			
			while(tka.hasMoreTokens()) ta.addElement(tka.nextToken().toLowerCase());
			while(tkb.hasMoreTokens()) tb.addElement(tkb.nextToken().toLowerCase());
	
			common=commonWords(ta,tb);
			tot=ta.size()+tb.size()-common;
			
			ta.clear();ta=null;
			tb.clear();tb=null;
		}
		return common==0?0:(float)common/tot;
	}

	private static int commonWords(Vector<String> ta, Vector<String> tb) {
		int c=0;
		for(int i=0;i<ta.size();i++) {
			if(tb.contains(ta.elementAt(i))) c++;
		}
		return c;
	}
}
