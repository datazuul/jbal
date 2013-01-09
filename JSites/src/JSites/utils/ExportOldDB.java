package JSites.utils;

/*******************************************************************************
*
*  JOpac2 (C) 2002-2009 JOpac2 project
*
*     This file is part of JOpac2. http://www.jopac2.org
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
*  Please, see NOTICE.txt AND LEGAL directory for more info. Different licences
*  may apply for components included in JOpac2.
*
*******************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.*;

import JSites.utils.site.Orario;
import JSites.utils.site.Section;


public class ExportOldDB {

	protected static final String MySQLclassDriver = "com.mysql.jdbc.Driver";
	protected static final String MDBclassDriver = "sun.jdbc.odbc.JdbcOdbcDriver";

	protected static final String MySQLdbURL = "jdbc:mysql://localhost/";
	protected static final String MDBdbURL = "jdbc:odbc:Driver={MicroSoft Access Driver (*.mdb)};DBQ=C:/Reddot_Design_Notes.mdb";

	protected static final String dbName = "dbsito";

	protected static final String dbUser = "root";

	protected static final String dbPassword = "";

	private Connection MySQLconn = null;
	private Connection MDBconn = null;
	
	int cidcount = 39;
	int pagecount = 15;
	String basedir = "e:\\java_source\\prova2\\components\\";

	public ExportOldDB() {
	    
        try {
            Class.forName(MDBclassDriver);
            MDBconn = DriverManager.getConnection("jdbc:odbc:Driver={MicroSoft Access Driver (*.mdb)};DBQ=C:\\Documents and Settings\\Iztok\\Desktop\\Biblioteche.mdb","","");
            
            Class.forName(MySQLclassDriver);
            MySQLconn = DriverManager.getConnection(MySQLdbURL + dbName, dbUser,dbPassword);
            
            Statement MDBst = MDBconn.createStatement();
            ResultSet MDBrs = MDBst.executeQuery("select * from tblBiblioteca where NOT (IDSBN = 'nul')");
            PreparedStatement ps = MySQLconn.prepareStatement("insert into tblpagine (ID, Name, ParentID, Valid, HasChild, Codice) values (?,?, null, 1 , 0, ?)");
            while(MDBrs.next()){
            	
            	String nome = MDBrs.getString("Nome");
            	
            	Section s1 = doFirstSection(MDBrs, nome);
            	if(!exists(s1.titolo)) continue;
            	pagecount++;
            	System.out.println(pagecount);
            	
            	ps.setLong(1,pagecount);
            	ps.setString(2,this.toDB(nome));
            	ps.setString(3, MDBrs.getString("IDSBN"));
            	//ps.execute();
            	
            	Statement st = MySQLconn.createStatement();
            	cidcount++;
            	int contentID = cidcount;
            	String query = "insert into tblcomponenti (ID, Type, HasChildren) values (" + contentID + ",'content', 1)";
            	execute(st,query);
            	
            	query = "insert into tblstrutture values (" + pagecount + ",1)";
            	execute(st,query);
            	query = "insert into tblstrutture values (" + pagecount + ",11)";
            	execute(st,query);
            	query = "insert into tblstrutture values (" + pagecount + ","+contentID+")";
            	execute(st,query);
            	query = "insert into tblstrutture values (" + pagecount + ",4)";
            	execute(st,query);
            	
            	if(exists(s1.testo)){
            		cidcount++;
            		query = "insert into tblcomponenti (ID, Type) values (" + cidcount + ",'section')";
            		execute(st,query);
            		
	            	PrintWriter pw = new PrintWriter(new FileOutputStream(new File(basedir + "section\\data\\section"+cidcount+".xml")));
	            	s1.titolo = s1.titolo.toUpperCase();
	        		pw.print(s1.XMLSerialize(true));
	        		pw.flush();
	        		pw.close();
	        		
	        		query = "insert into tblcontenuti values (" + contentID + ","+ cidcount +",3,1)";
            		execute(st,query);
	        		
            	}
            	
            	Section avvisi = doAvvisi(MDBrs);
            	if(exists(avvisi.testo)){
            		cidcount++;
            		query = "insert into tblcomponenti (ID, Type) values (" + cidcount + ",'section')";
            		execute(st,query);
            		
	            	PrintWriter pw = new PrintWriter(new FileOutputStream(new File(basedir + "section\\data\\section"+cidcount+".xml")));
	        		pw.print(avvisi.XMLSerialize());
	        		pw.flush();
	        		pw.close();
	        		
	        		query = "insert into tblcontenuti values (" + contentID + ","+ cidcount +",3,2)";
            		execute(st,query);
	        		
            	}
            	
            	
            	
            	Section s2 = doLocation(MDBrs.getLong("IDDistribuzione"));
            	if(exists(s2.testo)){
            		cidcount++;
            		query = "insert into tblcomponenti (ID, Type) values (" + cidcount + ",'section')";
            		execute(st,query);
            		
	            	PrintWriter pw = new PrintWriter(new FileOutputStream(new File(basedir + "section\\data\\section"+cidcount+".xml")));
	        		pw.print(s2.XMLSerialize());
	        		pw.flush();
	        		pw.close();
	        		
	        		query = "insert into tblcontenuti values (" + contentID + ","+ cidcount +",3,2)";
            		execute(st,query);
	        		
            	}
            	
            	long idOrario = MDBrs.getLong("IDOrario");
            	Orario orario = DoOrario(idOrario);
            	
            	if(exists(orario.body)){
            		cidcount++;
            		query = "insert into tblcomponenti (ID, Type) values (" + cidcount + ",'table')";
            		execute(st,query);
            		
	            	PrintWriter pw = new PrintWriter(new FileOutputStream(new File(basedir + "table\\data\\table"+cidcount+".xml")));
	        		pw.print(orario.XMLSerialize());
	        		pw.flush();
	        		pw.close();
	        		
	        		query = "insert into tblcontenuti values (" + contentID + ","+ cidcount +",3,3)";
            		execute(st,query);
	        		
            	}
            	
            	Section s3 = DoCosa(idOrario);
            	if(exists(s3.testo)){
            		cidcount++;
            		query = "insert into tblcomponenti (ID, Type) values (" + cidcount + ",'section')";
            		execute(st,query);
            		
	            	PrintWriter pw = new PrintWriter(new FileOutputStream(new File(basedir + "section\\data\\section"+cidcount+".xml")));
	        		pw.print(s3.XMLSerialize());
	        		pw.flush();
	        		pw.close();
	        		
	        		query = "insert into tblcontenuti values (" + contentID + ","+ cidcount +",3,4)";
            		execute(st,query);
	        		
            	}
            	
            	Section s4 = DoUffici(MDBrs);
            	if(exists(s4.testo)){
            		cidcount++;
            		query = "insert into tblcomponenti (ID, Type) values (" + cidcount + ",'section')";
            		execute(st,query);
            		
	            	PrintWriter pw = new PrintWriter(new FileOutputStream(new File(basedir + "section\\data\\section"+cidcount+".xml")));
	        		pw.print(s4.XMLSerialize());
	        		pw.flush();
	        		pw.close();
	        		
	        		query = "insert into tblcontenuti values (" + contentID + ","+ cidcount +",3,5)";
            		execute(st,query);
	        		
            	}
            	
            	Section s5 = DoPatrimonio(MDBrs);
            	if(exists(s5.testo)){
            		cidcount++;
            		query = "insert into tblcomponenti (ID, Type) values (" + cidcount + ",'section')";
            		execute(st,query);
            		
	            	PrintWriter pw = new PrintWriter(new FileOutputStream(new File(basedir + "section\\data\\section"+cidcount+".xml")));
	        		pw.print(s5.XMLSerialize());
	        		pw.flush();
	        		pw.close();
	        		
	        		query = "insert into tblcontenuti values (" + contentID + ","+ cidcount +",3,6)";
            		execute(st,query);
	        		
            	}
            	
            	Section s6 = DoServizi(MDBrs);
            	if(exists(s6.testo)){
            		cidcount++;
            		query = "insert into tblcomponenti (ID, Type) values (" + cidcount + ",'section')";
            		execute(st,query);
            		
	            	PrintWriter pw = new PrintWriter(new FileOutputStream(new File(basedir + "section\\data\\section"+cidcount+".xml")));
	        		pw.print(s6.XMLSerialize());
	        		pw.flush();
	        		pw.close();
	        		
	        		query = "insert into tblcontenuti values (" + contentID + ","+ cidcount +",3,7)";
            		execute(st,query);
	        		
            	}
            	
            }
            
        }     
        catch (Exception e){
             e.printStackTrace();
        }

	}

	private Section doAvvisi(ResultSet brs) throws SQLException {
		Section s = new Section();
		s.titolo = "Avvisi";
		String testo = reverseWiki(brs.getString("avvisi"));
		if(exists(testo))
			s.testo = testo.substring(0,1).toUpperCase() + testo.substring(1);
		return s;
	}

	private boolean exists(String testo) {
		return (testo != null && !(testo.equals("")));
	}

	private Section DoServizi(ResultSet brs) throws SQLException {
		Section s = new Section();
		s.titolo = "Servizi";
		String testo = reverseWiki(brs.getString("servizi"));
		if(exists(testo))
			s.testo = testo.substring(0,1).toUpperCase() + testo.substring(1);
		return s;
	}

	private Section DoPatrimonio(ResultSet brs) throws SQLException {
		Section s = new Section();
		s.titolo = "Patrimonio";
		String copertura = reverseWiki(brs.getString("copertura"));
		String collezioni = reverseWiki(brs.getString("collezioni"));
		
		if(exists(copertura)){
			s.testo = "''Copertura disciplinare'': " + copertura;
		}
		if(exists(collezioni)){
			if(exists(copertura))s.testo = s.testo + "\n";
			s.testo = s.testo + "''Collezioni'': " + collezioni;
		}
		
		return s;
	}

	private Section DoUffici(ResultSet brs) throws SQLException {
		Section s = new Section();
		s.titolo = "Uffici amministrativi";
		s.testo = brs.getString("Amministrazione");
		if(exists(s.testo)){
			s.testo = reverseWiki(s.testo);
		}
		return s;
	}

	private String reverseWiki(String testo) {
		if(testo==null) return null;
		
		testo = testo.replaceAll("<BR/>","\n").replaceAll("<br/>","\n");
		testo = testo.replaceAll("<BR>","\n").replaceAll("<br>","\n");
		
		testo = testo.replaceAll("<B><I>","<I><B>").replaceAll("</I></B>","</B></I>");
		testo = testo.replaceAll("<b><i>","<i><b>").replaceAll("</i></b>","</b></i>");

		
		String[] splitted = testo.split("<");
		String ret = "";
		for(int i=0;i<splitted.length;i++){
			splitted[i] = splitted[i].replaceAll("mailto: ", "mailto:");
			if(splitted[i].toLowerCase().startsWith("a href")){
				splitted[i] = "[" + splitted[i].substring(8);
				splitted[i] = splitted[i].replaceAll("\"","");
			}
			if(splitted[i].toLowerCase().startsWith("/a")){
				splitted[i] = "]" + splitted[i].substring(3);
			}
			if(splitted[i].toLowerCase().startsWith("i>") || splitted[i].toLowerCase().startsWith("/i>")){
				splitted[i] = "''" + splitted[i].substring(splitted[i].indexOf(">")+1);
			}
			if(splitted[i].toLowerCase().startsWith("b>") || splitted[i].toLowerCase().startsWith("/b>")){
				splitted[i] = "__" + splitted[i].substring(splitted[i].indexOf(">")+1);
			}
			if(splitted[i].toLowerCase().startsWith("p>") || splitted[i].toLowerCase().startsWith("/p>")){
				splitted[i] = splitted[i].substring(splitted[i].indexOf(">")+1);
			}
			if(splitted[i].toLowerCase().startsWith("font") || splitted[i].toLowerCase().startsWith("/font")){
				splitted[i] = splitted[i].substring(splitted[i].indexOf(">")+1);
			}
			if(splitted[i].toLowerCase().startsWith("blink>") || splitted[i].toLowerCase().startsWith("/blink>")){
				splitted[i] = splitted[i].substring(splitted[i].indexOf(">")+1);
			}
			ret = ret + splitted[i];
		}
		
		return ret;
	}

	private Orario DoOrario(long id) throws SQLException {
		Orario t = new Orario();
		//t.description = "Orario";
		
		Statement st = MDBconn.createStatement();
		ResultSet rs = st.executeQuery("select * from tblOrario where IDOrario="+id);
		if(rs.next())
			t.init(rs);
		
		rs.close();
		st.close();
		return t;
	}
	
	private Section DoCosa(long id) throws SQLException {
		Section s = new Section();
		s.titolo = "Note";
		
		Statement st = MDBconn.createStatement();
		ResultSet rs = st.executeQuery("select NOTA,Chiusure from tblOrario where IDOrario="+id);
		if(rs.next()){
			String note = reverseWiki(rs.getString("NOTA"));
			String chius =  reverseWiki(rs.getString("Chiusure"));
			if(exists(note)){
				s.testo = s.testo.concat(HtmlCodec.encode(note));
			}
			if(exists(chius)){
				if(exists(note))
					s.testo = s.testo.concat("\n");
				s.testo = s.testo.concat("__CHIUSURA__: " + chius);
			}
		}
		rs.close();
		st.close();
		
		return s;
	}

	private Section doLocation(long id) throws SQLException {
		Section ret = new Section();
		ret.titolo = "Prestito e consultazione";
		Statement st = MDBconn.createStatement();
		ResultSet rs = st.executeQuery("select * from tblDistribuzione where iddistribuzione="+id);
		if(rs.next()){
			String luogo = rs.getString("Luogo");
			if(exists(luogo))
				ret.testo = ret.testo + "__" + luogo + "__\n";  
			
			String teldis = rs.getString("teldis");
			if(exists(teldis))
				ret.testo = ret.testo + "Tel.: " + teldis + "\n";
			
			String faxdis = rs.getString("faxdis");
			if(exists(faxdis))
				ret.testo = ret.testo + "Fax: " + faxdis + "\n";
		}
		rs.close();
		st.close();
		return ret;
	}

	private Section doFirstSection(ResultSet MDBrs, String nome) throws FileNotFoundException, SQLException {
		
		Section s = new Section(); 
    	String titolo = MDBrs.getString("Denominazione");
    	
    	if(exists(titolo))
    		s.titolo = titolo;
    	else if(exists(nome))
    		s.titolo = nome;
    	
    	s.img = "images/contentimg/biblioteche.jpg";
    	
    	/*if(titolo != null)
    		s.testo = s.testo.concat("(" + nome +")\n");*/
    	
    	String indirizzo = MDBrs.getString("Indirizzo");
    	if(exists(indirizzo))
    		s.testo = s.testo.concat(indirizzo + "\n");
    	
    	String telefono = MDBrs.getString("Tel");
    	String fax = MDBrs.getString("Fax");
    	if(exists(telefono))
    		s.testo = s.testo.concat("Tel.: " + telefono);
    	if(exists(fax)){
    		if(exists(telefono)) s.testo = s.testo.concat(" - ");
    		s.testo = s.testo.concat("Fax: " + fax);
    	}

    	String direttore = MDBrs.getString("Direttore");
    	if(exists(direttore)){
    		s.testo = s.testo.concat("\nDirettore: ");
        	String emaildir = MDBrs.getString("EmailDir"); 
        	if(exists(emaildir))
        		s.testo = s.testo.concat("[mailto:" + emaildir + ">" + direttore + "]\n");
        	else
        		s.testo = s.testo.concat(direttore+"\n");
    	}
    	return s;
    	
		
	}
		public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}
		ExportOldDB crea = new ExportOldDB();
		crea.doJob();

	}

	private void doJob() {

	}
	
	private String toDB(String s){
		String[] words = s.split(" ");
		s = "";
		for(int i=0 ; i<words.length; i++){
			if(words[i].length()>3)
				words[i] = words[i].substring(0,1).toUpperCase() + words[i].substring(1).toLowerCase();
			else
				words[i] = words[i].toLowerCase();
			s = s + " " + words[i];
		}
		s = s.trim();
		s = s.substring(0,1).toUpperCase() + s.substring(1);
		return rinomina.elabora(s);
	}
	
	private void execute(Statement s, String query) throws SQLException{
		//s.execute(query);
	}

}