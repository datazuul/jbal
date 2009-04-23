<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0">

	<xsl:param name="datadir" />
	
	<xsl:template match="/save">
		<source:write>
	    <source:source><xsl:value-of select="$datadir"/>/data/box<xsl:value-of select="cid/text()"/>.xml
	    </source:source>
	    <source:fragment>
			<box>
				<unit1>
					<xsl:apply-templates select="prevImage1"/>
					<xsl:apply-templates select="title1"/>
					<xsl:apply-templates select="text1"/>
				</unit1>
				<unit2>
					<xsl:apply-templates select="prevImage2"/>
					<xsl:apply-templates select="title2"/>
					<xsl:apply-templates select="text2"/>
				</unit2>
			</box>
	    </source:fragment>
		</source:write>
	</xsl:template>
	
	<xsl:template match="unit1">
		<unit1>
			<xsl:value-of select="text()"/>
		</unit1>
	</xsl:template>
	
		<xsl:template match="unit2">
		<unit2>
			<xsl:value-of select="text()"/>
		</unit2>
	</xsl:template>
	
	<xsl:template match="text1">
		<testo1>
			<xsl:value-of select="text()"/>
		</testo1>
	</xsl:template>
	
	<xsl:template match="text2">
		<testo2>
			<xsl:value-of select="text()"/>
		</testo2>
	</xsl:template>
	
	<xsl:template match="title1">
		<titolo1>
			<xsl:attribute name="type">
				<xsl:value-of select="@type"/>
			</xsl:attribute>
			<xsl:value-of select="text()"/>
		</titolo1>
	</xsl:template>
	
	<xsl:template match="title2">
		<titolo2>
			<xsl:attribute name="type">
				<xsl:value-of select="@type"/>
			</xsl:attribute>
			<xsl:value-of select="text()"/>
		</titolo2>
	</xsl:template>
	
	<xsl:template match="prevImage1">
		<img1>
			<xsl:value-of select="text()"/>
		</img1>
	</xsl:template>
	
	<xsl:template match="prevImage2">
		<img2>
			<xsl:value-of select="text()"/>
		</img2>
	</xsl:template>
	
	<xsl:template match="text()"/>

	<xsl:template match="@*|node()|text()">
		<!-- xsl:copy-->
			<xsl:apply-templates select="@*|node()|text()|*" />
		<!-- /xsl:copy-->
	</xsl:template>

</xsl:stylesheet>