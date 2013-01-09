<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE a [
        <!ENTITY nbsp    "&#160;">
        <!ENTITY iexcl   "&#161;">
        <!ENTITY cent    "&#162;">
        <!ENTITY pound   "&#163;">
        <!ENTITY curren  "&#164;">
        <!ENTITY yen     "&#165;">
        <!ENTITY brvbar  "&#166;">
        <!ENTITY sect    "&#167;">
        <!ENTITY uml     "&#168;">

        <!ENTITY copy    "&#169;">
        <!ENTITY ordf    "&#170;">
        <!ENTITY laquo   "&#171;">
        <!ENTITY not     "&#172;">
        <!ENTITY shy     "&#173;">
        <!ENTITY reg     "&#174;">
        <!ENTITY macr    "&#175;">
        <!ENTITY deg     "&#176;">
        <!ENTITY plusmn  "&#177;">

        <!ENTITY sup2    "&#178;">
        <!ENTITY sup3    "&#179;">
        <!ENTITY acute   "&#180;">
        <!ENTITY micro   "&#181;">
        <!ENTITY para    "&#182;">
        <!ENTITY middot  "&#183;">
        <!ENTITY cedil   "&#184;">
        <!ENTITY sup1    "&#185;">
        <!ENTITY ordm    "&#186;">

        <!ENTITY raquo   "&#187;">
        <!ENTITY frac14  "&#188;">
        <!ENTITY frac12  "&#189;">
        <!ENTITY frac34  "&#190;">
        <!ENTITY iquest  "&#191;">
        <!ENTITY Agrave  "&#192;">
        <!ENTITY Aacute  "&#193;">
        <!ENTITY Acirc   "&#194;">
        <!ENTITY Atilde  "&#195;">

        <!ENTITY Auml    "&#196;">
        <!ENTITY Aring   "&#197;">
        <!ENTITY AElig   "&#198;">
        <!ENTITY Ccedil  "&#199;">
        <!ENTITY Egrave  "&#200;">
        <!ENTITY Eacute  "&#201;">
        <!ENTITY Ecirc   "&#202;">
        <!ENTITY Euml    "&#203;">
        <!ENTITY Igrave  "&#204;">

        <!ENTITY Iacute  "&#205;">
        <!ENTITY Icirc   "&#206;">
        <!ENTITY Iuml    "&#207;">
        <!ENTITY ETH     "&#208;">
        <!ENTITY Ntilde  "&#209;">
        <!ENTITY Ograve  "&#210;">
        <!ENTITY Oacute  "&#211;">
        <!ENTITY Ocirc   "&#212;">
        <!ENTITY Otilde  "&#213;">

        <!ENTITY Ouml    "&#214;">
        <!ENTITY times   "&#215;">
        <!ENTITY Oslash  "&#216;">
        <!ENTITY Ugrave  "&#217;">
        <!ENTITY Uacute  "&#218;">
        <!ENTITY Ucirc   "&#219;">
        <!ENTITY Uuml    "&#220;">
        <!ENTITY Yacute  "&#221;">
        <!ENTITY THORN   "&#222;">

        <!ENTITY szlig   "&#223;">
        <!ENTITY agrave  "&#224;">
        <!ENTITY aacute  "&#225;">
        <!ENTITY acirc   "&#226;">
        <!ENTITY atilde  "&#227;">
        <!ENTITY auml    "&#228;">
        <!ENTITY aring   "&#229;">
        <!ENTITY aelig   "&#230;">
        <!ENTITY ccedil  "&#231;">

        <!ENTITY egrave  "&#232;">
        <!ENTITY eacute  "&#233;">
        <!ENTITY ecirc   "&#234;">
        <!ENTITY euml    "&#235;">
        <!ENTITY igrave  "&#236;">
        <!ENTITY iacute  "&#237;">
        <!ENTITY icirc   "&#238;">
        <!ENTITY iuml    "&#239;">
        <!ENTITY eth     "&#240;">

        <!ENTITY ntilde  "&#241;">
        <!ENTITY ograve  "&#242;">
        <!ENTITY oacute  "&#243;">
        <!ENTITY ocirc   "&#244;">
        <!ENTITY otilde  "&#245;">
        <!ENTITY ouml    "&#246;">
        <!ENTITY divide  "&#247;">
        <!ENTITY oslash  "&#248;">
        <!ENTITY ugrave  "&#249;">

        <!ENTITY uacute  "&#250;">
        <!ENTITY ucirc   "&#251;">
        <!ENTITY uuml    "&#252;">
        <!ENTITY yacute  "&#253;">
        <!ENTITY thorn   "&#254;">
        <!ENTITY yuml    "&#255;">
]>

	<!--
		xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:cinclude="http://apache.org/cocoon/include/1.0"
		
		
	-->
