package org.jopac2.engine.metasearch;

import java.util.Map;
import java.util.Vector;

import org.jopac2.engine.metasearch.managers.AbstractManager;
import org.jopac2.engine.utils.SingleSearch;
import org.jopac2.utils.RecordItem;

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
	
	public static void executeMetaSearch(String message, SingleSearch aSearch) {
		AbstractManager aManager=getManager(aSearch,message); // TODO: e' ciascun manager che deve stabilire la sintassi della query
    	aSearch.setManager(aManager);
    	aManager.start();
	}
	
	public static void printMetaSearchResult(SingleSearch aSearch) {
		AbstractManager aManager=aSearch.getManager();
		if(!(aManager.isStopped())){
    		try {
    			System.out.println("___---=== Waiting for " + aManager.getName() + " for max " + aManager.getTimeOut() + " millis ===---___");
    			aManager.join(aManager.getTimeOut());
    			aManager.setStopped(true);
				if(aManager.getRunningTime() == 0){
					System.out.println("___---=== " + aManager.getName() + " timeout ===---___");
				}
				else
					System.out.println("___---=== Execution time for " + aManager.getName() + ": " + aManager.getRunningTime() + " ===---___");
			}
    		catch (InterruptedException e) {
    			System.out.println("----!!! Interrupted !!!----");
    		}
		}
		else{
			System.out.println("___---=== " + aManager.getName() + " has already notified the chief ===---___");
		}
		
		
		// Get results
		
//		SingleSearchResult ssr=new SingleSearchResult(aManager.getName(),!(aManager.isAlive()),aManager.getRunningTime(),aManager.getRecords());
		
		
		Vector<RecordItem> result=aManager.getRecords();
		
		for(int i=0;result!=null && i<result.size();i++) {
			System.out.println(result.elementAt(i).getData());
		}
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
