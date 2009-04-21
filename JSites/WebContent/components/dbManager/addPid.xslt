<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:param name="pid" />

	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="@*|node()|text()">
		<xsl:copy><xsl:apply-templates select="@*|node()|text()|*" /></xsl:copy>
	</xsl:template>

	<xsl:template match="pid"><xsl:value-of select="$pid" /></xsl:template>

</xsl:stylesheet>