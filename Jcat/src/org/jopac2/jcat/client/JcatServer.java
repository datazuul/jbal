package org.jopac2.jcat.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("jcat")
public interface JcatServer extends RemoteService {

	public static class Util {

		public static JcatServerAsync getInstance() {

			return GWT.create(JcatServer.class);
		}
	}

}
