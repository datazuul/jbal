<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0"
								xmlns:sql="http://apache.org/cocoon/SQL/2.0">
    
    <xsl:template match="/">
    	<externalLink><titolo><xsl:value-of select="externalLink/sql:rowset/sql:row/sql:name" /></titolo>
        	<url><xsl:value-of select="externalLink/sql:rowset/sql:row/sql:url" /></url>            
        </externalLink>
    </xsl:template>
    
</xsl:stylesheet>