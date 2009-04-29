package JSites.fileManager;

import java.io.File;

import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.SAXException;

import JSites.generation.MyAbstractPageGenerator;
import JSites.utils.DirectoryHelper;

public class FileManager extends MyAbstractPageGenerator {
	/*************************************************************************
			Program: Mad File Manager for TinyMCE 3.
			Version: 0.1a
			Author: Mad182
			E-mail: Mad182@gmail.com
			WEB: http://sourceforge.net/projects/tinyfilemanager/

	    This program is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.

	    This program is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.

	    You should have received a copy of the GNU General Public License
	    along with this program.  If not, see <http://www.gnu.org/licenses/>.
	    
			TO DO:
			* tree reload after adding new directory;
			* need to find some way to get rid of unnecessary directories;
			* image manipulations - crop/resize, filters, etc. - partly done;
	*************************************************************************/


	//config (if your TinyMCE location is different from example, you should also check paths at lines 227 and 228)
	String file_root = "upload"; 		//where to store files, must be created and writable
	String root_path = ""; 					//path from webroot, without trailing slash. If your page is located in http://www.example.com/john/, this should be '/john'
	int thmb_size = 100;       	//max size of preview thumbnail
	boolean no_script = false;       //true/false - turns scripts into text files
	String lang = "en";           	//language (look in /mfm/lang/ for available)
	//error_reporting(0);				//'E_ALL' for debugging, '0' for use

	//array of known file types (used for icons)
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

	//upload class (see file for credits)
	//require('mfm/class.upload.php');

	//lang
	// TODO controllare l'uso delle lingue
	//$lng = array();
	//require('mfm/lang/lang_' . strtolower($lang) . '.php');
	//header("Content-type: text/html; charset=utf-8");

	//stand alone or tynimce?
	//String mode = "mce";
	// non ci interessa, noi solo tiny_mce
	//if(isset($_GET['mode'])) { $mode = $_GET['mode']; }

