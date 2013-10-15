package JSites.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.w3c.tidy.Tidy;

public class JTidy {

	
	public static String jtidyNoOfficeNamespace(String temp) {
		String r=jtidy(temp);
		String xmldecPattern="<\\?[\\s\\S]*\\?>";
		String oPatternOpen="<o:[\\S]*>";
		String oPatternClose="</o:[\\S]*>";
		r=r.replaceAll(xmldecPattern+"|"+oPatternOpen+"|"+oPatternClose, "");
		return r;
	}
	
	public static String jtidy(String temp) {
		String r="";
		Tidy tidy = new Tidy(); // obtain a new Tidy instance
		tidy.setXHTML(false);
		tidy.setWord2000(true);
		tidy.setDropProprietaryAttributes(true);
		tidy.setMakeClean(true);
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
