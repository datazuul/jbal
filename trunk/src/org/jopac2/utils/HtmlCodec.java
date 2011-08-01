package org.jopac2.utils;
/*******************************************************************************
*
*  JOpac2 (C) 2002-2007 JOpac2 project
*
*     This file is part of JOpac2. http://jopac2.sourceforge.net
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
*******************************************************************************/
import java.util.Hashtable;

public class HtmlCodec {

	private static Hashtable<String,String> specialChars = new Hashtable<String,String>();
	
	static{
        specialChars.put("nbsp",     "\u00a0"); // no-break space = non-breaking space, U+00A0 ISOnum
        specialChars.put("iexcl",    "\u00a1"); // inverted exclamation mark, U+00A1 ISOnum
        specialChars.put("cent",     "\u00a2"); // cent sign, U+00A2 ISOnum
        specialChars.put("pound",    "\u00a3"); // pound sign, U+00A3 ISOnum
        specialChars.put("curren",   "\u00a4"); // currency sign, U+00A4 ISOnum
        specialChars.put("yen",      "\u00a5"); // yen sign = yuan sign, U+00A5 ISOnum
        specialChars.put("brvbar",   "\u00a6"); // broken bar = broken vertical bar, U+00A6 ISOnum
        specialChars.put("sect",     "\u00a7"); // section sign, U+00A7 ISOnum
        specialChars.put("uml",      "\u00a8"); // diaeresis = spacing diaeresis, U+00A8 ISOdia
        specialChars.put("copy",     "\u00a9"); // copyright sign, U+00A9 ISOnum
        specialChars.put("ordf",     "\u00aa"); // feminine ordinal indicator, U+00AA ISOnum
        specialChars.put("laquo",    "\u00ab"); // left-pointing double angle quotation mark = left pointing guillemet, U+00AB ISOnum
        specialChars.put("not",      "\u00ac"); // not sign, U+00AC ISOnum
        specialChars.put("shy",      "\u00ad"); // soft hyphen = discretionary hyphen, U+00AD ISOnum
        specialChars.put("reg",      "\u00ae"); // registered sign = registered trade mark sign, U+00AE ISOnum
        specialChars.put("macr",     "\u00af"); // macron = spacing macron = overline = APL overbar, U+00AF ISOdia
        specialChars.put("deg",      "\u00b0"); // degree sign, U+00B0 ISOnum
        specialChars.put("plusmn",   "\u00b1"); // plus-minus sign = plus-or-minus sign, U+00B1 ISOnum
        specialChars.put("sup2",     "\u00b2"); // superscript two = superscript digit two = squared, U+00B2 ISOnum
        specialChars.put("sup3",     "\u00b3"); // superscript three = superscript digit three = cubed, U+00B3 ISOnum
        specialChars.put("acute",    "\u00b4"); // acute accent = spacing acute, U+00B4 ISOdia
        specialChars.put("micro",    "\u00b5"); // micro sign, U+00B5 ISOnum
        specialChars.put("para",     "\u00b6"); // pilcrow sign = paragraph sign, U+00B6 ISOnum
        specialChars.put("middot",   "\u00b7"); // middle dot = Georgian comma = Greek middle dot, U+00B7 ISOnum
        specialChars.put("cedil",    "\u00b8"); // cedilla = spacing cedilla, U+00B8 ISOdia
        specialChars.put("sup1",     "\u00b9"); // superscript one = superscript digit one, U+00B9 ISOnum
        specialChars.put("ordm",     "\u00ba"); // masculine ordinal indicator, U+00BA ISOnum
        specialChars.put("raquo",    "\u00bb"); // right-pointing double angle quotation mark = right pointing guillemet, U+00BB ISOnum
        specialChars.put("frac14",   "\u00bc"); // vulgar fraction one quarter = fraction one quarter, U+00BC ISOnum
        specialChars.put("frac12",   "\u00bd"); // vulgar fraction one half = fraction one half, U+00BD ISOnum
        specialChars.put("frac34",   "\u00be"); // vulgar fraction three quarters = fraction three quarters, U+00BE ISOnum
        specialChars.put("iquest",   "\u00bf"); // inverted question mark = turned question mark, U+00BF ISOnum
        specialChars.put("Agrave",   "\u00c0"); // latin capital letter A with grave = latin capital letter A grave, U+00C0 ISOlat1
        specialChars.put("Aacute",   "\u00c1"); // latin capital letter A with acute, U+00C1 ISOlat1
        specialChars.put("Acirc",    "\u00c2"); // latin capital letter A with circumflex, U+00C2 ISOlat1
        specialChars.put("Atilde",   "\u00c3"); // latin capital letter A with tilde, U+00C3 ISOlat1
        specialChars.put("Auml",     "\u00c4"); // latin capital letter A with diaeresis, U+00C4 ISOlat1
        specialChars.put("Aring",    "\u00c5"); // latin capital letter A with ring above = latin capital letter A ring, U+00C5 ISOlat1
        specialChars.put("AElig",    "\u00c6"); // latin capital letter AE = latin capital ligature AE, U+00C6 ISOlat1
        specialChars.put("Ccedil",   "\u00c7"); // latin capital letter C with cedilla, U+00C7 ISOlat1
        specialChars.put("Egrave",   "\u00c8"); // latin capital letter E with grave, U+00C8 ISOlat1
        specialChars.put("Eacute",   "\u00c9"); // latin capital letter E with acute, U+00C9 ISOlat1
        specialChars.put("Ecirc",    "\u00ca"); // latin capital letter E with circumflex, U+00CA ISOlat1
        specialChars.put("Euml",     "\u00cb"); // latin capital letter E with diaeresis, U+00CB ISOlat1
        specialChars.put("Igrave",   "\u00cc"); // latin capital letter I with grave, U+00CC ISOlat1
        specialChars.put("Iacute",   "\u00cd"); // latin capital letter I with acute, U+00CD ISOlat1
        specialChars.put("Icirc",    "\u00ce"); // latin capital letter I with circumflex, U+00CE ISOlat1
        specialChars.put("Iuml",     "\u00cf"); // latin capital letter I with diaeresis, U+00CF ISOlat1
        specialChars.put("ETH",      "\u00d0"); // latin capital letter ETH, U+00D0 ISOlat1
        specialChars.put("Ntilde",   "\u00d1"); // latin capital letter N with tilde, U+00D1 ISOlat1
        specialChars.put("Ograve",   "\u00d2"); // latin capital letter O with grave, U+00D2 ISOlat1
        specialChars.put("Oacute",   "\u00d3"); // latin capital letter O with acute, U+00D3 ISOlat1
        specialChars.put("Ocirc",    "\u00d4"); // latin capital letter O with circumflex, U+00D4 ISOlat1
        specialChars.put("Otilde",   "\u00d5"); // latin capital letter O with tilde, U+00D5 ISOlat1
        specialChars.put("Ouml",     "\u00d6"); // latin capital letter O with diaeresis, U+00D6 ISOlat1
        specialChars.put("times",    "\u00d7"); // multiplication sign, U+00D7 ISOnum
        specialChars.put("Oslash",   "\u00d8"); // latin capital letter O with stroke = latin capital letter O slash, U+00D8 ISOlat1
        specialChars.put("Ugrave",   "\u00d9"); // latin capital letter U with grave, U+00D9 ISOlat1
        specialChars.put("Uacute",   "\u00da"); // latin capital letter U with acute, U+00DA ISOlat1
        specialChars.put("Ucirc",    "\u00db"); // latin capital letter U with circumflex, U+00DB ISOlat1
        specialChars.put("Uuml",     "\u00dc"); // latin capital letter U with diaeresis, U+00DC ISOlat1
        specialChars.put("Yacute",   "\u00dd"); // latin capital letter Y with acute, U+00DD ISOlat1
        specialChars.put("THORN",    "\u00de"); // latin capital letter THORN, U+00DE ISOlat1
        specialChars.put("szlig",    "\u00df"); // latin small letter sharp s = ess-zed, U+00DF ISOlat1
        specialChars.put("agrave",   "\u00e0"); // latin small letter a with grave = latin small letter a grave, U+00E0 ISOlat1
        specialChars.put("aacute",   "\u00e1"); // latin small letter a with acute, U+00E1 ISOlat1
        specialChars.put("acirc",    "\u00e2"); // latin small letter a with circumflex, U+00E2 ISOlat1
        specialChars.put("atilde",   "\u00e3"); // latin small letter a with tilde, U+00E3 ISOlat1
        specialChars.put("auml",     "\u00e4"); // latin small letter a with diaeresis, U+00E4 ISOlat1
        specialChars.put("aring",    "\u00e5"); // latin small letter a with ring above = latin small letter a ring, U+00E5 ISOlat1
        specialChars.put("aelig",    "\u00e6"); // latin small letter ae = latin small ligature ae, U+00E6 ISOlat1
        specialChars.put("ccedil",   "\u00e7"); // latin small letter c with cedilla, U+00E7 ISOlat1
        specialChars.put("egrave",   "\u00e8"); // latin small letter e with grave, U+00E8 ISOlat1
        specialChars.put("eacute",   "\u00e9"); // latin small letter e with acute, U+00E9 ISOlat1
        specialChars.put("ecirc",    "\u00ea"); // latin small letter e with circumflex, U+00EA ISOlat1
        specialChars.put("euml",     "\u00eb"); // latin small letter e with diaeresis, U+00EB ISOlat1        
        specialChars.put("igrave",   "\u00ec"); // latin small letter i with grave, U+00EC ISOlat1
        specialChars.put("iacute",   "\u00ed"); // latin small letter i with acute, U+00ED ISOlat1
        specialChars.put("icirc",    "\u00ee"); // latin small letter i with circumflex, U+00EE ISOlat1
        specialChars.put("iuml",     "\u00ef"); // latin small letter i with diaeresis, U+00EF ISOlat1
        specialChars.put("eth",      "\u00f0"); // latin small letter eth, U+00F0 ISOlat1
        specialChars.put("ntilde",   "\u00f1"); // latin small letter n with tilde, U+00F1 ISOlat1
        specialChars.put("ograve",   "\u00f2"); // latin small letter o with grave, U+00F2 ISOlat1
        specialChars.put("oacute",   "\u00f3"); // latin small letter o with acute, U+00F3 ISOlat1
        specialChars.put("ocirc",    "\u00f4"); // latin small letter o with circumflex, U+00F4 ISOlat1
        specialChars.put("otilde",   "\u00f5"); // latin small letter o with tilde, U+00F5 ISOlat1
        specialChars.put("ouml",     "\u00f6"); // latin small letter o with diaeresis, U+00F6 ISOlat1
        specialChars.put("divide",   "\u00f7"); // division sign, U+00F7 ISOnum
        specialChars.put("oslash",   "\u00f8"); // latin small letter o with stroke, = latin small letter o slash, U+00F8 ISOlat1
        specialChars.put("ugrave",   "\u00f9"); // latin small letter u with grave, U+00F9 ISOlat1
        specialChars.put("uacute",   "\u00fa"); // latin small letter u with acute, U+00FA ISOlat1
        specialChars.put("ucirc",    "\u00fb"); // latin small letter u with circumflex, U+00FB ISOlat1
        specialChars.put("uuml",     "\u00fc"); // latin small letter u with diaeresis, U+00FC ISOlat1
        specialChars.put("yacute",   "\u00fd"); // latin small letter y with acute, U+00FD ISOlat1
        specialChars.put("thorn",    "\u00fe"); // latin small letter thorn, U+00FE ISOlat1
        specialChars.put("yuml",     "\u00ff"); // latin small letter y with diaeresis, U+00FF ISOlat1
        // Mathematical, Greek and Symbolic characters for HTML
        // Character entity set. Typical invocation:
        // <!ENTITY % HTMLsymbol PUBLIC
        // "-//W3C//ENTITIES Symbols//EN//HTML">
        // %HTMLsymbol;
        // Portions © International Organization for Standardization 1986:
        // Permission to copy in any form is granted for use with
        // conforming SGML systems and applications as defined in
        // ISO 8879, provided this notice is included in all copies.
        // Relevant ISO entity set is given unless names are newly introduced.
        // New names (i.e., not in ISO 8879 list) do not clash with any
        // existing ISO 8879 entity names. ISO 10646 character numbers
        // are given for each character, in hex. CDATA values are decimal
        // conversions of the ISO 10646 values and refer to the document
        // character set. Names are ISO 10646 names.
        // Latin Extended-B
        specialChars.put("fnof",     "\u0192"); // latin small f with hook = function = florin, U+0192 ISOtech
        // Greek
        specialChars.put("Alpha",    "\u0391"); // greek capital letter alpha, U+0391
        specialChars.put("Beta",     "\u0392"); // greek capital letter beta, U+0392
        specialChars.put("Gamma",    "\u0393"); // greek capital letter gamma, U+0393 ISOgrk3
        specialChars.put("Delta",    "\u0394"); // greek capital letter delta, U+0394 ISOgrk3
        specialChars.put("Epsilon",  "\u0395"); // greek capital letter epsilon, U+0395
        specialChars.put("Zeta",     "\u0396"); // greek capital letter zeta, U+0396
        specialChars.put("Eta",      "\u0397"); // greek capital letter eta, U+0397
        specialChars.put("Theta",    "\u0398"); // greek capital letter theta, U+0398 ISOgrk3
        specialChars.put("Iota",     "\u0399"); // greek capital letter iota, U+0399
        specialChars.put("Kappa",    "\u039a"); // greek capital letter kappa, U+039A
        specialChars.put("Lambda",   "\u039b"); // greek capital letter lambda, U+039B ISOgrk3
        specialChars.put("Mu",       "\u039c"); // greek capital letter mu, U+039C
        specialChars.put("Nu",       "\u039d"); // greek capital letter nu, U+039D
        specialChars.put("Xi",       "\u039e"); // greek capital letter xi, U+039E ISOgrk3
        specialChars.put("Omicron",  "\u039f"); // greek capital letter omicron, U+039F
        specialChars.put("Pi",       "\u03a0"); // greek capital letter pi, U+03A0 ISOgrk3
        specialChars.put("Rho",      "\u03a1"); // greek capital letter rho, U+03A1
        // there is no Sigmaf, and no U+03A2 character either
        specialChars.put("Sigma",    "\u03a3"); // greek capital letter sigma, U+03A3 ISOgrk3
        specialChars.put("Tau",      "\u03a4"); // greek capital letter tau, U+03A4
        specialChars.put("Upsilon",  "\u03a5"); // greek capital letter upsilon, U+03A5 ISOgrk3
        specialChars.put("Phi",      "\u03a6"); // greek capital letter phi, U+03A6 ISOgrk3
        specialChars.put("Chi",      "\u03a7"); // greek capital letter chi, U+03A7
        specialChars.put("Psi",      "\u03a8"); // greek capital letter psi, U+03A8 ISOgrk3
        specialChars.put("Omega",    "\u03a9"); // greek capital letter omega, U+03A9 ISOgrk3
        specialChars.put("alpha",    "\u03b1"); // greek small letter alpha, U+03B1 ISOgrk3
        specialChars.put("beta",     "\u03b2"); // greek small letter beta, U+03B2 ISOgrk3
        specialChars.put("gamma",    "\u03b3"); // greek small letter gamma, U+03B3 ISOgrk3
        specialChars.put("delta",    "\u03b4"); // greek small letter delta, U+03B4 ISOgrk3
        specialChars.put("epsilon",  "\u03b5"); // greek small letter epsilon, U+03B5 ISOgrk3
        specialChars.put("zeta",     "\u03b6"); // greek small letter zeta, U+03B6 ISOgrk3
        specialChars.put("eta",      "\u03b7"); // greek small letter eta, U+03B7 ISOgrk3
        specialChars.put("theta",    "\u03b8"); // greek small letter theta, U+03B8 ISOgrk3
        specialChars.put("iota",     "\u03b9"); // greek small letter iota, U+03B9 ISOgrk3
        specialChars.put("kappa",    "\u03ba"); // greek small letter kappa, U+03BA ISOgrk3
        specialChars.put("lambda",   "\u03bb"); // greek small letter lambda, U+03BB ISOgrk3
        specialChars.put("mu",       "\u03bc"); // greek small letter mu, U+03BC ISOgrk3
        specialChars.put("nu",       "\u03bd"); // greek small letter nu, U+03BD ISOgrk3
        specialChars.put("xi",       "\u03be"); // greek small letter xi, U+03BE ISOgrk3
        specialChars.put("omicron",  "\u03bf"); // greek small letter omicron, U+03BF NEW
        specialChars.put("pi",       "\u03c0"); // greek small letter pi, U+03C0 ISOgrk3
        specialChars.put("rho",      "\u03c1"); // greek small letter rho, U+03C1 ISOgrk3
        specialChars.put("sigmaf",   "\u03c2"); // greek small letter final sigma, U+03C2 ISOgrk3
        specialChars.put("sigma",    "\u03c3"); // greek small letter sigma, U+03C3 ISOgrk3
        specialChars.put("tau",      "\u03c4"); // greek small letter tau, U+03C4 ISOgrk3
        specialChars.put("upsilon",  "\u03c5"); // greek small letter upsilon, U+03C5 ISOgrk3
        specialChars.put("phi",      "\u03c6"); // greek small letter phi, U+03C6 ISOgrk3
        specialChars.put("chi",      "\u03c7"); // greek small letter chi, U+03C7 ISOgrk3
        specialChars.put("psi",      "\u03c8"); // greek small letter psi, U+03C8 ISOgrk3
        specialChars.put("omega",    "\u03c9"); // greek small letter omega, U+03C9 ISOgrk3
        specialChars.put("thetasym", "\u03d1"); // greek small letter theta symbol, U+03D1 NEW
        specialChars.put("upsih",    "\u03d2"); // greek upsilon with hook symbol, U+03D2 NEW
        specialChars.put("piv",      "\u03d6"); // greek pi symbol, U+03D6 ISOgrk3
        // General Punctuation
        specialChars.put("bull",     "\u2022"); // bullet = black small circle, U+2022 ISOpub
        // bullet is NOT the same as bullet operator, U+2219
        specialChars.put("hellip",   "\u2026"); // horizontal ellipsis = three dot leader, U+2026 ISOpub
        specialChars.put("prime",    "\u2032"); // prime = minutes = feet, U+2032 ISOtech
        specialChars.put("Prime",    "\u2033"); // double prime = seconds = inches, U+2033 ISOtech
        specialChars.put("oline",    "\u203e"); // overline = spacing overscore, U+203E NEW
        specialChars.put("frasl",    "\u2044"); // fraction slash, U+2044 NEW
        // Letterlike Symbols
        specialChars.put("weierp",   "\u2118"); // script capital P = power set = Weierstrass p, U+2118 ISOamso
        specialChars.put("image",    "\u2111"); // blackletter capital I = imaginary part, U+2111 ISOamso
        specialChars.put("real",     "\u211c"); // blackletter capital R = real part symbol, U+211C ISOamso
        specialChars.put("trade",    "\u2122"); // trade mark sign, U+2122 ISOnum
        specialChars.put("alefsym",  "\u2135"); // alef symbol = first transfinite cardinal, U+2135 NEW
        // alef symbol is NOT the same as hebrew letter alef,
        // U+05D0 although the same glyph could be used to depict both characters
        // Arrows
        specialChars.put("larr",     "\u2190"); // leftwards arrow, U+2190 ISOnum
        specialChars.put("uarr",     "\u2191"); // upwards arrow, U+2191 ISOnum
        specialChars.put("rarr",     "\u2192"); // rightwards arrow, U+2192 ISOnum
        specialChars.put("darr",     "\u2193"); // downwards arrow, U+2193 ISOnum
        specialChars.put("harr",     "\u2194"); // left right arrow, U+2194 ISOamsa
        specialChars.put("crarr",    "\u21b5"); // downwards arrow with corner leftwards = carriage return, U+21B5 NEW
        specialChars.put("lArr",     "\u21d0"); // leftwards double arrow, U+21D0 ISOtech
        // ISO 10646 does not say that lArr is the same as the 'is implied by' arrow
        // but also does not have any other character for that function. So ? lArr can
        // be used for 'is implied by' as ISOtech suggests
        specialChars.put("uArr",     "\u21d1"); // upwards double arrow, U+21D1 ISOamsa
        specialChars.put("rArr",     "\u21d2"); // rightwards double arrow, U+21D2 ISOtech
        // ISO 10646 does not say this is the 'implies' character but does not have 
        // another character with this function so ?
        // rArr can be used for 'implies' as ISOtech suggests
        specialChars.put("dArr",     "\u21d3"); // downwards double arrow, U+21D3 ISOamsa
        specialChars.put("hArr",     "\u21d4"); // left right double arrow, U+21D4 ISOamsa
        // Mathematical Operators
        specialChars.put("forall",   "\u2200"); // for all, U+2200 ISOtech
        specialChars.put("part",     "\u2202"); // partial differential, U+2202 ISOtech
        specialChars.put("exist",    "\u2203"); // there exists, U+2203 ISOtech
        specialChars.put("empty",    "\u2205"); // empty set = null set = diameter, U+2205 ISOamso
        specialChars.put("nabla",    "\u2207"); // nabla = backward difference, U+2207 ISOtech
        specialChars.put("isin",     "\u2208"); // element of, U+2208 ISOtech
        specialChars.put("notin",    "\u2209"); // not an element of, U+2209 ISOtech
        specialChars.put("ni",       "\u220b"); // contains as member, U+220B ISOtech
        // should there be a more memorable name than 'ni'?
        specialChars.put("prod",     "\u220f"); // n-ary product = product sign, U+220F ISOamsb
        // prod is NOT the same character as U+03A0 'greek capital letter pi' though
        // the same glyph might be used for both
        specialChars.put("sum",      "\u2211"); // n-ary sumation, U+2211 ISOamsb
        // sum is NOT the same character as U+03A3 'greek capital letter sigma'
        // though the same glyph might be used for both
        specialChars.put("minus",    "\u2212"); // minus sign, U+2212 ISOtech
        specialChars.put("lowast",   "\u2217"); // asterisk operator, U+2217 ISOtech
        specialChars.put("radic",    "\u221a"); // square root = radical sign, U+221A ISOtech
        specialChars.put("prop",     "\u221d"); // proportional to, U+221D ISOtech
        
        
        
        
        specialChars.put("infin",    "\u221e"); // infinity, U+221E ISOtech
        specialChars.put("ang",      "\u2220"); // angle, U+2220 ISOamso
        specialChars.put("and",      "\u2227"); // logical and = wedge, U+2227 ISOtech
        specialChars.put("or",       "\u2228"); // logical or = vee, U+2228 ISOtech
        specialChars.put("cap",      "\u2229"); // intersection = cap, U+2229 ISOtech
        specialChars.put("cup",      "\u222a"); // union = cup, U+222A ISOtech
        specialChars.put("int",      "\u222b"); // integral, U+222B ISOtech
        specialChars.put("there4",   "\u2234"); // therefore, U+2234 ISOtech
        specialChars.put("sim",      "\u223c"); // tilde operator = varies with = similar to, U+223C ISOtech
        // tilde operator is NOT the same character as the tilde, U+007E,
        // although the same glyph might be used to represent both
        specialChars.put("cong",     "\u2245"); // approximately equal to, U+2245 ISOtech
        specialChars.put("asymp",    "\u2248"); // almost equal to = asymptotic to, U+2248 ISOamsr
        specialChars.put("ne",       "\u2260"); // not equal to, U+2260 ISOtech
        specialChars.put("equiv",    "\u2261"); // identical to, U+2261 ISOtech
        specialChars.put("le",       "\u2264"); // less-than or equal to, U+2264 ISOtech
        specialChars.put("ge",       "\u2265"); // greater-than or equal to, U+2265 ISOtech
        specialChars.put("sub",      "\u2282"); // subset of, U+2282 ISOtech
        specialChars.put("sup",      "\u2283"); // superset of, U+2283 ISOtech
        // note that nsup, 'not a superset of, U+2283' is not covered by the Symbol 
        // font encoding and is not included. Should it be, for symmetry?
        // It is in ISOamsn
        specialChars.put("nsub",     "\u2284"); // not a subset of, U+2284 ISOamsn
        specialChars.put("sube",     "\u2286"); // subset of or equal to, U+2286 ISOtech
        specialChars.put("supe",     "\u2287"); // superset of or equal to, U+2287 ISOtech
        specialChars.put("oplus",    "\u2295"); // circled plus = direct sum, U+2295 ISOamsb
        specialChars.put("otimes",   "\u2297"); // circled times = vector product, U+2297 ISOamsb
        specialChars.put("perp",     "\u22a5"); // up tack = orthogonal to = perpendicular, U+22A5 ISOtech
        specialChars.put("sdot",     "\u22c5"); // dot operator, U+22C5 ISOamsb
        // dot operator is NOT the same character as U+00B7 middle dot
        // Miscellaneous Technical
        specialChars.put("lceil",    "\u2308"); // left ceiling = apl upstile, U+2308 ISOamsc
        specialChars.put("rceil",    "\u2309"); // right ceiling, U+2309 ISOamsc
        specialChars.put("lfloor",   "\u230a"); // left floor = apl downstile, U+230A ISOamsc
        specialChars.put("rfloor",   "\u230b"); // right floor, U+230B ISOamsc
        specialChars.put("lang",     "\u2329"); // left-pointing angle bracket = bra, U+2329 ISOtech
        // lang is NOT the same character as U+003C 'less than' 
        // or U+2039 'single left-pointing angle quotation mark'
        specialChars.put("rang",     "\u232a"); // right-pointing angle bracket = ket, U+232A ISOtech
        // rang is NOT the same character as U+003E 'greater than' 
        // or U+203A 'single right-pointing angle quotation mark'
        // Geometric Shapes
        specialChars.put("loz",      "\u25ca"); // lozenge, U+25CA ISOpub
        // Miscellaneous Symbols
        specialChars.put("spades",   "\u2660"); // black spade suit, U+2660 ISOpub
        // black here seems to mean filled as opposed to hollow
        specialChars.put("clubs",    "\u2663"); // black club suit = shamrock, U+2663 ISOpub
        specialChars.put("hearts",   "\u2665"); // black heart suit = valentine, U+2665 ISOpub
        specialChars.put("diams",    "\u2666"); // black diamond suit, U+2666 ISOpub
        // Special characters for HTML
        // Character entity set. Typical invocation:
        // <!ENTITY % HTMLspecial PUBLIC
        // "-//W3C//ENTITIES Special//EN//HTML">
        // TMLspecial;
        // Portions © International Organization for Standardization 1986:
        // Permission to copy in any form is granted for use with
        // conforming SGML systems and applications as defined in
        // ISO 8879, provided this notice is included in all copies.
        // Relevant ISO entity set is given unless names are newly introduced.
        // New names (i.e., not in ISO 8879 list) do not clash with any
        // existing ISO 8879 entity names. ISO 10646 character numbers
        // are given for each character, in hex. CDATA values are decimal
        // conversions of the ISO 10646 values and refer to the document
        // character set. Names are ISO 10646 names.
        // C0 Controls and Basic Latin
        specialChars.put("quot",     "\\u0022"); // quotation mark = APL quote, U+0022 ISOnum
        specialChars.put("amp",      "\u0026"); // ampersand, U+0026 ISOnum
        specialChars.put("lt",       "\u003c"); // less-than sign, U+003C ISOnum
        specialChars.put("gt",       "\u003e"); // greater-than sign, U+003E ISOnum
        // Latin Extended-A
        specialChars.put("OElig",    "\u0152"); // latin capital ligature OE, U+0152 ISOlat2
        specialChars.put("oelig",    "\u0153"); // latin small ligature oe, U+0153 ISOlat2
        // ligature is a misnomer, this is a separate character in some languages
        specialChars.put("Scaron",   "\u0160"); // latin capital letter S with caron, U+0160 ISOlat2
        specialChars.put("scaron",   "\u0161"); // latin small letter s with caron, U+0161 ISOlat2
        specialChars.put("Yuml",     "\u0178"); // latin capital letter Y with diaeresis, U+0178 ISOlat2
        // Spacing Modifier Letters
        specialChars.put("circ",     "\u02c6"); // modifier letter circumflex accent, U+02C6 ISOpub
        specialChars.put("tilde",    "\u02dc"); // small tilde, U+02DC ISOdia
        // General Punctuation
        specialChars.put("ensp",     "\u2002"); // en space, U+2002 ISOpub
        specialChars.put("emsp",     "\u2003"); // em space, U+2003 ISOpub
        specialChars.put("thinsp",   "\u2009"); // thin space, U+2009 ISOpub
        specialChars.put("zwnj",     "\u200c"); // zero width non-joiner, U+200C NEW RFC 2070
        specialChars.put("zwj",      "\u200d"); // zero width joiner, U+200D NEW RFC 2070
        specialChars.put("lrm",      "\u200e"); // left-to-right mark, U+200E NEW RFC 2070
        specialChars.put("rlm",      "\u200f"); // right-to-left mark, U+200F NEW RFC 2070
        specialChars.put("ndash",    "\u2013"); // en dash, U+2013 ISOpub
        specialChars.put("mdash",    "\u2014"); // em dash, U+2014 ISOpub
        specialChars.put("lsquo",    "\u2018"); // left single quotation mark, U+2018 ISOnum
        specialChars.put("rsquo",    "\u2019"); // right single quotation mark, U+2019 ISOnum
        specialChars.put("sbquo",    "\u201a"); // single low-9 quotation mark, U+201A NEW
        specialChars.put("ldquo",    "\u201c"); // left double quotation mark, U+201C ISOnum
        specialChars.put("rdquo",    "\u201d"); // right double quotation mark, U+201D ISOnum
        specialChars.put("bdquo",    "\u201e"); // double low-9 quotation mark, U+201E NEW
        specialChars.put("dagger",   "\u2020"); // dagger, U+2020 ISOpub
        specialChars.put("Dagger",   "\u2021"); // double dagger, U+2021 ISOpub
        specialChars.put("permil",   "\u2030"); // per mille sign, U+2030 ISOtech
        specialChars.put("lsaquo",   "\u2039"); // single left-pointing angle quotation mark, U+2039 ISO proposed
        // lsaquo is proposed but not yet ISO standardized
        specialChars.put("rsaquo",   "\u203a"); // single right-pointing angle quotation mark, U+203A ISO proposed
        // rsaquo is proposed but not yet ISO standardized
        specialChars.put("euro",     "\u20ac"); // euro sign, U+20AC NEW;
	}
	
	public static String decode(String s){
		
		int refIndex = s.indexOf("&");
		while(refIndex > -1){
			int endRefIndex = s.indexOf(";",refIndex);
			if(endRefIndex > -1){
				String special = s.substring(refIndex,endRefIndex);
                String specialChar = specialChars.get(special.substring(1));
                if(specialChar==null && special.matches("&#\\d+")){
                	int code = Integer.parseInt(special.substring(2));
                	specialChar = String.valueOf((char)code);
                }
                if(specialChar!=null){
                    s = s.replaceAll(special+";", specialChar);
                }
				//System.out.println();
			}
			refIndex = s.indexOf("&", refIndex+1);
		}
		
		return s;
	}
	

}