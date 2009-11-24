package JSites.fileManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.servlet.multipart.Part;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.SAXException;

import JSites.components.FileUploadManager;
import JSites.generation.MyAbstractPageGenerator;
import JSites.utils.DirectoryHelper;

public class FileManager extends MyAbstractPageGenerator {
    private FileUploadManager upload_manager;
    private Part part;
    
	String root_path="";
	String file_root="";
	String context=null;
	String pid=null;
	
	String[] file_type = {"File","Image","Flash","Media"};

	String[] file_class = {
	                "jpg",
	                "txt",
	                "zip",
	                "gz",
	                "rar",
	                "tar",
	                "7z",
	                "mp3",
	                "ogg",
	                "mid",
	                "gif",
	                "avi",
	                "mpg",
	                "mpeg",
	                "pdf",
	                "png",
	                "jpeg"
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

	private void deleteFile(String sResourceType,String sCurrentFolder) throws SAXException {
	    String sErrorNumber = "0" ;
	    String sErrorMsg = "" ;

	    String fileName=request.getParameter("FileName");
	    if(fileName!=null) {
	        // Map the virtual path to the local server path.
	    	if(!root_path.endsWith("/")) root_path+="/";
	        if (fileName.contains("..")) {
	        	sErrorNumber = "102" ; // Invalid file name.
	            sErrorMsg = "Invalid file name";
	        } else {
		    	File f=new File(root_path+sCurrentFolder+"/"+fileName);

	            if (f.delete()) {
	                sErrorNumber = "0" ; // deleted
	            } else {
	                sErrorNumber = "103" ; // not deleted
	                sErrorMsg = "Could not delete file "+root_path+sCurrentFolder+"/"+fileName;
	            }
	        }
	    } else {
	        sErrorNumber = "102" ; // no file set
	        sErrorMsg = "No file specified";
	    }
	    // Create the "Error" node.

	    sendError(sErrorNumber, sErrorMsg);

	}

	
	private void sendError(String number, String text) throws SAXException {
		AttributesImpl errorAttr=new AttributesImpl();
		errorAttr.addCDATAAttribute("number", number);
		errorAttr.addCDATAAttribute("text", text);
		sendElement("Error","",errorAttr);
	}
	
	private void sendError(int number, String text) throws SAXException {
		sendError(Integer.toString(number),text);
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
	
	private void fileUpload(String resourceType, String currentFolder) throws SAXException, IOException {
		Request request = ObjectModelHelper.getRequest(objectModel);
    	ObjectModelHelper.getContext(objectModel).getAttributeNames().nextElement();
        part = (Part) request.get("NewFile");        
        	
        InputStream in = null;
        FileOutputStream out = null;
        
        try {
        	String fileName=part.getFileName();
        	String extension=null;
        	if(fileName.contains(".")) {
        		extension=fileName.substring(fileName.indexOf(".")+1);
        	}
        	if(extension==null || inArray(extension,file_class)) {
				String path = root_path+"/" + currentFolder + "/"+ fileName;
				File destination = new File(path);
	    		byte[] readchar = new byte[20480];
	    		in = part.getInputStream();
				out = new FileOutputStream(destination);
				
	    		int c = 0;
	    		while ((c = in.read(readchar)) != -1) {
	    			out.write(readchar, 0, c);
	    		}
//	    		out.close();
//	    		in.close();
	    		
//	    		sendError(0, "");
	    		
	    		contentHandler.startElement("","html","html", emptyAttrs);
	    		contentHandler.startElement("","head","head", emptyAttrs);
	    		
	    		AttributesImpl ok=new AttributesImpl();
	    		ok.addCDATAAttribute("type", "text/javascript");
	    		String js="window.parent.frames['frmUpload'].OnUploadCompleted('0','"+fileName+"');";
	    		sendElement("script", js, ok);
	    		
	    		contentHandler.endElement("","head","head");
	    		contentHandler.endElement("","html","html");
        	}
        	else {
        		sendError(202,"");
        	}

//			sendElement("status","ok");
//	        sendElement("filename",part.getFileName());
//	        sendElement("mimetype",part.getMimeType());
//	        sendElement("uploadname",part.getUploadName());
//	        sendElement("size",String.valueOf(part.getSize()));
//	        sendElement("uploadfolder",upload_manager.getUploadFolder());
			
		} catch (Exception e) {
			e.printStackTrace();
			String message="error";
			sendError(202,message);
		}
		finally {
			in.close();
			out.close();
		}
		
	}
	
	
	public void generate() throws SAXException {
		pid=request.getParameter("pid");
		root_path=this.source+"/page"+pid+"/";
		File p=new File (root_path);
		if(!p.exists()) p.mkdirs();
		
		
		String sCommand=request.getParameter("Command");
		String sResourceType=request.getParameter("Type");
		String sCurrentFolder=request.getParameter("CurrentFolder");
		
		try {
			context=this.parameters.getParameter("context");
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		
		contentHandler.startDocument();
		
		if(sCommand!=null && sCommand.equals("FileUpload")) {
			try {
				fileUpload(sResourceType,sCurrentFolder);
			} catch (IOException e) {
				sendError(102,"");
			}
		}
		else {
			AttributesImpl startAttr=new AttributesImpl();

		
			startAttr.addCDATAAttribute("command", sCommand);
			startAttr.addCDATAAttribute("resourceType", sResourceType);
			
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
					else if(sCommand.equals("DeleteFile")) deleteFile(sResourceType, sCurrentFolder);
				}
				else {
					sendError(102,"");
				}
			}
			
			contentHandler.endElement("","Connector","Connector");
		}
		contentHandler.endDocument();

	}

	private boolean inArray(String item, String[] array) {
		boolean r=false;
		for(int i=0;item!=null && array!=null && i<array.length;i++) {
			if(item.equalsIgnoreCase(array[i])) {
				r=true;
				break;
			}
		}
		return r;
	}

	private String getUrlFromPath(String resourceType, String currentFolder) {
		return "./images/page"+pid+currentFolder;
	}
	
    public void compose(ComponentManager manager) throws ComponentException {
    	super.compose(manager);
        upload_manager = (FileUploadManager) manager.lookup(FileUploadManager.ROLE);
    }
}
