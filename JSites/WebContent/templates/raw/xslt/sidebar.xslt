<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml">
	
	<xsl:template match="navpage">
		<xsl:if test="@level = 1">
			<li class="parent"><span class="top"><a href="{@link}"><xsl:value-of select="@name" /></a></span></li>
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
	
	<!--  
		<xsl:template match="navpage">
			<li>
				<a href="{@link}">
					<xsl:value-of select="@name" />
				</a>
			
				<xsl:if test="count(navpage) > 0">
					<ul>
						<xsl:apply-templates />
					 </ul>
				</xsl:if>
			</li>			 
	</xsl:template>
	-->

	<xsl:template match="navbar">
		<div id="sidebar">
			<div id="menuHolder">
				<ul id="menuOuter">
					<li class="lv1-li"><!--[if lte IE 6]><a class="lv1-a" href="#url"><table><tr><td><![endif]-->
					<!--  <ul class="art-vmenu"> -->
						<ul id="menuInner">
							<xsl:for-each select="/page/navbar/navigator/navpage/navpage">
								<xsl:apply-templates select="." />
							</xsl:for-each>
						</ul>
					</li>
				</ul>
			</div>
			<!-- chiusura menu verticale -->
			<div class="cleared"></div>
			<xsl:copy-of select="." />
		</div>
		<div style="clear: both;">&#160;</div>
	</xsl:template>

</xsl:stylesheet>