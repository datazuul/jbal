package org.jopac2.test;

import org.jopac2.utils.BookSignature;


public class BookSignatureTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BookSignature a=new BookSignature("a","b","c","d");
		BookSignature b=new BookSignature("a","b","c","d");
		System.out.println(a.equals(b));
	}

}
