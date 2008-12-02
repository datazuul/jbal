package org.jopac2.jbal.dbGateway;

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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;

import org.jopac2.jbal.stemmer.Radice;
import org.jopac2.jbal.stemmer.StemmerItv2;
import org.jopac2.utils.Utils;

import com.whirlycott.cache.Cache;
import com.whirlycott.cache.CacheConfiguration;
import com.whirlycott.cache.CacheException;
import com.whirlycott.cache.CacheManager;

public class ParoleSpooler implements org.jopac2.jbal.importers.ParoleSpooler {
	private PreparedStatement preparedParole[];
	private int nvalues=100;
	private String prepared="insert into anagrafe_parole (id,parola,stemma) values (?,?,?)";
	private int currentStmt=0, currentValue=0;
	private Hashtable<String,Long> buffer;
	private Connection c;
	private int block=0;
	private Cache cache;
	private String selectID="select id from anagrafe_parole where parola = (?)";
	private PreparedStatement preparedSelectID;
	private Radice stemmer=null;

	public ParoleSpooler(Connection[] conn, int nvalues) {
		super();
		this.nvalues=nvalues;
		
		preparedParole = new PreparedStatement[conn.length];
		for(int i=0;i<nvalues-1;i++) {
			prepared+=",(?,?,?)";
		}
		try {
			for(int i=0;i<conn.length;i++) {
				preparedParole[i]=conn[i].prepareStatement(prepared);
			}
			c=conn[0];
			preparedSelectID=c.prepareStatement(selectID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		buffer=new Hashtable<String,Long>();

		
		
//		Use the cache manager to create the default cache
		try {
			CacheConfiguration cacheConf=new CacheConfiguration();
			cacheConf.setTunerSleepTime(10);
			cacheConf.setBackend("com.whirlycott.cache.impl.ConcurrentHashMapImpl");
			cacheConf.setMaxSize(100000);
			cacheConf.setPolicy("com.whirlycott.cache.policy.LFUMaintenancePolicy");
			cacheConf.setName("JOpac2cache");
			cache= CacheManager.getInstance().createCache(cacheConf);
			//cache = CacheManager.getInstance().getCache();
			
		} catch (CacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		stemmer=new StemmerItv2();
	}
	
	private void insertCache(String parola,long id) {
//		Put an object into the cache
		cache.store(Utils.removeAccents(parola), new Long(id));
	}
	
	public void insertParola(long id, String parola) {
		parola=Utils.removeAccents(parola);
		buffer.put(parola,new Long(id));
		insertCache(parola,id);
		block++;
	  	try {
			preparedParole[currentStmt].setLong(currentValue*3+1,id);
			preparedParole[currentStmt].setString(currentValue*3+2,parola);
			preparedParole[currentStmt].setString(currentValue*3+3,stemmer.radice(parola));
		  	currentValue++;
		  	currentValue%=nvalues;
		  	
			if(currentValue==0) {
				preparedParole[currentStmt].execute();
				preparedParole[currentStmt].clearParameters();
				currentStmt=(++currentStmt)%preparedParole.length; 
				if(currentStmt==0&&block>1000) {
					buffer.clear();
					buffer=null;
					System.gc();
					buffer=new Hashtable<String,Long>();
					block=0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	public void flush() {
		try {
			preparedParole[currentStmt].execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public long getParola(String parola) {
		parola=Utils.removeAccents(parola);
		Long r=null;
		
//		Get the object back out of the cache
		r = (Long) cache.retrieve(parola);	
		
		if(r==null) {
			r=(Long)buffer.get(parola);
		}
		if(r==null) {
			
//			Statement stmt;
			try {
				preparedSelectID.setString(1,parola);
				ResultSet rs=preparedSelectID.executeQuery();
//				stmt = c.createStatement();
//				ResultSet rs = stmt.executeQuery("select id from anagrafe_parole where parola = '"+parola+"'");

				if(rs.next()) { // c'e' un record
					r=rs.getLong(1);
					cache.store(parola, r);
				}
				rs.close();
//				stmt.close();
				rs=null; //stmt=null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return r==null?-1:r.longValue();
	}
	
	public void destroy() throws SQLException {
		if(currentValue!=0) {
			Enumeration<String> e=buffer.keys();
			while(e.hasMoreElements()) {
				String p=e.nextElement();
				Long id=(Long)buffer.get(p);
				boolean is=false;
				
				Statement stmt=c.createStatement();

	            ResultSet r=stmt.executeQuery("select id from anagrafe_parole "+
	                "where (parola='"+p+"')");
	            if(r.next()) is=true;
	            r.close();
	            stmt.close();
				
				if(!is) DbGateway.InsertParola(c, p, stemmer.radice(p), id);
			}
			
			buffer.clear();
			buffer=null;
			System.gc();
		}
//		Shut down the cache manager
		try {
			CacheManager.getInstance().destroy("JOpac2cache");
			CacheManager.getInstance().shutdown();
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Returns the currentValue.
	 */
	public int getCurrentValue() {
		return currentValue;
	}
}