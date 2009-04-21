<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                              
	<xsl:template match="/">
		<xsl:apply-templates /> <!-- select="navigator"/-->
	</xsl:template>
	
	<xsl:template match="navigator">
		<!-- div id="{$classlevel0}"-->
			<xsl:apply-templates /> <!-- select="voce" / -->
		<!--  /div>-->
			
	</xsl:template>
	
	<xsl:template match="voce">
		<xsl:variable name="classlevel0"><xsl:value-of select="/navigator/id/text()"/></xsl:variable>
		<xsl:variable name="level"><xsl:value-of select="count(ancestor-or-self::*) - 1"/></xsl:variable>
		
		<xsl:element name="a">
			
			<!--  PRIMA IMMAGINE -->
			<xsl:if test="@img1 != ''">
				<xsl:element name="img">
					<xsl:attribute name="src"><xsl:value-of select="@img1"/></xsl:attribute>
					<xsl:attribute name="alt" />
					<xsl:attribute name="class"><xsl:value-of select="concat($classlevel0,'img1')"/></xsl:attribute>
				</xsl:element>
			</xsl:if>
			
			<xsl:attribute name="href"><xsl:value-of select="@link"/></xsl:attribute>
			<xsl:choose>
				<xsl:when test="@active = 'true'">
					<xsl:attribute name="class"><xsl:value-of select="concat($classlevel0,'hover',$level,'lev')"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class"><xsl:value-of select="concat($classlevel0,$level,'lev')"/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="@text"/>

			<!-- SECONDA IMMAGINE (DOPO IL TESTO) -->
			<xsl:choose>
				<xsl:when test="@img2 = 'closed'">
					<img src="images/freccia_dx.gif" alt="toggle" class="imgright" width="7" height="9"/>
				</xsl:when>
				<xsl:when test="@img2 = 'opened'">
					<img src="images/freccia_down.gif" alt="toggle" class="imgright" width="7" height="9"/>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
				
		</xsl:element>
		
		
		
		<xsl:apply-templates select="voce"/>
	</xsl:template>
	
	<xsl:template match="area">
		<div id="etichetta">
			<a href="{a/@href}"><xsl:value-of select="."/></a>
		</div>
	</xsl:template>
	
	<xsl:template match="id"></xsl:template>

</xsl:stylesheet>