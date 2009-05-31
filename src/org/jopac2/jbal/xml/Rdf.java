package org.jopac2.jbal.xml;

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

/*
* @author	Romano Trampus
* @version 17/02/2005
*/

/**
 * JOpac2 - Modulo formato "RDF". v. 0.1
 *          Questo modulo permette di importare dati registrati nel formato
 *          XML/RDF Dublin Core + DCMI Cite
 */

/**
 * TODO: tutto da rifare
 */
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.*;

//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.iso2709.ISO2709Impl;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.BookSignature;
import org.jopac2.utils.JOpac2Exception;
//import org.xml.sax.XMLReader;
@SuppressWarnings("unchecked")
public class Rdf extends ISO2709Impl {
//  private String Rdf="http://www.w3.org/1999/02/22-Rdf-syntax-ns#";
//  private String xsi="http://www.w3.org/2001/XMLSchema";
//  private String dcterms="http://purl.org/dc/terms/";
//  private String dcq="http://purl.org/dc/qualifiers/1.0/";
//  private String dc="http://purl.org/dc/elements/1.1/";

  RdfData record;
  
  public Rdf(RdfData r) {
  	record=r;
  }
  
  public Rdf(String stringa, String dTipo) {
  	init(stringa);
  }

  public Rdf(String stringa, String dTipo, String livello) {
  	init(stringa);
  }
  
  public void init(String stringa) {
  	record=new RdfData();
    parse(stringa,record);
  }
  
  /* 
   * Da un nodo esplora e carica gli autori. 
   * Si aspetta di essere in una Rdf:bag e poi le linee
   */
  private void exploreData(Node node, Object v,String prefix) {
  	int type = node.getNodeType();
  	switch(type) {
	  	case Node.ELEMENT_NODE:
			NodeList children = node.getChildNodes();
			int len = children.getLength();
			for (int i=0; i<len; i++)
				exploreData(children.item(i),v,prefix);
		break;
	  	case Node.TEXT_NODE:
	  		if(v.getClass().getCanonicalName().equals("java.util.Vector")) {
	  			((Vector)v).addElement(node.getNodeValue());
	  		}
	  		else if (v.getClass().getCanonicalName().equals("java.util.String")) {
	  			v=prefix+node.getNodeValue();
	  		}
	    break;
  	}
  }
  
  private void exploreData(Node node, Object v) {
  	exploreData(node, v, null);
  }
  
  public void exploreAttributes(Node node) {
  
    NamedNodeMap attrs = node.getAttributes();
  
    int len = attrs.getLength();
    for (int i=0; i<len; i++) {
        Attr attr = (Attr)attrs.item(i);
        System.out.print(" " + attr.getNodeName() + "=\"" +
                  attr.getNodeValue() + "\"");
    }
  }
  
  public String lookAttr(NamedNodeMap attrs,String name) {
  	String r=null;
  	int len = attrs.getLength();
    for (int i=0; i<len; i++) {
        Attr attr = (Attr)attrs.item(i);
        if(attr.getNodeName().equals(name)) r=attr.getNodeValue();
    }
    return r;
  }
  
