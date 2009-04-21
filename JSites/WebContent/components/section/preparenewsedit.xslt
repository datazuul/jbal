<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:param name="cid" />
	
	<xsl:template match="section">
		<section>
		<xsl:apply-templates />
		<sql:execute-query xmlns:sql="http://apache.org/cocoon/SQL/2.0">
			<sql:query>
				select * from tblnews where cid=<xsl:value-of select="$cid" />
			</sql:query>
		</sql:execute-query>
		</section>
	</xsl:template>
	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>

	

</xsl:stylesheet>