package org.jopac2.jbal.abstractStructure;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringEscapeUtils;

public class Field implements Cloneable {
	@Override
	public Object clone() throws CloneNotSupportedException {
		Field f=new Field(fieldCode, content, delimiter);
		return f;
	}

	private String fieldCode;
	private String delimiter=String.valueOf((char)0x1f);
	private String content;
	
	public Field(String fieldCode,String content) {
		this.fieldCode=fieldCode.trim();
		this.content=content;
	}
	
	public Field(String fieldCode,String content, String delimiter) {
		this.fieldCode=fieldCode.trim();
		this.content=content;
		this.delimiter=delimiter;
	}
	
	public String getFieldCode() {
		return fieldCode;
	}
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode.trim();
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String toString() {
//		return delimiter+fieldCode+content.replaceAll("\n", "");
		return (new Delimiters()).getDl()+fieldCode+content.replaceAll("\n", "");
	}
	
	public static String printableNSBNSE(String string) {
		String nse=String.valueOf((char)0x1b)+"I";
		String nsb=String.valueOf((char)0x1b)+"H";
		string = string.replaceAll(nsb, "").replaceAll(nse, "*");
		
		byte[] _nsb = {-62, -120};
		byte[] _nse = {-62, -119};
		
		try {
			nsb = new String(_nsb,"utf8");
			nse = new String(_nse,"utf8");
			
		} catch (UnsupportedEncodingException e) {
		}
		string = string.replaceAll(nsb, "").replaceAll(nse, "*");
		return string;
	}

	public String toXML() {
		String r="<subfield code=\"";
		r=r+fieldCode;
		r=r+"\">"+StringEscapeUtils.escapeXml(content.trim());
		r=r+"</subfield>";
		return r;
	}

	public static String getContent(Field field) {
		if(field!=null) return field.getContent();
		else return "";
	}
}
