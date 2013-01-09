<?xml version="1.0" encoding="UTF-8"?>
<!-- xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:cinclude="http://apache.org/cocoon/include/1.0"-->
<xsl:stylesheet version="1.1" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:session="http://apache.org/cocoon/session/1.0"
								xmlns:email="http://apache.org/cocoon/transformation/sendmail">
								
	<xsl:param name="viewForm" />
	<xsl:param name="showCustomer" />
	
	<xsl:template match="content">
		<content>
			<xsl:apply-templates/>
		</content>
	</xsl:template>
	
	<xsl:template match="email:result">
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="email:success">
		<session:createcontext name="orderContext"/>
		<session:removexml context="orderContext" path="/carrello/"/>
		<div class="sezione">
			<h1 class="sezione_titolo">Il Suo ordine &#232; stato inoltrato</h1>
			<div class="sezione_testo">
				Gentile cliente,<br/>
				La informiamo che il suo ordine &#232; stato correttamente inoltrato
				al nostro sistema di gestione ordini.
			</div>
			<div class="clearer">&#160;</div>
		</div>
	</xsl:template>
	
	<xsl:template match="email:exception">
		<div class="sezione">
			<h1 class="sezione_titolo">Si &#232; verificato un errore nei dati</h1>
			<div class="sezione_testo">
				Gentile cliente,<br/>
				i dati da Lei inseriti non sono validi. Il nostro sistema ha restituito
				il seguente messaggio d'errore:<br/>
				<strong><xsl:value-of select="email:message"/></strong>
			</div>
			<div class="clearer">&#160;</div>
		</div>
	</xsl:template>
	
	<xsl:template match="email:failure">
		<div class="sezione">
			<h1 class="sezione_titolo">Si è verificato un errore nella spedizione</h1>
			<div class="sezione_testo">
				Gentile cliente,<br/>
				Il nostro sistema non è stato in grado di inoltrare la Sua richiesta a causa
				del seguente errore:<br/>
				<strong><xsl:value-of select="."/></strong>
			</div>
			<div class="clearer">&#160;</div>
		</div>
	</xsl:template>
	
	
	
</xsl:stylesheet>
		
