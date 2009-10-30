<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0">


	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="testo">
		<testo>ATTENZIONE: State per uscire da questo sito Web.
		
		
			__Redirecting to__ [<xsl:value-of select="text()"/>&gt;__<xsl:value-of select="text()"/>__]
		</testo>
	</xsl:template>

	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>