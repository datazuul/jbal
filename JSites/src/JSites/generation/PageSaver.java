/*
 * Created on 19-set-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package JSites.generation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.apache.avalon.framework.component.ComponentException;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.SAXException;

import JSites.authentication.Permission;

import JSites.utils.DBGateway;

public class PageSaver extends MyAbstractPageGenerator{
	
	private long save_cid = 0;
	private long newcid = 0;
	private String save_componentType = "";
	private long save_pacid = 0;
	private long statecode = 0;
	private long order = 0;

	public void processChild(long id, Connection conn) throws SAXException, SQLException {
		
		if(! (containerType.equals("content"))){
			super.processChild(id, conn);
			return;
		}
		
		if( save_pacid == id ){					//se il contenitore � quello giusto	
			DBGateway.setHasChild(id, conn);
			boolean isActive = false;
			try { 
				isActive = DBGateway.getState(newcid, conn)==3;
				if(newcid==0 || isActive){		//se il componente deve essere validato (stato PND)							//o se il componente � una new entry	
					newcid = DBGateway.getNewFreeCid(conn);		//cerca un nuovo CID nel DB
					DBGateway.saveDBComponentAndRelationship(newcid, save_componentType, 0, save_pacid, save_cid, 
							new java.util.Date(), statecode, pageId, request.getParameter("url"), order, conn);
				}
				else if (statecode==2){	DBGateway.setPending(newcid, conn); }
				else if (statecode==1){	DBGateway.setWorking(newcid, conn); }
			} catch (Exception e) {e.printStackTrace();}
			
			super.processChild(id, conn);
		}
		
	}

	public void saveChild(long cid, long pacid, Connection conn) throws SAXException, SQLException {
			
		String type = DBGateway.getTypeForCid(cid, conn);

		AttributesImpl saving_attrs = new AttributesImpl();
		saving_attrs.addCDATAAttribute("cid",String.valueOf(cid));
		saving_attrs.addCDATAAttribute("pacid",String.valueOf(pacid));
		saving_attrs.addCDATAAttribute("pid",String.valueOf(pageId));
		
		order = DBGateway.getOrederNumber(cid,conn);
		
		saving_attrs.addCDATAAttribute("order",String.valueOf(order));
		saving_attrs.addCDATAAttribute("type",type);
		
		saving_attrs = doColor(cid,saving_attrs,conn);
		
		contentHandler.startElement("","saving","saving",saving_attrs);
		contentHandler.endElement("","saving","saving");

	}

	protected void init(Connection conn){
		
		super.init(conn);
		try {
			//-----------LETTURA PARAMETRI E ATTRIBUTI
			save_componentType = request.getParameter("type");

			newcid = save_cid = Long.parseLong(request.getParameter("cid"));
			save_pacid = getPacid(conn);

			if(request.getParameter("order") != null)
			try{ order = Long.parseLong(request.getParameter("order"));} catch(Exception e){e.printStackTrace();}
			try{ statecode = DBGateway.getStateCode(request.getParameter("state"), conn);} catch(Exception e){e.printStackTrace();}
		}
		catch (Exception e2) {	e2.printStackTrace(); }

	}

	protected void subClassProcess(String componentType, long id, AttributesImpl attrCid, boolean hasChildren, Connection conn) throws SAXException, SQLException {

		if(! (containerType.equals("content"))){
			super.subClassProcess(componentType, id, attrCid, hasChildren, conn);
			return;
		}
		
		attrCid = doColor(id, attrCid, conn);
		attrCid.addCDATAAttribute("accessible",String.valueOf(permission.hasPermission(Permission.ACCESSIBLE)));
		attrCid.addCDATAAttribute("editable",String.valueOf(permission.hasPermission(Permission.EDITABLE)));
		attrCid.addCDATAAttribute("validable",String.valueOf(permission.hasPermission(Permission.VALIDABLE)));
		attrCid.addCDATAAttribute("sfa",String.valueOf(permission.hasPermission(Permission.SFA)));
		
		contentHandler.startElement("",componentType,componentType,attrCid);
		if(hasChildren){
			Statement st = conn.createStatement();
	    	ResultSet rs = st.executeQuery("select CID from tblcontenuti where PaCID='" + id +"' and StateID<4 order by OrderNumber"); // ho tolto and IDStato=3
			while(rs.next()){
				long childId = rs.getLong(1);
				if(this.containerType.equals("content")){
					if(childId==newcid) saveChild(childId,id, conn);
				}
				else processChild(childId, conn);

			}
			rs.close();
			st.close();
		}
		contentHandler.endElement("",componentType,componentType);
		
	}
	
	

	private long getPacid(Connection conn) throws SQLException, ComponentException {
		long ret = Long.parseLong(request.getParameter("pacid"));
		if(ret == 0){
			Long l = (Long) session.getAttribute("newPacid");
			if(l!=null) ret = l.longValue();
		}
		if(ret==0){
			ret = DBGateway.getNewFreeCid(conn);
			session.setAttribute("newPacid",new Long(ret));
			DBGateway.saveDBComponent(ret,"content",1,0,new Date(), pageId, request.getParameter("url"), conn);
			DBGateway.linkPageContainers(ret, pageId, conn);
		}
		return ret;
	}
}
