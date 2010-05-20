<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:include href="../../stylesheets/xslt/helpedit.xslt" />

	<xsl:param name="cid" />
	<xsl:param name="time" />
	<xsl:param name="pid" />
	<xsl:param name="context" />
	<xsl:param name="permission" />
	
	<xsl:include href="../section/edit.xslt" />

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
					<!--   <xsl:apply-templates select="dbType" /> -->
					</table>
					<script type="text/javascript" wiki="false">
					<![CDATA[

						 function autoPopup() {
						   var stili = "top=10, left=10, width=400, height=250, status=no, menubar=no, toolbar=no scrollbar=no";
						   var nomecatalogo = document.getElementById("catalogName").value;
						   var connessione = document.getElementById("catalogConnection").value;
						   var format = document.getElementById("catalogFormat").value;
						   //var dbtype = "";
						   
						   //nomecatalogo = document.editform.catalogName.value;
						   //connessione = document.editform.catalogConnection.value;
						   
						   //alert(connessione);

							//for( i = 0; i < document.editform.dbType.length; i++ ) {
							//	if( document.editform.dbType[i].checked == true ) {
							//		dbtype = document.editform.dbType[i].value;
							//		break;
							//	}
							//}

						   var testo = window.open("importCatalog?pid=]]><xsl:value-of select="$pid" /><![CDATA[&cid=]]><xsl:value-of select="$cid" /><![CDATA[&conn="+connessione+"&format="+format, nomecatalogo, stili);
						 }

					]]>
					<!-- 
					for( i = 0; i < document.editform.dbType.length; i++ ) {
							if( document.editform.dbType[i].checked == true )
								dbtype = document.editform.dbType[i].value;
								break;
							}
					 -->
					</script>
					<button onClick="javascript:autoPopup();return false;">Load data</button>
				</fieldset>
				
				<fieldset>
				 <legend>Canali attivati</legend>
				 <style>
					table.catedit { font-size: 12px; padding: 0px; border: none; border: 1px solid #789DB3;}
					table.catedit td { border: none; border: 1px solid #789DB3; 
					vertical-align: middle; padding: 2px; font-weight: bold; }
				</style>
				 	<table class="catedit">
				 		<thead>
				 			<th>Order by</th><th>Search</th><th>List</th><th>Name</th><th>Label</th>
				 		</thead>
				 		
					 	<xsl:for-each select="search">
					 		<tr>
						 		<td>
						 			<xsl:if test="@name != 'ANY' ">
							 			<input type="radio" name="catalogOrder" value="{@name}">
								 			<xsl:if test="@name=//catalogOrder">
								 				<xsl:attribute name="checked">checked</xsl:attribute>
								 			</xsl:if>
							 			</input>
						 			</xsl:if>
						 		</td>
						 		<td><input type="checkbox" name="search-{@name}" value="true">
						 			<xsl:if test="string-length(@checked) != 0">
						 				<xsl:attribute name="checked">checked</xsl:attribute>
						 			</xsl:if>
						 		</input></td>
						 		<td>
						 		<xsl:if test="@name != 'ANY' ">
							 		<input type="checkbox" name="list-{@name}" value="true">
							 			<xsl:if test="string-length(@checked) != 0">
							 				<xsl:attribute name="checked">checked</xsl:attribute>
							 			</xsl:if>
							 		</input>
						 		</xsl:if>
						 		</td>
						 		<td><xsl:value-of select="@name" /></td>
						 		<td><input type="text" name="name-{@name}" value="{@desc}"/></td>
					 		</tr>
					 	</xsl:for-each>
				 	</table>
				</fieldset>
				
<!-- 				<fieldset>
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
				-->
				
				<xsl:apply-templates select="template" />
			</div>		
		</div>
	</xsl:template>

	<xsl:template match="catalogFormat">
		<tr>
		<td><label class="cat_data_label">Formato catalogo</label></td>
		<td><input type="text" size="40" name="catalogFormat" id="catalogFormat" value="{text()}" /></td>
		</tr>
	</xsl:template>
	
	<!-- 
	<xsl:template match="dbType">
		<tr>
		<td>
			<input type="radio" name="dbType" value="derby">
				<xsl:if test="text() = 'derby'">
	 				<xsl:attribute name="checked">checked</xsl:attribute>
				</xsl:if>
			</input> Derby
		</td>
		<td>
			<input type="radio" name="dbType" value="mysql">
				<xsl:if test="text() = 'mysql'">
	 				<xsl:attribute name="checked">checked</xsl:attribute>
				</xsl:if>
			</input> Mysql
		</td>
		</tr>
	</xsl:template>
	 -->
	
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
		<td><label class="cat_data_label">Descrizione catalogo</label></td>
		<td><input type="text" size="40" name="catalogName" id="catalogName" value="{text()}" /></td>
		</tr>
	</xsl:template>


	<xsl:template match="catalogConnection">
		<tr>
		<td><label class="cat_data_label">Nome catalogo (senza spazi)</label></td>
		<td><input type="text" size="40" name="catalogConnection" id="catalogConnection" value="{text()}" /></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="template">
		<xsl:call-template name="editor" />
	
		<b>Descrizione:</b>
		<br/>
		<input type="hidden" name="UndoText" id="UndoText"/>
		<textarea id="elm2" name="template" rows="10" class="edittextarea" tinyMCE_this="true">
			<!--  xsl:value-of select="text()" /-->
			<!-- xsl:copy-->
				<xsl:apply-templates select="@*|node()|text()|*" />
			<!--  /xsl:copy-->
		</textarea>
		<br />
	</xsl:template>

</xsl:stylesheet>