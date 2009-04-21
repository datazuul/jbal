package JSites.utils.site;

import JSites.utils.HtmlCodec;

public class Table extends Component {

	public String title = "";
	public String description = "";
	public int rows = 0;
	public int columns = 0;
	public String body = "";
	
	public String XMLSerialize() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><table><title>" + HtmlCodec.encode(title) + "</title>" +
			   "<description>" + HtmlCodec.encode(description) + "</description><rows>" + rows + "</rows><columns>" + columns + "</columns>" + 
			   body + "</table>";
	}

}
