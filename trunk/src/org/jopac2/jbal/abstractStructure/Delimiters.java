package org.jopac2.jbal.abstractStructure;

public class Delimiters {
	private String rt=String.valueOf((char)0x1d);
	private String ft=String.valueOf((char)0x1e);
	private String dl=String.valueOf((char)0x1f);
	
	public Delimiters(String rt, String ft, String dl) {
		this.dl=dl;
		this.ft=ft;
		this.rt=rt;
	}
	
	public String getRt() {
		return rt;
	}
	public void setRt(String rt) {
		this.rt = rt;
	}
	public String getFt() {
		return ft;
	}
	public void setFt(String ft) {
		this.ft = ft;
	}
	public String getDl() {
		return dl;
	}
	public void setDl(String dl) {
		this.dl = dl;
	}
  
}
