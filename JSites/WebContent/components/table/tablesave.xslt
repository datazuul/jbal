<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0">
<xsl:param name="datadir"/>
<xsl:template match="/save">
	<source:write>
    <source:source><xsl:value-of select="$datadir" />/data/table<xsl:copy-of select="cid/text()"/>.xml
    </source:source>
    <source:fragment>
		<table>
			<title><xsl:value-of select="title/text()"/></title>
			<!-- description><xsl:value-of select="summuary/text()"/></description-->
			<rows><xsl:value-of select="rows/text()" /></rows>
			<columns><xsl:value-of select="cols/text()" /></columns>
			<header>
				<upper>
					<xsl:call-template name="hfields">
						<xsl:with-param name="prefix">upper</xsl:with-param>
						<xsl:with-param name="element">1</xsl:with-param>
					</xsl:call-template>
				</upper>
				<left>
					<xsl:call-template name="hfields">
						<xsl:with-param name="prefix">left</xsl:with-param>
						<xsl:with-param name="element">1</xsl:with-param>
					</xsl:call-template>
				</left>
			</header>
			<body>
				<xsl:call-template name="rows">
					<xsl:with-param name="row">1</xsl:with-param>
				</xsl:call-template>
			</body>
		</table>
    </source:fragment>
	</source:write>
</xsl:template>

<!--  TEMPLATE PER I CAMPI HEADER -->

<xsl:template name="hfields">
    <xsl:param name="prefix" />
    <xsl:param name="element" />
    <xsl:variable name="nodi" select="*[local-name() = concat($prefix,'-',$element,'-text')]" />
	<xsl:if test="count($nodi) > 0">
		<xsl:element name="hfield">
			<xsl:attribute name="type">
				<xsl:value-of select="*[local-name() = concat($prefix,'-',$element,'-style')]/text()" />
			</xsl:attribute>
			<xsl:value-of select="*[local-name() = concat($prefix,'-',$element,'-text')]/text()" />
		</xsl:element>
		<xsl:call-template name="hfields">
			<xsl:with-param name="element"><xsl:value-of select="$element + 1" /></xsl:with-param>
			<xsl:with-param name="prefix"><xsl:value-of select="$prefix" /></xsl:with-param>
		</xsl:call-template>
	</xsl:if>
</xsl:template>

<!--  TEMPLATE PER LE RIGHE -->

<xsl:template name="rows">
	<xsl:param name="row" />
	<!-- xsl:if test="*[starts-with(local-name(),concat('field','-',$row))]/text() != ''"-->
	<xsl:if test="count(*[starts-with(local-name(),concat('field','-',$row,'-'))]) > 0">
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