<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:sql="http://apache.org/cocoon/SQL/2.0"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:include href="../../stylesheets/xslt/helpedit.xslt" />
    <!-- Questo include serve per la cornice di help  -->
	
	<xsl:param name="cid" />
	<xsl:param name="time" />
	<xsl:param name="pid" />
	<xsl:param name="extra" />
	<xsl:param name="context" />

	<xsl:template match="/">
		<xsl:apply-templates select="section" />
		
		<xsl:call-template name="callhelp" />
		
	</xsl:template>

	<!--  SEZIONE -->

	<xsl:template match="section">
		<div class="{$time}">
			<div class="sezione">
				<div class="sezione_immagine">
					<xsl:apply-templates select="image" />
				</div>
				<div class="sezione_contenuto">
					<xsl:call-template name="news"/>
					<xsl:apply-templates select="titolo" />
					<xsl:apply-templates select="testo" />
				</div>
			</div>		
		</div>
	</xsl:template>

	<!--  TITOLO -->

	<xsl:template match="titolo">
		<!--div-->

		<!-- div id="sezione_titolo"-->
		<b>Titolo:</b>
		<br />
		<input type="text" size="40" name="title" value="{text()}" />
		<xsl:choose>
			<xsl:when test="@type != ''">
				<input type="text" size="1" name="title_type" value="{@type}" />
			</xsl:when>
			<xsl:otherwise>
				<input type="text" size="1" name="title_type" value="2" />
			</xsl:otherwise>
		</xsl:choose>
		<!-- /div-->

		<br />
		<!-- /div-->
	</xsl:template>

	<!--  IMMAGINE (PIPELINE INSERIMENTO IMMAGINE CHIAMATA CON CINCLUDE) -->

	<xsl:template match="image">
		<xsl:variable name="url">
			<xsl:value-of select="text()" />
		</xsl:variable>
		<!-- div class="sezione_immagine"><img src="{$url}" width="70" height="70"/></div-->
		<c:include src="cocoon://imageeditcomponent?id=prevImage&amp;currentimage={$url}&amp;previewwidth=70&amp;previewheight=70&amp;minwidth=70&amp;minheight=70&amp;maxwidth=150&amp;maxheight=150" />
		<!-- br />
		<br />
		<input width="20" type="button" value="Inserisci Nuova" onClick="location.href='upload/pid{$pid}'"/-->
		<br/>Image link:<br/>
		<input name="imglink" type="text" size="6" value="{@link}"/>
	</xsl:template>


	<xsl:template match="testo">
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
		theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,|,insertdate,inserttime,preview,|,forecolor,backcolor", // code,
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
		<textarea id="elm2" name="text" rows="10" class="edittextarea" tinyMCE_this="true">
			<!--  xsl:value-of select="text()" /-->
			<!-- xsl:copy-->
				<xsl:apply-templates select="@*|node()|text()|*" />
			<!--  /xsl:copy-->
		</textarea>
		<br />
	</xsl:template>
	
	<xsl:template name="news">
		<xsl:if test="$extra = 'news'">
			<b>Data inizio:</b>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
			<b>Data fine:</b>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<b>Nelle liste</b>
			<br/>

			<input type="text" size="8" name="startdate" value="{sql:rowset/sql:row/sql:startdate}" id="startdate" readonly="1" />
			<img src="images/img.gif" id="f_trigger_c" style="cursor: pointer; border: 1px solid red;" title="Date selector"
      onmouseover="this.style.background='red';" onmouseout="this.style.background=''" />&#160;&#160;
	
			<input type="text" size="8" name="enddate" value="{sql:rowset/sql:row/sql:enddate}" id="enddate" readonly="1" />
			<img src="images/img.gif" id="f_trigger_d" style="cursor: pointer; border: 1px solid red;" title="Date selector"
	      onmouseover="this.style.background='red';" onmouseout="this.style.background=''" />&#160;&#160;
		
			

			<input type="checkbox" name="list">
				<xsl:choose>
					<xsl:when test="sql:rowset/sql:row/sql:list = 'true'">
						<xsl:attribute name="checked">checked</xsl:attribute>
					</xsl:when>
					<xsl:otherwise></xsl:otherwise>
				</xsl:choose>
			</input>
			

			<br/>
			
			<script type="text/javascript">
			<![CDATA[
			    function catcalc(cal) {
			        var date = cal.date;
			        var time = date.getTime();
			        var modifico=cal.params.inputField;
			        
			        // use the _other_ field
			        var field = document.getElementById("startdate");
			        if (field == cal.params.inputField) {
			            field = document.getElementById("enddate");
			        }

			        // se è vuoto metti la data uguale
			        if(Number(field.value)==0) {
			            	field.value=date.print("%Y%m%d");
			        }
			        
			        var data=new Date();
			        data.setFullYear(Number(field.value.substr(0,4))); // substr inizia a contare da 0
			        data.setMonth(Number(field.value.substr(4,2))-1);  // i mesi vanno da 0 a 11
			        data.setDate(Number(field.value.substr(6,2)));
			        
			        if((modifico==document.getElementById("startdate") && time > data.getTime()) || 
			        		(modifico==document.getElementById("enddate") && data.getTime() > time)) {
			        	alert("La data di inizio deve essere antecedente o uguale alla data di fine avviso!");
			        	field.value=date.print("%Y%m%d");
			        }
			        
			        if(Math.abs(time-data.getTime())>2592000000) {
			        	alert("Un avviso no può essere valido per piu' di 30 giorni!")
			        	field.value=date.print("%Y%m%d");
			        }
			        
			        
			    }
			    Calendar.setup({
			        inputField     :    "startdate",     // id of the input field
			        ifFormat       :    "%Y%m%d",      // format of the input field
			        button         :    "f_trigger_c",  // trigger for the calendar (button ID)
			        align          :    "Br",           // alignment (defaults to "Bl")
			        singleClick    :    true,
			        onUpdate       :    catcalc
			    });
			    Calendar.setup({
			        inputField     :    "enddate",     // id of the input field
			        ifFormat       :    "%Y%m%d",      // format of the input field
			        button         :    "f_trigger_d",  // trigger for the calendar (button ID)
			        align          :    "Br",           // alignment (defaults to "Bl")
			        singleClick    :    true,
			        onUpdate       :    catcalc
			    });
			   ]]>
			</script>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>