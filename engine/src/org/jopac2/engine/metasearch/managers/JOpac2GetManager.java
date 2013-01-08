package org.jopac2.engine.metasearch.managers;
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

/**
* @author	Iztok Cergol
* @version	19/08/2004
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.jopac2.utils.RecordItem;


/*
 *implementazione di GetConstructor per JOpac2
 *si tratta di un elaboratore di stringhe
 */
public class JOpac2GetManager extends AbstractManager
{
	Parser p;
//	private Hashtable hashquery;
//    private Hashtable<String,String> ChiaviCodici;
	
	
	public JOpac2GetManager()
	{
//		ChiaviCodici = DbGateway.getValori();
        p = new JOpac2Parser();
	}

    private String construct(String q, String campi)
    {
       String queryrtl = q;
       
       String[] parametri;
       String[] coppia = new String[2];
       parametri = queryrtl.split("&");
       
       int index=0;
       String req = "";
       
       for(int i=0;i<parametri.length;i++)
       {
           coppia = parametri[i].split("=");
           if(!campi.contains(coppia[0])) coppia[0]="ANY";
           req = req + "C" + index + "=" + coppia[0] + "&stringaRicerca" + index + "=" + coppia[1] + "&O" + index + "=%26&";
           index++;
       }
       
       req = req.replaceAll(" ", "+");
       return req;
    }
    
    public void sendquery() throws IOException
    {
    	String req = ss.getPrefix() + construct(q,ss.getCampi());
    	System.out.println("isoSeatch: "+ss.getHost()+":"+ss.getPort()+req);
    	Vector<RecordItem> result = new Vector<RecordItem>();
  
                //HttpConnection conn = new HttpConnection((String)addressVector.get(reqindex),Integer.parseInt((String)portaVector.get(reqindex)));
                HttpConnection conn = new HttpConnection(ss.getHost(),ss.getPort());
                HttpState state = new HttpState();
                GetMethod get = new GetMethod(req);
                
                InputStream resp;
                //HttpConnectionParams httpparam = new HttpConnectionParams();
            	//conn.setHttpConnectionManager(new SimpleHttpConnectionManager());
                conn.open();
                get.execute(state,conn);
                resp = get.getResponseBodyAsStream();
                //String risposta = new String(resp);

                //
                //risposta = elaboraResult(risposta);
                //risposta = (new EncodeDecode()).encode(risposta);
                result.addAll(p.parse(new BufferedReader(new InputStreamReader(resp)),ss.getHost(),getContextDir(),ss.getDbname()));
                conn.close();

                
                //result.add(risposta);
            
           
        //return result;
    }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRecordCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector<RecordItem> getRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCurrentStatus() {
		// TODO Auto-generated method stub
		return 0;
	}
    
    
}
