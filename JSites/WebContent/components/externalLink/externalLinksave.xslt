<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:session="http://apache.org/cocoon/session/1.0"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	<xsl:param name="cid" />
	<xsl:param name="time" />
	<xsl:param name="pid" />

	<xsl:template match="/">
		<xsl:apply-templates select="externalLink" />
		<div class="clearer">&#160;</div>

	</xsl:template>

	<!--  SEZIONE -->

	<xsl:template match="externalLink">
		<div class="{$time}">
			<div class="sezione">
				<xsl:call-template name="img" />
				<div class="sezione_contenuto">
					<xsl:apply-templates select="titolo" />
					<br/><br/>
					<xsl:apply-templates select="url" />
				</div>
			</div>		
		</div>
	</xsl:template>

	<!--  TITOLO -->

	<xsl:template match="titolo">Titolo: <b><xsl:value-of select="."/></b></xsl:template>

	<!--  IMMAGINE (PIPELINE INSERIMENTO IMMAGINE CHIAMATA CON CINCLUDE) -->

	<xsl:template name="img">
		<div class="sezione_immagine">
		<!-- img height="70" width="70" id="prevImage"/><br/-->
		</div>
	</xsl:template>

	<!--  TESTO -->

	<xsl:template match="url">Link:   <xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>