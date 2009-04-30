package JSites.fileManager;

import java.io.File;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.SAXException;

import JSites.generation.MyAbstractPageGenerator;
import JSites.utils.DirectoryHelper;

public class FileManager extends MyAbstractPageGenerator {
	String root_path="";
	String file_root="";
	String context=null;
	
	String[] file_type = {"File","Image","Flash","Media"};

	String[] file_class = {
	                "swf",
	                "txt",
	                "htm",
	                "html",
	                "zip",
	                "gz",
	                "rar",
	                "cab",
	                "tar",
	                "7z",
	                "deb",
	                "rpm",
	                "php",
	                "mp3",
	                "ogg",
	                "mid",
	                "avi",
	                "mpg",
	                "flv",
	                "mpeg",
	                "pdf",
	                "ttf",
	                "exe"
	};
	
	private String format_filename(String filename) {
		filename=org.jopac2.utils.Utils.removeAccents(filename);
		//$bads = array(' ','ƒÅ','ƒç','ƒì','ƒ£','ƒ´','ƒ∑','ƒº','≈Ü','≈ó','≈°','≈´','≈æ','ƒÄ','ƒå','ƒí','ƒ¢','ƒ™','ƒ∂','ƒª','≈Ö','≈ñ','≈†','≈™','≈Ω','$','&','–ê','–ë','–í','–ì','–î','–ï','–Å','–ñ','–ó','–ò','–ô','–ö','–õ','–ú','–ù','–û','–ü','–†','–°','–¢','–£','–§','–•','–¶','–ß','–®','–©','–™','–´–¨','–≠','–Æ','–Ø','–∞','–±','–≤','–≥','–¥','–µ','—ë','–∂','–∑','–∏','–π','–∫','–ª','–º','–Ω','–æ','–ø','—Ä','—Å','—Ç','—É','—Ñ','—Ö','—Ü','—á','—à—â','—ä','—ã','—å','—ç','—é','—è');
		//$good = array('-','a','c','e','g','i','k','l','n','r','s','u','z','A','C','E','G','I','K','L','N','R','S','U','Z','s','and','A','B','V','G','D','E','J','Z','Z','I','J','K','L','M','N','O','P','R','S','T','U','F','H','C','C','S','S','T','T','E','Ju','Ja','a','b','v','g','d','e','e','z','z','i','j','k','l','m','n','o','p','r','s','t','u','f','h','c','c','s','t','t','y','z','e','ju','ja');
		//$filename = str_replace($bads,$good,trim($filename));
		String allowed = "/[^a-z0-9\\.\\-\\_\\\\]/i";
		filename = filename.replaceAll(allowed, ""); //preg_replace($allowed,'',$filename);
		return filename;
	}

	//convert file size to human readable format
	private String byte_convert(long bytes) {
	  String[] symbol = {"B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB"};
	  int exp = 0;
	  int converted_value = 0;
	  if( bytes > 0 ) {
	    exp = (int)Math.floor( Math.log(bytes)/Math.log(1024) );
	    converted_value = (int)( bytes/Math.pow(1024,Math.floor(exp)) );
	  }
	  return converted_value + " "+symbol[exp];
	}

	private void getFolders(String sResourceType,String sCurrentFolder) throws SAXException {
		contentHandler.startElement("","Folders","Folders", this.emptyAttrs);
		if(sCurrentFolder==null) sCurrentFolder="";
		if(!root_path.endsWith("/")) root_path+="/";

		File[] d=DirectoryHelper.processFiles(root_path+sCurrentFolder);
		for(int i=0;d!=null && i<d.length;i++) {
			if(d[i].isDirectory()) {
				String name=d[i].getName();
				
				AttributesImpl attr=new AttributesImpl();
				attr.addCDATAAttribute("name", name);
				
				sendElement("Folder","",attr);
			}
		}
		contentHandler.endElement("","Folders","Folders");
	}

