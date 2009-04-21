package JSites.transformation;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Stack;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import JSites.utils.HtmlCodec;

public class WikiTransformer3 extends AbstractTransformer{
	
	private StringBuffer readbuffer;
	private final OutputNode paragraph=new OutputNode("br",new AttributesImpl());
	private final OutputNode img=new OutputNode("img",new AttributesImpl());
	private final OutputNode ahref=new OutputNode("a",new AttributesImpl());
	private final OutputNode general=new OutputNode("general",new AttributesImpl());
	//boolean delayed=false;
	public boolean DEBUG=false;
	Stack<OutputNode> nodes=new Stack<OutputNode>();
	public StringBuffer DEBUG_RET=new StringBuffer();
	
	public boolean isHtml=false;
	boolean xmlParsing = false;
	
	boolean wiki = true;
	Stack<Integer> wikiChange = new Stack<Integer>();
	int xmlDepth = 0;
	
	@Override
	public void endDocument() throws SAXException {
		if(!xmlParsing)
			super.endDocument();
		else
			xmlParsing = false;
	}

	@Override
	public void startDocument() throws SAXException {
		if(!xmlParsing)
			super.startDocument();
	}

	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		readbuffer = new StringBuffer(10240);
		isHtml = false;
		xmlParsing = false;
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
		xmlDepth++;
		if(localName.equals("futiz"))return;
		if(isHtml) {
			super.startElement(namespaceURI, localName, qName, attributes);
		}
		else {
			doFinal(readbuffer);
			readbuffer.delete(0,readbuffer.length());
			if(localName.equals("ul") || localName.equals("ol"))
				super.startElement(namespaceURI, localName, qName, new AttributesImpl());
			else
				super.startElement(namespaceURI, localName, qName, attributes);
		}
		
		String wikiS = attributes.getValue("wiki");
		if(wikiS != null && wikiS.length()>0){
			boolean newwiki = Boolean.parseBoolean(wikiS);
			if(newwiki != wiki){
				wikiChange.push(xmlDepth);
				wiki = !wiki;
			}
				
		}
		
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if(localName.equals("futiz"))return;
		if(!isHtml) {
			doFinal(readbuffer);
			readbuffer.delete(0,readbuffer.length());
		}
		
