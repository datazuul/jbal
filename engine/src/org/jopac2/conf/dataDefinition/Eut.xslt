<?xml version="1.0" encoding="UTF-8"?>

<!-- 
	Trasforma dati per EUT da Excel XML 2003 in
	
	<root>
		<record>
		    <titolo>Le due sponde del Mediterraneo : l'immagine riflessa. 473 p., 24 cm.</titolo>
		    <autore></autore>
		    <isbn></isbn>
		    <anno>1999</anno>
		    <prezzo>20</prezzo>
		    <immagine>image001.jpg</immagine>
		    <abstract></abstract>
		</record>
	</root>
 -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                              xmlns="http://www.w3.org/1999/xhtml"
                              xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">

  <xsl:template match="ss:Workbook">
  	<root>
		<xsl:apply-templates />
	</root>
  </xsl:template>
  
  <xsl:template match="ss:Worksheet">
	<xsl:apply-templates />
  </xsl:template>
  
  <xsl:template match="ss:Table">
	<xsl:apply-templates />
  </xsl:template>

  <xsl:template match="ss:Row">
  	<xsl:if test="ss:Cell[1] != 'Titolo'">
  		<xsl:if test="string-length(ss:Cell[1]) != 0">
		  	<record>
				<titolo>
					<xsl:call-template name="remove-null">
      					<xsl:with-param name="value">
							<xsl:value-of select="ss:Cell[1]" />
						</xsl:with-param>
					</xsl:call-template>
				</titolo>
			    <autore>
					<xsl:call-template name="remove-null">
      					<xsl:with-param name="value">
							<xsl:value-of select="ss:Cell[2]" />
						</xsl:with-param>
					</xsl:call-template>
				</autore>
			    <isbn>
					<xsl:call-template name="remove-null">
      					<xsl:with-param name="value">
							<xsl:value-of select="ss:Cell[3]" />
						</xsl:with-param>
					</xsl:call-template>
				</isbn>
			    <anno>
					<xsl:call-template name="remove-null">
      					<xsl:with-param name="value">
							<xsl:value-of select="ss:Cell[4]" />
						</xsl:with-param>
					</xsl:call-template>
				</anno>
			    <prezzo>
					<xsl:call-template name="remove-null">
      					<xsl:with-param name="value">
							<xsl:value-of select="ss:Cell[5]" />
						</xsl:with-param>
					</xsl:call-template>
				</prezzo>
			    <immagine>
					<xsl:call-template name="remove-null">
      					<xsl:with-param name="value">
							<xsl:value-of select="ss:Cell[6]" />
						</xsl:with-param>
					</xsl:call-template>
				</immagine>
			    <abstract>
					<xsl:call-template name="remove-null">
      					<xsl:with-param name="value">
							<xsl:value-of select="ss:Cell[7]" />
						</xsl:with-param>
					</xsl:call-template>
				</abstract>
			</record>
		</xsl:if>
	</xsl:if>
  </xsl:template>
  
  <xsl:template name="remove-null">
   <xsl:param name="value" select="."/>
   <xsl:if test="$value != 'null'">
   	<xsl:value-of select="$value"/>
   </xsl:if>
</xsl:template>
  
  <xsl:template match="*"/>

</xsl:stylesheet>
