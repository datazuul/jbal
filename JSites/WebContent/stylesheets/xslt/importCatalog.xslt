<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	


	<xsl:template match="root">
		<html>
			<head>
			</head>
			<body>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="form">
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
	</xsl:template>
	
	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>