package JSites.transformation.catalogSearch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

public class Limit implements TextFunction {

	public String parse(Document doc, String in, String... args) throws Exception {
			String temp="";
			try {
				temp=Templator.parseContext(doc, "{{"+args[0]+":"+in+"}}");
			}
			catch(Exception e) {
				temp=args[0];
			}
			int length=temp.length();
			int limit=Integer.MAX_VALUE;
			
			try {
				limit=Integer.parseInt(args[1]);
			}
			catch(Exception e) {}
			
			if(limit<length) {
				int u=temp.lastIndexOf(" ",limit);
				temp=temp.substring(0,u);
				if(args.length>2) {
					String t2=Templator.parseContext(doc, "{{/:"+args[2]+"}}");
					temp=temp+t2;
				}
			}
			
			
			return jtidy(temp);
	}

	private String jtidy(String temp) {
		String r="";
		Tidy tidy = new Tidy(); // obtain a new Tidy instance
		tidy.setXHTML(false);
		tidy.setPrintBodyOnly(true);
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		tidy.setQuoteMarks(true);
		tidy.setOutputEncoding("utf-8");
		tidy.setInputEncoding("utf-8");
		tidy.setXmlOut(true); // set desired config options using tidy setters 
		                          // (equivalent to command line options)
		tidy.setXmlPi(false);
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try {
			tidy.parse(new ByteArrayInputStream(temp.getBytes("utf-8")), baos);
			r=baos.toString("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} // run tidy, providing an input and output stream
		return r;
	}
	
}
