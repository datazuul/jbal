 package org.jopac2.engine.dbengine.dbGateway;

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

import org.jopac2.engine.utils.ZipUnzip;
import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jbal.Readers.LoadDataInterface;
import org.jopac2.jbal.Readers.ParoleSpoolerInterface;
import org.jopac2.jbal.Readers.RecordReader;
import org.jopac2.jbal.stemmer.Radice;
import org.jopac2.jbal.xml.Mdb;
import org.jopac2.utils.ClasseDettaglio;
import org.jopac2.utils.TokenWord;

import com.whirlycott.cache.Cache;


/** @todo
 *
 *   CREARE ANCHE IL FILE DELLE BIBLIOTECHE!!
 *     */
public class LoadData implements LoadDataInterface {
  public Connection conn[];
  public String catalog="";
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
  
  private DbGateway dbGateway=null;

  private Vector<ClasseDettaglio> cl_dettaglio;
  
//  private String confDir=null;
  private String dbType=null;
  public String tipoNotizia="ISO2709";
  public long idTipo=-1;
  //private static Transliterator t;

  //private String dl;
  //private String ft;
  //private String rt;
  
  private String[] chToIndex=null;
  
  PrintStream out=null;
  PrintStream outputErrorRecords=out;
  
  public void appendNotizie() throws SQLException {
	  Statement stmt=null;
	  ResultSet rs=null;
    try {
      stmt=conn[0].createStatement();
      rs=stmt.executeQuery("SELECT Max(je_"+catalog+"_notizie.id) AS MaxOfid FROM je_"+catalog+"_notizie");
      if(rs.next()) {
        id_nz_f=rs.getInt(1);
        inz=id_nz_f;
      }
    }
    catch (SQLException ex) {
    	throw ex;
    }
    finally {
    	if(rs!=null) rs.close();
    	if(stmt!=null) rs.close();
    }
    
    try {
        stmt=conn[0].createStatement();
        rs=stmt.executeQuery("SELECT Max(je_"+catalog+"_anagrafe_parole.id) AS MaxOfid FROM je_"+catalog+"_anagrafe_parole");
        if(rs.next()) {
          id_ap_f=rs.getInt(1);
          //inz=id_nz_f;
        }
      }
      catch (SQLException ex) {
        throw ex;
      }
      finally {
      	if(rs!=null) rs.close();
      	if(stmt!=null) rs.close();
      }
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
private boolean clearDatabase;
  
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
		finally {
			preparedTempLCP[currentPrepTempLCP].clearParameters();
		}
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
	  Statement stmt=null;
	  ResultSet rs=null;
	  try {
		  stmt=conn[0].createStatement();
		  rs = stmt.executeQuery("select id from je_"+catalog+"_anagrafe_parole where parola = '"+parola+"'");
	
	      if(rs.next()) { // c'e' un record
	        r=rs.getLong(1);
	      }
	  }
      catch(Exception e) {}
      finally {
    	  try {
	    	  if(rs!=null) rs.close();
		      if(rs!=null) stmt.close();
    	  }
    	  catch(SQLException e) {}
      }
      return r;
  }
  
  private void createDB(Connection conn) throws SQLException {
	  out.println("Creating tables");
		dbGateway.createAllTables(conn, catalog);
  }
  
  private void inizializeDB(Connection conn) throws SQLException {
		
		out.println("Importing data types");

		dbGateway.importClassiDettaglio(chToIndex,conn,catalog,out); //confDir+"/dataDefinition/DataType.xml",out);
		
		out.println("Create DB 1st index");
		dbGateway.create1stIndex(conn, catalog);
		idTipo=dbGateway.getTypeID(conn, catalog, dbType);
		cl_dettaglio=DbGateway.initClDettaglio(conn, catalog, idTipo);
		appendNotizie();
		
	}

