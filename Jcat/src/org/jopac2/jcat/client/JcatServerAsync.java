package org.jopac2.jcat.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface JcatServerAsync {
	public void getRecord(String jid, AsyncCallback<Object> callback);
}
