<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                              
	<xsl:param name="cid"/>
	<xsl:param name="pid"/>
	<xsl:param name="editing"/>
	<xsl:param name="validating"/>
	<xsl:param name="disabling"/>
	<xsl:param name="time"/>
	
	<xsl:template match="/">
		<div class="mappa"><xsl:apply-templates select="map"/></div>	
	</xsl:template>
	
	<!-- MAPPE -->
	<xsl:template match="map">
		<div class="{$time}">
			<xsl:variable name="map"><xsl:value-of select="image/text()"/></xsl:variable>
			
			<!--  TITOLO -->
			<div class="mappa_titolo"><xsl:value-of select="title/text()" /></div>
			
			<!--  SERVIZI -->
			<div class="servizi"><xsl:apply-templates select="services"/></div>
			
			<!--  MAPPA -->
			<div class="mappa_immagine"><img src="{$map}" width="300" height="300" alt="Mappa biblioteca"/></div>
			
			<!-- ZOOM -->
			<div class="zoom">
    			<p><img src="images/zoom.gif" alt="Mappa biblioteca" width="167" height="66" /></p>
  			</div>
			
			<!--  LEGENDA -->
			<div class="legenda"><img src="images/legenda_a.gif" alt="" width="50" height="13" /> voce
    		legenda 1 </div>
  			<div class="legenda"><img src="images/legenda_b.gif" alt="" width="50" height="13" /> voce
    		legenda 2 </div>
    		
    		<!--  DESCRIZIONE -->
			<div class="mappa_descrizione"><xsl:value-of select="description/text()" /></div>
			
			<!-- ALTRE PIPELINE -->
			<xsl:if test="$editing='true'">
				<div class="modifica">
					<input type="button" value="Modifica" onClick="location.href='pageedit?pid={$pid}&amp;cid={$cid}'"/>
				</div>
			</xsl:if>
	        <xsl:if test="$validating='true'">
				<div class="modifica">
					<input type="button" value="Attiva" onClick="location.href='pageactivate?pid={$pid}&amp;cid={$cid}'"/>
				</div>
			</xsl:if>
			<xsl:if test="$disabling='true'">
				<div class="modifica">
					<input type="button" value="Disattiva" onClick="location.href='pagedisable?pid={$pid}&amp;cid={$cid}'"/>
				</div>
			</xsl:if>
			<xsl:apply-templates select="order" />
		</div>
	</xsl:template>
	
	<!-- SERVIZI -->
	<xsl:template match="services">
		<xsl:apply-templates select="entry"/>
	</xsl:template>
	
	<!--  ENTRY SERVIZI -->
	<xsl:template match="entry">
		<div class="servizi_voci">
			<xsl:variable name="imageurl"><xsl:value-of select="image/text()"/></xsl:variable>
			<xsl:variable name="text"><xsl:value-of select="text/text()"/></xsl:variable>
			<img class="servizi_immagine" src="{$imageurl}" alt="{$text}" width="26" height="21"/>
			<xsl:value-of select="text/text()"/>
		</div>
	</xsl:template>
	
	<!-- PIPELINE DI ORDINAMENTO -->
	<xsl:template match="order">
	
		<div class="modifica">
			<select name="cid{$cid}">
			<xsl:call-template name="iterate">
				<xsl:with-param name = "iterateleft" ><xsl:value-of select="elementnumber/text()"/></xsl:with-param>
			</xsl:call-template>
			</select>
		</div>
	
	</xsl:template>
	
	<xsl:template name="iterate">
		<xsl:param name = "iterateleft" />
		<xsl:variable name="order"><xsl:value-of select="selected/text()"/></xsl:variable>
		<xsl:variable name="top"><xsl:value-of select="elementnumber/text()"/></xsl:variable>
			
		<xsl:element name="option">
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
	</xsl:template>
	    
</xsl:stylesheet>