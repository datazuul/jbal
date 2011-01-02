<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0">

<xsl:param name="datadir"/>

<xsl:template match="/save">
	<source:write>
   	    <source:source><xsl:value-of select="$datadir" />/data/footerContent<xsl:value-of select="cid/text()"/>.xml
    	</source:source>
	    <source:fragment>
			<footerContent>
				<xsl:apply-templates/>
			</footerContent>
	    </source:fragment>
	</source:write>
</xsl:template>

<xsl:template match="text">
	<testo>
		<xsl:value-of select="text()"/>
	</testo>
</xsl:template>


<xsl:template match="text()"/>

</xsl:stylesheet>