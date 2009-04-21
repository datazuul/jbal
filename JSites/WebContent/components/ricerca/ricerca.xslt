<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                              
	<xsl:param name="cid"/>
	<xsl:param name="pid"/>
	<xsl:param name="pacid"/>
	
	<!-- form name="form1" method="get" action="metaSearch(1)">
                <input name="cocoon-view" type="hidden" value="pSearch" />
                <input name="C7" type="hidden" value="ANY" />
                <input name="R7" type="text" />
                <input type="submit" value="Cerca" />
            </form-->
	
	<xsl:template match="/">
		<form method="post" action="http://sol.units.it/SebinaOpac/Opac" id="cerca"> 
    		
    		<input type = "text" name="LIBERA" />
    		<input type = "hidden" name = "action" value = "search"/>
            <input type = "hidden" name = "kindOfSearch" value = "simple"/>
            <input type = "hidden" name = "filter" value = ""/>
            <input type = "hidden" name = "XSOGG|XDESD" value = ""/>
            <input type = "hidden" name = "XDDW|XCDW" value = ""/>
			<input type = "hidden" name = "|'XCATM" value = ""/>
			<input type = "hidden" name = "gruppo.x" value = ""/>
            <input type = "hidden" name = "XNADET" value = ""/>
			<input type = "hidden" name = "|'XBIBC" value = ""/>
            <input type = "hidden" name = "submit.x" value = "0"/>
            <input type = "hidden" name = "submit.y" value = "0"/>
    		
    		
    		<xsl:text>&#160;&#160;</xsl:text>
    		<!-- select name="select"> 
      			<option value="COM" selected="">Tutto</option> 
      			<option value="SBNFVG">Libri</option> 
      			<option value="SBATS">Periodici</option> 
      			<option value="SBATS">Tesi</option> 
   			 </select> 
   			 <xsl:text>&#160;&#160;</xsl:text>
   			 <select name="catalogo"> 
     			 <option value="COM" selected="">Tutte le biblioteche</option> 
     			 <option value="SBNFVG">Economia</option>
      			 <option value="SBATS">Medicina</option> 
    		</select>
    		<xsl:text>&#160;&#160;</xsl:text-->
    		<input type="submit" value="Cerca"/>
  		</form>
	</xsl:template>
		
	
	<xsl:template match="section">
		<xsl:variable name="url"><xsl:value-of select="img/text()"/></xsl:variable>
		<div class="sezione_immagine"><img src="{$url}" width="70" height="70"/></div>
		<div class="modifica">
			<input type="button" value="Modifica" onClick="location.href='pageedit?pid={$pid}&amp;cid={$cid}'"/>
		</div>
		<div class="sezione_contenuto">
			<h1 class="sezione_titolo"><xsl:value-of select="titolo/text()"/>
			</h1>
			<p class="sezione_testo"><xsl:value-of select="testo/text()"/></p>
		</div>
	
	</xsl:template>
	    
</xsl:stylesheet>