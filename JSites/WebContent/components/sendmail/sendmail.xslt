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
			<xsl:if test="contains(h:request/h:requestHeaders/h:header[@name='referer']/text(),concat('pid=',$pid))">
				<xsl:variable name="r">
					<xsl:call-template name="check-tokens">
		            	<xsl:with-param name="list"><xsl:value-of select="sendmail/required" /></xsl:with-param>
		        	</xsl:call-template>
				</xsl:variable>
								
				<xsl:if test="not(contains($r,'1'))">
					<xsl:variable name="body">
						<xsl:apply-templates select="h:request"/>
					</xsl:variable>
					<sendmail:sendmail>		
						<sendmail:from>
							<xsl:value-of select="sendmail/email" />
						</sendmail:from>
						
						<xsl:apply-templates select="sendmail" />
						
						<sendmail:body>
							<![CDATA[
							<html>
								<body>
							
							Dati da: 
							]]><xsl:value-of select="h:request/h:requestHeaders/h:header[@name='referer']/text()" /><![CDATA[<br/>]]>
							<xsl:value-of select="$body" />
							<![CDATA[
								</body>
							</html>
							]]>
						</sendmail:body>
			
					</sendmail:sendmail>
					Sono state inviate le seguenti informazioni:<br/>
					<futiz>
					<xsl:value-of select="normalize-space($body)" disable-output-escaping="no"/>
					</futiz>
				</xsl:if>
			</xsl:if>
		</content>
		
		
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
	</xsl:template>
		
	<xsl:template match="h:requestHeaders">
	
		<xsl:if test="string-length(normalize-space(/root/sendmail/parameters))=0">
			<![CDATA[
				<h2>Header</h2>
				<table border='1'>
			]]>
			<xsl:apply-templates />
			<![CDATA[
				</table>
			]]>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="h:requestParameters">
		<![CDATA[
			<h2>Parametri</h2>
			<table border='1'>
		]]>
		<xsl:if test="string-length(normalize-space(/root/sendmail/parameters))=0">
			
			<xsl:apply-templates />
			
		</xsl:if>
		<xsl:if test="string-length(normalize-space(/root/sendmail/parameters))!=0">
			<xsl:call-template name="output-tokens">
            	<xsl:with-param name="list"><xsl:value-of select="/root/sendmail/parameters/text()" /></xsl:with-param>
        	</xsl:call-template>
			
		</xsl:if>
		<![CDATA[
			</table>
		]]>
	</xsl:template>
	
	<xsl:template name="output-tokens">
      <xsl:param name="list" />
      <xsl:variable name="newlist" select="concat(normalize-space($list), ' ')" />
      <xsl:variable name="first" select="substring-before($newlist, ' ')" />
      <xsl:variable name="remaining" select="substring-after($newlist, ' ')" />
      <![CDATA[<tr><td><b>]]>
     	<xsl:value-of select="$first" /><![CDATA[</b></td><td>]]><xsl:value-of select="/root/h:request/h:requestParameters/h:parameter[@name=$first]/h:value" /> 
      	<xsl:text>&#xD;&#xA;</xsl:text>
      <![CDATA[</td></tr>]]>
		
      <xsl:if test="$remaining">
          <xsl:call-template name="output-tokens">
              <xsl:with-param name="list" select="$remaining" />
          </xsl:call-template>
      </xsl:if>
  	</xsl:template>
  	
  	<xsl:template name="check-tokens">
      <xsl:param name="list" />
      <xsl:if test="string-length(normalize-space($list))!=0">
	      <xsl:variable name="newlist" select="concat(normalize-space($list), ' ')" />
	      <xsl:variable name="first" select="substring-before($newlist, ' ')" />
	      <xsl:variable name="remaining" select="substring-after($newlist, ' ')" />
	      
	
		  <xsl:if test="string-length(normalize-space(/root/h:request/h:requestParameters/h:parameter[@name=$first]/h:value))=0">1</xsl:if>
	
			
	      <xsl:if test="$remaining">
	          <xsl:call-template name="check-tokens">
	              <xsl:with-param name="list" select="$remaining" />
	          </xsl:call-template>
	      </xsl:if>
      </xsl:if>
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