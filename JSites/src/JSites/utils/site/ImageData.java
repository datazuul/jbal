package JSites.utils.site;
/*******************************************************************************
*
*  JOpac2 (C) 2002-2009 JOpac2 project
*
*     This file is part of JOpac2. http://www.jopac2.org
*
*  JOpac2 is free software; you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation; either version 2 of the License, or
*  (at your option) any later version.
*
*  JOpac2 is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with JOpac2; if not, write to the Free Software
*  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*
*  Please, see NOTICE.txt AND LEGAL directory for more info. Different licences
*  may apply for components included in JOpac2.
*
*******************************************************************************/
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
