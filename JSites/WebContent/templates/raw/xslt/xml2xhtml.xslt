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
		
		<xsl:param name="pid"/>
		
		<xsl:param name="pageid"/>
		<xsl:param name="pagecode"/>
		<xsl:param name="name"/>
		<xsl:param name="pageusername"/>
		<xsl:param name="pagechangeip"/>
		<xsl:param name="pagedate"/>
		<xsl:param name="pageresp"/>
		
		<xsl:include href="sidebar.xslt"/>
	
	<xsl:template match="/page">
		<html lang="it">
			<head>
				<title>
					<xsl:value-of select="$sitename" />
					-
					<xsl:value-of select="/page/content/@pageTitle" />
				</title>
				<!--  <meta content="catalogo on line per biblioteche" name="description" />
				<meta content="biblioteca, libro, ricerca, opac" name="keywords" />
				<meta content="index, follow" name="robots" />
				<link rel="shortcut icon" href="icon/favicon.ico" />
				<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<link href="./templates/{$template}/css/style.css" rel="stylesheet" type="text/css" media="screen" />
				<link href="./templates/{$template}/css/menu_style.css" rel="stylesheet" type="text/css" media="screen" /> -->
				

				<!--  APPLICO I RIFERIMENTI AI STYLESHEETS -->
				<xsl:call-template name="css" />
				<xsl:apply-templates select="redirect" />

			</head>
			
			<body>
				<div id="wrapper">
					<!--  <xsl:apply-templates select="header" /> -->
					
					
					<div id="page">
						<div id="page-bgtop">
							<div id="page-bgbtm">
								<xsl:apply-templates select="content" />
							</div>
						</div>
					</div>
					<!--  <xsl:apply-templates select="navbar" /> -->
					
				</div>
				<xsl:apply-templates select="footer" />
			</body>

		</html>
	</xsl:template>

	<xsl:template match="header">
		<div id="header">
			<div id="logo">
				<img src="./images/page1/logo.png" alt="{$sitename}"/>
			</div>
		</div>
		<!-- end #header -->
		<div class="menu">
			<ul>
		
		
				<xsl:apply-templates select="/page/navbar/navigator/navpage"/>
				<!-- <li class="current_page_item"><a href="#">Home</a></li>
				<li><a href="#">Blog</a></li>
				<li><a href="#">Photos</a></li>
				<li><a href="#">About</a></li>
				<li><a href="#">Links</a></li>
				<li><a href="#">Contact</a></li>
				 -->
			</ul>
		</div>
		<!-- end #menu -->
		<xsl:copy-of select="div[@id='briciole']" />
	</xsl:template>

	<xsl:template match="content">
		<!--Begin Main Content Area-->
		<div id="content">
		
			<xsl:copy-of select="*" />
			
			<!--  <xsl:copy-of select="div[@class='dbManager']"/>
			
			<xsl:copy-of select="form[@id='editform']"/>
			
			<xsl:for-each select="div/div">
				<div class="post" id="{@id}">
					<h2 class="title">
						<xsl:copy-of select="div/h2[@class='sezione_titolo']" />
					</h2>
	    			<xsl:copy-of select="div[@class='sezione_testo']" />
	    			
	    			<xsl:copy-of select="."></xsl:copy-of>
	    		</div>
	    	</xsl:for-each>
			-->
		</div>
	</xsl:template>

	<xsl:template match="footer">
		<div id="footer">
			<xsl:copy-of select="*" />
		</div>
	<!-- end #footer -->
	</xsl:template>

	<xsl:template name="css">
		<!-- <xsl:variable name="nomefile">
			<xsl:value-of select="text()" />
		</xsl:variable>
		 -->
		<link href="./templates/{$template}/css/sezione.css" rel="stylesheet" type="text/css" />
		<link href="./templates/{$template}/css/tabella.css" rel="stylesheet" type="text/css" />
		<link href="./templates/{$template}/css/editing.css" rel="stylesheet" type="text/css" />

		<link href="./templates/{$template}/css/dbManager.css" rel="stylesheet" type="text/css" />
		<link href="./templates/{$template}/css/calendar-blue2.css" rel="stylesheet" type="text/css" />
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