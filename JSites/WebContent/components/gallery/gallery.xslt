<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:sql="http://apache.org/cocoon/SQL/2.0"
							xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
							xmlns:cinclude="http://apache.org/cocoon/include/1.0">

    <xsl:include href="../../stylesheets/xslt/editlinks.xslt" />
    <!-- Questo include serve pei link de modifica, elimina e (dis)attiva 
		 ma anca per le combo de ordinamento  -->
                              
	<xsl:param name="cid"/>
	<xsl:param name="pid"/>
	<xsl:param name="pacid"/>
	<xsl:param name="editing"/>
	<xsl:param name="lang"/>
    <xsl:param name="validating"/>
    <xsl:param name="disabling"/>
    <xsl:param name="time"/>
    <xsl:param name="extra" />
    <xsl:param name="type" />
    
	<xsl:template match="/">
		<div class="{$time}">
			<div class="sezione{$extra}" id="{$cid}">
				<xsl:apply-templates select="gallery"/>
				<div class="clearer">&#160;</div>
			</div>
		</div>
		
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
	</xsl:template>
	
	<xsl:template match="titolo">
		<h2 class="sezione_titolo{$extra}"><xsl:value-of select="text()"/></h2>
	</xsl:template>
	
		
	<!-- <xsl:template match="section/lang"> -->
	<xsl:template match="gallery">
		<xsl:apply-templates select="titolo"/>
		<cinclude:include src="cocoon:/pgallery/{dir}"/>
	</xsl:template>

</xsl:stylesheet>