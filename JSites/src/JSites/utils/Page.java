package JSites.utils;

import java.sql.Date;

public class Page {
	private int pid,papid;
	private boolean valid,haschild,insidebar;
	private String name,pcode,username,remoteip,resp;
	private Date insertdate;
	
	public Page(int pid, int papid, 
			String name, String pcode, 
			boolean valid, boolean haschild,boolean insidebar,
			String username, String remoteip, String resp, Date insertdate) {
			
		super();
		this.pid = pid;
		this.papid = papid;
		this.valid = valid;
		this.haschild = haschild;
		this.insidebar = insidebar;
		this.name = name;
		this.pcode = pcode;
		this.username = username;
		this.remoteip = remoteip;
		this.resp = resp;
		this.insertdate = insertdate;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getPapid() {
		return papid;
	}
	public void setPapid(int papid) {
		this.papid = papid;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public boolean isHaschild() {
		return haschild;
	}
	public void setHaschild(boolean haschild) {
		this.haschild = haschild;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRemoteip() {
		return remoteip;
	}
	public void setRemoteip(String remoteip) {
		this.remoteip = remoteip;
	}
	public String getResp() {
		return resp;
	}
	public void setResp(String resp) {
		this.resp = resp;
	}
	public Date getInsertdate() {
		return insertdate;
	}
	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}
	public void setInsidebar(boolean insidebar) {
		this.insidebar = insidebar;
	}
	public boolean isInsidebar() {
		return insidebar;
	}
}
