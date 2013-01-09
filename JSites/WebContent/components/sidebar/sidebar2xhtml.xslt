<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	
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
		<!--  xsl:copy-of select="." / -->
		<xsl:apply-templates />
		<!--  TASTO DI MODIFICA -->
		<xsl:call-template name="editlinks" />
		<xsl:apply-templates select="order" />
	</xsl:template>
	
			<xsl:template match="navpage">
			<li>
				<a href="{@link}">
					<xsl:value-of select="@name" />
				</a>
			
				<xsl:if test="count(navpage) > 0">
					<ul>
						<xsl:apply-templates />
					 </ul>
				</xsl:if>
			</li>			 
	</xsl:template>
	
	<xsl:template match="navigator">
	
	<div class="art-vmenublock">
				<div class="art-vmenublock-body">
					
					<div class="art-vmenublockcontent">
						<div class="art-vmenublockcontent-body">
					
							<!-- apertura menu verticale -->
							
							<div id="menuHolder">
								<ul id="menuOuter">
									<li class="lv1-li"><!--[if lte IE 6]><a class="lv1-a" href="#url"><table><tr><td><![endif]-->
									<!--  <ul class="art-vmenu"> -->
										<ul id="menuInner">
											<xsl:for-each select="navpage/navpage">
												<xsl:apply-templates select="." />
											</xsl:for-each>
										</ul>
									</li>
								</ul>
							</div>
							<!-- chiusura menu verticale -->
							<div class="cleared"></div>
					  	</div>
					</div>
					
					<div class="cleared"></div>
			  	</div>
			</div>
	
	
			<!-- xsl:apply-templates / --> 
			
	</xsl:template>
	
	
	
	
	
	
	<xsl:template match="voce">
		<xsl:variable name="classlevel0"><xsl:value-of select="/navigator/id/text()"/></xsl:variable>
		<xsl:variable name="level"><xsl:value-of select="count(ancestor-or-self::*) - 1"/></xsl:variable>
		
		<xsl:element name="a">
			
			<!--  PRIMA IMMAGINE -->
			<xsl:if test="@img1 != ''">
				<xsl:element name="img">
					<xsl:attribute name="src"><xsl:value-of select="@img1"/></xsl:attribute>
					<xsl:attribute name="alt" />
					<xsl:attribute name="class"><xsl:value-of select="concat($classlevel0,'img1')"/></xsl:attribute>
				</xsl:element>
			</xsl:if>
			
			<xsl:attribute name="href"><xsl:value-of select="@link"/></xsl:attribute>
			<xsl:choose>
				<xsl:when test="@active = 'true'">
					<xsl:attribute name="class"><xsl:value-of select="concat($classlevel0,'hover',$level,'lev')"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class"><xsl:value-of select="concat($classlevel0,$level,'lev')"/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="@text"/>

			<!-- SECONDA IMMAGINE (DOPO IL TESTO) -->
			<xsl:choose>
				<xsl:when test="@img2 = 'closed'">
					<img src="images/freccia_dx.gif" alt="toggle" class="imgright" width="7" height="9"/>
				</xsl:when>
				<xsl:when test="@img2 = 'opened'">
					<img src="images/freccia_down.gif" alt="toggle" class="imgright" width="7" height="9"/>
				</xsl:when>
				<xsl:otherwise/>
			</xsl:choose>
				
		</xsl:element>
		
		
		
		<xsl:apply-templates select="voce"/>
	</xsl:template>
	
	<xsl:template match="area">
		<div id="etichetta">
			<a href="{a/@href}"><xsl:value-of select="."/></a>
		</div>
	</xsl:template>
	
	<xsl:template match="id"></xsl:template>

</xsl:stylesheet>