  public void explore(Node node, RdfData r) {
  	int type = node.getNodeType();
  	String nodeName = node.getNodeName();
  	NamedNodeMap attrs = node.getAttributes();
  	
    switch (type) {
	    case Node.ELEMENT_NODE:
	    	if(nodeName.equals("Rdf:Description")) {
      			r.addAbout(lookAttr(attrs,"Rdf:about"));
	    	}
	    	if(nodeName.equals("dc:creator")) {
      			exploreData(node,r.getAuthors());
	    	}
	    	if(nodeName.equals("dc:source")) {
      			exploreData(node,r.getSource());
	    	}
	    	if(nodeName.equals("dc:publisher")) {
	    		exploreData(node,r.getEditors());
	    	}
	    	if(nodeName.equals("dc:date")) {
	    		exploreData(node,r.getPublicationDate());
	    	}
	    	if(nodeName.equals("dc:language")) {
	    		exploreData(node,r.getLanguage());
	    	}
	    	if(nodeName.equals("dc:title")) {
	    		exploreData(node,r.getTitle());
	    	}
	    	if(nodeName.equals("dc:description")) {
	    		exploreData(node,r.getDescription());
	    	}
	    	if(nodeName.equals("dcterms:isPartOf")) {
	    		exploreData(node,r.getIsPartOf());
	    	}
	    	if(nodeName.equals("dcterms:bibliographicCitation")) {
	    		exploreData(node,r.getCitations(),lookAttr(attrs,"xsi:type")+"://");
	    	}
	    	if(nodeName.equals("dc:relation")) {
	    		if(lookAttr(attrs,"Rdf:parseType").equalsIgnoreCase("Resource")) {
	    			NodeList l=node.getChildNodes();
	    			Node c=l.item(0);
	    			if(c.getNodeName().equalsIgnoreCase("dcq:relationType")) {
	    				String relType= lookAttr(c.getAttributes(),"Rdf:resource");
	    				
	    				if(relType.equalsIgnoreCase("http://purl.org/dc/qualifiers/1.0/IsPartOf")) {
	    					RdfData rt=new RdfData();
	    					explore(l.item(1), rt);
	    					r.getLinkUp().addElement(new Rdf(rt));
	    				}
	    				if(relType.equalsIgnoreCase("http://purl.org/dc/qualifiers/1.0/hasPart")) {
	    					RdfData rt=new RdfData();
	    					explore(l.item(1), rt);
	    					r.getLinkDown().addElement(new Rdf(rt));
	    				}
	    			}
	    		}
	    	}

	      
	      NodeList children = node.getChildNodes();
	      int len = children.getLength();
	      for (int i=0; i<len; i++)
	        explore(children.item(i),r);
	      
	      break;
	    case Node.ENTITY_REFERENCE_NODE:
//	    	System.out.print("&" + node.getNodeName() + ";");
	      break;
	    case Node.CDATA_SECTION_NODE:
//	    	System.out.print("<![CDATA[" + node.getNodeValue() + "]]>");
	      break;
	    case Node.TEXT_NODE:
//	    	System.out.print(node.getNodeValue());
	      break;
	    case Node.PROCESSING_INSTRUCTION_NODE:
//	    	System.out.print("<?" + node.getNodeName());
//	      String data = node.getNodeValue();
//	      if (data!=null && data.length()>0)
//	      	System.out.print(" " + data);
//	      System.out.println("?>");
	      break;
	  }
  }
  
  static void print(Node node) {
    int type = node.getNodeType();
    switch (type) {
      case Node.ELEMENT_NODE:
        System.out.print("<" + node.getNodeName());
        NamedNodeMap attrs = node.getAttributes();
        int len = attrs.getLength();
        for (int i=0; i<len; i++) {
            Attr attr = (Attr)attrs.item(i);
            System.out.print(" " + attr.getNodeName() + "=\"" +
                      attr.getNodeValue() + "\"");
        }
        System.out.print('>');
        NodeList children = node.getChildNodes();
        len = children.getLength();
        for (int i=0; i<len; i++)
          print(children.item(i));
        System.out.print("</" + node.getNodeName() + ">");
        break;
      case Node.ENTITY_REFERENCE_NODE:
      	System.out.print("&" + node.getNodeName() + ";");
        break;
      case Node.CDATA_SECTION_NODE:
      	System.out.print("<![CDATA[" + node.getNodeValue() + "]]>");
        break;
      case Node.TEXT_NODE:
      	System.out.print(node.getNodeValue());
        break;
      case Node.PROCESSING_INSTRUCTION_NODE:
      	System.out.print("<?" + node.getNodeName());
        String data = node.getNodeValue();
        if (data!=null && data.length()>0)
        	System.out.print(" " + data);
        System.out.println("?>");
        break;
    }
  }

  public void parse(String in,RdfData record) {  
    try{
    	in=in.replaceAll("&#13;","");
    	in=in.replaceAll("&#10;","");
    	in=in.replaceAll("&#9;","");
    	//in=in.replaceAll("&","&amp;");
        
    	InputSource is=new InputSource(new StringReader(in));
    	DOMParser p=new DOMParser();

    	p.parse(is);
    	
    	Node n=p.getDocument();
    	
    	NodeList children = n.getChildNodes();
	    int len = children.getLength();
	      for (int i=0; i<len; i++)
	        explore(children.item(i),record);
//	    System.out.println("Out Rdf");
    }
    catch(Exception e){
        e.printStackTrace();
    }
  }

