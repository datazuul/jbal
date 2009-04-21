package JSites.utils.site;

public class ImageData {
	private String url=null,
		type=null,
		description=null,
		startDate=null,
		endDate=null,
		name=null;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void recycle() {
		url=null;
		type=null;
		description=null;
		startDate=null;
		endDate=null;
		name=null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setup(String imageLine) {
		if(!imageLine.trim().startsWith("#")&&imageLine.trim().length()>0) {
			String[] imgData=imageLine.split(":::");
			if(imgData.length>2) {
				recycle();
				String[] periodo=imgData[0].split("-");
				startDate=periodo[0];
				if(periodo.length>1) {
					endDate=periodo[1];
				}
				else {
					endDate=periodo[0];
				}
			}
			url=imgData[1];
			type=url.substring(url.lastIndexOf('.')+1);
			name=imgData[2];
			if(imgData.length>3) description=imgData[3];
		}
	}

	public boolean isValidWithDate(String reqDate) {
		boolean valid=false;
		if(startDate!=null&&startDate.startsWith("0000")) 
			reqDate="0000"+reqDate.substring(4);
		if(startDate!=null &&
				endDate!=null &&
				reqDate.compareTo(startDate)>=0 && 
				reqDate.compareTo(endDate)<=0) valid=true;
		return valid;
	}
}
