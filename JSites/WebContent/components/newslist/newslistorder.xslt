<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
		
	<xsl:param name="cid" />
	<xsl:param name="order" />
	<xsl:param name="maxn" />
	<xsl:param name="time"/>
		
	<xsl:template match="/newslist">
		<newslist>
			<xsl:copy-of select="*" />
		</newslist>
		<order>
				<elementnumber><xsl:value-of select="$maxn"/></elementnumber>
				<selected><xsl:value-of select="$order"/></selected>
		</order>
	</xsl:template>
	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>
		
</xsl:stylesheet>