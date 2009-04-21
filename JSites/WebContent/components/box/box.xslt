<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:include href="../../stylesheets/xslt/editlinks.xslt" />
    <!-- Questo include serve pei link de modifica, elimina e (dis)attiva 
		 ma anca per le combo de ordinamento  -->
                              
	<xsl:param name="cid"/>
	<xsl:param name="unit"/>
	<xsl:param name="pid"/>
	<xsl:param name="pacid"/>
	<xsl:param name="editing"/>
	<xsl:param name="lang"/>
    <xsl:param name="validating"/>
    <xsl:param name="disabling"/>
    <xsl:param name="time"/>
    
    <xsl:template match="/">
	    <xsl:apply-templates select="box"/>
	    <!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
    </xsl:template>
    
	<xsl:template match="box">
		<div class="{$time}">
			<xsl:choose>
				<xsl:when test="$unit = '1'"><xsl:apply-templates select="unit1"/></xsl:when>
				<xsl:when test="$unit = '2'"><xsl:apply-templates select="unit2"/></xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="unit1"/>
					<xsl:apply-templates select="unit2"/>
				</xsl:otherwise>
			</xsl:choose>
			<div class="clearer"/>
		</div>
		<div class="clearer"/>
	</xsl:template>
	
	<xsl:template match="titolo1">
		<xsl:variable name="tit"><xsl:value-of select="@type"/></xsl:variable>
		<xsl:if test="$tit = '1'">
			<h1 class="box_titolo"><xsl:value-of select="text()"/></h1>
		</xsl:if>
		<xsl:if test="$tit != '1'">
			<h2 class="box_titolo"><xsl:value-of select="text()"/></h2>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="titolo2">
		<xsl:variable name="tit"><xsl:value-of select="@type"/></xsl:variable>
		<xsl:if test="$tit = '1'">
			<h1 class="box_titolo"><xsl:value-of select="text()"/></h1>
		</xsl:if>
		<xsl:if test="$tit != '1'">
			<h2 class="box_titolo"><xsl:value-of select="text()"/></h2>
		</xsl:if>
	</xsl:template>
		
	<!-- <xsl:template match="section/lang"> -->
	<xsl:template match="unit1">

		<div class="box">

		<!--  IMMAGINE -->

			<xsl:variable name="url"><xsl:value-of select="img1/text()"/></xsl:variable>
			<xsl:variable name="titolo"><xsl:value-of select="titolo1/text()"/></xsl:variable>
			<xsl:variable name="link"><xsl:value-of select="img1/@link"/></xsl:variable>
			
			<xsl:if test="$url != 'images/contentimg/no_image'">
				<xsl:call-template name="putimage">
					<xsl:with-param name="url" select="$url"/>
					<xsl:with-param name="titolo" select="$titolo"/>
					<xsl:with-param name="link" select="$link"/>
				</xsl:call-template>
			</xsl:if>
			
			
			
			<!-- CONTENUTO SE C'E' L'IMMAGINE -->
			
			<xsl:if test="$url != 'images/contentimg/no_image'">
				<div class="box_contenuto">
				
					<xsl:apply-templates select="titolo1"/>
							
					<div class="box_testo">
						<xsl:call-template name="acquista">
							<xsl:with-param name="testo"><xsl:value-of select="testo1/text()"/></xsl:with-param>
							<xsl:with-param name="cid"><xsl:value-of select="$cid"/></xsl:with-param>
							<xsl:with-param name="unit">1</xsl:with-param>
						</xsl:call-template>
					</div>
				</div>
			</xsl:if>
			
			<!--  CONTENUTO SE NON C'E' L'IMMAGINE -->
			
			<xsl:if test="$url = 'images/contentimg/no_image'">
				<xsl:apply-templates select="titolo1"/>
			
				<div class="box_testo">
					<xsl:call-template name="acquista">
						<xsl:with-param name="testo"><xsl:value-of select="testo1/text()"/></xsl:with-param>
						<xsl:with-param name="cid"><xsl:value-of select="$cid"/></xsl:with-param>
						<xsl:with-param name="unit">1</xsl:with-param>
					</xsl:call-template>
				</div>
			</xsl:if>
				
		</div>
	</xsl:template>
	
	<xsl:template name="putimage">
		<xsl:param name="url" />
		<xsl:param name="titolo" />
		<xsl:param name="link" />
		
		
		<div class="box_immagine">
			<xsl:choose>
				<xsl:when test="$link != ''">
					<a href="{$link}">
						<img src="{$url}" width="70" height="70">
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
				</xsl:when>
				<xsl:otherwise>
					<img src="{$url}" width="104">
						<xsl:choose>
							<xsl:when test="contains($titolo,'>')">
								<xsl:attribute name="alt"><xsl:value-of select="substring-before(substring-after($titolo,'>'),']')"/></xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="alt"><xsl:value-of select="$titolo"/></xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
					</img>
				</xsl:otherwise>
			</xsl:choose>

		</div>

	</xsl:template>
	
	<xsl:template match="unit2">

		<div class="box">

		<!--  IMMAGINE -->

			<xsl:variable name="url"><xsl:value-of select="img2/text()"/></xsl:variable>
			<xsl:variable name="titolo"><xsl:value-of select="titolo2/text()"/></xsl:variable>
			<xsl:variable name="link"><xsl:value-of select="img2/@link"/></xsl:variable>
			
			<xsl:if test="$url != 'images/contentimg/no_image'">
				<xsl:call-template name="putimage">
					<xsl:with-param name="url" select="$url"/>
					<xsl:with-param name="titolo" select="$titolo"/>
					<xsl:with-param name="link" select="$link"/>
				</xsl:call-template>
			</xsl:if>
			
			<!-- CONTENUTO SE C'E' L'IMMAGINE -->
			
			<xsl:if test="$url != 'images/contentimg/no_image'">
				<div class="box_contenuto">
				
					<xsl:apply-templates select="titolo2"/>
							
					<div class="box_testo">
						<xsl:call-template name="acquista">
							<xsl:with-param name="testo"><xsl:value-of select="testo2/text()"/></xsl:with-param>
							<xsl:with-param name="cid"><xsl:value-of select="$cid"/></xsl:with-param>
								<xsl:with-param name="unit">2</xsl:with-param>
						</xsl:call-template>
					</div>
				</div>
			</xsl:if>
			
			<!--  CONTENUTO SE NON C'E' L'IMMAGINE -->
			
			<xsl:if test="$url = 'images/contentimg/no_image'">
				<xsl:apply-templates select="titolo2"/>
			
				<div class="box_testo">
					<xsl:call-template name="acquista">
						<xsl:with-param name="testo"><xsl:value-of select="testo2/text()"/></xsl:with-param>
						<xsl:with-param name="cid"><xsl:value-of select="$cid"/></xsl:with-param>
						<xsl:with-param name="unit">2</xsl:with-param>
					</xsl:call-template>
				</div>
			</xsl:if>
				
		</div>
	</xsl:template>
	
	<xsl:template name="acquista">
		<xsl:param name="testo" />
		<xsl:param name="cid" />
		<xsl:param name="unit" />
		<xsl:choose>
			<xsl:when test="contains($testo,'__acquista__')">
				<xsl:value-of select="substring-before($testo,'__acquista__')" />
				&#160;([orderAddItem?cid=<xsl:value-of select="$cid"/>&amp;unit=<xsl:value-of select="$unit" />&amp;datadir=/siti/eut_components>Acquista])
				<xsl:value-of select="substring-after($testo,'__acquista__')"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$testo" />
			</xsl:otherwise>
		</xsl:choose>
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