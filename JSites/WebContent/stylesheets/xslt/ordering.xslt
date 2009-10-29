<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
						xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
						xmlns="http://www.w3.org/1999/xhtml">
	<xsl:param name="cid" />
	
	<xsl:template match="root">
		<xsl:apply-templates />
	</xsl:template>
	
	<!--  COMBOBOX DI ORDINAMENTO -->
	<xsl:template match="order">
	
		<div class="modifica">
			<select class="buttonright" name="cid{$cid}">
				<xsl:call-template name="iterate">
					<xsl:with-param name = "iterateleft" ><xsl:value-of select="elementnumber/text()"/></xsl:with-param>
				</xsl:call-template>
			</select>
		</div>
		<div class="clearer">&#160;</div>
		<hr color="#8BB6D8"/>
	</xsl:template>
	
	<!--  PIPELINE DI ITERAMENTO ELEMENTI DI ORDINAMENTO -->					
	<xsl:template name="iterate">
		<xsl:param name = "iterateleft" />
		<xsl:variable name="order"><xsl:value-of select="selected/text()"/></xsl:variable>
		<xsl:variable name="top"><xsl:value-of select="elementnumber/text()"/></xsl:variable>
			
		<xsl:element name="option"> <!--  onChange="verifyChoice()" -->
			<xsl:attribute name="onChange">verifyChoice()</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="$top - $iterateleft + 1" /></xsl:attribute>
			<xsl:if test="$top - $iterateleft + 1 = $order">
				<xsl:attribute name="selected" />
			</xsl:if>
			<xsl:value-of select="$top - $iterateleft + 1" />
		</xsl:element>
			
		<xsl:if test="number($iterateleft) > 1">
			<xsl:call-template name="iterate">
				<xsl:with-param name = "iterateleft" ><xsl:value-of select="$iterateleft - 1"/></xsl:with-param>
			</xsl:call-template>
		</xsl:if>	
	</xsl:template>
	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>