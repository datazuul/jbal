<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:include href="../../stylesheets/xslt/editlinks.xslt" />
    <!-- Questo include serve pei link de modifica, elimina e (dis)attiva 
		 ma anca per le combo de ordinamento  -->
		 
		 <!-- TODO: 
		 Romano dice:
		 
		 questo xslt è un work-around per gli ordini eut. Iztok ha scritto
		 gli ordini assumendo che i dati arrivano da JOpac2 in xml (struttura 
		 nativa dei dati) perché in quel momento di pensava che fosse sempre
		 così. Ora li prendiamo ANCHE direttamente da una parte di una 
		 componente box.
		 
		 Bisognerà creare un'interfaccia dei dati dell'ordine, fare una view
		 da jopac2 che sia aderente all'interfaccia e qui fare la stessa cosa
		 (non adattare la parte del box alla strutura di jopac2, che è una cosa
		 senza senso).
		 
		 Se si definisce l'interfaccia degli ordini allora si possono usare anche 
		 con altre sorgenti di dati.
		 
		  -->
                              
	<xsl:param name="cid"/>
	<xsl:param name="unit"/>
	<xsl:param name="pid"/>
	<xsl:param name="pacid"/>
	<xsl:param name="editing"/>
	<xsl:param name="lang"/>
    <xsl:param name="validating"/>
    <xsl:param name="disabling"/>
    <xsl:param name="time"/>
    <xsl:param name="datadir"/>
    <xsl:param name="quant"/>
    
    <xsl:template match="/">
	    <xsl:apply-templates select="box"/>
	    <!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
    </xsl:template>
    
	<xsl:template match="box">
		<xsl:choose>
			<xsl:when test="$unit = '1'"><xsl:apply-templates select="unit1"/></xsl:when>
			<xsl:when test="$unit = '2'"><xsl:apply-templates select="unit2"/></xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="unit1"/>
				<xsl:apply-templates select="unit2"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- 
	<ordine id="{id}">
			<xsl:variable name="prezzo"><xsl:value-of select="signatures/signature/libraryId"/></xsl:variable>
			
			<titolo><xsl:value-of select="ISBD"/></titolo>
			<autore><xsl:value-of select="authors/author"/></autore>
			<isbn><xsl:value-of select="standardNumber"/></isbn>
			<prezzo><xsl:value-of select="$prezzo"/></prezzo>
			<img><xsl:value-of select="signatures/signature/localization"/></img>
			<quant><xsl:value-of select="$quant"/></quant>
			<prezzoXquant><xsl:value-of select="($prezzo * $quant)"/></prezzoXquant>
			
		</ordine>
	 -->
	
	<xsl:template match="unit1">
		<ordine cid="{$cid}" unit="{$unit}" datadir="{$datadir}" rid="{$cid}-{$unit}">
			<xsl:variable name="prezzo">
				<xsl:call-template name="trova">
					<xsl:with-param name="testo" select="testo1" />
					<xsl:with-param name="riga">__prezzo__</xsl:with-param>
				</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="prezzod">
				<xsl:call-template name="dot">
					<xsl:with-param name="testo" select="$prezzo" />
				</xsl:call-template>
			</xsl:variable>
			
			<titolo><xsl:value-of select="titolo1"/></titolo>
			<autore>
				<xsl:call-template name="trova">
					<xsl:with-param name="testo" select="testo1" />
					<xsl:with-param name="riga">__autore__</xsl:with-param>
				</xsl:call-template>
			</autore>
			<isbn>
				<xsl:call-template name="trova">
					<xsl:with-param name="testo" select="testo1" />
					<xsl:with-param name="riga">__isbn__</xsl:with-param>
				</xsl:call-template>
			</isbn>
			<prezzo><xsl:value-of select="$prezzod"/></prezzo>
			<img><xsl:value-of select="img1"/></img>
			<quant><xsl:value-of select="$quant"/></quant>
			<prezzoXquant><xsl:value-of select="$prezzod * $quant"/></prezzoXquant>
		</ordine>
	</xsl:template>
	
	
	<xsl:template match="unit2">
		<ordine cid="{$cid}" unit="{$unit}" datadir="{$datadir}" rid="{$cid}-{$unit}">
			<xsl:variable name="prezzo">
				<xsl:call-template name="trova">
					<xsl:with-param name="testo" select="testo2" />
					<xsl:with-param name="riga">__prezzo__</xsl:with-param>
				</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="prezzod">
				<xsl:call-template name="dot">
					<xsl:with-param name="testo" select="$prezzo" />
				</xsl:call-template>
			</xsl:variable>
			
			<titolo><xsl:value-of select="titolo2"/></titolo>
			<autore>
				<xsl:call-template name="trova">
					<xsl:with-param name="testo" select="testo2" />
					<xsl:with-param name="riga">__autore__</xsl:with-param>
				</xsl:call-template>
			</autore>
			<isbn>
				<xsl:call-template name="trova">
					<xsl:with-param name="testo" select="testo2" />
					<xsl:with-param name="riga">__isbn__</xsl:with-param>
				</xsl:call-template>
			</isbn>
			<prezzo><xsl:value-of select="$prezzod"/></prezzo>
			<img><xsl:value-of select="img2"/></img>
			<quant><xsl:value-of select="$quant"/></quant>
			<prezzoXquant><xsl:value-of select="$prezzod * $quant"/></prezzoXquant>
		</ordine>
	</xsl:template>
	
	<xsl:template name="dot">
		<xsl:param name="testo" />
		<xsl:choose>
			<xsl:when test="contains($testo,',')">
				<xsl:value-of select="substring-before($testo,',')" />.<xsl:value-of select="substring-after($testo,',')"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$testo" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="trova">
		<xsl:param name="testo" />
		<xsl:param name="riga" />
		<xsl:variable name="fine">__</xsl:variable>
		<xsl:variable name="finef">([</xsl:variable>
		<xsl:variable name="euro">&#x20AC;</xsl:variable>
		<xsl:variable name="r" select="substring-after($testo,$riga)" />
		
		<xsl:choose>
			<xsl:when test="contains($r,$fine)">
				<xsl:variable name="parte" select="substring-before($r,$fine)" />
				<xsl:choose>
					<xsl:when test="contains($parte,$euro)">
						<xsl:variable name="partep" select="substring-after($parte,$euro)" />
						<xsl:value-of select="$partep" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$parte" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>

			<xsl:otherwise>
				<xsl:variable name="parte" select="substring-before($r,$finef)" />
				<xsl:choose>
					<xsl:when test="contains($parte,$euro)">
						<xsl:variable name="partep" select="substring-after($parte,$euro)" />
						<xsl:value-of select="$partep" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$parte" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>