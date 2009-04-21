<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
								xmlns:source="http://apache.org/cocoon/source/1.0">
								
	<xsl:variable name="list">
		<xsl:choose>
			<xsl:when test="string-length(save/list) &gt; 0">1</xsl:when>
			<xsl:otherwise>0</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:param name="datadir"/>
	
	<xsl:template match="/save">
		<source:write>
	    <source:source><xsl:value-of select="$datadir" />/data/section<xsl:value-of select="cid/text()"/>.xml
	    </source:source>
	    <source:fragment>
			<section>
				<xsl:apply-templates/>
			</section>
	    </source:fragment>
		</source:write>
		
		<sql:execute-query xmlns:sql="http://apache.org/cocoon/SQL/2.0">
			<sql:query>
				insert into tblnews (CID,startdate,enddate,list) values(<xsl:value-of select="cid/text()"/>,
					'<xsl:value-of select="startdate" />',
					'<xsl:value-of select="enddate" />',
					<xsl:value-of select="$list" />)
			</sql:query>
		</sql:execute-query>
		<sql:execute-query xmlns:sql="http://apache.org/cocoon/SQL/2.0">
			<sql:query>
				update tblnews set 
					startdate = '<xsl:value-of select="startdate" />',
					enddate = '<xsl:value-of select="enddate" />',
					list = <xsl:value-of select="$list" />
					where cid = <xsl:value-of select="cid/text()"/>
			</sql:query>
		</sql:execute-query>
	</xsl:template>
	
	<xsl:template match="startdate" />
	<xsl:template match="enddate" />
	<xsl:template match="list" />
	
	<xsl:template match="text">
		<testo>
			<xsl:value-of select="text()"/>
		</testo>
	</xsl:template>
	
	<xsl:template match="title">
		<titolo>
			<xsl:attribute name="type">
				<xsl:value-of select="@type"/>
			</xsl:attribute>
			<xsl:value-of select="text()"/>
		</titolo>
	</xsl:template>
	
	<xsl:template match="prevImage">
		<img link="{../imglink}">
			<xsl:value-of select="text()"/>
		</img>
	</xsl:template>
	
	<xsl:template match="text()"/>

	<xsl:template match="@*|node()|text()">
		<!-- xsl:copy-->
			<xsl:apply-templates select="@*|node()|text()|*" />
		<!-- /xsl:copy-->
	</xsl:template>

</xsl:stylesheet>