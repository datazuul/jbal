package JSites.utils.site;

import java.util.Date;

public class News extends Component {

	public String title = "";
	public String content = "";
	public Date start = null;
	public Date end = null;
	public int daysOfValidity = 1;
	
	@Override
	public String XMLSerialize() {
		return "";
	}

}
