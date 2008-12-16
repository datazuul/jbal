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

/*
* @author	Iztok Cergol
* @version  18/08/2004
* 
* @author	Romano Trampus
* @version  19/05/2005
*/

/*
 * SearchResultSet.java
 *
 * Created on 18 agosto 2004, 20.59
 * Any search has to return:
 *  the starting query
 *  the optimized query
 *  the query cardinality
 *  a Vector with all the cardinality for each search item
 *  a Vector with the record ids matching the query
 *
 * All that are generate in the doSearch class. SearchResultSet collect that informations.
 */

package org.jopac2.engine.utils;

import java.util.Vector;

import org.jopac2.engine.NewSearch.NewItemCardinality;

//import org.xml.sax.*;

/**
 *
 * @author  romano
 */
public class SearchResultSet {
    
    private String query,optimizedQuery;
    private Vector<NewItemCardinality> itemCard=null;
    private Vector<Long> recordIDs=null;
    private long queryCount;
    private double queryTime;
    
    public void setQuery(String query) {this.query=query;}
    public void setOptimizedQuery(String optimizedQuery) {this.optimizedQuery=optimizedQuery;}
    public void setItemCardinalities(Vector<NewItemCardinality> itemCardinalities) {itemCard=itemCardinalities;}
    public void setRecordIDs(Vector<Long> recordIDs) {this.recordIDs=recordIDs;}
    public void setQueryCount(long queryCount) {this.queryCount=queryCount;}
    public void setQueryTime(double queryTime) {this.queryTime=queryTime;}
    
    public String getQuery() {return query;}
    public String getOptimizedQuery() {return optimizedQuery;}
    public Vector<NewItemCardinality> getItemCardinalities() {return itemCard;}
    public Vector<Long> getRecordIDs() {return recordIDs;}
    public long getQueryCount() {return queryCount;}
    public double getQueryTime() {return queryTime;}
}
