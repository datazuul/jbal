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
 
 <save loadFromFile="true">

	<upload-file>
/java_source/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/work/Catalina/localhost/JSites/cocoon-files/upload-dir/Analisi di accessibilità.pdf
</upload-file>
<action>Upload</action>
<base>images/contentimg/</base>
</save>
 
  -->
  

  <xsl:param name="components-dir" />
 
  <xsl:template match="/">
    <root>
    	<source><xsl:value-of select="save/upload-file" /></source>
    	<destination><xsl:value-of select="$components-dir" />/<xsl:value-of select="save/base" /><xsl:value-of select="save/filename" /></destination>
    	<base><xsl:value-of select="save/base" /></base>
    	<unzip><xsl:value-of select="save/unzip" /></unzip>
    </root>
  </xsl:template>



</xsl:stylesheet>
