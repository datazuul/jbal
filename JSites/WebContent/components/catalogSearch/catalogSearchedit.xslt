<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:include href="../../stylesheets/xslt/helpedit.xslt" />

	<xsl:param name="cid" />
	<xsl:param name="time" />
	<xsl:param name="pid" />

	<xsl:template match="/">
		<xsl:apply-templates select="catalogSearch" />
		<xsl:call-template name="callhelp" />
	</xsl:template>


	<xsl:template match="catalogSearch">
		<div class="{$time}">
			<div class="sezione">
				<xsl:apply-templates select="catalogName" />
				<xsl:apply-templates select="catalogConnection" />
				<xsl:apply-templates select="links" />
			</div>		
		</div>
	</xsl:template>
	

	<xsl:template match="catalogName">
	
		<b>Nome catalogo:</b>
		<br />
		<input type="text" size="40" name="catalogName" value="{text()}" />
		<br />

	</xsl:template>


	<xsl:template match="catalogConnection">
		<b>Nome connessione:</b>
		<br />
		<input type="text" size="40" name="catalogConnection" value="{text()}" />
		<br />
	</xsl:template>
	
	<xsl:template match="links">
		<b>Links:</b>
		<br />
		<textarea rows="15" cols="80" name="links">
			<xsl:value-of select="."/>
		</textarea>
		<!-- input type="text" size="40" name="catalogConnection" value="{text()}" /-->
		<br />
	</xsl:template>

</xsl:stylesheet>