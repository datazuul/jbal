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
	<xsl:param name="container" />
	
	<xsl:template match="/excels">
		

		<div class="{$time}">
			<xsl:apply-templates select="excel" />
			<xsl:apply-templates select="order" />		
		</div>
		
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
	</xsl:template>

	<!--  TABELLA PRINCIPALE -->
	<xsl:template match="excel">

		<xsl:variable name="summuary">
			<xsl:value-of select="description/text()" />
		</xsl:variable>
		
		<table  summary="{$summuary}">

			<!--  TITOLO -->
			<caption >
				<xsl:value-of select="title/text()" />
			</caption>

			<!--  EVIDENZIAZIONI -->
			<!-- colgroup>
				<col />
				<xsl:for-each select="header/upper/hfield">
					<xsl:element name="col">
						<xsl:attribute name="class">
							<xsl:value-of select="@type" />
						</xsl:attribute>
					</xsl:element>
				</xsl:for-each>
			</colgroup-->

			<!--  TUTTO IL RESTO -->


			<xsl:apply-templates select="body" />
		</table>
		<br/>
			
	</xsl:template>



	<!--  CORPO CON HEADER A FIANCO -->
	<xsl:template match="body">
		<xsl:apply-templates select="record" />
	
	</xsl:template>
	
	<xsl:template match="record">
		<tr>
			<xsl:apply-templates select="field" />
		</tr>
	</xsl:template>
	
	<xsl:template match="field">
		<td class="tabella_testo" align="center">
			<xsl:value-of select="."/>
		</td>
	</xsl:template>

	<!--  UN ELEMENTO HEADER -->
	<xsl:template match="hfield">
		<th>
			<xsl:value-of select="text()" />
		</th>
	</xsl:template>

	<xsl:template match="text()" />

</xsl:stylesheet>