package org.jopac2.engine.Managers;
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

import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jopac2.utils.RecordItem;
//import com.k_int.IR.*;

//import JOpac2.utils.*;

/*
 *implementazione di GetConstructor per JOpac2
 *si tratta di un elaboratore di stringhe
 */
public class EasyWebGetManager extends AbstractManager
{
	private Parser parser;
    private Hashtable<String,String> ChiaviCodici;
	

	public EasyWebGetManager()
	{
		ChiaviCodici = new Hashtable<String,String>();
        ChiaviCodici.put("AUT","AU"); //autore
        ChiaviCodici.put("INV","KW"); //tutti i campi
        ChiaviCodici.put("SBJ","SO"); //soggetto
        ChiaviCodici.put("TIT","TI"); //titolo
        ChiaviCodici.put("CLL","CO"); //collana  
        ChiaviCodici.put("ALL","KW"); //tutti i campi
        parser = new EasyWebParser();       
	}

    /**
     * 
     * @param q formato chiave=valore&chiave=valore
     * dove chiave=AUT, INV, etc
     * @return
     */
	private String construct(String q) //?
    {
        String queryrtl = q; //(new QueryUtil()).changeQuery(q);
		//String queryrtl="AUT=BONI&";
		//queryrtl+="TIT=VIE E PIAZZE&";
		//System.out.println("SCOMMENTARE PER TEST!!!!");
       
       String[] parametri;
       String[] coppia = new String[2];
       parametri = queryrtl.split("&");
       
       Hashtable<String,String> hashquery = new Hashtable<String,String>();
       
       for(int i=0;i<parametri.length;i++)
       {
           coppia = parametri[i].split("=");
           hashquery.put(ChiaviCodici.get(coppia[0]), coppia[1]);
       }
       //hash con AU,autore     
       
       Enumeration<String> keys = null;
       
       String chiave="";
       String valore="";

       int index=0;
       /*
http://212.131.152.85/cgi-bin/Easyweb/ewgettest?
EW_HIL=trev/ew_menu.html&
EW_HFL=trev/ew_copy.html&
EW_D=TREV&
EW4_DLL=10&
EW4_DLP=10&
EW4_NVR=&
EW4_NVT=&
EW4_NMI=&
EW_RM=10&

EW4_PY=(AU=PIPPO)&
*EW4_NEX=1&
*EW_P=TAG&
*EW_T=R&
EW=(AU=PIPPO)&
        */
       String NUM_RIS="0"; //validi 0,10,100 : numero di risultati
       String req = "EW_HIL=trev/ew_menu.html&EW_HFL=trev/ew_copy.html&EW_D=TREV&EW4_DLL=10&EW4_DLP=10&EW4_NVR=&EW4_NVT=&EW4_NMI=&EW_RM="+NUM_RIS+"&EW4_NEX=1&EW_P=TAG&EW_T=R&";
       keys = hashquery.keys();
       // convertire valore in maiuscolo?
       String e="";
       while(keys.hasMoreElements())
       {
           index++;
           chiave = (String)keys.nextElement();
           valore = (String)hashquery.get(chiave);
           String a= chiave+"="+valore;
           a = a.replaceAll(" ", "_AND_"+chiave+"=");
           
           e+="_AND_"+"("+a+")";
       }
       e=e.substring(5);  //toglie primo and
       
       req = req + "EW4_PY="+e+"&";
       req = req + "EW="+e+"&";
       //req = req.substring(0,req.length()-1); // non serve
       req = req.replaceAll(" ", "+");       
       return req;
    }
    
    public void sendquery() throws IOException
    {
    	String req = ss.getPrefix() + construct(q);
    	Vector<RecordItem> result = new Vector<RecordItem>();
    	System.out.println("rit: http://"+ss.getHost()+req);
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