  public void initLinkUp() {
    //linkUp=getLink("");
  }

  public void initLinkSerie() {
//    linkSerie=getLink("016");
  }

  public void initLinkDown() {
//    linkDown=getLink("021");
  }

  // ritorna un vettore di elementi Rdf

  public Vector getAuthors() {
    return record.getAuthors();
  }

  public Vector getSignatures() {
    Vector res=new Vector();
 
 //   res.addElement(new BookSignature(codiceBib,b,inv,col,con));
    try {
    res.addElement(new BookSignature((String)record.getAbout().firstElement(),
    			"Catalogo "+record.getSource().firstElement(),
				null,null,null));
    }
    catch(Exception e) {}
    return res;
  }

  /*
   * TODO da implementare getEdition
   */
  public String getEdition() {return null;}
  
  public boolean hasLinkUp() {
    return (record.getLinkUp().size()>0);
  }

  public boolean hasLinkDown() {
    return (record.getLinkDown().size()>0);
  }

  public boolean hasLinkSerie() {
    return (record.getLinkSerie().size()>0);
  }
  
  public Vector getHasParts() {
    return record.getLinkDown();
  }

  public Vector getIsPartOf() {
    return record.getLinkUp();
  }

  public Vector getSerie() {
    return record.getLinkSerie();
  }
  
  public String getTitle() {return record.getTitle().size()>0?(String)record.getTitle().elementAt(0):null;}

  public String getISBD() {return record.getISBD().size()>0?(String)record.getISBD().elementAt(0):null;}

  public Vector<SubjectInterface> getSubjects() {return record.getSubjects();}
  public Vector getClassifications() {return record.getClassifications();}
  public Vector getEditors() {return record.getEditors();}
  public String getPublicationPlace() {return record.getPublicationPlace().size()>0?(String)record.getPublicationPlace().elementAt(0):null;}
  public String getPublicationDate() {return record.getPublicationDate().size()>0?(String)record.getPublicationDate().elementAt(0):null;}
  public String getBid() {return record.getAbout().size()>0?(String)record.getAbout().elementAt(0):null;}
  public String getAbstract() {return record.getDescription().size()>0?(String)record.getDescription().elementAt(0):null;}
  
  public Rdf() {
  }
  
  public void doJob() {
	String ris=new String();
	
	try {
    	File f=new File("c:/result.xml");
    	FileInputStream fi=new FileInputStream(f);
    	BufferedReader r=new BufferedReader(new InputStreamReader(fi));
    	int i=2500;
    	while((r.ready())&&(i-->0)) {
    		ris=ris+r.readLine();
    	}
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	parse(ris,new RdfData());
}
  
  public static void main(String args[]) {
  	Rdf r=new Rdf();
  	r.doJob();
  }

/* (non-Javadoc)
 * @see JOpac2.dataModules.ISO2709#getDescription()
 */
public String getDescription() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Vector<RecordInterface> getLinked(String tag) throws JOpac2Exception {
	// TODO Auto-generated method stub
	return null;
}

public void addAuthor(String author) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addClassification(ClassificationInterface data) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addComment(String comment) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addPart(RecordInterface part) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addPartOf(RecordInterface partof) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addSerie(RecordInterface serie) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addSignature(BookSignature signature) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addSubject(SubjectInterface subject) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void clearSignatures() throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public String getComments() {
	// TODO Auto-generated method stub
	return null;
}

public String getStandardNumber() {
	// TODO Auto-generated method stub
	return null;
}

public void setAbstract(String abstractText) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setDescription(String description) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setEdition(String edition) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setISBD(String isbd) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setPublicationDate(String publicationDate) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setPublicationPlace(String publicationPlace) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setStandardNumber(String standardNumber, String codeSystem) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setTitle(String title) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void setTitle(String title, boolean significant) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public void addPublisher(String publisher) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}

public BufferedImage getImage() {
	// TODO Auto-generated method stub
	return null;
}

public String getLanguage() {
	// TODO Auto-generated method stub
	return null;
}

public void setLanguage(String language) throws JOpac2Exception {
	// TODO Auto-generated method stub
	
}
}