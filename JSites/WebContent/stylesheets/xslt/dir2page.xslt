<?xml version="1.0"?>
<!--
  Copyright 2002-2004 The Apache Software Foundation

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->

<xsl:stylesheet version="2.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:dir="http://apache.org/cocoon/directory/2.0">
 
 <!-- 
 
 −
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
  
  <xsl:param name="base" />
  <xsl:param name="context" />
  <xsl:param name="permissionCode" />
 
  <xsl:template match="/">
  	<xsl:choose>
  	<xsl:when test="$permissionCode != 7">
  		<html>
	    	<head>
	    		<title>Image Directory</title>
	    		<meta http-equiv="refresh" content="0;URL={$context}" />
	    	</head>
	    	<body/>
	    </html>
  	</xsl:when>
  	<xsl:otherwise>
	    <html>
	    	<head>
	    		<title>Image Directory</title>
	    		<link rel="stylesheet" type="text/css" media="all" href="{$context}/css/dir2page.css" title="dir2page" />
	    	</head>
	    <body>
	      <h1>Image Directory</h1>
	      	<table class="dirtable">
	      		<tr class="tabhead">
	      			<th>Name</th>
	      			<th>Type</th>
	      			<th>Date</th>
	      			<th>Size</th>
	      			<th>Uri</th>
	      		</tr>
	      		<xsl:apply-templates/>
	      	</table>
	      	
	      	
	      	<form class="uploadform" action="upload" method="POST" enctype="multipart/form-data">
				<input type="file" name="upload-file" />
				<input type="hidden" name="base" value="{$base}" />
				<input type="submit" name="action" value="Upload" />
			</form>
			<br/>
			<form class="uploadform" action="upload" method="POST" enctype="multipart/form-data">
				<input type="file" name="upload-file" />
				<input type="hidden" name="base" value="{$base}" />
				<input type="hidden" name="unzip" value="true" />
				<input type="submit" name="action" value="Upload and UNZIP" />
			</form>
	      	
	    </body></html>
	</xsl:otherwise>
	</xsl:choose>
	    
  </xsl:template>

  <xsl:template match="dir:directory">
  	<xsl:if test="not(@requested)">
  		<tr class="dirrow">
	  		<td><a href="{@name}/"><xsl:value-of select="@name" /></a></td>
	  		<td>DIR</td>
		  	<td><xsl:value-of select="@date" /></td>
		  	<td><xsl:value-of select="@size" /></td>
		  	<td/>
	  	</tr>
  	</xsl:if>
  	<xsl:if test="@requested">
  		<tr class="dirrow">
	  		<td><a href="..">..</a></td>
		  	<td>DIR</td>
		  	<td></td>
		  	<td></td>
		  	<td/>
	  	</tr>
  	</xsl:if>
  	<xsl:apply-templates />
    
  </xsl:template>
  
   <xsl:template match="dir:file">
   		<tr class="filerow">
		  	<td>
		  		<xsl:variable name="te"><xsl:value-of select="substring(@name,string-length(@name)-3)" /></xsl:variable>
		  		<xsl:choose>
		  			
			  		<xsl:when test="$te = '.gif' or $te = '.jpg' or $te = '.png'">
			  			<a href="{$context}/{$base}{@name}"><xsl:value-of select="@name" /></a>
			  		</xsl:when>
			  		<xsl:otherwise>
			  			<xsl:value-of select="@name" />
			  		</xsl:otherwise>
		  		</xsl:choose>
		  	</td>
		  	<td>FILE</td>
		  	<td><xsl:value-of select="@date" /></td>
		  	<td><xsl:value-of select="@size" /></td>
		  	<td><xsl:value-of select="$base" /><xsl:value-of select="@name" /></td>
    	</tr>
  </xsl:template>



</xsl:stylesheet>
