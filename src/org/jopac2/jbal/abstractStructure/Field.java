package org.jopac2.jbal.abstractStructure;

public class Field {
	private String fieldCode;
	public static String delimiter=String.valueOf((char)0x1f);
	private String content;
	
	public Field(String fieldCode,String content) {
		this.fieldCode=fieldCode;
		this.content=content;
		
	}
	
	public String getFieldCode() {
		return fieldCode;
	}
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		Field.delimiter = delimiter;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
