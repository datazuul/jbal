<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0">
								

	
	<xsl:param name="datadir"/>
	
	<xsl:template match="/save">
		<source:write>
	    <source:source><xsl:value-of select="$datadir" />/data/catalogSearch<xsl:value-of select="cid/text()"/>.xml
	    </source:source>
	    <source:fragment>
			<catalogSearch>
				<!-- links deve essere il primo! -->
				<xsl:apply-templates select="links" />
				<xsl:apply-templates select="catalogName" />
				<xsl:apply-templates select="catalogConnection" />
			</catalogSearch>
	    </source:fragment>
		</source:write>
		
	</xsl:template>
	
	<xsl:template match="catalogName">
		<catalogName>
			<xsl:value-of select="text()"/>
		</catalogName>
	</xsl:template>
	
	<xsl:template match="links">
		<links>
			<xsl:value-of select="text()"/>
		</links>
	</xsl:template>
	
	<xsl:template match="catalogConnection">
		<catalogConnection>
			<xsl:value-of select="text()"/>
		</catalogConnection>
	</xsl:template>
	
	<xsl:template match="@*|node()|text()">
			<xsl:apply-templates select="@*|node()|text()|*" />
	</xsl:template>

</xsl:stylesheet>