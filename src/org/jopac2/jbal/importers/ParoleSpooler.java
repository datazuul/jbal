package org.jopac2.jbal.importers;

import java.sql.SQLException;

public interface ParoleSpooler {

	void destroy() throws SQLException;

	long getParola(String parola);

	void insertParola(long id_ap_f, String parola);

}
