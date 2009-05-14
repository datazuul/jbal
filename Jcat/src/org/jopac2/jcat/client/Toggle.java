package org.jopac2.jcat.client;

public class Toggle {
	private String status="M";
	
	public String getStatus() {
		return status;
	}
	
	public void toggle() {
		if(status.startsWith("M")) status="S";
		else status="M";
	}
	
	public String toString() {
		return status;
	}
}
