/*
 * Record2display.java
 *
 * Created on 15 settembre 2004, 11.58
 */

package JSites.transformation.catalogSearch;
/*******************************************************************************
*
*  JOpac2 (C) 2002-2012 JOpac2 project
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

/**
* @author	Albert Caramia
* @version	??/??/2002
* 
* @author	Romano Trampus
* @version	??/??/2002
* 
* @author	Romano Trampus
* @version	15/09/2004
* 
* @author 	Romano Trampus
* @version	09/03/2006
* Corretto bug sulle connessioni e aggiunto dispose: se la variabile di tipo 
* Connection e' globale allora non viene rilasciata e il pool si satura.
* 
* @author Romano Trampus
* @version 15/11/2012
* Aggiunta funzione per la fusione di record consecutivi con campi uguali
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.ComponentSelector;
import org.apache.cocoon.xml.AttributesImpl;
import org.apache.cocoon.xml.dom.DOMStreamer;
import org.jopac2.jbal.ElectronicResource;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.abstractStructure.Tag;
import org.jopac2.jbal.classification.ClassificationInterface;
import org.jopac2.jbal.subject.SubjectInterface;
import org.jopac2.utils.BookSignature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import JSites.utils.XMLUtil;


public class Record2document
{
    //private DataSourceComponent datasource;
    protected ComponentSelector dbselector;
    protected ComponentManager manager;

    //private RecordInterface ma;
    private BookSignature b;
//    protected String catalog,dbType,merge="";
//    protected String[] toMerge=null;
    protected Document r1, r2;
    protected DOMStreamer streamer;
    

	private Node includeData(Document document, Vector<RecordInterface> v, String name) {
    	Node node=document.createElement(name);
    	for(int i=0; v!=null & i<v.size();i++) {
    		Node rec=document.createElement("record");
    		node.appendChild(rec);

    		XMLUtil.appendNode(document, rec, "isbd", v.elementAt(i).getISBD());
    		XMLUtil.appendNode(document, rec, "bid", v.elementAt(i).getBid());
    		XMLUtil.appendNode(document, rec, "jid", v.elementAt(i).getJOpacID());
    	}
    	return node;
    }
    
    private Node includeData(Document document, Vector<RecordInterface> v, String name, String tag) {
        Node node=document.createElement(name);
        for(int i=0;i<v.size();i++) {
        	XMLUtil.appendNode(document, node, tag, (v.elementAt(i)).getISBD());
        }
        return node;
    }
    
    private Node sendMdb(Document document, Node root, RecordInterface ma2) throws ParserConfigurationException {
    	String[] ch=ma2.getChannels();
    	
    	for(int i=0;i<ch.length;i++) {
    		if(ch[i].startsWith("/")) {
    			String name=ch[i].substring(ch[i].lastIndexOf("/")+1);
    			String value=ma2.getField(name);
    			XMLUtil.appendNode(document,root,name,value);
    		}
    	}
        
    	return root;
    }

	private Node sendIso(Document document, Node root, RecordInterface ma2,String datadir) throws ParserConfigurationException {
    	
    	Vector<String> v=null;
    	Vector<RecordInterface> vma=null;
    	Vector<BookSignature> vbs=null;
    	
        XMLUtil.appendNode(document,root,"type",ma2.getTipo());
        
		String file = saveImgFile(ma2,datadir);
		AttributesImpl a = new AttributesImpl();
		
		String nat = ma2.getPublicationNature();

		XMLUtil.appendNode(document,root,"nature",nat);

		
		Element image=document.createElement("image");
		image.setAttribute("nature", nat);
		image.setTextContent(file);
		root.appendChild(image);
    	
    	XMLUtil.appendNode(document,root,"title",ma2.getTitle());
    	
    	v=ma2.getAuthors();
        if(v!=null && v.size()>0) {
        	Element authors=document.createElement("authors");
        	root.appendChild(authors);
            for(int i=0;i<v.size();i++) {
            	XMLUtil.appendNode(document, authors, "author", (String)v.elementAt(i));
            }
            v.clear();
        }
        v=null;
        
        

        
        XMLUtil.appendNode(document, root, "ISBD", ma2.getISBD());
        XMLUtil.appendNode(document, root, "description", ma2.getDescription());
        XMLUtil.appendNode(document, root, "comments", ma2.getComments());
        
        v=ma2.getEditors();
        if(v!=null && v.size()>0) {
        	Element editors=document.createElement("editors");
        	root.appendChild(editors);
	        for(int i=0;i<v.size();i++) {
	        	XMLUtil.appendNode(document, editors, "editor", (String)v.elementAt(i));
	        }
	        v.clear();
        }
        v=null;

    	XMLUtil.appendNode(document, root, "standardNumber", ma2.getStandardNumber());
    	XMLUtil.appendNode(document, root, "publicationPlace", ma2.getPublicationPlace());
    	XMLUtil.appendNode(document, root, "publicationDate", ma2.getPublicationDate());

        Vector<SubjectInterface> v1=ma2.getSubjects();
        if((v1!=null)&&(v1.size()>0)) {
            Element subjects=document.createElement("subjects");
        	root.appendChild(subjects);
            for(int i=0;i<v1.size();i++) {
            	XMLUtil.appendNode(document, subjects, "subject", (v1.elementAt(i).getData()).elementAt(0).getContent());
            }

            v1.clear();v1=null;
        }
        
        Vector<ClassificationInterface> vc=ma2.getClassifications();
        if((vc!=null)&&(vc.size()>0)) {
            Element classifications=document.createElement("classifications");
        	root.appendChild(classifications);
            for(int i=0;i<vc.size();i++) {
            	XMLUtil.appendNode(document, classifications, "classification", (String)vc.elementAt(i).toString());
            }

            vc.clear();v=null;
        }
        
        if(ma2.hasLinkUp()) {
            vma=ma2.getIsPartOf();
            Node partof=includeData(document,vma,"partof");
            root.appendChild(partof);
            if(vma!=null) {vma.clear();vma=null;}
        }
        if(ma2.hasLinkDown()) {
            vma=ma2.getHasParts();
            Node haspart=includeData(document,vma,"haspart");
            root.appendChild(haspart);
            if(vma!=null) {vma.clear();vma=null;}
        }
        if(ma2.hasLinkSerie()) {
            vma=ma2.getSerie();
            Node serie=includeData(document,vma,"serie","isbdserie");
            root.appendChild(serie);
            if(vma!=null) {vma.clear();vma=null;}
        }
        ElectronicResource[] elResources=ma2.getElectronicVersion();
        if(elResources!=null)
	        for(ElectronicResource el:elResources) {
	        	Node resource=document.createElement("resource");
	        	root.appendChild(resource);
	        	 XMLUtil.appendNode(document, resource, "type",el.getAccessmethod());
	        	 XMLUtil.appendNode(document, resource, "url",el.getUrl());
			}

    	XMLUtil.appendNode(document, root, "abstract", ma2.getAbstract());
        
        try {
//        	if(v!=null) v.clear();
//        	v=null;
        	vbs=ma2.getSignatures();
        }
        catch(Exception e) {
        	// ignora eventuali errori nella decodifica delle signature
        }

        if((vbs!=null)&&(vbs.size()>0)) {
            Node signatures=document.createElement("signatures");
            root.appendChild(signatures);
            try {

                for(int i=0;i<vbs.size();i++) {
                	try {
                    	b=vbs.elementAt(i);
                    	Node signature=document.createElement("signature");
                    	signatures.appendChild(signature);

                        XMLUtil.appendNode(document, signature, "libraryId",b.getLibraryId());
                        XMLUtil.appendNode(document, signature, "libraryName",b.getLibraryName());
                        XMLUtil.appendNode(document, signature, "idNumber",b.getBookNumber());
                        XMLUtil.appendNode(document, signature, "localization",b.getBookLocalization());
                        XMLUtil.appendNode(document, signature, "holding",b.getBookCons());
                        
                	}
                	catch(Exception e) {
                		// ignoro errori di codifica delle signatures
                	}
                }
            }
            catch(NullPointerException e) {}
            vbs.clear();vbs=null;
        }
       
        XMLUtil.appendNode(document, root, "prezzo", ma2.getAvailabilityAndOrPrice());

        return root;
	}




    protected String saveImgFile(RecordInterface ri, String datadir) {
    	String r="images/pubimages/NS.jpg";
		try {
    	
			if(ri.getImage()!=null) {
			
				File dir = new File(datadir + "/images/pubimages");
				if(!dir.exists())dir.mkdirs();
				String imgstr = datadir + "/images/pubimages/eut" + ri.getJOpacID() + ".jpg";
				try {
					FileOutputStream imgfile = new FileOutputStream(imgstr);
					ImageIO.write(ri.getImage(), "jpeg", imgfile);
					imgfile.flush();
					imgfile.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				r="images/pubimages/eut" + ri.getBid() + ".jpg";
			}
		}
		catch(Exception e) {
			
		}
		return r;
		
	}

	public Document getDocument(RecordInterface ma, String catalog, String id, String display, String datadir) throws ParserConfigurationException {
		Document document=XMLUtil.createDocument();
		Node root=document.createElement("record");
		ma.setJOpacID(id);
    	
    	XMLUtil.appendNode(document,root,"jid",ma.getJOpacID());
    	XMLUtil.appendNode(document,root,"db",catalog);
    	XMLUtil.appendNode(document,root,"bid",ma.getBid());
    	
    	if(display!=null && display.equals("iso")) {
        	XMLUtil.appendNode(document, root, "data", ma.toString());
    	}
    	else if(display!=null && display.equals("xml")) {

        	try {
        		XMLUtil.appendNode(document, root, "data", ma.toXML());
			} catch (Exception e) {
				XMLUtil.appendNode(document, root, "error", e.getMessage());
			}
    	}
    	else if(ma.getTipo().equals("mdb")) {
    		sendMdb(document,root,ma);
    	}
    	else {
    		sendIso(document,root,ma,datadir);
    	}
    	document.appendChild(root);
		return document;
	}


    
//    private void viewFascicoli(RecordInterface ma) {
//    	try {
//	    	String tit = ma.getTitle();
//			StaticDataComponent sd = new StaticDataComponent();
//			sd.init(JSites.utils.DirectoryHelper.getPath()+"/WEB-INF/conf/");
//			DoSearchNew doSearchNew = new DoSearchNew(getConnection(dbname),catalog,sd);
//			SearchResultSet result = doSearchNew.executeSearch("CLL="+tit, false);
//			
//			
//		} catch (ExpressionException e1) { e1.printStackTrace();
//		} catch (ComponentException e) { e.printStackTrace();
//		} catch (SQLException e) { e.printStackTrace();
//		}
//		
//	}


}



