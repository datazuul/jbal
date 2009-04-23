<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0">


	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="testo">
		<testo>ATTENZIONE: State per uscire dal sito Web del Sistema Bibliotecario di Ateneo (SBA)

SBA non esercita alcun controllo sui contenuti del sito Web che state per visitare. 
Tali contenuti potrebbero essere in una lingua diversa rispetto a questo sito. 
SBA fornisce questo link a vostro esclusivo vantaggio e non intende promuovere in alcun modo i contenuti, 
i prodotti o i servizi che vi verranno offerti nel sito collegato.
		
		
			__Redirecting to__ [<xsl:value-of select="text()"/>&gt;__<xsl:value-of select="text()"/>__]
		</testo>
	</xsl:template>

	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>