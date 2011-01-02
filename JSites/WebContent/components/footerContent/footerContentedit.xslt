<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="../../stylesheets/xslt/helpedit.xslt" />
	
  <xsl:param name="cid" />
	<xsl:param name="time" />
	<xsl:param name="pid" />
	<xsl:param name="extra" />
	<xsl:param name="context" />
	<xsl:param name="permission" />
	<xsl:param name="template"/>
  
  <xsl:include href="../section/edit.xslt" />
  
	<xsl:template match="/">
		<xsl:apply-templates select="footerContent" />
		<xsl:call-template name="callhelp" />
	</xsl:template>
	
	<xsl:template match="footerContent">
		<div class="{$time}">
			<div class="sezione">
				<div class="sezione_contenuto">
					<xsl:call-template name="editor" />
					<input type="hidden" name="UndoText" id="UndoText"/>
					<textarea id="elm2" name="text" rows="10" class="edittextarea" tinyMCE_this="true">
			<!--  xsl:value-of select="text()" /-->
			<!-- xsl:copy-->
						<xsl:apply-templates select="@*|node()|text()|*" />
			<!--  /xsl:copy-->
					</textarea>
					<br />
				</div>
			</div>		
		</div>
	</xsl:template>
	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>
    
</xsl:stylesheet>