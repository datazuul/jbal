package org.jopac2.jcat.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jopac2.jbal.RecordInterface;

public class RecordBuilder {
	RecordInterface buildRecord(Map<String,String> parameters) {
		EutMarc ma=new EutMarc();
		Iterator<String> i=parameters.keySet().iterator();
		String k=null;
		String v=null;
		while(i.hasNext()) {
			k=i.next();
			v=parameters.get(k);
			if(k.equalsIgnoreCase("titolo")) {
				ma.setTitle(v);
			}
		}
		return ma;
	}
}
