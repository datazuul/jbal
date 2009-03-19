package org.jopac2.jbal.abstractStructure;

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
}
