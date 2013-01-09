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
					<xsl:apply-templates select="direction" />
					<tr>
					<td><label class="cat_data_label">Numero record per pagina</label></td>
					<td><input type="text" size="40" name="rxp" id="rxp" value="{rxp}" /></td>
					</tr>
					
					<tr>
					<td><label class="cat_data_label">Default Query</label></td>
					<td><input type="text" size="40" name="defaultQuery" id="defaultQuery" value="{defaultQuery}" /></td>
					</tr>
					
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
				
				<script type="text/javascript">
<![CDATA[
var count=]]><xsl:value-of select="count(search)" /><![CDATA[;

function exchange(el1,el2) {
	var v1=el1.value;
	var v2=el2.value;
	var n1=el1.name;
	var n2=el2.name;
	var c1=el1.checked;
	var c2=el2.checked;
	var d1=el1.disabled;
	var d2=el2.disabled;

	//alert("el1 "+el1.nodeName+" "+n1+" "+v1+" "+el1.type+" "+el1.checked);
	//alert("el2 "+el2.nodeName+" "+n2+" "+v2+" "+el2.type+" "+el2.checked);

	el1.value=v2;
	el2.value=v1;

	el1.name=n2;
	el2.name=n1;

	el1.checked=c2;
	el2.checked=c1;
	
	el1.disabled=d2;
	el2.disabled=d1;

	if(el1.nodeName=='SPAN') {
		var t1=el1.firstChild;
		var t2=el2.firstChild;
		var t=t1.nodeValue;
		t1.nodeValue=t2.nodeValue;
		t2.nodeValue=t;
	}
}


function down(elid) {
	move(elid,1);
}

function up(elid) {
	move(elid,-1);
}

function move(elid,p) {
	
	var morder1=document.getElementById("order"+elid);
	var msearch1=document.getElementById("search"+elid);
	var mlist1=document.getElementById("list"+elid);
	var mnome1=document.getElementById("nome"+elid);
	var mlabel1=document.getElementById("label"+elid);

	elidn=(elid+p);
	if(elidn>count) elidn=1;
	if(elidn<1) elidn=count;

	var morder2=document.getElementById("order"+elidn);
	var msearch2=document.getElementById("search"+elidn);
	var mlist2=document.getElementById("list"+elidn);
	var mnome2=document.getElementById("nome"+elidn);
	var mlabel2=document.getElementById("label"+elidn);

	exchange(morder1,morder2);
	exchange(msearch1,msearch2);
	exchange(mlist1,mlist2);
	exchange(mnome1,mnome2);
	exchange(mlabel1,mlabel2);
}

function validate() {
	var o="";
	for(i=1;i<count+1;i++) {
		var v=document.getElementById("nome"+i);
		o=o+" "+v.firstChild.nodeValue;
	}
	var s=document.getElementById("neworder");
	s.value=o;
}




]]>
</script>
					<input type="hidden" id="neworder" name="neworder" value="" />

				 	<table class="catedit">
				 		<thead>
				 			<th></th><th>Order by</th><th>Search</th><th>List</th><th>Name</th><th>Label</th>
				 		</thead>
				 		
					 	<xsl:for-each select="search">
					 		<tr>
					 			<td>
					 				<img src="./components/catalogSearch/images/ButtonUp.png" onClick="up({position()})"/>
					 				<img src="./components/catalogSearch/images/ButtonDown.png" onClick="down({position()})"/>
					 			</td>
						 		<td>
						 			
						 			<input type="radio" name="catalogOrder" value="{@name}" id="order{position()}">
							 			<xsl:if test="@name=//catalogOrder">
							 				<xsl:attribute name="checked">checked</xsl:attribute>
							 			</xsl:if>
							 			<xsl:if test="@name = 'ANY' ">
							 				<xsl:attribute name="disabled">disabled</xsl:attribute>
							 			</xsl:if>
						 			</input>
						 			
						 		</td>
						 		<td><input type="checkbox" name="search-{@name}" value="true" id="search{position()}">
						 			<xsl:if test="string-length(@checked) != 0">
						 				<xsl:attribute name="checked">checked</xsl:attribute>
						 			</xsl:if>
						 		</input></td>
						 		<td>
						 		
						 			<xsl:variable name="na"><xsl:value-of select="@name" /></xsl:variable>
							 		<input type="checkbox" name="list-{@name}" value="true" id="list{position()}">							 			
							 			<xsl:if test="string-length(//list[@name=$na]/@checked) != 0">
							 				<xsl:attribute name="checked">checked</xsl:attribute>
							 			</xsl:if>
							 			<xsl:if test="@name = 'ANY' ">
							 				<xsl:attribute name="disabled">disabled</xsl:attribute>
							 			</xsl:if>
							 		</input>
						 		
						 		</td>
						 		<td><span id="nome{position()}"><xsl:value-of select="@name" /></span></td>
						 		<td><input type="text" name="name-{@name}" value="{@desc}" id="label{position()}"/></td>
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
	
	<xsl:template match="direction">
		<tr>
		<td><label class="cat_data_label">Ordinamento</label></td>
		<td>
			<xsl:if test="text()='desc' or text()='descendant'">
				<select name="direction" id="direction">
					<option value="ascendant">Crescente</option>
					<option value="descendant" selected="selected">Decrescente</option>
				</select>
			</xsl:if>
			<xsl:if test="not(text()='desc' or text()='descendant')">
				<select name="direction" id="direction">
					<option value="ascendant" selected="selected">Crescente</option>
					<option value="descendant">Decrescente</option>
				</select>
			</xsl:if>
		</td>
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