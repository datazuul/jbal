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
				<LINK href="./templates/{$template}/css/template_css.css" type="text/css" rel="stylesheet" />
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

				<xsl:apply-templates select="navbar" />
				<xsl:apply-templates select="content" />

				<xsl:apply-templates select="footer" />
			</BODY>

		</html>
	</xsl:template>

	<xsl:template match="header">
		<!--Begin Menu-->
		<DIV id="menu-bar">
		  <DIV class="wrapper">
		    <DIV class="splitmenu" id="horiz-menu">
		    
		    <ul class="nav" id="horiznav">
		    
		   
		    	<xsl:for-each select="div[@id='mainbar']">
		    		<LI class="bg0"><span><xsl:copy-of select="." /></span></LI>
		    	</xsl:for-each>
			</ul>

		    </DIV>
		  </DIV>
		</DIV>
		<!--End Menu-->
		<!--Begin Inset Area-->
		<DIV id="inset">
		  <DIV class="wrapper">
		  	<A class="nounder" 
				href=".">
				<IMG id="logo" style="BORDER-WIDTH: 0px;" alt="" src="./templates/{$template}/images/content/blank.gif" /></A>
		    <!--<DIV class="content"><a href="link" target="_blank"><img alt=Advertisement src="/images/content/osmbanner1.png" 
		border="0"></a></DIV>-->
		  </DIV>
		  <xsl:copy-of select="div[@id='briciole']" />
		</DIV>
		
		<!--End Inset Area-->
	

	</xsl:template>

	<xsl:template match="navbar">
 
 
		<!-- <div id="sidebar">  -->
			
			<!-- <xsl:copy-of select="*" />  -->
		<!--  </div> -->

	</xsl:template>

	<xsl:template match="content">
		<!--Begin Main Content Area-->
		<DIV id="content">
			<DIV class="wrapper">
				<DIV id="sidecol">
					<DIV id="side-column">
						<DIV class="padding">
							<DIV class="inner">
								<DIV class="moduletable">
									<H3>Main Menu</H3>
									<A class="mainlevel" id="active_menu" href=".">Home</A>
									<xsl:for-each select="/page/navbar/a"><!-- sublevel -->
										<xsl:if test="@class = 'navbar1lev'">
											<a href="{@href}" class="mainlevel">
												<xsl:value-of select="text()" />
											</a>
										</xsl:if>
										<xsl:if test="@class = 'navbar2lev'">
											<a href="{@href}" class="sublevel">
												<xsl:value-of select="text()" />
											</a>
										</xsl:if>
										<xsl:if test="@class = 'navbarhover1lev'">
											<a href="{@href}" class="mainlevel">
												<xsl:value-of select="text()" />
											</a>
										</xsl:if>
										<xsl:if test="@class = 'navbarhover2lev'">
											<a href="{@href}" class="sublevel">
												<xsl:value-of select="text()" />
											</a>
										</xsl:if>
									</xsl:for-each>
								</DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
				<DIV id="main-column">
					<DIV class="padding">
						<DIV class="inner">
							<DIV id="top">
								<DIV class="block">
									<DIV class="moduletable">
										<xsl:copy-of select="*" />
									</DIV>
								</DIV>
								<DIV class="clr"></DIV>
							</DIV>
						</DIV>
					</DIV>
				</DIV>
			</DIV>
			<!--End Main Content Area-->
			<DIV class="clr"></DIV>
		
	<!--  
		
			<div id="content">
				<xsl:copy-of select="*" />
			</div>
	-->
		</DIV>
	</xsl:template>

	<xsl:template match="footer">
		<div id="bottom">
			<div class="wrapper">
				<div align="center">
					<A class="nounder" title="{text}" href="/">
						<IMG id="jopac" alt="{leftImg/@alt}" src="./templates/{$template}/images/jopac.png"/>
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