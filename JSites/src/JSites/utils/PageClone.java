package JSites.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
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
	public static int cloneSinglePage(DataSourceComponent datasourceComponent, int sourcePid, int papid, String dataDirectory, String username, String remoteaddr) throws SQLException, IOException {
		String title=DBGateway.getPageName(datasourceComponent, sourcePid);
		int pid = DBGateway.getNewPageId(datasourceComponent);
		DBGateway.createPage(datasourceComponent, title, pid, papid);
		
		long newContentCid = DBGateway.getNewFreeCid(datasourceComponent);
		DBGateway.saveDBComponent(datasourceComponent, newContentCid,"content",1,0,new Date(), pid, "", 
				username, remoteaddr);
		DBGateway.linkPageContainers(datasourceComponent, newContentCid, pid);
		
		DBGateway.clonePermission(datasourceComponent, papid,pid);

		DBGateway.cloneComponents(datasourceComponent, pid,sourcePid, dataDirectory, username,remoteaddr);
		return pid;
	}
	
	public static void clonePageComponents(DataSourceComponent datasourceComponent, int sourcePid, int destPid, String dataDirectory, String username, String remoteaddr) throws SQLException, IOException {
		DBGateway.cloneComponents(datasourceComponent, destPid,sourcePid, dataDirectory, username,remoteaddr);
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
	public static int cloneRecursivePage(DataSourceComponent datasourceComponent, int sourcePid, int papid, String dataDirectory, String username, String remoteaddr) throws SQLException, IOException {
		int pid=cloneSinglePage(datasourceComponent, sourcePid,papid,dataDirectory,username,remoteaddr);
		Vector<Long> clds=DBGateway.getChildPages(datasourceComponent, sourcePid);
		for(int i=0;clds!=null && i<clds.size();i++) {
			int curPid=clds.elementAt(i).intValue();
			if(DBGateway.isPageValid(datasourceComponent, curPid)) {
				cloneRecursivePage(datasourceComponent, curPid,pid,dataDirectory,username,remoteaddr);
			}
		}
		return pid;
	}
	
	public static void cloneRecursiveIntoPage(DataSourceComponent datasourceComponent, int sourcePid, int destPid, String dataDirectory, String username, String remoteaddr) throws SQLException, IOException {
		clonePageComponents(datasourceComponent, sourcePid, destPid, dataDirectory, username, remoteaddr);
		Vector<Long> clds=DBGateway.getChildPages(datasourceComponent, sourcePid);
		for(int i=0;clds!=null && i<clds.size();i++) {
			int curPid=clds.elementAt(i).intValue();
			if(DBGateway.isPageValid(datasourceComponent, curPid)) {
				cloneRecursivePage(datasourceComponent, curPid,destPid,dataDirectory,username,remoteaddr);
			}
		}
	}
}
