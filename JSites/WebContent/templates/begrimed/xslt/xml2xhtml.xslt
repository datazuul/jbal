<?xml version="1.0" encoding="UTF-8"?>
	<!--
		xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:cinclude="http://apache.org/cocoon/include/1.0"
	-->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:param name="template"/>
	
	<xsl:template match="/page">
		<html lang="it">
			<HEAD>
				<TITLE>Jopac2</TITLE>
				<META content="catalogo on line per biblioteche" name="description" />
				<META content="biblioteca, libro, ricerca, opac" name="keywords" />
				<META content="index, follow" name="robots" />
				<LINK rel="shortcut icon" href="icon/favicon.ico" />
				<META http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<link href="./templates/{$template}/css/style.css" rel="stylesheet" type="text/css" media="screen" />
				<STYLE type="text/css"> DIV.wrapper { MARGIN: 0px auto; WIDTH: 950px }
					#sidecol { WIDTH: 25%
					}
					#main-column {
						MARGIN-LEFT: 25%
					}
				</STYLE>

				<!--  APPLICO I RIFERIMENTI AI STYLESHEETS -->
				<xsl:apply-templates select="styles/css" />
				<xsl:apply-templates select="redirect" />

			</HEAD>
			
			<BODY class="f-default ">
				<xsl:apply-templates select="header" />
				<div id="page">
					<div id="page-bgtop">
						<div id="page-bgbtm">
							<xsl:apply-templates select="navbar" />
							<xsl:apply-templates select="content" />
						</div>
					</div>
				</div>

				<xsl:apply-templates select="footer" />
			</BODY>

		</html>
	</xsl:template>

	<xsl:template match="header">
		<div id="header">
			<div id="logo">
				<h1><a href="#">Begrimed   </a></h1>
				<p> design by <a href="http://www.freecsstemplates.org/">Free CSS Templates</a></p>
			</div>
			<div id="search">
				<form method="get" action="">
					<fieldset>
					<input type="text" name="s" id="search-text" size="15" />
					<input type="submit" id="search-submit" value="GO" />
					</fieldset>
				</form>
			</div>
		</div>
		<!-- end #header -->
		<div id="menu">
			<ul>
				<xsl:for-each select="div[@id='mainbar']/a">
		    		<li><xsl:copy-of select="." /></li>
		    	</xsl:for-each>
			</ul>
		</div>
		<xsl:copy-of select="div[@id='briciole']" />
	</xsl:template>

	<xsl:template match="navbar">
		<div id="sidebar">	
			<xsl:copy-of select="*" /> 
  		</div> 
	</xsl:template>

	<xsl:template match="content">
		<!--Begin Main Content Area-->
		<div id="content">
			<xsl:for-each select="div/div">
				<div class="post" id="{@id}">
					<h2 class="title">
						<xsl:copy-of select="div/h2[@class='sezione_titolo']" />
					</h2>
	    			<xsl:copy-of select="div[@class='sezione_testo']" />
	    			
	    			<xsl:copy-of select="."></xsl:copy-of>
	    		</div>
	    	</xsl:for-each>

		</div>
	</xsl:template>

	<xsl:template match="footer">
		<div id="bottom">
			<div class="wrapper">
				<div align="center">
					<A class="nounder" title="{text}" href="/">
						<IMG id="jopac" alt="{leftImg/@alt}" src="./templates/{$template}/{leftImg}"/>
					</A>
				</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="css">
		<!-- <xsl:variable name="nomefile">
			<xsl:value-of select="text()" />
		</xsl:variable>
		 -->
		<link href="/templates/{$template}/css/import.css" rel="stylesheet" type="text/css" />
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