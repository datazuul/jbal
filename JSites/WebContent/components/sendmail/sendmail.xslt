<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:sql="http://apache.org/cocoon/SQL/2.0"
							xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
							xmlns:sendmail="http://apache.org/cocoon/transformation/sendmail"
							xmlns:h="http://apache.org/cocoon/request/2.0">

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
    
	<xsl:template match="root">
		<content>
			<sendmail:sendmail>		
				<sendmail:from>
					<xsl:value-of select="sendmail/email" />
				</sendmail:from>
				
				<xsl:apply-templates select="sendmail" />
				
				<sendmail:body>
					<![CDATA[
					<html>
						<body>
					
					Dati<br/>
					]]>
					<xsl:apply-templates select="h:request"/>
					<![CDATA[
						</body>
					</html>
					]]>
				</sendmail:body>
	
			</sendmail:sendmail>
		</content>
		
		
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
	</xsl:template>
		
	<xsl:template match="h:requestHeaders">
		<![CDATA[
			<h2>Header</h2>
			<table border='1'>
		]]>
		<xsl:apply-templates />
		<![CDATA[
			</table>
		]]>
	</xsl:template>
	
	<xsl:template match="h:requestParameters">
		<![CDATA[
			<h2>Parametri</h2>
			<table border='1'>
		]]>
		<xsl:apply-templates />
		<![CDATA[
			</table>
		]]>
	</xsl:template>
	
	<xsl:template match="h:parameter">
		<![CDATA[<tr><td><b>]]>
		<xsl:value-of select="@name"/><![CDATA[</b></td><td>]]><xsl:value-of select="h:value" />
		<xsl:text>&#xD;&#xA;</xsl:text>
		<![CDATA[</td></tr>]]>
	</xsl:template>
	
	<xsl:template match="h:header">
		<![CDATA[<tr><td><b>]]>
		<xsl:value-of select="@name"/><![CDATA[</b></td><td>]]><xsl:value-of select="." />
		<xsl:text>&#xD;&#xA;</xsl:text>
		<![CDATA[</td></tr>]]>
	</xsl:template>
	
	
	<xsl:template match="email">
		<sendmail:to><xsl:value-of select="."/></sendmail:to>
	</xsl:template>
	
	<xsl:template match="subject">
		<sendmail:subject><xsl:value-of select="."/></sendmail:subject>
	</xsl:template>
	
	<xsl:template match="smtphost">
	<!-- 	<sendmail:smtphost><xsl:value-of select="."/></sendmail:smtphost> -->
	</xsl:template>
	
	<xsl:template match="smtpuser">
   	<!-- 	<sendmail:smtpuser><xsl:value-of select="."/></sendmail:smtpuser>  -->
   	</xsl:template>
   	
   	<xsl:template match="smtppassword">
   	<!-- 	<sendmail:smtppassword><xsl:value-of select="."/></sendmail:smtppassword>  -->
	</xsl:template>
	
	
</xsl:stylesheet>