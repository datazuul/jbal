package JSites.transformation;

import java.io.IOException;
import java.util.*;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.apache.cocoon.environment.*;


public class ImageFilter extends AbstractTransformer {
	
	private long maxsize;
	private int minwidth;
	private int maxwidth;
	private int minheight;
	private int maxheight;
	private AttributesImpl emptyAttr = new AttributesImpl();
	boolean flag = false;
	@SuppressWarnings("unchecked")
	Map map;
	@SuppressWarnings("unchecked")
	Map objectModel;

	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		
        Request request = ObjectModelHelper.getRequest(arg1);
        
    	maxsize = 0;
    	minwidth = 1;
    	maxwidth = 0;
    	minheight = 1;
    	maxheight = 0;

        Enumeration<String> parameters = request.getParameterNames();
        if ( parameters != null ) {
            while (parameters.hasMoreElements()) {
                String name = (String) parameters.nextElement();
                if (name.equals("maxsize")) maxsize =  Long.parseLong(request.getParameter(name));
                else if (name.equals("minwidth")) minwidth =  Integer.parseInt(request.getParameter(name));
                else if (name.equals("maxwidth")) maxwidth =  Integer.parseInt(request.getParameter(name));
                else if (name.equals("minheight")) minheight =  Integer.parseInt(request.getParameter(name));
                else if (name.equals("maxheight")) maxheight =  Integer.parseInt(request.getParameter(name));
            }
        }
	}
	
	public void startDocument() throws SAXException {
		super.startDocument();
		AttributesImpl emptyAttr = new AttributesImpl();
		super.startElement("","images","images",emptyAttr);
	}
	
	public void endDocument() throws SAXException  {
		
		super.startElement("","image","image",emptyAttr);
		super.characters("no_image".toCharArray(),0,8);
		super.endElement("","image","image");
		
		super.endElement("","images","images");
		super.endDocument();
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals("dir:file")) {
			
			String temp;
			int width;
			int height;
			long size;
			
			temp = attributes.getValue("width");
			if (temp != null) width = Integer.parseInt(temp);
			else return;
			temp = attributes.getValue("height");
			if (temp != null) height = Integer.parseInt(temp);
			else return;
			temp = attributes.getValue("size");
			if (temp != null) size = Long.parseLong(temp);
			else return;
			if ((maxsize >= size) || (maxsize == 0)) {
				if ((minwidth <= width) && (minheight <= height)) {
					if ((maxwidth >= width) || (maxwidth == 0)) {
						if ((maxheight >= height) || (maxheight == 0)) {
							String filename = attributes.getValue("name");
							super.startElement("","image","image",emptyAttr);
							super.characters(filename.toCharArray(),0,filename.length());
							flag = true;
						}
					}
				}
			}
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if ((qName.equals("dir:file")) && (flag==true)) {
			super.endElement("", "image", "image");
			flag = false;
		}
	}



}
