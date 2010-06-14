<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:sendmail="http://apache.org/cocoon/transformation/sendmail"
								xmlns:source="http://apache.org/cocoon/source/1.0">
								
	
	<xsl:template match="/content">
		<content>
		<xsl:apply-templates />
		</content>
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