<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                              xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:template name="editor">
				<script type="text/javascript" src="js/tiny_mce/tiny_mce.js"></script>
		<script type="text/javascript">
		<![CDATA[
		
	tinyMCE.init({
		file_browser_callback : "fileBrowserCallBack",
		// General options
		// mode : "textareas",
		mode : "exact",
		elements : "elm2",
		theme : "advanced",
		entity_encoding : "numeric",		
		plugins : "safari,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template,jmform",

		// Theme options
		theme_advanced_buttons1 : "newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,formatselect", // save,
		theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,|,insertdate,inserttime,preview", // code,
		theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,|,sub,sup,|,charmap,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",]]>
		<xsl:if test="$permission = 15">
		theme_advanced_buttons4 : "styleselect,fontselect,fontsizeselect,|,code,help,|,forecolor,backcolor,|,visualaid,|,emotions,|,jmform,jmform_insert_input,jmform_insert_select,jmform_insert_textarea,jmform_delete",
		</xsl:if>
		<![CDATA[
		// theme_advanced_buttons4 : "insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak",
		// theme_advanced_buttons4 : "jmform,jmform_insert_input,jmform_insert_select,jmform_insert_textarea,jmform_delete",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		theme_advanced_statusbar_location : "bottom",
		theme_advanced_resizing : true,

		// Example content CSS (should be your site CSS)
		content_css : "css/content.css",

		// Drop lists for link/image/media/template dialogs
		template_external_list_url : "lists/template_list.js",
		external_link_list_url : "lists/link_list.js",
		external_image_list_url : "lists/image_list.js",
		media_external_list_url : "lists/media_list.js",
		
		force_br_newlines : true,
		force_p_newlines : false,

		// Replace values for the template plugin
		template_replace_values : {
			username : "Some User",
			staffid : "991234"
		}
	});
	

	
function fileBrowserCallBack(field_name, url, type, win) {
	var connector = "]]><xsl:value-of select="$context" /><![CDATA[/js/tiny_mce/filemanager/browser.html?Connector=]]><xsl:value-of select="$context" /><![CDATA[/fileManager&pid=]]><xsl:value-of select="$pid" /><![CDATA[";
	var enableAutoTypeSelection = true;
	
	var cType;
	tinyfck_field = field_name;
	tinyfck = win;
	
	switch (type) {
		case "image":
			cType = "Image";
			break;
		case "flash":
			cType = "Flash";
			break;
		case "file":
			cType = "File";
			break;
	}
	
	if (enableAutoTypeSelection && cType) {
		connector += "&Type=" + cType;
	}
	
	window.open(connector, "tinyfck", "modal,width=600,height=400");
}
		]]>
			
		</script>
	</xsl:template>
	
</xsl:stylesheet>