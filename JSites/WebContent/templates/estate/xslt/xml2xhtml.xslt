<?xml version="1.0" encoding="UTF-8"?>
<!-- xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:cinclude="http://apache.org/cocoon/include/1.0"-->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="template"/>

	<xsl:template match="/page">
		<html lang="it">
			<head>
				<title>Estate in Movimento</title>
				<meta http-equiv="Content-Type"
					content="text/html; charset=utf-8" />
				<script src="js/calendar.js" type="text/javascript"></script> 
				<script src="js/calendar-en.js" type="text/javascript"></script>
            	<script src="js/calendar-setup.js" type="text/javascript"></script>
			

				<!--  APPLICO I RIFERIMENTI AI STYLESHEETS -->
				<xsl:apply-templates select="styles/css" />
				<xsl:apply-templates select="redirect" />
			</head>

			<body id="page_bg">
				<div id="wrapper">

				<xsl:apply-templates select="header" />

				<div id="mainbody">
				<!-- 	<xsl:apply-templates select="navbar" />   -->
				<xsl:apply-templates select="content" />
				</div>
				<xsl:apply-templates select="footer" />
				</div>
			</body>

		</html>
	</xsl:template>

	<xsl:template match="header">

		<div id="header">
			<div class="rk-1">
				<div class="rk-2">
					<a href="/" class="nounder">
						<img src="images/blank.gif" alt="" name="logo" border="0" id="logo" />
					</a>
					<div id="top">
		<!--				<div class="padding"></div> -->
					</div>
				</div>
			</div>
		</div>
	<div id="horiz-menu">
		<ul id="mainlevel-nav">	
			<xsl:copy-of select="div[@id='mainbar']" />
		</ul>
		<div id="language" style="margin-right: 5px; text-align: center;">
			<!--
				<a href="/eng/index.html" style="float:right; width: 10px;
				font-size: 12px; font-weight:normal;
				text-decoration:underline;">English</a>
			-->
		</div>
	</div>


	<!--  IN ORDINE: LINGUE, STRUMENTI, E BARRA DI RICERCA -->
	<!-- 
		<div id="header">

			

			<xsl:copy-of select="div[@id='lingue']" />
			<xsl:copy-of select="div[@id='strumenti']" />

			<xsl:copy-of select="form[@id='cerca']" />
			<xsl:copy-of select="div[@id='briciole']" />


			

		</div>
		-->
		<!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
	</xsl:template>

	<xsl:template match="navbar">
		<div id="sidebar">
			<xsl:copy-of select="*" />
		</div>
	</xsl:template>

	<xsl:template match="content">
	<div id="mainbody">
<table class="mainbody" cellspacing="0" cellpadding="0">
	<tr valign="top">
		<td class="mainbody">
			<!-- InstanceBeginEditable name="testo principale" -->
		
			<table cellspacing="15" class="usermodules">
				<tr valign="top">
					<td class="usermodules">
					<div class="moduletable">
						<xsl:copy-of select="*" />
					</div>
					</td>
				</tr>
			</table>
			<!-- InstanceEndEditable -->
		</td>
		<td class="right">

		<div class="padding">
		<div class="moduletable">
		<h3>Benessere ASDC</h3>
                <p><a href="http://www.benessereasdc.it">
			<img src="http://www.benessereasdc.it/images/logo.jpg" width="182"
                        height="138" />
			</a>
			<br />
                </p>

		<h3>Attivita'</h3>
		<p><img src="images/portfolio/01_portfolio.png" width="182"
			height="138" /><br />
		</p>
		<p><img src="images/portfolio/02portfolio.png" width="182"
			height="113" /><br />
		</p>

		</div>
		</div>
		</td>
	</tr>
</table>
</div>
	
	
<!-- 
	
		<div id="content">
			<xsl:copy-of select="*" />
		</div>
 -->
		
	</xsl:template>
	
	<xsl:template match="footer">
		<div id="bottom">
			<table class="usermodules">
				<tr valign="top">
					<td class="usermodules">
						<div class="moduletable" style="padding: 15px 15px 0px 5px">
							Estate in Movimento
							<br />
							Associazione Benessere
							<br />
							mail: info@estateinmovimento.it 
						</div>
					</td>
					<td class="usermodules">
						<div class="moduletable"></div>
					</td>
					<td class="usermodules">
						<div class="moduletable" style="padding: 15px 5px 0px 15px; text-align: right">
							
							<br />
							
							<br />
							
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div id="footer">
			<div class="rk-1">
				<div class="rk-2">
					<div id="the-footer">
						<a href="" title="" class="nounder">
							<img src="images/blank.gif" alt="" name="rocket" border="0"
								id="rocket" />
						</a>
					</div>
				</div>
			</div>
		</div>

	
	
	
	<!-- 
	<div id="footer">
			<xsl:copy-of select="*" />
		</div>
	 -->
	
		
		
	</xsl:template>

	<xsl:template match="css">
		<xsl:variable name="nomefile">
			<xsl:value-of select="text()" />
		</xsl:variable>
		<link href="./templates/{$template}/{$nomefile}" rel="stylesheet" type="text/css" />
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
