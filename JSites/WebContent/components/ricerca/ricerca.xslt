<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                              
	<xsl:param name="cid"/>
	<xsl:param name="pid"/>
	<xsl:param name="pacid"/>
	
	<!-- form name="form1" method="get" action="metaSearch(1)">
                <input name="cocoon-view" type="hidden" value="pSearch" />
                <input name="C7" type="hidden" value="ANY" />
                <input name="R7" type="text" />
                <input type="submit" value="Cerca" />
            </form-->
	
	<xsl:template match="/">
		<xsl:copy-of select="*" />
	</xsl:template>
	
	<!-- 	
	
	<xsl:template match="section">
		<xsl:variable name="url"><xsl:value-of select="img/text()"/></xsl:variable>
		<div class="sezione_immagine"><img src="{$url}" width="70" height="70"/></div>
		<div class="modifica">
			<input type="button" value="Modifica" onClick="location.href='pageedit?pid={$pid}&amp;cid={$cid}'"/>
		</div>
		<div class="sezione_contenuto">
			<h1 class="sezione_titolo"><xsl:value-of select="titolo/text()"/>
			</h1>
			<p class="sezione_testo"><xsl:value-of select="testo/text()"/></p>
		</div>
	
	</xsl:template>
	
	 -->
	    
</xsl:stylesheet>