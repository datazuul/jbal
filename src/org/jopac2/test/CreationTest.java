package org.jopac2.test;


import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.junit.After;
import org.junit.Before;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class CreationTest extends TestCase {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testSebinaURLEncoded() throws Exception {
		String n1="01189nam0022002530004500001002000000005000900020020001100029100004100040101000800081102000700089200005100096210002700147215001800174225010300192410013400295606004400429676005700473700004200530801002300572899006000595899005800655899013200713899009000845%1EIT%5CICCU%5CTSA%5C0021482%1E19970702%1E++%1Fb59-485%1E++%1Fa19970630d1959++++%7C%7C%7C%7C%7Citac0103++++ba%1E%7C+%1Faita%1E++%1FaIT%1E1+%1FaShakespeare+e+il+Rinascimento%1FfNemi+D%27Agostino%1E++%1FaTrieste%1FcSmolars%1Fd1959%1E++%1Fa23+p.%1Fd23+cm.%1E0+%1FaUniversita+degli+studi+di+Trieste%2C+Facolta+di+lettere+e+filosofia%2C+Istituto+di+filologia+germanica%1E+1%1F1001IT%5CICCU%5CCFI%5C0083335%1F12001+%1FaUniversita+degli+studi+di+Trieste%2C+Facolta+di+lettere+e+filosofia%2C+Istituto+di+filologia+germanica%1E++%1FaShakespeare%2C+William+e+Rinascimento%1F2FI%1E++%1Fa822.3%1FvFI%1F1LETTERATURA+DRAMMATICA+INGLESE.+1558-1625%1E+1%1FaD%27Agostino%2C+Nemi%1F3IT%5CICCU%5CCFIV%5C032233%1E+0%1FaIT%1FbICCU%1Fc20040216%1E++%1FaBiblioteca+nazionale+centrale+Firenze+FI%1F1FI0098%1F2CFICF%1E++%1FaBiblioteca+statale+Isontina+Gorizia+GO%1F1GO0025%1F2TSABI%1E++%1FaBiblioteca+delle+facolt%E2%80%A1+di+Giurisprudenza+e+Lettere+e+filosofia+dell%27Universit%E2%80%A1+degli+studi+di+Milano+Milano+MI%1F1MI0190%1F2USMA6%1E++%1FaBiblioteca+generale+dell%27Universita%27+degli+studi+di+Trieste+Trieste+TS%1F1TS0137%1F2TSABG%1E%1D";
		
		n1=java.net.URLDecoder.decode(n1, "utf-8");
				
		String n2=rebuild(n1, "sebina");
		
		boolean r=n1.equals(n2);

		if(!r) {
			System.out.println("Originale: "+n1);
			System.out.println("Calcolato: "+n2);
		}
		assertTrue("Confronto: "+r,r);  
	}

	
	public void testSebinaBase64() throws Exception {
		String base64encoded="MDEzNzZuYU0wIDIyMDAzMjUgICA0NTAgMDAxMDAxMTAwMDAwMDA1MDAwOTAwMDExMDEwMDAxODAw" +
				"MDIwMTAwMDA0MTAwMDM4MTAxMDAwODAwMDc5MTAyMDAwNzAwMDg3MjAwMDEyNTAwMDk0MjA1MDAx" +
				"MDAwMjE5MjEwMDA0ODAwMjI5MjE1MDAyODAwMjc3NjIwMDAxNDAwMzA1Njc2MDAwODAwMzE5NzAw" +
				"MDAzNDAwMzI3NzEyMDA0MDAwMzYxODAxMDAyMjAwNDAxODk5MDAzOTAwNDIzODk5MDA0MTAwNDYy" +
				"ODk5MDAyNjAwNTAzODk5MDAzMTAwNTI5ODk5MDAyNTAwNTYwODk5MDA0MTAwNTg1ODk5MDA0NTAw" +
				"NjI2ODk5MDAzODAwNjcxODk5MDAzODAwNzA5OTUwMDMwMzAwNzQ3HlRTQTAwMDI1ODAeMjAwMTA2" +
				"MjkeICAfYTA1LTIxNC0zNjgwLVgeICAfYTE5OTQxMTI4ZDE5OTQgICAgfHx8eTBlbmdhMDEwMyAg" +
				"ICBiYR4gIB9hZW5nHiAgH2FnYh4xIB9hRW5nbGlzaCBncmFtbWFyIGluIHVzZR9lYSBzZWxmLXN0" +
				"dWR5IHJlZmVyZW5jZSBhbmQgcHJhY3RpY2UgYm9vayBmb3IgaW50ZXJtZWRpYXRlIHN0dWRlbnRz" +
				"H2V3aXRoIGFuc3dlcnMfZlJheW1vbmQgTXVycGh5HiAgH2EyLiBlZB4gIB9hQ2FtYnJpZGdlH2ND" +
				"YW1icmlkZ2UgdW5pdmVyc2l0eSBwcmVzcx9kMTk5NB4gIB9hWCwgMzUwIHAuH2NpbGwuH2QyNSBj" +
				"bS4eICAfZENBTUJSSURHRR4gIB9hNDI1HiAxH2FNdXJwaHkfYiwgUmF5bW9uZB8zTUlMVjAxNTky" +
				"Nh4gIB9hQ2FtYnJpZGdlIHVuaXZlcnNpdHkgcHJlc3MfMzAwMDAwMzQeIDAfYUlUH2JUU0EfYzIw" +
				"MDMxMDA4HiAgH2FCSUIuIEdFTkVSQUxFIFVOSVYuIFRSSUVTVEUfMlRTQUJHHiAgH2FCSUIuIFNF" +
				"REUgREkgR09SSVpJQS1VTklWLiBUUx8yVFNBQ0EeICAfYUJJQi4gRklMT1NPRklBHzJUU0FESR4g" +
				"IB9hQklCLiBTT0NJTy1QT0xJVElDQR8yVFNBRFYeICAfYUJJQi4gRUNPTk9NSUEfMlRTQUUwHiAg" +
				"H2FCSUIuIFRFQ05JQ08gU0NJRU5ULi1GQVJNQUNJQR8yVFNBRjAeICAfYURJUC4gTEVUVC4gRSBD" +
				"SVYuQU5HTE8tR0VSTUFOSUNIRR8yVFNBSkMeICAfYUJJQi4gU0NJRU5aRSBELiBGT1JNQVpJT05F" +
				"HzJUU0FNMB4gIB9hRFVDU1JTLUdJTkVDLiBFIE9TVEVUUklDSUEfMlRTQU5MHiAwH2FESVAuIExF" +
				"VFQuIEUgQ0lWLkFOR0xPLUdFUk1BTklDSEUfYzEgdi4sIDguIHJpc3QuIDE5OTcgH2RKQ0lOLzAy" +
				"LmEgICAvICAgICAgICAgICAwMDA1ICAgICAgICAfZUpDSkNBMDAwMDE4ODI3NSAgICAgICAgICAg" +
				"ICAgICAgICAgICAgICAgICAxIHYuLCA4LiByaXN0LiAxOTk3ICAgICAgICAgICAgICAgICAgICAg" +
				"ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg" +
				"ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg" +
				"ICAgICAgHh0=";
		String rec=new String(Base64.decode(base64encoded),"utf-8");
		String r=rebuild(rec,"sebina");
		
		//System.out.println(Base64.encode(r.getBytes()));
		
		boolean m=rec.equals(r);
		if(!m) {
			System.out.println(rec+"\n"+r);
		}
		assertTrue("Equal: "+m ,m); 
	}
	
	
	public void testIsisbiblo1() throws Exception {
		String n1="005460000000002770004500001000700000101000400007102000300011011008500014014002900099015002100128172002500149041002100174061001100195062000300206700000300209712000400212715002000216741000600236742000700242743000300249755000200252757000200254761000200256762000300258999000700261#000001#ITA#IT#^TOpera omnia medica, quorum series post praefationem subjicitur^RHermanni Boerhaave#^LVenetiis^NL.Basilium^D1796#^EXII, 597 p.^D23 cm#Con ritratto dell'autore#^CBOERHAAVE^NHermann#^Smedicina#61#am#BCC#^CBesenghi 598/1953#^v871#921105#av#a#s#p#ct#000001##";	
		String r=rebuild(n1,"isisbiblo");
		boolean m=n1.equals(r);
		if(!m) {
			System.out.println(n1+"\n"+r);
		}
		assertTrue("Equal: "+m ,m); 
	}
	
	public void testIsisbiblo2() throws Exception {
		String n1="004900000000002770004500001000700000101000400007102000300011011003800014012000800052014003800060015002200098041001800120061001100138062000300149700000300152712000400155715002100159741000600180742000700186743000300193755000200196757000200198761000200200762000300202999000700205#000002#LAT#IT#^TPrincipia medicinae^RFrancisco Home#^E2. ed#^LVenetiis^NBertella et Perlini^D1776#^EVIII, 206 p.^D18 cm#^CHOME^NFrancisco#^Smedicina#61#am#BCC#^CBesenghi 3160/1954#^v176#921105#av#a#s#p#ct#000002##";
		String r=rebuild(n1,"isisbiblo");
		boolean m=n1.equals(r);
		if(!m) {
			System.out.println(n1+"\n"+r);
		}
		assertTrue("Equal: "+m ,m); 
	}


	
	private String rebuild(String record, String type) throws InvocationTargetException {
		RecordInterface ma=null;
		try {
			ma = RecordFactory.buildRecord(0, record.getBytes(), type, 0);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		String r=ma.toString();
		ma.destroy();
		return r;
	}
}
