<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0">
								
	<xsl:variable name="list">
		<xsl:choose>
			<xsl:when test="string-length(save/list) &gt; 0">1</xsl:when>
			<xsl:otherwise>0</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:param name="datadir"/>
	
	<xsl:template match="/save">
		<source:write>
	    <source:source><xsl:value-of select="$datadir" />/data/sendmail<xsl:value-of select="cid/text()"/>.xml
	    </source:source>
	    <source:fragment>
			<sendmail>
				<xsl:apply-templates/>
			</sendmail>
	    </source:fragment>
		</source:write>
	</xsl:template>
	
	<!-- 
	
	<email>address to send data</email>
	<subject>subject all mails will have</subject>
	<smtphost>SMTP host to use</smtphost>
	<smtpuser></smtpuser>
	<smtppassword></smtppassword>
	
	 -->
	
	<xsl:template match="email">
		<email>
			<xsl:value-of select="text()"/>
		</email>
	</xsl:template>
	
	<xsl:template match="subject">
		<subject>
			<xsl:value-of select="text()"/>
		</subject>
	</xsl:template>
	
	<xsl:template match="smtphost">
		<smtphost>
			<xsl:value-of select="text()"/>
		</smtphost>
	</xsl:template>
	
	<xsl:template match="smtpuser">
		<smtpuser>
			<xsl:value-of select="text()"/>
		</smtpuser>
	</xsl:template>
	
	<xsl:template match="smtppassword">
		<smtppassword>
			<xsl:value-of select="text()"/>
		</smtppassword>
	</xsl:template>
	
	<xsl:template match="parameters">
		<parameters>
			<xsl:value-of select="text()"/>
		</parameters>
	</xsl:template>
	
	
	
	<xsl:template match="text()"/>

	<xsl:template match="@*|node()|text()">
		<!-- xsl:copy-->
			<xsl:apply-templates select="@*|node()|text()|*" />
		<!-- /xsl:copy-->
	</xsl:template>

</xsl:stylesheet>