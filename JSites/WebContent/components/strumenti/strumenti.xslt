<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:url="http://whatever/java/java.net.URLEncoder" 
								exclude-result-prefixes="url">
								
<!-- info importanti sull'url-encoding http://sources.redhat.com/ml/xsl-list/2002-05/msg01054.html -->
                              
	<xsl:param name="logged" />
	<xsl:param name="pid" />
	<xsl:param name="lang" />
	
	<xsl:template match="/">
		<xsl:apply-templates select="strumenti"/>
	</xsl:template>
	
	<xsl:template match="strumenti">
		<div id="strumenti">
			<xsl:apply-templates />
		</div>

	</xsl:template>
		
	<!--  PIPELINE PER I NORMALI LINK -->
	<xsl:template match="link">
		<a class="strumenti1lev" href="{concat(@href,'%3Flang%3D',$lang)}"><xsl:value-of select="@name"/></a>
		<xsl:if test="count(following::*) != 0"><img src="images/separatore.gif" alt="separatore" class="strumentiimg2"/></xsl:if>
	</xsl:template>
	
	<!--  PIPELINE PER IL BOTTONE LOGIN/LOGOUT (USA IL PARAMETRO LOGGED) -->
	<xsl:template match="login-logout">
		<xsl:variable name="url">pageview?pid=<xsl:value-of select="$pid" />&amp;lang=<xsl:value-of select="lang" /></xsl:variable>
			<xsl:choose>
				<xsl:when test="$logged = 'true'">Benvenuto
					<page xmlns:session="http://apache.org/cocoon/session/1.0">
    					<session:getxml context="authentication" path="/authentication/data/displayName"/>
					</page>
					<xsl:text> </xsl:text>
					<page xmlns:session="http://apache.org/cocoon/session/1.0">
    					(<session:getxml context="authentication" path="/authentication/data/mail"/>)
					</page>
					<xsl:text> </xsl:text>
					<a class="strumenti1lev" href="do-logout?resource={url:encode($url)}">Logout</a>
				</xsl:when>
				<xsl:otherwise>
					<a class="strumenti1lev" href="login?resource={url:encode($url)}">Login</a>
				</xsl:otherwise>
			</xsl:choose>
		<img src="images/separatore.gif" alt="separatore" class="strumentiimg2"/>
	</xsl:template>

</xsl:stylesheet>