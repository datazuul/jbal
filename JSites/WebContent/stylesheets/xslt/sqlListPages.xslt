<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
								xmlns:c="http://apache.org/cocoon/include/1.0"
								xmlns:sql="http://apache.org/cocoon/SQL/2.0">
	
    <xsl:template match="/">
    	<pages>
	    	<xsl:apply-templates />
    	</pages>
    </xsl:template>
    
    <xsl:template match="sql:rowset">
    	<xsl:apply-templates />
    </xsl:template>
    
    <xsl:template match="sql:row">
    	<pageId><xsl:value-of select="sql:id" /></pageId>
	</xsl:template>

</xsl:stylesheet>