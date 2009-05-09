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
					<xsl:apply-templates select="dbType" />
					
					<script type="text/javascript" wiki="false">
					<![CDATA[

						 function autoPopup() {
						   var stili = "top=10, left=10, width=400, height=250, status=no, menubar=no, toolbar=no scrollbar=no";
						   var nomecatalogo = document.getElementById("catalogName").value;
						   var connessione = document.getElementById("catalogConnection").value;
						   var dbtype = "";

							for( i = 0; i < document.editform.dbtype.length; i++ ) {
							if( document.editform.dbtype[i].checked == true )
								dbtype = document.editform.dbtype[i].value;
								break;
							}

						   var testo = window.open("importCatalog?pid=]]><xsl:value-of select="$pid" /><![CDATA[&cid=]]><xsl:value-of select="$cid" /><![CDATA[&conn="+connessione+"&dbtype="+dbtype, nomecatalogo, stili);
						 }

					]]>
					</script>

					</table>
					<button onClick="autoPopup();return false;">Load data</button>
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
		<td><input type="text" size="40" name="catalogFormat" id="catalogFormat" value="{text()}" /></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="dbType">
		<tr>
		<td>
			<input type="radio" name="dbtype" value="derby" checked="true" /> Derby
		</td>
		<td>
			<input type="radio" name="dbtype" value="mysql" /> Mysql
		</td>
		</tr>
	</xsl:template>
	
	<xsl:template match="catalogFile">
		<tr>
		<td><label class="cat_data_label">File da caricare</label></td>
		<td>
			<input type="hidden" size="40" name="catalogFile" value="{text()}" />
			
		</td>
		</tr>
	</xsl:template>

	<xsl:template match="catalogName">
		<tr>
		<td><label class="cat_data_label">Nome catalogo</label></td>
		<td><input type="text" size="40" name="catalogName" id="catalogName" value="{text()}" /></td>
		</tr>
	</xsl:template>


	<xsl:template match="catalogConnection">
		<tr>
		<td><label class="cat_data_label">Nome connessione</label></td>
		<td><input type="text" size="40" name="catalogConnection" id="catalogConnection" value="{text()}" /></td>
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