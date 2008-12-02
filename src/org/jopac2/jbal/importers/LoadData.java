package org.jopac2.jbal.importers;

import java.sql.SQLException;


public interface LoadData {

	public void process(String stringa,String tipo,long idTipo, ParoleSpooler paroleSpooler) throws SQLException;

}