  public void process(byte[] stringa, ParoleSpoolerInterface paroleSpooler) throws Exception {
	  RecordInterface notizia=null;
	  Enumeration<TokenWord> tags=null;
  
    //long id_notizia=0;

	  try {
		  notizia=RecordFactory.buildRecord(0,stringa,dbType,0);
	  }
	  catch(Exception e) {
		  e.printStackTrace();
		  throw e;
	  }
	if(notizia.toString()!=null) {
	  	if(clearDatabase) {
			inizializeDB(conn[0]);
			clearDatabase=false;
	  	}

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
			
			if(tag.contains("/")) tag=tag.substring(tag.lastIndexOf("/")+1);
			
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
		  String tempLCP="insert into je_"+catalog+"_temp_lcpn (id_notizia,id_parola,id_classe) values ("+
		  	arTempLCP[i].getFirst()+","+arTempLCP[i].getSecond()+","+arTempLCP[i].getThird()+")";
		  try {
			DbGateway.execute(conn[0], tempLCP);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  }
  }
  

  
  public String[] doJob(InputStream dataFile,String dbType,String temporaryDir,
		  Cache cache, Radice stemmer) { //, Transliterator t) {
	  //this.t=t;
	  //Cache cache=DbGateway.getCache();
	  ParoleSpoolerInterface paroleSpooler=new ParoleSpooler(conn, catalog, maxValues4prepared,cache,stemmer,out);
	  
    
	  long start_time=System.currentTimeMillis();

	  if(dbType.contains(":")) {
		  tipoNotizia=dbType.substring(0,dbType.indexOf(":"));
	  }
	  else {
		  tipoNotizia=dbType;
	  }

      this.dbType=tipoNotizia;

      RecordInterface n=null;
      try {
    	  n=RecordFactory.buildRecord(0,null,tipoNotizia,0);
    	  if(dbType.contains(":") && n instanceof Mdb) ((Mdb)n).setImportTableName(dbType.substring(dbType.indexOf(":")+1));
      }
      catch(Exception e) {
    	  e.printStackTrace();
      }
      //String terms=n.getTerminators();
      if(dbType!=null && dbType.length()>0) {
        
        
        try {
          
          RecordReader bf=n.getRecordReader(dataFile);
          bf.setup(conn[0]);
          bf.setTipoNotizia(tipoNotizia);
          bf.setIdTipo(idTipo);
          bf.setParoleSpooler(paroleSpooler);
          
          chToIndex=bf.getChToIndex();
          
          
          /* R.T. 05/06/2006: ciclo di lettura spostato nel RecordReader per
           * facilitare l'uso di SAXParser per dati in formato XML
           */
          bf.parse(bf,this,out,this.outputErrorRecords);
          
          
          
          bf.destroy(conn[0]);
          
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      else {
        out.println("Tipo "+dbType+" non trovato nella tabella tipi_notizie.");
      }
      
      close();
      
      try {
		paroleSpooler.destroy();
	} catch (SQLException e) {
		e.printStackTrace();
	}

	if(n!=null) n.destroy();
	
    out.println("tempo totale minuti:"+(System.currentTimeMillis()-start_time)/(60000.0));
    return chToIndex;
  }


  public LoadData(Connection conn[],String catalog, boolean clearDatabase, PrintStream console, PrintStream outputRecordErrors) throws SQLException {
  	  this.conn=conn;
  	  this.catalog=catalog;
  	  this.clearDatabase=clearDatabase;
//  	  this.confDir=confDir;
  	  out=console;
  	  this.outputErrorRecords=outputRecordErrors;
  	  dbGateway=DbGateway.getInstance(conn[0].toString(),console);
  	  
  	  /**
  	   * TODO: hsqldb non consente inserimenti multipli 
  	   */
  	  if(conn[0].toString().contains("derby")) maxValues4prepared=1;
  	  
  	if(clearDatabase) {
  		createDB(conn[0]);
	}
  	  
  	  arTempLCP=new mySet[maxValues4prepared];
  	  
  	  for(int i=0;i<maxValues4prepared;i++) arTempLCP[i]=new mySet();
  	
  	  preparedNotizia = new PreparedStatement[conn.length];
  	  preparedTempLCP = new PreparedStatement[conn.length];
  	  
  	//maxValues4prepared

  	  String TempLCP="insert into je_"+catalog+"_temp_lcpn (id_notizia,id_parola,id_classe) values (?,?,?)";
  	  
  	  for(int i=0;i<maxValues4prepared-1;i++) {
  		TempLCP=TempLCP+",(?,?,?)";
  	  }
  	  
      try {
    	for(int i=0;i<conn.length;i++) {
    		preparedNotizia[i]=conn[i].prepareStatement("insert into je_"+catalog+"_notizie (id,bid,id_tipo,notizia) values (?,?,?,?)");
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