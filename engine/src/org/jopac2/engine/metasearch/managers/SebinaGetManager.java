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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
//import com.k_int.IR.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jopac2.utils.RecordItem;



/*
 * implementazione di GetConstructor per JOpac2 si tratta di un elaboratore di
 * stringhe
 */
public class SebinaGetManager extends AbstractManager
{
	private Parser parser;

	private Hashtable<String,String> ChiaviCodici;

	public SebinaGetManager() {
		ChiaviCodici = new Hashtable<String,String>();
		ChiaviCodici.put("AUT", "autore"); //autore
		ChiaviCodici.put("INV", "**"); //tutti i campi
		ChiaviCodici.put("SBJ", "soggetto"); //soggetto
		ChiaviCodici.put("TIT", "titolo"); //titolo
		ChiaviCodici.put("ANY", "**"); //any
		ChiaviCodici.put("CLL", "**"); //collana
		ChiaviCodici.put("ALL", "**"); //tutti i campi
		parser = new SebinaParser();
	}

	/**
	 * 
	 * @param q
	 *            formato chiave=valore&chiave=valore dove chiave=AUT, INV, etc
	 * @return
	 */
	private Hashtable<String,String> construct(String q) //?
	{
		String queryrtl = q; //(new QueryUtil()).changeQuery(q);
//		System.out.println("SCOMMENTARE PER TEST!!!!");
//		String queryrtl = "AUT=trampus&";
		
		String[] parametri;
		String[] coppia = new String[2];
		parametri = queryrtl.split("&");

		Hashtable<String,String> hashquery = new Hashtable<String,String>();

		for (int i = 0; i < parametri.length; i++) {
			coppia = parametri[i].split("=");
			if(hashquery.get(ChiaviCodici.get(coppia[0]))!=null)
				hashquery.put(ChiaviCodici.get(coppia[0]), hashquery.get(ChiaviCodici.get(coppia[0]))+" "+coppia[1]);
			else 
				hashquery.put(ChiaviCodici.get(coppia[0]), coppia[1]);
		}
		Enumeration<String> keys = null;
		String chiave = "";
		String valore = "";

		int index = 0;

		keys = hashquery.keys();
		// convertire valore in maiuscolo?
		Hashtable<String,String> params=new Hashtable<String,String>();
		// inizializza i valori sempre presenti
		params.put("autore","");
		params.put("titolo","");
		params.put("soggetto","");
		params.put("**","");
		while (keys.hasMoreElements()) {
			index++;
			chiave = (String) keys.nextElement();
			valore = (String) hashquery.get(chiave);
			if (chiave.equalsIgnoreCase("autore"))
				params.put(chiave,params.get(chiave)+valore);
			if (chiave.equalsIgnoreCase("titolo"))
				params.put(chiave,params.get(chiave)+valore);
			if (chiave.equalsIgnoreCase("soggetto"))
				params.put(chiave,params.get(chiave)+valore);
			if (chiave.equalsIgnoreCase("**"))
				params.put(chiave,params.get(chiave)+valore);
		}
		return params;
	}

	private InputStream PostReqSEBINA(String url, Hashtable<String,String> params) {
		try {			
			String autore="";
			String titolo="";
			String soggetto="";
			String libera="";
			
			autore=(String)params.get("autore");
			titolo=(String)params.get("titolo");
			soggetto=(String)params.get("soggetto");			
			libera=(String)params.get("**");
			
			List<NameValuePair> form_data = new ArrayList<NameValuePair>(13);
			
			form_data.add(new BasicNameValuePair("lingua.x", "ita;vdir.x=;dbg.x=0000"));
			form_data.add(new BasicNameValuePair("xform.x", "seb_qbe"));
			form_data.add(new BasicNameValuePair("BIBEA", autore));
			form_data.add(new BasicNameValuePair("XTITS|BIBTT", titolo));
			form_data.add(new BasicNameValuePair("XSOGG|XDESD", soggetto));
			form_data.add(new BasicNameValuePair("XDDW|XCDW", ""));
			form_data.add(new BasicNameValuePair("libera.x", libera));
			form_data.add(new BasicNameValuePair("|\"XCATM", ""));
			form_data.add(new BasicNameValuePair("gruppo.x", ""));
			form_data.add(new BasicNameValuePair("XNADET", ""));
			form_data.add(new BasicNameValuePair("|\"XBIBC", ""));
			form_data.add(new BasicNameValuePair("submit.x", "31"));
			form_data.add(new BasicNameValuePair("submit.y", "13"));
			
			// Create an instance of HttpClient.
            HttpClient conn = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(form_data));

            InputStream resp=null;
		                
            try{
            	HttpResponse response = conn.execute(httppost);
            	HttpEntity entity = response.getEntity();
                resp = entity.getContent();
            }
            catch(Exception e){e.printStackTrace();}

			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void sendquery() throws IOException {
		Hashtable<String,String> params = construct(q);
		Vector<RecordItem> result = new Vector<RecordItem>();
		String url=ss.getHost()+":"+ss.getPort()+ss.getPrefix();
		//req="http://opac.units.it/h3/h3.exe/ase/Fanalisi?lingua.x=ita;vdir.x=;dbg.x=0000";
		InputStream risposta=PostReqSEBINA(url,params);
		result.addAll(parser.parse(new BufferedReader(new InputStreamReader(risposta)), ss.getHost(), 
				getContextDir(),ss.getDbname()));
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
