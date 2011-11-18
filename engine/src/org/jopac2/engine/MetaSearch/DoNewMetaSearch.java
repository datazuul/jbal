package org.jopac2.engine.MetaSearch;

import java.util.Map;
import java.util.Vector;

import org.jopac2.engine.MetaSearch.Managers.AbstractManager;
import org.jopac2.engine.utils.SingleSearch;

public class DoNewMetaSearch {

	/**
	 * Lancia i thread di metaricerca.
	 * Restituisce un vettore di SingleSearch.
	 * @param message
	 * @param session
	 * @param objectModel
	 * @param serverList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Vector<SingleSearch> executeMetaSearch(String message, Map objectModel, String confPath, String contextDir) {
        

        return executeMetaSearch(message, confPath, contextDir);
	}
	
	/**
	 * Lancia i thread di metaricerca
	 * Restituisce un vettore di SingleSearch.
	 * @param message
	 * @param serverList
	 * @param contextDir
	 * @return
	 */
	public static Vector<SingleSearch> executeMetaSearch(String message, String serverList, String contextDir) {
		if(serverList==null) serverList="servers.lst";
		Vector<SingleSearch> ss=ServerListParser.parseServerList(serverList, contextDir);
        
        for(int i=0;i<ss.size();i++) {
        	SingleSearch aSearch = ss.elementAt(i);
	    	System.out.println("+++ Lancio " + aSearch.getHost());
	    	AbstractManager aManager=getManager(aSearch,message); // TODO: e' ciascun manager che deve stabilire la sintassi della query
	    	aSearch.setManager(aManager);
	    	aManager.start();
        }
        
		return ss;
	}
	
	
	
	/**
	 * System.out.println("-------------------------------------- Tutti i thread sono partiti --------------------------------------");
    	
    	it = threads.iterator();
    	while(it.hasNext()){
    		t = (AbstractManager)it.next();
    		if(!(t.isStopped())){
	    		try {
	    			System.out.println("___---=== Waiting for " + t.getName() + " for max " + t.getTimeOut() + " millis ===---___");
					t.join(t.getTimeOut());
					t.setStopped(true);
					if(t.getRunningTime() == 0){
						System.out.println("___---=== " + t.getName() + " timeout ===---___");
					}
					else
						System.out.println("___---=== Execution time for " + t.getName() + ": " + t.getRunningTime() + " ===---___");
				}
	    		catch (InterruptedException e) {
	    			System.out.println("----!!! Interrupted !!!----");
	    		}
    		}
    		else{
    			System.out.println("___---=== " + t.getName() + " has already notified the chief ===---___");
    		}
    	}
    	
   	
    	it = threads.iterator();
    	while(it.hasNext()){
    		t = (AbstractManager)it.next();
    		result.add(new SingleSearchResult(t.getName(),!(t.isAlive()),t.getRunningTime(),t.getResults()));
    	}
    	
    	return result;
	 */
	
	private static AbstractManager getManager(SingleSearch ss, String query) {
		AbstractManager req_man=null;
        try
        {
            req_man = (AbstractManager)ss.getConstructorClass().newInstance();
            req_man.setInfo(ss,query,ss.getSyntax(),ss.getTimeOut());
            req_man.setName(ss.getHost());
            req_man.setContextDir(ss.getContextDir());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return req_man;
	}
}
