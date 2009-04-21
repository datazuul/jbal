<?xml version="1.0" encoding="UTF-8"?>
<!-- xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:cinclude="http://apache.org/cocoon/include/1.0"-->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/page">
		<html lang="it">
			<head>
				<title>JSites example</title>
				<meta http-equiv="Content-Type"
					content="text/html; charset=utf-8" />
				<script src="js/calendar.js" type="text/javascript"></script> 
				<script src="js/calendar-en.js" type="text/javascript"></script>
            	<script src="js/calendar-setup.js" type="text/javascript"></script>
			

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


			<!-- xsl:copy-of select="div[@id='logo']"/-->
			<div id="logo">

				<img height="125"
					alt="logo JSites" src="images/logo.jpg" width="119" border="0" />

			</div>

			<!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
			<!--  TABS -->

			<xsl:copy-of select="div[@id='mainbar']" />

			<!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->
			<!--  SCRITTA  -->

			<div id="scritta_sba">
				<img src="images/scritta_sba.jpg" width="307" height="62" alt="Sistema Bibliotecario di Ateneo"/>
			</div>
			
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