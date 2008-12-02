/*
 * Created on 16-dic-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jopac2.utils;
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
* @version	??/??/2002
* 
* @author	Romano Trampus
* @version	??/??/2002
* 
* @author	Romano Trampus
* @version	16/12/2004
*/
/**
 * @author romano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RecordItem {
	private String record,hostname,type,syntax,database,databasecode,id;
	
	public RecordItem(String host,String database, String data,String dataType,
			String dataSyntax, String databasecode, String id) {
		hostname=host;
		record=data;
		type=dataType;
		syntax=dataSyntax;
		this.database=database;
		this.databasecode=databasecode;
		this.id=id;
	}
	
	public RecordItem() {
		hostname=null;
		record=null;
		type=null;
		syntax=null;
		database=null;
		databasecode=null;
		id=null;
	}
	
	public void setHost(String host) {hostname=host;}
	public void setId(String id) {this.id=id;}
	public void setData(String data) {record=data;}
	public void setDataType(String dataType) {type=dataType;}
	public void setDataSyntax(String dataSyntax) {syntax=dataSyntax;}
	/**
	 * @param database The database to set.
	 */
	public void setDatabase(String database) {
		this.database = database;
	}
	
	/**
	 * @return Returns the database.
	 */
	public String getDatabase() {
		return database;
	}
	
	public String getData() {return record;}
	public String getHost() {return hostname;}
	public String getDataType() {return type;}
	public String getDataSyntax() {return syntax;}
	public String getId() {return id;}

	public String getDatabaseCode() {
		return databasecode;
	}
	
	public void setDatabaseCode(String databasecode) {
		this.databasecode = databasecode;
	}

}
