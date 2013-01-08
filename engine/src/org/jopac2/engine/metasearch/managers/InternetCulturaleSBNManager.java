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
* @author	Albert Caramia
* @version	19/01/2005
* 
* @author	Romano Trampus
* @version  19/01/2005
* 
* @author	Romano Trampus
* @version	19/05/2005
*/
import java.io.*;
import java.util.*;
//import com.k_int.IR.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.jopac2.utils.RecordItem;
import org.jopac2.utils.Utils;


/*
 *implementazione di GetConstructor per JOpac2
 *si tratta di un elaboratore di stringhe
 */
public class InternetCulturaleSBNManager extends AbstractManager
{
	private Parser parser;
    private Hashtable<String,String> ChiaviCodici;
    private Vector<RecordItem> result = null;
	
    /* http://opac.internetculturale.it/cgi-bin/records.cgi?address=iccu
    *		&numentries=30
    *		&request=1%3D1003%20%204%3D2%20%22antonio%20trampus%22
    *		&type=full
    */

	public InternetCulturaleSBNManager()
	{
		ChiaviCodici = new Hashtable<String,String>();
        ChiaviCodici.put("AUT","1=1003  4=2"); //autore
        ChiaviCodici.put("INV","null"); //tutti i campi
        ChiaviCodici.put("SBJ","1=21  4=2"); //soggetto
        ChiaviCodici.put("TIT","1=4  4=2"); //titolo
        ChiaviCodici.put("CLL","1=4  4=2"); //collana  
        ChiaviCodici.put("ANY","1=1016  4=2"); //tutti i campi
        // (1=54   4=2  "art") @and@ ((1=4  4=2 "patria"))
        ChiaviCodici.put("LAN","1=54   4=2"); //lingua
        
        parser = new InternetCulturaleSBNParser();       
	}

    /**
     * 
     * @param q formato chiave=valore&chiave=valore
     * dove chiave=AUT, INV, etc
     * @return
     */
	private String construct(String q) //?
    {
        String queryrtl = Utils.removeAttribute(q, "stemmer"); 
       
       String[] parametri;
       String[] coppia = new String[2];
       parametri = queryrtl.split("&");
       
       String e="";
       
       for(int i=0;i<parametri.length;i++)
       {
           coppia = parametri[i].split("=");
           String a= ChiaviCodici.get(coppia[0])+"  %22"+coppia[1]+"%22";           
           e+="@and@"+"("+a+")";
       }
       
       e=e.substring(5);  //toglie primo and

       String NUM_RIS="30"; // numero risposte
       String req = "&numentries="+NUM_RIS+"&type=full&request=";

       // convertire valore in maiuscolo?
       
       req=req+e;
       req = req.replaceAll(" ", "+");       
       return req;
    }
    
    public void sendquery() throws IOException
    {
    	String req = ss.getPrefix() + construct(q);
    	
    	result = new Vector<RecordItem>();
    	System.out.println("rit: "+ss.getHost()+req);
        try{
                HttpConnection conn = new HttpConnection(ss.getHost(),ss.getPort());
                HttpState state = new HttpState();
                GetMethod get = new GetMethod(req);
                InputStream resp;
                
                try{
                    conn.open();
                    get.execute(state,conn);
                    resp = get.getResponseBodyAsStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(resp));

                    result.addAll(parser.parse(br,ss.getHost(),getContextDir(),ss.getDbname()));
                }
                catch(Exception e){e.printStackTrace();}

        }
        catch(Exception e){
            e.printStackTrace();
        }
        //return result;
    }

	@Override
	public void destroy() {
		
	}

	@Override
	public int getRecordCount() {
		return result.size();
	}

	@Override
	public Vector<RecordItem> getRecords() {
		return result;
	}

	public int getCurrentStatus() {
		// TODO Auto-generated method stub
		return 0;
	}
}    