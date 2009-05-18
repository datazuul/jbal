<?xml version="1.0" encoding="UTF-8"?>
        
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns="http://www.w3.org/1999/xhtml">

<!-- xsl:variable name="requestQuery" select="/root/queryData/requestQuery"/ -->
   
   <xsl:template match="cookies" />
   <xsl:template match="catalogName" />
   <xsl:template match="prevPage" />
   <xsl:template match="nextPage" />
   
   <xsl:template match="queryData">
      <DIV class="queryData">
        <xsl:value-of select="queryCount" /><xsl:text> </xsl:text>records found in
        <xsl:value-of select="queryTime" /><xsl:text> </xsl:text>seconds.
      </DIV>
   </xsl:template>
   
   <xsl:template match="searchData">
      <DIV class="searchData">
        <xsl:apply-templates select="item"/>
      </DIV>
   </xsl:template>

   <xsl:template match="resultSet">
        <xsl:for-each select="record">
            <DIV class="sezione">
            
  				<div class="record_testo"><xsl:value-of disable-output-escaping="yes" select="text()"/></div>
            
                <div class="clearer">&#160;</div>
                <hr/>
            </DIV>
        </xsl:for-each>
   </xsl:template>
 
</xsl:stylesheet>
