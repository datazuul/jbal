<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:sql="http://apache.org/cocoon/SQL/2.0"
							xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
							xmlns:sendmail="http://apache.org/cocoon/transformation/sendmail"
							xmlns:h="http://apache.org/cocoon/request/2.0">
                              
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
    
	<xsl:template match="root">
		<content>
			<xsl:variable name="urlrequest">http://<xsl:value-of select="h:request/h:requestHeaders/h:header[@name='host']" /><xsl:value-of select="$pagerequest" /></xsl:variable>
			
			<xsl:if test="not(contains(h:request/h:requestHeaders/h:header[@name='referer']/text(),$urlrequest))">
				<xsl:value-of select="//welcome" />
			</xsl:if>
			
			<xsl:if test="contains(h:request/h:requestHeaders/h:header[@name='referer']/text(),$urlrequest)">
				<xsl:variable name="r">
					<xsl:call-template name="check-tokens">
		            	<xsl:with-param name="list"><xsl:value-of select="sendmail/required" /></xsl:with-param>
		        	</xsl:call-template>
				</xsl:variable>
								
				<xsl:if test="string-length(normalize-space($r)) > 0">
					<xsl:value-of select="//missingparameter" />&#160;<xsl:value-of select="$r" />
				</xsl:if>			
				
				<xsl:if test="string-length(normalize-space($r)) = 0">
					<xsl:variable name="body">
						<xsl:apply-templates select="h:request"/>
					</xsl:variable>
					<sendmail:sendmail>		
						<!-- 
						<sendmail:from>
							<xsl:value-of select="sendmail/email" />
						</sendmail:from>
						-->
						<xsl:apply-templates select="sendmail" />
						
						<sendmail:body>
							<![CDATA[
							Dati da: 
							
							]]><xsl:value-of select="h:request/h:requestHeaders/h:header[@name='referer']/text()" /><![CDATA[<br/>]]>
							<xsl:value-of select="$body" />
							
						</sendmail:body>
			
					</sendmail:sendmail>
					
					<xsl:if test="contains(//recap,'true')">
						<![CDATA[
						Sono state inviate le seguenti informazioni:
						]]>
						<futiz>
						<xsl:value-of select="normalize-space($body)" disable-output-escaping="no"/>
						</futiz>
					</xsl:if>
				</xsl:if>
			</xsl:if>
		</content>
		
	</xsl:template>
		
	<xsl:template match="h:requestHeaders">
	
		<xsl:if test="string-length(normalize-space(/root/sendmail/parameters))=0">
			<![CDATA[
				Header
				
			]]>
			<xsl:apply-templates />
			<![CDATA[
				
			]]>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="h:requestParameters">
		<![CDATA[
			Parametri
			
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
			
		]]>
	</xsl:template>
	
	<xsl:template name="output-tokens">
      <xsl:param name="list" />
      <xsl:variable name="newlist" select="concat(normalize-space($list), ' ')" />
      <xsl:variable name="first" select="substring-before($newlist, ' ')" />
      <xsl:variable name="remaining" select="substring-after($newlist, ' ')" />
      <![CDATA[]]>
     	<xsl:value-of select="$first" /><![CDATA[ = ]]><xsl:value-of select="/root/h:request/h:requestParameters/h:parameter[@name=$first]/h:value" /> 
      	<xsl:text>&#xD;&#xA;</xsl:text>
      <![CDATA[
	  ]]>
		
      <xsl:if test="$remaining">
          <xsl:call-template name="output-tokens">
              <xsl:with-param name="list" select="$remaining" />
          </xsl:call-template>
      </xsl:if>
  	</xsl:template>
  	
  	<xsl:template match="parameters"></xsl:template>
  	<xsl:template match="required"></xsl:template>
  	
  	<xsl:template name="check-tokens">
      <xsl:param name="list" />
      <xsl:if test="string-length(normalize-space($list))!=0">
	      <xsl:variable name="newlist" select="concat(normalize-space($list), ' ')" />
	      <xsl:variable name="first" select="substring-before($newlist, ' ')" />
	      <xsl:variable name="remaining" select="substring-after($newlist, ' ')" />
	      
	
		  <xsl:if test="string-length(normalize-space(/root/h:request/h:requestParameters/h:parameter[@name=$first]/h:value))=0">&#160;<xsl:value-of select="$first" /></xsl:if>
	
			
	      <xsl:if test="$remaining">
	          <xsl:call-template name="check-tokens">
	              <xsl:with-param name="list" select="$remaining" />
	          </xsl:call-template>
	      </xsl:if>
      </xsl:if>
  	</xsl:template>
	
	
	<xsl:template match="h:parameter">
		<![CDATA[]]>
		<xsl:value-of select="@name"/><![CDATA[ = ]]><xsl:value-of select="h:value" />
		<xsl:text>&#xD;&#xA;</xsl:text>
		<![CDATA[
		]]>
	</xsl:template>
	
	<xsl:template match="h:header">
		<![CDATA[]]>
		<xsl:value-of select="@name"/><![CDATA[ = ]]><xsl:value-of select="." />
		<xsl:text>&#xD;&#xA;</xsl:text>
		<![CDATA[
		]]>
	</xsl:template>
	
	<xsl:template match="cc">
		<xsl:variable name="node"><xsl:value-of select="normalize-space(//cc)" /></xsl:variable>
		<sendmail:from><xsl:value-of select="/root/h:request/h:requestParameters/h:parameter[@name=$node]/h:value"/></sendmail:from>
	</xsl:template>
	
	<xsl:template match="email">
		<sendmail:to><xsl:value-of select="."/></sendmail:to>
		<xsl:variable name="node"><xsl:value-of select="//cc" /></xsl:variable>
		<xsl:variable name="nodeval"><xsl:value-of select="/root/h:request/h:requestParameters/h:parameter[@name=$node]/h:value"/></xsl:variable>
		<xsl:if test="string-length(normalize-space($nodeval))!=0">
			<sendmail:to><xsl:value-of select="$nodeval" /></sendmail:to>
		</xsl:if>
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
	
	
	<xsl:template match="welcome">
	<!-- 	<xsl:value-of select="." />  -->
	</xsl:template>
	
	<xsl:template match="mailsent">
		<mailsent>
			<xsl:value-of select="." />
		</mailsent>
	</xsl:template>
	
	<xsl:template match="mailerror">
		<mailerror>
			<xsl:value-of select="." />
		</mailerror>
	</xsl:template>
	
	<xsl:template match="missingparameter">
	<!--  <xsl:value-of select="." />  -->
	</xsl:template>
	
</xsl:stylesheet>