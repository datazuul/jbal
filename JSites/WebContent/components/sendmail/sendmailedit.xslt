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
					<table border='0'>
						<tr><td><b>email:</b></td><td><input type="text" size="40" name="email" value="{email/text()}" /></td></tr>
						<tr><td><b>subject:</b></td><td><input type="text" size="40" name="subject" value="{subject/text()}" /></td></tr>
						<tr><td><b>parameters:</b></td><td><input type="text" size="40" name="parameters" value="{parameters/text()}" /></td></tr>
						<tr><td><b>required:</b></td><td><input type="text" size="40" name="required" value="{required/text()}" /></td></tr>
						<tr><td><b>smtphost:</b></td><td><input type="text" size="40" name="smtphost" value="{smtphost/text()}" /></td></tr>
						<tr><td><b>welcome message:</b></td><td><input type="text" size="40" name="welcome" value="{welcome/text()}" /></td></tr>
						<tr><td><b>mail sent message:</b></td><td><input type="text" size="40" name="mailsent" value="{mailsent/text()}" /></td></tr>
						<tr><td><b>mail error message:</b></td><td><input type="text" size="40" name="mailerror" value="{mailerror/text()}" /></td></tr>
						<tr><td><b>missing parameter message:</b></td><td><input type="text" size="40" name="missingparameter" value="{missingparameter/text()}" /></td></tr>
						<tr><td><b>recap sent data to user:</b></td><td><input type="checkbox" name="recap" value="{recap/text()}" /></td></tr>
						<tr><td><b>field containing user email to send cc:</b></td><td><input type="text" size="40" name="cc" value="{cc}" /></td></tr>
					</table>
				</div>
			</div>		
		</div>
	</xsl:template>

	
</xsl:stylesheet>