<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:sql="http://apache.org/cocoon/SQL/2.0"
							xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
							xmlns:cinclude="http://apache.org/cocoon/include/1.0">

    <xsl:include href="../../stylesheets/xslt/editlinks0.xslt" />
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
    <xsl:param name="type" />
    <xsl:param name="container" />
    
	<xsl:template match="/">
		<div class="{$time}" style="height: {slideshow/height}px">
			<div class="sezione{$extra}" id="{$cid}">
				<xsl:apply-templates select="slideshow"/>
				<div class="clearer">&#160;</div>
			</div>
		</div>
		
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
	</xsl:template>
	
	<xsl:template match="titolo">
		<h2 class="sezione_titolo{$extra}"><xsl:value-of select="text()"/></h2>
	</xsl:template>
	
		
	<!-- <xsl:template match="section/lang"> -->
	<xsl:template match="slideshow">
		<xsl:apply-templates select="titolo"/>
		
		<link rel="stylesheet" type="text/css" href="./components/slideshow/css/slideshow.css" media="screen" />
		<script type="text/javascript" src="./components/slideshow/js/mootools-1.3.2-core.js"></script>
		<script type="text/javascript" src="./components/slideshow/js/mootools-1.3.2.1-more.js"></script>
		<script type="text/javascript" src="./components/slideshow/js/slideshow.js"></script>
		<script type="text/javascript" src="./components/slideshow/js/slideshow.kenburns.js"></script>
		<script type="text/javascript" src="./components/slideshow/js/slideshow.push.js"></script>
		<script type="text/javascript" src="./components/slideshow/js/slideshow.flash.js"></script>
		<script type="text/javascript" src="./components/slideshow/js/slideshow.fold.js"></script>
		
	
	<script type="text/javascript">		