	//replaces special characters for latvian and russian lang., and removes all other
	
	
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
	  //String.format( "%.2f "+symbol[exp], converted_value );
	}

	//show recursive directory tree
	private void print_tree(String dir) throws SAXException {
		contentHandler.startElement("","dirlist","dirlist", this.emptyAttrs);
		if(dir==null) dir="";
		sendElement("root_path",root_path);
		sendElement("dir",dir);
		File[] d=DirectoryHelper.processFiles(root_path+dir);
		for(int i=0;d!=null && i<d.length;i++) {
			String name=dir+d[i].getName();
			String isDir=d[i].isDirectory()?"true":"false";
			AttributesImpl attr=new AttributesImpl();
			attr.addCDATAAttribute("directory", isDir);
			if(!d[i].isDirectory()) {
				long l=d[i].length();
				attr.addCDATAAttribute("size", byte_convert(l));
				
			}
			sendElement("item",name,attr);

//	  	  echo '<li><a href="' . $root_path . '/' . $ff . '/" onclick="load(\'mfm.php?viewdir=' . $ff . '\',\'view-files\'); return false;">' . $f . '</a>';
//				print_tree($ff);
//	  	  echo '</li>';
//			}
		}
		contentHandler.endElement("","dirlist","dirlist");
	}

	//show file list of given directory
	private void print_files(String dir) throws SAXException {
		print_tree(dir);
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
	
	
	public void generate() throws SAXException {
		String pid=request.getParameter("pid");
		root_path=this.source+"/page"+pid+"/";
		File p=new File (root_path);
		if(!p.exists()) p.mkdirs();
		String uploadstatus = "0";
		String t=request.getParameter("status");
		
		if(t!=null) {
			uploadstatus=t;
		}
		
		contentHandler.startDocument();
		contentHandler.startElement("","root","root", this.emptyAttrs);
		
		// TODO handles file uploads
//		if(isset($_FILES['new_file']) && isset($_POST['return'])) {
//			if(is_dir($_POST['return'])) {
//				$handle = new upload($_FILES['new_file']);
//			  if ($handle->uploaded) {
//		      $handle->file_new_name_body   = format_filename(substr($_FILES['new_file']['name'],0,-4));
//		      //resize image. more options coming soon.
//		      if(isset($_POST['new_resize']) && $_POST['new_resize'] > 0) {
//			      $handle->image_resize         = true;
//			      $handle->image_x              = (int)$_POST['new_resize'];
//			      $handle->image_ratio_y        = true;
//		      }
//		      if(isset($_POST['new_greyscale']) && $_POST['new_greyscale']) {
//						$handle->image_greyscale      = true;
//					}
//		      if(isset($_POST['new_rotate']) && $_POST['new_rotate'] == 90 or $_POST['new_rotate'] == 180 or $_POST['new_rotate'] == 270) {
//						$handle->image_rotate      		= $_POST['new_rotate'];
//					}
//					$handle->mime_check = $no_script;
//					$handle->no_script = $no_script;
//		      $handle->process($_POST['return'] . '/');
//		      if ($handle->processed) {
//		        $handle->clean();
//		        $uploadstatus = 1;
//		      } else {
//						//uncomment upload for debugging
//		        //echo 'error : ' . $handle->error;
//		        $uploadstatus = 2;
//		      }
//			  }
//			} else {
//				$uploadstatus = 3;
//			}
//		}
		
		//remove unnecessary folder
		t=request.getParameter("deletefolder");
		if(t!=null) uploadstatus=delete_directory(t);
		
		//display only directory tree for dynamic AHAH requests
		t=request.getParameter("viewtree");
		if(t!=null) {
			print_tree(file_root);
//			<ul class="dirlist">
//			<li>
//				<a href="<?php echo $root_path . '/' . $file_root; ?>/" onclick="load('mfm.php?viewdir=<?php echo $file_root; ?>','view-files'); return false;">
//					<?php echo $file_root; ?>
//				</a> 
//				<a href="#" onclick="load('mfm.php?viewtree=true','view-tree'); return false;" id="refresh-tree">
//					<?php echo $lng['refresh']; ?>
//				</a>
//				<?php print_tree($file_root); ?>
//			</li>
//			</ul>
		}

		//display file list for dynamic requests
		t=request.getParameter("viewdir");
		if(t!=null) {

		}
		else {
			t="";
		}
		sendElement("viewdir",t);

		//create directory and show results
		t=request.getParameter("newdir");
		if(t!=null) {
		    String new_title = format_filename(t);
		    String viewdir=request.getParameter("viewdir");
		    File f=new File(viewdir+"/"+new_title);
		    if(!f.isDirectory()) {
		    	if(f.mkdir()) {
//						echo '<p class="successful">&quot;' . $new_title . '&quot;' . $lng['message_created_folder'] . '</p>';
				} else {
//						echo '<p class="failed">' . $lng['message_cannot_create'] . '&quot;' . $new_title . '&quot;!<br />' . $lng['message_cannot_write'] . '</p>';
				}
		    } else {
//				echo '<p class="failed">' . $lng['message_cannot_create'] . '&quot;' . $new_title . '&quot;!<br />' . $lng['message_exists'] . '</p>';
			}
		}
			
		//remove unnecessary files
		t=request.getParameter("deletefile");
		if(t!=null) {
			String viewdir=request.getParameter("viewdir");
			String deletefile=request.getParameter("deletefile");
			File f=new File(viewdir+"/"+deletefile);
			if(!f.exists()) {
//				echo '<p class="failed">' . $lng['message_cannot_delete_nonexist'] . '</p>';
			} else {
				if(f.delete()) {
//					echo '<p class="successful">' . $lng['message_deleted'] . '</p>';
				} else {
//					echo '<p class="failed">' . $lng['message_cannot_delete'] . '</p>';
				}
			}
		}
			
		//show status messages by code
		t=request.getParameter("status");
		sendElement("status",t);
//		if(t!=null) {
//			int tv=Integer.parseInt(t);
//		  //upload file
//			if(tv == 1) {
//				output= "<p class=\"successful\">File uploaded!</p>";
//			} else if(tv == 2) {
//				output="<p class=\"failed\">Upload failed</p>";
//			} else if(tv == 3) {
//				output="<p class=\"failed\">Wrong folder</p>";
//			//remove directory
//			} else if(tv == 4) {
//				output="<p class=\"successful\">Folder deleted</p>";
//			} else if(tv == 5) {
//				output="<p class=\"failed\">Folder cannot be deleted</p>";
//			} else if(tv == 6) {
//				output="<p class=\"failed\">Folder doesn't exist</p>";
//			}
//		}

		sendElement("uploadstatus",uploadstatus);

		//finally show file list
		print_files(request.getParameter("viewdir"));
		

		
		contentHandler.endElement("","root","root");
		contentHandler.endDocument();

	}
}
