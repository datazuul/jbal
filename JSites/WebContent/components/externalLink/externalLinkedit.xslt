<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:include href="../../stylesheets/xslt/helpedit.xslt" />
    <!-- Questo include serve per la cornice di help  -->
    
	
	<xsl:param name="cid" />
	<xsl:param name="time" />
	<xsl:param name="pid" />

	<xsl:template match="/">

		<xsl:apply-templates select="externalLink" />
		
		<xsl:call-template name="callhelp" />
		
	</xsl:template>

	<!--  SEZIONE -->

	<xsl:template match="externalLink">
		<div class="{$time}">
			<div class="sezione">
				<div class="sezione_immagine">
					<xsl:call-template name="img" />
				</div>
				<div class="sezione_contenuto">
					<xsl:apply-templates select="titolo" />
					<xsl:apply-templates select="url" />
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
			<input type="hidden" name="title_type" value="1" />
		<!-- /div-->
	
		<br />
		<!-- /div-->
	</xsl:template>

	<!--  IMMAGINE (PIPELINE INSERIMENTO IMMAGINE CHIAMATA CON CINCLUDE) -->

	<xsl:template name="img">
		<div class="sezione_immagine">
		<!-- img height="70" width="70" id="prevImage"/><br/-->
		</div>
	</xsl:template>

	<!--  TESTO -->

	<xsl:template match="url">
		<b>URL:</b>
		<br />
		<input type="text" size="40" name="url" value="{text()}" />
		<!-- input type="hidden" name="title_type" value="{@type}" /-->
		<!-- /div-->

		<br />
		<!-- /div-->
	</xsl:template>

</xsl:stylesheet>