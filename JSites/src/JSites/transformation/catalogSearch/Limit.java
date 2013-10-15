package JSites.transformation.catalogSearch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import JSites.utils.JTidy;

public class Limit implements TextFunction {

	public String parse(Document doc, String in, boolean escape, String... args) throws Exception {
			String temp="";
			try {
				temp=Templator.parseContext(doc, "{{"+args[0]+":"+in+"}}",escape);
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
					String t2=Templator.parseContext(doc, "{{/:"+args[2]+"}}",escape);
					temp=temp+t2;
				}
			}
			
			String r=JTidy.jtidy(temp).replaceAll("\\p{C}", " ");

			return r;
	}


	
}
