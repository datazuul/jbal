<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0">
								
	<xsl:variable name="list">
		<xsl:choose>
			<xsl:when test="string-length(save/list) &gt; 0">1</xsl:when>
			<xsl:otherwise>0</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:param name="datadir"/>
	
	<xsl:template match="/save">
		<source:write>
	    <source:source><xsl:value-of select="$datadir" />/data/gallery<xsl:value-of select="cid/text()"/>.xml
	    </source:source>
	    <source:fragment>
			<gallery>
				<xsl:apply-templates/>
			</gallery>
	    </source:fragment>
		</source:write>
	</xsl:template>
	
	<xsl:template match="dir">
		<dir>
			<xsl:value-of select="text()"/>
		</dir>
	</xsl:template>
	
	<xsl:template match="title">
		<titolo>
			<xsl:attribute name="type">
				<xsl:value-of select="@type"/>
			</xsl:attribute>
			<xsl:value-of select="text()"/>
		</titolo>
	</xsl:template>
	
	
	<xsl:template match="text()"/>

	<xsl:template match="@*|node()|text()">
		<!-- xsl:copy-->
			<xsl:apply-templates select="@*|node()|text()|*" />
		<!-- /xsl:copy-->
	</xsl:template>

</xsl:stylesheet>