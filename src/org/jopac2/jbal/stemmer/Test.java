package org.jopac2.jbal.stemmer;

/* Classe di prova dell'algoritmo di stemming
 * ha la possibilità di:
 *
 * accettare parola da tastiera ( daTastiera(Stemmer) )
 * accettare parole da file e visualizzarle a video ( fileMonitor1(Stemmer, String percorso origine) )
 * accettare parole da file e controllarse l'output con un altro file (radice)
 * e segnalare le parole che sono diverso ( fileConCntrollo(Stemmer, String percorso origine, String percorso file di controllo) )
 */


import java.io.*;

public class Test {
    
    /** Creates a new instance of Test */
    public Test() {
    }
    
    public static void main(String[] args){
        Radice a = new StemmerItv2();
        @SuppressWarnings("unused")
		String percorso = "c:\\ita.txt";
        @SuppressWarnings("unused")
		String controllo = "c:\\ita1.txt";
        //String percorso = "c:\\Stopword.txt";
        //String controllo = "c:\\Stopword.txt";
        daTastiera(a);
        //fileMonitor1(a, percorso);
        //fileConControllo(a, percorso, controllo);
        
    }
    
    public static void daTastiera(Radice a){
        String prova = new String();
        //per leggere da tastiera
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader myInput = new BufferedReader (in);
        
        while(true){
            try {
                prova = myInput.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (prova.equals("fine"))
                break;
            prova = a.radice(prova);
            System.out.println(prova);            
        }
    }
    
    public static void fileMonitor(Radice s, String path){
       char[] w = new char[501];
       try{
           FileInputStream in = new FileInputStream(path);
           try{
               while(true){
                   int ch = in.read();
                       if (Character.isLetter((char) ch)){
                           int j = 0;
                           while(true){
                               ch = Character.toLowerCase((char) ch);
                               w[j] = (char) ch;
                               if (j < 500)
                                   j++;
                               ch = in.read();
                               if (!Character.isLetter((char) ch)){
                                /* to test add(char ch) */
                                   for (int c = 0; c < j; c++)
                                       s.add(w[c]);
                                /* or, to test add(char[] w, int j) */
                                /* s.add(w, j); */
                                   s.stem();
                                   String u;
                                /* and now, to test toString() : */
                                   u = s.toString();
                                /* to test getResultBuffer(), getResultLength() : */
                                /* u = new String(s.getResultBuffer(), 0, s.getResultLength()); */
                                   System.out.print(u);
                                   break;
                               }
                           }
                       }
                       if (ch < 0)
                           break;
                       System.out.print((char)ch);
                   }
               } catch (IOException e){
                   System.out.println("Error reading");
               }
           } catch (FileNotFoundException e){
               System.out.println("File not found");
           }
   }
    
    public static void fileConControllo(Radice s, String path1, String path2){
       String parola = null;
       String soluzione = null;
       String parolaB;
       FileInputStream file1 = null;
       FileInputStream file2 = null;
       int numeroParole = 0;
       int errore = 0;
       
       try {
            file1 = new FileInputStream(path1);
       } catch (FileNotFoundException ex) {
            System.out.println("File not found " + path1);
            return;
       }
       InputStreamReader inStreamReader = new InputStreamReader(file1);
       BufferedReader entrata = new BufferedReader(inStreamReader);
       
       try {
            file2 = new FileInputStream(path2);
       } catch (FileNotFoundException ex) {
            System.out.println("File not found " + path2);
            return;
       }
       InputStreamReader inStreamReader1 = new InputStreamReader(file2);
       BufferedReader controllo = new BufferedReader(inStreamReader1);
       
       while(true){
           try {
               parola = entrata.readLine();
           } catch (IOException ex) {
               System.out.println("Error reading: parola da leggere");
           }
           if (parola == null){
               //System.out.println("Error reading: parola da leggere");
               break;
           }
           parola.toLowerCase();
           parolaB = parola;
           
           try {
               soluzione = controllo.readLine();
           } catch (IOException ex) {
               System.out.println("Error reading: soluzione da leggere");
           }
           if (soluzione == null)
                   break;
           soluzione.toLowerCase();
           parola = s.radice(parola);
           numeroParole++;
           
           if (!parola.equals(soluzione)){
               errore++;
               System.out.println(parolaB + " --> " + parola + " soluzione: " + soluzione);
           }
       }
       
       System.out.println("---------------");
       System.out.println("Fine file");
       System.out.println("---------------");
       System.out.println("Risultato:");
       System.out.println("Parole analizzate: " + numeroParole);
       System.out.println("Parole errate: " + errore);
       double media = (((double)errore)/numeroParole)*100;
       
       System.out.println("Percentuale di errore: " + media + " %");       
       return;
   }
   
    public static void fileMonitor1(Radice s, String path){
       String parola = null;
       FileInputStream file1 = null;
       
       try {
            file1 = new FileInputStream(path);
       } catch (FileNotFoundException ex) {
            System.out.println("File not found" + path);
       }
       InputStreamReader inStreamReader = new InputStreamReader(file1);
       BufferedReader entrata = new BufferedReader(inStreamReader);
        
       while(true){
           try {
               parola = entrata.readLine();
           } catch (IOException ex) {
            ex.printStackTrace();
           }
           if (parola == null)
                   break;
           parola.toLowerCase();
           parola = s.radice(parola);
           System.out.println(parola);
       }
   }
    
    
    
}
