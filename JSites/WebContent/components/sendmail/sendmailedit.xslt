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
		<xsl:apply-templates select="sendmail" />
		
		<xsl:call-template name="callhelp" />
		
	</xsl:template>

	<!--  SEZIONE -->

	<xsl:template match="sendmail">
		<div class="{$time}">
			<div class="sezione">
				<div class="sezione_contenuto">
					<b>email:</b><input type="text" size="40" name="email" value="{email/text()}" /><br />
					<b>subject:</b><input type="text" size="40" name="subject" value="{subject/text()}" /><br />
					<b>smtphost:</b><input type="text" size="40" name="smtphost" value="{smtphost/text()}" /><br />
					<b>smtpuser:</b><input type="text" size="40" name="smtpuser" value="{smtpuser/text()}" /><br />
					<b>smtppassword:</b><input type="text" size="40" name="smtppassword" value="{smtppassword/text()}" /><br />
				</div>
			</div>		
		</div>
	</xsl:template>

	<!--  
	
	<email>address to send data</email>
	<subject>subject all mails will have</subject>
	<smtphost>SMTP host to use</smtphost>
	<smtpuser></smtpuser>
	<smtppassword></smtppassword>
	 -->

	
</xsl:stylesheet>