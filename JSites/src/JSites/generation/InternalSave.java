package JSites.generation;

import java.io.IOException;
import java.util.*;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.servlet.multipart.Part;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.SAXException;


public class InternalSave extends MyAbstractPageGenerator {

	private Part part = null;
	private boolean newPage = false;
	
	@SuppressWarnings("unchecked")
	public void generate() throws SAXException {
		
		String key = "";
		String value = "";
		
		AttributesImpl saving_attrs = new AttributesImpl();
		
		boolean loadFromFile = request.getParameter("upload-file") != null;
		saving_attrs.addCDATAAttribute("loadFromFile", String.valueOf(loadFromFile));
		
		contentHandler.startDocument();
		contentHandler.startElement("","save","save",saving_attrs);
		
		String title_type = request.getParameter("title_type");
		title_type = title_type == null ? "2" : title_type; 
		
		Enumeration<String> en = request.getParameterNames();
		while(en.hasMoreElements()){
			key = (String)en.nextElement();
			value = request.getParameter(key);
			
			key = key.replaceAll("\n","").replaceAll("\t","").replaceAll("\r","");
			value = value.replaceAll("\r\n", "\n").replaceAll("\n\r","\n");
			if(value.equals(""))value=" ";
			AttributesImpl attr = new AttributesImpl();
			if(key.equals("title")){
				if(newPage)
					attr.addCDATAAttribute("type","1");
				else
					attr.addCDATAAttribute("type",title_type);
			}
			
			contentHandler.startElement("",key,key,attr);
			contentHandler.characters(value.toCharArray(),0,value.length());
			contentHandler.endElement("",key,key);
		}
		
		try {
			part = (Part) request.get("upload-file");
			AttributesImpl attr = new AttributesImpl();
			
			value=part.getFileName();
			key="filename";
	
	        
	        contentHandler.startElement("",key,key,attr);
			contentHandler.characters(value.toCharArray(),0,value.length());
			contentHandler.endElement("",key,key);
		}
		catch(Exception e) {
			// Non fa nulla, il codice sopra vale solo per gli upload
		}

		contentHandler.endElement("","save","save");
		
		contentHandler.endDocument();
		
	}
	
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) throws ProcessingException, SAXException, IOException{
		
		super.setup(resolver, objectModel, src, par);

    	if(session!=null){
    		newPage = session.getAttribute("newPageID") != null;
    		session.removeAttribute("newPageID");
    		session.removeAttribute("newPacid");
    		
    	}
    	
	}

}
