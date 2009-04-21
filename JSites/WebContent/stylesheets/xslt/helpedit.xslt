<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
						xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
						xmlns="http://www.w3.org/1999/xhtml">
	
	<xsl:template name="callhelp">
		<div id="inviaRichiesta">
			<div id="radios">
				<input name="state" type="radio" value="WRK" checked="true">Modificherò ancora il componente</input>
				<br/>
				<input name="state" type="radio" value="PND">Il componente è pronto per la validazione</input>
			</div>
			<div id="stateSubmit">
				<input value="Salva" type="submit" id="submit"/>
			</div>
		</div>
		<div id="help">
			<b style="color:red; font-size:20px;">Link con target:</b> [_blank>>http://www.miosito.it/tuapagina>Titolo]<br />
			<b>Link:</b> [http://www.miosito.it/tuapagina>Titolo]<br />
			<b>Immagini:</b> [img:http://www.miosito.it/tuafoto.jpg>Alt text]<br />
			<b>Font monospaced:</b> --Monospaced--<br />
			<b>Font corsivo:</b> ''Corsivo''<br />
			<b>Font grassetto:</b> __Grassetto__<br />
		</div>
		<div class="clearer">&#160;</div>
	</xsl:template>

</xsl:stylesheet>