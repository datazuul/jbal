<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                              
	<xsl:param name="pid" />
	<xsl:param name="lang" />
	
	<xsl:template match="/">
		<xsl:apply-templates select="lingue"/>
	</xsl:template>
	
	<xsl:template match="lingue">
		<div id="lingue">
			<xsl:apply-templates />
		</div>
	</xsl:template>
		
	
	<xsl:template match="item">
		<xsl:variable name="langcode"><xsl:value-of select="code/text()" /></xsl:variable>
		<xsl:if test="$lang != $langcode"> 
			<a class="lingue1lev" href="pageview?pid={$pid}&amp;lang={$langcode}">
				<img src="{image/text()}" alt="{text/text()}" />
			</a>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>