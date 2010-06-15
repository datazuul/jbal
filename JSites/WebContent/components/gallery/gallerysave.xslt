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
		<xsl:variable name="ddir">
			<xsl:call-template name="basepath"><xsl:with-param name="path" select="text()" /></xsl:call-template>
		</xsl:variable>
		<dir>
			<xsl:value-of select="$ddir"/>
		</dir>
	</xsl:template>
	
	<xsl:template name="basepath">
      <xsl:param name="path" />
      <xsl:if test="string-length(normalize-space($path))!=0">
   		<xsl:if test="contains($path,'/')">
  			<xsl:value-of select="substring-before($path,'/')" />/<xsl:call-template name="basepath"><xsl:with-param name="path" select="substring-after($path,'/')" /></xsl:call-template>
   		</xsl:if>
      </xsl:if>
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