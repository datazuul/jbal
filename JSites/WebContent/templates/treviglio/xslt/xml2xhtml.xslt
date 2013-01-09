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
				<title>Comune di Treviglio: biblioteca</title>
				<META content="catalogo on line per biblioteche" name="description" />
				<META content="biblioteca, libro, ricerca, opac" name="keywords" />
				<META content="index, follow" name="robots" />
				<LINK rel="shortcut icon" href="icon/favicon.ico" />
				<META http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<LINK href="./templates/{$template}/css/treviglio.css" type="text/css" rel="stylesheet" />
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
		 <div id="testataTreviglio">
      <div id="imgSXTreviglio">
        <a href="http://www.comune.treviglio.bg.it/">
        	<img id="imgTestataSxTreviglio" alt="Stemma e logo del comune di Treviglio" src="templates/treviglio/images/testata.gif" border="0"/></a>
      </div><!-- id = imgSXTreviglio XXXX class =  -->
    </div>
    <div id="barraInformativa">
      <ul class="menuOrizzontaleSX">
        <li>
          <a tabindex="tabindex" href="http://www.comune.treviglio.bg.it">
            <img alt="vai alla home page" src="templates/treviglio/images/bullet.gif" border="0"/>&#160;
            <span>home page</span>
          </a>
        </li>
        <li>
          <a tabindex="1" href="mailto:segreteria.sindaco@comune.treviglio.bg.it">
            <img alt="vai allla pagina dove puoi inviare messaggi al comune" src="templates/treviglio/images/bullet.gif" border="0"/>&#160;
            <span>contattaci </span>
          </a>
        </li>
        <li>
          <a tabindex="2" href="http://www.comune.treviglio.bg.it/index.php?pagina=forum">
            <img alt="vai al forum" src="templates/treviglio/images/bullet.gif" border="0"/>&#160;
            <span>forum</span>
          </a>
        </li>
        <li>
          <a tabindex="3" href="http://www.comune.treviglio.bg.it/index.php?pagina=newsletter">
            <img alt="vai alla newsletter" src="templates/treviglio/images/bullet.gif" border="0"/>&#160;
            <span>newsletter</span>
          </a>
        </li>
      </ul>
      <span>Sede comunale: piazza L. Manara 1, 24047 Treviglio (BG) - tel. 0363/3171 - fax 0363/317309 - P. IVA: 00230810160 </span>
    </div>    
    <div style="text-align: left; margin-left: 5px; margin-top: 3px;">
        
            
            <br/>
        
    </div>   
    
		<!--Begin Menu-->
<!-- 		<DIV id="menu-bar">
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
 -->
		<!--End Menu-->
		<!--Begin Inset Area-->
<!-- 		<DIV id="inset">
		  <DIV class="wrapper">
		  	<A class="nounder" 
				href=".">
				<IMG id="logo" style="BORDER-WIDTH: 0px;" alt="" src="./templates/{$template}/images/content/blank.gif" /></A>

		  </DIV>
		  <xsl:copy-of select="div[@id='briciole']" />
		</DIV> -->
		
		<!--End Inset Area-->
	

	</xsl:template>

	<xsl:template match="navbar">
 
 
		<!-- <div id="sidebar">  -->
			
			<!-- <xsl:copy-of select="*" />  -->
		<!--  </div> -->

	</xsl:template>

	<xsl:template match="content">
		<!--Begin Main Content Area-->
		<DIV id="leftBox">
			
			<h2>Main Menu</h2>
			<A class="mainlevel" id="active_menu" href=".">Home</A>
			<xsl:for-each select="/page/navbar/a"><!-- sublevel -->
				<xsl:if test="@class = 'navbar1lev'">
					<h2>
						<a href="{@href}" class="mainlevel">
							<xsl:value-of select="text()" />
						</a>
					</h2>
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

			<!--End Main Content Area-->
			<DIV class="clr"></DIV>
		</DIV>
	</xsl:template>

	<xsl:template match="footer">
	<div id="piedipagina">
			      <ul>
			        <li>
			          <a tabindex="1000" href="https://webmail.comune.treviglio.bg.it/">webmail</a>
			          <span> | </span>
			        </li>
			        <li>
			          <a tabindex="1001" href="http://www.comune.treviglio.bg.it/index.php?pagina=personalizzazioni">personalizzazioni</a>
			          <span> | </span>
			          <a tabindex="1002" href="http://www.comune.treviglio.bg.it/index.php?pagina=tastiAccessoRapido">accesso rapido</a>
			          <span> | </span>
			        </li>
			        <li>
			          <a tabindex="1003" href="http://www.comune.treviglio.bg.it/index.php?pagina=mappadelsito">mappa del sito</a>
			          <span> | </span>
			        </li>
			        <li>
			          <a tabindex="1004" href="http://www.comune.treviglio.bg.it/index.php?pagina=accessibilita">accessibilità</a>
			          <span> | </span>
			        </li>
			        <li>
			          <a target="_blank" tabindex="1003" href="http://www.comune.treviglio.bg.it/index_priv.php">area privata</a>
			        </li>
			      </ul>
			</div>
<!--  		<div id="bottom">
			<div class="wrapper">
				<div align="center">
					<A class="nounder" title="{text}" href="/">
						<IMG id="jopac" alt="{leftImg/@alt}" src="./templates/{$template}/{leftImg}"/>
					</A>
				</div>
			</div>
		</div>
-->
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