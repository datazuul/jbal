<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	


	<xsl:template match="root">
				<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="load">
		<html>
			<head>
				<script type="text/javascript">
<![CDATA[

// Customise those settings

var seconds = 10;
var divid = "log";
var url = "status]]><xsl:value-of select="cid" /><![CDATA[";

////////////////////////////////
//
// Refreshing the DIV
//
////////////////////////////////

function refreshdiv(){

// The XMLHttpRequest object

var xmlHttp;
try{
xmlHttp=new XMLHttpRequest(); // Firefox, Opera 8.0+, Safari
}
catch (e){
try{
xmlHttp=new ActiveXObject("Msxml2.XMLHTTP"); // Internet Explorer
}
catch (e){
try{
xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
}
catch (e){
alert("Your browser does not support AJAX.");
return false;
}
}
}

// Timestamp for preventing IE caching the GET request

fetch_unix_timestamp = function()
{
return parseInt(new Date().getTime().toString().substring(0, 10))
}

function scrollToBottom(){
var scrollH=document.body.scrollHeight;
var offsetH=document.body.offsetHeight;
//if(scrollH&amp;gt;offsetH) 
window.scrollTo(0,scrollH);
//else window.scrollTo(0,offsetH);
}

var timestamp = fetch_unix_timestamp();
var nocacheurl = url+"?t="+timestamp;

// The code...

xmlHttp.onreadystatechange=function(){
if(xmlHttp.readyState==4){
document.getElementById(divid).innerHTML=xmlHttp.responseText;
scrollToBottom();
setTimeout('refreshdiv()',seconds*1000);
}
}
xmlHttp.open("GET",nocacheurl,true);
xmlHttp.send(null);
}

// Start the refreshing process

window.onload = function startrefresh(){
setTimeout('refreshdiv()',seconds*1000);
}
]]>
				</script>
			</head>
			<body>
				Info:<br/>
				Status: <xsl:value-of select="status" /><br/>
				Dir: <xsl:value-of select="dir" /><br/>
		        Filename: <xsl:value-of select="filename" /><br/>
		        MimeType: <xsl:value-of select="mimetype" /><br/>
		        Uploadname: <xsl:value-of select="uploadname" /><br/>
		        Size: <xsl:value-of select="size" /><br/>
		        CID: <xsl:value-of select="cid" /><br/>
	        	PID: <xsl:value-of select="pid" /><br/>
	        	Connection: <xsl:value-of select="conn" /><br/>
	        	Format: <xsl:value-of select="format" /><br/>
	        	DbType: <xsl:value-of select="dbtype" />
	        	<hr/>
	        	<script type="text/javascript">
					refreshdiv();
				</script>
	        	<pre id="log"></pre>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="form">
		<html>
			<head>
			</head>
			<body>
				<b>Importa catalogo</b>
				<hr/>
				
				<script type="text/javascript">
					<![CDATA[
					function saveCatalogFormat()
					{
						opener.document.forms["editform"].catalogFormat.value=document.forms["upfrm"].format.value;
					}		
		
					]]>
				</script>
		
				<form id="upfrm" enctype="multipart/form-data" action="importCatalog?pid={pid}&amp;cid={cid}&amp;dbtype={dbtype}&amp;conn={conn}" 
					method="post" onSubmit="saveCatalogFormat();">
					<!-- ?pid={pid}&amp;cid={cid}  -->
					<fieldset>
						<legend>File da importare</legend>
						<table border="0">
							<tr>
								<td><label>File:</label></td><td><input type="file" name="upload-file" /></td>
							</tr>
							<tr>
								<td><label>Formato dati:</label></td><td><input type="text" name="format" id="format" /></td>
							</tr>
						</table>
					</fieldset>
					<input value="Upload" name="action" type="submit" />
					<!-- con, format, dbtype -->
				</form>
				
				Db-type: <b><xsl:value-of select="dbtype" /></b><br/>
				Nome db: <b><xsl:value-of select="conn" /></b><br/>
			</body>
		</html>
	</xsl:template>
	
	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>