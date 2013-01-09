<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:sql="http://apache.org/cocoon/SQL/2.0"
							xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
							xmlns:fn="http://www.w3.org/2006/xpath-functions">

    <xsl:include href="../../stylesheets/xslt/editlinks.xslt" />
    <!-- Questo include serve pei link de modifica, elimina e (dis)attiva 
		 ma anca per le combo de ordinamento  -->
                              
	<xsl:param name="cid"/>
	<xsl:param name="pid"/>
	<xsl:param name="pacid"/>
	<xsl:param name="editing"/>
	<xsl:param name="lang"/>
    <xsl:param name="validating"/>
    <xsl:param name="disabling"/>
    <xsl:param name="time"/>
    <xsl:param name="extra" />
    <xsl:param name="container" />
    <xsl:param name="context" />
    <xsl:param name="pcode" />
    <xsl:param name="requesturi" />
    <xsl:param name="querystring" />
    <xsl:param name="site" />
    
	<xsl:template match="/">
		<rss version="2.0"
					xmlns:content="http://purl.org/rss/1.0/modules/content/"
					xmlns:wfw="http://wellformedweb.org/CommentAPI/">
				<xsl:apply-templates select="root" />
		</rss>
			
		
	</xsl:template>
	
	<xsl:template match="root">
		<channel>
			<title><xsl:value-of select="$site" /> - <xsl:value-of select="catalogName" /></title>
			<link><xsl:value-of select="querystring" /></link>
			<description><![CDATA[Feed RSS - ]]><xsl:value-of select="$site" /> - <xsl:value-of select="catalogName" /></description>
		
			<xsl:apply-templates />
		
		</channel>
		
	</xsl:template>
	
	<xsl:template match="resultSet">
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template name="replace-string">
    <xsl:param name="text"/>
    <xsl:param name="replace"/>
    <xsl:param name="with"/>
    <xsl:choose>
      <xsl:when test="contains($text,$replace)">
        <xsl:value-of select="substring-before($text,$replace)"/>
        <xsl:value-of select="$with"/>
        <xsl:call-template name="replace-string">
          <xsl:with-param name="text"
select="substring-after($text,$replace)"/>
          <xsl:with-param name="replace" select="$replace"/>
          <xsl:with-param name="with" select="$with"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$text"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
	
	<xsl:template match="record">
		<item>
			<title>
				
			</title>
			<description>
				<!--  <xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text> -->
				<xsl:call-template name="replace-string"> <!-- imported template -->
        <xsl:with-param name="text" select="."/>
        <xsl:with-param name="replace" select="'images/pubimages'"/>
        <xsl:with-param name="with" select="concat($requesturi,'/images/pubimages')"/>
      </xsl:call-template>
				
					<!--  xsl:value-of select="." / -->
				<!--  <xsl:text disable-output-escaping="yes">]]&gt;</xsl:text> -->
			</description>
			<link>http://<xsl:value-of select="$requesturi" />/pageview?pid=<xsl:value-of select="@pid" />#<xsl:value-of select="@cid" /></link>
			<pubDate>
				
			</pubDate>
		</item>

	</xsl:template>
	

	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>


</xsl:stylesheet>
