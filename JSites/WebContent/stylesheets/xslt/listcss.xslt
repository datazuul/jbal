<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
							xmlns:dir="http://apache.org/cocoon/directory/2.0">
	
	<xsl:template match="/">

		<styles>
			<xsl:apply-templates/>
		</styles>
	</xsl:template>
	
	<xsl:template match="dir:file">
		<css>
			<xsl:text>css/</xsl:text><xsl:value-of select="@name"/>
		</css>
	</xsl:template>
	
	<xsl:template match="redirect">
		<redirect/>
	</xsl:template>
				
</xsl:stylesheet>