
```
History
23.04.2006	AC usare classe BitSet di java al posto di BitArray home made
		   	ok eliminare classe bitarray e usare solo bitset
	       	ok completare classe bitsetutility
24.04.2006	AC inserito parserRicerche nel progetto e tolto JAR
30.04.2006  AC inseriti errori sul parser espressioni
09.05.2006  AC ricerca in ANY tramite l_classi_parole
10.05.2006  RT,AC creato build_JOpac2 per deploy applicazioni
10.05.2006	AC,RT eliminata la necessita di Data_element e L_parole_DE per import e ricerche	       
10.05.2006	RT implementato HSQLDB
31.05.2006  AC,RT handle-errors in subsitemap
15.06.2006	RT cocoon 2.1.9, gerarchia tipo, creata interfaccia tipi, factory tipi, implemenato tipo xml
05.07.2006	RM Logo Keiko
15.07.2006	AC ricerca per troncamento,  eliminazione data_element, l_parole_de dai sorgenti e dal db
20.07.2006	RT creata tabella notizie_posizione_parole e popolamento in import
27.07.2006	RT bachi minori in importazione (LoadData.java)
02.08.2006  AC query SQL per liste
18.09.2006  RT,AC prove su liste
20.09.2006	RT inserito indice di sequenza tag in tabella notizie_posizione_parole
25.09.2006	RT,AC create select e test per liste
19.09.2006	AC aggiunto campo parola in notizie_posizione_parole e modificato indice idx_pcp in (id_classe, posizione_parola, parola)
19.03.2007	RT aggiunto campo stemma su anagrafe parole, aggiunta classe per lo stemmer, impostato in importazione (stemmer fatto da Luca Gregoretti)
26.03.2007	AC,RT Tag Clouds (query) Fare pulizia di ricerche e ricerche_dettaglio, testare ricerche in hsqldb
27.03.2007	RT corretto bug in estrazione JID per paramSearch
15.05.2007	RT SessionListener e Context Listener per pulizia ricerche_dettaglio
12.06.2007	RT aggiunto campo MD5, hashSearch per rete P2P di Iztok
15.07.2007	RT rifatto metaopac con thread e risultati parziali
22.08.2007	RT Importazione tabelle da MS Access
22.08.2007	RT salvataggio di notizie in formato compresso
22.08.2007	RT cambiato RecordReader per leggere da InputStream invece che da Reader
22.08.2007	RT disattivato salvataggio ricerche per le Cloud (è troppo lento se ci sono tanti ID, da ottimizzare)
27.08.2007	AC RT riattivata ricerca con liste
28.08.2007	RT Eliminata classe JOpac2DBManager e consolidati i metodi in DBGateway
30.08.2007	RT Aggiunti metodi setup(Connection conn) e destroy(Connection conn) a RecordReader
31.08.2007	RT se con search= non viene specificata la classe assume una ricerca in ANY
```