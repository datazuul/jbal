<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0">

	<xsl:param name="datadir" />
	
	<xsl:template match="/save">
		<source:write>
	    <source:source><xsl:value-of select="$datadir"/>/data/publicationPairs<xsl:value-of select="cid/text()"/>.xml
	    </source:source>
	    <source:fragment>
			<publicationPairs>
				<titolo><xsl:value-of select="titolo"/></titolo>
				<list><xsl:value-of select="list"/></list>
			</publicationPairs>		
	    </source:fragment>
		</source:write>
	</xsl:template>

	
	<xsl:template match="text()"/>

	<xsl:template match="@*|node()|text()">
		<!-- xsl:copy-->
			<xsl:apply-templates select="@*|node()|text()|*" />
		<!-- /xsl:copy-->
	</xsl:template>

</xsl:stylesheet>