<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                              

	
	<!-- xsl:template match="/">
		<div class="footerContent">
			<xsl:apply-templates select="footerContent"/>
		</div>
	</xsl:template-->
		
	
	<xsl:template match="footerContent">
			<xsl:apply-templates/>	
	</xsl:template>
	
	<xsl:template match="rightImg">
		<!-- div id="logotipo_a">
			<img>
				<xsl:attribute name="src"><xsl:value-of select="."/></xsl:attribute>
			</img>
		</div-->
	</xsl:template>
	
	<xsl:template match="leftImg">
		<div id="logotipo_b">
			<img>
				<xsl:attribute name="src"><xsl:value-of select="."/></xsl:attribute>
				<xsl:attribute name="alt"><xsl:value-of select="@alt"/></xsl:attribute>
			</img>
		</div>
	</xsl:template>
	
	<xsl:template match="text">
		<div id="testo">
			<xsl:value-of select="."/>
		</div>
	</xsl:template>
	
	<xsl:template match="link">
		<div id="indirizzo">
			<a>
				<xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
				<xsl:value-of select="."/>
			</a>
		</div>
	</xsl:template>
	    
</xsl:stylesheet>