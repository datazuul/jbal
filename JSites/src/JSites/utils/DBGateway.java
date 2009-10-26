package JSites.utils;

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
	
    
    public static void activateComponent(long cid, Connection conn) throws SQLException, ComponentException {
		Statement st = conn.createStatement();
    	st.executeUpdate("update tblcontenuti set StateID=3 where CID="+cid);
    	ResultSet rs = st.executeQuery("select HistoryCid from tblcomponenti where CID="+cid);
    	if(rs.next()){
    		long hCid = rs.getLong(1);
    		st.executeUpdate("update tblcontenuti set StateID=4 where CID="+hCid);
    	}
    	rs.close();
    	st.close();
	}

	public static void activatePage(long pid, Connection conn) throws SQLException, ComponentException {
		Statement st = conn.createStatement();
		st.executeUpdate("update tblpagine set Valid=1 where PID="+pid);
		st.close();
	}
	
	public static void deleteComponent(long cid, Connection conn) throws SQLException, ComponentException {
		Statement st = conn.createStatement();
    	st.executeUpdate("update tblcontenuti set StateID=5 where CID="+cid);
    	long pacid = DBGateway.getPacid(cid, conn);
    	OrdinaDB.normalizeDB(pacid, conn);
    	st.close();
	}

	public static void creaPaginaInDB(Long ret, Request o, Connection conn) throws SQLException {
		String name = o.getParameter("title");
		String papid = o.getParameter("papid");
		String code = doCode(name,conn);
		String query1 = "insert into tblpagine (PID,Name,PaPID,Valid,HasChild,PCode) values (?,?,?,0,0,?)";
		String query2 = "update tblpagine set HasChild = 1 where PID="+papid;
		
		PreparedStatement ps = null;
		Statement st = null;
		try {
			ps = conn.prepareStatement(query1);
			ps.setLong(1, ret);
			ps.setString(2, name);
			ps.setString(3, papid);
			ps.setString(4, code);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("pid: " + ret);
			System.out.println("name: " + name);
			System.out.println("papid: " + papid);
			System.out.println("code: " + code);	
		}
		try{
			st = conn.createStatement();
			st.execute(query2);
			st.close();
		} catch (SQLException e) {e.printStackTrace(); System.out.println("Exception generated by query:\n" + query2);}
		
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
		PreparedStatement st = conn.prepareStatement("select count(*) from tblpagine where PCode=?");
		st.setString(1, ret);
		ResultSet rs = st.executeQuery();
		rs.next();
		Long a = rs.getLong(1);
		rs.close();
		st.close();
		return a>0;
	}

	public static void disableComponent(long cid, Connection conn) throws SQLException, ComponentException { 
    	Statement st = conn.createStatement();
		st.executeUpdate("update tblcontenuti set StateID=2 where CID="+cid);
		st.close();
	}

	public static void disablePage(long pid, Connection conn) throws SQLException, ComponentException {
		Statement st = conn.createStatement();
		st.executeUpdate("update tblpagine set Valid=0 where PID="+pid);
		st.close();
	}

	public static String getAreaName(long pid, Connection conn) throws SQLException{
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select name from tblpagine where PID=" + getStartFromPid(pid, conn));
		rs.next();
		String a = rs.getString(1);
		rs.close();
		st.close();
		return a;
	}

	public static Long getNewPageId(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("select PID from tblpagine where PID=?");
		long ret=1;
		ResultSet temp = null;
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
		ps.close();
		return ret;
	}

	public static int getPageLevel(long pid, int c, Connection conn) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT PaPID from tblpagine where PID="+pid);
		
		long newpid = 0;
		if(rs.next()){newpid = rs.getLong(1);}
		rs.close();
		st.close();
		if(newpid==0)return c;
		else return getPageLevel(newpid,++c, conn);
	
	}

	public static long getPapid(long pid, Connection conn) throws SQLException {
		Statement st = conn.createStatement();
		long ret = 0;
		ResultSet rs = st.executeQuery("select PaPID from tblpagine where PID="+pid);
		if(rs.next())ret = rs.getLong(1);
		rs.close();
		st.close();
		return ret;
	}
	
	public static long getNewFreeCid(Connection conn) throws SQLException, ComponentException {
		
		PreparedStatement ps = conn.prepareStatement("select CID from tblcomponenti where CID=?");
		long ret=1;
		ResultSet temp = null;
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
		ps.close();
		return ret;
	}

	public static long getStartFromPid(long startFromPid, Connection conn) throws SQLException {
		Statement st = conn.createStatement();
		long newStartFromPid = 0;	long ret = 0;
		
		String query = "SELECT PaPID from tblpagine where PID=" + startFromPid;
	
		ResultSet rs = st.executeQuery(query);
		rs.next();
		
		newStartFromPid = rs.getLong(1);
		
		rs.close();
		st.close();
		if(newStartFromPid<=1)ret = startFromPid;
		else ret = getStartFromPid(newStartFromPid, conn);
		
		return ret;
	}

	public static long getState(long id, Connection conn) throws ComponentException, SQLException{
		Statement st = conn.createStatement();
		long ret = 0;
		ResultSet rsState = st.executeQuery("select StateID from tblcontenuti where CID = "+id);
		if(rsState.next()) ret = rsState.getLong(1);
		rsState.close(); 
		st.close();
		return ret;
	}

	public static String getTypeForCid(long cid, Connection conn) throws SQLException {
		Statement st = conn.createStatement();
		st.execute("select Type from tblcomponenti where CID=" + cid);
		ResultSet rs = st.getResultSet();
		String ret = "";
		if(rs.next()){
			ret = rs.getString(1);
		}
		rs.close();
		st.close();
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
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select Valid from tblpagine where PID="+pid);
		if(rs.next()) ret = rs.getLong(1);
		rs.close();
		st.close();
		return ret==1;
	}
	
	public static boolean isPageInSidebar(long pid, Connection conn) throws SQLException {
		long ret = 0;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select InSidebar from tblpagine where PID="+pid);
		if(rs.next()) ret = rs.getLong(1);
		rs.close();
		st.close();
		return ret==1;
	}

	public static long getPacid(long id, Connection conn) throws SQLException {
		long ret = 0;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select PaCID from tblcontenuti where CID="+id);
		if(rs.next()) ret = rs.getLong(1);
		rs.close();
		st.close();
		return ret;
	}

	public static long getOrederNumber(long cid, long pacid, Connection conn) throws SQLException {
		long ret = -1;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select OrderNumber from tblcontenuti where CID="+cid + " and PaCID="+pacid);
		if(rs.next()) ret = rs.getLong(1);
		rs.close();
		
		if(ret==-1){
			rs = st.executeQuery("select max(OrderNumber) from tblcontenuti where PaCID="+pacid);
			if(rs.next()) ret = rs.getLong(1);
			rs.close();
			ret++;
		}
		st.close();

		return ret;
	}
	
	public static long getOrederNumber(long cid, Connection conn) throws SQLException {
		return getOrederNumber(cid, DBGateway.getPacid(cid, conn) ,conn);
	}

	public static boolean hasNewVersion(long id, Connection conn) throws SQLException {
		int ret = 0;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select count(*) from tblcomponenti where HistoryCid="+id);
		if(rs.next()) ret = rs.getInt(1);
		rs.close();
		st.close();
		
		return ret>0;
	}
	
	public static boolean hasChild(long pid, Connection conn, Session session) throws SQLException, JOpac2Exception {
		if(pid==0)return false;
		int ret = 0;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select PID,Valid from tblpagine where PaPID="+pid);
		while(rs.next()){
			long tempPid = rs.getLong("PID");
			Permission tempP = Authentication.assignPermissions(session, tempPid, conn);
			if(tempP.hasPermission(Permission.ACCESSIBLE)) ret++;
		}
		rs.close();
		st.close();
		
		return ret>0;
	}

	public static void setPending(long cid, Connection conn) throws SQLException {
		Statement st = conn.createStatement();
    	st.executeUpdate("update tblcontenuti set StateID=2 where CID="+cid);
    	st.close();
	}

	public static void setWorking(long cid, Connection conn) throws SQLException {
		Statement st = conn.createStatement();
    	st.executeUpdate("update tblcontenuti set StateID=1 where CID="+cid);
    	st.close();
	}

	public static void setHasChild(long id, Connection conn) throws SQLException {
		Statement st = conn.createStatement();
    	st.executeUpdate("update tblcomponenti set HasChildren=1 where CID="+id);
    	st.close();
	}

	public static void orderComponents(Vector<String> cids, Vector<String> nums, String pacid, Connection conn) throws SQLException {
		
		String query = "update tblcontenuti set OrderNumber=? where CID = ?";
		if(!(pacid.equals(""))) query = query + " and PaCID = " + pacid;
		
		PreparedStatement ps = conn.prepareStatement("update tblcontenuti set OrderNumber=? where CID = ?");
		
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
				Statement st = conn.createStatement();
		    	st.executeUpdate(q);
		    	st.close();
			}
			
		}
		
		ps.close();
	}


	private static Vector<String> futureCid(String cid, Connection conn) throws SQLException {
		Vector<String> ret = new Vector<String>();
		Statement sqlState = conn.createStatement();
		sqlState.executeQuery("select CID from tblcomponenti where HistoryCid="+cid);
		ResultSet temp = sqlState.getResultSet();
		while(temp.next()){
			ret.add(temp.getString(1));
		}
		temp.close();
		sqlState.close();
		return ret;
	}

	public static long getStateCode(String state, Connection conn) throws SQLException, ComponentException {
		long ret = 0;
		PreparedStatement sqlState = conn.prepareStatement("select StateID from tblstati where StateName=?");
		sqlState.setString(1, state);
		sqlState.executeQuery();
		ResultSet temp = sqlState.getResultSet();
		try{
			temp.next();
			ret = temp.getLong(1);
		}catch(SQLException sqle){
			System.out.println("Eccezione con query: select StateID from tblstati where StateName='"+state+"'");
			System.out.println(sqle.getMessage());
		}
		
		temp.close();
		sqlState.close();
		return ret;
	}

	public static String getPageName(long pid, Connection conn) throws SQLException {
		Statement sqlState = conn.createStatement();
		sqlState.executeQuery("select Name from tblpagine where PID="+pid);
		ResultSet temp = sqlState.getResultSet();
		temp.next();
		String ret = temp.getString(1);
		temp.close();
		sqlState.close();
		return ret;
	}
	
	public static void setPageName(long pid, String newTitle, Connection conn) throws SQLException {
		String temp = newTitle ;
		PreparedStatement ps = conn.prepareStatement("update tblpagine set Name=? where PID=?");
		ps.setString(1, temp);
		ps.setLong(2, pid);
		ps.execute();
		ps.close();
	}
	
	public static void setPageCode(long pid, String newCode, Connection conn) throws SQLException {
		Statement sqlState = conn.createStatement();
		sqlState.executeUpdate("update tblpagine set PCode='"+newCode+"'where PID="+pid);
		sqlState.close();
	}
	
	public static boolean pageExists(long pid, Connection conn) throws SQLException {
		boolean ret = false;
		Statement sqlState = conn.createStatement();
		sqlState.executeQuery("select Name from tblpagine where PID="+pid);
		ResultSet temp = sqlState.getResultSet();
		ret = temp.next();
		temp.close();
		sqlState.close();
		return ret;
	}

	public static Permission getPermission(String username, long pid, Connection conn) throws SQLException, JOpac2Exception {
		Statement sqlState = conn.createStatement();
		String sql="select PermissionCode from tblroles where PID="+pid+" and Username='"+username+"'";
		
		sqlState.executeQuery(sql);
		
//		try {
//			sqlState.executeQuery(sql);
//		}
//		catch(SQLException e) {
//			// Cambiato nome colonna
//			String alter="ALTER TABLE tblroles CHANGE COLUMN User Username VARCHAR(50)";
//			int n=e.getErrorCode();
//			if(n==1054) {
//				try {
//					sqlState.execute(alter);
//					sqlState.executeQuery(sql);
//				}
//				catch(Exception ez) {
//					System.out.println("ALTER ERROR DBGateway.getPermission: "+ez.getMessage());
//					sqlState.close();
//				}
//			}
//			else {
//				sqlState.close();
//				if(n==30000) throw e;
//				else throw new JOpac2Exception("User/Username exception");
//			}
//		}
		ResultSet temp = sqlState.getResultSet();
		byte ret = -1;
		if(temp.next()){
			ret = temp.getByte(1);
		}
		temp.close();
		
		if(ret==-1) {
			temp = sqlState.executeQuery("select PermissionCode from tblroles where PID=0 and Username='"+username+"'");
			if(temp.next()){
				ret=temp.getByte(1);
			}
			else{
				if(DBGateway.isPageValid(pid, conn)) ret = 1;
				else ret = 0;
			}
		}

		temp.close();
		sqlState.close();
		
		Permission p = new Permission();
		p.setPermissionCode(ret);
		
		return p;
	}

	public static long getPidFrom(String pcode, Connection conn) throws SQLException {
		long ret = 1;
		Statement sqlState = conn.createStatement();
		sqlState.executeQuery("select PID from tblpagine where PCode='"+pcode+"'");
		ResultSet temp = sqlState.getResultSet();
		if(temp.next()){
			ret = temp.getLong(1);
		}
		temp.close();
		sqlState.close();
		return ret;
	}

	public static void setPageInSidebar(Connection conn, long pid, boolean insidebar) throws SQLException {
		Statement st = conn.createStatement();
		String query = "update tblpagine set InSidebar=0 where PID="+pid;
		if(insidebar) query = "update tblpagine set InSidebar=1 where PID="+pid;
		st.executeUpdate(query);
		st.close();
		
	}

	public static String getRedirectURL(long pageId, Connection conn) throws SQLException {
		String url = null;
		Statement sqlState = conn.createStatement();
		sqlState.executeQuery("select Url from tblredirects where PID="+pageId);
		ResultSet temp = sqlState.getResultSet();
		if(temp.next()){
			url = temp.getString(1);
		}
		temp.close();
		sqlState.close();
		return url;
	}
	
	public static void erasePage(long pid, Connection conn) throws SQLException{
		Statement st = conn.createStatement();
		String q = "select CID from tblstrutture where PID="+pid;
		ResultSet containers = st.executeQuery(q);
		while(containers.next()){
			long cid = containers.getLong(1);
			if(uniquePageRelation(pid, cid, conn)){
				eraseContent(cid, conn);
			}
		}
		containers.close();
		
		q = "select PID from tblpagine where PaPID="+pid;
		ResultSet childPages = st.executeQuery(q);
		while(childPages.next()){
			long childpid = childPages.getLong(1);
			erasePage(childpid,conn);
		}
		childPages.close();
		
    	st.execute("delete from tblstrutture where PID="+pid);
    	st.execute("delete from tblpagine where PID="+pid);
    	st.execute("delete from tblredirects where PID="+pid);
    	st.execute("delete from tblroles where PID="+pid);
		
		st.close();
	}

	private static void eraseContent(long cid, Connection conn) throws SQLException {
		Statement st = conn.createStatement();
		String q = "select CID from tblcontenuti where PaCID="+cid;
		ResultSet children = st.executeQuery(q);
		while(children.next()){
			long childId = children.getLong(1);
			eraseComponent(childId, conn);
		}		
		children.close();
		
    	st.execute("delete from tblstrutture where CID="+cid);
    	st.execute("delete from tblcomponenti where CID="+cid);
		st.close();
	}

	private static void eraseComponent(long cid, Connection conn) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet hCid = st.executeQuery("select HistoryCid from tblcomponenti where CID="+cid);
		if(hCid.next()){
			long historyCid = hCid.getLong(1);
			if(historyCid!=0){
				eraseComponent(historyCid, conn);
			}
		}
		hCid.close();
    	st.execute("delete from tblcontenuti where CID="+cid);
    	st.execute("delete from tblcomponenti where CID="+cid);
    	st.close();
	}

	private static boolean uniquePageRelation(long pid, long cid, Connection conn) throws SQLException {
		boolean ret = true;
		Statement st = conn.createStatement();
		String q = "select PID from tblstrutture where CID="+cid;
		ResultSet pages = st.executeQuery(q);
		while(pages.next()){
			long tempPid = pages.getLong(1);
			if(tempPid!=pid){
				ret = false;
				break;
			}
		}
		pages.close();
		st.close();
		return ret;
	}
	
	public static boolean isLeaf(long tempPid, Session session, Connection conn) throws SQLException, JOpac2Exception {
		boolean ret = true;
		Statement st = conn.createStatement();
		String q = "select PID from tblpagine where PaPID="+tempPid;
		ResultSet pages = st.executeQuery(q);
		while(pages.next()){
			long pid = pages.getLong(1);
			Permission tempperm = Authentication.assignPermissions(session, pid, conn);
			if(	( tempperm.hasPermission(Permission.ACCESSIBLE) && DBGateway.isPageInSidebar(pid,conn) ) ||
				  tempperm.hasPermission(Permission.EDITABLE) || tempperm.hasPermission(Permission.VALIDABLE) ){
				ret = false;
				break;
			}
		}
		pages.close();
		st.close();
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
				Statement st = conn.createStatement();
				st.execute(query1);
				st.close();
			} catch (SQLException e) {
				query1  = "update tblroles set PermissionCode=" + (int)p.getPermissionCode() +
							" where Username = '" + userData + "' and PID=" + pid;
				try{
					Statement st = conn.createStatement();
					st.execute(query1);
					st.close();
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

		PreparedStatement ps = conn.prepareStatement("insert into tblstrutture (PID,CID) values (?,?)");
		ps.setLong(1,pid);
		for(int i=1;i<5;i++){
			if(i==3)ps.setLong(2,ret);
			else if(i==2)ps.setLong(2,11);
			else ps.setLong(2,i);
			ps.execute();
		}
		ps.close();
	}
	
	public static void saveDBComponent(long cid, String componentType, int hasChild, long historycid, Date date, 
			long pid, String url, Connection conn) throws SQLException, ComponentException{
//		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		
		String sqlComponenteNuovo = "INSERT INTO tblcomponenti (CID,Type,Attributes,HasChildren,HistoryCid)" + // ,InsertDate
									"VALUES (" + cid + ",'" + componentType + "',null,"+ hasChild+"," + historycid + ")"; // ,'" + sqlDate + "'
		Statement st = conn.createStatement();
		st.execute(sqlComponenteNuovo);
		
		if(componentType.equals("externalLink")){
			String redir = DBGateway.getRedirectURL(pid, conn);
			
			if(redir==null){
				String sqlExternalLink = "INSERT INTO tblredirects (PID,Url) " + 
										 "VALUES (" + pid + ",'" + url + "')";
				
				st.execute(sqlExternalLink);
			}

		}
		
		st.close();
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
			Statement st = conn.createStatement();
			st.execute(sqlRelazioneNuovo);
			st.close();
			
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
		try{
			String select = "select CID, OrderNumber from tblcontenuti where PaCID="+pacid+" and OrderNumber>="+ord;
			Statement st = conn.createStatement();
			PreparedStatement ps = conn.prepareStatement("update tblcontenuti set OrderNumber = ? where CID = ? and PaCID="+pacid);
			
			ResultSet rs = st.executeQuery(select);
			long currCid = 0;
			long currOrd = 0;
			while(rs.next()){
				currCid = rs.getLong(1);
				currOrd = rs.getLong(2);
				ps.setLong(1,currOrd+1);
				ps.setLong(2,currCid);
				ps.execute();
			}
			
			ps.close();
			rs.close();
			st.close();
		}catch(Exception e){e.printStackTrace();}
	}

	public static long getContentCID(long pageId, Connection conn) throws SQLException {
		long cid = 0;
		String query = "SELECT tblstrutture.CID " +
						"FROM tblcomponenti INNER JOIN tblstrutture ON tblcomponenti.CID = tblstrutture.CID " +
						"WHERE (((tblstrutture.PID)=" + pageId+ ") AND ((tblcomponenti.Type)='content'))";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		if(rs.next()){
			cid = rs.getLong(1);
		}
		rs.close();
		st.close();
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
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
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
		rs.close();
		st.close();
		
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
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		while(rs.next()){
			v.add(rs.getLong(1));
		}
		rs.close();
		st.close();
		return v;
		
	}


}
