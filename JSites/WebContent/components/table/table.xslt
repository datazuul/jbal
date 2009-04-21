<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:include href="../../stylesheets/xslt/editlinks.xslt" />
	<!-- Questo include serve pei link de modifica, elimina e (dis)attiva 
		 ma anca per le combo de ordinamento  -->
    

	<xsl:param name="cid" />
	<xsl:param name="pid" />
	<xsl:param name="editing" />
	<xsl:param name="validating" />
	<xsl:param name="disabling" />
	<xsl:param name="time" />

	<!--  TABELLA PRINCIPALE -->
	<xsl:template match="/table">

		<xsl:variable name="summuary">
			<xsl:value-of select="description/text()" />
		</xsl:variable>

		<div class="{$time}">
			<table class="tabella_testo" summary="{$summuary}">

				<!--  TITOLO -->
				<caption class="tabella_titolo">
					<xsl:value-of select="title/text()" />
				</caption>

				<!--  EVIDENZIAZIONI -->
				<colgroup>
					<col />
					<xsl:for-each select="header/upper/hfield">
						<xsl:element name="col">
							<xsl:attribute name="class">
								<xsl:value-of select="@type" />
							</xsl:attribute>
						</xsl:element>
					</xsl:for-each>
				</colgroup>

				<!--  TUTTO IL RESTO -->

				<xsl:apply-templates select="header" />
				<xsl:apply-templates select="body" />
			
				
			</table>
			<xsl:apply-templates select="order" />
			
		</div>
		
		
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
	</xsl:template>

	<!--  HEADER SOPRA -->
	<xsl:template match="header">
		<tr class="tabella_voci">
			<th class="tabella_voci_sx" scope="col">
				<xsl:value-of select="shared/hfield/text()" />
			</th>
			<xsl:apply-templates select="upper/hfield" />
		</tr>
	</xsl:template>

	<!--  CORPO CON HEADER A FIANCO -->
	<xsl:template match="body">
		<xsl:for-each select="record">
			<xsl:variable name="position">
				<xsl:value-of select="position()" />
			</xsl:variable>
			<xsl:variable name="style">
				<xsl:value-of
					select="/table/header/left/hfield[number($position)]/@type" />
			</xsl:variable>
			<tr class="{$style}">
				<th scope="row">
					<xsl:value-of
						select="/table/header/left/hfield[number($position)]/text()" />
				</th>
				<xsl:for-each select="/table/header/upper/hfield">
					<xsl:variable name="position2">
						<xsl:value-of select="position()" />
					</xsl:variable>
					<td class="tabella_testo" align="center">
						<xsl:value-of
							select="/table/body/record[number($position)]/field[number($position2)]/text()" />
					</td>
				</xsl:for-each>
			</tr>
		</xsl:for-each>
	</xsl:template>

	<!--  UN ELEMENTO HEADER -->
	<xsl:template match="hfield">
		<th>
			<xsl:value-of select="text()" />
		</th>
	</xsl:template>

	<xsl:template match="text()" />

</xsl:stylesheet>