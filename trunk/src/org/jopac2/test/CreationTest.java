package org.jopac2.test;


import junit.framework.TestCase;

import org.jopac2.jbal.RecordFactory;
import org.jopac2.jbal.RecordInterface;
import org.junit.After;
import org.junit.Before;

public class CreationTest extends TestCase {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
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
