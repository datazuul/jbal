package org.jopac2.jbal.Readers;

import java.sql.SQLException;

public interface ParoleSpoolerInterface {

	long getParola(String parola);

	void insertParola(long id_ap_f, String parola);

	void destroy() throws SQLException;

}
