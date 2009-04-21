<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
								xmlns:sql="http://apache.org/cocoon/SQL/2.0">

    <xsl:param name="pid" />
    <xsl:param name="contentcid" />
    
	<xsl:template match="/">
			<div class="dbManager">
				<form method="post" action="pagemanagedb?pid={$pid}" accept-charset="UTF-8">
					<xsl:apply-templates />
					<div class="clearer">&#160;</div>
				</form>
				<!--  a href="pageedit?type=news&amp;pid={$pid}&amp;pacid={$contentcid}&amp;cid=0&amp;order=1">[ Aggiungi avviso ]</a-->
			</div>
			<hr/>
	</xsl:template>
	
	<xsl:template match="pageTitle">
		<div class="pageInfo">
			<h1>Proprietà della pagina</h1>
			<br/>
			<div class="label">Titolo pagina:</div>
			<input type="text" name="pageTitle">
				<xsl:attribute name="value">
					<xsl:value-of select="sql:rowset/sql:row/sql:name" />
				</xsl:attribute>
			</input>
			<div class="clearer">&#160;</div>
			<div class="label">Codice pagina:</div>
			<input type="text" name="pageCode">
				<xsl:attribute name="value">
					<xsl:value-of select="sql:rowset/sql:row/sql:pcode" />
				</xsl:attribute>
			</input>
			<br/><!--br/><a href="dir/" class="navbar1lev">[ upload file ]</a-->
			<input type="submit" value="Salva" id="boton"/><br/>
			
		</div>
	</xsl:template>
	
	<xsl:template match="permissions">
		<div class="userInfo">
			<input type="hidden" name="count" value="{count(sql:rowset/sql:row)}" />
			<table id="dbManagerTable">
				<tr class="caption">
					<td>Utente</td><td class="myTableLine">modifica</td><td class="myTableLine">valida</td>
				</tr>
				
				<xsl:for-each select="sql:rowset/sql:row">
					<xsl:variable name="pcode"><xsl:value-of select="sql:permissioncode" /></xsl:variable>
					<tr>
						<td>
							<xsl:value-of select="sql:user" />
							<input type="hidden" name="utente{position()}" value="{sql:user}" />
						</td>
						<td>
							<input type="checkbox" name="editable{position()}" class="myTableLine">
								<xsl:if test="$pcode = 2
											or $pcode = 3
											or $pcode = 6
											or $pcode = 7">
									<xsl:attribute name="checked">true</xsl:attribute>
								</xsl:if>
							</input>
						</td>
						<td>				
							<input type="checkbox" name="validable{position()}" class="myTableLine">
								<xsl:if test="$pcode = 4
											or $pcode = 5
											or $pcode = 6
											or $pcode = 7">
									<xsl:attribute name="checked">true</xsl:attribute>
								</xsl:if>
							</input>
						</td>
					</tr>
		      	</xsl:for-each>
		      	<tr>
			      	<td>
						<input type="text" name="utenteNew" value="" maxlength="30"/>
					</td>
					<td>
						<input type="checkbox" name="editableNew"/>
					</td>
					<td>				
						<input type="checkbox" name="validableNew"/>
					</td>
				</tr>
	      	</table>
	      </div>
	</xsl:template>

</xsl:stylesheet>
