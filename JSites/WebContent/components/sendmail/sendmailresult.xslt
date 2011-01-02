<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:sendmail="http://apache.org/cocoon/transformation/sendmail"
								xmlns:source="http://apache.org/cocoon/source/1.0">
								
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
    <xsl:param name="pagecode" />
    <xsl:param name="type" />
    <xsl:param name="pagerequest" />
    <xsl:param name="container" />
    
		 
	<xsl:template match="/content">
		<content>
		<xsl:apply-templates />
		</content>
		
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
	</xsl:template>
	
	
	<xsl:template match="sendmail:result">
		<xsl:if test="sendmail:failure">
			<xsl:value-of select="//mailerror" />
		</xsl:if>
		<xsl:if test="sendmail:success">
			<xsl:value-of select="//mailsent" />
		</xsl:if>
	</xsl:template>

	<xsl:template match="mailsent" />
	
	<xsl:template match="mailerror" />
	
	
	 
<!-- 	<xsl:template match="text()"/>  -->

	<xsl:template match="@*|node()|text()">
		<xsl:copy-of select=".">
			<!-- <xsl:apply-templates select="@*|node()|text()|*" />  -->
		</xsl:copy-of> 
	</xsl:template>
	

</xsl:stylesheet>