package org.jopac2.jbal.importers.Readers;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;

import org.jopac2.jbal.dbGateway.LoadData;
import org.jopac2.jbal.dbGateway.ParoleSpooler;

public abstract  class RecordReader extends BufferedReader {
	protected String tipoNotizia;
	protected long idTipo;
	protected ParoleSpooler paroleSpooler;
	
	public RecordReader(Reader in) {
		super(in);
	}
	
	public RecordReader(InputStream in, String charsetEncoding) throws UnsupportedEncodingException {
		super(new BufferedReader(new InputStreamReader(in,charsetEncoding)));
	}
	
	public RecordReader(InputStream in) throws UnsupportedEncodingException {
		super(new BufferedReader(new InputStreamReader(in,"UTF-8")));
	}
	
	public abstract String readRecord() throws IOException;
	
	
	public void setTipoNotizia(String tipoNotizia) {
		this.tipoNotizia=tipoNotizia;
	}
	
	public void setIdTipo(long idTipo) {
		this.idTipo=idTipo;
	}
	
	public void setParoleSpooler(ParoleSpooler paroleSpooler) {
		this.paroleSpooler=paroleSpooler;
	}
	
	public void setup(Connection conn) {}
	public void destroy(Connection conn) {}
	
	public void parse(BufferedReader f, LoadData data) throws IOException {
		String linea="";
		long start_time=System.currentTimeMillis();
		long i=0;
		double t;
		
        while(this.ready()&&linea!=null) {
            linea=this.readRecord();
            try {
				data.process(linea,tipoNotizia,idTipo,paroleSpooler);
			} catch (SQLException e) {
				e.printStackTrace();
			}
            i++;
            
            if((i%1000)==0) {
              t=(System.currentTimeMillis()-start_time)/(60000.0);
              System.out.println(i+" record in "+ t +" minuti (" + i/t + " R/m)");
              //System.gc();
            }
          }
          System.gc();
		
	}
}
