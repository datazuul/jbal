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
		<xsl:apply-templates select="slideshow" />
		
		<xsl:call-template name="callhelp" />
		
	</xsl:template>

	<!--  SEZIONE -->

	<xsl:template match="slideshow">
		<div class="{$time}">
			<div class="sezione">
				<div class="sezione_contenuto">
					<xsl:apply-templates select="titolo" />
					<xsl:apply-templates select="dir" />
					
					<input type="hidden" id="rtop" name="top" value="{top}" />
					<input type="hidden" id="rleft" name="left" value="{left}" />
					
					<table border="0">
						<tr>
							<td>KenBurns enabled</td><td><input type="radio" name="effect" value="kenburns"><xsl:if test="contains(effect,'kenburns')"><xsl:attribute name="checked">true</xsl:attribute></xsl:if></input></td>
						</tr>
						<tr>
							<td>Fade enabled</td><td><input type="radio" name="effect" value="fade"><xsl:if test="contains(effect,'fade')"><xsl:attribute name="checked">true</xsl:attribute></xsl:if></input></td>
						</tr>
						<tr>
							<td>Push enabled</td><td><input type="radio" name="effect" value="push"><xsl:if test="contains(effect,'push')"><xsl:attribute name="checked">true</xsl:attribute></xsl:if></input></td>
						</tr>
						<tr>
							<td>Controller enabled</td><td><input type="checkbox" name="controller"><xsl:if test="contains(controller,'true')"><xsl:attribute name="checked">true</xsl:attribute></xsl:if></input></td>
						</tr>
						<tr>
							<td>Thumbnails enabled</td><td><input type="checkbox" name="thumbnails"><xsl:if test="contains(thumbnails,'true')"><xsl:attribute name="checked">true</xsl:attribute></xsl:if></input></td>
						</tr>
						<tr>
							<td>Captions enabled</td><td><input type="checkbox" name="captions"><xsl:if test="contains(captions,'true')"><xsl:attribute name="checked">true</xsl:attribute></xsl:if></input></td>
						</tr>
						<tr>
							<td>Duration</td><td><input type="text" name="duration" value="{duration}" /></td>
						</tr>
						<tr>
							<td>Width</td><td><input type="text" name="width" value="{width}" /></td>
						</tr>
						<tr>
							<td>Height</td><td><input type="text" name="height" value="{height}" /></td>
						</tr>
						<tr>
							<script type="text/javascript">
							function enabledisable(cb,el) {
								if(cb.checked) {el.disabled=false;}
								else {el.disabled=true;}
							}
							</script>
							<td>Absolute positioning</td>
							<td>
								<input type="checkbox" id="absolute" name="absolute" 
									onChange="enabledisable(this,document.getElementById('atoppos'));enabledisable(this,document.getElementById('aleftpos'))">
									<xsl:if test="contains(absolute,'true')"><xsl:attribute name="checked">true</xsl:attribute></xsl:if>
								</input>
							</td>
							
						</tr>
						<tr>
							<td>Top</td><td><input type="text" id="atoppos" value="{top}" onChange="document.getElementById('rtop').value=this.value"/></td>
						</tr>
						<tr>
							<td>Left</td><td><input type="text" id="aleftpos" value="{left}" onChange="document.getElementById('rleft').value=this.value" /></td>
						</tr>
					</table>
					<script type="text/javascript">
						enabledisable(document.getElementById('absolute'),document.getElementById('atoppos'));
						enabledisable(document.getElementById('absolute'),document.getElementById('aleftpos'));
						document.getElementById('atoppos').value=document.getElementById('rtop').value;
						document.getElementById('aleftpos').value=document.getElementById('rleft').value;
					</script>
					<hr/>
					<h3>Slideshow 2</h3>
					Slideshow effect provided by <a href="http://www.electricprism.com/aeron/slideshow/index.html">Slideshow 2</a> 
					with <a href="http://www.opensource.org/licenses/mit-license.php">MIT License</a>.<p/>
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


	<!--  Dir -->

	<xsl:template match="dir">
	<script type="text/javascript">
		<![CDATA[
		var f=function(){
			window.SetUrl=function(value){
				document.getElementById('dir').value=value;
			}
			var connector = "]]><xsl:value-of select="$context" /><![CDATA[/js/tiny_mce/filemanager/browser.html?Connector=]]><xsl:value-of select="$context" /><![CDATA[/fileManager&pid=]]><xsl:value-of select="$pid" /><![CDATA[";

	window.open(filemanager+'?Connector='+connector+'&Type=Image','fileupload','modal,width=600,height=400');
}
		
		
		
		
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
	
	
		<b>Directory (/images/...):</b>
		<br/>
		<input type="text" size="40" id="dir" name="dir" onClick="fileBrowserCallBack('dir','','file',window)" value="{text()}" />
		<br />
	</xsl:template>
	
</xsl:stylesheet>