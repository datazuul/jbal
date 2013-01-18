package org.jopac2.jbal;

/**
 * Descrizione di un accesso ad una risorsa elettronica, con sottocampi presi dalla
 * definizione unimarc (856)
 * @author romano
 *
 */

/**
 * 
Occorrenza: ripetibile
Indicatore 1: Modalità di accesso

    Nessuna informazione fornita
    Email
    1 FTP
    2 Login da remoto (Telnet)
    3 Dial-up
    4 HTTP
    7 Modalità specifica in $y

Indicatore 2: non definito
Codici di sottocampo:

    $a Nome Host (ripetibile)
    $b Numero di accesso (ripetibile)
    $c Compressione dell'informazione (ripetibile)
    $d Percorso (ripetibile)
    $e Data e orario della consultazione e dell'accesso(non ripetibile)
    $f Nome del file (ripetibile)
    $h Processore delle richieste (non ripetibile)
    $i Istruzione (ripetibile)
    $j Bits per second (non ripetibile)
    $k Password (Not ripetibile)
    $l Logon/login (non ripetibile)
    $m Contatto per l'assistenza nell'accesso (ripetibile)
    $n Nome dell'ubicazione dell'host in $a (non ripetibile)
    $o Sistema operativo (non ripetibile)
    $p Porta (non ripetibile)
    $q Tipo di formato elettronico (non ripetibile)
    $r Impostazioni (non ripetibile)
    $s Dimensioni del file (ripetibile)
    $t Emulazione del terminale (ripetibile)
    $u Uniform Resource Locator (non ripetibile)
    $v Orario in cui è possibile l'accesso alla risorsa (ripetibile)
    $w Numero di controllo della registrazione (ripetibile)
    $x Nota non pubblica (ripetibile)
    $y Metodo di accesso (non ripetibile)
    $z Nota pubblica (ripetibile)
    $2 Testo del link (ripetibile)

 *
 */

public class ElectronicResource implements Comparable<ElectronicResource> {

	public static String[] accessType={"email","ftp","telnet","dialup","http","","","other"}; // mantiene l'ordine di unimarc
	private String hostaname="";
	private String accessnumber=""; // ex. IP
	private String compression="";
	private String path=""; // ex. directory of storage
	private String lastaccesstime=""; // YYYYMMDDHHMM
	private String filename="";
	private String requestprocessor=""; // The username, or processor of the request; generally the data which precedes the at sign ('@') in the host address.
	private String command="";
	private String bitpersecond="";
	private String password="";
	private String login="";
	private String contact="";
	private String location=""; // Name of location of host i
	private String operatingsystem="";
	private String portnumber="";
	private String mimetype="";
	private String settings="";
	private String filesize="";
	private String terminalemulationsettings="";
	private String url="";
	private String accesstime=""; // orario di disponibilità della risorsa
	private String recordcontrolnumber="";
	private String nonpublicnote="";
	private String accessmethod="";
	private String publicnote="";
	private String linktext=""; // da visualizzare al posto dell'url nella pagina html
	public static String[] getAccessType() {
		return accessType;
	}
	public static void setAccessType(String[] accessType) {
		ElectronicResource.accessType = accessType;
	}
	public String getHostaname() {
		return hostaname;
	}
	public void setHostaname(String hostaname) {
		this.hostaname = hostaname;
	}
	public String getAccessnumber() {
		return accessnumber;
	}
	public void setAccessnumber(String accessnumber) {
		this.accessnumber = accessnumber;
	}
	public String getCompression() {
		return compression;
	}
	public void setCompression(String compression) {
		this.compression = compression;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getLastaccesstime() {
		return lastaccesstime;
	}
	public void setLastaccesstime(String lastaccesstime) {
		this.lastaccesstime = lastaccesstime;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getRequestprocessor() {
		return requestprocessor;
	}
	public void setRequestprocessor(String requestprocessor) {
		this.requestprocessor = requestprocessor;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getBitpersecond() {
		return bitpersecond;
	}
	public void setBitpersecond(String bitpersecond) {
		this.bitpersecond = bitpersecond;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getOperatingsystem() {
		return operatingsystem;
	}
	public void setOperatingsystem(String operatingsystem) {
		this.operatingsystem = operatingsystem;
	}
	public String getPortnumber() {
		return portnumber;
	}
	public void setPortnumber(String portnumber) {
		this.portnumber = portnumber;
	}
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	public String getSettings() {
		return settings;
	}
	public void setSettings(String settings) {
		this.settings = settings;
	}
	public String getFilesize() {
		return filesize;
	}
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	public String getTerminalemulationsettings() {
		return terminalemulationsettings;
	}
	public void setTerminalemulationsettings(String terminalemulationsettings) {
		this.terminalemulationsettings = terminalemulationsettings;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAccesstime() {
		return accesstime;
	}
	public void setAccesstime(String accesstime) {
		this.accesstime = accesstime;
	}
	public String getRecordcontrolnumber() {
		return recordcontrolnumber;
	}
	public void setRecordcontrolnumber(String recordcontrolnumber) {
		this.recordcontrolnumber = recordcontrolnumber;
	}
	public String getNonpublicnote() {
		return nonpublicnote;
	}
	public void setNonpublicnote(String nonpublicnote) {
		this.nonpublicnote = nonpublicnote;
	}
	public String getAccessmethod() {
		return accessmethod;
	}
	public void setAccessmethod(String accessmethod) {
		this.accessmethod = accessmethod;
	}
	public String getPublicnote() {
		return publicnote;
	}
	public void setPublicnote(String publicnote) {
		this.publicnote = publicnote;
	}
	public String getLinktext() {
		return linktext;
	}
	public void setLinktext(String linktext) {
		this.linktext = linktext;
	}

	@Override
	public int compareTo(ElectronicResource electronicResource) {
		return url.compareTo(electronicResource.getUrl());
	}
	
	/**
	 * Equals if url is equal (as String)
	 * @param electronicResource
	 * @return
	 */
	public boolean equals(ElectronicResource electronicResource) {
		return url.equals(electronicResource.getUrl());
	}
}
