<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
             
    <xsl:param name="permissionCode"/>          
	
	<xsl:template match="banner">
		
		<div id="home_sidebar">
			<xsl:apply-templates select="img"/>
			<div id="home_testo">
				<xsl:apply-templates select="titolo"/>
				<p><xsl:apply-templates select="link|testo"/></p>
			</div>
			<xsl:if test="$permissionCode = 7">
				<add/>
			</xsl:if>
		</div>
	</xsl:template>
	
	
	<xsl:template match="img">	
		<div id="home_immagine">
			<img src="{text()}" alt="{@alt}">
				<!-- xsl:attribute name="src"><xsl:value-of select="text()"/></xsl:attribute-->
			</img>
		</div>
	</xsl:template>
		
	
	<xsl:template match="link">
		<a>
			<xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
			<xsl:value-of select="text()"/>
		</a>
		<br/>
	</xsl:template>
	
	<xsl:template match="testo">
		<xsl:value-of select="text()"/><br></br>
	</xsl:template>
	
	<xsl:template match="titolo">
		<h1><xsl:value-of select="text()"/></h1>
	</xsl:template>

</xsl:stylesheet>