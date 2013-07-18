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
			<xsl:variable name="idate">
                    <xsl:value-of select="substring-before(@insertionDate,' ')" />
            </xsl:variable>
            <xsl:variable name="day"><xsl:value-of select="substring($idate,9,2)" /></xsl:variable>
            <xsl:variable name="month"><xsl:value-of select="substring($idate,6,2)" /></xsl:variable>
            <xsl:variable name="year"><xsl:value-of select="substring($idate,1,4)" /></xsl:variable>
            <xsl:variable name="dayname">
            <xsl:call-template name="get-day-of-the-week-abbreviation">
                    <xsl:with-param name="day-of-the-week">
                    <xsl:call-template name="calculate-day-of-the-week">
                            <xsl:with-param name="day"><xsl:value-of select="$day" /></xsl:with-param>
                            <xsl:with-param name="month"><xsl:value-of select="$month" /></xsl:with-param>
                            <xsl:with-param name="year"><xsl:value-of select="$year" /></xsl:with-param>
                    </xsl:call-template>
                    </xsl:with-param>
            </xsl:call-template>
            </xsl:variable>
            <xsl:variable name="monthname">
            <xsl:call-template name="get-month-name">
                    <xsl:with-param name="month"><xsl:value-of select="$month" /></xsl:with-param>
            </xsl:call-template>
            </xsl:variable>
            <pubDate><xsl:value-of select="$dayname" />, <xsl:value-of select="$day" /><xsl:text> </xsl:text><xsl:value-of select="$monthname" /><xsl:text> </xsl:text><xsl:value-of select="$year" /><xsl:text> </xsl:text><xsl:value-of select="substring-before(substring-after(@insertionDate,' '),'.')" /> +0000</pubDate>
		</item>

	</xsl:template>
	
  <xsl:template name="get-month-name">
    <xsl:param name="month"/>
   
    <xsl:choose>
      <xsl:when test="$month = 1">January</xsl:when>
      <xsl:when test="$month = 2">February</xsl:when>
      <xsl:when test="$month = 3">March</xsl:when>
      <xsl:when test="$month = 4">April</xsl:when>
      <xsl:when test="$month = 5">May</xsl:when>
      <xsl:when test="$month = 6">June</xsl:when>
      <xsl:when test="$month = 7">July</xsl:when>
      <xsl:when test="$month = 8">August</xsl:when>
      <xsl:when test="$month = 9">September</xsl:when>
      <xsl:when test="$month = 10">October</xsl:when>
      <xsl:when test="$month = 11">November</xsl:when>
      <xsl:when test="$month = 12">December</xsl:when>
      <xsl:otherwise>error: <xsl:value-of select="$month"/></xsl:otherwise>
    </xsl:choose>
   
  </xsl:template>
	
	<xsl:template name="calculate-day-of-the-week">
	    <xsl:param name="day"/>
	    <xsl:param name="month"/>
	    <xsl:param name="year"/>
	    
	    
	    <xsl:variable name="a" select="floor((14 - $month) div 12)"/>
	    <xsl:variable name="y" select="$year - $a"/>
	    <xsl:variable name="m" select="$month + 12 * $a - 2"/>
	   
	    <xsl:value-of select="($day + $y + floor($y div 4) - floor($y div 100) 
	    + floor($y div 400) + floor((31 * $m) div 12)) mod 7"/>
	   
	  </xsl:template>
	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template name="get-day-of-the-week-abbreviation">
    <xsl:param name="day-of-the-week"/>
   
    <xsl:choose>
      <xsl:when test="$day-of-the-week = 0">Sun</xsl:when>
      <xsl:when test="$day-of-the-week = 1">Mon</xsl:when>
      <xsl:when test="$day-of-the-week = 2">Tue</xsl:when>
      <xsl:when test="$day-of-the-week = 3">Wed</xsl:when>
      <xsl:when test="$day-of-the-week = 4">Thu</xsl:when>
      <xsl:when test="$day-of-the-week = 5">Fri</xsl:when>
      <xsl:when test="$day-of-the-week = 6">Sat</xsl:when>
      <xsl:otherwise>
          error: <xsl:value-of select="$day-of-the-week"/>
        </xsl:otherwise>
    </xsl:choose>
   
  </xsl:template>


</xsl:stylesheet>
