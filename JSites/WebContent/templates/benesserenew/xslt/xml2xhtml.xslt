<?xml version="1.0" encoding="UTF-8"?>
	<!--
		xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:cinclude="http://apache.org/cocoon/include/1.0"
	-->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:param name="template"/>
	<xsl:param name="context"/>
	<xsl:param name="sitename"/>
	
	<xsl:template match="/page">
		<html lang="it">
			<head>
				<title><xsl:value-of select="$sitename" /> - <xsl:value-of select="/page/content/@pageTitle" /></title>
				<meta content="index, follow" name="robots" />
				<link rel="shortcut icon" href="icon/favicon.ico" />
				<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<link href="./templates/{$template}/css/global.css" type="text/css" rel="stylesheet" media="screen"/>
				<link href="./templates/{$template}/css/horiz-menu.css" type="text/css" rel="stylesheet"  media="screen"/>
				<link href="./templates/{$template}/css/stampa.css" type="text/css" rel="stylesheet"  media="print"/>
			<!-- <STYLE type="text/css"> DIV.wrapper { MARGIN: 0px auto; WIDTH: 950px }
					#sidecol { WIDTH: 25%
					}
					#main-column {
						MARGIN-LEFT: 25%
					}
				</STYLE>
				-->

				<!--  APPLICO I RIFERIMENTI AI STYLESHEETS -->
				<!--   <xsl:apply-templates select="styles/css" /> -->
				<xsl:apply-templates select="redirect" />
				
				<script type="text/javascript">
					<![CDATA[
					
					function det_time()
					   {
					   var d = new Date();
					   var c_hour = d.getHours();
					   var c_min = d.getMinutes();
					   var c_sec = d.getSeconds();
					   var dd=d.getDay();
					   var mm=d.getMonth();
					   var yy=d.getFullYear();
					   parseInt(c_hour) < 10 ? c_hour = "0" + c_hour : null;
     				   parseInt(c_min) < 10 ? c_min = "0" + c_min : null;
     				   parseInt(c_sec) < 10 ? c_sec = "0" + c_sec : null;
     				   parseInt(dd) < 10 ? dd = "0" + dd : null;
     				   parseInt(mm) < 10 ? mm = "0" + mm : null;
					   var t = dd + "/" + mm + "/" + yy + "  " + c_hour + ":" + c_min + ":" + c_sec;
					   document.getElementById('dataora').innerHTML = t;
					   }
					
					]]>
				</script>

				

			</head>
			
			<body>
				<div id="container">
					<xsl:apply-templates select="header" />
					<table id="corpo">
   					 <tr>
						<xsl:apply-templates select="navbar" />
						<xsl:apply-templates select="content" />
					</tr>
					</table>
					<xsl:apply-templates select="footer" />
				</div>
			</body>

		</html>
	</xsl:template>

	<xsl:template match="header">
	<div id="header">

    <!-- begin masthead -->
    <div id="masthead">
      <div id="i"><a href="/"><img src="templates/{$template}/images/spacer.gif" width="90" height="110" border="0" /></a></div>
      <div id="dataora" title="Data e ora attuale">12/12/2009 12:31:31</div>
      <script type="text/javascript">
		<![CDATA[
		
		setInterval('det_time()', 1000);
		
		]]>
		</script>
      
    </div>
    <!-- end masthead -->
    <!-- begin horiz-menu -->
    <div id="horiz-menu" class="moomenu">

      <ul class="menutop" id="horiznav">

			<xsl:apply-templates select="/page/navbar/navigator/navpage"/>

      </ul>
      <ul class="menutop" id="messaggi">
        <li class="active"> <span class="top"><a class="daddy" href="#">Contatti</a></span> </li>
      </ul>
    </div>

    <!-- end horiz-menu -->
  </div>
  <!-- end header -->
		
		<!--Begin Inset Area-->
		<DIV id="spacer" />

		<xsl:copy-of select="div[@id='briciole']" />
		<!--End Inset Area-->
	

	</xsl:template>
	
	<xsl:template match="navpage">
		<xsl:if test="@level = 1">
			<li class="parent"><span class="top"><a href="{@link}" ><xsl:value-of select="@name" /></a></span></li>
			<xsl:apply-templates />
		</xsl:if>
		<xsl:if test="@level != 1">
			<xsl:if test="count(navpage) = 0">
				<li class=""><span class="top"><a href="{@link}"><xsl:value-of select="@name" /></a></span></li>
			</xsl:if>
			<xsl:if test="count(navpage) != 0">
				<li class="parent"><span class="top"><a href="{@link}" class="daddy"><xsl:value-of select="@name" /></a></span>
					<ul>
						<xsl:apply-templates />
					</ul>
				</li>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<xsl:template match="navbar">
		<td valign="top" class="sx"><div id="scelta">
		<ul>
			<xsl:apply-templates select="/page/navbar/navigator/navpage"/>
		</ul>
       

      </div>
			<br />
			<div class="inevidenza">In Evidenza</div>
        <img src="./templates/{$template}/images/centro_estivo.png" alt="Centro estivo" width="200" height="46" /><br /><br />
        <img src="./templates/{$template}/images/animazione.png" alt="Centro estivo" width="200" height="44" /><br /><br />
        <img src="./templates/{$template}/images/estate_in_movimento.png" alt="Centro estivo" width="200" height="45" /></td>

	</xsl:template>

	<xsl:template match="content">
		<!--Begin Main Content Area-->
		<td valign="top" class="centro"><img src="./templates/{$template}/images/home_image.png" width="563" height="281" />
			<div id="content">
	        	<xsl:copy-of select="*" />
	       
	      	</div>
      	</td>
      	<td valign="top" class="dx">
      		<div id="orario">
	    	  <p style="margin: 0px 0px 5px 0px; font-size: 14px; font-weight: bold"><strong>Orario di Segreteria</strong></p>

			  Trieste Via Beccaria, 6<br />
			  Tel e Fax
			  040 569431<br />
			  Email info@estateinmovimento.it<br />
			  asdcbenessere@gmail.com <br />
			  
			</div>
			<img src="./templates/{$template}/images/facebook.png" width="200" height="44" alt="facebook" /><br />
            <img src="./templates/{$template}/images/cinque.png" width="200" height="44" alt="facebook" /><br /><br />

			<div class="inevidenza">Partners</div>
			<img src="./templates/{$template}/images/partner1.png" width="200" height="44" alt="facebook" /><br /><br />
            <img src="./templates/{$template}/images/partner2.png" width="200" height="44" alt="facebook" /><br /><br />
            <img src="./templates/{$template}/images/partner3.png" width="200" height="44" alt="facebook" /><br /><br />
	  </td>
	  

	</xsl:template>

	<xsl:template match="footer">
	
		
		<div id="footer">
			E-mail: asdcbenessere@gmail.com  | © 2010 Benessere | Partita IVA 88888888888
			<!--   <xsl:copy-of select="*" /> -->
		</div>
	</xsl:template>

	<xsl:template match="css">
		<!-- <xsl:variable name="nomefile">
			<xsl:value-of select="text()" />
		</xsl:variable>
		 -->
		<link href="./templates/{$template}/css/import.css" rel="stylesheet" type="text/css" />
	</xsl:template>

	<xsl:template match="redirect">
		<xsl:choose>
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