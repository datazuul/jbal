<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:sql="http://apache.org/cocoon/SQL/2.0"
							xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:include href="../../stylesheets/xslt/editlinks.xslt" />
    <xsl:include href="jo2section.xslt" />
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
    <xsl:param name="query" />
    <xsl:param name="page" />
    
    
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
				<table border="0" class="searchtableform">
					<tr>
					<td>
						<form method="post" action="pageview?pid={$pid}" onsubmit="readParam()" id="catalogForm" style="float: left;">
							<!-- <input name="pid" id="pid" type="hidden" value="{$pid}"/>  -->
						    <input name="query" id="query" type="hidden" value=""/>
						    <input id="page" name="page" type="hidden" value="0"/>
						    
						    <fieldset>
						    <legend>Search</legend>
						    <xsl:for-each select="//search">
						    	<label class="searchlabel"><xsl:value-of select="@desc" />:&#160;</label><input id="{@name}" type="text" /><br/>
						    </xsl:for-each>
						    <input type="submit" value="Cerca"/>
						    </fieldset>
						</form>
						</td>
						<td>
						<!-- 
						<div class="clearer">&#160;</div>
		 				<xsl:call-template name="nav"/>
						 -->
						<xsl:for-each select="//list">
							<form method="post" action="pageview?pid={$pid}">
								<!--  <input name="pid" id="pid" type="hidden" value="{$pid}"/> -->
						    	<label class="searchlabel"><xsl:value-of select="@desc" />:&#160;</label>
						    	<input name="list{@name}" type="text" />
						    	<input type="submit" value="List" />
					    	</form>
					    </xsl:for-each>
					    </td>
				    </tr>
			    </table>
				
				<!-- form method="post" action="pageview?pid={$pid}">
				    Titolo: <input name="listTIT" type="text"/>&#160;&#160;
				    <input type="submit" value="List" />
				</form-->
				
				<!--  <xsl:apply-templates select="section/lang[@code=$lang]"/> -->
				<div id="risultati">
					<xsl:apply-templates />
				</div>
				<div class="clearer">&#160;</div>
				<xsl:call-template name="nav"/>
			</div>
		</div>
		
		
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
	</xsl:template>
	
	<xsl:template match="search">
	    
	    <!-- Autore: <input id="AUT" type="text"/>&#160;Titolo: <input id="TIT" type="text"/>&#160; -->
	</xsl:template>			    
			
<!-- 	<xsl:template match="catalogSearch">
		<xsl:apply-templates />
	</xsl:template> -->
	
	<xsl:template name="nav">
		<xsl:if test="string-length($page) > 0">
					<div class="ricerca_nav" style="text-align: center;">
	 					<xsl:variable name="count"><xsl:value-of select="root/queryData/queryCount"/></xsl:variable>
	 				
						<xsl:if test="$page > 0">
							<a href="pageview?pid={$pid}&amp;query={$query}&amp;page={$page+(-1)}">&lt;&lt; Indietro</a>
						</xsl:if>
						&#160;pagina&#160;<xsl:value-of select="$page +1"/>&#160;di&#160;<xsl:value-of select="floor($count div 10)+1"/>&#160;
						<xsl:if test="(($page+1) * 10) &lt; $count ">
							<a href="pageview?pid={$pid}&amp;query={$query}&amp;page={$page+1}">Avanti &gt;&gt;</a>
						</xsl:if>
						
						<br/>
					</div>
					<div class="clearer">&#160;</div>
				</xsl:if>
	</xsl:template>

</xsl:stylesheet>