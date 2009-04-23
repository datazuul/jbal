<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:sql="http://apache.org/cocoon/SQL/2.0"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:include href="../../stylesheets/xslt/helpedit.xslt" />
    <!-- Questo include serve per la cornice di help  -->
	
	<xsl:param name="cid" />
	<xsl:param name="time" />
	<xsl:param name="pid" />
	<xsl:param name="extra" />

	<xsl:template match="/">
		<xsl:apply-templates select="gallery" />
		
		<xsl:call-template name="callhelp" />
		
	</xsl:template>

	<!--  SEZIONE -->

	<xsl:template match="gallery">
		<div class="{$time}">
			<div class="sezione">
				<div class="sezione_contenuto">
					<xsl:apply-templates select="titolo" />
					<xsl:apply-templates select="dir" />
				</div>
			</div>		
		</div>
	</xsl:template>

	<!--  TITOLO -->

	<xsl:template match="titolo">
		<!--div-->

		<!-- div id="sezione_titolo"-->
		<b>Titolo:</b>
		<br />
		<input type="text" size="40" name="title" value="{text()}" />
		<xsl:choose>
			<xsl:when test="@type != ''">
				<input type="text" size="1" name="title_type" value="{@type}" />
			</xsl:when>
			<xsl:otherwise>
				<input type="text" size="1" name="title_type" value="2" />
			</xsl:otherwise>
		</xsl:choose>
		<!-- /div-->

		<br />
		<!-- /div-->
	</xsl:template>


	<!--  Dir -->

	<xsl:template match="dir">
		<b>Directory (/images/...):</b>
		<br/>
		<input type="text" size="40" name="dir" value="{text()}" />
		<br />
	</xsl:template>
	
</xsl:stylesheet>