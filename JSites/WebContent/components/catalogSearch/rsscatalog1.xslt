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

	<xsl:template match="record">
		<item>
			<title>
				<xsl:value-of select="title" />
			</title>
			<description>
				<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
				<xsl:text disable-output-escaping="yes">&lt;table/&gt;&lt;tr&gt;&lt;td valign="top"&gt;</xsl:text>
				<xsl:text disable-output-escaping="yes">&lt;img alt="" src="</xsl:text><xsl:value-of select="concat($requesturi,image)" />" <xsl:text disable-output-escaping="yes"> /&gt;</xsl:text>
				<xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;td valign="top"&gt;</xsl:text>
					
					<xsl:value-of select="authors"  disable-output-escaping="yes"/><xsl:text disable-output-escaping="yes">&lt;br/&gt;</xsl:text>
					<xsl:value-of select="ISBD"  disable-output-escaping="yes"/><xsl:text disable-output-escaping="yes">&lt;br/&gt;</xsl:text>
					<xsl:value-of select="standardNumber" /><xsl:text disable-output-escaping="yes">&lt;br/&gt;</xsl:text>
					<xsl:value-of select="comments"  disable-output-escaping="yes"/><xsl:text disable-output-escaping="yes">&lt;br/&gt;</xsl:text>
					
					
				<xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;/tr&gt;&lt;/table/&gt;</xsl:text>	
					
				<xsl:value-of select="abstract" disable-output-escaping="yes"/><xsl:text disable-output-escaping="yes">&lt;br/&gt;</xsl:text>
				
					
				<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
				
					<!--  xsl:value-of select="." / -->
				<!--  <xsl:text disable-output-escaping="yes">]]&gt;</xsl:text> -->
			</description>
			<link><xsl:value-of select="$requesturi" />/dettaglio<xsl:value-of select="$pcode" />?query=JID=<xsl:value-of select="jid" /></link>
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
