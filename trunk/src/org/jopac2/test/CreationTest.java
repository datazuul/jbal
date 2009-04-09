package org.jopac2.test;


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

	
	public void testSebina() throws Exception {
		String n1="MDEzNzhuYU0wIDIyMDAzMjUgICA0NTAgMDAxMDAxMTAwMDAwMDA1MDAwOTAwMDExMDEwMDAxODAw" +
					"MDIwMTAwMDA0MTAwMDM4MTAxMDAwODAwMDc5MTAyMDAwNzAwMDg3MjAwMDEyNTAwMDk0MjA1MDAx" +
					"MDAwMjE5MjEwMDA0ODAwMjI5MjE1MDAyODAwMjc3NjIwMDAxNDAwMzA1Njc2MDAwODAwMzE5NzAw" +
					"MDAzNDAwMzI3NzEyMDA0MjAwMzYxODAxMDAyMjAwNDAzODk5MDAzOTAwNDI1ODk5MDA0MTAwNDY0" +
					"ODk5MDAyNjAwNTA1ODk5MDAzMTAwNTMxODk5MDAyNTAwNTYyODk5MDA0MTAwNTg3ODk5MDA0NTAw" +
					"NjI4ODk5MDAzODAwNjczODk5MDAzODAwNzExOTUwMDMwMzAwNzQ5HlRTQTAwMDI1ODAeMjAwMTA2" +
					"MjkeICAfYTA1LTIxNC0zNjgwLVgeICAfYTE5OTQxMTI4ZDE5OTQgICAgfHx8eTBlbmdhMDEwMyAg" +
					"ICBiYR4gIB9hZW5nHiAgH2FnYh4xIB9hRW5nbGlzaCBncmFtbWFyIGluIHVzZR9lYSBzZWxmLXN0" +
					"dWR5IHJlZmVyZW5jZSBhbmQgcHJhY3RpY2UgYm9vayBmb3IgaW50ZXJtZWRpYXRlIHN0dWRlbnRz" +
					"H2V3aXRoIGFuc3dlcnMfZlJheW1vbmQgTXVycGh5HiAgH2EyLiBlZB4gIB9hQ2FtYnJpZGdlH2ND" +
					"YW1icmlkZ2UgdW5pdmVyc2l0eSBwcmVzcx9kMTk5NB4gIB9hWCwgMzUwIHAuH2NpbGwuH2QyNSBj" +
					"bS4eICAfZENBTUJSSURHRR4gIB9hNDI1HiAxH2FNdXJwaHkfYiwgUmF5bW9uZB8zTUlMVjAxNTky" +
					"Nh4gIB80H2FDYW1icmlkZ2UgdW5pdmVyc2l0eSBwcmVzcx8zMDAwMDAzNB4gMB9hSVQfYlRTQR9j" +
					"MjAwMzEwMDgeICAfYUJJQi4gR0VORVJBTEUgVU5JVi4gVFJJRVNURR8yVFNBQkceICAfYUJJQi4g" +
					"U0VERSBESSBHT1JJWklBLVVOSVYuIFRTHzJUU0FDQR4gIB9hQklCLiBGSUxPU09GSUEfMlRTQURJ" +
					"HiAgH2FCSUIuIFNPQ0lPLVBPTElUSUNBHzJUU0FEVh4gIB9hQklCLiBFQ09OT01JQR8yVFNBRTAe" +
					"ICAfYUJJQi4gVEVDTklDTyBTQ0lFTlQuLUZBUk1BQ0lBHzJUU0FGMB4gIB9hRElQLiBMRVRULiBF" +
					"IENJVi5BTkdMTy1HRVJNQU5JQ0hFHzJUU0FKQx4gIB9hQklCLiBTQ0lFTlpFIEQuIEZPUk1BWklP" +
					"TkUfMlRTQU0wHiAgH2FEVUNTUlMtR0lORUMuIEUgT1NURVRSSUNJQR8yVFNBTkweIDAfYURJUC4g" +
					"TEVUVC4gRSBDSVYuQU5HTE8tR0VSTUFOSUNIRR9jMSB2LiwgOC4gcmlzdC4gMTk5NyAfZEpDSU4v" +
					"MDIuYSAgIC8gICAgICAgICAgIDAwMDUgICAgICAgIB9lSkNKQ0EwMDAwMTg4Mjc1ICAgICAgICAg" +
					"ICAgICAgICAgICAgICAgICAgIDEgdi4sIDguIHJpc3QuIDE5OTcgICAgICAgICAgICAgICAgICAg" +
					"ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg" +
					"ICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg" +
					"ICAgICAgICAeHQ==";
		String rec=new String(Base64.decode(n1),"utf-8");
		String r=rebuild(rec,"sebina");
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


	
	private String rebuild(String record, String type) {
		RecordInterface ma=RecordFactory.buildRecord(0, record, type, 0);
		String r=ma.toString();
		ma.destroy();
		return r;
	}
}
