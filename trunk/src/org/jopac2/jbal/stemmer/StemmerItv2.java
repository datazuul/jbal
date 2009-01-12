package org.jopac2.jbal.stemmer;

/* La classe stemmerItv2 trasforma la parola nella sua radice.
 * La parola immessa viene prima messa in un array di caratteri
 * poi vengono chiamati il metodi stem()
 *
 * Per usare la classe basta chiamare il metodo radice immettendo
 * la parola di cui si vuole trovare la radice
 *
 */

public class StemmerItv2 extends Radice {
    private boolean modifica; // se la parola viene troncata nello step1 non deve passare per lo step2
    
    private int rv; // variabili dove vengono salvati i valori dei puntatori
    private int r1; // che rendono possibile la scelta di troncare o no
    private int r2; // una parola
    
    public StemmerItv2(){
        super();
        modifica = false;
        rv=0;
        r1=0;
        r2=0;
    }
    
    /* Rappresenta tutti i passi da fare per trovare la radice di una parola.
     * k e' uguale a i (numero di caratteri immessi nell'array b[]) ma decrementato di uno
     * dato che i viene incrementato dopo aver messo in dato in b[], allora con l'ultimo
     * carattere i viene incrementato puntanto sulla prima posizone vuota di b[].
     * step(k) toglie tutti gli accenti gravi
     * puntatorer1r2() determinano i valori di r1 e r2
     * puntatorerv() determina il valore di rv
     * step0() e step1() toglie i suffissi per i sostantivi
     * step2() viene eseguito solo se nello step1() non e' stato non ci sono stati troncamenti
     * step3a() toglie la vocale finale
     * stem3b() toglie l'h finale se e' preceduta dal carattere 'c' o 'g'
     */
    public void stem(){
        k = i - 1;
        
        rv=0;
        r1=0;
        r2=0;

        if (k > 1){
           step(k);
           
           puntatorer1r2();
           puntatorerv();
           //prova(); // funzione per controllare se r1 r2 e rv 
                      // sono stati impostati in modo corretto
           
           step0();
           step1();
           if(modifica) step2();
           step3a();
           step3b();
       }
       i_end = k+1;
       i = 0;
    }

    /* step(int i) toglie tutti gli accenti gravi
     */
    private void step(int i) {
        for (int h=0; h<i+1; h++ ){
            switch (b[h]){
                case 225: { b[h]='a'; break; } // a acuta
                case 233: { b[h]='e'; break; } // e acuta
                case 237: { b[h]='i'; break; } // i acuta
                case 243: { b[h]='o'; break; } // o acuta
                case 250: { b[h]='u'; break; } // u acuta
                default: break;
            }
        }
        return;       
    }
    
