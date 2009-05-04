<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0">
								

	
	<xsl:param name="datadir"/>
	
	<xsl:template match="catalogSearch">
		<source:write>
	    <source:source><xsl:value-of select="$datadir" /></source:source>
	    <source:fragment>
			
		<xsl:copy-of select="." />
			
	    </source:fragment>
		</source:write>
		
	</xsl:template>
	

	<!-- 
	<xsl:template match="@*|node()|text()">
			<xsl:apply-templates select="@*|node()|text()|*" />
	</xsl:template>
	 -->
</xsl:stylesheet>