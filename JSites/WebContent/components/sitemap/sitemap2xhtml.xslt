<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	
	 <xsl:include href="../../stylesheets/xslt/editlinks0.xslt" />
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
    <xsl:param name="type" />
    <xsl:param name="container" />
                              
	<xsl:template match="/">
		<xsl:apply-templates />
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
	</xsl:template>
	
	<xsl:template match="navigator">
		<!-- div id="{$classlevel0}"-->
		<div class="sitemap">
			<xsl:if test="count(navpage) > 0">
				<ul>
					<xsl:apply-templates /> <!-- select="voce" / -->
				</ul>
			</xsl:if>
		</div>
		<!--  /div>-->
			
	</xsl:template>
	
	<xsl:template match="navpage">
			<li>
				<a href="{@link}">
					<xsl:value-of select="@name" />
				</a>
			
				<xsl:if test="count(navpage) > 0">
					<ul>
						<xsl:apply-templates />
					 </ul>
				</xsl:if>
			</li>			 
	</xsl:template>
	
	
	
	<xsl:template match="id"></xsl:template>

</xsl:stylesheet>