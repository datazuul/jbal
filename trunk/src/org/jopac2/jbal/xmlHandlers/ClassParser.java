/*
 * File:           ClassParser.java
 * Date:           18 gennaio 2004  15.57
 *
 * @author  romano
  */
package org.jopac2.jbal.xmlHandlers;
/*******************************************************************************
*
*  JOpac2 (C) 2002-2007 JOpac2 project
*
*     This file is part of JOpac2. http://jopac2.sourceforge.net
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
*******************************************************************************/

/**
* @author	Romano Trampus
* @version	18/01/2004
*/
import org.xml.sax.*;

/**
 * The class reads XML documents according to specified DTD and
 * translates all related events into ClassHandler events.
 * <p>Usage sample:
 * <pre>
 *    ClassParser parser = new ClassParser(...);
 *    parser.parse(new InputSource("..."));
 * </pre>
 * <p><b>Warning:</b> the class is machine generated. DO NOT MODIFY</p>
 *
 */
public class ClassParser implements ContentHandler {
    
    private java.lang.StringBuffer buffer;
    
    private ClassHandler handler;
    
    private java.util.Stack<Object> context;
    
    private EntityResolver resolver;
    
    private String currentElement;
    
    private java.util.Vector<ClassItem> SQLInstructions;
    
    public java.util.Vector<ClassItem> getSQLInstructions() {
        return SQLInstructions;
    }
    
