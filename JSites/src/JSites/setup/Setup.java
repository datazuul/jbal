package JSites.setup;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;

public class Setup {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sourcePath=getPath();
		String dataComponentsDir="/siti";
		String siteName="treviglio";
		String dbName="db"+siteName.toLowerCase();
		String virtualDomainName="www."+siteName+".it";
		String dbUrl="jdbc:mysql://localhost/";
		String dbUser="root";
		String dbPassword="";
		String classDriver = "com.mysql.jdbc.Driver";
		
		
		Setup.creaDirectory(sourcePath, dataComponentsDir+"/"+siteName);
		Setup.creaSito(sourcePath, siteName);
		Setup.generaSitemapSito(sourcePath, siteName, dbName, dataComponentsDir+"/"+siteName);
		Setup.configuraDb(sourcePath,dbName,dbUser,dbPassword, dbUrl);
		Setup.configureVirtualHost(sourcePath,siteName, virtualDomainName);
		Setup.configureHostSelector(sourcePath,siteName);
		Setup.caricaDb(classDriver, dbName, dbUrl, dbUser, dbPassword);
		Setup.displayNotes(siteName, virtualDomainName, dataComponentsDir);
	}
	
	public static void displayNotes(String siteName, String virtualDomainName, String dataComponentsDir) {
		System.out.println("" +
				"OPERAZIONI DA FARE A MANO:" +
				"1. nel file "+siteName+"/xslt/xml2html.xslt modificare:\n" +
				"\ta. il titolo della pagina nella sezione <HEAD>\n" +
				"\tb. nel match header l'alt relativo al logo, eventualmente il file del logo\n" +
				"\tc. nel <div id=\"scritta_sba\"> l'indicazione della scritta e dell'alt associato\n" +
				"\n" +
				"2. inserire il logo in "+dataComponentsDir+"/"+siteName+"/images/logo.jpg (o come indicato nel punto 1b\n" +
				"" +
				"\nIL SITO RISPONDERA' SOLO ALLE RICHIESTE HTTP DI "+virtualDomainName+"\n" +
				"(aggiornare dns o /etc/hosts)\n");
	}
	
	public static String getPath() {
		URL u=ClassLoader.getSystemResource("JSites");
		return u.getPath().substring(0,u.getPath().indexOf("WEB-INF")-1);
	}
	
	private static void caricaDb(String classDriver, String dbName, String dbUrl, 
			String dbUser, String dbPassword) {

		try {
			Connection mysql=DbSetup.getConnection("mysql", classDriver, dbUrl, dbUser, dbPassword);
			DbSetup.createDatabase(mysql,dbName);
			mysql.close();
			Connection conn=DbSetup.getConnection(dbName, classDriver, dbUrl, dbUser, dbPassword);
			DbSetup.createTableInfo(conn);
			DbSetup.loadTableInfo(conn);
			DbSetup.createTableTblComponenti(conn);
			DbSetup.loadTableTblComponenti(conn);
			DbSetup.createTableTblContenuti(conn);
			DbSetup.loadTableTblContenuti(conn);
			DbSetup.createTableTblPagine(conn);
			DbSetup.loadTableTblPagine(conn);
			DbSetup.createTableTblRedirects(conn);
			DbSetup.loadTableTblRedirects(conn);
			DbSetup.createTableTblRoles(conn);
			DbSetup.loadTableTblRoles(conn);
			DbSetup.createTableTblStrutture(conn);
			DbSetup.loadTableTblStrutture(conn);
			DbSetup.createTableTblStati(conn);
			DbSetup.loadTableTblStati(conn);
			DbSetup.createTableNews(conn);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void creaDirectory(String sourcePath, String dataDirectory) {
		// Crea la directory se non esiste
		boolean success = (new File(dataDirectory)).mkdirs();
	    if (!success) {
	        System.out.println("ERROR creating dataDirectory");
	    }

		// copiare il contenuto di JSites/example/example_components in dataDirectory
	    try {
			FileUtils.copyDirectory(new File(sourcePath+"/../example/example_components"), new File(dataDirectory));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void creaSito(String sourcePath, String nomeSito) {
		// copia il contenuto di WebContent/exmple in nomeSito
		try {
			FileUtils.copyDirectory(new File(sourcePath+"/example"), new File(sourcePath+"/"+nomeSito));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void generaSitemapSito(String sourcePath, String nomesito, String nomeDb, String dataDirecotry) {
		// modifica <global-variables> nella sitemap
		try {
			File sitemap=new File(sourcePath+"/"+nomesito+"/sitemap.xmap");
			FileUtils.copyFile(sitemap, 
					new File(sourcePath+"/"+nomesito+"/sitemap.xmap.old"));
			
			String sitemapContent=FileUtils.readFileToString(sitemap);
			
			/**
			 * TODO: controllare come si usano le regular expression in java
			 */
			sitemapContent=sitemapContent.replaceFirst("<db>.+</db>", "<db>"+nomeDb+"</db>");
			sitemapContent=sitemapContent.replaceFirst("<sitename>.+</sitename>", 
					"<sitename>"+nomesito+"</sitename>");
			sitemapContent=sitemapContent.replaceFirst("<data-directory>.+</data-directory>", 
					"<data-directory>"+dataDirecotry+"</data-directory>");
			
			FileUtils.writeStringToFile(sitemap, sitemapContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void modifyContent(File f, String ifNotContains, String replace, String with) {
		try {
			String content=FileUtils.readFileToString(f);
			
			// controlla che non ci sia gia' una definizione analoga
			if(!content.contains(ifNotContains)) {
				// fa una copia di backup del file
				String backup=f.getPath()+".old";
				FileUtils.copyFile(f, 
						new File(backup));
				
				content=content.replaceFirst(replace, with);
				
				FileUtils.writeStringToFile(f, content);
			}
			else {
				System.out.println("WARNING: File "+f.getPath()+" contiene gia' "+ifNotContains);
				System.out.println("WARNING: nessuna modifica eseguita al file "+f.getPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void configuraDb(String sourcePath, String nomeDb, String dbUser, String dbPassword, String dbUrl) {
		// crea voce in WEB-INF/dbConnections.xml
		File dbConnections=new File(sourcePath+"/WEB-INF/dbConnections.xml");
		String insert="<datasources>\n" +
			"\t<jdbc name=\""+nomeDb+"\">\n" +
			"\t\t<pool-controller max=\"500\" min=\"50\" />\n" +
			"\t\t<auto-commit>true</auto-commit>\n" +
			"\t\t<dburl>"+dbUrl+nomeDb+"?autoReconnect=true</dburl>\n" +
			"\t\t<user>"+dbUser+"</user>\n" +
			"\t\t<password>"+dbPassword+"</password>\n" +
			"\t</jdbc>";
		modifyContent(dbConnections, "<jdbc name=\""+nomeDb+"\">", "<datasources>", insert);
		
	}

	
	public static void configureVirtualHost(String sourcePath, String nomeSito, String nomedominio) {
		File vh=new File(sourcePath+"/sitemap/virtualhosts.xml");
		String insert="\t<host name=\""+nomeSito+"\" value=\""+nomedominio+"\" />\n" +
			"</map:selector>";
		modifyContent(vh, "<host name=\""+nomeSito+"\"", "</map:selector>", insert);
	}

	public static void configureHostSelector(String sourcePath, String nomeSito) {
		// configurare selector in sitemap/hostselector.xml
		File ht=new File(sourcePath+"/sitemap/hostselector.xml");
		String insert="<map:select type=\"host\">\n" +
			"\t<map:when test=\""+nomeSito+"\">\n" +
			"\t\t<map:mount uri-prefix=\"\" src=\""+nomeSito+"/sitemap.xmap\" />\n" +
			"\t</map:when>\n";
		modifyContent(ht, "<map:when test=\""+nomeSito+"\">", "<map:select type=\"host\">", insert);
	}
}
