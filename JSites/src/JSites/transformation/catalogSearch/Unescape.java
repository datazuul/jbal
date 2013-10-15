package JSites.transformation.catalogSearch;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;

public class Unescape implements TextFunction {

	@Override
	public String parse(Document doc, String in, boolean escape, String... args)
			throws Exception {
		String r="";
		try {
			r=Templator.parseContext(doc, "{{"+args[0]+":"+in+"}}",escape);
			r=StringEscapeUtils.unescapeXml(r);
		}
		catch(Exception e) {
			r=args[0];
		}
		return r;
	}

}
