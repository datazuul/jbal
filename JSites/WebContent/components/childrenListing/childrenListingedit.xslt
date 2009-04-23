<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:include href="../../stylesheets/xslt/helpedit.xslt" />
    <!-- Questo include serve per la cornice di help  -->

  <xsl:param name="cid"/>
  <xsl:param name="time"/>
  <xsl:param name="pid"/>
  
  <xsl:template match="/">
    	<xsl:apply-templates select="childrenListing"/>
    	<xsl:call-template name="callhelp" />
  </xsl:template>
  
  <!--  SEZIONE -->
  
  <xsl:template match="childrenListing">
	  <div class="{$time}">
		 <div class="sezione">  
		    <div class="sezione_immagine"><xsl:apply-templates select="img"/></div>
		    <div class="sezione_contenuto">
		    	<xsl:apply-templates select="listParent"/>
			    <xsl:apply-templates select="testo"/>
		    </div>
		 </div>
	  </div>
  </xsl:template>
  
  <!--  TITOLO -->
  
  <xsl:template match="listParent">
  		Parent ID o Nome:<br/>
  		<input type="text" size="20" name="listParent" value="{text()}"/><br/>
  </xsl:template>
  	
  <!--  IMMAGINE (PIPELINE INSERIMENTO IMMAGINE CHIAMATA CON CINCLUDE) -->
  	
  <xsl:template match="img">
  		<xsl:variable name="url"><xsl:value-of select="text()"/></xsl:variable>
  		<c:include src="cocoon://imageeditcomponent?id=prevImage&amp;currentimage={$url}&amp;previewwidth=70&amp;previewheight=70&amp;minwidth=70&amp;minheight=70&amp;maxwidth=150&amp;maxheight=150" />
  		<!-- br />
  		<input type="button" value="Inserisci Nuova" onClick="location.href='upload/pid{$pid}'"/-->
  </xsl:template>
  
  <!--  TESTO -->
  
  <xsl:template match="testo">
	    Descrizione:<br/>
  		<textarea name="text" rows="3" cols="100%"><xsl:value-of select="text()"/></textarea>
  		<br />
  </xsl:template>
    
  </xsl:stylesheet>