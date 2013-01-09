<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml">

	<xsl:template match="navbar">
			<xsl:if test="$viewsidebar = '1'">
		<div class="art-layout-cell art-sidebar1">
			<xsl:call-template name="inner" />
		</div>
	</xsl:if>
</xsl:template>


	<xsl:template name="inner">
	<div class="art-block">
			<div class="art-block-body">

				<xsl:for-each select="*">
					<xsl:copy-of select="." />
					<div class="cleared">&#160;</div>
					
				</xsl:for-each>

				<div class="cleared">&#160;</div>
				
				
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
