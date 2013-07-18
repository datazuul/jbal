package org.jopac2.jbal.abstractStructure;

public class Delimiters {
	public static byte standardRt=0x1d;
	public static byte standardFt=0x1e;
	public static byte standardDl=0x1f;        //' delimiter
	
	private byte rt=0x1d;
	private byte ft=0x1e;
	private byte dl=0x1f;        //' delimiter
	
	public Delimiters(byte rt, byte ft, byte dl) {
		this.dl=dl;
		this.ft=ft;
		this.rt=rt;
	}
	
	public Delimiters() {
		// default values
	}
	
	public byte getByteRt() {
		return rt;
	}
	
	public byte getByteFt() {
		return ft;
	}
	
	public byte getByteDl() {
		return dl;
	}
	
	public String getRt() {
		return String.valueOf((char)rt);
	}
	public void setRt(byte rt) {
		this.rt = rt;
	}
	public String getFt() {
		return String.valueOf((char)ft);
	}
	public void setFt(byte ft) {
		this.ft = ft;
	}
	public String getDl() {
		return String.valueOf((char)dl);
	}
	public void setDl(byte dl) {
		this.dl = dl;
	}

	@Override
	public String toString() {
		return getFt()+getDl()+getRt();
	}
	
	
  
}
