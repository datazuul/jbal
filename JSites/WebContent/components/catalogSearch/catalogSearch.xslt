<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:sql="http://apache.org/cocoon/SQL/2.0"
							xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:include href="../../stylesheets/xslt/editlinks.xslt" />
    <xsl:include href="jo2xhtml.xslt" />
    <!-- Questo include serve pei link de modifica, elimina e (dis)attiva 
		 ma anca per le combo de ordinamento  -->
                              
	<xsl:param name="cid"/>
	<xsl:param name="pid"/>
	<xsl:param name="pacid"/>
	<xsl:param name="editing"/>
	<xsl:param name="lang"/>
    <xsl:param name="validating"/>
    <xsl:param name="disabling"/>
    <xsl:param name="time"/>
    <xsl:param name="extra" />
    <xsl:param name="type" />
    
    
	<xsl:template match="/">
		<div class="{$time}">
			<div class="catalogSearch" id="{$cid}" wiki="false">
				
				<script type="text/javascript" wiki="false">
				<![CDATA[
				function readParam(){
				
				    var q = "";
				    var form = document.getElementById("catalogForm");
				    
				    for(i=0; i<form.elements.length; i++){
				        var el = form.elements[i];
				        if(el.type == "text" && el.value.length > 0 && el.id != "page"){
				            q = q + el.id + "=" + el.value + "&";
				        }
				    }
				    q = q.substring(0,q.length-1);
				    
				    document.getElementById("query").value = escape(q);
				  
				}
				]]>
				</script> 
				
				<form method="get" action="pageview" onsubmit="readParam()" id="catalogForm">
					<input name="pid" id="pid" type="hidden" value="{$pid}"/>
				    <input name="query" id="query" type="hidden" value=""/>
				    Autore: <input id="AUT" type="text"/>&#160;Titolo: <input id="TIT" type="text"/><br/><br/>
				    Page: <input id="page" name="page" type="text"/><br/><br/>
				    <input type="submit"/>
				</form>
				
				<form method="post" action="pageview?pid={$pid}">
				    Titolo: <input name="listTIT" type="text"/>&#160;&#160;
				    <input type="submit" value="List" />
				</form>
				
				<form method="post" action="pageview?pid={$pid}">
				    Autore: <input name="listAUT" type="text"/>&#160;&#160;
				    <input type="submit" value="List" />
				</form>
				
				<a href="pageview?pid={$pid}">Order tit</a></br>
								
				
				<!--  <xsl:apply-templates select="section/lang[@code=$lang]"/> -->
				<xsl:apply-templates />
				<div class="clearer">&#160;</div>
			</div>
		</div>
		
		
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
	</xsl:template>
			
	<xsl:template match="catalogSearch">
		<xsl:apply-templates />
	</xsl:template>

</xsl:stylesheet>