<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0">
<xsl:param name="datadir" />
<xsl:template match="/save">
	<source:write>
    <source:source><xsl:value-of select="$datadir" />/data/childrenListing<xsl:value-of select="cid/text()"/>.xml</source:source>
    <source:fragment>
		<childrenListing>
			<xsl:apply-templates/>
		</childrenListing>
    </source:fragment>
	</source:write>
</xsl:template>

<xsl:template match="text">
	<testo>
		<xsl:value-of select="text()"/>
	</testo>
</xsl:template>

<xsl:template match="listParent">
	<listParent>
		<xsl:value-of select="text()"/>
	</listParent>
</xsl:template>

<xsl:template match="prevImage">
	<img>
		<xsl:value-of select="text()"/>
	</img>
</xsl:template>

<xsl:template match="text()"/>

</xsl:stylesheet>