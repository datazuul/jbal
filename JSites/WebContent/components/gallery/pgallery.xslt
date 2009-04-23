<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                              xmlns:dir="http://apache.org/cocoon/directory/2.0">
	
	<xsl:param name="pid" />
	<xsl:param name="cid" />
	<xsl:param name="pacid" />
	<xsl:param name="context" />
	<xsl:param name="base" />
	
	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="dir:directory">
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="dir:file">
		<xsl:if test="@width &gt; 0">
			<a href="{$context}{$base}/{@name}">
				<img src="{$context}/{$base}/{@name}" width="100px" /><xsl:text> </xsl:text>
			</a>
		</xsl:if>
	</xsl:template>
	
	<!-- 
		<dir:directory name="images" lastModified="1189679598000" date="13/09/07 12.33" size="578" sort="name" reverse="false" requested="true">
<dir:directory name="CVS" lastModified="1189678446000" date="13/09/07 12.14" size="170"/>
<dir:file name="background.gif" lastModified="1165871327000" date="11/12/06 22.08" size="103"/>
<dir:file name="cocoon.gif" lastModified="1165871327000" date="11/12/06 22.08" size="4102"/>
<dir:directory name="contentimg" lastModified="1189678446000" date="13/09/07 12.14" size="136"/>
<dir:file name="jopac.jpg" lastModified="1165871327000" date="11/12/06 22.08" size="40028"/>
<dir:file name="jopac2.jpg" lastModified="1165871327000" date="11/12/06 22.08" size="3600"/>
<dir:file name="jopac_left.jpg" lastModified="1165871327000" date="11/12/06 22.08" size="2228"/>
<dir:file name="jopac_right.jpg" lastModified="1165871327000" date="11/12/06 22.08" size="2206"/>
<dir:file name="logo.jpg" lastModified="1189679598000" date="13/09/07 12.33" size="22618"/>
<dir:file name="menu_bg1.png" lastModified="1165442428000" date="06/12/06 23.00" size="757"/>
<dir:file name="menu_bg2.png" lastModified="1165442428000" date="06/12/06 23.00" size="354"/>
<dir:file name="menu_bg3.png" lastModified="1165442428000" date="06/12/06 23.00" size="448"/>
<dir:file name="menu_bg4.png" lastModified="1165442428000" date="06/12/06 23.00" size="461"/>
<dir:file name="powered.gif" lastModified="1165871327000" date="11/12/06 22.08" size="2338"/>
<dir:file name="separatore.gif" lastModified="1165442428000" date="06/12/06 23.00" size="58"/>
</dir:directory>
	 -->

</xsl:stylesheet>