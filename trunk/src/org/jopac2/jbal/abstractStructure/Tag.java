package org.jopac2.jbal.abstractStructure;

import java.util.StringTokenizer;
import java.util.Vector;

public class Tag {
	String tagName;
	char modifier1,modifier2;
	Vector<Field> fields=null;
	
	public Tag(String tagName, char modifier1, char modifier2) {
		this.tagName=tagName;
		this.modifier1=modifier1;
		this.modifier2=modifier2;
	}
	
	public Tag(String tagString) {
		this.tagName=tagString.substring(0,3);
		modifier1=tagString.charAt(3);
		modifier2=tagString.charAt(4);
		tagString=tagString.substring(4);
		if(fields==null) fields=new Vector<Field>();
		
		String ctk;
	    StringTokenizer tk=new StringTokenizer(tagString,Field.delimiter);
	    while(tk.hasMoreTokens()) {
	      ctk=tk.nextToken();
	      String element=ctk.substring(0,1).trim();
	      String content=ctk.substring(1);
	      if(content.length()>0) fields.addElement(new Field(element,content));
	    }
	}
	
	public void addField(Field field) {
		if(fields==null) fields=new Vector<Field>();
		fields.add(field);
	}
	
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public char getModifier1() {
		return modifier1;
	}
	public void setModifier1(char modifier1) {
		this.modifier1 = modifier1;
	}
	public char getModifier2() {
		return modifier2;
	}
	public void setModifier2(char modifier2) {
		this.modifier2 = modifier2;
	}
	public Vector<Field> getFields() {
		return fields;
	}
	
	public String toString() {
		String r=tagName+modifier1+modifier2;
		for(int i=0;fields!=null && i<fields.size();i++) {
			r+=fields.elementAt(i).getDelimiter()+fields.elementAt(i).getFieldCode()+fields.elementAt(i).getContent();
		}
		return r;
	}

	public void checkNSBNSE(String nsb, String nse) {
		for(int i=0;fields!=null && i<fields.size();i++) {
			String content=fields.elementAt(i).getContent();
			int  insb=content.indexOf(nsb);
			int  inse=content.indexOf(nse);
			int cp=0;
			while(inse>=0) {
				if(insb>inse || insb==-1) {
					// rimuovi nse
					content=content.substring(0,inse)+content.substring(inse+nse.length());
				}
				else {
					cp=inse+1;
				}
				insb=content.indexOf(nsb,cp);
				inse=content.indexOf(nse,cp);
			}
			fields.elementAt(i).setContent(content.trim());
		}
	}

	public void checkNSBNSE() {
		String nse=String.valueOf((char)0x1b)+"I";
		String nsb=String.valueOf((char)0x1b)+"H";
		checkNSBNSE(nsb,nse);
	}
}
