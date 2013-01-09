<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                              xmlns:cinclude="http://apache.org/cocoon/include/1.0">
	
	<xsl:param name="pid" />
	
	<xsl:template match="/">
		<div>
			<xsl:apply-templates/>
		</div>
	</xsl:template>
	
	<xsl:template match="//sourceResult/execution/text()">
		<strong><xsl:copy/></strong>
		<br />
		<input type="button" value="Aggiorna" onClick="location.href='pageview?pid={$pid}'" />
	</xsl:template>
	
	<xsl:template match="text()"/>
	
</xsl:stylesheet>