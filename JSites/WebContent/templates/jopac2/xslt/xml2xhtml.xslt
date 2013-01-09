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
	
	<!-- 
	<map:parameter name="context" value="{context}"/>
	<map:parameter name="pid" value="{request-param:pid}"/>
   	<map:parameter name="template" value="{global:template}"/>
   	<map:parameter name="sitename" value="{global:sitename}"/>
   	<map:parameter name="pageid" value="{request-attr:pid}"/>
   	<map:parameter name="pagecode" value="{request-attr:pcode}"/>
   	<map:parameter name="pagename" value="{request-attr:name}"/>
   	<map:parameter name="pageusername" value="{request-attr:username}"/>
   	<map:parameter name="pagechangeip" value="{request-attr:remoteip}"/>
   	<map:parameter name="pagedate" value="{request-attr:insertdate}"/>
   	<map:parameter name="pageresp" value="{request-attr:resp}"/>
   	
	 -->
	
	
	<xsl:template match="/page">
		<html lang="it">
			<HEAD>
				<title><xsl:value-of select="$sitename" /> - <xsl:value-of select="/page/content/@pageTitle" /></title>
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
	
	<xsl:template match="navpage">
		<li><a href="{@link}"><xsl:value-of select="@name" /></a></li>
		<ul>
		<xsl:apply-templates />
		</ul>
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
									<ul>
									<xsl:apply-templates select="/page/navbar/navigator/navpage"/>
									</ul>
									<!--  
									<xsl:for-each select="/page/navbar/navigator/page">
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
									-->
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
		<div id="footer">
			<xsl:copy-of select="*" />
			<br/>
			Page last change <xsl:value-of select="$pagedate" /> by <xsl:value-of select="$pageusername"/>. Resp is <xsl:value-of select="$pageresp" />
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