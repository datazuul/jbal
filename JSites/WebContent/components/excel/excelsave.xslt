<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0">
<xsl:param name="datadir"/>
<xsl:template match="/save">
	<source:write>
    <source:source><xsl:value-of select="$datadir" />/data/excel<xsl:copy-of select="cid/text()"/>.xml
    </source:source>
    <source:fragment>
		<excels>
			<xsl:apply-templates/>
		</excels>
    </source:fragment>
	</source:write>
</xsl:template>

<xsl:template match="excel">
	<excel>
		<title><xsl:value-of select="title/text()"/></title>
		
		<rows><xsl:value-of select="rows/text()" /></rows>
		<columns><xsl:value-of select="cols/text()" /></columns>
		<body>
			<xsl:call-template name="rows">
				<xsl:with-param name="row">1</xsl:with-param>
			</xsl:call-template>
		</body>
	</excel>
</xsl:template>


<!--  TEMPLATE PER LE RIGHE -->

<xsl:template name="rows">
	<xsl:param name="row" />
	
	<xsl:if test="count(*[starts-with(local-name(),concat('field','-',$row,'-'))]) > 0">
		<xsl:value-of select="count(*[starts-with(local-name(),concat('field','-',$row,'-'))])" />
		<record>
			<xsl:call-template name="fields">
				<xsl:with-param name="row"><xsl:value-of select="$row" /></xsl:with-param>
				<xsl:with-param name="col">1</xsl:with-param>
			</xsl:call-template>
		</record>
		<xsl:call-template name="rows">
			<xsl:with-param name="row"><xsl:value-of select="$row + 1" /></xsl:with-param>
		</xsl:call-template>
	</xsl:if>
</xsl:template>

<!-- TEMPLATE FIELDS -->

<xsl:template name="fields">
	<xsl:param name="row" />
	<xsl:param name="col" />
	<xsl:variable name="nodi" select="*[local-name() = concat('field-',$row,'-',$col)]" />
	<xsl:if test="count($nodi) > 0">
		<field>
			<xsl:value-of select="*[local-name() = concat('field-',$row,'-',$col)]/text()"/>
		</field>
		<xsl:call-template name="fields">
			<xsl:with-param name="row"><xsl:value-of select="$row" /></xsl:with-param>
			<xsl:with-param name="col"><xsl:value-of select="$col + 1" /></xsl:with-param>
		</xsl:call-template>
	</xsl:if>
</xsl:template>

<xsl:template match="text()"/>

</xsl:stylesheet>