package org.jopac2.jbal.stemmer;

public class Radice extends Stemmer {
		public Radice() {
			super();
		}
	   /* Funzione che automatizza tutto, basta immettere la parola di cui si vuole
	    * la radice e si ha come ritorno una stringa.
	    *
	    * Funzionamento:
	    * per prima cosa azzera l'array b dove finiscono le parole
	    * trasforma tutta la stringa in caratteri che con la funzione add()
	    * immette nell'array b e controlla se ci sono altri simboli oltre
	    * i caratteri, se si vengono saltati
	    *
	    * vedere se bisogna matenere quella funzionalità dato che cancella anche
	    * gli spazi.
	    */
	   public String radice (String parola){
	       //char[] w = new char[501];
	       int ch;
	       String u;
	       char cha;
	       
	       pulisci();
	       
	       for (int l=0; l < parola.length();l++){
	           ch = parola.charAt(l);
	           if (Character.isLetter((char) ch)){
	                   cha = Character.toLowerCase((char) ch);
	                   add(cha);
	           }
	       }
	       stem();
	       u = toString();
	       return u;
	   }
	   
	   private void pulisci(){
	       for(int i = 0 ; i<b.length; i++){
	           b[i]=0;
	       }
	   }

}
