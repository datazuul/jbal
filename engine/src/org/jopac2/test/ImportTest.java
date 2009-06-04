package org.jopac2.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import org.jopac2.engine.NewSearch.DoSearchNew;
import org.jopac2.engine.command.JOpac2Import;
import org.jopac2.engine.dbGateway.DbGateway;
import org.jopac2.engine.dbGateway.StaticDataComponent;
import org.jopac2.engine.listSearch.ListSearch;
import org.jopac2.engine.parserRicerche.parser.exception.ExpressionException;
import org.jopac2.engine.utils.MyTimer;
import org.jopac2.engine.utils.SearchResultSet;
import org.junit.After;
import org.junit.Before;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import junit.framework.TestCase;

public class ImportTest extends TestCase {
	private static String sitename = "sebina";
	private InputStream in = null;
	private static String filetype = "sebina";
	private static String JOpac2confdir = "src/org/jopac2/conf";
	//private static String dbUrl = "jdbc:derby:/siti/jopac2/catalogs/db" + sitename + ";create=true";
	private static String dbUrl = "jdbc:mysql://localhost/db" + sitename;
	private static String dbUser = "root";
	private static String dbPassword = "";

	private static String _classMySQLDriver = "com.mysql.jdbc.Driver";
	private static String _classHSQLDBDriver = "org.hsqldb.jdbcDriver";
	private static String _classDerbyDriver = "org.apache.derby.jdbc.EmbeddedDriver";
	private DoSearchNew doSearchNew;
	private static boolean ru = false;
	private Connection conn;

