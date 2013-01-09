<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:output
		method="xml"
		omit-xml-declaration="yes"
		indent="yes"/> 
	
	<xsl:template name="units-style">
		<script type="text/javascript" src="./templates/{$template}/script/styleswitcher.js" />
		<script type="text/javascript" src="./templates/{$template}/script/menutendina.js" />
		
		<link rel="stylesheet" type="text/css" href="http://www.units.it:80/css/main.css" media="screen" />
		<link rel="stylesheet" type="text/css" href="http://www.units.it:80/css/httpmain.css" media="screen" />
		<link rel="stylesheet" type="text/css" href="http://www.units.it:80/css/printmain.css" media="print" />
		<link rel="stylesheet" type="text/css" href="http://www.units.it:80/css/menutop.css" media="screen" />

		<!-- css template -->
                <link rel="stylesheet" href="./templates/{$template}/css/style.css" type="text/css" media="screen" />

		<link href="./templates/{$template}/css/sezione.css" rel="stylesheet" type="text/css" />
		<link href="./templates/{$template}/css/tabella.css" rel="stylesheet" type="text/css" />
		<link href="./templates/{$template}/css/editing.css" rel="stylesheet" type="text/css" />

		<link href="./templates/{$template}/css/dbManager.css" rel="stylesheet" type="text/css" />
		<link href="./templates/{$template}/css/calendar-blue2.css" rel="stylesheet" type="text/css" />		


		<link rel="alternate stylesheet" title="small" type="text/css" href="http://www.units.it:80/css/small.css" media="screen" />
		<link rel="alternate stylesheet" title="large" type="text/css" href="http://www.units.it:80/css/large.css" media="screen" />
		<link rel="alternate stylesheet" title="xlarge" type="text/css" href="http://www.units.it:80/css/xlarge.css" media="screen" />
		<link rel="alternate" type="application/rss+xml" title="Agenda Eventi" href="http://www.units.it/feed/rssfeed.php/?tipo=7" />
		<link rel="alternate" type="application/rss+xml" title="Comunicati stampa" href="http://www.units.it/feed/rssfeed.php/?tipo=2" />
		<link rel="alternate" type="application/rss+xml" title="Avvisi" href="http://www.units.it/feed/rssfeed.php/?tipo=3" />
		
		
		<xsl:text disable-output-escaping="yes"><![CDATA[<!--[if IE 6]>]]></xsl:text><link rel="stylesheet" href="./templates/{$template}/css/style.ie6.css" type="text/css" media="screen" /><xsl:text disable-output-escaping="yes"><![CDATA[<![endif]-->]]></xsl:text>
  		<xsl:text disable-output-escaping="yes"><![CDATA[<!--[if IE 7]>]]></xsl:text><link rel="stylesheet" href="./templates/{$template}/css/style.ie7.css" type="text/css" media="screen" /><xsl:text disable-output-escaping="yes"><![CDATA[<![endif]-->]]></xsl:text>
		
		
		<link rel="stylesheet" href="./templates/{$template}/css/mymenu.css" type="text/css" media="screen" />		
		<link rel="stylesheet" href="./templates/{$template}/css/{$sitename}.css" type="text/css" media="screen" />	
	</xsl:template>
</xsl:stylesheet>
