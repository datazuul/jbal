<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml">

	<xsl:template name="centracon-footer">
		<div class="cleared"></div>
		<div class="art-footer">
			<div class="art-footer-t"></div>
			<div class="art-footer-l"></div>
			<div class="art-footer-b"></div>
			<div class="art-footer-r"></div>
			<div class="art-footer-body">
				<!--  <a href="#" class="art-rss-tag-icon" title="RSS">&#160;</a> -->
				<div class="art-footer-text">
					<xsl:copy-of select="*" />
					
				</div>
				<div class="cleared"></div>
			</div>
			<div class="cleared"></div>
		</div>
	
		
	</xsl:template>
	
	
	<xsl:template name="units-footer">
		<div class="cleared"></div>

		<div class="cleared"></div>
		<p class="art-page-footer">Piazzale Europa,1 34127 Trieste, Italia - Tel. +39
			040.558.7111 - P.IVA 00211830328 - C.F. 80013890324 - P.E.C.:
			ateneo@pec.units.it </p>
		<div class="cleared"></div>
	</xsl:template>
</xsl:stylesheet>