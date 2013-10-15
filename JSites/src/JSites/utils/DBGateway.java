package JSites.utils;
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
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.commons.io.FileUtils;
import org.hsqldb.Types;
import org.jopac2.utils.JOpac2Exception;
import org.jopac2.utils.Utils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import JSites.setup.DbSetup;
import JSites.utils.site.NewsItem;


import JSites.authentication.Authentication;
import JSites.authentication.Permission;

public class DBGateway {
	
	
	public static void executeStatement(DataSourceComponent datasourceComponent, String sql) throws SQLException {
		Statement st=null;
		Connection conn=null;
		try {
			conn=datasourceComponent.getConnection();
			st = conn.createStatement();
			st.executeUpdate(sql);
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.executeStatement exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.executeStatement exception " + fe.getMessage());}
		}
	}
	
	/**

ALTER TABLE tblnews ADD COLUMN eventstarttime VARCHAR(12) NULL  , 
	ADD COLUMN eventendtime VARCHAR(12) NULL  , 
	ADD INDEX evstarttime (eventstarttime ASC) , 
	ADD INDEX evendtime (eventendtime ASC) ;


 ALTER TABLE tblcomponenti 
 	ADD COLUMN username VARCHAR(50)  NOT NULL DEFAULT 'unknown', 
	ADD COLUMN remoteip VARCHAR(20)  NOT NULL DEFAULT 'unknown', 
	ADD INDEX Index_3(username), 
	ADD INDEX Index_4(remoteip);
	
	
ALTER TABLE tblpagine 
	ADD COLUMN resp VARCHAR(50)  NOT NULL DEFAULT 'unknown', 
	ADD COLUMN username VARCHAR(50)  NOT NULL DEFAULT 'unknown', 
	ADD COLUMN remoteip VARCHAR(20)  NOT NULL DEFAULT 'unknown', 
	ADD COLUMN insertdate datetime NOT NULL default '0000-00-00 00:00:00', 
	ADD INDEX Index_resp(resp), 
	ADD INDEX Index_username(username), 
	ADD INDEX Index_remoteip(remoteip), 
	ADD INDEX Index_insertdate(insertdate);
		
 
 ALTER TABLE tblpagine 
	ADD COLUMN priority int(10) NOT NULL default '99',
	ADD INDEX Index_priority(priority);
	
 ALTER TABLE tblpagine 
	ADD COLUMN viewsidebar tinyint(1) NOT NULL default '1';
 
	 **/
	
	public static void updateTblPagineForSidebarView(DataSourceComponent datasourceComponent) throws SQLException {
		String sql="ALTER TABLE tblpagine " +
				"ADD COLUMN viewsidebar tinyint(1) NOT NULL default '1'";
		DBGateway.executeStatement(datasourceComponent,sql);
	}
	
	public static void updateTblComponentiForUsernameRemoteip(DataSourceComponent datasourceComponent) throws SQLException {
		String sql="ALTER TABLE tblcomponenti " +
			"ADD COLUMN username VARCHAR(50)  NOT NULL DEFAULT 'unknown', " +
			"ADD COLUMN remoteip VARCHAR(20)  NOT NULL DEFAULT 'unknown', " +
			"ADD INDEX Index_3(username), " +
			"ADD INDEX Index_4(remoteip)";
		
		DBGateway.executeStatement(datasourceComponent,sql);
	}
	
	public static void updateTblPagineForUsernameRemoteip(DataSourceComponent datasourceComponent) throws SQLException {
		String sql="ALTER TABLE tblpagine " +
			"ADD COLUMN resp VARCHAR(50)  NOT NULL DEFAULT 'unknown', " +
			"ADD COLUMN username VARCHAR(50)  NOT NULL DEFAULT 'unknown', " +
			"ADD COLUMN remoteip VARCHAR(20)  NOT NULL DEFAULT 'unknown', " +
			"ADD COLUMN priority int(10) NOT NULL default '99'," +
			"ADD COLUMN insertdate datetime NOT NULL default '0000-00-00 00:00:00', " +
			"ADD INDEX Index_resp(resp), " +
			"ADD INDEX Index_priority(priority), " +
			"ADD INDEX Index_username(username), " +
			"ADD INDEX Index_remoteip(remoteip)," +
			"ADD INDEX Index_insertdate(insertdate)";
		
		DBGateway.executeStatement(datasourceComponent,sql);
	}
	
	public static void updateTblPagineForPriority(DataSourceComponent datasourceComponent) throws SQLException {
		String sql="ALTER TABLE tblpagine " +
			"ADD COLUMN priority int(10) NOT NULL default '99'," +
			"ADD INDEX Index_priority(priority)";
		
		DBGateway.executeStatement(datasourceComponent,sql);
	}
	
	public static void updateComponentCid(DataSourceComponent datasourceComponent,long cid, String username, String remoteip) throws SQLException {
		// current_timestamp
		String sql="update tblcomponenti " +
				"set username = '"+username+"', " +
					"remoteip = '"+remoteip+"', " +
					"InsertDate = current_timestamp " +
				"where CID="+cid;
		DBGateway.executeStatement(datasourceComponent,sql);
		long pid=DBGateway.getPidFromCid(datasourceComponent,cid);
		DBGateway.updateComponentPid(datasourceComponent,pid, username, remoteip);
	}
	
	private static long getPidFromCid(DataSourceComponent datasourceComponent,long cid) throws SQLException {
		long pid=-1;
		String sql="select PID from tblstrutture s,tblcontenuti c " +
				"where c.PaCID=s.CID and " +
				"c.CID="+cid;
		ResultSet rs=null;
    	Statement st=null;
    	Connection conn=null;
    	try {
    		conn=datasourceComponent.getConnection();
	    	st=conn.createStatement();
	    	rs=st.executeQuery(sql);
	    	if(rs.next()){
	    		pid = rs.getLong(1);
	    	}
    	}
    	catch(SQLException e) {
	    	throw e;
    	}
    	finally {
    		if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPidFromCid exception " + fe.getMessage());}
    		if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPidFromCid exception " + fe.getMessage());}
    		if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPidFromCid exception " + fe.getMessage());}
    	}
		return pid;
	}

	public static void updateComponentPid(DataSourceComponent datasourceComponent,long pid, String username, String remoteip) throws SQLException {
		// current_timestamp
		String sql="update tblpagine " +
				"set username = '"+username+"', " +
					"remoteip = '"+remoteip+"', " +
					"insertdate = current_timestamp " +
				"where PID="+pid;
		DBGateway.executeStatement(datasourceComponent,sql);
	}
	
    public static void activateComponent(DataSourceComponent datasourceComponent, long cid, String username, String remoteip) throws SQLException, ComponentException {
    	ResultSet rs=null;
    	Statement st=null;
    	Connection conn=null;
    	try {
    		
	    	executeStatement(datasourceComponent,"update tblcontenuti set StateID=3 where CID="+cid);
	    	conn=datasourceComponent.getConnection();
	    	st=conn.createStatement();
	    	rs=st.executeQuery("select HistoryCid from tblcomponenti where CID="+cid);
	    	if(rs.next()){
	    		long hCid = rs.getLong(1);
	    		executeStatement(datasourceComponent,"update tblcontenuti set StateID=4 where CID="+hCid);
	    	}
	    	updateComponentCid(datasourceComponent,cid, username, remoteip);
    	}
    	catch(SQLException e) {
	    	throw e;
    	}
    	finally {
    		if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.activateComponent exception " + fe.getMessage());}
    		if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.activateComponent exception " + fe.getMessage());}
    		if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.activateComponent exception " + fe.getMessage());}
    	}
	}

