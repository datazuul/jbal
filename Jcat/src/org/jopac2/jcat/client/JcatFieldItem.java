package org.jopac2.jcat.client;

public class JcatFieldItem {
	private String type,name,hint,element;
	private String[] syntax;
	
	public JcatFieldItem(String type, String name, String hint, String elementType, String[] syntax) {
		this.type = type;
		this.name = name;
		this.hint = hint;
		this.element=elementType;
		this.setSyntax(syntax);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public void setSyntax(String[] syntax) {
		this.syntax = syntax;
	}

	public String[] getSyntax() {
		return syntax;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String getElement() {
		return element;
	}
}
