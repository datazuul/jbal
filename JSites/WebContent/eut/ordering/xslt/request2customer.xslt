<?xml version="1.0" encoding="UTF-8"?>
<!-- xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:cinclude="http://apache.org/cocoon/include/1.0"-->
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:email="http://apache.org/cocoon/transformation/sendmail"
								xmlns:session="http://apache.org/cocoon/session/1.0"
								xmlns:cinclude="http://apache.org/cocoon/include/1.0"
								xmlns:h="http://apache.org/cocoon/request/2.0">
	
	
	<xsl:template match="h:request">
		<content>
			<session:createcontext name="orderContext"/>			<!--  creo il contesto -->
			<xsl:apply-templates select="h:requestParameters" />	<!--  lavoro sui parametri -->
																		 
			<session:getxml context="orderContext" path="/carrello/"/>
		</content>
	</xsl:template>
	
	<xsl:template match="h:requestParameters">
		<session:mergexml context="orderContext" path="/carrello/">
			<customerInfo>
				<nome><xsl:value-of select="./h:parameter[@name='nome']"/></nome>
				<cognome><xsl:value-of select="./h:parameter[@name='cognome']"/></cognome>
				<indirizzo>
					<via><xsl:value-of select="./h:parameter[@name='via']"/></via>
					<numercoCivico><xsl:value-of select="./h:parameter[@name='numero_civico']"/></numercoCivico>
					<provincia><xsl:value-of select="./h:parameter[@name='provincia']"/></provincia>
					<comune><xsl:value-of select="./h:parameter[@name='comune']"/></comune>
					<CAP><xsl:value-of select="./h:parameter[@name='cap']"/></CAP>
				</indirizzo>
				<email><xsl:value-of select="./h:parameter[@name='email']"/></email>
				<telefono><xsl:value-of select="./h:parameter[@name='telefono']"/></telefono>
				<cfopi><xsl:value-of select="./h:parameter[@name='cfopi']"/></cfopi>
				<datanascita><xsl:value-of select="./h:parameter[@name='datanascita']"/></datanascita>
				<luogonascita><xsl:value-of select="./h:parameter[@name='luogonascita']"/></luogonascita>
			</customerInfo>
		</session:mergexml>
	</xsl:template>
	
	
</xsl:stylesheet>
