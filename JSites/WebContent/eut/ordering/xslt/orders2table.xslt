<?xml version="1.0" encoding="UTF-8"?>
<!-- xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:cinclude="http://apache.org/cocoon/include/1.0"-->
<xsl:stylesheet version="1.1" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:email="http://apache.org/cocoon/transformation/sendmail">
								
	<xsl:param name="viewForm" />
	<xsl:param name="showCustomer" />
	
	<xsl:template match="content">
		<content>
			<div class="presente">
				
				<table summary="" class="tabella_testo">
			        <caption class="tabella_titolo">Carrello</caption>
			        <!-- <colgroup>
			            <col/>
			            <col class="tabella_area"/>
			            <col class="tabella_area"/>
			            <col class="tabella_area"/>
			            <col class="tabella_area"/>
			            <col class="tabella_area"/>
			            <xsl:if test="$viewForm = 'true'">
				            <col class="tabella_area"/>
				            <col class="tabella_area"/>
			            </xsl:if>
			        </colgroup>  -->
			        <tr class="tabella_voci">
			            <!-- th scope="col" class="tabella_voci_sx">ID</th-->
			            <th>Titolo</th>
			            <th>Autore</th>
			            <th>Qt.</th>
			            <th>isbn</th>
			            <th style="width: 80px;">prezzo</th>
			            <xsl:if test="$viewForm = 'true'">
				            <th></th>
				            <th></th>
				        </xsl:if>
			        </tr>
			        
			        <xsl:apply-templates select="ordine"/>
			        
			        <tr style="font-weight: bold;">
				        <td/><td/><td/>
				        <td align="center" class="tabella_testo">Totale</td>
				        <td align="center" class="tabella_testo">
				        	<xsl:variable name="subtotal">
				        		<xsl:value-of select="round(sum(ordine/prezzoXquant)*100) div 100"/>
				        	</xsl:variable>
				        	<xsl:value-of select="$subtotal"/>
				        	<xsl:choose>
				        		<xsl:when test="(floor($subtotal*100) mod 10) = 0">
			        				<xsl:if test="(floor($subtotal*10) mod 10) = 0">.0</xsl:if>0
				        		</xsl:when>
		            		</xsl:choose>
		            		&#8364;
				        </td>
				        <xsl:if test="$viewForm = 'true'"><td/><td/></xsl:if>
			        </tr>
			        
		        </table>
		        
		        <xsl:apply-templates select="refurl"/>
		        <xsl:apply-templates select="msg"/>

	        	<br/>
	        	<xsl:apply-templates select="customerInfo"/>
	        	<hr/>
	        	<xsl:if test="ordine/prezzoXquant != 0 and customerInfo/email !='' ">
			        <div class="modifica">
			        	<a href="orderSendEmail">[ Invia Ordine ]</a>
			        </div>
			    </xsl:if>

			</div>
		</content>
		
	</xsl:template>
	
	<xsl:template match="ordine">
		
		<tr class=" ">
			<!--  <form action="orderAddItem" method="get" name="updateOrder"> -->
	            <td class="tabella_testo">
	            	<div style="float: left;">
	            		<xsl:variable name="i" select="img" />
	            		<xsl:variable name="c">contentimg</xsl:variable>
	            		<xsl:choose>
	            			<xsl:when test="contains($i,$c)">
	            				<img width="30" src="{img}" alt=""/>
	            			</xsl:when>
	            			<xsl:otherwise>
	            				<img width="30" src="images/copertine/eut/{img}.jpg" alt="{img}"/>
	            			</xsl:otherwise>
	            		</xsl:choose>
		            	
	            	</div>
	            	<div style="margin-left: 50px;">
		            	<xsl:value-of select="titolo"/>
	            	</div>
	            </td>
	            <td class="tabella_testo"><xsl:value-of select="autore"/></td>
	            <td align="center">
	            	<xsl:choose>
	            		<xsl:when test="$viewForm = 'true'">
			            	<select style="width: 40px; font-size: 7pt;" name="quant" onchange="document.getElementById('q{@id}').value=this.value">
								<option value="1"><xsl:if test="quant=1"><xsl:attribute name="selected">true</xsl:attribute></xsl:if>1</option>
								<option value="2"><xsl:if test="quant=2"><xsl:attribute name="selected">true</xsl:attribute></xsl:if>2</option>
								<option value="3"><xsl:if test="quant=3"><xsl:attribute name="selected">true</xsl:attribute></xsl:if>3</option>
								<option value="4"><xsl:if test="quant=4"><xsl:attribute name="selected">true</xsl:attribute></xsl:if>4</option>
								<option value="5"><xsl:if test="quant=5"><xsl:attribute name="selected">true</xsl:attribute></xsl:if>5</option>
								<option value="6"><xsl:if test="quant=6"><xsl:attribute name="selected">true</xsl:attribute></xsl:if>6</option>
								<option value="7"><xsl:if test="quant=7"><xsl:attribute name="selected">true</xsl:attribute></xsl:if>7</option>
								<option value="8"><xsl:if test="quant=8"><xsl:attribute name="selected">true</xsl:attribute></xsl:if>8</option>
								<option value="9"><xsl:if test="quant=9"><xsl:attribute name="selected">true</xsl:attribute></xsl:if>9</option>
								<option value="10"><xsl:if test="quant=10"><xsl:attribute name="selected">true</xsl:attribute></xsl:if>10</option>
							</select>
						</xsl:when>
						<xsl:otherwise><xsl:value-of select="quant"/></xsl:otherwise>
					</xsl:choose>
	            </td>
	            <td align="center" class="tabella_testo"><xsl:value-of select="isbn"/></td>
	            <td align="center" class="tabella_testo">
	            	<xsl:variable name="prezzo">
			        		<xsl:value-of select="prezzo"/>
		        	</xsl:variable>
		        	<xsl:value-of select="$prezzo"/>
            		<xsl:choose>
            			<xsl:when test="contains($prezzo,'.')">
            				<xsl:variable name="dc" select="substring-after($prezzo,'.')" />
            				<xsl:if test="string-length($dc) &lt; 2">0</xsl:if>
            			</xsl:when>
            			<xsl:otherwise>.00</xsl:otherwise>
		        		<!-- xsl:when test="(floor($prezzo*100) mod 10) = 0">
	        				<xsl:if test="(floor($prezzo*10) mod 10) = 0">.0</xsl:if>0
		        		</xsl:when -->
            		</xsl:choose>
            		&#8364;
	            </td>
	            <xsl:if test="$viewForm = 'true'">
		            <td align="center">
		            	<form action="orderAddItem" method="get" id="f{@id}" name="updateOrder">
		            		<input type="hidden" name="quant" value="{quant}" id="q{@id}" />
		            		<input type="hidden" name="id" value="{@id}"/>
			            	<input type="hidden" name="cid" value="{@cid}"/>
			            	<input type="hidden" name="unit" value="{@unit}"/>
			            	<input type="hidden" name="datadir" value="{@datadir}"/>
							<input type="hidden" name="refurl" value="{../refurl}"/>
		            		<input type="submit" value="Update" style="width: 45px; font-size: 7pt;"/>
		            	</form>
		            </td>
		        </xsl:if>
	            
	            <xsl:if test="$viewForm = 'true'">
	            	<td align="center">
	            		<form action="orderAddItem">        	
		            		<input type="hidden" name="quant" value="0"/>
		            		<input type="hidden" name="id" value="{@id}"/>
			            	<input type="hidden" name="cid" value="{@cid}"/>
			            	<input type="hidden" name="unit" value="{@unit}"/>
			            	<input type="hidden" name="datadir" value="{@datadir}"/>
							<input type="hidden" name="refurl" value="{../refurl}"/>
		            		<input type="submit" value="Delete" style="width: 45px; font-size: 7pt;"/>
	            		</form>
		            </td>
				</xsl:if>
	            
	        </tr>
	</xsl:template>
	
	<xsl:template match="refurl">
		<div class="modifica">
			<a href="/">[ Torna alla pagina di ricerca ]</a>&#160;
			<xsl:if test="../ordine/prezzoXquant != 0">
				<a href="pageview?pid=16">[ Gestione dati di fatturazione ]</a>
			</xsl:if>
		</div>
	</xsl:template>
	
	<xsl:template match="customerInfo">
		<strong>Dati di fatturazione:</strong><br/>
		<xsl:value-of select="nome"/>&#160;<xsl:value-of select="cognome"/><br/>
		via/piazza <xsl:value-of select="indirizzo/via"/>&#160;<xsl:value-of select="indirizzo/numercoCivico"/><br/>
		<xsl:value-of select="indirizzo/comune"/>, <xsl:value-of select="indirizzo/provincia"/> - <xsl:value-of select="indirizzo/CAP"/><br/>
		<xsl:value-of select="telefono"/><br/><xsl:value-of select="email"/>
		<br/>Cod.Fisc o P.I. <xsl:value-of select="cfopi"/>
		<br/>Data di nascita <xsl:value-of select="datanascita"/>
		<br/>Luogo di nascita <xsl:value-of select="luogonascita"/>
	</xsl:template>
	
	
	<xsl:template match="msg">
		<xsl:value-of select="." />
	</xsl:template>
	
</xsl:stylesheet>
		
