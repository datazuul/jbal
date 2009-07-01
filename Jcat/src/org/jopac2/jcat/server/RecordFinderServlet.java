package org.jopac2.jcat.server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jopac2.engine.NewSearch.DoSearchNew;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.dbGateway.StaticDataComponent;
import org.jopac2.engine.utils.SearchResultSet;
import org.jopac2.jbal.RecordInterface;

public class RecordFinderServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 8305367618713715640L;
	
	private static String dbUrl = "jdbc:mysql://localhost/dbeutmarc";
	private static String dbUser = "root";
	private static String dbPassword = "";

	private static String _classMySQLDriver = "com.mysql.jdbc.Driver";
	private static String _classHSQLDBDriver = "org.hsqldb.jdbcDriver";
	private static String _classDerbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml; charset=utf-8");
		
		System.out.println("QueryString: "+request.getQueryString());
		
		Enumeration<String> e=request.getParameterNames();
		while(e.hasMoreElements()) {
			String k=e.nextElement();
			System.out.println(k+"="+request.getParameter(k));
		}
		
		Connection conn=null;
		String isbd=request.getParameter("ISBD");
		response.getWriter().write("<response>");
		
		if(isbd!=null)
			isbd=new String(isbd.getBytes("iso-8859-1"),"utf-8");
			
			try {
				StaticDataComponent sd = new StaticDataComponent();
				sd.init("/java_jopac2/engine/src/org/jopac2/conf/commons/");
				conn = CreaConnessione();
				DoSearchNew doSearchNew = new DoSearchNew(conn, sd);
				SearchResultSet rs = doSearchNew.executeSearch("(ANY="+isbd+")",false);
				
				int s=rs.getRecordIDs().size();
				
				response.getWriter().write("<status>0</status>");
				response.getWriter().write("<startRow>1</startRow>");
				response.getWriter().write("<endRow>"+s+"</endRow>");
				response.getWriter().write("<totalRows>"+s+"</totalRows>");
				
				response.getWriter().write("<data>");
				
				for(int i=0;i<s;i++) {
					long jid=rs.getRecordIDs().elementAt(i).longValue();
					RecordInterface ma=DbGateway.getNotiziaByJID(conn, jid);
					response.getWriter().write("<record>" +
							"	<JID>"+jid+"</JID>" +
							"	<ISBD>"+ma.getISBD()+"</ISBD>" +
							"</record>");
					ma.destroy();
				}
				
				response.getWriter().write("</data>");
				
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			finally {
				try {
					
					if(conn!=null)
						conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		
		response.getWriter().write("</response>");
		
	}

	public static Connection CreaConnessione() throws SQLException {
		Connection conn = null;
		String driver = _classMySQLDriver;
		if (dbUrl.contains(":hsqldb:"))
			driver = _classHSQLDBDriver;
		if (dbUrl.contains(":derby:"))
			driver = _classDerbyDriver;

		boolean inizializzato = false;
		if (!inizializzato) {
			inizializzato = true;
			try {
				Class.forName(driver).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("getting conn....");
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		System.out.println("presa");

		return conn;
	}

}
