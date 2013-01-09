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
		<xsl:apply-templates select="newslist" />
		
		<xsl:call-template name="callhelp" />
		
	</xsl:template>



	<xsl:template match="newslist">
		<div class="{$time}">
			<div class="newslist">

			</div>		
		</div>
	</xsl:template>

</xsl:stylesheet>