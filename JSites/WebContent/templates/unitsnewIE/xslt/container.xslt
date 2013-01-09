<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE a [
        <!ENTITY nbsp    "&#160;">
        <!ENTITY iexcl   "&#161;">
        <!ENTITY cent    "&#162;">
        <!ENTITY pound   "&#163;">
        <!ENTITY curren  "&#164;">
        <!ENTITY yen     "&#165;">
        <!ENTITY brvbar  "&#166;">
        <!ENTITY sect    "&#167;">
        <!ENTITY uml     "&#168;">

        <!ENTITY copy    "&#169;">
        <!ENTITY ordf    "&#170;">
        <!ENTITY laquo   "&#171;">
        <!ENTITY not     "&#172;">
        <!ENTITY shy     "&#173;">
        <!ENTITY reg     "&#174;">
        <!ENTITY macr    "&#175;">
        <!ENTITY deg     "&#176;">
        <!ENTITY plusmn  "&#177;">

        <!ENTITY sup2    "&#178;">
        <!ENTITY sup3    "&#179;">
        <!ENTITY acute   "&#180;">
        <!ENTITY micro   "&#181;">
        <!ENTITY para    "&#182;">
        <!ENTITY middot  "&#183;">
        <!ENTITY cedil   "&#184;">
        <!ENTITY sup1    "&#185;">
        <!ENTITY ordm    "&#186;">

        <!ENTITY raquo   "&#187;">
        <!ENTITY frac14  "&#188;">
        <!ENTITY frac12  "&#189;">
        <!ENTITY frac34  "&#190;">
        <!ENTITY iquest  "&#191;">
        <!ENTITY Agrave  "&#192;">
        <!ENTITY Aacute  "&#193;">
        <!ENTITY Acirc   "&#194;">
        <!ENTITY Atilde  "&#195;">

        <!ENTITY Auml    "&#196;">
        <!ENTITY Aring   "&#197;">
        <!ENTITY AElig   "&#198;">
        <!ENTITY Ccedil  "&#199;">
        <!ENTITY Egrave  "&#200;">
        <!ENTITY Eacute  "&#201;">
        <!ENTITY Ecirc   "&#202;">
        <!ENTITY Euml    "&#203;">
        <!ENTITY Igrave  "&#204;">

        <!ENTITY Iacute  "&#205;">
        <!ENTITY Icirc   "&#206;">
        <!ENTITY Iuml    "&#207;">
        <!ENTITY ETH     "&#208;">
        <!ENTITY Ntilde  "&#209;">
        <!ENTITY Ograve  "&#210;">
        <!ENTITY Oacute  "&#211;">
        <!ENTITY Ocirc   "&#212;">
        <!ENTITY Otilde  "&#213;">

        <!ENTITY Ouml    "&#214;">
        <!ENTITY times   "&#215;">
        <!ENTITY Oslash  "&#216;">
        <!ENTITY Ugrave  "&#217;">
        <!ENTITY Uacute  "&#218;">
        <!ENTITY Ucirc   "&#219;">
        <!ENTITY Uuml    "&#220;">
        <!ENTITY Yacute  "&#221;">
        <!ENTITY THORN   "&#222;">

        <!ENTITY szlig   "&#223;">
        <!ENTITY agrave  "&#224;">
        <!ENTITY aacute  "&#225;">
        <!ENTITY acirc   "&#226;">
        <!ENTITY atilde  "&#227;">
        <!ENTITY auml    "&#228;">
        <!ENTITY aring   "&#229;">
        <!ENTITY aelig   "&#230;">
        <!ENTITY ccedil  "&#231;">

        <!ENTITY egrave  "&#232;">
        <!ENTITY eacute  "&#233;">
        <!ENTITY ecirc   "&#234;">
        <!ENTITY euml    "&#235;">
        <!ENTITY igrave  "&#236;">
        <!ENTITY iacute  "&#237;">
        <!ENTITY icirc   "&#238;">
        <!ENTITY iuml    "&#239;">
        <!ENTITY eth     "&#240;">

        <!ENTITY ntilde  "&#241;">
        <!ENTITY ograve  "&#242;">
        <!ENTITY oacute  "&#243;">
        <!ENTITY ocirc   "&#244;">
        <!ENTITY otilde  "&#245;">
        <!ENTITY ouml    "&#246;">
        <!ENTITY divide  "&#247;">
        <!ENTITY oslash  "&#248;">
        <!ENTITY ugrave  "&#249;">

        <!ENTITY uacute  "&#250;">
        <!ENTITY ucirc   "&#251;">
        <!ENTITY uuml    "&#252;">
        <!ENTITY yacute  "&#253;">
        <!ENTITY thorn   "&#254;">
        <!ENTITY yuml    "&#255;">
]>

