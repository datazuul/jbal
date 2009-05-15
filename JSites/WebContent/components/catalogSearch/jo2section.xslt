<?xml version="1.0" encoding="UTF-8"?>
        
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns="http://www.w3.org/1999/xhtml">

<!-- xsl:variable name="requestQuery" select="/root/queryData/requestQuery"/ -->
   
   <xsl:template match="cookies" />
   <xsl:template match="catalogName" />
   <xsl:template match="prevPage" />
   <xsl:template match="nextPage" />
   
   <xsl:template match="queryData">
      <DIV class="queryData">
        <xsl:value-of select="queryCount" /><xsl:text> </xsl:text>records found in
        <xsl:value-of select="queryTime" /><xsl:text> </xsl:text>seconds.
      </DIV>
   </xsl:template>
   
   <xsl:template match="searchData">
      <DIV class="searchData">
        <xsl:apply-templates select="item"/>
      </DIV>
   </xsl:template>
   
   <xsl:template match="title">
      <h1 class="sezione_titolo">
        <xsl:value-of select="."/>
      </h1>
   </xsl:template>
   
   <xsl:template match="image">
   	  <xsl:if test="@nature = 'M' or @nature='F'">
	      <div class="sezione_immagine">
	      	<img src="{.}"/>
	      </div>
	  </xsl:if>
   </xsl:template>
   
   
   <xsl:template match="item">
      <DIV class="item">
   	    <xsl:if test="@key">
		   <A>
		      <xsl:attribute name="href">
        	      <xsl:value-of select="@key" />
          	  </xsl:attribute>
	   	  	  (<xsl:value-of select="classe" />=<xsl:value-of select="parola" />):<xsl:value-of select="conta" />
	   	   </A>
		</xsl:if>
		<xsl:if test="string-length(@key)=0">
			(<xsl:value-of select="classe" />=<xsl:value-of select="parola" />):<xsl:value-of select="conta" />
		</xsl:if>
      </DIV>
   </xsl:template>
   
   <xsl:template match="authors">
   	Autori
   	<xsl:apply-templates />
   	<br/>
   </xsl:template>

   <xsl:template match="author">
      <DIV class="author">
		<xsl:if test="@key">
		   <A>
		      <xsl:attribute name="href">
        	      <xsl:value-of select="@key" />
          	  </xsl:attribute>
	   	  	  <xsl:apply-templates /><br />
	   	   </A>
		</xsl:if>
		<xsl:if test="string-length(@key)=0">
			<xsl:apply-templates /><br />
		</xsl:if>
   	  </DIV>
   </xsl:template>

   <xsl:template match="isbd">
      <DIV class="isbd">
		<xsl:if test="@key">
		   <A>
		      <xsl:attribute name="href">
        	      <xsl:value-of select="@key" />
          	  </xsl:attribute>
	   	  	  <xsl:apply-templates /><br />
	   	   </A>
		</xsl:if>
		<xsl:if test="string-length(@key)=0">
			<xsl:apply-templates /><br />
		</xsl:if>
   	  </DIV>
   </xsl:template>

   <xsl:template match="isbdserie">
      <DIV class="isbd">
		<xsl:if test="@key">
		   <A>
		      <xsl:attribute name="href">
        	      <xsl:value-of select="@key" />
          	  </xsl:attribute>
	   	  	  <xsl:apply-templates /><br />
	   	   </A>
		</xsl:if>
		<xsl:if test="string-length(@key)=0">
			<xsl:apply-templates /><br />
		</xsl:if>
   	  </DIV>
   </xsl:template>


   <xsl:template match="resultSet">
      <!-- DIV class="resultSet"-->
        <xsl:for-each select="record">
            <DIV class="sezione">
            <!-- 
                <xsl:apply-templates select="image"/>
                <div class="sezionericerca_contenuto">
                	<xsl:apply-templates select="*[not(local-name() = 'image')]" />
                </div>
            -->
  				<div class="sezione_testo"><xsl:value-of disable-output-escaping="yes" select="text()"/></div>
            
                <div class="clearer">&#160;</div>
                <hr/>
            </DIV>
        </xsl:for-each>
      <!-- /DIV-->
   </xsl:template>
 
