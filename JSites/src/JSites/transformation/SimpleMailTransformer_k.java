package JSites.transformation;
import javax.mail.*;
import javax.mail.internet.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import JSites.transformation.catalogSearch.Templator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

class SimpleMailTransformer_k extends MyAbstractPageTransformer {
	
	@Override
	public void setup(SourceResolver arg0, Map arg1, String arg2,
			Parameters arg3) throws ProcessingException, SAXException,
			IOException {
		super.setup(arg0, arg1, arg2, arg3);
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	/**
	 * <content>
−
<sendmail>
<subject>subject all mails will have</subject>
<mailerror>Error message</mailerror>
<mailsent>Mail sent message (on success)</mailsent>
<to>trampus@units.it</to>
<from>romano.trampus@gmail.com</from>
−
<body>

							
							Dati da: 
							
							http://localhost:8080/JOpac2/PDP
<br/>

							
		
			Parametri
			
		
		pi = freed
</body>
</sendmail>
</content>

content>
<mailerror>Error message</mailerror>
<mailsent>Mail sent message (on success)</mailsent>
−
<result>
<success to="trampus@units.it">Mail sent</success>
−
<exception>
<message>Illegal address</message>
</exception>
</result>
</content>
	 */
	private TreeMap<String,String> elements=new TreeMap<String,String>();
	private String current=null;
	private StringBuffer sb=new StringBuffer();
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException
	{
		super.startElement(namespaceURI, localName, qName, attributes);
		current=localName;
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
		super.endElement(namespaceURI, localName, qName);
		elements.put(current, sb.toString());
		sb.delete(0, sb.length());
    }
	
	public void characters(char[] a, int s, int e) throws SAXException{
		super.characters(a,s,e);
		sb.append(a, s, e);

	}
	
    public void sendmail(String[] args) throws Exception{
        System.out.println("Sending mail...");
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "mail.units.it");
//        props.setProperty("mail.user", "myuser");
//        props.setProperty("mail.password", "mypwd");

        Session mailSession = Session.getDefaultInstance(props, null);
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject("HTML  mail with images");
        message.setFrom(new InternetAddress("trampus@units.it"));
        message.setContent("<h1>Hello world</h1>", "text/html");
        message.addRecipient(Message.RecipientType.TO,
             new InternetAddress("trampus@units.it"));

        transport.connect();
        transport.sendMessage(message,
            message.getRecipients(Message.RecipientType.TO));
        transport.close();
        }
}