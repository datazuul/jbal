package org.jopac2.jbal.Readers;
/*******************************************************************************
*
*  JOpac2 (C) 2002-2011 JOpac2 project
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;

public abstract  class RecordReader {
	protected String tipoNotizia;
	protected long idTipo;
	protected ParoleSpoolerInterface paroleSpooler;
	protected String[] chToIndex=null;
	protected char[] charRecord=null;
	protected InputStream inputStream=null;
	protected String inputCharset=null;
	protected byte[] buf=new byte[8192];
	protected int bufLength=0;
	protected OutputStream errorStream=null;
    
    /** If the next character is a line feed, skip it */
    private boolean skipLF = false;
	
	
	public RecordReader(InputStream in, String charsetEncoding) throws UnsupportedEncodingException {
		inputStream=in;
		inputCharset=charsetEncoding;
	}
	
	public RecordReader(InputStream in) throws UnsupportedEncodingException {
		inputStream=in;
		inputCharset="UTF-8";
	}
	
	public abstract byte[] readRecord() throws IOException;
	
	
	public void setTipoNotizia(String tipoNotizia) {
		this.tipoNotizia=tipoNotizia;
	}
	
	public void setIdTipo(long idTipo) {
		this.idTipo=idTipo;
	}
	
	public void setParoleSpooler(ParoleSpoolerInterface paroleSpooler) {
		this.paroleSpooler=paroleSpooler;
	}
	
	public void setup(Connection conn) {}
	public void destroy(Connection conn) {}
	
	public void parse(RecordReader f, LoadDataInterface data, PrintStream console) throws IOException {
		parse(f, data, console, console);
	}
	
	public void parse(RecordReader f, LoadDataInterface data, PrintStream console, PrintStream outputRecordErrors) throws IOException {
		byte[] linea=new byte[1];
		long start_time=System.currentTimeMillis();
		long i=0;
		double t;
		
        while(ready()&&linea!=null) {
            linea=this.readRecord();

			try {
				data.process(linea,paroleSpooler); //tipoNotizia,idTipo,
			} catch (Exception e) {
				e.printStackTrace(console);
				if(outputRecordErrors!=null) outputRecordErrors.println(linea);
			}

            i++;
            
            if((i%1000)==0) {
              t=(System.currentTimeMillis()-start_time)/(60000.0);
              console.println("Loading: "+i+" record in "+ t +" minuti (" + i/t + " R/m)");
              //System.gc();
            }
          }
          System.gc();
		
	}
	

	public boolean ready() {
		try {
			return inputStream.available()!=0?true:false;
		} catch (IOException e) {
			return false;
		}
	}

	public void setChToIndex(String[] chToIndex) {
		this.chToIndex = chToIndex;
	}

	public String[] getChToIndex() {
		return chToIndex;
	}

	public void close() {
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String readLine() {
		try {
			while(ready() && buf[bufLength-1]!=10) {
				buf[bufLength++]=(byte)inputStream.read();
			}
		}
		catch(Exception e) {e.printStackTrace();}
		String r=new String(buf,0,bufLength);
		bufLength=0;
		return r;
	}
}