<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns="http://www.w3.org/1999/xhtml">
	
	<xsl:output
		method="xml"
		omit-xml-declaration="yes"
		indent="yes"/> 
	
	<xsl:param name="template"/>
	<xsl:param name="context"/>
	<xsl:param name="sitename"/>
	
	<xsl:param name="pid"/>
	
	<xsl:param name="pageid"/>
	<xsl:param name="pagecode"/>
	<xsl:param name="name"/>
	<xsl:param name="pageusername"/>
	<xsl:param name="pagechangeip"/>
	<xsl:param name="pagedate"/>
	<xsl:param name="pageresp"/>
	<xsl:param name="requesturi"/>
	<xsl:param name="useragent"/>
	<xsl:param name="viewsidebar" />

	<xsl:include href="styles.xslt"/>
	<xsl:include href="container.xslt"/>
	<xsl:include href="centracon-menubar.xslt"/>
	<xsl:include href="centracon-banner.xslt"/>
	<xsl:include href="centracon-sidebar.xslt"/>
	<xsl:include href="centracon-footer.xslt"/>
	
	

	<xsl:template match="/page">
		<!--  <xsl:choose>
			<xsl:when test="starts-with($useragent,'explorer')">
				<html>
					<head>
						<meta http-equiv="REFRESH" content="0;url={$requesturi}?_template=unitsnewIE" />
					</head>
				</html>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="pagexhtml" />
			</xsl:otherwise>
		</xsl:choose> -->
		
		<xsl:call-template name="pagexhtml" />
		
	</xsl:template>
	
	<xsl:template name="pagexhtml">
		<html xmlns="http://www.w3.org/1999/xhtml" lang="it-IT" xml:lang="it-IT"> <!--  dir="ltr" lang="en-US" xml:lang="en"  xmlns="http://www.w3.org/1999/xhtml" -->
			<head>
				<title>
					<xsl:value-of select="$sitename" />
					-
					<xsl:value-of select="/page/content/@pageTitle" />
				</title>
				
				<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
				<meta http-equiv="Content-Script-Type" content="text/javascript" />
				<meta http-equiv="Content-Style-Type" content="text/css" />
				<meta http-equiv="X-UA-Compatible" content="IE=8" />

				<!-- <xsl:value-of select="document('http://www.units.it/grafica4out/ute/amm_style/')" 
					/> -->

				<xsl:call-template name="units-style" />
				<xsl:apply-templates select="redirect" />

<script type="text/javascript">
<![CDATA[
			function readParam1(){
				
			var q = "";
			var form = document.getElementById("catalogForm1");
				    
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
				    
			document.getElementById("query1").value = escape(q);
				  
			}
]]>
		</script>





