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
			<div class="pagemanager">
				<script type="text/javascript">
				<![CDATA[
var dbmanager_userInfo = true;
var dbmanager_pageInfo = true;
var dbmanager_cloneFromPage = true;
var dbmanager_newPage = true;
				
function setLinkText(sLink_id, sText) {
	var el;
	if (document.getElementById
		&& (el = document.getElementById(sLink_id))
		|| document.all
		&& (el = document.all[sLink_id])) {
			while (el.hasChildNodes())
				el.removeChild(el.lastChild);
			el.appendChild(document.createTextNode(sText));
	}
}

function switchLinkText(sLink_id, eElement, aText, bText) {
	var t=document.getElementById(sLink_id).getAttribute('class');
	
	if(t != '0') {
		document.getElementById(sLink_id).setAttribute('class', '0');
		
		setLinkText(sLink_id,bText);
		document.getElementById(eElement).style.display='block';
	}
	else {
		document.getElementById(sLink_id).setAttribute('class', '1');
		
		setLinkText(sLink_id,aText);
		document.getElementById(eElement).style.display='none';
	}
}

]]>
				</script>
				
				<style>
				#pageInfo {display: none;}
				#userInfo {display: none;}
				#priorityInfo {display: none;}
				#cloneFromPage {display: none;}
				#newPage {display: none;}
				</style>
				
				[<xsl:choose>
					<xsl:when test="sql:rowset/sql:row/sql:valid = 'true'"><a href="pagedisable?pid={$pid}&amp;cid=0">Disattiva questa pagina</a></xsl:when> <!-- &amp;pacid={@pacid}&amp;cid=0 -->
					<xsl:otherwise><a href="pageactivate?pid={$pid}&amp;cid=0">Attiva questa pagina</a></xsl:otherwise> <!-- &amp;pacid={@pacid}&amp;cid=0 -->
				</xsl:choose>]&#160;-&#160;
				
				[<xsl:choose>					
					<xsl:when test="sql:rowset/sql:row/sql:insidebar = 'false'"><a href="pageinsidebar?pid={$pid}&amp;insidebar=true">Visualizza la pagina nella sidebar</a></xsl:when> <!-- &amp;pacid={@pacid}&amp;cid=0 -->
					<xsl:otherwise><a href="pageinsidebar?pid={$pid}&amp;insidebar=false">Non visualizzare la pagina nella sidebar</a></xsl:otherwise> <!-- &amp;pacid={@pacid}&amp;cid=0 -->
				</xsl:choose>]&#160;-&#160;
				[<xsl:choose>					
					<xsl:when test="sql:rowset/sql:row/sql:viewsidebar = 'false'"><a href="pageviewsidebar?pid={$pid}&amp;viewsidebar=true">Visualizza la sidebar</a></xsl:when> <!-- &amp;pacid={@pacid}&amp;cid=0 -->
					<xsl:otherwise><a href="pageviewsidebar?pid={$pid}&amp;viewsidebar=false">Nascondi la sidebar</a></xsl:otherwise> <!-- &amp;pacid={@pacid}&amp;cid=0 -->
				</xsl:choose>]
				<br/>
				[<a href="#" id="dbmanager_info" onclick = "switchLinkText('dbmanager_info','pageInfo','Mostra info pagina','Nascondi info pagina'); return false; ">Mostra info pagina</a>]&#160;-&#160;
				[<a href="#" id="dbmanager_users" onclick = "switchLinkText('dbmanager_users','userInfo','Mostra utenti pagina','Nascondi utenti pagina'); return false; ">Mostra utenti pagina</a>]&#160;-&#160;
				[<a href="#" id="dbmanager_priority" onclick = "switchLinkText('dbmanager_priority','priorityInfo','Mostra ordine pagine figlie','Nascondi ordine pagine figlie'); return false;">Mostra ordine pagine figlie</a>]
				<br/>
				[<a href="#" id="dbmanager_newpage" onclick = "switchLinkText('dbmanager_newpage','newPage','Aggiungi pagina','Aggiungi pagina'); return false;">Aggiungi pagina</a>]&#160;-&#160;
				<!--  [<a href="pagecreate?papid={$pid}&amp;pid=0&amp;type=externalLink">Aggiungi link</a>]&#160;-&#160; -->
				[<a href="pageerase?pid={$pid}" onclick = "if (! confirm('Con questa operazione si cancella la pagina.\n
					I dati non saranno recuperabili.\n
					Sei sicuro?')) return false;">Rimuovi pagina</a>]&#160;-&#160;
				[<a href="#" id="dbmanager_clone" onclick = "switchLinkText('dbmanager_clone','cloneFromPage','Copia contenuti','Nascondi copia contenuti'); return false;">Copia contenuti</a>]
			</div>
			
		<div id="newPage">
			<form action="pagecreate" method="post">
				<input type="hidden" name="pid" value="{$pid}" />
				Titolo nuova pagina: <input type="input" name="title" />
				<input type="submit" value="Crea nuova pagina" />
			</form>
		</div>
			
		<div id="cloneFromPage">
			<form action="pageclone" method="post" onsubmit="return confirm('ATTENZIONE: dopo questa operazione e' necessario correggere gli eventuali collegamenti verso le pagine copiate.\n
					I documenti collegati (immagini, pdf, altri tipi di documento) non saranno copiati.\n
					Eventuali cataloghi non vengono copiati (gli elementi catalogo punteranno ai cataloghi originali).\n\n
					Continuare?'));">
				<input type="hidden" name="pid" value="{$pid}" />
				Cod.pagina sorgente: <input type="input" name="source" />
				<input type="submit" value="Copia contenuti" />
			</form>
		</div>
		<div id="pageInfo" >
			<!-- <h1>Proprietà della pagina</h1>
			<br/>
			-->
			
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
					<xsl:value-of select="sql:rowset/sql:row/sql:pcode[1]" />
				</xsl:attribute>
			</input>
			<div class="clearer">&#160;</div>
			<div class="label">Codice pagina PADRE:</div>
			<input type="text" name="pagePaPCode">
				<xsl:attribute name="value">
					<xsl:value-of select="sql:rowset/sql:row/sql:pcode[2]" />
				</xsl:attribute>
			</input>
			<div class="clearer">&#160;</div>
			<div class="label">Responsabile pagina:</div>
			<input type="text" name="resp">
				<xsl:attribute name="value">
					<xsl:value-of select="sql:rowset/sql:row/sql:resp" />
				</xsl:attribute>
			</input>
			
			<br/><!--br/><a href="dir/" class="navbar1lev">[ upload file ]</a-->
			<input type="submit" value="Salva"/><br/>
			
		</div>
	</xsl:template>
	
	<xsl:template match="childPriority">
		<div id="priorityInfo">
			<table id="dbManagerPriorityTable">
				<tr>
					<th>Name</th>
					<th>Code</th>
					<th>Position</th>
				</tr>
				<xsl:for-each select="sql:rowset/sql:row">
					<tr>
						<td>
							<xsl:value-of select="sql:name" />
						</td>
						<td>
							<xsl:value-of select="sql:pcode" />
						</td>
						<td>				
							<input type="text" name="priority_{sql:pcode}" class="myTableLine" value="{position()}"/>
						</td>
					</tr>
		      	</xsl:for-each>
			</table>
			<br/><input type="submit" value="Salva"/><br/>
		</div>
	</xsl:template>
	
	<xsl:template match="permissions">
		<div id="userInfo">
			<input type="hidden" name="count" value="{count(sql:rowset/sql:row)}" />
			<table id="dbManagerTable">
				<tr class="caption">
					<td>Utente</td><td class="myTableLine">modifica</td><td class="myTableLine">valida</td>
				</tr>
				
				<xsl:for-each select="sql:rowset/sql:row">
					<xsl:variable name="pcode"><xsl:value-of select="sql:permissioncode" /></xsl:variable>
					<tr>
						<td>
							<xsl:value-of select="sql:username" />
							<input type="hidden" name="utente{position()}" value="{sql:username}" />
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
	      	<input type="submit" value="Salva"/><br/>
	      </div>
	</xsl:template>
	
</xsl:stylesheet>
