package org.jopac2.jbal.classification;

import java.util.Vector;

import org.jopac2.jbal.abstractStructure.Field;

/**
 * 		$a Number (not repeatable)
 * 		$v Edition (not repeatable)
 * 		$z Language of edition (not repeatable)
 * @author romano
 *
 */

public class UDC implements ClassificationInterface {
	private Vector<Field> data=new Vector<Field>();
	
	public UDC(String number, String edition, String language) {
		if(number!=null && number.length()>0) data.addElement(new Field("a",number));
		if(edition!=null && edition.length()>0) data.addElement(new Field("v",edition));
		if(language!=null && language.length()>0) data.addElement(new Field("z",language));
	}
	
	public String getClassificationName() {
		return "UDC";
	}

	public Vector<Field> getData() {
		return data;
	}

	
}
