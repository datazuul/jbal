<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:include href="../../stylesheets/xslt/helpedit.xslt" />
    <!-- Questo include serve per la cornice di help  -->
    	
	<xsl:param name="cid" />
	<xsl:param name="pid" />
	<xsl:param name="rows" />
	<xsl:param name="cols" />
	<xsl:param name="redim" />
	<xsl:param name="pacid" />
	<xsl:param name="time" />

	<!--  MAGHEGGIO UN PO' COL NUMERO DI RIGHE E COLONNE DELLA TABELLA.... -->
	<xsl:template match="/">
		<xsl:choose>
			<xsl:when test="$redim = 'true'">
				<input type="hidden" name="rows" value="{$rows}" />
				<input type="hidden" name="cols" value="{$cols}" />
				<xsl:apply-templates select="excel">
					<xsl:with-param name="rownum">
						<xsl:value-of select="$rows" />
					</xsl:with-param>
					<xsl:with-param name="colnum">
						<xsl:value-of select="$cols" />
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="rownum">
					<xsl:value-of select="/excel/rows/text()" />
				</xsl:variable>
				<xsl:variable name="colnum">
					<xsl:value-of select="/excel/columns/text()" />
				</xsl:variable>
				<input type="hidden" name="rows" value="{$rownum}" />
				<input type="hidden" name="cols" value="{$colnum}" />
				<xsl:apply-templates select="excel">
					<xsl:with-param name="rownum">
						<xsl:value-of select="$rownum" />
					</xsl:with-param>
					<xsl:with-param name="colnum">
						<xsl:value-of select="$colnum" />
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
		
		<input type="checkbox" name="clean"/> Cancella righe vuote
		
		<div class="help">
		Puoi caricare una tabella da un file XLS.<br/>
		<b>In questo caso i dati sovrastanti VERRANNO CANCELLATI</b><br/>
		<input type="file" name="upload-file" /> <br />
		</div>
		
		<xsl:call-template name="callhelp" />
	</xsl:template>


	<!--  CORPO DELLA TABELLA -->

	<xsl:template match="text()" />

</xsl:stylesheet>