    /**
     * Creates a parser instance.
     * @param handler handler interface implementation (never <code>null</code>
     * @param resolver SAX entity resolver implementation or <code>null</code>.
     * It is recommended that it could be able to resolve at least the DTD.
     */
    public ClassParser(final ClassHandler handler, final EntityResolver resolver) {
        this.handler = handler;
        this.resolver = resolver;
        buffer = new StringBuffer(111);
        context = new java.util.Stack<Object>();
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void setDocumentLocator(Locator locator) {
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void startDocument() throws SAXException {
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void endDocument() throws SAXException {
        SQLInstructions=handler.getSQLInstructions();
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void startElement(java.lang.String ns, java.lang.String name, java.lang.String qname, Attributes attrs) throws SAXException {
        dispatch(true);
        context.push(new Object[] {qname, new org.xml.sax.helpers.AttributesImpl(attrs)});
        
        if("class".equals(qname)) {
            handler.start_class(attrs);
        } else if ("element".equals(qname)) {
            handler.handle_element(attrs);
        } else if ("description".equals(qname)) {
            handler.start_description(attrs);
        } else {
            currentElement=qname;
            handler.start_Element(qname,attrs);
        }
/*        
        if ("INV".equals(qname)) {
            handler.start_INV(attrs);
        } else if ("class".equals(qname)) {
            handler.start_class(attrs);
        } else if ("DTE".equals(qname)) {
            handler.start_DTE(attrs);
        } else if ("TIT".equals(qname)) {
            handler.start_TIT(attrs);
        } else if ("BIB".equals(qname)) {
            handler.start_BIB(attrs);
        } else if ("NUM".equals(qname)) {
            handler.start_NUM(attrs);
        } else if ("element".equals(qname)) {
            handler.handle_element(attrs);
        } else if ("CLL".equals(qname)) {
            handler.start_CLL(attrs);
        } else if ("AUT".equals(qname)) {
            handler.start_AUT(attrs);
        } else if ("description".equals(qname)) {
            handler.start_description(attrs);
        } else if ("LAN".equals(qname)) {
            handler.start_LAN(attrs);
        } else if ("SBJ".equals(qname)) {
            handler.start_SBJ(attrs);
        } else if ("MAT".equals(qname)) {
            handler.start_MAT(attrs);
        }*/
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void endElement(java.lang.String ns, java.lang.String name, java.lang.String qname) throws SAXException {
        dispatch(false);
        context.pop();
        
        if("class".equals(qname)) {
            handler.end_class();
        } else if ("description".equals(qname)) {
            handler.end_description();
        } else if (currentElement.equals(qname)) {
            handler.end_Element();
        }
        
/*        
        if ("INV".equals(qname)) {
            handler.end_INV();
        } else if ("class".equals(qname)) {
            handler.end_class();
        } else if ("DTE".equals(qname)) {
            handler.end_DTE();
        } else if ("TIT".equals(qname)) {
            handler.end_TIT();
        } else if ("BIB".equals(qname)) {
            handler.end_BIB();
        } else if ("NUM".equals(qname)) {
            handler.end_NUM();
        } else if ("CLL".equals(qname)) {
            handler.end_CLL();
        } else if ("AUT".equals(qname)) {
            handler.end_AUT();
        } else if ("description".equals(qname)) {
            handler.end_description();
        } else if ("LAN".equals(qname)) {
            handler.end_LAN();
        } else if ("SBJ".equals(qname)) {
            handler.end_SBJ();
        } else if ("MAT".equals(qname)) {
            handler.end_MAT();
        }
 **/
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void characters(char[] chars, int start, int len) throws SAXException {
        buffer.append(chars, start, len);
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void ignorableWhitespace(char[] chars, int start, int len) throws SAXException {
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void processingInstruction(java.lang.String target, java.lang.String data) throws SAXException {
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void startPrefixMapping(final java.lang.String prefix, final java.lang.String uri) throws SAXException {
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void endPrefixMapping(final java.lang.String prefix) throws SAXException {
    }
    
    /**
     * This SAX interface method is implemented by the parser.
     *
     */
    public final void skippedEntity(java.lang.String name) throws SAXException {
    }
    
    private void dispatch(final boolean fireOnlyIfMixed) throws SAXException {
        if (fireOnlyIfMixed && buffer.length() == 0) return; //skip it
        context.peek();
        //Object[] ctx = (Object[]) context.peek();
        //String here = (String) ctx[0];
        //Attributes attrs = (Attributes) ctx[1];
        buffer.delete(0, buffer.length());
    }
    
    /**
     * The recognizer entry method taking an InputSource.
     * @param input InputSource to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
     *
     */
    public void parse(final InputSource input) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(input, this);
    }
    
    /**
     * The recognizer entry method taking a URL.
     * @param url URL source to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
     *
     */
    public void parse(final java.net.URL url) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(new InputSource(url.toExternalForm()), this);
    }
    
    /**
     * The recognizer entry method taking an Inputsource.
     * @param input InputSource to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
     *
     */
    public static void parse(final InputSource input, final ClassHandler handler) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(input, new ClassParser(handler, null));
    }
    
    /**
     * The recognizer entry method taking a URL.
     * @param url URL source to be parsed.
     * @throws java.io.IOException on I/O error.
     * @throws SAXException propagated exception thrown by a DocumentHandler.
     * @throws javax.xml.parsers.ParserConfigurationException a parser satisfining requested configuration can not be created.
     * @throws javax.xml.parsers.FactoryConfigurationRrror if the implementation can not be instantiated.
     *
     */
    public static void parse(final java.net.URL url, final ClassHandler handler) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        parse(new InputSource(url.toExternalForm()), handler);
    }
    
    private static void parse(final InputSource input, final ClassParser recognizer) throws SAXException, javax.xml.parsers.ParserConfigurationException, java.io.IOException {
        javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory.newInstance();
        factory.setValidating(true);  //the code was generated according DTD
        factory.setNamespaceAware(false);  //the code was generated according DTD
        XMLReader parser = factory.newSAXParser().getXMLReader();
        parser.setContentHandler(recognizer);
        parser.setErrorHandler(recognizer.getDefaultErrorHandler());
        if (recognizer.resolver != null) parser.setEntityResolver(recognizer.resolver);
        parser.parse(input);
    }
    
    /**
     * Creates default error handler used by this parser.
     * @return org.xml.sax.ErrorHandler implementation
     *
     */
    protected ErrorHandler getDefaultErrorHandler() {
        return new ErrorHandler() {
            public void error(SAXParseException ex) throws SAXException  {
                if (context.isEmpty()) System.err.println("Missing DOCTYPE.");
                throw ex;
            }
            
            public void fatalError(SAXParseException ex) throws SAXException {
                throw ex;
            }
            
            public void warning(SAXParseException ex) throws SAXException {
                // ignore
            }
        };
        
    }
    
}

