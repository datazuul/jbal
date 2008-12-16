package org.jopac2.jbal.Readers;

import java.sql.SQLException;

public interface LoadDataInterface {

	void process(String linea, String tipoNotizia, long idTipo,
			ParoleSpoolerInterface paroleSpooler) throws SQLException;

}
