<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:param name="cid"/>
  
  <xsl:template match="/">
  	<div class="sezione">
    	<xsl:apply-templates select="data/section"/>
    </div>
  </xsl:template>
  
  <xsl:template match="data/section">
	    <div class="sezione_immagine"><xsl:apply-templates select="img"/></div>
	    <div class="sezione_contenuto">
		    <xsl:apply-templates select="titolo"/>
		    <xsl:apply-templates select="testo"/>
	    </div>
  </xsl:template>
  
  <xsl:template match="titolo">
  		<input type="text" name="title" value="{text()}" size="100">Titolo</input>
  		<br/>
  </xsl:template>
  	
  <xsl:template match="img">
  		<xsl:variable name="url"><xsl:value-of select="text()"/></xsl:variable>
  		<img id="prevImage" src="{$url}" width="70" height="70"/><br />
  		<select name="image" onChange="document.getElementById('prevImage').src = this.value;">
  			<xsl:call-template name="imagelist">
  				<xsl:with-param name = "selected" ><xsl:value-of select="$url"/></xsl:with-param>
  			</xsl:call-template>
  		</select>
  		<br />
  		<input type="button" value="Inserisci Nuova" onClick="location.href='about:blank'"/>
  </xsl:template>
  
  <xsl:template match="testo">
  		<textarea name="text" rows="5" cols="75"><xsl:value-of select="text()"/></textarea>Descrizione
  		<br />
  </xsl:template>
  
  <xsl:template name="imagelist">
  	<xsl:param name = "selected" />
  	<xsl:for-each select="/data/images/image">
  		<xsl:variable name="data"><xsl:value-of select="text()"/></xsl:variable>
  		<xsl:element name = "option">
		                   		 <xsl:attribute name = "value" ><xsl:value-of select="$data"/></xsl:attribute>
		                   		 <xsl:if test="$data = $selected"><xsl:attribute name = "selected" /></xsl:if>
		                   		 <xsl:value-of select="substring-after($data,'/')"/>
		</xsl:element>
	</xsl:for-each>
  </xsl:template>
    
  </xsl:stylesheet>