	public static void activatePage(DataSourceComponent datasourceComponent, long pid, String username, String remoteip) throws SQLException {
		executeStatement(datasourceComponent,"update tblpagine set Valid=1 where PID="+pid);
		updateComponentPid(datasourceComponent,pid, username, remoteip);
	}
	
	public static void deleteComponent(DataSourceComponent datasourceComponent, long cid, String username, String remoteip) throws SQLException {
		executeStatement(datasourceComponent,"update tblcontenuti set StateID=5 where CID="+cid);
		updateComponentCid(datasourceComponent,cid, username, remoteip);
		long pacid = DBGateway.getPacid(datasourceComponent,cid);
    	OrdinaDB.normalizeDB(datasourceComponent,pacid);
	}

	public static void creaPaginaInDB(DataSourceComponent datasourceComponent, int pid, Request o) throws SQLException {
		String title = o.getParameter("title");
		String papid = o.getParameter("papid");
		int _papid=Integer.parseInt(papid);
		createPage(datasourceComponent,title,pid,_papid);
	}
	
	public static void createPage(DataSourceComponent datasourceComponent, String title, int pid, int papid) throws SQLException {
		String code = doCode(datasourceComponent,title);
		String query1 = "insert into tblpagine (PID,Name,PaPID,Valid,HasChild,PCode,priority) values (?,?,?,0,0,?,99)";
		String query2 = "update tblpagine set HasChild = 1 where PID="+papid;
		
		PreparedStatement ps = null;
		Connection conn=null;
		try {
			conn=datasourceComponent.getConnection();
			ps = conn.prepareStatement(query1);
			ps.setLong(1, pid);
			ps.setString(2, title);
			ps.setLong(3, papid);
			ps.setString(4, code);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("pid: " + pid);
			System.out.println("name: " + title);
			System.out.println("papid: " + papid);
			System.out.println("code: " + code);
			throw e;
		}
		finally {
			if(ps!=null) try{ps.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.createPage exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.createPage exception " + fe.getMessage());}
		}
		executeStatement(datasourceComponent,query2);
	}
    
    private static String doCode(DataSourceComponent datasourceComponent, String name) throws SQLException {
		
    	name = name.toUpperCase();
    	String ret = "";
		String[] words = name.split(" ");
		int index = 0;
		boolean stop=false;
		
		while(!stop && ret.length()<3){
			stop=true;
			for(int i=0;i<words.length;i++){
				if(index<words[i].length()) {
					ret = ret + words[i].substring(index,index+1);
					stop=false;
				}
			}
			index++;
		}
		
		index=ret.length();
		switch(index) {
		case 1:ret=ret+"ZZ";break;
		case 2:ret=ret+"Z";break;
		}
		
		index = 0;
		ret = ret.substring(0,3).toUpperCase();
		if(codeExists(datasourceComponent,ret)){
			String a = ret;
			
			for(int j=2;j>0;j--){
				for(index=0;index<10;index++){
					ret = a.substring(0,j) + index + a.substring(j+1);
					if(!codeExists(datasourceComponent,ret)) break;
				}
				if(!codeExists(datasourceComponent,ret)) break;
			}
		}
		
		if(codeExists(datasourceComponent,ret)){
			String a = ret;
			
			for(index=10;index<100;index++){
				ret = a.substring(0,1) + index;
				if(!codeExists(datasourceComponent,ret)) break;
			}
		}
		
		if(codeExists(datasourceComponent,ret)){
			
			for(index=100;index<1000;index++){
				ret = index + "";
				if(!codeExists(datasourceComponent,ret)) break;
			}
		}
		
		ret = ret.substring(0,3).toUpperCase();
		return ret;
	}


	private static boolean codeExists(DataSourceComponent datasourceComponent, String ret) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		Connection conn=null;
		long a=0;
		try {
			conn=datasourceComponent.getConnection();
			st = conn.prepareStatement("select count(*) from tblpagine where PCode=?");
			st.setString(1, ret);
			rs = st.executeQuery();
			rs.next();
			a = rs.getLong(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.codeExists exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.codeExists exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.codeExists exception " + fe.getMessage());}
		}
		return a>0;
	}

	public static void disableComponent(DataSourceComponent datasourceComponent, long cid, String username, String remoteip) throws SQLException {
		executeStatement(datasourceComponent,"update tblcontenuti set StateID=2 where CID="+cid);
		updateComponentCid(datasourceComponent,cid, username, remoteip);
	}

	public static void disablePage(DataSourceComponent datasourceComponent, long pid, String username, String remoteip) throws SQLException {
		executeStatement(datasourceComponent,"update tblpagine set Valid=0 where PID="+pid);
		updateComponentCid(datasourceComponent,pid, username, remoteip);
	}

	public static String getAreaName(DataSourceComponent datasourceComponent, long pid) throws SQLException{
		ResultSet rs = null;
		Statement st = null;
		String a=null;
		Connection conn=null;

		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs=st.executeQuery("select name from tblpagine where PID=" + getStartFromPid(datasourceComponent,pid));
			rs.next();
			a = rs.getString(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getAreaName exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getAreaName exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getAreaName exception " + fe.getMessage());}
		}
		return a;
	}
	
	public static int getNewPageId(DataSourceComponent datasourceComponent) throws SQLException {
		PreparedStatement ps = null;
		ResultSet temp = null;
		int ret=1;
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			ps = conn.prepareStatement("select max(PID) as max from tblpagine");
			temp = ps.executeQuery();
			if(temp.next()) {
				ret=temp.getInt("max");
				ret++;
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) try{temp.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getNewPageId exception " + fe.getMessage());}
			if(ps!=null) try{ps.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getNewPageId exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getNewPageId exception " + fe.getMessage());}
		}
		return ret;
	}

//	public static int getNewPageId(Connection conn) throws SQLException {
//		PreparedStatement ps = null;
//		ResultSet temp = null;
//		int ret=1;
//		
//		try {
//			ps = conn.prepareStatement("select PID from tblpagine where PID=?");
//			
//			while(true){
//				ps.setLong(1,ret);
//				temp = ps.executeQuery();
//				if(temp.next()){
//					ret++;
//					temp.close();
//				}
//				else{
//					temp.close();
//					break;
//				}
//			}
//		}
//		catch(SQLException e) {
//			
//			throw e;
//		}
//		finally {
//			if(temp!=null) temp.close();
//			if(ps!=null) ps.close();
//		}
//		return ret;
//	}

	public static int getPageLevel(DataSourceComponent datasourceComponent, long pid, int c) throws SQLException {
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		long newpid = 0;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs = st.executeQuery("SELECT PaPID from tblpagine where PID="+pid);
			if(rs.next()){newpid = rs.getLong(1);}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(st!=null) st.close();
			if(conn!=null) conn.close();
		}
		if(newpid==0 || newpid==pid) {
			return c;
		}
		else {
			return getPageLevel(datasourceComponent,newpid,++c);
		}
	
	}

	public static long getPapid(DataSourceComponent datasourceComponent, long pid) throws SQLException {
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		long ret = 0;
		
		try {
			conn=datasourceComponent.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery("select PaPID from tblpagine where PID="+pid);
			if(rs.next())ret = rs.getLong(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPapid exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPapid exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPapid exception " + fe.getMessage());}
		}
		
		return ret;
	}
	
	public static long getNewFreeCid(DataSourceComponent datasourceComponent) throws SQLException {
		PreparedStatement ps = null;
		ResultSet temp = null;
		Connection conn=null;
		long ret=1;
		
		try {
			conn=datasourceComponent.getConnection();
			ps = conn.prepareStatement("select max(CID) as newcid from tblcomponenti");
			temp = ps.executeQuery();
			if(temp.next()){
				ret=temp.getLong("newcid");
			}
			temp.close();
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) try{temp.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getNewFreePid exception " + fe.getMessage());}
			if(ps!=null) try{ps.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getNewFreePid exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getNewFreePid exception " + fe.getMessage());}
		}
		return ++ret;
	}
	
//	public static long getNewFreeCid(Connection conn) throws SQLException {
//		PreparedStatement ps = null;
//		ResultSet temp = null;
//		long ret=1;
//		
//		try {
//			ps = conn.prepareStatement("select CID from tblcomponenti where CID=?");
//	
//			while(true){
//				ps.setLong(1,ret);
//				temp = ps.executeQuery();
//				if(temp.next()){
//					ret++;
//					temp.close();
//				}
//				else{
//					temp.close();
//					break;
//				}
//			}
//		}
//		catch(SQLException e) {
//			throw e;
//		}
//		finally {
//			if(temp!=null) temp.close();
//			if(ps!=null) ps.close();
//		}
//		return ret;
//	}

	public static long getStartFromPid(DataSourceComponent datasourceComponent, long startFromPid) throws SQLException {
		ResultSet rs = null;
		Statement st = null;
		Connection conn=null;
		long newStartFromPid = 0;	long ret = 0;
		
		String query = "SELECT PaPID from tblpagine where PID=" + startFromPid;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs = st.executeQuery(query);
			rs.next();
			newStartFromPid = rs.getLong(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getStartFromPid exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getStartFromPid exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getStartFromPid exception " + fe.getMessage());}
		}
		if(newStartFromPid<=1)ret = startFromPid;
		else ret = getStartFromPid(datasourceComponent,newStartFromPid);
		
		return ret;
	}

	public static long getState(DataSourceComponent datasourceComponent, long id) throws SQLException{
		ResultSet rsState = null;
		Statement st = null;
		Connection conn = null;
		long ret = 0;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rsState = st.executeQuery("select StateID from tblcontenuti where CID = "+id);
			if(rsState.next()) ret = rsState.getLong(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rsState!=null) try{rsState.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getState exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getState exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getState exception " + fe.getMessage());}
		}
		return ret;
	}

	public static String getTypeForCid(DataSourceComponent datasourceComponent, long cid) throws SQLException {
		ResultSet rs = null;
		Statement st = null;
		Connection conn=null;
		String ret = "";
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs=st.executeQuery("select Type from tblcomponenti where CID=" + cid);
			if(rs.next()){
				ret = rs.getString(1);
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getState exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getState exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getState exception " + fe.getMessage());}
		}
		return ret;
	}
    
    public static boolean is2Validate(DataSourceComponent datasourceComponent, long id) throws SQLException {
		return getState(datasourceComponent, id)==2;
	}
	
	public static boolean isValid(DataSourceComponent datasourceComponent, long id) throws SQLException {
		return getState(datasourceComponent, id)==3;
	}
	
	public static boolean isPageValid(DataSourceComponent datasourceComponent, long pid) throws SQLException {
		long ret = 0;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs = st.executeQuery("select Valid from tblpagine where PID="+pid);
			if(rs.next()) ret = rs.getLong(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.isPageValid exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.isPageValid exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.isPageValid exception " + fe.getMessage());}
		}
		return ret==1;
	}
	
	public static boolean isPageInSidebar(DataSourceComponent datasourceComponent, long pid) throws SQLException {
		Statement st = null;
		long ret = 0;
		ResultSet rs = null;
		Connection conn=null;
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs = st.executeQuery("select InSidebar from tblpagine where PID="+pid);
			if(rs.next()) ret = rs.getLong(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.isPageInSidebar exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.isPageInSidebar exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.isPageInSidebar exception " + fe.getMessage());}
		}
		return ret==1;
	}

	public static long getPacid(DataSourceComponent datasourceComponent, long id) throws SQLException {
		long ret = 0;
		ResultSet rs = null;
		Statement st = null;
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs = st.executeQuery("select PaCID from tblcontenuti where CID="+id);
			if(rs.next()) ret = rs.getLong(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPacid exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPacid exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPacid exception " + fe.getMessage());}
		}
		return ret;
	}

	public static long getOrderNumber(DataSourceComponent datasourceComponent, long cid, long pacid) throws SQLException {
		long ret = -1;
		ResultSet rs = null;
		Statement st = null;
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs = st.executeQuery("select OrderNumber from tblcontenuti where CID="+cid + " and PaCID="+pacid);
			if(rs.next()) ret = rs.getLong(1);
			
			if(ret==-1){
				if(rs!=null) rs.close();
				rs = st.executeQuery("select max(OrderNumber) from tblcontenuti where PaCID="+pacid);
				if(rs.next()) ret = rs.getLong(1);
				ret++;
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getOrderNumber exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getOrderNumber exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getOrderNumber exception " + fe.getMessage());}
		}
		return ret;
	}
	
	public static long getOrderNumber(DataSourceComponent datasourceComponent, long cid) throws SQLException {
		return getOrderNumber(datasourceComponent, cid, DBGateway.getPacid(datasourceComponent, cid));
	}

	public static boolean hasNewVersion(DataSourceComponent datasourceComponent, long id) throws SQLException {
		int ret = 0;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs = st.executeQuery("select count(*) from tblcomponenti where HistoryCid="+id);
			if(rs.next()) ret = rs.getInt(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.hasNewVersion exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.hasNewVersion exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.hasNewVersion exception " + fe.getMessage());}
		}
		return ret>0;
	}
	
	public static boolean hasChild(DataSourceComponent datasourceComponent,long pid, String remoteaddr, Session session) throws SQLException, JOpac2Exception {
		if(pid==0)return false;
		int ret = 0;
		ResultSet rs = null;
		Statement st = null;
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs = st.executeQuery("select PID,Valid from tblpagine where PaPID="+pid);
			while(rs.next()){
				long tempPid = rs.getLong("PID");
				Permission tempP = Authentication.assignPermissions(datasourceComponent,session, remoteaddr, tempPid);
				if(tempP.hasPermission(Permission.ACCESSIBLE)) ret++;
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.hasChild exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.hasChild exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.hasChild exception " + fe.getMessage());}
		}
		
		return ret>0;
	}

	public static void setPending(DataSourceComponent datasourceComponent, long cid) throws SQLException {
		executeStatement(datasourceComponent,"update tblcontenuti set StateID=2 where CID="+cid);
	}

	public static void setWorking(DataSourceComponent datasourceComponent, long cid) throws SQLException {
		executeStatement(datasourceComponent, "update tblcontenuti set StateID=1 where CID="+cid);
	}

	public static void setHasChild(DataSourceComponent datasourceComponent, long id) throws SQLException {
		executeStatement(datasourceComponent,"update tblcomponenti set HasChildren=1 where CID="+id);
	}

//	public static void orderComponents(Vector<String> cids, Vector<String> ordernumbers, String pacid, Connection conn) throws SQLException {
//		
//		String query = "update tblcontenuti set OrderNumber=? where CID = ?";
//		if(!(pacid.equals(""))) query = query + " and PaCID = " + pacid;
//		
//		PreparedStatement ps = null;
//		
//		try {
//			ps = conn.prepareStatement("update tblcontenuti set OrderNumber=? where CID = ?");
//			
//			for( int i=0; i<cids.size() ; i++ ){
//				String cid = ((String)cids.get(i)).substring(3);
//				ps.setString(1, (String)ordernumbers.get(i));
//				ps.setString(2, cid);
//				ps.executeUpdate();
//				
//				Vector<String> fc = futureCid(cid, conn);
//				Iterator<String> efc = fc.iterator();
//				while(efc.hasNext()){
//					String q = "update tblcontenuti set OrderNumber=" + ordernumbers.get(i) +
//								" where CID=" + efc.next();
//					executeStatement(conn, q);
//				}
//				
//			}
//		}
//		catch(SQLException e) {
//			throw e;
//		}
//		finally {
//			if(ps!=null) ps.close();
//		}
//	}

//	private static Vector<String> futureCid(String cid, Connection conn) throws SQLException {
//		Vector<String> ret = new Vector<String>();
//		ResultSet temp = null;
//		Statement st = null;
//		
//		try {
//			st=conn.createStatement();
//			temp=st.executeQuery("select CID from tblcomponenti where HistoryCid="+cid);
//			while(temp.next()){
//				ret.add(temp.getString(1));
//			}
//		}
//		catch(SQLException e) {
//			throw e;
//		}
//		finally {
//			if(temp!=null) temp.close();
//			if(st!=null) st.close();
//		}
//		return ret;
//	}

	public static long getStateCode(DataSourceComponent datasourceComponent, String state) throws SQLException {
		long ret = 0;
		PreparedStatement sqlState = null;
		ResultSet temp = null;
		Connection conn=null;

		try{
			conn=datasourceComponent.getConnection();
			sqlState = conn.prepareStatement("select StateID from tblstati where StateName=?");
			sqlState.setString(1, state);
			sqlState.executeQuery();
			
			temp = sqlState.getResultSet();
			temp.next();
			ret = temp.getLong(1);
		}catch(SQLException sqle){
			System.out.println("Eccezione con query: select StateID from tblstati where StateName='"+state+"'");
			System.out.println(sqle.getMessage());
		}
		finally {
			if(temp!=null) try{temp.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getStateCode exception " + fe.getMessage());}
			if(sqlState!=null) try{sqlState.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getStateCode exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getStateCode exception " + fe.getMessage());}
		}
		return ret;
	}

	public static String getPageName(DataSourceComponent datasourceComponent, long pid) throws SQLException {
		ResultSet temp = null;
		String ret = null;
		Statement st = null;
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			temp=st.executeQuery("select Name from tblpagine where PID="+pid);
			temp.next();
			ret = temp.getString(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) try{temp.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPageName exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPageName exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPageName exception " + fe.getMessage());}
		}
		return ret;
	}
	
	public static Document getPageMetadata(DataSourceComponent datasourceComponent, String l, Map<String, String> lastdata) throws SQLException, SAXException, IOException, ParserConfigurationException {
		String sql="select * from tblpagine where pcode='"+l+"'";
		return _getPageMetadata(datasourceComponent, sql, lastdata);
	}
	
	public static Document getPageMetadata(DataSourceComponent datasourceComponent, long pid, Map<String, String> lastdata) throws SQLException, SAXException, IOException, ParserConfigurationException {
		String sql="select * from tblpagine where PID="+pid;
		return _getPageMetadata(datasourceComponent, sql, lastdata);
	}
	
	
	private static Document _getPageMetadata(DataSourceComponent datasourceComponent, String sql, Map<String, String> lastdata) throws SQLException, SAXException, IOException, ParserConfigurationException {
		ResultSet temp = null;
		Statement st = null;
		Connection conn=null;
		
		StringBuffer d=new StringBuffer("<metadata>\n");
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			try {
				temp=st.executeQuery(sql);
			}
			catch (SQLException e2) {
				try {
					caricaDB(datasourceComponent);
					temp=st.executeQuery(sql);
				}
				catch(SQLException e1) {
					throw e1;
				}
				
			}
			if(temp.next()) {
				ResultSetMetaData meta=temp.getMetaData();
				for(int i=1;i<=meta.getColumnCount();i++) {
					String column=meta.getColumnName(i).toLowerCase();
					int type=meta.getColumnType(i);
					String value="";
					if(type==Types.TIMESTAMP) {
						try {
							Timestamp date=temp.getTimestamp(i);
							value=date.toString();
							GregorianCalendar gc = new GregorianCalendar();
							gc.setTimeInMillis(date.getTime());
							lastdata.put(column+"_YEAR", Integer.toString(gc.get(GregorianCalendar.YEAR)));
							lastdata.put(column+"_MONTH", pad(Integer.toString(gc.get(GregorianCalendar.MONTH)+1)));
							lastdata.put(column+"_DAY", pad(Integer.toString(gc.get(GregorianCalendar.DAY_OF_MONTH))));
							lastdata.put(column+"_HOUR", pad(Integer.toString(gc.get(GregorianCalendar.HOUR_OF_DAY))));
							lastdata.put(column+"_MINUTE", pad(Integer.toString(gc.get(GregorianCalendar.MINUTE))));
							lastdata.put(column+"_SECOND", pad(Integer.toString(gc.get(GregorianCalendar.SECOND))));
							d.append("<"+column+">\n");
							d.append("<year>"+Integer.toString(gc.get(GregorianCalendar.YEAR))+"</year>\n");
							d.append("<month>"+pad(Integer.toString(gc.get(GregorianCalendar.MONTH)+1))+"</month>\n");
							d.append("<day>"+pad(Integer.toString(gc.get(GregorianCalendar.DAY_OF_MONTH)))+"</day>\n");
							d.append("<hour>"+pad(Integer.toString(gc.get(GregorianCalendar.HOUR_OF_DAY)))+"</hour>\n");
							d.append("<minute>"+pad(Integer.toString(gc.get(GregorianCalendar.MINUTE)))+"</minute>\n");
							d.append("<second>"+pad(Integer.toString(gc.get(GregorianCalendar.SECOND)))+"</second>\n");
							d.append("</"+column+">\n");
						}
						catch(Exception e) {}
					}
					else {
						
						value=temp.getString(i);
					}
					if(value!=null) {
						d.append("<"+column+">"+value+"</"+column+">\n");
						lastdata.put(column, value);
					}
				}
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) try{temp.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway._getPageMetadata exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway._getPageMetadata exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway._getPageMetadata exception " + fe.getMessage());}
		}
		d.append("</metadata>");
		return XMLUtil.String2XML(d.toString());
	}
	
	private static String pad(String string) {
		String l="0";
		if(string.length()<2) l=l+string;
		else l=string;
		return l;
	}

	public static String getPageCode(DataSourceComponent datasourceComponent, long pid) throws SQLException {
		ResultSet temp = null;
		String ret = null;
		Statement st = null;
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			temp=st.executeQuery("select PCode from tblpagine where PID="+pid);
			temp.next();
			ret = temp.getString(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) try{temp.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPageCode exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPageCode exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPageCode exception " + fe.getMessage());}
		}
		return ret;
	}
	
	public static void setPageName(DataSourceComponent datasourceComponent, long pid, String newTitle) throws SQLException {
		String temp = newTitle;
		PreparedStatement ps =null;
		Connection conn=null;
		try {
			conn=datasourceComponent.getConnection();
			ps = conn.prepareStatement("update tblpagine set Name=? where PID=?");
			ps.setString(1, temp);
			ps.setLong(2, pid);
			ps.execute();
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(ps!=null) try{ps.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.setPageName exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.setPageName exception " + fe.getMessage());}
		}
	}
	
	public static void setPageCode(DataSourceComponent datasourceComponent, long pid, String newCode) throws SQLException {
		executeStatement(datasourceComponent,"update tblpagine set PCode='"+newCode+"' where PID="+pid);
	}

	public static void setPageResp(DataSourceComponent datasourceComponent, long pid, String newCode) throws SQLException {
		executeStatement(datasourceComponent,"update tblpagine set resp='"+newCode+"' where PID="+pid);
	}

	
	public static boolean pageExists(DataSourceComponent datasourceComponent, long pid) throws SQLException {
		ResultSet temp =null;
		boolean ret = false;
		Statement st = null;
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			temp = st.executeQuery("select Name from tblpagine where PID="+pid);
			ret = temp.next();
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) try{temp.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.pageExists exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.pageExists exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.pageExists exception " + fe.getMessage());}
		}
		return ret;
	}
	
//	try {
//	sqlState.executeQuery(sql);
//}
//catch(SQLException e) {
//	// Cambiato nome colonna
//	String alter="ALTER TABLE tblroles CHANGE COLUMN User Username VARCHAR(50)";
//	int n=e.getErrorCode();
//	if(n==1054) {
//		try {
//			sqlState.execute(alter);
//			sqlState.executeQuery(sql);
//		}
//		catch(Exception ez) {
//			System.out.println("ALTER ERROR DBGateway.getPermission: "+ez.getMessage());
//			sqlState.close();
//		}
//	}
//	else {
//		sqlState.close();
//		if(n==30000) throw e;
//		else throw new JOpac2Exception("User/Username exception");
//	}
//}

	public static Permission getPermission(DataSourceComponent datasourceComponent, String username, String remoteaddr, long pid) throws SQLException, JOpac2Exception {
		String sql="select PermissionCode from tblroles where PID="+pid+" and Username='"+username+"'";
		ResultSet temp =null;
		Statement st = null;
		Connection conn=null;
		byte ret = -1;
		
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			temp=st.executeQuery(sql);

			if(temp.next()){
				ret = temp.getByte(1);
			}
			
			if(ret==-1) {
				if(temp!=null) temp.close();
				temp = st.executeQuery("select PermissionCode from tblroles where PID=0 and Username='"+username+"'");
				if(temp.next()){
					ret=temp.getByte(1);
				}
				else{
					if(DBGateway.isPageValid(datasourceComponent,pid)) ret = 1;
					else ret = 0;
				}
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) try{temp.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPermission resultset exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPermission statement exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPermission connection exception " + fe.getMessage());}
		}
		Permission p = new Permission();
		p.setPermissionCode(ret);
		
		return p;
	}

	public static long getPidFrom(DataSourceComponent datasourceComponent, String pcode) throws SQLException {
		long ret = -1;
		ResultSet temp = null;
		PreparedStatement st = null;
		String sql="select PID from tblpagine where PCode= ? "; //'"+pcode+"'";
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.prepareStatement(sql);
			st.setString(1, pcode);
			temp=st.executeQuery();
			if(temp.next()){
				ret = temp.getLong(1);
			}
		}
		catch(SQLException e) {
			System.err.println("Query: "+sql);
			throw e;
		}
		finally {
			if(temp!=null) try{temp.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPidFrom exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPidFrom exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getPidFrom exception " + fe.getMessage());}
		}
		return ret;
	}

	public static void setPageInSidebar(DataSourceComponent datasourceComponent, long pid, boolean insidebar) throws SQLException {
		String query = "update tblpagine set InSidebar=0 where PID="+pid;
		if(insidebar) query = "update tblpagine set InSidebar=1 where PID="+pid;
		executeStatement(datasourceComponent, query);
		
	}
	
	public static void setPageViewSidebar(DataSourceComponent datasourceComponent, long pid, boolean viewsidebar) throws SQLException {
		String query = "update tblpagine set viewsidebar=0 where PID="+pid;
		if(viewsidebar) query = "update tblpagine set viewsidebar=1 where PID="+pid;
		try {
			executeStatement(datasourceComponent, query);
		}
		catch(Exception e) {
			updateTblPagineForSidebarView(datasourceComponent);
			executeStatement(datasourceComponent, query);
		}
	}

	public static String getRedirectURL(DataSourceComponent datasourceComponent, long pageId) throws SQLException {
		String url = null;
		ResultSet temp = null;
		Statement st = null;
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			temp=st.executeQuery("select Url from tblredirects where PID="+pageId);
			if(temp.next()){
				url = temp.getString(1);
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) try{temp.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getRedirectURL exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getRedirectURL exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getRedirectURL exception " + fe.getMessage());}
		}
		return url;
	}
	
	public static void erasePage(DataSourceComponent datasourceComponent, long pid, String username, String remoteip) throws SQLException{
		String q = "select CID from tblstrutture where PID="+pid;
		Statement st = null;
		ResultSet containers = null;
		ResultSet childPages = null;
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			containers=st.executeQuery(q);
			while(containers.next()){
				long cid = containers.getLong(1);
				if(uniquePageRelation(datasourceComponent,pid, cid)){
					eraseContent(datasourceComponent,cid, username, remoteip);
				}
			}
			
			q = "select PID from tblpagine where PaPID="+pid;
			
			childPages = st.executeQuery(q);
			while(childPages.next()){
				long childpid = childPages.getLong(1);
				erasePage(datasourceComponent,childpid,username,remoteip);
			}
			updateComponentPid(datasourceComponent,pid, username, remoteip);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(containers!=null) try{containers.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.erasePage exception " + fe.getMessage());}
			if(childPages!=null) try{childPages.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.erasePage exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.erasePage exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.erasePage exception " + fe.getMessage());}
		}

    	executeStatement(datasourceComponent,"delete from tblstrutture where PID="+pid);
    	executeStatement(datasourceComponent,"delete from tblpagine where PID="+pid);
    	executeStatement(datasourceComponent,"delete from tblredirects where PID="+pid);
    	executeStatement(datasourceComponent,"delete from tblroles where PID="+pid);
	}

	private static void eraseContent(DataSourceComponent datasourceComponent,long cid, String username, String remoteip) throws SQLException {
		String q = "select CID from tblcontenuti where PaCID="+cid;
		ResultSet children = null;
		Statement st = null;
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			children = st.executeQuery(q);
			while(children.next()){
				long childId = children.getLong(1);
				eraseComponent(datasourceComponent,childId, username, remoteip);
			}
			updateComponentCid(datasourceComponent,cid, username, remoteip);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(children!=null) try{children.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.eraseContent exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.eraseContent exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.eraseContent exception " + fe.getMessage());}
		}
		
		executeStatement(datasourceComponent,"delete from tblstrutture where CID="+cid);
		executeStatement(datasourceComponent,"delete from tblcomponenti where CID="+cid);
	}

	private static void eraseComponent(DataSourceComponent datasourceComponent, long cid, String username, String remoteip) throws SQLException {
		ResultSet hCid = null;
		Statement st = null;
		Connection conn=null;
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			hCid=st.executeQuery("select HistoryCid from tblcomponenti where CID="+cid);
			if(hCid.next()){
				long historyCid = hCid.getLong(1);
				if(historyCid!=0){
					updateComponentCid(datasourceComponent,historyCid, username, remoteip);
					eraseComponent(datasourceComponent,historyCid, username, remoteip);
				}
			}
			updateComponentCid(datasourceComponent,cid, username, remoteip);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(hCid!=null) try{hCid.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.eraseComponent exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.eraseComponent exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.eraseComponent exception " + fe.getMessage());}
		}
		executeStatement(datasourceComponent,"delete from tblcontenuti where CID="+cid);
		executeStatement(datasourceComponent,"delete from tblcomponenti where CID="+cid);
	}

	private static boolean uniquePageRelation(DataSourceComponent datasourceComponent, long pid, long cid) throws SQLException {
		boolean ret = true;
		String q = "select PID from tblstrutture where CID="+cid;
		ResultSet pages = null;
		Statement st = null;
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			pages = st.executeQuery(q);
			while(pages.next()){
				long tempPid = pages.getLong(1);
				if(tempPid!=pid){
					ret = false;
					break;
				}
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(pages!=null) try{pages.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.uniquePageRelation exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.uniquePageRelation exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.uniquePageRelation exception " + fe.getMessage());}
		}
		return ret;
	}
	
	public static boolean isLeaf(DataSourceComponent datasourceComponent, long tempPid, String remoteaddr, Session session) throws SQLException, JOpac2Exception {
		boolean ret = true;
		String q = "select PID from tblpagine where PaPID="+tempPid;
		ResultSet pages = null;
		Statement st = null;
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			pages=st.executeQuery(q);
			while(pages.next()){
				long pid = pages.getLong(1);
				Permission tempperm = Authentication.assignPermissions(datasourceComponent,session, remoteaddr, pid);
				if(	( tempperm.hasPermission(Permission.ACCESSIBLE) && DBGateway.isPageInSidebar(datasourceComponent,pid) ) ||
					  tempperm.hasPermission(Permission.EDITABLE) || tempperm.hasPermission(Permission.VALIDABLE) ){
					ret = false;
					break;
				}
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(pages!=null) try{pages.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.isLeaf exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.isLeaf exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.isLeaf exception " + fe.getMessage());}
		}
		return ret;
	}

	public static void setPermission(DataSourceComponent datasourceComponent, Long pid, String userData, Permission p) {
		
		String query1 = "";
		Permission tempP = null;
		try {
			tempP = DBGateway.getPermission(datasourceComponent, userData, null, 0);
		} catch (Exception e2) {e2.printStackTrace();}
		
		if(tempP.getPermissionCode() != p.getPermissionCode() || p.getPermissionCode() == 0 )
		{		
			query1  = "insert into tblroles values('"+userData+"'," + (int)p.getPermissionCode() +","+pid+")";
			
			if(p.getPermissionCode() == 0){
				query1 = "delete from tblroles where Username='" + userData + "' and PID="+pid;
			}
			
			
			
			try {
				executeStatement(datasourceComponent,query1);
			} catch (SQLException e) {
				query1  = "update tblroles set PermissionCode=" + (int)p.getPermissionCode() +
							" where Username = '" + userData + "' and PID=" + pid;
				try{
					executeStatement(datasourceComponent,query1);
				}catch(Exception e1){e1.printStackTrace();}
			}
		}
		
	}
	
	public static void setPermission(DataSourceComponent datasourceComponent, Long pid, String userData) {
		Permission p = new Permission();
		p.setPermissionCode((byte)15);
		setPermission(datasourceComponent, pid, userData, p);
		
	}
	
	public static void linkPageContainers(DataSourceComponent datasourceComponent, long ret, long pid) throws SQLException {
		PreparedStatement ps = null;
		Connection conn=null;
		try {
			conn=datasourceComponent.getConnection();
			ps = conn.prepareStatement("insert into tblstrutture (PID,CID) values (?,?)");
			ps.setLong(1,pid);
			for(int i=1;i<5;i++){
				if(i==3)ps.setLong(2,ret);
				else if(i==2)ps.setLong(2,11);
				else ps.setLong(2,i);
				ps.execute();
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(ps!=null) try{ps.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.linkPageContainers exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.linkPageContainers exception " + fe.getMessage());}
		}
	}
	
	public static void saveDBComponent(DataSourceComponent datasourceComponent, long cid, String componentType, int hasChild, long historycid, Date date, 
			long pid, String url, String user, String remoteIp) throws SQLException {
//		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		
		String sqlComponenteNuovo = "INSERT INTO tblcomponenti (InsertDate,CID,Type,Attributes,HasChildren,HistoryCid, username, remoteip)" + // ,InsertDate
									"VALUES (current_timestamp," + cid + ",'" + componentType + "',null,"+ hasChild+"," + historycid+",'" + user+"','" + remoteIp + "')"; // ,'" + sqlDate + "'
		executeStatement(datasourceComponent, sqlComponenteNuovo);
		
		if(componentType.equals("externalLink")){
			String redir = DBGateway.getRedirectURL(datasourceComponent, pid);
			
			if(redir==null){
				String sqlExternalLink = "INSERT INTO tblredirects (PID,Url) " + 
										 "VALUES (" + pid + ",'" + url + "')";
				
				executeStatement(datasourceComponent, sqlExternalLink);
			}

		}
	}
	
	public static void saveDBComponentAndRelationship(DataSourceComponent datasourceComponent, long id, String componentType, int hasChild, long pacid, 
			long historycid, Date date, long intStatecode, long pid, String url, long order, String user, String remoteIp) throws SQLException {
		
		long orderNumber = 1;
		if(order == -1)
			orderNumber = DBGateway.getOrderNumber(datasourceComponent, historycid, pacid);
		else{
			orderNumber = order;
			updateOrder(datasourceComponent,order, pacid);
		}
		saveDBComponent(datasourceComponent, id, componentType,hasChild,historycid,date,pid,url,user,remoteIp);
		
		if(intStatecode==0){
			System.out.println("Wait... there's a problem\nState Code is 0... Couldn't be!");
		}
		else{
			if(componentType.equals("news")) intStatecode = 3;
			saveCIDrelationship(datasourceComponent,id,pacid,intStatecode,orderNumber);
			
			long state = DBGateway.getState(datasourceComponent,id);
			if(state==0){
				Connection conn=datasourceComponent.getConnection();
				String dbname=conn.getCatalog();
				conn.close();
				System.out.println("Wait... there's a problem\nState Code is 0... Couldn't be!");
//				System.out.println("User Data: " + Authentication.getUserData(session, "ID"));
				System.out.println("while saving: cid="+id);
				System.out.println("              pid="+pid);
				System.out.println("              db ="+dbname);
				System.out.println("Inserted State Code was "+intStatecode);

				System.out.println("Please execute 'UPDATE tblcontenuti set IDStato=1 where IDStato=0' on "+dbname);
			}
		}
	}

	public static void saveCIDrelationship(DataSourceComponent datasourceComponent, long cid, long pacid,
			long state, long orderNumber) throws SQLException {
		String sqlRelazioneNuovo = "INSERT INTO tblcontenuti (PaCID,CID,StateID,OrderNumber)" + 
				   "VALUES (" + pacid + "," + cid + "," + state + "," + (orderNumber) + ")";
		executeStatement(datasourceComponent, sqlRelazioneNuovo);
		
	}

	public static void updateOrder(DataSourceComponent datasourceComponent, long ord, long pacid) throws SQLException {
		ResultSet rs = null;
		Statement st = null;
		PreparedStatement ps = null;
		Connection conn = null;
		try{
			conn=datasourceComponent.getConnection();
			String select = "select CID, OrderNumber from tblcontenuti where PaCID="+pacid+" and OrderNumber>="+ord;
			ps = conn.prepareStatement("update tblcontenuti set OrderNumber = ? where CID = ? and PaCID="+pacid);
			
			st=conn.createStatement();
			rs = st.executeQuery(select);
			long currCid = 0;
			long currOrd = 0;
			while(rs.next()){
				currCid = rs.getLong(1);
				currOrd = rs.getLong(2);
				ps.setLong(1,currOrd+1);
				ps.setLong(2,currCid);
				ps.execute();
			}
		}catch(SQLException e){
			e.printStackTrace();
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.updateOrder exception " + fe.getMessage());}
			if(ps!=null) try{ps.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.updateOrder exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.updateOrder exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.updateOrder exception " + fe.getMessage());}
		}
	}

	public static long getContentCID(DataSourceComponent datasourceComponent, long pageId) throws SQLException {
		long cid = 0;
		String query = "SELECT tblstrutture.CID " +
						"FROM tblcomponenti INNER JOIN tblstrutture ON tblcomponenti.CID = tblstrutture.CID " +
						"WHERE (((tblstrutture.PID)=" + pageId+ ") AND ((tblcomponenti.Type)='content'))";
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs = st.executeQuery(query);
			if(rs.next()){
				cid = rs.getLong(1);
			}
		}
		catch(SQLException e){
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getContentCID exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getContentCID exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getContentCID exception " + fe.getMessage());}
		}
		return cid;
	}
	
	public static Vector<NewsItem> getNewsCID(DataSourceComponent datasourceComponent, String date) throws SQLException {
		Vector<NewsItem> v = new Vector<NewsItem>();
		String query =	"SELECT s.PID as pid, " +
						"p.Name as pagename, " +
						"c.CID as cid, " +
						"co.InsertDate as InsertDate, " +
						"n.startdate as startdate, " +
						"n.enddate as enddate " +
						"FROM tblnews n,  " +
						"tblcontenuti c,  " +
						"tblcomponenti co, " +
						"tblstrutture s, " +
						"tblpagine p " +
						"WHERE c.CID=n.CID  " +
						"AND c.StateID=3 " +
						"AND co.CID=c.CID " +
						"AND n.list=1 " +
						"AND n.enddate>='"+date+"' " +
						"AND n.startdate<='"+date+"' " +
						"AND s.CID=c.PaCID " +
						"AND p.PID=s.PID " +
						"ORDER BY co.InsertDate DESC";
		ResultSet rs = null;
		Statement st = null;
		Connection conn=null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs = st.executeQuery(query);
			while(rs.next()){
				long pid=rs.getLong("pid");
				long cid=rs.getLong("cid");
				v.add(new NewsItem(pid,
						rs.getString("pagename"),
						cid,
						rs.getString("InsertDate"),
						rs.getString("startdate"),
						rs.getString("enddate")));
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getNewsCID exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getNewsCID exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getNewsCID exception " + fe.getMessage());}
		}
		return v;
	}
	
	public static boolean isGrandParent(DataSourceComponent datasourceComponent, long pid, long child) throws SQLException {
		boolean r=false;
		long papid=child;
		while(papid>1 && papid!=pid) {
			papid=DBGateway.getPapid(datasourceComponent,papid);
		}
		if(papid==pid) r=true;
		return r;
	}
	
	public static void caricaDB(DataSourceComponent datasourceComponent) throws SQLException {
		Connection conn=null;
		try {
			conn=datasourceComponent.getConnection();
			caricaDB(conn);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.caricaDB exception " + fe.getMessage());}
		}
	}

	public static void caricaDB(Connection conn) throws SQLException {
		DbSetup dbs=DbSetup.getInstance(conn.toString());
		if(!dbs.existTable(conn, "info")) {
			dbs.createTableInfo(conn);
			dbs.loadTableInfo(conn);
		}
		if(!dbs.existTable(conn, "tblcomponenti")) {
			dbs.createTableTblComponenti(conn);
			dbs.loadTableTblComponenti(conn);
		}
		if(!dbs.existTable(conn, "tblcontenuti")) {
			dbs.createTableTblContenuti(conn);
			dbs.loadTableTblContenuti(conn);
		}
		if(!dbs.existTable(conn, "tblpagine")) {
			dbs.createTableTblPagine(conn);
			dbs.loadTableTblPagine(conn);
		}
		if(!dbs.existTable(conn, "tblredirects")) {
			dbs.createTableTblRedirects(conn);
			dbs.loadTableTblRedirects(conn);
		}
		if(!dbs.existTable(conn, "tblroles")) {
			dbs.createTableTblRoles(conn);
			dbs.loadTableTblRoles(conn);
		}
		if(!dbs.existTable(conn, "tblstrutture")) {
			dbs.createTableTblStrutture(conn);
			dbs.loadTableTblStrutture(conn);
		}
		if(!dbs.existTable(conn, "tblstati")) {
			dbs.createTableTblStati(conn);
			dbs.loadTableTblStati(conn);
		}
		if(!dbs.existTable(conn, "tblnews")) {
			dbs.createTableNews(conn);
		}
	}

	public static Vector<Long> getChildPages(DataSourceComponent datasourceComponent, long pageId) throws SQLException {
		Vector<Long> v = new Vector<Long>();
		String query =	"SELECT PID FROM tblpagine where PaPID="+pageId;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs=st.executeQuery(query);
			while(rs.next()){
				v.add(rs.getLong(1));
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getChildPages exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getChildPages exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getChildPages exception " + fe.getMessage());}
		}
		return v;
		
	}

	public static String getLastModifiedPID(DataSourceComponent datasourceComponent, long pageId) throws SQLException {
		String r = "";
		String query =	"SELECT last PID FROM tblpagine where PaPID="+pageId;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs=st.executeQuery(query);
			while(rs.next()){
				r=rs.getString(1);
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getLastModifiedPID exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getLastModifiedPID exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.getLastModifiedPID exception " + fe.getMessage());}
		}
		return r;
		
	}

	public static void setPagePriority(DataSourceComponent datasourceComponent, String code, String priority) throws SQLException {
		executeStatement(datasourceComponent, "update tblpagine set priority="+priority+" where PCode='"+code+"'");
	}

	public static void setPaPid(DataSourceComponent datasourceComponent, long pid, long papid) throws SQLException {
		executeStatement(datasourceComponent, "update tblpagine set PaPID="+papid+" where PID="+pid);
	}

	public static void clonePermission(DataSourceComponent datasourceComponent, int sourcepid, int destpid) throws SQLException {
		String sql="insert into tblroles (username,permissioncode,pid) " +
				"select username,permissioncode,"+destpid+" from tblroles where pid="+sourcepid;
		executeStatement(datasourceComponent, sql);
	}

	public static long createComponent(DataSourceComponent datasourceComponent, String component) throws SQLException {
		long newComponent=-1;

		newComponent=getNewFreeCid(datasourceComponent);
		
		return newComponent;
	}

	public static void cloneComponents(DataSourceComponent datasourceComponent, int pid, int sourcePid, String dataDirectory, String username,
			String remoteaddr) throws SQLException {
		
		long sourceContentCid=getContentCID(datasourceComponent, sourcePid);
		long destContentCid=getContentCID(datasourceComponent, pid);
		String url=null;
		setHasChild(datasourceComponent, destContentCid);
		String componentType="section"; // esempio
		long sourceCid=0,destCid;
		long orderNumber=1;
		String sql="select a.cid, b.type " +
				"from tblcontenuti a,tblcomponenti b " +
				"where a.pacid="+sourceContentCid+" and a.cid=b.cid and a.stateid=3 " +
				"order by a.ordernumber";
		
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		
		try {
			conn=datasourceComponent.getConnection();
			st=conn.createStatement();
			rs=st.executeQuery(sql);
			while(rs.next()){
				componentType=rs.getString("type");
				sourceCid=rs.getLong("cid");
				destCid = DBGateway.getNewFreeCid(datasourceComponent);
				DBGateway.saveDBComponent(datasourceComponent, destCid,componentType,0,0,new java.util.Date(), pid, url, 
						username, remoteaddr);
				DBGateway.saveCIDrelationship(datasourceComponent, destCid, destContentCid, 2, orderNumber++);
				DBGateway.setPending(datasourceComponent, destCid);
				
				File s=new File(dataDirectory+"/data/"+componentType+sourceCid+".xml");
				File d=new File(dataDirectory+"/data/"+componentType+destCid+".xml");
				
				try {
					FileUtils.copyFile(s, d);
				} catch (IOException e) {
//					e.printStackTrace();
					System.err.println(e.getMessage());
				}
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.cloneComponents exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.cloneComponents exception " + fe.getMessage());}
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.cloneComponents exception " + fe.getMessage());}
		}
	}

	/**
	 * Exhcanche cid order on dir direction
	 * dir = next or otherwise
	 * @param cid
	 * @param pacid
	 * @param dir
	 * @param username
	 * @param remoteAddr
	 * @param conn
	 * @throws SQLException 
	 */
	public static synchronized void exchangeCidOrder(DataSourceComponent datasourceComponent, long cid, long pacid, String dir,
			String username, String remoteAddr) throws SQLException {
		int order=-1;
		int previous=-1;
		int next=-1;
		int lastprev=-1;
		int exch=-1;
		String sql="select * from tblcontenuti where pacid="+pacid+" and stateid<4 order by ordernumber";
		
		
		
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		String update="update tblcontenuti set ordernumber=? where pacid="+pacid+" and ordernumber=?";
		
		String correctOrder="update tblcontenuti t1 " +
				"inner join (select @rownum:=@rownum+1 posizione, t.* from tblcontenuti t, " +
				"(select @rownum:=0) r where pacid="+pacid+" order by ordernumber) o " +
				"on t1.cid = o.cid " +
				"set t1.ordernumber = o.posizione";
		
		PreparedStatement pst=null;
		
				
		Vector<Component> components=new Vector<Component>();
		
		try {
			conn=datasourceComponent.getConnection();
			pst=conn.prepareStatement(update);
			st=conn.createStatement();
			st.execute(correctOrder);
			rs=st.executeQuery(sql);
			while(rs.next()){
				Component component=new Component(rs.getInt("cid"),rs.getInt("pacid"),rs.getInt("stateid"),rs.getInt("ordernumber"));
				components.addElement(component);
				if(order!=-1 && next==-1 && order!=component.getOrdernumber()) next=component.getOrdernumber();
				if(component.getCid()==cid) order=component.getOrdernumber();
				if(order==-1 && previous!=component.getOrdernumber()) {
					lastprev=previous;
					previous=component.getOrdernumber();
				}
			}
			if(previous==order) previous=lastprev;
			
			if(next!=-1 && dir.equals("next")) {exch=next;}
			if(previous!=-1 && !dir.equals("next")) {exch=previous;}
			
			if(exch!=-1) {
				pst.setInt(1, 99999);
				pst.setInt(2, order);
				pst.execute();
				pst.clearParameters();
				
				pst.setInt(1, order);
				pst.setInt(2, exch);
				pst.execute();
				pst.clearParameters();
				
				pst.setInt(1, exch);
				pst.setInt(2, 99999);
				pst.execute();
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) try{rs.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.exchangeCidOrder exception " + fe.getMessage());}
			if(st!=null) try{st.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.exchangeCidOrder exception " + fe.getMessage());}
			if(pst!=null) try{pst.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.exchangeCidOrder exception " + fe.getMessage());};
			if(conn!=null) try{conn.close();} catch(Exception fe) {System.out.println(Utils.currentDate()+" - DBGateway.exchangeCidOrder exception " + fe.getMessage());}
		}
	}

}
