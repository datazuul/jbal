<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:h="http://apache.org/cocoon/request/2.0"
								xmlns:source="http://apache.org/cocoon/source/1.0">
<xsl:param name="datadir"/>
<xsl:template match="/save">
	<source:write>
    <source:source><xsl:value-of select="$datadir" />/data/map<xsl:value-of select="cid/text()"/>.xml
    </source:source>
    <source:fragment>
		<map>
			<xsl:apply-templates select="title" />
			<xsl:apply-templates select="description" />
			<xsl:apply-templates select="imgmap" />
			<services>
				<xsl:call-template name="entry">
					<xsl:with-param name="current">1</xsl:with-param>
					<xsl:with-param name="count"><xsl:value-of select="elements/text()" /></xsl:with-param>
				</xsl:call-template>
			</services>
		</map>
    </source:fragment>
	</source:write>
</xsl:template>

	<xsl:template match="title">
		<title><xsl:value-of select="text()" /></title>
	</xsl:template>
	
	<xsl:template match="description">
		<description><xsl:value-of select="text()" /></description>
	</xsl:template>
	
	<xsl:template match="imgmap">
		<image><xsl:value-of select="text()" /></image>
	</xsl:template>
	
	<xsl:template name="entry">
		<xsl:param name="current" />
		<xsl:param name="count" />
		<xsl:variable name="text"><xsl:value-of select="concat('text',$current)" /></xsl:variable>
		<entry>
			<text>
				<xsl:value-of select="*[local-name() = concat('text',$current)]/text()" />
			</text>
			<image>
				<xsl:value-of select="*[local-name() = concat('imgserv',$current)]/text()" />
			</image>
		</entry>
		<xsl:if test="$current &lt; $count">
			<xsl:call-template name="entry">
				<xsl:with-param name="current"><xsl:value-of select="$current + 1" /></xsl:with-param>
				<xsl:with-param name="count"><xsl:value-of select="$count" /></xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>