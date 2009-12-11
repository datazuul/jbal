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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.avalon.framework.component.ComponentException;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.jopac2.utils.JOpac2Exception;

import JSites.setup.DbSetup;
import JSites.utils.site.NewsItem;


import JSites.authentication.Authentication;
import JSites.authentication.Permission;

public class DBGateway {
	
	
	public static void executeStatement(Connection conn, String sql) throws SQLException {
		Statement st=null;
		try {
			st = conn.createStatement();
			st.executeUpdate(sql);
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			if(st!=null) st.close();
		}
	}
	
    public static void activateComponent(long cid, Connection conn) throws SQLException, ComponentException {
    	ResultSet rs=null;
    	Statement st=null;
    	try {
	    	executeStatement(conn,"update tblcontenuti set StateID=3 where CID="+cid);
	    	st=conn.createStatement();
	    	rs=st.executeQuery("select HistoryCid from tblcomponenti where CID="+cid);
	    	if(rs.next()){
	    		long hCid = rs.getLong(1);
	    		executeStatement(conn,"update tblcontenuti set StateID=4 where CID="+hCid);
	    	}
    	}
    	catch(SQLException e) {
	    	throw e;
    	}
    	finally {
    		if(rs!=null) rs.close();
    		if(st!=null) st.close();
    	}
	}

	public static void activatePage(long pid, Connection conn) throws SQLException, ComponentException {
		executeStatement(conn, "update tblpagine set Valid=1 where PID="+pid);
	}
	
	public static void deleteComponent(long cid, Connection conn) throws SQLException, ComponentException {
		executeStatement(conn, "update tblcontenuti set StateID=5 where CID="+cid);
		long pacid = DBGateway.getPacid(cid, conn);
    	OrdinaDB.normalizeDB(pacid, conn);
	}

