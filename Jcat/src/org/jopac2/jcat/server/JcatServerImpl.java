package org.jopac2.jcat.server;

import java.sql.Connection;
import java.util.Vector;

import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.jbal.RecordInterface;
import org.jopac2.jcat.client.JcatServer;
import org.jopac2.jcat.client.RecordInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class JcatServerImpl extends RemoteServiceServlet implements JcatServer {

	private static final long serialVersionUID = -3754367253317517340L;
	
	public RecordInfo getRecord(String jid) {
		RecordInfo r=new RecordInfo();
		r.setJid("-1");
		try {
			Connection conn=RecordFinderServlet.CreaConnessione();
			RecordInterface ma=DbGateway.getNotiziaByJID(conn, jid);
			if(ma!=null) {
				setInfo(ma,r);
				ma.destroy();
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	private void setInfo(RecordInterface ma, RecordInfo r) {
		String titolo=ma.getTitle();
		if(!titolo.contains("/")) {
			Vector<String> authors=ma.getAuthors();
			for(int i=0;authors!=null && i<authors.size();i++) titolo=titolo+"; "+authors.elementAt(i);
			titolo=titolo.replaceFirst("; ", " / ");
		}
		r.setTitolo(titolo);
		if(ma.getIsPartOf()!=null && ma.getIsPartOf().size()>0)
			r.setCollana(ma.getIsPartOf().elementAt(0).getTitle());
		r.setCollazione(ma.getDescription());
		r.setEdizione(ma.getEdition());
		if(ma.getEditors()!=null && ma.getEditors().size()>0)
			r.setEditore(ma.getEditors().elementAt(0));
		r.setNumerostandard(ma.getStandardNumber());
	
		r.setJid(Long.toHexString(ma.getJOpacID()));
	}

}
