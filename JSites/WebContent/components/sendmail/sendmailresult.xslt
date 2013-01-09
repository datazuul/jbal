<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:sendmail="http://apache.org/cocoon/transformation/sendmail"
								xmlns:source="http://apache.org/cocoon/source/1.0">
								
    <xsl:include href="../../stylesheets/xslt/editlinks.xslt" />
    <!-- Questo include serve pei link de modifica, elimina e (dis)attiva   -->
		 
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
    
   	<xsl:template match="subject" />
	<xsl:template match="from" />
	<xsl:template match="to" />
	<xsl:template match="smtphost" />
	<xsl:template match="body" />
    
   	<xsl:template match="/content">
		<xsl:apply-templates />
		
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
	</xsl:template>
	
	<xsl:template match="result[2]" />
	
	<xsl:template match="result[1]">
		<div>
		<xsl:apply-templates />
		</div>
	</xsl:template>
	
	<xsl:template match="success">
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="failure">
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="sendmail">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="debug">
		<xsl:if test="contains(/content/sendmail/debug,'true')" >
			<pre>
				<xsl:value-of select="." />
			</pre>
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