<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                              
	<xsl:param name="pid"/>

	
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="form">
	
		<form method="POST" enctype="multipart/form-data" action="upload{$pid}">
			CIAO!	
			<xsl:apply-templates/>
		</form>
	</xsl:template>
	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>
	
	
	    
</xsl:stylesheet>