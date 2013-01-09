<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                        
    <xsl:param name="template"/>   
                           
	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="briciole">
		<div id="briciole">
		
			<xsl:value-of select="text()"/>
			<a href="feed" style="float: right; margin: 1px; height: 1px;"><img src="templates/{$template}/images/feed-icon-28x28.png" alt="rss feed" /></a>
		</div>
		<br/>
		
	</xsl:template>
		
	
	

</xsl:stylesheet>