		if(!wikiChange.empty() && wikiChange.peek() == xmlDepth){
			wiki = !wiki;
			wikiChange.pop();
		}
		xmlDepth--;
		super.endElement(namespaceURI, localName, qName);
	}

	
	public void characters(char[] c, int s, int e) throws SAXException{
		
		if(!wiki){
			super.characters(c, s, e);
			//wiki=true;
			return;
		}
		
		if(isHtml) super.characters(c, s, e);
		else readbuffer.append(c,s,e);
	}
	
	
	public void doFinal(StringBuffer s) throws SAXException {
		if(s.length()>0 && s.charAt(0)=='<') {
			isHtml=true;
			try {
				doFinalHtml(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
			isHtml=false;
		}
		else {
			doFinalWiki(s);
		}
	}

	
	public void doFinalHtml(StringBuffer s) throws SAXException, IOException {

		xmlParsing = true;
		
		XMLReader rdr = XMLReaderFactory.createXMLReader( "org.apache.xerces.parsers.SAXParser" );
        rdr.setContentHandler(this);
        
        //TODO la dichiarazione meglio se venisse salvata nel file xml cosi'
        // e' anche un buon pattern per riconoscere
        // la dichiarazione non va messa
        String temp = s.toString();
        
        StringReader sr= new StringReader("<futiz>"+temp+"</futiz>");
        
        // Il parser crea un nuovo documento XML, l'handler invocera' startDocument e endDocument
        // inoltre un documento XML deve avere un solo root element
        // la nostra stringa non � un document XML, ma un frammento XML, quindi una parte di un docuemnto
        // XML che non deve invocare start e endDocument, e puo' essere formato da piu' elementi.
        // Per risolvere questi problemi e' stata introdotta una variabile booleana per evitare di invocare 
        // start e endDocument in fase di parsing, ed e' stato aggiunto un tag <futiz> come root element
        // che non viene considerato dall'handler.
        
		InputSource i=new InputSource(sr);
        rdr.parse(i);
        
	}

	public void doFinalWiki(StringBuffer s) throws SAXException {		
		// Decode Html
		HtmlCodec.decode(s);
		int start=0,end=0,l=s.length();
		
		char[] chars=new char[l];
		s.getChars(0, l, chars, 0);
		
		//OutputNode p=paragraph.clone();
		//nodes.push(p);
		//startElement(p);
		
		for(int i=0; i<l; i++) {
			switch (chars[i]) {
				case '\\':	{// quote?
					if(i<l-1) {
						if(end>start) internalCharacters(chars, start, i, nodes);
						i++;
						end=i+1;start=i;
					}
					else { // do nothing
						end++;
					}
				}break;
				case '\'':	{// italics?
					if((i<l-1)&&(chars[i+1]=='\'')) {
						// dispatch other chars
						if(end>start) internalCharacters(chars, start, i, nodes);
						i++;
						end=i+1;start=i+1; // new characyers will be after match
						//check if starts or ends
						decideNode("i",nodes);
					}
					else { // do nothing
						end++;
					}
				}break;
				case '_': {// bold?
					if((i<l-1)&&(chars[i+1]=='_')) {
						i++;
						// dispatch other chars
						if(end>start) internalCharacters(chars, start, end, nodes);
						end=i+1;start=i+1; // new characters will be after match
						//check if starts or ends
						decideNode("b",nodes);
					}
					else {
						end++;
					}
				}break;
				/**
				 * 
				 * TODO: ---- == hr
				 */
				case '-': {// monospaced?
					if((i<l-1)&&(chars[i+1]=='-')) {
						i++;
						// dispatch other chars
						if(end>start) internalCharacters(chars, start, end, nodes);
						end=i+1;start=i+1; // new characters will be after match
						//check if starts or ends
						AttributesImpl attrs = new AttributesImpl();
						attrs.addCDATAAttribute("class","monospace");
						decideNode("font",attrs,nodes);
					}
					else {
						end++;
					}
				}break;
				case '\n': {// end of line
					if(end>start) internalCharacters(chars, start, end, nodes);
					end=i+1;start=i+1;
					if(!nodes.isEmpty()&&
							(nodes.peek().getNodeName().equals("li")
									)) { // if it's a item of a list
						endElement(nodes.pop());
					}
					else if(!nodes.isEmpty()&&(nodes.peek().getNodeName().equals("ul")||
							nodes.peek().getNodeName().equals("ol"))) {
						while(!nodes.isEmpty()&&(nodes.peek().getNodeName().equals("ul")||
								nodes.peek().getNodeName().equals("ol"))) {
							endElement(nodes.pop());
						}
					}
					else {
						OutputNode p1=paragraph.clone();
						//nodes.push(p1);
						startElement(p1);
						endElement(p1);
						/*if(paragraph.equals(nodes.peek())) {
							endElement(nodes.pop());
						}*/
					}
					
				}break;
				case '*': {// unordered list
					if(start==end) { // if at begin of row
						i=startLineList("ul",chars,i,nodes);
						start=i+1;end=i+1;
					}
					else {
						end++; // do nothing, it's a star
					}
				}break;
				case '#': {// ordered list
					if(start==end) { // if at begin of row
						i=startLineList("ol",chars,i,nodes);
						start=i+1;end=i+1;
					}
					else {
						end++; // do nothing, it's a dash
					}
				}break;
				case '[': {// external reference (page or image)
					OutputNode thisnode = null;
					if(!nodes.isEmpty()) thisnode=nodes.peek();
					if(thisnode!=null && thisnode.getNodeName().equals(ahref.getNodeName())) {
						nodes.pop();
						thisnode.setDelayed(false);
						startElement(thisnode);
						endElement(thisnode);
					}
					if(thisnode!=null&&thisnode.getNodeName().equals(general.getNodeName())) {
						String realname=thisnode.getAttributes().getValue("realname");
						if(realname==null) {
							thisnode.getAttributes().addCDATAAttribute("realname", new String(chars,start,i-start));
						}
						else {
							String par=new String(chars,start,i-start);
							int sep=par.indexOf('=');
							if(sep>0) thisnode.getAttributes().addCDATAAttribute(par.substring(0,sep), par.substring(sep+1));
							else thisnode.getAttributes().addCDATAAttribute(par, "");
						}
						//delayed=false;
						thisnode.setDelayed(false);
						startElement(thisnode);
						start=i;end=i;
					}
					
					if(end>start) internalCharacters(chars, start, end, nodes);
					OutputNode ref=null;
					if(chars[i+1]=='$') { // it's a form or a form element!
						ref=general.clone();
						i=i+1;
						ref.setDelayed(true);
						//delayed=true;
					}
					else if((i<l-4) && (chars[i+1]=='i') && (chars[i+2]=='m') && (chars[i+3]=='g') && (chars[i+4]==':')) { // it's a picture!!
						i=i+4;								
						ref=img.clone();
						ref.setDelayed(true);
					}
					else { // it's a page !!
						ref=ahref.clone();
						ref.setDelayed(true);
					}
					start=i+1;end=i+1;
					nodes.push(ref);
					startElement(ref);
				}break;
				
				case '>': {// external ref separator or form/field	
					OutputNode thisnode=null;
					if(!nodes.isEmpty()) thisnode=nodes.peek();
					if(img.equals(thisnode)) {
						thisnode.getAttributes().addCDATAAttribute("src", new String(chars,start,i-start));
						start=i+1;end=i+1;
					}
					else if(thisnode!=null&&thisnode.getNodeName().equals(ahref.getNodeName())) {
						if(chars[i+1]=='>') {
							thisnode.getAttributes().addCDATAAttribute("target", new String(chars,start,i-start));
							i++;
							start=i+1;end=i+1;
						}
						else {
							thisnode.getAttributes().addCDATAAttribute("href", (new String(chars,start,i-start)).replaceAll("&", "&amp;"));
							thisnode.setDelayed(false);
							//delayed=false;
							startElement(thisnode);
							start=i+1;end=i+1;
						}
					}
					else if(thisnode!=null&&thisnode.getNodeName().equals(general.getNodeName())) {
						String realname=thisnode.getAttributes().getValue("realname");
						if(realname==null) {
							thisnode.getAttributes().addCDATAAttribute("realname", new String(chars,start,i-start));
						}
						else {
							String par=new String(chars,start,i-start);
							int sep=par.indexOf('=');
							if(sep>0) thisnode.getAttributes().addCDATAAttribute(par.substring(0,sep), par.substring(sep+1));
							else thisnode.getAttributes().addCDATAAttribute(par, "");
						}
						if(chars[i+1]=='>') {
							i=i+1;
							thisnode.setDelayed(false);
							//delayed=false;
							startElement(thisnode);
						}
						start=i+1;end=i+1;
					}
					else {
						end++; // do nothing, it's a 'greater than'
					}
				}break;
				case ']': {// end of something?
					OutputNode thisnode=null;
					if(!nodes.isEmpty()) thisnode=nodes.peek();
					if(thisnode!=null&&thisnode.getNodeName().equals("img")) {
						thisnode.getAttributes().addCDATAAttribute("alt", new String(chars,start,i-start));
						thisnode.setDelayed(false);
						//delayed=false;
						nodes.pop();
						startElement(thisnode);
						endElement(thisnode);
						end=i+1;start=i+1;
					}
					else if(thisnode!=null&&thisnode.getNodeName().equals("a")) {
						String href=thisnode.getAttributes().getValue("href");
						String goOut=new String(chars,start,i-start);
						if(end>start) {
							if(href==null) thisnode.getAttributes().addCDATAAttribute("href", goOut);
							internalCharacters(chars, start, i, nodes);
						}
						nodes.pop();
						endElement(thisnode);
						start=i+1;end=i+1;
					}
					else if(thisnode!=null&&thisnode.getNodeName().equals("general")) {
						String realname=thisnode.getAttributes().getValue("realname");
						if(realname==null) {
							thisnode.getAttributes().addCDATAAttribute("realname", new String(chars,start,i-start));
						}
						else {
							String par=new String(chars,start,i-start);
							int sep=par.indexOf('=');
							if(sep>0) thisnode.getAttributes().addCDATAAttribute(par.substring(0,sep), par.substring(sep+1));
							else thisnode.getAttributes().addCDATAAttribute(par, "");
						}
						thisnode.setDelayed(false);
						//delayed=false;
						nodes.pop();
						startElement(thisnode);
						endElement(thisnode);
						start=i+1;end=i+1;
					}
					else if(thisnode!=null) {
						if(end>start) internalCharacters(chars, start, end, nodes);
						nodes.pop();
						endElement(thisnode);
						start=i+1;end=i+1;
					}
					else {
						end++; // do nothing, it's a closing square braket
					}
				}break;
				default:end++;
			}
		}
		
		// dispatch other chars
		if(end>start) internalCharacters(chars, start, end, nodes);
		
		// ends any open node
		while(!nodes.isEmpty()) {
			OutputNode out=nodes.pop();
			endElement(out);
		}
	}
	
	private int startLineList(String type, char[] chars, int i, Stack<OutputNode> nodes) throws SAXException {
		// count how many levels we want
		int level=0;
		char symbol=chars[i];
		while(chars[i]==symbol && i<chars.length) {
			i++;level++;
		}

		/*
		if(delayed) {
			if(paragraph.equals(nodes.peek())) nodes.pop();
			delayed=false;
		}
		*/
		
		OutputNode o=null;
		if(!nodes.isEmpty()) o=nodes.peek();
		if(o!=null && o.isDelayed()) {
			if(paragraph.equals(o)) nodes.pop();
			o.setDelayed(false);
			//delayed=false;
		}
		
		// get current position
		OutputNode weAre=null;
		if(!nodes.isEmpty()) weAre=nodes.peek();
		// define position we want
		AttributesImpl attrs=new AttributesImpl();
		attrs.addCDATAAttribute("level", Integer.toString(level));
		OutputNode weWant=new OutputNode(type,attrs);
		
		if(!weWant.equals(weAre)) { // position is not the same
			// remove inner levels
			while(weAre!=null && weAre.getNodeName().equals(type) && Integer.parseInt(weAre.getAttributes().getValue("level"))>level) {
				endElement(weAre);
				nodes.pop();
				if(!nodes.isEmpty()) weAre=nodes.peek();
				else weAre=null;
			}
			// OK, we need a new level?
			if(!weWant.equals(weAre)) {
				nodes.push(weWant);
				startElement(weWant);
			}
		}
		OutputNode li=new OutputNode("li", new AttributesImpl());
		nodes.push(li);
		startElement(li);
		
		return i-1;
	}

	private void internalCharacters(char[] chars, int start, int end, Stack<OutputNode> nodes) throws SAXException {
		OutputNode o=null;
		if(!nodes.isEmpty()) o=nodes.peek();
		if(o!=null && o.isDelayed()) {
			//if(paragraph.equals(nodes.peek()))
			if(DEBUG) DEBUG_RET.append(nodes.peek());
			else superStartElement("", nodes.peek().getNodeName(), nodes.peek().getNodeName(), nodes.peek().getAttributes());
			o.setDelayed(false);
			//delayed=false;
		}
		if(DEBUG) DEBUG_RET.append(new String(chars,start,end-start));
		else super.characters(chars, start, end-start);
	}



	private void startElement(OutputNode outputNode) throws SAXException {
		/*if(//outputNode.equals(paragraph) ||
				outputNode.equals(img) ||
				outputNode.equals(ahref)) 
			delayed=true;
		else */
		//if(!delayed){
		if(!outputNode.isDelayed()) {
			String realname=outputNode.getAttributes().getValue("realname");
			if(realname!=null && outputNode.getNodeName().equals(general.getNodeName())) {
				outputNode.outputNodeName=realname;
				outputNode.getAttributes().removeAttribute("realname");
			}
			if(DEBUG) {
				DEBUG_RET.append(outputNode.toString());
			}
			else superStartElement("", outputNode.getNodeName(), outputNode.getNodeName(), outputNode.getAttributes());
		}
	}
	
	private void endElement(OutputNode outputNode) throws SAXException {
		//if(delayed) {
		//if(outputNode.isDelayed()) {
			if(!nodes.isEmpty() && nodes.peek().isDelayed()) {
				if(DEBUG) DEBUG_RET.append(nodes.peek());
				else superStartElement("", nodes.peek().getNodeName(), nodes.peek().getNodeName(), nodes.peek().getAttributes());
				nodes.peek().setDelayed(false);
			}
			
			//delayed=false;
		//}
		if(DEBUG) DEBUG_RET.append("</"+outputNode.getNodeName()+">");
		else super.endElement("", outputNode.getNodeName(), outputNode.getNodeName());
	}

	private void decideNode(String nodeName, Stack<OutputNode> nodes) throws SAXException {
		decideNode(nodeName,new AttributesImpl(),nodes);
	}

	private void decideNode(String nodeName, AttributesImpl attrs, Stack<OutputNode> nodes) throws SAXException {
		OutputNode top=null;
		if(!nodes.isEmpty()) top=nodes.peek();
		OutputNode thisnode=new OutputNode(nodeName, attrs);
		
		if(thisnode.equals(top)) {
			// is end!!
			nodes.pop();
			endElement(top);
		}
		else {
			// id begin!!
			nodes.push(thisnode);
			startElement(thisnode);
		}
	}
	
	private void superStartElement(String string, String nodeName, String nodeName2, AttributesImpl attributes) throws SAXException {
		if(nodeName.equals("ul") || nodeName.equals("ol"))
			super.startElement(string, nodeName, nodeName2, new AttributesImpl());
		else
			super.startElement(string, nodeName, nodeName2, attributes);
		
	}

	
	private class OutputNode {
		
		private String outputNodeName=null;
		private AttributesImpl thisattrs=null;
		private boolean delayed=false;
		
		OutputNode(String nodeName, AttributesImpl nodeAttrs) {
			outputNodeName=nodeName;
			thisattrs=nodeAttrs;
		}
		
		public boolean isDelayed() {
			return delayed;
		}
		
		public void setDelayed(boolean d) {
			delayed=d;
		}
		
		public String getNodeName() {
			return outputNodeName;
		}
		
		public AttributesImpl getAttributes() {
			return thisattrs;
		}
		
		public boolean equals(OutputNode outputNode) {
			if(outputNode==null) return false;
			
			boolean r=outputNodeName.equals(outputNode.getNodeName());
			if(thisattrs.getLength()==outputNode.getAttributes().getLength()) {
				for(int i=0;i<thisattrs.getLength();i++) {
					if(!thisattrs.getLocalName(i).equals(outputNode.getAttributes().getLocalName(i)) ||
							!thisattrs.getValue(i).equals(outputNode.getAttributes().getValue(i))) r=false;
				}
			}
			else {
				r=false;
			}
			return r;
		}
		
		public OutputNode clone() {
			AttributesImpl n=new AttributesImpl(thisattrs);
			return new OutputNode(outputNodeName,n);
		}
		
		public String toString() {
			String r="<"+outputNodeName;
			for(int i=0;i<thisattrs.getLength();i++) {
				r+=" "+thisattrs.getLocalName(i)+"=\""+thisattrs.getValue(i)+"\"";
			}
			r+=">";
			return r;
		}
	}
		
		
}

