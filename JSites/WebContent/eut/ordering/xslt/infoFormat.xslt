<?xml version="1.0" encoding="UTF-8"?>
<!-- xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:cinclude="http://apache.org/cocoon/include/1.0"-->
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:h="http://apache.org/cocoon/request/2.0"
								xmlns:email="http://apache.org/cocoon/transformation/sendmail" >
	<xsl:param name="quant"/>
	
	<xsl:template match="root">

		<xsl:apply-templates select="resultSet/record"/>
			
	</xsl:template>
	
	<xsl:template match="record">
		<ordine id="{jid}" rid="{jid}-">
			<xsl:variable name="prezzo"><xsl:value-of select="translate(translate(prezzo,',','.'),'&#8364;','')" /></xsl:variable>
			<id><xsl:value-of select="jid"/></id>
			<titolo><xsl:value-of select="ISBD"/></titolo>
			<autore><xsl:value-of select="authors/author"/></autore>
			<isbn><xsl:value-of select="substring-after(standardNumber,'ISBN')"/></isbn>
			<prezzo><xsl:value-of select="$prezzo" /></prezzo>
			<img>../../../<xsl:value-of select="substring-before(image,'.')"/></img>
			<quant><xsl:value-of select="$quant"/></quant>
			<prezzoXquant><xsl:value-of select="$prezzo * $quant"/></prezzoXquant>
			
		</ordine>
	</xsl:template>
	
	
</xsl:stylesheet>
		
