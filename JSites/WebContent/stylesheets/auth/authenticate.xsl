<?xml version="1.0"?>
<!--
  Copyright 1999-2004 The Apache Software Foundation

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
<!-- $Id: authenticate.xsl,v 1.3 2008-03-15 20:10:57 romano Exp $ 

-->

<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<!-- Get the name from the request paramter -->
	<xsl:param name="name"/>
	<xsl:param name="password"/>
	
	<xsl:template match="authentication">
	  <authentication>
		<xsl:apply-templates select="users"/>
	  </authentication>
	</xsl:template>
	
	
	<xsl:template match="users">
	    <xsl:apply-templates select="user"/>
	</xsl:template>
	
	
	<xsl:template match="user">
	    <!-- Compare the name of the user -->
	    <xsl:if test="normalize-space(name) = $name">
	    	<xsl:if test="normalize-space(password) = $password">
		        <!-- found, so create the ID -->
		        <ID><xsl:value-of select="name"/></ID>
		        <data><xsl:apply-templates select="data"/></data>
	        </xsl:if>
	    </xsl:if>
	</xsl:template>
	
	<xsl:template match="data">
	    <displayName><xsl:value-of select="displayName"/></displayName>
		<username><xsl:value-of select="username"/></username>
		<mail><xsl:value-of select="mail"/></mail>
	</xsl:template>

</xsl:stylesheet>
