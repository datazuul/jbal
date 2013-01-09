<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:sql="http://apache.org/cocoon/SQL/2.0"
							xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- Questo include serve pei link de modifica, elimina e (dis)attiva 
		 ma anca per le combo de ordinamento  -->
                              
	<xsl:param name="cid"/>
	<xsl:param name="pid"/>
	<xsl:param name="pacid"/>
	<xsl:param name="editing"/>
	<xsl:param name="lang"/>
    <xsl:param name="validating"/>
    <xsl:param name="disabling"/>
    <xsl:param name="time"/>
    <xsl:param name="extra" />
    <xsl:param name="type" />
    
    <xsl:variable name="status"><xsl:value-of select="/root/status"/></xsl:variable>
    <xsl:variable name="uploadstatus"><xsl:value-of select="/root/uploadstatus"/></xsl:variable>
    <xsl:variable name="viewdir"><xsl:value-of select="/root/viewdir"/></xsl:variable>
    <xsl:variable name="rootpath"><xsl:value-of select="/root/rootpath"/></xsl:variable>
    <xsl:variable name="fileroot"><xsl:value-of select="/root/fileroot"/></xsl:variable>
    
    
    <xsl:template match="item">
     <!-- 	  while($f = readdir($d)) {
//	    if(strpos($f, '.') === 0) continue;
//	    $ff = $c . '/' . $f;
//	    $ext = strtolower(substr(strrchr($f, '.'), 1));
//	    if(!is_dir($ff)) {
 -->
		<tr class="light">
	<!-- show preview and different icon, if file is image  -->
	<!-- 
//		    $imageinfo = @getimagesize($ff);
//		    if($imageinfo && $imageinfo[2] > 0 && $imageinfo[2]< 4) {
//		    	$resize = '';
//		    	if($imageinfo[0] > $thmb_size or $imageinfo[1] > $thmb_size) {
//			    	if($imageinfo[0] > $imageinfo[1]) {
//							$resize = ' style="width: ' . $thmb_size . 'px;"';
//						} else {
//							$resize = ' style="height: ' . $thmb_size . 'px;"';
//						}
//					}
//					if ($imageinfo[2] == 1) {
//						$imagetype = "image_gif";
//					} elseif ($imageinfo[2] == 2) {
//						$imagetype = "image_jpg";
//					} elseif ($imageinfo[2] == 3) {
//						$imagetype = "image_jpg";
//					} else {
//						$imagetype = "image";
//					}
//					echo '<td><a class="file thumbnail ' . $imagetype . '" href="#" onclick="submit_url(\'' . $root_path . '/' . $ff . '\');">' . $f . '<span><img' . $resize . ' src="' . $root_path . '/' . $ff . '" /></span></a>'; echo '</td>';
//				//known file types
//				} elseif(in_array($ext,$file_class)) {
//					echo '<td><a class="file file_' . $ext . '" href="#" onclick="submit_url(\'' . $root_path . '/' . $ff . '\');">' . $f . '</a>'; echo '</td>';
//				//all other files
//				} else {
 -->
			<td>
				<a class="file unknown" href="#" onclick="submit_url(\'' . $root_path . '/' . $ff . '\');">
					<xsl:value-of select="." />
				</a>
			</td>
			<td>
				<xsl:value-of select="@size" />
			</td>
			<td class="delete">
				<a href="#" title="' . $lng['delete_title'] . '" onclick="delete_file(\'' . $c . '\',\'' . $f . '\');">
					Delete
				</a>
			</td>
		</tr>
    </xsl:template>
    
    <xsl:template match="dirlist">
    	<table>
    		<xsl:apply-templates />
    	</table>
    </xsl:template>
    
    
    <xsl:template match="viewdir">
 		<ul id="browser-toolbar">
			<li class="file-new">
				<a href="#" title="New file title" onclick="toggle_visibility('load-file'); return false;">
					New file
				</a>
			</li>
			<li class="folder-new">
				<a href="#" title="new_folder" onclick="create_folder('$viewdir'); return false;">
					New folder
				</a>
			</li>
  				<li class="folder-delete">
				<a href="#" title="delete_folder" onclick="delete_folder('$viewdir');">
					Delete folder
				</a>
			</li>
			<li class="file-refresh">
				<a href="#" title="refresh" 
						onclick="load('fileManager?viewdir=$viewdir','view-files'); return false;">
					Refresh
				</a>
			</li>
		</ul>
					
		<div id="current-loction">
			<xsl:value-of select="$rootpath" />/<xsl:value-of select="$viewdir" />
		</div>
					
		<form style="display: none;" id="load-file" action="" class="load-file" method="post" enctype="multipart/form-data">

			<fieldset>
			  <legend>file</legend>
				<input type="hidden" value="{$viewdir}" name="return" />
				<label>file<input type="file" name="new_file" /></label>
			</fieldset>
			
			<fieldset>
			  <legend>file manipulation</legend>
			  <table>
					<tr>
						<td><label for="new_resize">width</label></td>
						<td><input type="text" class="number" maxlength="4" id="new_resize" name="new_resize" value="" /> px</td>
					</tr>
					<tr>
						<td><label for="new_rotate">rotate</label></td>
						<td>
							<select id="new_rotate" name="new_rotate">
							  <option value="0"></option>
							  <option value="90">90</option>
							  <option value="180">180</option>
							  <option value="270">270</option>
							</select>
						</td>
					</tr>
					<tr>
						<td></td>
						<td>
							<input type="checkbox" class="checkbox" id="new_greyscale" name="new_greyscale" />
							<label for="new_greyscale">greyscale</label>
						</td>
					</tr>
			  </table>
			</fieldset>
			
			<input type="submit" id="insert" value="Submit" />
			
		</form>
    </xsl:template>
    
    
	<xsl:template match="root">
		<html>

			<head>
			  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
			  <meta http-equiv="content-language" content="en" />
			  <title>File manager</title>
			  <link rel="stylesheet" href="mfm/style.css" type="text/css" />
			  <link rel="stylesheet" href="/tiny_mce/themes/advanced/skins/default/dialog.css" type="text/css" />
			  <script type="text/javascript" src="/tiny_mce/tiny_mce_popup.js"></script>
				<script type="text/javascript">
					//<![CDATA[
					
					//load content using AHAH (asynchronous HTML and HTTP)
			  	function ahah(url, target) {
						document.getElementById(target).innerHTML = '<img src="mfm/loading.gif" alt="" /> <?php echo $lng['loading']; ?>';
						if (window.XMLHttpRequest) {
							req = new XMLHttpRequest();
						} else if (window.ActiveXObject) {
							req = new ActiveXObject("Microsoft.XMLHTTP");
						}
						if (req != undefined) {
							req.onreadystatechange = function() {ahahDone(url, target);};
							req.open("GET", url, true);
							req.send("");
						}
					}
						
					function ahahDone(url, target) {
						if (req.readyState == 4) {
							if (req.status == 200) {
								document.getElementById(target).innerHTML = req.responseText;
							} else {
							document.getElementById(target).innerHTML=" AHAH Error:\n"+ req.status + "\n" +req.statusText;
							}
						}
					}
						
					function load(name, div) {
						ahah(name,div);
						return false;
					}
				
				  //ask for folder title and request it's creation
					function create_folder(viewdir) {
						var name=prompt("<?php echo $lng['ask_folder_title']; ?>","<?php echo $lng['default_folder']; ?>");
						if (name!=null && name!=""){
					  	load('mfm.php?viewdir=' + viewdir + '&newdir=' + name + '','view-files');
					  }
					}

//					<?php
//						//first one for inserting file name into given field, second for working as tinyMCE plugin
//						if ($mode == 'standalone' && isset($_GET['field'])) {
//					?>
//			    function submit_url(URL) {
//			      window.opener.document.getElementById('<?php echo $_GET['field']; ?>').value = URL;
//						self.close();
//			    }

			    function submit_url(URL) {
			      var win = tinyMCEPopup.getWindowArg("window");
			      win.document.getElementById(tinyMCEPopup.getWindowArg("input")).value = URL;
			      if (win.ImageDialog.getImageData) win.ImageDialog.getImageData();
			      if (win.ImageDialog.showPreviewImage) win.ImageDialog.showPreviewImage(URL);
			      tinyMCEPopup.close();
			    }

			    
			    //confirm and delete file
			    function delete_file(dir,file) {
						var answer = confirm("Confirm delete file?");
						if (answer){
					  	load('mfm.php?viewdir=' + dir + '&deletefile=' + file,'view-files');
						} 
					}

			    //confirm and delete folder
			    function delete_folder(dir) {
						var answer = confirm("Confirm delete folder?");
						if (answer){
					  	location.href = 'mfm.php?deletefolder=' + dir;
						}
					}

					//show/hide element (for file upload form)
					function toggle_visibility(id) {
						var e = document.getElementById(id);
						if(e != null) {
							if(e.style.display == 'none') {
								e.style.display = 'block';
							} else {
								e.style.display = 'none';
							}
						}
					}
					//]]>
				</script>
			</head>
<!-- 
			<?php
				$return = $file_root;
				if(isset($_REQUEST['return'])) {$return = $_REQUEST['return'];}
			?>
 -->
			<body onload="load('fileManager?status={$uploadstatus}&amp;viewdir={$viewdir}','view-files');">
				<div id="browser-wrapper">
			    <div id="view-tree">
						<ul class="dirlist">
							<li>
								
 
								<a href="{$rootpath}/{$fileroot}/" onclick="load('fileManager?viewdir={$fileroot}','view-files'); return false;">
									<xsl:value-of select="$fileroot" />
								</a>

								<a href="#" title="Refresh" onclick="load('fileManager?viewtree=true','view-tree'); return false;" id="refresh-tree">
									Refresh
								</a>
								
								<xsl:apply-templates />
							  <!--  <?php print_tree($file_root); ?>  -->
							</li>
						</ul>
					</div>
			    <div id="view-files">
			    	
			    </div>
			  </div>

			</body>

			</html>
	</xsl:template>

</xsl:stylesheet>