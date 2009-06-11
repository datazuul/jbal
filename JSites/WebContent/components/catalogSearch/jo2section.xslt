<?xml version="1.0" encoding="UTF-8"?>
        
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<!-- xsl:variable name="requestQuery" select="/root/queryData/requestQuery"/ -->
   
   <xsl:template match="cookies" />
   <xsl:template match="catalogName" />
   <xsl:template match="prevPage" />
   <xsl:template match="nextPage" />
   
   <xsl:template match="queryData">
      <DIV class="queryData">
        <xsl:value-of select="substring-after(queryCount,')')" /><xsl:text> </xsl:text>record trovati in 
        <xsl:value-of select="queryTime" /><xsl:text> </xsl:text>secondi.
      </DIV>
   </xsl:template>
   
   <xsl:template match="searchData">
      <DIV class="searchData">
        <xsl:apply-templates select="item"/>
      </DIV>
   </xsl:template>
	<xsl:template match="resultSet">
		<xsl:if test="count(record)!=0">
			<xsl:if test="contains(record[1],'tbody')">
				<xsl:value-of disable-output-escaping="yes"
							select="substring-before(record[1],'&lt;tbody&gt;')" />
			</xsl:if>

			<xsl:for-each select="record">
				<xsl:value-of disable-output-escaping="yes"
					select="substring-before(substring-after(text(),'&lt;tbody&gt;'),'&lt;/tbody&gt;')" />
			</xsl:for-each>
			
			<xsl:if test="contains(record[1],'tbody')">
				<xsl:value-of disable-output-escaping="yes"
							select="substring-after(record[1],'&lt;/tbody&gt;')" />
			</xsl:if>

		</xsl:if>
	</xsl:template>
 
</xsl:stylesheet>
