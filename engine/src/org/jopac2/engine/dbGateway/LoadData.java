 package org.jopac2.engine.dbGateway;

 /*******************************************************************************
 *
 *  JOpac2 (C) 2002-2007 JOpac2 project
 *
 *     This file is part of JOpac2. http://jopac2.sourceforge.net
 *
 *  JOpac2 is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  JOpac2 is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with JOpac2; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *******************************************************************************/

 /*
 * @author	Albert Caramia
 * @version ??/??/2002
 * 
 * @author	Romano Trampus
 * @version ??/??/2002
 */


import java.io.*;
//import java.lang.reflect.Method;
import java.util.*;
import java.sql.*;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.LoadDataInterface;
import org.jopac2.jbal.Readers.ParoleSpoolerInterface;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.utils.ClasseDettaglio;
import org.jopac2.utils.TokenWord;
import org.jopac2.utils.ZipUnzip;

import com.ibm.icu.text.Transliterator;
import com.whirlycott.cache.Cache;


/** @todo
 *
 *   CREARE ANCHE IL FILE DELLE BIBLIOTECHE!!
 *     */
public class LoadData implements LoadDataInterface {
  public Connection conn[];
  private PreparedStatement preparedNotizia[];
  private PreparedStatement preparedTempLCP[];

  public ResultSet rs_cl;
  public static final boolean RECOVER=true;
  public static final boolean CLEAN=!RECOVER;

  public long id_ap_f=0;
  public long id_nz_f=0;
  public long inz=0;
  private int curPrepNz=0;
  
  private int maxValues4prepared=100;

  private Vector<ClasseDettaglio> cl_dettaglio;
  private static Transliterator t;
  //private String dl;
  //private String ft;
  //private String rt;
  
