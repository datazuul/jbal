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
				<xsl:apply-templates select="table">
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
					<xsl:value-of select="/table/rows/text()" />
				</xsl:variable>
				<xsl:variable name="colnum">
					<xsl:value-of select="/table/columns/text()" />
				</xsl:variable>
				<input type="hidden" name="rows" value="{$rownum}" />
				<input type="hidden" name="cols" value="{$colnum}" />
				<xsl:apply-templates select="table">
					<xsl:with-param name="rownum">
						<xsl:value-of select="$rownum" />
					</xsl:with-param>
					<xsl:with-param name="colnum">
						<xsl:value-of select="$colnum" />
					</xsl:with-param>
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
		
		<div class="help">
		Puoi caricare una tabella da un file CSV.<br/>
		<b>In questo caso i dati sovrastanti VERRANNO CANCELLATI</b><br/>
		<input type="file" name="upload-file" /> <br />
		</div>
		
		<xsl:call-template name="callhelp" />
	</xsl:template>

	<xsl:template match="table">

		<xsl:param name="rownum" />
		<xsl:param name="colnum" />

		<div class="{$time}">

			<div class="tabella">

				<!--  TABELLA -->

				<table class="tabella_testo"
					summary="{description/text()}">

					<!--  TITOLO -->
					<caption class="tabella_titolo">
						Titolo:
						<input type="text" name="title"
							value="{title/text()}" />
						<!-- br />
						Descrizione:
						<input type="text" name="summuary"
							value="{description/text()}" /-->
					</caption>

					<!--  STILI COLONNE -->

					<xsl:element name="colgroup">
						<col /><!-- colonna intestazioni... -->
						<xsl:call-template name="hfield">
							<xsl:with-param name="counter">
								1
							</xsl:with-param>
							<xsl:with-param name="colnum">
								<xsl:value-of select="$colnum" />
							</xsl:with-param>
						</xsl:call-template>
					</xsl:element>

					<!--  RIGA INTESTAZIONE -->
					<xsl:apply-templates select="header">
						<xsl:with-param name="colnum">
							<xsl:value-of select="$colnum" />
						</xsl:with-param>
						<xsl:with-param name="rownum">
							<xsl:value-of select="$rownum" />
						</xsl:with-param>
					</xsl:apply-templates>

					<!-- CORPO DELLA TABELLA -->
					<xsl:apply-templates select="body">
						<xsl:with-param name="rownum">
							<xsl:value-of select="$rownum" />
						</xsl:with-param>
						<xsl:with-param name="colnum">
							<xsl:value-of select="$colnum" />
						</xsl:with-param>
						<xsl:with-param name="currentelement">
							1
						</xsl:with-param>
					</xsl:apply-templates>

				</table>
			</div>
		</div>
	</xsl:template>

	<!-- TEMPLATE EVIDENZIAZIONE -->
	<xsl:template name="hfield">
		<xsl:param name="counter" />
		<xsl:param name="colnum" />
		<xsl:choose>
			<xsl:when
				test="header/upper/hfield[number($counter)]/@type != ''">
				<xsl:element name="col">
					<xsl:attribute name="class">
						<xsl:value-of
							select="header/upper/hfield[number($counter)]/@type" />
					</xsl:attribute>
				</xsl:element>
			</xsl:when>
			<xsl:otherwise>
				<xsl:element name="col">
					<xsl:attribute name="class">
						tabella_area
					</xsl:attribute>
				</xsl:element>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="$counter &lt; $colnum">
			<xsl:call-template name="hfield">
				<xsl:with-param name="counter">
					<xsl:value-of select="$counter + 1" />
				</xsl:with-param>
				<xsl:with-param name="colnum">
					<xsl:value-of select="$colnum" />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<!--  PRIMA RIGA DI INTESTAZIONE -->

	<xsl:template match="header">

		<xsl:param name="colnum" />
		<xsl:param name="rownum" />

		<tr class="tabella_voci">

			<!-- MODIFICA -->

			<th class="tabella_voci_sx" scope="col">
				<!-- input type="button" value="Nuova Riga"
					onClick="location.href='pageedit?pid={$pid}&amp;cid={$cid}&amp;pacid={$pacid}&amp;redim=true&amp;rows={$rownum + 1}&amp;cols={$colnum}&amp;type=table'" />
				<input type="button" value="Nuova Colonna"
					onClick="location.href='pageedit?pid={$pid}&amp;cid={$cid}&amp;pacid={$pacid}&amp;redim=true&amp;rows={$rownum}&amp;cols={$colnum + 1}&amp;type=table'" /-->
				<a href="pageedit?pid={$pid}&amp;cid={$cid}&amp;pacid={$pacid}&amp;redim=true&amp;rows={$rownum + 1}&amp;cols={$colnum}&amp;type=table">Nuova Riga</a>
				<br />
				<a href="pageedit?pid={$pid}&amp;cid={$cid}&amp;pacid={$pacid}&amp;redim=true&amp;rows={$rownum}&amp;cols={$colnum + 1}&amp;type=table">Nuova Colonna</a>				
				<br />
				<xsl:if test="$rownum > 1">
					<!-- input type="button" value="Rimuovi Riga" onClick="location.href='pageedit?pid={$pid}&amp;cid={$cid}&amp;pacid={$pacid}&amp;redim=true&amp;rows={$rownum - 1}&amp;cols={$colnum}&amp;type=table'" /-->
					<a href="pageedit?pid={$pid}&amp;cid={$cid}&amp;pacid={$pacid}&amp;redim=true&amp;rows={$rownum - 1}&amp;cols={$colnum}&amp;type=table">Rimuovi riga</a>
					<br />
				</xsl:if>
				<xsl:if test="$colnum > 1">
					<!-- input type="button" value="Rimuovi Colonna" onClick="location.href='pageedit?pid={$pid}&amp;cid={$cid}&amp;pacid={$pacid}&amp;redim=true&amp;rows={$rownum}&amp;cols={$colnum - 1}&amp;type=table'" /-->
					<a href="pageedit?pid={$pid}&amp;cid={$cid}&amp;pacid={$pacid}&amp;redim=true&amp;rows={$rownum}&amp;cols={$colnum - 1}&amp;type=table">Rimuovi colonna</a>
				</xsl:if>
				<br />
			</th>

			<xsl:call-template name="headerfield">
				<xsl:with-param name="currentelement">1</xsl:with-param>
				<xsl:with-param name="iterazioni">
					<xsl:value-of select="$colnum" />
				</xsl:with-param>
			</xsl:call-template>

		</tr>
	</xsl:template>

	<!--  ELEMENTO INTESTAZIONE -->
	<xsl:template name="headerfield">
		<xsl:param name="currentelement" />
		<xsl:param name="iterazioni" />

		<th>
			<xsl:variable name="hcamp">
				<xsl:value-of
					select="upper/hfield[number($currentelement)]/text()" />
			</xsl:variable>
			<xsl:variable name="type">
				<xsl:value-of
					select="upper/hfield[number($currentelement)]/@type" />
			</xsl:variable>
			Intestazione:<br/>
			<textarea rows="3" cols="10"
				name="{concat('upper-',$currentelement,'-text')}">
				<xsl:value-of select="$hcamp" />
			</textarea>
			<br />
			Stile:
			<select
				name="{concat('upper-',$currentelement,'-style')}">
				<xsl:element name="option">
					<xsl:attribute name="value">
						tabella_area
					</xsl:attribute>
					<xsl:if test="$type = 'tabella_area'">
						<xsl:attribute name="selected" />
					</xsl:if>
					<xsl:text>Normale</xsl:text>
				</xsl:element>
				<xsl:element name="option">
					<xsl:attribute name="value">
						tabella_area_evidenziata
					</xsl:attribute>
					<xsl:if test="$type = 'tabella_area_evidenziata'">
						<xsl:attribute name="selected" />
					</xsl:if>
					<xsl:text>Evidenziata</xsl:text>
				</xsl:element>
				<xsl:element name="option">
					<xsl:attribute name="value">
						tabella_area_oscurata
					</xsl:attribute>
					<xsl:if test="$type = 'tabella_area_oscurata'">
						<xsl:attribute name="selected" />
					</xsl:if>
					<xsl:text>Oscurata</xsl:text>
				</xsl:element>
			</select>
		</th>
		<xsl:if test="$currentelement &lt; $iterazioni">
			<xsl:call-template name="headerfield">
				<xsl:with-param name="currentelement">
					<xsl:value-of select="$currentelement + 1" />
				</xsl:with-param>
				<xsl:with-param name="iterazioni">
					<xsl:value-of select="$iterazioni" />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>

	</xsl:template>

	<!--  CORPO DELLA TABELLA -->
	<xsl:template name="addrow" match="body">

		<xsl:param name="rownum" />
		<xsl:param name="colnum" />
		<xsl:param name="currentelement" />

		<xsl:variable name="style">
			<xsl:choose>
				<xsl:when
					test="/table/header/left/hfield[number($currentelement)]/@type != ''">
					<xsl:value-of
						select="/table/header/left/hfield[number($currentelement)]/@type" />
				</xsl:when>
				<xsl:otherwise>
					<!--  VUOTO PER NON OVVERIDDARE LO STILE DELLE COLONNE -->
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr class="{$style}">
			<th scope="row">
				<xsl:variable name="hcamp">
					<xsl:value-of
						select="/table/header/left/hfield[number($currentelement)]/text()" />
				</xsl:variable>
				<xsl:variable name="type">
					<xsl:value-of
						select="/table/header/left/hfield[number($currentelement)]/@type" />
				</xsl:variable>
				Intestazione:<br />
				<textarea rows="3" cols="10" name="{concat('left-',$currentelement,'-text')}"><xsl:value-of select="$hcamp" /></textarea>
				<br />
				Stile:
				<select	name="{concat('left-',$currentelement,'-style')}">
					<xsl:element name="option">
						<!-- NOTA: LA CLASSE DELLE RIGHE OVVERIDA LE CLASSI DELLE COLONNE QUINDI SE IL STILE E NORMALE LASCIO LA RIGA SENZA CLASSE -->
						<xsl:attribute name="value"></xsl:attribute>
						<xsl:if test="$type = 'tabella_area'">
							<xsl:attribute name="selected" />
						</xsl:if>
						<xsl:text>Normale</xsl:text>
					</xsl:element>
					<xsl:element name="option">
						<xsl:attribute name="value">
							tabella_area_evidenziata
						</xsl:attribute>
						<xsl:if
							test="$type = 'tabella_area_evidenziata'">
							<xsl:attribute name="selected" />
						</xsl:if>
						<xsl:text>Evidenziata</xsl:text>
					</xsl:element>
					<xsl:element name="option">
						<xsl:attribute name="value">
							tabella_area_oscurata
						</xsl:attribute>
						<xsl:if
							test="$type = 'tabella_area_oscurata'">
							<xsl:attribute name="selected" />
						</xsl:if>
						<xsl:text>Oscurata</xsl:text>
					</xsl:element>
				</select>
			</th>
			<xsl:call-template name="field">
				<xsl:with-param name="currentelement">1</xsl:with-param>
				<xsl:with-param name="row">
					<xsl:value-of select="$currentelement" />
				</xsl:with-param>
				<xsl:with-param name="colnum">
					<xsl:value-of select="$colnum" />
				</xsl:with-param>
			</xsl:call-template>
		</tr>

		<xsl:if test="$currentelement &lt; $rownum">
			<xsl:call-template name="addrow">
				<xsl:with-param name="currentelement">
					<xsl:value-of select="$currentelement + 1" />
				</xsl:with-param>
				<xsl:with-param name="rownum">
					<xsl:value-of select="$rownum" />
				</xsl:with-param>
				<xsl:with-param name="colnum">
					<xsl:value-of select="$colnum" />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>

	</xsl:template>

	<!-- TEMPLATE PER I CAMPI DELLE TABELLE -->

	<xsl:template name="field">
		<xsl:param name="colnum" />
		<xsl:param name="row" />
		<xsl:param name="currentelement" />
		<td>
			<xsl:variable name="element">
				<xsl:value-of
					select="/table/body/record[number($row)]/field[number($currentelement)]/text()" />
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="$element != ''">
					<xsl:variable name="camp">
						<xsl:value-of select="$element" />
					</xsl:variable>
					<textarea rows="3" cols="10"
						name="field-{$row}-{$currentelement}">
					<xsl:value-of select="$camp" />
					</textarea>
				</xsl:when>
				<xsl:otherwise>
					<textarea rows="3" cols="10" type="text"
						name="field-{$row}-{$currentelement}" />
				</xsl:otherwise>
			</xsl:choose>
		</td>
		<xsl:if test="$currentelement &lt; $colnum">
			<xsl:call-template name="field">
				<xsl:with-param name="colnum">
					<xsl:value-of select="$colnum" />
				</xsl:with-param>
				<xsl:with-param name="row">
					<xsl:value-of select="$row" />
				</xsl:with-param>
				<xsl:with-param name="currentelement">
					<xsl:value-of select="$currentelement + 1" />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>

	</xsl:template>

	<xsl:template match="text()" />

</xsl:stylesheet>