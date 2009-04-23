<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:sql="http://apache.org/cocoon/SQL/2.0"
							xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:include href="../../stylesheets/xslt/editlinks.xslt" />
    <!-- Questo include serve pei link de modifica, elimina e (dis)attiva 
		 ma anca per le combo de ordinamento  -->
                              
	<xsl:param name="cid"/>
	<xsl:param name="pid"/>
	<xsl:param name="pacid"/>
	<xsl:param name="editing"/>
	<xsl:param name="lang"/>
    <xsl:param name="validating"/>
    <xsl:param name="disabling"/>
    <xsl:param name="time"/>
    <xsl:param name="extra" />
    
	<xsl:template match="/">
		<div class="{$time}">
			<div class="sezione">
				<!--  <xsl:apply-templates select="section/lang[@code=$lang]"/> -->
				<xsl:apply-templates select="newslist" />
				<div class="clearer">&#160;</div>
			</div>
		</div>
		
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
	</xsl:template>
	
	<xsl:template match="newslist">
		
		<h1 class="newslist_titolo">AVVISI</h1>

		<div class="newslist_avvisi">
			<xsl:apply-templates />
		</div>
	</xsl:template>
	
	<xsl:template match="newsitem">
		<div class="newsitem">
			<a href="pageview?pid={@pid}#{@cid}">
				<span class="newsitem_insertiondate">
					<xsl:variable name="idate">
						<xsl:value-of select="substring-before(@insertionDate,' ')" />
					</xsl:variable>
					<xsl:value-of select="substring($idate,9,2)" />/<xsl:value-of select="substring($idate,6,2)" />&#160;</span> 
				<!-- span class="newsitem_pagina">
					<xsl:value-of select="@pageName" />:&#160;</spani -->
				<span class="newsitem_titolo">
					<xsl:value-of select="div[@class='presente']/div[@class='sezione']/h2[@class='sezione_titolo']" />
					<xsl:value-of select="div[@class='presente']/div[@class='sezione']/h1[@class='sezione_titolo']" />
					<xsl:value-of select="div[@class='presente']/div[@class='sezione']/div[@class='sezione_contenuto']/h2[@class='sezione_titolo']" />
					<xsl:value-of select="div[@class='presente']/div[@class='sezione']/div[@class='sezione_contenuto']/h1[@class='sezione_titolo']" />&#160;</span>
				<span class="newsitem_testo">
					<xsl:value-of select="substring(div[@class='presente']/div[@class='sezione']/div[@class='sezione_testo'],1,50)" />
					<xsl:value-of select="substring(div[@class='presente']/div[@class='sezione']/div[@class='sezione_contenuto']/div[@class='sezione_testo'],1,50)" />...
				</span>
			</a>
		</div>
	</xsl:template>
	

	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>


</xsl:stylesheet>