<!--  
	 <xsl:template match="buy">
	      <DIV class="id">
	      	<br/>
	      	<xsl:if test="@dett = 'Y' ">
	      	<a>
	      		<xsl:attribute name="href">
	        		pageview?pid=7&amp;query=JID%253D<xsl:value-of select="text"/>
	          	</xsl:attribute>
	          	Dettagli</a>
			</xsl:if>
			<xsl:if test="@key and (@nature ='M' or @nature='F') ">
			   <A>
			      <xsl:attribute name="href">
	        	      <xsl:value-of select="@key" />
	          	  </xsl:attribute>
		   	  	  Acquista</A>
			</xsl:if>
			<xsl:if test="string-length(@key)=0">
				<xsl:apply-templates /><br />
			</xsl:if>
	   	  </DIV>
	   </xsl:template>
  -->
   <xsl:template match="bid">
      <!-- DIV class="bid">
		<xsl:if test="@key">
		   <A>
		      <xsl:attribute name="href">
        	      <xsl:value-of select="@key" />
          	  </xsl:attribute>
	   	  	  Dettagli
	   	   </A>
		</xsl:if>
		<xsl:if test="string-length(@key)=0">
			<xsl:apply-templates /><br />
		</xsl:if>
   	  </DIV-->
   </xsl:template> 
   
   <xsl:template match="id">
      <!--  DIV class="BID">
              	 <xsl:if test="@key">
		   <A>
		      <xsl:attribute name="href">
        	      <xsl:value-of select="@key" />
          	  </xsl:attribute>
	   	  	  Correggi questa schedina: <xsl:apply-templates />
	   	   </A>
		</xsl:if>
		<xsl:if test="string-length(@key)=0">
			<xsl:apply-templates /><br />
		</xsl:if>
      </DIV-->
   </xsl:template>   

	<xsl:template match="abstract">
      <DIV class="abstract">
        <xsl:value-of select="."/>
      </DIV>
   </xsl:template>  

   <xsl:template match="source">
      <DIV class="source">
        <xsl:value-of select="."/>
      </DIV>
   </xsl:template>  

   <xsl:template match="syntax" />
   <xsl:template match="type" />

   <xsl:template match="match">
      <DIV class="match">
        <xsl:value-of select="."/>
      </DIV>
   </xsl:template>   
     
   <xsl:template match="ISBD">
      <DIV class="isbd">
      	 <xsl:if test="@key">
		   <A>
		      <xsl:attribute name="href">
        	      <xsl:value-of select="@key" />
          	  </xsl:attribute>
	   	  	  <xsl:apply-templates /><br />
	   	   </A>
		</xsl:if>
		<xsl:if test="string-length(@key)=0">
			<xsl:apply-templates /><br />
		</xsl:if>
      </DIV>
   </xsl:template>   


   <xsl:template match="libraryId">
      	 <xsl:if test="@key">
		   <A>
		      <xsl:attribute name="href">
        	      <xsl:value-of select="@key" />
          	  </xsl:attribute>
	   	  	  <!-- xsl:apply-templates / -->
	   	  	  <xsl:value-of select="../libraryName" />
	   	   </A>
		</xsl:if>
		<xsl:if test="string-length(@key)=0">
			<xsl:apply-templates /><br />
		</xsl:if>
   </xsl:template>   


   <xsl:template match="serie">
     <DIV class="titoletto">
        Serie:
     </DIV>
     <DIV class="serie">
		<xsl:if test="@key">
		   <A>
		      <xsl:attribute name="href">
        	      <xsl:value-of select="@key" />
          	  </xsl:attribute>
	   	  	  <xsl:apply-templates /><br />
	   	   </A>
		</xsl:if>
		<xsl:if test="string-length(@key)=0">
			<xsl:apply-templates /><br />
		</xsl:if>
     </DIV>
   </xsl:template>
   
   <xsl:template match="partof">
     <DIV class="titoletto">
        Fa parte di:
     </DIV>
     <DIV class="partof">
        <xsl:apply-templates /><br />
     </DIV>
   </xsl:template>

   <xsl:template match="haspart">
     <DIV class="titoletto">
        Contiene:
     </DIV>
     <DIV class="haspart">
        <xsl:apply-templates /><br />
     </DIV>
   </xsl:template>
   
   <xsl:template match="db">
   </xsl:template>
   
   <xsl:template match="subjects">
     <DIV class="titoletto">
        Soggetti:
     </DIV>
     <DIV class="subjects">
        <xsl:apply-templates /><br />
     </DIV>
   </xsl:template>

   <xsl:template match="classifications">
     <DIV class="titoletto">
        Classificazioni:
     </DIV>
     <DIV class="classifications">
        <xsl:apply-templates /><br />
     </DIV>
   </xsl:template>
      
   <xsl:template match="signatures">
      <DIV class="titoletto">
        Localizzazioni:
      </DIV>
      <DIV class="signatures">
        <xsl:for-each select="signature">
            <DIV class="library">
            	<!-- 
            	<DIV class="name"><xsl:value-of select="libraryName" /></DIV>
                &nbsp;(<DIV class="code">
                <xsl:apply-templates select="libraryId"/></DIV>)
                -->
                <DIV class="name"><xsl:apply-templates select="libraryId"/></DIV>
            </DIV>
            <xsl:if test="string-length(idNumber)>0">
	            <DIV class="idNumber">Inventario: <xsl:value-of select="idNumber" /></DIV>
	        </xsl:if>
	        <xsl:if test="string-length(localization)>0">
	            <DIV class="localization">Collocazione: <xsl:value-of select="localization" /></DIV>
            </xsl:if>
        </xsl:for-each>
      </DIV>
   </xsl:template>   
   
   <xsl:template match="standardNumber">
		<br/><b>Standard number:</b>&#160;<xsl:value-of select="."/>
   </xsl:template>
   <xsl:template match="publicationDate">
		<br/><b>Data pubblicazione:</b>&#160;<xsl:value-of select="."/>
   </xsl:template>
   <xsl:template match="publicationPlace">
		<br/><b>Luogo pubblicazione:</b>&#160;<xsl:value-of select="."/>
   </xsl:template>
   <xsl:template match="editor">
		<br/><b>Editore:</b>&#160;<xsl:value-of select="."/>
   </xsl:template>
   <xsl:template match="prezzo">
		<br/><b>Prezzo:</b>&#160;<xsl:value-of select="."/>
   </xsl:template>
   <xsl:template match="jid">
		<!-- <br/><b>JID:</b>&#160;<xsl:value-of select="."/>  -->
   </xsl:template>
   
   
   
      
</xsl:stylesheet>
