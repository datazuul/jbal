package JSites.transformation;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CheckContentLenghtTransformer extends MyAbstractPageTransformer {

	private String componentType = "unknown";
	private boolean alert = false;
	private StringBuffer readbuffer;
	private int sectionTextLimit = 100;
	
	@Override
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		super.setup(arg0, arg1, arg2, arg3);
		componentType = o.getParameter("type");		
		readbuffer = new StringBuffer();
		alert = false;
	}

	@Override
	public void startElement(String uri, String loc, String raw, Attributes a) throws SAXException {
		super.startElement(uri, loc, raw, a);
		if(componentType.equals("section") && loc.equals("text"))
			alert = true;
	}

	@Override
	public void endElement(String uri, String loc, String raw) throws SAXException {
		if(alert) {
			check();
			readbuffer.delete(0,readbuffer.length());
			alert = false;
		}
		super.endElement(uri, loc, raw);
	}


	public void characters(char[] c, int s, int e) throws SAXException{
		if(!alert) super.characters(c, s, e);
		else readbuffer.append(c,s,e);
	}

	private void check() throws SAXException { //<.*?>
		
		String temp = readbuffer.toString();
		
//		if(componentType.equals("section")){
//			while(temp.replaceAll("<.*?>", "").replaceAll("&.*?;", "").length() > sectionTextLimit){
//				temp = sbuccia(temp);
//			}
//		}
		
		super.characters(temp.toCharArray(), 0, temp.length());
		
	}

	private String sbuccia(String temp) {
		String[] ss = temp.replaceAll("&#.*;", "").replaceAll("\n", "").split("<.*?>");
		String oldS = ss[ss.length-1];
		String newS = ss[ss.length-1].substring(0,oldS.length()-1);
		
		int index = temp.lastIndexOf(oldS);
		
		temp = temp.substring(0,index) + temp.substring(index).replace(oldS, newS);
		
		return temp;
		
	}
	
	

}