	private void getFiles(String sResourceType,String sCurrentFolder) throws SAXException {
		contentHandler.startElement("","Files","Files", this.emptyAttrs);
		if(sCurrentFolder==null) sCurrentFolder="";
		if(!root_path.endsWith("/")) root_path+="/";
		

		File[] d=DirectoryHelper.processFiles(root_path+sCurrentFolder);
		for(int i=0;d!=null && i<d.length;i++) {
			AttributesImpl attr=new AttributesImpl();
			String name=d[i].getName();
			
			if(!d[i].isDirectory()) {
				String fSize=byte_convert(d[i].length());
				attr.addCDATAAttribute("name", name);
				attr.addCDATAAttribute("size", fSize);
				sendElement("File","",attr);
			}
		}
		contentHandler.endElement("","Files","Files");
	}
	
	private void getFoldersAndFiles(String sResourceType,String sCurrentFolder) throws SAXException {
		getFolders(sResourceType,sCurrentFolder);
		getFiles(sResourceType,sCurrentFolder);
	}

	private String delete_directory(String dirname) {
		String r="6";
		if(dirname!=null) {
			r="5";
			boolean k=DirectoryHelper.deleteDir(dirname);
			if(k) r="4";
		}
		return r;
	}
	
	private void sendError(int number, String text) throws SAXException {
		AttributesImpl errorAttr=new AttributesImpl();
		errorAttr.addCDATAAttribute("number", Integer.toString(number));
		errorAttr.addCDATAAttribute("text", text);
		sendElement("Error","",errorAttr);
	}
	
	private void createFolder(String resourceType, String currentFolder) throws SAXException {
		int sErrorNumber=0;
		String sErrorMsg="0";
		String newFolderName=request.getParameter("NewFolderName");
		
		if(newFolderName.contains("..")) {
			sErrorNumber=102;
		}
		else {
			if(!root_path.endsWith("/")) root_path+="/";
			File nf=new File(root_path+currentFolder+"/"+newFolderName);
			if(nf.mkdir()) {
				// ok
				sErrorNumber=0;
			}
			else {
				// error
				sErrorNumber=102;
			}
		}
		if(sErrorNumber!=0) {
			sendError(sErrorNumber,sErrorMsg);
		}
	}
	
	
	public void generate() throws SAXException {
		String pid=request.getParameter("pid");
		root_path=this.source+"/page"+pid+"/";
		File p=new File (root_path);
		if(!p.exists()) p.mkdirs();
		
		AttributesImpl startAttr=new AttributesImpl();
		
		String sCommand=request.getParameter("Command");
		String sResourceType=request.getParameter("Type");
		String sCurrentFolder=request.getParameter("CurrentFolder");
		
		try {
			context=this.parameters.getParameter("context");
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		
		startAttr.addCDATAAttribute("command", sCommand);
		startAttr.addCDATAAttribute("resourceType", sResourceType);
		
		contentHandler.startDocument();
		contentHandler.startElement("","Connector","Connector", startAttr);
		
		AttributesImpl cf=new AttributesImpl();
		cf.addCDATAAttribute("path", sCurrentFolder);
		cf.addCDATAAttribute("url", getUrlFromPath(sResourceType,sCurrentFolder));
		
		sendElement("CurrentFolder","",cf);

		if(inArray(sResourceType,file_type)) {
			// TODO: Check current foldere syntax
			if(sCurrentFolder!=null && sCommand!=null && !sCurrentFolder.contains("..")) {
				if(sCommand.equals("GetFolders")) getFolders(sResourceType, sCurrentFolder);
				else if(sCommand.equals("GetFoldersAndFiles")) getFoldersAndFiles(sResourceType, sCurrentFolder);
				else if(sCommand.equals("CreateFolder")) createFolder(sResourceType, sCurrentFolder);
			}
			else {
				sendError(102,"");
			}
		}
		
		contentHandler.endElement("","Connector","Connector");
		contentHandler.endDocument();

	}

	private boolean inArray(String item, String[] array) {
		boolean r=false;
		for(int i=0;item!=null && array!=null && i<array.length;i++) {
			if(item.equals(array[i])) {
				r=true;
				break;
			}
		}
		return r;
	}

	private String getUrlFromPath(String resourceType, String currentFolder) {
		return context+"/images/"+currentFolder;
	}
}
