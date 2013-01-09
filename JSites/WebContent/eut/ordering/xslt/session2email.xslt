<?xml version="1.0" encoding="UTF-8"?>
<!-- xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:cinclude="http://apache.org/cocoon/include/1.0"-->
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:email="http://apache.org/cocoon/transformation/sendmail"
								xmlns:session="http://apache.org/cocoon/session/1.0"
								xmlns:h="http://apache.org/cocoon/request/2.0">
	
	<xsl:template match="content">
		<content>
		<email:sendmail>		
			<email:from>
				<xsl:value-of select="customerInfo/email"/>
			</email:from>
			<email:to>mrossi@units.it</email:to>
			<email:to><xsl:value-of select="customerInfo/email"/></email:to>
			<email:to>trampus@units.it</email:to>
			<email:to>cortese@units.it</email:to>
			<email:smtphost>mail.units.it</email:smtphost>
			<email:subject>[ORDINE-EUT] Ordine da <xsl:value-of select="customerInfo/nome"/>&#160;<xsl:value-of select="customerInfo/cognome"/></email:subject>
			<email:body>

				<b>E' stato ricevuto un ordine.</b><br/><br/>
								
				<xsl:apply-templates select="ordine"/>
				
				
							        <b>Totale:</b>&#160;
							        	<xsl:variable name="subtotal">
							        		<xsl:value-of select="round(sum(ordine/prezzoXquant)*100) div 100"/>
							        	</xsl:variable>
							        	<xsl:value-of select="$subtotal"/>
							        	<xsl:choose>
							        		<xsl:when test="(floor($subtotal*100) mod 10) = 0">
						        				<xsl:if test="(floor($subtotal*10) mod 10) = 0">.0</xsl:if>0
							        		</xsl:when>
					            		</xsl:choose>
							<xsl:apply-templates select="customerInfo"/>
				
	
			</email:body>
			 

				
		</email:sendmail>
		</content>
	</xsl:template>
	
	<xsl:template match="ordine">
		<br/><br/><b>ID: </b><xsl:value-of select="@id"/><br/>	
	        <b>Titolo:</b> <xsl:value-of select="titolo"/><br/>
	        <b>Autore:</b> <xsl:value-of select="autore"/><br/>
	        <b>ISBN:</b> <xsl:value-of select="isbn"/><br/>
	        <b>QT: </b><xsl:value-of select="quant"/><br/>
	        <b>Prezzo: </b><xsl:variable name="prezzo">
	      		<xsl:value-of select="prezzo"/>
	     	</xsl:variable>
	     	<xsl:value-of select="$prezzo"/>
	       		<xsl:choose>
	      		<xsl:when test="(floor($prezzo*100) mod 10) = 0">
	     				<xsl:if test="(floor($prezzo*10) mod 10) = 0">.0</xsl:if>0
	      		</xsl:when>
	        		</xsl:choose>
	        		&#8364;
		
		<!-- email:attachment name="{img}.jpg" mime-type="image/jpg" url="http://eut.units.it/images/copertine/eut/{img}.jpg"/-->
		<br/>
	</xsl:template>
	
	<xsl:template match="customerInfo">
		<br/><br/><br/><b>Dati di fatturazione:</b><br/><br/>
		<b>Nome:</b>&#160; 
		<xsl:value-of select="nome"/>&#160;<xsl:value-of select="cognome"/><br/>
		<b>Indirizzo: Via/Piazza </b><xsl:value-of select="indirizzo/via"/>&#160;<xsl:value-of select="indirizzo/numercoCivico"/><br/>
		<xsl:value-of select="indirizzo/comune"/>, <xsl:value-of select="indirizzo/provincia"/> - <xsl:value-of select="indirizzo/CAP"/>
		<br/><b>Telefono:&#160;</b>
		<xsl:value-of select="telefono"/><br/>
		<b>Email:</b>&#160;
		<xsl:value-of select="email"/><br/>
		<b>Cod. Fis. o P.I.: </b><xsl:value-of select="cfopi"/><br/>
		<b>Data di nascita: </b><xsl:value-of select="datanascita"/><br/>
		<b>Luogo di nascita: </b><xsl:value-of select="luogonascita"/><br/>
	</xsl:template>
	
</xsl:stylesheet>
		