	public Connection CreaConnessione() throws SQLException {
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

	@Before
	public void setUp() throws Exception {
		super.setUp();
		if (!ru) {
			String inFile = new String(
					Base64
							.decode(org.jopac2.test.Base64DataExample.unimarcBase64DataExample),
					"utf-8");

			in = new ByteArrayInputStream(inFile.getBytes());

			JOpac2Import ji = new JOpac2Import(in, filetype, JOpac2confdir,
					dbUrl, dbUser, dbPassword, true, System.out);
			ji.doJob(false);
			// ji.wait();
			ji.destroy(dbUrl);
			ru = true;
		}

		StaticDataComponent sd = new StaticDataComponent();
		sd.init("src/org/jopac2/conf/commons/");
		conn = CreaConnessione();
		doSearchNew = new DoSearchNew(conn, sd);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		conn.close();
	}

	private SearchResultSet doSearch(String str) throws ExpressionException,
			SQLException {
		MyTimer t = new MyTimer(new String[] { "NEW", "esecuzione" });
		t.Start();
		SearchResultSet rs = doSearchNew.executeSearch(str, false);
		t.SaveTimer("time:" + rs.getQueryTime() + "#rec:" + rs.getQueryCount());
		t.Stop();
		System.out.println(rs.getQuery() + "\n[optimized:"
				+ rs.getOptimizedQuery() + "]");
		System.out.println(t);
		return rs;
	}

	private boolean checkStringSequence(Vector<String> v1, String[] a) {
		boolean r = false;
		if (v1 != null && v1.size() == a.length) {
			r = true;
			for (int i = 0; i < v1.size(); i++) {
				if (!v1.elementAt(i).equals(a[i])) {
					r = false;
					break;
				}
			}
		}
		return r;
	}

	private boolean checkIdSequence(Vector<Long> recordIDs, long[] a) {
		boolean r = false;
		if (recordIDs != null && recordIDs.size() == a.length) {
			r = true;
			for (int i = 0; i < recordIDs.size(); i++) {
				if (recordIDs.elementAt(i) != a[i]) {
					r = false;
					break;
				}
			}
		}
		return r;
	}

	private void outputJava(Vector<String> v1) {
		for (int i = 0; v1 != null && i < v1.size(); i++) {
			System.out.println("\"" + v1.elementAt(i) + "\",\n");
		}
	}

	// **** inizio test ***//

	public void testTblTipiNotizie() throws Exception {

		/**
		 * FROM l_classi_parole lcp," // QUESTA TABELLA DEVE ESSERE LA PRIMA +
		 * "          l_classi_parole_notizie lcpn," +
		 * "          anagrafe_parole a "
		 */

		String[] tipi_notizie = { "1,sebina", "2,sosebi", "3,sbn-unix",
				"4,isisbiblo", "5,easyweb", "6,bibliowin4", "7,pregresso",
				"8,ejournal", "9,eut", "10,eutmarc" , "11,mdb"};

		Vector<String> v1 = DbGateway.dumpTable(conn, "tipi_notizie");
		boolean r1 = checkStringSequence(v1, tipi_notizie);
		if (!r1) {
			System.out.println(v1);
		}
		assertTrue("Done ", r1);
	}

	public void testTblAnagrafeParole() throws Exception {
		String[] tipi_notizie = { "1,05,", "2,214,", "3,3680,", "4,x,x",
				"5,eng,eng", "6,english,english", "7,grammar,gramm", "8,in,in",
				"9,use,use", "10,a,a", "11,self,self", "12,study,study",
				"13,reference,referenc", "14,and,and", "15,practice,practic",
				"16,book,book", "17,for,for", "18,intermediate,intermed",
				"19,students,students", "20,with,with", "21,answers,answers",
				"22,raymond,raymond", "23,murphy,murphy", "24,bib,bib",
				"25,generale,general", "26,univ,univ", "27,trieste,triest",
				"28,sede,sed", "29,di,di", "30,gorizia,goriz", "31,ts,ts",
				"32,filosofia,filosof", "33,socio,soc", "34,politica,polit",
				"35,economia,econom", "36,tecnico,tecnic", "37,scient,scient",
				"38,farmacia,farmac", "39,dip,dip", "40,lett,lett", "41,e,e",
				"42,civ,civ", "43,anglo,anglo", "44,germaniche,german",
				"45,scienze,scienz", "46,d,d", "47,formazione,formazion",
				"48,ducsrs,ducsrs", "49,ginec,ginec", "50,ostetricia,ostetric",
				"51,jcjca0000188275,jcjca", "52,1,", "53,v,v", "54,8,",
				"55,rist,rist", "56,1997,", "57,711,", "58,6984,",
				"59,high,high", "60,cotton,cotton", "61,novel,novel",
				"62,darryl,darryl", "63,pinckney,pinckney",
				"64,jcjca0000176855,jcjca", "65,08,", "66,014,", "67,1131,",
				"68,9,", "69,story,story", "70,discourse,discours",
				"71,narrative,narrat", "72,structure,structur",
				"73,fiction,fiction", "74,film,film", "75,by,by",
				"76,seymour,seymour", "77,chatman,chatman",
				"78,cornell,cornell", "79,paperbacks,paperbacks",
				"80,bultot,bultot", "81,jcjca0000176865,jcjca", "82,6,",
				"83,1993,", "84,02,", "85,310,", "86,7360,", "87,7,",
				"88,the,the", "89,columbia,columb", "90,history,history",
				"91,of,of", "92,american,american", "93,emory,emory",
				"94,elliott,elliott", "95,general,general", "96,editor,editor",
				"97,associate,assoc", "98,editors,editors", "99,carthy,carthy",
				"100,n,n", "101,davidson,davidson", "102,et,et", "103,al,al",
				"104,new,new", "105,views,views", "106,cathy,cathy",
				"107,jcjca0000176875,jcjca", "108,36,", "109,314,",
				"110,4767,", "111,ger,ger", "112,mythos,mythos", "113,und,und",
				"114,mysterium,mysterium", "115,die,die",
				"116,rezeption,rezeption", "117,des,des",
				"118,mittelalters,mittelalters", "119,im,im", "120,werk,werk",
				"121,gerhart,gerhart", "122,hauptmanns,hauptmanns",
				"123,jorg,jorg", "124,platiel,platiel",
				"125,mikrokosmos,mikrokosmos", "126,hauptmann,hauptmann",
				"127,jcjca0000177435,jcjca", "128,31,", "129,500,",
				"130,8749,", "131,gmh,gmh", "132,der,der", "133,ring,ring",
				"134,fruhneuhochdeutsch,fruhneuhochdeutsc",
				"135,neuhochdeutsch,neuhochdeutsc", "136,heinrich,heinric",
				"137,wittenwiler,wittenwiler", "138,nach,nach", "139,dem,dem",
				"140,text,text", "141,von,von", "142,edmund,edmund",
				"143,wiessner,wiessner", "144,ins,ins",
				"145,neuhochdeutsche,neuhochdeutsc", "146,ubersetzt,ubersetzt",
				"147,herausgegeben,herausgegeben", "148,horst,horst",
				"149,brunner,brunner", "150,universal,universal",
				"151,bibliothek,bibliothek", "152,jcjca0000176125,jcjca",
				"153,jcjca0000179302,jcjca", "154,2,", "155,copia,cop",
				"156,34,", "157,581,", "158,6072,", "159,buch,buch",
				"160,natur,natur", "161,konrad,konrad",
				"162,megenberg,megenberg", "163,ubertragen,ubertragen",
				"164,eingeleitet,eingeleitet", "165,gerhard,gerhard",
				"166,sollbach,sollbac", "167,jcjca0000176135,jcjca",
				"168,8796,", "169,moriz,moriz", "170,craun,craun",
				"171,mittelhochdeutsch,mittelhochdeutsc",
				"172,mittelhochdeutscher,mittelhochdeutscher",
				"173,ausgabe,ausgab", "174,ulrich,ulric",
				"175,pretzel,pretzel", "176,ubersetzung,ubersetzung",
				"177,kommentar,komment", "178,nachwort,nachwort",
				"179,albrecht,albrecht", "180,classen,classen",
				"181,jcjca0000177635,jcjca", "182,tristan,tristan",
				"183,isolde,isold", "184,den,den", "185,dichtungen,dichtungen",
				"186,neuen,neuen", "187,zeit,zeit", "188,wolfgang,wolfgang",
				"189,golther,golther", "190,jcjca0000177665,jcjca",
				"191,2855,", "192,kempten,kempten", "193,welt,welt",
				"194,lohn,lohn", "195,das,das", "196,herzmaere,herzm",
				"197,wurzburg,wurzburg",
				"198,mittelhochdeutsche,mittelhochdeutsc", "199,edward,edward",
				"200,schroder,schroder", "201,ubersetz,ubersetz",
				"202,mit,mit", "203,anmerkungen,anmerkungen",
				"204,heinz,heinz", "205,rolleke,rollek",
				"206,jcjca0000177625,jcjca", "207,38,", "208,847,",
				"209,9764,", "210,ingeborg,ingeborg",
				"211,bachmanns,bachmanns", "212,undine,undin", "213,geht,geht",
				"214,ein,ein", "215,stoff,stoff",
				"216,motivgeschichtlicher,motivgeschichtlicher",
				"217,vergleich,vergleic", "218,friederich,friederic",
				"219,de,de", "220,la,la", "221,motte,mott",
				"222,fouques,fouques", "223,jean,jean",
				"224,girodoux,girodoux", "225,ondine,ondin", "226,mona,mon",
				"227,el,el", "228,nawab,nawab", "229,jcjca0000180455,jcjca",
				"230,35,", "231,252,", "232,1014,", "233,0,",
				"234,deutschsprachige,deutschsprachig",
				"235,literatur,literatur", "236,ausland,ausland",
				"237,alexander,alexander", "238,ritter,ritter",
				"239,zeitschrift,zeitschrift", "240,fur,fur",
				"241,literaturwissenschaft,literaturwissenschaft",
				"242,linguistik,linguistik", "243,beihefte,beiheft",
				"244,jcjca0000180435,jcjca", "245,32,", "246,610,",
				"247,3898,", "248,5,", "249,auslandes,auslandes",
				"250,erwin,erwin", "251,theodor,theodor",
				"252,rosenthal,rosenthal", "253,hrsg,hrsg",
				"254,germanistische,germanistisc",
				"255,lehrbuchsammlung,lehrbuchsammlung",
				"256,jcjca0000180425,jcjca", "257,213,", "258,9040,",
				"259,ellen,ellen", "260,glasgow,glasgow",
				"261,contemporary,contemporary", "262,reviews,reviews",
				"263,edited,edited", "264,dorothy,dorothy", "265,m,m",
				"266,scura,scur", "267,critical,critical",
				"268,archives,archives", "269,jcjca0000181065,jcjca",
				"270,8319,", "271,edith,edith", "272,wharton,wharton",
				"273,james,james", "274,w,w", "275,tuttleton,tuttleton",
				"276,kristin,kristin", "277,o,o", "278,lauer,lauer",
				"279,margaret,margaret", "280,p,p", "281,murray,murray",
				"282,jcjca0000181055,jcjca", "283,8336,",
				"284,emerson,emerson", "285,thoreau,thoreau", "286,joel,joel",
				"287,myerson,myerson", "288,jcjca0000181075,jcjca", "289,900,",
				"290,1521,", "291,kursbuch,kursbuc", "292,hartmut,hartmut",
				"293,aufderstrasse,aufderstr", "294,jcjca0000189845,jcjca",
				"295,themen,themen", "296,neu,neu", "297,lehrwerk,lehrwerk",
				"298,deutsch,deutsc", "299,als,als",
				"300,fremdsprache,fremdsprac", "306,jcjca0000189855,jcjca",
				"305,bock,bock", "301,901,", "303,arbeitsbuch,arbeitsbuc",
				"304,heiko,heik", "302,4," };

		Vector<String> v1 = DbGateway.dumpTable(conn, "anagrafe_parole");
		// outputJava(v1);
		boolean r1 = checkStringSequence(v1, tipi_notizie);
		if (!r1) {
			System.out.println(v1);
		}
		assertTrue("Done ", r1);
	}

	
	  public void testSearchOrder() throws Exception {
		SearchResultSet rs = doSearch("(TIT=in)|(TIT=der)");
		long[] unordered = { 1, 3, 6, 7, 9, 10 };
		long[] ordered = { 7, 6, 1, 10, 3, 9 };
		boolean r1 = checkIdSequence(rs.getRecordIDs(), unordered);
		//SearchResultSet.dumpSearchResultSet(conn, rs);
		DbGateway.orderBy(conn, "TIT", rs);
		//SearchResultSet.dumpSearchResultSet(conn, rs);
		boolean r2 = checkIdSequence(rs.getRecordIDs(), ordered);
		assertTrue("Done ", r1 && r2);
	}

	public void testListTIT() throws Exception {
		SearchResultSet rs = ListSearch.listSearch(conn, "TIT",
				"English grammar in use", 100);
		long[] listres = { 1, 10, 2, 11, 17, 8, 5, 3, 4, 18, 9 };
		//SearchResultSet.dumpSearchResultSet(conn, rs);
		boolean r1 = checkIdSequence(rs.getRecordIDs(), listres);
		assertTrue("Done ", r1);
	}
	
	public void testListAUT() throws Exception {
		SearchResultSet rs = ListSearch.listSearch(conn, "AUT",
				"a", 100);
		long[] listres = { 17, 19, 6, 3, 8, 4, 11, 4, 9, 5, 7, 10, 15, 1, 15, 16, 2, 5, 8, 12, 10, 13, 10, 14, 7, 15, 6, 6 };
		SearchResultSet.dumpSearchResultSet(conn, rs);
		boolean r1 = checkIdSequence(rs.getRecordIDs(), listres);
		assertTrue("Done ", r1);
	}
	 

}
