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
	    <xsl:apply-templates select="publicationPairs"/>
		
		<xsl:call-template name="callhelp" />
		
	</xsl:template>
	
	<xsl:template match="publicationPairs">
		<div class="{$time}">
			Titolo:<br/>
			<input type="text" size="40" name="titolo" value="{titolo}" class="box_title"/><br/>
			Lista pubblicazioni:<br/>
			<input type="text" size="40" name="list" value="{list}" class="box_title"/>
			<div class="clearer"/>
		</div>
	</xsl:template>

	<!--  SEZIONE -->



</xsl:stylesheet>