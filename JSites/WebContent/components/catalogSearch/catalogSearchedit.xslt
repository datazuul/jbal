<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:include href="../../stylesheets/xslt/helpedit.xslt" />

	<xsl:param name="cid" />
	<xsl:param name="time" />
	<xsl:param name="pid" />

	<xsl:template match="/">
		<xsl:apply-templates select="catalogSearch" />
		<xsl:call-template name="callhelp" />
	</xsl:template>


	<xsl:template match="catalogSearch">
		<div class="{$time}">
			<div class="sezione">
				<fieldset>
					<legend>Catalogo</legend>
					<table border="0" class="noc" id="noc">
					<xsl:apply-templates select="catalogName" />
					<xsl:apply-templates select="catalogConnection" />
					<xsl:apply-templates select="catalogFormat" />
					<xsl:apply-templates select="catalogFile" />
					</table>
				</fieldset>
				
				<fieldset>
				 <legend>Canali ricerca attivati</legend>
				 	<xsl:for-each select="search">
				 		<input type="checkbox" name="search-{@name}" value="true">
				 			<xsl:if test="string-length(@checked) != 0">
				 				<xsl:attribute name="checked">checked</xsl:attribute>
				 			</xsl:if>
				 		</input>
				 		<xsl:value-of select="@desc" />
				 		<br/>
				 	</xsl:for-each>
				</fieldset>
				
				<fieldset>
				 <legend>Canali liste attivati</legend>
					 <xsl:for-each select="list">
				 		<input type="checkbox" name="list-{@name}" value="true">
				 			<xsl:if test="string-length(@checked) != 0">
				 				<xsl:attribute name="checked">checked</xsl:attribute>
				 			</xsl:if>
				 		</input>
				 		<xsl:value-of select="@desc" />
				 		<br/>
				 	</xsl:for-each>
				</fieldset>
				
				<xsl:apply-templates select="links" />
			</div>		
		</div>
	</xsl:template>

	<xsl:template match="catalogFormat">
		<tr>
		<td><label class="cat_data_label">Formato catalogo</label></td>
		<td><input type="text" size="40" name="catalogFormat" value="{text()}" /></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="catalogFile">
		<tr>
		<td><label class="cat_data_label">File da caricare</label></td>
		<td><input type="text" size="40" name="catalogFile" value="{text()}" /></td>
		</tr>
	</xsl:template>

	<xsl:template match="catalogName">
		<tr>
		<td><label class="cat_data_label">Nome catalogo</label></td>
		<td><input type="text" size="40" name="catalogName" value="{text()}" /></td>
		</tr>
	</xsl:template>


	<xsl:template match="catalogConnection">
		<tr>
		<td><label class="cat_data_label">Nome connessione</label></td>
		<td><input type="text" size="40" name="catalogConnection" value="{text()}" /></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="links">
		<b>Links:</b>
		<br />
		<textarea rows="15" cols="80" name="links">
			<xsl:value-of select="."/>
		</textarea>
		<!-- input type="text" size="40" name="catalogConnection" value="{text()}" /-->
		<br />
	</xsl:template>

</xsl:stylesheet>