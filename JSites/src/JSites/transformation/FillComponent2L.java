/*
 * Created on 26-set-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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

import java.io.IOException;
import java.sql.*;
import java.util.Map;

import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import JSites.authentication.Permission;

/**
 * @author Iztok
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FillComponent2L extends MyAbstractPageTransformer {
	
	private AttributesImpl emptyAttr = new AttributesImpl();	
	private int level;
	private long pageId;
	private long contentCid;
	private long nComponentsInContainer;
	long counter;
	private Permission permission;
	long ordercounter = 0;

	@SuppressWarnings("unchecked")
	public void setup(SourceResolver arg0, Map arg1, String arg2, Parameters arg3) throws ProcessingException, SAXException, IOException {
		
		super.setup(arg0, arg1, arg2, arg3);
		
		level = 0;
		pageId = -1;
		contentCid = 0;
		nComponentsInContainer = 0;
		counter = 0;
		ordercounter = 0;
		if(permission == null)
			permission = new Permission();
		permission.erasePermissions();
	}

	public void startElement(String namespaceURI, String localName, String qName, Attributes origAttributes) throws SAXException
	{
		AttributesImpl attributes = new AttributesImpl(origAttributes);
		
		try{
			String temp = attributes.getValue("type");
			
			if(temp != null){
				String[] type = temp.split(":");
				if(type.length>1) {
					attributes.removeAttribute("type");
					attributes.addCDATAAttribute("type", type[0]);
					attributes.addCDATAAttribute("extra", type[1]);
				}
			}

			String tempPid = attributes.getValue("pid");
			if(pageId == -1 && tempPid != null) pageId = Long.parseLong(tempPid);
			level++;
			if(localName.equals("editing")){
				edit(localName, attributes); 
			}
			else if(localName.equals("saving")){
				save(localName, attributes);
			}
			else if(localName.equals("sidebar")){
				AttributesImpl tempAttrs = new AttributesImpl();
			
				if (attributes.getValue("pid")==null){
					tempAttrs.addCDATAAttribute("pid",String.valueOf(pageId));
					fill("sidebar", tempAttrs);}
				else
					fill("sidebar", attributes);
			}
			else if(localName.endsWith("2Order")){
				String newTagName = localName.substring(0,localName.indexOf("2Order"));
				super.startElement(namespaceURI, newTagName, newTagName, attributes);
				containerOrder(localName, Long.parseLong(attributes.getValue("cid")));
	
			}
			else if(localName.equals("ordering")){
				componentOrder(localName, Long.parseLong(attributes.getValue("cid")), attributes.getValue("type"), attributes.getValue("pacid"), attributes.getValue("time"));
			}
			else if(level==2){
				fill(localName, attributes);
			}
			
			else super.startElement(namespaceURI, localName, qName, attributes);
			
			if(localName.equals("content")){
				if(attributes.getValue("accessible").equals("true"))
					permission.setPermission(Permission.ACCESSIBLE);
				if(attributes.getValue("editable").equals("true"))
					permission.setPermission(Permission.EDITABLE);
				if(attributes.getValue("validable").equals("true"))
					permission.setPermission(Permission.VALIDABLE);
				if(attributes.getValue("sfa").equals("true"))
					permission.setPermission(Permission.SFA);
				contentCid = Long.parseLong(attributes.getValue("cid"));	
			}
		}
		catch (NumberFormatException e) { e.printStackTrace(); }
		catch (ComponentException e) { e.printStackTrace();	}
		catch (SAXException e) { e.printStackTrace(); } 
		catch (SQLException e) { e.printStackTrace();}
	}
	
	private void componentOrder(String nodeName, long cid, String componentType, String sPacid, String time) throws SAXException {
		counter++;
		String src = "cocoon://components/" + componentType + "/order?cid=" + cid + "&order=" + counter + "&maxn=" + nComponentsInContainer + "&time=" + time;
		
		AttributesImpl attrs = new AttributesImpl();
		attrs.addCDATAAttribute("src", src);

		super.startElement("http://apache.org/cocoon/include/1.0", "include", "include", attrs);
		super.endElement("http://apache.org/cocoon/include/1.0", "include", "include");
		
	}
		
	
	private void containerOrder(String localName, long cid) throws SAXException, SQLException, ComponentException {
		
		//-----------------------------*/
		emptyAttr.clear();
		emptyAttr.addCDATAAttribute("pid",String.valueOf(pageId));
		super.startElement("","OrderManager","OrderManager", emptyAttr);
		emptyAttr.clear();

		Connection conn = null;
		
		try{
			conn = getConnection(dbname);
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select count(*) from (select CID from tblcontenuti where PaCID=" + cid +" and StateID=3) as t");
			rs.next();
			nComponentsInContainer = rs.getLong(1);
			rs.close();
			st.close();
		}catch(Exception ex) {ex.printStackTrace();}
		
		try{ if(conn!=null)conn.close(); } catch(Exception ex){System.out.println("Non ho potuto chiudere la connessione");}
		
		counter = 0;
	}

	/**
	 * @throws SAXException
	 * 
	 */
	private void fill(String componentType, Attributes attrz) throws SAXException {
		
		String src = "cocoon://components/" + componentType + "/view?";
		
		int index = attrz.getLength();
		for(int i=0;i<index;i++){
			String name = attrz.getLocalName(i);
			String value = attrz.getValue(name);
			src = src + name + "=" + value + "&";
		}
		src = src.substring(0,src.length()-1);
		
		AttributesImpl attrs = new AttributesImpl();
		attrs.addCDATAAttribute("src", src);
		
		try{
			if(permission.hasPermission(Permission.EDITABLE) && ordercounter < Long.parseLong(attrz.getValue("order"))){
                addSectionManager();
				ordercounter = Long.parseLong(attrz.getValue("order"));
			}
		}
		catch(Exception e){e.printStackTrace(); System.out.println("attrz.getValue(\"order\") = " + attrz.getValue("order"));}
		
		super.startElement("http://apache.org/cocoon/include/1.0", "include", "include", attrs);
		super.endElement("http://apache.org/cocoon/include/1.0", "include", "include");
	}
	
	private void edit(String nodeName, Attributes attributes) throws SQLException, SAXException, ComponentException {
		
		long cid = Long.parseLong(attributes.getValue("cid"));
		String componentType = attributes.getValue("type");
		String extra = attributes.getValue("extra");
		String sPacid = attributes.getValue("pacid");
		String paPid = attributes.getValue("papid");
		String order = attributes.getValue("order");
		
		String src = "cocoon://components/" + componentType + "/edit?";
		
		for(int i=0;i<attributes.getLength();i++){
			String name = attributes.getQName(i);
			String value = attributes.getValue(i);
			src=src+name+"="+value+"&";
		}
//		src = src.substring(0,src.length()-1);
		src = src  +"permission="+permission.getPermissionCode();

		long pacid = 0;
		
		if(sPacid!=null)pacid = Long.parseLong(sPacid);
		else{
			
			Connection conn = null;
			
			try{
				conn = getConnection(dbname);
				PreparedStatement ps = conn.prepareStatement("select PaCID from tblcontenuti where CID = ?");
				ps.setLong(1,cid);
				ps.execute();
				ResultSet rs = ps.getResultSet();
				
				if(rs.next())
					pacid = rs.getLong(1);
				
				rs.close();
				ps.close();
			}catch(Exception ex) {ex.printStackTrace();}
			
			try{ if(conn!=null)conn.close(); } catch(Exception ex){System.out.println("Non ho potuto chiudere la connessione");}

		}
		
		String action = "";
				
		String tCompType = extra==null?componentType:componentType+":"+extra;
		
		if(pacid==0)action = "pagesave?papid=" + paPid + "&pacid=" + pacid + "&cid=" + cid + "&type=" + tCompType + "&order=" + order;
		else action = "pagesave?pid=" + pageId + "&pacid=" + pacid + "&cid=" + cid + "&type=" + tCompType + "&order=" + order;
		
		
		AttributesImpl attrs = new AttributesImpl();
		attrs.addCDATAAttribute("src", src);
		
		//ADDATO DA DAMIANO
		AttributesImpl attrib = new AttributesImpl();
		attrib.addCDATAAttribute("method","post");
		attrib.addCDATAAttribute("action",action);
		attrib.addCDATAAttribute("name","editform");
		attrib.addCDATAAttribute("id","editform");
		attrib.addCDATAAttribute("onsubmit","validate()");
		attrib.addCDATAAttribute("enctype","multipart/form-data");
		//-----------------------------*/
		
		super.startElement("","form","form",attrib);
		
		super.startElement("http://apache.org/cocoon/include/1.0", "include", "include", attrs);
		super.endElement("http://apache.org/cocoon/include/1.0", "include", "include");
		
		super.endElement("","form","form");
	}
	
	private void save(String nodeName, Attributes attributes) throws SQLException, SAXException {
		
		String componentType = attributes.getValue("type");
		
		String src = "cocoon://components/" + componentType + "/save?";
		
		for(int p=0;p<attributes.getLength();p++){
			if(attributes.getLocalName(p).equals("componentType")){}
			else
				src = src + attributes.getLocalName(p) + "=" + attributes.getValue(p) + "&";
		}
		src = src.substring(0,src.length()-1);
		
		AttributesImpl attrs = new AttributesImpl();
		attrs.addCDATAAttribute("src", src);
		
		try{
			if(permission.hasPermission(Permission.EDITABLE) && ordercounter < Long.parseLong(attributes.getValue("order"))){
                addSectionManager();
				ordercounter = Long.parseLong(attributes.getValue("order"));
			}
		}
		catch(Exception e){e.printStackTrace();}
		
		try{
			super.startElement("http://apache.org/cocoon/include/1.0", "include", "include", attrs);
			super.endElement("http://apache.org/cocoon/include/1.0", "include", "include");
		}catch(Exception e){
			System.out.println("Exception generated after: "+src);
			e.printStackTrace();
		}
		
	}


	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
		if(localName.endsWith("2Order")){
			String newTagName = localName.substring(0,localName.indexOf("2Order"));
			
		    super.endElement("","OrderManager","OrderManager");
		    super.endElement("",newTagName,newTagName);
		}
		else if(localName.equals("content")){
			if(permission.hasPermission(Permission.EDITABLE) || permission.hasPermission(Permission.VALIDABLE) ) {
				super.startElement("", "managers", "managers", this.emptyAttr);
				if(permission.hasPermission(Permission.EDITABLE)){
					try {
						//this.ordercounter++;
						addSectionManager();
						addContentManager();
					}
					catch (Exception e) {	e.printStackTrace();}
				}
				if(permission.hasPermission(Permission.VALIDABLE)){
					try {addValidationManager();}
					catch (Exception e) {	e.printStackTrace();}
				}
				super.endElement("", "managers", "managers");
			}
			super.endElement(namespaceURI, localName, qName);
		}
		else if(level!=2)
			super.endElement(namespaceURI, localName, qName);
		level--;
    }
	
	private void addValidationManager() throws ComponentException, SAXException {
		Statement st = null;
		AttributesImpl formAttrs = new AttributesImpl();
		formAttrs.addCDATAAttribute("pid",String.valueOf(pageId));
		formAttrs.addCDATAAttribute("pacid",String.valueOf(contentCid));
		
		Connection conn = null;
		
		try {
			conn = getConnection(dbname); 
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("select Valid, InSidebar from tblpagine where PID="+this.pageId);
			if(rs.next()){
				formAttrs.addCDATAAttribute("active",String.valueOf(rs.getBoolean(1)));
				formAttrs.addCDATAAttribute("insidebar",String.valueOf(rs.getBoolean(2)));
			}
			rs.close();
			st.close();
			
		}catch(Exception ex) {ex.printStackTrace();}
		
		try{ if(conn!=null)conn.close(); } catch(Exception ex){System.out.println("Non ho potuto chiudere la connessione");}
		
		contentHandler.startElement("","ValidationManager","ValidationManager",formAttrs);
		contentHandler.endElement("","ValidationManager","ValidationManager");
		
	}

	private void addContentManager() throws SAXException, ComponentException {
		AttributesImpl formAttrs = new AttributesImpl();
		formAttrs.addCDATAAttribute("pid",String.valueOf(pageId));
		formAttrs.addCDATAAttribute("pacid",String.valueOf(contentCid));
		contentHandler.startElement("","ContentManager","ContentManager",formAttrs);
		contentHandler.endElement("","ContentManager","ContentManager");
	}
	
	private void addSectionManager() throws SAXException, ComponentException {
		AttributesImpl formAttrs = new AttributesImpl();
		formAttrs.addCDATAAttribute("pid",String.valueOf(pageId));
		formAttrs.addCDATAAttribute("pacid",String.valueOf(contentCid));
		formAttrs.addCDATAAttribute("order",String.valueOf(ordercounter+1));
		contentHandler.startElement("","SectionManager","SectionManager",formAttrs);
		contentHandler.endElement("","SectionManager","SectionManager");
	}

}
