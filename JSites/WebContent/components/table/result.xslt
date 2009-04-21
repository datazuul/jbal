<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                              xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:param name="pid" />
	<xsl:param name="cid" />
	<xsl:param name="pacid" />
	<xsl:param name="state" />
	
	<xsl:template match="/">
	
		<xsl:variable name="state"><xsl:value-of select="$state"/></xsl:variable>
		<xsl:if test="$state = 'WRK'">
			<c:include src="cocoon://components/table/view?cid={$cid}&amp;pid={$pid}&amp;time=strafuturo&amp;editing=true"/>	
		</xsl:if>
		<xsl:if test="$state = 'PND'">
			<c:include src="cocoon://components/table/view?cid={$cid}&amp;pid={$pid}&amp;time=futuro&amp;editing=true"/>	
		</xsl:if>
		
	<!--<div>
			<xsl:apply-templates/>
		</div>-->
	</xsl:template>
	
	<xsl:template match="//sourceResult/execution/text()">
		<strong><xsl:copy/></strong>
		<br />
		<input type="button" value="Aggiorna" onClick="location.href='pageview?pid={$pid}'" />
	</xsl:template>
	
	<xsl:template match="text()"/>
	
</xsl:stylesheet>