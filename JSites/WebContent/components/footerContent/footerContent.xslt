<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:include href="../../stylesheets/xslt/editlinks.xslt" />
	
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
    <xsl:param name="container" />
	
	<xsl:template match="footerContent">
		<xsl:copy-of select="*" />
		<!--  xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" / -->
	</xsl:template>
	    
</xsl:stylesheet>