    /* step0() toglie determinati suffissi se e solo se sono preceduti da
     * ando e endo che rimangono invariati oppure da
     * ar er ir che diventano rispettivamente are ere ire
     *
     * La variabile mod segnala se sono stati trovati questi suffissi
     * La variabile s memorizza quante lettere sono state tolte dalla fine della
     *    parola
     * se la variabile mod è true viene controllato se ci sono anche i suffissi
     *    ando endo ar er ir e così vengono modificati e si esce
     * senno viene ripristinata la parola iniziale riaggiungento a k la s
     */
    private void step0() {
        int s = 0;
        boolean mod = false;
        if ((k>6)&&(!mod)){ //6 lettere
            if (ends("gliela")&&(k-6>=rv)&&(!mod)) { k = k - 6; s = 6; mod = true; }
            if (ends("gliele")&&(k-6>=rv)&&(!mod)) { k = k - 6; s = 6; mod = true; }
            if (ends("glieli")&&(k-6>=rv)&&(!mod)) { k = k - 6; s = 6; mod = true; }
            if (ends("glielo")&&(k-6>=rv)&&(!mod)) { k = k - 6; s = 6; mod = true; }
            if (ends("gliene")&&(k-6>=rv)&&(!mod)) { k = k - 6; s = 6; mod = true; }
        }
        if ((k>4)&&(!mod)){ //4 lettere
            if (ends("sene")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("mela")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("mele")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("meli")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("melo")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("mene")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("tela")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("tele")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("teli")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("telo")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("tene")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("cela")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("cele")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("celi")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("celo")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("cene")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("vela")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("vele")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("veli")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("velo")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
            if (ends("vene")&&(k-4>=rv)&&(!mod)) { k = k - 4; s = 4; mod = true; }
        }
        if((k>3)&&(!mod)) //3 lettere
            if (ends("gli")&&(k-3>=rv)&&(!mod)) { k = k - 3; s = 3; mod = true; }
        if((k>2)&&(!mod)){ //2 lettere
            if (ends("ci")&&(k-2>=rv)&&(!mod)) { k = k - 2; s = 2; mod = true; }
            if (ends("la")&&(k-2>=rv)&&(!mod)) { k = k - 2; s = 2; mod = true; }
            if (ends("le")&&(k-2>=rv)&&(!mod)) { k = k - 2; s = 2; mod = true; }
            if (ends("li")&&(k-2>=rv)&&(!mod)) { k = k - 2; s = 2; mod = true; }
            if (ends("lo")&&(k-2>=rv)&&(!mod)) { k = k - 2; s = 2; mod = true; }
            if (ends("mi")&&(k-2>=rv)&&(!mod)) { k = k - 2; s = 2; mod = true; }
            if (ends("ne")&&(k-2>=rv)&&(!mod)) { k = k - 2; s = 2; mod = true; }
            if (ends("si")&&(k-2>=rv)&&(!mod)) { k = k - 2; s = 2; mod = true; }
            if (ends("ti")&&(k-2>=rv)&&(!mod)) { k = k - 2; s = 2; mod = true; }
            if (ends("vi")&&(k-2>=rv)&&(!mod)) { k = k - 2; s = 2; mod = true; }
        }
        if (mod) {
            if (((ends("ando"))||(ends("endo")))&&(k-4>=rv)) return;
            if (ends("ar")&&(k-2>=rv)) { setto("are"); return; }
            if (ends("er")&&(k-2>=rv)) { setto("ere"); return; }
            if (ends("ir")&&(k-2>=rv)) { setto("ire"); return; }
        }
        k = k + s;
        return;
    }
    
    /* step1() toglie determinati suffissi
     *
     * La variabile modifica salva se in questo passaggio e' stato tolto qualche suffisso
     * perche' se viene tolto lo step2() deve essere saltato.
     */
    private void step1() {
        if (k>10){ //10 lettere
            if ((ends("ativamente"))&&(k-10>=r2)) { k = k - 10; modifica = false; return;}
            if ((ends("abilamente"))&&(k-10>=r2)) { k = k - 10; modifica = false; return;}
        } 
        if (k>8){ //8 lettere
            if ((ends("icazione"))&&(k-8>=r2)) { k = k - 8; modifica = false; return;}
            if ((ends("icazioni"))&&(k-8>=r2)) { k = k - 8; modifica = false; return;}
            
            if ((ends("ivamente"))&&(k-8>=r2)) { k = k - 8; modifica = false; return;}
            if ((ends("osamente"))&&(k-8>=r2)) { k = k - 8; modifica = false; return;}
            if ((ends("icamente"))&&(k-8>=r2)) { k = k - 8; modifica = false; return;}
        }
        if (k>7){ //7 lettere
            if ((ends("icatore"))&&(k-7>=r2)) { k = k - 7 ; modifica = false; return;}
            if ((ends("icatori"))&&(k-7>=r2)) { k = k - 7; modifica = false; return;}
            
            if ((ends("abilit�"))&&(k-7>=r2)) { k = k - 7; modifica = false; return;}
            
            if ((ends("icativo"))&&(k-7>=r2)) { k = k - 7; modifica = false; return;}
            if ((ends("icativi"))&&(k-7>=r2)) { k = k - 7; modifica = false; return;}
            if ((ends("icativa"))&&(k-7>=r2)) { k = k - 7; modifica = false; return;}
            if ((ends("icative"))&&(k-7>=r2)) { k = k - 7; modifica = false; return;}
        }
        if (k>6){ //6 lettere
            if (ends("azione")&&(k-6>=r2)) { k = k - 6; modifica = false; return;}
            if (ends("azioni")&&(k-6>=r2)) { k = k - 6; modifica = false; return;}
            if (ends("atrice")&&(k-6>=r2)) { k = k - 6; modifica = false; return;}
            if (ends("atrici")&&(k-6>=r2)) { k = k - 6; modifica = false; return;}
            if (ends("uzione")&&(k-6>=r2)) { setto("u"); modifica = false; return;}
            if (ends("uzioni")&&(k-6>=r2)) { setto("u"); modifica = false; return;}
            if (ends("usione")&&(k-6>=r2)) { setto("u"); modifica = false; return;}
            if (ends("usioni")&&(k-6>=r2)) { setto("u"); modifica = false; return;}
            if (ends("amento")&&(k-6>=rv)) { k = k - 6; modifica = false; return;}
            if (ends("amenti")&&(k-6>=rv)) { k = k - 6; modifica = false; return;}
            if (ends("imento")&&(k-6>=rv)) { k = k - 6; modifica = false; return;}
            if (ends("imenti")&&(k-6>=rv)) { k = k - 6; modifica = false; return;}
            
            if (ends("amente")&&(k-6>=r1)) { k = k - 6; modifica = false; return;}
        }
        if (k>5){ //5 lettere
            if (ends("abile")&&(k-5>=r2)) { k = k - 5; modifica = false; return;}
            if (ends("abili")&&(k-5>=r2)) { k = k - 5; modifica = false; return;}
            if (ends("ibile")&&(k-5>=r2)) { k = k - 5; modifica = false; return;}
            if (ends("ibili")&&(k-5>=r2)) { k = k - 5; modifica = false; return;}
            if (ends("mente")&&(k-5>=r2)) { k = k - 5; modifica = false; return;}
            if (ends("logia")&&(k-5>=r2)) { setto("log"); modifica = false; return;}
            if (ends("logie")&&(k-5>=r2)) { setto("log"); modifica = false; return;}
            
            if ((ends("icit�"))&&(k-5>=r2)) { k = k - 5; modifica = false; return;}
            if ((ends("ivit�"))&&(k-5>=r2)) { k = k - 5; modifica = false; return;}
            
            if ((ends("ativo"))&&(k-5>=r2)) { k = k - 5; modifica = false; return;}
            if ((ends("ativi"))&&(k-5>=r2)) { k = k - 5; modifica = false; return;}
            if ((ends("ativa"))&&(k-5>=r2)) { k = k - 5; modifica = false; return;}
            if ((ends("ative"))&&(k-5>=r2)) { k = k - 5; modifica = false; return;}
            if ((ends("atore"))&&(k-5>=r2)) { k = k - 5; modifica = false; return;}
            if ((ends("atori"))&&(k-5>=r2)) { k = k - 5; modifica = false; return;}
        }
        if (k>4){ //4 lettere
            if (ends("anza")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            if (ends("anze")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            if (ends("iche")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            if (ends("ichi")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            if (ends("ismo")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            if (ends("ismi")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            if (ends("ista")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            if (ends("iste")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            if (ends("isti")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            if (ends("ist�")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            if (ends("ist�")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            if (ends("ist�")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            if (ends("ante")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            if (ends("anti")&&(k-4>=r2)) {k = k - 4; modifica = false; return;}
            
            if (ends("enza")&&(k-4>=r2)) { setto("ente"); modifica = false; return;}
            if (ends("enze")&&(k-4>=r2)) { setto("ente"); modifica = false; return;}
        }
        if(k>3) { //3 lettere
            if (ends("ico")&&(k-3>=r2)) { k = k - 3; modifica = false; return;}
            if (ends("ici")&&(k-3>=r2)) { k = k - 3; modifica = false; return;}
            if (ends("ica")&&(k-3>=r2)) { k = k - 3; modifica = false; return;}
            if (ends("ice")&&(k-3>=r2)) { k = k - 3; modifica = false; return;}
            if (ends("oso")&&(k-3>=r2)) { k = k - 3; modifica = false; return;}
            if (ends("osi")&&(k-3>=r2)) { k = k - 3; modifica = false; return;}
            if (ends("osa")&&(k-3>=r2)) { k = k - 3; modifica = false; return;}
            if (ends("ose")&&(k-3>=r2)) { k = k - 3; modifica = false; return;}
        
            if (ends("it�")&&(k-3>=r2)) { k = k - 3; modifica = false; return;}
            
            if (ends("ivo")&&(k-3>=r2)) { k = k - 3; modifica = false; return;}
            if (ends("ivi")&&(k-3>=r2)) { k = k - 3; modifica = false; return;}
            if (ends("iva")&&(k-3>=r2)) { k = k - 3; modifica = false; return;}
            if (ends("ive")&&(k-3>=r2)) { k = k - 3; modifica = false; return;}
        }
        modifica = true;
        return;
     }
     
    /* step2() toglie determinati suffissi
     * Viene eseguita se e solo se la variabile modifica � true.
     * Questa variabile viene modificata nello step1(), diventa true se non
     * viene trovato nessun suffisso senno diventa false. 
     */
    private void step2() {
        if (k>8){ //8 lettere
            if (ends("erebbero")&&(k-8>=rv)) { k = k - 8; return;}
            if (ends("irebbero")&&(k-8>=rv)) { k = k - 8; return;}  
        }
        if (k>6){ //6 lettere
            if (ends("assero")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("assimo")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("eranno")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("erebbe")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("eremmo")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("ereste")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("eresti")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("essero")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("iranno")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("irebbe")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("iremmo")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("ireste")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("iresti")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("iscano")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("iscono")&&(k-6>=rv)) { k = k - 6; return;}
            if (ends("issero")&&(k-6>=rv)) { k = k - 6; return;}
        }
        if (k>5){ //5 lettere
            if (ends("arono")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("avamo")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("avano")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("avate")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("eremo")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("erete")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("erono")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("evamo")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("evano")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("evate")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("iremo")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("irete")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("irono")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("ivamo")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("ivano")&&(k-5>=rv)) { k = k - 5; return;}
            if (ends("ivate")&&(k-5>=rv)) { k = k - 5; return;}
        }
        if (k>4){ //4 lettere
            if (ends("ammo")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("ando")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("asse")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("assi")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("emmo")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("enda")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("ende")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("endi")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("endo")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("erai")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("erei")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("yamo")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("iamo")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("immo")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("irai")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("irei")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("isca")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("isce")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("isci")&&(k-4>=rv)) {k = k - 4; return;}
            if (ends("isco")&&(k-4>=rv)) {k = k - 4; return;}
        }
        if(k>3) { //3 lettere
            if (ends("ano")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("are")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ata")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ate")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ati")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ato")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ava")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("avi")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("avo")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("er�")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ere")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("er�")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ete")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("eva")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("evi")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("evo")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ir�")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ire")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ir�")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ita")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ite")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("iti")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ito")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("iva")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ivi")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ivo")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ono")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("uta")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("ute")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("uti")&&(k-3>=rv)) { k = k - 3; return;}
            if (ends("uto")&&(k-3>=rv)) { k = k - 3; return;}
        }
        if (k>2){ //2 lettere
            if (ends("ar")&&(k-2>=rv)) { k = k - 2; return;}
            if (ends("ir")&&(k-2>=rv)) { k = k - 2; return;}
        }
        return;
     }
    
    /* stem3a toglie le vocali finali
     * toglie tutte le vocali finali fatto eccezione per la u che se è presente
     * fa parte della radice.
     * e toglie la i se è seguita da una vocale
     */
    private void step3a() {
        boolean precedente = false;
        
        if (k<3) return;
        if ((k-1)>=rv){
            switch (b[k]){
                case 'a':
                case 'e':
                case 'i':
                case 'o':
                //case 'u':
                	/**
                	 * TODO: Controllare le lettere accentate con la versione originale!!
                	 * (c'e' stato un problema di codifca caratteri sui sorgenti)
                	 * 
                	 */
                case 'à': // a accentata
                case 'è': // e accentata
                case 'ì': // i accentata
                case 'ò': { k = k-1; precedente = true; break; } // o accentata
                //case 'ù': { k = k-1; break; } // u accentata
                default: return;
            }
        }
        
        if ((b[k]=='i') && ((k-1)>=rv)&& precedente) k = k-1;
        if ((b[k+1]=='i')&&(!cons(k))&&(!cons(k+2))) k = k+1;
        return;
    }
    
    /* step3b toglie la h dalle parole che finiscono per ch o gh
     */
    private void step3b() {
        if ((k>2)&&((k-2)>=rv)){
            if (ends("ch")) { k = k - 1; return;}
            if (ends("gh")) { k = k - 1; return;}
        }
        return;
    }
    
    /* Funzione che determina i valori di r1 e r2
     * che decino se una determinato suffisso pu� essere eliminato o no
     * r1 viene determinato come la prima consonate preceduta da vocale
     * r2 viene determinato come la prima consonate preceduta da vocale dopo r1
     */
    private void puntatorer1r2(){
        int indice = 0;
        
        if (indice == k) 
            return;
        //va avanti fino a quando non trova una vocale
        while(true){
            if (!cons(indice))
                break;
            else{
                indice++;
                if (indice > k){
                    r1=k;
                    r2=k;
                    return;
                } 
            }
        }
        //va avanti fino a quando non trova la prima consonante
        while(true){
            if ((indice+1)>=k){
                if ((b[indice]=='i')&&(!cons(indice-1))&&(!cons(indice+1))){
                    r1=indice;
                    break;
                }
            }
            
            if (cons(indice)){
                r1=indice;
                break;
            }
            else{
                indice++;
                if (indice > k){
                    r1=k;
                    r2=k;
                    return;
                } 
            }
        }
        
        //va avanti fino a quando non trova una vocale
        while(true){
            if (!cons(indice))
                break;
            else{
                indice++;
                if (indice > k){
                    r2=k;
                    return;
                } 
            }
        }
        //va avanti fino a quando non trova la prima consonante
        while(true){
            if ((indice+1)>=k){
                if ((b[indice]=='i')&&(!cons(indice-1))&&(!cons(indice+1))){
                    r2=indice;
                    break;
                }
            }
            
            if (cons(indice)){
                r2=indice;
                break;
            }
            else{
                indice++;
                if (indice > k){
                    r2=k;
                    return;
                } 
            }
        }
        return;
    }
    
    
    /* Funzione che determina il valore di rv
     * che decide se una determinato suffisso pu� essere eliminato o no.
     * Se la seconda lettere � una consonate rv diventa la prima vocale successiva.
     * Se le prime due lettere sono vocali rv diventa la prima consonante successiva.
     * Negli altri casi rv diventa la terza lettera.
     * Se per� la parola � pi� corta rv rimane a zero.
     */
    private void puntatorerv(){
        int indice = 0;
        int amen = k;
        
        if (indice == k)
             return;
        
        // if dove la seconda lettera � una consonante
        if (cons(1)) {
            indice = 1;
            //va avanti fino a quando non trova una vocale
            while(true){
                if (!cons(indice)){
                    rv=indice;
                    return;
                }
                else{
                    indice++;
                    if (indice > k){
                        rv=amen;
                        return;
                    } 
                }
            }
        } // fine if dove la seconda lettera � una consonante
        
        // if dove le prime due lettere sono vocali
        if ((!cons(0))&&(!cons(1))) {
            indice = 1;
            while(true){
                if (cons(indice)){
                    rv=indice;
                    return;
                }
                else{
                    indice++;
                    if (indice > k){
                        rv=amen;
                        return;
                    } 
                }
            }
        } // fine if dove le prime due lettere sono vocali
        
        // if nelle altre condizioni superiori a 3 lettere
        if (k>1){
            rv = 2;
        } // fine if nelle altre condizioni superiori a 3 lettere
        
        // if nelle altre condizioni inferiore a 3 lettere
        if (k<2){
            rv = amen;
        } // fine if nelle altre condizioni inferiore a 3 lettere
        return;
    }
    
    /* Determina se all'iesima posizone c'� una consonante o una vocale
     * torna true se � una consonante
     * e false se � una vocale
     */
    private boolean cons(int i){
       switch (b[i]){
           case 'a':
           case 'e':
           case 'i':
           case 'o':
           case 'u':
           case 225:
           case 233:
           case 237:
           case 243:
           case 250: return false;
           default: return true;
       }
   }
}







