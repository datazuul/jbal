package JSites.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.jopac2.utils.JOpac2Exception;

import JSites.authentication.Authentication;
import JSites.authentication.Permission;

public class PageClone {
	/**
	 * Import in a new page, child of papid, all active elements from sourcePid, copying xml files.
	 * Note: if a catalog is present, data are not cloned
	 *
	 * @param sourcePid
	 * @param papid
	 * @param dataDirectory
	 * @param conn
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static int cloneSinglePage(int sourcePid, int papid, String dataDirectory, String username, String remoteaddr, Connection conn) throws SQLException, IOException {
		String title=DBGateway.getPageName(sourcePid, conn);
		int pid = DBGateway.getNewPageId(conn);
		DBGateway.createPage(title, pid, papid, conn);
		
		long newContentCid = DBGateway.getNewFreeCid(conn);
		DBGateway.saveDBComponent(newContentCid,"content",1,0,new Date(), pid, "", 
				username, remoteaddr, conn);
		DBGateway.linkPageContainers(newContentCid, pid, conn);
		
		DBGateway.clonePermission(papid,pid,conn);

		DBGateway.cloneComponents(pid,sourcePid, dataDirectory, username,remoteaddr,conn);
		return pid;
	}
	
	public static void clonePageComponents(int sourcePid, int destPid, String dataDirectory, String username, String remoteaddr, Connection conn) throws SQLException, IOException {
		DBGateway.cloneComponents(destPid,sourcePid, dataDirectory, username,remoteaddr,conn);
	}


	/**
	 * Import in a new page, child of papid, all active elements from sourcePid, copying xml files.
	 * Children are recursively cloned.
	 * Note: if a catalog is present, data are not cloned
	 * @param sourcePid
	 * @param papid
	 * @param dataDirectory
	 * @param conn
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static int cloneRecursivePage(int sourcePid, int papid, String dataDirectory, String username, String remoteaddr, Connection conn) throws SQLException, IOException {
		int pid=cloneSinglePage(sourcePid,papid,dataDirectory,username,remoteaddr,conn);
		Vector<Long> clds=DBGateway.getChildPages(sourcePid, conn);
		for(int i=0;clds!=null && i<clds.size();i++) {
			int curPid=clds.elementAt(i).intValue();
			if(DBGateway.isPageValid(curPid, conn)) {
				cloneRecursivePage(curPid,pid,dataDirectory,username,remoteaddr,conn);
			}
		}
		return pid;
	}
	
	public static void cloneRecursiveIntoPage(int sourcePid, int destPid, String dataDirectory, String username, String remoteaddr, Connection conn) throws SQLException, IOException {
		clonePageComponents(sourcePid, destPid, dataDirectory, username, remoteaddr, conn);
		Vector<Long> clds=DBGateway.getChildPages(sourcePid, conn);
		for(int i=0;clds!=null && i<clds.size();i++) {
			int curPid=clds.elementAt(i).intValue();
			if(DBGateway.isPageValid(curPid, conn)) {
				cloneRecursivePage(curPid,destPid,dataDirectory,username,remoteaddr,conn);
			}
		}
	}
}
