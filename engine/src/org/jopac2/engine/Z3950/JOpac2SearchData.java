package org.jopac2.engine.Z3950;

import com.k_int.IR.SearchTask;
import com.k_int.IR.Searchable;

public class JOpac2SearchData {
	private SearchTask st;
	private String host;
	private String prefix;
	private String dataType;
	private String querySyntax;
	private String databasecode;
	private Searchable origin;


	public JOpac2SearchData(SearchTask st, Searchable origin, String host, String prefix, String dataType, String querySyntax, String databasecode) {
		this.databasecode=databasecode;
		this.dataType=dataType;
		this.host=host;
		this.prefix=prefix;
		this.querySyntax=querySyntax;
		this.st=st;
		this.origin=origin;
	}
	
	public void destroy() {
		st.setTaskStatusCode(SearchTask.TASK_COMPLETE);
		st.destroyTask();
		origin.destroy();
	}


	/**
	 * @return the databasecode
	 */
	public String getDatabasecode() {
		return databasecode;
	}


	/**
	 * @param databasecode the databasecode to set
	 */
	public void setDatabasecode(String databasecode) {
		this.databasecode = databasecode;
	}


	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}


	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}


	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}


	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}


	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}


	/**
	 * @return the querySyntax
	 */
	public String getQuerySyntax() {
		return querySyntax;
	}


	/**
	 * @param querySyntax the querySyntax to set
	 */
	public void setQuerySyntax(String querySyntax) {
		this.querySyntax = querySyntax;
	}


	/**
	 * @return the st
	 */
	public SearchTask getSearchTask() {
		return st;
	}


	/**
	 * @param st the st to set
	 */
	public void setSearchTask(SearchTask st) {
		this.st = st;
	}
}