<xsl:stylesheet version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:output
		method="xml"
		omit-xml-declaration="yes"
		indent="yes"/> 
	
	<xsl:template name="units-container">

<div id="root"><!-- root-->

<!-- Main Menu Table Follows -->
<div>
	<map name="int" id="int"><!-- Mappa per la funzionalita' back to home solo sul Logo dell'Universita' -->
		<area shape="rect" alt="Homepage Universit&agrave; di Trieste" coords="1,1,85,82" href="http://www.units.it" title="UNITS Home" />
		<area shape="default" nohref="nohref" alt="Homepage Universit&agrave; di Trieste" />
	</map>
</div>

<!--container-->
<div id="container">
	<div id="intestazione"><!--INIZIO INTESTAZIONE -->
		<div id="arealogo"><img src="http://www.units.it/img/intestazione.jpg" alt="" usemap="#int" /></div>
		<div id="areasearch">
				<div class="permanent_icone">
<span class="Asmall"><a href="#" class="accessibility" onclick="setActiveStyleSheet('small'); return false;">A</a></span>
<span class="Amedium"><a href="#" class="accessibility" onclick="setActiveStyleSheet(''); return false;">A</a></span>
<span class="Alarge"><a href="#" class="accessibility" onclick="setActiveStyleSheet('large'); return false;">A</a></span>
<span class="Axlarge"><a href="#" class="accessibility" onclick="setActiveStyleSheet('xlarge'); return false;">A</a></span>
</div>
<!-- inizio search.inc -->
<form action="http://www.units.it/search/searchredir.php" method="post">
<table class="cerca">
<tr>
	<td class="cerca" align="left"><label for="cerca">Cerca: </label><input class="cerca" type="text" name="scope" size="25px" maxlength="200" accesskey="C" id="cerca" /></td>
</tr>
<tr>
		<td class="cerca">
		<select  class="cerca" name="who">
			<option value="campus">in tutto il Campus</option>
			<option value="people" selected="selected">persone</option>
			<option value="strutture">strutture</option>
			<option value="competenze">processi</option>
			<option value="sitemap">mappa del sito</option>
		</select>

		<input class="cerca" type="submit" value="go" />
	</td>
</tr>
</table>
</form>
<!-- fine search.inc -->

		</div>
	</div><!--FINE INTESTAZIONE -->
	<!-- INIZIO MENU TENDINA -->
