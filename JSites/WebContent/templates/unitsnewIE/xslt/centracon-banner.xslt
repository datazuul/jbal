<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:output
		method="xml"
		omit-xml-declaration="yes"
		indent="yes"/> 
	
	<xsl:template name="centracon-banner">
		<div class="cleared">
        <!-- apertura sfondo flash + banner orizzonatale -->
         <div class="art-header">
		<!-- a href="{$context}" --><a href="/">
              <img src="./images/page1/banner.png" alt=""
								style="margin: 0 auto;display:block;width:100%" />
		</a>
         </div>
     </div>
		
	</xsl:template>
</xsl:stylesheet>
