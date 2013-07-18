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
		<textarea id="elm2" name="text" rows="10" class="edittextarea"> <!--  tinyMCE_this="true" -->
			<!--  xsl:value-of select="text()" /-->
			<!--  <xsl:copy /> -->
			<xsl:apply-templates select="@*|node()|text()|*" />
			<!--  /xsl:copy-->
		</textarea>
		<br />
	</xsl:template>
	
	<xsl:template name="news">
		<xsl:if test="$extra = 'news'">
			<link type="text/css" rel="stylesheet" href="./components/section/css/dhtmlgoodies_calendar.css?random=20051112" media="screen"></link>
			<script type="text/javascript" src="./components/section/js/dhtmlgoodies_calendar.js?random=20060118"></script>
			
			<table style="border: 0">
				<tr>
					<td><b>Data inizio pubblicazione:</b></td>
					<td><input type="text" size="8" name="startdate" value="{sql:rowset/sql:row/sql:startdate}" id="startdate" readonly="1" />
						<img src="./components/section/images/calendar.gif"  style="cursor: pointer; border: 1px solid red;" title="Date selector"
						      onmouseover="this.style.background='red';" onmouseout="this.style.background=''" 
						      onclick="displayCalendar(document.getElementById('startdate'),'yyyymmdd',this)"/>
			      </td>
				</tr>
				<tr>
					<td><b>Data fine pubblicazione:</b></td>
					<td><input type="text" size="8" name="enddate" value="{sql:rowset/sql:row/sql:enddate}" id="enddate" readonly="1" />
						<img src="./components/section/images/calendar.gif" style="cursor: pointer; border: 1px solid red;" title="Date selector"
						      onmouseover="this.style.background='red';" onmouseout="this.style.background=''" 
						      onclick="displayCalendar(document.getElementById('enddate'),'yyyymmdd',this)"/>
					</td>
				</tr>
				<tr>
					<td><b>Data inizio evento:</b></td>
					<td><input type="text" size="12" name="eventstarttime" value="{sql:rowset/sql:row/sql:eventstarttime}" id="eventstarttime" readonly="1" />
						<img src="./components/section/images/calendar.gif"  style="cursor: pointer; border: 1px solid red;" title="Date selector"
						      onmouseover="this.style.background='red';" onmouseout="this.style.background=''" 
						      onclick="displayCalendar(document.getElementById('eventstarttime'),'yyyymmddhhii',this,true)"/>
			      </td>
				</tr>
				<tr>
					<td><b>Data fine evento:</b></td>
					<td><input type="text" size="12" name="eventendtime" value="{sql:rowset/sql:row/sql:eventendtime}" id="eventendtime" readonly="1" />
						<img src="./components/section/images/calendar.gif" style="cursor: pointer; border: 1px solid red;" title="Date selector"
						      onmouseover="this.style.background='red';" onmouseout="this.style.background=''" 
						      onclick="displayCalendar(document.getElementById('eventendtime'),'yyyymmddhhii',this,true)"/>
					</td>
				</tr>
				
				<tr>
					<td><b>Nelle liste</b></td>
					<td><input type="checkbox" name="list">
						<xsl:choose>
							<xsl:when test="sql:rowset/sql:row/sql:list = 'true'">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:when>
							<xsl:otherwise></xsl:otherwise>
						</xsl:choose>
					</input>
					</td>
				</tr>
				
			</table>
	


			
			

			<br/>

		</xsl:if>
	</xsl:template>
	
	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()|*" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>