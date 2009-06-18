<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:include href="../../stylesheets/xslt/helpedit.xslt" />

	<xsl:param name="cid" />
	<xsl:param name="time" />
	<xsl:param name="pid" />
	<xsl:param name="context" />
	<xsl:param name="permission" />

	<xsl:template match="/">
		<xsl:apply-templates select="catalogSearch" />
		<xsl:call-template name="callhelp" />
	</xsl:template>


	<xsl:template match="catalogSearch">

		<div class="{$time}">
			<div class="sezione">
				<fieldset>
					<legend>Catalogo</legend>
					<table border="0" class="noc" id="noc">
					<xsl:apply-templates select="catalogName" />
					<xsl:apply-templates select="catalogConnection" />
					<xsl:apply-templates select="catalogFormat" />
					<xsl:apply-templates select="dbType" />
					</table>
					<script type="text/javascript" wiki="false">
					<![CDATA[

						 function autoPopup() {
						   var stili = "top=10, left=10, width=400, height=250, status=no, menubar=no, toolbar=no scrollbar=no";
						   var nomecatalogo = document.getElementById("catalogName").value;
						   var connessione = document.getElementById("catalogConnection").value;
						   var dbtype = "";

							for( i = 0; i < document.editform.dbType.length; i++ ) {
								if( document.editform.dbType[i].checked == true ) {
									dbtype = document.editform.dbType[i].value;
									break;
								}
							}

						   var testo = window.open("importCatalog?pid=]]><xsl:value-of select="$pid" /><![CDATA[&cid=]]><xsl:value-of select="$cid" /><![CDATA[&conn="+connessione+"&dbtype="+dbtype, nomecatalogo, stili);
						 }

					]]>
					<!-- 
					for( i = 0; i < document.editform.dbType.length; i++ ) {
							if( document.editform.dbType[i].checked == true )
								dbtype = document.editform.dbType[i].value;
								break;
							}
					 -->
					</script>
					<button onClick="javascript:autoPopup();return false;">Load data</button>
				</fieldset>
				
				<fieldset>
				 <legend>Canali ricerca attivati</legend>
				 	<xsl:for-each select="search">
				 		<input type="checkbox" name="search-{@name}" value="true">
				 			<xsl:if test="string-length(@checked) != 0">
				 				<xsl:attribute name="checked">checked</xsl:attribute>
				 			</xsl:if>
				 		</input>
				 		<xsl:value-of select="@desc" />
				 		<br/>
				 	</xsl:for-each>
				</fieldset>
				
				<fieldset>
				 <legend>Canali liste attivati</legend>
					 <xsl:for-each select="list">
				 		<input type="checkbox" name="list-{@name}" value="true">
				 			<xsl:if test="string-length(@checked) != 0">
				 				<xsl:attribute name="checked">checked</xsl:attribute>
				 			</xsl:if>
				 		</input>
				 		<xsl:value-of select="@desc" />
				 		<br/>
				 	</xsl:for-each>
				</fieldset>
				
				<xsl:apply-templates select="template" />
			</div>		
		</div>
	</xsl:template>

	<xsl:template match="catalogFormat">
		<tr>
		<td><label class="cat_data_label">Formato catalogo</label></td>
		<td><input type="text" size="40" name="catalogFormat" id="catalogFormat" value="{text()}" /></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="dbType">
		<tr>
		<td>
			<input type="radio" name="dbType" value="derby">
				<xsl:if test="text() = 'derby'">
	 				<xsl:attribute name="checked">checked</xsl:attribute>
				</xsl:if>
			</input> Derby
		</td>
		<td>
			<input type="radio" name="dbType" value="mysql">
				<xsl:if test="text() = 'mysql'">
	 				<xsl:attribute name="checked">checked</xsl:attribute>
				</xsl:if>
			</input> Mysql
		</td>
		</tr>
	</xsl:template>
	
	<xsl:template match="catalogFile">
		<tr>
		<td><label class="cat_data_label">File da caricare</label></td>
		<td>
			<input type="hidden" size="40" name="catalogFile" value="{text()}" />
			
		</td>
		</tr>
	</xsl:template>

	<xsl:template match="catalogName">
		<tr>
		<td><label class="cat_data_label">Nome catalogo</label></td>
		<td><input type="text" size="40" name="catalogName" id="catalogName" value="{text()}" /></td>
		</tr>
	</xsl:template>


	<xsl:template match="catalogConnection">
		<tr>
		<td><label class="cat_data_label">Nome connessione</label></td>
		<td><input type="text" size="40" name="catalogConnection" id="catalogConnection" value="{text()}" /></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="template">
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
		plugins : "safari,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template",

		// Theme options
		theme_advanced_buttons1 : "newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,styleselect,formatselect,fontselect,fontsizeselect", // save,
		theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,|,insertdate,inserttime,preview,|,forecolor,backcolor]]><xsl:if test="$permission = 15">,|,code</xsl:if><![CDATA[", 
		theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",
		// theme_advanced_buttons4 : "insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak",
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

		// Replace values for the template plugin
		template_replace_values : {
			username : "Some User",
			staffid : "991234"
		}
	});
	

	
function fileBrowserCallBack(field_name, url, type, win) {
	var connector = "../../filemanager/browser.html?Connector=]]><xsl:value-of select="$context" /><![CDATA[/fileManager&pid=]]><xsl:value-of select="$pid" /><![CDATA[";
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
	
		<b>Descrizione:</b>
		<br/>
		<input type="hidden" name="UndoText" id="UndoText"/>
		<textarea id="elm2" name="template" rows="10" class="edittextarea" tinyMCE_this="true">
			<!--  xsl:value-of select="text()" /-->
			<!-- xsl:copy-->
				<xsl:apply-templates select="@*|node()|text()|*" />
			<!--  /xsl:copy-->
		</textarea>
		<br />
	</xsl:template>

</xsl:stylesheet>