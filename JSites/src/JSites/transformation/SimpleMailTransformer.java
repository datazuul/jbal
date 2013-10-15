package JSites.transformation;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2009 JOpac2 project
*
*     This file is part of JOpac2. http://www.jopac2.org
*
*  JOpac2 is free software; you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation; either version 2 of the License, or
*  (at your option) any later version.
*
*  JOpac2 is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with JOpac2; if not, write to the Free Software
*  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*
*  Please, see NOTICE.txt AND LEGAL directory for more info. Different licences
*  may apply for components included in JOpac2.
*
*******************************************************************************/

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Vector;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.jopac2.utils.Utils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.sun.mail.smtp.SMTPAddressFailedException;

public class SimpleMailTransformer extends MyAbstractPageTransformer {
	
	private TreeMap<String,Vector<String>> elements=new TreeMap<String,Vector<String>>();
	private StringBuffer sb=new StringBuffer();
	private boolean maildebug=false;
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		super.setup(arg0, arg1, arg2, arg3);
	}
	
	public void startDocument() throws SAXException{
		super.startDocument();
	}
	
	@Override
	public void endDocument() throws SAXException {	
			super.endDocument();
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException
	{
		super.startElement(namespaceURI, localName, qName, attributes);
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
		
		String t=sb.toString().trim();
		if(!t.isEmpty()) {
			Vector<String> v=elements.get(qName);
			if(v==null) v=new Vector<String>();
			v.add(t);
			elements.put(qName, v);
		}
		sb.delete(0, sb.length());
		
		if(localName.equals("sendmail")) {
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			PrintStream ps=new PrintStream(baos);
			boolean success=true;
			String md="";
			try {
				md=elements.get("debug").firstElement().trim();
				if(md.equalsIgnoreCase("true")) maildebug=true;
			}
			catch(Exception e) {}
			
			String from=getFirstElement("sendmail:from"); // from must be a valid email address			
			
			Vector<String> to=elements.get("sendmail:to");
			for(int i=0;success&&to!=null&&i<to.size();i++) {
				try {
						sendmail(getFirstElement("sendmail:smtphost"), 
								getFirstElement("sendmail:smtpport"), 
								getFirstElement("sendmail:smtpfqdn"), 
								getFirstElement(from), 
								to.elementAt(i), 
								getFirstElement("sendmail:subject"), 
								getFirstElement("sendmail:body"),
								getFirstElement("sendmail:smtpuser"), // username
								getFirstElement("sendmail:smtppassword"), // password
								ps, // debug print stream
								maildebug); // debug
					ps.print("\n");
				} catch (Exception e) {
//					e.printStackTrace();
//					ps.println(e.getMessage());
					success=false;
				}
				finally {
					try {
						ps.close();
						baos.close();
						contentHandler.startElement("", "result", "sendmail:result", emptyAttrs);
						if(success)
							throwField("success","sendmail:success",elements.get("mailsent").firstElement());
						else
							throwField("failure","sendmail:failure",elements.get("mailerror").firstElement());
						if(maildebug && baos!=null) {
							throwField("debug","sendmail:debug", "<![CDATA["+baos.toString()+"]]>");
						}
						contentHandler.endElement("", "result", "sendmail:result");
						
					} catch (IOException e) {
					}
					
				}
			}
			elements.clear();
		}
		
		super.endElement(namespaceURI, localName, qName);
    }
	
	private String getFirstElement(String element) {
		String r="";
		Vector<String> t=elements.get(element);
		if(t!=null) r=t.firstElement();
		return r;
	}

	public void characters(char[] a, int s, int e) throws SAXException{
		super.characters(a,s,e);
		sb.append(a, s, e);

	}
	
    private void sendmail(String server, String smtpport, String fqdn, String from, String to, String subject, String body, final String user, final String password, PrintStream debugOutput, boolean debug) throws Exception{
    	Properties props = new Properties();
    	props.put("mail.smtp.transport.protocol", "smtp"); 
        props.put("mail.smtp.host", server);
        props.put("mail.smtp.localhost", fqdn);
        props.put("mail.smtps.localhost", fqdn);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", maildebug);
        props.put("mail.smtp.port", smtpport);
        props.put("mail.smtp.socketFactory.port", smtpport);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");

        Session mailSession = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                        		user, password);
                    }
                });
    	
    	
    	
//    	Properties props = new Properties();
//        props.setProperty("mail.transport.protocol", "smtp");
//        props.setProperty("mail.host", server);
//        if(user!=null && password!=null) {
//	        props.setProperty("mail.user", user);
//	        props.setProperty("mail.password", password);
//        }

//        Session mailSession = Session.getDefaultInstance(props, null);
//        Session mailSession = Session.getDefaultInstance(props);
        if(debug && debugOutput!=null) {
	        mailSession.setDebug(debug);
	        mailSession.setDebugOut(debugOutput);
        }
        Transport transport = mailSession.getTransport("smtp");

        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject(subject);
        message.setFrom(new InternetAddress(from));
        message.setContent(body, "text/html");
        message.addRecipient(Message.RecipientType.TO,
             new InternetAddress(to));

        try {
	        transport.connect();
	        transport.sendMessage(message,
	            message.getRecipients(Message.RecipientType.TO));
	        transport.close();
        }
        catch(Exception e) {
        	System.out.println(Utils.currentDate()+" - SimpleMailTransformer:"+e.getMessage());
        	System.out.println("from: "+from);
        	System.out.println("to: "+to);
        	System.out.println("user: "+user);
        	System.out.println("remoteAddr: "+getParameter("compositeRemoteAddr"));
        	throw e;
        }
   }

}
