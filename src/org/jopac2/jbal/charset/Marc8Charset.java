package org.jopac2.jbal.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class Marc8Charset extends java.nio.charset.Charset {
	protected Marc8Charset(String canonicalName, String[] aliases) {
		super(canonicalName, aliases);
	}
	
	public static byte[] marc8toUtf8(byte[] in) {
		
		for(int i=0;i<in.length;i++) {
			switch(0xff & in[i]) {
			case 0xc1:
				in[i++]=(byte)0xc3;
				switch(0xff & in[i]) {
				case 0x15:in[i]=(byte)0xac;break;	// i grave
				case 0x65:in[i]=(byte)0xa8;break;	// e grave
				case 0x4f:in[i]=(byte)0x92;break;	// O grave
				case 0x41:in[i]=(byte)0x80;break;	// A grave
				case 0x49:in[i]=(byte)0x8c;break;	// I grave
				case 0x45:in[i]=(byte)0x88;break;	// E grave
				case 0x61:in[i]=(byte)0xa0;break;	// a grave
				case 0x6f:in[i]=(byte)0xb2;break;	// o grave
				case 0x75:in[i]=(byte)0xb9;break;	// u grave
				case 0x55:in[i]=(byte)0x99;break;	// U grave
				}
				break;
			case 0xc2:
				in[i++]=(byte)0xc3;
				switch(0xff & in[i]) {
				case 0x61:in[i]=(byte)0xa1;break;	// a acuta
				case 0x41:in[i]=(byte)0x81;break;	// A acuta
				case 0x45:in[i]=(byte)0x89;break;	// E acuta
				case 0x65:in[i]=(byte)0xa9;break;	// e acuta
				case 0x49:in[i]=(byte)0x8d;break;	// I acuta
				case 0x4f:in[i]=(byte)0x93;break;	// O acuta
				case 0x15:in[i]=(byte)0xad;break;	// i acuta
				case 0x75:in[i]=(byte)0xba;break;	// u acuta
				case 0x6f:in[i]=(byte)0xb3;break;	// o acuta
				case 0x59:in[i]=(byte)0x9d;break;	// Y acuta
				case 0x55:in[i]=(byte)0x9a;break;	// U acuta
				}
				break;
			case 0xc3:
				in[i++]=(byte)0xc3;
				switch(0xff & in[i]) {
				case 0x61:in[i]=(byte)0xa2;break;	// a ^
				case 0x41:in[i]=(byte)0x82;break;	// A ^
				case 0x45:in[i]=(byte)0x8a;break;	// E ^
				case 0x65:in[i]=(byte)0xaa;break;	// e ^
				case 0x49:in[i]=(byte)0x8e;break;	// I ^
				case 0x4f:in[i]=(byte)0x94;break;	// O ^
				case 0x69:in[i]=(byte)0xae;break;	// i ^
				case 0x75:in[i]=(byte)0xbb;break;	// u ^
				case 0x6f:in[i]=(byte)0xb4;break;	// o ^
				case 0x55:in[i]=(byte)0x9b;break;	// U ^
				}
				break;
			case 0xc4:
				in[i++]=(byte)0xc3;
				switch(0xff & in[i]) {
				case 0x61:in[i]=(byte)0xa3;break;	// a ~
				case 0x41:in[i]=(byte)0x83;break;	// A ~
				case 0x4f:in[i]=(byte)0x95;break;	// O ~
				case 0x6f:in[i]=(byte)0xb5;break;	// o ~
				case 0x6e:in[i]=(byte)0xb1;break;	// n ~
				case 0x4e:in[i]=(byte)0x91;break;	// N ~
				}
				break;
			case 0xc9:
				in[i++]=(byte)0xc3;
				switch(0xff & in[i]) {
				case 0x55:in[i]=(byte)0x9c;break;	// U :
				case 0x75:in[i]=(byte)0xbc;break;	// u :
				case 0x41:in[i]=(byte)0x84;break;	// A :
				case 0x61:in[i]=(byte)0xa4;break;	// a :
				case 0x4f:in[i]=(byte)0x96;break;	// O :
				case 0x6f:in[i]=(byte)0xb6;break;	// o :
				case 0x49:in[i]=(byte)0x8f;break;	// I :
				case 0x69:in[i]=(byte)0xaf;break;	// i :
				case 0x45:in[i]=(byte)0x8b;break;	// E :
				case 0x65:in[i]=(byte)0xab;break;	// e :
				}
				break;
			case 0xd0:
				in[i++]=(byte)0xc3;
				switch(0xff & in[i]) {
				case 0x63:in[i]=(byte)0xa7;break;	// c ,
				case 0x43:in[i]=(byte)0x87;break;	// C ,
				}
				break;
			case 0x86:
				break;
			default:break;
			}
			i++;
		}
		return in;
	}

	@Override
	public boolean contains(Charset arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CharsetDecoder newDecoder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CharsetEncoder newEncoder() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 * Carattere

Codifica
	marc8	utf-8


å	86		c3 a5
Å	8f		c3 85
¿	a8		c2 bf
®	a9		c2 ae
½	ab		c2 bd
¤	cf		c2 b0
Æ	e1		c3 86
Ð	e2		c3 90
þ	e7		c3 be
Þ	e8		c3 9e
Ø	e9		c3 98
æ	f1		c3 a6
¾	f3		c2 be
ø	f9		c3 b8

ì	c1 15	c3 ac
è	c1 65	c3 a8
Ò	c1 4f	c3 92
À	c1 41	c3 80
Ì	c1 49	c3 8c
È	c1 45	c3 88		
à	c1 61	c3 a0
ò	c1 6f	c3 b2
ù	c1 75	c3 b9
Ù	c1 55	c3 99

á	c2 61	c3 a1
Á	c2 41	c3 81
É	c2 45	c3 89
é	c2 65	c3 a9
Í	c2 49	c3 8d
Ó	c2 4f	c3 93
í	c2 15	c3 ad
ú	c2 75	c3 ba
ó	c2 6f	c3 b3
Ý	c2 59	c3 9d
ý	c2 79	c3 bd
Ú	c2 55	c3 9a

â	c3 61	c3 a2
ô	c3 6f	c3 b4
û	c3 75	c3 bb
Â	c3 41	c3 82
Î	c3 49	c3 8e
î	c3 69	c3 ae
ê	c3 65	c3 aa
Ê	c3 45	c3 8a
Ô	c3 4f	c3 94
Û	c3 55	c3 9b

Ã	c4 41	c3 83
õ	c4 6f	c3 b5
ñ	c4 6e	c3 b1
ã	c4 61	c3 a3
Õ	c4 4f	c3 95
Ñ	c4 4e	c3 91
ã	c4 61	c3 a3

ü	c9 75	c3 bc
Ä	c9 41	c3 84
ö	c9 6f	c3 b6
Ë	c9 45	c3 8b
Ï	c9 49	c3 8f
ä	c9 61	c3 a4
ë	c9 65	c3 ab
ï	c9 69	c3 af
Ö	c9 4f	c3 96
Ü	c9 55	c3 9c

ç	d0 63	c3 a7
Ç	d0 43	c3 87
	 */
}