<![CDATA[

// http://www.quirksmode.org/js/findpos.html

function findPos(obj) {
	var curleft = curtop = 0;

	if (obj.offsetParent) {

		do {
			curleft += obj.offsetLeft;
			curtop += obj.offsetTop;
		} while (obj = obj.offsetParent);
	}
	return [curleft,curtop];
}

	

	window.addEvent('domready', function(){
	    var data]]><xsl:value-of select="$cid" /><![CDATA[ = {
]]>
			<cinclude:include src="cocoon:/pslideshow/{dir}"/>
<![CDATA[
	    };
]]>

<xsl:variable name="_controller"><xsl:choose><xsl:when test="contains(controller,'true')">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="_thumbnails"><xsl:choose><xsl:when test="contains(thumbnails,'true')">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:variable>
<xsl:variable name="_captions"><xsl:choose><xsl:when test="contains(captions,'true')">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:variable>

<xsl:if test="contains(effect,'kenburns')">
<![CDATA[
	    var myShow]]><xsl:value-of select="$cid" /><![CDATA[ = new Slideshow.KenBurns('show]]><xsl:value-of select="$cid" /><![CDATA[', 
	    	data]]><xsl:value-of select="$cid" /><![CDATA[, 
	    	{ 
	    		captions: ]]><xsl:value-of select="$_captions" /><![CDATA[, 
	    		controller: ]]><xsl:value-of select="$_controller" /><![CDATA[, 
	    		delay: 7000, 
	    		duration: ]]><xsl:value-of select="duration" /><![CDATA[, 
	    		height: ]]><xsl:value-of select="height" /><![CDATA[, 
	    		hu: ']]><xsl:value-of select="dir" /><![CDATA[', 
	    		width: ]]><xsl:value-of select="width" /><![CDATA[ 
	    	});
]]>
</xsl:if>
<xsl:if test="contains(effect,'push')">
<![CDATA[
	    var myShow]]><xsl:value-of select="$cid" /><![CDATA[ = new Slideshow.Push('show]]><xsl:value-of select="$cid" /><![CDATA[', 
	    	data]]><xsl:value-of select="$cid" /><![CDATA[, 
	    	{ 
	    		transition: 'back:in:out',
	    		captions: ]]><xsl:value-of select="$_captions" /><![CDATA[, 
	    		controller: ]]><xsl:value-of select="$_controller" /><![CDATA[, 
	    		//delay: 7000, 
	    		duration: ]]><xsl:value-of select="duration" /><![CDATA[, 
	    		height: ]]><xsl:value-of select="height" /><![CDATA[, 
	    		hu: ']]><xsl:value-of select="dir" /><![CDATA[', 
	    		width: ]]><xsl:value-of select="width" /><![CDATA[ 
	    	});
]]>
</xsl:if>
<xsl:if test="contains(effect,'fade')">
<![CDATA[	    
	    var myShow]]><xsl:value-of select="$cid" /><![CDATA[ = new Slideshow('show]]><xsl:value-of select="$cid" /><![CDATA[', 
	    	data]]><xsl:value-of select="$cid" /><![CDATA[, 
	    	{
		    	controller: ]]><xsl:value-of select="$_controller" /><![CDATA[, 
		    	duration: ]]><xsl:value-of select="duration" /><![CDATA[, 
		    	height: ]]><xsl:value-of select="height" /><![CDATA[, 
		    	hu: ']]><xsl:value-of select="dir" /><![CDATA[', 
		    	thumbnails: ]]><xsl:value-of select="$_thumbnails" /><![CDATA[, 
		    	captions: ]]><xsl:value-of select="$_captions" /><![CDATA[, 
		    	width: ]]><xsl:value-of select="width" /><![CDATA[
		    });
]]>
</xsl:if>

<xsl:if test="contains(effect,'flash')">
<![CDATA[	    
	    var myShow]]><xsl:value-of select="$cid" /><![CDATA[ = new Slideshow.Flash('show]]><xsl:value-of select="$cid" /><![CDATA[', 
	    	data]]><xsl:value-of select="$cid" /><![CDATA[, 
	    	{
		    	color: ['tomato', 'palegreen', 'orangered', 'aquamarine'],
		    	controller: ]]><xsl:value-of select="$_controller" /><![CDATA[, 
		    	duration: ]]><xsl:value-of select="duration" /><![CDATA[, 
		    	height: ]]><xsl:value-of select="height" /><![CDATA[, 
		    	hu: ']]><xsl:value-of select="dir" /><![CDATA[', 
		    	thumbnails: ]]><xsl:value-of select="$_thumbnails" /><![CDATA[, 
		    	captions: ]]><xsl:value-of select="$_captions" /><![CDATA[, 
		    	width: ]]><xsl:value-of select="width" /><![CDATA[
		    });
]]>
</xsl:if>

<xsl:if test="contains(effect,'fold')">
<![CDATA[	    
	    var myShow]]><xsl:value-of select="$cid" /><![CDATA[ = new Slideshow.Fold('show]]><xsl:value-of select="$cid" /><![CDATA[', 
	    	data]]><xsl:value-of select="$cid" /><![CDATA[, 
	    	{
		    	controller: ]]><xsl:value-of select="$_controller" /><![CDATA[, 
		    	duration: ]]><xsl:value-of select="duration" /><![CDATA[, 
		    	height: ]]><xsl:value-of select="height" /><![CDATA[, 
		    	hu: ']]><xsl:value-of select="dir" /><![CDATA[', 
		    	thumbnails: ]]><xsl:value-of select="$_thumbnails" /><![CDATA[, 
		    	captions: ]]><xsl:value-of select="$_captions" /><![CDATA[, 
		    	width: ]]><xsl:value-of select="width" /><![CDATA[
		    });
]]>
</xsl:if>


<xsl:if test="contains(absolute,'true')">
<![CDATA[
		var imgp]]><xsl:value-of select="$cid" /><![CDATA[ = document.getElementById('show]]><xsl:value-of select="$cid" /><![CDATA[');
		[cl,ct] = findPos(imgp]]><xsl:value-of select="$cid" /><![CDATA[);
		imgp]]><xsl:value-of select="$cid" /><![CDATA[.style.top = ]]><xsl:value-of select="top" /><![CDATA[-ct; //+]]><xsl:value-of select="height" /><![CDATA[;
		imgp]]><xsl:value-of select="$cid" /><![CDATA[.style.left = ]]><xsl:value-of select="left" /><![CDATA[-cl; //+]]><xsl:value-of select="width" /><![CDATA[;
]]>
</xsl:if>
<![CDATA[
	});
	    
	
]]>	
	</script>

	
	 <div id="show{$cid}" class="slideshow" style="width: {width}px; height: {height}px;  "> <!-- position: absolute; -->
    <img src="" alt="" width="{width}px" height="{height}px"/>
  </div>
		
	</xsl:template>

</xsl:stylesheet>