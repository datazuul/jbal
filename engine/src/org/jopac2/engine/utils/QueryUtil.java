/*
 * Created on 29-nov-2004
 */
package org.jopac2.engine.utils;
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
* @version	29/11/2004
* 
*/
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.jopac2.utils.Parametro;


/*import com.k_int.IR.IRQuery;
import com.k_int.IR.QueryModels.InternalQueryRep.AttrPlusTermNode;
import com.k_int.IR.QueryModels.InternalQueryRep.ComplexNode;
import com.k_int.IR.QueryModels.InternalQueryRep.RootNode;
*/

/**
 * @author Iztok
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class QueryUtil {
	
	  private Hashtable<String,String> Type1_2_rtl = new Hashtable<String,String>();
	  private Hashtable<String,String> operatori = new Hashtable<String,String>();
	  private Hashtable<String,String> ChiaviCodici = new Hashtable<String,String>();
	  
	
	public QueryUtil()
	{
		Type1_2_rtl.put("4","TIT");
	    Type1_2_rtl.put("5","TIT");
	    Type1_2_rtl.put("6","TIT");
	    Type1_2_rtl.put("7","ISN");
	    Type1_2_rtl.put("8","ISN");
	    Type1_2_rtl.put("12","INV");
	    Type1_2_rtl.put("21","SBJ");
	    Type1_2_rtl.put("22","SBJ");
	    Type1_2_rtl.put("34","CLL");
	    Type1_2_rtl.put("1003","AUT");
	    Type1_2_rtl.put("1004","AUT");
	    Type1_2_rtl.put("1005","AUT");
	    Type1_2_rtl.put("1006","AUT");
	    Type1_2_rtl.put("1016","ANY");
	    
	    operatori.put("1","&");
	    operatori.put("2","|");
	    operatori.put("3","!");
	    operatori.put("4","@");
	    
	    
	    ChiaviCodici.put("AUT","1"); // o 1003 ?
	    ChiaviCodici.put("TIT","4");
	    ChiaviCodici.put("NUM","7"); //ma anche 8 (ISSN)
	    ChiaviCodici.put("LAN","1183"); //lingua
	    ChiaviCodici.put("MAT","1031"); //material-type
	    ChiaviCodici.put("DTE","30"); //date - forse anche 31 (date of publication), 32 (date of acquisition)
	    ChiaviCodici.put("SBJ","21"); //anche 22?
	    ChiaviCodici.put("BIB","5");
	    ChiaviCodici.put("INV","12");
	    ChiaviCodici.put("CLL","5");
	    ChiaviCodici.put("ANY","1016");
	    //ChiaviCodici.put("autore","1003");
	}
	
	public String costruisciQuery(Vector<Parametro> param)
	  {
	      Hashtable<String,String> ParamTable = new Hashtable<String,String>();
	      for(int i=0;i<param.size();i++)
	      {
		 String chiave = (String)((Parametro)param.elementAt(i)).getChiave();
	         String valore = (String)((Parametro)param.elementAt(i)).getValore();
	         ParamTable.put(chiave,ParamTable.get(chiave)==null?valore:ParamTable.get(chiave)+" "+valore);
	      }

	      //System.out.println(ParamTable.toString());
	  
	      String and = "";
	      String or = "";
	      Enumeration<String> Enum = ParamTable.keys();
	      
	      for(int i=0;i<ParamTable.size();i++)
	      {
	        String chiave_attuale = (String) Enum.nextElement();
	        //System.out.println("chiave attuale: "+chiave_attuale+"\n");
	        
	        if(chiave_attuale.equals("NUM"))
	        {    
	            or=or+" @or @attr 1=7 \""+ParamTable.get(chiave_attuale)+"\" @attr 1=8 \""+ParamTable.get(chiave_attuale)+"\"";
	        }
	        else
	        {
	            if(ParamTable.size()>1)and=and+" @and";
	            and=and+" @attr 1="+ChiaviCodici.get(chiave_attuale)+" \""+ParamTable.get(chiave_attuale)+"\"";
	            
	        }
	        
	      }
	      int lastAnd = and.lastIndexOf("@and");
	      if(or.equals("")&&lastAnd>3)
	      {
	          
	 	  and = and.substring(0,lastAnd)+and.substring(lastAnd+5);
	          
	      }
	      String query = "@attrset bib-1" + and + or;
	      return query;
	  }
}
