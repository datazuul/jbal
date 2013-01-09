<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	
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

	<xsl:template match="navbar">
	<div class="art-layout-cell art-sidebar1">
		<div class="art-vmenublock">
			<div class="art-vmenublock-body">
				<div class="art-vmenublockheader">
					<div class="l"></div>
					<div class="r"></div>


					<!-- titolo menu verticale -->
					<!-- <div class="t">MENU</div> -->
					<!-- /titolo menu verticale -->


				</div>
				<div class="art-vmenublockcontent">
					<div class="art-vmenublockcontent-body">
				
						<!-- apertura menu verticale -->
						
						<div id="menuHolder">
							<ul id="menuOuter">
								<li class="lv1-li"><!--[if lte IE 6]><a class="lv1-a" href="#url"><table><tr><td><![endif]-->
								<!--  <ul class="art-vmenu"> -->
									<ul id="menuInner">
										<xsl:for-each select="/page/navbar/navigator/navpage/navpage">
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

		<!--  
		<xsl:call-template name="ricerca" />

		<xsl:call-template name="extra" />
		-->
		<xsl:call-template name="contatti" />
		
		
		
	</div>
</xsl:template>

	<xsl:template name="contatti">
	<div class="art-block">
			<div class="art-block-body">
				<div class="art-blockheader">   <!-- apertura blocco contatti + immagine -->

					<!-- <div class="t">Contatti</div> -->
				</div>
				<div class="art-blockcontent">
					<div class="art-blockcontent-tl"></div>
					<div class="art-blockcontent-tr"></div>
					<div class="art-blockcontent-bl"></div>
					<div class="art-blockcontent-br"></div>
					<div class="art-blockcontent-tc"></div>
					<div class="art-blockcontent-bc"></div>
					<div class="art-blockcontent-cl"></div>
					<div class="art-blockcontent-cr"></div>
					<div class="art-blockcontent-cc"></div>
					<div class="art-blockcontent-body">
						<div>
							<img src="./images/page1/contact.png" alt=""
								style="margin: 0 auto;display:block;width:95%" />
							<br />
							<xsl:copy-of select="*[not(name() = 'navigator')]" />
							<!--  <b>Sistema Bibliotecario di Ateneo</b>
							<br />
							via Weis, 30 - 34100 Trieste
							<br />

							<a href="http://www.biblio.units.it/pageview?pid=337">Contatti</a>
							<br />

							<br />
							Fax: +39/040/558-6133
							-->
						</div>
						<!-- chiusura blocco contatti + immagine -->

						<div class="cleared">&#160;</div>
					</div>
				</div>
				<div class="cleared">&#160;</div>
			</div>
		</div>
	</xsl:template>


	<xsl:template name="extra">
	<div class="art-block">
			<div class="art-block-body">
				<div class="art-blockheader">
					<div class="t">secondo menu</div>
				</div>
				<div class="art-blockcontent">
					<div class="art-blockcontent-tl"></div>
					<div class="art-blockcontent-tr"></div>
					<div class="art-blockcontent-bl"></div>
					<div class="art-blockcontent-br"></div>
					<div class="art-blockcontent-tc"></div>
					<div class="art-blockcontent-bc"></div>
					<div class="art-blockcontent-cl"></div>
					<div class="art-blockcontent-cr"></div>
					<div class="art-blockcontent-cc"></div>




					<div class="art-blockcontent-body">
						<!-- apertura blocco menu link aggiuntivi -->
						<!-- <div> <ul> <li> <a href="#">Titolo</a> </li> <li> <a href="#">Avvisi</a> 
							</li> <li> <a href="#">Servizi offerti</a> </li> <li> <a href="#">Patrimonio 
							documentario</a> </li> <li> <a href="#">Staff e Contatti</a> </li> <li> <a 
							href="#">Hyperlink</a> </li> <li> <a href="#" class="visited">Visited link</a> 
							</li> <li> <a href="#" class="hover">Hovered link</a> </li> </ul> <p> <b>Avviso</b> 
							<br /> Aliquam sit amet felis. Mauris semper, velit semper laoreet dictum, 
							quam diam dictum urna, nec placerat elit nisl in quam. Etiam augue pede, 
							molestie eget, rhoncus at, convallis ut, eros. Aliquam pharetra. <br /> <a 
							href="javascript:void(0)">Leggi...</a> </p> <p> <b>Avviso</b> <br /> Aliquam 
							sit amet felis. Mauris semper, velit semper laoreet dictum, quam diam dictum 
							urna, nec placerat elit nisl in quam. Etiam augue pede, molestie eget, rhoncus 
							at, convallis ut, eros. Aliquam pharetra. <br /> <a href="javascript:void(0)">Leggi...</a> 
							</p> </div> -->
						<!-- chiusura blocco menu link aggiuntivi -->

						<div class="cleared">&#160;</div>
					</div>
				</div>
				<div class="cleared">&#160;</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="ricerca">
		<div class="art-block">
			<div class="art-block-body">
				<div class="art-blockheader">
					<div class="t">Newsletter</div>
				</div>
				<div class="art-blockcontent">
					<div class="art-blockcontent-tl"></div>
					<div class="art-blockcontent-tr"></div>
					<div class="art-blockcontent-bl"></div>
					<div class="art-blockcontent-br"></div>
					<div class="art-blockcontent-tc"></div>
					<div class="art-blockcontent-bc"></div>
					<div class="art-blockcontent-cl"></div>
					<div class="art-blockcontent-cr"></div>
					<div class="art-blockcontent-cc"></div>
					<div class="art-blockcontent-body">
						<!-- apertura blocco campo ricerca -->
						<div>
							<form method="get" id="newsletterform" action="javascript:void(0)">
								<input type="text" value="" name="email" id="s" style="width: 95%;" />
								<span class="art-button-wrapper">
									<span class="l">&#160;</span>
									<span class="r">&#160;</span>
									<input class="art-button" type="submit" name="search"
										value="cerca risorse" />
								</span>

							</form>
						</div>
						<!-- chiusura blocco campo ricerca -->

						<div class="cleared">&#160;</div>
					</div>
				</div>
				<div class="cleared">&#160;</div>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
