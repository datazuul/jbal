<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:sql="http://apache.org/cocoon/SQL/2.0"
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
    <xsl:param name="orderby" />
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
				        
				        if(el.type == "select-one" && el.selectedIndex != 0 ){
                                q = q + el.id + "=" + el[el.selectedIndex].value + "&";
                        }
				    }
				    q = q.substring(0,q.length-1);
				    
				    document.getElementById("query").value = escape(q);
				  
				}
				
				
				function list(nome) {					
					var l=document.getElementById("lista");
					l.name="list"+nome;
					var el=document.getElementById(nome);
					l.value=el.value;
					var f=document.getElementById('listForm');
					f.submit();
				}
				
				]]>
				</script>
				 
				<xsl:variable name="countSearch">
					<xsl:value-of select="count(//search/@checked)" />
				</xsl:variable>
				<xsl:variable name="countList">
					<xsl:value-of select="count(//list/@checked)" />
				</xsl:variable>
								
				<xsl:if test="$countSearch &gt; 0">
				 	<form method="post" action="pageview?pid={$pid}" id="listForm">
						<input type="hidden" id="lista" name="lista" value="" />
					</form>
								
					<table border="0" class="searchtableform">
						<tr>
						<td>
							<form method="post" action="pageview?pid={$pid}" onsubmit="readParam()" id="catalogForm" style="float: left;">
								<input name="pid" id="pid" type="hidden" value="{$pid}"/>
							    <input name="query" id="query" type="hidden" value=""/>
							    <input id="page" name="page" type="hidden" value="0"/>
							    <input name="orderby" id="orderby" type="hidden" value="{//catalogOrder}" />
							    
							    <fieldset>
							    <legend>Search</legend>
							    <table border="0">
							    <xsl:for-each select="//search">
							    	<xsl:if test="string-length(@checked)>0">
							    		<tr>
							    			<td style="font-size: 10px; text-align: right;">
							    				<label class="searchlabel"><xsl:value-of select="@desc" />:&#160;</label>
							    				
							    			</td>
							    			<td>
							    				<input id="{@name}" type="text" size="60%"/>
							    				<xsl:variable name="lname"><xsl:value-of select="@name" /></xsl:variable>
							    				<xsl:variable name="ck"><xsl:value-of select="//list[@name=$lname]/@checked" /></xsl:variable>
							    				<xsl:if test="string-length($ck)>0">
						    						<input type="button" value="List" onClick="list('{$lname}')" />
							    				</xsl:if>
							    			</td>
							    		</tr>
							    	</xsl:if>
							    </xsl:for-each>
							    </table>
							    <input type="submit" value="Cerca"/>
							    </fieldset>
							</form>
							</td>
<!--  							<td>
							
							<table border="0">
							<xsl:for-each select="//list">
								<xsl:if test="string-length(@checked)>0">
									
									<form method="post" action="pageview?pid={$pid}">
										<input name="pid" id="pid" type="hidden" value="{$pid}"/>
										<tr><td style="font-size: 10px; text-align: right;">
								    	<label class="searchlabel"><xsl:value-of select="@desc" />:&#160;</label>
								    	</td><td>
								    	<input name="list{@name}" type="text" />
								    	<input type="submit" value="List" />
								    	</td></tr>
							    	</form>
							    </xsl:if>
						    </xsl:for-each>
						    </table>
						    </td>
-->
					    </tr>
				    </table>
				    
				</xsl:if>
				
				
				<!-- 
				<form method="post" action="http://localhost:8080/JSites/HMPy">
				    Query: <input name="query" type="text"/>&#160;&#160;
				    <input type="submit" value="Submit" />
				</form>
				 -->
				<!--  <xsl:apply-templates select="section/lang[@code=$lang]"/> -->
				<div id="resultSet">
					<xsl:apply-templates />
				</div>
				<div class="clearer">&#160;</div>
				<xsl:if test="string-length(root/listRecord) = 0">
					<xsl:call-template name="nav"/>
				</xsl:if>
				<xsl:if test="string-length(root/listRecord) > 0">
					<xsl:call-template name="listnav"/>
				</xsl:if>
			</div>
		</div>
		
		
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
	</xsl:template>
	
	<xsl:template match="catalogFormat"></xsl:template>
	<xsl:template match="catalogOrder"></xsl:template>
	
	<xsl:template match="search">
	    
	    <!-- Autore: <input id="AUT" type="text"/>&#160;Titolo: <input id="TIT" type="text"/>&#160; -->
	</xsl:template>			    
			
	<xsl:template name="nav">
		<xsl:variable name="count"><xsl:value-of select="root/queryData/queryCount"/></xsl:variable>
		<!--  
		Query: <xsl:value-of select="$query" /><br/>
		Page: <xsl:value-of select="$page" /><br/>
		Count: <xsl:value-of select="$count" /><br/>
		Pid: <xsl:value-of select="$pid" /><br/>
		Orderby: <xsl:value-of select="$orderby" /><br/>
		-->
		<xsl:if test="string-length($query) > 0">
			<xsl:if test="string-length($page) > 0">
				<div class="ricerca_nav" style="text-align: center;">
					<xsl:if test="$page > 0">
						<a accesskey="I" href="pageview?pid={$pid}&amp;orderby={$orderby}&amp;query={$query}&amp;page={$page+(-1)}">&lt;&lt; Indietro</a>
					</xsl:if>
					&#160;pagina&#160;<xsl:value-of select="$page +1"/>&#160;di&#160;<xsl:value-of select="floor($count div 10)+1"/>&#160;
					<xsl:if test="(($page+1) * 10) &lt; $count ">
						<a accesskey="A" href="pageview?pid={$pid}&amp;orderby={$orderby}&amp;query={$query}&amp;page={$page+1}">Avanti &gt;&gt;</a>
					</xsl:if>
					
					<br/>
				</div>
				<div class="clearer">&#160;</div>
			</xsl:if>
			<xsl:if test="string-length($page) = 0">
				<div class="ricerca_nav" style="text-align: center;">					
					&#160;pagina&#160;1&#160;di&#160;<xsl:value-of select="floor($count div 10)+1"/>&#160;
					<xsl:if test="10 &lt; $count ">
						<a accesskey="A"  href="pageview?pid={$pid}&amp;orderby={$orderby}&amp;query={$query}&amp;page=1">Avanti &gt;&gt;</a>
					</xsl:if>
					
					<br/>
				</div>
				<div class="clearer">&#160;</div>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="listnav">
		<xsl:if test="root/sjid > 0">
			<div class="ricerca_nav" style="text-align: center;">
				<xsl:variable name="ejid"><xsl:value-of select="root/ejid"/></xsl:variable>
				<xsl:variable name="list"><xsl:value-of select="root/listRecord"/></xsl:variable>
				<a accesskey="P" href="pageview?pid={$pid}&amp;nlist{$list}={$ejid}">Prossimi risultati &gt;&gt;</a>				
				<br/>
			</div>
			<div class="clearer">&#160;</div>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>