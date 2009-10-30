<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:sql="http://apache.org/cocoon/SQL/2.0"
	xmlns:c="http://apache.org/cocoon/include/1.0">
	
	<xsl:include href="../../stylesheets/xslt/helpedit.xslt" />
	
    <!-- Questo include serve per la cornice di help  -->
	
	<xsl:param name="cid" />
	<xsl:param name="time" />
	<xsl:param name="pid" />
	<xsl:param name="extra" />
	<xsl:param name="context" />
	<xsl:param name="permission" />
	<xsl:param name="template"/>
	
	<xsl:include href="edit.xslt" />

	<xsl:template match="/">
		<xsl:apply-templates select="section" />
		
		<xsl:call-template name="callhelp" />
		
	</xsl:template>

	<!--  SEZIONE -->

	<xsl:template match="section">
		<div class="{$time}">
			<div class="sezione">
				<div class="sezione_immagine">
					<xsl:apply-templates select="image" />
				</div>
				<div class="sezione_contenuto">
					<xsl:call-template name="news"/>
					<xsl:apply-templates select="titolo" />
					<xsl:apply-templates select="testo" />
				</div>
			</div>		
		</div>
	</xsl:template>

	<!--  TITOLO -->

	<xsl:template match="titolo">
		<!--div-->

		<!-- div id="sezione_titolo"-->
		<b>Titolo:</b>
		<br />
		<input type="text" size="40" name="title" value="{text()}" />
		<xsl:choose>
			<xsl:when test="@type != ''">
				<input type="text" size="1" name="title_type" value="{@type}" />
			</xsl:when>
			<xsl:otherwise>
				<input type="text" size="1" name="title_type" value="2" />
			</xsl:otherwise>
		</xsl:choose>
		<!-- /div-->

		<br />
		<!-- /div-->
	</xsl:template>

	<!--  IMMAGINE (PIPELINE INSERIMENTO IMMAGINE CHIAMATA CON CINCLUDE) -->

	<xsl:template match="image">
		<xsl:variable name="url">
			<xsl:value-of select="text()" />
		</xsl:variable>
		<!-- div class="sezione_immagine"><img src="{$url}" width="70" height="70"/></div-->
		<c:include src="cocoon://imageeditcomponent?id=prevImage&amp;currentimage={$url}&amp;previewwidth=70&amp;previewheight=70&amp;minwidth=70&amp;minheight=70&amp;maxwidth=150&amp;maxheight=150" />
		<!-- br />
		<br />
		<input width="20" type="button" value="Inserisci Nuova" onClick="location.href='upload/pid{$pid}'"/-->
		<br/>Image link:<br/>
		<input name="imglink" type="text" size="6" value="{@link}"/>
	</xsl:template>


	<xsl:template match="testo">
		<xsl:call-template name="editor" />
	
		<b>Descrizione:</b>
		<br/>
		<input type="hidden" name="UndoText" id="UndoText"/>
		<textarea id="elm2" name="text" rows="10" class="edittextarea" tinyMCE_this="true">
			<!--  xsl:value-of select="text()" /-->
			<!-- xsl:copy-->
				<xsl:apply-templates select="@*|node()|text()|*" />
			<!--  /xsl:copy-->
		</textarea>
		<br />
	</xsl:template>
	
	<xsl:template name="news">
		<xsl:if test="$extra = 'news'">
			<b>Data inizio:</b>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
			<b>Data fine:</b>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<b>Nelle liste</b>
			<br/>

			<input type="text" size="8" name="startdate" value="{sql:rowset/sql:row/sql:startdate}" id="startdate" readonly="1" />
			<img src="./components/section/images/calendar.gif" id="f_trigger_c" style="cursor: pointer; border: 1px solid red;" title="Date selector"
      onmouseover="this.style.background='red';" onmouseout="this.style.background=''" />&#160;&#160;
	
			<input type="text" size="8" name="enddate" value="{sql:rowset/sql:row/sql:enddate}" id="enddate" readonly="1" />
			<img src="./components/section/images/calendar.gif" id="f_trigger_d" style="cursor: pointer; border: 1px solid red;" title="Date selector"
	      onmouseover="this.style.background='red';" onmouseout="this.style.background=''" />&#160;&#160;
		
			

			<input type="checkbox" name="list">
				<xsl:choose>
					<xsl:when test="sql:rowset/sql:row/sql:list = 'true'">
						<xsl:attribute name="checked">checked</xsl:attribute>
					</xsl:when>
					<xsl:otherwise></xsl:otherwise>
				</xsl:choose>
			</input>
			

			<br/>
			
			<link type="text/css" rel="stylesheet" href="components/section/css/calendar.css" />
			
			
			<script src="js/calendar.js" type="text/javascript"></script>
			<script src="js/calendar-en.js" type="text/javascript"></script>
			<script src="js/calendar-setup.js" type="text/javascript"></script>
			
			
			<script type="text/javascript">
			<![CDATA[
			    function catcalc(cal) {
			        var date = cal.date;
			        var time = date.getTime();
			        var modifico=cal.params.inputField;
			        
			        // use the _other_ field
			        var field = document.getElementById("startdate");
			        if (field == cal.params.inputField) {
			            field = document.getElementById("enddate");
			        }

			        // se è vuoto metti la data uguale
			        if(Number(field.value)==0) {
			            	field.value=date.print("%Y%m%d");
			        }
			        
			        var data=new Date();
			        data.setFullYear(Number(field.value.substr(0,4))); // substr inizia a contare da 0
			        data.setMonth(Number(field.value.substr(4,2))-1);  // i mesi vanno da 0 a 11
			        data.setDate(Number(field.value.substr(6,2)));
			        
			        if((modifico==document.getElementById("startdate") && time > data.getTime()) || 
			        		(modifico==document.getElementById("enddate") && data.getTime() > time)) {
			        	alert("La data di inizio deve essere antecedente o uguale alla data di fine avviso!");
			        	field.value=date.print("%Y%m%d");
			        }
			        
			        if(Math.abs(time-data.getTime())>2592000000) {
			        	alert("Un avviso no può essere valido per piu' di 30 giorni!")
			        	field.value=date.print("%Y%m%d");
			        }
			        
			        
			    }
			    Calendar.setup({
			        inputField     :    "startdate",     // id of the input field
			        ifFormat       :    "%Y%m%d",      // format of the input field
			        button         :    "f_trigger_c",  // trigger for the calendar (button ID)
			        align          :    "Br",           // alignment (defaults to "Bl")
			        singleClick    :    true,
			        onUpdate       :    catcalc
			    });
			    Calendar.setup({
			        inputField     :    "enddate",     // id of the input field
			        ifFormat       :    "%Y%m%d",      // format of the input field
			        button         :    "f_trigger_d",  // trigger for the calendar (button ID)
			        align          :    "Br",           // alignment (defaults to "Bl")
			        singleClick    :    true,
			        onUpdate       :    catcalc
			    });
			   ]]>
			</script>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>