	public static void creaPaginaInDB(Long ret, Request o, Connection conn) throws SQLException {
		String name = o.getParameter("title");
		String papid = o.getParameter("papid");
		String code = doCode(name,conn);
		String query1 = "insert into tblpagine (PID,Name,PaPID,Valid,HasChild,PCode) values (?,?,?,0,0,?)";
		String query2 = "update tblpagine set HasChild = 1 where PID="+papid;
		
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query1);
			ps.setLong(1, ret);
			ps.setString(2, name);
			ps.setString(3, papid);
			ps.setString(4, code);
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("pid: " + ret);
			System.out.println("name: " + name);
			System.out.println("papid: " + papid);
			System.out.println("code: " + code);
			throw e;
		}
		finally {
			if(ps!=null) ps.close();
		}
		executeStatement(conn, query2);
	}
    
    private static String doCode(String name, Connection conn) throws SQLException {
		
    	name = name.toUpperCase();
    	String ret = "";
		String[] words = name.split(" ");
		int index = 0;
		
		while(ret.length()<3){
			for(int i=0;i<words.length;i++){
				if(index>words[i].length()-1)
					continue;
				ret = ret + words[i].substring(index,index+1);
				
			}
			index++;
		}
		index = 0;
		ret = ret.substring(0,3).toUpperCase();
		if(codeExists(ret, conn)){
			String a = ret;
			
			for(int j=2;j>0;j--){
				for(index=0;index<10;index++){
					ret = a.substring(0,j) + index + a.substring(j+1);
					if(!codeExists(ret, conn)) break;
				}
				if(!codeExists(ret, conn)) break;
			}
		}
		
		if(codeExists(ret, conn)){
			String a = ret;
			
			for(index=10;index<100;index++){
				ret = a.substring(0,1) + index;
				if(!codeExists(ret, conn)) break;
			}
		}
		
		if(codeExists(ret, conn)){
			
			for(index=100;index<1000;index++){
				ret = index + "";
				if(!codeExists(ret, conn)) break;
			}
		}
		
		ret = ret.substring(0,3).toUpperCase();
		return ret;
	}


	private static boolean codeExists(String ret, Connection conn) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		long a=0;
		try {
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
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return a>0;
	}

	public static void disableComponent(long cid, Connection conn) throws SQLException, ComponentException {
		executeStatement(conn, "update tblcontenuti set StateID=2 where CID="+cid);
	}

	public static void disablePage(long pid, Connection conn) throws SQLException, ComponentException {
		executeStatement(conn, "update tblpagine set Valid=0 where PID="+pid);
	}

	public static String getAreaName(long pid, Connection conn) throws SQLException{
		ResultSet rs = null;
		Statement st = null;
		String a=null;

		try {
			st=conn.createStatement();
			rs=st.executeQuery("select name from tblpagine where PID=" + getStartFromPid(pid, conn));
			rs.next();
			a = rs.getString(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return a;
	}

	public static Long getNewPageId(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet temp = null;
		long ret=1;
		
		try {
			ps = conn.prepareStatement("select PID from tblpagine where PID=?");
			
			while(true){
				ps.setLong(1,ret);
				temp = ps.executeQuery();
				if(temp.next()){
					ret++;
					temp.close();
				}
				else{
					temp.close();
					break;
				}
			}
		}
		catch(SQLException e) {
			
			throw e;
		}
		finally {
			if(temp!=null) temp.close();
			if(ps!=null) ps.close();
		}
		return ret;
	}

	public static int getPageLevel(long pid, int c, Connection conn) throws SQLException {
		ResultSet rs = null;
		Statement st = null;
		long newpid = 0;
		
		try {
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
		}
		if(newpid==0)return c;
		else return getPageLevel(newpid,++c, conn);
	
	}

	public static long getPapid(long pid, Connection conn) throws SQLException {
		ResultSet rs = null;
		Statement st = null;
		long ret = 0;
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("select PaPID from tblpagine where PID="+pid);
			if(rs.next())ret = rs.getLong(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		
		return ret;
	}
	
	public static long getNewFreeCid(Connection conn) throws SQLException, ComponentException {
		PreparedStatement ps = null;
		ResultSet temp = null;
		long ret=1;
		
		try {
			ps = conn.prepareStatement("select CID from tblcomponenti where CID=?");
	
			while(true){
				ps.setLong(1,ret);
				temp = ps.executeQuery();
				if(temp.next()){
					ret++;
					temp.close();
				}
				else{
					temp.close();
					break;
				}
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) temp.close();
			if(ps!=null) ps.close();
		}
		return ret;
	}

	public static long getStartFromPid(long startFromPid, Connection conn) throws SQLException {
		ResultSet rs = null;
		Statement st = null;
		long newStartFromPid = 0;	long ret = 0;
		
		String query = "SELECT PaPID from tblpagine where PID=" + startFromPid;
		
		try {
			st=conn.createStatement();
			rs = st.executeQuery(query);
			rs.next();
			newStartFromPid = rs.getLong(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		if(newStartFromPid<=1)ret = startFromPid;
		else ret = getStartFromPid(newStartFromPid, conn);
		
		return ret;
	}

	public static long getState(long id, Connection conn) throws ComponentException, SQLException{
		ResultSet rsState = null;
		Statement st = null;
		long ret = 0;
		
		try {
			st=conn.createStatement();
			rsState = st.executeQuery("select StateID from tblcontenuti where CID = "+id);
			if(rsState.next()) ret = rsState.getLong(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rsState!=null) rsState.close();
			if(st!=null) st.close();
		}
		return ret;
	}

	public static String getTypeForCid(long cid, Connection conn) throws SQLException {
		ResultSet rs = null;
		Statement st = null;
		String ret = "";
		
		try {
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
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return ret;
	}
    
    public static boolean is2Validate(long id, Connection conn) throws ComponentException, SQLException {
		return getState(id, conn)==2;
	}
	
	public static boolean isValid(long id, Connection conn) throws ComponentException, SQLException {
		return getState(id, conn)==3;
	}
	
	public static boolean isPageValid(long pid, Connection conn) throws SQLException {
		long ret = 0;
		ResultSet rs = null;
		Statement st = null;
		try {
			st=conn.createStatement();
			rs = st.executeQuery("select Valid from tblpagine where PID="+pid);
			if(rs.next()) ret = rs.getLong(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return ret==1;
	}
	
	public static boolean isPageInSidebar(long pid, Connection conn) throws SQLException {
		Statement st = null;
		long ret = 0;
		ResultSet rs = null;
		try {
			st=conn.createStatement();
			rs = st.executeQuery("select InSidebar from tblpagine where PID="+pid);
			if(rs.next()) ret = rs.getLong(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return ret==1;
	}

	public static long getPacid(long id, Connection conn) throws SQLException {
		long ret = 0;
		ResultSet rs = null;
		Statement st = null;
		
		try {
			st=conn.createStatement();
			rs = st.executeQuery("select PaCID from tblcontenuti where CID="+id);
			if(rs.next()) ret = rs.getLong(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return ret;
	}

	public static long getOrederNumber(long cid, long pacid, Connection conn) throws SQLException {
		long ret = -1;
		ResultSet rs = null;
		Statement st = null;
		
		try {
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
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return ret;
	}
	
	public static long getOrederNumber(long cid, Connection conn) throws SQLException {
		return getOrederNumber(cid, DBGateway.getPacid(cid, conn) ,conn);
	}

	public static boolean hasNewVersion(long id, Connection conn) throws SQLException {
		int ret = 0;
		ResultSet rs = null;
		Statement st = null;
		
		try {
			st=conn.createStatement();
			rs = st.executeQuery("select count(*) from tblcomponenti where HistoryCid="+id);
			if(rs.next()) ret = rs.getInt(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return ret>0;
	}
	
	public static boolean hasChild(long pid, Connection conn, Session session) throws SQLException, JOpac2Exception {
		if(pid==0)return false;
		int ret = 0;
		ResultSet rs = null;
		Statement st = null;
		
		try {
			st=conn.createStatement();
			rs = st.executeQuery("select PID,Valid from tblpagine where PaPID="+pid);
			while(rs.next()){
				long tempPid = rs.getLong("PID");
				Permission tempP = Authentication.assignPermissions(session, tempPid, conn);
				if(tempP.hasPermission(Permission.ACCESSIBLE)) ret++;
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		
		return ret>0;
	}

	public static void setPending(long cid, Connection conn) throws SQLException {
		executeStatement(conn,"update tblcontenuti set StateID=2 where CID="+cid);
	}

	public static void setWorking(long cid, Connection conn) throws SQLException {
		executeStatement(conn,"update tblcontenuti set StateID=1 where CID="+cid);
	}

	public static void setHasChild(long id, Connection conn) throws SQLException {
		executeStatement(conn, "update tblcomponenti set HasChildren=1 where CID="+id);
	}

	public static void orderComponents(Vector<String> cids, Vector<String> nums, String pacid, Connection conn) throws SQLException {
		
		String query = "update tblcontenuti set OrderNumber=? where CID = ?";
		if(!(pacid.equals(""))) query = query + " and PaCID = " + pacid;
		
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("update tblcontenuti set OrderNumber=? where CID = ?");
			
			for( int i=0; i<cids.size() ; i++ ){
				String cid = ((String)cids.get(i)).substring(3);
				ps.setString(1, (String)nums.get(i));
				ps.setString(2, cid);
				ps.executeUpdate();
				
				Vector<String> fc = futureCid(cid, conn);
				Iterator<String> efc = fc.iterator();
				while(efc.hasNext()){
					String q = "update tblcontenuti set OrderNumber=" + nums.get(i) +
								" where CID=" + efc.next();
					executeStatement(conn, q);
				}
				
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(ps!=null) ps.close();
		}
	}

	private static Vector<String> futureCid(String cid, Connection conn) throws SQLException {
		Vector<String> ret = new Vector<String>();
		ResultSet temp = null;
		Statement st = null;
		
		try {
			st=conn.createStatement();
			temp=st.executeQuery("select CID from tblcomponenti where HistoryCid="+cid);
			while(temp.next()){
				ret.add(temp.getString(1));
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) temp.close();
			if(st!=null) st.close();
		}
		return ret;
	}

	public static long getStateCode(String state, Connection conn) throws SQLException, ComponentException {
		long ret = 0;
		PreparedStatement sqlState = null;
		ResultSet temp = null;
		
		sqlState = conn.prepareStatement("select StateID from tblstati where StateName=?");
		sqlState.setString(1, state);
		sqlState.executeQuery();
		
		temp = sqlState.getResultSet();
		try{
			temp.next();
			ret = temp.getLong(1);
		}catch(SQLException sqle){
			System.out.println("Eccezione con query: select StateID from tblstati where StateName='"+state+"'");
			System.out.println(sqle.getMessage());
		}
		finally {
			if(temp!=null) temp.close();
			if(temp!=null) sqlState.close();
		}
		return ret;
	}

	public static String getPageName(long pid, Connection conn) throws SQLException {
		ResultSet temp = null;
		String ret = null;
		Statement st = null;
		
		try {
			st=conn.createStatement();
			temp=st.executeQuery("select Name from tblpagine where PID="+pid);
			temp.next();
			ret = temp.getString(1);
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) temp.close();
			if(st!=null) st.close();
		}
		return ret;
	}
	
	public static void setPageName(long pid, String newTitle, Connection conn) throws SQLException {
		String temp = newTitle;
		PreparedStatement ps =null;
		try {
			ps = conn.prepareStatement("update tblpagine set Name=? where PID=?");
			ps.setString(1, temp);
			ps.setLong(2, pid);
			ps.execute();
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(ps!=null) ps.close();
		}
	}
	
	public static void setPageCode(long pid, String newCode, Connection conn) throws SQLException {
		executeStatement(conn, "update tblpagine set PCode='"+newCode+"'where PID="+pid);
	}
	
	public static boolean pageExists(long pid, Connection conn) throws SQLException {
		ResultSet temp =null;
		boolean ret = false;
		Statement st = null;
		
		try {
			st=conn.createStatement();
			temp = st.executeQuery("select Name from tblpagine where PID="+pid);
			ret = temp.next();
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) temp.close();
			if(st!=null) st.close();
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

	public static Permission getPermission(String username, long pid, Connection conn) throws SQLException, JOpac2Exception {
		String sql="select PermissionCode from tblroles where PID="+pid+" and Username='"+username+"'";
		ResultSet temp =null;
		Statement st = null;
		byte ret = -1;
		
		try {
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
					if(DBGateway.isPageValid(pid, conn)) ret = 1;
					else ret = 0;
				}
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) temp.close();
			if(st!=null) st.close();
		}
		Permission p = new Permission();
		p.setPermissionCode(ret);
		
		return p;
	}

	public static long getPidFrom(String pcode, Connection conn) throws SQLException {
		long ret = 1;
		ResultSet temp = null;
		Statement st = null;
		
		try {
			st=conn.createStatement();
			temp=st.executeQuery("select PID from tblpagine where PCode='"+pcode+"'");
			if(temp.next()){
				ret = temp.getLong(1);
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(temp!=null) temp.close();
			if(st!=null) st.close();
		}
		return ret;
	}

	public static void setPageInSidebar(Connection conn, long pid, boolean insidebar) throws SQLException {
		String query = "update tblpagine set InSidebar=0 where PID="+pid;
		if(insidebar) query = "update tblpagine set InSidebar=1 where PID="+pid;
		executeStatement(conn,query);
		
	}

	public static String getRedirectURL(long pageId, Connection conn) throws SQLException {
		String url = null;
		ResultSet temp = null;
		Statement st = null;
		
		try {
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
			if(temp!=null) temp.close();
			if(st!=null) st.close();
		}
		return url;
	}
	
	public static void erasePage(long pid, Connection conn) throws SQLException{
		String q = "select CID from tblstrutture where PID="+pid;
		Statement st = null;
		ResultSet containers = null;
		ResultSet childPages = null;
		
		try {
			st=conn.createStatement();
			containers=st.executeQuery(q);
			while(containers.next()){
				long cid = containers.getLong(1);
				if(uniquePageRelation(pid, cid, conn)){
					eraseContent(cid, conn);
				}
			}
			
			q = "select PID from tblpagine where PaPID="+pid;
			
			childPages = st.executeQuery(q);
			while(childPages.next()){
				long childpid = childPages.getLong(1);
				erasePage(childpid,conn);
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(containers!=null) containers.close();
			if(childPages!=null) childPages.close();
			if(st!=null) st.close();
		}

    	executeStatement(conn,"delete from tblstrutture where PID="+pid);
    	executeStatement(conn,"delete from tblpagine where PID="+pid);
    	executeStatement(conn,"delete from tblredirects where PID="+pid);
    	executeStatement(conn,"delete from tblroles where PID="+pid);
	}

	private static void eraseContent(long cid, Connection conn) throws SQLException {
		String q = "select CID from tblcontenuti where PaCID="+cid;
		ResultSet children = null;
		Statement st = null;
		
		try {
			st=conn.createStatement();
			children = st.executeQuery(q);
			while(children.next()){
				long childId = children.getLong(1);
				eraseComponent(childId, conn);
			}		
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(children!=null) children.close();
			if(st!=null) st.close();
		}
		
		executeStatement(conn,"delete from tblstrutture where CID="+cid);
		executeStatement(conn,"delete from tblcomponenti where CID="+cid);
	}

	private static void eraseComponent(long cid, Connection conn) throws SQLException {
		ResultSet hCid = null;
		Statement st = null;
		try {
			st=conn.createStatement();
			hCid=st.executeQuery("select HistoryCid from tblcomponenti where CID="+cid);
			if(hCid.next()){
				long historyCid = hCid.getLong(1);
				if(historyCid!=0){
					eraseComponent(historyCid, conn);
				}
			}
		}
		catch(SQLException e) {
			throw e;
		}
		finally {
			if(hCid!=null) hCid.close();
			if(st!=null) st.close();
		}
		executeStatement(conn,"delete from tblcontenuti where CID="+cid);
		executeStatement(conn,"delete from tblcomponenti where CID="+cid);
	}

	private static boolean uniquePageRelation(long pid, long cid, Connection conn) throws SQLException {
		boolean ret = true;
		String q = "select PID from tblstrutture where CID="+cid;
		ResultSet pages = null;
		Statement st = null;
		
		try {
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
			if(pages!=null) pages.close();
			if(st!=null) st.close();
		}
		return ret;
	}
	
	public static boolean isLeaf(long tempPid, Session session, Connection conn) throws SQLException, JOpac2Exception {
		boolean ret = true;
		String q = "select PID from tblpagine where PaPID="+tempPid;
		ResultSet pages = null;
		Statement st = null;
		
		try {
			st=conn.createStatement();
			pages=st.executeQuery(q);
			while(pages.next()){
				long pid = pages.getLong(1);
				Permission tempperm = Authentication.assignPermissions(session, pid, conn);
				if(	( tempperm.hasPermission(Permission.ACCESSIBLE) && DBGateway.isPageInSidebar(pid,conn) ) ||
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
			if(pages!=null) pages.close();
			if(st!=null) st.close();
		}
		return ret;
	}

	public static void setPermission(Long pid, String userData, Permission p, Connection conn) {
		
		String query1 = "";
		Permission tempP = null;
		try {
			tempP = DBGateway.getPermission(userData, 0, conn);
		} catch (Exception e2) {e2.printStackTrace();}
		
		if(tempP.getPermissionCode() != p.getPermissionCode() || p.getPermissionCode() == 0 )
		{		
			query1  = "insert into tblroles values('"+userData+"'," + (int)p.getPermissionCode() +","+pid+")";
			
			if(p.getPermissionCode() == 0){
				query1 = "delete from tblroles where Username='" + userData + "' and PID="+pid;
			}
			
			
			
			try {
				executeStatement(conn, query1);
			} catch (SQLException e) {
				query1  = "update tblroles set PermissionCode=" + (int)p.getPermissionCode() +
							" where Username = '" + userData + "' and PID=" + pid;
				try{
					executeStatement(conn, query1);
				}catch(Exception e1){e1.printStackTrace();}
			}
		}
		
	}
	
	public static void setPermission(Long pid, String userData, Connection conn) {
		Permission p = new Permission();
		p.setPermissionCode((byte)7);
		setPermission(pid, userData, p, conn);
		
	}
	
	public static void linkPageContainers(long ret, long pid, Connection conn) throws SQLException, ComponentException {
		PreparedStatement ps = null;
		try {
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
			if(ps!=null) ps.close();
		}
	}
	
	public static void saveDBComponent(long cid, String componentType, int hasChild, long historycid, Date date, 
			long pid, String url, Connection conn) throws SQLException, ComponentException{
//		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		
		String sqlComponenteNuovo = "INSERT INTO tblcomponenti (InsertDate,CID,Type,Attributes,HasChildren,HistoryCid)" + // ,InsertDate
									"VALUES (current_timestamp," + cid + ",'" + componentType + "',null,"+ hasChild+"," + historycid + ")"; // ,'" + sqlDate + "'
		executeStatement(conn,sqlComponenteNuovo);
		
		if(componentType.equals("externalLink")){
			String redir = DBGateway.getRedirectURL(pid, conn);
			
			if(redir==null){
				String sqlExternalLink = "INSERT INTO tblredirects (PID,Url) " + 
										 "VALUES (" + pid + ",'" + url + "')";
				
				executeStatement(conn,sqlExternalLink);
			}

		}
	}
	
	public static void saveDBComponentAndRelationship(long id, String componentType, int hasChild, long pacid, 
			long historycid, Date date, long intStatecode, long pid, String url, long order, Connection conn) throws SQLException, ComponentException {
		
		long orderNumber = 1;
		if(order == -1)
			orderNumber = DBGateway.getOrederNumber(historycid, pacid, conn);
		else{
			orderNumber = order;
			updateOrder(order, pacid, conn);
		}
		saveDBComponent(id, componentType,hasChild,historycid,date,pid,url,conn);
		
		if(intStatecode==0){
			System.out.println("Wait... there's a problem\nState Code is 0... Couldn't be!");
		}
		else{
			if(componentType.equals("news"))intStatecode = 3;
			String sqlRelazioneNuovo = "INSERT INTO tblcontenuti (PaCID,CID,StateID,OrderNumber)" + 
			 						   "VALUES (" + pacid + "," + id + "," + intStatecode + "," + (orderNumber) + ")";
			executeStatement(conn,sqlRelazioneNuovo);
			
			long state = DBGateway.getState(id, conn);
			if(state==0){
				String dbname=conn.getCatalog();
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

	public static void updateOrder(long ord, long pacid, Connection conn) throws SQLException {
		ResultSet rs = null;
		Statement st = null;
		PreparedStatement ps = null;
		try{
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
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(st!=null) st.close();
		}
	}

	public static long getContentCID(long pageId, Connection conn) throws SQLException {
		long cid = 0;
		String query = "SELECT tblstrutture.CID " +
						"FROM tblcomponenti INNER JOIN tblstrutture ON tblcomponenti.CID = tblstrutture.CID " +
						"WHERE (((tblstrutture.PID)=" + pageId+ ") AND ((tblcomponenti.Type)='content'))";
		ResultSet rs = null;
		Statement st = null;
		
		try {
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
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return cid;
	}
	
	public static Vector<NewsItem> getNewsCID(Connection conn, String date) throws SQLException {
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
		
		try {
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
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return v;
	}
	
	public static boolean isGrandParent(long pid, long child, Connection conn) throws SQLException {
		boolean r=false;
		long papid=child;
		while(papid>1 && papid!=pid) {
			papid=DBGateway.getPapid(papid, conn);
		}
		if(papid==pid) r=true;
		return r;
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

	public static Vector<Long> getChildPages(long pageId, Connection conn) throws SQLException {
		Vector<Long> v = new Vector<Long>();
		String query =	"SELECT PID FROM tblpagine where PaPID="+pageId;
		ResultSet rs = null;
		Statement st = null;
		
		try {
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
			if(rs!=null) rs.close();
			if(st!=null) st.close();
		}
		return v;
		
	}


}