<div class="barra">
<ul id="nav" class='linksunits'>
	<li id="print"><a href=""></a></li>
	<li id="ateneo"><a href="http://www.units.it/ateneo/">ATENEO</a><!-- inizio area ateneo -->
	<ul class='linksunits'><!-- inizio menu ateneo -->
		<li class="voce sub"><a href="http://www.units.it/ateneo/ateneo/">Chi siamo</a>
			<ul class='linksunits'><!-- inizio sottomenu di ateneo: ateneo_ateneo -->
			<li class="voce"><a href="http://www.units.it/ateneo/storia/index.php/from/ateneo_ateneo">Cenni Storici</a></li>
			<li class="voce"><a href="http://www.units.it/ateneo/cifre/">Ateneo in cifre</a></li>
			<li class="voce"><a href="http://www-amm.units.it/regolamenti">Normativa</a></li>
			<li class="voce"><a href="http://www.units.it/organigramma/index.php/from/ateneo_ateneo">Organizzazione</a></li>
			<li class="voce"><a href="http://www-amm.units.it/rstampa.nsf">Rassegna stampa</a></li>
			</ul><!-- fine sottomenu ateneo_ateneo -->
		</li>
		<li class="voce"><a href="http://www.units.it/dove/mappe/">Dove siamo</a></li>
		<li class="voce"><a href="http://www2.units.it/urp/">URP</a></li>
		<li class="voce sub"><a href="http://www.units.it/ateneo/bandi/">Concorsi, gare e consulenze</a>
			<ul class='linksunits'><!-- inizio sottomenu di ateneo: ateneo_bandi -->
			<li class="voce"><a href="http://www-amm.units.it/concorsi">Concorsi e selezioni</a></li>
			<li class="voce"><a href="http://www-amm.units.it/gare">Gare d'appalto</a></li>
			<li class="voce"><a href="http://www.units.it/consulesterne/">Elenco collaboratori e consulenti</a></li>
			</ul><!-- fine sottomenu ateneo_bandi -->
		</li>
		<li class="voce"><a href="http://www.units.it/ateneo/albo/">Albo Ufficiale</a></li>
		<li class="voce"><a href="http://www.units.it/convenzioni">Convenzioni, Protocolli e Consorzi</a></li>
		<li class="voce sub"><a href="http://www.units.it/servizi/index.php/from/ateneo">Servizi di Ateneo</a>
			<ul class='linksunits'><!-- inizio sottomenu di ateneo: servizi -->
			<li class="voce"><a href="http://www.units.it/studenti/mailservers/index.php/from/ateneo">Posta Elettronica</a></li>
			<li class="sep"></li>
			<li class="voce"><a href="https://classrooms.units.it">Aule (prenotazione e visualizzazione)</a></li>
			<li class="voce"><a href="http://www2.units.it/disabili/">Servizi ai Disabili</a></li>
			<li class="voce"><a href="http://www.units.it/sportellocasa/index.php/from/ateneo">Sportello casa</a></li>
			<li class="voce"><a href="http://www.biblio.units.it/">Sistema Bibliotecario di Ateneo</a></li>
			<li class="voce"><a href="http://www2.units.it/csia/">Servizi Informatici</a></li>
			<li class="voce"><a href="http://www2.units.it/prevenzione/">Servizio Prevenzione</a></li>
			<li class="voce"><a href="http://www.cla.units.it/">Centro Linguistico</a></li>
			<li class="voce"><a href="http://eut.units.it/">Edizioni Universit&agrave; di Trieste</a></li>
			<li class="voce"><a href="http://www.smats.units.it">Sistema Museale</a></li>
			<li class="voce"><a href="http://www2.units.it/cdets/">Centro di Documentazione Europea</a></li>
			<li class="voce"><a href="http://www2.units.it/cspa/">Coordinamento e Sviluppo Progetti e Apparecchiature</a></li>
			<li class="voce"><a href="http://www2.units.it/nirtv/">Servizio Televisivo</a></li>
			<li class="voce"><a href="http://www.openstarts.units.it/">Open starTs</a></li>
			<li class="sep"></li>
			<li class="voce"><a href="http://www.units.it/servizi/centri_servizi/index.php/from/ateneo">Elenco completo Centri Servizi</a></li>
			</ul><!-- fine sottomenu servizi -->
		</li>
		<li class="sep"></li>
		<li class="voce sub"><a href="http://www.units.it/associazioni/index.php/from/ateneo">Sindacati/Associazioni</a>
			<ul class='linksunits'><!-- inizio sottomenu di ateneo: associazioni -->
			<li class="voce"><a href="http://www.units.it/associazioni/sindacati/index.php/from/associazioni">Sindacati</a></li>
			<li class="voce"><a href="http://www.units.it/associazioni/ass_univ/index.php/from/associazioni">Associazioni Universitarie</a></li>
			<li class="voce"><a href="http://www.units.it/associazioni/ass_stud/index.php/from/associazioni">Associazioni di Studenti</a></li>
			<li class="voce"><a href="http://www.units.it/links/index.php/linksOf/ospiti/from/associazioni">Ospitati in questo sito...</a></li>
			</ul><!-- fine sottomenu associazioni -->
		</li>
		<li class="voce"><a href="http://www.units.it/links/index.php/from/ateneo">Link utili</a></li>
		<li class="voce"><a href="http://www.units.it/intra/">Intranet</a></li>
		<li class="sep"></li>
		<li class="voce"><a href="http://www-amm.units.it/Elezioni.nsf?OpenDatabase">Elezioni</a></li>
		<li class="sep"></li>
		<li class="voce"><a href="http://www.units.it/intra/bilanci/index.php/from/ateneo">Bilanci</a></li>
		<li class="voce"><a href="http://www.units.it/ateneo/relazioni/">Relazioni annuali istituzionali</a></li>
		<li class="voce"><a href="http://www.units.it/operazionetrasparenza/">Trasparenza, valutazione e merito</a></li>
	</ul><!-- fine menu ateneo -->
	</li><!-- fine area ateneo -->
	<li id="studenti"><a href="http://www.units.it/studenti/">STUDENTI</a><!-- inizio area studenti -->
	<ul class='linksunits'><!-- inizio menu studenti -->
		<li class="voce sub"><a href="http://www.units.it/studenti/segrstud/">Segreteria Studenti</a>
			<ul class='linksunits'><!-- inizio sottomenu di studenti: segrstud -->
			<li class="voce"><a href="http://www.units.it/avv_dida/">Avvisi</a></li>
			<li class="voce"><a href="http://www2.units.it/dida/ordamm/?file=OrariMASTER.inc">Orari</a></li>
			<li class="voce"><a href="http://www2.units.it/dida/contatti/">Contatti</a></li>
			<li class="voce"><a href="http://www2.units.it/immatricolazioni/">Test ammissione e Immatricolazioni</a></li>
			<li class="voce"><a href="http://www2.units.it/dida/ordamm/?file=bandi.inc">Tasse e esoneri - Studenti 150 ore</a></li>
			<li class="voce"><a href="http://www2.units.it/dida/ordamm/?file=carriera.inc">Carriera studenti</a></li>
			<li class="voce"><a href="http://www2.units.it/dida/titolofinale/">Laurea - Diploma</a></li>
			<li class="voce"><a href="http://www2.units.it/dida/ordamm/?file=ModulisticaMASTER.inc">Modulistica varia</a></li>
			</ul><!-- fine sottomenu segrstud -->
		</li>
		<li class="voce"><a href="http://www.units.it/esse3/online/">Servizi on-line</a></li>
		<li class="voce"><a href="http://www.units.it/studenti/mailservers/index.php/from/studenti">Posta Elettronica</a></li>
		<li class="voce sub"><a href="http://www.units.it/studenti/borsepremi/">Borse e Premi</a>
			<ul class='linksunits'><!-- inizio sottomenu di studenti: borsepremi -->
			<li class="voce"><a href="http://www-amm.units.it/borse">Borse di Studio</a></li>
			<li class="voce"><a href="http://www-amm.units.it/premistudio">Premi di Studio</a></li>
			</ul><!-- fine sottomenu borsepremi -->
		</li>
		<li class="voce"><a href="http://www2.units.it/sportellolavoro">Sportello Lavoro, Stage, Tirocini</a></li>
		<li class="voce"><a href="http://www.erdisu.trieste.it/">E.R.Di.S.U.</a></li>
		<li class="voce"><a href="http://www2.units.it/internationalia/">Mobilit&agrave; Internazionale</a></li>
		<li class="voce"><a href="http://www2.units.it/orienta">Orientamento</a></li>
		<li class="voce sub"><a href="http://www.units.it/servizi/index.php/from/studenti">Altri Servizi</a>
			<ul class='linksunits'><!-- inizio sottomenu di studenti: servizi -->
			<li class="voce"><a href="http://www.units.it/studenti/mailservers/index.php/from/studenti">Posta Elettronica</a></li>
			<li class="sep"></li>
			<li class="voce"><a href="https://classrooms.units.it">Aule (prenotazione e visualizzazione)</a></li>
			<li class="voce"><a href="http://www2.units.it/disabili/">Servizi ai Disabili</a></li>
			<li class="voce"><a href="http://www.units.it/sportellocasa/index.php/from/studenti">Sportello casa</a></li>
			<li class="voce"><a href="http://www.biblio.units.it/">Sistema Bibliotecario di Ateneo</a></li>
			<li class="voce"><a href="http://www2.units.it/csia/">Servizi Informatici</a></li>
			<li class="voce"><a href="http://www2.units.it/prevenzione/">Servizio Prevenzione</a></li>
			<li class="voce"><a href="http://www.cla.units.it/">Centro Linguistico</a></li>
			<li class="voce"><a href="http://eut.units.it/">Edizioni Universit&agrave; di Trieste</a></li>
			<li class="voce"><a href="http://www.smats.units.it">Sistema Museale</a></li>
			<li class="voce"><a href="http://www2.units.it/cdets/">Centro di Documentazione Europea</a></li>
			<li class="voce"><a href="http://www2.units.it/cspa/">Coordinamento e Sviluppo Progetti e Apparecchiature</a></li>
			<li class="voce"><a href="http://www2.units.it/nirtv/">Servizio Televisivo</a></li>
			<li class="voce"><a href="http://www.openstarts.units.it/">Open starTs</a></li>
			<li class="sep"></li>
			<li class="voce"><a href="http://www.units.it/servizi/centri_servizi/index.php/from/studenti">Elenco completo Centri Servizi</a></li>
			</ul><!-- fine sottomenu servizi -->
		</li>
		<li class="voce"><a href="http://www2.units.it/cds/componenti/">Consiglio Studenti</a></li>
		<li class="voce"><a href="http://www.triesteuniversitaria.it/site.htm">Trieste universitaria</a></li>
		<li class="sep"></li>
		<li class="voce"><a href="http://www2.units.it/stranieri/it/"><!--<img src="http://www.units.it/img/home/varie/homepage-it.gif" alt="[Italiano] " />-->Studenti stranieri</a></li>
		<li class="sep"></li>
		<li class="voce"><a href="http://www2.units.it/immatricolazioni/"><span class="big" style="padding:0 0 0 40px;"><span class="big">Immatricolazioni<br /><span class="colorBlulight-Units big" style="padding:0 0 0 60px;">2011-2012</span></span></span></a></li>
	</ul><!-- fine menu studenti -->
	</li><!-- fine area studenti -->
	<li id="didattica"><a href="http://www.units.it/didattica/">DIDATTICA</a><!-- inizio area didattica -->
	<ul class='linksunits'><!-- inizio menu didattica -->
		<li class="voce sub"><a href="http://www.units.it/facolta/index.php/from/didattica">Facolt&agrave;<br />&nbsp;Offerta Didattica</a>
			<ul class='linksunits'><!-- inizio sottomenu di didattica: facolta -->
			<li class="voce"><a href="http://www.units.it/strutture/index.php/from/didattica/area/didattica/menu/didattica/strutture/007000">Architettura</a></li>
			<li class="voce"><a href="http://www.units.it/strutture/index.php/from/didattica/area/didattica/menu/didattica/strutture/014000">Economia</a></li>
			<li class="voce"><a href="http://www.units.it/strutture/index.php/from/didattica/area/didattica/menu/didattica/strutture/017000">Farmacia</a></li>
			<li class="voce"><a href="http://www.units.it/strutture/index.php/from/didattica/area/didattica/menu/didattica/strutture/012000">Giurisprudenza</a></li>
			<li class="voce"><a href="http://www.units.it/strutture/index.php/from/didattica/area/didattica/menu/didattica/strutture/011000">Ingegneria</a></li>
			<li class="voce"><a href="http://www.units.it/strutture/index.php/from/didattica/area/didattica/menu/didattica/strutture/018000">Lettere e Filosofia</a></li>
			<li class="voce"><a href="http://www.units.it/strutture/index.php/from/didattica/area/didattica/menu/didattica/strutture/015000">Medicina e Chirurgia</a></li>
			<li class="voce"><a href="http://www.units.it/strutture/index.php/from/didattica/area/didattica/menu/didattica/strutture/008000">Psicologia</a></li>
			<li class="voce"><a href="http://www.units.it/strutture/index.php/from/didattica/area/didattica/menu/didattica/strutture/019000">Scienze Politiche</a></li>
			<li class="voce"><a href="http://www.units.it/strutture/index.php/from/didattica/area/didattica/menu/didattica/strutture/009000">Scienze della  Formazione</a></li>
			<li class="voce"><a href="http://www.units.it/strutture/index.php/from/didattica/area/didattica/menu/didattica/strutture/016000">Scienze matematiche, fisiche e naturali</a></li>
			<li class="voce"><a href="http://www.units.it/strutture/index.php/from/didattica/area/didattica/menu/didattica/strutture/010000">Scuola superiore di lingue moderne per Interpreti e Traduttori</a></li>
			</ul><!-- fine sottomenu facolta -->
		</li>
		<li class="voce sub"><a href="http://www.units.it/didattica/postlauream/">Post Lauream</a>
			<ul class='linksunits'><!-- inizio sottomenu di didattica: didattica_postlauream -->
			<li class="voce"><a href="http://www-amm.units.it/esamistato">Esami di Stato</a></li>
			<li class="voce"><a href="http://www2.units.it/dottorati/">Dottorati di Ricerca</a></li>
			<li class="voce"><a href="http://www-amm.units.it/ScSpec">Scuole di Specializzazione</a></li>
			<li class="voce"><a href="http://www-amm.units.it/perfez">Corsi di Perfezionamento</a></li>
			<li class="voce"><a href="http://www-amm.units.it/master">Master</a></li>
			</ul><!-- fine sottomenu didattica_postlauream -->
		</li>
		<li class="voce"><a href="http://www2.units.it/csia/ecdl/">ECDL</a></li>
		<li class="voce"><a href="http://moodle.units.it">Materiale didattico (Moodle)</a></li>
		<li class="voce sub"><a href="http://www.units.it/didattica/altripercorsiformativi/">Altri percorsi formativi</a>
			<ul class='linksunits'><!-- inizio sottomenu di didattica: didattica_altripercorsiformativi -->
			<li class="voce"><a href="http://www2.units.it/corsopariopp/">Donne, Politica e Istituzioni</a></li>
			<li class="voce"><a href="http://www.units.it/didattica/altripercorsiformativi/formazioneinsegnanti.pdf">Formazione Insegnanti</a></li>
			</ul><!-- fine sottomenu didattica_altripercorsiformativi -->
		</li>
		<li class="sep"></li>
		<li class="voce"><a href="http://www-amm.units.it/nucleo.nsf/vpaginehtml/ValDidattica?OpenDocument">Soddisfazione degli studenti <br />&nbsp;per le attivit&agrave; didattiche</a></li>
	</ul><!-- fine menu didattica -->
	</li><!-- fine area didattica -->
	<li id="ricerca"><a href="http://www.units.it/ricerca/">RICERCA</a><!-- inizio area ricerca -->
	<ul class='linksunits'><!-- inizio menu ricerca -->
		<li class="voce"><a href="http://www.units.it/dipartimenti/index.php/from/ricerca">Dipartimenti</a></li>
		<li class="voce sub"><a href="http://www.units.it/ricerca/centri/">Centri di Ricerca</a>
			<ul class='linksunits'><!-- inizio sottomenu di ricerca: ricerca_centri -->
			<li class="voce"><a href="http://www.units.it/organigramma/index.php/from//elemento/c_ricerca">- Interdipartimentali</a></li>
			<li class="voce"><a href="http://www.units.it/ricerca/c_eccellenza/index.php/from/ricerca_centri">- di Eccellenza</a></li>
			<li class="voce"><a href="http://www.units.it/c_interuni/index.php/from/ricerca_centri">- Interuniversitari</a></li>
			</ul><!-- fine sottomenu ricerca_centri -->
		</li>
		<li class="voce"><a href="http://www2.units.it/dottorati/">Dottorati di Ricerca</a></li>
		<li class="voce"><a href="http://apps.units.it/sitedirectory/Pubblicazioni/Default.aspx">Catalogo delle Pubblicazioni</a></li>
	</ul><!-- fine menu ricerca -->
	</li><!-- fine area ricerca -->
	<li id="impresa"><a href="http://www.units.it/impresa/">IMPRESE</a><!-- inizio area impresa -->
	<ul class='linksunits'><!-- inizio menu impresa -->
		<li class="voce"><a href="http://www2.units.it/imprese/">Industrial Liaison Office (ILO)</a></li>
		<li class="voce"><a href="http://www2.units.it/brevetti/">Brevetti</a></li>
		<li class="voce"><a href="http://www2.units.it/startcup/">StartCup</a></li>
		<li class="voce"><a href="http://www2.units.it/spoalma/">Sportello AlmaLaurea</a></li>
		<li class="voce"><a href="http://www2.units.it/sportellolavoro">Sportello Lavoro, Stage, Tirocini</a></li>
		<li class="sep"></li>
		<li class="voce"><a href="http://www.uni2b.it/">Uni2B</a></li>
	</ul><!-- fine menu impresa -->
	</li><!-- fine area impresa -->
	<li id="international"><a href="http://www.units.it/international/"><span class="nowrap"><img src="http://www.units.it/img/home/varie/homepage-en_small.gif" alt="[English] " /> INTERNATIONAL</span></a><!-- inizio area international -->
	<ul class='linksunits'><!-- inizio menu international -->
		<li class="voce"><a href="http://www2.units.it/relint/">International Relations</a></li>
		<li class="voce"><a href="http://www2.units.it/internationalia/index_en.html">International Mobility</a></li>
		<li class="voce sub"><a href="http://www.units.it/international/international_programs/">International Programs</a>
			<ul class='linksunits'><!-- inizio sottomenu di international: international_programs -->
			<li class="tit">Bachelor (Triennial):</li>
			<li class="voce"><a href="http://www.econ.units.it/eng/">Economics of Financial Markets and Innovation</a></li>
			<li class="tit">Master of Science (Biennial):</li>
			<li class="voce"><a href="http://www.biologia.units.it/neuroscienze/">Functional Genomics</a></li>
			<li class="voce"><a href="http://www.biologia.units.it/neuroscienze/">Neuroscience</a></li>
			<li class="voce"><a href="http://www.fisica.units.it/">Physics</a></li>
			<li class="tit">PhD:</li>
			<li class="voce"><a href="http://phdfluidmechanics.appspot.com/index.html">Environmental and Industrial Fluid Mechanics</a></li>
			<li class="voce"><a href="http://www.nanotech.units.it">Nanotechnologies</a></li>
			<li class="voce"><a href="http://www2.units.it/dmm/index.html">Molecular Biomedicine</a></li>
			<li class="voce"><a href="http://www.fisica.units.it/">Physics</a></li>
			</ul><!-- fine sottomenu international_programs -->
		</li>
		<li class="voce"><a href="http://www2.units.it/stranieri/en/">Foreign Students</a></li>
		<li class="voce"><a href="http://www2.units.it/tto">Technology Transfer - Intellectual Property</a></li>
		<li class="sep"></li>
		<li class="voce"><a href="http://www.ceinet.org/">C.E.I.</a></li>
		<li class="sep"></li>
		<li class="voce"><a href="http://www.welcomeoffice.fvg.it/trieste/home-trieste.aspx">Welcome Office</a></li>
		<li class="voce"><a href="http://www.intese.polimi.it/web/guest/units">Integrazione studenti esteri</a></li>
	</ul><!-- fine menu international -->
	</li><!-- fine area international -->
</ul>
</div>
<!-- FINE MENU TENDINA -->
</div> <!-- chiudo il contaier -->
</div> <!-- chuido il root -->

</xsl:template>
</xsl:stylesheet>

