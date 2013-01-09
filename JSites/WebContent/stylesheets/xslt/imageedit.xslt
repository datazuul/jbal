<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:dir="http://apache.org/cocoon/directory/2.0"
	>

	<xsl:param name="currentimage" />
	<xsl:param name="previewwidth" />
	<xsl:param name="previewheight" />
	<xsl:param name="id" />
	
	<xsl:template match="/dir:directory">
  		<img id="{$id}" src="{$currentimage}" /> <!--   width="{$previewwidth}" height="{$previewheight}" -->
  		<br />
  		<select name="{$id}" onChange="document.getElementById('{$id}').src = this.value;">
  			<xsl:call-template name="imagelist">
  				<xsl:with-param name = "selected" ><xsl:value-of select="$currentimage"/></xsl:with-param>
  			</xsl:call-template>
  		</select>
	 </xsl:template>
	 
	 <xsl:template name="imagelist">
		<xsl:param name = "selected" />
		<option name="no_image" >				
		<xsl:if test="'images/contentimg/no_image' = $selected"><xsl:attribute name = "selected" /></xsl:if>
		no_image
		</option>
		<xsl:for-each select="/dir:directory/dir:file">
			<xsl:variable name="data"><xsl:value-of select="@name"/></xsl:variable>
			<xsl:element name = "option">
				<xsl:attribute name = "value" ><xsl:value-of select="concat('images/contentimg/',$data)"/></xsl:attribute>
				<xsl:if test="concat('images/contentimg/',$data) = $selected"><xsl:attribute name = "selected" /></xsl:if>
				<xsl:value-of select="$data"/>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>
  		
</xsl:stylesheet>