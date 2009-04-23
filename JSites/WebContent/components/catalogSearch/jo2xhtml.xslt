<?xml version="1.0" encoding="UTF-8"?>
        
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns="http://www.w3.org/1999/xhtml">

<!-- xsl:variable name="requestQuery" select="/root/queryData/requestQuery"/ -->
   
   <xsl:template match="cookies" /> 
   
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
      <h1>
        <xsl:value-of select="."/>
      </h1>
   </xsl:template>
   
   <xsl:template match="image">
      <DIV class="img">
      	<img src="{.}"/>
      </DIV>
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
      <DIV class="resultSet">
        <xsl:for-each select="record">
            <DIV class="record">
                <xsl:apply-templates />
            </DIV>
        </xsl:for-each>
      </DIV>
   </xsl:template>
 
 
	 <xsl:template match="buy">
	      <DIV class="id">
			<xsl:if test="@key">
			   <A>
			      <xsl:attribute name="href">
	        	      <xsl:value-of select="@key" />
	          	  </xsl:attribute>
		   	  	  Acquista<!-- xsl:apply-templates /--><br />
		   	   </A>
			</xsl:if>
			<xsl:if test="string-length(@key)=0">
				<xsl:apply-templates /><br />
			</xsl:if>
	   	  </DIV>
	   </xsl:template>
 
   <xsl:template match="bid">
      <DIV class="bid">
		<xsl:if test="@key">
		   <A>
		      <xsl:attribute name="href">
        	      <xsl:value-of select="@key" />
          	  </xsl:attribute>
	   	  	  Dettagli<!-- xsl:apply-templates /--><br />
	   	   </A>
		</xsl:if>
		<xsl:if test="string-length(@key)=0">
			<xsl:apply-templates /><br />
		</xsl:if>
   	  </DIV>
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
		Standard number: <xsl:value-of select="."/><br/>
   </xsl:template>
   <xsl:template match="publicationDate">
		Data pubblicazione: <xsl:value-of select="."/><br/>
   </xsl:template>
   <xsl:template match="prezzo">
		Prezzo: <xsl:value-of select="."/>
   </xsl:template>
   
   
   
   
      
</xsl:stylesheet>
