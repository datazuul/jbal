<?xml version="1.0" encoding="UTF-8"?>
<!-- xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:cinclude="http://apache.org/cocoon/include/1.0"-->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/page">
		<html lang="it">
			<head>
				<title>SBA - Sistema Bibliotecario di Ateneo</title>
				<meta http-equiv="Content-Type"
					content="text/html; charset=utf-8" />

								
				<!-- main calendar program -->
				<script type="text/javascript" src="js/calendar.js"></script>
					
				<!-- language for the calendar -->
				<script type="text/javascript" src="js/calendar-it.js"></script>
					
				<!-- the following script defines the Calendar.setup helper function, which makes
				     adding a calendar a matter of 1 or 2 lines of code. -->
				<script type="text/javascript" src="js/calendar-setup.js"></script>
				
				<!--  APPLICO I RIFERIMENTI AI STYLESHEETS -->
				<xsl:apply-templates select="styles/css" />
				<xsl:apply-templates select="redirect" />

			</head>

			<body>

				<xsl:apply-templates select="header" />

				<div id="corpo">
					<xsl:apply-templates select="navbar" />
					<xsl:apply-templates select="content" />
				</div>
				<xsl:apply-templates select="footer" />
			</body>

		</html>
	</xsl:template>

	<xsl:template match="header">
		<div id="header">

			<!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
			<!-- IMMAGINE SBA -->

			<!-- xsl:copy-of select="div[@id='logo']"/-->
			<div id="logo">

				<img usemap="#Map" height="125"
					alt="Servizio Bibliotecario di Ateneo" src="images/logo.jpg"
					width="119" border="0" />
				<map name="Map">
					<area shape="rect" coords="9,11,106,99"
						href="pageview?pid=1" alt="Servizio Bibliotecario di Ateneo" />
					<area shape="rect" coords="7,103,117,119"
						href="http://www.units.it" alt="Università di Trieste" />
				</map>

			</div>

			<!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
			<!--  TABS -->

			<xsl:copy-of select="div[@id='mainbar']" />

			<!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
			<!--  SCRITTA SBA -->

			<div id="scritta_sba">
				<img src="images/scritta_sba.jpg" width="307"
					height="62" />
			</div>
			<!-- div id="scritta_sba"><img src="images/sand_box1.jpg" width="345" height="62" />Servizio Bibliotecario Nazionale</div-->

			<!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
			<!--  IN ORDINE: LINGUE, STRUMENTI, E BARRA DI RICERCA -->

			<xsl:copy-of select="div[@id='lingue']" />
			<xsl:copy-of select="div[@id='strumenti']" />

			<xsl:copy-of select="form[@id='cerca']" />
			<xsl:copy-of select="div[@id='briciole']" />


			<!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->

		</div>
	</xsl:template>

	<xsl:template match="navbar">
		<div id="sidebar">
			<xsl:copy-of select="*" />
		</div>
	</xsl:template>

	<xsl:template match="content">
		<div id="content">
			<xsl:copy-of select="*" />
		</div>
	</xsl:template>

	<xsl:template match="footer">
		<div id="footer">
			<xsl:copy-of select="*" />
		</div>
	</xsl:template>

	<xsl:template match="css">
		<xsl:variable name="nomefile">
			<xsl:value-of select="text()" />
		</xsl:variable>
		<link href="{$nomefile}" rel="stylesheet" type="text/css" />
		
		<!-- calendar stylesheet -->
		<link rel="stylesheet" type="text/css" media="all" href="css/calendar-win2k-cold-1.css" title="win2k-cold-1" />
			
		<!-- link type="text/css" rel="stylesheet" href="css/sbn.css"/-->
	</xsl:template>

	<xsl:template match="redirect">
		<meta http-equiv="expires" content="-1" />
		<meta http-equiv="pragma" content="no-cache" />
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