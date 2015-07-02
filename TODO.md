
```
TODO
23.3.2009 liste nuova versione
1. in dbgateway, dove c'è il tag cr_liste ciclare su tutte le classi
2. modificare il metodo updatetableliste passando anche la classe stringa
3. nella ricerca, se la tabella non esiste già, chiamare prima il metodo dbgateway.rebuildList passando la classe specifica
4. per testare l'esistenza della tabella 
-----------
2. parserRicerche
	eliminare pezzi non utili
	stampa albero migliorare stampa albero del parser
3. implementare suite test complessivo
5. cambiare id_notizia da long a int
6. gestire errori web in maniera decente
7. manca in doRicercaBitArray, non serve perché effettuato in ricercaID_LCP
			this.cardinality = b.cardinality();
			this.id_lcp=-1;
9. inserire query di verifica delle tabelle (ESQL di cocoon)
11.implementare postgress
12.design delle pagine di amministrazione
13.mkiso fs per creazione di cd autoboot con applicazione customizzata completa
14. completare inserimento e cancellazione notizie (con maschere)
15bis. cosa fa se metto 'TIT=vie piazze trieste' ??
16. guardare log4j
17. da verificare liste per titolo su treviso: pochi titoli che iniziano in alfabeto
18. ricerca per più parole in liste
query:
select distinct a.parola,b.parola
FROM notizie_posizione_parole a,notizie_posizione_parole b
WHERE (a.id_classe=2 and a.posizione_parola=0 and a.parola>='ill' ) 
and (b.id_classe=2 and b.posizione_parola=1 and b.parola>='|ruolo' ) 
and a.id_notizia=b.id_notizia 
and a.id_sequenza_tag=b.id_sequenza_tag
ORDER BY a.parola,b.parola LIMIT 10
19. verificare cosa è |ruolo
20. fare indice apposito su questi campi
21. eliminare la tabella parole da query successive nella creione liste
22. verificare il compact delle tabelle (riduzione spazio a seguito di aggiunta indici)
23. eliminare indice solo su parole su notizie_posizione_parole
24. stemma di numeri
25. aggiungere porter al catalogo demo
26. Try this URL: http://yourwebapp/?cocoon-reload=1
	"allow-reload" init parameter must be set to "true" in web.xml for
	this to work.
27. MdbJoinTableRecordReader fare un reader che faccia anche join di tabelle access. 
	Idee: impoerare prima le tabelle che servono in tabelle temporanee mysql e poi importare 
	dalle tabelle temporanee.


ALternative per liste e ordinamento:

1) faccio tabella temporanea classe-valore, scorro tutto notizie e metto il valore nella tab temporanea, 
   la ordino e riempio il campo posizione
   
2) in importazione aggiungo la posizione della parola in qualche tabella e poi ricostruisco

DbGateway, LoadData e JOpac2DBmanager da fare refactoring


TODO LISTE
-------------------------
pensamenti liste:
costruire in fase di import le liste già complete di autori, titolo etc per ciascuna classe

paginazione sulle liste: aggiungere chiave su notizie_posizione_parole per indicare da dove ripartire?

lista per due parole, bisogna spezzare (guarda la prima query che viene eseguita)

---
SELECT id_notizia, id_parola,posizione_parola,parola
FROM notizie_posizione_parole a, anagrafe_parole p 
WHERE a.id_parola=p.id and a.id_classe=1 and a.posizione_parola=0
and parola>='altan'
ORDER BY a.posizione_parola,p.parola
LIMIT 10

dato l'id 
---
SELECT * 
FROM l_classi_parole_notizie lcpn,l_classi_parole lcp  ,anagrafe_parole a
where lcpn.id_notizia=56855 and lcp.id=lcpn.id_l_classi_parole
and a.id=lcp.id_parola
---
SELECT * 
FROM l_classi_parole_notizie lcpn,l_classi_parole lcp, anagrafe_parole a, notizie_posizione_parole n
where lcpn.id_notizia=78923 and lcp.id=lcpn.id_l_classi_parole
and a.id=lcp.id_parola and n.id_notizia=lcpn.id_notizia and n.id_classe=lcp.id_classe and
n.id_parola=lcp.id_parola
---
SELECT id_notizia, id_parola,posizione_parola,parola
FROM notizie_posizione_parole a, anagrafe_parole p 
WHERE a.id_notizia=78923 and a.id_parola=p.id and a.id_classe=1 
ORDER BY a.posizione_parola


-----------------
il = 96
leone = 927

SELECT distinct id_parola, parola 
FROM notizie_posizione_parole a
WHERE (a.id_classe=2 and a.posizione_parola=0 and parola>='il' ) and (a.id_classe=2 and a.posizione_parola=1 and parola>='leone' )
ORDER BY parola LIMIT 10

update notizie_posizione_parole 
set parola = (select parola from anagrafe_parole where id=id_parola) where id_notizia =3

-------------------------------
CLOUDS:
select id_parola, sum(n_notizie) from l_classi_parole_notizie lcpn, l_classi_parole lcp where id_notizia in (5,6) and lcp.id=lcpn.id_l_classi_parole group by id_parola

select stemma, sum(n_notizie) from l_classi_parole_notizie lcpn, l_classi_parole lcp, anagrafe_parole a where id_notizia in (5,6) and lcp.id=lcpn.id_l_classi_parole and a.id=lcp.id_parola group by stemma order by stemma

CREATE TABLE `dbTrev`.`dettaglio_ricerche` (
  `id_notizia` INT NOT NULL,
  `id_ricerca` INT NOT NULL,
  PRIMARY KEY(`id_notizia`, `id_ricerca`)
)
ENGINE = MYISAM
CHARACTER SET utf8;


CREATE TABLE `dbTrev`.`ricerche` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `jsession_id` varchar(100) NOT NULL,
  `testo_ricerca` LONGTEXT,
  PRIMARY KEY(`id`),
  INDEX `jsession_idx`(`jsession_id`)
)
ENGINE = MYISAM
CHARACTER SET utf8;



--------------------------------
select stemma, sum(n_notizie) 
from l_classi_parole_notizie lcpn, l_classi_parole lcp, anagrafe_parole a, ricerche_dettaglio dr 
where lcpn.id_notizia=dr.id_notizia 
and lcp.id=lcpn.id_l_classi_parole 
and a.id=lcp.id_parola 
and dr.id_ricerca=4
and lcp.id_classe in (1,2)
group by stemma 
order by stemma
```