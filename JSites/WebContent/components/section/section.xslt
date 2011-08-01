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
    <xsl:param name="type" />
    <xsl:param name="container" />
    
	<xsl:template match="/">
		<div class="{$time}">
			<div class="sezione{$extra}" id="{$cid}">
				<!--  <xsl:apply-templates select="section/lang[@code=$lang]"/> -->
				<xsl:apply-templates select="section"/>
				<div class="_jclearer">&#160;</div>
			</div>
		</div>
		
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
	</xsl:template>
	
	<xsl:template match="titolo">
	<!--
		<xsl:variable name="tit"><xsl:value-of select="@type"/></xsl:variable>

		<xsl:if test="$tit = '1'">
			<h1 class="sezione_titolo{$extra}"><xsl:value-of select="text()"/></h1>
		</xsl:if>
		<xsl:if test="$tit != '1'">
			<h2 class="sezione_titolo{$extra}"><xsl:value-of select="text()"/></h2>
		</xsl:if>
		-->
	</xsl:template>
	
		
	<!-- <xsl:template match="section/lang"> -->
	<xsl:template match="section">
	
		<!--  IMMAGINE -->

		<xsl:variable name="url"><xsl:value-of select="image/text()"/></xsl:variable>
		<xsl:variable name="titolo"><xsl:value-of select="titolo/text()"/></xsl:variable>
		<xsl:variable name="tit"><xsl:value-of select="titolo/@type"/></xsl:variable>
		<xsl:variable name="link"><xsl:value-of select="image/@link"/></xsl:variable>
		
		
		<h1 class="sezione_titolo{$extra}">
		
		
		
			<xsl:if test="not(contains($url, 'no_image'))">
				<!--  <div class="sezione_immagine"> -->
					<xsl:if test="string-length(normalize-space($link)) > 0">
						<!-- Link non e vuoto  -->
						<a href="{$link}">
							<xsl:if test="contains($link,'http')">
								<xsl:attribute name="target">_blank</xsl:attribute>
							</xsl:if>
							<img src="{$url}" class="image">
								<xsl:choose>
									<xsl:when test="contains($titolo,'>')">
										<xsl:attribute name="alt"><xsl:value-of select="substring-before(substring-after($titolo,'>'),']')"/></xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:attribute name="alt"><xsl:value-of select="$titolo"/></xsl:attribute>
									</xsl:otherwise>
								</xsl:choose>
							</img>
						</a>
					</xsl:if>
					<xsl:if test="string-length(normalize-space($link)) = 0">
						<!-- Link e vuoto  -->
						<img src="{$url}" class="image">
							<xsl:choose>
								<xsl:when test="contains($titolo,'>')">
									<xsl:attribute name="alt"><xsl:value-of select="substring-before(substring-after($titolo,'>'),']')"/></xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="alt"><xsl:value-of select="$titolo"/></xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
						</img>
					</xsl:if>
				<!--  </div> -->
			</xsl:if>
			
			<xsl:value-of select="$titolo" />
		
		</h1>
		
		
		<!-- CONTENUTO SE C'E' L'IMMAGINE -->
		
		
		<div class="sezione_contenuto{$extra}">
			<div class="sezione_testo{$extra}"><xsl:value-of disable-output-escaping="yes" select="testo/text()"/></div>
		</div>
	
		
		<!--  CONTENUTO SE NON C'E' L'IMMAGINE -->
		
		<!--  <xsl:if test="$url = 'images/contentimg/no_image'">
			
		
			<div class="sezione_testo{$extra}"><xsl:value-of select="testo/text()"/></div>
		</xsl:if>
				
		-->
		
	</xsl:template>
	
	<!--  COMBOBOX DI ORDINAMENTO -->
	<!-- xsl:template match="order">
	
		<div class="modifica">
			<select class="buttonright" name="cid{$cid}">
				<xsl:call-template name="iterate">
					<xsl:with-param name = "iterateleft" ><xsl:value-of select="elementnumber/text()"/></xsl:with-param>
				</xsl:call-template>
			</select>
		</div>
		<div class="clearer">&#160;</div>
		<hr color="#8BB6D8"/>
	</xsl:template-->
	
	<!--  PIPELINE DI ITERAMENTO ELEMENTI DI ORDINAMENTO -->						
	<!-- xsl:template name="iterate">
		<xsl:param name = "iterateleft" />
		<xsl:variable name="order"><xsl:value-of select="selected/text()"/></xsl:variable>
		<xsl:variable name="top"><xsl:value-of select="elementnumber/text()"/></xsl:variable>
			
		<xsl:element name="option" onChange="verifyChoice()">
			<xsl:attribute name="value"><xsl:value-of select="$top - $iterateleft + 1" /></xsl:attribute>
			<xsl:if test="$top - $iterateleft + 1 = $order">
				<xsl:attribute name="selected" />
			</xsl:if>
			<xsl:value-of select="$top - $iterateleft + 1" />
		</xsl:element>
			
		<xsl:if test="number($iterateleft) > 1">
			<xsl:call-template name="iterate">
				<xsl:with-param name = "iterateleft" ><xsl:value-of select="$iterateleft - 1"/></xsl:with-param>
			</xsl:call-template>
		</xsl:if>	
	</xsl:template-->

</xsl:stylesheet>