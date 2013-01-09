<?xml version="1.0" encoding="UTF-8"?>
<!-- xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:cinclude="http://apache.org/cocoon/include/1.0"-->
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:email="http://apache.org/cocoon/transformation/sendmail"
								xmlns:session="http://apache.org/cocoon/session/1.0"
								xmlns:cinclude="http://apache.org/cocoon/include/1.0"
								xmlns:h="http://apache.org/cocoon/request/2.0">
	
	
	<xsl:template match="content">
		<content>
			<session:createcontext name="orderContext"/>			<!--  creo il contesto -->
			<xsl:apply-templates select="h:request/h:requestParameters" />	<!--  lavoro sui parametri -->
			<!--<result>												  guardiamo cos'è successo nel contesto 
				<session:getxml context="orderContext" path="/carrello/"/>
			</result>-->
		</content>
	</xsl:template>
	
	<xsl:template match="h:requestParameters">
		<xsl:variable name="id"><xsl:value-of select="./h:parameter[@name='id']"/></xsl:variable>	<!-- preparo id di JOpac -->
		<xsl:variable name="quant"><xsl:value-of select="./h:parameter[@name='quant']"/></xsl:variable>
		<xsl:variable name="cid"><xsl:value-of select="./h:parameter[@name='cid']"/></xsl:variable>
		<xsl:variable name="unit"><xsl:value-of select="./h:parameter[@name='unit']"/></xsl:variable>
		<xsl:variable name="datadir"><xsl:value-of select="./h:parameter[@name='datadir']"/></xsl:variable>
		<xsl:variable name="rid"><xsl:value-of select="$id"/><xsl:value-of select="$cid"/>-<xsl:value-of select="$unit"/></xsl:variable> <!-- usi $rid nella session per identificare le righe -->

		<!-- se viene dalla ricerca $quant = '', altrimenti ha la quantita' desiderata -->
		<!-- se l'item deve essere cancellato allora si ha quant = 0 -->
		<xsl:if test="$id != '' or $cid != ''">
			<xsl:choose>
				<xsl:when test="$quant = 0">										<!--  se la quantità richiesta è 0 -->
					<!--  rimuovo il nodo /carrello/ordine con il rid specificato -->
					
					<session:removexml context="orderContext" path="/carrello/ordine[@rid='{$rid}']/"/>
					<!-- msg>Il libro <xsl:value-of select="./h:parameter[@name='titolo']"/> di
					<xsl:value-of select="./h:parameter[@name='autore']"/> &#232; stato rimosso dal carrello.</msg-->	<!--  stampo un messaggio -->
				</xsl:when>
				<xsl:otherwise>																<!--  altrimenti se la quantità è diversa da 0 -->
					<xsl:variable name="currentQuant"><xsl:value-of select="//currentQuant"/></xsl:variable>
					<xsl:value-of select="$currentQuant"/>
					<session:mergexml context="orderContext" path="/carrello/">				<!--  faccio il MERGE -->
						<xsl:choose>																	
							<xsl:when test="$quant=''">
								<xsl:if test="$currentQuant = ''">
									<cinclude:include src="cocoon:/OrderInfoItem?id={$id}&amp;quant=1" />
								</xsl:if>
							</xsl:when>
							<xsl:otherwise>
								<cinclude:include src="cocoon:/OrderInfoItem?id={$id}&amp;quant={$quant}" />
							</xsl:otherwise>
						</xsl:choose>
					</session:mergexml>
					<!-- msg>Il libro <xsl:value-of select="./h:parameter[@name='titolo']"/> di
					<xsl:value-of select="./h:parameter[@name='autore']"/> &#232; stato aggiunto al carrello.</msg--> <!--  stampo un messaggio -->
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		<!-- form action="post" method="SendOrderEmail"-->
		<session:getxml context="orderContext" path="/carrello/"/>
		<!-- /form-->

		<refurl><xsl:value-of select="./h:parameter[@name='refurl']"/></refurl>						<!-- mi passo l'url dal quale sono partito -->
	</xsl:template>
	
	
</xsl:stylesheet>
		