  public void appendNotizie() {
    try {
      Statement stmt=conn[0].createStatement();
      ResultSet rs=stmt.executeQuery("SELECT Max(notizie.id) AS MaxOfid FROM notizie");
      if(rs.next()) {
        id_nz_f=rs.getInt(1);
        inz=id_nz_f;
      }
      rs.close();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    
    try {
        Statement stmt=conn[0].createStatement();
        ResultSet rs=stmt.executeQuery("SELECT Max(anagrafe_parole.id) AS MaxOfid FROM anagrafe_parole");
        if(rs.next()) {
          id_ap_f=rs.getInt(1);
          //inz=id_nz_f;
        }
        rs.close();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
  }

  public String tipoNotizia="ISO2709";
  public long idTipo=0;

  
public void init(String outDir,long idTipo) {
	cl_dettaglio=DbGateway.initClDettaglio(conn[0],idTipo);
  }

  public void close() {
	  // do nothing
  }
  
  private Hashtable<String,String> LCPbuffer=null;
  private long currentIdNotizia=-1;
  
  public void InsertParole(String valore,long id_notizia, long idSequenzaTag, long id_classe, 
		  ParoleSpoolerInterface paroleSpooler) throws SQLException { // long id_de, 
    String parola;
    //boolean asterisco_trovato=!(valore.indexOf(" *")<DbGateway.MAX_POSIZIONE_ASTERISCO)||!valore.contains(" *");

    StringTokenizer tk=new StringTokenizer(DbGateway.processaMarcatori(valore),DbGateway.SEPARATORI_PAROLE);

    if(id_notizia!=currentIdNotizia) {
    	if(LCPbuffer!=null) {
    		LCPbuffer.clear();
    		LCPbuffer=null;
    	}
    	LCPbuffer=new Hashtable<String,String>();
    	currentIdNotizia=id_notizia;
    }
    
    long posizione_parola=0;
    
    while(tk.hasMoreTokens()) {
      parola=tk.nextToken().trim();
      
      if(parola.startsWith("*")) {
    	  //asterisco_trovato=true;
    	  parola=parola.substring(1);
      }

      if((parola!=null)&&(parola.length()>0)) {
	      long id_parola=InsertParola(parola,paroleSpooler);
	
	      String c=code(id_parola,id_classe);
	      
	      if(!LCPbuffer.containsKey(c)) {
	    	  LCPbuffer.put(c,"ok");
	      	  executePreparedTempLCP(id_notizia,id_parola,id_classe);
	      }
	      
	      
	      /*
	      if(asterisco_trovato) {
	    	  DbGateway.insertNotiziePosizioneParole(conn[0],id_notizia, idSequenzaTag, id_classe, id_parola, posizione_parola, parola);
	    	  posizione_parola++;
	      }
	      */
      }
    }
  }
  
  private String code(long id_parola,long id_classe) {
	  return Long.toString(id_parola)+"|"+id_classe;
  }
  
  /**
   * TODO: salvare all'ultimo giro!!
   */
  
  private int currentSegTempLCP=0;
  private int currentPrepTempLCP=0;
  private mySet[] arTempLCP;
  
  public void executePreparedTempLCP(long id_notizia,long id_parola,long id_classe) throws SQLException {
	  preparedTempLCP[currentPrepTempLCP].setLong(currentSegTempLCP*3+1,id_notizia);
	  preparedTempLCP[currentPrepTempLCP].setLong(currentSegTempLCP*3+2,id_parola);
	  preparedTempLCP[currentPrepTempLCP].setLong(currentSegTempLCP*3+3,id_classe);
	  
	  arTempLCP[currentSegTempLCP].setFirst(id_notizia);
	  arTempLCP[currentSegTempLCP].setSecond(id_parola);
	  arTempLCP[currentSegTempLCP].setThird(id_classe);
	  
	  currentSegTempLCP=(++currentSegTempLCP)%(maxValues4prepared);
	if(currentSegTempLCP==0) {
		try {
			preparedTempLCP[currentPrepTempLCP].execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		preparedTempLCP[currentPrepTempLCP].clearParameters();
		currentPrepTempLCP=(++currentPrepTempLCP)%preparedTempLCP.length;
	}
  }
  
  /**
   * TODO: c'e' un metodo simile in DbGateway.java. Non sono unificati a causa di id_ap_f.
   * Dovrebbe restare solo quello in DbGateway.java
   * @param parola
   * @param paroleSpooler
   * @return
   */
  public long InsertParola(String parola, ParoleSpoolerInterface paroleSpooler) {
	  long r=-1;
      parola=DbGateway.pulisciParola(parola);
      //parola=t.transliterate(parola);
      r=paroleSpooler.getParola(parola);
	  if(r==-1) {
    	  id_ap_f++;
    	  r=id_ap_f;
    	  if(parola.length()<50) paroleSpooler.insertParola(id_ap_f,parola);
      }
      return r;
  }
  
  public Long getDbParola(String parola) {
	  Long r=null;
	  try {
		  Statement stmt=conn[0].createStatement();
		  ResultSet rs = stmt.executeQuery("select id from anagrafe_parole where parola = '"+parola+"'");
	
	      if(rs.next()) { // c'e' un record
	        r=rs.getLong(1);
	      }
	      rs.close();
	      stmt.close();
	  }
      catch(Exception e) {}
      return r;
  }

  public void process(String stringa,String tipo,long idTipo, ParoleSpoolerInterface paroleSpooler) throws SQLException {
	  RecordInterface notizia;
	  Enumeration<TokenWord> tags=null;
  
    //long id_notizia=0;

	  notizia=RecordFactory.buildRecord(0,stringa,tipo,0);
	if(notizia.toString()!=null) {

	    long idSequenzaTag=0;
	    String record = notizia.getBid();
	    id_nz_f++;
	    // fa schifo, ma se non c'e' il bid e' l'unico modo. Fare
	    // qualche cosa di meglio
	    if(record.equals("0")) {record=String.valueOf(id_nz_f);}
	    String valore_tag,tag;
	
		curPrepNz=(++curPrepNz)%preparedNotizia.length;
		
		preparedNotizia[curPrepNz].setLong(1,id_nz_f);
		preparedNotizia[curPrepNz].setString(2,record);
		preparedNotizia[curPrepNz].setLong(3,idTipo);
		preparedNotizia[curPrepNz].setBytes(4, ZipUnzip.compressString(notizia.toString()));
		//preparedNotizia[curPrepNz].setString(4,stringa);
		preparedNotizia[curPrepNz].execute();
		
		tags=notizia.getItems();
		
		while(tags.hasMoreElements()) {
			long id_classe=-1;
			TokenWord tw=tags.nextElement();
			valore_tag=tw.getValue();
			tag=tw.getTag();
			
			int k=cl_dettaglio.indexOf(new ClasseDettaglio(-1,-1,-1,tag,tw.getDataelement()));
			if(k>=0) {
				ClasseDettaglio cd=cl_dettaglio.elementAt(k);
				id_classe=cd.getIdClasse();
			}
	        if(id_classe!=-1) {
	        	InsertParole(valore_tag,id_nz_f,idSequenzaTag++,id_classe,paroleSpooler); 
	        }
		}
	}
    notizia.destroy();
    notizia=null;
    stringa=null;
    if(tags!=null) {tags=null;}
  }
  
  public void destroy() {
	  for(int i=0;i<currentSegTempLCP;i++) {
		  String tempLCP="insert into temp_lcpn (id_notizia,id_parola,id_classe) values ("+
		  	arTempLCP[i].getFirst()+","+arTempLCP[i].getSecond()+","+arTempLCP[i].getThird()+")";
		  try {
			Statement st=conn[0].createStatement();
			st.execute(tempLCP);
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  }
  }
  

  
  public void doJob(InputStream dataFile,String dbType,String temporaryDir,
		  long classID, Cache cache, Transliterator t) {
	  this.t=t;
	  //Cache cache=DbGateway.getCache();
	  ParoleSpoolerInterface paroleSpooler=new ParoleSpooler(conn,maxValues4prepared,cache);
    
	  long start_time=System.currentTimeMillis();

      tipoNotizia=dbType;
      String outDir=temporaryDir;

      idTipo=classID;

      RecordInterface n=RecordFactory.buildRecord(0,"",tipoNotizia,0);
      //String terms=n.getTerminators();
      if(idTipo>=0) {
        init(outDir,idTipo);
        appendNotizie();
        try {
          
          RecordReader bf=n.getRecordReader(dataFile);
          bf.setup(conn[0]);
          bf.setTipoNotizia(tipoNotizia);
          bf.setIdTipo(idTipo);
          bf.setParoleSpooler(paroleSpooler);
          
          
          /* R.T. 05/06/2006: ciclo di lettura spostato nel RecordReader per
           * facilitare l'uso di SAXParser per dati in formato XML
           */
          bf.parse(bf,this);
          
          bf.destroy(conn[0]);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      else {
        System.out.println("Tipo "+tipoNotizia+" non trovato nella tabella tipi_notizie.");
      }
      
      close();
      
      try {
		paroleSpooler.destroy();
	} catch (SQLException e) {
		e.printStackTrace();
	}
    
    System.out.println("tempo totale minuti:"+(System.currentTimeMillis()-start_time)/(60000.0));

  }


  public LoadData(Connection conn[]) {
  	  this.conn=conn;
  	  
  	  /**
  	   * TODO: hsqldb non consente inserimenti multipli 
  	   */
  	  if(conn[0].toString().contains("hsqldb")||
  			conn[0].toString().contains("derby")) maxValues4prepared=1;
  	  
  	  arTempLCP=new mySet[maxValues4prepared];
  	  
  	  for(int i=0;i<maxValues4prepared;i++) arTempLCP[i]=new mySet();
  	
  	  preparedNotizia = new PreparedStatement[conn.length];
  	  preparedTempLCP = new PreparedStatement[conn.length];
  	  
  	//maxValues4prepared

  	  String TempLCP="insert into temp_lcpn (id_notizia,id_parola,id_classe) values (?,?,?)";
  	  
  	  for(int i=0;i<maxValues4prepared-1;i++) {
  		TempLCP=TempLCP+",(?,?,?)";
  	  }
  	  
      try {
    	for(int i=0;i<conn.length;i++) {
    		preparedNotizia[i]=conn[i].prepareStatement("insert into notizie (id,bid,id_tipo,notizia) values (?,?,?,?)");
    	    preparedTempLCP[i]=conn[i].prepareStatement(TempLCP);
    	}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

  }
  
  private class mySet {
	  private long a,b,c;
	  mySet() {
	  }
	  
	public void setFirst(long first) {
		a=first;
	}
	
	public void setSecond(long second) {
		b=second;
	}
	
	public void setThird(long third) {
		c=third;
	}
	
	
	/**
	 * @return the first
	 */
	public long getFirst() {
		return a;
	}
	/**
	 * @return the second
	 */
	public long getSecond() {
		return b;
	}
	/**
	 * @return the third
	 */
	public long getThird() {
		return c;
	}
  }
  

}