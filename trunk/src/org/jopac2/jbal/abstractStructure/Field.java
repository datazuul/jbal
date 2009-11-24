package org.jopac2.jbal.abstractStructure;

import java.io.UnsupportedEncodingException;

public class Field {
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
		return delimiter+fieldCode+content;
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
}
