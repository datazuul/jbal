package JSites.backup;

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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import org.binarystor.mysql.MySQLDump;
import org.jopac2.engine.utils.ZipUnzip;

import JSites.utils.DirectoryHelper;

public class Backup {
	public static void restore(Connection conn, String sitename, String contextPath) {
		
	}
	
	
	public static File backup(Connection conn, String sitename, String contextPath) throws SQLException, IOException {
		// backup db
		File backup=File.createTempFile(sitename+"_backup", "");
		backup.delete();
		backup.mkdirs();
		
		String backupDir=backup.getAbsolutePath();
		
		File mainDB=backupDb(conn,backupDir);
		
		String sitePath=contextPath+"/"+sitename;
		String sitemapLocation=sitePath+"/sitemap.xmap";
		String sitemapContent=getFileContent(sitemapLocation);
		sitemapContent=sitemapContent.replaceAll("\\s", "");
		
		// backup directory dati
		String siteData=getDummyTag(sitemapContent,"data-directory");
		File[] data=DirectoryHelper.processFilesRecursive(new File(siteData));
		File dataZip=new File(backupDir+"/"+sitename+"Data.zip");
		ZipUnzip.zipArchive(dataZip.getAbsolutePath(), fileToStringVector(data), siteData);
		
		// backup db di eventuali cataloghi
		String[] catalogs=getReferencedCatalogs(conn,siteData);
		for(int i=0;i<catalogs.length;i++) {
			try {
				Connection connCat=getCatalogConnection(contextPath,catalogs[i]);
				backupDb(connCat,backupDir);
				connCat.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// backup directory web
		File[] site=DirectoryHelper.processFilesRecursive(new File(sitePath));
		File siteZip=new File(backupDir+"/"+sitename+"Site.zip");
		ZipUnzip.zipArchive(siteZip.getAbsolutePath(), fileToStringVector(site), sitePath);
		
		// backup template
		String templateName=getDummyTag(sitemapContent,"template");
		String templatePath=contextPath+"/templates/"+templateName;
		File[] template=DirectoryHelper.processFilesRecursive(new File(templatePath));
		File templateZip=new File(backupDir+"/"+sitename+"Template.zip");
		ZipUnzip.zipArchive(templateZip.getAbsolutePath(), fileToStringVector(template), contextPath+"/templates");
		
		
		// zip finale
		File bckZipFile=new File(backupDir+".zip");
		File[] bckZipFiles=DirectoryHelper.processFilesRecursive(new File(backupDir));
		ZipUnzip.zipArchive(bckZipFile.getAbsolutePath(), fileToStringVector(bckZipFiles), backupDir);
		return bckZipFile;
	}
	
	private static Connection getCatalogConnection(String contextPath,
			String catalogName) throws Exception {
		String dbConnections=getFileContent(contextPath+"/WEB-INF/dbConnections.xml");
		dbConnections=dbConnections.replaceAll("\\s", "");
		
		// non uso getDummyTag perche' il nome catalogo e' come attributo
		// e i parametri dono foglie
		String begin="<jdbcname=\""+catalogName+"\">";
		String end="</jdbc>";
		int b=dbConnections.indexOf(begin);
		int e=dbConnections.indexOf(end);
		String connection="";
		if(b>=0 && e>=b) {
			int l=begin.length();
			connection=dbConnections.substring(b+l);
			connection=connection.substring(0,e-(b+l));
		}
		
		Connection conn=null;
		if(connection.length()>0) {
			String dburl=getDummyTag(connection, "dburl");
			String user=getDummyTag(connection,"user");
			String password=getDummyTag(connection,"password");
			
			Class.forName ("com.mysql.jdbc.Driver").newInstance ();
			conn = DriverManager.getConnection (dburl, user, password);
		}
		else throw new Exception("Not valid a connection found in dbConnections.xml for "+catalogName);
		return conn;
	}

	private static String[] getReferencedCatalogs(Connection conn,String siteData) {
		Hashtable<String,String> r=new Hashtable<String,String>();
		String sql="SELECT c.CID FROM tblcomponenti t, tblcontenuti c where Type='catalogSearch' and t.CID=c.CID and stateID<4";
		try {
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(sql);
			while(rs.next()) {
				try {
					r.put(getCatalogConnectionName(siteData, rs.getString(1)), "is");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r.keySet().toArray(new String[r.size()]);
	}

	private static String getCatalogConnectionName(String siteData,
			String cid) throws IOException {
		String content=getFileContent(siteData+"/data/catalogSearch"+cid+".xml");
		content=content.replaceAll("\\s", "");

		return getDummyTag(content, "catalogConnection");
	}

	private static Vector<String> fileToStringVector(File[] data) {
		Vector<String> r=new Vector<String>();
		for(int i=0;i<data.length;i++) {
			if(!data[i].isDirectory())
				r.addElement(data[i].getAbsolutePath());
			else
				r.addElement(data[i].getAbsolutePath()+"/."); // to force zip of empty dirs
		}
		return r;
	}

	private static String getDummyTag(String content, String tag) {
		String begin="<"+tag+">";
		String end="</"+tag+">";
		int b=content.indexOf(begin);
		int e=content.indexOf(end);
		String r="";
		if(b>=0 && e>=b) {
			int l=begin.length();
			r=content.substring(b+l);
			r=r.substring(0,e-(b+l));
		}
		return r;
	}

	private static String getFileContent(String filePath) throws java.io.IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            fileData.append(buf, 0, numRead);
        }
        reader.close();
        return fileData.toString();
	}

	public static File backupDb(Connection conn, String dirStore) throws SQLException, IOException {
		MySQLDump mysqldump=new MySQLDump();
        mysqldump.init(conn);
        
        String schema=conn.getCatalog();
      
        //Create temporary file to hold SQL output.
        File temp = new File(dirStore+"/"+schema+".sql");
        BufferedWriter out = new BufferedWriter(new FileWriter(temp));
          
        out.write(mysqldump.getHeader());
          
        mysqldump.dumpAllTables(out);

        out.flush();
        out.close();
        //mysqldump.cleanup(); // chiude la connessione
        return temp;
	}


}
