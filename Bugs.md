
```
ERRORI CLASSI MainTest
2. caricamento di l_classi_parole con id_parola=0 e id_classe=0 (i minimi sono 1)
	select * from l_classi_parole limit 10;
3. ricerca parola ANY=fratis
	perchË non restituisce nulla?
	select * from l_classi_parole where id_parola>=21740;
	nessuna delle ultime parole della tabella e' presente in temp_lcpn
	select * from temp_lcpn where id_notizia>=5508;
	mancano ultime 2 notizie?

ERRORI
1. controllare parola null in insertparole: se parola =* viene messa null
2. se doppio autore, nel caso delle liste, la numerazione della posizione parola riparte da 0. va stabilito un criterio per accorpare correttamente le sequenze di parole
   in notizie_posizione_parola inserire numero sequenza del tag
3. generando le tag clouds il numero di notizie sommato non corrisponde al numero di notizie effettive
4. switch stemmer sbaglia il parametro stemmer (paramSearch)
```