package org.jopac2.jbal.Readers;

import java.sql.SQLException;

public interface LoadDataInterface {

	void process(byte[] linea, 
			ParoleSpoolerInterface paroleSpooler) throws SQLException, Exception; //String tipoNotizia, long idTipo,
//	void process(String linea,
//			ParoleSpoolerInterface paroleSpooler) throws SQLException, Exception; //String tipoNotizia, long idTipo,
}
