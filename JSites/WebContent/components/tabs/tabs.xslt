<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                              
	<xsl:param name="pid" />
	<xsl:param name="lang" />
	
	<xsl:template match="/">
		<xsl:apply-templates select="tabs"/>
	</xsl:template>
	
	<xsl:template match="tabs">
		<div id="mainbar">
			<xsl:apply-templates />
		</div>
	</xsl:template>
		
	
	<xsl:template match="item">
		<a class="mainbar1lev" href="pageview?pid={pid/text()}&amp;lang={$lang}">
			<xsl:value-of select="text/text()" />
		</a>
	</xsl:template>

</xsl:stylesheet>