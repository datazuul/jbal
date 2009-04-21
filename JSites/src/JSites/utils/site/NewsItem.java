package JSites.utils.site;

public class NewsItem {
	private String startDate,endDate,insertionDate,pageName;
	long pid=0,cid=0;

	public NewsItem(long pid, String pageName, long cid, String insertionDate, String startDate, String endDate) {
		this.pid=pid;
		this.cid=cid;
		this.insertionDate=insertionDate;
		this.startDate=startDate;
		this.endDate=endDate;
		this.pageName=pageName;
	}
	
	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getInsertionDate() {
		return insertionDate;
	}

	public void setInsertionDate(String insertionDate) {
		this.insertionDate = insertionDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
}
