<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
							  xmlns:c="http://apache.org/cocoon/include/1.0">
  
  	<xsl:param name="cid"/>
	<xsl:param name="pid"/>
	<xsl:param name="pacid"/>
	<xsl:param name="type"/>
	<xsl:param name="redim"/>
	<xsl:param name="elements"/>
	<xsl:param name="time"/>
	
	<xsl:template match="/">
		<xsl:choose>
			<xsl:when test="$redim = 'true'">
				<div class="mappa">
					<input type="hidden" name="elements" value="{$elements}" />
					<xsl:apply-templates select="map">
						<xsl:with-param name="elnum"><xsl:value-of select="$elements" /></xsl:with-param>
					</xsl:apply-templates>
				</div>		
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="elnum"><xsl:value-of select="count(/map/services/entry)"/></xsl:variable>
				<div class="mappa">
					<input type="hidden" name="elements" value="{$elnum}" />
					<xsl:apply-templates select="map">
						<xsl:with-param name="elnum"><xsl:value-of select="$elnum" /></xsl:with-param>
					</xsl:apply-templates>
				</div>		
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="map">
		<xsl:param name="elnum" />
		<div class="{$time}">
			<!--  TITOLO -->
			<xsl:variable name="title"><xsl:value-of select="title/text()"/></xsl:variable>
			<div class="mappa_titolo"><input type="text" name="title" value="{$title}">Titolo Mappa</input></div>
			
			<!--  SERVIZI -->
			<div class="servizi">
				<xsl:if test="$elnum &gt; 0">
					<xsl:call-template name="services">
						<xsl:with-param name="current">1</xsl:with-param>
						<xsl:with-param name="total"><xsl:value-of select="$elnum" /></xsl:with-param>
					</xsl:call-template>
				</xsl:if>
				<input type="button" value="Aggiungi" onClick="location.href='pageedit?pid={$pid}&amp;cid={$cid}&amp;pacid={$pacid}&amp;type=map&amp;redim=true&amp;elements={$elnum + 1}'" />
				<xsl:if test="$elnum &gt; 0">
					<input type="button" value="Rimuovi" onClick="location.href='pageedit?pid={$pid}&amp;cid={$cid}&amp;pacid={$pacid}&amp;type=map&amp;redim=true&amp;elements={$elnum - 1}'" />
				</xsl:if>
			</div>
			
			<!--  PIANTINA -->
			<xsl:variable name="map"><xsl:value-of select="image/text()"/></xsl:variable>
			<div class="mappa_immagine">
				<c:include  src="cocoon://imageeditcomponent?id=imgmap&amp;previewwidth=300&amp;previewheight=300&amp;currentimage={$map}&amp;minwidth=250&amp;minheight=250&amp;maxwidth=350&amp;maxheight=350" />
			</div>
			
			<!-- DESCRIZIONE -->
			<xsl:variable name="description"><xsl:value-of select="description/text()"/></xsl:variable>
			<div class="mappa_descrizione"><input type="text" name="description" value="{$description}">Descrizione</input></div>
		</div>
	</xsl:template>
	
	<!-- PIPELINE ITERATIVA SERVIZI -->
	<xsl:template name="services">
		<xsl:param name="current" />
		<xsl:param name="total" />
		<div class="servizi_voci">
			<xsl:variable name="imageurl"><xsl:value-of select="services/entry[number($current)]/image/text()"/></xsl:variable>
			<xsl:variable name="text"><xsl:value-of select="services/entry[number($current)]/text/text()"/></xsl:variable>
			<xsl:variable name="servid"><xsl:value-of select="concat('imgserv',$current)"/></xsl:variable>
			<c:include  src="cocoon://imageeditcomponent?id={$servid}&amp;previewwidth=26&amp;previewheight=21&amp;currentimage={$imageurl}&amp;minwidth=20&amp;minheight=20&amp;maxwidth=30&amp;maxheight=30" />
			<input type="text" name="text{$current}" value="{$text}">Testo Servizio</input>	
		</div>
		<xsl:if test="$current &lt; $total">
			<xsl:call-template name="services">
				<xsl:with-param name="current"><xsl:value-of select="$current + 1" /></xsl:with-param>
				<xsl:with-param name="total"><xsl:value-of select="$total" /></xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
    
  </xsl:stylesheet>