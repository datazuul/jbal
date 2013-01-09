<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:param name="pid"/>
	<xsl:param name="pcode"/>
	
	<xsl:template match="managers">
		<!--  <div> -->
			<xsl:apply-templates />
		<!-- </div> -->
	</xsl:template>
	
	<xsl:template match="add" >
		<!--  <br/>
		<a href="pagecreate?papid={$pid}&amp;pid=0&amp;type=section" class="navbar1lev">[ aggiungi pagina ]</a>
		<a href="pagecreate?papid={$pid}&amp;pid=0&amp;type=externalLink" class="navbar1lev">[ aggiungi link esterno ]</a> -->
	</xsl:template>
	

	<xsl:template match="SectionManager">
		<div class="new_component">
			<form action="pageedit">
				<select name="type">
					<option value="section" selected="">sezione</option>
					<!--  <option value="box">box</option> -->
					<!--  <option value="table">tabella</option> -->
					<option value="section:news">avviso</option>
					<option value="newslist">lista avvisi</option>
					<!--  <option value="excel">foglio excel</option> -->
					<option value="gallery">foto gallery</option>
					<option value="slideshow">slideshow</option>
					<option value="catalogSearch">ricerca catalogo</option>
					<option value="sendmail">invio form per email</option>
					<option value="sitemap">mappa sito</option>
					<xsl:if test="starts-with($pcode,'_')">
						<option value="sidebar">sidebar verticale</option>
					</xsl:if>
					
				</select>
	
				<input type="hidden" name="pid">
					<xsl:attribute name="value">
						<xsl:value-of select="@pid" />
					</xsl:attribute>
				</input>
				<input type="hidden" name="pacid">
					<xsl:attribute name="value">
						<xsl:value-of select="@pacid" />
					</xsl:attribute>
				</input>
				<input type="hidden" name="cid" value="0" />
				
				<input type="hidden" name="order">
					<xsl:attribute name="value">
						<xsl:value-of select="@order" />
					</xsl:attribute>
				</input>
				<input type="submit" value="Nuovo" />
			</form>
		</div>
		
	</xsl:template>

	<xsl:template match="ContentManager">
		<!--  <span>[<a href="pageorder?pid={@pid}&amp;pacid={@pacid}">Cambia ordine</a>]</span>&#160;-&#160; -->
	</xsl:template>
	
	
	<xsl:template match="OrderManager">
			<form method="post" action="pageordersave?pid={@pid}">
				<xsl:apply-templates />
				<input class="buttonright" type="submit" value="Ordina"/>
			</form>
	</xsl:template>


	<xsl:template match="ValidationManager">
	<!--  
			<span>
			[<xsl:choose>
				<xsl:when test="@active = 'true'"><a href="pagedisable?pid={@pid}&amp;pacid={@pacid}&amp;cid=0">Disattiva questa pagina</a></xsl:when>
				<xsl:otherwise><a href="pageactivate?pid={@pid}&amp;pacid={@pacid}&amp;cid=0">Attiva questa pagina</a></xsl:otherwise>
			</xsl:choose>]&#160;-&#160;
			
			[<xsl:choose>					
				<xsl:when test="@insidebar = 'false'"><a href="pageinsidebar?pid={@pid}&amp;pacid={@pacid}&amp;cid=0&amp;insidebar=true">Visualizza la pagina nella sidebar</a></xsl:when>
				<xsl:otherwise><a href="pageinsidebar?pid={@pid}&amp;pacid={@pacid}&amp;cid=0&amp;insidebar=false">Non visualizzare la pagina nella sidebar</a></xsl:otherwise>
			</xsl:choose>]
			</span>
	-->
	</xsl:template>
	
	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>