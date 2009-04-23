<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:include href="../../stylesheets/xslt/helpedit.xslt" />
    <!-- Questo include serve per la cornice di help  -->
    
	
	<xsl:param name="cid" />
	<xsl:param name="time" />
	<xsl:param name="pid" />
	
	<xsl:variable name="count">0</xsl:variable>

    <xsl:template match="/">
	    <xsl:apply-templates select="box"/>
		
		<xsl:call-template name="callhelp" />
		
	</xsl:template>
	
	<xsl:template match="box">
		<div class="{$time}">
			<xsl:apply-templates select="unit1" />
			<xsl:apply-templates select="unit2" />
			<div class="clearer"/>
		</div>
	</xsl:template>

	<!--  SEZIONE -->

	<xsl:template match="unit1">
		<div class="box">
			<div class="box_immagine">
				<xsl:apply-templates select="img1" />
			</div>
			<div class="box_contenuto">
				<xsl:apply-templates select="titolo1" />
				<xsl:apply-templates select="testo1" />
			</div>
		</div>		
	</xsl:template>
	
	<xsl:template match="unit2">
		<div class="box">
			<div class="box_immagine">
				<xsl:apply-templates select="img2" />
			</div>
			<div class="box_contenuto">
				<xsl:apply-templates select="titolo2" />
				<xsl:apply-templates select="testo2" />
			</div>
		</div>		
	</xsl:template>

	<!--  TITOLO -->

	<xsl:template match="titolo1">
		
		<b>Titolo:</b>
		<br />
		<input type="text" size="40" name="title1" value="{text()}" class="box_title"/>
		<input type="hidden" name="title_type1" value="{@type}" />
		<br />
	</xsl:template>
	
	<xsl:template match="titolo2">
		
		<b>Titolo:</b>
		<br />
		<input type="text" size="40" name="title2" value="{text()}" class="box_title"/>
		<input type="hidden" name="title_type2" value="{@type}" />
		<br />
	</xsl:template>

	<!--  IMMAGINE (PIPELINE INSERIMENTO IMMAGINE CHIAMATA CON CINCLUDE) -->

	<xsl:template match="img1">
		<xsl:variable name="url"><xsl:value-of select="text()" /></xsl:variable>
		<c:include src="cocoon://imageeditcomponent?id=prevImage1&amp;currentimage={$url}&amp;previewwidth=104&amp;previewheight=140&amp;minwidth=10&amp;minheight=120&amp;maxwidth=104&amp;maxheight=160" />
		<!-- c:include src="cocoon://imageeditcomponent?id=prevImage1&amp;currentimage={$url}&amp;previewwidth=104&amp;previewheight=70&amp;minwidth=70&amp;minheight=70&amp;maxwidth=150&amp;maxheight=150" /-->
	</xsl:template>
	
	<xsl:template match="img2">
		<xsl:variable name="url"><xsl:value-of select="text()" /></xsl:variable>
		<c:include src="cocoon://imageeditcomponent?id=prevImage2&amp;currentimage={$url}&amp;previewwidth=104&amp;previewheight=140&amp;minwidth=10&amp;minheight=120&amp;maxwidth=104&amp;maxheight=160" />
		<!-- c:include src="cocoon://imageeditcomponent?id=prevImage2&amp;currentimage={$url}&amp;previewwidth=104&amp;previewheight=70&amp;minwidth=70&amp;minheight=70&amp;maxwidth=150&amp;maxheight=150" /-->
	</xsl:template>

	<!--  TESTO -->

	<xsl:template match="testo1">
		<b>Descrizione:</b>
		<br/>
		<textarea name="text1" rows="10" class="edittextarea"><xsl:value-of select="text()" /></textarea>
		<br />
	</xsl:template>
	
	<xsl:template match="testo2">
		<b>Descrizione:</b>
		<br/>
		<textarea name="text2" rows="10" class="edittextarea"><xsl:value-of select="text()" /></textarea>
		<br />
	</xsl:template>

</xsl:stylesheet>