<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:sql="http://apache.org/cocoon/SQL/2.0"
							xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

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
				<xsl:apply-templates select="newslist" />
		</rss>
			
		
	</xsl:template>
	
	<xsl:template match="newslist">
		<channel>
			<title><xsl:value-of select="$site" /></title>
			<link><xsl:value-of select="querystring" /></link>
			<description><![CDATA[Feed RSS - ]]><xsl:value-of select="$site" /></description>
		
			<xsl:apply-templates />
		
		</channel>
		
	</xsl:template>
	
	<xsl:template match="newsitem">
		<item>
			<title>
				<xsl:value-of select="div/div[@class='sezione']/h2[@class='sezione_titolo']" />
				<xsl:value-of select="div/div[@class='sezione']/h1[@class='sezione_titolo']" />
				<xsl:value-of select="div/div[@class='sezione']/div[@class='sezione_contenuto']/h2[@class='sezione_titolo']" />
				<xsl:value-of select="div/div[@class='sezione']/div[@class='sezione_contenuto']/h1[@class='sezione_titolo']" />
			</title>
			<description>
				<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
					<xsl:value-of select="div/div[@class='sezione']/div[@class='sezione_testo']" />
					<xsl:value-of select="div/div[@class='sezione']/div[@class='sezione_contenuto']/div[@class='sezione_testo']" />
				<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
			</description>
			<link>http://<xsl:value-of select="$requesturi" />/pageview?pid=<xsl:value-of select="@pid" />#<xsl:value-of select="@cid" /></link>
			<pubDate><xsl:variable name="idate">
						<xsl:value-of select="substring-before(@insertionDate,' ')" />
					</xsl:variable>
					<xsl:value-of select="substring($idate,9,2)" />/<xsl:value-of select="substring($idate,6,2)" />/<xsl:value-of select="substring($idate,1,4)" />
			</pubDate>
		</item>

	</xsl:template>
	

	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>


</xsl:stylesheet>
