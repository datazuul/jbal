package org.jopac2.engine.MetaSearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

import org.jopac2.engine.utils.SingleSearch;



public class ServerListParser {
	public static Vector<SingleSearch> parseServerList(String serverList, String contextDir) {
		Vector<SingleSearch> searches = new Vector<SingleSearch>();
    
        try
        {
        	InputStream is = new FileInputStream(new File(serverList));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String read = br.readLine();
            while(read != null)
            {
            	read=read.trim();
            	if((read.length()>0)&&!read.startsWith("#")) {
	                StringTokenizer st = new StringTokenizer(read,",");
	                String h = new String(st.nextToken());
	                int p = Integer.parseInt(st.nextToken());
	                String pr = new String(st.nextToken());
	                String classe=st.nextToken();
					String sn = new String(st.nextToken());
					String user = new String(st.nextToken());
					String password = new String(st.nextToken());
					String authentication_type = new String(st.nextToken());
					long to = Long.parseLong(st.nextToken());
					String databasecode=null;
					if(pr.indexOf(":")>0) {
						databasecode=pr.substring(pr.indexOf(":")+1);
						pr=pr.substring(0,pr.indexOf(":"));
					}
					String campi="";
					while(st.hasMoreTokens()) {
						campi+=","+st.nextToken();
					}
					if(campi.length()>0) campi=campi.substring(1);
	                SingleSearch ss = new SingleSearch(h, p, pr, classe, sn, user, password, authentication_type, to, contextDir, databasecode,campi);
	                searches.add(ss);
            	}
                read = br.readLine();
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return searches;
	}
}