<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-11067568-2']);
  _gaq.push(['setDomainName', '.<xsl:value-of select="$sitename" />.units.it']);
  _gaq.push(['setAllowHash', true]);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>

			</head>
			<body class="{$pagecode}">
				<div id="art-main">
					<div class="art-sheet">
						<div class="art-sheet-tl"></div>
						<div class="art-sheet-tr"></div>
						<div class="art-sheet-bl"></div>
						<div class="art-sheet-br"></div>
						<div class="art-sheet-tc"></div>
						<div class="art-sheet-bc"></div>
						<div class="art-sheet-cl"></div> 
						<div class="art-sheet-cr"></div> 
						<div class="art-sheet-cc"></div>
						<div class="art-sheet-body">
			
							<div id="container1">
								<xsl:apply-templates select="header" />
								
				            	<div class="art-content-layout">
                    				<div class="art-content-layout-row">
										<xsl:apply-templates select="navbar" />
										<xsl:apply-templates select="content" />
									</div>
								</div>
							</div>
						</div>
						
						<xsl:apply-templates select="footer" />
						
					</div>
					<xsl:call-template name="units-footer" />
					
				</div>
				
			</body>
		</html>
				 
		
	</xsl:template>
	
	<xsl:template match="header">
		<xsl:call-template name="units-container" />
		<xsl:if test="not($sitename = 'sslmit' or $sitename = 'dslit')">	
  			<xsl:call-template name="centracon-menubar" />
  		</xsl:if>
  		<xsl:call-template name="centracon-banner" />
  		<xsl:copy-of select="//div[@id='briciole']" />
	</xsl:template>


	<xsl:template match="content">
		<div class="art-layout-cell art-content">
			<div class="art-post">
				<div class="art-post-body">
					<div class="art-post-inner art-article">
						<xsl:copy-of select="*" />
					</div>
				</div>
			</div>
		</div>
	</xsl:template>
	
	<xsl:template match="footer">
		<xsl:call-template name="centracon-footer" />
		<!--  <script type="text/javascript">
		 	var gaJsHost = (("https:" ==
			document.location.protocol) ? "https://ssl." : "http://www.");
			document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
		<script type="text/javascript">
			var pageTracker = _gat._getTracker("UA-5397797-1");
			pageTracker._trackPageview();
		</script>
-->
<!--FINE COPYRIGHT -->
	<!-- 
		<div id="footer">
			<xsl:copy-of select="*" />
		</div>
		-->
	</xsl:template>

	<xsl:template match="css">
		<!-- 
		<xsl:variable name="nomefile">
			<xsl:value-of select="text()" />
		</xsl:variable>
		<link href="{$nomefile}" rel="stylesheet" type="text/css" />
		-->
		<link href="./templates/{$template}/css/sezione.css" rel="stylesheet" type="text/css" />
		<link href="./templates/{$template}/css/tabella.css" rel="stylesheet" type="text/css" />
		<link href="./templates/{$template}/css/editing.css" rel="stylesheet" type="text/css" />

		<link href="./templates/{$template}/css/dbManager.css" rel="stylesheet" type="text/css" />
		<link href="./templates/{$template}/css/calendar-blue2.css" rel="stylesheet" type="text/css" />
		
	</xsl:template>

	<xsl:template match="redirect">
		<xsl:choose>
			<xsl:when test='starts-with(text(),"http://www.biblio")'>
				<meta http-equiv="refresh" content="0;URL={text()}" />
			</xsl:when>
			<xsl:when test='starts-with(text(),"http://www.sba")'>
				<meta http-equiv="refresh" content="0;URL={text()}" />
			</xsl:when>
			<xsl:when test='starts-with(text(),"http://www.smats")'>
				<meta http-equiv="refresh" content="0;URL={text()}" />
			</xsl:when>
			<xsl:when test='starts-with(text(),"http://www.eut")'>
				<meta http-equiv="refresh" content="0;URL={text()}" />
			</xsl:when>
			<xsl:when test='starts-with(text(),"http://")'>
				<meta http-equiv="refresh" content="5;URL={text()}" />
			</xsl:when>
			<xsl:otherwise>
				<meta http-equiv="refresh" content="0;URL={text()}" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	

	<xsl:template match="text()" />

</xsl:stylesheet>
